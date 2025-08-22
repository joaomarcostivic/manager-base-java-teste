package com.tivic.manager.mob.ait.relatorios;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.tivic.manager.grl.FormularioAtributoValorServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.correios.CorreiosEtiquetaRepositoryDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class RelatorioRecursoServices implements IRelatorioRecursoServices {
	private int PRAZOCORRIDO = 30;
	
	@Override
	public Report reportRecursoJari(SearchCriterios searchCriterios) throws Exception {
		Search<RelatorioRecursoDTO> search = new SearchBuilder<RelatorioRecursoDTO>("mob_ait A")
				.fields(" A.cd_ait, A.nr_placa, A.dt_prazo_defesa, A.dt_resultado_defesa, A.dt_resultado_jari, A.dt_vencimento, A.nm_proprietario, A.id_ait, " +
						" A.nm_proprietario_autuacao, A.dt_ar_nai, A.nr_controle, B.dt_envio as dt_correios, B.tp_status as tp_status_correios, " +
						" C.nr_processo, C.tp_status, C.st_publicacao_do, C.dt_publicacao_do as dt_edital, C.dt_movimento as dt_recurso_jari, D.nr_cod_detran," + 
						" G.nr_documento as nr_sessao, F.cd_documento ")
				.addJoinTable(" LEFT OUTER JOIN mob_correios_etiqueta 		B ON(A.cd_ait = B.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_movimento 			C ON(A.cd_ait = C.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN mob_infracao 				D ON (D.cd_infracao = A.cd_infracao)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_movimento_documento E ON (E.cd_ait = A.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN ptc_documento 				F ON (F.cd_documento = E.cd_documento)")
				.addJoinTable(" LEFT OUTER JOIN ptc_documento 				G ON (G.cd_documento = F.cd_documento_superior)")
				.searchCriterios(searchCriterios)
				.build();
		
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", "PREFEITURA DE MARIANA-MG");
		reportCriterios.addParametros("DS_TITULO_2", "SECRETARIA MUNICIPAL DE DEFESA SOCIAL");
		reportCriterios.addParametros("DS_TITULO_3", "DEPARTAMENTO MUNICIPAL DE TRÂNSITO");
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		
		while(search.getRsm().next()) {
			if(search.getRsm().getInt("TP_STATUS_CORREIOS") == AitMovimentoServices.NAI_ENVIADO)
				reportCriterios.addParametros("DT_CORREIOS", dealDateNull(search.getRsm().getGregorianCalendar("DT_CORREIOS")));
			if(search.getRsm().getInt("TP_STATUS_CORREIOS") == AitMovimentoServices.NIP_ENVIADA)
				reportCriterios.addParametros("DT_CORREIOS_NIP", dealDateNull(search.getRsm().getGregorianCalendar("DT_CORREIOS")));
			CorreiosEtiqueta etiqueta = getEtiqueta(search.getRsm().getInt("CD_AIT"), AitMovimentoServices.NAI_ENVIADO);
			switch(search.getRsm().getInt("TP_STATUS")) {
				case AitMovimentoServices.NAI_ENVIADO:
					if(search.getRsm().getInt("ST_PUBLICACAO_DO") == AitMovimentoServices.PUBLICACAO_NAI)
						reportCriterios.addParametros("DT_EDITAL", dealDateNull(search.getRsm().getGregorianCalendar("DT_EDITAL")));
					if(etiqueta.getDtAvisoRecebimento() != null)
						reportCriterios.addParametros("DT_DESTINATARIO_NAI", etiqueta.getDtAvisoRecebimento());
					
					break;
				case AitMovimentoServices.JARI_SEM_PROVIMENTO:
					reportCriterios.addParametros("NR_RECURSO_JARI", search.getRsm().getString("NR_PROCESSO"));
					reportCriterios.addParametros("NR_SESSAO", search.getRsm().getString("NR_SESSAO"));
					reportCriterios.addParametros("ST_JULGAMENTO", AitMovimentoServices.JARI_SEM_PROVIMENTO);
					reportCriterios.addParametros("DT_DECISAO_JARI", dealDateNull(search.getRsm().getGregorianCalendar("DT_EDITAL")));
					reportCriterios.addParametros("DT_JULGAMENTO_JARI", dealDateNull(search.getRsm().getGregorianCalendar("DT_RESULTADO_JARI")));
					break;
				case AitMovimentoServices.RECURSO_JARI:
					reportCriterios.addParametros("DT_RECURSO_JARI", dealDateNull(search.getRsm().getGregorianCalendar("DT_RECURSO_JARI")));
					reportCriterios.addParametros("TEMPESTIVO", (search.getRsm().getGregorianCalendar("DT_PRAZO_DEFESA") != null?(search.getRsm().getGregorianCalendar("DT_PRAZO_DEFESA").after(search.getRsm().getGregorianCalendar("DT_RECURSO_JARI")) == true? 1 : 0): null));
					reportCriterios.addParametros("NR_PROCESSAMENTO_PRODEMGE", search.getRsm().getString("NR_CONTROLE"));
					reportCriterios.addParametros("DT_EDITAL", dealDateNull(search.getRsm().getGregorianCalendar("DT_EDITAL")));
					if(!(reportCriterios.getParametros().get("NM_REQUERENTE") != null))
						reportCriterios.addParametros("NM_REQUERENTE", FormularioAtributoValorServices.getValorByDocumentoAtributo(search.getRsm().getInt("CD_DOCUMENTO"), "nmRequerente"));
					reportCriterios.addParametros("CD_INFRACAO", search.getRsm().getInt("NR_COD_DETRAN"));
					reportCriterios.addParametros("NR_PLACA", search.getRsm().getString("NR_PLACA"));
					reportCriterios.addParametros("NR_AUTO_INFRACAO", search.getRsm().getString("ID_AIT"));
					break;
				case AitMovimentoServices.NIP_ENVIADA:
					reportCriterios.addParametros("DT_DESTINATARIO_NIP", etiqueta.getDtAvisoRecebimento());
					if(search.getRsm().getInt("ST_PUBLICACAO_DO") == AitMovimentoServices.PUBLICACAO_NIP)
						reportCriterios.addParametros("DT_EDITAL_NIP", dealDateNull(search.getRsm().getGregorianCalendar("DT_EDITAL")));
				
					break;
				case AitMovimentoServices.RECURSO_CETRAN:
					reportCriterios.addParametros("DT_EMISSAO_AUTUACAO_CORREIO", dealDateNull(search.getRsm().getGregorianCalendar("DT_EMISSAO_AUTUACAO_CORREIO")));
					reportCriterios.addParametros("DT_RECURSO_CETRAN", dealDateNull(search.getRsm().getGregorianCalendar("DT_RECURSO_JARI")));
					reportCriterios.addParametros("TEMPESTIVOCETRAN", ((search.getRsm().getGregorianCalendar("DT_RESULTADO_JARI") != null? (search.getRsm().getGregorianCalendar("DT_RESULTADO_JARI").after(somaDataPrazo(search.getRsm().getGregorianCalendar("DT_RESULTADO_JARI"))) == true? 0 : 1):null)));
					break;
				default:
					if(etiqueta.getDtAvisoRecebimento() != null) {
						reportCriterios.addParametros("DT_DESTINATARIO_NAI", etiqueta.getDtAvisoRecebimento());
					}
			}
		}
		
		Report report = new ReportBuilder()
				.search(search)
				.reportCriterios(reportCriterios)
				.build();
		
		return report;
	}
	
	private Date dealDateNull(GregorianCalendar date) {
		if(date != null) 
			return date.getTime();
		return null;
	}
	
	private GregorianCalendar somaDataPrazo(GregorianCalendar dataEntrada) {
		if(dataEntrada == null)
			return null;
		
		dataEntrada.add(Calendar.DAY_OF_MONTH, PRAZOCORRIDO);
		return dataEntrada;
	}
	
	private static CorreiosEtiqueta getEtiqueta(int cdAit, int tpStatus) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("B.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus);
		CorreiosEtiqueta etiqueta = new CorreiosEtiquetaRepositoryDAO().find(searchCriterios, new CustomConnection()).get(0);
		return etiqueta;
	}
}
