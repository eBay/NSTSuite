package com.ebay.tool.thinmodelgen.gui.graphql.schemaselectdialog;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.ebay.graphql.model.GraphQLSchema;
import com.ebay.graphql.types.GraphQLType;
import com.ebay.tool.thinmodelgen.gui.MainWindow;
import com.ebay.tool.thinmodelgen.gui.openapi.schemaselectdialog.GraphQLOperation;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserPayload;

public class GraphQLSchemaSelectDialog extends JDialog implements ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private JList<String> operationList;
	private JList<String> operationNameList;
	private JButton okButton;
	private SchemaParserPayload payload;
	private GraphQLSchema schema;

	public GraphQLSchemaSelectDialog(GraphQLSchema schema, Frame parent) {
		super(parent, "OpenAPI Schema Selection", ModalityType.APPLICATION_MODAL);
		this.schema = schema;
		init();
	}

	/**
	 * Show the dialog and get the payload values selected.
	 *
	 * @return Payload of selected values.
	 */
	public SchemaParserPayload getPayload() {
		this.setVisible(true);
		return payload;
	}

	/**
	 * Initialize the dialog.
	 */
	private void init() {

		Dimension dialogDimension = new Dimension(500, 300);
		setSize(dialogDimension);
		setPreferredSize(dialogDimension);
		setLocationRelativeTo(MainWindow.getInstance());

		JPanel dialogPanel = new JPanel(new GridBagLayout());

		// ------------------------
		// Operation
		// ------------------------

		// Label
		JLabel operationLabel = new JLabel("Operation");

		GridBagConstraints operationLabelConstraints = new GridBagConstraints();
		operationLabelConstraints.insets = new Insets(2, 2, 0, 2);
		operationLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		operationLabelConstraints.gridx = 0;
		operationLabelConstraints.gridy = 0;
		operationLabelConstraints.weightx = 0.5;
		operationLabelConstraints.weighty = 0.1;

		dialogPanel.add(operationLabel, operationLabelConstraints);

		// selection
		GridBagConstraints operationSelectionConstraints = new GridBagConstraints();
		operationSelectionConstraints.insets = new Insets(0, 2, 2, 2);
		operationSelectionConstraints.fill = GridBagConstraints.BOTH;
		operationSelectionConstraints.gridx = 0;
		operationSelectionConstraints.gridy = 1;
		operationSelectionConstraints.weightx = 1.0;
		operationSelectionConstraints.weighty = 1.0;

		DefaultListModel<String> operationOptions = new DefaultListModel<>();
		for (GraphQLOperation operation : GraphQLOperation.values()) {
			operationOptions.addElement(operation.getValue());
		}

		operationList = new JList<>();
		operationList.setModel(operationOptions);
		operationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		operationList.addListSelectionListener(this);

		JScrollPane operationScrollPane = new JScrollPane(operationList);

		dialogPanel.add(operationScrollPane, operationSelectionConstraints);

		// ------------------------
		// Operation Name
		// ------------------------

		// Label
		JLabel operationNameLabel = new JLabel("Operation Name");

		GridBagConstraints operationNameLabelConstraints = new GridBagConstraints();
		operationNameLabelConstraints.insets = new Insets(2, 2, 0, 2);
		operationNameLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		operationNameLabelConstraints.gridx = 1;
		operationNameLabelConstraints.gridy = 0;
		operationNameLabelConstraints.weightx = 0.25;
		operationNameLabelConstraints.weighty = 0.1;

		dialogPanel.add(operationNameLabel, operationNameLabelConstraints);

		// selection
		GridBagConstraints operationNameSelectionConstraints = new GridBagConstraints();
		operationNameSelectionConstraints.insets = new Insets(0, 2, 2, 2);
		operationNameSelectionConstraints.fill = GridBagConstraints.BOTH;
		operationNameSelectionConstraints.gridx = 1;
		operationNameSelectionConstraints.gridy = 1;
		operationNameSelectionConstraints.weightx = 0.25;
		operationNameSelectionConstraints.weighty = 1.0;

		operationNameList = new JList<>();
		operationNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		operationNameList.addListSelectionListener(this);

		JScrollPane operationNameScrollPane = new JScrollPane(operationNameList);

		dialogPanel.add(operationNameScrollPane, operationNameSelectionConstraints);

		// Save Button
		GridBagConstraints okButtonPanelConstraints = new GridBagConstraints();
		okButtonPanelConstraints.insets = new Insets(0, 0, 2, 2);
		//okButtonPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		okButtonPanelConstraints.anchor = GridBagConstraints.EAST;
		okButtonPanelConstraints.gridx = 1;
		okButtonPanelConstraints.gridy = 2;

		okButton = new JButton("Open");
		okButton.setEnabled(false);

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String operation = operationList.getSelectedValue();
				String operationName = operationNameList.getSelectedValue();
				GraphQLOperation graphQLOperation = GraphQLOperation.getOperationByValue(operation);

				payload = new SchemaParserPayload().setGraphQLOperation(graphQLOperation)
						.setGraphQLOperationName(operationName);

				setVisible(false);
			}
		});

		dialogPanel.add(okButton, okButtonPanelConstraints);

		// Wrap it all up
		getContentPane().add(dialogPanel);
		pack();
	}

	// ------------------------------------------------
	// ListSelectionListener
	// ------------------------------------------------

	@Override
	public void valueChanged(ListSelectionEvent e) {

		Object source = e.getSource();
		if (source == operationList) {

			String selectedOperation = operationList.getSelectedValue();
			GraphQLOperation operation = GraphQLOperation.getOperationByValue(selectedOperation);
			Map<String, GraphQLType> operationMap;

			switch (operation) {
			case MUTATION:
				operationMap = schema.getMutations();
				break;
			case QUERY:
				operationMap = schema.getQuerys();
				break;
			case SUBSCRIPTION:
				operationMap = schema.getSubscriptions();
				break;
			default:
				operationMap = new HashMap<>();
			}

			DefaultListModel<String> operationNameModel = new DefaultListModel<>();
			for (Entry<String, GraphQLType> entry : operationMap.entrySet()) {
				operationNameModel.addElement(entry.getKey());
			}

			setOperationNameList(operationNameModel);
			okButton.setEnabled(false);
		} else if (source == operationNameList) {
			okButton.setEnabled(true);
		}
	}

	private void setOperationNameList(DefaultListModel<String> model) {
		operationNameList.clearSelection();
		operationNameList.setModel(model);
	}
}
