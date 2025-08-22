package com.tivic.manager.grl.ArquivoBuilder;

import java.util.GregorianCalendar;
import com.tivic.manager.grl.Arquivo;

public class ArquivoBuilder {
	private Arquivo arquivo;
	
	public ArquivoBuilder() {
		arquivo = new Arquivo();
	}
	
	public ArquivoBuilder setCdArquivo(int cdArquivo) {
		arquivo.setCdArquivo(cdArquivo);
		return this;
	}
	
	public ArquivoBuilder setNmArquivo(String nmArquivo) {
		arquivo.setNmArquivo(nmArquivo);
		return this;
	}
	
	public ArquivoBuilder setDtArquivamento(GregorianCalendar dtArquivamento) {
		arquivo.setDtArquivamento(dtArquivamento);
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
	
	public ArquivoBuilder setCdTipoArquivo(int cdTipoArquivo) {
		arquivo.setCdTipoArquivo(cdTipoArquivo);
		return this;
	}
	
	public ArquivoBuilder setDtCriacao(GregorianCalendar dtCriacao) {
		arquivo.setDtCriacao(dtCriacao);
		return this;
	}
	
	public ArquivoBuilder setCdUsuario(int cdUsuario) {
		arquivo.setCdUsuario(cdUsuario);
		return this;
	}
	
	public ArquivoBuilder setStArquivo(int stArquivo) {
		arquivo.setStArquivo(stArquivo);
		return this;
	}
	
	public ArquivoBuilder setDtInicial(GregorianCalendar dtInicial) {
		arquivo.setDtInicial(dtInicial);
		return this;
	}
	
	public ArquivoBuilder setDtFinal(GregorianCalendar dtFinal) {
		arquivo.setDtFinal(dtFinal);
		return this;
	}
	
	public ArquivoBuilder setCdAssinatura(int cdAssinatura) {
		arquivo.setCdAssinatura(cdAssinatura);
		return this;
	}
	
	public ArquivoBuilder setTxtOcr(String txtOcr) {
		arquivo.setTxtOcr(txtOcr);
		return this;
	}
	
	public ArquivoBuilder setNrRegistros(int nrRegistros) {
		arquivo.setNrRegistros(nrRegistros);
		return this;
	}
	
	public Arquivo build() {
		return arquivo;
	}
}
