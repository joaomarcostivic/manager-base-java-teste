package com.tivic.test.manager.tasks.limpeza.lotes;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoArquivo;
import com.tivic.manager.tasks.limpeza.lotes.ILimpezaLotesService;
import com.tivic.manager.tasks.limpeza.lotes.LimpezaLotesDTO;
import com.tivic.manager.tasks.limpeza.lotes.exceptions.LimpezaLoteDataMaiorException;
import com.tivic.manager.tasks.limpeza.lotes.exceptions.LimpezaSemCriteriosException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.cdi.InicializationBeans;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;


public class LimpezaLoteImpressaoTest {

	private ILimpezaLotesService limpezaLoteService;
	private ILoteImpressaoRepository loteImpressaoRepository;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private ILoteImpressaoArquivoRepository loteImpressaoArquivoRepository;
	private IArquivoRepository arquivoRepository;

	@Before
	public void before() throws Exception{
		InicializationBeans.init(new InjectLimpezaLoteImpressaoTesteInjectBuilder());
		this.limpezaLoteService = (ILimpezaLotesService) BeansFactory.get(ILimpezaLotesService.class);
		this.loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		this.loteImpressaoArquivoRepository = (ILoteImpressaoArquivoRepository) BeansFactory.get(ILoteImpressaoArquivoRepository.class);
		this.arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
	}	

	@Test
	public void testarBuscaLoteSemCriterios() throws Exception {
		Assertions.assertThrows(LimpezaSemCriteriosException.class, () -> {
			limpezaLoteService.limparLotes(new LimpezaLotesDTO());
		}, "Era esperado o erro de limpeza de lotes sem critérios");
	}

	@Test
	public void testarBuscaLoteDataMaiorAtual() throws Exception {
		Assertions.assertThrows(LimpezaLoteDataMaiorException.class, () -> {
			//Definição de dados
			LimpezaLotesDTO limpezaLotesDTO = new LimpezaLotesDTO();
			GregorianCalendar dtMaiorAtual = DateUtil.getDataAtual();
			dtMaiorAtual.add(Calendar.DAY_OF_MONTH, 1);
			limpezaLotesDTO.setDtCriacao(dtMaiorAtual);
			//Operações a serem realizadas
			limpezaLoteService.limparLotes(limpezaLotesDTO);
			//Aferição de resultados
		}, "Era esperado o erro de critério de data de criação maior do que a atual");
	}

	@Test
	public void testarLimpezaLotePorCodigo() throws Exception {
		//Definição de dados
		LimpezaLotesDTO limpezaLotesDTO = new LimpezaLotesDTO();
		limpezaLotesDTO.getCodigosLote().add(1);
		//Operações a serem realizadas
		limpezaLoteService.limparLotes(limpezaLotesDTO);
		//Aferição de resultados
		LoteImpressao loteImpressao = loteImpressaoRepository.get(1, new CustomConnection());
		SearchCriterios searchCriteriosLoteImpressao = new SearchCriterios();
		searchCriteriosLoteImpressao.addCriteriosEqualInteger("cd_lote_impressao", 1);
		List<LoteImpressaoAit> listLoteImpressaoAit = loteImpressaoAitRepository.find(searchCriteriosLoteImpressao, new CustomConnection());
		List<LoteImpressaoArquivo> listLoteImpressaoArquivo = loteImpressaoArquivoRepository.find(searchCriteriosLoteImpressao, new CustomConnection());
		SearchCriterios searchCriteriosArquivo = new SearchCriterios();
		searchCriteriosArquivo.addCriteriosEqualInteger("cd_arquivo", 7);
		List<Arquivo> listArquivo = arquivoRepository.find(searchCriteriosArquivo, new CustomConnection());
		assertAll(
			() -> assertNull(loteImpressao, "O lote de impressão não foi removido"),
			() -> assertTrue("Não deveria haver aits nesse lote", listLoteImpressaoAit.isEmpty()),
			() -> assertTrue("Não deveria haver arquivos nesse lote", listLoteImpressaoArquivo.isEmpty()),
			() -> assertTrue("Não deveria haver um arquivo associado a esse Lote", listArquivo.isEmpty())
		);
	}
	
	@Test
	public void testarLimpezaLotePorData() throws Exception {
		LimpezaLotesDTO limpezaLotesDTO = new LimpezaLotesDTO();
		limpezaLotesDTO.setDtCriacao(new GregorianCalendar(2024, 0, 1));
		
		limpezaLoteService.limparLotes(limpezaLotesDTO);
		
		List<LoteImpressao> lotesImpressao = loteImpressaoRepository.getAll(new CustomConnection());
		
		assertAll(
			() -> assertTrue("Só deveria ter sobrado um único lote", lotesImpressao.size()==1)
		);
		
	}
}
