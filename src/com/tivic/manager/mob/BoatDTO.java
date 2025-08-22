package com.tivic.manager.mob;

import java.sql.SQLException;
import java.util.List;

import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.pagination.PagedResponse;

import sol.dao.ResultSetMap;

public class BoatDTO extends Boat {
	
	public static class ListBuilder {

		private int total;
		private ResultSetMapper<BoatDTO> boats;
		
		public ListBuilder(ResultSetMap rsm, int total) {
			try {
				this.boats = new ResultSetMapper<BoatDTO>(rsm, BoatDTO.class);
				this.total = total;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		
		public PagedResponse<BoatDTO> build() throws IllegalArgumentException, Exception {
			List<BoatDTO> boats = this.boats.toList();
			
			int size = boats.size();
//			for(int i = 0; i < size; i++) {
//				TabelaHorarioDTO tabela = tabelas.get(i);
//				
//				if(tabela.getCdLinha() > 0 && this.linha.size() > 0) {
//					tabela.setLinha(this.linha.get(i));
//				}
//				
//				if(tabela.getCdConcessaoVeiculo() > 0 && this.concessaoVeiculo.size() > 0) {
//					tabela.setConcessaoVeiculo(this.concessaoVeiculo.get(i));
//				}
//			}
//			
			return new PagedResponse<BoatDTO>(boats, this.total);
		}
	}

}
