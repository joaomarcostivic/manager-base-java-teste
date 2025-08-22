package com.tivic.manager.mcr;

public class EmpreendimentoBem {

	private int cdEmpreendimento;
	private int cdBem;
	private String nmBem;
	private float vlMercado;
	private int lgDacaoGarantia;

	public EmpreendimentoBem(int cdEmpreendimento,
			int cdBem,
			String nmBem,
			float vlMercado,
			int lgDacaoGarantia){
		setCdEmpreendimento(cdEmpreendimento);
		setCdBem(cdBem);
		setNmBem(nmBem);
		setVlMercado(vlMercado);
		setLgDacaoGarantia(lgDacaoGarantia);
	}
	public void setCdEmpreendimento(int cdEmpreendimento){
		this.cdEmpreendimento=cdEmpreendimento;
	}
	public int getCdEmpreendimento(){
		return this.cdEmpreendimento;
	}
	public void setCdBem(int cdBem){
		this.cdBem=cdBem;
	}
	public int getCdBem(){
		return this.cdBem;
	}
	public void setNmBem(String nmBem){
		this.nmBem=nmBem;
	}
	public String getNmBem(){
		return this.nmBem;
	}
	public void setVlMercado(float vlMercado){
		this.vlMercado=vlMercado;
	}
	public float getVlMercado(){
		return this.vlMercado;
	}
	public void setLgDacaoGarantia(int lgDacaoGarantia){
		this.lgDacaoGarantia=lgDacaoGarantia;
	}
	public int getLgDacaoGarantia(){
		return this.lgDacaoGarantia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpreendimento: " +  getCdEmpreendimento();
		valueToString += ", cdBem: " +  getCdBem();
		valueToString += ", nmBem: " +  getNmBem();
		valueToString += ", vlMercado: " +  getVlMercado();
		valueToString += ", lgDacaoGarantia: " +  getLgDacaoGarantia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EmpreendimentoBem(cdEmpreendimento,
			cdBem,
			nmBem,
			vlMercado,
			lgDacaoGarantia);
	}

}