package com.tivic.manager.agd;

import java.util.GregorianCalendar;

public class Recorrencia {

	private int cdRecorrencia;
	private GregorianCalendar dtInicio;
	private GregorianCalendar dtTermino;
	private int tpTermino;
	private int nrRecorrencias;
	private int tpRecorrencia;
	private int tpEspecificidadeRecorrencia;
	private int lgDomingo;
	private int lgSegunda;
	private int lgTerca;
	private int lgQuarta;
	private int lgQuinta;
	private int lgSexta;
	private int lgSabado;
	private int qtIntervaloRecorrencia;
	private int nrDiaRecorrenciaMensal;
	private int nrOrdemRecorrenciaMensal;
	private int tpOrdemRecorrenciaMensal;
	private int nrDiaRecorrenciaAnual;
	private int nrMesRecorrenciaAnual;
	private int tpOrdemRecorrenciaAnual;
	private int nrOrdemRecorrenciaAnual;
	private int cdAgenda;

	public Recorrencia(int cdRecorrencia,
			GregorianCalendar dtInicio,
			GregorianCalendar dtTermino,
			int tpTermino,
			int nrRecorrencias,
			int tpRecorrencia,
			int tpEspecificidadeRecorrencia,
			int lgDomingo,
			int lgSegunda,
			int lgTerca,
			int lgQuarta,
			int lgQuinta,
			int lgSexta,
			int lgSabado,
			int qtIntervaloRecorrencia,
			int nrDiaRecorrenciaMensal,
			int nrOrdemRecorrenciaMensal,
			int tpOrdemRecorrenciaMensal,
			int nrDiaRecorrenciaAnual,
			int nrMesRecorrenciaAnual,
			int tpOrdemRecorrenciaAnual,
			int nrOrdemRecorrenciaAnual,
			int cdAgenda){
		setCdRecorrencia(cdRecorrencia);
		setDtInicio(dtInicio);
		setDtTermino(dtTermino);
		setTpTermino(tpTermino);
		setNrRecorrencias(nrRecorrencias);
		setTpRecorrencia(tpRecorrencia);
		setTpEspecificidadeRecorrencia(tpEspecificidadeRecorrencia);
		setLgDomingo(lgDomingo);
		setLgSegunda(lgSegunda);
		setLgTerca(lgTerca);
		setLgQuarta(lgQuarta);
		setLgQuinta(lgQuinta);
		setLgSexta(lgSexta);
		setLgSabado(lgSabado);
		setQtIntervaloRecorrencia(qtIntervaloRecorrencia);
		setNrDiaRecorrenciaMensal(nrDiaRecorrenciaMensal);
		setNrOrdemRecorrenciaMensal(nrOrdemRecorrenciaMensal);
		setTpOrdemRecorrenciaMensal(tpOrdemRecorrenciaMensal);
		setNrDiaRecorrenciaAnual(nrDiaRecorrenciaAnual);
		setNrMesRecorrenciaAnual(nrMesRecorrenciaAnual);
		setTpOrdemRecorrenciaAnual(tpOrdemRecorrenciaAnual);
		setNrOrdemRecorrenciaAnual(nrOrdemRecorrenciaAnual);
		setCdAgenda(cdAgenda);
	}
	public void setCdRecorrencia(int cdRecorrencia){
		this.cdRecorrencia=cdRecorrencia;
	}
	public int getCdRecorrencia(){
		return this.cdRecorrencia;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setDtTermino(GregorianCalendar dtTermino){
		this.dtTermino=dtTermino;
	}
	public GregorianCalendar getDtTermino(){
		return this.dtTermino;
	}
	public void setTpTermino(int tpTermino){
		this.tpTermino=tpTermino;
	}
	public int getTpTermino(){
		return this.tpTermino;
	}
	public void setNrRecorrencias(int nrRecorrencias){
		this.nrRecorrencias=nrRecorrencias;
	}
	public int getNrRecorrencias(){
		return this.nrRecorrencias;
	}
	public void setTpRecorrencia(int tpRecorrencia){
		this.tpRecorrencia=tpRecorrencia;
	}
	public int getTpRecorrencia(){
		return this.tpRecorrencia;
	}
	public void setTpEspecificidadeRecorrencia(int tpEspecificidadeRecorrencia){
		this.tpEspecificidadeRecorrencia=tpEspecificidadeRecorrencia;
	}
	public int getTpEspecificidadeRecorrencia(){
		return this.tpEspecificidadeRecorrencia;
	}
	public void setLgDomingo(int lgDomingo){
		this.lgDomingo=lgDomingo;
	}
	public int getLgDomingo(){
		return this.lgDomingo;
	}
	public void setLgSegunda(int lgSegunda){
		this.lgSegunda=lgSegunda;
	}
	public int getLgSegunda(){
		return this.lgSegunda;
	}
	public void setLgTerca(int lgTerca){
		this.lgTerca=lgTerca;
	}
	public int getLgTerca(){
		return this.lgTerca;
	}
	public void setLgQuarta(int lgQuarta){
		this.lgQuarta=lgQuarta;
	}
	public int getLgQuarta(){
		return this.lgQuarta;
	}
	public void setLgQuinta(int lgQuinta){
		this.lgQuinta=lgQuinta;
	}
	public int getLgQuinta(){
		return this.lgQuinta;
	}
	public void setLgSexta(int lgSexta){
		this.lgSexta=lgSexta;
	}
	public int getLgSexta(){
		return this.lgSexta;
	}
	public void setLgSabado(int lgSabado){
		this.lgSabado=lgSabado;
	}
	public int getLgSabado(){
		return this.lgSabado;
	}
	public void setQtIntervaloRecorrencia(int qtIntervaloRecorrencia){
		this.qtIntervaloRecorrencia=qtIntervaloRecorrencia;
	}
	public int getQtIntervaloRecorrencia(){
		return this.qtIntervaloRecorrencia;
	}
	public void setNrDiaRecorrenciaMensal(int nrDiaRecorrenciaMensal){
		this.nrDiaRecorrenciaMensal=nrDiaRecorrenciaMensal;
	}
	public int getNrDiaRecorrenciaMensal(){
		return this.nrDiaRecorrenciaMensal;
	}
	public void setNrOrdemRecorrenciaMensal(int nrOrdemRecorrenciaMensal){
		this.nrOrdemRecorrenciaMensal=nrOrdemRecorrenciaMensal;
	}
	public int getNrOrdemRecorrenciaMensal(){
		return this.nrOrdemRecorrenciaMensal;
	}
	public void setTpOrdemRecorrenciaMensal(int tpOrdemRecorrenciaMensal){
		this.tpOrdemRecorrenciaMensal=tpOrdemRecorrenciaMensal;
	}
	public int getTpOrdemRecorrenciaMensal(){
		return this.tpOrdemRecorrenciaMensal;
	}
	public void setNrDiaRecorrenciaAnual(int nrDiaRecorrenciaAnual){
		this.nrDiaRecorrenciaAnual=nrDiaRecorrenciaAnual;
	}
	public int getNrDiaRecorrenciaAnual(){
		return this.nrDiaRecorrenciaAnual;
	}
	public void setNrMesRecorrenciaAnual(int nrMesRecorrenciaAnual){
		this.nrMesRecorrenciaAnual=nrMesRecorrenciaAnual;
	}
	public int getNrMesRecorrenciaAnual(){
		return this.nrMesRecorrenciaAnual;
	}
	public void setTpOrdemRecorrenciaAnual(int tpOrdemRecorrenciaAnual){
		this.tpOrdemRecorrenciaAnual=tpOrdemRecorrenciaAnual;
	}
	public int getTpOrdemRecorrenciaAnual(){
		return this.tpOrdemRecorrenciaAnual;
	}
	public void setNrOrdemRecorrenciaAnual(int nrOrdemRecorrenciaAnual){
		this.nrOrdemRecorrenciaAnual=nrOrdemRecorrenciaAnual;
	}
	public int getNrOrdemRecorrenciaAnual(){
		return this.nrOrdemRecorrenciaAnual;
	}
	public void setCdAgenda(int cdAgenda){
		this.cdAgenda=cdAgenda;
	}
	public int getCdAgenda(){
		return this.cdAgenda;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRecorrencia: " +  getCdRecorrencia();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtTermino: " +  sol.util.Util.formatDateTime(getDtTermino(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpTermino: " +  getTpTermino();
		valueToString += ", nrRecorrencias: " +  getNrRecorrencias();
		valueToString += ", tpRecorrencia: " +  getTpRecorrencia();
		valueToString += ", tpEspecificidadeRecorrencia: " +  getTpEspecificidadeRecorrencia();
		valueToString += ", lgDomingo: " +  getLgDomingo();
		valueToString += ", lgSegunda: " +  getLgSegunda();
		valueToString += ", lgTerca: " +  getLgTerca();
		valueToString += ", lgQuarta: " +  getLgQuarta();
		valueToString += ", lgQuinta: " +  getLgQuinta();
		valueToString += ", lgSexta: " +  getLgSexta();
		valueToString += ", lgSabado: " +  getLgSabado();
		valueToString += ", qtIntervaloRecorrencia: " +  getQtIntervaloRecorrencia();
		valueToString += ", nrDiaRecorrenciaMensal: " +  getNrDiaRecorrenciaMensal();
		valueToString += ", nrOrdemRecorrenciaMensal: " +  getNrOrdemRecorrenciaMensal();
		valueToString += ", tpOrdemRecorrenciaMensal: " +  getTpOrdemRecorrenciaMensal();
		valueToString += ", nrDiaRecorrenciaAnual: " +  getNrDiaRecorrenciaAnual();
		valueToString += ", nrMesRecorrenciaAnual: " +  getNrMesRecorrenciaAnual();
		valueToString += ", tpOrdemRecorrenciaAnual: " +  getTpOrdemRecorrenciaAnual();
		valueToString += ", nrOrdemRecorrenciaAnual: " +  getNrOrdemRecorrenciaAnual();
		valueToString += ", cdAgenda: " +  getCdAgenda();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Recorrencia(getCdRecorrencia(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getDtTermino()==null ? null : (GregorianCalendar)getDtTermino().clone(),
			getTpTermino(),
			getNrRecorrencias(),
			getTpRecorrencia(),
			getTpEspecificidadeRecorrencia(),
			getLgDomingo(),
			getLgSegunda(),
			getLgTerca(),
			getLgQuarta(),
			getLgQuinta(),
			getLgSexta(),
			getLgSabado(),
			getQtIntervaloRecorrencia(),
			getNrDiaRecorrenciaMensal(),
			getNrOrdemRecorrenciaMensal(),
			getTpOrdemRecorrenciaMensal(),
			getNrDiaRecorrenciaAnual(),
			getNrMesRecorrenciaAnual(),
			getTpOrdemRecorrenciaAnual(),
			getNrOrdemRecorrenciaAnual(),
			getCdAgenda());
	}

}
