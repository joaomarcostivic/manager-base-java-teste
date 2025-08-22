package com.tivic.manager.mob.lote.impressao.notificacao;

import java.util.List;
import java.util.Locale;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.AitReportServicesNip;
import com.tivic.manager.mob.AitReportValidatorsNIP;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public abstract class DadosNotificacaoComJurosBase {
	
	private AitReportServicesNip aitReportServicesNip;
	private CidadeRepository cidadeRepository;
	private EstadoRepository estadoRepository;
	private IOrgaoService orgaoService;
	private IParametroRepository parametroRepository;
	private CustomConnection customConnection;
	
	protected abstract String getReportPath();
	protected abstract Search<DadosNotificacao> searchPenalidade(SearchCriterios searchCriterios) throws Exception;
	
	public DadosNotificacaoComJurosBase() throws Exception {
		this.aitReportServicesNip = (AitReportServicesNip) BeansFactory.get(AitReportServicesNip.class);
		this.cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
		this.estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
		this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}

	public byte[] gerarDocumento(int cdAit, Boolean printPortal, CustomConnection customConnection) throws Exception, ValidacaoException {
		this.customConnection = customConnection;
		ReportCriterios reportCriterios = montarReportCriterios(cdAit, printPortal, customConnection);
		Report documentoNip = findDocumentoNip(cdAit, reportCriterios);
		return documentoNip.getReportPdf(getReportPath());
	}
	
	private Report findDocumentoNip(int cdAit, ReportCriterios reportCriterios) throws Exception {
		SearchCriterios searchCriterios = montarCriterios(cdAit);
	    Search<DadosNotificacao> searchNip = searchPenalidade(searchCriterios);
		montarDadosDocumento(searchNip, cdAit, reportCriterios);
		Report report = new ReportBuilder()
				.search(searchNip)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}

	protected SearchBuilder<DadosNotificacao> searchBuilder(SearchCriterios searchCriterios) throws Exception {
        return new SearchBuilder<DadosNotificacao>("mob_ait A")
        		.fields(  " A.cd_ait, A.id_ait, A.nr_renainf, A.nr_controle, A.dt_infracao, A.dt_prazo_defesa, A.ds_observacao, A.nr_cpf_condutor, A.dt_afericao,"
						+ " A.nr_lacre, A.nr_placa, A.sg_uf_veiculo, A.nr_renavan, A.ds_observacao, A.nm_proprietario, A.nr_cpf_cnpj_proprietario, "
						+ " A.nm_condutor, A.nr_cnh_condutor, A.uf_cnh_condutor, A.ds_local_infracao, A.ds_ponto_referencia, A.vl_velocidade_permitida, "
						+ " A.vl_velocidade_aferida, A.vl_velocidade_penalidade, A.ds_logradouro, A.ds_nr_imovel, A.nr_cep, A.dt_vencimento, "
						+ " A.dt_vencimento as DT_LIMITE_RECURSO, "
						+ " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, B.nr_inciso, B.nr_paragrafo, B.nr_alinea, B.nm_natureza, B.nr_pontuacao, "
						+ " C.nm_cidade, C.nm_cidade AS nm_municipio, "
						+ " C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado, "
						+ " E.nm_categoria,"
						+ " F.nm_cor,"
						+ " G.ds_especie, "
						+ " H.nm_marca, H.nm_modelo, "
						+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento, I.cd_equipamento, "
						+ " J.nm_agente, J.nr_matricula,"
						+ " K.dt_movimento, K.tp_status, K.nr_processo, K.lg_enviado_detran, "
						+ " M.dt_afericao, M.id_identificacao_inmetro, "
						+ " N.ds_logradouro, "
						+ " P.nm_bairro, "
						+ " Q.cd_cidade, Q.nm_cidade AS CIDADE_PROPRIETARIO, "
						+ " R.sg_estado AS uf_proprietario, R.nm_estado AS ESTADO_PROPRIETARIO ")
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
                .orderBy(" B.dt_fim_vigencia DESC");
    }
	
	private void montarDadosDocumento(Search<DadosNotificacao> searchNip, int cdAit, ReportCriterios reportCriterios) throws Exception {
		if (searchNip.getRsm().next()) {
			int cd_equipamento = searchNip.getList(DadosNotificacao.class).get(0).getCdEquipamento();
			searchNip.getRsm().setValueToField("NOME_EQUIPAMENTO", cd_equipamento > 0 ? EquipamentoEnum.valueOf(searchNip.getRsm().getInt("tp_equipamento")) : EquipamentoEnum.NENHUM.getValue());
		}
		searchNip.getRsm().setValueToField("DT_EMISSAO", Util.getDataAtual());
		aitReportServicesNip.calcularJuros(searchNip.getRsm(), cdAit, this.customConnection.getConnection());
		aitReportServicesNip.pegarBancoConveniado(this.customConnection.getConnection(), searchNip.getRsm());
		AitReportValidatorsNIP.trataDefesaPrevia(searchNip.getRsm(), reportCriterios.getParametros(), this.customConnection.getConnection());
		if (reportCriterios.getParametros().get("NR_CD_FEBRABAN") != null) {	
			aitReportServicesNip.gerarCodigoBarras(searchNip.getRsm(), reportCriterios.getParametros(), this.customConnection.getConnection());	 
		}
		ApresentacaoCondutor apresentacaoCondutor = getCondutorFici(cdAit);
		if (apresentacaoCondutor != null) {
			searchNip.getRsm().setValueToField("NM_CONDUTOR", apresentacaoCondutor.getNmCondutor() != null ? apresentacaoCondutor.getNmCondutor() : null);
			searchNip.getRsm().setValueToField("NR_CNH_CONDUTOR", apresentacaoCondutor.getNrCnh() != null ? apresentacaoCondutor.getNrCnh() : null);
			searchNip.getRsm().setValueToField("UF_CNH_CONDUTOR", apresentacaoCondutor.getUfCnh() != null ? apresentacaoCondutor.getUfCnh() : null);
	    }
	}

	private ReportCriterios montarReportCriterios(int cdAit, Boolean printPortal, CustomConnection customConnection) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("LOGO_1", this.parametroRepository.recImageToString("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", customConnection));
		reportCriterios.addParametros("LOGO_2", this.parametroRepository.recImageToString("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", customConnection));
		reportCriterios.addParametros("DS_TITULO_1", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_1", customConnection));
		reportCriterios.addParametros("DS_TITULO_2", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_2", customConnection));
		reportCriterios.addParametros("DS_TITULO_3", this.parametroRepository.getValorOfParametroAsString("DS_TITULO_3", customConnection));
		reportCriterios.addParametros("MOB_IMPRESSOS_DS_TEXTO_NIP", this.parametroRepository.getValorOfParametroAsString("MOB_IMPRESSOS_DS_TEXTO_NIP", customConnection));
		reportCriterios.addParametros("MOB_CD_ORGAO_AUTUADOR", this.parametroRepository.getValorOfParametroAsString("MOB_CD_ORGAO_AUTUADOR", customConnection));
		reportCriterios.addParametros("NM_CIDADE", this.parametroRepository.getValorOfParametroAsString("NM_CIDADE", customConnection));
		reportCriterios.addParametros("NR_BANCO_CREDITO", this.parametroRepository.getValorOfParametroAsString("NR_BANCO_CREDITO", customConnection));
		reportCriterios.addParametros("NR_AGENCIA_CREDITO", this.parametroRepository.getValorOfParametroAsString("NR_AGENCIA_CREDITO", customConnection));
		reportCriterios.addParametros("NR_CONTA_CREDITO", this.parametroRepository.getValorOfParametroAsString("NR_CONTA_CREDITO", customConnection));
		reportCriterios.addParametros("MOB_CORREIOS_NR_CONTRATO", this.parametroRepository.getValorOfParametroAsString("MOB_CORREIOS_NR_CONTRATO", customConnection));
		reportCriterios.addParametros("SG_ORGAO", this.parametroRepository.getValorOfParametroAsString("SG_ORGAO", customConnection));
		reportCriterios.addParametros("SG_DEPARTAMENTO", this.parametroRepository.getValorOfParametroAsString("SG_DEPARTAMENTO", customConnection));
		reportCriterios.addParametros("NM_LOGRADOURO", this.parametroRepository.getValorOfParametroAsString("NM_LOGRADOURO", customConnection));
		reportCriterios.addParametros("NR_ENDERECO", this.parametroRepository.getValorOfParametroAsString("NR_ENDERECO", customConnection));
		reportCriterios.addParametros("NM_BAIRRO", this.parametroRepository.getValorOfParametroAsString("NM_BAIRRO", customConnection));
		reportCriterios.addParametros("NR_CEP", this.parametroRepository.getValorOfParametroAsString("NR_CEP", customConnection).replaceAll("[^\\d]", ""));
		reportCriterios.addParametros("NR_TELEFONE", this.parametroRepository.getValorOfParametroAsString("NR_TELEFONE", customConnection));
		reportCriterios.addParametros("NR_TELEFONE_2", this.parametroRepository.getValorOfParametroAsString("NR_TELEFONE2", customConnection));
		reportCriterios.addParametros("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", this.parametroRepository.getValorOfParametroAsString("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", customConnection));
		reportCriterios.addParametros("MOB_PRAZOS_NR_RECURSO_JARI", this.parametroRepository.getValorOfParametroAsString("MOB_PRAZOS_NR_RECURSO_JARI", customConnection));
		reportCriterios.addParametros("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", this.parametroRepository.getValorOfParametroAsString("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", customConnection));
		reportCriterios.addParametros("NR_CD_FEBRABAN", this.parametroRepository.getValorOfParametroAsString("NR_CD_FEBRABAN", customConnection));
		reportCriterios.addParametros("MOB_PRAZOS_NR_DEFESA_PREVIA", this.parametroRepository.getValorOfParametroAsString("MOB_PRAZOS_NR_DEFESA_PREVIA", customConnection));
		reportCriterios.addParametros("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", this.parametroRepository.recImageToString("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", customConnection));
		reportCriterios.addParametros("mob_local_impressao",this.parametroRepository.getValorOfParametroAsString("mob_local_impressao", customConnection));
		reportCriterios.addParametros("NM_COMPLEMENTO", this.parametroRepository.getValorOfParametroAsString("NM_COMPLEMENTO", customConnection));
		reportCriterios.addParametros("MOB_INFORMACOES_ADICIONAIS_NIP", this.parametroRepository.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_NIP", customConnection));
		reportCriterios.addParametros("NM_EMAIL", this.parametroRepository.getValorOfParametroAsString("NM_EMAIL", customConnection));
		reportCriterios.addParametros("REPORT_LOCALE", new Locale("pt", "BR"));
		reportCriterios.addParametros("SG_UF", getEstado());
		reportCriterios.addParametros("PRINT_PORTAL", printPortal);
		return reportCriterios;
	}
	
	private SearchCriterios montarCriterios(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		return searchCriterios;
	}
	
	private String getEstado() throws Exception {
		Orgao orgao = this.orgaoService.getOrgaoUnico();
		Cidade cidade = this.cidadeRepository.get(orgao.getCdCidade());
		return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
	}
	
	private ApresentacaoCondutor getCondutorFici(int cdAit) throws Exception {
		int codigoFiciAceita = this.parametroRepository.getValorOfParametroAsInt("CD_OCORRENCIA_APRESENTACAO_CONDUTOR_ACEITO");
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
		searchCriterios.addCriteriosEqualInteger("A.cd_ocorrencia", codigoFiciAceita, true);
		Search<ApresentacaoCondutor> searchCondutor = new SearchBuilder<ApresentacaoCondutor>("mob_ait_movimento A")
				.fields("D.nm_condutor, D.nr_cnh, D.uf_cnh").addJoinTable("JOIN mob_ait_movimento_documento B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("JOIN ptc_documento C ON (B.cd_documento = C.cd_documento)")
				.addJoinTable("JOIN ptc_apresentacao_condutor D ON (C.cd_documento = D.cd_documento)")
				.searchCriterios(searchCriterios).build();
		List<ApresentacaoCondutor> apresentacaoCondutorList = searchCondutor.getList(ApresentacaoCondutor.class);
	    if (!apresentacaoCondutorList.isEmpty()) {
	        return apresentacaoCondutorList.get(0);
	    }
	    return null;
	}
	
}
