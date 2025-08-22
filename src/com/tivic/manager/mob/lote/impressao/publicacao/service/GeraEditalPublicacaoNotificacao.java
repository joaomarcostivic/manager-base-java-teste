package com.tivic.manager.mob.lote.impressao.publicacao.service;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.lote.impressao.TipoLoteDocumentoEnum;
import com.tivic.manager.mob.lote.impressao.publicacao.dto.NotificacaoPublicacaoPendenteDto;
import com.tivic.manager.ptc.generators.IGeraNumeroEdital;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;

public class GeraEditalPublicacaoNotificacao {
	
	private IGeraNumeroEdital geraNumeroEdital;
	private IParametroRepository parametroRepository;
	
	public GeraEditalPublicacaoNotificacao() throws Exception {
		this.geraNumeroEdital = (IGeraNumeroEdital) BeansFactory.get(IGeraNumeroEdital.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	public byte[] gerar(int tpDocumento, List<NotificacaoPublicacaoPendenteDto> notificacaoPublicacaoPendenteDtos) throws Exception {
		return printRelatorio(tpDocumento, notificacaoPublicacaoPendenteDtos).getReportDoc("mob/relatorio_publicacao_notificacao");
	}
	
	private Report printRelatorio(int tpDocumento, List<NotificacaoPublicacaoPendenteDto> notificacaoPublicacaoPendenteDtos) throws Exception {
		ReportCriterios reportCriterios = montarReportCriterios(tpDocumento, notificacaoPublicacaoPendenteDtos);
		Report report = new ReportBuilder()
				.list(notificacaoPublicacaoPendenteDtos)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}
	
	private ReportCriterios montarReportCriterios(int tpDocumento, List<NotificacaoPublicacaoPendenteDto> notificacaoPublicacaoPendenteDtos) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("TOTAL_REGISTROS", notificacaoPublicacaoPendenteDtos.size());
		reportCriterios.addParametros("DS_TIPO_EDITAL", TipoLoteDocumentoEnum.valueOf(tpDocumento) );
		reportCriterios.addParametros("MOB_PUBLICAR_AITS", tpDocumento == TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NIP.getKey() 
				? ParametroServices.getValorOfParametro("MOB_PUBLICAR_NIPS") 
				: ParametroServices.getValorOfParametro("MOB_PUBLICAR_NAIS"));
		reportCriterios.addParametros("MOB_PUBLICAR_AITS_NAO_ENTREGUES", ParametroServices.getValorOfParametro("MOB_PUBLICAR_AITS_NAO_ENTREGUES"));
		reportCriterios.addParametros("DT_GERACAO", new SimpleDateFormat("dd/MM/yyyy").format(new GregorianCalendar().getTime()));
		reportCriterios.addParametros("NUMERO_EDITAL", getNumeroEdital(tpDocumento));
		return reportCriterios;
	}	
	
	private String getNumeroEdital(int tpDocumento) throws Exception {
        String nrEdital = TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NAI.getKey() == tpDocumento ? this.geraNumeroEdital.gerar(this.parametroRepository.getValorOfParametroAsInt("CD_TIPO_DOCUMENTO_PUBLICACAO_NA")) 
        		: this.geraNumeroEdital.gerar(this.parametroRepository.getValorOfParametroAsInt("CD_TIPO_DOCUMENTO_PUBLICACAO_NP"));
		return nrEdital;
	}

}
