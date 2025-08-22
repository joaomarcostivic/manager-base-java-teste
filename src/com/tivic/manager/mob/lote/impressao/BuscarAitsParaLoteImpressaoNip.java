package com.tivic.manager.mob.lote.impressao;

import java.sql.Types;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class BuscarAitsParaLoteImpressaoNip implements IBuscarAitsParaLoteImpressao {

	@Override
	public Search<AitDTO> buscarAitsParaLoteImpressao(int quantidadeAit) throws Exception {
		SearchCriterios searchCriterios = montarCriterios(quantidadeAit);
		return searchAits(searchCriterios);
	}
	
	public Search<AitDTO> searchAits(SearchCriterios searchCriterios) throws Exception {
		Search<AitDTO> search = new SearchBuilder<AitDTO>("MOB_AIT A")
				.fields(" DISTINCT ON (A.cd_ait) A.cd_ait, A.id_ait, A.dt_infracao, A.nr_placa, "
					   +" K.dt_movimento, K.tp_status, K.nr_processo, K.lg_enviado_detran ")
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento K ON (A.cd_ait = K.cd_ait)")
				.searchCriterios(searchCriterios)
				.additionalCriterias(" NOT EXISTS "
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
								   + "       			AND B3.cd_ait = A.cd_ait ORDER BY B3.dt_movimento DESC limit 1 "
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
								   + "				( D.tp_documento IN ( " + TipoLoteNotificacaoEnum.LOTE_NIP.getKey() + ", " 
								   + 										TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey() +  " ) AND A.cd_ait = E.cd_ait )"
								   + " 		)"
								   + "		OR EXISTS "
								   + "		( "
								   + "			SELECT E1.cd_ait FROM mob_lote_impressao_ait E1 "
								   + "				WHERE E1.cd_ait = A.cd_ait and E1.cd_lote_impressao = "
								   + "				("
								   + "					SELECT D1.cd_lote_impressao FROM mob_lote_impressao D1 "
								   + "					JOIN mob_lote_impressao_ait E2 ON (E2.cd_lote_impressao = D1.cd_lote_impressao)"
								   + "						WHERE D1.tp_documento IN ( " + TipoLoteNotificacaoEnum.LOTE_NIP.getKey() + ", "
								   + "														" + TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey() 
								   + "						) ORDER BY D1.dt_criacao DESC LIMIT 1"
								   + "				) AND E1.cd_ait = A.cd_ait AND E1.st_impressao = " + LoteImpressaoAitSituacao.REGISTRO_CANCELADO
								   + "		)"
								   + "	)"
								   + " " + incluirCondicionalAdesaoSne()
								   + " ")
				.orderBy("A.cd_ait DESC, K.dt_movimento DESC")
				.count()
				.build();
		return search;
	}
	
	private SearchCriterios montarCriterios(int quantidadeDocumento) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("K.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		searchCriterios.setQtLimite(quantidadeDocumento);
		return searchCriterios;
	}
	
	private String incluirCondicionalAdesaoSne() throws Exception {
		int sneAdesaoOrgaoAutuador = ParametroServices.getValorOfParametroAsInteger("MOB_SNE_ADESAO_ORGAO_AUTUADOR", 0);
		if(sneAdesaoOrgaoAutuador == TipoAdesaoSneEnum.COM_OPCAO_SNE.getKey()) {
			return " AND NOT EXISTS( SELECT L.cd_ait FROM mob_ait_movimento L"
	                    + " WHERE("
	                    + " L.st_adesao_sne = " + TipoAdesaoSneEnum.COM_OPCAO_SNE.getKey() 
	                    + " AND L.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey()
	                    + ") AND L.cd_ait = A.cd_ait )";
	    }
	    return "";
	}
	
	@Override
	public Search<AitDTO> montarSearchLoteImpressao(SearchCriterios searchCriterios) throws Exception {
		searchCriterios.getAndRemoveCriterio("K.tp_status");
		String status = String.valueOf(TipoStatusEnum.NIP_ENVIADA.getKey()) + "," + String.valueOf(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
		searchCriterios.addCriteriosEqualInteger("K.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		searchCriterios.addCriterios("K.tp_status", status, ItemComparator.IN, Types.INTEGER);
		montarCriterios(searchCriterios.getQtLimite());
		return searchAits(searchCriterios);
	}

}
