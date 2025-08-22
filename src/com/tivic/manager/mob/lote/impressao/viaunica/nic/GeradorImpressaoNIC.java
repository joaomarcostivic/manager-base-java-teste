package com.tivic.manager.mob.lote.impressao.viaunica.nic;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;

import com.itextpdf.text.DocumentException;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.aitimagem.IAitImagemService;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoAitService;
import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoService;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.remessacorreios.CartaSimples;
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

public class GeradorImpressaoNIC {
	private byte[] byteNotificationConcat;
	private List<byte[]> listBytePdf;
	private File notiticationConcat;
	private ArrayList<String> pathNotifications;
	private byte[] content;
	private ILoteNotificacaoService loteNotificacaoService;
	private IPdfGenerator pdfGenerator;
	private CustomConnection customConnection;
	private IArquivoService arquivoService;
	private ILoteNotificacaoAitService loteNotificacaoAitService;
	private IManipulateFolder manipulateFolder;
	private IGenerateFile generateFile;
	private IAitImagemService aitImagemService;
	private List<DadosNotificacao> listDadosNotificacao;
	private Report report;
	private Image imgMulta;
	private AitImagem aitImagem;
	private String path;
	private ILoteImpressaoRepository loteImpressaoRepository;

	public GeradorImpressaoNIC() throws Exception {
		this.loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		this.pdfGenerator = (IPdfGenerator) BeansFactory.get(IPdfGenerator.class);
		this.arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		this.loteNotificacaoAitService = (ILoteNotificacaoAitService) BeansFactory.get(ILoteNotificacaoAitService.class);
		this.manipulateFolder = (IManipulateFolder) BeansFactory.get(IManipulateFolder.class);
		this.generateFile = (IGenerateFile) BeansFactory.get(IGenerateFile.class);
		this.aitImagemService = (IAitImagemService) BeansFactory.get(IAitImagemService.class);
		loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
		
	}

	public byte[] geraDocumentos(NicSimplificadaDTO nicSimplificadaDTO, int cdUsuario, CustomConnection connection)
			throws Exception {
		this.customConnection = connection;
		criarPasta(nicSimplificadaDTO.getCdLoteImpressao());
		LoteImpressao loteImpressao = loteImpressaoRepository.get(nicSimplificadaDTO.getCdLoteImpressao(), customConnection);
		DadosDocumento dadosDocumento = new CartaSimples()
				.gerarDadosDocumento(TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey(), loteImpressao, connection);
		ReportCriterios reportCriterios = montarReportCriterios(dadosDocumento.getCriteriosDocumentos());
		for (int i = 0; i < dadosDocumento.getDadosNotificacaoList().size(); i++) {
			this.content = new byte[] {};
			this.content = findDocumentoNai(dadosDocumento.getDadosNotificacaoList().get(i), reportCriterios)
					.getReportPdf("mob/nic_nai_v2");
			salvarDocumentoLoteAit(dadosDocumento.getDadosNotificacaoList().get(i), content);
			connection.finishConnection();
		}
		ArrayList<String> pathNotifications = manipulateFolder.listFiles(new File(this.path));
		File notiticationConcat = new File(pathNotifications.get(0));
		byte[] bytePdfLoteNotificacao = generateFile.convertFileToByte(notiticationConcat);
		salvarDocumentoLote(loteImpressao, bytePdfLoteNotificacao, cdUsuario);
		manipulateFolder.removeAllFiles(new File(this.path));
		return bytePdfLoteNotificacao;
	}
	

