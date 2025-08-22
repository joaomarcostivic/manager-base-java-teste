package com.tivic.manager.mob.ait.relatorios;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class RelatorioAitSneDTO {

	int cdAit;
	int tpStatus;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	GregorianCalendar dtMovimento;
	String idAit;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	GregorianCalendar dtDigitacao;
	String nrPlaca;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	GregorianCalendar dtInfracao;
	String nmAgente;
	int nrCodDetran;
	int stSne;
	Double totalMultas;
	String dsTpStatus;
	String dsTpMovimento;
	Double vlMulta;
	int ctMovimento;

	public RelatorioAitSneDTO() {
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

	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}

	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}

	public GregorianCalendar getDtDigitacao() {
		return dtDigitacao;
	}

	public void setDtDigitacao(GregorianCalendar dtDigitacao) {
		this.dtDigitacao = dtDigitacao;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}

	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}

	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}

	public String getNmAgente() {
		return nmAgente;
	}

	public void setNmAgente(String nmAgente) {
		this.nmAgente = nmAgente;
	}

	public int getNrCodDetran() {
		return nrCodDetran;
	}

	public void setNrCodDetran(int nrCodDetran) {
		this.nrCodDetran = nrCodDetran;
	}

	
	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	
	public int getStSne() {
		return this.stSne;
	}

	public void setStSne(int stSne) {
		this.stSne = stSne;
	}

	public Double getTotalMultas() {
		return this.totalMultas;
	}

	public void setTotalMultas(Double totalMultas) {
		this.totalMultas = totalMultas;
	}
	
	public String getDsTpStatus() {
		return this.dsTpStatus;
	}

	public void setDsTpStatus(String dsTpStatus) {
		this.dsTpStatus = dsTpStatus;
	}
	
	public String getDsTpMovimento() {
		return this.dsTpMovimento;
	}

	public void setDsTpMovimento(String dsTpMovimento) {
		this.dsTpMovimento = dsTpMovimento;
	}
	
	public Double getVlMulta() {
		return this.vlMulta;
	}

	public void setVlMulta(Double vlMulta) {
		this.vlMulta = vlMulta;
	}
	
	public int getCtMovimento() {
		return this.ctMovimento;
	}

	public void setCtMovimento(int ctMovimento) {
		this.ctMovimento = ctMovimento;
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
