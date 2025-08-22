package com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.gpn.TipoDocumento;
import com.tivic.manager.gpn.tipodocumento.TipoDocumentoRepository;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoArquivo;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.builders.LoteImpressaoArquivoBuilder;
import com.tivic.manager.mob.lote.impressao.remessacorreios.DadosDocumento;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

public class ArquivoListaPostagem implements IArquivoListaPostagem {

	private CustomConnection customConnection;
	private DadosDocumento dadosDocumento;
	private IArquivoRepository arquivoRepository;
	private ILoteImpressaoArquivoRepository loteImpressaoArquivoRepository;
	private String nmArquivo;	
	private int tpNotificacao;
	private TipoDocumentoRepository tipoDocumentoRepository;
	
	public ArquivoListaPostagem(DadosDocumento dadosDocumento, int tpNotificacao, CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
		this.dadosDocumento = dadosDocumento;
		this.arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		this.loteImpressaoArquivoRepository = (ILoteImpressaoArquivoRepository) BeansFactory.get(ILoteImpressaoArquivoRepository.class);
		this.tpNotificacao = tpNotificacao;
		this.tipoDocumentoRepository = (TipoDocumentoRepository) BeansFactory.get(TipoDocumentoRepository.class);
	}
	
	@Override
	public Arquivo gerar() throws Exception {
		this.nmArquivo = nomearArquivo();
		byte[] arquivoListaPostagemBytes = printListaPostagem().getReportPdf("mob/lista_postagem_correios");
		Arquivo arquivoListaPostagem = new ArquivoBuilder()
				.setBlbArquivo(arquivoListaPostagemBytes)
				.setDtArquivamento(new GregorianCalendar())
				.setDtCriacao(new GregorianCalendar())
				.setNmDocumento("Arquivo de Lista de Postagem Correios")
				.setNmArquivo(this.nmArquivo)
				.setCdTipoArquivo(Integer.valueOf(ParametroServices.getValorOfParametro("MOB_TIPO_ARQUIVO_LISTA_POSTAGEM_CORREIOS")))
				.build();
		this.arquivoRepository.insert(arquivoListaPostagem, customConnection);
		vincularArquivoListaPostagem(arquivoListaPostagem);
		return arquivoListaPostagem;
	}
	
	private Report printListaPostagem() throws Exception {
		ReportCriterios reportCriterios = montarReportCriterios();
		Report report = new ReportBuilder()
				.reportCriterios(reportCriterios)
				.list(this.dadosDocumento.getDadosNotificacaoList())
				.build();
		return report;
	}
	
	private ReportCriterios montarReportCriterios() throws Exception {	
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("MOB_LOGO_CORREIOS_CHANCELA", ParametroServices.RecImg("MOB_LOGO_CORREIOS_CHANCELA", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CONTRATO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_DEPARTAMENTO", ParametroServices.getValorOfParametro("NM_DEPARTAMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_CIDADE_ORGAO", OrgaoServices.getCidadeOrgaoAutuador().getNmCidade());
		reportCriterios.addParametros("SG_UF", OrgaoServices.getSgEstadoOrgaoAutuador());
		reportCriterios.addParametros("TP_ENVIO_CORREIOS", Integer.parseInt(tpNotificacao == TipoLoteNotificacaoEnum.LOTE_NAI.getKey()? 
				ParametroServices.getValorOfParametro("MOB_TIPO_ENVIO_CORREIOS_NAI", customConnection.getConnection()):
				ParametroServices.getValorOfParametro("MOB_TIPO_ENVIO_CORREIOS_NIP", customConnection.getConnection())));
		reportCriterios.addParametros("TP_MODELO_ENVIO", Integer.parseInt(tpNotificacao == TipoLoteNotificacaoEnum.LOTE_NAI.getKey()? 
				ParametroServices.getValorOfParametro("MOB_IMPRESSOS_TP_MODELO_NAI", customConnection.getConnection()): 
				ParametroServices.getValorOfParametro("MOB_IMPRESSOS_TP_MODELO_NIP", customConnection.getConnection())));
		reportCriterios.addParametros("NR_DOCUMENTO", gerarNumero());
		return reportCriterios;
	}
		
	private String nomearArquivo() {
		String diaMes = DateUtil.formatDate(DateUtil.getDataAtual(), "'-'ddMMyyyy");
		String extensao = ".pdf";
		String nameFile = "LISTA_POSTAGEM_CORREIOS_LOTE_"
						+ dadosDocumento.getCdLoteNotificacao()
						+ diaMes
						+ extensao;
		return nameFile;
	}
	
	private void vincularArquivoListaPostagem(Arquivo arquivoListaPostagem) throws Exception {
		LoteImpressaoArquivo loteImpressaoArquivo = new LoteImpressaoArquivoBuilder()
				.setCdArquivo(arquivoListaPostagem.getCdArquivo())
				.setCdLoteImpressao(this.dadosDocumento.getCdLoteNotificacao())
				.build();
		loteImpressaoArquivoRepository.insert(loteImpressaoArquivo, this.customConnection);
	}
	
	public String gerarNumero() throws Exception {
		TipoDocumento tipoDocumento = getTipoDocumento(tpNotificacao);
		String prefixo = tipoDocumento.getIdPrefixoNumeracao() != null ? tipoDocumento.getIdPrefixoNumeracao() + "-" : "";
		int sequencial = incrementarNumeracaoDocumento(tipoDocumento);
		return mountNrDocumento(prefixo, sequencial, Calendar.getInstance());
	}
	
	private TipoDocumento getTipoDocumento(int tpNotificacao) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_tipo_documento", String.valueOf(tpNotificacao));
		TipoDocumento tipoDocumento = tipoDocumentoRepository.getTipoDocumento(searchCriterios);
		if (tipoDocumento == null) {
			throw new BadRequestException("Impossível buscar documento. Não existe nenhum documento com esse tipo.");
		}
		return tipoDocumento;
	}
	
	private int incrementarNumeracaoDocumento(TipoDocumento tipoDocumento) throws Exception {
		int sequencial = tipoDocumento.getNrUltimaNumeracao() > -1 ? tipoDocumento.getNrUltimaNumeracao() + 1 : 1;
		tipoDocumento.setNrUltimaNumeracao(sequencial);
		tipoDocumentoRepository.update(tipoDocumento, customConnection);
		return sequencial;
	}

	private String mountNrDocumento(String prefixo, Integer sequencial, Calendar calendar) {
		int ano = calendar.get(Calendar.YEAR);
		return prefixo + sequencial + "/" + ano;
	}
}
