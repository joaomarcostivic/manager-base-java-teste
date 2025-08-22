package com.tivic.manager.acd;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tivic.manager.acd.OfertaDTO.Builder;
import com.tivic.manager.acd.OfertaDTO.ListBuilder;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class AulaDTO extends Aula implements Serializable  {
	private static final long serialVersionUID = 5149912491768510821L;
	
	/**
	 * Construir List<AulaDTO> a partir de um {@link ResultSetMap}	
	 */
	public static class ListBuilder{
		private ResultSetMapper<AulaDTO> rsmAula;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.rsmAula = new ResultSetMapper<AulaDTO>(rsm, AulaDTO.class);	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public List<AulaDTO> build() throws IllegalArgumentException, Exception {
			List<AulaDTO> aulas = rsmAula.toList();
			return aulas;
		}
	}
	
	public static class Builder {
		
		private AulaDTO dto;
		
		public Builder(Map<String, Object> map) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = objectMapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), AulaDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(int cdAula) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Aula aula = AulaDAO.get(cdAula);
				dto = objectMapper.readValue(aula.toString(), AulaDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public AulaDTO build() {
			return dto;
		}
		
	}
	
	

}
