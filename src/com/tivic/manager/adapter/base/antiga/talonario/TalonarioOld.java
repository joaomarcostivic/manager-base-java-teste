package com.tivic.manager.adapter.base.antiga.talonario;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TalonarioOld {
	private int codTalao;
	private int nrInicial;
	private int codAgente;
	private int nrFinal;
	private GregorianCalendar dtEntrega;
	private GregorianCalendar dtDevolucao;
	private int stTalao;
	private int nrTalao;
	private int tpTalao;
	private String sgTalao;
	private int nrUltimoAit;

	public TalonarioOld() { }

	public TalonarioOld(int codTalao,
			int nrInicial,
			int codAgente,
			int nrFinal,
			GregorianCalendar dtEntrega,
			GregorianCalendar dtDevolucao,
			int stTalao,
			int nrTalao,
			int tpTalao,
			String sgTalao,
			int nrUltimoAit) {
		setCodTalao(codTalao);
		setNrInicial(nrInicial);
		setCodAgente(codAgente);
		setNrFinal(nrFinal);
		setDtEntrega(dtEntrega);
		setDtDevolucao(dtDevolucao);
		setStTalao(stTalao);
		setNrTalao(nrTalao);
		setTpTalao(tpTalao);
		setSgTalao(sgTalao);
		setNrUltimoAit(nrUltimoAit);
	}
	public void setCodTalao(int codTalao){
		this.codTalao=codTalao;
	}
	public int getCodTalao(){
		return this.codTalao;
	}
	public void setNrInicial(int nrInicial){
		this.nrInicial=nrInicial;
	}
	public int getNrInicial(){
		return this.nrInicial;
	}
	public void setCodAgente(int codAgente){
		this.codAgente=codAgente;
	}
	public int getCodAgente(){
		return this.codAgente;
	}
	public void setNrFinal(int nrFinal){
		this.nrFinal=nrFinal;
	}
	public int getNrFinal(){
		return this.nrFinal;
	}
	public void setDtEntrega(GregorianCalendar dtEntrega){
		this.dtEntrega=dtEntrega;
	}
	public GregorianCalendar getDtEntrega(){
		return this.dtEntrega;
	}
	public void setDtDevolucao(GregorianCalendar dtDevolucao){
		this.dtDevolucao=dtDevolucao;
	}
	public GregorianCalendar getDtDevolucao(){
		return this.dtDevolucao;
	}
	public void setStTalao(int stTalao){
		this.stTalao=stTalao;
	}
	public int getStTalao(){
		return this.stTalao;
	}
	public void setNrTalao(int nrTalao){
		this.nrTalao=nrTalao;
	}
	public int getNrTalao(){
		return this.nrTalao;
	}
	public void setTpTalao(int tpTalao){
		this.tpTalao=tpTalao;
	}
	public int getTpTalao(){
		return this.tpTalao;
	}
	public void setSgTalao(String sgTalao){
		this.sgTalao=sgTalao;
	}
	public String getSgTalao(){
		return this.sgTalao;
	}
	public void setNrUltimoAit(int nrUltimoAit) {
		this.nrUltimoAit = nrUltimoAit;
	}
	public int getNrUltimoAit() {
		return nrUltimoAit;
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

	public Object clone() {
		return new TalonarioOld(getCodTalao(),
			getNrInicial(),
			getCodAgente(),
			getNrFinal(),
			getDtEntrega()==null ? null : (GregorianCalendar)getDtEntrega().clone(),
			getDtDevolucao()==null ? null : (GregorianCalendar)getDtDevolucao().clone(),
			getStTalao(),
			getNrTalao(),
			getTpTalao(),
			getSgTalao(),
			getNrUltimoAit());
	}
}
