package com.tivic.manager.mob.lotes.service.impressao.consulta;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.IParametroService;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.inconsistencias.TipoInconsistenciaEnum;
import com.tivic.manager.mob.infracao.TipoCompetenciaEnum;
import com.tivic.manager.mob.lotes.enums.impressao.PrazosNipEnum;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ListAitsCadidatasFimPrazoDefesaService implements IListAitsCandidatasNotificacao {
	private CustomConnection customConnection;
	private List<Ait> aitList;
	private SearchCriterios searchCriterios;
	private IParametroService parametroService;
	
	public ListAitsCadidatasFimPrazoDefesaService(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		aitList = new ArrayList<Ait>();
		this.customConnection = customConnection;
		this.searchCriterios = searchCriterios;
		parametroService = (IParametroService) BeansFactory.get(IParametroService.class);
		aitList = buscarAitsCandidatas().getList(Ait.class);
	}
	
	private Search<Ait> buscarAitsCandidatas() throws Exception {
		Search<Ait> search = new SearchBuilder<Ait>("mob_ait A")
				.customConnection(customConnection)
				.fields(" A.*, B.*, C.*,"
					  + " G.ds_especie,"
					  + " H.nm_marca, H.nm_modelo ")
				.addJoinTable("JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("JOIN mob_infracao C ON (A.cd_infracao = C.cd_infracao)")
				.addJoinTable("LEFT OUTER JOIN fta_especie_veiculo G ON (A.cd_especie_veiculo = G.cd_especie)")
				.addJoinTable("LEFT OUTER JOIN fta_marca_modelo H  ON (A.cd_marca_veiculo = H.cd_marca)")
				.searchCriterios(searchCriteriosAitsCandidatas())
				.additionalCriterias( "( NOT EXISTS (SELECT B1.tp_status, B1.lg_enviado_detran from mob_ait_movimento B1"
							   + "				 		WHERE (B1.tp_status = " + TipoStatusEnum.FIM_PRAZO_DEFESA.getKey() + ")"
						   	   + "				 		AND B1.cd_ait = A.cd_ait "
						   	   + "					)"
						   	   + "       OR EXISTS ("
							   + "	     	SELECT B2.cd_ait, B2.dt_movimento, B2.tp_status from mob_ait_movimento B2"
							   + "       	WHERE (B2.tp_status = " + TipoStatusEnum.CANCELAMENTO_NIP.getKey()
							   + "					AND B2.dt_movimento > ("
							   + "						SELECT B3.dt_movimento FROM mob_ait_movimento B3 WHERE B3.tp_status = " + TipoStatusEnum.FIM_PRAZO_DEFESA.getKey()
							   + "                       AND B3.cd_ait = B2.cd_ait ORDER BY dt_movimento DESC limit 1)"
							   + "			)" 
							   + "          AND B2.cd_ait = A.cd_ait ORDER BY B2.dt_movimento DESC"
							   + "       )"
							   + "     )"
						   	   + "		 AND NOT EXISTS ("
							   + " 				SELECT tp_status FROM mob_ait_movimento B "
							   + "				WHERE("
							   + "					tp_status =    " + TipoStatusEnum.CADASTRO_CANCELADO.getKey()
							   + "					OR tp_status = " + TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey()
							   + "					OR tp_status = " + TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey()
							   + "					OR tp_status = " + TipoStatusEnum.CANCELAMENTO_MULTA.getKey()
							   + "	 				OR tp_status = " + TipoStatusEnum.CANCELAMENTO_PONTUACAO.getKey()
							   + "					OR tp_status = " + TipoStatusEnum.DEVOLUCAO_PAGAMENTO.getKey()
							   + " 				)"
							   + "				AND B.cd_ait = A.cd_ait"	
							   + "  	 )"
							   + "       AND ("
							   + "           NOT EXISTS ("
							   + " 				SELECT tp_status FROM mob_ait_movimento B4 "
							   + "				WHERE("
							   + "					tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey()
							   + " 				)"
							   + "				AND B4.cd_ait = A.cd_ait"	
							   + "  	     )"
							   + "		     OR EXISTS ( "
							   + "		 		SELECT B5.tp_status from mob_ait_movimento B5"
							   + "				WHERE (B5.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() + ") AND B5.cd_ait = A.cd_ait"
							   + "					AND EXISTS ("
							   + "         				SELECT B6.tp_status, B6.dt_movimento, B6.lg_enviado_detran FROM mob_ait_movimento B6"
							   + "						WHERE (B6.tp_status = " + TipoStatusEnum.CANCELAMENTO_NIP.getKey() 
							   + "                             AND B6.dt_movimento > ("
							   + "                             		SELECT B7.dt_movimento FROM mob_ait_movimento B7 WHERE B7.tp_status = " + TipoStatusEnum.FIM_PRAZO_DEFESA.getKey()
							   + "       							AND B7.cd_ait = B6.cd_ait ORDER BY dt_movimento DESC limit 1 )"
							   + "						) AND B6.cd_ait = A.cd_ait ORDER BY B6.dt_movimento DESC" 
							   + "                  ) AND B5.cd_ait = A.cd_ait" 
							   + "           )"
							   + "       )"
							   + "		 AND EXISTS "
							   + "		 ( "
							   + "			SELECT B.cd_ait, B.dt_movimento "	
							   + " 			FROM mob_ait_movimento B "	
							   + "				WHERE CAST(A.dt_prazo_defesa as date) + "
							   + "				" + ParametroServices.getValorOfParametroAsInteger("MOB_PRAZO_DEFESA", 10) + " < CURRENT_DATE "
							   + " 				AND B.cd_ait = A.cd_ait "	
							   + "		 )"
							   + "	     AND EXISTS "
							   + "		 ( "
							   + "			SELECT I.cd_ait, I.dt_infracao, J.tp_status, J.dt_movimento FROM mob_ait I "
							   + "				LEFT JOIN mob_ait_movimento J on (J.cd_ait = I.cd_ait) "
							   + "			WHERE "	
							   + "			( "
							   + "				J.tp_status = " + TipoStatusEnum.NAI_ENVIADO.getKey()
							   + "				AND CASE WHEN I.dt_infracao > '"+formatDatePrazoLegal()+ "'"
							   + "				AND CAST(I.dt_infracao as date) + " + PrazosNipEnum.PRAZO_180_DIAS.getKey() + " > CURRENT_DATE "
							   + "				THEN true "
							   + "				ELSE false END  "
							   + "				OR J.tp_status = " + AitMovimentoServices.DEFESA_INDEFERIDA + " AND I.dt_infracao > "
							   + "				'" +formatDatePrazoLegal()+"' AND CAST(I.dt_infracao as date) + "
							   + "				 " + PrazosNipEnum.PRAZO_360_DIAS.getKey() + " > CURRENT_DATE "				
							   + "				OR J.tp_status = " + TipoStatusEnum.NAI_ENVIADO.getKey() + " AND I.dt_infracao < '"+formatDatePrazoLegal()+"'"
							   + "			) "
							   + "			AND I.cd_ait = A.cd_ait "
							   + "		 )"
							   + "		 AND NOT EXISTS "
							   + " 		 ( "
							   + "			SELECT C.* FROM MOB_AIT_INCONSISTENCIA C "
							   + "			JOIN mob_inconsistencia Z on (Z.cd_inconsistencia = C.cd_inconsistencia)"
							   + " 			WHERE "
							   + "				( "
							   + " 					Z.tp_inconsistencia = " + TipoInconsistenciaEnum.TP_INDEFERIMENTO.getKey()
							   + " 				)"
							   + " 			AND A.cd_ait = C.cd_ait "
							   + " 		 )"
							   + "	     AND("
							   + "			NOT EXISTS ( "
							   + "				SELECT D.cd_lote_impressao, D.tp_impressao, E.cd_ait FROM mob_lote_impressao D "
							   + "				JOIN mob_lote_impressao_ait E ON (D.cd_lote_impressao = E.cd_lote_Impressao) "	
							   + " 				WHERE ( D.tp_impressao = " + TipoLoteImpressaoEnum.LOTE_NIP.getKey() 
							   + "              ) AND E.cd_ait = A.cd_ait "
							   + " 		 	)"
							   + "			OR EXISTS ( "
							   + "				SELECT E1.* FROM mob_lote_impressao_ait E1 "
							   + "					WHERE E1.cd_lote_impressao = ("
							   + "				    	SELECT D1.cd_lote_impressao FROM mob_lote_impressao D1 "
							   + "						JOIN mob_lote_impressao_ait E2 ON (E2.cd_lote_impressao = D1.cd_lote_impressao)"
							   + "						WHERE E2.cd_ait = A.cd_ait AND D1.tp_impressao = " + TipoLoteImpressaoEnum.LOTE_NIP.getKey() 
							   + "						ORDER BY D1.cd_lote DESC LIMIT 1"
							   + "					) AND E1.cd_ait = A.cd_ait AND E1.st_impressao = " + LoteImpressaoAitSituacao.REGISTRO_CANCELADO
							   + "			)"
							   + "		) "
							   + "		 AND NOT EXISTS "
							   + " 		 ( "
							   + "			SELECT C.tp_competencia FROM mob_infracao C  "
							   + " 			WHERE "
							   + "				( "
							   + " 					C.tp_competencia = " + TipoCompetenciaEnum.ESTADUAL.getKey()
							   + " 				)"
							   + " 		 AND A.cd_infracao = C.cd_infracao "						
							   + " 		 )"
							   + " " )
				.orderBy(" A.cd_ait DESC ")
				.build();
		return search;
				
	}
	
	private String formatDatePrazoLegal() throws Exception {
		String inicioPrazoLegal  = parametroService.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_DATA_INICIO_EMISSAO_NP").getTxtValorParametro();
		if(inicioPrazoLegal == null || Objects.equals(inicioPrazoLegal, "0") ){
			throw new ValidacaoException("O parâmetro MOB_INFORMACOES_ADICIONAIS_DATA_INICIO_EMISSAO_NP é obrigatório e não foi informado.");
		}
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate data = LocalDate.parse(inicioPrazoLegal, inputFormatter);
        String dataFormatada = data.format(outputFormatter);
		return dataFormatada;
	}
	
	private SearchCriterios searchCriteriosAitsCandidatas() {
		searchCriterios.addCriteriosEqualInteger("B.tp_status", TipoStatusEnum.NAI_ENVIADO.getKey(), true);
		searchCriterios.addCriteriosEqualInteger("B.lg_enviado_detran", TipoStatusEnum.ENVIADO_AO_DETRAN.getKey(), true);
		return searchCriterios;
	}
	
	public List<Ait> build() throws Exception {
		return aitList;
	}

}
