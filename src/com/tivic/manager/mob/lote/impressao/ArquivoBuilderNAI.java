package com.tivic.manager.mob.lote.impressao;

import java.util.GregorianCalendar;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoServices;

public class ArquivoBuilderNAI {
	
	private Arquivo arquivo;
	
	public ArquivoBuilderNAI(String idNotificacao, byte[] content, int cdUsuario) {
		arquivo = new Arquivo();
		arquivo.setCdArquivo(0);
		arquivo.setNmArquivo("NAI AIT "+idNotificacao);
		arquivo.setDtArquivamento(new GregorianCalendar());
		arquivo.setNmDocumento("nai_"+idNotificacao+".pdf");
		arquivo.setBlbArquivo(content);
		arquivo.setDtCriacao(new GregorianCalendar());
		arquivo.setCdUsuario(cdUsuario);
		arquivo.setStArquivo(ArquivoServices.ST_ENVIADO);
	}
	
	public Arquivo build() {
		return arquivo;
	}
	
}
