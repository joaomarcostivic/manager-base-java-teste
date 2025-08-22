package com.tivic.manager.acd;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.tivic.manager.acd.Aluno;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class AlunoDTO extends Aluno implements Serializable {
	private static final long serialVersionUID = -2161900953506898761L;
	
	/**
	 * Construir List<AlunoDTO> a partir de um {@link ResultSetMap}	
	 */
	public static class ListBuilder {
		
		private ResultSetMapper<AlunoDTO> rsmAluno;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.rsmAluno = new ResultSetMapper<AlunoDTO>(rsm, AlunoDTO.class);	
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public List<AlunoDTO> build() throws IllegalArgumentException, Exception {
			
			List<AlunoDTO> alunos = rsmAluno.toList();
			
			for(int i = 0; i < alunos.size(); i++) {
				AlunoDTO dto = alunos.get(i);
				
			}
			return alunos;
		}
	}
	
	public static class Builder {
		
		private AlunoDTO dto;
		private Gson gson;
		
		public Builder(Map<String, Object> map) {
			try {
				gson = new Gson();
				dto = gson.fromJson(Util.map2Json((HashMap<String, Object>)map).toString(), AlunoDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public AlunoDTO build() {
			return dto;
		}
		
	}
	
}
