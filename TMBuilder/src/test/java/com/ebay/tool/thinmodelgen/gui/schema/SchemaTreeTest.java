package com.ebay.tool.thinmodelgen.gui.schema;

import static com.ebay.constants.TestConstants.unitTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import javax.swing.tree.DefaultMutableTreeNode;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ebay.tool.thinmodelgen.gui.schema.events.SchemaEventListener;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonBaseType;
import com.ebay.tool.thinmodelgen.jsonschema.type.JsonStringType;

public class SchemaTreeTest {

  SchemaTree schemaTree;
  DefaultMutableTreeNode rootNode;
  DefaultMutableTreeNode childNode;
  JsonBaseType[] expectedNotification;

  @BeforeMethod(alwaysRun = true)
  public void setup() throws Throwable {
    schemaTree = new SchemaTree();

    childNode = new DefaultMutableTreeNode(new JsonStringType("B"));

    rootNode = new DefaultMutableTreeNode(new JsonStringType("A"));
    rootNode.add(childNode);

    expectedNotification = new JsonBaseType[2];
    expectedNotification[0] = new JsonStringType("A");
    expectedNotification[1] = new JsonStringType("B");
  }

  @Test(groups = unitTest)
  public void addSchemaEventListenerGetNotification() {

    TestListener listener = new TestListener();

    schemaTree.addSchemaEventListener(listener);
    schemaTree.notifySchemaEventListenersOfNodeChange(childNode);

    assertThat("Notification should be received correctly.", listener.getPath(), is(equalTo(expectedNotification)));
  }

  @Test(groups = unitTest)
  public void removeSchemaEventListenerDoNotGetNotification() {

    TestListener listener = new TestListener();

    schemaTree.addSchemaEventListener(listener);
    schemaTree.removeSchemaEventListener(listener);
    schemaTree.notifySchemaEventListenersOfNodeChange(childNode);

    assertThat("No notification should be received after removing listener.", listener.getPath(), is(nullValue()));
  }

  @Test(groups = unitTest)
  public void notificationSendsNullTreeNode() {

    TestListener listener = new TestListener();

    schemaTree.addSchemaEventListener(listener);
    schemaTree.notifySchemaEventListenersOfNodeChange(null);

    assertThat("No notification should receive null value.", listener.getPath(), is(nullValue()));
  }

  @Test(expectedExceptions = ClassCastException.class, groups = "unitTest")
  public void notificationTriesToSendTreeNodeWithNonJsonBaseTypeUserData() {

    TestListener listener = new TestListener();

    schemaTree.addSchemaEventListener(listener);
    schemaTree.notifySchemaEventListenersOfNodeChange(new DefaultMutableTreeNode("FOO"));
  }

  public class TestListener implements SchemaEventListener {

    private JsonBaseType[] path = null;

    @Override
    public void treeNodeSelected(JsonBaseType[] path) {
      this.path = path;
    }

    public JsonBaseType[] getPath() {
      return path;
    }
  }
}
