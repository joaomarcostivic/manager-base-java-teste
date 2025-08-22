package com.tivic.manager.blb;

public class Exemplar {

	private int cdExemplar;
	private int cdPublicacao;
	private int cdLocalizacao;
	private String nrLocalizacao;
	private int stExemplar;
	private int tpExemplar;
	private String txtObservacao;
	private String idExemplar;

	public Exemplar(){ }

	public Exemplar(int cdExemplar,
			int cdPublicacao,
			int cdLocalizacao,
			String nrLocalizacao,
			int stExemplar,
			int tpExemplar,
			String txtObservacao,
			String idExemplar){
		setCdExemplar(cdExemplar);
		setCdPublicacao(cdPublicacao);
		setCdLocalizacao(cdLocalizacao);
		setNrLocalizacao(nrLocalizacao);
		setStExemplar(stExemplar);
		setTpExemplar(tpExemplar);
		setTxtObservacao(txtObservacao);
		setIdExemplar(idExemplar);
	}
	public void setCdExemplar(int cdExemplar){
		this.cdExemplar=cdExemplar;
	}
	public int getCdExemplar(){
		return this.cdExemplar;
	}
	public void setCdPublicacao(int cdPublicacao){
		this.cdPublicacao=cdPublicacao;
	}
	public int getCdPublicacao(){
		return this.cdPublicacao;
	}
	public void setCdLocalizacao(int cdLocalizacao){
		this.cdLocalizacao=cdLocalizacao;
	}
	public int getCdLocalizacao(){
		return this.cdLocalizacao;
	}
	public void setNrLocalizacao(String nrLocalizacao){
		this.nrLocalizacao=nrLocalizacao;
	}
	public String getNrLocalizacao(){
		return this.nrLocalizacao;
	}
	public void setStExemplar(int stExemplar){
		this.stExemplar=stExemplar;
	}
	public int getStExemplar(){
		return this.stExemplar;
	}
	public void setTpExemplar(int tpExemplar){
		this.tpExemplar=tpExemplar;
	}
	public int getTpExemplar(){
		return this.tpExemplar;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setIdExemplar(String idExemplar){
		this.idExemplar=idExemplar;
	}
	public String getIdExemplar(){
		return this.idExemplar;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdExemplar: " +  getCdExemplar();
		valueToString += ", cdPublicacao: " +  getCdPublicacao();
		valueToString += ", cdLocalizacao: " +  getCdLocalizacao();
		valueToString += ", nrLocalizacao: " +  getNrLocalizacao();
		valueToString += ", stExemplar: " +  getStExemplar();
		valueToString += ", tpExemplar: " +  getTpExemplar();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", idExemplar: " +  getIdExemplar();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Exemplar(getCdExemplar(),
			getCdPublicacao(),
			getCdLocalizacao(),
			getNrLocalizacao(),
			getStExemplar(),
			getTpExemplar(),
			getTxtObservacao(),
			getIdExemplar());
	}

}