package com.ebay.tool.thinmodelgen.utility;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ebay.jsonpath.JPBooleanCheck;
import com.ebay.jsonpath.JPDoubleCheck;
import com.ebay.jsonpath.JPIntegerCheck;
import com.ebay.jsonpath.JPJsonArrayCheck;
import com.ebay.jsonpath.JPJsonObjectCheck;
import com.ebay.jsonpath.JPListOfBooleanCheck;
import com.ebay.jsonpath.JPListOfDoubleCheck;
import com.ebay.jsonpath.JPListOfIntegerCheck;
import com.ebay.jsonpath.JPListOfStringCheck;
import com.ebay.jsonpath.JPStringCheck;
import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.jsonpath.TMJPBooleanCheck;
import com.ebay.jsonpath.TMJPDoubleCheck;
import com.ebay.jsonpath.TMJPIntegerCheck;
import com.ebay.jsonpath.TMJPJsonArrayCheck;
import com.ebay.jsonpath.TMJPJsonObjectCheck;
import com.ebay.jsonpath.TMJPListOfBooleanCheck;
import com.ebay.jsonpath.TMJPListOfDoubleCheck;
import com.ebay.jsonpath.TMJPListOfIntegerCheck;
import com.ebay.jsonpath.TMJPListOfStringCheck;
import com.ebay.jsonpath.TMJPStringCheck;

public class JPCheckConversionTest {

  @DataProvider(name = "conversionCheckData")
  public Object[][] conversionCheckData() {
    return new Object[][] {
      { new JPBooleanCheck(), TMJPBooleanCheck.class },
      { new JPDoubleCheck(), TMJPDoubleCheck.class },
      { new JPIntegerCheck(), TMJPIntegerCheck.class },
      { new JPJsonArrayCheck(), TMJPJsonArrayCheck.class },
      { new JPJsonObjectCheck(), TMJPJsonObjectCheck.class },
      { new JPListOfBooleanCheck(), TMJPListOfBooleanCheck.class },
      { new JPListOfDoubleCheck(), TMJPListOfDoubleCheck.class },
      { new JPListOfIntegerCheck(), TMJPListOfIntegerCheck.class },
      { new JPListOfStringCheck(), TMJPListOfStringCheck.class },
      { new JPStringCheck(), TMJPStringCheck.class }

    };
  }

  @Test(dataProvider = "conversionCheckData")
  public void conversionCheck(JsonPathExecutor sourceType, Class<?> expectedType) {

    JsonPathExecutor returnedType = JPCheckConversion.convertCheck(sourceType);
    MatcherAssert.assertThat("Returned type must match expected class type.", returnedType, is(instanceOf(expectedType)));
  }
}
