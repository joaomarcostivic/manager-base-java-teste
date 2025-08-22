package com.tivic.manager.grl;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class ArquivoPostagemDOBuilder {
	
	public ArquivoPostagemDOBuilder() {}
	
	public Arquivo build(byte[] blbArquivo, String tipoEdital, int tpArquivo) {
		Arquivo arquivo = new Arquivo();
		
		SimpleDateFormat df = new SimpleDateFormat("dd_MM_yyyy");
		GregorianCalendar data = new GregorianCalendar();

		arquivo.setBlbArquivo(blbArquivo);
		arquivo.setDtArquivamento(new GregorianCalendar());
		arquivo.setDtCriacao(new GregorianCalendar());
		arquivo.setNmDocumento(tipoEdital + "'s Publicadas No Diário Oficial");
		arquivo.setNmArquivo("Documento-Postagem-" + tipoEdital + "_" + df.format(data.getTime()));
		arquivo.setCdTipoArquivo(ArquivoServices.verificarTipoArquivo(tpArquivo));
		
		return arquivo;
	}
	
}
