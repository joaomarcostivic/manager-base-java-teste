package com.tivic.manager.mob.ait.sync.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class InfracaoObservacaoSyncDTO {

	private int cdInfracao;
	private int cdObservacao;
	private String nrObservacao;
	private String nmObservacao;
	private String txtObservacao;

	public int getCdInfracao() {
		return cdInfracao;
	}

	public void setCdInfracao(int cdInfracao) {
		this.cdInfracao = cdInfracao;
	}

	public int getCdObservacao() {
		return cdObservacao;
	}

	public void setCdObservacao(int cdObservacao) {
		this.cdObservacao = cdObservacao;
	}

	public String getNrObservacao() {
		return nrObservacao;
	}

	public void setNrObservacao(String nrObservacao) {
		this.nrObservacao = nrObservacao;
	}

	public String getNmObservacao() {
		return nmObservacao;
	}

	public void setNmObservacao(String nmObservacao) {
		this.nmObservacao = nmObservacao;
	}

	public String getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
