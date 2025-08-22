package com.tivic.manager.wsdl.detran.mg.soap.backup;

import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class NomeArquivoBackupFactory {
	
	public INomeArquivoBackup getStrategy(DadosEntradaMG dadosEntrada) {
		if (dadosEntrada.getItens().get("ait") != null) {
			return new NomeArquivoBackupComAIT(dadosEntrada);
		}
		else {
			return new NomeArquivoBackupSemAIT(dadosEntrada);
		}
	}
	
}
