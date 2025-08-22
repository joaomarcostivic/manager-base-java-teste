package com.tivic.manager.adm;

public class NaturezaOperacao {

	private int cdNaturezaOperacao;
	private String nmNaturezaOperacao;
	private String nrCodigoFiscal;
	private int cdNaturezaSuperior;
	private int cdNaturezaEntrada;
	private int cdNaturezaEntradaSubstituicao;

	public NaturezaOperacao(int cdNaturezaOperacao,
			String nmNaturezaOperacao,
			String nrCodigoFiscal){
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setNmNaturezaOperacao(nmNaturezaOperacao);
		setNrCodigoFiscal(nrCodigoFiscal);
	}
	
	public NaturezaOperacao(int cdNaturezaOperacao,
			String nmNaturezaOperacao,
			String nrCodigoFiscal,
			int cdNaturezaSuperior,
			int cdNaturezaEntrada,
			int cdNaturezaEntradaSubstituicao){
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setNmNaturezaOperacao(nmNaturezaOperacao);
		setNrCodigoFiscal(nrCodigoFiscal);
		setCdNaturezaSuperior(cdNaturezaSuperior);
		setCdNaturezaEntrada(cdNaturezaEntrada);
		setCdNaturezaEntradaSubstituicao(cdNaturezaEntradaSubstituicao);
	}
	public void setCdNaturezaOperacao(int cdNaturezaOperacao){
		this.cdNaturezaOperacao=cdNaturezaOperacao;
	}
	public int getCdNaturezaOperacao(){
		return this.cdNaturezaOperacao;
	}
	public void setNmNaturezaOperacao(String nmNaturezaOperacao){
		this.nmNaturezaOperacao=nmNaturezaOperacao;
	}
	public String getNmNaturezaOperacao(){
		return this.nmNaturezaOperacao;
	}
	public void setNrCodigoFiscal(String nrCodigoFiscal){
		this.nrCodigoFiscal=nrCodigoFiscal;
	}
	public String getNrCodigoFiscal(){
		return this.nrCodigoFiscal;
	}
	public void setCdNaturezaSuperior(int cdNaturezaSuperior){
		this.cdNaturezaSuperior=cdNaturezaSuperior;
	}
	public int getCdNaturezaSuperior(){
		return this.cdNaturezaSuperior;
	}
	public void setCdNaturezaEntrada(int cdNaturezaEntrada){
		this.cdNaturezaEntrada=cdNaturezaEntrada;
	}
	public int getCdNaturezaEntrada(){
		return this.cdNaturezaEntrada;
	}
	public void setCdNaturezaEntradaSubstituicao(int cdNaturezaEntradaSubstituicao){
		this.cdNaturezaEntradaSubstituicao=cdNaturezaEntradaSubstituicao;
	}
	public int getCdNaturezaEntradaSubstituicao(){
		return this.cdNaturezaEntradaSubstituicao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNaturezaOperacao: " +  getCdNaturezaOperacao();
		valueToString += ", nmNaturezaOperacao: " +  getNmNaturezaOperacao();
		valueToString += ", nrCodigoFiscal: " +  getNrCodigoFiscal();
		valueToString += "cdNaturezaSuperior: " +  getCdNaturezaSuperior();
		valueToString += "cdNaturezaEntrada: " +  getCdNaturezaEntrada();
		valueToString += "cdNaturezaEntradaSubstituicao: " +  getCdNaturezaEntradaSubstituicao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NaturezaOperacao(getCdNaturezaOperacao(),
			getNmNaturezaOperacao(),
			getNrCodigoFiscal(),
			getCdNaturezaSuperior(),
			getCdNaturezaEntrada(),
			getCdNaturezaEntradaSubstituicao());
	}

}
