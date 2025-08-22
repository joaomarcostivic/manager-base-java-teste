package com.tivic.manager.ptc.portal.credencialestacionamento;

import java.util.List;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.ptc.protocolosv3.CartaoIdoso;
import com.tivic.manager.ptc.protocolosv3.factories.ProtocoloParametroFactory;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ImprimeCartaoEstacionamento {

	private CidadeRepository cidadeRepository;
    private EstadoRepository estadoRepository;
    private IOrgaoService orgaoService;
    private ProtocoloParametroFactory protocoloParametroFactory;

	public ImprimeCartaoEstacionamento() throws Exception {
		cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
    	estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
        orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
		protocoloParametroFactory = new ProtocoloParametroFactory();
	}
	
	public byte[] imprimir(int cdDocumento, String caminho, CustomConnection customConnection) throws Exception {
		List<CartaoIdoso> cartaoIdosoList = getBeneficiario(cdDocumento, customConnection);
		Report report = new ReportBuilder()
				.reportCriterios(setParametros(cartaoIdosoList.get(0), customConnection))
				.list(cartaoIdosoList)
				.build();
		return report.getReportPdf(caminho);
	}
	
	private List<CartaoIdoso> getBeneficiario(int cdDocumento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_documento", cdDocumento);
		Search<CartaoIdoso> search = new SearchBuilder<CartaoIdoso>("ptc_documento A")
				.fields("D.dt_ocorrencia, A.nr_documento, C.nm_pessoa, D.dt_ocorrencia + INTERVAL '5 year' AS dt_validade")
				.addJoinTable("JOIN ptc_documento_pessoa B ON (A.cd_documento = b.cd_documento)")
				.addJoinTable("JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa)")
				.addJoinTable("JOIN ptc_documento_ocorrencia D ON (A.cd_documento = D.cd_documento)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(CartaoIdoso.class);
	}
	
	private ReportCriterios setParametros(CartaoIdoso cartaoIdoso, CustomConnection customConnection) throws Exception {
		ReportCriterios report = new ReportCriterios();
		report.addParametros("DT_EMISSAO", cartaoIdoso.getDtOcorrencia().getTime());
		report.addParametros("DT_VALIDADE", cartaoIdoso.getDtValidade().getTime());
		report.addParametros("NM_EMISSOR", this.protocoloParametroFactory.getStrategy().getValorOfParametroAsString("PTC_NM_EMISSOR_RESPONSAVEL", customConnection));
		report.addParametros("NR_DOCUMENTO", cartaoIdoso.getNrDocumento());
		report.addParametros("LOGO_1", this.protocoloParametroFactory.getStrategy().recImageToString("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", customConnection));
		report.addParametros("NM_UF", getEstado());
		report.addParametros("NM_MUNICIPIO", this.protocoloParametroFactory.getStrategy().getValorOfParametroAsString("NM_CIDADE", customConnection));
		report.addParametros("NM_ORGAO_AUTUADOR", this.protocoloParametroFactory.getStrategy().getValorOfParametroAsString("NM_DEPARTAMENTO", customConnection));
		report.addParametros("NM_BENEFICIARIO", cartaoIdoso.getNmPessoa());
		return report;
	}
	
	private String getEstado() throws Exception {
        Orgao orgao = this.orgaoService.getOrgaoUnico();
        Cidade cidade = this.cidadeRepository.get(orgao.getCdCidade());
        return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
    }
}
