package com.tivic.manager.agd;

public class Grupo {

	private int cdGrupo;
	private String nmGrupo;
	private int cdProprietario;
	private int tpVisibilidade;
	private int cdVinculo;
	private String nmEmail;
	private int stGrupo;

	public Grupo() { }

	public Grupo(int cdGrupo,
			String nmGrupo,
			int cdProprietario,
			int tpVisibilidade,
			int cdVinculo,
			String nmEmail,
			int stGrupo) {
		setCdGrupo(cdGrupo);
		setNmGrupo(nmGrupo);
		setCdProprietario(cdProprietario);
		setTpVisibilidade(tpVisibilidade);
		setCdVinculo(cdVinculo);
		setNmEmail(nmEmail);
		setStGrupo(stGrupo);
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setNmGrupo(String nmGrupo){
		this.nmGrupo=nmGrupo;
	}
	public String getNmGrupo(){
		return this.nmGrupo;
	}
	public void setCdProprietario(int cdProprietario){
		this.cdProprietario=cdProprietario;
	}
	public int getCdProprietario(){
		return this.cdProprietario;
	}
	public void setTpVisibilidade(int tpVisibilidade){
		this.tpVisibilidade=tpVisibilidade;
	}
	public int getTpVisibilidade(){
		return this.tpVisibilidade;
	}
	public void setCdVinculo(int cdVinculo){
		this.cdVinculo=cdVinculo;
	}
	public int getCdVinculo(){
		return this.cdVinculo;
	}
	public void setNmEmail(String nmEmail){
		this.nmEmail=nmEmail;
	}
	public String getNmEmail(){
		return this.nmEmail;
	}
	public void setStGrupo(int stGrupo){
		this.stGrupo=stGrupo;
	}
	public int getStGrupo(){
		return this.stGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupo: " +  getCdGrupo();
		valueToString += ", nmGrupo: " +  getNmGrupo();
		valueToString += ", cdProprietario: " +  getCdProprietario();
		valueToString += ", tpVisibilidade: " +  getTpVisibilidade();
		valueToString += ", cdVinculo: " +  getCdVinculo();
		valueToString += ", nmEmail: " +  getNmEmail();
		valueToString += ", stGrupo: " +  getStGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Grupo(getCdGrupo(),
			getNmGrupo(),
			getCdProprietario(),
			getTpVisibilidade(),
			getCdVinculo(),
			getNmEmail(),
			getStGrupo());
	}

}