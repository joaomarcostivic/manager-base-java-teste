package com.tivic.manager.mob.restituicao;

import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;

public class GerarRelatorioRestituicao implements IGerarRelatorioRestituicao {
	private double totalMultas = 0;

	@Override
	public byte[] gerar(List<RestituicaoDTO> aitsList) throws Exception {
		return imprimirRelatorio(aitsList).getReportPdf("mob/restituicao_de_pagamentos");
	}
	
	private Report imprimirRelatorio(List<RestituicaoDTO> aitsList) throws Exception {
		setarCamposImpressao(aitsList);
		ReportCriterios reportCriterios = getReportCriterios();
		Report report = new ReportBuilder()
			.list(aitsList)
			.reportCriterios(reportCriterios)
			.build();
		return report;
	}
	
	private void setarCamposImpressao(List<RestituicaoDTO> aitsList) {
		this.totalMultas = 0;
		for (RestituicaoDTO restituicao : aitsList) {
			restituicao.setNmSituacaoAtual(TipoStatusEnum.valueOf(restituicao.getTpStatus()));
			this.totalMultas += restituicao.getVlMulta();
		}
	}
	
	private ReportCriterios getReportCriterios() throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("TOTAL_MULTAS", this.totalMultas);
		return reportCriterios;
	}

}
