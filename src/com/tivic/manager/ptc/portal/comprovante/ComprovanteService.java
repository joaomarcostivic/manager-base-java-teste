package com.tivic.manager.ptc.portal.comprovante;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;

public class ComprovanteService implements IComprovanteService {
	
	private ParametroRepositoryDAO parametroRepositoryDAO;
	private CidadeRepository cidadeRepository;
    private EstadoRepository estadoRepository;
    private IOrgaoService orgaoService;
	
	public ComprovanteService() throws Exception {
		cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
    	estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
        orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
	}
	
	@Override
	public byte[] imprimirComprovante(ProtocoloDTO protocolo, String referer) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			byte[] comprovante = imprimirComprovante(protocolo, referer, customConnection);
			customConnection.finishConnection();
			return comprovante;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public byte[] imprimirComprovante(ProtocoloDTO protocolo, String referer, CustomConnection customConnection) throws Exception {
				
		Report report = new ReportBuilder()
				.reportCriterios(setParametros(protocolo, referer, customConnection))
				.list(setComprovante(protocolo.getDocumento().getNrDocumento()))
				.build();
		return report.getReportPdf("mob/comprovante-portal");		
	}
	
	@Override
	public byte[] imprimirComprovanteCartaoIdoso(String nrDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			byte[] formulario = imprimirComprovanteCartaoIdoso(nrDocumento, customConnection);
			customConnection.finishConnection();
			return formulario;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public byte[] imprimirComprovanteCartaoIdoso(String nrDocumento, CustomConnection customConnection) throws Exception {
		List<Documento> documentos = new ArrayList<Documento>();
		Documento documento =  new Documento();
		documentos.add(documento);
		Report report = new ReportBuilder()
				.reportCriterios(setParametrosToBaseNova(nrDocumento, customConnection))
				.list(documentos)
				.build();		
		return report.getReportPdf("mob/comprovante-portal-cartao-idoso");
	}
	
	private ReportCriterios setParametrosToBaseNova(String nrDocumento, CustomConnection customConnection) throws Exception {
		parametroRepositoryDAO = new ParametroRepositoryDAO();
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", parametroRepositoryDAO.getValorOfParametroAsString("DS_TITULO_1", customConnection));
		reportCriterios.addParametros("DS_TITULO_2", parametroRepositoryDAO.getValorOfParametroAsString("NM_ORGAO_AUTUADOR", customConnection));
		reportCriterios.addParametros("DS_TITULO_3", parametroRepositoryDAO.getValorOfParametroAsString("SG_ORGAO", customConnection));
		reportCriterios.addParametros("LOGO_1", parametroRepositoryDAO.recImageToString("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", customConnection));
		reportCriterios.addParametros("LOGO_2", parametroRepositoryDAO.recImageToString("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", customConnection));
		reportCriterios.addParametros("NR_PROTOCOLO", nrDocumento);
		reportCriterios.addParametros("NM_EMAIL", parametroRepositoryDAO.getValorOfParametroAsString("NM_EMAIL", customConnection));
		reportCriterios.addParametros("SG_UF", getEstado());
		reportCriterios.addParametros("NM_LOGRADOURO",  parametroRepositoryDAO.getValorOfParametroAsString("NM_LOGRADOURO", customConnection));
		reportCriterios.addParametros("NR_ENDERECO",  parametroRepositoryDAO.getValorOfParametroAsString("NR_ENDERECO", customConnection));
		reportCriterios.addParametros("NM_COMPLEMENTO",  parametroRepositoryDAO.getValorOfParametroAsString("NM_COMPLEMENTO", customConnection));
		reportCriterios.addParametros("NR_CEP",  parametroRepositoryDAO.getValorOfParametroAsString("NR_CEP", customConnection));
		reportCriterios.addParametros("NM_BAIRRO",  parametroRepositoryDAO.getValorOfParametroAsString("NM_BAIRRO", customConnection));
		return reportCriterios;
	}
	
	private ReportCriterios setParametros(ProtocoloDTO protocolo, String referer, CustomConnection customConnection) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1",  ParametroServices.getValorOfParametro("DS_TITULO_1", customConnection.getConnection()));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", customConnection.getConnection()));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", customConnection.getConnection()));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("NR_TELEFONE",  ParametroServices.getValorOfParametro("NR_TELEFONE", customConnection.getConnection()));
		reportCriterios.addParametros("NR_PROTOCOLO", protocolo.getDocumento().getNrDocumento());
		reportCriterios.addParametros("TP_DOCUMENTO", TipoDocumentoProtocoloEnum.valueOf(protocolo.getTipoDocumento().getCdTipoDocumento()));
		reportCriterios.addParametros("DS_ORIGEM", referer);
		reportCriterios.addParametros("DS_MENSAGEM_RECURSO_PORTAL", ParametroServices.getValorOfParametro("DS_MENSAGEM_RECURSO_PORTAL", customConnection.getConnection()));
		reportCriterios.addParametros("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL", customConnection.getConnection()));
		reportCriterios.addParametros("EMAIL_SOLICITANTE", protocolo.getEmailSolicitante());
		reportCriterios.addParametros("CPF_SOLICITANTE", protocolo.getCpfSolicitante());
		reportCriterios.addParametros("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO", customConnection.getConnection()));
		reportCriterios.addParametros("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO", customConnection.getConnection()));
		reportCriterios.addParametros("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", customConnection.getConnection()));
		reportCriterios.addParametros("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE", "LOREM IPSUM DOLOR SIT AMET", customConnection.getConnection()));
		reportCriterios.addParametros("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("NR_TELEFONE_2", ParametroServices.getValorOfParametro("NR_TELEFONE2", customConnection.getConnection()));
		return reportCriterios;
	}
	
	private List<DadosComprovanteProtocolo> setComprovante(String nrProtocolo) {
		List<DadosComprovanteProtocolo> comprovanteList = new ArrayList<DadosComprovanteProtocolo>();
		DadosComprovanteProtocolo comprovante = new DadosComprovanteProtocolo();
		comprovante.setNrProtocolo(nrProtocolo);
		comprovanteList.add(comprovante);
		return comprovanteList;
	}	
	
	private String getEstado() throws Exception {
        Orgao orgao = this.orgaoService.getOrgaoUnico();
        Cidade cidade = this.cidadeRepository.get(orgao.getCdCidade());
        return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
    }
}
