package com.tivic.manager.mob.concessao.relatorio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.mob.ConcessaoServices;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ResultSetMap;

public class RelatorioConcessaoServices {
	
	public static ResultSetMap find(SearchCriterios searchCriterios) throws Exception {
		Search search= new SearchBuilder("mob_concessao A ")
				.addJoinTable("LEFT OUTER JOIN grl_pessoa B ON (A.cd_concessionario = B.cd_pessoa) ")
				.searchCriterios(searchCriterios)
				.fields("*, B.nm_pessoa as nm_concessionario")
				.orderBy(searchCriterios.getOrderBy())
				.count()
				.build();
		return search.getRsm();
	}	
	
	public static Report findRelatorioConcessao(SearchCriterios searchCriterios, String nmUsuario) throws Exception {
		Search search = new SearchBuilder("mob_concessao A")
				.addJoinTable("LEFT OUTER JOIN grl_pessoa B ON (A.cd_concessionario = B.cd_pessoa) ")
				.searchCriterios(searchCriterios)
				.fields("*, B.nm_pessoa as nm_concessionario")
				.build();
		
		while(search.getRsm().next()) {
			search.getRsm().setValueToField("NM_ST_CONCESSAO",ConcessaoServices.situacaoConcessao[search.getRsm().getInt("ST_CONCESSAO")]);
			search.getRsm().setValueToField("NM_TP_CONCESSAO",ConcessaoServices.tiposConcessao[search.getRsm().getInt("TP_CONCESSAO")]);
		}
		

		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("nmUsuario", nmUsuario);
		
		Report report = new ReportBuilder()
				.search(search)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}
	
    public static Report findQuantitativo(ReportCriterios reportCriterios) throws Exception{
    	getParamsQuantitativo(reportCriterios);
    	
    	List<RelatorioConcessaoQuantitativoDTO> listRelatorioConcessaoQuantitativoDTO = new RelatorioConcessaoQuantitativoBuilder().build();
    	for (RelatorioConcessaoQuantitativoDTO relatorioConcessaoQuantitativoDTO : listRelatorioConcessaoQuantitativoDTO) {
    		if(relatorioConcessaoQuantitativoDTO.getNmTpConcessao().equals("Coletivo Urbano")) {
    			reportCriterios.addParametros("DATASET_COLETIVO_URBANO", relatorioConcessaoQuantitativoDTO.getResultSetMap().getLines().stream().collect(Collectors.toList()));
    		} else if(relatorioConcessaoQuantitativoDTO.getNmTpConcessao().equals("Táxi")) {
    			reportCriterios.addParametros("DATASET_TAXI", relatorioConcessaoQuantitativoDTO.getResultSetMap().getLines().stream().collect(Collectors.toList()));
    		} else if(relatorioConcessaoQuantitativoDTO.getNmTpConcessao().equals("Coletivo Rural")) {
    			reportCriterios.addParametros("DATASET_COLETIVO_RURAL", relatorioConcessaoQuantitativoDTO.getResultSetMap().getLines().stream().collect(Collectors.toList()));
    		} else if(relatorioConcessaoQuantitativoDTO.getNmTpConcessao().equals("Escolar Urbano")) {
    			reportCriterios.addParametros("DATASET_ESCOLAR_URBANO", relatorioConcessaoQuantitativoDTO.getResultSetMap().getLines().stream().collect(Collectors.toList()));
    		}
			
		}
		Report report = new ReportBuilder()
			.reportCriterios(reportCriterios)
			.build();
		return report;
    }
    
    private static void getParamsQuantitativo(ReportCriterios reportCriterios){		
    	reportCriterios.addParametros("DS_TITULO_1", "PREFEITURA MUNICIPAL DE VITÓRIA DA CONQUISTA - BA");
    	reportCriterios.addParametros("DS_TITULO_2", "SECRETARIA DE MOBILIDADE URBANA - SEMOB");
    	reportCriterios.addParametros("DS_TITULO_3", "COORDENAÇÃO DE TRANSPORTE PÚBLICO");
		
		ArrayList<String> subreportNames = new ArrayList<String>();		
		subreportNames.add("mob/relatorio_concessao_quantitativo_coletivo_urbano_subreport");
		subreportNames.add("mob/relatorio_concessao_quantitativo_taxi_subreport");
		subreportNames.add("mob/relatorio_concessao_quantitativo_coletivo_rural_subreport");
		subreportNames.add("mob/relatorio_concessao_quantitativo_escolar_urbano_subreport");
		
		reportCriterios.addParametros("SUBREPORT_NAMES", subreportNames);
	}
    
	
        
}
