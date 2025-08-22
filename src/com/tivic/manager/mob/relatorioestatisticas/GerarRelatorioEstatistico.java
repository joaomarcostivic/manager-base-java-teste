package com.tivic.manager.mob.relatorioestatisticas;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.SearchCriterios;

public class GerarRelatorioEstatistico implements IGerarRelatorioEstatistico {

	@Override
	public byte[] gerar(List<RelatorioEstatisticasDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		return imprimirRelatorio(itemsList, searchCriterios).getReportPdf("mob/relatorio_estatistico_nai");
	}
	
	@Override
	public byte[] gerarRelatorioNips(List<RelatorioEstatisticasNipDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		return imprimirRelatorioNip(itemsList, searchCriterios).getReportPdf("mob/relatorio_estatistico_nip");
	}
	
	@Override
	public byte[] gerarGraficoNais(List<RelatorioEstatisticasDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		return imprimirRelatorio(itemsList, searchCriterios).getReportPdf("mob/relatorio_estatistico_grafico");
	}
	
	@Override
	public byte[] gerarGraficoNips(List<RelatorioEstatisticasNipDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		return imprimirRelatorioNip(itemsList, searchCriterios).getReportPdf("mob/relatorio_estatistico_grafico_nip");
	}
	
	private Report imprimirRelatorio(List<RelatorioEstatisticasDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		ReportCriterios reportCriterios = getReportCriterios(searchCriterios);
		Report report = new ReportBuilder()
			.list(itemsList)
			.reportCriterios(reportCriterios)
			.build();
		return report;
	}
	
	private Report imprimirRelatorioNip(List<RelatorioEstatisticasNipDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		ReportCriterios reportCriterios = getReportCriterios(searchCriterios);
		Report report = new ReportBuilder()
				.list(itemsList)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}

	private ReportCriterios getReportCriterios(SearchCriterios searchCriterios) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
        String dtInicial = LocalDate.parse(searchCriterios.getAndRemoveCriterio("dt_inicial").getValue()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String dtFinal = LocalDate.parse(searchCriterios.getAndRemoveCriterio("dt_final").getValue()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("DT_INICIAL", dtInicial);
		reportCriterios.addParametros("DT_FINAL", dtFinal);
		reportCriterios.addParametros("TP_CONSULTA", Integer.parseInt(searchCriterios.getAndRemoveCriterio("tp_consulta").getValue()));
		reportCriterios.addParametros("TP_GRAFICO", Integer.parseInt(searchCriterios.getAndRemoveCriterio("tp_grafico").getValue()));
		return reportCriterios;
	}
}
