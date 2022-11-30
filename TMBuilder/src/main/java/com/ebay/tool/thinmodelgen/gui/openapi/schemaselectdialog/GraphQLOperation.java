package com.ebay.tool.thinmodelgen.gui.openapi.schemaselectdialog;

public enum GraphQLOperation {
	  QUERY("Query"), MUTATION("Mutation"), SUBSCRIPTION("Subscription");
	  
	  private final String value;
	  
	  GraphQLOperation(String value) {
		  this.value = value;
	  }
	  
	  public String getValue() {
		  return value;
	  }
	  
	  public static GraphQLOperation getOperationByValue(String value) {
		  for (GraphQLOperation element : GraphQLOperation.values()) {
			  if (element.getValue().equalsIgnoreCase(value)) {
				  return element;
			  }
		  }
		  
		  return null;
	  }
}
