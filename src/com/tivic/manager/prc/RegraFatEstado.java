package com.tivic.manager.prc;

public class RegraFatEstado {

	private int cdRegraFaturamento;
	private int cdEstado;

	public RegraFatEstado(){ }

	public RegraFatEstado(int cdRegraFaturamento,
			int cdEstado){
		setCdRegraFaturamento(cdRegraFaturamento);
		setCdEstado(cdEstado);
	}
	public void setCdRegraFaturamento(int cdRegraFaturamento){
		this.cdRegraFaturamento=cdRegraFaturamento;
	}
	public int getCdRegraFaturamento(){
		return this.cdRegraFaturamento;
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegraFaturamento: " +  getCdRegraFaturamento();
		valueToString += ", cdEstado: " +  getCdEstado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RegraFatEstado(getCdRegraFaturamento(),
			getCdEstado());
	}

}
