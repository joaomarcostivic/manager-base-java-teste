package com.tivic.manager.acd;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class TurmaDTO extends Turma {
	/**
	 * Construir List<TurmaDTO> a partir de um {@link ResultSetMap}	
	 */
	public static class ListBuilder {
		
		private ResultSetMapper<TurmaDTO> rsmTurma;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				
				this.rsmTurma = new ResultSetMapper<TurmaDTO>(rsm, TurmaDTO.class);	
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public List<TurmaDTO> build() throws IllegalArgumentException, Exception {
			
			List<TurmaDTO> turmas = rsmTurma.toList();
			return turmas;
		}
	}
	
	public static class Builder {
		
		private TurmaDTO dto;
		
		public Builder(Map<String, Object> map) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = objectMapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), TurmaDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(int cdTurma) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Turma turma = TurmaDAO.get(cdTurma);
				dto = objectMapper.readValue(turma.toString(), TurmaDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
	
		public TurmaDTO build() {
			return dto;
		}
		
	}
	
}
