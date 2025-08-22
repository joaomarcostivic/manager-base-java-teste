package com.tivic.manager.ptc.protocolos.julgamento;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoServices;

public class ArquivoBuilderDefesaDeferida {
	
	private Arquivo arquivo;
	
	public ArquivoBuilderDefesaDeferida(String idResultado, byte[] content, int cdUsuario) {
		arquivo = new Arquivo();
		arquivo.setCdArquivo(0);
		arquivo.setNmArquivo("DEFESA_DEFERIDA_" + idResultado + ".pdf");
		arquivo.setDtArquivamento(new GregorianCalendar());
		arquivo.setNmDocumento("DEFESA DEFERIDA");
		arquivo.setBlbArquivo(content);
		arquivo.setDtCriacao(new GregorianCalendar());
		arquivo.setCdUsuario(cdUsuario);
		arquivo.setStArquivo(ArquivoServices.ST_ENVIADO);
	}
	
	public Arquivo build() {
		return arquivo;
	}
	
}
