package com.tivic.manager.grl.equipamento.radar.v3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RadarLocalizacaoDTO {
	private int cdEquipamento;
	private String nmEquipamento;
	private int cdVia;
	private String dsLocal;
	private int nrFaixa;

	public RadarLocalizacaoDTO() {}
	
	public int getCdEquipamento() {
		return cdEquipamento;
	}

	public void setCdEquipamento(int cdEquipamento) {
		this.cdEquipamento = cdEquipamento;
	}

	public String getNmEquipamento() {
		return nmEquipamento;
	}

	public void setNmEquipamento(String nmEquipamento) {
		this.nmEquipamento = nmEquipamento;
	}

	public int getCdVia() {
		return cdVia;
	}

	public void setCdVia(int cdVia) {
		this.cdVia = cdVia;
	}

	public String getDsLocal() {
		return dsLocal;
	}

	public void setDsLocal(String dsLocal) {
		this.dsLocal = dsLocal;
	}

	public int getNrFaixa() {
		return nrFaixa;
	}

	public void setNrFaixa(int nrFaixa) {
		this.nrFaixa = nrFaixa;
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
