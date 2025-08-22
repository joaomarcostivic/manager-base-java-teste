package com.tivic.manager.mob.veiculosmultavencida;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ItemComparator;

public class VeiculosMultaVencidaService implements IVeiculosMultaVencidaService {
	
	@Override
	public PagedResponse<VeiculosMultaVencidaDTO> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<VeiculosMultaVencidaDTO> search = search(searchCriterios, customConnection);
			customConnection.finishConnection();
			return new PagedResponse<VeiculosMultaVencidaDTO>(search.getList(VeiculosMultaVencidaDTO.class), search.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public Search<VeiculosMultaVencidaDTO> search(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.addCriteriosMinorDate("dt_vencimento", DateUtil.formatDate(new Timestamp(DateUtil.getDataAtual().getTimeInMillis()), "yyyy-MM-dd"));
		searchCriterios.addCriterios("B.tp_status", String.valueOf(TipoStatusEnum.NIP_ENVIADA.getKey()), ItemComparator.IN, Types.INTEGER);
		List<String> listaMovimentos = new ArrayList<String>();
		listaMovimentos.add(String.valueOf(TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()));
		listaMovimentos.add(String.valueOf(TipoStatusEnum.CETRAN_DEFERIDO.getKey()));
		listaMovimentos.add(String.valueOf(TipoStatusEnum.PENALIDADE_SUSPENSA.getKey()));
		searchCriterios.addCriterios("A.tp_status", listaMovimentos.toString().replace("[", "").replace("]", ""), ItemComparator.NOTIN, Types.INTEGER);
		Search<VeiculosMultaVencidaDTO> search = new SearchBuilder<VeiculosMultaVencidaDTO>("mob_ait A")
				.fields("A.nr_placa, nm_proprietario, COUNT(A.cd_ait) as qt_multas_vencidas, SUM(A.vl_multa) as vl_total_multas_vencidas ")
				.addJoinTable(" LEFT JOIN mob_ait_movimento B ON(A.cd_ait = B.cd_ait)")
				.additionalCriterias(" NOT EXISTS (SELECT * FROM mob_ait_pagamento B WHERE B.cd_ait = A.cd_ait)")
				.searchCriterios(searchCriterios)
				.groupBy(" A.nr_placa, nm_proprietario ")
				.orderBy("A.nm_proprietario")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	@Override
	public PagedResponse<AitVeiculoMultaVencidaDTO> findAits(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<AitVeiculoMultaVencidaDTO> search = searchAits(searchCriterios, customConnection);
			customConnection.finishConnection();
			return new PagedResponse<AitVeiculoMultaVencidaDTO>(search.getList(AitVeiculoMultaVencidaDTO.class), search.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public Search<AitVeiculoMultaVencidaDTO> searchAits(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.addCriteriosMinorDate("dt_vencimento", DateUtil.formatDate(new Timestamp(DateUtil.getDataAtual().getTimeInMillis()), "yyyy-MM-dd"));
		Search<AitVeiculoMultaVencidaDTO> search = new SearchBuilder<AitVeiculoMultaVencidaDTO>("mob_ait A")
				.fields("A.id_ait, A.dt_infracao, A.dt_vencimento, A.vl_multa, A.tp_status, A.lg_enviado_detran ")
				.additionalCriterias(" NOT EXISTS (SELECT * FROM mob_ait_pagamento B WHERE B.cd_ait = A.cd_ait)")
				.searchCriterios(searchCriterios)
				.orderBy("A.dt_infracao DESC")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	@Override
	public byte[] imprimir(List<VeiculosMultaVencidaDTO> itemsList) throws Exception {
		return imprimirRelatorio(itemsList).getReportPdf("mob/relatorio_veiculos_multa_vencida");
	}
	
	private Report imprimirRelatorio(List<VeiculosMultaVencidaDTO> itemsList) throws Exception {
		ReportCriterios reportCriterios = getReportCriterios();
		Report report = new ReportBuilder()
			.list(itemsList)
			.reportCriterios(reportCriterios)
			.build();
		return report;
	}
	
	private ReportCriterios getReportCriterios() throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		return reportCriterios;
	}
}
