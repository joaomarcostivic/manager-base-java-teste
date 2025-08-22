package com.tivic.manager.mob;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaFisica;

public class ConcessaoTaxiDTO {
	
	private Concessao concessao;
	private Pessoa pessoa;
	private Parada parada;
	private PessoaFisica pessoaFisica;
	private PessoaEndereco pessoaEndereco;
	private GrupoParada grupoParada;
	private int lgHabilitacaoAuxiliar;
	private int lgTaximetro;
	private String nrPlaca;
	
	public ConcessaoTaxiDTO() {
		super();
	}

	public ConcessaoTaxiDTO(Concessao concessao, Pessoa pessoa, Parada parada, PessoaFisica pessoaFisica,
		PessoaEndereco pessoaEndereco, GrupoParada grupoParada, int lgHabilitacaoAuxiliar, int lgTaximetro, String nrPlaca) {
		super();
		this.concessao = concessao;
		this.pessoa = pessoa;
		this.pessoaFisica = pessoaFisica;
		this.pessoaEndereco = pessoaEndereco;
		this.grupoParada = grupoParada;
		this.lgHabilitacaoAuxiliar = lgHabilitacaoAuxiliar;
		this.lgTaximetro = lgTaximetro;
		this.parada = parada;
		this.nrPlaca = nrPlaca;
	}

	public Concessao getConcessao() {
		return concessao;
	}

	public void setConcessao(Concessao concessao) {
		this.concessao = concessao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public PessoaEndereco getPessoaEndereco() {
		return pessoaEndereco;
	}

	public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
		this.pessoaEndereco = pessoaEndereco;
	}

	public GrupoParada getGrupoParada() {
		return grupoParada;
	}
	
	public void setGrupoParada(GrupoParada grupoParada) {
		this.grupoParada = grupoParada;
	}
	
	public int getLgHabilitacaoAuxiliar() {
		return lgHabilitacaoAuxiliar;
	}

	public void setLgHabilitacaoAuxiliar(int lgHabilitacaoAuxiliar) {
		this.lgHabilitacaoAuxiliar = lgHabilitacaoAuxiliar;
	}

	public int getLgTaximetro() {
		return lgTaximetro;
	}

	public void setLgTaximetro(int lgTaximetro) {
		this.lgTaximetro = lgTaximetro;
	}

	public Parada getParada() {
		return parada;
	}

	public void setParada(Parada parada) {
		this.parada = parada;
	}
	
	public String getNrPlaca() {
		return nrPlaca;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	
	
}
