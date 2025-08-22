package com.tivic.manager.str;

public class InfracaoObservacao {

	private int cdInfracao;
	private int cdObservacao;
	private String nrObservacao;
	private String nmObservacao;
	private String txtObservacao;
	private int stObservacao;

	public InfracaoObservacao(){ }

	public InfracaoObservacao(int cdInfracao,
			int cdObservacao,
			String nrObservacao,
			String nmObservacao,
			String txtObservacao,
			int stObservacao){
		setCdInfracao(cdInfracao);
		setCdObservacao(cdObservacao);
		setNrObservacao(nrObservacao);
		setNmObservacao(nmObservacao);
		setTxtObservacao(txtObservacao);
		setStObservacao(stObservacao);
	}
	public void setCdInfracao(int cdInfracao){
		this.cdInfracao=cdInfracao;
	}
	public int getCdInfracao(){
		return this.cdInfracao;
	}
	public void setCdObservacao(int cdObservacao){
		this.cdObservacao=cdObservacao;
	}
	public int getCdObservacao(){
		return this.cdObservacao;
	}
	public void setNrObservacao(String nrObservacao){
		this.nrObservacao=nrObservacao;
	}
	public String getNrObservacao(){
		return this.nrObservacao;
	}
	public void setNmObservacao(String nmObservacao){
		this.nmObservacao=nmObservacao;
	}
	public String getNmObservacao(){
		return this.nmObservacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setStObservacao(int stObservacao){
		this.stObservacao=stObservacao;
	}
	public int getStObservacao(){
		return this.stObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInfracao: " +  getCdInfracao();
		valueToString += ", cdObservacao: " +  getCdObservacao();
		valueToString += ", nrObservacao: " +  getNrObservacao();
		valueToString += ", nmObservacao: " +  getNmObservacao();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", stObservacao: " +  getStObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InfracaoObservacao(getCdInfracao(),
			getCdObservacao(),
			getNrObservacao(),
			getNmObservacao(),
			getTxtObservacao(),
			getStObservacao());
	}

}
