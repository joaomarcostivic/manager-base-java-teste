package com.tivic.manager.grl.equipamento.report.dto;

import java.util.List;

import com.tivic.sol.search.Search;

public class ReportEquipamentoDTOListBuilder {

	private List<ReportEquipamentoDTO> listReportEquipamentoDTO;
	private Search search;
	
	public ReportEquipamentoDTOListBuilder(Search search) {
		this.search = search;
	}
	
	
	public List<ReportEquipamentoDTO> build(){
		while(this.search.getRsm().next()) {
			ReportEquipamentoDTO reportEquipamentoDTO = new ReportEquipamentoDTOBuilder()
					.nomeEquipamento(this.search.getRsm().getString("nm_equipamento"))
					.idEquipamento(this.search.getRsm().getString("id_equipamento"))
					.tipoEquipamento(this.search.getRsm().getInt("tp_equipamento"))
					.situacaoEquipamento(this.search.getRsm().getInt("st_equipamento"))
					.build();
			this.listReportEquipamentoDTO.add(reportEquipamentoDTO);
		}
		return this.listReportEquipamentoDTO;
	}
	
}
