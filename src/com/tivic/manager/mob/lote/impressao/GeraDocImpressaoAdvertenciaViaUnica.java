package com.tivic.manager.mob.lote.impressao;

import java.io.File;
import java.io.IOException;
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
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lote.impressao.builders.GeradorDtPostagem;
import com.tivic.manager.mob.lote.impressao.remessacorreios.CartaAdvertencia;
import com.tivic.manager.mob.lote.impressao.remessacorreios.DadosDocumento;
import com.tivic.manager.util.manipulatefiles.IGenerateFile;
import com.tivic.manager.util.manipulatefiles.IManipulateFolder;
import com.tivic.manager.util.manipulatefiles.PathFolderBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.files.pdf.IPdfGenerator;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;

public class GeraDocImpressaoAdvertenciaViaUnica implements IGerarDocumentosLoteImpressao {
	
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
	private String path;
	
	public GeraDocImpressaoAdvertenciaViaUnica() throws Exception {
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		pdfGenerator = (IPdfGenerator) BeansFactory.get(IPdfGenerator.class);
		loteNotificacaoAitService = (ILoteNotificacaoAitService) BeansFactory.get(ILoteNotificacaoAitService.class);
		arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		manipulateFolder = (IManipulateFolder) BeansFactory.get(IManipulateFolder.class);
		generateFile = (IGenerateFile) BeansFactory.get(IGenerateFile.class);
	}

	@Override
	public byte[] gerarDocumentos(LoteImpressao loteImpressao, int cdUsuario, CustomConnection connection) throws Exception, ValidacaoException {
		this.customConnection = connection;
		criarPasta(loteImpressao.getCdLoteImpressao());
		DadosDocumento dadosDocumento = new CartaAdvertencia()
				.gerarDadosDocumento(TipoLoteNotificacaoEnum.LOTE_NIP.getKey(), loteImpressao, connection);
		runtime.gc();
		ReportCriterios reportCriterios = montarReportCriterios(dadosDocumento.getCriteriosDocumentos(), loteImpressao.getDtCriacao());
		for (int i = 0; i < dadosDocumento.getDadosNotificacaoList().size(); i++) {
			this.content = new byte[] {};
			this.content = findDocumentoAdvertencia(dadosDocumento.getDadosNotificacaoList().get(i), reportCriterios).getReportPdf(dadosDocumento.getNmDocumento());
			salvarDocumentoLoteAit(dadosDocumento.getDadosNotificacaoList().get(i), content, cdUsuario);
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
	
	private void criarPasta(int cdLoteNotificacao) {
		this.path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR", new PathFolderBuilder()
						.add("")
						.add("tivic")
						.add("work")
						.getPath())
				  + new PathFolderBuilder()
				  		.add("LoteAdvertenciaNIP_" + cdLoteNotificacao)
				  		.getPath();
		this.manipulateFolder.createFolder(this.path);
	}
	
	private ReportCriterios montarReportCriterios(ReportCriterios reportCriterios, GregorianCalendar dtCriacao) throws ValidacaoException {
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
		reportCriterios.addParametros("DT_POSTAGEM", new GeradorDtPostagem().gerar(dtCriacao));
		return reportCriterios;
	}
	
	private Report findDocumentoAdvertencia(DadosNotificacao dadosNotificacao, ReportCriterios reportCriterios) throws Exception {
		this.listDadosNotificacao = new ArrayList<DadosNotificacao>();
		this.report = new Report();
		this.listDadosNotificacao.add(dadosNotificacao);
		this.report = new ReportBuilder()
				.list(listDadosNotificacao)
				.reportCriterios(reportCriterios)
				.build();
		return report;
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
}
