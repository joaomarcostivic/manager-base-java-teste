package com.tivic.manager.mob;

import java.util.ArrayList;

import com.tivic.manager.grl.Logradouro;
import com.tivic.manager.grl.Pessoa;

public class ParadaDTO {
	private Parada parada;
	private GrupoParada grupoParada;
	private Concessao concessao;
	private Logradouro logradouro;
	private Pessoa pessoa;

	
	public ParadaDTO() {
		super();
	}
	public ParadaDTO(Parada parada, GrupoParada grupoParada, Logradouro logradouro, Pessoa pessoa, Concessao concessao) {
		super();
		this.parada = parada;
		this.grupoParada = grupoParada;
		this.logradouro = logradouro;
		this.pessoa = pessoa;
		this.concessao = concessao;
	}

	public Concessao getConcessao() {
		return concessao;
	}

	public void setConcessao(Concessao concessao) {
		this.concessao = concessao;
	}

	public Parada getParada() {
		return parada;
	}

	public void setParada(Parada parada) {
		this.parada = parada;
	}

	public GrupoParada getGrupoParada() {
		return grupoParada;
	}

	public void setGrupoParada(GrupoParada grupoParada) {
		this.grupoParada = grupoParada;
	}

	public Logradouro getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(Logradouro logradouro) {
		this.logradouro = logradouro;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
