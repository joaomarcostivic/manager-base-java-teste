package com.tivic.manager.mob.lotes.impressao.arquivopostagem;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.lotes.dto.DadosDocumento;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.model.arquivo.LoteImpressaoArquivo;
import com.tivic.manager.mob.lotes.repository.arquivo.ILoteImpressaoArquivoRepository;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.util.date.DateUtil;

public class ArquivoListaPostagem  implements IArquivoListaPostagem {

	private CustomConnection customConnection;
	private DadosDocumento dadosDocumento;
	private IArquivoRepository arquivoRepository;
	private ILoteImpressaoArquivoRepository loteImpressaoArquivoRepository;
	private String nmArquivo;	
	private int tpNotificacao;
	
	public ArquivoListaPostagem(DadosDocumento dadosDocumento, int tpNotificacao, CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
		this.dadosDocumento = dadosDocumento;
		this.arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		this.loteImpressaoArquivoRepository = (ILoteImpressaoArquivoRepository) BeansFactory.get(ILoteImpressaoArquivoRepository.class);
		this.tpNotificacao = tpNotificacao;
	}
	
	@Override
	public Arquivo gerar() throws Exception {
		this.nmArquivo = nomearArquivo();
		byte[] arquivoListaPostagemBytes = printListaPostagem().getReportPdf("mob/lista_postagem_correios");
		Arquivo arquivoListaPostagem = new ArquivoBuilder()
				.setBlbArquivo(arquivoListaPostagemBytes)
				.setDtArquivamento(DateUtil.getDataAtual())
				.setDtCriacao(DateUtil.getDataAtual())
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
				.list(this.dadosDocumento.getNotificacoes())
				.build();
		return report;
	}
	
	private ReportCriterios montarReportCriterios() throws ValidacaoException {	
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("MOB_LOGO_CORREIOS_CHANCELA", ParametroServices.RecImg("MOB_LOGO_CORREIOS_CHANCELA", customConnection.getConnection()));
		reportCriterios.addParametros("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CONTRATO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_DEPARTAMENTO", ParametroServices.getValorOfParametro("NM_DEPARTAMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_CIDADE_ORGAO", OrgaoServices.getCidadeOrgaoAutuador().getNmCidade());
		reportCriterios.addParametros("SG_UF", OrgaoServices.getSgEstadoOrgaoAutuador());
		reportCriterios.addParametros("TP_ENVIO_CORREIOS", Integer.parseInt(tpNotificacao == TipoLoteImpressaoEnum.LOTE_NAI.getKey()? 
				ParametroServices.getValorOfParametro("MOB_TIPO_ENVIO_CORREIOS_NAI", customConnection.getConnection()):
				ParametroServices.getValorOfParametro("MOB_TIPO_ENVIO_CORREIOS_NIP", customConnection.getConnection())));
		reportCriterios.addParametros("TP_MODELO_ENVIO", Integer.parseInt(tpNotificacao == TipoLoteImpressaoEnum.LOTE_NAI.getKey()? 
				ParametroServices.getValorOfParametro("MOB_IMPRESSOS_TP_MODELO_NAI", customConnection.getConnection()): 
				ParametroServices.getValorOfParametro("MOB_IMPRESSOS_TP_MODELO_NIP", customConnection.getConnection())));
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
		LoteImpressaoArquivo loteImpressaoArquivo = new LoteImpressaoArquivo(this.dadosDocumento.getCdLoteNotificacao(), arquivoListaPostagem.getCdArquivo());
		loteImpressaoArquivoRepository.insert(loteImpressaoArquivo, this.customConnection);
	}
	
}
