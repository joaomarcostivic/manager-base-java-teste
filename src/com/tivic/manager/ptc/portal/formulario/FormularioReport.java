package com.tivic.manager.ptc.portal.formulario;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.BadRequestException;

import org.apache.commons.lang3.StringUtils;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.mob.orgaoaquivo.IOrgaoArquivoService;
import com.tivic.manager.mob.orgaoaquivo.OrgaoArquivoDTO;
import com.tivic.manager.mob.orgaoaquivo.enums.TipoArquivoDocumento;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;

public class FormularioReport {
	private CidadeRepository cidadeRepository;
    private EstadoRepository estadoRepository;
    private IOrgaoService orgaoService;
    private IOrgaoArquivoService orgaoArquivoService;
    private Map<Integer, String> caminhoFormulario;

    public FormularioReport() throws Exception {
    	cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
    	estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
        orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
        orgaoArquivoService = (IOrgaoArquivoService) BeansFactory.get(IOrgaoArquivoService.class);
        inicializarHashFormulario();
        
    }
	
	public byte[] gerar(int tpDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			byte[] formulario = imprimirFormulario(tpDocumento, customConnection);
			customConnection.finishConnection();
			return formulario;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private byte[] imprimirFormulario(int tpDocumento, CustomConnection customConnection) throws Exception {
		boolean isOptante = parametroImpressao();
		return isOptante && tpDocumento == TipoDocumentoProtocoloEnum.CARTAO_PCD.getKey() ? getFormularioPasta() : getFormularioSistema(tpDocumento, customConnection);
	}
	
	private boolean parametroImpressao() throws ValidacaoException {
		int habilitaImpressao = ParametroServices.getValorOfParametroAsInteger("LG_HABILITAR_IMPRESSAO_ORGAO", -1);
		if(habilitaImpressao == -1) {
			throw new ValidacaoException("O parâmetro LG_HABILITAR_IMPRESSAO_ORGAO não foi configurado. Entre em contato com o suporte.");
		}
		return habilitaImpressao == 1;
	}
	
	private byte[] getFormularioPasta() throws Exception {
		OrgaoArquivoDTO orgaoArquivoDTO = orgaoArquivoService.getCaminhoPorTipoDocumento(TipoArquivoDocumento.CARTAO_PCD.getKey());
		if(StringUtils.isEmpty(orgaoArquivoDTO.getTxtCaminhoArquivo())) {
			throw new BadRequestException("O caminho não foi encontrado!");
		}
		Path path = Paths.get(orgaoArquivoDTO.getTxtCaminhoArquivo(), orgaoArquivoDTO.getNmArquivo());
		if (!Files.exists(path)) {
	        throw new FileNotFoundException("Arquivo não encontrado no caminho: " + path.toString());
	    }

	    return Files.readAllBytes(path);
		
	}
	
	private byte[] getFormularioSistema(int tpDocumento, CustomConnection customConnection) throws Exception {
		Report report = new ReportBuilder()
				.reportCriterios(setParametros(customConnection))
				.list(setFormulario(tpDocumento))
				.build();
		return report.getReportPdf(caminhoFormulario.get(tpDocumento));
	}

	private void inicializarHashFormulario() {
		caminhoFormulario = new HashMap<>();
		caminhoFormulario.put(TipoDocumentoProtocoloEnum.APRESENTACAO_CONDUTOR.getKey(), "mob/formulario_apresentacao_condutor");
		caminhoFormulario.put(TipoDocumentoProtocoloEnum.CARTAO_IDOSO.getKey(), "mob/formulario_cartao_idoso");
		caminhoFormulario.put(TipoDocumentoProtocoloEnum.CARTAO_PCD.getKey(), "ptc/formulario_cartao_pcd");
		caminhoFormulario.put(TipoDocumentoProtocoloEnum.DEFESA_PREVIA.getKey(), "mob/formulario_defesa_previa");
		caminhoFormulario.put(TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey(), "mob/formulario_defesa_previa");
		caminhoFormulario.put(TipoDocumentoProtocoloEnum.RECURSO_CETRAN.getKey(), "mob/formulario_defesa_previa");
	}
		
	private ReportCriterios setParametros(CustomConnection customConnection) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1",  ParametroServices.getValorOfParametro("DS_TITULO_1", customConnection.getConnection()));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", customConnection.getConnection()));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("SG_UF", getEstado());
		reportCriterios.addParametros("NM_LOGRADOURO",  ParametroServices.getValorOfParametro("NM_LOGRADOURO", customConnection.getConnection()));
		reportCriterios.addParametros("NR_ENDERECO",  ParametroServices.getValorOfParametro("NR_ENDERECO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_COMPLEMENTO",  ParametroServices.getValorOfParametro("NM_COMPLEMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("NR_CEP",  ParametroServices.getValorOfParametro("NR_CEP", customConnection.getConnection()).replaceAll("[^\\d]", ""));
		reportCriterios.addParametros("NM_BAIRRO",  ParametroServices.getValorOfParametro("NM_BAIRRO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_CIDADE",  ParametroServices.getValorOfParametro("NM_CIDADE", customConnection.getConnection()));
		reportCriterios.addParametros("NM_EMAIL",  ParametroServices.getValorOfParametro("NM_EMAIL", customConnection.getConnection()));
		return reportCriterios;
	}
	
	private List<FormularioDTO> setFormulario(int tpDocumento) {
		List<FormularioDTO> formularioList = new ArrayList<FormularioDTO>();
		FormularioDTO formulario = new FormularioDTO();
		formulario.setTpDocumento(tpDocumento);
		formularioList.add(formulario);
		return formularioList;
	}
	
	private String getEstado() throws Exception {
        Orgao orgao = this.orgaoService.getOrgaoUnico();
        Cidade cidade = this.cidadeRepository.get(orgao.getCdCidade());
        return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
    }
}
