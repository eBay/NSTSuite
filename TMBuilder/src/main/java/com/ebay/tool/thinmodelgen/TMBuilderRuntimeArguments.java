package com.ebay.tool.thinmodelgen;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TMBuilderRuntimeArguments {

  private static final String title = "TMBuilder command line options";

  private static TMBuilderRuntimeArguments instance;

  private boolean verboseLoggingEnabled = false;

  private TMBuilderRuntimeArguments(String[] args) {

    if (args == null) {
      return;
    }

    Options options = new Options();
    options.addOption("v", "verbose", false, "Turn on verbose logging.");
    options.addOption("h", "help", false, "Print this help message.");

    CommandLineParser commandLineParser = new DefaultParser();
    CommandLine commandLine = null;
    try {
      commandLine = commandLineParser.parse(options, args);
    } catch (ParseException e) {
      e.printStackTrace();
      System.exit(1);
    }

    verboseLoggingEnabled = commandLine.hasOption("v");
    boolean help = commandLine.hasOption("h");

    if (help) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(title, options);
      System.exit(0);
    }

    if (verboseLoggingEnabled) {
      System.out.println("Verbose logging is enabled.");
    }
  }

  /**
   * Get the singleton instance - assumes that the singleton has already been
   * initialized with the runtime arguments. Use this as a general consumer.
   *
   * @return Singleton instance.
   */
  public static TMBuilderRuntimeArguments getInstance() {

    if (instance == null) {
      throw new IllegalArgumentException("For first initialization, the runtime argument list is required.");
    }

    return instance;
  }

  /**
   * Initialize the singleton instance with the provided runtime arguments.
   * Initialization will only occur once with the runtime arguments. Each
   * consecutive call with a list of arguments will be ignored.
   *
   * @param args
   *          Runtime arguments. If you are passing in null, you likely want the
   *          overloaded variant that takes no parameters.
   * @return Singleton instance.
   */
  public static TMBuilderRuntimeArguments getInstance(String[] args) {

    if (instance == null) {
      synchronized (TMBuilderRuntimeArguments.class) {
        if (instance == null) {
          instance = new TMBuilderRuntimeArguments(args);
        }
      }
    }

    return instance;
  }

  /**
   * Check if verbose logging is enabled.
   *
   * @return True for verbose logging, false otherwise.
   */
  public boolean isVerboseLoggingEnabled() {
    return verboseLoggingEnabled;
  }
}
