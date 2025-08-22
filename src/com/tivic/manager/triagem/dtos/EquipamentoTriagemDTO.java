package com.tivic.manager.triagem.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.equipamento.Equipamento;

public class EquipamentoTriagemDTO extends Equipamento {
	private OrgaoTriagemDTO orgao;

	public OrgaoTriagemDTO getOrgao() {
		return orgao;
	}

	public void setOrgao(OrgaoTriagemDTO orgao) {
		this.orgao = orgao;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
