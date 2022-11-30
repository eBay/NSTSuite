package com.ebay.tool.thinmodelgen.gui.schema;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.ebay.jsonpath.JsonPathExecutor;
import com.ebay.tool.thinmodelgen.gui.MainWindow;
import com.ebay.tool.thinmodelgen.gui.checkeditor.builder.components.ErrorComponent;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.FileOperationHandler;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.NodeModel;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.PathNode;
import com.ebay.tool.thinmodelgen.gui.menu.filemodel.TMFileSingleton;
import com.ebay.tool.thinmodelgen.gui.schema.events.SchemaEventListener;
import com.ebay.tool.thinmodelgen.jsonschema.parser.SchemaParserPayload;
import com.ebay.tool.thinmodelgen.jsonschema.parser.graphql.GraphQLSchemaParser;
import com.ebay.tool.thinmodelgen.jsonschema.parser.jsonschema.JsonSchemaParser;
import com.ebay.tool.thinmodelgen.jsonschema.parser.openapi.OpenApiSchemaParser;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;
import com.ebay.tool.thinmodelgen.jsonschema.type.persistence.JsonBaseTypePersistence;
import com.ebay.tool.thinmodelgen.utility.JPCheckConversion;

public class SchemaTree implements TreeSelectionListener, JTreeRefresh, FileOperationHandler {

  private JScrollPane treeScrollPane = null;
  private JTree tree = null;

  private ArrayList<SchemaEventListener> schemaEventListeners = new ArrayList<>();

  public SchemaTree() throws IOException {
    loadTmSchema(null);
  }

  public JScrollPane getTreeScrollPane() {
    return treeScrollPane;
  }

  // ---------------------------------------
  // JTreeRefresh
  // ---------------------------------------

  @Override
  public void refreshTree() {
    tree.invalidate();
    tree.validate();
    tree.repaint();
  }

