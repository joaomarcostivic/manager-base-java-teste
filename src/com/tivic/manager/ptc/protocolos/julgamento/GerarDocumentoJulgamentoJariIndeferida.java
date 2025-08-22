package com.tivic.manager.ptc.protocolos.julgamento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.colaborador.IColaboradorService;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.impressao.ILoteImpressaoAitRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.files.pdf.IPdfGenerator;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

public class GerarDocumentoJulgamentoJariIndeferida implements IGerarDocumentosLoteJulgamento {
	
	private ICartaJulgamentoService cartaJulgamentoService;
	private CustomConnection customConnection;
	private IPdfGenerator pdfGenerator;
	private CidadeRepository cidadeRepository;
	private EstadoRepository estadoRepository;
	private IArquivoService arquivoservice;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private LoteRepository loteRepository;
	private IColaboradorService colaboradorService;

	public GerarDocumentoJulgamentoJariIndeferida() throws Exception {
		cartaJulgamentoService = (ICartaJulgamentoService) BeansFactory.get(ICartaJulgamentoService.class);
		pdfGenerator = (IPdfGenerator) BeansFactory.get(IPdfGenerator.class);
		cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
		estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
		arquivoservice = (IArquivoService) BeansFactory.get(IArquivoService.class);
		loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
		colaboradorService = (IColaboradorService) BeansFactory.get(IColaboradorService.class);
	}
	
	public byte[] gerarDocumentos(LoteImpressao	 loteImpressao, int cdUsuario, CustomConnection connection) throws Exception {
		this.customConnection = connection;
		ReportCriterios reportCriterios = montarReportCriterios();
		List<LoteImpressaoAit> loteImpressaoAitList = searchLoteAits(loteImpressao.getCdLoteImpressao()).getList(LoteImpressaoAit.class);
		List<byte[]> listBytePdf = new ArrayList<byte[]>();
		for (LoteImpressaoAit loteImpressaoAit: loteImpressaoAitList) {
			Report reportDocumentoDefesaDeferida = findDocumentoJariIndeferida(loteImpressaoAit, reportCriterios);
			byte[] content = reportDocumentoDefesaDeferida.getReportPdf("ptc/carta_julgamento");
			listBytePdf.add(content);
			atualizarSituacaoImpressaoAit(loteImpressaoAit, connection);
			connection.finishConnection();
		}
		byte[] bytePdfNotificacao = pdfGenerator.generator(listBytePdf);
		salvarDocumentoLote(loteImpressao, bytePdfNotificacao, cdUsuario);
		return bytePdfNotificacao;
	}
	
