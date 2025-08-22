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

public class TipoEtapaDTO extends TipoEtapa implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2297500645976172811L;

	public static class ListBuilder {
		
		private ResultSetMap rsm;
		
		public ListBuilder(ResultSetMap rsm) {
			this.rsm = rsm;		
		}
		
		public List<TipoEtapaDTO> build() {
			List<TipoEtapaDTO> tipoEtapa = new ArrayList<TipoEtapaDTO>();
			
			this.rsm.beforeFirst();
			while(this.rsm.next()) {
				tipoEtapa.add(new TipoEtapaDTO.Builder(this.rsm.getRegister()).build());
			}
			this.rsm.beforeFirst();
			
			return tipoEtapa;
		}
	}
	
	public static class Builder {

		private ObjectMapper mapper;
		private TipoEtapaDTO dto;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), TipoEtapaDTO.class);
				
				// TODO: imagens
				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public TipoEtapaDTO build() {
			return dto;
		}
		
	}


}
