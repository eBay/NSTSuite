package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.ebay.tool.thinmodelgen.gui.TMGuiConstants;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

  public MainFrame() {

    Dimension windowDimension = new Dimension(TMGuiConstants.DEFAULT_WINDOW_WIDTH, TMGuiConstants.DEFAULT_WINDOW_HEIGHT);

    this.setTitle(TMGuiConstants.APP_NAME);
    this.setPreferredSize(windowDimension);
    this.setSize(windowDimension);
    this.setBackground(Color.BLACK);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
