package com.tivic.manager.str.endereco;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Endereco {
	private int cdEndereco;
	private int cdCidade;
	private String dsLogradouro;
	private String nrCep;
	private String nrEndereco;
	private String dsComplemento;
	private String nmBairro;
	
	public Endereco() {}
	
	public Endereco(int cdEndereco, 
					int cdCidade,
					String dsLogradouro,
					String nrCep,
					String nrEndereco,
					String dsComplemento,
					String nmBairro) {
		this.cdEndereco = cdEndereco;	
		this.cdCidade = cdCidade;
		this.dsLogradouro = dsLogradouro;
		this.nrCep = nrCep;
		this.nrEndereco = nrEndereco;
		this.dsComplemento = dsComplemento;
		this.nmBairro = nmBairro;
	}
	
	public int getCdEndereco() {
		return cdEndereco;
	}

	public void setCdEndereco(int cdEndereco) {
		this.cdEndereco = cdEndereco;
	}

	public int getCdCidade() {
		return cdCidade;
	}

	public void setCdCidade(int cdCidade) {
		this.cdCidade = cdCidade;
	}

	public String getDsLogradouro() {
		return dsLogradouro;
	}

	public void setDsLogradouro(String dsLogradouro) {
		this.dsLogradouro = dsLogradouro;
	}

	public String getNrCep() {
		return nrCep;
	}

	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}

	public String getNrEndereco() {
		return nrEndereco;
	}

	public void setNrEndereco(String nrEndereco) {
		this.nrEndereco = nrEndereco;
	}

	public String getDsComplemento() {
		return dsComplemento;
	}

	public void setDsComplemento(String dsComplemento) {
		this.dsComplemento = dsComplemento;
	}

	public String getNmBairro() {
		return nmBairro;
	}

	public void setNmBairro(String nmBairro) {
		this.nmBairro = nmBairro;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possivel serializar o objeto";
		}
	}
}
