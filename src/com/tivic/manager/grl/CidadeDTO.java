package com.tivic.manager.grl;

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

public class CidadeDTO extends Cidade implements Serializable  {
	private static final long serialVersionUID = 5420209884853278592L;
	
	private Estado estado;
	
	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}	
	

	/**
	 * Construir List<CidadeDTO> a partir de um {@link ResultSetMap}	
	 */
	public static class ListBuilder {
		
		private ResultSetMapper<CidadeDTO> rsmCidade;
		private ResultSetMapper<Estado> rsmEstado;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.rsmCidade = new ResultSetMapper<CidadeDTO>(rsm, CidadeDTO.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ListBuilder setEstado(ResultSetMap rsm) {
			try {
				this.rsmEstado = new ResultSetMapper<Estado>(rsm, Estado.class);
				return this;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public List<CidadeDTO> build() throws IllegalArgumentException, Exception {
			List<CidadeDTO> estadosFisica = rsmCidade.toList();
			List<Estado> estados = rsmEstado.toList();
			
			for(int i = 0; i < estadosFisica.size(); i++) {
				CidadeDTO dto = estadosFisica.get(i);
				if(i < estados.size())
					dto.setEstado(estados.get(i));
				
			}
			
			return estadosFisica;
		}
	}
	
	public static class Builder {
		
		private CidadeDTO dto;
		private ObjectMapper mapper;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), CidadeDTO.class);				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}

		public Builder(int cdCidade, boolean cascade) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Cidade cidade = CidadeDAO.get(cdCidade);
				dto = mapper.readValue(cidade.toString(), CidadeDTO.class);
				if(cascade)
					setEstado(cidade.getCdEstado());
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder setEstado(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto.setEstado(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), Estado.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		private Builder setEstado(int cdEstado) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Estado estado = EstadoDAO.get(cdEstado);
				dto.setEstado(mapper.readValue(estado.toString(), Estado.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setEstado(Estado estado) {
			try {
				dto.setEstado(estado);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public CidadeDTO build() {
			return dto;
		}
		
	}
}
