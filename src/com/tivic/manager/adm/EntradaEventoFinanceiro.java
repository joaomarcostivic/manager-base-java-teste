package com.tivic.manager.adm;

public class EntradaEventoFinanceiro {

	private int cdDocumentoEntrada;
	private int cdEventoFinanceiro;
	private float vlEventoFinanceiro;
	private int lgCusto;
	private int lgFiscal;
	private int lgDespesaAcessoria;

	public EntradaEventoFinanceiro(){ }

	public EntradaEventoFinanceiro(int cdDocumentoEntrada,
			int cdEventoFinanceiro,
			float vlEventoFinanceiro,
			int lgCusto,
			int lgFiscal,
			int lgDespesaAcessoria){
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setVlEventoFinanceiro(vlEventoFinanceiro);
		setLgCusto(lgCusto);
		setLgFiscal(lgFiscal);
		setLgDespesaAcessoria(lgDespesaAcessoria);
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setVlEventoFinanceiro(float vlEventoFinanceiro){
		this.vlEventoFinanceiro=vlEventoFinanceiro;
	}
	public float getVlEventoFinanceiro(){
		return this.vlEventoFinanceiro;
	}
	public void setLgCusto(int lgCusto){
		this.lgCusto=lgCusto;
	}
	public int getLgCusto(){
		return this.lgCusto;
	}
	public void setLgFiscal(int lgFiscal){
		this.lgFiscal=lgFiscal;
	}
	public int getLgFiscal(){
		return this.lgFiscal;
	}
	public void setLgDespesaAcessoria(int lgDespesaAcessoria){
		this.lgDespesaAcessoria=lgDespesaAcessoria;
	}
	public int getLgDespesaAcessoria(){
		return this.lgDespesaAcessoria;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", vlEventoFinanceiro: " +  getVlEventoFinanceiro();
		valueToString += ", lgCusto: " +  getLgCusto();
		valueToString += ", lgFiscal: " +  getLgFiscal();
		valueToString += ", lgDespesaAcessoria: " +  getLgDespesaAcessoria();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EntradaEventoFinanceiro(getCdDocumentoEntrada(),
			getCdEventoFinanceiro(),
			getVlEventoFinanceiro(),
			getLgCusto(),
			getLgFiscal(),
			getLgDespesaAcessoria());
	}

}