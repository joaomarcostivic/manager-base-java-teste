package com.tivic.manager.ptc.protocolosv3;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class CartaoIdoso {

	private String nmPessoa;
	private String nrDocumento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtOcorrencia;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtValidade;
	private String nmEmissor;

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public String getNrDocumento() {
		return nrDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public GregorianCalendar getDtOcorrencia() {
		return dtOcorrencia;
	}

	public void setDtOcorrencia(GregorianCalendar dtOcorrencia) {
		this.dtOcorrencia = dtOcorrencia;
	}

	public GregorianCalendar getDtValidade() {
		return dtValidade;
	}

	public void setDtValidade(GregorianCalendar dtValidade) {
		this.dtValidade = dtValidade;
	}

	public String getNmEmissor() {
		return nmEmissor;
	}

	public void setNmEmissor(String nmEmissor) {
		this.nmEmissor = nmEmissor;
	}
}
