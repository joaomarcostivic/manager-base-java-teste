package com.tivic.manager.mob.ecarta.builders;

import com.tivic.manager.mob.ecarta.dtos.ECartaItemSV;

public class ECartaItemSVBuilder {
	
	private ECartaItemSV eCartaItemSV;
	
	public ECartaItemSVBuilder() {
		this.eCartaItemSV = new ECartaItemSV();
	}
	
	public ECartaItemSVBuilder setNumeroLote(String numeroLote) {
		this.eCartaItemSV.setNrLote(numeroLote);
		return this;
	}
	
	public ECartaItemSVBuilder setNumeroEtiqueta(String numeroEtiqueta) {
		this.eCartaItemSV.setNrEtiqueta(numeroEtiqueta);
		return this;
	}
	
	public ECartaItemSVBuilder setNomeDestinatario(String nomeDestinatario) {
		this.eCartaItemSV.setNmDestinatario(nomeDestinatario);
		return this;
	}
	
	public ECartaItemSVBuilder setDsLogradouro(String nomeLogradouro) {
		this.eCartaItemSV.setDsLogradouro(nomeLogradouro);
		return this;
	}
	
	public ECartaItemSVBuilder setNumEndereco(String numEndereco) {
		this.eCartaItemSV.setNrEndereco(numEndereco);
		return this;
	}
	
	public ECartaItemSVBuilder setComplementoEndereco(String complementoEndereco) {
		this.eCartaItemSV.setComplementoEndereco(complementoEndereco);
		return this;
	}
	
	public ECartaItemSVBuilder setNmBairro(String nmBairro) {
		this.eCartaItemSV.setNmBairro(nmBairro);
		return this;
	}
	
	public ECartaItemSVBuilder setNmMunicipio(String nmMunicipio) {
		this.eCartaItemSV.setNmMunicipio(nmMunicipio);
		return this;
	}
	
	public ECartaItemSVBuilder setSgEstado(String sgEstado) {
		this.eCartaItemSV.setSgEstado(sgEstado);
		return this;
	}
	
	public ECartaItemSVBuilder setNrCep(String cep) {
		this.eCartaItemSV.setNrCep(cep);
		return this;
	}
	
	public ECartaItemSVBuilder setBlbNotificacao(byte[] blbNotificacao) {
		this.eCartaItemSV.setBlbNotificacao(blbNotificacao);
		return this;
	}
	
	public ECartaItemSVBuilder setCdAit(String cdAit) {
		this.eCartaItemSV.setCdAit(cdAit);
		return this;
	}
	
	public ECartaItemSV build() {
		return this.eCartaItemSV;
	}
	
}
