package com.tivic.manager.ptc.portal.response;

import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.ptc.portal.dtos.AitMovimentoDTO;
import com.tivic.manager.ptc.portal.dtos.DocumentoDTO;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitResponse {

	private int cdAit;
	private int cdUsuario;
	private String idAit;
	private int tpStatus;
	private String nrPlaca;
	private String nrRenavan;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPrazoDefesa;
	private boolean possuiNip;
	private boolean possuiNai;
	private boolean possuiMultaPaga;
	private List<AitMovimentoDTO> movimentos;
	private List<DocumentoDTO> protocolos;

	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
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

	public String getNrRenavan() {
		return nrRenavan;
	}

	public void setNrRenavan(String nrRenavan) {
		this.nrRenavan = nrRenavan;
	}

	public int getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public boolean isPossuiNip() {
		return possuiNip;
	}

	public void setPossuiNip(boolean possuiNip) {
		this.possuiNip = possuiNip;
	}

	public boolean isPossuiNai() {
		return possuiNai;
	}

	public void setPossuiNai(boolean possuiNai) {
		this.possuiNai = possuiNai;
	}
	
	public GregorianCalendar getDtPrazoDefesa() {
		return dtPrazoDefesa;
	}
	public void setDtPrazoDefesa(GregorianCalendar dtPrazoDefesa) {
		this.dtPrazoDefesa = dtPrazoDefesa;
	}

	public boolean isPossuiMultaPaga() {
		return possuiMultaPaga;
	}

	public void setPossuiMultaPaga(boolean possuiMultaPaga) {
		this.possuiMultaPaga = possuiMultaPaga;
	}

	public List<AitMovimentoDTO> getMovimentos() {
		return movimentos;
	}

	public void setMovimentos(List<AitMovimentoDTO> movimentos) {
		this.movimentos = movimentos;
	}

	public List<DocumentoDTO> getProtocolos() {
		return protocolos;
	}

	public void setProtocolos(List<DocumentoDTO> protocolos) {
		this.protocolos = protocolos;
	}	
}