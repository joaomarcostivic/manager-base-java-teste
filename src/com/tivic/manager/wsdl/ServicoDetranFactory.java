package com.tivic.manager.wsdl;

import com.tivic.manager.wsdl.detran.mg.ServicoDetranFactoryMG;
import com.tivic.manager.wsdl.detran.sp.ServicoDetranFactorySP;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;

public class ServicoDetranFactory {

	public static final int DEFAULT = -1;
	
	public static final String MG = "MG";
	public static final String BA = "BA";
	public static final String SP = "SP";
	public static final String PA = "PA";
	
	public static ServicoDetran gerarServico(String sgEstado, int tpStatus, boolean isProducao) throws Exception{
		ServicoDetran servicoDetran = null;
		switch(sgEstado){
			case MG:
				servicoDetran = ServicoDetranFactoryMG.gerarServico(tpStatus, isProducao);
			break;
			case BA:
				servicoDetran = servicosBahia(tpStatus, isProducao);
			break;
			case SP:
			case PA:
				servicoDetran = ServicoDetranFactorySP.gerarServico(tpStatus, isProducao);
			break;
		}
		return servicoDetran;
	}
	
	private static ServicoDetran servicosBahia(int servico, boolean isProducao){
		ServicoDetran servicoDetran = null;
		//IMPLEMENTAR SERVICOS
		return servicoDetran;
	}
	
}
