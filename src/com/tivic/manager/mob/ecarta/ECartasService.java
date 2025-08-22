package com.tivic.manager.mob.ecarta;

import java.util.List;

import javax.ws.rs.core.NoContentException;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.tivic.manager.mob.ecarta.dtos.ArquivoEcartaDTO;
import com.tivic.manager.mob.ecarta.dtos.ArquivoServicoDTO;
import com.tivic.manager.mob.ecarta.dtos.ListaArquivoRetornoCorreioDTO;
import com.tivic.manager.mob.ecarta.dtos.StatusLeituraArquivoRetornoDTO;
import com.tivic.manager.mob.ecarta.services.ArquivoServiceEDI;
import com.tivic.manager.mob.ecarta.services.IArquivoProducaoServiceEDI;
import com.tivic.manager.mob.ecarta.services.IArquivoRetornoServiceEDI;
import com.tivic.manager.mob.ecarta.services.IArquivoServiceEDI;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoStatusBuilder;
import com.tivic.manager.mob.lote.impressao.enums.LoteImpressaoSituacaoEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.util.Result;

public class ECartasService implements IEcartasService {
	private IArquivoServiceEDI arquivoServiceEDI;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private ILoteImpressaoRepository loteImpressaoRepository;
	private IArquivoRetornoServiceEDI arquivoRetornoServiceEDI;
	private IArquivoProducaoServiceEDI arquivoProducaoServiceEDI;

	public ECartasService() throws Exception {
		this.arquivoServiceEDI = (IArquivoServiceEDI) BeansFactory.get(IArquivoServiceEDI.class);
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory
				.get(ILoteImpressaoAitRepository.class);
		this.loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
		this.arquivoRetornoServiceEDI = (IArquivoRetornoServiceEDI) BeansFactory.get(IArquivoRetornoServiceEDI.class);
		this.arquivoProducaoServiceEDI = (IArquivoProducaoServiceEDI) BeansFactory
				.get(IArquivoProducaoServiceEDI.class);
	}

	@Override
	public Result iniciarGeracaoArquivoServicoEDI(int cdLoteImpressao, int cdUsuario) throws Exception {
		Result status = new Result(-1);
		arquivoServiceEDI.iniciarThreadGeracaoArquivoServiceEDI(String.valueOf(cdLoteImpressao),
				new ECartasGerarArquivoServicoEDITask(cdLoteImpressao, cdUsuario));
		status.setCode(1);
		status.setMessage("Geração de arquivo de serviço EDI iniciada.");
		status.addObject("STATUS", getStatus(cdLoteImpressao, new CustomConnection()));
		return status;
	}

	@Override
	public void getStatusGeracaoDocumentos(SseEventSink sseEventSink, Sse sse, int cdLoteImpressao) throws Exception {
		ArquivoServiceEDI events = arquivoServiceEDI.setSse(sseEventSink, sse);

		while (events.ativo()) {
			LoteImpressaoStatus status = getStatus(cdLoteImpressao, new CustomConnection());
			events.setStatus(status).notificar();
		}
	}

	@Override
	public LoteImpressaoStatus getStatus(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		LoteImpressao lote = loteImpressaoRepository.get(cdLoteImpressao, customConnection);
		List<LoteImpressaoAit> loteImpressaoAitsList = loteImpressaoAitRepository
				.find(searchCriteriosGeracaoArquivoServiceEDI(cdLoteImpressao), customConnection);
		int qtDocumentosGerados = (int) loteImpressaoAitsList.stream().filter(
				loteImpressaoAit -> loteImpressaoAit.getStImpressao() > LoteImpressaoAitSituacao.AGUARDANDO_GERACAO)
				.count();
		LoteImpressaoStatus status = new LoteImpressaoStatusBuilder(lote, loteImpressaoAitsList.size(),
				qtDocumentosGerados).build();
		return status;
	}

	@Override
	public ArquivoServicoDTO pegarArquivoLote(int cdLoteNotificacao) throws Exception {
		SearchCriterios searchCriterios = searchCriteriosArquivoLote(cdLoteNotificacao);
		List<ArquivoServicoDTO> arquivoList = searchArquivoLote(searchCriterios).getList(ArquivoServicoDTO.class);
		if (arquivoList.isEmpty()) {
			throw new NoContentException("Nenhum arquivo encontrado.");
		}
		ArquivoServicoDTO arquivo = arquivoList.get(0);
		atualizarLote(cdLoteNotificacao);
		return arquivo;
	}
	
	private void atualizarLote(int cdLoteNotificacao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			LoteImpressao loteImpressao = this.loteImpressaoRepository.get(cdLoteNotificacao, customConnection);
			loteImpressao.setStLoteImpressao(LoteImpressaoSituacaoEnum.ENVIADO_eCARTAS_AGUARDANDO_RESPOSTA.getKey());
			this.loteImpressaoRepository.update(loteImpressao, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public StatusLeituraArquivoRetornoDTO processarArquivo(ListaArquivoRetornoCorreioDTO listaArquivoRetornoCorreio,
			int cdLoteImpressao, int cdUsuario) throws Exception {
		List<ArquivoEcartaDTO> arquivosRetorno = listaArquivoRetornoCorreio.getArquivoRetornoCorreio();
		StatusLeituraArquivoRetornoDTO statusLeituraArquivoRetorno = arquivoRetornoServiceEDI.processar(arquivosRetorno,
				cdLoteImpressao, cdUsuario);
		return statusLeituraArquivoRetorno;
	}

	@Override
	public ArquivoServicoDTO confirmarProducao(int cdLoteImpressao, int cdUsuario) throws Exception {
		return arquivoProducaoServiceEDI.gerarConfirmacao(cdLoteImpressao, cdUsuario);
	}

	@Override
	public ArquivoServicoDTO rejeitarProducao(int cdLoteImpressao) throws Exception {
		return arquivoProducaoServiceEDI.gerarRejeicao(cdLoteImpressao);
	}

	private SearchCriterios searchCriteriosArquivoLote(int cdLoteNotificacao) {
		SearchCriterios criterios = new SearchCriterios();
		criterios.addCriteriosEqualInteger("B.cd_lote_impressao", cdLoteNotificacao);
		return criterios;
	}

	private Search<ArquivoServicoDTO> searchArquivoLote(SearchCriterios criterios) throws Exception {
		Search<ArquivoServicoDTO> search = new SearchBuilder<ArquivoServicoDTO>("GRL_ARQUIVO A")
				.addField("A.nm_arquivo, A.blb_arquivo as arquivo_servico")
				.addJoinTable(" JOIN mob_lote_impressao B ON (B.cd_arquivo = A.cd_arquivo) ").searchCriterios(criterios)
				.build();
		return search;
	}

	private SearchCriterios searchCriteriosGeracaoArquivoServiceEDI(int cdLoteImpressao) {
		SearchCriterios search = new SearchCriterios();
		search.addCriteriosEqualInteger("cd_lote_impressao", cdLoteImpressao, true);
		return search;
	}
}
