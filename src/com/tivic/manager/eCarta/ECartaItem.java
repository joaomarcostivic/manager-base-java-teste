package com.tivic.manager.eCarta;

public class ECartaItem {
	private String numeroLote;
	private String numeroEtiqueta;
	private String nomeDestinatario;
	private String nomeLogradouro;
	private String numEndereco;
	private String complementEndereco;
	private String nmBairro;
	private String nmMunicipio;
	private String sgEstado;
	private String cep;
	private byte[] blbArquivo;
	private String cdArquivo;
	
	public String getCdArquivo() {
		return cdArquivo;
	}
	public void setCdArquivo(String cdArquivo) {
		if (cdArquivo != null)
		{
			this.cdArquivo = cdArquivo;
		}
		else
		{
			this.cdArquivo = "";
		}
	}
	public String getNumeroLote() {
		return numeroLote;
	}
	public void setNumeroLote(String numeroLote) {
		if (numeroLote != null)
		{
			this.numeroLote = numeroLote;
		}
		else
		{
			this.numeroLote = "";
		}
	}
	public String getNumeroEtiqueta() {
		return numeroEtiqueta;
	}
	public void setNumeroEtiqueta(String numeroEtiqueta) {
		if (numeroEtiqueta != null)
		{
			this.numeroEtiqueta = numeroEtiqueta;
		}
		else
		{
			this.numeroEtiqueta = "";
		}
	}
	public String getNomeDestinatario() {
		return nomeDestinatario;
	}
	public void setNomeDestinatario(String nomeDestinatario) {
		if (nomeDestinatario != null)
		{
			this.nomeDestinatario = nomeDestinatario;
		}
		else
		{
			this.nomeDestinatario = "";
		}
	}
	public String getNomeLogradouro() {
		return nomeLogradouro;
	}
	public void setNomeLogradouro(String nomeLogradouro) {
		if (nomeLogradouro != null)
		{
			this.nomeLogradouro = nomeLogradouro;
		}
		else
		{
			this.nomeLogradouro = "";
		}
	}
	public String getNumEndereco() {
		return numEndereco;
	}
	public void setNumEndereco(String numEndereco) {
		if (numEndereco != null)
		{
			this.numEndereco = numEndereco;
		}
		else
		{
			this.numEndereco = "";
		}
	}
	public String getComplementEndereco() {
		return complementEndereco;
	}
	public void setComplementEndereco(String complementEndereco) {
		if (complementEndereco != null)
		{
			this.complementEndereco = complementEndereco;
		}
		else
		{
			this.complementEndereco = "";
		}
	}
	public String getNmBairro() {
		return nmBairro;
	}
	public void setNmBairro(String nmBairro) {
		if (nmBairro != null)
		{
			this.nmBairro = nmBairro;
		}
		else
		{
			this.nmBairro = "";
		}
	}
	public String getNmMunicipio() {
		return nmMunicipio;
	}
	public void setNmMunicipio(String nmMunicipio) {
		if (nmMunicipio != null)
		{
			this.nmMunicipio = nmMunicipio;
		}
		else
		{
			this.nmMunicipio = "";
		}
	}
	public String getSgEstado() {
		return sgEstado;
	}
	public void setSgEstado(String sgEstado) {
		if (sgEstado != null)
		{
			this.sgEstado = sgEstado;
		}
		else
		{
			this.sgEstado = "";
		}
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		if (cep != null)
		{
			this.cep = cep;
		}
		else
		{
			this.cep = "";
		}
	}
	public byte[] getBlbArquivo() {
		return blbArquivo;
	}
	public void setBlbArquivo(byte[] blbArquivo) {
		this.blbArquivo = blbArquivo;
	}
}
