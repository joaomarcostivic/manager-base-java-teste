package com.tivic.manager.util;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.tivic.manager.exception.EmptyListException;

import sol.dao.ResultSetMap;

public class ResultSetMapper<T> extends ResultSetMap implements Serializable {
	private static final long serialVersionUID = 1420072088119354048L;
	
	private Class<T> type;

    public ResultSetMapper(Class<T> type) {
    	super();
        this.type = type;
    }
    
    public ResultSetMapper(ResultSet rs, Class<T> type) throws SQLException {
    	super(rs);
        this.type = type;
    }
    
    public ResultSetMapper(ResultSetMap rsm, Class<T> type) throws SQLException {
    	super();
    	super.setLines(rsm.getLines());
        this.type = type;
    }

    public List<T> toList() throws IllegalArgumentException, Exception {		
		List<T> list = new ArrayList<>();
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		this.beforeFirst();
		while(this.next()) {			
			T object = objectMapper.readValue(Util.map2Json(this.getRegister()).toString(), type);			
			list.add(object);
		}
		this.beforeFirst();
		
		return list;
	}
	
	public T getFirst() throws JsonSyntaxException, Exception {
		if(this.getLines().size() <= 0)
			throw new EmptyListException("ResultSetMap está vazio.");
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		this.beforeFirst();
		if(this.next()) {
			return objectMapper.readValue(Util.map2Json(this.getRegister()).toString(), type);
		}
		
		return null;
	}
	
	public T get(int index) throws IllegalArgumentException, Exception {
		List<T> list = toList();
		
		if(list.size() <= 0)
			throw new EmptyListException("Lista está vazia.");
		
		return list.get(index);
	}

}
