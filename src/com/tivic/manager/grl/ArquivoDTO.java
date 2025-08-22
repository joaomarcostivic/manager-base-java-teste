package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class ArquivoDTO extends Arquivo {
	
	private byte[] strArquivo;

	public ArquivoDTO() {
		super();
	}

	public ArquivoDTO(int cdArquivo, String nmArquivo, GregorianCalendar dtArquivamento, String nmDocumento,
			byte[] strArquivo, int cdTipoArquivo, GregorianCalendar dtCriacao, int cdUsuario, int stArquivo,
			GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdAssinatura, String txtOcr) {
		
		super(cdArquivo, nmArquivo, dtArquivamento, nmDocumento, null, cdTipoArquivo, dtCriacao, cdUsuario, stArquivo,
				dtInicial, dtFinal, cdAssinatura, txtOcr, 0, null, null);
		
		this.strArquivo = strArquivo;
	}

	public byte[] getStrArquivo() {
		return strArquivo;
	}

	public void setStrArquivo(byte[] strArquivo) {
		this.strArquivo = strArquivo;
	}

}
