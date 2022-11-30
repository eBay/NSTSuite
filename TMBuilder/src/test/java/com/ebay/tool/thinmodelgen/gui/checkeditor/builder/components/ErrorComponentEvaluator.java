package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import javax.swing.SwingUtilities;

public class ErrorComponentEvaluator {

  public static void main(String[] args) throws Throwable {

    MainFrame mainFrame = new MainFrame();
    mainFrame.add(new ErrorComponent("TEST MESSAGE"));

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        mainFrame.setVisible(true);
      }
    });
  }
}
