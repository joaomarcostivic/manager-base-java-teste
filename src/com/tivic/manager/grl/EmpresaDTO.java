package com.tivic.manager.grl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.tivic.manager.acd.InstituicaoDTO;
import com.tivic.manager.acd.InstituicaoDTO.Builder;
import com.tivic.manager.acd.InstituicaoDTO.ListBuilder;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class EmpresaDTO extends Empresa {

	private PessoaJuridicaDTO pessoaJuridica;
	
	public PessoaJuridicaDTO getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridicaDTO pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}	
	
	/**
	 * Construir List<EmpresaDTO> a partir de um {@link ResultSetMap}	
	 */
	public static class ListBuilder {
		
		private ResultSetMapper<EmpresaDTO> rsmEmpresa;
		private List<PessoaJuridicaDTO> pessoasJuridica;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.rsmEmpresa = new ResultSetMapper<EmpresaDTO>(rsm, EmpresaDTO.class);
				this.pessoasJuridica = new ArrayList<PessoaJuridicaDTO>();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ListBuilder setPessoaJuridica(ResultSetMap rsm) {
			try {
				this.pessoasJuridica = new PessoaJuridicaDTO.ListBuilder(rsm).setPessoa(rsm).build();
				return this;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public List<EmpresaDTO> build() throws IllegalArgumentException, Exception {
			
			List<EmpresaDTO> empresas = rsmEmpresa.toList();
			
			for(int i = 0; i < empresas.size(); i++) {
				EmpresaDTO dto = empresas.get(i);
				
				if(i < this.pessoasJuridica.size())
					dto.setPessoaJuridica(this.pessoasJuridica.get(i));
				
			}
			
			return empresas;
		}
	}
	
	public static class Builder {
		
		private EmpresaDTO dto;
		private Gson gson;
		
		public Builder(Map<String, Object> map) {
			try {
				gson = new Gson();
				dto = gson.fromJson(Util.map2Json((HashMap<String, Object>)map).toString(), EmpresaDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}

		public Builder(int cdEmpresa, boolean cascade) {
			try {
				gson = new Gson();
				Empresa empresa = EmpresaDAO.get(cdEmpresa);
				dto = gson.fromJson(empresa.toString(), EmpresaDTO.class);
				if(cascade)
					setPessoaJuridica(cdEmpresa, cascade);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder setPessoaJuridica(Map<String, Object> map) {
			try {
				dto.setPessoaJuridica(new PessoaJuridicaDTO.Builder(map).setPessoa(map).build());
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setPessoaJuridica(int cdPessoa, boolean cascade) {
			try {
				dto.setPessoaJuridica(new PessoaJuridicaDTO.Builder(cdPessoa, cascade).build());
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public EmpresaDTO build() {
			return dto;
		}
		
	}
	
}
