package com.tivic.manager.mob.lotes.repository.impressao;

import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoSearch;
import com.tivic.manager.mob.lotes.dao.impressao.LoteImpressaoDAO;
import com.tivic.manager.mob.lotes.dto.impressao.AitDTO;
import com.tivic.manager.mob.lotes.dto.impressao.LoteImpressaoDTO;
import com.tivic.manager.mob.lotes.enums.impressao.StatusLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.enums.impressao.TipoAdesaoSneEnum;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class LoteImpressaoRepositoryDAO implements LoteImpressaoRepository {
	
	@Override
	public LoteImpressao insert(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		int cdLoteImpressao = LoteImpressaoDAO.insert(loteImpressao, customConnection.getConnection());
		if (cdLoteImpressao <= 0)
			throw new Exception("Erro ao inserir LoteImpressao.");
		loteImpressao.setCdLoteImpressao(cdLoteImpressao);
		return loteImpressao;
	}

	@Override
	public LoteImpressao update(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		LoteImpressaoDAO.update(loteImpressao, customConnection.getConnection());
		return loteImpressao;
	}
	
	@Override
	public void delete(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		int codigoRetorno = LoteImpressaoDAO.delete(cdLoteImpressao, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao excluir lote.");
	}
	
	@Override
	public LoteImpressao get(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		return LoteImpressaoDAO.get(cdLoteImpressao, customConnection.getConnection());
	}

	@Override
	public List<LoteImpressao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<LoteImpressao> search = new SearchBuilder<LoteImpressao>("mob_lote_impressao")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(LoteImpressao.class);
	}
	
	@Override
	public Search<AitDTO> buscarAitsParaLoteImpressao(SearchCriterios searchCriterios, int tpNotificacao) throws Exception {
		return searchAits(searchCriterios, tpNotificacao);
	}
	
	public Search<AitDTO> searchAits(SearchCriterios searchCriterios, int tpNotificacao) throws Exception {
		Search<AitDTO> search = new SearchBuilder<AitDTO>("mob_ait A")
				.fields("DISTINCT ON (A.cd_ait) A.cd_ait, A.id_ait, A.nr_placa, A.dt_infracao, K.dt_movimento, K.tp_status, K.nr_processo, K.lg_enviado_detran ")
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento K ON (A.cd_ait = K.cd_ait)")
				.searchCriterios(searchCriterios)
				.additionalCriterias( getCriteriosNaiNip(tpNotificacao) )
				.additionalCriterias( incluirCondicionalAdesaoSne(tpNotificacao) )
				.orderBy("A.cd_ait DESC, K.dt_movimento DESC")
				.count()
				.build();
		return search;
	}
	
	private String incluirCondicionalAdesaoSne(int tpNotificacao) throws Exception {
		int sneAdesaoOrgaoAutuador = ParametroServices.getValorOfParametroAsInteger("MOB_SNE_ADESAO_ORGAO_AUTUADOR", 0);
		if(sneAdesaoOrgaoAutuador == TipoAdesaoSneEnum.COM_OPCAO_SNE.getKey()) {
			return " NOT EXISTS( SELECT L.cd_ait FROM mob_ait_movimento L"
	                    + " WHERE("
	                    + " L.st_adesao_sne = " + TipoAdesaoSneEnum.COM_OPCAO_SNE.getKey() 
	                    + " AND L.tp_status = " + tpNotificacao
	                    + ") AND  L.cd_ait = A.cd_ait)";
	    }
		return "";
	}

	private String getCriteriosNaiNip(int tpNotificacao) throws ValidacaoException {
		if (tpNotificacao != TipoStatusEnum.NAI_ENVIADO.getKey() && tpNotificacao != TipoStatusEnum.NIP_ENVIADA.getKey())
			throw new ValidacaoException("Tipo de Notificação invalido ou não informado.");

	    if (tpNotificacao == TipoStatusEnum.NAI_ENVIADO.getKey()) {
	        return additionalCriteriasNai();
	    }
	    return additionalCriteriasNip();
	}
	
	private String additionalCriteriasNai() {
		return " NOT EXISTS "
				   + " ("   
				   + " 	SELECT K.cd_ait FROM mob_ait_movimento K"
				   + " 		WHERE"
				   + "			("
				   + "				K.tp_status IN (" + TipoStatusEnum.NIP_ENVIADA.getKey()
				   + "				," + TipoStatusEnum.CADASTRO_CANCELADO.getKey()
				   + "				, " + TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey()
				   + "				, " + TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey()
				   + "				, " + TipoStatusEnum.CANCELAMENTO_MULTA.getKey()
				   + "				, " + TipoStatusEnum.CANCELAMENTO_PONTUACAO.getKey()
				   + "				, " + TipoStatusEnum.DEVOLUCAO_PAGAMENTO.getKey()
				   + "				)"
				   + "			) AND K.cd_ait = A.cd_ait "
				   + "	)"
				   + "	AND NOT EXISTS("
				   + "		SELECT D.cd_lote_impressao, D.tp_impressao, E.cd_ait "
				   + "		FROM mob_lote_impressao D"
				   + "		JOIN mob_lote_impressao_ait E ON (D.cd_lote_impressao = E.cd_lote_Impressao)"
				   + "		WHERE ("
				   + "				D.tp_impressao IN (" + TipoLoteImpressaoEnum.LOTE_NAI.getKey() + ", "
				   + "								   " + TipoLoteImpressaoEnum.LOTE_NIC_NAI.getKey()
				   + "				) "
				   + "		) AND E.cd_ait = A.cd_ait "
				   + "	)";
	}
	
	private String additionalCriteriasNip() {
		return " NOT EXISTS "
				   + " ("   
				   + " 		SELECT K.cd_ait FROM mob_ait_movimento K"
				   + " 		WHERE"
				   + "			(" 
				   + "				K.tp_status IN (" + TipoStatusEnum.CADASTRO_CANCELADO.getKey()
				   + "				, " + TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey()
				   + "			    , " + TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey()
				   + "			   	," + TipoStatusEnum.CANCELAMENTO_MULTA.getKey()
				   + "	 		    , " + TipoStatusEnum.CANCELAMENTO_PONTUACAO.getKey()
				   + "			    , " + TipoStatusEnum.DEVOLUCAO_PAGAMENTO.getKey()
				   + "			    , " + TipoStatusEnum.MULTA_PAGA.getKey()
				   + "				)"
				   + "			) AND K.cd_ait = A.cd_ait"
				   + "	)"
				   + " AND "
				   + " ("
				   + " 		NOT EXISTS  "
				   + "		("
				   + " 			SELECT B.cd_ait FROM mob_ait_movimento B "
				   + "				WHERE"
				   + "				("
				   + "					B.tp_status = " + TipoStatusEnum.CANCELAMENTO_NIP.getKey() 
				   + " 				and B.cd_ait = A.cd_ait )"	
				   + "  	)"
				   + "		OR "
				   + "		( "
				   + "			K.dt_movimento > "
				   + "			("
				   + "          	SELECT B3.dt_movimento FROM mob_ait_movimento B3 WHERE B3.tp_status = " + TipoStatusEnum.CANCELAMENTO_NIP.getKey()
				   + "       			AND B3.cd_ait = A.cd_ait ORDER BY B3.dt_movimento DESC LIMIT 1 "
				   + "			)"
				   + "      ) " 
				   + " 	)"
				   + "	AND"
				   + "	("
				   + "		NOT EXISTS "
				   + "		( "
				   + "			SELECT E.cd_ait FROM mob_lote_impressao D "
				   + "			JOIN mob_lote_impressao_ait E ON (D.cd_lote_impressao = E.cd_lote_Impressao) "	
				   + " 				WHERE "
				   + "				( D.tp_impressao IN ( " + TipoLoteImpressaoEnum.LOTE_NIP.getKey() + ", " 
				   + 										TipoLoteImpressaoEnum.LOTE_NIC_NIP.getKey() +  " ) AND A.cd_ait = E.cd_ait )"
				   + " 		)"
				   + "		OR EXISTS "
				   + "		( "
				   + "			SELECT E1.cd_ait FROM mob_lote_impressao_ait E1 "
				   + "				WHERE E1.cd_ait = A.cd_ait and E1.cd_lote_impressao = "
				   + "				("
				   + "					SELECT D1.cd_lote_impressao FROM mob_lote_impressao D1 "
				   + "					JOIN mob_lote_impressao_ait E2 ON (E2.cd_lote_impressao = D1.cd_lote_impressao)"
				   + "						WHERE D1.tp_impressao IN ( " + TipoLoteImpressaoEnum.LOTE_NIP.getKey() + ", "
				   + "														" + TipoLoteImpressaoEnum.LOTE_NIC_NIP.getKey() 
				   + "						) ORDER BY D1.cd_lote_impressao DESC LIMIT 1"
				   + "				) AND E1.cd_ait = A.cd_ait AND E1.st_impressao = " + LoteImpressaoAitSituacao.REGISTRO_CANCELADO
				   + "		)"
				   + "	)";
	}
	
	@Override
	public Search<LoteImpressaoDTO> findLotes(LoteImpressaoSearch loteImpressaoSearch) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<LoteImpressaoDTO> lotes = findLotes(loteImpressaoSearch, customConnection);
			customConnection.finishConnection();
			return lotes;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Search<LoteImpressaoDTO> findLotes(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception {
		Search<LoteImpressaoDTO> search = new SearchBuilder<LoteImpressaoDTO>("mob_lote_impressao A")
				.fields("A.*,D.*, F.*, "
					  + "(SELECT COUNT (B.cd_lote_impressao) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao) as qtd_Documentos, "
					  + "(SELECT COUNT (*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao AND B.st_impressao > " + LoteImpressaoAitSituacao.AGUARDANDO_GERACAO + ") as total_gerados ")
				.addJoinTable("LEFT OUTER JOIN mob_lote_impressao_ait B on (B.cd_lote_impressao = A.cd_lote_impressao)")
				.addJoinTable("LEFT OUTER JOIN grl_lote F on (F.cd_lote = A.cd_lote)")
				.addJoinTable("LEFT OUTER JOIN seg_usuario C ON (C.cd_usuario = F.cd_criador)")
                .addJoinTable("LEFT OUTER JOIN grl_pessoa D ON (D.cd_pessoa = C.cd_pessoa)")
                .addJoinTable("LEFT OUTER JOIN mob_ait E on (B.cd_ait = E.cd_ait)")
            	.searchCriterios(setCriteriosSearchLotes(loteImpressaoSearch))
				.additionalCriterias(" NOT EXISTS "
						+ " ("
						+ " 	SELECT st_lote FROM mob_lote_impressao C"
						+ "		WHERE "
						+ "			("
						+ " 			st_lote = " + StatusLoteImpressaoEnum.ECARTAS_SOLICITAR_CANCELAMENTO.getKey()
						+ "			)"
						+ " 		AND C.cd_lote_impressao = A.cd_lote_impressao"
						+ " )"
						+ "AND EXISTS"
						+ " ("
						+ "	SELECT tp_impressao FROM mob_lote_impressao Z"
						+ "		WHERE "
						+ "			("
						+ " 			tp_impressao = " + TipoLoteImpressaoEnum.LOTE_NAI.getKey()
						+ " 			OR tp_impressao = " + TipoLoteImpressaoEnum.LOTE_NIP.getKey()
						+ " 			OR tp_impressao = " + TipoLoteImpressaoEnum.LOTE_NIC_NAI.getKey()
						+ " 			OR tp_impressao = " + TipoLoteImpressaoEnum.LOTE_NIC_NIP.getKey()
						+ "			)"
						+ " 		AND Z.cd_lote_impressao = A.cd_lote_impressao"
						+ " )"
						)
				.groupBy(" A.cd_lote_impressao, D.cd_pessoa, F.cd_lote, F.dt_criacao ")
				.orderBy(" F.dt_criacao DESC ")
				.count()
				.customConnection(customConnection)
				.build();
		return search;
	}
	
	private SearchCriterios setCriteriosSearchLotes(LoteImpressaoSearch loteImpressaoSearch) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosLikeAnyString("F.id_lote", loteImpressaoSearch.getIdLote(), loteImpressaoSearch.getIdLote() != null);
		searchCriterios.addCriteriosLikeAnyString("E.id_ait", loteImpressaoSearch.getIdAit(), loteImpressaoSearch.getIdAit() != null);
		searchCriterios.addCriteriosGreaterDate("A.dt_criacao", loteImpressaoSearch.getDtCriacao(), loteImpressaoSearch.getDtCriacao() != null);
		searchCriterios.addCriteriosMinorDate("A.dt_criacao", loteImpressaoSearch.getDtCriacao(), loteImpressaoSearch.getDtCriacao() != null );
		searchCriterios.addCriteriosEqualInteger("A.tp_impressao", loteImpressaoSearch.getTpImpressao(), loteImpressaoSearch.getTpImpressao() > 0);
		searchCriterios.addCriteriosEqualInteger("A.st_lote", loteImpressaoSearch.getStLote(), loteImpressaoSearch.getStLote() > -1);
		searchCriterios.setQtDeslocamento(((loteImpressaoSearch.getLimit() * loteImpressaoSearch.getPage()) - loteImpressaoSearch.getLimit()));
		searchCriterios.setQtLimite(loteImpressaoSearch.getLimit());
		return searchCriterios;
	}
	
	@Override
	public Search<LoteImpressaoDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<LoteImpressaoDTO> loteAits = findLoteAits(loteImpressaoSearch, customConnection);
			customConnection.finishConnection();
			return loteAits;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Search<LoteImpressaoDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch,	CustomConnection customConnection) throws Exception {
		Search<LoteImpressaoDTO> search = new SearchBuilder<LoteImpressaoDTO>("mob_lote_impressao_ait A")
				.fields("A.cd_ait, A.cd_lote_impressao, A.txt_erro, K.cd_pessoa, K.nm_pessoa, B.id_ait, B.dt_infracao, "
						+ "B.nr_placa, B.nr_renavan, B.nm_proprietario, L.dt_criacao ")
				.addJoinTable("LEFT OUTER JOIN mob_ait B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("LEFT OUTER JOIN mob_lote_impressao I ON (A.cd_lote_impressao = I.cd_lote_impressao)")
				.addJoinTable("LEFT OUTER JOIN grl_lote L ON (L.cd_lote = I.cd_lote)")
				.addJoinTable("LEFT OUTER JOIN seg_usuario J ON (J.cd_usuario = L.cd_criador)")
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
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", loteImpressaoSearch.getCdLoteImpressao(), loteImpressaoSearch.getCdLoteImpressao() > -1);
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
	public Search<LoteImpressaoDTO> getLote(LoteImpressaoSearch loteImpressaoSearch) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<LoteImpressaoDTO> lote = getLote(loteImpressaoSearch, customConnection);
			customConnection.finishConnection();
			return lote;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Search<LoteImpressaoDTO> getLote(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception {
		Search<LoteImpressaoDTO> search = new SearchBuilder<LoteImpressaoDTO>("mob_lote_impressao A")
				.fields("A.*, D.cd_pessoa, D.nm_pessoa, F.id_lote, F.dt_criacao, "
					  + "(SELECT COUNT(B.cd_lote_impressao) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao) as qtd_Documentos, "
					  + "(SELECT COUNT(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao AND B.st_impressao > " + LoteImpressaoAitSituacao.AGUARDANDO_GERACAO + ") as total_gerados ")
				.searchCriterios(setCriteriosSearchLote(loteImpressaoSearch))
				.addJoinTable("LEFT OUTER JOIN mob_lote_impressao_ait B ON (B.cd_lote_impressao = A.cd_lote_impressao)")
				.addJoinTable("LEFT OUTER JOIN grl_lote F ON (F.cd_lote = A.cd_lote)")
				.addJoinTable("LEFT OUTER JOIN seg_usuario C ON (C.cd_usuario = F.cd_criador)")
                .addJoinTable("LEFT OUTER JOIN grl_pessoa D ON (D.cd_pessoa = C.cd_pessoa)")
                .addJoinTable("LEFT OUTER JOIN mob_ait E ON (B.cd_ait = E.cd_ait)")
				.additionalCriterias(" NOT EXISTS "
						+ " ("
						+ " 	SELECT st_lote FROM mob_lote_impressao C"
						+ "		WHERE "
						+ "			("
						+ " 			st_lote = " + StatusLoteImpressaoEnum.ECARTAS_SOLICITAR_CANCELAMENTO.getKey()
						+ "			)"
						+ " 		AND C.cd_lote_impressao = A.cd_lote_impressao"
						+ " )")
				.groupBy(" A.cd_lote_impressao, D.cd_pessoa, F.cd_lote ")
				.orderBy(" F.dt_criacao DESC ")
				.customConnection(customConnection)
				.build();
		return search;
	}
	
	private SearchCriterios setCriteriosSearchLote(LoteImpressaoSearch loteImpressaoSearch) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", loteImpressaoSearch.getCdLoteImpressao(), loteImpressaoSearch.getCdLoteImpressao() > 0);
		return searchCriterios;
	}
	
}
