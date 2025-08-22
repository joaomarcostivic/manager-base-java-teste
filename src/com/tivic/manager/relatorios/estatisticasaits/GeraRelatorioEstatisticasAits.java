package com.tivic.manager.relatorios.estatisticasaits;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.SearchCriterios;

public class GeraRelatorioEstatisticasAits implements IGeraRelatorioEstatisticasAits {
	
	@Override
	public byte[] gerar(List<RelatorioEstatisticasAitsDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		return imprimirRelatorio(itemsList, searchCriterios).getReportPdf("mob/relatorio_estatisticas_aits");
	}
	
	@Override
	public byte[] gerarGrafico(List<RelatorioEstatisticasAitsDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		return imprimirRelatorio(itemsList, searchCriterios).getReportPdf("mob/relatorio_grafico_estatisticas_aits");
	}
	
	private Report imprimirRelatorio(List<RelatorioEstatisticasAitsDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		int totalInfracoes = 0;
		for (RelatorioEstatisticasAitsDTO item : itemsList) {
			totalInfracoes += item.getQtdInfracoes();
		}
		searchCriterios.addCriteriosEqualInteger("total_infracoes", totalInfracoes);
		ReportCriterios reportCriterios = getReportCriterios(searchCriterios);
		Report report = new ReportBuilder()
			.list(itemsList)
			.reportCriterios(reportCriterios)
			.build();
		return report;
	}
	
	private ReportCriterios getReportCriterios(SearchCriterios searchCriterios) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dataInicial = sdf.parse(searchCriterios.getAndRemoveCriterio("dt_inicial").getValue());
        Date dataFinal = sdf.parse(searchCriterios.getAndRemoveCriterio("dt_final").getValue());
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("TOTAL_INFRACOES", Integer.parseInt(searchCriterios.getAndRemoveCriterio("total_infracoes").getValue()));
		reportCriterios.addParametros("DT_INICIAL", dataInicial);
		reportCriterios.addParametros("DT_FINAL", dataFinal);
		reportCriterios.addParametros("TP_RELATORIO", Integer.parseInt(searchCriterios.getAndRemoveCriterio("tp_relatorio").getValue()));
		reportCriterios.addParametros("TP_GRAFICO", Integer.parseInt(searchCriterios.getAndRemoveCriterio("tp_grafico").getValue()));
		return reportCriterios;
	}
}
