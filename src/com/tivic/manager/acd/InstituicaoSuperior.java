package com.tivic.manager.acd;

public class InstituicaoSuperior {

	private int cdInstituicaoSuperior;
	private String idInstituicaoSuperior;
	private String nmInstituicaoSuperior;
	private int cdCidade;
	private int tpDependenciaAdministrativa;
	private int tpInstituicaoSuperior;
	private int stFuncionamento;

	public InstituicaoSuperior(){ }

	public InstituicaoSuperior(int cdInstituicaoSuperior,
			String idInstituicaoSuperior,
			String nmInstituicaoSuperior,
			int cdCidade,
			int tpDependenciaAdministrativa,
			int tpInstituicaoSuperior,
			int stFuncionamento){
		setCdInstituicaoSuperior(cdInstituicaoSuperior);
		setIdInstituicaoSuperior(idInstituicaoSuperior);
		setNmInstituicaoSuperior(nmInstituicaoSuperior);
		setCdCidade(cdCidade);
		setTpDependenciaAdministrativa(tpDependenciaAdministrativa);
		setTpInstituicaoSuperior(tpInstituicaoSuperior);
		setStFuncionamento(stFuncionamento);
	}
	public void setCdInstituicaoSuperior(int cdInstituicaoSuperior){
		this.cdInstituicaoSuperior=cdInstituicaoSuperior;
	}
	public int getCdInstituicaoSuperior(){
		return this.cdInstituicaoSuperior;
	}
	public void setIdInstituicaoSuperior(String idInstituicaoSuperior){
		this.idInstituicaoSuperior=idInstituicaoSuperior;
	}
	public String getIdInstituicaoSuperior(){
		return this.idInstituicaoSuperior;
	}
	public void setNmInstituicaoSuperior(String nmInstituicaoSuperior){
		this.nmInstituicaoSuperior=nmInstituicaoSuperior;
	}
	public String getNmInstituicaoSuperior(){
		return this.nmInstituicaoSuperior;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setTpDependenciaAdministrativa(int tpDependenciaAdministrativa){
		this.tpDependenciaAdministrativa=tpDependenciaAdministrativa;
	}
	public int getTpDependenciaAdministrativa(){
		return this.tpDependenciaAdministrativa;
	}
	public void setTpInstituicaoSuperior(int tpInstituicaoSuperior){
		this.tpInstituicaoSuperior=tpInstituicaoSuperior;
	}
	public int getTpInstituicaoSuperior(){
		return this.tpInstituicaoSuperior;
	}
	public void setStFuncionamento(int stFuncionamento){
		this.stFuncionamento=stFuncionamento;
	}
	public int getStFuncionamento(){
		return this.stFuncionamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInstituicaoSuperior: " +  getCdInstituicaoSuperior();
		valueToString += ", idInstituicaoSuperior: " +  getIdInstituicaoSuperior();
		valueToString += ", nmInstituicaoSuperior: " +  getNmInstituicaoSuperior();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", tpDependenciaAdministrativa: " +  getTpDependenciaAdministrativa();
		valueToString += ", tpInstituicaoSuperior: " +  getTpInstituicaoSuperior();
		valueToString += ", stFuncionamento: " +  getStFuncionamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoSuperior(getCdInstituicaoSuperior(),
			getIdInstituicaoSuperior(),
			getNmInstituicaoSuperior(),
			getCdCidade(),
			getTpDependenciaAdministrativa(),
			getTpInstituicaoSuperior(),
			getStFuncionamento());
	}

}