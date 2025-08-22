package com.tivic.manager.tasks.limpeza.lotes;

import java.util.List;

import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoArquivo;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.SearchCriterios;

public class LimpezaLotesService implements ILimpezaLotesService {

	private ILoteImpressaoRepository loteImpressaoRepository;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private ILoteImpressaoArquivoRepository loteImpressaoArquivoRepository;
	private IArquivoRepository arquivoRepository;
	private ManagerLog managerLog;
	
	public LimpezaLotesService() throws Exception {
		loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
		loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		loteImpressaoArquivoRepository = (ILoteImpressaoArquivoRepository) BeansFactory.get(ILoteImpressaoArquivoRepository.class);
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@Override
	public void limparLotes(LimpezaLotesDTO limpezaLotesDTO) throws Exception {
		limparLotes(limpezaLotesDTO, new CustomConnection());
	}

	@Override
	public void limparLotes(LimpezaLotesDTO limpezaLotesDTO, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			managerLog.info("INICIALIZACAO", "Iniciando a limpeza de lotes");
			managerLog.info("Parâmetros", limpezaLotesDTO.toString());
			SearchCriterios searchCriterios = new SearchCriteriosLoteImpressaoBuilder(limpezaLotesDTO).build();
			List<LoteImpressao> loteImpressaoList = loteImpressaoRepository.find(searchCriterios.getCriterios(), customConnection);
			managerLog.info("Quantidade de lotes", loteImpressaoList.size() + " lotes a serem removidos");
			for (LoteImpressao loteImpressao : loteImpressaoList) {
				managerLog.info("Lote Impressão a ser removido", loteImpressao.toString());
				limparArquivoImpressao(loteImpressao, customConnection);
				limparLoteImpressaoAit(loteImpressao, customConnection);
				loteImpressaoRepository.delete(loteImpressao, customConnection);
				arquivoRepository.delete(loteImpressao.getCdArquivo(), customConnection);
			}
			managerLog.info("FINALIZACAO", "Finalizada a limpeza de lotes");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private void limparArquivoImpressao(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception{
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote_impressao", loteImpressao.getCdLoteImpressao());
		List<LoteImpressaoArquivo> loteImpressaoArquivoList = loteImpressaoArquivoRepository.find(searchCriterios);
		if(!loteImpressaoArquivoList.isEmpty()) {
			LoteImpressaoArquivo loteImpressaoArquivo = loteImpressaoArquivoList.get(0);
			managerLog.info("Relação de arquivo com lote de impressão a ser removido", loteImpressao.toString());
			loteImpressaoArquivoRepository.delete(loteImpressaoArquivo, customConnection);
			managerLog.info("Arquivo a ser removido", loteImpressao.toString());
			arquivoRepository.delete(loteImpressaoArquivo.getCdArquivo(), customConnection);
		}
	}
	
	private void limparLoteImpressaoAit(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception{
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote_impressao", loteImpressao.getCdLoteImpressao());
		List<LoteImpressaoAit> loteImpressaoAitList = loteImpressaoAitRepository.find(searchCriterios, customConnection);
		managerLog.info("Quantidade de lotes/ait", loteImpressaoAitList.size() + " lotes/ait a serem removidos");
		for (LoteImpressaoAit loteImpressaoAit : loteImpressaoAitList) {
			managerLog.info("Relação de ait com lote Impressão a ser removido", loteImpressaoAit.toString());
			loteImpressaoAitRepository.delete(loteImpressaoAit, customConnection);
			arquivoRepository.delete(loteImpressaoAit.getCdArquivo(), customConnection);
		}
	}

}
