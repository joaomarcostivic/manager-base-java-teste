package com.tivic.manager.acd;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tivic.manager.acd.OfertaDTO.Builder;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaJuridicaDTO;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.grl.PessoaJuridicaDTO.ListBuilder;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class DisciplinaDTO extends Disciplina {

	private ProdutoServico produtoServico;
	
	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}	

	/**
	 * Construir List<DisciplinaDTO> a partir de um {@link ResultSetMap}	
	 */
	public static class ListBuilder {
		
		private ResultSetMapper<DisciplinaDTO> rsmDisciplina;
		private ResultSetMapper<ProdutoServico> rsmProdutoServico;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				
				this.rsmDisciplina = new ResultSetMapper<DisciplinaDTO>(rsm, DisciplinaDTO.class);	
				this.rsmProdutoServico = new ResultSetMapper<ProdutoServico>(new ResultSetMap(), ProdutoServico.class);	
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ListBuilder setProdutoServico(ResultSetMap rsm) {
			try {
				this.rsmProdutoServico = new ResultSetMapper<ProdutoServico>(rsm, ProdutoServico.class);
				return this;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public List<DisciplinaDTO> build() throws IllegalArgumentException, Exception {
			
			List<DisciplinaDTO> disciplinas = rsmDisciplina.toList();
			List<ProdutoServico> produtosServico = rsmProdutoServico.toList();
			
			for(int i = 0; i < disciplinas.size(); i++) {
				DisciplinaDTO dto = disciplinas.get(i);
				
				if(i < produtosServico.size())
					dto.setProdutoServico(produtosServico.get(i));
				
			}
			
			return disciplinas;
		}
	}
	
	public static class Builder {
		
		private DisciplinaDTO dto;
		
		public Builder(Map<String, Object> map) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = objectMapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), DisciplinaDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(int cdDisciplina, boolean cascade) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Disciplina disciplina = DisciplinaDAO.get(cdDisciplina);
				dto = objectMapper.readValue(disciplina.toString(), DisciplinaDTO.class);
				if(cascade)
					setProdutoServico(cdDisciplina);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder setProdutoServico(Map<String, Object> map) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto.setProdutoServico(objectMapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), ProdutoServico.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		private Builder setProdutoServico(int cdProdutoServico) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				ProdutoServico produtoServico = ProdutoServicoDAO.get(cdProdutoServico);
				dto.setProdutoServico(objectMapper.readValue(produtoServico.toString(), ProdutoServico.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public DisciplinaDTO build() {
			return dto;
		}
		
	}
}
