package com.tivic.manager.wsdl.detran.mg;

import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.wsdl.detran.mg.advertenciadefesa.AdvertenciaDefesa;
import com.tivic.manager.wsdl.detran.mg.advertenciadefesa.cancelamento.CancelarAdvertenciaDefesa;
import com.tivic.manager.wsdl.detran.mg.baixas.Baixa;
import com.tivic.manager.wsdl.detran.mg.cancelarautoinfracao.CancelarAutoInfracao;
import com.tivic.manager.wsdl.detran.mg.correios.Correios;
import com.tivic.manager.wsdl.detran.mg.incluirautoinfracao.IncluirAutoInfracao;
import com.tivic.manager.wsdl.detran.mg.notificacao.Notificacao;
import com.tivic.manager.wsdl.detran.mg.publicacao.Publicacao;
import com.tivic.manager.wsdl.detran.mg.reaberturaprazo.ReaberturaPrazo;
import com.tivic.manager.wsdl.detran.mg.recursodefesa.IncluirDefesa;
import com.tivic.manager.wsdl.detran.mg.recursodefesa.cancelamento.CancelarDefesa;
import com.tivic.manager.wsdl.detran.mg.recursofici.IncluirFici;
import com.tivic.manager.wsdl.detran.mg.recursofici.cancelamento.CancelarFici;
import com.tivic.manager.wsdl.detran.mg.recursojari.IncluirRecursoJari;
import com.tivic.manager.wsdl.detran.mg.recursojari.cancelamento.CancelarRecursoJari;
import com.tivic.manager.wsdl.detran.mg.recursonic.IncluirNIC;
import com.tivic.manager.wsdl.detran.mg.soap.ArquivoConfiguracaoMg;
import com.tivic.manager.wsdl.detran.mg.soap.ArquivoConfiguracaoMgHomologacao;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;

public class ServicoDetranFactoryMG {

	public static final int DEFAULT = -1;
	
	public static final int REMESSA 				= 0;
	public static final int CANCELAMENTO 			= 1;
	public static final int NOTIFICACAO 			= 2;
	public static final int APRESENTACAO_CONDUTOR 	= 3;
	public static final int DEFESA		 			= 4;
	public static final int RECURSO_JARI		 	= 5;
	public static final int CETRAN 					= 6;
	public static final int BAIXA 					= 7;
	public static final int REABERTURA				= 8;
	public static final int ADVERTENCIA_DEFESA		= 9;
	public static final int CANCELAMENTO_DEFESA		= 10;
	public static final int CANCELAMENTO_ADVERTENCIA= 11;
	public static final int CANCELAMENTO_FICI		= 12;
	public static final int CANCELAMENTO_JARI		= 13;
	public static final int CANCELAMENTO_CETRAN		= 14;
	public static final int CANCELAMENTO_NIP		= 15;
	public static final int PUBLICACAO				= 16;
	public static final int EFEITO_SUSPENSIVO	 	= 17;
	public static final int NIC	 					= 18;
	public static final int DADOS_CORREIOS          = 19;
	
	
	public static ServicoDetran gerarServico(int tpStatus, boolean isProducao) throws Exception{
		ServicoDetran servicoDetran = null;
		switch(convertStatusToServico(tpStatus)){
			case REMESSA:
				servicoDetran = new IncluirAutoInfracao();
			break;
			
			case CANCELAMENTO:
				servicoDetran = new CancelarAutoInfracao();
				break;
			
			case NOTIFICACAO:
				servicoDetran = new Notificacao();
				break;

			case APRESENTACAO_CONDUTOR:
				servicoDetran = new IncluirFici();
				break;

			case DEFESA:
				servicoDetran = new IncluirDefesa();
				break;

			case RECURSO_JARI:
				servicoDetran = new IncluirRecursoJari();
				break;
			
			case CETRAN:
				servicoDetran = new IncluirRecursoJari();
				break;
			
			case BAIXA:
				servicoDetran = new Baixa();
				break;
				
			case REABERTURA:
				servicoDetran = new ReaberturaPrazo();
				break;
				
			case ADVERTENCIA_DEFESA:
				servicoDetran = new AdvertenciaDefesa();
				break;
				
			case CANCELAMENTO_DEFESA:
				servicoDetran = new CancelarDefesa();
				break;						
			
			case CANCELAMENTO_FICI:
				servicoDetran = new CancelarFici();
				break;
			
			case CANCELAMENTO_JARI:
				servicoDetran = new CancelarRecursoJari();
				break;
			
			case CANCELAMENTO_CETRAN:
				servicoDetran = new CancelarRecursoJari();
				break;
				
			case CANCELAMENTO_ADVERTENCIA:
				servicoDetran = new CancelarAdvertenciaDefesa();
				break;
				
			case PUBLICACAO:
				servicoDetran = new Publicacao();
				break;

			case EFEITO_SUSPENSIVO:
				servicoDetran = new IncluirRecursoJari();
				break;

			case NIC:
				servicoDetran = new IncluirNIC();
				break;
				
			case DADOS_CORREIOS:
				servicoDetran = new Correios();
				break;
				
			default:
				throw new Exception("Nenhum serviço encontrado");
		}
		
		
		if(isProducao)
			servicoDetran.setArquivoConfiguracao(new ArquivoConfiguracaoMg());
		else
			servicoDetran.setArquivoConfiguracao(new ArquivoConfiguracaoMgHomologacao());
		
		return servicoDetran;
	}
	

