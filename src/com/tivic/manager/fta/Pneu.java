package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class Pneu {

	private int cdComponentePneu;
	private int cdModelo;
	private int cdPosicao;
	private String nrReferencia;
	private String nrSerie;
	private int tpAquisicao;
	private GregorianCalendar dtInstalacao;
	private int cdMarca;

	public Pneu(int cdComponentePneu,
			int cdModelo,
			int cdPosicao,
			String nrReferencia,
			String nrSerie,
			int tpAquisicao,
			GregorianCalendar dtInstalacao,
			int cdMarca){
		setCdComponentePneu(cdComponentePneu);
		setCdModelo(cdModelo);
		setCdPosicao(cdPosicao);
		setNrReferencia(nrReferencia);
		setNrSerie(nrSerie);
		setTpAquisicao(tpAquisicao);
		setDtInstalacao(dtInstalacao);
		setCdMarca(cdMarca);
	}
	public void setCdComponentePneu(int cdComponentePneu){
		this.cdComponentePneu=cdComponentePneu;
	}
	public int getCdComponentePneu(){
		return this.cdComponentePneu;
	}
	public void setCdModelo(int cdModelo){
		this.cdModelo=cdModelo;
	}
	public int getCdModelo(){
		return this.cdModelo;
	}
	public void setCdPosicao(int cdPosicao){
		this.cdPosicao=cdPosicao;
	}
	public int getCdPosicao(){
		return this.cdPosicao;
	}
	public void setNrReferencia(String nrReferencia){
		this.nrReferencia=nrReferencia;
	}
	public String getNrReferencia(){
		return this.nrReferencia;
	}
	public void setNrSerie(String nrSerie){
		this.nrSerie=nrSerie;
	}
	public String getNrSerie(){
		return this.nrSerie;
	}
	public void setTpAquisicao(int tpAquisicao){
		this.tpAquisicao=tpAquisicao;
	}
	public int getTpAquisicao(){
		return this.tpAquisicao;
	}
	public void setDtInstalacao(GregorianCalendar dtInstalacao){
		this.dtInstalacao=dtInstalacao;
	}
	public GregorianCalendar getDtInstalacao(){
		return this.dtInstalacao;
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdComponentePneu: " +  getCdComponentePneu();
		valueToString += ", cdModelo: " +  getCdModelo();
		valueToString += ", cdPosicao: " +  getCdPosicao();
		valueToString += ", nrReferencia: " +  getNrReferencia();
		valueToString += ", nrSerie: " +  getNrSerie();
		valueToString += ", tpAquisicao: " +  getTpAquisicao();
		valueToString += ", dtInstalacao: " +  sol.util.Util.formatDateTime(getDtInstalacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdMarca: " +  getCdMarca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Pneu(getCdComponentePneu(),
			getCdModelo(),
			getCdPosicao(),
			getNrReferencia(),
			getNrSerie(),
			getTpAquisicao(),
			getDtInstalacao()==null ? null : (GregorianCalendar)getDtInstalacao().clone(),
			getCdMarca());
	}

}