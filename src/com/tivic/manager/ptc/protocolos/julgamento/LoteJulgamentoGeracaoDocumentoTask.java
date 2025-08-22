package com.tivic.manager.ptc.protocolos.julgamento;

import com.tivic.sol.cdi.BeansFactory;

public class LoteJulgamentoGeracaoDocumentoTask implements Runnable {
	private ICartaJulgamentoService cartaJulgamentoService;
	private int cdLoteImpressao;
	private int cdUsuario;
	
	public LoteJulgamentoGeracaoDocumentoTask(int cdLoteImpressao, int cdUsuario) throws Exception {
		cartaJulgamentoService = (ICartaJulgamentoService) BeansFactory.get(ICartaJulgamentoService.class);
		this.cdLoteImpressao = cdLoteImpressao;	
		this.cdUsuario = cdUsuario;
	}
	 
	public void run() {
		try {
			cartaJulgamentoService.gerarDocumentosLote(cdLoteImpressao, cdUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
