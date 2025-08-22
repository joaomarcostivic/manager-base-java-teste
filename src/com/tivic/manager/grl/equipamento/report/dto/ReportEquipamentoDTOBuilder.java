package com.tivic.manager.grl.equipamento.report.dto;

import com.tivic.manager.grl.equipamento.EquipamentoServices;

public class ReportEquipamentoDTOBuilder {

	private ReportEquipamentoDTO reportEquipamentoDTO;
	
	public ReportEquipamentoDTOBuilder() {
	
	}
	
	public ReportEquipamentoDTOBuilder nomeEquipamento(String nmEquipamento) {
		this.reportEquipamentoDTO.setNmEquipamento(nmEquipamento);
		return this;
	}

	public ReportEquipamentoDTOBuilder idEquipamento(String idEquipamento) {
		this.reportEquipamentoDTO.setIdEquipamento(idEquipamento);
		return this;
	}

	public ReportEquipamentoDTOBuilder tipoEquipamento(int tpEquipamento) {
		this.reportEquipamentoDTO.setTipoEquipamento(EquipamentoServices.tiposEquipamento[tpEquipamento]);
		return this;
	}

	public ReportEquipamentoDTOBuilder situacaoEquipamento(int stEquipamento) {
		this.reportEquipamentoDTO.setSituacaoEquipamento(EquipamentoServices.situacoesEquipamento[stEquipamento]);
		return this;
	}
	
	public ReportEquipamentoDTO build() {
		return this.reportEquipamentoDTO;
	}
}
