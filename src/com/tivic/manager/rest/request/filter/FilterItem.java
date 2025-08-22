package com.tivic.manager.rest.request.filter;

import java.io.Serializable;
import java.sql.Types;

import sol.dao.ItemComparator;

public class FilterItem implements Serializable {
	private static final long serialVersionUID = 5729900312683542695L;
	
	public static final String EQUAL 				= "eq";
	public static final String IN 					= "in";
	public static final String NOT_IN 				= "nin";
	public static final String NOT_EQUAL 			= "neq";
	public static final String GREATER_THAN 		= "gt";
	public static final String GREATER_THAN_EQUAL 	= "gte";
	public static final String LESS_THAN 			= "lt";
	public static final String LESS_THAN_EQUAL 		= "lte";
	
	public static final String INTEGER 		= "integer";
	public static final String BOOLEAN 		= "boolean";
	public static final String STRING 		= "string";
	public static final String DATE_TIME 	= "datetime";
	
	private String column;
	private String value;
	private String operation;
	private String columnType;
	
	public FilterItem(String column, String value, String operation, String columnType) {
		super();
		this.column = column;
		this.value = value;
		this.operation = operation;
		this.columnType = columnType;
	}

	public FilterItem() {
		super();
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	
	public int toComparator() {
		switch (this.getOperation()) {
			case EQUAL: return ItemComparator.EQUAL;
			case IN: return ItemComparator.IN;
			case NOT_IN: return ItemComparator.NOTIN;
			case NOT_EQUAL: return ItemComparator.DIFFERENT;
			case GREATER_THAN: return ItemComparator.GREATER;
			case GREATER_THAN_EQUAL: return ItemComparator.GREATER_EQUAL;
			case LESS_THAN: return ItemComparator.MINOR;
			case LESS_THAN_EQUAL: return ItemComparator.MINOR_EQUAL;
			default: return ItemComparator.EQUAL;
		}
	}
	
	public int toType() {
		switch (this.getColumnType()) {
			case INTEGER: return Types.INTEGER;
			case BOOLEAN: return Types.BOOLEAN;
			case STRING: return Types.VARCHAR;
			case DATE_TIME: return Types.TIMESTAMP;
		}
		return 0;
	}
	
	
}
