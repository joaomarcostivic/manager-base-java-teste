package com.tivic.manager.mob.ait.relatorios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.tivic.manager.mob.AitReportServices;
import com.tivic.manager.mob.ait.RelatorioAitDTOListBuilder;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.files.pdf.IPdfGenerator;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class RelatorioAitCompetenciaEstadual implements IRelatorioAitCompetenciaEstadual {
	
	private IPdfGenerator pdfGenerator;
	
	public RelatorioAitCompetenciaEstadual() throws Exception {
		pdfGenerator = (IPdfGenerator) BeansFactory.get(IPdfGenerator.class);
	}
	
	public PagedResponse<RelatorioAitDTO> filtrarListagemAits(RelatorioAitCompetenciaEstadualSearch relatorioAitCompetenciaEstadualSearch) throws ValidacaoException, Exception {
		SearchCriterios searchCriteriosCopy = new SearchCriterios();
		searchCriteriosCopy.getCriterios().addAll(setCriteriosSearch(relatorioAitCompetenciaEstadualSearch).getCriterios());
		Search<RelatorioAitDTO> searchRelatorioAitDTO = searchListagemAits(setCriteriosSearch(relatorioAitCompetenciaEstadualSearch));
		if (searchRelatorioAitDTO.getList(RelatorioAitDTO.class) == null || searchRelatorioAitDTO.getList(RelatorioAitDTO.class).isEmpty()) {
			throw new ValidacaoException("Nenhum AIT encontrado.");
		}
		double totalValor = pegarTotalMultas(searchCriteriosCopy);
		return new RelatorioAitDTOListBuilder(searchRelatorioAitDTO.getList(RelatorioAitDTO.class), searchRelatorioAitDTO.getRsm().getTotal(), totalValor).build();
	}
	
	public Search<RelatorioAitDTO> searchListagemAits(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		String incluirContemMovimento = incluirContemMovimento(searchCriterios);
		Search<RelatorioAitDTO> search = new SearchBuilder<RelatorioAitDTO>("mob_ait A")
				.fields("DISTINCT(A.cd_ait), B.dt_movimento, A.dt_infracao, A.id_ait, A.nr_placa, D.nr_cod_detran, A.vl_multa, A.nm_proprietario, A.tp_status, A.dt_digitacao, A.ds_local_infracao, B.cd_ocorrencia,"
					+ " A.nr_cpf_cnpj_proprietario, E.tp_talao, F.tp_equipamento, D.tp_competencia, A.cd_agente, C.nm_agente, A.cd_usuario, A.tp_convenio, A.lg_auto_assinado, A.nr_renavan"
				)	
			.addJoinTable("LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait AND A.cd_movimento_atual = B.cd_movimento)")
			.addJoinTable("LEFT OUTER JOIN mob_agente C ON (A.cd_agente = C.cd_agente)")
			.addJoinTable("LEFT OUTER JOIN mob_infracao D ON (A.cd_infracao = D.cd_infracao)")
			.addJoinTable("LEFT OUTER JOIN mob_talonario E ON (A.cd_talao = E.cd_talao)")
			.addJoinTable("LEFT OUTER JOIN grl_equipamento F ON (A.cd_equipamento = F.cd_equipamento)")
			.addJoinTable("LEFT OUTER JOIN mob_via G ON (F.ds_local = G.nm_via)")
			.addJoinTable("LEFT OUTER JOIN mob_faixa H ON (G.cd_via = H.cd_via)")
			.searchCriterios(searchCriterios)
			.additionalCriterias(incluirContemMovimento)
			.orderBy("A.dt_infracao DESC")
			.count()
		.build();
		return search;	
	}
	
	private Double pegarTotalMultas(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		Search<RelatorioAitDTO> search = new SearchBuilder<RelatorioAitDTO>("mob_ait A")
				.fields("SUM(A.vl_multa) AS total_multas")	
			.addJoinTable("LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait AND A.cd_movimento_atual = B.cd_movimento)")
			.addJoinTable("LEFT OUTER JOIN mob_agente C ON (A.cd_agente = C.cd_agente)")
			.addJoinTable("LEFT OUTER JOIN mob_infracao D ON (A.cd_infracao = D.cd_infracao)")
			.addJoinTable("LEFT OUTER JOIN mob_talonario E ON (A.cd_talao = E.cd_talao)")
			.addJoinTable("LEFT OUTER JOIN grl_equipamento F ON (A.cd_equipamento = F.cd_equipamento)")
			.addJoinTable("LEFT OUTER JOIN mob_via G ON (F.ds_local = G.nm_via)")
			.addJoinTable("LEFT OUTER JOIN mob_faixa H ON (G.cd_via = H.cd_via)")
			.searchCriterios(searchCriterios)
			.additionalCriterias(incluirContemMovimento(searchCriterios))
		.build();
		if(search.getList(RelatorioAitDTO.class).isEmpty() || search.getList(RelatorioAitDTO.class).get(0).getTotalMultas() == null)
			return 0.0;
		return search.getList(RelatorioAitDTO.class).get(0).getTotalMultas();	
	}
	
	private String incluirContemMovimento(SearchCriterios searchCriterios) throws Exception {
		ItemComparator itemComparatorCtMovimento = searchCriterios.getAndRemoveCriterio("ctMovimento");
		if(itemComparatorCtMovimento != null) {
			Integer ctMovimento = Integer.parseInt(itemComparatorCtMovimento.getValue());
		
			if (ctMovimento != null) {
				return "EXISTS (SELECT * FROM mob_ait_movimento B WHERE A.cd_ait = B.cd_ait AND B.tp_status = "+ctMovimento+")";
			}
		}
		return null;
	}
	
	private SearchCriterios setCriteriosSearch(RelatorioAitCompetenciaEstadualSearch relatorioAitCompetenciaEstadualSearch) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("ctMovimento", relatorioAitCompetenciaEstadualSearch.getCtMovimento(),  relatorioAitCompetenciaEstadualSearch.getCtMovimento() > -1);
		searchCriterios.addCriteriosGreaterDate("A.dt_infracao", relatorioAitCompetenciaEstadualSearch.getDtInicialInfracao(), relatorioAitCompetenciaEstadualSearch.getDtInicialInfracao() != null);
		searchCriterios.addCriteriosMinorDate("A.dt_infracao", relatorioAitCompetenciaEstadualSearch.getDtFinalInfracao(), relatorioAitCompetenciaEstadualSearch.getDtFinalInfracao() != null);
		searchCriterios.addCriteriosEqualInteger("D.tp_competencia", relatorioAitCompetenciaEstadualSearch.getTpCompetencia(), relatorioAitCompetenciaEstadualSearch.getTpCompetencia() > -1);
		return searchCriterios;
	}
	
	public byte[] gerarMovimentacoesAit(int[] cdAits) throws Exception {
		List<byte[]> listBytePdf = new ArrayList<byte[]>();
		for (int cdAit: cdAits) {
			byte[] content = AitReportServices.getImpressaoAit(cdAit);
			listBytePdf.add(content);
		} 
		return pdfGenerator.generator(listBytePdf);
	}
	
	public byte[] gerarSegudaVia(int[] cdAits) throws Exception {
		List<byte[]> listBytePdf = new ArrayList<byte[]>();
		for (int cdAit: cdAits) {
			byte[] content = AitReportServices.getSegundaViaAIT(cdAit);
			listBytePdf.add(content);
		} 
		return pdfGenerator.generator(listBytePdf);
	}
}
