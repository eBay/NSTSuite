package com.ebay.tool.thinmodelgen.jsonschema.type;

import java.awt.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.ebay.jsonpath.JsonPathExecutor;

public abstract class JsonBaseType implements Serializable {

	/**
	 * Serialized ID.
	 */
	private static final long serialVersionUID = -4953613775899097912L;

	/**
	 * High level type classifications for sub-type identification.
	 */
	public static enum JsonType {
		OBJECT, ARRAY, PRIMITIVE, POLYMORPHIC, POLYMORPHIC_DICTIONARY, DICTIONARY
	}

	private boolean skipInJsonPath = false;
	private String jsonPathNodeName;
	private String presentationName;
	private String nodeType;
	private HashMap<String, JsonPathExecutor> pathToJPCheckMap = new HashMap<>();

	public JsonBaseType(String jsonPathNodeName, String presentationName, String nodeType) {
		this.jsonPathNodeName = jsonPathNodeName;
		this.presentationName = presentationName;
		this.nodeType = nodeType;
	}

	/**
	 * Retrieve the high level type classification of this instance.
	 *
	 * @return Json sub-type classification.
	 */
	public abstract JsonType getJsonType();

	/**
	 * Retrieve the check editor component that represents the capabilities of the
	 * type.
	 *
	 * @param jsonPath The JSON path to perform the check on.
	 * @return Corresponding view component to show.
	 */
	public abstract Component getCheckEditorComponent(String jsonPath);

	/**
	 * Mark this type to be skipped in t he JSON path editor.
	 */
	public void setSkipInJsonPath() {
		skipInJsonPath = true;
	}

	/**
	 * Get the state of the skip in JSON path flag.
	 * 
	 * @return True to skip in JSON path, false otherwise.
	 */
	public boolean getSkipInJsonPath() {
		return skipInJsonPath;
	}

	/**
	 * Change the presentation name. This is the name for the node as shown in the
	 * UI tree view.
	 * 
	 * @param presentationName Name to show for this node.
	 */
	public void setPresentationName(String presentationName) {
		this.presentationName = presentationName;
	}

	/**
	 * Get the presentation node name for this node.
	 *
	 * @return JSON path node name.
	 */
	public String getJsonPathNodeName() {
		return jsonPathNodeName;
	}

	/**
	 * Get the node type. Useful to differentiate node types by name during runtime.
	 * 
	 * @return Name of the node type.
	 */
	public String getNodeType() {
		return nodeType;
	}

	@Override
	public String toString() {
		return String.format("%s (type: %s)", presentationName, nodeType);
	}

	/**
	 * Get the saved paths for this node.
	 *
	 * @return Saved paths for this node.
	 */
	public String[] getSavedPathsForNode() {
		return pathToJPCheckMap.keySet().toArray(new String[0]);
	}

	/**
	 * Get the check for the specified path.
	 *
	 * @param path Path to get check for.
	 * @return Check found, or null if path is unknown.
	 */
	public JsonPathExecutor getCheckForPath(String path) {
		return pathToJPCheckMap.get(path);
	}

	/**
	 * Remove the specified JSON path record.
	 *
	 * @param path JSON path to remove.
	 */
	public void removePath(String path) {
		pathToJPCheckMap.remove(path);
	}

	/**
	 * Update the original JSON path to the new JSON path. If the original JSON path
	 * does not exist then this is treated as an add operation.
	 *
	 * @param originalPath Original JSON path to replace.
	 * @param newPath      New JSON path to use.
	 */
	public void updatePath(String originalPath, String newPath) {

		if (!pathToJPCheckMap.containsKey(originalPath)) {
			pathToJPCheckMap.put(newPath, null);
		} else {
			JsonPathExecutor associatedValue = pathToJPCheckMap.get(originalPath);
			pathToJPCheckMap.remove(originalPath);
			pathToJPCheckMap.put(newPath, associatedValue);
		}
	}

	/**
	 * Update the check for the specified path. If the path does not exist it will
	 * be added.
	 *
	 * @param path  JSON path.
	 * @param check Check to associate with JSON path.
	 */
	public void updateCheckForPath(String path, JsonPathExecutor check) {
		pathToJPCheckMap.put(path, check);
	}

	/**
	 * Check if the instance has assigned validations to be exported.
	 *
	 * @return True if validations are assigned, false otherwise.
	 */
	public boolean hasAssignedValidations() {
		if (pathToJPCheckMap.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Check if the specified JSON path will result in a list of results being
	 * returned or just a single result.
	 *
	 * @param jsonPath JSON path to evaluate.
	 * @return True if a list of results will be returned, false otherwise.
	 */
	protected boolean willJsonPathReturnListOfResults(String jsonPath) {

		ArrayList<String> arrayBlocks = new ArrayList<>();
		String arrayBlock = "";
		int bracketCounter = 0;

		for (int i = 0; i < jsonPath.length(); i++) {

			char character = jsonPath.charAt(i);

			if (character == '[') {
				bracketCounter++;
			} else if (character == ']') {
				bracketCounter--;
				if (bracketCounter == 0) {
					arrayBlock += character;
					arrayBlocks.add(arrayBlock);
					arrayBlock = "";
				}
			}

			if (bracketCounter > 0) {
				arrayBlock += character;
			}
		}

		if (bracketCounter != 0) {
			throw new IllegalStateException(
					String.format("Unbalanced number of []'s identified in JSON path: %s", jsonPath));
		}

		for (String block : arrayBlocks) {
			if (!block.matches("\\[[0-9]+\\]")) {
				return true;
			}
		}

		// If the path contains an *, then it too should return a list.
		if (jsonPath.contains(".*.")) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jsonPathNodeName == null) ? 0 : jsonPathNodeName.hashCode());
		result = prime * result + ((nodeType == null) ? 0 : nodeType.hashCode());
		result = prime * result + ((pathToJPCheckMap == null) ? 0 : pathToJPCheckMap.hashCode());
		result = prime * result + ((presentationName == null) ? 0 : presentationName.hashCode());
		result = prime * result + ((skipInJsonPath) ? 0 : 1);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof JsonBaseType)) {
			return false;
		}
		JsonBaseType other = (JsonBaseType) obj;
		if (jsonPathNodeName == null) {
			if (other.jsonPathNodeName != null) {
				return false;
			}
		} else if (!jsonPathNodeName.equals(other.jsonPathNodeName)) {
			return false;
		}
		if (nodeType == null) {
			if (other.nodeType != null) {
				return false;
			}
		} else if (!nodeType.equals(other.nodeType)) {
			return false;
		}
		if (pathToJPCheckMap == null) {
			if (other.pathToJPCheckMap != null) {
				return false;
			}
		} else if (!pathToJPCheckMap.equals(other.pathToJPCheckMap)) {
			return false;
		}
		if (presentationName == null) {
			if (other.presentationName != null) {
				return false;
			}
		} else if (!presentationName.equals(other.presentationName)) {
			return false;
		}
		if (skipInJsonPath != other.getSkipInJsonPath()) {
			return false;
		}
		return true;
	}

}
