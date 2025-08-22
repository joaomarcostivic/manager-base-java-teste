package com.tivic.manager.wsdl.detran.mg.soap.backup;

import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;
import com.tivic.sol.util.date.DateUtil;

public class NomeArquivoBackupSemAIT implements INomeArquivoBackup {

	private DadosEntradaMG dadosEntrada;
	
	public NomeArquivoBackupSemAIT(DadosEntradaMG dadosEntrada) {
		this.dadosEntrada = dadosEntrada;
	}
	
	@Override
	public String build(String path, String sufixo) {
		return path +"/"+dadosEntrada.getNmServico() + "_" + DateUtil.formatDate(DateUtil.getDataAtual(), "ddMMyyyyHHmmss")+"_"+sufixo+".XML";
	}

}
