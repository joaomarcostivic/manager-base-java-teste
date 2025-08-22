package com.tivic.manager.evt;

import java.util.GregorianCalendar;

public class Evento {

	private int cdEvento;
	private String nmEvento;
	private String dsEvento;
	private int qtVagas;
	private GregorianCalendar dtEvento;
	private int tpEvento;
	private String idEvento;
	private int cdLocal;
	private int cdFacilitador;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private GregorianCalendar dtVencimentoBoleto;
	private int cdConta;
	private int cdContaCarteira;
	private int cdArquivo;
	private int cdEventoPrincipal;
	private float vlInscricao;
	private String nmUrlEdital;

	public Evento(){ }

	public Evento(int cdEvento,
			String nmEvento,
			String dsEvento,
			int qtVagas,
			GregorianCalendar dtEvento,
			int tpEvento,
			String idEvento,
			int cdLocal,
			int cdFacilitador,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			GregorianCalendar dtVencimentoBoleto,
			int cdConta,
			int cdContaCarteira,
			int cdArquivo,
			int cdEventoPrincipal,
			float vlInscricao,
			String nmUrlEdital){
		setCdEvento(cdEvento);
		setNmEvento(nmEvento);
		setDsEvento(dsEvento);
		setQtVagas(qtVagas);
		setDtEvento(dtEvento);
		setTpEvento(tpEvento);
		setIdEvento(idEvento);
		setCdLocal(cdLocal);
		setCdFacilitador(cdFacilitador);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setDtVencimentoBoleto(dtVencimentoBoleto);
		setCdConta(cdConta);
		setCdContaCarteira(cdContaCarteira);
		setCdArquivo(cdArquivo);
		setCdEventoPrincipal(cdEventoPrincipal);
		setVlInscricao(vlInscricao);
		setNmUrlEdital(nmUrlEdital);
	}
	public void setCdEvento(int cdEvento){
		this.cdEvento=cdEvento;
	}
	public int getCdEvento(){
		return this.cdEvento;
	}
	public void setNmEvento(String nmEvento){
		this.nmEvento=nmEvento;
	}
	public String getNmEvento(){
		return this.nmEvento;
	}
	public void setDsEvento(String dsEvento){
		this.dsEvento=dsEvento;
	}
	public String getDsEvento(){
		return this.dsEvento;
	}
	public void setQtVagas(int qtVagas){
		this.qtVagas=qtVagas;
	}
	public int getQtVagas(){
		return this.qtVagas;
	}
	public void setDtEvento(GregorianCalendar dtEvento){
		this.dtEvento=dtEvento;
	}
	public GregorianCalendar getDtEvento(){
		return this.dtEvento;
	}
	public void setTpEvento(int tpEvento){
		this.tpEvento=tpEvento;
	}
	public int getTpEvento(){
		return this.tpEvento;
	}
	public void setIdEvento(String idEvento){
		this.idEvento=idEvento;
	}
	public String getIdEvento(){
		return this.idEvento;
	}
	public void setCdLocal(int cdLocal){
		this.cdLocal=cdLocal;
	}
	public int getCdLocal(){
		return this.cdLocal;
	}
	public void setCdFacilitador(int cdFacilitador){
		this.cdFacilitador=cdFacilitador;
	}
	public int getCdFacilitador(){
		return this.cdFacilitador;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setDtVencimentoBoleto(GregorianCalendar dtVencimentoBoleto){
		this.dtVencimentoBoleto=dtVencimentoBoleto;
	}
	public GregorianCalendar getDtVencimentoBoleto(){
		return this.dtVencimentoBoleto;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdContaCarteira(int cdContaCarteira){
		this.cdContaCarteira=cdContaCarteira;
	}
	public int getCdContaCarteira(){
		return this.cdContaCarteira;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdEventoPrincipal(int cdEventoPrincipal){
		this.cdEventoPrincipal=cdEventoPrincipal;
	}
	public int getCdEventoPrincipal(){
		return this.cdEventoPrincipal;
	}
	public void setVlInscricao(float vlInscricao){
		this.vlInscricao=vlInscricao;
	}
	public float getVlInscricao(){
		return this.vlInscricao;
	}
	public void setNmUrlEdital(String nmUrlEdital){
		this.nmUrlEdital=nmUrlEdital;
	}
	public String getNmUrlEdital(){
		return this.nmUrlEdital;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEvento: " +  getCdEvento();
		valueToString += ", nmEvento: " +  getNmEvento();
		valueToString += ", dsEvento: " +  getDsEvento();
		valueToString += ", qtVagas: " +  getQtVagas();
		valueToString += ", dtEvento: " +  sol.util.Util.formatDateTime(getDtEvento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpEvento: " +  getTpEvento();
		valueToString += ", idEvento: " +  getIdEvento();
		valueToString += ", cdLocal: " +  getCdLocal();
		valueToString += ", cdFacilitador: " +  getCdFacilitador();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtVencimentoBoleto: " +  sol.util.Util.formatDateTime(getDtVencimentoBoleto(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", cdContaCarteira: " +  getCdContaCarteira();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", cdEventoPrincipal: " +  getCdEventoPrincipal();
		valueToString += ", vlInscricao: " +  getVlInscricao();
		valueToString += ", nmUrlEdital: " +  getNmUrlEdital();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Evento(getCdEvento(),
			getNmEvento(),
			getDsEvento(),
			getQtVagas(),
			getDtEvento()==null ? null : (GregorianCalendar)getDtEvento().clone(),
			getTpEvento(),
			getIdEvento(),
			getCdLocal(),
			getCdFacilitador(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getDtVencimentoBoleto()==null ? null : (GregorianCalendar)getDtVencimentoBoleto().clone(),
			getCdConta(),
			getCdContaCarteira(),
			getCdArquivo(),
			getCdEventoPrincipal(),
			getVlInscricao(),
			getNmUrlEdital());
	}

}