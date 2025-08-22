package com.tivic.manager.crt;

public class Produto {

	public static final int PERCENTUAL_EMPRESTADO = 0;
	public static final int PERCENTUAL_SPREAD = 1;
	public static final int PERCENTUAL_DIFERENCA = 1;
	public static final int FIXO_POR_OPERACAO = 2;
	public static final int PERCENTUAL_TAC = 3;
	public static final int PERCENTUAL_OUTRA_COMISSAO = 4;
	public static final int TAC_MENOS_FIXO = 5;

	private int cdProduto;
	private String nmProduto;
	private int cdOrgao;
	private int cdInstituicaoFinanceira;
	private int stProduto;
	private float prMargem;
	private int lgTabelaUnica;

	public Produto(int cdProduto,
			String nmProduto,
			int cdOrgao,
			int cdInstituicaoFinanceira,
			float prMargem,
			int lgTabelaUnica,
			int stProduto){
		setCdProduto(cdProduto);
		setNmProduto(nmProduto);
		setCdOrgao(cdOrgao);
		setCdInstituicaoFinanceira(cdInstituicaoFinanceira);
		setPrMargem(prMargem);
		setLgTabelaUnica(lgTabelaUnica);
		setStProduto(stProduto);
	}
	public void setCdProduto(int cdProduto){
		this.cdProduto=cdProduto;
	}
	public int getCdProduto(){
		return this.cdProduto;
	}
	public void setNmProduto(String nmProduto){
		this.nmProduto=nmProduto;
	}
	public String getNmProduto(){
		return this.nmProduto;
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setCdInstituicaoFinanceira(int cdInstituicaoFinanceira){
		this.cdInstituicaoFinanceira=cdInstituicaoFinanceira;
	}
	public int getCdInstituicaoFinanceira(){
		return this.cdInstituicaoFinanceira;
	}
	public void setPrMargem(float prMargem){
		this.prMargem=prMargem;
	}
	public float getPrMargem(){
		return this.prMargem;
	}
	public void setLgTabelaUnica(int lgTabelaUnica){
		this.lgTabelaUnica=lgTabelaUnica;
	}
	public int getLgTabelaUnica(){
		return this.lgTabelaUnica;
	}
	public void setStProduto(int stProduto){
		this.stProduto=stProduto;
	}
	public int getStProduto(){
		return this.stProduto;
	}
}