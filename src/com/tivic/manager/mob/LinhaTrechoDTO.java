package com.tivic.manager.mob;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class LinhaTrechoDTO implements Serializable {

	private static final long serialVersionUID = -4284533035220901784L;
	
	private LinhaTrecho linhaTrecho;
	private GrupoParadaDTO grupoParadaDTO;
	
	public GrupoParada getGrupoParadaDTO() {
		return grupoParadaDTO;
	}
	
	public void setGrupoParadaDTO(GrupoParadaDTO grupoParadaDTO) {
		this.grupoParadaDTO = grupoParadaDTO;
	}
	
	
	public void setLinhaTrecho(LinhaTrecho linhaTrecho) {
		this.linhaTrecho = linhaTrecho;
	}
	
	
	public LinhaTrecho getLinhaTrecho() {
		return linhaTrecho;
	}
	
	
	
	public static class Builder {

		private ObjectMapper mapper;
		private LinhaTrechoDTO dto;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), LinhaTrechoDTO.class);
				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		

		public LinhaTrechoDTO build() {
			return dto;
		}
		
		@Override
		public String toString() {
			try {
				return mapper.writeValueAsString(this.dto);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static class ListBuilder {

		private int total;
		private ResultSetMapper<LinhaTrechoDTO> linhaTrechos;
		private ResultSetMapper<LinhaTrecho> linhaTrecho;
		private ResultSetMapper<GrupoParadaDTO> grupoParadaDTO;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.linhaTrechos = new ResultSetMapper<LinhaTrechoDTO>(rsm, LinhaTrechoDTO.class);
				this.total = total;
				setGrupoParadaDTO(rsm);
				setLinhaTrecho(rsm);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public ListBuilder setGrupoParadaDTO(ResultSetMap rsm) throws SQLException {
			this.grupoParadaDTO = new ResultSetMapper<GrupoParadaDTO>(rsm, GrupoParadaDTO.class);
			return this;
		}
		
		public ListBuilder setLinhaTrecho(ResultSetMap rsm) throws SQLException {
			this.linhaTrecho = new ResultSetMapper<LinhaTrecho>(rsm, LinhaTrecho.class);
			return this;
		}
		
		
		public List<LinhaTrechoDTO> build() throws IllegalArgumentException, Exception {
			List<LinhaTrechoDTO> linhaTrechos = this.linhaTrechos.toList();
			
			int size = linhaTrechos.size();
			for(int i = 0; i < size; i++) {
				LinhaTrechoDTO linhaTrecho = linhaTrechos.get(i);
				if(this.linhaTrecho.size() > 0) {
					linhaTrecho.setGrupoParadaDTO(grupoParadaDTO.get(i));
					linhaTrecho.setLinhaTrecho(this.linhaTrecho.get(i));
				}
				
			}
			
			return linhaTrechos;
		}
	}
	
}
