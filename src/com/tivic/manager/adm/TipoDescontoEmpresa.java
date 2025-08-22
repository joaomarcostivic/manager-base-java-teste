package com.tivic.manager.adm;

public class TipoDescontoEmpresa {

	private int cdTipoDesconto;
	private int cdEmpresa;
	private int stTipoDesconto;

	public TipoDescontoEmpresa(int cdTipoDesconto,
			int cdEmpresa,
			int stTipoDesconto){
		setCdTipoDesconto(cdTipoDesconto);
		setCdEmpresa(cdEmpresa);
		setStTipoDesconto(stTipoDesconto);
	}
	public void setCdTipoDesconto(int cdTipoDesconto){
		this.cdTipoDesconto=cdTipoDesconto;
	}
	public int getCdTipoDesconto(){
		return this.cdTipoDesconto;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setStTipoDesconto(int stTipoDesconto){
		this.stTipoDesconto=stTipoDesconto;
	}
	public int getStTipoDesconto(){
		return this.stTipoDesconto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDesconto: " +  getCdTipoDesconto();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", stTipoDesconto: " +  getStTipoDesconto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDescontoEmpresa(getCdTipoDesconto(),
			getCdEmpresa(),
			getStTipoDesconto());
	}

}
