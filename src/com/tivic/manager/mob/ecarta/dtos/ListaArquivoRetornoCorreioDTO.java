package com.tivic.manager.mob.ecarta.dtos;

import java.util.List;

public class ListaArquivoRetornoCorreioDTO {
	private List<ArquivoEcartaDTO> arquivoRetornoCorreio;

	public List<ArquivoEcartaDTO> getArquivoRetornoCorreio() {
		return arquivoRetornoCorreio;
	}

	public void setArquivoRetornoCorreio(List<ArquivoEcartaDTO> arquivoRetornoCorreio) {
		this.arquivoRetornoCorreio = arquivoRetornoCorreio;
	}
}