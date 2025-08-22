package com.tivic.manager.adapter.base.antiga.loteimpressao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoAitDTO;
import com.tivic.manager.mob.lote.impressao.LoteNotificacaoDTO;
import com.tivic.manager.mob.lote.impressao.enums.LoteImpressaoSituacaoEnum;
import com.tivic.manager.mob.lote.impressao.loteimpressaosearch.LoteImpressaoSearch;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class LoteImpressaoRepositoryOldDAO implements ILoteImpressaoRepository {
	
	@Override
	public LoteImpressao insert(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		int cdLoteImpressao = LoteImpressaoDAO.insert(loteImpressao, customConnection.getConnection());
		if (cdLoteImpressao <= 0)
			throw new ValidacaoException("Erro ao inserir lote");
		return loteImpressao;
	}

	@Override
	public LoteImpressao update(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		int codigoRetorno = LoteImpressaoDAO.update(loteImpressao, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao atualizar lote");
		return loteImpressao;
	}

	@Override
	public LoteImpressao delete(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		int codigoRetorno = LoteImpressaoDAO.delete(loteImpressao.getCdLoteImpressao(), customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao deletar lote");
		return loteImpressao;
	}

	@Override
	public LoteImpressao get(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		return LoteImpressaoDAO.get(cdLoteImpressao, customConnection.getConnection());
	}

	@Override
	public List<LoteImpressao> getAll(CustomConnection customConnection) throws Exception {
		ResultSetMapper<LoteImpressao> rsm = new ResultSetMapper<LoteImpressao>(LoteImpressaoDAO.getAll(), LoteImpressao.class);
		return rsm.toList();
	}

	@Override
	public List<LoteImpressao> find(ArrayList<ItemComparator> criterios, CustomConnection customConnection)	throws Exception {
		ResultSetMapper<LoteImpressao> rsm = new ResultSetMapper<LoteImpressao>(LoteImpressaoDAO.find(criterios, customConnection.getConnection()), LoteImpressao.class);
		return rsm.toList();
	}

	@Override
	public Search<LoteNotificacaoDTO> findLotes(LoteImpressaoSearch loteImpressaoSearch) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<LoteNotificacaoDTO> lotes = findLotes(loteImpressaoSearch, customConnection);
			customConnection.finishConnection();
			return lotes;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Search<LoteNotificacaoDTO> findLotes(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception {
		Search<LoteNotificacaoDTO> search = new SearchBuilder<LoteNotificacaoDTO>("mob_lote_impressao A")
				.fields("A.*, "
					  + "(select count(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao) as qtd_Documentos, "
					  + "(select count(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao AND B.st_impressao > "+LoteImpressaoAitSituacao.AGUARDANDO_GERACAO+") as total_gerados ")
			
            	.searchCriterios(setCriteriosSearchLotes(loteImpressaoSearch))
				.additionalCriterias(" NOT EXISTS "
						+ " ("
						+ " SELECT st_lote_impressao FROM mob_lote_impressao C "
						+ " WHERE "
						+ "("
							+ " st_lote_impressao = " + LoteImpressaoSituacaoEnum.ECARTAS_SOLICITAR_CANCELAMENTO.getKey()
						+ " )"
						+" AND C.cd_lote_impressao = A.cd_lote_impressao)")
				.orderBy(" A.dt_criacao DESC ")
				.count()
				.customConnection(customConnection)
				.build();
		
		return search;
	}
	
	private SearchCriterios setCriteriosSearchLotes(LoteImpressaoSearch loteImpressaoSearch) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosLikeAnyString("A.id_lote_impressao", loteImpressaoSearch.getIdLoteImpressao(), loteImpressaoSearch.getIdLoteImpressao() != null);
		searchCriterios.addCriteriosGreaterDate("A.dt_criacao", loteImpressaoSearch.getDtCriacao(), loteImpressaoSearch.getDtCriacao() != null);
		searchCriterios.addCriteriosMinorDate("A.dt_criacao", loteImpressaoSearch.getDtCriacao(), loteImpressaoSearch.getDtCriacao() != null );
		searchCriterios.addCriteriosEqualInteger("A.st_lote_impressao", loteImpressaoSearch.getStLoteImpressao(), loteImpressaoSearch.getStLoteImpressao() > -1);
		searchCriterios.addCriteriosEqualInteger("A.tp_lote_impressao", loteImpressaoSearch.getTpLoteImpressao(), loteImpressaoSearch.getTpLoteImpressao() > -1);
		searchCriterios.addCriteriosEqualInteger("A.tp_documento", loteImpressaoSearch.getTpDocumento(), loteImpressaoSearch.getTpDocumento() > -1);
		searchCriterios.addCriteriosEqualInteger("A.tp_destino", loteImpressaoSearch.getTpDestino(), loteImpressaoSearch.getTpDestino() > -1);
		searchCriterios.addCriteriosEqualInteger("A.tp_transporte", loteImpressaoSearch.getTpTransporte(), loteImpressaoSearch.getTpTransporte() > -1);
		searchCriterios.addCriteriosEqualInteger("A.cd_usuario", loteImpressaoSearch.getCdUsuario(), loteImpressaoSearch.getCdUsuario() > -1);
		searchCriterios.setQtDeslocamento(((loteImpressaoSearch.getLimit() * loteImpressaoSearch.getPage()) - loteImpressaoSearch.getLimit()));
		searchCriterios.setQtLimite(loteImpressaoSearch.getLimit());
		return searchCriterios;
	}

	@Override
	public Search<LoteImpressaoAitDTO> getLote(LoteImpressaoSearch loteImpressaoSearch) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<LoteImpressaoAitDTO> lote = getLote(loteImpressaoSearch, customConnection);
			customConnection.finishConnection();
			return lote;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Search<LoteImpressaoAitDTO> getLote(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception {
		Search<LoteImpressaoAitDTO> search = new SearchBuilder<LoteImpressaoAitDTO>("mob_lote_impressao A")
				.fields("A.*, C.cod_usuario, C.nm_usuario as nm_pessoa, "
					  + "(select count(B.cd_lote_impressao) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao) as qtd_Documentos, "
					  + "(select count(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao AND B.st_impressao > " +LoteImpressaoAitSituacao.AGUARDANDO_GERACAO+ ") as total_gerados ")
				.searchCriterios(setCriteriosSearchLote(loteImpressaoSearch))
				.addJoinTable("LEFT OUTER JOIN mob_lote_impressao_ait B on (B.cd_lote_impressao = A.cd_lote_impressao)")
				.addJoinTable("LEFT OUTER JOIN usuario C ON (C.cod_usuario = A.cd_usuario) ")
                .addJoinTable("LEFT OUTER JOIN ait E ON (B.cd_ait = E.codigo_ait)")
                .addJoinTable("LEFT OUTER JOIN stt_correios_etiqueta F ON (B.cd_ait = F.cd_ait AND F.tp_status = A.tp_documento)")
				.additionalCriterias(" NOT EXISTS "
						+ " ("
						+ " 	SELECT st_lote_impressao FROM mob_lote_impressao C"
						+ "		WHERE "
						+ "			("
						+ " 			st_lote_impressao = " + LoteImpressaoSituacaoEnum.ECARTAS_SOLICITAR_CANCELAMENTO.getKey()
						+ "			)"
						+ " 		AND C.cd_lote_impressao = A.cd_lote_impressao"
						+ " )")
				.groupBy(" A.cd_lote_impressao, C.cod_usuario ")
				.orderBy(" A.dt_criacao DESC ")
				.customConnection(customConnection)
				.build();
		return search;
	}
	
	private SearchCriterios setCriteriosSearchLote(LoteImpressaoSearch loteImpressaoSearch) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", loteImpressaoSearch.getCdLoteImpressao(), loteImpressaoSearch.getCdLoteImpressao() > 0);
		return searchCriterios;
	}

	@Override
	public Search<LoteImpressaoAitDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<LoteImpressaoAitDTO> loteAits = findLoteAits(loteImpressaoSearch, customConnection);
			customConnection.finishConnection();
			return loteAits;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Search<LoteImpressaoAitDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception {
		Search<LoteImpressaoAitDTO> search = new SearchBuilder<LoteImpressaoAitDTO>("mob_lote_impressao_ait A ")
				.fields("A.cd_ait, A.cd_lote_impressao, J.cod_usuario, J.nm_usuario, "
						+ "B.nr_ait as id_ait, B.dt_infracao, B.nr_placa, B.cd_renavan as nr_renavan, "
						+ "B.nm_proprietario, I.dt_criacao")
				.addJoinTable("LEFT OUTER JOIN ait B ON (A.cd_ait = B.codigo_ait)")
				.addJoinTable("LEFT OUTER JOIN mob_lote_impressao I ON (A.cd_lote_impressao = I.cd_lote_impressao)")
				.addJoinTable("LEFT OUTER JOIN usuario J ON (J.cod_usuario = I.cd_usuario)")
				.searchCriterios(setCriteriosSearchLotesAit(loteImpressaoSearch))
				.orderBy("B.dt_infracao DESC")
				.customConnection(customConnection)
				.build();
		return search;
	}

	@Override
	public List<LoteImpressao> findLoteImpressao(SearchCriterios searchCriterios) throws Exception {
		return Collections.emptyList();
	}

	@Override
	public List<LoteImpressao> findLoteImpressao(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return Collections.emptyList();
	}

	private SearchCriterios setCriteriosSearchLotesAit(LoteImpressaoSearch loteImpressaoSearch) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", loteImpressaoSearch.getCdLoteImpressaoAit(), loteImpressaoSearch.getCdLoteImpressao() > -1);
		searchCriterios.addCriteriosEqualString("B.nr_ait", loteImpressaoSearch.getIdAit(), loteImpressaoSearch.getIdAit() != null);
		searchCriterios.addCriteriosGreaterDate("B.dt_infracao", loteImpressaoSearch.getDtInfracao(), loteImpressaoSearch.getDtInfracao() != null);
		searchCriterios.addCriteriosMinorDate("B.dt_infracao", loteImpressaoSearch.getDtInfracao(), loteImpressaoSearch.getDtInfracao() != null);
		searchCriterios.addCriteriosEqualString("B.nr_placa", loteImpressaoSearch.getNrPlaca(), loteImpressaoSearch.getNrPlaca() != null);
		searchCriterios.addCriteriosEqualString("B.cd_renavan", loteImpressaoSearch.getNrRenavan(), loteImpressaoSearch.getNrRenavan() != null);
		searchCriterios.setQtDeslocamento(((loteImpressaoSearch.getLimit() * loteImpressaoSearch.getPage()) - loteImpressaoSearch.getLimit()));
		searchCriterios.setQtLimite(loteImpressaoSearch.getLimit());
		return searchCriterios;
	}
}
