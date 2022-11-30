package com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ErrorComponent extends JPanel {

  public ErrorComponent(String message) {

    JLabel label = new JLabel(message, JLabel.LEFT);

    JPanel pane = new JPanel();
    pane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    pane.add(label);

    GridBagConstraints paneConstraints = new GridBagConstraints();
    paneConstraints.fill = GridBagConstraints.HORIZONTAL;
    paneConstraints.gridx = 0;
    paneConstraints.gridy = 0;
    paneConstraints.weightx = 1.0;
    paneConstraints.weighty = 0.1;
    paneConstraints.insets = new Insets(2, 2, 2, 2);

    this.setLayout(new GridBagLayout());
    this.add(pane, paneConstraints);
  }
}
