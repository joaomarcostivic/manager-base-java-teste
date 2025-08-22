package com.tivic.manager.mob.grafica;

public class LoteImpressaoGeracaoDocumentosTask implements Runnable {
	private int cdLoteImpressao;
	private int cdUsuario;
	
	public LoteImpressaoGeracaoDocumentosTask(int cdLoteImpressao, int cdUsuario) {
		this.cdLoteImpressao = cdLoteImpressao;	
		this.cdUsuario = cdUsuario;
	}
	 
	public void run() {
		try {
			LoteImpressaoServices.gerarDocumentos(cdLoteImpressao, cdUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
