package com.tivic.manager.mob.lote.impressao;

import java.io.File;
import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.DocumentException;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.parametro.IParametroService;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lote.impressao.builders.GeradorDtPostagem;
import com.tivic.manager.mob.lote.impressao.codigobarras.CodigoBarras;
import com.tivic.manager.mob.lote.impressao.codigobarras.GerarCodigoBarrasFactory;
import com.tivic.manager.mob.lote.impressao.remessacorreios.CartaSimples;
import com.tivic.manager.mob.lote.impressao.remessacorreios.DadosDocumento;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.util.manipulatefiles.IGenerateFile;
import com.tivic.manager.util.manipulatefiles.IManipulateFolder;
import com.tivic.manager.util.manipulatefiles.PathFolderBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.files.pdf.IPdfGenerator;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ItemComparator;

public class GeraDocImpressaoNipViaUnica implements IGerarDocumentosLoteImpressao {
	
	private AitRepository aitRepository;
	private byte[] byteNotificationConcat;
	private List<byte[]> listBytePdf;
	private File notiticationConcat;
	private ArrayList<String> pathNotifications;
	private byte[] content;
	private Runtime runtime = Runtime.getRuntime();
	private ILoteNotificacaoService loteNotificacaoService;
	private IPdfGenerator pdfGenerator;
	private CustomConnection customConnection;
	private ILoteNotificacaoAitService loteNotificacaoAitService;
	private IArquivoService arquivoService;
	private IManipulateFolder manipulateFolder;
	private IGenerateFile generateFile;
	private List<DadosNotificacao> listDadosNotificacao;
	private Report report;
	private CodigoBarras codigoBarras;
	private String path;
	private IAitMovimentoService aitMovimentoService;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private ManagerLog managerLog;
	private AitMovimentoRepository aitMovimentoRepository;
	private IParametroService parametroService;
	
	public GeraDocImpressaoNipViaUnica() throws Exception {
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		pdfGenerator = (IPdfGenerator) BeansFactory.get(IPdfGenerator.class);
		loteNotificacaoAitService = (ILoteNotificacaoAitService) BeansFactory.get(ILoteNotificacaoAitService.class);
		arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		manipulateFolder = (IManipulateFolder) BeansFactory.get(IManipulateFolder.class);
		generateFile = (IGenerateFile) BeansFactory.get(IGenerateFile.class);
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		parametroService = (IParametroService) BeansFactory.get(IParametroService.class);
	}
	
	public byte[] gerarDocumentos(LoteImpressao loteImpressao, int cdUsuario, CustomConnection connection) throws Exception, ValidacaoException {
		this.customConnection = connection;
		criarPasta(loteImpressao.getCdLoteImpressao());
		Ait ait = buscarCdAitOrigem(loteImpressao.getCdLoteImpressao());
		DadosDocumento dadosDocumento = new CartaSimples()
				.gerarDadosDocumento((ait != null ? TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey() : TipoLoteNotificacaoEnum.LOTE_NIP.getKey()), loteImpressao, connection);
		runtime.gc();
		ReportCriterios reportCriterios = montarReportCriterios(dadosDocumento.getCriteriosDocumentos());
		for (int i = 0; i < dadosDocumento.getDadosNotificacaoList().size(); i++) {
			this.content = new byte[] {};
			this.content = findDocumentoNip(dadosDocumento.getDadosNotificacaoList().get(i), reportCriterios)
					.getReportPdf((dadosDocumento.getNmDocumento() != null && dadosDocumento.getNmDocumento().equals("mob/nic_nip_v2")) 
					? dadosDocumento.getNmDocumento() : getNomeDocumentoPenalidade(dadosDocumento.getDadosNotificacaoList().get(i).getCdAit()));
			salvarDocumentoLoteAit(dadosDocumento.getDadosNotificacaoList().get(i), content, cdUsuario);
			addReportCriteriosDtPostagem(dadosDocumento.getDadosNotificacaoList().get(i).getCdAit(), reportCriterios);
			connection.finishConnection();
			runtime.gc();
		}
		ArrayList<String> pathNotifications = manipulateFolder.listFiles(new File(this.path));
		File notiticationConcat = new File(pathNotifications.get(0));
		byte[] bytePdfLoteNotificacao = generateFile.convertFileToByte(notiticationConcat);
		salvarDocumentoLote(loteImpressao, bytePdfLoteNotificacao, cdUsuario);
		runtime.gc();
		manipulateFolder.removeAllFiles(new File(this.path));
		return bytePdfLoteNotificacao;
	}
	
