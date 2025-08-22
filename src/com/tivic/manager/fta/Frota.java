package com.tivic.manager.fta;

public class Frota {

	private int cdFrota;
	private String nmFrota;
	private String idFrota;
	private String txtObservacao;
	private int stFrota;
	private int cdResponsavel;
	private int cdProprietario;
	private int tpFrota;

	public Frota(){ }

	public Frota(int cdFrota,
			String nmFrota,
			String idFrota,
			String txtObservacao,
			int stFrota,
			int cdResponsavel,
			int cdProprietario,
			int tpFrota){
		setCdFrota(cdFrota);
		setNmFrota(nmFrota);
		setIdFrota(idFrota);
		setTxtObservacao(txtObservacao);
		setStFrota(stFrota);
		setCdResponsavel(cdResponsavel);
		setCdProprietario(cdProprietario);
		setTpFrota(tpFrota);
	}
	public void setCdFrota(int cdFrota){
		this.cdFrota=cdFrota;
	}
	public int getCdFrota(){
		return this.cdFrota;
	}
	public void setNmFrota(String nmFrota){
		this.nmFrota=nmFrota;
	}
	public String getNmFrota(){
		return this.nmFrota;
	}
	public void setIdFrota(String idFrota){
		this.idFrota=idFrota;
	}
	public String getIdFrota(){
		return this.idFrota;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setStFrota(int stFrota){
		this.stFrota=stFrota;
	}
	public int getStFrota(){
		return this.stFrota;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setCdProprietario(int cdProprietario){
		this.cdProprietario=cdProprietario;
	}
	public int getCdProprietario(){
		return this.cdProprietario;
	}
	public void setTpFrota(int tpFrota){
		this.tpFrota=tpFrota;
	}
	public int getTpFrota(){
		return this.tpFrota;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFrota: " +  getCdFrota();
		valueToString += ", nmFrota: " +  getNmFrota();
		valueToString += ", idFrota: " +  getIdFrota();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", stFrota: " +  getStFrota();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", cdProprietario: " +  getCdProprietario();
		valueToString += ", tpFrota: " +  getTpFrota();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Frota(getCdFrota(),
			getNmFrota(),
			getIdFrota(),
			getTxtObservacao(),
			getStFrota(),
			getCdResponsavel(),
			getCdProprietario(),
			getTpFrota());
	}

}