package com.tivic.manager.rest.request.filter;

import java.io.Serializable;
import java.util.ArrayList;

import sol.dao.ItemComparator;

public class Filter extends ArrayList<FilterItem> implements Serializable {
	private static final long serialVersionUID = -133587238815151094L;
		
	public ArrayList<ItemComparator> toItemComparator() {
		
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		
		for (FilterItem item : this) {
			crt.add(new ItemComparator(item.getColumn(), item.getValue(), item.toComparator(), item.toType()));
		}		
		
		return crt;
	}
	
}
