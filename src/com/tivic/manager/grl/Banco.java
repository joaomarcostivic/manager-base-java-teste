package com.tivic.manager.grl;

public class Banco {

	private int cdBanco;
	private String nrBanco;
	private String nmBanco;
	private String idBanco;
	private String nmUrl;
	private boolean bancoConveniado;

	public Banco () {}
	
	public Banco(int cdBanco,
			String nrBanco,
			String nmBanco,
			String idBanco,
			String nmUrl
			){
		setCdBanco(cdBanco);
		setNrBanco(nrBanco);
		setNmBanco(nmBanco);
		setIdBanco(idBanco);
		setNmUrl(nmUrl);
	}
	
	public Banco(int cdBanco,
			String nrBanco,
			String nmBanco,
			String idBanco,
			String nmUrl,
			boolean bancoConveniado
			){
		setCdBanco(cdBanco);
		setNrBanco(nrBanco);
		setNmBanco(nmBanco);
		setIdBanco(idBanco);
		setNmUrl(nmUrl);
		setBancoConveniado(bancoConveniado);
	}
	public void setCdBanco(int cdBanco){
		this.cdBanco=cdBanco;
	}
	public int getCdBanco(){
		return this.cdBanco;
	}
	public void setNrBanco(String nrBanco){
		this.nrBanco=nrBanco;
	}
	public String getNrBanco(){
		return this.nrBanco;
	}
	public void setNmBanco(String nmBanco){
		this.nmBanco=nmBanco;
	}
	public String getNmBanco(){
		return this.nmBanco;
	}
	public void setIdBanco(String idBanco){
		this.idBanco=idBanco;
	}
	public String getIdBanco(){
		return this.idBanco;
	}
	public void setNmUrl(String nmUrl){
		this.nmUrl=nmUrl;
	}
	public String getNmUrl(){
		return this.nmUrl;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBanco: " +  getCdBanco();
		valueToString += ", nrBanco: " +  getNrBanco();
		valueToString += ", nmBanco: " +  getNmBanco();
		valueToString += ", idBanco: " +  getIdBanco();
		valueToString += ", nmUrl: " +  getNmUrl();
		return "{" + valueToString + "}";
	}
	
	public Object clone() {
		return new Banco(
			getCdBanco(),
			getNrBanco(),
			getNmBanco(),
			getIdBanco(),
			getNmUrl(),
			getBancoConveniado());
	}


	public boolean getBancoConveniado() {
		return bancoConveniado;
	}

	public void setBancoConveniado(boolean bancoConveniado) {
		this.bancoConveniado = bancoConveniado;
	}

}
