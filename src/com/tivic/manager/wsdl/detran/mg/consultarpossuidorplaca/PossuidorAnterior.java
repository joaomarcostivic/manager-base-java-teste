package com.tivic.manager.wsdl.detran.mg.consultarpossuidorplaca;

import java.util.GregorianCalendar;

public class PossuidorAnterior {
	private GregorianCalendar dataAquisicao;
    private String cpf;
    private String cnpj;
    private String codigoMunicipio;
    private String uf;
    private String nome;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    
    public PossuidorAnterior() { }
    
	public PossuidorAnterior(GregorianCalendar dataAquisicao, String cpf, String cnpj, String codigoMunicipio, String uf,
			String nome, String logradouro, String numero, String complemento, String bairro) {
		super();
		this.dataAquisicao = dataAquisicao;
		this.cpf = cpf;
		this.cnpj = cnpj;
		this.codigoMunicipio = codigoMunicipio;
		this.uf = uf;
		this.nome = nome;
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
	}

	public GregorianCalendar getDataAquisicao() {
		return dataAquisicao;
	}

	public void setDataAquisicao(GregorianCalendar dataAquisicao) {
		this.dataAquisicao = dataAquisicao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}  
}
