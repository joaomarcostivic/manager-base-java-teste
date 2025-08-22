package com.tivic.manager.mob.edat;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.BoatDeclarante;
import com.tivic.manager.mob.BoatOcorrenciaDTO;
import com.tivic.manager.mob.BoatVeiculo;
import com.tivic.manager.mob.Declarante;
import com.tivic.manager.mob.DeclaranteEndereco;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.boat.Boat;

public class EDatDTO extends Boat {

	private static final long serialVersionUID = 1L;

	Agente agente;	

	List<Inconsistencia> inconsistencias;
	List<BoatOcorrenciaDTO> boatOcorrencias;
	List<Boat> relacionados;
	List<BoatVeiculo> veiculos;
	
	Declarante declarante;
	DeclaranteEndereco declaranteEndereco;
	BoatDeclarante boatDeclarante;
	
	
	byte[] blbImagemVia;

	public void setInconsistencia(ArrayList<Inconsistencia> inconsistencias) {
		this.inconsistencias = inconsistencias;
	}

	public List<Inconsistencia> getInconsistencias() {
		return inconsistencias;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	public Agente getAgente() {
		return agente;
	}

	public void setBoatOcorrencias(ArrayList<BoatOcorrenciaDTO> boatOcorrencias) {
		this.boatOcorrencias = boatOcorrencias;
	}

	public List<BoatOcorrenciaDTO> getBoatOcorrencias() {
		return boatOcorrencias;
	}

	public void setRelacionados(ArrayList<Boat> boats) {
		this.relacionados = boats;
	}

	public List<Boat> getRelacionados() {
		return relacionados;
	}

	public byte[] getImgCenarioVia() {
		return blbImagemVia;
	}

	public void setImgCenarioVia(byte[] blbImagemVia) {
		this.blbImagemVia = blbImagemVia;
	}
	
	public List<BoatVeiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<BoatVeiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public Declarante getDeclarante() {
		return declarante;
	}

	public void setDeclarante(Declarante declarante) {
		this.declarante = declarante;
	}

	public DeclaranteEndereco getDeclaranteEndereco() {
		return declaranteEndereco;
	}

	public void setDeclaranteEndereco(DeclaranteEndereco declaranteEndereco) {
		this.declaranteEndereco = declaranteEndereco;
	}

	public BoatDeclarante getBoatDeclarante() {
		return boatDeclarante;
	}

	public void setBoatDeclarante(BoatDeclarante boatDeclarante) {
		this.boatDeclarante = boatDeclarante;
	}

}
