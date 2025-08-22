package com.tivic.manager.acd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class TipoDependenciaDTO extends TipoDependencia implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2297500645976172811L;

	public static class ListBuilder {
		
		private ResultSetMap rsm;
		
		public ListBuilder(ResultSetMap rsm) {
			this.rsm = rsm;		
		}
		
		public List<TipoDependenciaDTO> build() {
			List<TipoDependenciaDTO> tipoDependencia = new ArrayList<TipoDependenciaDTO>();
			
			this.rsm.beforeFirst();
			while(this.rsm.next()) {
				tipoDependencia.add(new TipoDependenciaDTO.Builder(this.rsm.getRegister()).build());
			}
			this.rsm.beforeFirst();
			
			return tipoDependencia;
		}
	}
	
	public static class Builder {

		private ObjectMapper mapper;
		private TipoDependenciaDTO dto;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), TipoDependenciaDTO.class);
				
				// TODO: imagens
				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public TipoDependenciaDTO build() {
			return dto;
		}
		
	}


}
