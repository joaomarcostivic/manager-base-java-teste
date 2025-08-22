package com.tivic.manager.mob.lote.impressao;

import java.util.List;
import java.util.Locale;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.colaborador.IColaboradorService;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class GerarDocumentoSegundaViaAdvertencia implements IGerarSegundaViaImpressao {
	private CustomConnection customConnection;
	private ILoteNotificacaoService loteNotificacaoService;
	private IAitMovimentoService aitMovimentoServices;
	private IColaboradorService colaboradorService;
	
	public GerarDocumentoSegundaViaAdvertencia() throws Exception {
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		colaboradorService = (IColaboradorService) BeansFactory.get(IColaboradorService.class);
	}

	@Override
	public byte[] gerarDocumentos(int cdAit, Boolean printPortal, CustomConnection customConnection) throws Exception, ValidacaoException {
		this.customConnection = customConnection;
		ReportCriterios reportCriterios = montarReportCriterios();
		return findAdvertencia(cdAit, reportCriterios).getReportPdf("mob/notificacao_advertencia");
	}
	
	private ReportCriterios montarReportCriterios() throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", customConnection.getConnection()));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", customConnection.getConnection()));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", customConnection.getConnection()));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_IMPRESSOS_DS_TEXTO_NOTIFICACAO_ADVERTENCIA", ParametroServices.getValorOfParametro("MOB_IMPRESSOS_DS_TEXTO_NOTIFICACAO_ADVERTENCIA", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_CD_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("MOB_CD_ORGAO_AUTUADOR", customConnection.getConnection()));
		reportCriterios.addParametros("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE", "LOREM IPSUM DOLOR SIT AMET", customConnection.getConnection()));
		reportCriterios.addParametros("NR_BANCO_CREDITO", ParametroServices.getValorOfParametro("NR_BANCO_CREDITO", customConnection.getConnection()));
		reportCriterios.addParametros("NR_AGENCIA_CREDITO", ParametroServices.getValorOfParametro("NR_AGENCIA_CREDITO", customConnection.getConnection()));
		reportCriterios.addParametros("NR_CONTA_CREDITO", ParametroServices.getValorOfParametro("NR_CONTA_CREDITO", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CONTRATO", customConnection.getConnection()));
		reportCriterios.addParametros("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO", customConnection.getConnection()));
		reportCriterios.addParametros("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO", customConnection.getConnection()));
		reportCriterios.addParametros("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO", customConnection.getConnection()));
		reportCriterios.addParametros("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", customConnection.getConnection()).replaceAll("[^\\d]", ""));
		reportCriterios.addParametros("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", customConnection.getConnection()));
		reportCriterios.addParametros("NR_TELEFONE_2", ParametroServices.getValorOfParametro("NR_TELEFONE2", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", ParametroServices.getValorOfParametro("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_PRAZOS_NR_RECURSO_JARI", ParametroServices.getValorOfParametro("MOB_PRAZOS_NR_RECURSO_JARI", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", ParametroServices.getValorOfParametro("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_PRAZOS_NR_DEFESA_PREVIA", ParametroServices.getValorOfParametro("MOB_PRAZOS_NR_DEFESA_PREVIA", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", customConnection.getConnection()));
		reportCriterios.addParametros("mob_local_impressao", ParametroServices.getValorOfParametro("mob_local_impressao", 0, customConnection.getConnection()));
		reportCriterios.addParametros("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("REPORT_LOCALE", new Locale("pt", "BR"));
		reportCriterios.addParametros("SG_UF", loteNotificacaoService.getEstadoOrgaoAutuador());
		reportCriterios.addParametros("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_CORREIOS_SG_CLIENTE", ParametroServices.getValorOfParametro("MOB_CORREIOS_SG_CLIENTE", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_INFORMACOES_ADICIONAIS_NIP", ParametroServices.getValorOfParametro("MOB_INFORMACOES_ADICIONAIS_NIP", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_TEXTO_LEI_NP_ADVERTENCIA", ParametroServices.getValorOfParametro("MOB_TEXTO_LEI_NP_ADVERTENCIA", customConnection.getConnection()));
		return reportCriterios;
	}
	
	private Report findAdvertencia(int cdAit, ReportCriterios reportCriterios) throws Exception {
		SearchCriterios searchCriterios = montarCriterios(cdAit);
		List<DadosNotificacao> listDadosNotificacao = searchNipAdvertencia(searchCriterios).getList(DadosNotificacao.class);
		setDadosDocumento(listDadosNotificacao, reportCriterios);
		Report report = new ReportBuilder()
				.list(listDadosNotificacao)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}

	private SearchCriterios montarCriterios(int cdAit) {
		SearchCriterios searchCriterios= new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		return searchCriterios;
	}
	
	private Search<DadosNotificacao> searchNipAdvertencia(SearchCriterios searchCriterios) throws Exception {
		Search<DadosNotificacao> search = new SearchBuilder<DadosNotificacao>("MOB_AIT A")
				.fields(  " A.cd_Ait, A.id_ait, A.nr_renainf, A.nr_controle, A.dt_infracao, A.dt_prazo_defesa, A.ds_observacao, A.nr_cpf_condutor, A.dt_afericao,"
						+ " A.nr_lacre, A.nr_placa, A.sg_uf_veiculo, A.nr_renavan, A.ds_observacao, A.nm_proprietario, A.nr_cpf_cnpj_proprietario, "
						+ " A.nm_condutor, A.nr_cnh_condutor, A.uf_cnh_condutor, A.ds_local_infracao, A.ds_ponto_referencia,  B.vl_infracao AS vl_multa, A.vl_velocidade_permitida, "
						+ " A.vl_velocidade_aferida, A.vl_velocidade_penalidade, A.ds_logradouro, A.ds_nr_imovel, A.nr_cep, A.dt_vencimento, "
						+ " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, nr_inciso, nr_paragrafo, nr_alinea, nm_natureza, nr_pontuacao,"
						+ " C.nm_cidade, C.nm_cidade AS nm_municipio, "
						+ " C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado,"
						+ " E.nm_categoria,"
						+ " F.nm_cor,"
						+ " G.ds_especie,"
						+ " H.nm_marca, H.nm_modelo,"
						+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento,"
						+ " J.nm_agente, J.nr_matricula,"
						+ " K.dt_movimento, K.tp_status, K.nr_processo, K.lg_enviado_detran, "
						+ " M.id_identificacao_inmetro,"
						+ " N.ds_logradouro,"
						+ " P.nm_bairro, "
						+ " Q.cd_cidade, Q.nm_cidade AS cidade_proprietario, "
						+ " R.sg_estado AS UF_PROPRIETARIO, R.nm_estado AS ESTADO_PROPRIETARIO ")
				.addJoinTable(" LEFT OUTER JOIN mob_infracao 		   B  ON (A.cd_infracao = B.cd_infracao)")
				.addJoinTable(" LEFT OUTER JOIN grl_cidade 			   C  ON (A.cd_cidade = C.cd_cidade)")
				.addJoinTable(" LEFT OUTER JOIN grl_estado 			   C1 ON (C.cd_estado = C1.cd_estado)")
				.addJoinTable(" LEFT OUTER JOIN fta_categoria_veiculo  E  ON (A.cd_categoria_veiculo = E.cd_categoria)")
				.addJoinTable(" LEFT OUTER JOIN fta_cor 			   F  ON (A.cd_cor_veiculo = F.cd_cor)")
				.addJoinTable(" LEFT OUTER JOIN fta_especie_veiculo    G  ON (A.cd_especie_veiculo = G.cd_especie)")
				.addJoinTable(" LEFT OUTER JOIN fta_marca_modelo	   H  ON (A.cd_marca_veiculo = H.cd_marca)")
				.addJoinTable(" LEFT OUTER JOIN grl_equipamento		   I  ON (A.cd_equipamento = I.cd_equipamento)")
				.addJoinTable(" LEFT OUTER JOIN mob_agente			   J  ON (A.cd_agente = J.cd_agente)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_movimento	   K  ON (A.cd_ait = K.cd_ait AND A.cd_movimento_atual = K.cd_movimento)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_evento         L  ON (A.cd_ait = L.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN mob_evento_equipamento M  ON (L.cd_evento = M.cd_evento)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_proprietario   N  ON (A.cd_ait = N.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN grl_bairro             P  ON (A.cd_bairro = P.cd_bairro)")
				.addJoinTable(" LEFT OUTER JOIN grl_cidade             Q  ON (Q.cd_cidade = A.cd_cidade_proprietario)")
				.addJoinTable(" LEFT OUTER JOIN grl_estado             R  ON (R.cd_estado = Q.cd_estado)")
				.searchCriterios(searchCriterios)
				.orderBy(" B.dt_fim_vigencia desc")
				.customConnection(customConnection)
				.build();
		return search;
	}
	
	private void setDadosDocumento(List<DadosNotificacao> listDadosNotificacao, ReportCriterios reportCriterios) throws Exception {
		for (DadosNotificacao dadosNotificacao : listDadosNotificacao) {
			dadosNotificacao.setNomeEquipamento(EquipamentoEnum.valueOf(dadosNotificacao.getTpEquipamento()));
			setDefesaPrevia(dadosNotificacao);
		}
	}
	
	private void setDefesaPrevia(DadosNotificacao dadosNotificacao) throws Exception {
		AitMovimento movimentoDefesaIndeferida = verificarDefesaIndeferida(dadosNotificacao.getCdAit());
		if (movimentoDefesaIndeferida.getNrProcesso() != null) {
			String defesaPreviaIndeferida = ParametroServices.getValorOfParametroAsString("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", "");
			String coodernador = colaboradorService.buscaNomeAutoridadeTransito();
			String cidade = ParametroServices.getValorOfParametroAsString("NM_CIDADE", "");
			String proprietario =  dadosNotificacao.getNmProprietario() != null ? dadosNotificacao.getNmProprietario() : " ";
			String nrAit = dadosNotificacao.getIdAit() != null ? dadosNotificacao.getIdAit() : " ";
			String nrRenavan = dadosNotificacao.getNrRenavan() != null ? dadosNotificacao.getNrRenavan() : " ";
			String nrPlaca = dadosNotificacao.getNrPlaca() != null ? dadosNotificacao.getNrPlaca() : " ";
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#PROPRIETARIO>", proprietario);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr PROCESSO>", movimentoDefesaIndeferida.getNrProcesso());
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#COORDENADOR>", coodernador);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr AIT>", nrAit);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr RENAVAN>", nrRenavan);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr PLACA>", nrPlaca);
			defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#CIDADE>", cidade);
			dadosNotificacao.setDefesaPreviaIndeferida(defesaPreviaIndeferida);
		}
	}
	
	private AitMovimento verificarDefesaIndeferida(int cdAit) throws Exception {
		List<AitMovimento> listDefesas = aitMovimentoServices.getAllDefesas(cdAit);
		for(AitMovimento movimentoDefesaIndeferida : listDefesas) {
			if(movimentoDefesaIndeferida.getTpStatus() == TipoStatusEnum.DEFESA_INDEFERIDA.getKey() ||
				movimentoDefesaIndeferida.getTpStatus() == TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey()) {
				return movimentoDefesaIndeferida;
			}
		}
		return new AitMovimento();
	}
}
