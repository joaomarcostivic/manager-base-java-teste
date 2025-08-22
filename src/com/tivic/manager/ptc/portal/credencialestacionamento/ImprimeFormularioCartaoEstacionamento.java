package com.tivic.manager.ptc.portal.credencialestacionamento;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.ptc.protocolosv3.factories.ProtocoloParametroFactory;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;

public class ImprimeFormularioCartaoEstacionamento {
	
	private CidadeRepository cidadeRepository;
    private EstadoRepository estadoRepository;
    private IOrgaoService orgaoService;
    private ProtocoloParametroFactory protocoloParametroFactory;
    
	public ImprimeFormularioCartaoEstacionamento() throws Exception {
    	cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
    	estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
        orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
        protocoloParametroFactory = new ProtocoloParametroFactory();
	}

	public byte[] gerar(String caminho) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);
			byte[] formulario = imprimirFormulario(caminho, customConnection);
			customConnection.finishConnection();
			return formulario;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private byte[] imprimirFormulario(String caminho, CustomConnection customConnection) throws Exception {
		Report report = new ReportBuilder()
				.reportCriterios(setParametros(customConnection))
				.build();		
		return report.getReportPdf(caminho);
	}
	
	private ReportCriterios setParametros(CustomConnection customConnection) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", protocoloParametroFactory.getStrategy().getValorOfParametroAsString("DS_TITULO_1", customConnection));
		reportCriterios.addParametros("DS_TITULO_3", protocoloParametroFactory.getStrategy().getValorOfParametroAsString("DS_TITULO_3", customConnection));
		reportCriterios.addParametros("LOGO_1", protocoloParametroFactory.getStrategy().recImageToString("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", customConnection));
		reportCriterios.addParametros("LOGO_2", protocoloParametroFactory.getStrategy().recImageToString("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", customConnection));
		reportCriterios.addParametros("SG_UF", getEstado());
		reportCriterios.addParametros("NM_LOGRADOURO", protocoloParametroFactory.getStrategy().getValorOfParametroAsString("NM_LOGRADOURO", customConnection));
		reportCriterios.addParametros("NR_ENDERECO", protocoloParametroFactory.getStrategy().getValorOfParametroAsString("NR_ENDERECO", customConnection));
		reportCriterios.addParametros("NM_COMPLEMENTO", protocoloParametroFactory.getStrategy().getValorOfParametroAsString("NM_COMPLEMENTO", customConnection));
		reportCriterios.addParametros("NR_CEP", protocoloParametroFactory.getStrategy().getValorOfParametroAsString("NR_CEP", customConnection));
		reportCriterios.addParametros("NM_BAIRRO", protocoloParametroFactory.getStrategy().getValorOfParametroAsString("NM_BAIRRO", customConnection));
		reportCriterios.addParametros("NM_CIDADE", protocoloParametroFactory.getStrategy().getValorOfParametroAsString("NM_CIDADE", customConnection));
		reportCriterios.addParametros("NM_EMAIL", protocoloParametroFactory.getStrategy().getValorOfParametroAsString("NM_EMAIL", customConnection));
		reportCriterios.addParametros("NR_TELEFONE", protocoloParametroFactory.getStrategy().getValorOfParametroAsString("NR_TELEFONE", customConnection));
		reportCriterios.addParametros("SG_DEPARTAMENTO", protocoloParametroFactory.getStrategy().getValorOfParametroAsString("SG_DEPARTAMENTO", customConnection));
		return reportCriterios;
	}
	
	private String getEstado() throws Exception {
        Orgao orgao = this.orgaoService.getOrgaoUnico();
        Cidade cidade = this.cidadeRepository.get(orgao.getCdCidade());
        return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
    }
}
