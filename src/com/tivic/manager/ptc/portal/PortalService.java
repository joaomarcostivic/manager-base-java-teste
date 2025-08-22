package com.tivic.manager.ptc.portal;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.util.Util;
import org.apache.commons.lang3.StringUtils;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAO;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitReportServices;
import com.tivic.manager.mob.lotes.impressao.strategy.viaunica.DocumentGeneratorSegundaViaNai;
import com.tivic.manager.mob.lotes.impressao.strategy.viaunica.DocumentGeneratorSegundaViaNip;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.manager.ptc.emailsender.InfMailSender;
import com.tivic.manager.ptc.portal.builders.ListAitResponseBuilder;
import com.tivic.manager.ptc.portal.builders.ParametroContatoResponseBuilder;
import com.tivic.manager.ptc.portal.builders.ProtocoloAitResponseBuilder;
import com.tivic.manager.ptc.portal.cetran.CetranPortalService;
import com.tivic.manager.ptc.portal.credencialestacionamento.BuscaInfEmail;
import com.tivic.manager.ptc.portal.credencialestacionamento.DatabaseConnectionManager;
import com.tivic.manager.ptc.portal.credencialestacionamento.ICredencialEstacionamentoService;
import com.tivic.manager.ptc.portal.credencialestacionamento.ImprimeFormularioCartaoEstacionamento;
import com.tivic.manager.ptc.portal.credencialestacionamento.validations.SolicitacaoCredencialEstacionamentoValidation;
import com.tivic.manager.ptc.portal.defesaprevia.DefesaPreviaPortalService;
import com.tivic.manager.ptc.portal.fici.FiciPortalService;
import com.tivic.manager.ptc.portal.formulario.FormularioReport;
import com.tivic.manager.ptc.portal.impressaoait.IAndamentoAitReport;
import com.tivic.manager.ptc.portal.jari.JariPortalService;
import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.manager.ptc.portal.request.DocumentoPortalRequest;
import com.tivic.manager.ptc.portal.response.AitProtocoloResponse;
import com.tivic.manager.ptc.portal.response.AitResponse;
import com.tivic.manager.ptc.portal.response.AndamentoAitResponse;
import com.tivic.manager.ptc.portal.response.DocumentoPortalResponse;
import com.tivic.manager.ptc.portal.response.ParametroContatoResponse;
import com.tivic.manager.ptc.portal.response.ParametroInstrucaoResponse;
import com.tivic.manager.ptc.portal.response.ParametroNmOrgaoResponse;
import com.tivic.manager.ptc.portal.response.ParametroValorResponse;
import com.tivic.manager.ptc.portal.response.SegundaViaNotificacaoResponse;
import com.tivic.manager.ptc.portal.response.TipoArquivoResponse;
import com.tivic.manager.ptc.portal.vagaespecial.ProtocoloSolicitacaoDTO;
import com.tivic.manager.ptc.portal.vagaespecial.VagaEspecialPortalService;
import com.tivic.manager.str.ait.veiculo.ConvertPlaca;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class PortalService implements IPortalService {
	
	private IAndamentoAitReport andamentoAitReport;
    private IParametroRepository parametroRepository;
    private ParametroRepositoryDAO parametroCustomDb;
	private ICredencialEstacionamentoService credencialEstacionamentoService;
	private ILoteImpressaoService loteImpressaoService;

	public PortalService() throws Exception {
		this.andamentoAitReport = (IAndamentoAitReport) BeansFactory.get(IAndamentoAitReport.class);
        this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
        credencialEstacionamentoService = (ICredencialEstacionamentoService) BeansFactory.get(ICredencialEstacionamentoService.class);
        parametroCustomDb = new ParametroRepositoryDAO();
        loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
	}

	@Override
	public List<AitResponse> findAit(String nrPlaca, SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			String nrPlacaConvertida = ConvertPlaca.convertPlaca(nrPlaca);
			List<AitResponse> aitList = findAit(nrPlaca, nrPlacaConvertida, searchCriterios, customConnection);
			customConnection.finishConnection();
			return aitList;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<AitResponse> findAit(String nrPlaca, String nrPlacaConvertida, SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitResponse> search = new SearchBuilder<AitResponse>("mob_ait A")
				.fields("A.cd_ait, A.id_ait, A.tp_status, A.nr_placa, A.nr_renavan, A.dt_prazo_defesa, A.dt_infracao")
				.searchCriterios(searchCriterios)
				.additionalCriterias("nr_placa IN (" + "'" + nrPlaca + "'" + ", " + "'" + nrPlacaConvertida + "'" + ")")
				.customConnection(customConnection)
				.orderBy("A.dt_infracao DESC")
				.build();
		List<AitResponse> aits = search.getList(AitResponse.class);
		if (aits.isEmpty()) {
			throw new NoContentException("Nenhum AIT encontrado");
		}
		return new ListAitResponseBuilder(aits).build();
	}
	
	@Override
	public TipoArquivoResponse findTipoArquivo() throws ValidationException {
		TipoArquivoResponse tipos = new TipoArquivoResponse();
		tipos.setTpCnh(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_CNH", 0));
		tipos.setTpRg(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_RG", 0));
		tipos.setTpPermissao(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_PERMISSAO", 0));
		tipos.setTpCpf(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_CPF", 0));
		tipos.setTpComprovanteResidencia(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_COMPROVANTE_RESIDENCIA", 0));
		tipos.setTpCrlv(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_CRLV", 0));
		tipos.setTpFormularioDefesa(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_FORMULARIO_DEFESA", 0));
		tipos.setTpNai(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_NAI", 0));
		tipos.setTpFormularioApresentacao(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_FORMULARIO_APRESENTACAO", 0));
		tipos.setTpCartao(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_CARTAO", 0));
		tipos.setTpComprovanteMulta(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_COMPROVANTE_MULTA", 0));
		tipos.setTpResultadoJulgamento(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_RESULTADO_JULGAMENTO", 0));
		tipos.setTpProcuracao(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_PROCURACAO", 0));
		tipos.setTpContratoSocial(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_CONTRATO_SOCIAL", 0));
		tipos.setTpCtps(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_CTPS", 0));
		tipos.setTpIdentidadeProfissional(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_ID_PROFISSIONAL", 0));
		tipos.setTpFormularioVagaEspecial(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_FORMULARIO_VAGA_ESPECIAL", 0));
		tipos.setTpRelatorioMedico(ParametroServices.getValorOfParametroAsInteger("TP_ARQUIVO_RELATORIO_MEDICO", 0));
		return tipos;
	}

	@Override
	public SegundaViaNotificacaoResponse gerarSegundaViaNotificacao(int cdAit, int tpStatus) throws Exception {
		switch (tpStatus) {
		case AitMovimentoServices.NAI_ENVIADO:
			return gerarSegundaViaNAI(cdAit);
		case AitMovimentoServices.NIP_ENVIADA:
			return gerarSegundaViaNIP(cdAit);
		default:
			throw new ValidacaoException("Tipo de notificação não encontrada.");
		}
	}

	private SegundaViaNotificacaoResponse gerarSegundaViaNAI(int cdAit) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Boolean printPortal = true;
			SegundaViaNotificacaoResponse printNAI = new SegundaViaNotificacaoResponse();
			printNAI.setArquivo(new DocumentGeneratorSegundaViaNai().gerarDocumentos(cdAit, printPortal, customConnection));
			customConnection.finishConnection();
			return printNAI;
		} finally {
			customConnection.closeConnection();
		}
	}

	private SegundaViaNotificacaoResponse gerarSegundaViaNIP(int cdAit) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			SegundaViaNotificacaoResponse printNIP = new SegundaViaNotificacaoResponse();
			AitReportServices aitReportService = new AitReportServices();
			Boolean nipVencida = aitReportService.verificarVencimentoNip(cdAit);
			Boolean printPortal = true;
			if(nipVencida) {
				printNIP.setArquivo(loteImpressaoService.gerarNotificacaoNipComJuros(cdAit, printPortal));
				return printNIP;
			}
			printNIP.setArquivo(new DocumentGeneratorSegundaViaNip().gerarDocumentos(cdAit, printPortal, customConnection));
			customConnection.finishConnection();
			return printNIP;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<CidadeDTO> getCidades(String nmCidade) throws Exception {
		String cidadeSemAcento = RemoveAcentos(nmCidade);
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosLikeAnyString("nm_cidade", cidadeSemAcento, cidadeSemAcento != null);
		searchCriterios.setQtLimite(10);
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<CidadeDTO> cidadeList = getCidades(searchCriterios, customConnection);
			if (cidadeList.isEmpty()) {
				new Exception("Nenhuma cidade foi encontrada.");
			}
			customConnection.finishConnection();
			return cidadeList;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private String RemoveAcentos(String nmCidade) {
	    return StringUtils.stripAccents(nmCidade);
	}

	@Override
	public List<CidadeDTO> getCidades(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<CidadeDTO> search = new SearchBuilder<CidadeDTO>("grl_cidade A")
				.fields("A.nm_cidade, B.sg_estado AS nm_uf, A.cd_cidade")
				.addJoinTable("INNER JOIN grl_estado B ON (A.cd_estado = B.cd_estado)")
				.orderBy("A.nm_cidade")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();

		if(Util.isStrBaseAntiga()) {
			search = new SearchBuilder<CidadeDTO>("municipio A")
					.fields("A.nm_cidade, B.sg_estado AS nm_uf, A.cd_cidade")
					.addJoinTable("INNER JOIN grl_estado B ON (A.cd_estado = B.cd_estado)")
					.orderBy("A.nm_cidade")
					.searchCriterios(searchCriterios)
					.customConnection(customConnection)
					.build();
		}

		List<CidadeDTO> cidadeList = search.getList(CidadeDTO.class);
		return cidadeList;
	}

    @Override
    public DocumentoPortalResponse solicitarDefesaPrevia(DocumentoPortalRequest documentoRecurso) throws Exception {
        CustomConnection customConnection = new CustomConnection();
        try {
            customConnection.initConnection(true);
            DocumentoPortalResponse defesa = new DefesaPreviaPortalService().solicitar(documentoRecurso, customConnection);
            customConnection.finishConnection();
            return defesa;
        } finally {
            customConnection.closeConnection();
        }
    }
    
    @Override
    public DocumentoPortalResponse solicitarCartaoIdoso(DocumentoPortalRequest documentoSolicitacao) throws Exception {
    	CustomConnection customConnection = new CustomConnection();
    	try {
    		customConnection.initConnection(true);
    		DocumentoPortalResponse solicitacao = new VagaEspecialPortalService().solicitarCartaoIdoso(documentoSolicitacao, customConnection);
    		customConnection.finishConnection();
    		return solicitacao;
    	} finally {
    		customConnection.closeConnection();
    	}
    }
    
    @Override
    public DocumentoPortalResponse solicitarCartaoPcd(DocumentoPortalRequest documentoSolicitacao) throws Exception {
    	CustomConnection customConnection = new CustomConnection();
    	try {
    		customConnection.initConnection(true);
    		DocumentoPortalResponse solicitacao = new VagaEspecialPortalService().solicitarCartaoPcd(documentoSolicitacao, customConnection);
    		customConnection.finishConnection();
    		return solicitacao;
    	} finally {
    		customConnection.closeConnection();
    	}
    }
    
    @Override
    public List<ProtocoloSolicitacaoDTO> findSolicitacoes(SearchCriterios searchCriterios) throws Exception {
    	CustomConnection customConnection = new CustomConnection();
    	try {
    		customConnection.initConnection(false);
    		Search<ProtocoloSolicitacaoDTO> solicitacoesSearch = searchSolicitacoes(searchCriterios, customConnection);
    		customConnection.finishConnection();
    		return solicitacoesSearch.getList(ProtocoloSolicitacaoDTO.class);
    	} finally {
    		customConnection.closeConnection();
    	}
    }
    
    private Search<ProtocoloSolicitacaoDTO> searchSolicitacoes(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
    	Search<ProtocoloSolicitacaoDTO> search = new SearchBuilder<ProtocoloSolicitacaoDTO>("ptc_documento D")
    			.fields("D.*, B.nr_cpf, C.nm_tipo_documento, E.nm_situacao_documento, F.dt_ocorrencia + INTERVAL '5 year' AS dt_prazo, F.txt_ocorrencia ")
    			.addJoinTable(" LEFT JOIN ptc_documento_pessoa A ON(D.cd_documento = A.cd_documento) ")
    			.addJoinTable(" LEFT JOIN grl_pessoa_fisica B ON(A.cd_pessoa = B.cd_pessoa)")
    			.addJoinTable(" LEFT JOIN gpn_tipo_documento C ON (D.cd_tipo_documento = C.cd_tipo_documento)")
    			.addJoinTable(" LEFT JOIN ptc_situacao_documento E ON(D.cd_situacao_documento = E.cd_situacao_documento)")
    			.addJoinTable(" LEFT JOIN ptc_documento_ocorrencia F ON(D.cd_documento = F.cd_documento)")
    			.searchCriterios(searchCriterios)
    			.customConnection(customConnection)
    			.orderBy("D.dt_protocolo DESC")
			.build();
    	return search;
    }
    
    @Override
    public List<Arquivo> findAnexos(SearchCriterios searchCriterios) throws Exception {
    	CustomConnection customConnection = new CustomConnection();
    	try {
    		customConnection.initConnection(false);
    		Search<Arquivo> anexosSearch = searchAnexos(searchCriterios, customConnection);
    		customConnection.finishConnection();
    		return anexosSearch.getList(Arquivo.class);
    	} finally {
    		customConnection.closeConnection();
    	}
    }
    
    private Search<Arquivo> searchAnexos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
    	Search<Arquivo> search = new SearchBuilder<Arquivo>("ptc_documento_arquivo D")
    			.fields("A.* ")
    			.addJoinTable(" LEFT JOIN grl_arquivo A ON(D.cd_arquivo = A.cd_arquivo) ")
    			.searchCriterios(searchCriterios)
    			.customConnection(customConnection)
			.build();
    	return search;
    }
    
	@Override
	public DocumentoPortalResponse solicitarFici(DocumentoPortalRequest documentoRecurso) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			DocumentoPortalResponse documentoPortalResponse = new FiciPortalService().solicitar(documentoRecurso, customConnection);
			customConnection.finishConnection();
			return documentoPortalResponse;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public DocumentoPortalResponse solicitarJari(DocumentoPortalRequest documentoRecurso) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			DocumentoPortalResponse documentoPortalResponse = new JariPortalService().solicitar(documentoRecurso, customConnection);
			customConnection.finishConnection();
			return documentoPortalResponse;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public DocumentoPortalResponse solicitarCetran(DocumentoPortalRequest documentoRecurso) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			DocumentoPortalResponse documentoPortalResponse = new CetranPortalService().solicitar(documentoRecurso, customConnection);
			customConnection.finishConnection();
			return documentoPortalResponse;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public AndamentoAitResponse gerarImpressaoAit(int cdAit) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			AndamentoAitResponse andamentoAitReport = this.andamentoAitReport.imprimir(cdAit, customConnection);
			customConnection.finishConnection();
			return andamentoAitReport;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public byte[] imprimirFormulario(int tpDocumento) throws Exception {
		byte[] formulario = new FormularioReport().gerar(tpDocumento);
		return formulario;
	}
	
	@Override
	public AitProtocoloResponse findAitProtocolo(String nrPlaca, SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			String nrPlacaConvertida = ConvertPlaca.convertPlaca(nrPlaca);
			AitProtocoloResponse aitList = findAitProtocolo(nrPlaca, nrPlacaConvertida, searchCriterios, customConnection);
			customConnection.finishConnection();
			return aitList;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public AitProtocoloResponse findAitProtocolo(String nrPlaca, String nrPlacaConvertida, SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitProtocoloResponse> search = new SearchBuilder<AitProtocoloResponse>("mob_ait A")
				.fields("A.cd_ait, A.id_ait, A.tp_status, A.nr_placa, A.nr_renavan, A.dt_prazo_defesa,"
						+ " A.nm_proprietario, A.nr_cpf_cnpj_proprietario,"
						+ " A.nr_telefone, A.nr_cep, A.ds_logradouro,A.ds_nr_imovel,"
						+ " A.nm_complemento, A.dt_infracao, A.cd_cidade, A.cd_bairro, A.dt_vencimento, B.nm_cidade, C.nm_bairro")
				.addJoinTable("LEFT OUTER JOIN grl_cidade B ON (A.cd_Cidade = B.cd_Cidade)")
				.addJoinTable("LEFT OUTER JOIN grl_bairro C ON (A.cd_Bairro = C.cd_Bairro)")
				.searchCriterios(searchCriterios)
				.additionalCriterias("nr_placa IN (" + "'" + nrPlaca + "'" + ", " + "'" + nrPlacaConvertida + "'" + ")")
				.customConnection(customConnection)
				.orderBy("A.dt_infracao DESC")
				.build();
		List<AitProtocoloResponse> aitProtocoloList = search.getList(AitProtocoloResponse.class);
		
		if (aitProtocoloList.isEmpty()) {
			throw new NoContentException("Nenhum AIT encontrado");
		}
		return new ProtocoloAitResponseBuilder(aitProtocoloList.get(0)).build();
	}
	
	@Override
	public byte[] buscarImagem(String nmParametro) throws Exception {
		return parametroRepository.getValorOfParametroImageAsBytes(nmParametro);
	}
	
	@Override
	public ParametroInstrucaoResponse buscarInstrucoes(String nmParametro) throws Exception {
		ParametroInstrucaoResponse instrucoes = new ParametroInstrucaoResponse();
		instrucoes.setTxtInstrucao(ParametroServices.getValorOfParametro(nmParametro));
		return instrucoes;
	}
	
	@Override
	public ParametroContatoResponse buscarContato() throws Exception {
		return new ParametroContatoResponseBuilder().builder();
	}
	
	@Override
	public byte[] imprimirFormularioCartaoIdoso() throws Exception {
		String caminho = "ptc/formulario_cartao_idoso";
		byte[] formulario = new ImprimeFormularioCartaoEstacionamento().gerar(caminho);
		return formulario;
	}
	
	@Override
	public DocumentoPortalResponse solicitarCredencialEstacionamento(CartaoEstacionamentoRequest documentoRecurso) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, true);	
			new SolicitacaoCredencialEstacionamentoValidation(documentoRecurso).validate(customConnection);
			DocumentoPortalResponse documentoPortal = credencialEstacionamentoService.solicitar(documentoRecurso, customConnection);
			customConnection.finishConnection();
			new InfMailSender(new BuscaInfEmail().buscar(documentoPortal.getCdDocumento(), "PTC_TEMPLATE_COMPROVANTE_CREDENCIAL_DEFICIENTE")).send();
			return documentoPortal;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public byte[] getCartaoIdoso(int cdDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);	
			byte[] cartao = credencialEstacionamentoService.getCartaoIdoso(cdDocumento, customConnection);
			customConnection.finishConnection();
			return cartao;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public byte[] getCartaoPcd(int cdDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);	
			byte[] cartao = credencialEstacionamentoService.getCartaoPcd(cdDocumento, customConnection);
			customConnection.finishConnection();
			return cartao;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public byte[] imprimirFormularioCartaoPcd() throws Exception {
		String caminho = "ptc/formulario_cartao_pcd";
		byte[] formulario = new ImprimeFormularioCartaoEstacionamento().gerar(caminho);
		return formulario;
	}

	@Override
	public String getTextIdoso() throws Exception {
		String text = parametroCustomDb.getValorAsStringWithCustomDb("PTC_TEXT_SOLICITACAO_CREDENCIAL_IDOSO");
		return text;
	}

	@Override
	public String getTextPcd() throws Exception {
		String text = parametroCustomDb.getValorAsStringWithCustomDb("PTC_TEXT_SOLICITACAO_CREDENCIAL_DEFICIENTE");
		return text;
	}

	@Override
	public CepDefault isCepDefault() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);	
			CepDefault cepDefault = isCepDefault(customConnection);
			customConnection.finishConnection();
			return cepDefault;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public CepDefault isCepDefault(CustomConnection customConnection) throws Exception {
		return setCepDefault(customConnection);
	}
	
	private CepDefault setCepDefault(CustomConnection customConnection) throws Exception {
		CepDefault cepDefault = new CepDefault();
		cepDefault.setIsCepDefault(parametroCustomDb.getValorOfParametroAsBoolean("LG_CEP_FIXO", customConnection));
		cepDefault.setCep(parametroCustomDb.getValorOfParametroAsString("NR_CEP", customConnection));
		return cepDefault;
	}
	
	@Override
	public ParametroNmOrgaoResponse buscarNmOrgao(String nmParametro) throws Exception {
		ParametroNmOrgaoResponse orgao = new ParametroNmOrgaoResponse();
		orgao.setNmOrgao(parametroRepository.getValorOfParametroAsString(nmParametro));
		return orgao;
	}
	
	@Override
	public String buscarSgDepartamento(String nmParametro) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);
			String paramSgDepartamento = parametroCustomDb.getValorOfParametroAsString(nmParametro, customConnection);
			customConnection.finishConnection();
			return paramSgDepartamento;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public ParametroValorResponse getParametroValor(String nmParametro) throws Exception {
		ParametroValorResponse valor = new ParametroValorResponse();
		valor.setvlIncial(ParametroServices.getValorOfParametro(nmParametro));
		return valor;
	}
	
}
