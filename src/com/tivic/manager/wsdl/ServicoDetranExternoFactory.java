package com.tivic.manager.wsdl;

import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.wsdl.detran.mg.advertenciadefesa.AdvertenciaDefesa;
import com.tivic.manager.wsdl.detran.mg.advertenciadefesa.externo.AdvertenciaDefesaExterna;
import com.tivic.manager.wsdl.detran.mg.cancelarautoinfracao.CancelarAutoInfracao;
import com.tivic.manager.wsdl.detran.mg.incluirautoinfracao.IncluirAutoInfracao;
import com.tivic.manager.wsdl.detran.mg.notificacao.Notificacao;
import com.tivic.manager.wsdl.detran.mg.reaberturaprazo.ReaberturaPrazo;
import com.tivic.manager.wsdl.detran.mg.recursodefesa.IncluirDefesa;
import com.tivic.manager.wsdl.detran.mg.recursodefesa.externo.DefesaPreviaExterna;
import com.tivic.manager.wsdl.detran.mg.recursofici.IncluirFici;
import com.tivic.manager.wsdl.detran.mg.recursofici.externo.IncluirFiciExterno;
import com.tivic.manager.wsdl.detran.mg.recursojari.IncluirRecursoJari;
import com.tivic.manager.wsdl.detran.mg.recursojari.externo.RecursoJariExterno;
import com.tivic.manager.wsdl.detran.mg.soap.ArquivoConfiguracaoMg;
import com.tivic.manager.wsdl.detran.mg.soap.ArquivoConfiguracaoMgHomologacao;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranRecurso;

public class ServicoDetranExternoFactory {
	
public static final int DEFAULT = -1;
	
	public static final String MG = "MG";
	public static final String BA = "BA";
	public static final String SP = "SP";
	
	public static final int APRESENTACAO_CONDUTOR 	= 3;
	public static final int DEFESA_PREVIA		 	= 4;
	public static final int RECURSO_JARI		 	= 5;
	public static final int ADVERTENCIA_DEFESA	= 7;
	
	
	public static ServicoDetranRecurso gerarServico(String sgEstado, int tpStatus, boolean isProducao) throws Exception{
		ServicoDetranRecurso servicoDetran = null;
		switch(sgEstado){
			case MG:
				servicoDetran = servicosMinas(tpStatus, isProducao);
			break;
			case BA:
				servicoDetran = servicosBahia(tpStatus, isProducao);
			break;
			case SP:
				servicoDetran = servicosSaoPaulo(tpStatus, isProducao);
			break;
		}
		return servicoDetran;
	}
	
	private static ServicoDetranRecurso servicosMinas(int tpStatus, boolean isProducao) throws Exception{
		ServicoDetranRecurso servicoDetran = null;
		switch(convertStatusToServicoMinas(tpStatus)){

			case APRESENTACAO_CONDUTOR:
				servicoDetran = new IncluirFiciExterno();
			break;

			case DEFESA_PREVIA:
				servicoDetran = new DefesaPreviaExterna();
			break;

			case RECURSO_JARI:
				servicoDetran = new RecursoJariExterno();
			break;
			
			case ADVERTENCIA_DEFESA: 
				servicoDetran = new AdvertenciaDefesaExterna();
			break;
			
			default:
				throw new Exception("Só é possível enviar recursos em protocolos externos");
		}
		
		
		if(isProducao)
			servicoDetran.setArquivoConfiguracao(new ArquivoConfiguracaoMg());
		else
			servicoDetran.setArquivoConfiguracao(new ArquivoConfiguracaoMgHomologacao());
		
		return servicoDetran;
	}
	
	private static ServicoDetranRecurso servicosBahia(int servico, boolean isProducao){
		ServicoDetranRecurso servicoDetran = null;
		//IMPLEMENTAR SERVICOS
		return servicoDetran;
	}
	
	private static ServicoDetranRecurso servicosSaoPaulo(int servico, boolean isProducao){
		ServicoDetranRecurso servicoDetran = null;
		return servicoDetran;
	}
	
	private static int convertStatusToServicoMinas(int tpStatus){
		switch(tpStatus){

			case AitMovimentoServices.TRANSFERENCIA_PONTUACAO:
				return APRESENTACAO_CONDUTOR;

			case AitMovimentoServices.DEFESA_PREVIA:
				return DEFESA_PREVIA;

			case AitMovimentoServices.RECURSO_JARI:
				return RECURSO_JARI;
				
			case AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA:
				return ADVERTENCIA_DEFESA;
				
			default:
				return DEFAULT;
			
		}
	}

}
