package com.tivic.manager.acd;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tivic.manager.acd.TurmaDTO.Builder;
import com.tivic.manager.acd.TurmaDTO.ListBuilder;
import com.tivic.manager.grl.EmpresaDTO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class InstituicaoDTO extends Instituicao {

	private EmpresaDTO empresa;
	
	public EmpresaDTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}	
	
	/**
	 * Construir List<InstituicaoDTO> a partir de um {@link ResultSetMap}	
	 */
	public static class ListBuilder {
		
		private ResultSetMapper<InstituicaoDTO> rsmInstituicao;
		private List<EmpresaDTO> empresas;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.rsmInstituicao = new ResultSetMapper<InstituicaoDTO>(rsm, InstituicaoDTO.class);	
				this.empresas = new ArrayList<EmpresaDTO>();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ListBuilder setEmpresa(ResultSetMap rsm) {
			try {
				this.empresas = new EmpresaDTO.ListBuilder(rsm).setPessoaJuridica(rsm).build();
				return this;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public List<InstituicaoDTO> build() throws IllegalArgumentException, Exception {
			
			List<InstituicaoDTO> instituicoes = rsmInstituicao.toList();
			
			for(int i = 0; i < instituicoes.size(); i++) {
				InstituicaoDTO dto = instituicoes.get(i);
				
				if(i < this.empresas.size())
					dto.setEmpresa(this.empresas.get(i));
				
			}
			
			return instituicoes;
		}
	}
	
	public static class Builder {
		
		private InstituicaoDTO dto;
		
		public Builder(Map<String, Object> map) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = objectMapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), InstituicaoDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(int cdInstituicao, boolean cascade) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Instituicao instituicao = InstituicaoDAO.get(cdInstituicao);
				dto = objectMapper.readValue(instituicao.toString(), InstituicaoDTO.class);
				if(cascade)
					setEmpresa(cdInstituicao, cascade);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder setEmpresa(Map<String, Object> map) {
			try {
				dto.setEmpresa(new EmpresaDTO.Builder(map).setPessoaJuridica(map).build());
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		private Builder setEmpresa(int cdEmpresa, boolean cascade) {
			try {
				dto.setEmpresa(new EmpresaDTO.Builder(cdEmpresa, cascade).build());
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		
		public InstituicaoDTO build() {
			return dto;
		}
		
	}
	
}
