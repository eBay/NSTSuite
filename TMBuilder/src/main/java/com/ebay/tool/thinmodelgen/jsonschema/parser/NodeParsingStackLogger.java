package com.ebay.tool.thinmodelgen.jsonschema.parser;

import java.util.Stack;

import com.ebay.tool.thinmodelgen.logging.TMBuilderLogger;
import com.ebay.tool.thinmodelgen.logging.TMBuilderLogger.LOG_TYPE;

@SuppressWarnings("serial")
public class NodeParsingStackLogger extends Stack<String> {

  @Override
  public String push(String item) {

    StringBuilder builder = new StringBuilder();

    for (String element : this) {
      builder.append(String.format("[%s]", element));
    }

    builder.append(String.format("[%s]", item));

    TMBuilderLogger.log(LOG_TYPE.DEGUG, String.format("Parsing: %s", builder.toString()));

    return super.push(item);
  }

}
