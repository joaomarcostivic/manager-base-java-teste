package com.tivic.manager.ptc.protocolosv3.formularios;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoService;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.report.ReportCriterios;

public abstract class GeradorFormularioProtocolo {
	private ILoteNotificacaoService loteNotificacaoService;
	
	public GeradorFormularioProtocolo() throws Exception {
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
	}

	public abstract byte[] gerar() throws Exception;
	
	protected ReportCriterios montarReportCriterios() throws ValidacaoException {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE"));
		reportCriterios.addParametros("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO"));
		reportCriterios.addParametros("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO"));
		reportCriterios.addParametros("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO"));
		reportCriterios.addParametros("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP"));
		reportCriterios.addParametros("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO"));
		reportCriterios.addParametros("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO"));
		reportCriterios.addParametros("DS_ENDERECO", ParametroServices.getValorOfParametro("DS_ENDERECO"));
		reportCriterios.addParametros("NM_SUBORGAO", ParametroServices.getValorOfParametro("NM_SUBORGAO"));
		reportCriterios.addParametros("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO"));
		reportCriterios.addParametros("SG_UF", loteNotificacaoService.getEstadoOrgaoAutuador());
		return reportCriterios;
	}
}
