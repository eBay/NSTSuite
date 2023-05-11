package com.ebay.tool.thinmodelgen.gui.menu.export;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.ValidationSetModel;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.ebay.tool.thinmodelgen.jsonschema.type.persistence.JsonBaseTypePersistence;
import com.ebay.tool.thinmodelgen.utility.CurlyBraceCounter;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class KotlinThinModelExport {

    private static final String VALIDATION_METHOD_SIGNATURE = "fun validate(softAssert: SoftAssert)";
    private static final String CLASS_SIGNATURE = "class";
    private static final String CLASS_SIGNATURE_SECOND_LINE = "NSTServiceModelBase(jsonRoot, softAssert) {";
    private static final String GENERATED_VALIDATION_METHOD_CALL = "generatedValidations(softAssert)";
    private static final String GENERATED_VALIDATION_METHOD_SIGNATURE = "generatedValidations(softAssert: SoftAssert)";
    private static final String GENERATED_VALIDATIONS_START_BLOCK = "// TMB Generated Validation Method";
    private static final String GENERATED_TWO_TAB_SPACE = "        ";
    private static final String GENERATED_TAB_SPACE = "    ";

    HashSet<String> imports = new HashSet<>();

    public KotlinThinModelExport() {
        imports.add("import com.ebay.jsonpath.JsonPathExecutor");
        imports.add("import org.testng.asserts.SoftAssert");
    }

    public void export(File exportFile, List<ValidationSetModel> validationSetModels) throws ClassNotFoundException, IOException {
        // Prepare all the statements to be injected into the file
        String validationStatements = prepareValidationStatements(validationSetModels);

        // Parse the file and pull out the previously generated
        // model code, if it exists, and add the new.
        updateFile(exportFile, validationStatements);
    }

    private void updateFile(File exportFile, String validationStatements) throws IOException {

        FileReader fileReader = new FileReader(exportFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        StringBuilder fileContents = new StringBuilder();
        String line;

        // Class fields
        boolean insideClass = false;
        CurlyBraceCounter curlyBraceCounter = new CurlyBraceCounter();

        // Validation method fields
        boolean insideValidationMethod = false;
        boolean generatedValidationMethodCallExists = false;
        CurlyBraceCounter validationMethodCounter = new CurlyBraceCounter();

        // Validation set method fields
        boolean insideGeneratedValidationMethod = false;
        boolean insideGeneratedValidationStartBlock = false;
        CurlyBraceCounter generatedValidationMethodCounter = new CurlyBraceCounter();

        while ((line = bufferedReader.readLine()) != null) {

            imports.remove(line);

            // Class signature unlike Java does not end with open curly braces and instead
            // Ends with a colon. So we are not still insideClass as false despite reading that line.
            if (line.contains(CLASS_SIGNATURE) && line.endsWith(":")) {
                insideClass = false;

                for (String val : imports) {
                    fileContents.append(String.format("%s\n", val));
                }

                if (imports.size() > 0) {
                    fileContents.append("\n");
                }
            }

            // Here we need to check for that second line with curly braces
            // continuing from class signature in Kotlin to set insideClass as true.
            if (line.contains(CLASS_SIGNATURE_SECOND_LINE)) {
                insideClass = true;
            }

            // A Bit of hack to remove extra new line that gets added after imports, inside the validate method.
            if (fileContents.toString().endsWith("SoftAssert) {\n\n")) {
                String trimmed = fileContents.toString().replace("SoftAssert) {\n\n", "SoftAssert) {\n");
                // Probably a bit expensive process, until if found a better way.
                fileContents = new StringBuilder();
                fileContents.append(trimmed);
            }

            if (line.contains(VALIDATION_METHOD_SIGNATURE)) {
                insideValidationMethod = true;
            }

            if (line.contains(GENERATED_VALIDATION_METHOD_SIGNATURE)) {
                insideGeneratedValidationMethod = true;
            } else if (line.contains(GENERATED_VALIDATIONS_START_BLOCK)) {
                insideGeneratedValidationMethod = true;
                insideGeneratedValidationStartBlock = true;
            }

            if (insideClass && !insideValidationMethod && !insideGeneratedValidationMethod) {

                curlyBraceCounter.readLine(line);

                if (curlyBraceCounter.isBraceCountEmpty()) {
                    if (fileContents.toString().endsWith("}\n")) {
                        fileContents.append("\n");
                    } else if (fileContents.toString().endsWith("}")) {
                        fileContents.append("\n\n");
                    }

                    fileContents.append(validationStatements);
                }
            }

            //region Validation Method
            if (insideValidationMethod) {
                validationMethodCounter.readLine(line);

                if (line.contains(GENERATED_VALIDATION_METHOD_CALL)) {
                    generatedValidationMethodCallExists = true;
                }

                // If we encounter the closing curly brace for the validation method
                // and we haven't found the generated validation method call, add it.
                if (validationMethodCounter.isBraceCountEmpty()) {
                    insideValidationMethod = false;

                    if (!generatedValidationMethodCallExists) {
                        fileContents.append(String.format(GENERATED_TWO_TAB_SPACE + "%s\n", GENERATED_VALIDATION_METHOD_CALL));
                        generatedValidationMethodCallExists = true;
                    }
                }
            }
            //endregion

            //region Generated Validation Set Methods
            if (insideGeneratedValidationMethod) {
                generatedValidationMethodCounter.readLine(line);

                if (generatedValidationMethodCounter.isBraceCountNotEmpty()) {
                    insideGeneratedValidationStartBlock = false;
                }

                if (!insideGeneratedValidationStartBlock && generatedValidationMethodCounter.isBraceCountEmpty()) {
                    insideGeneratedValidationMethod = false;
                }

                continue;
            }
            //endregion
            fileContents.append(line);
            fileContents.append("\n");
        }

        bufferedReader.close();
        fileReader.close();

        FileWriter fileWriter = new FileWriter(exportFile);
        fileWriter.write(fileContents.toString());
        fileWriter.close();
    }

    public String getValidationStatementsForValidationSet(ValidationSetModel validationSetModel) throws IOException, ClassNotFoundException {
        return getValidationStatementsForNodeModels(Arrays.asList(validationSetModel.getData()));
    }

    /**
     * Apply camel casing to the first letter of the validation set name.
     *
     * @param validationSetName Validation set name to process.
     * @return Validation set name with the first letter converted to lower case.
     */
    protected String lowerCaseCamelCaseValidationSetName(String validationSetName) {
        if (validationSetName.length() > 0) {
            String firstCharacter = String.valueOf(validationSetName.charAt(0));
            firstCharacter = firstCharacter.toLowerCase();
            validationSetName = firstCharacter + validationSetName.substring(1);
        }
        return validationSetName;
    }

    private String getValidationStatementsForNodeModels(List<NodeModel> nodeModels) throws IOException, ClassNotFoundException {
        StringBuilder methodBuilder = new StringBuilder();
        for (NodeModel nodeModel : nodeModels) {

            String serializedData = nodeModel.getSerializedUserObject();
            JsonBaseType jsonBaseType = JsonBaseTypePersistence.deserialize(serializedData);
            String[] savedJsonPaths = jsonBaseType.getSavedPathsForNode();

            for (String savedJsonPath : savedJsonPaths) {
                JsonPathExecutor jsonPathExecutor = jsonBaseType.getCheckForPath(savedJsonPath);

                if (jsonPathExecutor == null) {
                    System.out.printf("Preparing export methods and statement - Check for path %s was null.", savedJsonPath);
                    continue;
                }

                String importClass = jsonPathExecutor.getClass().getName();
                if (importClass.startsWith("com.ebay.jsonpath.TM")) {
                    importClass = jsonPathExecutor.getClass().getSuperclass().getName();
                }
                imports.add(String.format("import %s", importClass));

                if (jsonPathExecutor instanceof ThinModelSerializer) {
                    String statements = ((ThinModelSerializer) jsonPathExecutor).getKotlinStatements();
                    savedJsonPath = savedJsonPath.replace("\"", "\\\"");
                    methodBuilder.append(String.format(GENERATED_TWO_TAB_SPACE + "validations[\"%s\"] = %s\n", savedJsonPath, statements));
                }
            }
        }
        return methodBuilder.toString();
    }

    private String prepareValidationStatements(List<ValidationSetModel> validationSetModels) throws IOException, ClassNotFoundException {
        StringBuilder allValidations = new StringBuilder();

        for (ValidationSetModel validationSetModel : validationSetModels) {
            String validationSetName = validationSetModel.getValidationSetName();
            validationSetName = lowerCaseCamelCaseValidationSetName(validationSetName);
            String methodSignature = validationSetName + "(softAssert: SoftAssert)";
            allValidations.append(GENERATED_TAB_SPACE + GENERATED_VALIDATIONS_START_BLOCK);
            allValidations.append("\n");
            allValidations.append(prepareMethodAndStatementsWithMethod(Arrays.asList(validationSetModel.getData()), methodSignature));
            allValidations.append("\n");
        }
        return allValidations.toString();
    }

    private String prepareMethodAndStatementsWithMethod(List<NodeModel> nodeModels, String methodName) throws IOException, ClassNotFoundException {
        boolean coreValidation = methodName.contains(ExportConstants.CORE_VALIDATION_SET);
        String convertedMethod = coreValidation ? GENERATED_VALIDATION_METHOD_SIGNATURE : methodName;
        String accessModifier = coreValidation ? "private" : "";

        StringBuilder methodBuilder = new StringBuilder(String.format(GENERATED_TAB_SPACE + "%s fun %s {\n", accessModifier, convertedMethod));
        methodBuilder.append(GENERATED_TWO_TAB_SPACE + "val validations: MutableMap<String, JsonPathExecutor> = HashMap()\n");
        methodBuilder.append(getValidationStatementsForNodeModels(nodeModels));
        methodBuilder.append(GENERATED_TWO_TAB_SPACE + "evaluateJsonPaths(validations, softAssert)\n");

        if (!coreValidation) {
            methodBuilder.append("\t\tsoftAssert.assertAll();").append("\n");
        }

        methodBuilder.append(GENERATED_TAB_SPACE + "}");

        return methodBuilder.toString();
    }
}
