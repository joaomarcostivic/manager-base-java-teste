package com.tivic.manager.grl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class PessoaJuridicaDTO extends PessoaJuridica {

	private Pessoa pessoa;
	
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}	

	/**
	 * Construir List<PessoaJuridicaDTO> a partir de um {@link ResultSetMap}	
	 */
	public static class ListBuilder {
		
		private ResultSetMapper<PessoaJuridicaDTO> rsmPessoaJuridica;
		private ResultSetMapper<Pessoa> rsmPessoa;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.rsmPessoaJuridica = new ResultSetMapper<PessoaJuridicaDTO>(rsm, PessoaJuridicaDTO.class);	
				//this.rsmPessoa = new ResultSetMapper<Pessoa>(new ResultSetMap(), Pessoa.class);	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ListBuilder setPessoa(ResultSetMap rsm) {
			try {
				this.rsmPessoa = new ResultSetMapper<Pessoa>(rsm, Pessoa.class);
				return this;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public List<PessoaJuridicaDTO> build() throws IllegalArgumentException, Exception {
			List<PessoaJuridicaDTO> pessoasJuridica = rsmPessoaJuridica.toList();
			List<Pessoa> pessoas = rsmPessoa.toList();
			
			for(int i = 0; i < pessoasJuridica.size(); i++) {
				PessoaJuridicaDTO dto = pessoasJuridica.get(i);
				if(i < pessoas.size())
					dto.setPessoa(pessoas.get(i));
				
			}
			
			return pessoasJuridica;
		}
	}
	
	public static class Builder {
		
		private PessoaJuridicaDTO dto;
		private ObjectMapper mapper;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), PessoaJuridicaDTO.class);				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}

		public Builder(int cdPessoa, boolean cascade) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				PessoaJuridica pessoaJuridica = PessoaJuridicaDAO.get(cdPessoa);
				dto = mapper.readValue(pessoaJuridica.toString(), PessoaJuridicaDTO.class);
				if(cascade)
					setPessoa(cdPessoa);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder setPessoa(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto.setPessoa(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), Pessoa.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		private Builder setPessoa(int cdPessoa) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Pessoa pessoa = PessoaDAO.get(cdPessoa);
				dto.setPessoa(mapper.readValue(pessoa.toString(), Pessoa.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoa(Pessoa pessoa) {
			try {
				dto.setPessoa(pessoa);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public PessoaJuridicaDTO build() {
			return dto;
		}
		
	}
}
