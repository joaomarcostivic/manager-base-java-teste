package com.tivic.manager.acd;

public class PessoaOferta {

	private int cdPessoa;
	private int cdOferta;
	private int cdFuncao;
	private int stPessoaOferta;
	
	public PessoaOferta(){ }

	public PessoaOferta(int cdPessoa,
			int cdOferta,
			int cdFuncao, 
			int stPessoaOferta){
		setCdPessoa(cdPessoa);
		setCdOferta(cdOferta);
		setCdFuncao(cdFuncao);
		setStPessoaOferta(stPessoaOferta);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdOferta(int cdOferta){
		this.cdOferta=cdOferta;
	}
	public int getCdOferta(){
		return this.cdOferta;
	}
	public void setCdFuncao(int cdFuncao){
		this.cdFuncao=cdFuncao;
	}
	public int getCdFuncao(){
		return this.cdFuncao;
	}
	public void setStPessoaOferta(int stPessoaOferta) {
		this.stPessoaOferta = stPessoaOferta;
	}
	public int getStPessoaOferta() {
		return stPessoaOferta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdOferta: " +  getCdOferta();
		valueToString += ", cdFuncao: " +  getCdFuncao();
		valueToString += ", stPessoaOferta: " +  getStPessoaOferta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaOferta(getCdPessoa(),
			getCdOferta(),
			getCdFuncao(),
			getStPessoaOferta());
	}

}