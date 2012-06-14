package com.ad.android.ridesystems.passengercounter.model.entities;

import java.io.Serializable;

import org.dom4j.Element;
import org.dom4j.Node;

/**
 * Base class to all DB entities. Define id field.
 *
 */
public abstract class AEntity implements Serializable {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -751900055895079112L;
	
	
	private int id;
	

	public Integer getId() {
		return new Integer(id);
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public static String getElementString(Element el, String name) {
		Node node = el.selectSingleNode(name);
		return node != null ? node.getText() : ""; 
	}
	
	public static String getString(Element el, String name) {
		Node node = el.selectSingleNode(name);
		return node != null ? node.getText() : ""; 
	}

}
