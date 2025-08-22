package com.tivic.manager.mob.ait.sync.entities;

public class AgenteSyncDTO {

	private int cdAgente;
	private int cdUsuario;
	private String nrMatricula;
	private String nmAgente;

	public int getCdAgente() {
		return cdAgente;
	}

	public void setCdAgente(int cdAgente) {
		this.cdAgente = cdAgente;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getNrMatricula() {
		return nrMatricula;
	}

	public void setNrMatricula(String nrMatricula) {
		this.nrMatricula = nrMatricula;
	}

	public String getNmAgente() {
		return nmAgente;
	}

	public void setNmAgente(String nmAgente) {
		this.nmAgente = nmAgente;
	}
}
