package com.ebay.nst.resourcefile.reader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ResourceFileReaderTest {

  ResourceFileReader resourceFileReader = new ResourceFileReader();

  @Test
  public void testReadFixedColumnDataFromCsvBaseline() throws Throwable {

    List<Map<String, String>> values = resourceFileReader.readFixedColumnDataFromCsv("com/ebay/resourcefile/reader/readFixedColumnDataFromCsvBaseline.csv");

    ArrayList<HashMap<String, String>> expectedValues = new ArrayList<>();

    HashMap<String, String> expectedMap = new HashMap<>();
    expectedMap.put("SERVICE", "checkout");
    expectedMap.put("API", "EnterCheckoutSession");
    expectedMap.put("NAVIGATION", "enterCheckoutSession.navigateToCheckout();");
    expectedMap.put("ENTRY", "enterCheckoutSession.enterCheckout();\nenterCheckoutSession.doNextThing();");
    expectedMap.put("IMPORTS", "import com.test.EnterCheckoutSession;");
    expectedMap.put("MEMBER_FIELDS", "EnterCheckoutSession enterCheckoutSession = new EnterCheckoutSession();");

    HashMap<String, String> expectedMap2 = new HashMap<>();
    expectedMap2.put("SERVICE", "checkout");
    expectedMap2.put("API", "SetPayment");
    expectedMap2.put("NAVIGATION", "");
    expectedMap2.put("ENTRY", "");
    expectedMap2.put("IMPORTS", "");
    expectedMap2.put("MEMBER_FIELDS", "");

    expectedValues.add(expectedMap);
    expectedValues.add(expectedMap2);

    assertThat("Read fixed column data from csv baseline parsing values do not match expected.", values, is(equalTo(expectedValues)));
  }

  @Test
  public void testReadFixedColumnDataFromCsv_InlineCommaParsing() throws Throwable {

    List<Map<String, String>> values = resourceFileReader.readFixedColumnDataFromCsv("com/ebay/resourcefile/reader/inlineCommas.csv");

    ArrayList<HashMap<String, String>> expectedValues = new ArrayList<>();
    HashMap<String, String> expectedMap = new HashMap<>();
    expectedMap.put("SERVICE", "checkout");
    expectedMap.put("API", "EnterCheckoutSession");
    expectedMap.put("NAVIGATION", "enterCheckoutSession.navigateToCheckout(one, two, three);");
    expectedMap.put("ENTRY", "enterCheckoutSession.enterCheckout(one,two,three);\nenterCheckoutSession.doNextThing();");
    expectedMap.put("IMPORTS", "import com.test.EnterCheckoutSession;");
    expectedMap.put("MEMBER_FIELDS", "EnterCheckoutSession enterCheckoutSession = new EnterCheckoutSession(\"one\", \"two\", three);");

    expectedValues.add(expectedMap);

    assertThat("Inline comma parsing values do not match expected.", values, is(equalTo(expectedValues)));
  }

  @Test
  public void testReadFixedColumnDataFromCsv_EmptyCells() throws Throwable {

    List<Map<String, String>> values = resourceFileReader.readFixedColumnDataFromCsv("com/ebay/resourcefile/reader/emptyCells.csv");

    ArrayList<HashMap<String, String>> expectedValues = new ArrayList<>();
    HashMap<String, String> expectedMap = new HashMap<>();
    expectedMap.put("SERVICE", "checkout");
    expectedMap.put("API", "EnterCheckoutSession");
    expectedMap.put("NAVIGATION", "");
    expectedMap.put("ENTRY", "");
    expectedMap.put("IMPORTS", "");
    expectedMap.put("MEMBER_FIELDS", "");

    HashMap<String, String> expectedMap2 = new HashMap<>();
    expectedMap2.put("SERVICE", "checkout");
    expectedMap2.put("API", "EnterCheckoutSession");
    expectedMap2.put("NAVIGATION", "");
    expectedMap2.put("ENTRY", "");
    expectedMap2.put("IMPORTS", "");
    expectedMap2.put("MEMBER_FIELDS", "last");

    expectedValues.add(expectedMap);
    expectedValues.add(expectedMap2);

    assertThat("Empty parsing values do not match expected.", values, is(equalTo(expectedValues)));
  }

  @Test
  public void testReadFixedColumnDataFromCsv_EmptyRow() throws Throwable {

    List<Map<String, String>> values = resourceFileReader.readFixedColumnDataFromCsv("com/ebay/resourcefile/reader/emptyRow.csv");

    ArrayList<HashMap<String, String>> expectedValues = new ArrayList<>();
    HashMap<String, String> expectedMap = new HashMap<>();
    expectedMap.put("SERVICE", "");
    expectedMap.put("API", "");
    expectedMap.put("NAVIGATION", "");
    expectedMap.put("ENTRY", "");
    expectedMap.put("IMPORTS", "");
    expectedMap.put("MEMBER_FIELDS", "");

    expectedValues.add(expectedMap);

    assertThat("Empty row values do not match expected.", values, is(equalTo(expectedValues)));
  }

  @Test
  public void testReadFixedColumnDataFromCsv_WithWrappingLastColumnCell() throws Throwable {

    List<Map<String, String>> values = resourceFileReader.readFixedColumnDataFromCsv("com/ebay/resourcefile/reader/wrapLastCellInRow.csv");

    ArrayList<HashMap<String, String>> expectedValues = new ArrayList<>();
    HashMap<String, String> expectedMap = new HashMap<>();
    expectedMap.put("SERVICE", "checkout");
    expectedMap.put("API", "EnterCheckoutSession");
    expectedMap.put("NAVIGATION", "");
    expectedMap.put("ENTRY", "");
    expectedMap.put("IMPORTS", "");
    expectedMap.put("MEMBER_FIELDS", "@Rule\npublic final ActivityTestRule<FragmentActivity> activity = new ActivityTestRule<>(FragmentActivity.class, true, false);");

    expectedValues.add(expectedMap);

    assertThat("Wrapping last column cell row values do not match expected.", values, is(equalTo(expectedValues)));
  }

  @Test
  public void testReadFixedColumnDataFromCsv_WrappingMultipleCellsInRow() throws Throwable {

    List<Map<String, String>> values = resourceFileReader.readFixedColumnDataFromCsv("com/ebay/resourcefile/reader/wrapMultipleCellsInRow.csv");

    ArrayList<HashMap<String, String>> expectedValues = new ArrayList<>();
    HashMap<String, String> expectedMap = new HashMap<>();
    expectedMap.put("SERVICE", "checkout");
    expectedMap.put("API", "EnterCheckoutSession");
    expectedMap.put("NAVIGATION", "");
    expectedMap.put("ENTRY", "");
    expectedMap.put("IMPORTS", "@Rule\npublic final ActivityTestRule<FragmentActivity> activity = new ActivityTestRule<>(FragmentActivity.class, true, false);");
    expectedMap.put("MEMBER_FIELDS", "@Rule\npublic final ActivityTestRule<FragmentActivity> activity = new ActivityTestRule<>(FragmentActivity.class, true, false);");

    expectedValues.add(expectedMap);

    assertThat("Wrapping last column cell row values do not match expected.", values, is(equalTo(expectedValues)));
  }

  @Test
  public void testReadParametricDataSetFromCsv_Baseline_IgnoreFirstLine() throws Throwable {

    String[][] values = resourceFileReader.readParametricDataSetFromCsv("com/ebay/resourcefile/reader/readParametricDataSetFromCsvBaseline.csv", true);

    String[][] expectedValues = new String[][] {
      {"testuser","word","US","seller","QA","QA","nothin@test.com","nothingTest","no"},
      {"otheruser","pass","US","seller","PROD","PROD","nothing@test.com","nothingTest","no"}
    };

    assertThat("Ignore first line parsing values do not match expected.", values, is(equalTo(expectedValues)));
  }

  @Test
  public void testReadParametricDataSetFromCsv_Baseline_DoNotIgnoreFirstLine() throws Throwable {

    String[][] values = resourceFileReader.readParametricDataSetFromCsv("com/ebay/resourcefile/reader/readParametricDataSetFromCsvBaseline.csv", false);

    String[][] expectedValues = new String[][] {
      {"username","password","site","userType","environment","useCases","paypalAccount","paypalpassword","ignore"},
      {"testuser","word","US","seller","QA","QA","nothin@test.com","nothingTest","no"},
      {"otheruser","pass","US","seller","PROD","PROD","nothing@test.com","nothingTest","no"}
    };

    assertThat("Do not ignore first line parsing values do not match expected.", values, is(equalTo(expectedValues)));
  }

  @Test
  public void testReadParametricDataSetFromCsv_IgnoreColumn() throws Throwable {

    String[][] values = resourceFileReader.readParametricDataSetFromCsv("com/ebay/resourcefile/reader/ignoreColumn.csv", true);

    String[][] expectedValues = new String[][] {
      {"test","123499","US","seller","PROD","PROD","nothing@test.com","123499","no"},
      {"","","","","","","","",""}
    };

    assertThat("Ignore column parsing values do not match expected.", values, is(equalTo(expectedValues)));
  }

  @Test
  public void testReadParametricDataSetFromCsv_EmptyCells() throws Throwable {

    String[][] values = resourceFileReader.readParametricDataSetFromCsv("com/ebay/resourcefile/reader/emptyCells.csv", true);

    String[][] expectedValues = new String[][] {
      {"checkout","EnterCheckoutSession","","","",""},
      {"checkout","EnterCheckoutSession","","","","last"}
    };

    assertThat("Empty parsing values do not match expected.", values, is(equalTo(expectedValues)));
  }

  @Test
  public void testReadParametricDataSetFromCsv_EmptyRow() throws Throwable {

    String[][] values = resourceFileReader.readParametricDataSetFromCsv("com/ebay/resourcefile/reader/emptyRow.csv", true);

    String[][] expectedValues = new String[][] {
      {"","","","","",""}
    };

    assertThat("Empty row parsing values do not match expected.", values, is(equalTo(expectedValues)));
  }

  @DataProvider(name = "generateSplitCsvLineTestData")
  public Object[][] generateSplitCsvLineTestData() {
    return new Object[][] {
      {"one, two, three", Arrays.asList("one", "two", "three")},
      {"one, 2, 3.0", Arrays.asList("one", "2", "3.0")},
      {"one, \"two and two\", \"three,three,three\"", Arrays.asList("one", "two and two", "three,three,three")},
      {"\"one,\", 2 and 2, \"three, three, three\"", Arrays.asList("one,", "2 and 2", "three, three, three")},
      {",,,four", Arrays.asList("","","","four")},
      {" , \n, ::, \"special,,\"", Arrays.asList("", "", "::", "special,,")},
      {"one\"two\"three", Arrays.asList("one\"two\"three")},
      {"\"\"good night moon\"\"", Arrays.asList("\"good night moon\"")},
      {"\"good night\" moon", Arrays.asList("\"good night\" moon")},
      {"good \"night moon\"", Arrays.asList("good \"night moon\"")},
      {"one, \"two and two\", \"three,three,\nthree\"", Arrays.asList("one", "two and two", "three,three,\nthree")},
      {"one, two,,,", Arrays.asList("one", "two", "", "")},
      {"one, two,,, ", Arrays.asList("one", "two", "", "", "")},
      {"one, two,,,\n", Arrays.asList("one", "two", "", "", "")},
    };
  }

  @Test(dataProvider = "generateSplitCsvLineTestData")
  public void testSplitCsvLine(String line, List<String> expected) {

    List<String> actual = resourceFileReader.splitCsvLine(line);

    assertThat("Parsed CSV line should match expected.", actual, is(equalTo(expected)));
  }

  @DataProvider(name = "generateRemoveSurroundingQuotestTestData")
  public Object[][] generateRemoveSurroundingQuotesTestData() {
    return new Object[][] {
      {"\"blah blah blah\"", "blah blah blah"},
      {"blah blah blah\"", "blah blah blah\""},
      {"\"blah blah blah", "\"blah blah blah"},
      {"\"blah blah \"blah", "\"blah blah \"blah"},
      {"blah \"blah blah\"", "blah \"blah blah\""},
      {"b\"lah blah bla\"h", "b\"lah blah bla\"h"}
    };
  }

  @Test(dataProvider = "generateRemoveSurroundingQuotestTestData")
  public void testRemoveSurroundingQuotes(String line, String expected) {

    String actual = resourceFileReader.removeSurroundingQuotes(line);

    assertThat("Quote removal did not match expected.", actual, is(equalTo(expected)));
  }
}
