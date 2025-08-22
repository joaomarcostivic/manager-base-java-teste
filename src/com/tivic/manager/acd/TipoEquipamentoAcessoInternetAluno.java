package com.tivic.manager.acd;

public class TipoEquipamentoAcessoInternetAluno {

	private int cdTipoEquipamentoAcessoInternetAluno;
	private String nmTipoEquipamentoAcessoInternetAluno;
	private String idTipoEquipamentoAcessoInternetAluno;
	private int stTipoEquipamentoAcessoInternetAluno;

	public TipoEquipamentoAcessoInternetAluno() { }

	public TipoEquipamentoAcessoInternetAluno(int cdTipoEquipamentoAcessoInternetAluno,
			String nmTipoEquipamentoAcessoInternetAluno,
			String idTipoEquipamentoAcessoInternetAluno,
			int stTipoEquipamentoAcessoInternetAluno) {
		setCdTipoEquipamentoAcessoInternetAluno(cdTipoEquipamentoAcessoInternetAluno);
		setNmTipoEquipamentoAcessoInternetAluno(nmTipoEquipamentoAcessoInternetAluno);
		setIdTipoEquipamentoAcessoInternetAluno(idTipoEquipamentoAcessoInternetAluno);
		setStTipoEquipamentoAcessoInternetAluno(stTipoEquipamentoAcessoInternetAluno);
	}
	public void setCdTipoEquipamentoAcessoInternetAluno(int cdTipoEquipamentoAcessoInternetAluno){
		this.cdTipoEquipamentoAcessoInternetAluno=cdTipoEquipamentoAcessoInternetAluno;
	}
	public int getCdTipoEquipamentoAcessoInternetAluno(){
		return this.cdTipoEquipamentoAcessoInternetAluno;
	}
	public void setNmTipoEquipamentoAcessoInternetAluno(String nmTipoEquipamentoAcessoInternetAluno){
		this.nmTipoEquipamentoAcessoInternetAluno=nmTipoEquipamentoAcessoInternetAluno;
	}
	public String getNmTipoEquipamentoAcessoInternetAluno(){
		return this.nmTipoEquipamentoAcessoInternetAluno;
	}
	public void setIdTipoEquipamentoAcessoInternetAluno(String idTipoEquipamentoAcessoInternetAluno){
		this.idTipoEquipamentoAcessoInternetAluno=idTipoEquipamentoAcessoInternetAluno;
	}
	public String getIdTipoEquipamentoAcessoInternetAluno(){
		return this.idTipoEquipamentoAcessoInternetAluno;
	}
	public void setStTipoEquipamentoAcessoInternetAluno(int stTipoEquipamentoAcessoInternetAluno){
		this.stTipoEquipamentoAcessoInternetAluno=stTipoEquipamentoAcessoInternetAluno;
	}
	public int getStTipoEquipamentoAcessoInternetAluno(){
		return this.stTipoEquipamentoAcessoInternetAluno;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoEquipamentoAcessoInternetAluno: " +  getCdTipoEquipamentoAcessoInternetAluno();
		valueToString += ", nmTipoEquipamentoAcessoInternetAluno: " +  getNmTipoEquipamentoAcessoInternetAluno();
		valueToString += ", idTipoEquipamentoAcessoInternetAluno: " +  getIdTipoEquipamentoAcessoInternetAluno();
		valueToString += ", stTipoEquipamentoAcessoInternetAluno: " +  getStTipoEquipamentoAcessoInternetAluno();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoEquipamentoAcessoInternetAluno(getCdTipoEquipamentoAcessoInternetAluno(),
			getNmTipoEquipamentoAcessoInternetAluno(),
			getIdTipoEquipamentoAcessoInternetAluno(),
			getStTipoEquipamentoAcessoInternetAluno());
	}

}