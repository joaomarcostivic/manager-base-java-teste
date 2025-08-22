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
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class GrupoParadaDTO extends GrupoParada implements Serializable {

	private static final long serialVersionUID = -4284533035220901784L;
	
	private String DS_NM_GRUPO_PARADA;
	private String DS_NM_GRUPO_PARADA_SUPERIOR;
	private String nm_grupo;
	private String DS_NM_GRUPO;
	private int cd_parada;
	private String NM_GRUPO_PARADA_SUPERIOR;
	
	public String getDsNmGrupoParada() {
		return DS_NM_GRUPO_PARADA;
	}

	public void setDsNmGrupoParada(String DS_NM_GRUPO_PARADA ) {
		this.DS_NM_GRUPO_PARADA = DS_NM_GRUPO_PARADA;
	}
	
	public String getDsNmGrupoParadaSuperior() {
		return DS_NM_GRUPO_PARADA_SUPERIOR;
	}

	public void setDsNmGrupoParadaSuperior(String DS_NM_GRUPO_PARADA_SUPERIOR ) {
		this.DS_NM_GRUPO_PARADA_SUPERIOR = DS_NM_GRUPO_PARADA_SUPERIOR;
	}
	
	public String getNmGrupoParadaSuperior() {
		return NM_GRUPO_PARADA_SUPERIOR;
	}

	public void setNmGrupoParadaSuperior(String NM_GRUPO_PARADA_SUPERIOR ) {
		this.NM_GRUPO_PARADA_SUPERIOR = NM_GRUPO_PARADA_SUPERIOR;
	}
	
	public String getNmGrupo() {
		return nm_grupo;
	}

	public void setNmGrupo(String nm_grupo ) {
		this.nm_grupo = nm_grupo;
	}
	
	public int getCdParada() {
		return cd_parada;
	}

	public void setCdParada(int cd_parada ) {
		this.cd_parada = cd_parada;
	}
	
	public String getDsNmGrupo() {
		return DS_NM_GRUPO;
	}

	public void setDsNmGrupo(String DS_NM_GRUPO ) {
		this.DS_NM_GRUPO = DS_NM_GRUPO;
	}
	
	public static class Builder {

		private ObjectMapper mapper;
		private GrupoParadaDTO dto;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), GrupoParadaDTO.class);
				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		

		public GrupoParadaDTO build() {
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
		private ResultSetMapper<GrupoParadaDTO> grupoParadasDTO;
		private ArrayList<String> DS_NM_GRUPO_PARADA = new ArrayList();
		private ArrayList<String> DS_NM_GRUPO_PARADA_SUPERIOR = new ArrayList();
		private ArrayList<String> nm_grupo = new ArrayList();
		private ArrayList<Integer> cd_parada = new ArrayList();
		private ArrayList<String> DS_NM_GRUPO = new ArrayList();
		private ArrayList<String> NM_GRUPO_PARADA_SUPERIOR = new ArrayList();
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.grupoParadasDTO = new ResultSetMapper<GrupoParadaDTO>(rsm, GrupoParadaDTO.class);
				this.total = total;
				
				rsm.beforeFirst();
				while(rsm.next()) {
					setDsNmGrupoParada(rsm);
					setDsNmGrupoParadaSuperior(rsm);
					setNmGrupo(rsm);
					setCdParada(rsm);
					setDsNmGrupo(rsm);
					setNmGrupoParadaSuperior(rsm);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public ListBuilder setDsNmGrupoParada(ResultSetMap rsm) throws SQLException {
			this.DS_NM_GRUPO_PARADA.add(rsm.getString("DS_NM_GRUPO_PARADA"));
			return this;
		}
		
		public ListBuilder setDsNmGrupoParadaSuperior(ResultSetMap rsm) throws SQLException {
			this.DS_NM_GRUPO_PARADA_SUPERIOR.add(rsm.getString("DS_NM_GRUPO_PARADA_SUPERIOR"));
			return this;
		}
		
		public ListBuilder setNmGrupoParadaSuperior(ResultSetMap rsm) throws SQLException {
			this.NM_GRUPO_PARADA_SUPERIOR.add(rsm.getString("NM_GRUPO_PARADA_SUPERIOR"));
			return this;
		}
		
		public ListBuilder setCdParada(ResultSetMap rsm) throws SQLException {
			this.cd_parada.add(rsm.getInt("cd_parada"));
			return this;
		}
		
		public ListBuilder setNmGrupo(ResultSetMap rsm) throws SQLException {
			this.nm_grupo.add(rsm.getString("nm_grupo"));
			return this;
		}
		
		public ListBuilder setDsNmGrupo(ResultSetMap rsm) throws SQLException {
			this.DS_NM_GRUPO.add(rsm.getString("DS_NM_GRUPO"));
			return this;
		}
		
		
		public List<GrupoParadaDTO> build() throws IllegalArgumentException, Exception {
			List<GrupoParadaDTO> grupoParadasDTO = this.grupoParadasDTO.toList();
			
			int size = grupoParadasDTO.size();
			for(int i = 0; i < size; i++) {
				GrupoParadaDTO grupoParadaDTO = grupoParadasDTO.get(i);
				if(grupoParadaDTO.getCdGrupoParada() > 0) {
					grupoParadaDTO.setDsNmGrupoParada(DS_NM_GRUPO_PARADA.get(i));
					grupoParadaDTO.setDsNmGrupoParadaSuperior(DS_NM_GRUPO_PARADA_SUPERIOR.get(i));
					grupoParadaDTO.setDsNmGrupo(DS_NM_GRUPO.get(i));
					grupoParadaDTO.setNmGrupo(nm_grupo.get(i));
					grupoParadaDTO.setCdParada(cd_parada.get(i));
					grupoParadaDTO.setNmGrupoParadaSuperior(NM_GRUPO_PARADA_SUPERIOR.get(i));
				}
				
			}
			
			return grupoParadasDTO;
		}
	}
	
}
