package com.tivic.manager.str.endereco;

public class EnderecoBuilder {
	
	private Endereco endereco;
	
	public EnderecoBuilder() {
		endereco = new Endereco();
	}
	
	public EnderecoBuilder setCdEndereco(int cdEndereco) {
		endereco.setCdEndereco(cdEndereco);
		return this;
	}

	public EnderecoBuilder setCdCidade(int cdCidade) {
		endereco.setCdCidade(cdCidade);
		return this;
	}
	
	public EnderecoBuilder setDsLogradouro(String dsLogradouro) {
		endereco.setDsLogradouro(dsLogradouro);
		return this;
	}
	
	public EnderecoBuilder setNrCep(String nrCep) {
		endereco.setNrCep(nrCep);
		return this;
	}
	
	public EnderecoBuilder setNrEndereco(String nrEndereco) {
		endereco.setNrEndereco(nrEndereco);
		return this;
	}
	
	public EnderecoBuilder setDsComplemento(String dsComplemento) {
		endereco.setDsComplemento(dsComplemento);
		return this;
	}
	
	public EnderecoBuilder setNmBairro(String nmBairro) {
		endereco.setNmBairro(nmBairro);
		return this;
	}
	
	public Endereco builder() {
		return endereco;
	}
}
