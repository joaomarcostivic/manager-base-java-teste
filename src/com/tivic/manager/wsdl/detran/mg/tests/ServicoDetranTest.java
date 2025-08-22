package com.tivic.manager.wsdl.detran.mg.tests;

import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.arquivomovimento.ArquivoMovimentoRepository;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ServicoDetranTest {

	public static void main (String args[]) throws Exception {
		//InicializationBeans.init(new InjectTestBuilder(ScopeEnum.TEST));
		ServicoDetranTest servicoDetranTest = new ServicoDetranTest();
		servicoDetranTest.testAit();
	}
	
	private ServicoDetranServices servicoDetranServices;
	private AitRepository aitRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	private ArquivoMovimentoRepository arquivoMovimentoRepository;
	
	public ServicoDetranTest() throws Exception {
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.arquivoMovimentoRepository = (ArquivoMovimentoRepository) BeansFactory.get(ArquivoMovimentoRepository.class);
	}
	
	public void testAit() throws Exception {
		
		
		List<AitMovimento> movimentos = new MovimentosTestBuilder()
			.addNovoAit("IO00001000", "GKO8068")
		.build();
		
		List<ServicoDetranDTO> listDetranDto = this.servicoDetranServices.remessa(movimentos);
		
		
		System.out.println("Retornos: " + listDetranDto);
		System.out.println("Aits:");
		System.out.println(aitRepository.find(new SearchCriterios()));
		System.out.println("Movimentos:");
		System.out.println(aitMovimentoRepository.find(new SearchCriterios()));
		System.out.println("Arquivos:");
		System.out.println(arquivoMovimentoRepository.find(new SearchCriterios()));
	}
	
	public void testCancelamentoAit() throws Exception {
		
		List<AitMovimento> movimentosNecessarios = new MovimentosTestBuilder()
			.addNovoAit("IO00001000", "GKO8068")
		.build();
		
		for(AitMovimento movimento : movimentosNecessarios) {
			movimento.setLgEnviadoDetran(1);
			this.aitMovimentoRepository.insert(movimento, new CustomConnection());
		}
		
		
		List<AitMovimento> movimentos = new MovimentosTestBuilder()
			.addCancelamentoAit(1)
		.build();
		
		List<ServicoDetranDTO> listDetranDto = this.servicoDetranServices.remessa(movimentos);
		
		System.out.println("Retornos: " + listDetranDto);
		System.out.println("Aits:");
		System.out.println(aitRepository.find(new SearchCriterios()));
		System.out.println("Movimentos:");
		System.out.println(aitMovimentoRepository.find(new SearchCriterios()));
		System.out.println("Arquivos:");
		System.out.println(arquivoMovimentoRepository.find(new SearchCriterios()));
	}
	
}
