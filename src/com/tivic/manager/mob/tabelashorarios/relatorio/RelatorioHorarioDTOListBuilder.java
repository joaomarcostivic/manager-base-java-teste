package com.tivic.manager.mob.tabelashorarios.relatorio;

import java.sql.SQLException;
import java.util.List;

import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.pagination.PagedResponse;

import sol.dao.ResultSetMap;

public class RelatorioHorarioDTOListBuilder {
	private int total;
	private ResultSetMapper<RelatorioHorarioDTO> horarios;
	
	public RelatorioHorarioDTOListBuilder(ResultSetMap rsm, int total) {
		try {
			this.horarios = new ResultSetMapper<RelatorioHorarioDTO>(rsm, RelatorioHorarioDTO.class);
			this.total = total;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public PagedResponse<RelatorioHorarioDTO> build() throws IllegalArgumentException, Exception {
		List<RelatorioHorarioDTO> horarios = this.horarios.toList();
		
		return new PagedResponse<RelatorioHorarioDTO>(horarios, this.total);
	}
}
