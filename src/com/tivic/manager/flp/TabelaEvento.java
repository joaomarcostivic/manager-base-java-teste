package com.tivic.manager.flp;

public class TabelaEvento {

	private int cdTabelaEvento;
	private String nmTabelaEvento;
	private String idTabelaEvento;

	public TabelaEvento(){ }

	public TabelaEvento(int cdTabelaEvento,
			String nmTabelaEvento,
			String idTabelaEvento){
		setCdTabelaEvento(cdTabelaEvento);
		setNmTabelaEvento(nmTabelaEvento);
		setIdTabelaEvento(idTabelaEvento);
	}
	public void setCdTabelaEvento(int cdTabelaEvento){
		this.cdTabelaEvento=cdTabelaEvento;
	}
	public int getCdTabelaEvento(){
		return this.cdTabelaEvento;
	}
	public void setNmTabelaEvento(String nmTabelaEvento){
		this.nmTabelaEvento=nmTabelaEvento;
	}
	public String getNmTabelaEvento(){
		return this.nmTabelaEvento;
	}
	public void setIdTabelaEvento(String idTabelaEvento){
		this.idTabelaEvento=idTabelaEvento;
	}
	public String getIdTabelaEvento(){
		return this.idTabelaEvento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaEvento: " +  getCdTabelaEvento();
		valueToString += ", nmTabelaEvento: " +  getNmTabelaEvento();
		valueToString += ", idTabelaEvento: " +  getIdTabelaEvento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaEvento(getCdTabelaEvento(),
			getNmTabelaEvento(),
			getIdTabelaEvento());
	}

}
