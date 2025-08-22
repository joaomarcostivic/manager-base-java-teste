package com.tivic.manager.mob.lotes.dto.dividaativa;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DividaAtivaRetornoDTO {

	private GregorianCalendar dtEnvio;
	private String idDivida;
	private String idAit;
	private String txtObservacao;
	private int idInsercao;

	public GregorianCalendar getDtEnvio() {
		return dtEnvio;
	}

	public void setDtEnvio(GregorianCalendar dtEnvio) {
		this.dtEnvio = dtEnvio;
	}

	public String getIdDivida() {
		return idDivida;
	}

	public void setIdDivida(String idDivida) {
		this.idDivida = idDivida;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public String getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public int getIdInsercao() {
		return idInsercao;
	}

	public void setIdInsercao(int idInsercao) {
		this.idInsercao = idInsercao;
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
