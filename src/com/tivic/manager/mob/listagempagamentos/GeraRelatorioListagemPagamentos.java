package com.tivic.manager.mob.listagempagamentos;

import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.aitpagamento.enums.SituacaoPagamentoEnum;
import com.tivic.manager.mob.aitpagamento.enums.TipoArrecadacaoEnum;
import com.tivic.manager.util.StringUtil;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class GeraRelatorioListagemPagamentos implements IGeraRelatorioListagemPagamentos {
	String tpGrafico;
	String campoAnalisado;
	
	@Override
	public byte[] gerar(List<RelatorioPagamentoDTO> itemsList) throws Exception {
		return imprimir(itemsList, new SearchCriterios()).getReportPdf("mob/relatorio_listagem_pagamentos");
	}
	
	@Override
	public byte[] gerarGrafico(List<RelatorioPagamentoDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		campoAnalisado = searchCriterios.getAndRemoveCriterio("campo_analisado").getValue();
		tpGrafico = searchCriterios.getAndRemoveCriterio("tp_grafico").getValue();
		List<RelatorioPagamentoDTO> lista =  findLista(searchCriterios, campoAnalisado);
		return imprimir(lista, searchCriterios).getReportPdf("mob/grafico_listagem_pagamentos");
	}
	
	private List<RelatorioPagamentoDTO> findLista(SearchCriterios searchCriterios, String campoAnalisado) throws Exception {
		campoAnalisado = StringUtil.camelToSnake(campoAnalisado);
		Search<RelatorioPagamentoDTO> search = new SearchBuilder<RelatorioPagamentoDTO>("mob_ait_pagamento A")
				.fields("COUNT(A.cd_ait) AS value_axis, " + campoAnalisado + " AS category_axis ")
				.addJoinTable(" JOIN mob_ait B ON (A.cd_ait = B.cd_ait) ")
				.addJoinTable(" JOIN mob_infracao C ON (B.cd_infracao = C.cd_infracao) ")
				.addJoinTable(" JOIN grl_banco D ON (A.nr_banco = D.nr_banco) ")
				.addJoinTable(" LEFT OUTER JOIN grl_agencia E ON (A.nr_agencia = E.nr_agencia) ")
				.groupBy(campoAnalisado)
				.searchCriterios(searchCriterios)
				.count()
			.build();
		List<RelatorioPagamentoDTO> listResultadoEntradaNaiDTO = search.getList(RelatorioPagamentoDTO.class);
		return listResultadoEntradaNaiDTO;			
	}
	
	private Report imprimir(List<RelatorioPagamentoDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		ReportCriterios reportCriterios = getReportCriterios(searchCriterios);
		if (campoAnalisado != null) setarDescricao(itemsList, campoAnalisado);
		Report report = new ReportBuilder()
			.list(itemsList)
			.reportCriterios(reportCriterios)
			.build();
		return report;
	}

	private void setarDescricao(List<RelatorioPagamentoDTO> itemsList, String campoAnalisado) {
		if (!campoAnalisado.equals("stPagamento") && !campoAnalisado.equals("tpModalidade") && !campoAnalisado.equals("tpArrecadacao") && !campoAnalisado.equals("ufPagamento")) return;
		for (RelatorioPagamentoDTO relatorioPagamentoDTO : itemsList) {
			if (campoAnalisado.equals("ufPagamento")) {
				relatorioPagamentoDTO.setCategoryAxis(relatorioPagamentoDTO.getCategoryAxis() == null ? "Não informado" : relatorioPagamentoDTO.getCategoryAxis());
				continue;
			}
			int categoryAxisValue = Integer.parseInt(relatorioPagamentoDTO.getCategoryAxis());
			if(campoAnalisado.equals("stPagamento"))
				relatorioPagamentoDTO.setCategoryAxis(SituacaoPagamentoEnum.valueOf(categoryAxisValue));
			if(campoAnalisado.equals("tpModalidade")) {
				String dsTpFormaPagamento = (categoryAxisValue == 0) ? "Não informado" : FormaPagamentoEnum.valueOf(categoryAxisValue);
				relatorioPagamentoDTO.setCategoryAxis(dsTpFormaPagamento);
			}
			if (campoAnalisado.equals("tpArrecadacao")) {
				String dsTpArrecadacao = (categoryAxisValue == 0) ? "Não informado" : TipoArrecadacaoEnum.valueOf(categoryAxisValue).split("/")[0];
				relatorioPagamentoDTO.setCategoryAxis(dsTpArrecadacao);
			}
		}
	}
	
	private ReportCriterios getReportCriterios(SearchCriterios searchCriterios) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("TP_GRAFICO", tpGrafico);
		return reportCriterios;
	}
}
