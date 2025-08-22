package com.tivic.manager.mob.lotes.service.impressao.facades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaRepository;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.files.pdf.IPdfGenerator;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import edu.emory.mathcs.backport.java.util.Collections;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import sol.util.ConfManager;

public class DocumentGeneratorFacade {

    private IPdfGenerator pdfGenerator;
    private ICorreiosEtiquetaRepository correiosEtiquetaRepository;
    private File folder;
    private final CopyOnWriteArrayList<byte[]> files = new CopyOnWriteArrayList<>();

    public DocumentGeneratorFacade(LoteImpressao loteImpressao) throws Exception {
        pdfGenerator = (IPdfGenerator) BeansFactory.get(IPdfGenerator.class);
        correiosEtiquetaRepository = (ICorreiosEtiquetaRepository) BeansFactory.get(ICorreiosEtiquetaRepository.class);

        String basePath = ManagerConf.getInstance().get("TOMCAT_WORK_DIR");
        if (basePath == null) {
            throw new Exception("Caminho da pasta para salvar documento é inválido.");
        }
        setFolder(basePath);
    }

    public void generateNotificacao(Notificacao notificacao, ReportCriterios criterios, String nmDocumento, TipoRemessaCorreiosEnum tpRemessa) throws Exception {
        List<Notificacao> dadosSubreport = Arrays.asList(notificacao);
        boolean lgGeracaoViaUnica = Boolean.TRUE.equals(criterios.getParametros().get("LG_GERACAO_VIA_UNICA"));
        setParamsReport(criterios.getParametros(), dadosSubreport, lgGeracaoViaUnica, tpRemessa);

        Report report = new ReportBuilder()
                .list(Collections.singletonList(notificacao))
                .reportCriterios(criterios)
                .build();

        files.add(report.getReportPdf(nmDocumento));
    }

    private void setParamsReport(HashMap<String, Object> parametrosSubReport, List<?> dadosSubreport, boolean lgGeracaoViaUnica, TipoRemessaCorreiosEnum tpRemessa) throws Exception {
        ConfManager conf = ManagerConf.getInstance();
        String reportPath = conf.getProps().getProperty("REPORT_PATH");
        String path = ContextManager.getRealPath() + "/" + reportPath;

        parametrosSubReport.put("SUBREPORT_DIR", path + "//mob");
        parametrosSubReport.put("REPORT_LOCALE", new Locale("pt", "BR"));
        parametrosSubReport.put("SUBREPORT_NAMES", lgGeracaoViaUnica || tpRemessa == TipoRemessaCorreiosEnum.CARTA_SIMPLES
                ? "mob//verso_notificacao_carta_simples_subreport"
                : "mob//verso_notificacao_com_ar_subreport");
        parametrosSubReport.put("SUBREPORT_DADOS_FIELDS", new JRBeanCollectionDataSource(dadosSubreport));
        parametrosSubReport.put("SUBREPORT_PARAMETERS_MAP", parametrosSubReport);
    }

    public void updateStatusEtiquetas(TipoStatusEnum tpStatus, List<CorreiosEtiqueta> etiquetas, CustomConnection customConnection) throws Exception {
        for (CorreiosEtiqueta correiosEtiqueta : etiquetas) {
            correiosEtiqueta.setTpStatus(tpStatus.getKey());
            correiosEtiquetaRepository.update(correiosEtiqueta, customConnection);
        }
    }

    public byte[] gerarDocumentoLote(String idLote) throws Exception {
        byte[] arquivoLote = pdfGenerator.generator(files);
        salvarArquivo(arquivoLote, idLote);
        return arquivoLote;
    }

    private void salvarArquivo(byte[] content, String idLote) throws Exception {
        File arquivo = new File(folder, "LOTE_NOTIFICACAO_" + idLote.replace("/", "_") + ".pdf");
        try (FileOutputStream fos = new FileOutputStream(arquivo)) {
            fos.write(content);
        } catch (IOException e) {
            throw new Exception("Erro ao adicionar o arquivo", e);
        }
    }

    private void setFolder(String basePath) throws Exception {
        File baseDir = new File(basePath);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            throw new Exception("O caminho para salvar o documento não foi configurado corretamente.");
        }

        File pastaLote = new File(basePath, "lote-notificacao-" + Util.formatDate(new GregorianCalendar(), "yyyy-MM-dd").replace(":", "-"));
        if (!pastaLote.exists() && !pastaLote.mkdirs()) {
            throw new Exception("Falha ao criar a pasta para salvar o documento.");
        }

        this.folder = pastaLote;
    }
}
