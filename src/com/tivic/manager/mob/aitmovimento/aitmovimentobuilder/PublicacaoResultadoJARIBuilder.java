package com.tivic.manager.mob.aitmovimento.aitmovimentobuilder;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.AitMovimento;

public class PublicacaoResultadoJARIBuilder {
	private AitMovimento aitMovimento;
	
	public PublicacaoResultadoJARIBuilder() {
		this.aitMovimento = new AitMovimento();
	}
	
	public PublicacaoResultadoJARIBuilder setCdAit(int cdAit) {
		this.aitMovimento.setCdAit(cdAit);
		return this;
	}
	
	public PublicacaoResultadoJARIBuilder setDtMovimento(GregorianCalendar dtMovimento) {
		this.aitMovimento.setDtMovimento(dtMovimento);
		return this;
	}
	
	public PublicacaoResultadoJARIBuilder setTpStatus(int tpStatus) {
		this.aitMovimento.setTpStatus(tpStatus);
		return this;
	}
	
	public PublicacaoResultadoJARIBuilder setDtPublicacaoDo(GregorianCalendar dtPublicacaoDo) {
		this.aitMovimento.setDtPublicacaoDo(dtPublicacaoDo);
		return this;
	}
	
	public PublicacaoResultadoJARIBuilder setStPublicacaoDo(int stPublicacaoDo) {
		this.aitMovimento.setStPublicacaoDo(stPublicacaoDo);
		return this;
	}
	
	public PublicacaoResultadoJARIBuilder setNrProcesso(String nrProcesso) {
		this.aitMovimento.setNrProcesso(nrProcesso);
		return this;
	}
	
	public PublicacaoResultadoJARIBuilder setCdUsuario(int cdUsuario) {
		this.aitMovimento.setCdUsuario(cdUsuario);
		return this;
	}
	
	public AitMovimento build() {
		return this.aitMovimento;
	}
}
