package com.tivic.manager.mob.tabelashorarios.relatorio;

import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ResultSetMap;

public class RelatorioHorarioServices {
	public static ResultSetMap find(SearchCriterios searchCriterios) throws Exception {
		Search search = new SearchBuilder("mob_horario_afericao A")
				.addJoinTable(" LEFT OUTER JOIN mob_concessao_veiculo 	B ON (A.cd_concessao_veiculo = B.cd_concessao_veiculo) ")
				.addJoinTable(" LEFT OUTER JOIN mob_linha 				E ON (A.cd_linha = E.cd_linha) ")
				.addJoinTable(" LEFT OUTER JOIN mob_linha_rota 			F ON (A.cd_linha = F.cd_linha AND A.cd_rota = F.cd_rota) ")
				.addJoinTable(" LEFT OUTER JOIN mob_agente 				C ON (A.cd_agente = C.cd_agente) ")
				.addJoinTable(" LEFT OUTER JOIN mob_concessao 			D ON (B.cd_concessao = D.cd_concessao) ")
				.addJoinTable(" LEFT OUTER JOIN grl_pessoa 				G ON (D.cd_concessionario = G.cd_pessoa)")
				.addJoinTable(" LEFT OUTER JOIN grl_pessoa_empresa  	H ON( G.cd_pessoa = H.cd_pessoa)")
				.addJoinTable(" LEFT OUTER JOIN grl_empresa 			I ON (H.cd_empresa = I.cd_empresa)")
				.searchCriterios(searchCriterios)
				.fields(" A.dt_lancamento, A.hr_chegada, A.hr_partida, A.hr_previsto, A.st_horario_afericao, B.nr_prefixo, C.nm_agente, G.cd_pessoa, G.nm_pessoa as nm_concessionario, E.nr_linha, F.nm_rota, D.cd_concessionario, I.id_empresa")
				.orderBy(searchCriterios.getOrderBy())
				.count()
				.build();

		return search.getRsm();
	}

	public static Report findRelatorio(SearchCriterios searchCriterios) throws Exception {
		
		Search search = new SearchBuilder("mob_horario_afericao A")
				.addJoinTable(" LEFT OUTER JOIN mob_concessao_veiculo 	B ON (A.cd_concessao_veiculo = B.cd_concessao_veiculo) ")
				.addJoinTable(" LEFT OUTER JOIN mob_linha 				E ON (A.cd_linha = E.cd_linha) ")
				.addJoinTable(" LEFT OUTER JOIN mob_linha_rota 			F ON (A.cd_linha = F.cd_linha AND A.cd_rota = F.cd_rota) ")
				.addJoinTable(" LEFT OUTER JOIN mob_agente 				C ON (A.cd_agente = C.cd_agente) ")
				.addJoinTable(" LEFT OUTER JOIN mob_concessao 			D ON (B.cd_concessao = D.cd_concessao) ")
				.addJoinTable(" LEFT OUTER JOIN grl_pessoa 				G ON (D.cd_concessionario = G.cd_pessoa)")
				.addJoinTable(" LEFT OUTER JOIN grl_pessoa_empresa  	H ON( G.cd_pessoa = H.cd_pessoa)")
				.addJoinTable(" LEFT OUTER JOIN grl_empresa 			I ON (H.cd_empresa = I.cd_empresa)")
				.searchCriterios(searchCriterios)
				.fields(" A.dt_lancamento, A.hr_chegada, A.hr_partida, A.hr_previsto, A.st_horario_afericao, B.nr_prefixo, C.nm_agente, G.cd_pessoa, G.nm_pessoa as nm_concessionario, E.nr_linha, F.nm_rota, D.cd_concessionario, I.id_empresa")
				.build();
		
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", "PREFEITURA MUNICIPAL DE VITÓRIA DA CONQUISTA - BA");
		reportCriterios.addParametros("DS_TITULO_2", "SECRETARIA DE MOBILIDADE URBANA - SEMOB");
		reportCriterios.addParametros("DS_TITULO_3", "COORDENAÇÃO DE TRANSPORTE PÚBLICO");

		Report report = new ReportBuilder()
				.search(search)
				.reportCriterios(reportCriterios)
				.build();

		return report;
		
	}
}
