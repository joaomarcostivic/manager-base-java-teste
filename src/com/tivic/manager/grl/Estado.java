package com.tivic.manager.grl;

public class Estado {

	private int cdEstado;
	private int cdPais;
	private String nmEstado;
	private String sgEstado;
	private int cdRegiao;
	private String idEstado;

	public Estado() { }
	
	public Estado(int cdEstado,
			int cdPais,
			String nmEstado,
			String sgEstado,
			int cdRegiao){
		setCdEstado(cdEstado);
		setCdPais(cdPais);
		setNmEstado(nmEstado);
		setSgEstado(sgEstado);
		setCdRegiao(cdRegiao);
	}
	public Estado(int cdEstado,
			int cdPais,
			String nmEstado,
			String sgEstado,
			int cdRegiao,
			String idEstado){
		setCdEstado(cdEstado);
		setCdPais(cdPais);
		setNmEstado(nmEstado);
		setSgEstado(sgEstado);
		setCdRegiao(cdRegiao);
		setIdEstado(idEstado);
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public void setCdPais(int cdPais){
		this.cdPais=cdPais;
	}
	public int getCdPais(){
		return this.cdPais;
	}
	public void setNmEstado(String nmEstado){
		this.nmEstado=nmEstado;
	}
	public String getNmEstado(){
		return this.nmEstado;
	}
	public void setSgEstado(String sgEstado){
		this.sgEstado=sgEstado;
	}
	public String getSgEstado(){
		return this.sgEstado;
	}
	public void setCdRegiao(int cdRegiao){
		this.cdRegiao=cdRegiao;
	}
	public int getCdRegiao(){
		return this.cdRegiao;
	}
	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
	public String getIdEstado() {
		return idEstado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdEstado\": " +  getCdEstado();
		valueToString += ", \"cdPais\": " +  getCdPais();
		valueToString += ", \"nmEstado\": \"" +  getNmEstado() + "\"";
		valueToString += ", \"sgEstado\": \"" +  getSgEstado() + "\"";
		valueToString += ", \"cdRegiao\": " +  getCdRegiao();
		valueToString += ", \"idEstado\": \"" +  getIdEstado() + "\"";
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Estado(getCdEstado(),
			getCdPais(),
			getNmEstado(),
			getSgEstado(),
			getCdRegiao(),
			getIdEstado());
	}

}