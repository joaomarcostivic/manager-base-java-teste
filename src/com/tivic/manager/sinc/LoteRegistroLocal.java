package com.tivic.manager.sinc;

import java.util.GregorianCalendar;

public class LoteRegistroLocal {

	private int cdLoteRegistro;
	private int cdLocal;
	private GregorianCalendar dtSincronizacao;
	private String nmChaveLocal;

	public LoteRegistroLocal(){ }

	public LoteRegistroLocal(int cdLoteRegistro,
			int cdLocal,
			GregorianCalendar dtSincronizacao,
			String nmChaveLocal){
		setCdLoteRegistro(cdLoteRegistro);
		setCdLocal(cdLocal);
		setDtSincronizacao(dtSincronizacao);
		setNmChaveLocal(nmChaveLocal);
	}
	public void setCdLoteRegistro(int cdLoteRegistro){
		this.cdLoteRegistro=cdLoteRegistro;
	}
	public int getCdLoteRegistro(){
		return this.cdLoteRegistro;
	}
	public void setCdLocal(int cdLocal){
		this.cdLocal=cdLocal;
	}
	public int getCdLocal(){
		return this.cdLocal;
	}
	public void setDtSincronizacao(GregorianCalendar dtSincronizacao){
		this.dtSincronizacao=dtSincronizacao;
	}
	public GregorianCalendar getDtSincronizacao(){
		return this.dtSincronizacao;
	}
	public void setNmChaveLocal(String nmChaveLocal){
		this.nmChaveLocal=nmChaveLocal;
	}
	public String getNmChaveLocal(){
		return this.nmChaveLocal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLoteRegistro: " +  getCdLoteRegistro();
		valueToString += ", cdLocal: " +  getCdLocal();
		valueToString += ", dtSincronizacao: " +  sol.util.Util.formatDateTime(getDtSincronizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nmChaveLocal: " +  getNmChaveLocal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LoteRegistroLocal(getCdLoteRegistro(),
			getCdLocal(),
			getDtSincronizacao()==null ? null : (GregorianCalendar)getDtSincronizacao().clone(),
			getNmChaveLocal());
	}

}