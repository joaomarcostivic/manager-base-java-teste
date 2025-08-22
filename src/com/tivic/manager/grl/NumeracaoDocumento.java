package com.tivic.manager.grl;

public class NumeracaoDocumento {

	private int cdNumeracaoDocumento;
	private String nmTipoDocumento;
	private int cdUltimoNumero;
	private int nrAno;
	private int cdEmpresa;

	public NumeracaoDocumento(int cdNumeracaoDocumento,
			String nmTipoDocumento,
			int cdUltimoNumero,
			int nrAno,
			int cdEmpresa){
		setCdNumeracaoDocumento(cdNumeracaoDocumento);
		setNmTipoDocumento(nmTipoDocumento);
		setCdUltimoNumero(cdUltimoNumero);
		setNrAno(nrAno);
		setCdEmpresa(cdEmpresa);
	}
	public void setCdNumeracaoDocumento(int cdNumeracaoDocumento){
		this.cdNumeracaoDocumento=cdNumeracaoDocumento;
	}
	public int getCdNumeracaoDocumento(){
		return this.cdNumeracaoDocumento;
	}
	public void setNmTipoDocumento(String nmTipoDocumento){
		this.nmTipoDocumento=nmTipoDocumento;
	}
	public String getNmTipoDocumento(){
		return this.nmTipoDocumento;
	}
	public void setCdUltimoNumero(int cdUltimoNumero){
		this.cdUltimoNumero=cdUltimoNumero;
	}
	public int getCdUltimoNumero(){
		return this.cdUltimoNumero;
	}
	public void setNrAno(int nrAno){
		this.nrAno=nrAno;
	}
	public int getNrAno(){
		return this.nrAno;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNumeracaoDocumento: " +  getCdNumeracaoDocumento();
		valueToString += ", nmTipoDocumento: " +  getNmTipoDocumento();
		valueToString += ", cdUltimoNumero: " +  getCdUltimoNumero();
		valueToString += ", nrAno: " +  getNrAno();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NumeracaoDocumento(getCdNumeracaoDocumento(),
			getNmTipoDocumento(),
			getCdUltimoNumero(),
			getNrAno(),
			getCdEmpresa());
	}

}
