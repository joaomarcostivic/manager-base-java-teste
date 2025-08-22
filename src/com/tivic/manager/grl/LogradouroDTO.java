package com.tivic.manager.grl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class LogradouroDTO extends Logradouro implements Serializable {
	
	/**
	 * 
	 */
	private Bairro bairro;
	private Logradouro logradouro;

	public Bairro getBairro() {
		return this.bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}
	
	private static final long serialVersionUID = 2297500645976172811L;

	public static class ListBuilder {
		
		private ResultSetMap rsm;
		
		public ListBuilder(ResultSetMap rsm) {
			this.rsm = rsm;		
		}
		
		public List<LogradouroDTO> build() {
			List<LogradouroDTO> logradouro = new ArrayList<LogradouroDTO>();
			
			this.rsm.beforeFirst();
			while(this.rsm.next()) {
				logradouro.add(new LogradouroDTO.Builder(this.rsm.getRegister()).build());
			}
			this.rsm.beforeFirst();
			
			return logradouro;
		}
	}
	
	public static class Builder {

		private ObjectMapper mapper;
		private LogradouroDTO dto;
		private Logradouro logradouro;
		private ResultSetMap rsm;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), LogradouroDTO.class);
				
				// TODO: imagens
				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(Logradouro logradouro) {
			this.logradouro = logradouro;
			this.dto = new LogradouroDTO();
			this.rsm = new ResultSetMap();
			this.montarDTO(logradouro);
		}
		public Builder getBairro() {

			this.rsm = LogradouroBairroServices.getAllByLogradouro(logradouro.getCdLogradouro());
			while(rsm.next()) {
				this.dto.setBairro(BairroServices.getBairroByNome(rsm.getString("NM_BAIRRO"), null));
			}

			return this;
		}


		public void montarDTO(Logradouro logradouro) {		
			this.dto.setCdLogradouro(logradouro.getCdLogradouro());
			this.dto.setCdDistrito(logradouro.getCdDistrito());
			this.dto.setCdCidade(logradouro.getCdCidade());
			this.dto.setCdTipoLogradouro(logradouro.getCdTipoLogradouro());
			this.dto.setNmLogradouro(logradouro.getNmLogradouro());
			this.dto.setIdLogradouro(logradouro.getIdLogradouro());
			this.dto.setCdRegiao(logradouro.getCdRegiao());
		}
		
		public LogradouroDTO build() {
			return dto;
		}
		
	}


}
