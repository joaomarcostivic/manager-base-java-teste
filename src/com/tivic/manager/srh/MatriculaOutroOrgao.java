package com.tivic.manager.srh;

import java.util.GregorianCalendar;

public class MatriculaOutroOrgao {

	private int cdMatricula;
	private int cdOutroOrgao;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private String nmOrgao;
	private int lgLicencaPremio;
	private int lgContagemTempo;
	private int lgAnuidade;
	private int tpOrgao;

	public MatriculaOutroOrgao(int cdMatricula,
			int cdOutroOrgao,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			String nmOrgao,
			int lgLicencaPremio,
			int lgContagemTempo,
			int lgAnuidade,
			int tpOrgao){
		setCdMatricula(cdMatricula);
		setCdOutroOrgao(cdOutroOrgao);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setNmOrgao(nmOrgao);
		setLgLicencaPremio(lgLicencaPremio);
		setLgContagemTempo(lgContagemTempo);
		setLgAnuidade(lgAnuidade);
		setTpOrgao(tpOrgao);
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdOutroOrgao(int cdOutroOrgao){
		this.cdOutroOrgao=cdOutroOrgao;
	}
	public int getCdOutroOrgao(){
		return this.cdOutroOrgao;
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
	public void setNmOrgao(String nmOrgao){
		this.nmOrgao=nmOrgao;
	}
	public String getNmOrgao(){
		return this.nmOrgao;
	}
	public void setLgLicencaPremio(int lgLicencaPremio){
		this.lgLicencaPremio=lgLicencaPremio;
	}
	public int getLgLicencaPremio(){
		return this.lgLicencaPremio;
	}
	public void setLgContagemTempo(int lgContagemTempo){
		this.lgContagemTempo=lgContagemTempo;
	}
	public int getLgContagemTempo(){
		return this.lgContagemTempo;
	}
	public void setLgAnuidade(int lgAnuidade){
		this.lgAnuidade=lgAnuidade;
	}
	public int getLgAnuidade(){
		return this.lgAnuidade;
	}
	public void setTpOrgao(int tpOrgao){
		this.tpOrgao=tpOrgao;
	}
	public int getTpOrgao(){
		return this.tpOrgao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatricula: " +  getCdMatricula();
		valueToString += ", cdOutroOrgao: " +  getCdOutroOrgao();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nmOrgao: " +  getNmOrgao();
		valueToString += ", lgLicencaPremio: " +  getLgLicencaPremio();
		valueToString += ", lgContagemTempo: " +  getLgContagemTempo();
		valueToString += ", lgAnuidade: " +  getLgAnuidade();
		valueToString += ", tpOrgao: " +  getTpOrgao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MatriculaOutroOrgao(getCdMatricula(),
			getCdOutroOrgao(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getNmOrgao(),
			getLgLicencaPremio(),
			getLgContagemTempo(),
			getLgAnuidade(),
			getTpOrgao());
	}

}