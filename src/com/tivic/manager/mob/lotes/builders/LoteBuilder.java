package com.tivic.manager.mob.lotes.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.lotes.model.Lote;

public class LoteBuilder {
	
	private Lote lote;
	
	public LoteBuilder() {
		lote = new Lote();
	}
	
	public LoteBuilder setCdLote(int cdLote) {
		lote.setCdLote(cdLote);
		return this;
	}
	
	public LoteBuilder setIdLote(String idLote) {
		lote.setIdLote(idLote);
		return this;
	}
	
	public LoteBuilder setDtCriacao(GregorianCalendar dtCriacao) {
		lote.setDtCriacao(dtCriacao);
		return this;
	}
	
	public LoteBuilder setCdCriador(int cdUsuario) {
		lote.setCdCriador(cdUsuario);
		return this;
	}
	
	public LoteBuilder setCdArquivo(int cdArquivo) {
		lote.setCdArquivo(cdArquivo);
		return this;
	}
	
	public Lote build() {
		return lote;
	}
}
