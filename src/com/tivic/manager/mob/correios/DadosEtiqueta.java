package com.tivic.manager.mob.correios;

import java.util.GregorianCalendar;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class DadosEtiqueta {
	private Integer cdEtiqueta;
	private int cdLote;
	private Integer nrEtiqueta;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtEnvio;
	private String sgServico;
	private int cdAit;
	private int tpStatus;
	private int nrMovimento;
	
	public DadosEtiqueta(Integer cdEtiqueta, int cdLote, Integer nrEtiqueta, GregorianCalendar dtEnvio, String sgServico, int cdAit, int tpStatus, int nrMovimento) {
		this.cdEtiqueta = cdEtiqueta;
		this.cdLote = cdLote;
		this.nrEtiqueta = nrEtiqueta;
		this.dtEnvio = dtEnvio;
		this.sgServico = sgServico; 
		this.cdAit = cdAit;
		this.tpStatus = tpStatus;
		this.nrMovimento = nrMovimento;
	}

	public Integer getCdEtiqueta() {
		return cdEtiqueta;
	}

	public void setCdEtiqueta(Integer cdEtiqueta) {
		this.cdEtiqueta = cdEtiqueta;
	}

	public int getCdLote() {
		return cdLote;
	}

	public void setCdLote(int cdLote) {
		this.cdLote = cdLote;
	}

	public Integer getNrEtiqueta() {
		return nrEtiqueta;
	}

	public void setNrEtiqueta(Integer nrEtiqueta) {
		this.nrEtiqueta = nrEtiqueta;
	}

	public GregorianCalendar getDtEnvio() {
		return dtEnvio;
	}

	public void setDtEnvio(GregorianCalendar dtEnvio) {
		this.dtEnvio = dtEnvio;
	}

	public String getSgServico() {
		return sgServico;
	}

	public void setSgServico(String sgServico) {
		this.sgServico = sgServico;
	}

	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public int getNrMovimento() {
		return nrMovimento;
	}

	public void setNrMovimento(int nrMovimento) {
		this.nrMovimento = nrMovimento;
	}
	
}
