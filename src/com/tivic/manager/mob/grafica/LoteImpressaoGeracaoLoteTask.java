package com.tivic.manager.mob.grafica;

public class LoteImpressaoGeracaoLoteTask implements Runnable{
	private int cdLoteImpressao;
	private int cdUsuario;
	
	public LoteImpressaoGeracaoLoteTask(int cdLoteImpressao, int cdUsuario) {
		this.cdLoteImpressao = cdLoteImpressao;	
		this.cdUsuario = cdUsuario;
	}
	 
	public void run() {
		LoteImpressaoServices.gerarLote(cdLoteImpressao, cdUsuario);
	}
}
