package com.tivic.manager.mob;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class TalonarioDTO extends Talonario implements Serializable {
	private static final long serialVersionUID = -5555944334008977379L;
	
	private Agente agente;

	public Agente getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}
	
	
	public static class ListBuilder {
		
		private ResultSetMapper<TalonarioDTO> rsmTalonario;
		private ResultSetMapper<Agente> rsmAgente;
	
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.rsmTalonario = new ResultSetMapper<TalonarioDTO>(rsm, TalonarioDTO.class);
				
				setAgente(rsm);				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ListBuilder setAgente(ResultSetMap rsm) {
			try {
				this.rsmAgente = new ResultSetMapper<Agente>(rsm, Agente.class);				
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public List<TalonarioDTO> build() throws IllegalArgumentException, Exception {
			
			List<TalonarioDTO> talonarios = rsmTalonario.toList();
			List<Agente> agentes = rsmAgente.toList();
			
			for(int i = 0; i < talonarios.size(); i++) {
				TalonarioDTO dto = talonarios.get(i);
				
				if(i < agentes.size()) {
					dto.setAgente(agentes.get(i));
				}
			}
			
			return talonarios;
		}
	}
	
	public static class Builder {
		
		private ObjectMapper mapper;
		private TalonarioDTO dto;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), TalonarioDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}

		public Builder(int cdTalonario, boolean cascade) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Talonario talonario = TalonarioDAO.get(cdTalonario);
				dto = objectMapper.readValue(talonario.toString(), TalonarioDTO.class);
				if(cascade)
					setAgente(talonario.getCdAgente(), cascade);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder setAgente(Map<String, Object> map) {
			try {
				dto.setAgente(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), Agente.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setAgente(Agente agente) {
			try {
				dto.setAgente(agente);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		

		public Builder setAgente(int cdAgente, boolean cascade) {
			try {
				Agente agente = AgenteDAO.get(cdAgente);
				dto.setAgente(agente);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		
		public TalonarioDTO build() {
			return dto;
		}
		
	}
}
