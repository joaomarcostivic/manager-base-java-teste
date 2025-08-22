package com.tivic.manager.mob.lote.impressao;

import com.tivic.sol.cdi.BeansFactory;

public class LoteNotificacaoGeracaoDocumentosTask implements Runnable{
	ILoteNotificacaoService loteNotificacaoService;
	private int cdLoteImpressao;
	private int cdUsuario;
	
	public LoteNotificacaoGeracaoDocumentosTask(int cdLoteImpressao, int cdUsuario) throws Exception {
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		this.cdLoteImpressao = cdLoteImpressao;	
		this.cdUsuario = cdUsuario;
	}
	 
	public void run() {
		try {
			loteNotificacaoService.gerarDocumentosLote(cdLoteImpressao, cdUsuario);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
