package com.tivic.manager.triagem.builders;

import com.tivic.manager.triagem.dtos.EquipamentoTriagemDTO;
import com.tivic.manager.triagem.dtos.OrgaoTriagemDTO;

public class EquipamentoTriagemBuilder {
	private EquipamentoTriagemDTO equipamento;
	
	public EquipamentoTriagemBuilder() throws Exception {
		equipamento = new EquipamentoTriagemDTO();
	}
	
	public EquipamentoTriagemBuilder cdEquipamento(int cdEquipamento) {
		this.equipamento.setCdEquipamento(cdEquipamento);
		return this;
	}
	
	public EquipamentoTriagemBuilder orgaoTriagem(OrgaoTriagemDTO orgaoTriagem) {
		this.equipamento.setOrgao(orgaoTriagem);
		return this;
	}
	
	public EquipamentoTriagemDTO build() {
		return this.equipamento;
	}
}
