package com.tivic.manager.acd;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class AulaMatriculaDTO extends AulaMatricula implements Serializable   {
	private static final long serialVersionUID = -6338418635355047047L;
	
	private MatriculaDTO matricula;

	public MatriculaDTO getMatricula() {
		return matricula;
	}

	public void setMatricula(MatriculaDTO matricula) {
		this.matricula = matricula;
	}	
	
	/**
	 * Construir List<AulaMatriculaDTO> a partir de um {@link ResultSetMap}	
	 */
	public static class ListBuilder {
		
		private ResultSetMapper<AulaMatriculaDTO> rsmAulaMatricula;
		private List<MatriculaDTO> matriculas;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				
				this.rsmAulaMatricula = new ResultSetMapper<AulaMatriculaDTO>(rsm, AulaMatriculaDTO.class);	
				
				this.matriculas = new ArrayList<MatriculaDTO>();	
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ListBuilder setMatricula(ResultSetMap rsm) {
			try {
				this.matriculas = new MatriculaDTO.ListBuilder(rsm).setAluno(rsm).build();	
				return this;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public List<AulaMatriculaDTO> build() throws IllegalArgumentException, Exception {
			
			List<AulaMatriculaDTO> aulasMatriculas = rsmAulaMatricula.toList();
			
			for(int i = 0; i < aulasMatriculas.size(); i++) {
				AulaMatriculaDTO dto = aulasMatriculas.get(i);
				
				if(i < this.matriculas.size())
					dto.setMatricula(this.matriculas.get(i));
				
			}
			
			return aulasMatriculas;
		}
	}
	
	public static class Builder {
		
		private AulaMatriculaDTO dto;
		private Gson gson;
		
		public Builder(Map<String, Object> map) {
			try {
				gson = new Gson();
				dto = gson.fromJson(Util.map2Json((HashMap<String, Object>)map).toString(), AulaMatriculaDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder setMatricula(Map<String, Object> map) {
			try {
				dto.setMatricula(gson.fromJson(Util.map2Json((HashMap<String, Object>)map).toString(), MatriculaDTO.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public AulaMatriculaDTO build() {
			return dto;
		}
		
	}
	
	
}
