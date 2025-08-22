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
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class OfertaDTO extends Oferta implements Serializable {
	private static final long serialVersionUID = -8763841823758282688L;
	/**
	 * Construir List<OfertaDTO> a partir de um {@link ResultSetMap}	
	 */
	public static class ListBuilder {
		
		private ResultSetMapper<OfertaDTO> rsmOferta;
		public ListBuilder(ResultSetMap rsm) {
			try {
				
				this.rsmOferta = new ResultSetMapper<OfertaDTO>(rsm, OfertaDTO.class);	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		public List<OfertaDTO> build() throws IllegalArgumentException, Exception {
			List<OfertaDTO> ofertas = rsmOferta.toList();
			return ofertas;
		}
	}
	
	public static class Builder {
		
		private OfertaDTO dto;
		
		public Builder(Map<String, Object> map) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = objectMapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), OfertaDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(int cdOferta) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Oferta oferta = OfertaDAO.get(cdOferta);
				dto = objectMapper.readValue(oferta.toString(), OfertaDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}

		public OfertaDTO build() {
			return dto;
		}
		
	}
	
}
