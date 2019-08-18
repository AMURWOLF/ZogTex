package com.zog.tex.contracts.bib.model.entities;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * A descriptor of reference to publication.
 */
public class BibEntry {

	String type;                     // article, incollection, book...
	String citeKey;                  // similar to id, some word like "ShSm:754"
	Map<String, String> propertyMap; // any subset of about 20 different properties and corresponding values

	public BibEntry() {
		propertyMap = new TreeMap<>();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type.toLowerCase();
	}

	public String getCiteKey() {
		return citeKey;
	}

	public void setCiteKey(String citeKey) {
		this.citeKey = citeKey.toLowerCase();
	}

	public void addProperty(String propType, String propValue) {
		propertyMap.put(propType, propValue);
	}

	public String getProperty(String propType) {
		return propertyMap.get(propType);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(MessageFormat.format("@{0}'{'{1},\n", type, citeKey));
		for (Entry<String, String> bibProp : propertyMap.entrySet()) {
			String propType = bibProp.getKey();
			String propValue = bibProp.getValue();
			builder.append(MessageFormat.format("{0} = '{'{1}'}',\n", propType, propValue));
		}
		builder.append('}');
		
		return builder.toString();
	}
}