	private void criarPasta(int cdLoteNotificacao) {
		this.path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR",
				new PathFolderBuilder().add("").add("tivic").add("work").getPath())
				+ new PathFolderBuilder().add("LoteNotificacaoNAI_" + cdLoteNotificacao).getPath();
		this.manipulateFolder.createFolder(this.path);
	}

	private ReportCriterios montarReportCriterios(ReportCriterios reportCriterios) throws ValidacaoException {
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
		reportCriterios.addParametros("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", customConnection.getConnection()));
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
		reportCriterios.addParametros("SG_UF", loteNotificacaoService.getEstadoOrgaoAutuador());
		reportCriterios.addParametros("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_CORREIOS_SG_CLIENTE", ParametroServices.getValorOfParametro("MOB_CORREIOS_SG_CLIENTE", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_INFORMACOES_ADICIONAIS_NAI", ParametroServices.getValorOfParametro("MOB_INFORMACOES_ADICIONAIS_NAI", customConnection.getConnection()));
		return reportCriterios;
	}

	private Report findDocumentoNai(DadosNotificacao dadosNotificacao, ReportCriterios reportCriterios)
			throws Exception {
		this.listDadosNotificacao = new ArrayList<DadosNotificacao>();
		this.report = new Report();
		reportCriterios.addParametros("MOB_IMAGEM_VEICULO", pegarImagemVeiculo(dadosNotificacao.getCdAit()));
		this.listDadosNotificacao.add(dadosNotificacao);
		this.report = new ReportBuilder().list(listDadosNotificacao).reportCriterios(reportCriterios).build();
		return report;
	}

	private Image pegarImagemVeiculo(int cdAit) throws Exception {
		this.imgMulta = null;
		this.aitImagem = new AitImagem();
		this.aitImagem = aitImagemService.getFromAit(cdAit);
		if (aitImagem.getBlbImagem() != null) {
			this.imgMulta = new ImageIcon(aitImagem.getBlbImagem()).getImage();
		}
		return this.imgMulta;
	}

	private void salvarDocumentoLoteAit(DadosNotificacao dadosNotificacao, byte[] content) throws Exception {
		dadosNotificacao.setStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_IMPRESSAO);
		loteNotificacaoAitService.save(dadosNotificacao, customConnection);
		gerarDocumentoLoteNotificacao(dadosNotificacao, content);
	}

	private byte[] gerarDocumentoLoteNotificacao(DadosNotificacao dadosNotificacao, byte[] byteNotification)
			throws ValidacaoException, DocumentException, IOException {
		this.pathNotifications = manipulateFolder.listFiles(new File(path));
		this.byteNotificationConcat = new byte[] {};
		if (pathNotifications.isEmpty()) {
			this.generateFile.generateFile(byteNotification, path,
					"LOTE_NOTIFICACAO_" + dadosNotificacao.getCdLoteImpressao() + ".pdf");
		} else {
			pathNotifications = manipulateFolder.listFiles(new File(path));
			this.notiticationConcat = new File(pathNotifications.get(0));
			this.byteNotificationConcat = generateFile.convertFileToByte(notiticationConcat);
			this.listBytePdf = new ArrayList<byte[]>();
			this.listBytePdf.add(byteNotificationConcat);
			this.listBytePdf.add(byteNotification);
			this.byteNotificationConcat = pdfGenerator.generator(this.listBytePdf);
			this.generateFile.generateFile(this.byteNotificationConcat, path,
					"LOTE_NOTIFICACAO_" + dadosNotificacao.getCdLoteImpressao() + ".pdf");
		}
		return this.byteNotificationConcat;
	}

	private void salvarDocumentoLote(LoteImpressao loteImpressao, byte[] bytePdfNotificacao, int cdUsuario)
			throws Exception {
		Arquivo arquivoNotificacao = new ArquivoBuilder().setBlbArquivo(bytePdfNotificacao).setCdUsuario(cdUsuario)
				.setNmArquivo("LOTE_NOTIFICACAO_NAI_" + loteImpressao.getCdLoteImpressao() + ".pdf")
				.setNmDocumento("Lote de Notificações NAI").setDtArquivamento(new GregorianCalendar())
				.setDtCriacao(new GregorianCalendar()).build();
		this.arquivoService.save(arquivoNotificacao, customConnection);
		loteImpressao.setCdArquivo(arquivoNotificacao.getCdArquivo());
		loteImpressao.setStLoteImpressao(LoteImpressaoSituacao.EM_IMPRESSAO);
		this.loteNotificacaoService.save(loteImpressao, this.customConnection);
	}
}