package com.tivic.manager.ptc.protocolos.julgamento;

import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoStatusBuilder;
import com.tivic.manager.mob.lotes.dto.impressao.CreateLoteImpressaoDTO;
import com.tivic.manager.mob.lotes.enums.impressao.StatusLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lotes.repository.impressao.LoteImpressaoRepository;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.util.Result;

public class CartaJulgamentoService implements ICartaJulgamentoService {
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private IArquivoRepository arquivoRepository;
	private LoteImpressaoRepository loteImpressaoRepository;
	private LoteRepository loteRepository;

	public CartaJulgamentoService() throws Exception {
		this.arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		this.loteImpressaoRepository = (LoteImpressaoRepository) BeansFactory.get(LoteImpressaoRepository.class);
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		this.loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
	}

	public byte[] imprimirLoteJulgamento(int cdLoteImpressao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			LoteImpressao loteImpressao = this.loteImpressaoRepository.get(cdLoteImpressao, customConnection);
			Lote lote = this.loteRepository.get(loteImpressao.getCdLote(), customConnection);
			Arquivo arquivoLote = this.arquivoRepository.get(lote.getCdArquivo(), customConnection);
			if (lote.getCdArquivo() <= 0 || arquivoLote == null) {
				throw new ValidacaoException("O arquivo para download não foi encontrado.");
			}
			loteImpressao.setStLote(StatusLoteImpressaoEnum.IMPRESSO.getKey());
			customConnection.finishConnection();
			return arquivoLote.getBlbArquivo();
		}
		finally{
			customConnection.closeConnection();
		}
	}
	
	@Override
	public LoteImpressao gerarLoteCartaJulgamento(CreateLoteImpressaoDTO createLoteImpressao) throws ValidacaoException, Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			LoteImpressao loteImpressao = new GerarLoteJulgamentoFactory()
					.getStrategy(createLoteImpressao.getTpImpressao())
					.gerarLoteJulgamento(createLoteImpressao, createLoteImpressao.getCdCriador(), customConnection);
			customConnection.finishConnection();
			return loteImpressao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Search<CartaJulgamentoDTO> buscarLotes(SearchCriterios searchCriterios) throws ValidacaoException, Exception{ 
		Search<CartaJulgamentoDTO> list = searchLotes(searchCriterios);
		if(list.getList(CartaJulgamentoDTO.class).isEmpty()) {
			throw new ValidacaoException ("Não há Lote com esse filtro.");
		} 
		return list;
	}
	
	@Override
	public List<AitDTO> buscarQuantidadeAitsParaLoteImpressao(int quantidadeAit, int tipoLote) throws ValidacaoException, Exception {
		Search<AitDTO> searchAits = new BuscarAitsParaLoteJulgamentoFactory()
				.getStrategy(tipoLote)
				.buscarAitsParaLoteJulgamento(quantidadeAit);
		return searchAits.getList(AitDTO.class);
	}
	
	public PagedResponse<AitDTO> buscarAitsParaLoteImpressao(int tipoLote, SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		Search<AitDTO> searchAits = new BuscarAitsParaLoteJulgamentoFactory()
				.getStrategy(tipoLote)
				.montarSearchLoteJulgamento(searchCriterios);
		List<AitDTO> listAitDTO = searchAits.getList(AitDTO.class);
		return new CriacaoLoteAitDTOListBuilder(listAitDTO, searchAits.getRsm().getTotal()).build();
	}
	
	private Search<CartaJulgamentoDTO> searchLotes(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		Search<CartaJulgamentoDTO> search = new SearchBuilder<CartaJulgamentoDTO>("mob_lote_impressao A")
				.fields("A.*,D.*,G.cd_lote, G.id_lote, G.dt_criacao, "
						  + "(select count(B.cd_lote_impressao) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao) as qtd_Documentos, "
						  + "(select count(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao AND B.st_impressao > " +LoteImpressaoAitSituacao.AGUARDANDO_GERACAO+ ") as total_gerados ")
					.searchCriterios(searchCriterios)
					.addJoinTable("LEFT OUTER JOIN mob_lote_impressao_ait B ON (B.cd_lote_impressao = A.cd_lote_impressao)")
					.addJoinTable("LEFT OUTER JOIN grl_lote G ON (G.cd_lote = A.cd_lote)")
					.addJoinTable("LEFT OUTER JOIN seg_usuario C ON (C.cd_usuario = G.cd_criador)")
	                .addJoinTable("LEFT OUTER JOIN grl_pessoa D ON (D.cd_pessoa = C.cd_pessoa)")
	                .addJoinTable("LEFT OUTER JOIN mob_ait E ON (B.cd_ait = E.cd_ait)")
            	.additionalCriterias(" 	EXISTS "
						+ " ("
						+ " 	SELECT tp_impressao FROM mob_lote_impressao Z"
						+ "		WHERE "
						+ "			("
						+ " 			tp_impressao = " + TipoStatusEnum.DEFESA_DEFERIDA.getKey()
						+ " 			OR tp_impressao = " + TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()
						+ " 			OR tp_impressao = " + TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey()
						+ "			)"
						+ " 		AND Z.cd_lote_impressao = A.cd_lote_impressao"
						+ " )")
				.groupBy(" A.cd_lote_impressao, G.cd_lote, D.cd_pessoa ")
				.orderBy(" A.cd_lote_impressao DESC ")
				.build();
		return search;
	}
	
	public Result iniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario) throws Exception {
		Result r = new Result(-1);
		LoteJulgamentoGeracaoDocumentoTask task = new LoteJulgamentoGeracaoDocumentoTask(cdLoteImpressao, cdUsuario);
		Thread threadGeracaoDocumento = new Thread(task);
		threadGeracaoDocumento.start();
		r.setCode(1);
		r.setMessage("Geração de documentos iniciada.");
		r.addObject("STATUS", getStatusGeracaoDocumentos(cdLoteImpressao));
		return r;
	}
	
	public LoteImpressaoStatus getStatusGeracaoDocumentos(int cdLoteImpressao) throws Exception {
		return getStatusGeracaoDocumentos(cdLoteImpressao, new CustomConnection());
	}
	
	public LoteImpressaoStatus getStatusGeracaoDocumentos(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		customConnection.initConnection(false);
		try {
			LoteImpressao loteImpressao =  loteImpressaoRepository.get(cdLoteImpressao, customConnection);
			List<LoteImpressaoAit> loteImpressaoAitsList = loteImpressaoAitRepository.find(searchCriteriosNotificacaoDocumentos(cdLoteImpressao), customConnection);
			int qtDocumentosGerados = 0;
			for (LoteImpressaoAit loteImpressaoAit: loteImpressaoAitsList) {
				if (loteImpressaoAit.getStImpressao() > LoteImpressaoAitSituacao.AGUARDANDO_GERACAO)
					qtDocumentosGerados++;
			}
			LoteImpressaoStatus status = new LoteImpressaoStatusBuilder(loteImpressao, loteImpressaoAitsList.size(), qtDocumentosGerados)
					.build();
			customConnection.finishConnection();
			return status;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private SearchCriterios searchCriteriosNotificacaoDocumentos(int cdLoteImpressao) {
		SearchCriterios search = new SearchCriterios();
		search.addCriteriosEqualInteger("cd_lote_impressao", cdLoteImpressao, true);
		return search;
	}
	
	@Override
	public LoteImpressao save(LoteImpressao loteImpressao) throws Exception{
		return save(loteImpressao, new CustomConnection());
	}

	@Override
	public LoteImpressao save(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception{
		try {
			if(loteImpressao==null)
				throw new ValidacaoException("Erro ao salvar. LoteImpressao é nulo");
			customConnection.initConnection(true);
			if(loteImpressao.getCdLoteImpressao()==0){
				loteImpressao.setCdLoteImpressao(loteImpressaoRepository
						.insert(loteImpressao, customConnection)
						.getCdLoteImpressao());
			}
			else {
				loteImpressao = loteImpressaoRepository.update(loteImpressao, customConnection);
				customConnection.finishConnection();
				return loteImpressao;
			}
			if(loteImpressao.getAits()!=null && loteImpressao.getAits().size()>0) {
				for (LoteImpressaoAit loteImpressaoAit : loteImpressao.getAits()) {
					loteImpressaoAit.setCdLoteImpressao(loteImpressao.getCdLoteImpressao());
				}
			}
			customConnection.finishConnection();
			return loteImpressao;
		}
		finally {
			customConnection.closeConnection();
		}
	
	}

	@Override
	public void gerarDocumentosLote(int cdLoteImpressao, int cdUsuario) throws Exception {
		gerarDocumentosLote(cdLoteImpressao, cdUsuario, new CustomConnection());
	}
	
	public void gerarDocumentosLote(int cdLoteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			LoteImpressao loteImpressao = loteImpressaoRepository.get(cdLoteImpressao, customConnection);
			Lote lote = loteRepository.get(loteImpressao.getCdLote(), customConnection);
			Arquivo arquivo = arquivoRepository.get(lote.getCdArquivo(), customConnection);
			if (arquivo != null) {
				throw new ValidacaoException("Os documentos deste lote já foram gerados.");
			}
			new GerarDocumentosLoteJulgamentoFactory()
					.getStrategy(loteImpressao)
					.gerarDocumentos(loteImpressao, cdUsuario, customConnection);
			customConnection.finishConnection();
		}
		finally {
			customConnection.closeConnection();
		}
	}

}
