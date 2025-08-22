package com.tivic.manager.mob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tivic.manager.mob.Lacre;
import com.tivic.manager.mob.ConcessaoVeiculo;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.ImpedimentoAfericao;

public class AfericaoCatracaDTO {
	private AfericaoCatraca afericaoCatraca;
	private Lacre lacre;
	private ConcessaoVeiculo concessaoVeiculo;
	private Agente agente;
	private ArrayList<ImpedimentoAfericao> impedimentosAfericao;
	private Pessoa pessoa;
  
	public AfericaoCatracaDTO() {
		super();
	}
	
	public AfericaoCatracaDTO(AfericaoCatraca afericaoCatraca, Lacre lacre, ConcessaoVeiculo concessaoVeiculo, Agente agente, ArrayList<ImpedimentoAfericao> impedimentosAfericao) {
		super();
		this.afericaoCatraca = afericaoCatraca;
		this.concessaoVeiculo = concessaoVeiculo;
		this.agente = agente;
		this.lacre = lacre;
		this.impedimentosAfericao = impedimentosAfericao;
	}

	public AfericaoCatraca getAfericaoCatraca() {
		return afericaoCatraca;
	}

	public void setAfericaoCatraca(AfericaoCatraca afericaoCatraca) {
		this.afericaoCatraca = afericaoCatraca;
	}

	public Lacre getLacre() {
		return lacre;
	}

	public void setLacre(Lacre lacre) {
		this.lacre = lacre;
	}

	public ConcessaoVeiculo getConcessaoVeiculo() {
		return concessaoVeiculo;
	}

	public void setConcessaoVeiculo(ConcessaoVeiculo concessaoVeiculo) {
		this.concessaoVeiculo = concessaoVeiculo;
	}

	public Agente getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	public ArrayList<ImpedimentoAfericao> getImpedimentosAfericao() {
		return impedimentosAfericao;
	}

	public void setImpedimentosAfericao(ArrayList<ImpedimentoAfericao> impedimentosAfericao) {
		this.impedimentosAfericao = impedimentosAfericao;
	}
  
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}	
}
