package com.ebay.nst.schema.validation;

import java.io.File;

import org.testng.annotations.Test;

import com.ebay.nst.schema.validation.GraphQLSchemaValidator.OperationType;
import com.ebay.nst.schema.validation.support.SchemaValidationException;
import com.ebay.utility.ResourceParser;

public class GraphQLSchemaValidatorTest {

	@Test
	public void validVaultEnrollmentQueryResponse() throws Exception {
		String filePath = ResourceParser.getResourceFilePath("/com/ebay/schema/validation/graphql/schema/enrollment.graphqls");
		File file = new File(filePath);
		GraphQLSchemaValidator validator = new GraphQLSchemaValidator(file, OperationType.QUERY, "isUserEnrolled(userEnrolledInput: UserEnrolledInput)");
	    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/graphql/responses/validEnrollmentResponse.json"));
	}

	@Test(expectedExceptions = SchemaValidationException.class)
	public void invalidVaultEnrollmentResponseNullEnrollmentStatus() throws Exception {
		String filePath = ResourceParser.getResourceFilePath("/com/ebay/schema/validation/graphql/schema/enrollment.graphqls");
		File file = new File(filePath);
		GraphQLSchemaValidator validator = new GraphQLSchemaValidator(file, OperationType.QUERY, "isUserEnrolled(userEnrolledInput: UserEnrolledInput)");
	    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/graphql/responses/invalidEnrollmentResponseNullEnrollmentStatus.json"));
	}
	
	@Test(expectedExceptions = SchemaValidationException.class)
	public void invalidVaultEnrollmentResponseWrongEnrollmentTimeType() throws Exception {
		String filePath = ResourceParser.getResourceFilePath("/com/ebay/schema/validation/graphql/schema/enrollment.graphqls");
		File file = new File(filePath);
		GraphQLSchemaValidator validator = new GraphQLSchemaValidator(file, OperationType.QUERY, "isUserEnrolled(userEnrolledInput: UserEnrolledInput)");
	    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/graphql/responses/invalidEnrollmentResponseWrongEnrollmentTimeType.json"));
	}
	
	@Test
	public void validVaultEnrollmentQueryResponseFromRawServiceResponse() throws Exception {
		String filePath = ResourceParser.getResourceFilePath("/com/ebay/schema/validation/graphql/schema/enrollment.graphqls");
		File file = new File(filePath);
		GraphQLSchemaValidator validator = new GraphQLSchemaValidator(file, OperationType.QUERY, "isUserEnrolled(userEnrolledInput: UserEnrolledInput)");
	    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/graphql/responses/validEnrollmentResponseWithDataAndErrors.json"));
	}
	
	@Test(expectedExceptions = SchemaValidationException.class)
	public void invalidVaultEnrollmentResponseNullEnrollmentStatusFromRawServiceResponse() throws Exception {
		String filePath = ResourceParser.getResourceFilePath("/com/ebay/schema/validation/graphql/schema/enrollment.graphqls");
		File file = new File(filePath);
		GraphQLSchemaValidator validator = new GraphQLSchemaValidator(file, OperationType.QUERY, "isUserEnrolled(userEnrolledInput: UserEnrolledInput)");
	    validator.validate(ResourceParser.readInResourceFile("/com/ebay/schema/validation/graphql/responses/invalidEnrollmentResponseNullEnrollmentStatusWithDataAndErrors.json"));
	}
}
