package com.tivic.manager.wsdl.detran.sp;

import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.wsdl.detran.sp.ftp.ArquivoConfiguracaoSp;
import com.tivic.manager.wsdl.detran.sp.incluirautoinfracao.IncluirAutoInfracao;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;

public class ServicoDetranFactorySP {

	public static final int DEFAULT = -1;
	
	public static final int REMESSA 				= 0;
	public static final int CANCELAMENTO 			= 1;
	public static final int REGISTRO_NAI 			= 2;
	public static final int APRESENTACAO_CONDUTOR 	= 3;
	public static final int DEFESA_PREVIA		 	= 4;
	public static final int RECURSO_JARI		 	= 5;
	
	
	public static ServicoDetran gerarServico(int tpStatus, boolean isProducao) throws Exception{
		ServicoDetran servicoDetran = null;
		switch(convertStatusToServico(tpStatus)){
			case REMESSA:
				servicoDetran = new IncluirAutoInfracao();
			break;
			
			default:
				throw new Exception("Nenhum serviço encontrado");
		}
		
		servicoDetran.setArquivoConfiguracao(new ArquivoConfiguracaoSp());
		
		return servicoDetran;
	}
	

	private static int convertStatusToServico(int tpStatus){
		switch(tpStatus){
			case AitMovimentoServices.REGISTRO_INFRACAO:
				return REMESSA;
			default:
				return DEFAULT;
		}
	}
	
}
