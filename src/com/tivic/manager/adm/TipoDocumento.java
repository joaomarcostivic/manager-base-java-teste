package com.tivic.manager.adm;

public class TipoDocumento {

	private int cdTipoDocumento;
	private String nmTipoDocumento;
	private String sgTipoDocumento;
	private String idTipoDocumento;
	private int stTipoDocumento;
	private int cdFormaTransferencia;
	private String idSped;
	
	public TipoDocumento(){}
	
	public TipoDocumento(int cdTipoDocumento,
			String nmTipoDocumento,
			String sgTipoDocumento,
			String idTipoDocumento,
			int stTipoDocumento,
			int cdFormaTransferencia,
			String idSped){
		setCdTipoDocumento(cdTipoDocumento);
		setNmTipoDocumento(nmTipoDocumento);
		setSgTipoDocumento(sgTipoDocumento);
		setIdTipoDocumento(idTipoDocumento);
		setStTipoDocumento(stTipoDocumento);
		setCdFormaTransferencia(cdFormaTransferencia);
		setIdSped(idSped);
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setNmTipoDocumento(String nmTipoDocumento){
		this.nmTipoDocumento=nmTipoDocumento;
	}
	public String getNmTipoDocumento(){
		return this.nmTipoDocumento;
	}
	public void setSgTipoDocumento(String sgTipoDocumento){
		this.sgTipoDocumento=sgTipoDocumento;
	}
	public String getSgTipoDocumento(){
		return this.sgTipoDocumento;
	}
	public void setIdTipoDocumento(String idTipoDocumento){
		this.idTipoDocumento=idTipoDocumento;
	}
	public String getIdTipoDocumento(){
		return this.idTipoDocumento;
	}
	public void setStTipoDocumento(int stTipoDocumento){
		this.stTipoDocumento=stTipoDocumento;
	}
	public int getStTipoDocumento(){
		return this.stTipoDocumento;
	}
	public void setCdFormaTransferencia(int cdFormaTransferencia){
		this.cdFormaTransferencia=cdFormaTransferencia;
	}
	public int getCdFormaTransferencia(){
		return this.cdFormaTransferencia;
	}
	public void setIdSped(String idSped) {
		this.idSped = idSped;
	}
	public String getIdSped() {
		return idSped;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", nmTipoDocumento: " +  getNmTipoDocumento();
		valueToString += ", sgTipoDocumento: " +  getSgTipoDocumento();
		valueToString += ", idTipoDocumento: " +  getIdTipoDocumento();
		valueToString += ", stTipoDocumento: " +  getStTipoDocumento();
		valueToString += ", cdFormaTransferencia: " +  getCdFormaTransferencia();
		valueToString += ", idSped: " +  getIdSped();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDocumento(getCdTipoDocumento(),
			getNmTipoDocumento(),
			getSgTipoDocumento(),
			getIdTipoDocumento(),
			getStTipoDocumento(),
			getCdFormaTransferencia(),
			getIdSped());
	}

}
