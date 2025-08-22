package com.tivic.manager.grl;

public class Logradouro {

	private int cdLogradouro;
	private int cdDistrito;
	private int cdCidade;
	private int cdTipoLogradouro;
	private String nmLogradouro;
	private String idLogradouro;
	private int cdRegiao;

	public Logradouro() { }
			
	public Logradouro(int cdLogradouro,
			int cdDistrito,
			int cdCidade,
			int cdTipoLogradouro,
			String nmLogradouro,
			String idLogradouro){
		setCdLogradouro(cdLogradouro);
		setCdDistrito(cdDistrito);
		setCdCidade(cdCidade);
		setCdTipoLogradouro(cdTipoLogradouro);
		setNmLogradouro(nmLogradouro);
		setIdLogradouro(idLogradouro);
	}
	public Logradouro(int cdLogradouro,
			int cdDistrito,
			int cdCidade,
			int cdTipoLogradouro,
			String nmLogradouro,
			String idLogradouro,
			int cdRegiao){
		setCdLogradouro(cdLogradouro);
		setCdDistrito(cdDistrito);
		setCdCidade(cdCidade);
		setCdTipoLogradouro(cdTipoLogradouro);
		setNmLogradouro(nmLogradouro);
		setIdLogradouro(idLogradouro);
		setCdRegiao(cdRegiao);
	}
	public void setCdLogradouro(int cdLogradouro){
		this.cdLogradouro=cdLogradouro;
	}
	public int getCdLogradouro(){
		return this.cdLogradouro;
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
	public void setCdTipoLogradouro(int cdTipoLogradouro){
		this.cdTipoLogradouro=cdTipoLogradouro;
	}
	public int getCdTipoLogradouro(){
		return this.cdTipoLogradouro;
	}
	public void setNmLogradouro(String nmLogradouro){
		this.nmLogradouro=nmLogradouro;
	}
	public String getNmLogradouro(){
		return this.nmLogradouro;
	}
	public void setIdLogradouro(String idLogradouro){
		this.idLogradouro=idLogradouro;
	}
	public String getIdLogradouro(){
		return this.idLogradouro;
	}
	public void setCdRegiao(int cdRegiao){
		this.cdRegiao=cdRegiao;
	}
	public int getCdRegiao(){
		return this.cdRegiao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLogradouro: " +  getCdLogradouro();
		valueToString += ", cdDistrito: " +  getCdDistrito();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", cdTipoLogradouro: " +  getCdTipoLogradouro();
		valueToString += ", nmLogradouro: " +  getNmLogradouro();
		valueToString += ", idLogradouro: " +  getIdLogradouro();
		valueToString += ", cdRegiao: " +  getCdRegiao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Logradouro(cdLogradouro,
			cdDistrito,
			cdCidade,
			cdTipoLogradouro,
			nmLogradouro,
			idLogradouro, 
			cdRegiao);
	}

}