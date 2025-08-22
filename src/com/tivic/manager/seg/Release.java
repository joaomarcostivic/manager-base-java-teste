package com.tivic.manager.seg;

import java.util.GregorianCalendar;

public class Release {

	private int cdRelease;
	private int nrMaior;
	private int nrMenor;
	private String txtDescricao;
	private GregorianCalendar dtRelease;
	private byte[] blbRelease;
	private int lgExecutado;
	private int nrBuild;
	private int cdSistema;

	public Release(){ }

	public Release(int cdRelease,
			int nrMaior,
			int nrMenor,
			String txtDescricao,
			GregorianCalendar dtRelease,
			byte[] blbRelease,
			int lgExecutado,
			int nrBuild,
			int cdSistema){
		setCdRelease(cdRelease);
		setNrMaior(nrMaior);
		setNrMenor(nrMenor);
		setTxtDescricao(txtDescricao);
		setDtRelease(dtRelease);
		setBlbRelease(blbRelease);
		setLgExecutado(lgExecutado);
		setNrBuild(nrBuild);
		setCdSistema(cdSistema);
	}
	public void setCdRelease(int cdRelease){
		this.cdRelease=cdRelease;
	}
	public int getCdRelease(){
		return this.cdRelease;
	}
	public void setNrMaior(int nrMaior){
		this.nrMaior=nrMaior;
	}
	public int getNrMaior(){
		return this.nrMaior;
	}
	public void setNrMenor(int nrMenor){
		this.nrMenor=nrMenor;
	}
	public int getNrMenor(){
		return this.nrMenor;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public void setDtRelease(GregorianCalendar dtRelease){
		this.dtRelease=dtRelease;
	}
	public GregorianCalendar getDtRelease(){
		return this.dtRelease;
	}
	public void setBlbRelease(byte[] blbRelease){
		this.blbRelease=blbRelease;
	}
	public byte[] getBlbRelease(){
		return this.blbRelease;
	}
	public void setLgExecutado(int lgExecutado){
		this.lgExecutado=lgExecutado;
	}
	public int getLgExecutado(){
		return this.lgExecutado;
	}
	public void setNrBuild(int nrBuild){
		this.nrBuild=nrBuild;
	}
	public int getNrBuild(){
		return this.nrBuild;
	}
	public void setCdSistema(int cdSistema){
		this.cdSistema=cdSistema;
	}
	public int getCdSistema(){
		return this.cdSistema;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRelease: " +  getCdRelease();
		valueToString += ", nrMaior: " +  getNrMaior();
		valueToString += ", nrMenor: " +  getNrMenor();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		valueToString += ", dtRelease: " +  sol.util.Util.formatDateTime(getDtRelease(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", blbRelease: " +  getBlbRelease();
		valueToString += ", lgExecutado: " +  getLgExecutado();
		valueToString += ", nrBuild: " +  getNrBuild();
		valueToString += ", cdSistema: " +  getCdSistema();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Release(getCdRelease(),
			getNrMaior(),
			getNrMenor(),
			getTxtDescricao(),
			getDtRelease()==null ? null : (GregorianCalendar)getDtRelease().clone(),
			getBlbRelease(),
			getLgExecutado(),
			getNrBuild(),
			getCdSistema());
	}

}