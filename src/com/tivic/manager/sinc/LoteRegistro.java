package com.tivic.manager.sinc;

import java.util.GregorianCalendar;

public class LoteRegistro {

	private int cdLoteRegistro;
	private int cdLote;
	private int cdTabela;
	private String nmValorChaves;
	private String idOrigem;
	private int tpAtualizacao;
	private GregorianCalendar dtAtualizacao;
	private String nmChaveReferencia;
	private String nmCampoAlterado;

	public LoteRegistro(){ }

	public LoteRegistro(int cdLoteRegistro,
			int cdLote,
			int cdTabela,
			String nmValorChaves,
			String idOrigem,
			int tpAtualizacao,
			GregorianCalendar dtAtualizacao,
			String nmChaveReferencia,
			String nmCampoAlterado){
		setCdLoteRegistro(cdLoteRegistro);
		setCdLote(cdLote);
		setCdTabela(cdTabela);
		setNmValorChaves(nmValorChaves);
		setIdOrigem(idOrigem);
		setTpAtualizacao(tpAtualizacao);
		setDtAtualizacao(dtAtualizacao);
		setNmChaveReferencia(nmChaveReferencia);
		setNmCampoAlterado(nmCampoAlterado);
	}
	public void setCdLoteRegistro(int cdLoteRegistro){
		this.cdLoteRegistro=cdLoteRegistro;
	}
	public int getCdLoteRegistro(){
		return this.cdLoteRegistro;
	}
	public void setCdLote(int cdLote){
		this.cdLote=cdLote;
	}
	public int getCdLote(){
		return this.cdLote;
	}
	public void setCdTabela(int cdTabela){
		this.cdTabela=cdTabela;
	}
	public int getCdTabela(){
		return this.cdTabela;
	}
	public void setNmValorChaves(String nmValorChaves){
		this.nmValorChaves=nmValorChaves;
	}
	public String getNmValorChaves(){
		return this.nmValorChaves;
	}
	public void setIdOrigem(String idOrigem){
		this.idOrigem=idOrigem;
	}
	public String getIdOrigem(){
		return this.idOrigem;
	}
	public void setTpAtualizacao(int tpAtualizacao){
		this.tpAtualizacao=tpAtualizacao;
	}
	public int getTpAtualizacao(){
		return this.tpAtualizacao;
	}
	public void setDtAtualizacao(GregorianCalendar dtAtualizacao){
		this.dtAtualizacao=dtAtualizacao;
	}
	public GregorianCalendar getDtAtualizacao(){
		return this.dtAtualizacao;
	}
	public void setNmChaveReferencia(String nmChaveReferencia){
		this.nmChaveReferencia=nmChaveReferencia;
	}
	public String getNmChaveReferencia(){
		return this.nmChaveReferencia;
	}
	public void setNmCampoAlterado(String nmCampoAlterado){
		this.nmCampoAlterado=nmCampoAlterado;
	}
	public String getNmCampoAlterado(){
		return this.nmCampoAlterado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLoteRegistro: " +  getCdLoteRegistro();
		valueToString += ", cdLote: " +  getCdLote();
		valueToString += ", cdTabela: " +  getCdTabela();
		valueToString += ", nmValorChaves: " +  getNmValorChaves();
		valueToString += ", idOrigem: " +  getIdOrigem();
		valueToString += ", tpAtualizacao: " +  getTpAtualizacao();
		valueToString += ", dtAtualizacao: " +  sol.util.Util.formatDateTime(getDtAtualizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nmChaveReferencia: " +  getNmChaveReferencia();
		valueToString += ", nmCampoAlterado: " +  getNmCampoAlterado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LoteRegistro(getCdLoteRegistro(),
			getCdLote(),
			getCdTabela(),
			getNmValorChaves(),
			getIdOrigem(),
			getTpAtualizacao(),
			getDtAtualizacao()==null ? null : (GregorianCalendar)getDtAtualizacao().clone(),
			getNmChaveReferencia(),
			getNmCampoAlterado());
	}

}