package com.tivic.manager.mob;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.mob.tabelashorarios.ParadaDTO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class LinhaRotaDTO implements Serializable {

	private static final long serialVersionUID = -4284533035220901784L;
	
	private Concessao concessao;
	private Linha linha;
	private LinhaRota linhaRota;
	private List<LinhaTrechoDTO> linhaTrechos;
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
	
	public void setLinhaTrechosDTO(List<LinhaTrechoDTO> linhaTrechosDTO) {
		this.linhaTrechos = linhaTrechosDTO;
	}
	
	public void setRsmLinhaTrechosDTO(ResultSetMap rsm) {
		try {
			this.linhaTrechos = new LinhaTrechoDTO.ListBuilder(rsm).build();
		} catch (Exception e) {
			this.linhaTrechos = new ArrayList<LinhaTrechoDTO>();
			e.printStackTrace(System.out);
		}
	}
	
	public List<LinhaTrechoDTO> getLinhaTrechos() {
		return linhaTrechos;
	}
	
	public void setConcessao(Concessao concessao) {
		this.concessao = concessao;
	}
	
	public void setLinhaRota(LinhaRota linhaRota) {
		this.linhaRota = linhaRota;
	}
	
	
	public static class Builder {

		private ObjectMapper mapper;
		private LinhaRotaDTO dto;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), LinhaRotaDTO.class);
				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		

		public LinhaRotaDTO build() {
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
		private ResultSetMapper<LinhaRotaDTO> linhaRotas;
		private ResultSetMapper<Concessao> concessoes;
		private ResultSetMapper<Pessoa> concessionario;
		private ResultSetMapper<Linha> linha;
		private ResultSetMapper<LinhaRota> linhaRota;
		private List<ResultSetMapper<LinhaTrechoDTO>> linhaTrechosDTO;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.linhaRotas = new ResultSetMapper<LinhaRotaDTO>(rsm, LinhaRotaDTO.class);
				this.total = total;
				setConcessao(rsm);
				setConcessionario(rsm);
				setLinha(rsm);
				setLinhaRota(rsm);
				setLinhaTrechos(rsm);
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
			this.linhaRota = new ResultSetMapper<LinhaRota>(rsm, LinhaRota.class);
			return this;
		}
		
		public ListBuilder setLinhaTrechos(ResultSetMap rsm) throws SQLException {
			this.linhaTrechosDTO = new ArrayList<ResultSetMapper<LinhaTrechoDTO>>();
			rsm.beforeFirst();
			while(rsm.next()) {
				if(rsm.getObject("RSM_LINHA_TRECHOS") == "nenhuma coluna") {
					this.linhaTrechosDTO.add(new ResultSetMapper(LinhaTrechoDTO.class));
				}else {
					this.linhaTrechosDTO.add(new ResultSetMapper<LinhaTrechoDTO>((ResultSetMap) rsm.getObject("RSM_LINHA_TRECHOS"), LinhaTrechoDTO.class));
				}
			}

			return this;
		}
		
		
		public List<LinhaRotaDTO> build() throws IllegalArgumentException, Exception {
			List<LinhaRotaDTO> linhaRotas = this.linhaRotas.toList();
			int size = linhaRotas.size();

			for(int i = 0; i < size; i++) {
				LinhaRotaDTO linhaRota = linhaRotas.get(i);
				
				if(concessoes.size() > 0) {
					linhaRota.setConcessao(concessoes.get(i));
					linhaRota.setConcessionario(concessionario.get(i));
					linhaRota.setLinha(linha.get(i));
					linhaRota.setLinhaRota(this.linhaRota.get(i));
					linhaRota.setRsmLinhaTrechosDTO(this.linhaTrechosDTO.get(i));
				}
				
			}
			
			return linhaRotas;
		}
	}
	
}
