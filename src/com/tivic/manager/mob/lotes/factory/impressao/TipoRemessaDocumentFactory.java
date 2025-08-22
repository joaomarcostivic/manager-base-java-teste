package com.tivic.manager.mob.lotes.factory.impressao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaRepository;
import com.tivic.manager.mob.lote.impressao.builders.GeradorDtPostagem;
import com.tivic.manager.mob.lotes.dto.DadosDocumento;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.manager.mob.lotes.impressao.arquivopostagem.ArquivoListaPostagem;
import com.tivic.manager.mob.lotes.impressao.arquivopostagem.ArquivoPrevisaoPostagem;
import com.tivic.manager.mob.lotes.impressao.dadosnotificacao.DadosNotificacaoBuilder;
import com.tivic.manager.mob.lotes.impressao.dadosnotificacao.ImagemGenerator;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.manager.mob.lotes.repository.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

public class TipoRemessaDocumentFactory {
    private DadosDocumento dadosDocumento = new DadosDocumento();
    private LoteImpressao loteImpressao;
    private ICorreiosEtiquetaRepository correiosEtiquetaRepository;
    private AitRepository aitRepository;
    private ILoteImpressaoAitRepository loteImpressaoAitRepository;
    private IOrgaoService orgaoService;
    private CidadeRepository cidadeRepository;
    private EstadoRepository estadoRepository;

    public TipoRemessaDocumentFactory(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
        this.correiosEtiquetaRepository = (ICorreiosEtiquetaRepository) BeansFactory.get(ICorreiosEtiquetaRepository.class);
        this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
        this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
        this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
        this.cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
        this.estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
        this.loteImpressao = loteImpressao;
        dadosDocumento.setCdLoteNotificacao(loteImpressao.getCdLoteImpressao());
        setBaseReportCriterios(customConnection);
    }

    public TipoRemessaDocumentFactory() {}

    public TipoRemessaDocumentFactory setNotificacoes() throws Exception {
        List<LoteImpressaoAit> loteImpressaoAits = loteImpressaoAitRepository.findByCdLoteImpressao(loteImpressao.getCdLoteImpressao());
        List<Notificacao> notificacoes = new ArrayList<>();

        GeraDadosNotificacaoFactory geraDadosNotificacaoFactory = new GeraDadosNotificacaoFactory(aitRepository);

        for (LoteImpressaoAit loteImpressaoAit : loteImpressaoAits) {
            Notificacao notificacao = geraDadosNotificacaoFactory.getNotificacao(loteImpressaoAit.getCdAit(), loteImpressaoAit.getCdLoteImpressao());
            notificacoes.add(notificacao);
            addBarCodeCep(notificacao);
        }

        dadosDocumento.setNotificacoes(notificacoes);
        return this;
    }

    public TipoRemessaDocumentFactory setArquivoPostagem(CustomConnection customConnection) throws Exception {
        dadosDocumento.setArquivoPostagem(new ArquivoPrevisaoPostagem(dadosDocumento, customConnection).gerar());
        return this;
    }

    public TipoRemessaDocumentFactory setEtiquetas(CustomConnection customConnection) throws Exception {
        SearchCriterios criterios = new SearchCriterios();
        criterios.addCriteriosEqualInteger("cd_lote_impressao", loteImpressao.getCdLoteImpressao());
        List<CorreiosEtiqueta> etiquetas = correiosEtiquetaRepository.find(criterios, customConnection);

        for (int i = 0; i < dadosDocumento.getNotificacoes().size(); i++) {
            Notificacao notificacao = new DadosNotificacaoBuilder(dadosDocumento.getNotificacoes().get(i), etiquetas.get(i))
            		.build();
            dadosDocumento.getNotificacoes().set(i, notificacao);
        }

        dadosDocumento.setEtiquetas(etiquetas);
        return this;
    }

    public TipoRemessaDocumentFactory addParameter(String nome, Object valor) {
        dadosDocumento.getCriterios().addParametros(nome, valor);
        return this;
    }

