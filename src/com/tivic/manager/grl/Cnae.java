package com.tivic.manager.grl;

public class Cnae {

	private int cdCnae;
	private String nmCnae;
	private String sgCnae;
	private String idCnae;
	private int nrNivel;
	private int cdCnaeSuperior;
	private String nrCnae;
	public Cnae(){}
	public Cnae(int cdCnae,
			String nmCnae,
			String sgCnae,
			String idCnae,
			int nrNivel,
			int cdCnaeSuperior,
			String nrCnae){
		setCdCnae(cdCnae);
		setNmCnae(nmCnae);
		setSgCnae(sgCnae);
		setIdCnae(idCnae);
		setNrNivel(nrNivel);
		setCdCnaeSuperior(cdCnaeSuperior);
		setNrCnae(nrCnae);
	}
	public void setCdCnae(int cdCnae){
		this.cdCnae=cdCnae;
	}
	public int getCdCnae(){
		return this.cdCnae;
	}
	public void setNmCnae(String nmCnae){
		this.nmCnae=nmCnae;
	}
	public String getNmCnae(){
		return this.nmCnae;
	}
	public void setSgCnae(String sgCnae){
		this.sgCnae=sgCnae;
	}
	public String getSgCnae(){
		return this.sgCnae;
	}
	public void setIdCnae(String idCnae){
		this.idCnae=idCnae;
	}
	public String getIdCnae(){
		return this.idCnae;
	}
	public void setNrNivel(int nrNivel){
		this.nrNivel=nrNivel;
	}
	public int getNrNivel(){
		return this.nrNivel;
	}
	public void setCdCnaeSuperior(int cdCnaeSuperior){
		this.cdCnaeSuperior=cdCnaeSuperior;
	}
	public int getCdCnaeSuperior(){
		return this.cdCnaeSuperior;
	}
	public void setNrCnae(String nrCnae){
		this.nrCnae=nrCnae;
	}
	public String getNrCnae(){
		return this.nrCnae;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCnae: " +  getCdCnae();
		valueToString += ", nmCnae: " +  getNmCnae();
		valueToString += ", sgCnae: " +  getSgCnae();
		valueToString += ", idCnae: " +  getIdCnae();
		valueToString += ", nrNivel: " +  getNrNivel();
		valueToString += ", cdCnaeSuperior: " +  getCdCnaeSuperior();
		valueToString += ", nrCnae: " +  getNrCnae();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Cnae(getCdCnae(),
			getNmCnae(),
			getSgCnae(),
			getIdCnae(),
			getNrNivel(),
			getCdCnaeSuperior(),
			getNrCnae());
	}

}
