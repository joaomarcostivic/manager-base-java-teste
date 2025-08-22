package com.tivic.manager.mob.pessoa.dto;

import java.util.GregorianCalendar;

public class AitPortalDTO {
	
	private int cdAit;
	private String nrAit;
	private String nrPlaca;
	private GregorianCalendar dtMovimento;
	private int tpStatus;
	private boolean lgPenalidadeAdvertenciaNip;
	
	private boolean imprimirAndamentoAit;
	private boolean possuiNip;
	private boolean possuiNai;
	private boolean possuiResultadoJulgamento;
	
	private GregorianCalendar dtPrazoDefesa;
	private GregorianCalendar dtVencimento;

	AitPortalDTO() {};
	
	public String getNrAit() {
		return nrAit;
	}
	public void setNrAit(String nrAit) {
		this.nrAit = nrAit;
	}
	public String getNrPlaca() {
		return nrPlaca;
	}
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}
	public int getTpStatus() {
		return tpStatus;
	}
	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
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

	public boolean isImprimirAndamentoAit() {
		return imprimirAndamentoAit;
	}

	public void setImprimirAndamentoAit(boolean imprimitAndamentoAit) {
		this.imprimirAndamentoAit = imprimitAndamentoAit;
	}
	
	public GregorianCalendar getDtPrazoDefesa() {
		return dtPrazoDefesa;
	}

	public void setDtPrazoDefesa(GregorianCalendar dtPrazoDefesa) {
		this.dtPrazoDefesa = dtPrazoDefesa;
	}

	public GregorianCalendar getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(GregorianCalendar dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public boolean isPossuiResultadoJulgamento() {
		return this.possuiResultadoJulgamento;
	}

	public void setPossuiResultadoJulgamento(boolean possuiResultaJulgamento) {
		this.possuiResultadoJulgamento = possuiResultaJulgamento;
	}

	public boolean isLgPenalidadeAdvertenciaNip() {
		return lgPenalidadeAdvertenciaNip;
	}

	public void setLgPenalidadeAdvertenciaNip(boolean lgPenalidadeAdvertenciaNip) {
		this.lgPenalidadeAdvertenciaNip = lgPenalidadeAdvertenciaNip;
	}
	
}
