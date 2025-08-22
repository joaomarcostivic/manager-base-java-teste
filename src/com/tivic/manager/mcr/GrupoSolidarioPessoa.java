package com.tivic.manager.mcr;

public class GrupoSolidarioPessoa {

	private int cdGrupoSolidario;
	private int cdPessoa;
	private int lgCoordenador;

	public GrupoSolidarioPessoa(int cdGrupoSolidario,
			int cdPessoa,
			int lgCoordenador){
		setCdGrupoSolidario(cdGrupoSolidario);
		setCdPessoa(cdPessoa);
		setLgCoordenador(lgCoordenador);
	}
	public void setCdGrupoSolidario(int cdGrupoSolidario){
		this.cdGrupoSolidario=cdGrupoSolidario;
	}
	public int getCdGrupoSolidario(){
		return this.cdGrupoSolidario;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setLgCoordenador(int lgCoordenador){
		this.lgCoordenador=lgCoordenador;
	}
	public int getLgCoordenador(){
		return this.lgCoordenador;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupoSolidario: " +  getCdGrupoSolidario();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", lgCoordenador: " +  getLgCoordenador();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoSolidarioPessoa(getCdGrupoSolidario(),
			getCdPessoa(),
			getLgCoordenador());
	}

}
