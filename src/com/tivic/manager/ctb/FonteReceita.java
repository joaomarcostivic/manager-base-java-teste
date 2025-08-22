package com.tivic.manager.ctb;

public class FonteReceita {

	private int cdFonteReceita;
	private String nmFonteReceita;
	private String txtObservacao;
	private String idFonteReceita;
	private int stFonteReceita;

	public FonteReceita(){ }

	public FonteReceita(int cdFonteReceita,
			String nmFonteReceita,
			String txtObservacao,
			String idFonteReceita,
			int stFonteReceita){
		setCdFonteReceita(cdFonteReceita);
		setNmFonteReceita(nmFonteReceita);
		setTxtObservacao(txtObservacao);
		setIdFonteReceita(idFonteReceita);
		setStFonteReceita(stFonteReceita);
	}
	public void setCdFonteReceita(int cdFonteReceita){
		this.cdFonteReceita=cdFonteReceita;
	}
	public int getCdFonteReceita(){
		return this.cdFonteReceita;
	}
	public void setNmFonteReceita(String nmFonteReceita){
		this.nmFonteReceita=nmFonteReceita;
	}
	public String getNmFonteReceita(){
		return this.nmFonteReceita;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setIdFonteReceita(String idFonteReceita){
		this.idFonteReceita=idFonteReceita;
	}
	public String getIdFonteReceita(){
		return this.idFonteReceita;
	}
	public void setStFonteReceita(int stFonteReceita){
		this.stFonteReceita=stFonteReceita;
	}
	public int getStFonteReceita(){
		return this.stFonteReceita;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFonteReceita: " +  getCdFonteReceita();
		valueToString += ", nmFonteReceita: " +  getNmFonteReceita();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", idFonteReceita: " +  getIdFonteReceita();
		valueToString += ", stFonteReceita: " +  getStFonteReceita();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FonteReceita(getCdFonteReceita(),
			getNmFonteReceita(),
			getTxtObservacao(),
			getIdFonteReceita(),
			getStFonteReceita());
	}

}