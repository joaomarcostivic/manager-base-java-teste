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

public class LinhaDTO extends Linha implements Serializable {

	private static final long serialVersionUID = -4284533035220901784L;
	
	private Concessao concessao;
	private Linha linha;
	private LinhaRota linhaRota;
	private LinhaTrecho linhaTrecho;
	private Pessoa concessionario;
	
	public Pessoa getConcessionario() {
		return concessionario;
	}

	public void setConcessionario(Pessoa concessionario ) {
		this.concessionario = concessionario;
	}
	
	public Concessao getConcessao() {
		return concessao;
	}
	
	public Linha getLinha() {
		return linha;
	}
	
	public void setLinha(Linha linha) {
		this.linha = linha;
	}
	
	
	public LinhaRota getLinhaRota() {
		return linhaRota;
	}
	
	public void setLinhaTrecho(LinhaTrecho linhaTrecho) {
		this.linhaTrecho = linhaTrecho;
	}
	
	
	public LinhaTrecho getLinhaTrecho() {
		return linhaTrecho;
	}
/*
	public void setConcessao(int cdConcessao) {
		this.concessao = ConcessaoDAO.get(cdConcessao);
	}
*/
	
	public void setConcessao(Concessao concessao) {
		this.concessao = concessao;
	}
	
	public void setLinhaRota(LinhaRota linhaRota) {
		this.linhaRota = linhaRota;
	}
	
	
	public static class Builder {

		private ObjectMapper mapper;
		private LinhaDTO dto;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), LinhaDTO.class);
				
				//this.dto.setConcessao(dto.getCdConcessao());		
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		

		public LinhaDTO build() {
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
		private ResultSetMapper<LinhaDTO> linhas;
		private ResultSetMapper<Concessao> concessoes;
		private ResultSetMapper<Pessoa> concessionario;
		private ResultSetMapper<Linha> linha;
		private ResultSetMapper<LinhaRota> linhasRotas;
		private ResultSetMapper<LinhaTrecho> linhaTrecho;
		
		public ListBuilder(ResultSetMap rsm, int total) {
			try {
				this.linhas = new ResultSetMapper<LinhaDTO>(rsm, LinhaDTO.class);
				this.total = total;
				setConcessao(rsm);
				setConcessionario(rsm);
				setLinha(rsm);
				setLinhaRota(rsm);
				setLinhaTrecho(rsm);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public ListBuilder setConcessao(ResultSetMap rsm) throws SQLException {
			this.concessoes = new ResultSetMapper<Concessao>(rsm, Concessao.class);
			return this;
		}
		
		public ListBuilder setConcessionario(ResultSetMap rsm) throws SQLException {
			this.concessionario = new ResultSetMapper<Pessoa>(rsm, Pessoa.class);
			return this;
		}
		
		public ListBuilder setLinha(ResultSetMap rsm) throws SQLException {
			this.linha = new ResultSetMapper<Linha>(rsm, Linha.class);
			return this;
		}
		
		public ListBuilder setLinhaRota(ResultSetMap rsm) throws SQLException {
			this.linhasRotas = new ResultSetMapper<LinhaRota>(rsm, LinhaRota.class);
			return this;
		}
		
		public ListBuilder setLinhaTrecho(ResultSetMap rsm) throws SQLException {
			this.linhaTrecho = new ResultSetMapper<LinhaTrecho>(rsm, LinhaTrecho.class);
			return this;
		}
		
		
		public PagedResponse build() throws IllegalArgumentException, Exception {
			List<LinhaDTO> linhas = this.linhas.toList();
			List<LinhaTrecho> linhaTrecho = this.linhaTrecho.toList();
			
			int size = linhas.size();
			for(int i = 0; i < size; i++) {
				LinhaDTO linha = linhas.get(i);
				if(linha.getCdConcessao() > 0 && concessoes.size() > 0) {
					linha.setConcessao(concessoes.get(i));
					linha.setConcessionario(concessionario.get(i));
					linha.setLinhaRota(linhasRotas.get(i));
					linha.setLinhaTrecho(linhaTrecho.get(i));
				}
				
			}
			
			return new PagedResponse<LinhaDTO>(linhas, this.total);
		}
	}
	
}