    public TipoRemessaDocumentFactory addParameters(List<String> parametros, CustomConnection conn) {
        for (String parametro : parametros) {
            addParameter(parametro, ParametroServices.getValorOfParametro(parametro, conn.getConnection()));
        }
        return this;
    }

    public TipoRemessaDocumentFactory setARParameters(CustomConnection conn) throws Exception {
        addParameter("MOB_LOGO_RECEBIMENTO_AR_DIGITAL", ParametroServices.RecImg("MOB_LOGO_RECEBIMENTO_AR_DIGITAL", conn.getConnection()))
        .addParameter("MOB_LOGO_CORREIOS_CHANCELA", ParametroServices.RecImg("MOB_LOGO_CORREIOS_CHANCELA", conn.getConnection()));
        return this;
    }

    public TipoRemessaDocumentFactory setArquivoListaPostagem(CustomConnection customConnection) throws Exception {
        dadosDocumento.setArquivoListaPostagem(new ArquivoListaPostagem(dadosDocumento, loteImpressao.getTpImpressao(), customConnection).gerar());
        return this;
    }

    public DadosDocumento criar() {
        return dadosDocumento;
    }

    private void setBaseReportCriterios(CustomConnection conn) throws Exception {
        addParameter("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", conn.getConnection()))
        .addParameter("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", conn.getConnection()))
        .addParameter("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", conn.getConnection()))
        .addParameter("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", conn.getConnection()))
        .addParameter("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", conn.getConnection()))
        .addParameter("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", conn.getConnection()))
        .addParameter("NR_TELEFONE_2", ParametroServices.getValorOfParametro("NR_TELEFONE2", conn.getConnection()))
        .addParameter("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL", conn.getConnection()))
        .addParameter("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE", "LOREM IPSUM DOLOR SIT AMET", conn.getConnection()))
        .addParameter("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO", conn.getConnection()))
        .addParameter("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO", conn.getConnection()))
        .addParameter("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO", conn.getConnection()))
        .addParameter("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", "", conn.getConnection()).replaceAll("[^\\d]", ""))
        .addParameter("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CONTRATO", conn.getConnection()))
        .addParameter("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO", conn.getConnection()))
        .addParameter("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO", conn.getConnection()))
        .addParameter("MOB_CORREIOS_SG_CLIENTE", ParametroServices.getValorOfParametro("MOB_CORREIOS_SG_CLIENTE", conn.getConnection()))
        .addParameter("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO", conn.getConnection()))
        .addParameter("mob_local_impressao", ParametroServices.getValorOfParametro("mob_local_impressao", 0, conn.getConnection()))
        .addParameter("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", conn.getConnection()))
        .addParameter("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", ParametroServices.getValorOfParametro("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", conn.getConnection()))
        .addParameter("MOB_PRAZOS_NR_DEFESA_PREVIA", ParametroServices.getValorOfParametro("MOB_PRAZOS_NR_DEFESA_PREVIA", conn.getConnection()))
        .addParameter("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", ParametroServices.getValorOfParametro("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", conn.getConnection()))
        .addParameter("SG_UF", getEstado(conn))
        .addParameter("REPORT_LOCALE", new Locale("pt", "BR"))
        .addParameter("DT_POSTAGEM", new GeradorDtPostagem().gerar(DateUtil.getDataAtual()));
    }
    
    private String getEstado(CustomConnection customConnection) throws Exception {
		Orgao orgao = this.orgaoService.getOrgaoUnico();
		Cidade cidade = this.cidadeRepository.get(orgao.getCdCidade(), customConnection);
		addParameter("ID_CIDADE", cidade.getIdCidade() != null ? cidade.getIdCidade() : null);
		return estadoRepository.get(cidade.getCdEstado(), customConnection).getSgEstado();
	}
    
	public void addBarCodeCep(Notificacao dados) throws IOException {
		if(dados.getNrCep() != null) {
		    dados.setBarCodeImageCep(new ImagemGenerator().gerar(dados.getNrCep()));
		}
	}
}
