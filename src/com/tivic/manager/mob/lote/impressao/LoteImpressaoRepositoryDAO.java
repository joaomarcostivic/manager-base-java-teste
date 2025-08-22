package com.tivic.manager.mob.lote.impressao;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;
import com.tivic.manager.mob.lote.impressao.enums.LoteImpressaoSituacaoEnum;
import com.tivic.manager.mob.lote.impressao.loteimpressaosearch.LoteImpressaoSearch;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class LoteImpressaoRepositoryDAO implements ILoteImpressaoRepository {
	
	public LoteImpressao insert(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		int cdLoteImpressao = LoteImpressaoDAO.insert(loteImpressao, customConnection.getConnection());
		if (cdLoteImpressao <= 0)
			throw new ValidacaoException("Erro ao inserir lote");
		loteImpressao.setCdLoteImpressao(cdLoteImpressao);
		return loteImpressao;
	}

	public LoteImpressao update(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		int codigoRetorno = LoteImpressaoDAO.update(loteImpressao, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao atualizar lote");
		return loteImpressao;
	}

	public LoteImpressao delete(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		int codigoRetorno = LoteImpressaoDAO.delete(loteImpressao.getCdLoteImpressao(), customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao deletar lote");
		return loteImpressao;
	}

	public LoteImpressao get(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		return LoteImpressaoDAO.get(cdLoteImpressao, customConnection.getConnection());
	}

	public List<LoteImpressao> getAll(CustomConnection customConnection) throws Exception {
		ResultSetMapper<LoteImpressao> rsm = new ResultSetMapper<LoteImpressao>(LoteImpressaoDAO.getAll(), LoteImpressao.class);
		return rsm.toList();
	}
	

	public List<LoteImpressao> find(ArrayList<ItemComparator> criterios, CustomConnection customConnection) throws Exception {
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
				.fields("A.*,D.*,"
					  + "(SELECT COUNT (B.cd_lote_impressao) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao) as qtd_Documentos, "
					  + "(SELECT COUNT (*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao AND B.st_impressao > " +LoteImpressaoAitSituacao.AGUARDANDO_GERACAO+ ") as total_gerados ")
				.addJoinTable("LEFT OUTER JOIN mob_lote_impressao_ait B on (B.cd_lote_impressao = A.cd_lote_impressao)")
				.addJoinTable("LEFT OUTER JOIN seg_usuario C ON (C.cd_usuario = A.cd_usuario)")
                .addJoinTable("LEFT OUTER JOIN grl_pessoa D ON (D.cd_pessoa = C.cd_pessoa)")
                .addJoinTable("LEFT OUTER JOIN mob_ait E on (B.cd_ait = E.cd_ait)")
                .addJoinTable("LEFT OUTER JOIN mob_correios_etiqueta F ON (B.cd_ait = F.cd_ait AND F.tp_status = A.tp_documento)")
            	.searchCriterios(setCriteriosSearchLotes(loteImpressaoSearch))
				.additionalCriterias(" NOT EXISTS "
						+ " ("
						+ " 	SELECT st_lote_impressao FROM mob_lote_impressao C"
						+ "		WHERE "
						+ "			("
						+ " 			st_lote_impressao = " + LoteImpressaoSituacaoEnum.ECARTAS_SOLICITAR_CANCELAMENTO.getKey()
						+ "			)"
						+ " 		AND C.cd_lote_impressao = A.cd_lote_impressao"
						+ " )"
						+ "AND EXISTS"
						+ " ("
						+ "	SELECT tp_documento FROM mob_lote_impressao Z"
						+ "		WHERE "
						+ "			("
						+ " 			tp_documento = " + TipoLoteNotificacaoEnum.LOTE_NAI.getKey()
						+ " 			OR tp_documento = " + TipoLoteNotificacaoEnum.LOTE_NIP.getKey()
						+ " 			OR tp_documento = " + TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey()
						+ " 			OR tp_documento = " + TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey()
						+ "			)"
						+ " 		AND Z.cd_lote_impressao = A.cd_lote_impressao"
						+ " )"
						)
				.groupBy(" A.cd_lote_impressao, D.cd_pessoa ")
				.orderBy(" A.dt_criacao DESC ")
				.count()
				.customConnection(customConnection)
				.build();
		return search;
	}
	
	private SearchCriterios setCriteriosSearchLotes(LoteImpressaoSearch loteImpressaoSearch) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosLikeAnyString("A.id_lote_impressao", loteImpressaoSearch.getIdLoteImpressao(), loteImpressaoSearch.getIdLoteImpressao() != null);
		searchCriterios.addCriteriosLikeAnyString("E.id_ait", loteImpressaoSearch.getIdAit(), loteImpressaoSearch.getIdAit() != null);
		searchCriterios.addCriteriosGreaterDate("A.dt_criacao", loteImpressaoSearch.getDtCriacao(), loteImpressaoSearch.getDtCriacao() != null);
		searchCriterios.addCriteriosMinorDate("A.dt_criacao", loteImpressaoSearch.getDtCriacao(), loteImpressaoSearch.getDtCriacao() != null );
		searchCriterios.addCriteriosEqualInteger("A.tp_documento", loteImpressaoSearch.getTpDocumento(), loteImpressaoSearch.getTpDocumento() > 0);
		searchCriterios.addCriteriosEqualInteger("A.st_lote_impressao", loteImpressaoSearch.getStLoteImpressao(), loteImpressaoSearch.getStLoteImpressao() > -1);
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
				.fields("A.*,D.cd_pessoa, D.nm_pessoa, "
					  + "(select count(B.cd_lote_impressao) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao) as qtd_Documentos, "
					  + "(select count(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao AND B.st_impressao > " +LoteImpressaoAitSituacao.AGUARDANDO_GERACAO+ ") as total_gerados ")
				.searchCriterios(setCriteriosSearchLote(loteImpressaoSearch))
				.addJoinTable("LEFT OUTER JOIN mob_lote_impressao_ait B on (B.cd_lote_impressao = A.cd_lote_impressao)")
				.addJoinTable("LEFT OUTER JOIN seg_usuario C ON (C.cd_usuario = A.cd_usuario)")
                .addJoinTable("LEFT OUTER JOIN grl_pessoa D ON (D.cd_pessoa = C.cd_pessoa)")
                .addJoinTable("left outer join mob_ait E on (B.cd_ait = E.cd_ait)")
                .addJoinTable("left outer join mob_correios_etiqueta F ON (B.cd_ait = F.cd_ait AND F.tp_status = A.tp_documento)")
				.additionalCriterias(" NOT EXISTS "
						+ " ("
						+ " 	SELECT st_lote_impressao FROM mob_lote_impressao C"
						+ "		WHERE "
						+ "			("
						+ " 			st_lote_impressao = " + LoteImpressaoSituacaoEnum.ECARTAS_SOLICITAR_CANCELAMENTO.getKey()
						+ "			)"
						+ " 		AND C.cd_lote_impressao = A.cd_lote_impressao"
						+ " )")
				.groupBy(" A.cd_lote_impressao, D.cd_pessoa ")
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
	public Search<LoteImpressaoAitDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch,	CustomConnection customConnection) throws Exception {
		Search<LoteImpressaoAitDTO> search = new SearchBuilder<LoteImpressaoAitDTO>("mob_lote_impressao_ait A")
				.fields("A.cd_ait, A.cd_lote_impressao, A.txt_erro, K.cd_pessoa, K.nm_pessoa, B.id_ait, B.dt_infracao, "
						+ "B.nr_placa, B.nr_renavan, B.nm_proprietario, I.dt_criacao ")
				.addJoinTable("LEFT OUTER JOIN mob_ait B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("LEFT OUTER JOIN mob_lote_impressao I ON (A.cd_lote_impressao = I.cd_lote_impressao)")
				.addJoinTable("LEFT OUTER JOIN seg_usuario J ON (J.cd_usuario = I.cd_usuario)")
                .addJoinTable("LEFT OUTER JOIN grl_pessoa K ON (K.cd_pessoa = J.cd_pessoa) ")
        		.searchCriterios(setCriteriosSearchLotesAit(loteImpressaoSearch))
				.orderBy(" B.dt_infracao DESC ")
				.count()
				.customConnection(customConnection)
				.build();
		return search;	
	}
	
	private SearchCriterios setCriteriosSearchLotesAit(LoteImpressaoSearch loteImpressaoSearch) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", loteImpressaoSearch.getCdLoteImpressaoAit(), loteImpressaoSearch.getCdLoteImpressao() > -1);
		searchCriterios.addCriteriosLikeAnyString("B.id_ait", loteImpressaoSearch.getIdAit(), loteImpressaoSearch.getIdAit() != null);
		searchCriterios.addCriteriosGreaterDate("B.dt_infracao", loteImpressaoSearch.getDtInfracao(), loteImpressaoSearch.getDtInfracao() != null);
		searchCriterios.addCriteriosMinorDate("B.dt_infracao", loteImpressaoSearch.getDtInfracao(), loteImpressaoSearch.getDtInfracao() != null);
		searchCriterios.addCriteriosEqualString("B.nr_placa", loteImpressaoSearch.getNrPlaca(), loteImpressaoSearch.getNrPlaca() != null);
		searchCriterios.addCriteriosEqualString("B.nr_renavan", loteImpressaoSearch.getNrRenavan(), loteImpressaoSearch.getNrRenavan() != null);
		searchCriterios.setQtDeslocamento(((loteImpressaoSearch.getLimit() * loteImpressaoSearch.getPage()) - loteImpressaoSearch.getLimit()));
		searchCriterios.setQtLimite(loteImpressaoSearch.getLimit());
		return searchCriterios;
	}

	@Override
	public List<LoteImpressao> findLoteImpressao(SearchCriterios searchCriterios) throws Exception {
		return findLoteImpressao(searchCriterios, new CustomConnection());
	}

	@Override
	public List<LoteImpressao> findLoteImpressao(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<LoteImpressao> search = new SearchBuilder<LoteImpressao>("mob_lote_impressao")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
			.build();
		return search.getList(LoteImpressao.class);
	}
}