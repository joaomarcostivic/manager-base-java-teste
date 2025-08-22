package com.tivic.manager.mob.lotes.model.banco;


public class Banco {
	private int cdBanco;
	private String nrBanco;
	private String nmBanco;
	private String idBanco;
	private String nmUrl;
	private boolean bancoConveniado;

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

	public boolean getBancoConveniado() {
		return bancoConveniado;
	}

	public void setBancoConveniado(boolean bancoConveniado) {
		this.bancoConveniado = bancoConveniado;
	}

}
