package com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor;

public class PontuacaoDadosCondutorDTOBuilder {
	
	private PontuacaoDadosCondutorDTO dadosCondutorDTO;
	
	public PontuacaoDadosCondutorDTOBuilder(ConsultarPontuacaoDadosCondutorDadosRetorno dadosRetorno) {
		dadosCondutorDTO = new PontuacaoDadosCondutorDTO();
		getDadosCondutorForDadosRetorno(dadosRetorno);
	}
	
	private void getDadosCondutorForDadosRetorno(ConsultarPontuacaoDadosCondutorDadosRetorno dadosRetorno) {
		this.dadosCondutorDTO.setCpfCondutor(dadosRetorno.getCpfCondutor());
		this.dadosCondutorDTO.setRenachCondutor(dadosRetorno.getRenachCondutor());
		this.dadosCondutorDTO.setPguCondutor(dadosRetorno.getPguCondutor());
		this.dadosCondutorDTO.setNomeCondutor(dadosRetorno.getNomeCondutor());
		this.dadosCondutorDTO.setPontosCondutor(dadosRetorno.getPontosCondutor());
		this.dadosCondutorDTO.setUfCnh(dadosRetorno.getUfCnh());
		this.dadosCondutorDTO.setCategoriaCnh(dadosRetorno.getCategoriaCnh());
		this.dadosCondutorDTO.setDataEmissaoCnh(dadosRetorno.getDataEmissaoCnh());
		this.dadosCondutorDTO.setEnderecoCondutor(dadosRetorno.getEnderecoCondutor());
		this.dadosCondutorDTO.setNumeroLogradouro(dadosRetorno.getNumeroLogradouro());
		this.dadosCondutorDTO.setComplementoEndereco(dadosRetorno.getComplementoEndereco());
		this.dadosCondutorDTO.setBairro(dadosRetorno.getBairro());
		this.dadosCondutorDTO.setCodigoMunicipio(dadosRetorno.getCodigoMunicipio());
		this.dadosCondutorDTO.setNomeMunicipio(dadosRetorno.getNomeMunicipio());
		this.dadosCondutorDTO.setUfMunicipio(dadosRetorno.getUfMunicipio());
		this.dadosCondutorDTO.setCepEnderecoCondutor(dadosRetorno.getCepEnderecoCondutor());
	}
	
	public PontuacaoDadosCondutorDTO build() {
		return this.dadosCondutorDTO;
	}

}
