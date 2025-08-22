package com.tivic.manager.mcr;

import java.util.GregorianCalendar;

public class Proposta {

	private int cdProposta;
	private int cdAtaComite;
	private GregorianCalendar dtLiberacao;
	private int nrParcelas;
	private float vlParcelas;

	public Proposta(int cdProposta,
			int cdAtaComite,
			GregorianCalendar dtLiberacao,
			int nrParcelas,
			float vlParcelas){
		setCdProposta(cdProposta);
		setCdAtaComite(cdAtaComite);
		setDtLiberacao(dtLiberacao);
		setNrParcelas(nrParcelas);
		setVlParcelas(vlParcelas);
	}
	public void setCdProposta(int cdProposta){
		this.cdProposta=cdProposta;
	}
	public int getCdProposta(){
		return this.cdProposta;
	}
	public void setCdAtaComite(int cdAtaComite){
		this.cdAtaComite=cdAtaComite;
	}
	public int getCdAtaComite(){
		return this.cdAtaComite;
	}
	public void setDtLiberacao(GregorianCalendar dtLiberacao){
		this.dtLiberacao=dtLiberacao;
	}
	public GregorianCalendar getDtLiberacao(){
		return this.dtLiberacao;
	}
	public void setNrParcelas(int nrParcelas){
		this.nrParcelas=nrParcelas;
	}
	public int getNrParcelas(){
		return this.nrParcelas;
	}
	public void setVlParcelas(float vlParcelas){
		this.vlParcelas=vlParcelas;
	}
	public float getVlParcelas(){
		return this.vlParcelas;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProposta: " +  getCdProposta();
		valueToString += ", cdAtaComite: " +  getCdAtaComite();
		valueToString += ", dtLiberacao: " +  sol.util.Util.formatDateTime(getDtLiberacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrParcelas: " +  getNrParcelas();
		valueToString += ", vlParcelas: " +  getVlParcelas();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Proposta(getCdProposta(),
			getCdAtaComite(),
			getDtLiberacao()==null ? null : (GregorianCalendar)getDtLiberacao().clone(),
			getNrParcelas(),
			getVlParcelas());
	}

}
