package com.tivic.manager.ptc.protocolos.julgamento;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoServices;

public class ArquivoBuilderJariIndeferida {

private Arquivo arquivo;
	
	public ArquivoBuilderJariIndeferida(String idResultado, byte[] content, int cdUsuario) {
		arquivo = new Arquivo();
		arquivo.setCdArquivo(0);
		arquivo.setNmArquivo("Jari Indeferida"+idResultado);
		arquivo.setDtArquivamento(new GregorianCalendar());
		arquivo.setNmDocumento("jari_sem_provimento_"+idResultado+".pdf");
		arquivo.setBlbArquivo(content);
		arquivo.setDtCriacao(new GregorianCalendar());
		arquivo.setCdUsuario(cdUsuario);
		arquivo.setStArquivo(ArquivoServices.ST_ENVIADO);
	}
	
	public Arquivo build() {
		return arquivo;
	}
	
}
