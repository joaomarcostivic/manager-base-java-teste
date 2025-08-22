package com.tivic.manager.wsdl.detran.mg.soap.backup;

import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public interface Backup {
	public void gerarEntrada(DadosEntradaMG dadosEntrada);
	public void gerarRetorno(DadosEntradaMG dadosEntrada, String retorno);
}
