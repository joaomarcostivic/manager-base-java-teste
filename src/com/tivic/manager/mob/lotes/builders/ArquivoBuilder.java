package com.tivic.manager.mob.lotes.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.lotes.model.arquivo.Arquivo;

public class ArquivoBuilder {
	private Arquivo arquivo = new Arquivo();
	
	public ArquivoBuilder() {
		arquivo.setDtArquivamento(new GregorianCalendar());
		arquivo.setDtCriacao(new GregorianCalendar());
	}
	
	public ArquivoBuilder setCdArquivo(int cdArquivo) {
		arquivo.setCdArquivo(cdArquivo);
		return this;
	}
	
	public ArquivoBuilder setNmArquivo(String nmArquivo) {
		arquivo.setNmArquivo(nmArquivo);
		return this;
	}
	
	public ArquivoBuilder setNmDocumento(String nmDocumento) {
		arquivo.setNmDocumento(nmDocumento);
		return this;
	}
	
	public ArquivoBuilder setBlbArquivo(byte[] blbArquivo) {
		arquivo.setBlbArquivo(blbArquivo);
		return this;
	}
	
	public ArquivoBuilder setCdUsuario(int cdUsuario) {
		arquivo.setCdUsuario(cdUsuario);
		return this;
	}
	
	public Arquivo build() {
		return arquivo;
	}
}