	private ReportCriterios montarReportCriterios() throws Exception {
    	Map<String, Object> assinaturaAutoridade = colaboradorService.buscarAssinaturaAutoridade();
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE"));
		reportCriterios.addParametros("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL"));
		reportCriterios.addParametros("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE"));
		reportCriterios.addParametros("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO"));
		reportCriterios.addParametros("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO"));
		reportCriterios.addParametros("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO"));
		reportCriterios.addParametros("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP").replaceAll("[^\\d]", ""));
		reportCriterios.addParametros("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO"));
		reportCriterios.addParametros("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CONTRATO"));
		reportCriterios.addParametros("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO"));
		reportCriterios.addParametros("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS"));
		reportCriterios.addParametros("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", ParametroServices.getValorOfParametro("MOB_BLB_MENSAGEM_EDUCATIVA_NAI"));
		reportCriterios.addParametros("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO"));
		reportCriterios.addParametros("DS_SUBTITULO", "INDEFERIMENTO DO RECURSO - 1ª INSTÂNCIA - JARI");
		reportCriterios.addParametros("SG_UF", getEstado());
		reportCriterios.addParametros("ASSINATURA_AUTORIDADE", (assinaturaAutoridade != null && !assinaturaAutoridade.isEmpty()) 
				? (byte[]) assinaturaAutoridade.get("assinaturaAutoridade") : null);
		reportCriterios.addParametros("NM_AUTORIDADE", (assinaturaAutoridade != null && !assinaturaAutoridade.isEmpty()) 
				? (String) assinaturaAutoridade.get("nomeAutoridade") : null);
		return reportCriterios;
	}
	
	private void montarDadosDocumento(List<DadosCartaJulgamento> listDadosCartaJulgamento, ReportCriterios reportCriterios) throws Exception {
		for (DadosCartaJulgamento dadosCartaJulgamento : listDadosCartaJulgamento) {
			alteraTextoJariIndeferida(dadosCartaJulgamento);
			reportCriterios.addParametros("MOB_BLB_MENSAGEM_RESULTADO", dadosCartaJulgamento.getJariIndeferida());
		}
	}
	
	private void alteraTextoJariIndeferida(DadosCartaJulgamento dadosCartaJulgamento) throws Exception {
			String textoResultado = ParametroServices.getValorOfParametroAsString("MOB_BLB_JARI_SEM_PROVIMENTO", "");
			textoResultado = textoResultado.replace("<#Nr AIT>", dadosCartaJulgamento.getIdAit());
			textoResultado = textoResultado.replace("<#Nr PROCESSO>", dadosCartaJulgamento.getNrProcesso());
			textoResultado = textoResultado.replace("<#DATA_RESULTADO>", DateUtil.formatDate((dadosCartaJulgamento.getDtMovimento()), "dd/MM/yyyy"));
			textoResultado = textoResultado.replace("<#DATA_PUBLICACAO>", DateUtil.formatDate((dadosCartaJulgamento.getDtPublicacaoDo()), "dd/MM/yyyy"));
			dadosCartaJulgamento.setJariIndeferida(textoResultado);
	}
	
	private Search<LoteImpressaoAit> searchLoteAits(int cdLote) throws Exception {
		SearchCriterios searchCriterios = montarCriteriosLoteAits(cdLote);
		Search<LoteImpressaoAit> search = new SearchBuilder<LoteImpressaoAit>("MOB_LOTE_IMPRESSAO_AIT A")
				.searchCriterios(searchCriterios)
				.build();
		return search;
	}
	
	private SearchCriterios montarCriteriosLoteAits(int cdLote) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", cdLote, true);
		return searchCriterios;
	}
	
	private Report findDocumentoJariIndeferida(LoteImpressaoAit loteImpressaoAit, ReportCriterios reportCriterios) throws Exception {
		SearchCriterios searchCriterios = montarCriterios(loteImpressaoAit.getCdAit());
		List<DadosCartaJulgamento> listDadosJulgamento = searchJariIndeferida(searchCriterios).getList(DadosCartaJulgamento.class);
		montarDadosDocumento(listDadosJulgamento, reportCriterios);
		Report report = new ReportBuilder()
				.list(listDadosJulgamento)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}

	private SearchCriterios montarCriterios(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		return searchCriterios;
	}
	
	private Search<DadosCartaJulgamento> searchJariIndeferida(SearchCriterios searchCriterios) throws Exception {
		Search<DadosCartaJulgamento> search = new SearchBuilder<DadosCartaJulgamento>("MOB_AIT A")
				.customConnection(this.customConnection)
				.fields(  " A.cd_Ait, A.id_ait, A.dt_infracao, A.nr_placa, A.nm_proprietario, A.nr_cpf_cnpj_proprietario, "
						+ " A.ds_logradouro, A.ds_nr_imovel, A.nr_cep, "
						+ " C.nm_cidade, C.nm_cidade AS nm_municipio, "
						+ " D.sg_estado, D.sg_estado AS nm_uf, D.nm_estado, "
						+ " K.dt_movimento, K.tp_status, K.nr_processo, K.dt_publicacao_do, "
						+ " N.ds_logradouro,"
						+ " P.nm_bairro,"
						+ " Q.cd_cidade, Q.nm_cidade AS CIDADE_PROPRIETARIO, "
						+ " T.nr_documento ")
				.addJoinTable(" LEFT OUTER JOIN grl_cidade 			   C  ON (A.cd_cidade = C.cd_cidade)")
				.addJoinTable(" LEFT OUTER JOIN grl_estado 			   D  ON (C.cd_estado = D.cd_estado)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_movimento	   K  ON (A.cd_ait = K.cd_ait AND K.tp_status = " + TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey() + " )")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_proprietario   N  ON (A.cd_ait = N.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN grl_bairro             P  ON (A.cd_bairro = P.cd_bairro)")
				.addJoinTable(" LEFT OUTER JOIN grl_cidade             Q  ON (Q.cd_cidade = P.cd_cidade)")
				.addJoinTable(" LEFT JOIN mob_ait_movimento_documento  R  ON (A.cd_ait = R.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN ptc_documento          T  ON (T.cd_documento = R.cd_documento)")
				.searchCriterios(searchCriterios)
				.build();
		return search;
	}
	
	private void atualizarSituacaoImpressaoAit(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception {
		loteImpressaoAit.setStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_IMPRESSAO);
		loteImpressaoAitRepository.update(loteImpressaoAit, customConnection);
	}
	
	private void salvarDocumentoLote(LoteImpressao loteImpressao, byte[] bytePdfNotificacao, int cdUsuario) throws Exception {
		Lote lote = loteRepository.get(loteImpressao.getCdLote(), customConnection);
		Arquivo arquivoLote = new ArquivoBuilderDefesaDeferida(lote.getIdLote(), bytePdfNotificacao, cdUsuario).build();
		Arquivo arquivoCriado = arquivoservice.save(arquivoLote, customConnection);
		lote.setCdArquivo(arquivoCriado.getCdArquivo());
		loteRepository.update(lote, customConnection);
		loteImpressao.setStLote(LoteImpressaoSituacao.EM_IMPRESSAO);
		cartaJulgamentoService.save(loteImpressao, customConnection);
	}
	
	public String getEstado() throws Exception {
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Cidade cidade = cidadeRepository.get(orgao.getCdCidade());
		return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
	}
}