	private static int convertStatusToServico(int tpStatus){
		switch(tpStatus){
			case AitMovimentoServices.REGISTRO_INFRACAO:
				return REMESSA;
				
			case AitMovimentoServices.CADASTRO_CANCELADO:
			case AitMovimentoServices.CANCELA_REGISTRO_MULTA:
			case AitMovimentoServices.CANCELAMENTO_AUTUACAO:
			case AitMovimentoServices.CANCELAMENTO_MULTA:
			case AitMovimentoServices.CANCELAMENTO_PAGAMENTO:
			case AitMovimentoServices.CANCELAMENTO_NIP:
				return CANCELAMENTO;
			
			case AitMovimentoServices.NAI_ENVIADO:
			case AitMovimentoServices.NIP_ENVIADA:
			case AitMovimentoServices.NOVO_PRAZO_DEFESA:
	        case AitMovimentoServices.NOVO_PRAZO_JARI:
	        case AitMovimentoServices.NOTIFICACAO_ADVERTENCIA:
				return NOTIFICACAO;

			case AitMovimentoServices.TRANSFERENCIA_PONTUACAO:
				return APRESENTACAO_CONDUTOR;

			case AitMovimentoServices.DEFESA_PREVIA:
			case AitMovimentoServices.DEFESA_DEFERIDA:
			case AitMovimentoServices.DEFESA_INDEFERIDA:
			case AitMovimentoServices.FIM_PRAZO_DEFESA:
				return DEFESA;

			case AitMovimentoServices.RECURSO_JARI:
			case AitMovimentoServices.JARI_COM_PROVIMENTO:
			case AitMovimentoServices.JARI_SEM_PROVIMENTO:
			case AitMovimentoServices.PUBLICACAO_RESULTADO_JARI:
				return RECURSO_JARI;
				
			case AitMovimentoServices.RECURSO_CETRAN:
			case AitMovimentoServices.CETRAN_DEFERIDO:
			case AitMovimentoServices.CETRAN_INDEFERIDO:
				return CETRAN;
				
			case AitMovimentoServices.MULTA_PAGA:
			case AitMovimentoServices.SUSPENSAO_DIVIDA_ATIVA:
			case AitMovimentoServices.BAIXA_DESVINCULADA_LEILAO:
			case AitMovimentoServices.CANCELAMENTO_DIVIDA_ATIVA:
				return BAIXA;
				
			case AitMovimentoServices.REABERTURA_FICI:
			case AitMovimentoServices.REABERTURA_DEFESA:
			case AitMovimentoServices.REABERTURA_JARI:
				return REABERTURA;
				
			case AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA:
			case AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA:
			case AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA:
				return ADVERTENCIA_DEFESA;
				
			case AitMovimentoServices.CANCELAMENTO_DEFESA_PREVIA:
			case AitMovimentoServices.CANCELAMENTO_DEFESA_DEFERIDA:
			case AitMovimentoServices.CANCELAMENTO_DEFESA_INDEFERIDA:
				return CANCELAMENTO_DEFESA;
				
			case AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_ENTRADA:
			case AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA:
			case AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA:
				return CANCELAMENTO_ADVERTENCIA;
				
			case AitMovimentoServices.CANCELAMENTO_TRANSFERENCIA_PONTUACAO:
				return CANCELAMENTO_FICI;
				
			case AitMovimentoServices.CANCELAMENTO_RECURSO_JARI:
			case AitMovimentoServices.CANCELAMENTO_JARI_COM_PROVIMENTO:
			case AitMovimentoServices.CANCELAMENTO_JARI_SEM_PROVIMENTO:
			case AitMovimentoServices.CANCELAMENTO_PUBLICACAO_RESULTADO_JARI:
				return CANCELAMENTO_JARI;
				
			case AitMovimentoServices.CANCELAMENTO_RECURSO_CETRAN:
			case AitMovimentoServices.CANCELAMENTO_CETRAN_COM_PROVIMENTO:
			case AitMovimentoServices.CANCELAMENTO_CETRAN_SEM_PROVIMENTO:
				return CANCELAMENTO_CETRAN;
				
			case AitMovimentoServices.PUBLICACAO_NAI:
			case AitMovimentoServices.PUBLICACAO_NIP:
				return PUBLICACAO;

			case AitMovimentoServices.PENALIDADE_SUSPENSA:
			case AitMovimentoServices.PENALIDADE_REATIVADA:
				return EFEITO_SUSPENSIVO;
				
			case AitMovimentoServices.NIC_ENVIADO:
				return NIC;
				
			case AitMovimentoServices.DADOS_CORREIO_NA:
			case AitMovimentoServices.DADOS_CORREIO_NP:
				return DADOS_CORREIOS;
				
			default:
				return DEFAULT;
			
		}
	}
	
}
