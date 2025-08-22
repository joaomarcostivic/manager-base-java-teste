package com.tivic.manager.grl;

public class PessoaAlergia {

	private int cdPessoa;
	private int cdAlergia;
	private String txtDescricao;

	public PessoaAlergia(){ }

	public PessoaAlergia(int cdPessoa,
			int cdAlergia,
			String txtDescricao){
		setCdPessoa(cdPessoa);
		setCdAlergia(cdAlergia);
		setTxtDescricao(txtDescricao);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdAlergia(int cdAlergia){
		this.cdAlergia=cdAlergia;
	}
	public int getCdAlergia(){
		return this.cdAlergia;
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
		valueToString += ", cdAlergia: " +  getCdAlergia();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaAlergia(getCdPessoa(),
			getCdAlergia(),
			getTxtDescricao());
	}

}