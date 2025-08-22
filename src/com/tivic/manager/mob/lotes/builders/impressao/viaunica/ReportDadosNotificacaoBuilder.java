package com.tivic.manager.mob.lotes.builders.impressao.viaunica;

import java.util.List;
import java.util.Locale;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.util.ContextManager;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.ReportCriterios;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import sol.util.ConfManager;

public class ReportDadosNotificacaoBuilder {
	
	private IOrgaoService orgaoService;
	private CidadeRepository cidadeRepository;
	private EstadoRepository estadoRepository;
    private ReportCriterios parametros;

	public ReportDadosNotificacaoBuilder(CustomConnection customConnection) throws Exception {
		this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
		this.cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
		this.estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
	    this.parametros = new ReportCriterios(); 
		setBaseReportCriterios(customConnection);
	}

    public ReportCriterios getParametros() {
        return parametros; 
    }
	
	public ReportDadosNotificacaoBuilder addParameter(String nmParametro, Object value) {
		parametros.addParametros(nmParametro, value);
		return this;
	}
	
	public ReportDadosNotificacaoBuilder addParameters(List<String> criterios, CustomConnection customConnection) {    	
        for (String criterio : criterios) {
        	addParameter(criterio, ParametroServices.getValorOfParametro(criterio, customConnection.getConnection()));
        }
    	return this;
    }

	private void setBaseReportCriterios(CustomConnection customConnection) throws Exception {
    	addParameter("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", customConnection.getConnection()))
		.addParameter("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", customConnection.getConnection()))
		.addParameter("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", customConnection.getConnection()))
		.addParameter("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", customConnection.getConnection()))
		.addParameter("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", customConnection.getConnection()))
		.addParameter("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", customConnection.getConnection()))
		.addParameter("NR_TELEFONE_2", ParametroServices.getValorOfParametro("NR_TELEFONE2", customConnection.getConnection()))
		.addParameter("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL", customConnection.getConnection()))
		.addParameter("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE", "LOREM IPSUM DOLOR SIT AMET", customConnection.getConnection()))
		.addParameter("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO", customConnection.getConnection()))
		.addParameter("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO", customConnection.getConnection()))
		.addParameter("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO", customConnection.getConnection()))
		.addParameter("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", "", customConnection.getConnection()).replaceAll("[^\\d]", ""))
		.addParameter("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CONTRATO", customConnection.getConnection()))
		.addParameter("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO", customConnection.getConnection()))
		.addParameter("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO", customConnection.getConnection()))
		.addParameter("MOB_CORREIOS_SG_CLIENTE", ParametroServices.getValorOfParametro("MOB_CORREIOS_SG_CLIENTE", customConnection.getConnection()))
		.addParameter("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO", customConnection.getConnection()))
		.addParameter("mob_local_impressao", ParametroServices.getValorOfParametro("mob_local_impressao", 0, customConnection.getConnection()))
		.addParameter("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", customConnection.getConnection()))
		.addParameter("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", ParametroServices.getValorOfParametro("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", customConnection.getConnection()))
		.addParameter("MOB_PRAZOS_NR_DEFESA_PREVIA", ParametroServices.getValorOfParametro("MOB_PRAZOS_NR_DEFESA_PREVIA", customConnection.getConnection()))
		.addParameter("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", ParametroServices.getValorOfParametro("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", customConnection.getConnection()))
		.addParameter("SG_UF", getEstado(customConnection))
		.addParameter("REPORT_LOCALE", new Locale("pt", "BR"));
    }
    
    private String getEstado(CustomConnection customConnection) throws Exception {
		Orgao orgao = this.orgaoService.getOrgaoUnico();
		Cidade cidade = this.cidadeRepository.get(orgao.getCdCidade(), customConnection);
		addParameter("ID_CIDADE", cidade.getIdCidade() != null ? cidade.getIdCidade() : null);
		return estadoRepository.get(cidade.getCdEstado(), customConnection).getSgEstado();
	}
    
    public void setParamsSubreport(List<?> dadosSubreport) throws Exception {
        ConfManager conf = ManagerConf.getInstance();
        String reportPath = conf.getProps().getProperty("REPORT_PATH");
        String path = ContextManager.getRealPath() + "/" + reportPath;
        parametros.addParametros("SUBREPORT_DIR", path + "//mob");
        parametros.addParametros("REPORT_LOCALE", new Locale("pt", "BR"));
        parametros.addParametros("SUBREPORT_NAMES", "mob//verso_notificacao_carta_simples_subreport");
        parametros.addParametros("SUBREPORT_DADOS_FIELDS", new JRBeanCollectionDataSource(dadosSubreport));
        parametros.addParametros("SUBREPORT_MAP_PARAMETERS", parametros.getParametros());
    }

}
