package com.tivic.manager.mob.ecarta;

import com.tivic.manager.mob.ecarta.services.IArquivoServiceEDI;
import com.tivic.sol.cdi.BeansFactory;

public class ECartasGerarArquivoServicoEDITask implements Runnable {
	private IArquivoServiceEDI arquivoServiceEDI;
	private int cdLoteImpressao;
	private int cdUsuario;
	
	public ECartasGerarArquivoServicoEDITask(int cdLoteImpressao, int cdUsuario) throws Exception {
		this.arquivoServiceEDI = (IArquivoServiceEDI) BeansFactory.get(IArquivoServiceEDI.class);
		this.cdLoteImpressao = cdLoteImpressao;	
		this.cdUsuario = cdUsuario;
	}
	 
	public void run() {
		try {
			arquivoServiceEDI.gerar(cdLoteImpressao, cdUsuario);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
}
