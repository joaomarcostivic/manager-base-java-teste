package com.tivic.manager.mob.ait.relatorios.quantitativo;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.relatorios.quantitativo.enums.TipoMovimentoQuantitativoEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class RelatorioQuantitativoService implements IRelatorioQuantitativoService{
	
	private AitMovimentoRepository aitMovimentoRepository;
	
	public RelatorioQuantitativoService() throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}
	
	@Override
	public byte[] gerar(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception {
		return printRelatorio(relatorioQuantitativoSearch).getReportPdf("mob/relatorio_quantitativo");
	}

	@Override
	public PagedResponse<RelatorioQuantitativoDTO> buscarRelatorioQuantitativo(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws ValidacaoException, Exception {
	    List<RelatorioQuantitativoDTO> relatorioQuantitativoList = processarQuantitativoMovimentos(relatorioQuantitativoSearch);
	    relatorioQuantitativoList.add(buscarNipsComPagamento(relatorioQuantitativoSearch));
	    relatorioQuantitativoList.add(buscarNipsSemPagamento(relatorioQuantitativoSearch));
	    relatorioQuantitativoList.add(buscarPagamentos(relatorioQuantitativoSearch));
	    return new PagedResponse<RelatorioQuantitativoDTO>(relatorioQuantitativoList, relatorioQuantitativoList.size());
	}

	private List<RelatorioQuantitativoDTO> processarQuantitativoMovimentos(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception {
	    List<RelatorioQuantitativoDTO> quantitativoMovimentos = buscarQuantitativoMovimentos(relatorioQuantitativoSearch);
	    if (quantitativoMovimentos == null || quantitativoMovimentos.isEmpty()) {
	        return null;
	    }
	    Map<Integer, Integer> tpStatusQuantidadesMap = new HashMap<>();
	    for (RelatorioQuantitativoDTO relatorio : quantitativoMovimentos) {
	        tpStatusQuantidadesMap.merge(relatorio.getTpStatus(), relatorio.getQuantidadeMovimentos(), Integer::sum);
	    }
	    List<RelatorioQuantitativoDTO> relatorioQuantitativoList = new ArrayList<>();
	    tpStatusQuantidadesMap.forEach((tpStatus, quantidade) -> {
	        RelatorioQuantitativoDTO relatorioQuantitativoDTO = new RelatorioQuantitativoDTO();
	        relatorioQuantitativoDTO.setTpStatus(tpStatus);
	        relatorioQuantitativoDTO.setQuantidadeMovimentos(quantidade);
	        relatorioQuantitativoList.add(relatorioQuantitativoDTO);
	    });
	    return relatorioQuantitativoList;
	}


	private List<RelatorioQuantitativoDTO> buscarQuantitativoMovimentos(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception {
	    Search<RelatorioQuantitativoDTO> quantitativoMovimentos = this.aitMovimentoRepository.findQuantitativoMovimentos(setCriteriosSearchQuantitativo(relatorioQuantitativoSearch));
	    if (quantitativoMovimentos.getList(RelatorioQuantitativoDTO.class).isEmpty()) {
	        return null;
	    }
	    List<RelatorioQuantitativoDTO> listaAtualizada = new ArrayList<>(quantitativoMovimentos.getList(RelatorioQuantitativoDTO.class));
	    for (int i = 0; i < listaAtualizada.size(); i++) {
	        RelatorioQuantitativoDTO relatorio = listaAtualizada.get(i);
	        int tpStatus = relatorio.getTpStatus();
	        if (tpStatus == TipoMovimentoQuantitativoEnum.DEFESAS_ADVERTENCIA_AUTUACAO_PROTOCOLOCADA.getKey()) {
	            relatorio.setTpStatus(TipoMovimentoQuantitativoEnum.DEFESAS_PREVIA_AUTUACAO_PROTOCOLOCADA.getKey());
	            listaAtualizada.set(i, relatorio);
	        }
	    }
	    return listaAtualizada;
	}

	private RelatorioQuantitativoDTO buscarNipsComPagamento(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception {
		Search<RelatorioQuantitativoDTO> relatorioSearch = this.aitMovimentoRepository.findQuantitativoPagamentoNip(setCriteriosSearchNipPaga(relatorioQuantitativoSearch), 
				TipoMovimentoQuantitativoEnum.RECEBIMENTO_PENALIDADE_MULTA_PROCESSADAS_PERIODO.getKey());
		if(relatorioSearch.getList(RelatorioQuantitativoDTO.class).isEmpty()) {
			return null;
		}
		RelatorioQuantitativoDTO relatorioQuantitativoDTO = relatorioSearch.getList(RelatorioQuantitativoDTO.class).get(0);
		relatorioQuantitativoDTO.setTpStatus(TipoMovimentoQuantitativoEnum.RECEBIMENTO_PENALIDADE_MULTA_PROCESSADAS_PERIODO.getKey());
	    return relatorioQuantitativoDTO;
	}
	
	private RelatorioQuantitativoDTO buscarNipsSemPagamento(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception {
		Search<RelatorioQuantitativoDTO> relatorioSearch = this.aitMovimentoRepository.findQuantitativoPagamentoNip(setCriteriosSearchNipPaga(relatorioQuantitativoSearch), 
				TipoMovimentoQuantitativoEnum.A_RECEBER_PENALIDADES_MULTAS_PROCESSADAS.getKey());
		if(relatorioSearch.getList(RelatorioQuantitativoDTO.class).isEmpty()) {
			return null;
		}
		RelatorioQuantitativoDTO relatorioQuantitativoDTO = relatorioSearch.getList(RelatorioQuantitativoDTO.class).get(0);
		relatorioQuantitativoDTO.setTpStatus(TipoMovimentoQuantitativoEnum.A_RECEBER_PENALIDADES_MULTAS_PROCESSADAS.getKey());
	    return relatorioQuantitativoDTO;
	}
	
	private RelatorioQuantitativoDTO buscarPagamentos(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception {
		Search<RelatorioQuantitativoDTO> relatorioSearch = this.aitMovimentoRepository.findQuantitativoPagamentosRecebidos(setCriteriosSearchPagamentos(relatorioQuantitativoSearch));
		if(relatorioSearch.getList(RelatorioQuantitativoDTO.class).isEmpty()) {
			return null;
		}
		RelatorioQuantitativoDTO relatorioQuantitativoDTO = relatorioSearch.getList(RelatorioQuantitativoDTO.class).get(0);
		relatorioQuantitativoDTO.setTpStatus(TipoMovimentoQuantitativoEnum.RECEBIMENTOS_REALIZADOS_PERIODO.getKey());
	    return relatorioQuantitativoDTO;
	}
	
	private SearchCriterios setCriteriosSearchQuantitativo(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("A.tp_status", incluirCondicionalMovimentos(), ItemComparator.IN, Types.INTEGER);
		searchCriterios.addCriteriosGreaterDate("A.dt_movimento", relatorioQuantitativoSearch.getDtMovimentoInicial(),  relatorioQuantitativoSearch.getDtMovimentoInicial() != null);
		searchCriterios.addCriteriosMinorDate("A.dt_movimento", relatorioQuantitativoSearch.getDtMovimentoFinal(), relatorioQuantitativoSearch.getDtMovimentoFinal() != null);
		searchCriterios.addCriteriosEqualInteger("A.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		return searchCriterios;
	}
	
	private SearchCriterios setCriteriosSearchNipPaga(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosGreaterDate("A.dt_movimento", relatorioQuantitativoSearch.getDtMovimentoInicial(),  relatorioQuantitativoSearch.getDtMovimentoInicial() != null);
		searchCriterios.addCriteriosMinorDate("A.dt_movimento", relatorioQuantitativoSearch.getDtMovimentoFinal(), relatorioQuantitativoSearch.getDtMovimentoFinal() != null);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.NIP_ENVIADA.getKey(), true);
		searchCriterios.addCriteriosEqualInteger("A.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		return searchCriterios;
	}
	
	private SearchCriterios setCriteriosSearchPagamentos(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosGreaterDate("A.dt_movimento", relatorioQuantitativoSearch.getDtMovimentoInicial(),  relatorioQuantitativoSearch.getDtMovimentoInicial() != null);
		searchCriterios.addCriteriosMinorDate("A.dt_movimento", relatorioQuantitativoSearch.getDtMovimentoFinal(), relatorioQuantitativoSearch.getDtMovimentoFinal() != null);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.MULTA_PAGA.getKey(), true);
		searchCriterios.addCriteriosEqualInteger("A.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		return searchCriterios;
	}
		
	private String incluirCondicionalMovimentos() throws Exception {
		String movimentos = TipoMovimentoQuantitativoEnum.NOTIFICACOES_ATUACOES_PROCESSADAS.getKey() + ", " +
				       TipoMovimentoQuantitativoEnum.NOTIFICACOES_PENALIDADE_PROCESSADAS.getKey() + ", " +
				       TipoMovimentoQuantitativoEnum.NOTIFICACOES_NIC_PROCESSADAS.getKey() + ", " +
				       TipoMovimentoQuantitativoEnum.DEFESAS_PREVIA_AUTUACAO_PROTOCOLOCADA.getKey() + ", " +
				       TipoMovimentoQuantitativoEnum.DEFESAS_ADVERTENCIA_AUTUACAO_PROTOCOLOCADA.getKey() + ", " +
				       TipoMovimentoQuantitativoEnum.JARI_PROTOCOLADOS.getKey() + ", " +
				       TipoMovimentoQuantitativoEnum.CETRAN_PROTOCOLOCADOS.getKey();
		return movimentos;
	}
	
	private Report printRelatorio(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception {
	    PagedResponse<RelatorioQuantitativoDTO> pagedResponse = buscarRelatorioQuantitativo(relatorioQuantitativoSearch);
	    List<RelatorioQuantitativoDTO> relatorioQuantitativoList = pagedResponse.getItems();
	    setarDsTpStatus(relatorioQuantitativoList);
	    ReportCriterios reportCriterios = montarReportCriterios(relatorioQuantitativoSearch);
	    Report report = new ReportBuilder()
	            .list(relatorioQuantitativoList)
	            .reportCriterios(reportCriterios)
	            .build();
	    return report;
	}
	
	private ReportCriterios montarReportCriterios(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
	    List<String> filtros = new FiltrosRelatorioBuilder(relatorioQuantitativoSearch).construirFiltro().build();
	    reportCriterios.addParametros("NM_FILTROS", String.join(" | ", filtros));
		return reportCriterios;
	}
	
	private void setarDsTpStatus(List<RelatorioQuantitativoDTO> relatorioQuantitativoList) {
		for (RelatorioQuantitativoDTO relatorioQuantitativoDTO : relatorioQuantitativoList) {
			relatorioQuantitativoDTO.setDsTpStatus(TipoMovimentoQuantitativoEnum.valueOf(relatorioQuantitativoDTO.getTpStatus()));
		}
	}
}
