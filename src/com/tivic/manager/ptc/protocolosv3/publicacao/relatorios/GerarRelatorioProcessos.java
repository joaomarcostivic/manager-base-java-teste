package com.tivic.manager.ptc.protocolosv3.publicacao.relatorios;

import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.ptc.generators.IGeraNumeroEdital;
import com.tivic.manager.ptc.protocolosv3.publicacao.ProtocoloPublicacaoPendenteDto;
import com.tivic.manager.ptc.protocolosv3.publicacao.TipoParametroJulgamentoFactory;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;

public class GerarRelatorioProcessos  implements IGerarRelatorioProcessos {
	
	private IGeraNumeroEdital geraNumeroEdital;
	private IParametroRepository parametroRepository;
	
	public GerarRelatorioProcessos() throws Exception {
		this.geraNumeroEdital = (IGeraNumeroEdital) BeansFactory.get(IGeraNumeroEdital.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	@Override
	public byte[] gerar(String idRecurso, int stJulgamento, List<ProtocoloPublicacaoPendenteDto> publicaProtocoloDtoList) throws Exception {
		return printRelatorioProcessos(idRecurso, stJulgamento, publicaProtocoloDtoList).getReportDoc("ptc/" + selectJrxml(idRecurso));
	}
	
	private Report printRelatorioProcessos(String idRecurso, int stJulgamento, List<ProtocoloPublicacaoPendenteDto> publicaProtocoloDtoList) throws Exception {
		ReportCriterios reportCriterios = montarReportCriterios(idRecurso, stJulgamento, publicaProtocoloDtoList);
		Report report = new ReportBuilder()
				.list(publicaProtocoloDtoList)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}
	
	private String selectJrxml(String idRecurso) {
		if(TipoStatusEnum.RECURSO_JARI.getKey() == Integer.parseInt(idRecurso)) 
			return "relatorio_publicacao_protocolos_jari";
		if(TipoStatusEnum.DEFESA_PREVIA.getKey() == Integer.parseInt(idRecurso)) 
			return "relatorio_publicacao_protocolos_defesa";
		return "relatorio_publicacao_protocolos_defesa_advertencia";
	}
	
	private ReportCriterios montarReportCriterios(String idRecurso, int stJulgamento, List<ProtocoloPublicacaoPendenteDto> publicaProtocoloDtoList) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("QTD_PTC", publicaProtocoloDtoList.size());
		reportCriterios.addParametros("NM_RECURSO", TipoStatusEnum.valueOf(Integer.valueOf(idRecurso)) );
		reportCriterios.addParametros("PTC_DEFERIDOS", contarProtocolosDeferidos(publicaProtocoloDtoList));
		reportCriterios.addParametros("PTC_INDEFERIDOS",contarProtocolosIndeferidos(publicaProtocoloDtoList));
		reportCriterios.addParametros("MOB_PUBLICAR_JULGAMENTO", textoPublicacao(idRecurso, stJulgamento));
		reportCriterios.addParametros("NUMERO_EDITAL", getNumeroEdital(idRecurso, stJulgamento));
		return reportCriterios;
	}
	
	private String textoPublicacao(String idRecurso, int stJulgamento) {
		return new TipoParametroJulgamentoFactory().strategy(idRecurso, stJulgamento); 
	}
	
	private int contarProtocolosDeferidos(List<ProtocoloPublicacaoPendenteDto> publicaProtocoloDtoList) {
		int codSituacaoDeferido = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_DEFERIDO", 0);
		List<ProtocoloPublicacaoPendenteDto> protocolosDeferidos = publicaProtocoloDtoList.stream()
				.filter(item -> item.getCdSituacaoDocumento() == codSituacaoDeferido)
				.collect(Collectors.toList());
		return protocolosDeferidos.size();
	}
	
	private int contarProtocolosIndeferidos(List<ProtocoloPublicacaoPendenteDto> publicaProtocoloDtoList) {
		int codSituacaoIndeferido = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_INDEFERIDO", 0);
		List<ProtocoloPublicacaoPendenteDto> protocolosIndeferidos = publicaProtocoloDtoList.stream()
				.filter(item -> item.getCdSituacaoDocumento() == codSituacaoIndeferido)
				.collect(Collectors.toList());
		return  protocolosIndeferidos.size();
	}
	
	private String getNumeroEdital(String idRecurso, int stJulgamento) throws Exception {
        int stIndeferido = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_INDEFERIDO", 0);
		int stDeferido = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_DEFERIDO", 0);
		if(TipoStatusEnum.RECURSO_JARI.getKey() == Integer.parseInt(idRecurso) && stJulgamento == stDeferido) {
			return this.geraNumeroEdital.gerar(this.parametroRepository.getValorOfParametroAsInt("CD_TIPO_DOCUMENTO_PUBLICACAO_JARI_COM_PROVIMENTO"));
		}
		else if(TipoStatusEnum.RECURSO_JARI.getKey() == Integer.parseInt(idRecurso) && stJulgamento == stIndeferido) {
			return this.geraNumeroEdital.gerar(this.parametroRepository.getValorOfParametroAsInt("CD_TIPO_DOCUMENTO_PUBLICACAO_JARI_SEM_PROVIMENTO"));
		}
		else if(TipoStatusEnum.DEFESA_PREVIA.getKey() == Integer.parseInt(idRecurso) && stJulgamento == stDeferido ) {
			return this.geraNumeroEdital.gerar(this.parametroRepository.getValorOfParametroAsInt("CD_TIPO_DOCUMENTO_PUBLICACAO_DEFESA_DEFERIDA"));
		} 
		else if(TipoStatusEnum.DEFESA_PREVIA.getKey() == Integer.parseInt(idRecurso) && stJulgamento == stIndeferido) {
			return this.geraNumeroEdital.gerar(this.parametroRepository.getValorOfParametroAsInt("CD_TIPO_DOCUMENTO_PUBLICACAO_DEFESA_INDEFERIDA"));
		}
		if(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey() == Integer.parseInt(idRecurso) && stJulgamento == stDeferido) {
			return this.geraNumeroEdital.gerar(this.parametroRepository.getValorOfParametroAsInt("CD_TIPO_DOCUMENTO_PUBLICACAO_ADVERTENCIA_DEFERIDA"));
		}
		else if(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey() == Integer.parseInt(idRecurso) && stJulgamento == stIndeferido) {
			return this.geraNumeroEdital.gerar(this.parametroRepository.getValorOfParametroAsInt("CD_TIPO_DOCUMENTO_PUBLICACAO_ADVERTENCIA_INDEFERIDA"));
		}
		return null;
	}

}
