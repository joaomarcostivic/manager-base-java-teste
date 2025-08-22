package com.tivic.manager.ord;

public class SituacaoServico {

	private int cdSituacaoServico;
	private String nmSituacaoServico;
	private String txtSituacaoServico;
	private int nrOrdem;
	private int lgAltera;
	private int lgExclui;
	private int lgCancela;
	private int stCadastro;

	public SituacaoServico() { }

	public SituacaoServico(int cdSituacaoServico,
			String nmSituacaoServico,
			String txtSituacaoServico,
			int nrOrdem,
			int lgAltera,
			int lgExclui,
			int lgCancela,
			int stCadastro) {
		setCdSituacaoServico(cdSituacaoServico);
		setNmSituacaoServico(nmSituacaoServico);
		setTxtSituacaoServico(txtSituacaoServico);
		setNrOrdem(nrOrdem);
		setLgAltera(lgAltera);
		setLgExclui(lgExclui);
		setLgCancela(lgCancela);
		setStCadastro(stCadastro);
	}
	public void setCdSituacaoServico(int cdSituacaoServico){
		this.cdSituacaoServico=cdSituacaoServico;
	}
	public int getCdSituacaoServico(){
		return this.cdSituacaoServico;
	}
	public void setNmSituacaoServico(String nmSituacaoServico){
		this.nmSituacaoServico=nmSituacaoServico;
	}
	public String getNmSituacaoServico(){
		return this.nmSituacaoServico;
	}
	public void setTxtSituacaoServico(String txtSituacaoServico){
		this.txtSituacaoServico=txtSituacaoServico;
	}
	public String getTxtSituacaoServico(){
		return this.txtSituacaoServico;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setLgAltera(int lgAltera){
		this.lgAltera=lgAltera;
	}
	public int getLgAltera(){
		return this.lgAltera;
	}
	public void setLgExclui(int lgExclui){
		this.lgExclui=lgExclui;
	}
	public int getLgExclui(){
		return this.lgExclui;
	}
	public void setLgCancela(int lgCancela){
		this.lgCancela=lgCancela;
	}
	public int getLgCancela(){
		return this.lgCancela;
	}
	public void setStCadastro(int stCadastro){
		this.stCadastro=stCadastro;
	}
	public int getStCadastro(){
		return this.stCadastro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdSituacaoServico: " +  getCdSituacaoServico();
		valueToString += ", nmSituacaoServico: " +  getNmSituacaoServico();
		valueToString += ", txtSituacaoServico: " +  getTxtSituacaoServico();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", lgAltera: " +  getLgAltera();
		valueToString += ", lgExclui: " +  getLgExclui();
		valueToString += ", lgCancela: " +  getLgCancela();
		valueToString += ", stCadastro: " +  getStCadastro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new SituacaoServico(getCdSituacaoServico(),
			getNmSituacaoServico(),
			getTxtSituacaoServico(),
			getNrOrdem(),
			getLgAltera(),
			getLgExclui(),
			getLgCancela(),
			getStCadastro());
	}

}