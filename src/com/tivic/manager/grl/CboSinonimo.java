package com.tivic.manager.grl;

public class CboSinonimo {

	private int cdCboSinonimo;
	private int cdCbo;
	private String nmCboSinonimo;
	private String idCboSinonimo;

	public CboSinonimo(int cdCboSinonimo,
			int cdCbo,
			String nmCboSinonimo,
			String idCboSinonimo){
		setCdCboSinonimo(cdCboSinonimo);
		setCdCbo(cdCbo);
		setNmCboSinonimo(nmCboSinonimo);
		setIdCboSinonimo(idCboSinonimo);
	}
	public void setCdCboSinonimo(int cdCboSinonimo){
		this.cdCboSinonimo=cdCboSinonimo;
	}
	public int getCdCboSinonimo(){
		return this.cdCboSinonimo;
	}
	public void setCdCbo(int cdCbo){
		this.cdCbo=cdCbo;
	}
	public int getCdCbo(){
		return this.cdCbo;
	}
	public void setNmCboSinonimo(String nmCboSinonimo){
		this.nmCboSinonimo=nmCboSinonimo;
	}
	public String getNmCboSinonimo(){
		return this.nmCboSinonimo;
	}
	public void setIdCboSinonimo(String idCboSinonimo){
		this.idCboSinonimo=idCboSinonimo;
	}
	public String getIdCboSinonimo(){
		return this.idCboSinonimo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCboSinonimo: " +  getCdCboSinonimo();
		valueToString += ", cdCbo: " +  getCdCbo();
		valueToString += ", nmCboSinonimo: " +  getNmCboSinonimo();
		valueToString += ", idCboSinonimo: " +  getIdCboSinonimo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CboSinonimo(getCdCboSinonimo(),
			getCdCbo(),
			getNmCboSinonimo(),
			getIdCboSinonimo());
	}

}
