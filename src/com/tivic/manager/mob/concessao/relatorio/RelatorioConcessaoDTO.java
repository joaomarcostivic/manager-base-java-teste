package com.tivic.manager.mob.concessao.relatorio;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;

import sol.dao.ResultSetMap;
public class RelatorioConcessaoDTO {
	private Integer tpConcessao;
	private int stConcessao;
	private String dtInicioConcessao;
	private String dtFinalConcessao;
	private String nmConcessionario;
	
	public RelatorioConcessaoDTO() {
		super();
	}
	
	public RelatorioConcessaoDTO( Integer tpConcessao,  int stConcessao, String dtInicioConcessao, String dtFinalConcessao, String nmConcessionario) {
		 this.tpConcessao = tpConcessao;
		 this.stConcessao =stConcessao;
		 this.dtInicioConcessao = dtInicioConcessao;
		 this.dtFinalConcessao = dtFinalConcessao;
		 this.nmConcessionario = nmConcessionario;
	}
	
	public Integer getTpConcessao() {
		return tpConcessao;
	}

	public void setTpConcessao(Integer tpConcessao) {
		this.tpConcessao = tpConcessao;
	}

	public int getStConcessao() {
		return stConcessao;
	}

	public void setStConcessao(int stConcessao) {
		this.stConcessao = stConcessao;
	}

	public String getDtInicioConcessao() {
		return dtInicioConcessao;
	}

	public void setDtInicioConcessao(String dtInicioConcessao) {
		this.dtInicioConcessao = dtInicioConcessao;
	}

	public String getDtFinalConcessao() {
		return dtFinalConcessao;
	}

	public void setDtFinalConcessao(String dtFinalConcessao) {
		this.dtFinalConcessao = dtFinalConcessao;
	}

	public String getNmConcessionario() {
		return nmConcessionario;
	}

	public void setNmConcessionario(String nmConcessionario) {
		this.nmConcessionario = nmConcessionario;
	}

	public static class Builder {

		private ObjectMapper mapper;
		private RelatorioConcessaoDTO dto;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), RelatorioConcessaoDTO.class);

			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}


		public RelatorioConcessaoDTO build() {
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
		private ResultSetMapper<RelatorioConcessaoDTO> horarios;

		public ListBuilder(ResultSetMap rsm, int total) {
			try {
				this.horarios = new ResultSetMapper<RelatorioConcessaoDTO>(rsm, RelatorioConcessaoDTO.class);
				this.total = total;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public PagedResponse<RelatorioConcessaoDTO> build() throws IllegalArgumentException, Exception {
			List<RelatorioConcessaoDTO> horarios = this.horarios.toList();

			return new PagedResponse<RelatorioConcessaoDTO>(horarios, this.total);
		}
	}

}
