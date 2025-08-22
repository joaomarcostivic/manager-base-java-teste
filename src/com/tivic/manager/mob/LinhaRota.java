package com.tivic.manager.mob;

public class LinhaRota {

	private int cdLinha;
	private int cdRota;
	private String nmRota;
	private int nrRota;
	private String idRota;
	private int stRota;
	private String txtObservacao;
	private Double qtKm;
	private int tpRota;

	public LinhaRota() { }

	public LinhaRota(int cdLinha,
			int cdRota,
			String nmRota,
			int nrRota,
			String idRota,
			int stRota,
			String txtObservacao,
			Double qtKm,
			int tpRota) {
		setCdLinha(cdLinha);
		setCdRota(cdRota);
		setNmRota(nmRota);
		setNrRota(nrRota);
		setIdRota(idRota);
		setStRota(stRota);
		setTxtObservacao(txtObservacao);
		setQtKm(qtKm);
		setTpRota(tpRota);
	}
	public void setCdLinha(int cdLinha){
		this.cdLinha=cdLinha;
	}
	public int getCdLinha(){
		return this.cdLinha;
	}
	public void setCdRota(int cdRota){
		this.cdRota=cdRota;
	}
	public int getCdRota(){
		return this.cdRota;
	}
	public void setNmRota(String nmRota){
		this.nmRota=nmRota;
	}
	public String getNmRota(){
		return this.nmRota;
	}
	public void setNrRota(int nrRota){
		this.nrRota=nrRota;
	}
	public int getNrRota(){
		return this.nrRota;
	}
	public void setIdRota(String idRota){
		this.idRota=idRota;
	}
	public String getIdRota(){
		return this.idRota;
	}
	public void setStRota(int stRota){
		this.stRota=stRota;
	}
	public int getStRota(){
		return this.stRota;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setQtKm(Double qtKm){
		this.qtKm=qtKm;
	}
	public Double getQtKm(){
		return this.qtKm;
	}
	public void setTpRota(int tpRota){
		this.tpRota=tpRota;
	}
	public int getTpRota(){
		return this.tpRota;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLinha: " +  getCdLinha();
		valueToString += ", cdRota: " +  getCdRota();
		valueToString += ", nmRota: " +  getNmRota();
		valueToString += ", nrRota: " +  getNrRota();
		valueToString += ", idRota: " +  getIdRota();
		valueToString += ", stRota: " +  getStRota();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", qtKm: " +  getQtKm();
		valueToString += ", tpRota: " +  getTpRota();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LinhaRota(getCdLinha(),
			getCdRota(),
			getNmRota(),
			getNrRota(),
			getIdRota(),
			getStRota(),
			getTxtObservacao(),
			getQtKm(),
			getTpRota());
	}

}