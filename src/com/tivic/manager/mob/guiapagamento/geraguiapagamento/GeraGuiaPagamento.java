package com.tivic.manager.mob.guiapagamento.geraguiapagamento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.guiapagamento.DadosGuiaPagamento;
import com.tivic.manager.mob.guiapagamento.DadosNotificacaoBuilder;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.mob.lote.impressao.codigobarras.CodigoBarras;
import com.tivic.manager.mob.lote.impressao.codigobarras.GerarCodigoBarrasFactory;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.files.pdf.IPdfGenerator;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class GeraGuiaPagamento implements IGeraGuiaPagamento {

	private IPdfGenerator pdfGenerator;
	
	public GeraGuiaPagamento() throws Exception {
		pdfGenerator = (IPdfGenerator) BeansFactory.get(IPdfGenerator.class);
	}
	
	@Override
	public byte[] gerarGuiaPagamento(List<Ait> listAit) throws Exception{
		List<byte[]> listBytePdf = new ArrayList<byte[]>();
		for(Ait ait: listAit) {
			byte[] content = this.gerar(ait);
			listBytePdf.add(content);
		}
		return pdfGenerator.generator(listBytePdf);
	}
	
	private byte[] gerar(Ait ait) throws Exception {
		ReportCriterios reportCriterios = montarReportCriterios(ait);
		List<DadosGuiaPagamento> aitList = searchAit(ait).getList(DadosGuiaPagamento.class);
		montarDadosDocumento(aitList, reportCriterios);
		Report report = new ReportBuilder()
				.list(aitList)
				.reportCriterios(reportCriterios)
				.build();
		return report.getReportPdf("mob/guia_pagamento");
	}
	
	private ReportCriterios montarReportCriterios(Ait ait) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("MOB_CD_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("MOB_CD_ORGAO_AUTUADOR"));
		reportCriterios.addParametros("NM_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("MOB_IMPRESSOS_NM_ORGAO_AUTUADOR"));
		reportCriterios.addParametros("NR_CD_FEBRABAN", ParametroServices.getValorOfParametro("NR_CD_FEBRABAN"));
		return reportCriterios;
	}
	
	private Search<DadosGuiaPagamento> searchAit(Ait ait) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.cd_ait", ait.getCdAit(), ait.getCdAit() > 0);
			customConnection.initConnection(false);
			Search<DadosGuiaPagamento> search = searchAit(searchCriterios);
			if(search.getList(DadosGuiaPagamento.class).isEmpty())
				throw new NoContentException("Nenhum registro encontrado!");
			customConnection.finishConnection();
			return search;
		} finally {
			customConnection.closeConnection();
		}
		
	}
	
	private Search<DadosGuiaPagamento> searchAit(SearchCriterios searchCriterios) throws Exception {
			Search<DadosGuiaPagamento> search = new SearchBuilder<DadosGuiaPagamento>("mob_ait A")
					.fields("A.id_ait, A.nr_controle, A.nr_cpf_cnpj_proprietario, A.nm_proprietario, A.ds_local_infracao, A.ds_ponto_referencia," +
							"A.ds_observacao, A.nr_renainf, A.dt_infracao, A.lg_auto_assinado, A.nr_placa, A.sg_uf_veiculo," +
							"A.vl_velocidade_permitida, A.vl_velocidade_aferida, A.vl_velocidade_penalidade, A.nr_renavan, A.nr_cpf_condutor, A.dt_prazo_defesa," + 
							"B.ds_infracao, B.nr_cod_detran, B.vl_infracao AS vl_multa, B.nr_artigo, B.nr_paragrafo," +
							"B.nr_inciso, B.nr_alinea, B.nr_pontuacao, B.nm_natureza," +
							"D.id_lote_impressao," +
							"E.nm_cidade as nm_municipio, E1.sg_estado as nm_uf," +
							"F.nm_agente," +
							"G.nm_cidade, G1.sg_estado," +
							"H.nm_categoria," +
							"I.nm_marca, I.nm_modelo," +
							"J.nm_tipo_veiculo," +
							"K.ds_especie," +
							"L.nm_equipamento, L.nm_marca AS nm_marca_equipamento, L.nm_modelo AS nm_modelo_equipamento, L.dt_afericao, L.nr_inventario_inmetro")
					.addJoinTable("LEFT OUTER JOIN mob_infracao           B  ON (A.cd_infracao = B.cd_infracao)")
					.addJoinTable("LEFT OUTER JOIN mob_lote_impressao_ait C  ON (A.cd_ait = C.cd_ait)")
					.addJoinTable("LEFT OUTER JOIN mob_lote_impressao     D  ON (C.cd_lote_impressao = D.cd_lote_impressao)")
					.addJoinTable("LEFT OUTER JOIN grl_cidade             E  ON (A.cd_cidade = E.cd_cidade)")
					.addJoinTable("LEFT OUTER JOIN grl_estado             E1 ON (E.cd_estado = E1.cd_estado)")
					.addJoinTable("LEFT OUTER JOIN mob_agente             F  ON (A.cd_agente = F.cd_agente)")
					.addJoinTable("LEFT OUTER JOIN grl_cidade             G  ON (A.cd_cidade_proprietario = G.cd_cidade)")
					.addJoinTable("LEFT OUTER JOIN grl_estado             G1 ON (G.cd_estado = G1.cd_estado)")
					.addJoinTable("LEFT OUTER JOIN fta_categoria_veiculo  H  ON (A.cd_categoria_veiculo = H.cd_categoria)")
					.addJoinTable("LEFT OUTER JOIN fta_marca_modelo       I  ON (A.cd_marca_veiculo = I.cd_marca)")
					.addJoinTable("LEFT OUTER JOIN fta_tipo_veiculo       J  ON (A.cd_tipo_veiculo = J.cd_tipo_veiculo)")
					.addJoinTable("LEFT OUTER JOIN fta_especie_veiculo    K  ON (A.cd_especie_veiculo = K.cd_especie)")
					.addJoinTable("LEFT OUTER JOIN grl_equipamento        L  ON (A.cd_equipamento = L.cd_equipamento)")
					.searchCriterios(searchCriterios)
					.build();
			
			return search;
	}
	
	private void montarDadosDocumento(List<DadosGuiaPagamento> aitList, ReportCriterios reportCriterios) throws Exception {
		for(DadosGuiaPagamento dadosGuiaPagamento: aitList) {
			dadosGuiaPagamento.setDtEmissao(Util.getDataAtual());
			dadosGuiaPagamento.setDtVencimento(setDtVencimento());
			dadosGuiaPagamento.setVlMultaComDesconto((dadosGuiaPagamento.getVlMulta() - (Double.valueOf(20) * dadosGuiaPagamento.getVlMulta()) / 100));
			DadosNotificacao dadosNotificacao = new DadosNotificacaoBuilder(dadosGuiaPagamento).build();
			if (reportCriterios.getParametros().get("NR_CD_FEBRABAN") != null) {
				CodigoBarras codigoBarras = new GerarCodigoBarrasFactory()
						.getStrategy()
						.gerarCodigoBarras(dadosNotificacao, reportCriterios);
				dadosGuiaPagamento.setLinhaDigitavel(codigoBarras.getLinhaDigitavel());
				reportCriterios.addParametros("BARRAS", codigoBarras.getBarcodeImage());
			}
		}
	}
	
	private GregorianCalendar setDtVencimento() {
		GregorianCalendar dtVencimento = new GregorianCalendar();
		dtVencimento.add(Calendar.DAY_OF_MONTH, 1);
		
		if (dtVencimento.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			dtVencimento.add(Calendar.DAY_OF_MONTH, 2);
        }
		
        if (dtVencimento.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        	dtVencimento.add(Calendar.DAY_OF_MONTH, 1);
        }
		return dtVencimento;
	}
}
