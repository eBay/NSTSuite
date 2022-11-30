package com.ebay.nst.resourcefile.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Reporter;

public class ResourceFileReader {

  /**
   * Read fixed column data from the specified csv file. Expectation is that the
   * first row of the csv file is the header row. Each column value in the
   * header row will become the map key in map of row data so be sure to be
   * consistent in your csv header naming conventions.
   *
   * @param csvPath Path to CSV file.
   * @return List of row data contained in maps (column key to corresponding cell value for that row).
   * @throws IOException Pass through IO Exceptions.
   */
  public List<Map<String, String>> readFixedColumnDataFromCsv(String csvPath) throws IOException {

    ArrayList<Map<String, String>> csvData = new ArrayList<>();

    ClassLoader classLoader = getClass().getClassLoader();
    InputStream resourceInputStream = classLoader.getResourceAsStream(csvPath);

    if (resourceInputStream == null) {
      throw new IllegalArgumentException("File path is invalid: " + csvPath);
    }

    Reporter.log(String.format("Attempting to read data from: %s", csvPath), true);
    InputStreamReader inputStreamReader = new InputStreamReader(resourceInputStream);
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

    // Get the ordered header list.
    String line = bufferedReader.readLine();
    List<String> headers = splitCsvLine(line);

    while ((line = bufferedReader.readLine()) != null) {

      String lineCollector = line;
      boolean isCompleteCsvLine = isCompleteCsvLine(lineCollector);
      int splitSize = splitCsvLine(lineCollector + "\n").size();
      boolean done = (isCompleteCsvLine && (splitSize >= headers.size()));

      while (!done) {
        line = bufferedReader.readLine();
        if (line == null) {
          break;
        }
        lineCollector += "\n" + line;
        isCompleteCsvLine = isCompleteCsvLine(lineCollector);
        splitSize = splitCsvLine(lineCollector + "\n").size();
        done = (isCompleteCsvLine && (splitSize >= headers.size()));
      }

      if (!lineCollector.endsWith("\n")) {
        lineCollector += "\n";
      }

      lineCollector = lineCollector.replace("\"\"", "\"");

      List<String> lineCollectorSplit = splitCsvLine(lineCollector);
      if (lineCollectorSplit.size() != headers.size()) {
        bufferedReader.close();
        throw new IllegalStateException(
            String.format("We did not parse the expected number of column data (%d) for the current row: %s. Found (%d) columns of data.", headers.size(), lineCollector, lineCollectorSplit.size()));
      }

      HashMap<String, String> rowData = new HashMap<>();
      for (int i = 0; i < headers.size(); i++) {
        rowData.put(headers.get(i), removeSurroundingQuotes(lineCollectorSplit.get(i)));
      }

      csvData.add(rowData);
    }

    bufferedReader.close();
    return csvData;
  }

  /**
   * Read parametric test data from the specified CSV file. All values will be
   * read in as Strings. If there is a column titles 'ignore' and a corresponding
   * cell set to 'yes' then that row of data will be ignored.
   *
   * @param csvPath
   *          Relative path to csv file from the resource folder it is located
   *          in. Example:
   *          "com/ebay/xo/csvfile.csv"
   *          which resides in src/test/resources.
   * @param ignoreFirstRow
   *          True to skip the first row of data in the CSV (use if the first
   *          row are header names). False to include first row in parametric
   *          data set.
   * @return Parametric data set.
   * @throws URISyntaxException Pass through exception.
   * @throws IOException Pass through exception.
   */
  public String[][] readParametricDataSetFromCsv(String csvPath, boolean ignoreFirstRow) throws URISyntaxException, IOException {

    ClassLoader classLoader = getClass().getClassLoader();
    InputStream resourceInputStream = classLoader.getResourceAsStream(csvPath);

    if (resourceInputStream == null) {
      throw new IllegalArgumentException("File path is invalid: " + csvPath);
    }

    Reporter.log(String.format("Attempting to read parametric data from: %s", csvPath), true);
    InputStreamReader inputStreamReader = new InputStreamReader(resourceInputStream);
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

    ArrayList<String[]> rowData = new ArrayList<>();
    int numberOfColumns = 0;

    // Consume the headers and discard.
    String line;
    int indexOfIgnoreColumn = -1;
    if (ignoreFirstRow) {
      line = bufferedReader.readLine();

      // Look for the index of the ignore column.
      String[] headers = line.split(",");
      for (int i = 0; i < headers.length; i++) {
        if (headers[i].equalsIgnoreCase("ignore")) {
          indexOfIgnoreColumn = i;
          break;
        }
      }
    }

    while ((line = bufferedReader.readLine()) != null) {

      String lineCollector = line;

      if (!lineCollector.endsWith("\n")) {
        lineCollector += "\n";
      }

      lineCollector = lineCollector.replace("\"\"", "\"");

      List<String> lineCollectorSplit = splitCsvLine(lineCollector);

      // Skip if we are ignoring this data.
      if (indexOfIgnoreColumn >= 0 && lineCollectorSplit.get(indexOfIgnoreColumn).equalsIgnoreCase("yes")) {
          continue;
      }

      String[] rowArray = new String[0];
      rowArray = lineCollectorSplit.toArray(rowArray);

      if (rowArray.length > numberOfColumns) {
        numberOfColumns = rowArray.length;
      }

      rowData.add(rowArray);
    }

    bufferedReader.close();
    int numberOfRows = rowData.size();

    String[][] parametricValues = new String[numberOfRows][numberOfColumns];

    for (int i = 0; i < numberOfRows; i++) {

      String[] values = rowData.get(i);

      for (int j = 0; j < values.length; j++) {
        parametricValues[i][j] = values[j];
      }
    }

    return parametricValues;
  }

  /**
   * Check if the line we are evaluating is a complete line, without interrupted quotes/double quotes.
   * @param line Line to evaluate.
   * @return True if the line is a complete (uninterrupted) csv line.
   */
  protected boolean isCompleteCsvLine(String line) {

    boolean insideQuotes = false;
    char[] chars = line.toCharArray();
    for (char character : chars) {

      if (character == '"' || character == '\'') {
        insideQuotes = !insideQuotes;
      }
    }

    return !insideQuotes;
  }

  protected List<String> splitCsvLine(String line) {

    ArrayList<String> splitLine = new ArrayList<>();

    String cell = "";
    boolean insideQuotes = false;

    char[] chars = line.toCharArray();
    for (char character : chars) {

      if (character == '"' || character == '\'') {
        insideQuotes = !insideQuotes;
      }

      if (!insideQuotes && character == ',') {
        splitLine.add(cell.trim());
        cell = "";
      } else {
        cell += character;
      }
    }

    if (!cell.isEmpty()) {
      splitLine.add(cell.trim());
    }

    // Remove any double quote blocks that are wrapping CSV lines that contain intentional commas.
    for (int i = 0; i < splitLine.size(); i++) {
      String value = splitLine.get(i);
      splitLine.set(i, removeSurroundingQuotes(value));
    }

    return splitLine;
  }

  protected String removeSurroundingQuotes(String line) {

    if (line.isEmpty()) {
      return line;
    }

    if (line.startsWith("\"") && line.endsWith("\"")) {
      line = line.substring(1, line.length() - 1);
    }

    return line;
  }
}
