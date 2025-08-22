package com.tivic.manager.grl;

public class Pais {

	private int cdPais;
	private String nmPais;
	private String sgPais;
	private int cdRegiao;
	private String idPais;
	
	public Pais() { }
			
	public Pais(int cdPais,
			String nmPais,
			String sgPais,
			int cdRegiao){
		setCdPais(cdPais);
		setNmPais(nmPais);
		setSgPais(sgPais);
		setCdRegiao(cdRegiao);
	}
	public Pais(int cdPais,
			String nmPais,
			String sgPais,
			int cdRegiao,
			String idPais){
		setCdPais(cdPais);
		setNmPais(nmPais);
		setSgPais(sgPais);
		setCdRegiao(cdRegiao);
		setIdPais(idPais);
	}
	public void setCdPais(int cdPais){
		this.cdPais=cdPais;
	}
	public int getCdPais(){
		return this.cdPais;
	}
	public void setNmPais(String nmPais){
		this.nmPais=nmPais;
	}
	public String getNmPais(){
		return this.nmPais;
	}
	public void setSgPais(String sgPais){
		this.sgPais=sgPais;
	}
	public String getSgPais(){
		return this.sgPais;
	}
	public void setCdRegiao(int cdRegiao){
		this.cdRegiao=cdRegiao;
	}
	public int getCdRegiao(){
		return this.cdRegiao;
	}
	public void setIdPais(String idPais) {
		this.idPais = idPais;
	}
	public String getIdPais() {
		return idPais;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPais: " +  getCdPais();
		valueToString += ", nmPais: " +  getNmPais();
		valueToString += ", sgPais: " +  getSgPais();
		valueToString += ", cdRegiao: " +  getCdRegiao();
		valueToString += ", idPais: " +  getIdPais();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Pais(getCdPais(),
			getNmPais(),
			getSgPais(),
			getCdRegiao(),
			getIdPais());
	}

}