  // ---------------------------------------
  // TreeSelectionListener
  // ---------------------------------------

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    notifySchemaEventListenersOfNodeChange(node);
  }

  // ----------------------------------------
  // SchemaEventNotifications
  // ----------------------------------------

  public void addSchemaEventListener(SchemaEventListener listener) {
    schemaEventListeners.add(listener);
  }

  public void removeSchemaEventListener(SchemaEventListener listener) {
    schemaEventListeners.remove(listener);
  }

  protected void notifySchemaEventListenersOfNodeChange(DefaultMutableTreeNode node) {

    if (node == null) {
      for (SchemaEventListener listener : schemaEventListeners) {
        listener.treeNodeSelected(null);
      }
      return;
    }

    Object[] objectPath = node.getUserObjectPath();
    JsonBaseType[] path = new JsonBaseType[objectPath.length];

    for (int i = 0; i < objectPath.length; i++) {

      Object objectNode = objectPath[i];

      if (!(objectNode instanceof JsonBaseType)) {
        if (objectNode != null) {
          throw new ClassCastException("Unable to cast object to JsonBaseType for instance : " + objectNode.toString());
        } else {
          continue;
        }
      }

      path[i] = ((JsonBaseType) objectNode);
    }

    for (SchemaEventListener listener : schemaEventListeners) {
      listener.treeNodeSelected(path);
    }
  }

  // -----------------------------------------
  // FileOperationHandler
  // -----------------------------------------

  @Override
  public void loadSchema(String schemaPath, NodeModel[] savedData) throws IOException, ClassNotFoundException {

    loadTmSchema(schemaPath);

    if (savedData != null) {
      loadData(savedData);
    }

    notifySchemaEventListenersOfNodeChange(null);
  }

  @Override
  public List<NodeModel> getJsonPathCheckData() throws IOException {
    return saveData((DefaultMutableTreeNode) tree.getModel().getRoot());
  }

  // ------------------------------------------
  // Private Methods
  // ------------------------------------------

  private List<NodeModel> saveData(DefaultMutableTreeNode node) throws IOException {

    ArrayList<NodeModel> saveData = new ArrayList<>();

    int numberOfChildren = node.getChildCount();
    for (int i = 0; i < numberOfChildren; i++) {
      List<NodeModel> childData = saveData((DefaultMutableTreeNode) node.getChildAt(i));
      saveData.addAll(childData);
    }

    // If we have saved validations, we need to save those off.
    JsonBaseType userObject = (JsonBaseType) node.getUserObject();

    if (userObject != null && userObject.hasAssignedValidations()) {

      ArrayList<PathNode> pathNodes = new ArrayList<>();

      // Get the node path by starting from root.
      // Iterate over the path from root to the node using the previous
      // index to identify the position of the child in the child array.
      TreeNode[] nodePath = node.getPath();
      for (int i = 0; i < nodePath.length; i++) {

        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) nodePath[i];

        String treeNodeName = "";
        if (treeNode.getUserObject() != null) {
          treeNodeName = ((JsonBaseType) treeNode.getUserObject()).getJsonPathNodeName();
        }

        String className = "";
        if (treeNode.getUserObject() != null) {
          className = treeNode.getUserObject().getClass().toString();
        }

        int childIndex = 0;
        if (i > 0) {
          childIndex = nodePath[i - 1].getIndex(nodePath[i]);
        }

        pathNodes.add(new PathNode(treeNodeName, className, childIndex));
      }

      PathNode[] treePathToNode = pathNodes.toArray(new PathNode[0]);

      String serializedJsonPathExecutorInstanceData = JsonBaseTypePersistence.serialize(userObject);

      NodeModel nodeModel = new NodeModel(treePathToNode, serializedJsonPathExecutorInstanceData);
      saveData.add(nodeModel);
    }

    return saveData;
  }

  private void loadData(NodeModel[] savedData) throws IOException, ClassNotFoundException {

    DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();

    // Each NodeModel instance is a saved user object we need to apply
    // to the tree.
    for (NodeModel data : savedData) {

      PathNode[] pathNodes = data.getTreePathToNode();
      DefaultMutableTreeNode treeNode = rootNode;

      StringBuilder pathBuilder = null;
      boolean skipCurrentNode = false;

      // Walk the node path, updating treeNode to point to each consecutive node
      // in the tree.
      // Once the loop exits we will be pointing to the node that should be
      // updated.
      for (int i = 0; i < pathNodes.length; i++) {

        PathNode node = pathNodes[i];

        if (pathBuilder == null) {
          pathBuilder = new StringBuilder();
        } else {
          pathBuilder.append(".");
        }
        pathBuilder.append(node.getTreeUserObjectIdentifier());

        // Don't process the root node
        if (node.getTreeUserObjectIdentifier().equals("$")) {
          continue;
        } else if (node.getTreeUserObjectIdentifier().equals("")) {
          int childIndex = data.getTreePathToNode()[1].getTreeNodeIndex();
          treeNode = (DefaultMutableTreeNode) treeNode.getChildAt(childIndex);
          continue;
        }

        try {
          treeNode = findChildNodeMatching(pathBuilder.toString(), node, treeNode);
        } catch (IllegalArgumentException e) {

          String[] options = new String[] { "Yes", "No" };
          String message = String.format("%s\n\nDo you want to continue loading file without this node?", e.getMessage());
          int response = JOptionPane.showOptionDialog(MainWindow.getInstance(), message, "Problem Loading File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

          if (response == JOptionPane.YES_OPTION) {
            skipCurrentNode = true;
          } else {
            loadTmSchema(null);
            throw new UnsupportedOperationException(e.getMessage());
          }
        }

        if (skipCurrentNode) {
          break;
        }
      }

      if (skipCurrentNode) {
        continue;
      }

      String serializedUserObject = data.getSerializedUserObject();

      JsonBaseType jsonBaseType = JsonBaseTypePersistence.deserialize(serializedUserObject);

      // It is possible that there may be backwards compatibility issues with
      // older tmbfiles.
      // Perform a conversion on the stored data types, if they are of the
      // legacy type definitions.
      String[] jsonPaths = jsonBaseType.getSavedPathsForNode();
      for (String path : jsonPaths) {
        JsonPathExecutor jpExecutor = jsonBaseType.getCheckForPath(path);
        jpExecutor = JPCheckConversion.convertCheck(jpExecutor);
        jsonBaseType.updateCheckForPath(path, jpExecutor);
      }

      treeNode.setUserObject(jsonBaseType);
    }
  }

  private DefaultMutableTreeNode findChildNodeMatching(String pathWithExpectedChildNode, PathNode expectedChildNode, DefaultMutableTreeNode fromParentNode) throws IllegalArgumentException {

    DefaultMutableTreeNode childNode = null;
    JsonBaseType treeNodeUserObject = null;
    String childNodeName = null;
    String childUserObjectClassType = null;

    int childIndex = expectedChildNode.getTreeNodeIndex();
    try {
      childNode = (DefaultMutableTreeNode) fromParentNode.getChildAt(childIndex);
      treeNodeUserObject = (JsonBaseType) childNode.getUserObject();
      childNodeName = treeNodeUserObject.getJsonPathNodeName();
      childUserObjectClassType = treeNodeUserObject.getClass().toString();
    } catch (ArrayIndexOutOfBoundsException e) {
      ;
    }

    // If the child node's name does not match the expected child path's
    // name then we need didn't find it by index. Loop over all children
    // and try to find by name match.
    if (!expectedChildNode.getTreeUserObjectIdentifier().equals(childNodeName)) {

      int numberOfChildNodes = fromParentNode.getChildCount();

      for (int i = 0; i < numberOfChildNodes; i++) {

        childNode = (DefaultMutableTreeNode) fromParentNode.getChildAt(i);

        treeNodeUserObject = (JsonBaseType) childNode.getUserObject();
        childNodeName = treeNodeUserObject.getJsonPathNodeName();
        childUserObjectClassType = treeNodeUserObject.getClass().toString();

        if (expectedChildNode.getTreeUserObjectIdentifier().equals(childNodeName)) {
          break;
        }
      }
    }

    if (!childNodeName.equals(expectedChildNode.getTreeUserObjectIdentifier())) {
      throw new IllegalArgumentException(String.format("Expected path\n[%s]\nencountered unknown node name [%s].", pathWithExpectedChildNode, expectedChildNode.getTreeUserObjectIdentifier()));
    } else if (!childUserObjectClassType.equals(expectedChildNode.getTreeUserObjectClassName())) {
      throw new IllegalArgumentException(
          String
              .format(
                  "Expected path\n[%s]\nencountered user object class name mismatch for node [%s].\n\nExpected class type\n[%s]\nfound class type\n[%s].",
                  pathWithExpectedChildNode,
                  expectedChildNode.getTreeUserObjectIdentifier(),
                  expectedChildNode.getTreeUserObjectClassName(),
                  childUserObjectClassType));
    }

    return childNode;
  }

  @SuppressWarnings("serial")
  private void loadTmSchema(String schemaFilePath) throws IOException {

    DefaultMutableTreeNode rootNode = null;

    if (schemaFilePath != null) {

      if (schemaFilePath.endsWith(".yaml")) {
        OpenApiSchemaParser openApiSchemaParser = new OpenApiSchemaParser();
        rootNode = openApiSchemaParser.parseSchema(schemaFilePath);
        SchemaParserPayload payload = TMFileSingleton.getInstance().getPayload();
        TMFileSingleton.getInstance().setPayload(payload);
      } else if (schemaFilePath.endsWith(".json") || schemaFilePath.endsWith(".zip")) {
        rootNode = new JsonSchemaParser().parseSchema(schemaFilePath);
      } else if (schemaFilePath.endsWith(".graphql") || schemaFilePath.endsWith(".graphqls")) {
    	  try {
    		  rootNode = new GraphQLSchemaParser().parseSchema(schemaFilePath);
    	  } catch (IllegalStateException e) {
    		  JOptionPane.showMessageDialog(MainWindow.getInstance(), e.getMessage() + "\nSchema loading failed.\nPlease address issue and try again.");
    		  return;
    	  }
      } else {
        throw new IOException(String.format("Unsupported parser requirement. %s has no matched parser.", schemaFilePath));
      }
    } else {
      rootNode = new DefaultMutableTreeNode(new JsonStringType("Please load a JSON schema") {

        @Override
        public JsonType getJsonType() {
          return JsonType.PRIMITIVE;
        }

        @Override
        public Component getCheckEditorComponent(String jsonPath) {
          return new ErrorComponent("Please select a JSON schema.");
        }
      });
    }

    tree = new JTree(rootNode);
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.addTreeSelectionListener(this);
    tree.setCellRenderer(new SchemaTreeCellRenderer());

    if (treeScrollPane == null) {
      treeScrollPane = new JScrollPane(tree);
    } else {
      treeScrollPane.setViewportView(tree);
    }

    refreshTree();
  }
}
