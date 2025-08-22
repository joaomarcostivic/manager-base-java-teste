package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class Reboque extends com.tivic.manager.bpm.Referencia {

	private int cdReboque;
	private int tpReboque;
	private int tpCarga;
	private String nrCapacidade;
	private int tpEixoDianteiro;
	private int tpEixoTraseiro;
	private int qtEixosDianteiros;
	private int qtEixosTraseiros;

	public Reboque(int cdReferencia,
			int cdBem,
			int cdSetor,
			int cdDocumentoEntrada,
			int cdEmpresa,
			int cdMarca,
			GregorianCalendar dtAquisicao,
			GregorianCalendar dtGarantia,
			GregorianCalendar dtValidade,
			GregorianCalendar dtBaixa,
			String nrSerie,
			String nrTombo,
			int stReferencia,
			String nmModelo,
			GregorianCalendar dtIncorporacao,
			float qtCapacidade,
			int lgProducao,
			String idReferencia,
			int cdLocalArmazenamento,
			int tpReboque,
			int tpCarga,
			String nrCapacidade,
			int tpEixoDianteiro,
			int tpEixoTraseiro,
			int qtEixosDianteiros,
			int qtEixosTraseiros){
		super(cdReferencia,
			cdBem,
			cdSetor,
			cdDocumentoEntrada,
			cdEmpresa,
			cdMarca,
			dtAquisicao,
			dtGarantia,
			dtValidade,
			dtBaixa,
			nrSerie,
			nrTombo,
			stReferencia,
			nmModelo,
			dtIncorporacao,
			qtCapacidade,
			lgProducao,
			idReferencia,
			cdLocalArmazenamento,
			null,//tpReferencia,
			null);//txtVersao);
		setCdReboque(cdReferencia);
		setTpReboque(tpReboque);
		setTpCarga(tpCarga);
		setNrCapacidade(nrCapacidade);
		setTpEixoDianteiro(tpEixoDianteiro);
		setTpEixoTraseiro(tpEixoTraseiro);
		setQtEixosDianteiros(qtEixosDianteiros);
		setQtEixosTraseiros(qtEixosTraseiros);
	}
	public void setCdReboque(int cdReboque){
		this.cdReboque=cdReboque;
	}
	public int getCdReboque(){
		return this.cdReboque;
	}
	public void setTpReboque(int tpReboque){
		this.tpReboque=tpReboque;
	}
	public int getTpReboque(){
		return this.tpReboque;
	}
	public void setTpCarga(int tpCarga){
		this.tpCarga=tpCarga;
	}
	public int getTpCarga(){
		return this.tpCarga;
	}
	public void setNrCapacidade(String nrCapacidade){
		this.nrCapacidade=nrCapacidade;
	}
	public String getNrCapacidade(){
		return this.nrCapacidade;
	}
	public void setTpEixoDianteiro(int tpEixoDianteiro){
		this.tpEixoDianteiro=tpEixoDianteiro;
	}
	public int getTpEixoDianteiro(){
		return this.tpEixoDianteiro;
	}
	public void setTpEixoTraseiro(int tpEixoTraseiro){
		this.tpEixoTraseiro=tpEixoTraseiro;
	}
	public int getTpEixoTraseiro(){
		return this.tpEixoTraseiro;
	}
	public void setQtEixosDianteiros(int qtEixosDianteiros){
		this.qtEixosDianteiros=qtEixosDianteiros;
	}
	public int getQtEixosDianteiros(){
		return this.qtEixosDianteiros;
	}
	public void setQtEixosTraseiros(int qtEixosTraseiros){
		this.qtEixosTraseiros=qtEixosTraseiros;
	}
	public int getQtEixosTraseiros(){
		return this.qtEixosTraseiros;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdReboque: " +  getCdReboque();
		valueToString += ", tpReboque: " +  getTpReboque();
		valueToString += ", tpCarga: " +  getTpCarga();
		valueToString += ", nrCapacidade: " +  getNrCapacidade();
		valueToString += ", tpEixoDianteiro: " +  getTpEixoDianteiro();
		valueToString += ", tpEixoTraseiro: " +  getTpEixoTraseiro();
		valueToString += ", qtEixosDianteiros: " +  getQtEixosDianteiros();
		valueToString += ", qtEixosTraseiros: " +  getQtEixosTraseiros();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Reboque(getCdReferencia(),
			getCdBem(),
			getCdSetor(),
			getCdDocumentoEntrada(),
			getCdEmpresa(),
			getCdMarca(),
			getDtAquisicao(),
			getDtGarantia(),
			getDtValidade(),
			getDtBaixa(),
			getNrSerie(),
			getNrTombo(),
			getStReferencia(),
			getNmModelo(),
			getDtIncorporacao(),
			getQtCapacidade(),
			getLgProducao(),
			getIdReferencia(),
			getCdLocalArmazenamento(),
			getTpReboque(),
			getTpCarga(),
			getNrCapacidade(),
			getTpEixoDianteiro(),
			getTpEixoTraseiro(),
			getQtEixosDianteiros(),
			getQtEixosTraseiros());
	}

}
