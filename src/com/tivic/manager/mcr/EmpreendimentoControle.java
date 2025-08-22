package com.tivic.manager.mcr;

public class EmpreendimentoControle {

	private int cdEmpreendimento;
	private int lgControleFormal;
	private int lgLivroCaixa;
	private int lgControleEstoque;
	private int lgContaReceber;
	private int lgReceitaDespesa;
	private int lgContaPagar;
	private int lgOutroControle;
	private String nmOutroControle;
	private String txtSistemaOrganizacao;

	public EmpreendimentoControle(int cdEmpreendimento,
			int lgControleFormal,
			int lgLivroCaixa,
			int lgControleEstoque,
			int lgContaReceber,
			int lgReceitaDespesa,
			int lgContaPagar,
			int lgOutroControle,
			String nmOutroControle,
			String txtSistemaOrganizacao){
		setCdEmpreendimento(cdEmpreendimento);
		setLgControleFormal(lgControleFormal);
		setLgLivroCaixa(lgLivroCaixa);
		setLgControleEstoque(lgControleEstoque);
		setLgContaReceber(lgContaReceber);
		setLgReceitaDespesa(lgReceitaDespesa);
		setLgContaPagar(lgContaPagar);
		setLgOutroControle(lgOutroControle);
		setNmOutroControle(nmOutroControle);
		setTxtSistemaOrganizacao(txtSistemaOrganizacao);
	}
	public void setCdEmpreendimento(int cdEmpreendimento){
		this.cdEmpreendimento=cdEmpreendimento;
	}
	public int getCdEmpreendimento(){
		return this.cdEmpreendimento;
	}
	public void setLgControleFormal(int lgControleFormal){
		this.lgControleFormal=lgControleFormal;
	}
	public int getLgControleFormal(){
		return this.lgControleFormal;
	}
	public void setLgLivroCaixa(int lgLivroCaixa){
		this.lgLivroCaixa=lgLivroCaixa;
	}
	public int getLgLivroCaixa(){
		return this.lgLivroCaixa;
	}
	public void setLgControleEstoque(int lgControleEstoque){
		this.lgControleEstoque=lgControleEstoque;
	}
	public int getLgControleEstoque(){
		return this.lgControleEstoque;
	}
	public void setLgContaReceber(int lgContaReceber){
		this.lgContaReceber=lgContaReceber;
	}
	public int getLgContaReceber(){
		return this.lgContaReceber;
	}
	public void setLgReceitaDespesa(int lgReceitaDespesa){
		this.lgReceitaDespesa=lgReceitaDespesa;
	}
	public int getLgReceitaDespesa(){
		return this.lgReceitaDespesa;
	}
	public void setLgContaPagar(int lgContaPagar){
		this.lgContaPagar=lgContaPagar;
	}
	public int getLgContaPagar(){
		return this.lgContaPagar;
	}
	public void setLgOutroControle(int lgOutroControle){
		this.lgOutroControle=lgOutroControle;
	}
	public int getLgOutroControle(){
		return this.lgOutroControle;
	}
	public void setNmOutroControle(String nmOutroControle){
		this.nmOutroControle=nmOutroControle;
	}
	public String getNmOutroControle(){
		return this.nmOutroControle;
	}
	public void setTxtSistemaOrganizacao(String txtSistemaOrganizacao){
		this.txtSistemaOrganizacao=txtSistemaOrganizacao;
	}
	public String getTxtSistemaOrganizacao(){
		return this.txtSistemaOrganizacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpreendimento: " +  getCdEmpreendimento();
		valueToString += ", lgControleFormal: " +  getLgControleFormal();
		valueToString += ", lgLivroCaixa: " +  getLgLivroCaixa();
		valueToString += ", lgControleEstoque: " +  getLgControleEstoque();
		valueToString += ", lgContaReceber: " +  getLgContaReceber();
		valueToString += ", lgReceitaDespesa: " +  getLgReceitaDespesa();
		valueToString += ", lgContaPagar: " +  getLgContaPagar();
		valueToString += ", lgOutroControle: " +  getLgOutroControle();
		valueToString += ", nmOutroControle: " +  getNmOutroControle();
		valueToString += ", txtSistemaOrganizacao: " +  getTxtSistemaOrganizacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EmpreendimentoControle(cdEmpreendimento,
			lgControleFormal,
			lgLivroCaixa,
			lgControleEstoque,
			lgContaReceber,
			lgReceitaDespesa,
			lgContaPagar,
			lgOutroControle,
			nmOutroControle,
			txtSistemaOrganizacao);
	}

}