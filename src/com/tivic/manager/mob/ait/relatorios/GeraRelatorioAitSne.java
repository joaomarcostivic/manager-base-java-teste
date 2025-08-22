package com.tivic.manager.mob.ait.relatorios;

import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class GeraRelatorioAitSne implements IGeraRelatorioAitSne {
	
	public byte[] gerarSne(SearchCriterios searchCriterios) throws Exception {
		return printListagemAitsSne(searchCriterios).getReportPdf("mob/relatorio_de_aits_sne");
	}
	
	private Report printListagemAitsSne(SearchCriterios searchCriterios) throws Exception {
		ReportCriterios reportCriterios = montarReportCriterios(searchCriterios);
		List<RelatorioAitSneDTO> listDadosAits = filtrarAitsOpcaoSne(searchCriterios).getList(RelatorioAitSneDTO.class);
		setarDsTpStatus(listDadosAits);
		setaDsTpMovimento(listDadosAits);
		Report report = new ReportBuilder()
				.list(listDadosAits)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}
	
	private void setarDsTpStatus(List<RelatorioAitSneDTO> listDadosAits) {
		for (RelatorioAitSneDTO relatorioAitSneDTO : listDadosAits) {
			relatorioAitSneDTO.setDsTpStatus(TipoStatusEnum.valueOf(relatorioAitSneDTO.getTpStatus()));
		}
	}
	
	private void setaDsTpMovimento(List<RelatorioAitSneDTO> listDadosAits) {
		for(RelatorioAitSneDTO relatorioAitSneDTO : listDadosAits) {
			relatorioAitSneDTO.setDsTpMovimento(TipoStatusEnum.valueOf(relatorioAitSneDTO.getCtMovimento()));
		}
	}

	private ReportCriterios montarReportCriterios(SearchCriterios searchCriterios) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		
		List<String> filtros = new FiltroRelatorioSneBuilder(searchCriterios).construirFiltro(searchCriterios).build();
		reportCriterios.addParametros("NM_FILTROS", String.join(", ", filtros));
		return reportCriterios;
	}
	
	public Search<RelatorioAitSneDTO> filtrarAitsOpcaoSne(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		ItemComparator contendoMovimento = searchCriterios.getAndRemoveCriterio("B.tp_status");
		Search<RelatorioAitSneDTO> searchRelatorioAitDTO = contendoMovimento != null && Integer.valueOf(contendoMovimento.getValue()) > TipoStatusEnum.SITUACAO_NAO_DEFINIDA.getKey()
				? searchContendoMovimentoSne(contendoMovimento, searchCriterios) : searchAitsSne(searchCriterios);
		if(searchRelatorioAitDTO.getList(RelatorioAitSneDTO.class).isEmpty()) {
			throw new ValidacaoException ("Não há AIT's para geração de relatório!");
		}
		return searchRelatorioAitDTO;
	}
	
	public Search<RelatorioAitSneDTO> searchAitsSne(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		searchCriterios.getAndRemoveCriterio("B.dt_movimento_inicial");
		searchCriterios.getAndRemoveCriterio("B.dt_movimento_final");
		Search<RelatorioAitSneDTO> search = new SearchBuilder<RelatorioAitSneDTO>("mob_ait A")
				.fields("A.dt_infracao, B.dt_movimento, A.id_ait, A.nr_placa, A.tp_status, B.tp_status as ct_Movimento, C.nr_cod_detran, A.vl_multa")	
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("LEFT OUTER JOIN mob_infracao C ON (A.cd_infracao = C.cd_infracao)")
				.searchCriterios(searchCriterios)
				.orderBy("A.dt_infracao DESC")
				.count()
				.build();
		return search;
	}	
	
	public Search<RelatorioAitSneDTO> searchContendoMovimentoSne(ItemComparator contendoMovimento, SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		ItemComparator dtInicialMovimento = searchCriterios.getAndRemoveCriterio("B.dt_movimento_inicial");
		ItemComparator dtFinalMovimento = searchCriterios.getAndRemoveCriterio("B.dt_movimento_final");
		ItemComparator stAdesaoSne = searchCriterios.getAndRemoveCriterio("B.st_adesao_sne");
		Search<RelatorioAitSneDTO> search = new SearchBuilder<RelatorioAitSneDTO>("mob_ait A")
				.fields("A.dt_infracao, A.id_ait, A.nr_placa, A.tp_status, B.tp_status as ct_Movimento, C.nr_cod_detran, A.vl_multa")	
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait )")
				.addJoinTable("LEFT OUTER JOIN mob_infracao C ON (A.cd_infracao = C.cd_infracao)")
				.searchCriterios(searchCriterios)
				.additionalCriterias("EXISTS"
						+ " ("
						+ " 	SELECT G.cd_ait, G.tp_status, G.dt_movimento FROM mob_ait_movimento G"
						+ "		WHERE  ( G.tp_status = " + contendoMovimento.getValue()   
						+ "				 AND G.cd_ait = A.cd_ait AND G.st_adesao_sne = " + stAdesaoSne.getValue()
						+ " )) "
						+ "AND B.dt_movimento BETWEEN '" + dtInicialMovimento.getValue() + "'" 
						+ "AND '" + dtFinalMovimento.getValue() + "'"
						+ "AND B.tp_status = " + contendoMovimento.getValue())
				.orderBy("A.dt_infracao DESC")
				.count()
				.build();
		return search;	
	}
}
