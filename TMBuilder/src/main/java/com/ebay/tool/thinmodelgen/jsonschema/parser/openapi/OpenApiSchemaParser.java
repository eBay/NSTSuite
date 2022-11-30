package com.ebay.tool.thinmodelgen.jsonschema.parser.openapi;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.swing.tree.DefaultMutableTreeNode;

import com.ebay.tool.thinmodelgen.gui.MainWindow;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.TMFileSingleton;
import com.ebay.tool.thinmodelgen.gui.openapi.schemaselectdialog.SchemaSelectDialog;
import com.ebay.tool.thinmodelgen.jsonschema.parser.NodeParsingStackLogger;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParser;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserPayload;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserRequestMethod;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

public class OpenApiSchemaParser implements SchemaParser {

  // Parser Reference: https://github.com/swagger-api/swagger-parser

  private NodeParsingStackLogger stackLogger;
  private OpenApiNodeParser nodeParser;

  public OpenApiSchemaParser() {
    stackLogger = new NodeParsingStackLogger();
    nodeParser = new OpenApiNodeParser(stackLogger);
  }

  public OpenApiSchemaParser(OpenApiNodeParser nodeParser) {
    stackLogger = new NodeParsingStackLogger();
    this.nodeParser = nodeParser;
  }

  @Override
  public DefaultMutableTreeNode parseSchema(String schemaPath) {

    schemaPath = Objects.requireNonNull(schemaPath, "Schema path MUST NOT be null.");

    SchemaParserPayload payload = TMFileSingleton.getInstance().getPayload();

    // Inline everything.
    ParseOptions options = new ParseOptions();
    options.setResolve(true);
    options.setResolveFully(true);
    options.setResolveCombinators(true);

    OpenAPIParser parser = new OpenAPIParser();
    SwaggerParseResult parseResult = parser.readLocation(schemaPath, null, options);

    if (parseResult == null || parseResult.getOpenAPI() == null) {
      throw new IllegalArgumentException(String.format("Schema path [%s] could not be loaded.", schemaPath));
    }
    Paths paths = parseResult.getOpenAPI().getPaths();

    // Check the payload parameters
    if (payload == null) {
      // Prompt the user to select.
      SchemaSelectDialog schemaSelectDialog = new SchemaSelectDialog(paths, MainWindow.getInstance());
      payload = schemaSelectDialog.getPayload();
      TMFileSingleton.getInstance().setPayload(payload);
    }

    String path = Objects.requireNonNull(payload.getPath(), "Path must be specified.");
    SchemaParserRequestMethod method = Objects.requireNonNull(payload.getMethod(), "Method must be specified.");
    String responseCode = Objects.requireNonNull(payload.getResponseCode(), "Response code must be specified.");
    String contentType = Objects.requireNonNull(payload.getContentType(), "Content type must be specified.");

    // locate the path, method/operation, response code and then content type
    PathItem matchingPath = paths.get(path);
    if (matchingPath == null) {
      throw new NullPointerException(String.format("Path %s does not exist in the specified yaml document.", path));
    }

    Operation operation = null;
    String operationErrorMessage;

    switch (method) {
    case GET:
      operation = matchingPath.getGet();
      operationErrorMessage = String.format("Path %s is missing GET operation.", path);
      break;
    case POST:
      operation = matchingPath.getPost();
      operationErrorMessage = String.format("Path %s is missing POST operation.", path);
      break;
    case PUT:
      operation = matchingPath.getPut();
      operationErrorMessage = String.format("Path %s is missing PUT operation.", path);
      break;
    case DELETE:
      operation = matchingPath.getDelete();
      operationErrorMessage = String.format("Path %s is missing DELETE operation.", path);
      break;
    default:
      throw new RuntimeException("Unknown operation: " + method);
    }

    if (operation == null) {
      throw new NullPointerException(operationErrorMessage);
    }

    ApiResponse matchingResponseCode = operation.getResponses().get(responseCode);
    if (matchingResponseCode == null) {
      throw new NullPointerException(String.format("Path %s, method/operation %s is missing response code %s.", path, method, responseCode));
    }

    MediaType mediaType = matchingResponseCode.getContent().get(contentType);
    if (mediaType == null) {
      throw new NullPointerException(String.format("Path %s, method/operation %s, response code %s is missing media/content type %s.", path, method, responseCode, contentType));
    }

    Schema<?> rootSchema = mediaType.getSchema();

    try {
      stackLogger.push("$");
      List<DefaultMutableTreeNode> newNodes = nodeParser.routeParser("$", rootSchema);
      if (newNodes.size() != 1) {
        throw new IOException(String.format("Unable to parse a single root node from the json file [%s].", schemaPath));
      }
      return newNodes.get(0);
    } catch (IOException e) {
      System.out.println(String.format("$.%s", e.getMessage()));
      return new DefaultMutableTreeNode();
    }
  }
}
