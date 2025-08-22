package com.tivic.manager.mob.lotes.publicacao.strategy;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.lotes.dto.publicacao.NotificacaoPublicacaoPendenteDTO;
import com.tivic.manager.mob.lotes.enums.publicacao.TipoLotePublicacaoEnum;
import com.tivic.manager.ptc.generators.IGeraNumeroEdital;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;

public class EditalPublicacaoGenerator {
	private IGeraNumeroEdital geraNumeroEdital;
	private IParametroRepository parametroRepository;
	
	public EditalPublicacaoGenerator() throws Exception {
		this.geraNumeroEdital = (IGeraNumeroEdital) BeansFactory.get(IGeraNumeroEdital.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	public byte[] gerar(int tpPublicacao, List<NotificacaoPublicacaoPendenteDTO> notificacaoPublicacaoPendenteDtos) throws Exception {
	    return printRelatorio(tpPublicacao, notificacaoPublicacaoPendenteDtos).getReportDoc("mob/relatorio_publicacao_notificacao");
	}

	private Report printRelatorio(int tpPublicacao, List<NotificacaoPublicacaoPendenteDTO> notificacaoPublicacaoPendenteDtos) throws Exception {
	    ReportCriterios reportCriterios = montarReportCriterios(tpPublicacao, notificacaoPublicacaoPendenteDtos);
	    Report report = new ReportBuilder()
	        .list(notificacaoPublicacaoPendenteDtos)
	        .reportCriterios(reportCriterios)
	        .build();
	    return report;
	}

	private ReportCriterios montarReportCriterios(int tpPublicacao, List<NotificacaoPublicacaoPendenteDTO> notificacaoPublicacaoPendenteDtos) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("TOTAL_REGISTROS", notificacaoPublicacaoPendenteDtos.size());
		reportCriterios.addParametros("DS_TIPO_EDITAL", TipoLotePublicacaoEnum.valueOf(tpPublicacao) );
		reportCriterios.addParametros("MOB_PUBLICAR_AITS", tpPublicacao == TipoLotePublicacaoEnum.LOTE_PUBLICACAO_NIP.getKey() 
				? ParametroServices.getValorOfParametro("MOB_PUBLICAR_NIPS") 
				: ParametroServices.getValorOfParametro("MOB_PUBLICAR_NAIS"));
		reportCriterios.addParametros("MOB_PUBLICAR_AITS_NAO_ENTREGUES", ParametroServices.getValorOfParametro("MOB_PUBLICAR_AITS_NAO_ENTREGUES"));
		reportCriterios.addParametros("DT_GERACAO", new SimpleDateFormat("dd/MM/yyyy").format(new GregorianCalendar().getTime()));
		reportCriterios.addParametros("NUMERO_EDITAL", getNumeroEdital(tpPublicacao));
		return reportCriterios;
	}	
	
	private String getNumeroEdital(int tpPublicacao) throws Exception {
        String nrEdital = TipoLotePublicacaoEnum.LOTE_PUBLICACAO_NAI.getKey() == tpPublicacao ? this.geraNumeroEdital.gerar(this.parametroRepository.getValorOfParametroAsInt("CD_TIPO_DOCUMENTO_PUBLICACAO_NA")) 
        		: this.geraNumeroEdital.gerar(this.parametroRepository.getValorOfParametroAsInt("CD_TIPO_DOCUMENTO_PUBLICACAO_NP"));
		return nrEdital;
	}

}
