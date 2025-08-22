package com.tivic.manager.grl;

public class PessoaDoenca {

	private int cdPessoa;
	private int cdDoenca;
	private String txtDescricao;

	public PessoaDoenca(){ }

	public PessoaDoenca(int cdPessoa,
			int cdDoenca,
			String txtDescricao){
		setCdPessoa(cdPessoa);
		setCdDoenca(cdDoenca);
		setTxtDescricao(txtDescricao);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdDoenca(int cdDoenca){
		this.cdDoenca=cdDoenca;
	}
	public int getCdDoenca(){
		return this.cdDoenca;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdDoenca: " +  getCdDoenca();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaDoenca(getCdPessoa(),
			getCdDoenca(),
			getTxtDescricao());
	}

}