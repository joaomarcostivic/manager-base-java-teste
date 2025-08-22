package com.tivic.manager.wsdl;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.wsdl.detran.ba.soap.ArquivoConfiguracaoBa;
import com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual.ConsultaAutoBaseEstadual;
import com.tivic.manager.wsdl.detran.mg.consultaautobasenacional.ConsultaAutoBaseNacional;
import com.tivic.manager.wsdl.detran.mg.consultainfracoes.ConsultarInfracoes;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.ConsultarPontuacaoDadosCondutor;
import com.tivic.manager.wsdl.detran.mg.consultarmovimentacoes.ConsultarMovimentacoes;
import com.tivic.manager.wsdl.detran.mg.consultarplaca.ConsultarPlaca;
import com.tivic.manager.wsdl.detran.mg.consultarpossuidorplaca.ConsultarPossuidorPlaca;
import com.tivic.manager.wsdl.detran.mg.consultarrecursos.ConsultarRecursos;
import com.tivic.manager.wsdl.detran.mg.soap.ArquivoConfiguracaoMg;
import com.tivic.manager.wsdl.detran.mg.soap.ArquivoConfiguracaoMgHomologacao;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;

public class ServicoDetranConsultaFactory {

	public static final int PLACA 					 = 0;
	public static final int AUTO_BASE_NACIONAL 		 = 1;
	public static final int AUTO_BASE_ESTADUAL 		 = 2;
	public static final int MOVIMENTACOES    		 = 3;
	public static final int RECURSOS         	     = 4;
	public static final int PONTUACAO_DADOS_CONDUTOR = 5;
	public static final int INFRACOES 				 = 6;
	public static final int POSSUIDOR_PLACA			 = 7;
	
	public static ServicoDetran gerarServico(String sgEstado, int servico) {
		ServicoDetran servicoDetran = null;
		switch(sgEstado){
			case ServicoDetranFactory.MG:
				servicoDetran = servicosMinas(servico);
			break;
			case ServicoDetranFactory.BA:
				servicoDetran = servicosBahia(servico);
			break;
			case ServicoDetranFactory.SP:
				servicoDetran = servicosSaoPaulo(servico);
			break;
		}
		
		if(isProducao())
			servicoDetran.setArquivoConfiguracao(new ArquivoConfiguracaoMg());
		else
			servicoDetran.setArquivoConfiguracao(new ArquivoConfiguracaoMgHomologacao());
		
		return servicoDetran;
	}
	
	private static ServicoDetran servicosMinas(int servico) {
		ServicoDetran servicoDetran = null;
		switch(servico){
			case PLACA:
				servicoDetran = new ConsultarPlaca();
			break;
			
			case AUTO_BASE_NACIONAL:
				servicoDetran = new ConsultaAutoBaseNacional();
			break;			

			case AUTO_BASE_ESTADUAL:
				servicoDetran = new ConsultaAutoBaseEstadual();
			break;

			case MOVIMENTACOES:
				servicoDetran = new ConsultarMovimentacoes();
			break;

			case RECURSOS:
				servicoDetran = new ConsultarRecursos();
			break;
			
			case PONTUACAO_DADOS_CONDUTOR:
				servicoDetran = new ConsultarPontuacaoDadosCondutor();
				break;
				
			case INFRACOES:
				servicoDetran = new ConsultarInfracoes();
			break;
			
			case POSSUIDOR_PLACA:
				servicoDetran = new ConsultarPossuidorPlaca();
			break;
		}
		
		return servicoDetran;
	}
	
	private static ServicoDetran servicosBahia(int servico){
		ServicoDetran servicoDetran = null;
		switch(servico){
			case PLACA:
				servicoDetran = new com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlaca();
			break;
		}
		servicoDetran.setArquivoConfiguracao(new ArquivoConfiguracaoBa());
		return servicoDetran;
	}
	
	private static ServicoDetran servicosSaoPaulo(int servico){
		ServicoDetran servicoDetran = null;
		//IMPLEMENTAR SERVICOS
		return servicoDetran;
	}
	
	private static boolean isProducao() { 
		return ManagerConf.getInstance().getAsBoolean("PRODEMGE_PRODUCAO");
	}
}
