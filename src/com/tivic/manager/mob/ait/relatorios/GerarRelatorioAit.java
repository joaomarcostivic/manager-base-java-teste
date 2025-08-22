package com.tivic.manager.mob.ait.relatorios;

import java.sql.Types;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class GerarRelatorioAit implements IGerarRelatorioAit {
	
	public byte[] gerar(SearchCriterios searchCriterios, boolean lgExcetoCanceladas, Integer tpEquipamento) throws Exception {
		return printListagemAits(searchCriterios, lgExcetoCanceladas, tpEquipamento).getReportPdf("mob/relatorio_de_aits");
	}
	
	public byte[] gerarRelatorio(List<RelatorioAitDTO> aitsList, String filterList) throws Exception {
		return imprimirRelatorio(aitsList, filterList).getReportPdf("mob/relatorio_de_aits");
	}
	
	private Report imprimirRelatorio(List<RelatorioAitDTO> aitsList, String filterList) throws Exception {
		ReportCriterios reportCriterios = getReportCriterios(filterList);
		setarDsTpStatus(aitsList);
		Report report = new ReportBuilder()
			.list(aitsList)
			.reportCriterios(reportCriterios)
			.build();
		return report;
	}
	
	private ReportCriterios getReportCriterios(String filterList) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("NM_FILTROS", filterList);
		return reportCriterios;
	}
	
	private Report printListagemAits(SearchCriterios searchCriterios, boolean lgExcetoCanceladas, Integer tpEquipamento) throws Exception {
		ReportCriterios reportCriterios = montarReportCriterios(searchCriterios, lgExcetoCanceladas, tpEquipamento);
		List<RelatorioAitDTO> listDadosAits = buscarListagemAits(searchCriterios, lgExcetoCanceladas, tpEquipamento).getList(RelatorioAitDTO.class);
		setarDsTpStatus(listDadosAits);
		Report report = new ReportBuilder()
				.list(listDadosAits)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}
	
	private void setarDsTpStatus(List<RelatorioAitDTO> listDadosAits) {
		for (RelatorioAitDTO relatorioAitDTO : listDadosAits) {
			relatorioAitDTO.setDsTpStatus(TipoStatusEnum.valueOf(relatorioAitDTO.getTpStatus()));
		}
	}

	private ReportCriterios montarReportCriterios(SearchCriterios searchCriterios,  boolean lgExcetoCanceladas, Integer tpEquipamento) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		List<String> filtros = new BuscarFiltrosRelatorioBuilder(searchCriterios, lgExcetoCanceladas, tpEquipamento).construirFiltro(lgExcetoCanceladas, tpEquipamento).build();
		reportCriterios.addParametros("NM_FILTROS", String.join(", ", filtros));
		return reportCriterios;
	}

	public Search<RelatorioAitDTO> buscarListagemAits(SearchCriterios searchCriterios, boolean lgExcetoCanceladas, Integer tpEquipamento) throws ValidacaoException, Exception {
		ItemComparator contendoMovimento = searchCriterios.getAndRemoveCriterio("B.tp_status");
		Search<RelatorioAitDTO> searchRelatorioAitDTO = contendoMovimento != null && Integer.valueOf(contendoMovimento.getValue()) > TipoStatusEnum.SITUACAO_NAO_DEFINIDA.getKey()
				? searchContendoMovimento(contendoMovimento, searchCriterios, lgExcetoCanceladas, tpEquipamento) :  searchListagemAits(searchCriterios, lgExcetoCanceladas, tpEquipamento);
		if(searchRelatorioAitDTO.getList(RelatorioAitDTO.class).isEmpty()) {
			throw new ValidacaoException ("Não há AIT's para geração de relatório!");
		}
		
		return searchRelatorioAitDTO;
	}
	
	public Search<RelatorioAitDTO> searchListagemAits(SearchCriterios searchCriterios, boolean lgExcetoCanceladas, Integer tpEquipamento) throws ValidacaoException, Exception {
		searchCriterios.getAndRemoveCriterio("B.dt_movimento_inicial");
		searchCriterios.getAndRemoveCriterio("B.dt_movimento_final");
		incluirCondicionalCanceladas(searchCriterios, lgExcetoCanceladas);		
		incluirTpEquipamento(searchCriterios, tpEquipamento);
		
		Search<RelatorioAitDTO> search = new SearchBuilder<RelatorioAitDTO>("mob_ait A")
				.fields("A.cd_ait, B.dt_movimento, A.dt_infracao, A.id_ait, A.nr_placa, D.nr_cod_detran, A.vl_multa, A.cd_usuario, A.nm_proprietario, A.tp_status, B.tp_status, A.dt_digitacao, B.dt_movimento,"
						+ "A.nr_cpf_cnpj_proprietario, E.tp_talao,  F.tp_equipamento, D.tp_competencia, A.cd_agente, C.nm_agente, A.tp_convenio, A.lg_auto_assinado, A.nr_renavan,"
						+ "A.ds_local_infracao, A.ds_ponto_referencia,  A.ds_logradouro, A.nm_condutor, A.nr_cnh_condutor, A.ds_endereco_condutor, A.uf_cnh_condutor, H.nm_pessoa"
						)	
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait AND A.cd_movimento_atual = B.cd_movimento)")
				.addJoinTable("LEFT OUTER JOIN mob_agente C ON (A.cd_agente = C.cd_agente)")
				.addJoinTable("LEFT OUTER JOIN mob_infracao D ON (A.cd_infracao = D.cd_infracao)")
				.addJoinTable("LEFT OUTER JOIN mob_talonario E ON (A.cd_talao = E.cd_talao)")
				.addJoinTable("LEFT OUTER JOIN grl_equipamento F ON (A.cd_equipamento = F.cd_equipamento)")
				.addJoinTable("LEFT OUTER JOIN seg_usuario G ON (G.cd_usuario = A.cd_usuario)")
                .addJoinTable("LEFT OUTER JOIN grl_pessoa H ON (H.cd_pessoa = G.cd_pessoa)")
				.searchCriterios(searchCriterios)
				.orderBy("A.dt_infracao DESC")
				.count()
				.build();
		return search;	
	}
	
	private void incluirTpEquipamento(SearchCriterios searchCriterios, Integer tpEquipamento) throws Exception {
		if (tpEquipamento == EquipamentoEnum.NENHUM.getKey()) {
			searchCriterios.getAndRemoveCriterio("F.tp_equipamento");
			searchCriterios.addCriterios("A.cd_equipamento", tpEquipamento.toString(), ItemComparator.ISNULL, Types.INTEGER);
		}
	}
	
	private void incluirCondicionalCanceladas(SearchCriterios searchCriterios,  boolean lgExcetoCanceladas) throws Exception {
		String value = TipoStatusEnum.CADASTRO_CANCELADO.getKey() + ", " +
					   TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_MULTA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_DEFESA_PREVIA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_DEFESA_DEFERIDA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_DEFESA_INDEFERIDA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_FICI.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_RECURSO_JARI.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_JARI_COM_PROVIMENTO.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_JARI_SEM_ACOLHIMENTO.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_PREVIA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_NIP.getKey();
		if (lgExcetoCanceladas) {
			 searchCriterios.addCriterios("B.tp_status", value, ItemComparator.NOTIN, Types.INTEGER);
		}
	}
	
	public Search<RelatorioAitDTO> searchContendoMovimento(ItemComparator contendoMovimento, SearchCriterios searchCriterios, boolean lgExcetoCanceladas, Integer tpEquipamento) throws ValidacaoException, Exception {
		ItemComparator dtInicialMovimento = searchCriterios.getAndRemoveCriterio("B.dt_movimento_inicial");
		ItemComparator dtFinalMovimento = searchCriterios.getAndRemoveCriterio("B.dt_movimento_final");
		incluirCondicionalCanceladas(searchCriterios, lgExcetoCanceladas);
		incluirTpEquipamento(searchCriterios, tpEquipamento);
		Search<RelatorioAitDTO> search = new SearchBuilder<RelatorioAitDTO>("mob_ait A")
				.fields("A.cd_ait, B.dt_movimento, A.dt_infracao, A.id_ait, A.nr_placa, D.nr_cod_detran, A.vl_multa, A.cd_usuario, A.nm_proprietario, A.tp_status, B.tp_status, A.dt_digitacao, B.dt_movimento,"
						+ "A.nr_cpf_cnpj_proprietario, E.tp_talao,  F.tp_equipamento, D.tp_competencia, A.cd_agente, C.nm_agente, A.tp_convenio, A.lg_auto_assinado, A.nr_renavan,"
						+ "A.ds_local_infracao, A.ds_ponto_referencia,  A.ds_logradouro, A.nm_condutor, A.nr_cnh_condutor, A.ds_endereco_condutor, A.uf_cnh_condutor, H.nm_pessoa"
						)	
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("LEFT OUTER JOIN mob_agente C ON (A.cd_agente = C.cd_agente)")
				.addJoinTable("LEFT OUTER JOIN mob_infracao D ON (A.cd_infracao = D.cd_infracao)")
				.addJoinTable("LEFT OUTER JOIN mob_talonario E ON (A.cd_talao = E.cd_talao)")
				.addJoinTable("LEFT OUTER JOIN grl_equipamento F ON (A.cd_equipamento = F.cd_equipamento)")
				.addJoinTable("LEFT OUTER JOIN seg_usuario G ON (G.cd_usuario = A.cd_usuario)")
                .addJoinTable("LEFT OUTER JOIN grl_pessoa H ON (H.cd_pessoa = G.cd_pessoa)")
				.searchCriterios(searchCriterios)
				.additionalCriterias("EXISTS"
						+ " ("
						+ " 	SELECT G.cd_ait, G.tp_status, G.dt_movimento FROM mob_ait_movimento G"
						+ "		WHERE  ( G.tp_status = " + contendoMovimento.getValue()   
						+ "					AND G.cd_ait = A.cd_ait"
						+ " ))"
						+ "AND B.dt_movimento BETWEEN '" + dtInicialMovimento.getValue() + "'" 
						+ "AND '" + dtFinalMovimento.getValue() + "'"
						+ "AND B.tp_status = " + contendoMovimento.getValue())
				.orderBy("A.dt_infracao DESC")
				.count()
				.build();
		return search;	
	}
}
