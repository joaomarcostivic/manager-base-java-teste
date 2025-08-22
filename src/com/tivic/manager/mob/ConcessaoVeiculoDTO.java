package com.tivic.manager.mob;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaEndereco;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConcessaoVeiculoDTO {
	
	private ConcessaoVeiculo concessaoVeiculo;
	private Concessao concessao;
	private Veiculo veiculo;
	private Pessoa pessoa;
	private PessoaEndereco pessoaEndereco;
	private String nrCpfCnpj;
	private Parada parada;
	private Vistoria vistoria;
	
	public ConcessaoVeiculoDTO() {}

	public ConcessaoVeiculoDTO(ConcessaoVeiculo concessaoVeiculo, Concessao concessao, Veiculo veiculo, Pessoa pessoa,
			PessoaEndereco pessoaEndereco, String nrCpfCnpj, Parada parada) {
		super();
		this.concessaoVeiculo = concessaoVeiculo;
		this.concessao = concessao;
		this.veiculo = veiculo;
		this.pessoa = pessoa;
		this.pessoaEndereco = pessoaEndereco;
		this.nrCpfCnpj = nrCpfCnpj;
		this.parada = parada;
	}

	public ConcessaoVeiculo getConcessaoVeiculo() {
		return concessaoVeiculo;
	}

	public void setConcessaoVeiculo(ConcessaoVeiculo concessaoVeiculo) {
		this.concessaoVeiculo = concessaoVeiculo;
	}

	public Concessao getConcessao() {
		return concessao;
	}

	public void setConcessao(Concessao concessao) {
		this.concessao = concessao;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public PessoaEndereco getPessoaEndereco() {
		return pessoaEndereco;
	}

	public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
		this.pessoaEndereco = pessoaEndereco;
	}

	public String getNrCpfCnpj() {
		return nrCpfCnpj;
	}

	public void setNrCpfCnpj(String nrCpfCnpj) {
		this.nrCpfCnpj = nrCpfCnpj;
	}

	public Parada getParada() {
		return parada;
	}

	public void setParada(Parada parada) {
		this.parada = parada;
	}

	public Vistoria getVistoria() {
		return vistoria;
	}

	public void setVistoria(Vistoria vistoria) {
		this.vistoria = vistoria;
	}
	
}
