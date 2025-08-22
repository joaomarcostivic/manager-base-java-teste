package com.tivic.manager.mob.lotes.impressao.task;

import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class LoteGeracaoDocumentosTask implements Runnable{
	ILoteImpressaoService loteImpressaoService;
	private int cdLoteImpressao;
	private int cdUsuario;
	
	public LoteGeracaoDocumentosTask(int cdLoteImpressao, int cdUsuario) throws Exception {
		loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		this.cdLoteImpressao = cdLoteImpressao;	
		this.cdUsuario = cdUsuario;
	}
	 
	public void run() {
		try {
			loteImpressaoService.iniciarGeracaoDocumentosAit(cdLoteImpressao, cdUsuario, new CustomConnection());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
