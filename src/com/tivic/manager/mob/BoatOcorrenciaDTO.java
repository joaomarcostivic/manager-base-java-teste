package com.tivic.manager.mob;

public class BoatOcorrenciaDTO extends BoatOcorrencia {

	private Agente agente;
	private Ocorrencia ocorrencia;

	public BoatOcorrenciaDTO() { }

	public Agente getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
	
}