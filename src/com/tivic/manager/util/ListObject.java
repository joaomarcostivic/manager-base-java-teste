package com.tivic.manager.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ListObject<outputType, inputType> implements Serializable{
	private static final long serialVersionUID = 1L;
	private Class<outputType> type;
	
	public ListObject(Class<outputType> outputType) {
		this.type = outputType;
	}
	
	public List<outputType> convertList(List<inputType> inputList) throws JsonParseException, JsonMappingException, IOException {
		List<outputType> list = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		for (inputType obj : inputList) {	
			outputType object = objectMapper.readValue(obj.toString(), type);
			list.add(object);
		}
		return list;
	}
	
}
