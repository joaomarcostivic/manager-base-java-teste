package com.tivic.manager.mob.lote.impressao;

import java.awt.Image;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.grl.parametro.IParametroService;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitReportServices;
import com.tivic.manager.mob.AitReportValidatorsNAI;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.aitimagem.IAitImagemService;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.infracao.TipoResponsabilidadeInfracaoEnum;
import com.tivic.manager.mob.lote.impressao.builders.GeradorDtPostagem;
import com.tivic.manager.mob.lote.impressao.remessacorreios.factories.TipoEquipamentoFactory;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.manager.mob.orgao.IOrgaoService;
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

public class GerarDocumentoSegundaViaNAI implements IGerarSegundaViaImpressao {
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private CustomConnection customConnection;	
	private CidadeRepository cidadeRepository;
	private EstadoRepository estadoRepository;
	private IAitImagemService aitImagemService;
	private IOrgaoService orgaoService;
	private Image imgMulta;
	private AitImagem aitImagem;
	private IAitMovimentoService aitMovimentoServices;
	private int nrCodDetran = 0;
	private int tpEquipamento = 0;
	private IParametroService parametroService;
	private ILoteImpressaoService loteImpressaoService;
	
	public GerarDocumentoSegundaViaNAI()throws Exception{
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		this.cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
		this.estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
		this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
		this.aitImagemService = (IAitImagemService) BeansFactory.get(IAitImagemService.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.parametroService = (IParametroService) BeansFactory.get(IParametroService.class);
		this.loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
	}
	
	@Override
	public byte[] gerarDocumentos(int cdAit, Boolean printPortal, CustomConnection connection) throws Exception {
		this.customConnection = connection;
		ReportCriterios reportCriterios = montarReportCriterios(cdAit, printPortal);
		return findDocumentoNai(cdAit, reportCriterios).getReportPdf(selecionaNomeDocumento());
	}
	
	private String selecionaNomeDocumento() throws Exception {
		String nmDocumentoSelecionado =  new TipoEquipamentoFactory().getStrategy(this.tpEquipamento).getNomeRelatorio(this.nrCodDetran, "mob/nai_v2");
		return nmDocumentoSelecionado;
	}
	
	private ReportCriterios montarReportCriterios(int cdAit, Boolean printPortal) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", customConnection.getConnection()));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", customConnection.getConnection()));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", customConnection.getConnection()));
		reportCriterios.addParametros("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", customConnection.getConnection()));
		reportCriterios.addParametros("NR_TELEFONE_2", ParametroServices.getValorOfParametro("NR_TELEFONE2", customConnection.getConnection()));
		reportCriterios.addParametros("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL", customConnection.getConnection()));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", customConnection.getConnection()));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_CD_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("MOB_CD_ORGAO_AUTUADOR", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_IMPRESSOS_DS_TEXTO_NAI", ParametroServices.getValorOfParametro("MOB_IMPRESSOS_DS_TEXTO_NAI", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", ParametroServices.getValorOfParametro("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", customConnection.getConnection()));
		reportCriterios.addParametros("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO", customConnection.getConnection()));
		reportCriterios.addParametros("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO", customConnection.getConnection()));
		reportCriterios.addParametros("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", "", customConnection.getConnection()).replaceAll("[^\\d]", ""));
		reportCriterios.addParametros("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CONTRATO", customConnection.getConnection()));
		reportCriterios.addParametros("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO", customConnection.getConnection()));
		reportCriterios.addParametros("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_PRAZOS_NR_DEFESA_PREVIA", ParametroServices.getValorOfParametro("MOB_PRAZOS_NR_DEFESA_PREVIA", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", ParametroServices.getValorOfParametro("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", customConnection.getConnection()));
		reportCriterios.addParametros("DS_ENDERECO", ParametroServices.getValorOfParametro("DS_ENDERECO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_SUBORGAO", ParametroServices.getValorOfParametro("NM_SUBORGAO", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", customConnection.getConnection()));
		reportCriterios.addParametros("mob_local_impressao", ParametroServices.getValorOfParametro("mob_local_impressao", 0, customConnection.getConnection()));
		reportCriterios.addParametros("mob_impressao_tp_modelo_nai", ParametroServices.getValorOfParametro("mob_impressao_tp_modelo_nai", customConnection.getConnection()));
		reportCriterios.addParametros("mob_impressao_tp_modelo_nip", ParametroServices.getValorOfParametro("mob_impressao_tp_modelo_nip", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_APRESENTACAO_CONDUTOR", ParametroServices.getValorOfParametro("MOB_APRESENTACAO_CONDUTOR", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_PUBLICAR_AITS_NAO_ENTREGUES", ParametroServices.getValorOfParametro("MOB_PUBLICAR_AITS_NAO_ENTREGUES", customConnection.getConnection()));
		reportCriterios.addParametros("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("mob_apresentar_observacao", ParametroServices.getValorOfParametro("mob_apresentar_observacao", customConnection.getConnection()));
		reportCriterios.addParametros("REPORT_LOCALE", new Locale("pt", "BR"));
		reportCriterios.addParametros("DT_POSTAGEM", getDtPostagem(cdAit));
		reportCriterios.addParametros("SG_UF", getEstado());
		reportCriterios.addParametros("PRINT_PORTAL", printPortal ? printPortal : false);
		return reportCriterios;
	}
	
	private Report findDocumentoNai(int cdAit, ReportCriterios reportCriterios) throws Exception {
		SearchCriterios searchCriterios = montarCriterios(cdAit);
		List<DadosNotificacao> listDadosNotificacao = searchNais(searchCriterios).getList(DadosNotificacao.class);
		montarDadosDocumento(listDadosNotificacao, reportCriterios);
		Report report = new ReportBuilder()
				.list(listDadosNotificacao)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}
	
	private SearchCriterios montarCriterios(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		return searchCriterios;
	}
	
	private Search<DadosNotificacao> searchNais(SearchCriterios searchCriterios) throws Exception {
		Search<DadosNotificacao> search = new SearchBuilder<DadosNotificacao>("MOB_AIT A")
				.fields(  " A.cd_Ait, A.id_ait, A.nr_renainf, A.nr_controle, A.dt_infracao, A.dt_prazo_defesa, A.ds_observacao, A.nr_cpf_condutor, A.dt_afericao, "
						+ " A.nr_lacre, A.nr_placa, A.sg_uf_veiculo, A.nr_renavan, A.ds_observacao, A.nm_proprietario, A.nr_cpf_cnpj_proprietario, "
						+ " A.nm_condutor, A.nr_cnh_condutor, A.uf_cnh_condutor, A.ds_local_infracao, A.ds_ponto_referencia,  B.vl_infracao AS vl_multa, A.vl_velocidade_permitida, "
						+ " A.vl_velocidade_aferida, A.vl_velocidade_penalidade, A.ds_logradouro, A.ds_nr_imovel, A.nr_cep, A.lg_auto_assinado, A.nr_inventario_inmetro, "
						+ " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, B.tp_responsabilidade, nr_inciso, nr_paragrafo, nr_alinea, nm_natureza, nr_pontuacao,"
						+ " C.nm_cidade, C.nm_cidade AS nm_municipio, "
						+ " C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado,"
						+ " E.nm_categoria,"
						+ " F.nm_cor,"
						+ " G.ds_especie,"
						+ " H.nm_marca, H.nm_modelo,"
						+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento, I.cd_equipamento,"
						+ " J.nm_agente, J.nr_matricula,"
						+ " K.dt_movimento, K.tp_status, K.nr_processo, K.lg_enviado_detran, "
						+ " M.id_identificacao_inmetro,"
						+ " N.ds_logradouro, "
						+ " P.nm_bairro, "
						+ " Q.cd_cidade, Q.nm_cidade AS CIDADE_PROPRIETARIO, "
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
	
	private void montarDadosDocumento(List<DadosNotificacao> listDadosNotificacao, ReportCriterios reportCriterios) throws Exception {
		for (DadosNotificacao dadosNotificacao : listDadosNotificacao) {
			this.nrCodDetran = dadosNotificacao.getNrCodDetran();
			this.tpEquipamento = dadosNotificacao.getTpEquipamento();
			dadosNotificacao.setDtEmissao(Util.getDataAtual());
			AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(dadosNotificacao.getCdAit(), TipoStatusEnum.NAI_ENVIADO.getKey());
			dadosNotificacao.setDtMovimento(aitMovimento.getDtMovimento());
			if (dadosNotificacao.getCdEquipamento() > 0) {
			    dadosNotificacao.setNomeEquipamento(EquipamentoEnum.valueOf(dadosNotificacao.getTpEquipamento()));
			} else {
			    dadosNotificacao.setNomeEquipamento(EquipamentoEnum.NENHUM.getValue());
			}
			setarTxtCondutor(dadosNotificacao, reportCriterios);
			if (dadosNotificacao.getDtPrazoDefesa() == null) {
				dadosNotificacao.setDtPrazoDefesa(new CalculaPrazoRecurso()
						.getStrategy(TipoStatusEnum.DEFESA_PREVIA.getKey())
						.gerarPrazo(dadosNotificacao.getCdAit(), customConnection));
			}
			reportCriterios.addParametros("MOB_IMAGEM_VEICULO", pegarImagemVeiculo(dadosNotificacao.getCdAit()));
			reportCriterios.addParametros("MOB_INFORMACOES_ADICIONAIS_NAI", verificarTipoResponsabilidadeInfracao(dadosNotificacao.getTpResponsabilidade()));
		}
	}
	
	private static void setarTxtCondutor (DadosNotificacao dadosNotificacao, ReportCriterios reportCriterios) throws ValidacaoException{
		int isAr = AitReportValidatorsNAI.verificarAr(reportCriterios.getParametros());
		if (isAr == AitReportServices.MODELO_COM_AR)
			dadosNotificacao.setTxtCondutor("DATA LIMITE P/ APRESENTAÇÃO DO CONDUTOR: " + (dadosNotificacao.getDtPrazoDefesa() != null ? 
				Util.formatDate(dadosNotificacao.getDtPrazoDefesa(), "dd/MM/yyyy") + "." : "EM ATÉ 30 DIAS A CONTAR DO RECEBIMENTO DESTA NOTIFICAÇÃO."));
	}
	
	private Image pegarImagemVeiculo(int cdAit) throws Exception {
		this.imgMulta = null;
		this.aitImagem = new AitImagem();
		this.aitImagem = aitImagemService.getFromAit(cdAit);
		if(aitImagem.getBlbImagem() != null ) {
			this.imgMulta = new ImageIcon(aitImagem.getBlbImagem()).getImage();
		}
		return this.imgMulta;
	}
	
	public String getEstado() throws Exception {
		Orgao orgao = this.orgaoService.getOrgaoUnico();
		Cidade cidade = this.cidadeRepository.get(orgao.getCdCidade());
		return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
	}
	
	private String verificarTipoResponsabilidadeInfracao(int tpResponsabilidade) throws Exception {
		String parametro;
		if(tpResponsabilidade == TipoResponsabilidadeInfracaoEnum.MULTA_RESPONSABILIDADE_PROPRIETARIO.getKey()) {
			parametro = parametroService.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_RESPONSABILIDADE_PROPRIETARIO").getTxtValorParametro();
			return parametro;
		}
		parametro = parametroService.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_NAI").getTxtValorParametro();
		return parametro;
	}
	
	private String getDtPostagem(int cdAit) throws Exception {
		String dtPostagem = "";
		Boolean isAitsMesmoLote = this.loteImpressaoService.verificarAitsComMesmoLote(cdAit, TipoLoteNotificacaoEnum.LOTE_NAI.getKey());
		if(isAitsMesmoLote) {
			dtPostagem = new GeradorDtPostagem()
					.gerar(loteImpressaoAitRepository.getDTO(cdAit, TipoStatusEnum.NAI_ENVIADO.getKey(), customConnection).getDtCriacao());
			return dtPostagem;
		}
		return dtPostagem;
	}
}
