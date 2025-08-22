package com.tivic.manager.adm;

public class ClassificacaoFiscal {

	private int cdClassificacaoFiscal;
	private int cdClassificacaoFiscalSuperior;
	private String nmClassificacaoFiscal;
	private String idClassificacaoFiscal;
	private String nrClasse;
	private String nrEnquadramento;
	private int tpOrigem;

	public ClassificacaoFiscal(){ }

	public ClassificacaoFiscal(int cdClassificacaoFiscal,
			int cdClassificacaoFiscalSuperior,
			String nmClassificacaoFiscal,
			String idClassificacaoFiscal,
			String nrClasse,
			String nrEnquadramento,
			int tpOrigem){
		setCdClassificacaoFiscal(cdClassificacaoFiscal);
		setCdClassificacaoFiscalSuperior(cdClassificacaoFiscalSuperior);
		setNmClassificacaoFiscal(nmClassificacaoFiscal);
		setIdClassificacaoFiscal(idClassificacaoFiscal);
		setNrClasse(nrClasse);
		setNrEnquadramento(nrEnquadramento);
		setTpOrigem(tpOrigem);
	}
	public void setCdClassificacaoFiscal(int cdClassificacaoFiscal){
		this.cdClassificacaoFiscal=cdClassificacaoFiscal;
	}
	public int getCdClassificacaoFiscal(){
		return this.cdClassificacaoFiscal;
	}
	public void setCdClassificacaoFiscalSuperior(int cdClassificacaoFiscalSuperior){
		this.cdClassificacaoFiscalSuperior=cdClassificacaoFiscalSuperior;
	}
	public int getCdClassificacaoFiscalSuperior(){
		return this.cdClassificacaoFiscalSuperior;
	}
	public void setNmClassificacaoFiscal(String nmClassificacaoFiscal){
		this.nmClassificacaoFiscal=nmClassificacaoFiscal;
	}
	public String getNmClassificacaoFiscal(){
		return this.nmClassificacaoFiscal;
	}
	public void setIdClassificacaoFiscal(String idClassificacaoFiscal){
		this.idClassificacaoFiscal=idClassificacaoFiscal;
	}
	public String getIdClassificacaoFiscal(){
		return this.idClassificacaoFiscal;
	}
	public void setNrClasse(String nrClasse){
		this.nrClasse=nrClasse;
	}
	public String getNrClasse(){
		return this.nrClasse;
	}
	public void setNrEnquadramento(String nrEnquadramento){
		this.nrEnquadramento=nrEnquadramento;
	}
	public String getNrEnquadramento(){
		return this.nrEnquadramento;
	}
	public void setTpOrigem(int tpOrigem){
		this.tpOrigem=tpOrigem;
	}
	public int getTpOrigem(){
		return this.tpOrigem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdClassificacaoFiscal: " +  getCdClassificacaoFiscal();
		valueToString += ", cdClassificacaoFiscalSuperior: " +  getCdClassificacaoFiscalSuperior();
		valueToString += ", nmClassificacaoFiscal: " +  getNmClassificacaoFiscal();
		valueToString += ", idClassificacaoFiscal: " +  getIdClassificacaoFiscal();
		valueToString += ", nrClasse: " +  getNrClasse();
		valueToString += ", nrEnquadramento: " +  getNrEnquadramento();
		valueToString += ", tpOrigem: " +  getTpOrigem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ClassificacaoFiscal(getCdClassificacaoFiscal(),
			getCdClassificacaoFiscalSuperior(),
			getNmClassificacaoFiscal(),
			getIdClassificacaoFiscal(),
			getNrClasse(),
			getNrEnquadramento(),
			getTpOrigem());
	}

}