	private Ait buscarCdAitOrigem(int cdLoteImpressao) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote_impressao", cdLoteImpressao, true);
		List<LoteImpressaoAit> loteImpressaoAitList = this.loteImpressaoAitRepository.find(searchCriterios, new CustomConnection());
		if(loteImpressaoAitList.isEmpty()) {
			managerLog.info("[Geração de NIC] Não foi localizado nenhum AIT.", DateUtil.getDataAtual().toString());
			return null;	
		}
		Ait ait = this.aitRepository.get(loteImpressaoAitList.get(0).getCdAit());
		return ait != null && ait.getCdAitOrigem() > 0 ? ait : null;
	}
	
	private void criarPasta(int cdLoteNotificacao) {
		this.path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR", new PathFolderBuilder()
						.add("")
						.add("tivic")
						.add("work")
						.getPath())
				  + new PathFolderBuilder()
				  		.add("LoteNotificacaoNIP_" + cdLoteNotificacao)
				  		.getPath();
		this.manipulateFolder.createFolder(this.path);
	}
	
	private ReportCriterios montarReportCriterios(ReportCriterios reportCriterios) throws ValidacaoException {
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", customConnection.getConnection()));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", customConnection.getConnection()));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", customConnection.getConnection()));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_IMPRESSOS_DS_TEXTO_NIP", ParametroServices.getValorOfParametro("MOB_IMPRESSOS_DS_TEXTO_NIP", customConnection.getConnection()));
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
		reportCriterios.addParametros("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", customConnection.getConnection()));
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
		reportCriterios.addParametros("IS_NOT_SEGUNDA_VIA", true);
		return reportCriterios;
	}
		
	private Report findDocumentoNip(DadosNotificacao dadosNotificacao, ReportCriterios reportCriterios) throws Exception {
		this.listDadosNotificacao = new ArrayList<DadosNotificacao>();
		this.report = new Report();
		ApresentacaoCondutor apresentacaoCondutor = getCondutorFici(dadosNotificacao.getCdAit());
		if (apresentacaoCondutor != null) {
			dadosNotificacao.setNmCondutor(apresentacaoCondutor.getNmCondutor() != null ? apresentacaoCondutor.getNmCondutor(): null);
			dadosNotificacao.setNrCnhCondutor(apresentacaoCondutor.getNrCnh() != null ? apresentacaoCondutor.getNrCnh() : null);
            dadosNotificacao.setUfCnhCondutor(apresentacaoCondutor.getUfCnh() != null ? apresentacaoCondutor.getUfCnh() : null);
	    }
		gerarCodigoBarrar(dadosNotificacao, reportCriterios);
		this.listDadosNotificacao.add(dadosNotificacao);
		this.report = new ReportBuilder()
				.list(listDadosNotificacao)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}
	
	private ApresentacaoCondutor getCondutorFici(int cdAit) throws Exception {
		int codigoFiciAceita = ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_APRESENTACAO_CONDUTOR_ACEITO",	0);
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
		searchCriterios.addCriteriosEqualInteger("A.cd_ocorrencia", codigoFiciAceita, true);
		Search<ApresentacaoCondutor> searchCondutor = new SearchBuilder<ApresentacaoCondutor>("mob_ait_movimento A")
				.fields("D.nm_condutor, D.nr_cnh, D.uf_cnh").addJoinTable("JOIN mob_ait_movimento_documento B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("JOIN ptc_documento C ON (B.cd_documento = C.cd_documento)")
				.addJoinTable("JOIN ptc_apresentacao_condutor D ON (C.cd_documento = D.cd_documento)")
				.additionalCriterias("C.cd_fase <> " + parametroService.getValorOfParametroAsInt("CD_SITUACAO_DOCUMENTO_CANCELADO").getNrValorParametro())
				.searchCriterios(searchCriterios)
			.build();
		List<ApresentacaoCondutor> apresentacaoCondutorList = searchCondutor.getList(ApresentacaoCondutor.class);
	    if (!apresentacaoCondutorList.isEmpty()) {
	        return apresentacaoCondutorList.get(0);
	    }
	    return null;
	}
	
	private void gerarCodigoBarrar(DadosNotificacao dadosNotificacao, ReportCriterios reportCriterios) throws ValidacaoException, Exception {
		boolean isPaga = verificarAitComMultaPaga(dadosNotificacao.getCdAit());
		if (reportCriterios.getParametros().get("NR_CD_FEBRABAN") != null) {
			this.codigoBarras = new CodigoBarras();
			this.codigoBarras = new GerarCodigoBarrasFactory()
					.getStrategy()
					.gerarCodigoBarras(dadosNotificacao, reportCriterios);
			if(!isPaga) {
				dadosNotificacao.setLinhaDigitavel(codigoBarras.getLinhaDigitavel());
			}
			reportCriterios.addParametros("BARRAS", !isPaga ? codigoBarras.getBarcodeImage() : null);
			reportCriterios.addParametros("TXT_MULTA_PAGA", isPaga ? "MULTA JÁ PAGA" : " ");
		} 
	}
	
	private boolean verificarAitComMultaPaga(int cdAit) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.MULTA_PAGA.getKey(), true);
		List<AitMovimento> aitMovimento = this.aitMovimentoService.find(searchCriterios);
		if(aitMovimento.isEmpty()) {
			return false;
		}
		return true;
	}
	
	private void salvarDocumentoLoteAit(DadosNotificacao dadosNotificacao, byte[] content, int cdUsuario) throws Exception {
		dadosNotificacao.setStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_IMPRESSAO);
		loteNotificacaoAitService.save(dadosNotificacao, customConnection);
		gerarDocumentoLoteNotificacao(dadosNotificacao, content);
	}
	
	private byte[] gerarDocumentoLoteNotificacao(DadosNotificacao dadosNotificacao, byte[] byteNotification) throws ValidacaoException, DocumentException, IOException {
		this.pathNotifications = manipulateFolder.listFiles(new File(path));
		this.byteNotificationConcat = new byte[] {};
		if (pathNotifications.isEmpty()) {
			this.generateFile.generateFile(byteNotification, path, "LOTE_NOTIFICACAO_" + dadosNotificacao.getCdLoteImpressao()  + ".pdf");
			runtime.gc();
		} else {
			pathNotifications = manipulateFolder.listFiles(new File(path));
			this.notiticationConcat = new File(pathNotifications.get(0));
			this.byteNotificationConcat = generateFile.convertFileToByte(notiticationConcat);
			this.listBytePdf = new ArrayList<byte[]>();
			this.listBytePdf.add(byteNotificationConcat);
			this.listBytePdf.add(byteNotification);
			this.byteNotificationConcat = pdfGenerator.generator(this.listBytePdf);
			this.generateFile.generateFile(this.byteNotificationConcat, path, "LOTE_NOTIFICACAO_" + dadosNotificacao.getCdLoteImpressao()  + ".pdf");
			runtime.gc();
		}
		return this.byteNotificationConcat;
	}
	
	private void salvarDocumentoLote(LoteImpressao loteImpressao, byte[] bytePdfNotificacao, int cdUsuario) throws Exception {
		Arquivo arquivoNotificacao = new ArquivoBuilder()
				.setBlbArquivo(bytePdfNotificacao)
				.setCdUsuario(cdUsuario)
				.setNmArquivo("LOTE_NOTIFICACAO_NIP_" + loteImpressao.getCdLoteImpressao() + ".pdf" )
				.setNmDocumento("Lote de Notificações NIP")
				.setDtArquivamento(new GregorianCalendar())
				.setDtCriacao(new GregorianCalendar())
				.build();
		this.arquivoService.save(arquivoNotificacao, customConnection);
		loteImpressao.setCdArquivo(arquivoNotificacao.getCdArquivo());
		loteImpressao.setStLoteImpressao(LoteImpressaoSituacao.EM_IMPRESSAO);
		this.loteNotificacaoService.save(loteImpressao, this.customConnection);
	}
	
	private String getNomeDocumentoPenalidade(int cdAit) throws Exception, ValidacaoException {
		int tipoMovimento = getMovimentoPenalidade(cdAit);
		return tipoMovimento == TipoStatusEnum.NIP_ENVIADA.getKey() ? "mob/nip_v2" : "mob/notificacao_advertencia";
	}
	
	private int getMovimentoPenalidade(int cdAit) throws Exception, ValidacaoException {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
	    searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
	    searchCriterios.addCriterios("tp_status", String.valueOf(TipoStatusEnum.NIP_ENVIADA.getKey()) + ", " +
	            String.valueOf(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey()), ItemComparator.IN, Types.INTEGER);
	    List<AitMovimento> aitMovimentoList = this.aitMovimentoRepository.find(searchCriterios, new CustomConnection());
	    if (aitMovimentoList == null || aitMovimentoList.isEmpty()) {
	        throw new ValidacaoException("Movimento de penalidade não localizado ou não registrado.");
	    }
	    return aitMovimentoList.get(0).getTpStatus();
	}
	
	private void addReportCriteriosDtPostagem(int cdAit, ReportCriterios reportCriterios) throws Exception {
		String dtPostagem = getDtPostagem(cdAit);
		reportCriterios.addParametros("DT_POSTAGEM", dtPostagem);
	}
	
	private String getDtPostagem(int cdAit) throws Exception {
		String dtPostagem = "";
		Boolean isAitsMesmoLote = loteNotificacaoService.verificarAitsComMesmoLote(cdAit, TipoLoteNotificacaoEnum.LOTE_NIP.getKey());
		if(isAitsMesmoLote) {
			dtPostagem = new GeradorDtPostagem()
					.gerar(loteImpressaoAitRepository.getDTO(cdAit, TipoStatusEnum.NAI_ENVIADO.getKey(), customConnection).getDtCriacao());
			return dtPostagem;
		}
		return dtPostagem;
	}
}
