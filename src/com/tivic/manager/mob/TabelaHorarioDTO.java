package com.tivic.manager.mob;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tivic.manager.mob.LinhaDTO.ListBuilder;
import com.tivic.manager.mob.tabelashorarios.TabelaHorarioRotaDTO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;

import sol.dao.ResultSetMap;

public class TabelaHorarioDTO extends TabelaHorario implements Serializable {

	private static final long serialVersionUID = -6993657302696879359L;
	
	private Linha linha;
	private ConcessaoVeiculo concessaoVeiculo;
	private LinhaRota linhaRotaIda;
	private LinhaRota linhaRotaVolta;
	private List<TabelaHorarioRotaDTO> horariosParadas;
	
	public TabelaHorarioDTO() {
		super();
	}

	public Linha getLinha() {
		return linha;
	}

	public void setLinhaByCd(int cdLinha) {
		this.linha = LinhaDAO.get(cdLinha);
	}
	
	public void setLinha(Linha linha) {
		this.linha = linha;
	}

	public ConcessaoVeiculo getConcessaoVeiculo() {
		return concessaoVeiculo;
	}

	public void setConcessaoVeiculoByCd(int cdConcessaoVeiculo) {
		this.concessaoVeiculo = ConcessaoVeiculoDAO.get(cdConcessaoVeiculo);
	}

	public void setConcessaoVeiculo(ConcessaoVeiculo concessaoVeiculo) {
		this.concessaoVeiculo = concessaoVeiculo;
	}
	
	public void setLinhaRotaIda(LinhaRota linhaRotaIda) {
		this.linhaRotaIda = linhaRotaIda;
	}
	
	public void setLinhaRotaVolta(LinhaRota linhaRotaVolta) {
		this.linhaRotaVolta = linhaRotaVolta;
	}
	
	public LinhaRota getLinhaRotaIda() {
		return this.linhaRotaIda;
	}

	public LinhaRota getLinhaRotaVolta() {
		return this.linhaRotaVolta;
	}
	
	public List<TabelaHorarioRotaDTO> getHorariosParadas() {
		return horariosParadas;
	}

	public void setHorariosParadas(List<TabelaHorarioRotaDTO> horariosParadas) {
		this.horariosParadas = horariosParadas;
	}



	public static class Builder {

		private ObjectMapper mapper;
		private TabelaHorarioDTO dto;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), TabelaHorarioDTO.class);
				this.dto.setLinhaByCd(dto.getCdLinha());
				this.dto.setConcessaoVeiculoByCd(dto.getCdConcessaoVeiculo());		
				this.dto.setLinhaRotaVolta(mapper.readValue(Util.map2Json((HashMap<String, Object>)map.get("LINHAROTAVOLTA")).toString(), LinhaRota.class));
				this.dto.setLinhaRotaIda(mapper.readValue(Util.map2Json((HashMap<String, Object>)map.get("LINHAROTAIDA")).toString(), LinhaRota.class));

			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		

		public TabelaHorarioDTO build() {
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
		private ResultSetMapper<TabelaHorarioDTO> tabelas;
		private ResultSetMapper<Linha> linha;
		private ResultSetMapper<ConcessaoVeiculo> concessaoVeiculo;
		private ResultSetMapper<LinhaRota> linhaRotaIda;
		private ResultSetMapper<LinhaRota> linhaRotaVolta;

		public ListBuilder(ResultSetMap rsm, int total) {
			try {
				this.tabelas = new ResultSetMapper<TabelaHorarioDTO>(rsm, TabelaHorarioDTO.class);
				this.total = total;
				setLinha(rsm);
				setConcessaoVeiculo(rsm);
				setLinhaRotaIda(rsm);
				setLinhaRotaVolta(rsm);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public ListBuilder setLinha(ResultSetMap rsm) throws SQLException {
			this.linha = new ResultSetMapper<Linha>(rsm, Linha.class);
			return this;
		}
		public ListBuilder setConcessaoVeiculo(ResultSetMap rsm) throws SQLException {
			this.concessaoVeiculo = new ResultSetMapper<ConcessaoVeiculo>(rsm, ConcessaoVeiculo.class);
			return this;
		}
		
		public ListBuilder setLinhaRotaIda(ResultSetMap rsm) throws SQLException {
			ResultSetMap rsma = new ResultSetMap();
			rsm.beforeFirst();
			
			while(rsm.next()) {
				if(rsm.getObject("LINHAROTAIDA") != null)
					rsma.addRegister((HashMap)rsm.getObject("LINHAROTAIDA"));
				else
					rsma.addRegister(new HashMap<String, Object>());
			}
			this.linhaRotaIda = new ResultSetMapper<LinhaRota>(rsma, LinhaRota.class);
			return this;
		}
		
		public ListBuilder setLinhaRotaVolta(ResultSetMap rsm) throws SQLException {
			ResultSetMap rsma = new ResultSetMap();
			rsm.beforeFirst();
			
			while(rsm.next()) {
				if(rsm.getObject("LINHAROTAVOLTA") != null)
					rsma.addRegister((HashMap)rsm.getObject("LINHAROTAVOLTA"));
				else
					rsma.addRegister(new HashMap<String, Object>());
			}
			
			this.linhaRotaVolta = new ResultSetMapper<LinhaRota>(rsma, LinhaRota.class);
			return this;
		}
		
		public PagedResponse<TabelaHorarioDTO> build() throws IllegalArgumentException, Exception {
			List<TabelaHorarioDTO> tabelas = this.tabelas.toList();
			int size = tabelas.size();
			for(int i = 0; i < size; i++) {
				TabelaHorarioDTO tabela = tabelas.get(i);
				
				if(tabela.getCdLinha() > 0 && this.linha.size() > 0) {
					tabela.setLinha(this.linha.get(i));
					tabela.setLinhaRotaIda(linhaRotaIda.get(i));
					tabela.setLinhaRotaVolta(linhaRotaVolta.get(i));
				}
			
				if(tabela.getCdConcessaoVeiculo() > 0 && this.concessaoVeiculo.size() > 0) {
					tabela.setConcessaoVeiculo(this.concessaoVeiculo.get(i));
				}
			}
		
			return new PagedResponse<TabelaHorarioDTO>(tabelas, this.total);
		}
	}
	
}
