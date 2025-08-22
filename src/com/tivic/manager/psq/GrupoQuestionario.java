package com.tivic.manager.psq;

public class GrupoQuestionario {

	private int cdGrupoQuestionario;
	private int cdPessoa;
	private String nmGrupo;
	private String idGrupo;

	public GrupoQuestionario(int cdGrupoQuestionario,
			int cdPessoa,
			String nmGrupo,
			String idGrupo){
		setCdGrupoQuestionario(cdGrupoQuestionario);
		setCdPessoa(cdPessoa);
		setNmGrupo(nmGrupo);
		setIdGrupo(idGrupo);
	}
	public void setCdGrupoQuestionario(int cdGrupoQuestionario){
		this.cdGrupoQuestionario=cdGrupoQuestionario;
	}
	public int getCdGrupoQuestionario(){
		return this.cdGrupoQuestionario;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setNmGrupo(String nmGrupo){
		this.nmGrupo=nmGrupo;
	}
	public String getNmGrupo(){
		return this.nmGrupo;
	}
	public void setIdGrupo(String idGrupo){
		this.idGrupo=idGrupo;
	}
	public String getIdGrupo(){
		return this.idGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupoQuestionario: " +  getCdGrupoQuestionario();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", nmGrupo: " +  getNmGrupo();
		valueToString += ", idGrupo: " +  getIdGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoQuestionario(getCdGrupoQuestionario(),
			getCdPessoa(),
			getNmGrupo(),
			getIdGrupo());
	}

}