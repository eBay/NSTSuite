package com.ebay.tool.thinmodelgen;

import javax.swing.SwingUtilities;

import com.ebay.tool.thinmodelgen.gui.MainWindow;

public class TMBuilder {

  public static void main(String[] args) throws Throwable {

    final MainWindow mainWindow = MainWindow.getInstance();
    TMBuilderRuntimeArguments.getInstance(args);

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        mainWindow.setVisible(true);
      }
    });
  }

}
