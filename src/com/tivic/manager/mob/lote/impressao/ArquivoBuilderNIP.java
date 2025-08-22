package com.tivic.manager.mob.lote.impressao;

import java.util.GregorianCalendar;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoServices;

public class ArquivoBuilderNIP {
	private Arquivo arquivo;
	
	public ArquivoBuilderNIP(String idNotificacao, byte[] content, int cdUsuario) {
		arquivo = new Arquivo();
		arquivo.setCdArquivo(0);
		arquivo.setNmArquivo("NIP AIT "+idNotificacao);
		arquivo.setDtArquivamento(new GregorianCalendar());
		arquivo.setNmDocumento("nip_"+idNotificacao+".pdf");
		arquivo.setBlbArquivo(content);
		arquivo.setDtCriacao(new GregorianCalendar());
		arquivo.setCdUsuario(cdUsuario);
		arquivo.setStArquivo(ArquivoServices.ST_ENVIADO);
	}
	
	public Arquivo build() {
		return arquivo;
	}
}
