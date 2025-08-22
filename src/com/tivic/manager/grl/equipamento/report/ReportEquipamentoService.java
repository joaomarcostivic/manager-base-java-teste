package com.tivic.manager.grl.equipamento.report;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.equipamento.report.dto.ReportEquipamentoDTO;
import com.tivic.manager.grl.equipamento.report.dto.ReportEquipamentoDTOListBuilder;
import com.tivic.sol.report.IReportService;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ReportEquipamentoService implements IReportService<ReportEquipamentoDTO> {

	@Override
	public List<ReportEquipamentoDTO> find(SearchCriterios searchCriterios) throws Exception {
		Search<ReportEquipamentoDTO> search= new SearchBuilder<ReportEquipamentoDTO>("grl_equipamento A")
				.searchCriterios(searchCriterios)
				.orderBy(searchCriterios.getOrderBy())
				.build();
		
		if(search.getRsm().size() == 0)
			throw new NoContentException("Nenhum equipamento encontrado");
			
		return new ReportEquipamentoDTOListBuilder(search).build();
	}	

	@Override
	public Report buildReport(SearchCriterios searchCriterios) throws Exception {
		Search search= new SearchBuilder("grl_equipamento A")
				.searchCriterios(searchCriterios)
				.orderBy(searchCriterios.getOrderBy())
				.build();
		if(search.getRsm().size() == 0)
			throw new NoContentException("Nenhum equipamento encontrado");
		ReportCriterios reportCriterios = new ReportCriterios();
		Report report = new ReportBuilder()
				.search(search)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}
	
}
