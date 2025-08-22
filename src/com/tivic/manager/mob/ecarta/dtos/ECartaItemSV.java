package com.tivic.manager.mob.ecarta.dtos;

public class ECartaItemSV {
	
	private String cdAit;
	private String nrLote;
	private String nrEtiqueta;
	private String nmDestinatario;
	private String dsLogradouro;
	private String nrEndereco;
	private String complementoEndereco;
	private String nmBairro;
	private String nmMunicipio;
	private String sgEstado;
	private String nrCep;
	private byte[] blbNotificacao;
			
	public String getCdAit() {
		return cdAit;
	}

	public void setCdAit(String cdAit) {
		this.cdAit = cdAit;
	}

	public String getNrLote() {
		return nrLote;
	}

	public void setNrLote(String nrLote) {
		this.nrLote = nrLote;
	}

	public String getNrEtiqueta() {
		return nrEtiqueta;
	}

	public void setNrEtiqueta(String nrEtiqueta) {
		this.nrEtiqueta = nrEtiqueta;
	}

	public String getNmDestinatario() {
		return nmDestinatario;
	}

	public void setNmDestinatario(String nmDestinatario) {
		this.nmDestinatario = nmDestinatario;
	}

	public String getDsLogradouro() {
		return dsLogradouro;
	}

	public void setDsLogradouro(String dsLogradouro) {
		this.dsLogradouro = dsLogradouro;
	}

	public String getNrEndereco() {
		return nrEndereco;
	}

	public void setNrEndereco(String nrEndereco) {
		this.nrEndereco = nrEndereco;
	}

	public String getComplementoEndereco() {
		return complementoEndereco;
	}

	public void setComplementoEndereco(String complementoEndereco) {
		this.complementoEndereco = complementoEndereco;
	}

	public String getNmBairro() {
		return nmBairro;
	}
	
	public void setNmBairro(String nmBairro) {
		this.nmBairro = nmBairro;
	}
	
	public String getNmMunicipio() {
		return nmMunicipio;
	}
	
	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}
	
	public String getSgEstado() {
		return sgEstado;
	}
	
	public void setSgEstado(String sgEstado) {
		this.sgEstado = sgEstado;
	}
		
	public String getNrCep() {
		return nrCep;
	}

	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}

	public byte[] getBlbNotificacao() {
		return blbNotificacao;
	}

	public void setBlbNotificacao(byte[] blbNotificacao) {
		this.blbNotificacao = blbNotificacao;
	}
	
	public String getEnderecoDestinatario() {
	    return String.join("|", dsLogradouro, nrEndereco, complementoEndereco, 
	            nmBairro, nmMunicipio, sgEstado, nrCep);
	}

}
