package com.tivic.manager.grl;

public class Bairro {

	private int cdBairro;
	private int cdDistrito;
	private int cdCidade;
	private String nmBairro;
	private String idBairro;
	private int cdRegiao;

	public Bairro() { }
			
	public Bairro(int cdBairro,
			int cdDistrito,
			int cdCidade,
			String nmBairro,
			String idBairro,
			int cdRegiao){
		setCdBairro(cdBairro);
		setCdDistrito(cdDistrito);
		setCdCidade(cdCidade);
		setNmBairro(nmBairro);
		setIdBairro(idBairro);
		setCdRegiao(cdRegiao);
	}
	public void setCdBairro(int cdBairro){
		this.cdBairro=cdBairro;
	}
	public int getCdBairro(){
		return this.cdBairro;
	}
	public void setCdDistrito(int cdDistrito){
		this.cdDistrito=cdDistrito;
	}
	public int getCdDistrito(){
		return this.cdDistrito;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setNmBairro(String nmBairro){
		this.nmBairro=nmBairro;
	}
	public String getNmBairro(){
		return this.nmBairro;
	}
	public void setIdBairro(String idBairro){
		this.idBairro=idBairro;
	}
	public String getIdBairro(){
		return this.idBairro;
	}
	public void setCdRegiao(int cdRegiao){
		this.cdRegiao=cdRegiao;
	}
	public int getCdRegiao(){
		return this.cdRegiao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBairro: " +  getCdBairro();
		valueToString += ", cdDistrito: " +  getCdDistrito();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", nmBairro: " +  getNmBairro();
		valueToString += ", idBairro: " +  getIdBairro();
		valueToString += ", cdRegiao: " +  getCdRegiao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Bairro(getCdBairro(),
			getCdDistrito(),
			getCdCidade(),
			getNmBairro(),
			getIdBairro(),
			getCdRegiao());
	}

}
