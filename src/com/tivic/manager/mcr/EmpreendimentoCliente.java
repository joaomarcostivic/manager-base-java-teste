package com.tivic.manager.mcr;

public class EmpreendimentoCliente {

	private int cdEmpreendimento;
	private int cdCliente;
	private String nmCliente;
	private float prVendas;

	public EmpreendimentoCliente(int cdEmpreendimento,
			int cdCliente,
			String nmCliente,
			float prVendas){
		setCdEmpreendimento(cdEmpreendimento);
		setCdCliente(cdCliente);
		setNmCliente(nmCliente);
		setPrVendas(prVendas);
	}
	public void setCdEmpreendimento(int cdEmpreendimento){
		this.cdEmpreendimento=cdEmpreendimento;
	}
	public int getCdEmpreendimento(){
		return this.cdEmpreendimento;
	}
	public void setCdCliente(int cdCliente){
		this.cdCliente=cdCliente;
	}
	public int getCdCliente(){
		return this.cdCliente;
	}
	public void setNmCliente(String nmCliente){
		this.nmCliente=nmCliente;
	}
	public String getNmCliente(){
		return this.nmCliente;
	}
	public void setPrVendas(float prVendas){
		this.prVendas=prVendas;
	}
	public float getPrVendas(){
		return this.prVendas;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpreendimento: " +  getCdEmpreendimento();
		valueToString += ", cdCliente: " +  getCdCliente();
		valueToString += ", nmCliente: " +  getNmCliente();
		valueToString += ", prVendas: " +  getPrVendas();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EmpreendimentoCliente(cdEmpreendimento,
			cdCliente,
			nmCliente,
			prVendas);
	}

}