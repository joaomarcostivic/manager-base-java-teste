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

public class MatriculaDTO extends Matricula implements Serializable  {
	private static final long serialVersionUID = -5908512195060154302L;
	
	private AlunoDTO aluno;

	public AlunoDTO getAluno() {
		return aluno;
	}

	public void setAluno(AlunoDTO aluno) {
		this.aluno = aluno;
	}	
	
	/**
	 * Construir List<MatriculaDTO> a partir de um {@link ResultSetMap}	
	 */
	@Deprecated
	public static class ListBuilder {
		
		private ResultSetMapper<MatriculaDTO> rsmMatricula;
		private List<AlunoDTO> alunos;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.rsmMatricula = new ResultSetMapper<MatriculaDTO>(rsm, MatriculaDTO.class);	
				
				this.alunos = new ArrayList<AlunoDTO>();	
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ListBuilder setAluno(ResultSetMap rsm) {
			try {
//				this.alunos = new AlunoDTO.ListBuilder(rsm).setPessoaFisica(rsm).build();	
				return this;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public List<MatriculaDTO> build() throws IllegalArgumentException, Exception {
			
			List<MatriculaDTO> matriculas = rsmMatricula.toList();
			
			for(int i = 0; i < matriculas.size(); i++) {
				MatriculaDTO dto = matriculas.get(i);
				
				if(i < this.alunos.size())
					dto.setAluno(this.alunos.get(i));
				
			}
			
			return matriculas;
		}
	}
	
	public static class Builder {
		
		private MatriculaDTO dto;
		private Gson gson;
		
		public Builder(Map<String, Object> map) {
			try {
				gson = new Gson();
				dto = gson.fromJson(Util.map2Json((HashMap<String, Object>)map).toString(), MatriculaDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder setAluno(Map<String, Object> map) {
			try {
				dto.setAluno(gson.fromJson(Util.map2Json((HashMap<String, Object>)map).toString(), AlunoDTO.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public MatriculaDTO build() {
			return dto;
		}
		
	}
	
}
