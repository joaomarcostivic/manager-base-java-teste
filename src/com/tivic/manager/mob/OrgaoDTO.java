package com.tivic.manager.mob;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgaoDTO extends Orgao implements Serializable {
	private static final long serialVersionUID = -4730684659867097680L;

	private Pessoa pessoa;
	
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}	
	
	public Orgao toOrgao() {
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			JSONObject jsonOrgao = new JSONObject(mapper.writeValueAsString(this));
			JSONObject jsonPessoa = new JSONObject(mapper.writeValueAsString((pessoa != null ? pessoa : new Pessoa())));
			
			Iterator<String> it = jsonPessoa.keys();
			
			while(it.hasNext()){
				String key = (String)it.next();
			    jsonOrgao.put(key, jsonPessoa.get(key));
			}
			
			return mapper.readValue(jsonOrgao.toString(), Orgao.class);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Construir List<InstituicaoDTO> a partir de um {@link ResultSetMap}	
	 */
	public static class ListBuilder {
		
		private ResultSetMapper<OrgaoDTO> rsmOrgao;
		private ResultSetMapper<Pessoa> rsmPessoa;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.rsmOrgao = new ResultSetMapper<OrgaoDTO>(rsm, OrgaoDTO.class);	
				this.rsmPessoa = new ResultSetMapper<Pessoa>(new ResultSetMap(), Pessoa.class);	
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ListBuilder setPessoa(ResultSetMap rsm) {
			try {
				this.rsmPessoa = new ResultSetMapper<Pessoa>(rsm, Pessoa.class);	
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		public List<OrgaoDTO> build() throws IllegalArgumentException, Exception {
			
			List<OrgaoDTO> orgaos = rsmOrgao.toList();
			List<Pessoa> pessoas = rsmPessoa.toList();
			
			for(int i = 0; i < orgaos.size(); i++) {
				OrgaoDTO dto = orgaos.get(i);
				
				if(i < pessoas.size())
					dto.setPessoa(pessoas.get(i));
				
			}
			return orgaos;
		}
		
	}
	

	public static class Builder {
		
		private ObjectMapper mapper;
		private OrgaoDTO dto;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), OrgaoDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		

		public Builder(int cdOrgao, boolean cascade) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Orgao orgao = OrgaoDAO.get(cdOrgao);
				System.out.println(orgao.toString());
				dto = objectMapper.readValue(orgao.toString(), OrgaoDTO.class);
				if(cascade)
					setPessoa(cdOrgao, cascade);
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
		

		public Builder setPessoa(int cdPessoa, boolean cascade) {
			try {
				Pessoa pessoa = PessoaDAO.get(cdPessoa);
				dto.setPessoa(pessoa);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		
		public OrgaoDTO build() {
			return dto;
		}
		
	}

}
