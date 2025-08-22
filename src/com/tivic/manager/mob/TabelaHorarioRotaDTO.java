package com.tivic.manager.mob;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.EmpresaDTO;
import com.tivic.manager.mob.LinhaDTO.ListBuilder;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;

import sol.dao.ResultSetMap;



public class TabelaHorarioRotaDTO extends TabelaHorario implements Serializable{
	
	private static final long serialVersionUID = -4284533035220901784L;
	
	private ArrayList<TabelaHorarioDTO> horarios; 
	private ArrayList<LinhaDTO> paradas;

	public TabelaHorarioRotaDTO() {
		this.horarios = new ArrayList();
		this.paradas = new ArrayList();
	}
	
	public ArrayList<TabelaHorarioDTO> getHorarios() {
		return horarios;
	}
	
	public ArrayList<LinhaDTO> getParadas() {
		return paradas;
	}
	
	public void setParadas(ArrayList<LinhaDTO> linhaDTO) {
		this.paradas = linhaDTO;
	}
	
	public void setHorarios(ArrayList<TabelaHorarioDTO> horarios) {
		this.horarios = horarios;
	}

	public static class ListBuilder {
		
		private ResultSetMapper<TabelaHorarioRotaDTO> rsmTabela;
		private ResultSetMapper<TabelaHorarioDTO> horarios;
		private ResultSetMapper<LinhaDTO> paradas;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.rsmTabela = new ResultSetMapper<TabelaHorarioRotaDTO>(rsm, TabelaHorarioRotaDTO.class);	
				setHorarios(rsm);
				setParadas(rsm);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ListBuilder setHorarios(ResultSetMap rsm) throws SQLException {
			this.horarios = new ResultSetMapper<TabelaHorarioDTO>(rsm, TabelaHorarioDTO.class);
			return this;
		}
		
		public ListBuilder setParadas(ResultSetMap rsm){
			try {
				this.paradas = new ResultSetMapper<LinhaDTO>(rsm, LinhaDTO.class);
				return this;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		
		public List<TabelaHorarioRotaDTO> build() throws IllegalArgumentException, Exception  {
			
	        List<TabelaHorarioRotaDTO> tabelas = this.rsmTabela.toList();
	
	
			int size = tabelas.size();
			for(int i = 0; i < size; i++) {
				TabelaHorarioRotaDTO tabela = tabelas.get(i);
				
				if(tabela.getCdLinha() > 0 && this.rsmTabela.size() > 0) {
					tabela.setHorarios( (ArrayList<TabelaHorarioDTO>) horarios.toList());
					tabela.setParadas( (ArrayList<LinhaDTO>) paradas.toList());
				}
			
			}
		
			return rsmTabela.toList();
		}
	}

}
