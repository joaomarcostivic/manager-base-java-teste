package com.tivic.manager.ptc.portal;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.portal.builders.AitSearchBuilder;
import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.manager.ptc.portal.request.DocumentoPortalRequest;
import com.tivic.manager.ptc.portal.response.AitProtocoloResponse;
import com.tivic.manager.ptc.portal.response.AitResponse;
import com.tivic.manager.ptc.portal.response.AndamentoAitResponse;
import com.tivic.manager.ptc.portal.response.DocumentoPortalResponse;
import com.tivic.manager.ptc.portal.response.FormularioPortalResponse;
import com.tivic.manager.ptc.portal.response.ParametroContatoResponse;
import com.tivic.manager.ptc.portal.response.ParametroImagemResponse;
import com.tivic.manager.ptc.portal.response.ParametroInstrucaoResponse;
import com.tivic.manager.ptc.portal.response.ParametroNmOrgaoResponse;
import com.tivic.manager.ptc.portal.response.ParametroValorResponse;
import com.tivic.manager.ptc.portal.response.SegundaViaNotificacaoResponse;
import com.tivic.manager.ptc.portal.response.TipoArquivoResponse;
import com.tivic.manager.ptc.portal.vagaespecial.ProtocoloSolicitacaoDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloSearchBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.log.builders.InfoLogBuilder;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "PORTAL", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })

@Path("/v3/mob/portal")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PortalController {

	private IPortalService portalService;
	private ManagerLog managerLog;

	public PortalController() throws Exception {
		this.portalService = (IPortalService) BeansFactory.get(IPortalService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@GET
	@Path("/ait")
	@ApiOperation(value = "Buscar AITs", notes = "Endpoint para buscar AITs com base nos parâmetros fornecidos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs encontrados", response = AitResponse.class, responseContainer = "List"),
			@ApiResponse(code = 204, message = "Nenhum AIT encontrado"),
			@ApiResponse(code = 400, message = "Parâmetros inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response find(
			@ApiParam(name = "idAit", value = "ID do AIT a ser buscado (obrigatório)") @QueryParam("idAit") String idAit,
			@ApiParam(name = "nrPlaca", value = "Número da placa do veículo (obrigatório)", required = true) @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(name = "nrRenavan", value = "Número do RENAVAM do veículo (opcional)", required = true) @QueryParam("nrRenavan") String nrRenavan) {
		try {
			SearchCriterios searchCriterios = new AitSearchBuilder().setIdAit(idAit).setNrRenavan(nrRenavan).build();
			managerLog.showLog(new InfoLogBuilder("[GET] /ait", "Requisição de busca de AITs...").build());
			List<AitResponse> aits = this.portalService.findAit(nrPlaca, searchCriterios);
			managerLog.showLog(new InfoLogBuilder("[GET] /ait", "A busca de AITs foi realizada com sucesso.").build());
			return ResponseFactory.ok(aits);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/tipo-arquivo")
	@ApiOperation(value = "Buscar tipos de arquivos", notes = "Endpoint para buscar os possíveis tipos de arquivos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipos de arquivos encontrados", response = AitResponse.class, responseContainer = "List"),
			@ApiResponse(code = 204, message = "Nenhum tipo de arquivo encontrado"),
			@ApiResponse(code = 400, message = "Parâmetros inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response find() {
		try {
			TipoArquivoResponse tipoArquivo = this.portalService.findTipoArquivo();
			return ResponseFactory.ok(tipoArquivo);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@POST
	@Path("/fici")
	@ApiOperation(value = "Criação da fici", notes = "Endpoint para solicitação da fici")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Protocolo Fici criado com sucesso.", response = DocumentoPortalRequest.class),
			@ApiResponse(code = 204, message = "Não foi possível completar a solicitação."),
			@ApiResponse(code = 500, message = "Erro no servidor."),
	})
	public Response solicitarFici(@ApiParam(name = "DocumentoRecursoDTO", value = "Objeto principal da solicitação ao recurso Fici") DocumentoPortalRequest documentoRecursoDTO,
			@HeaderParam("referer") String referer) {
		try {
			documentoRecursoDTO.setReferer(referer);
			managerLog.showLog(new InfoLogBuilder("[POST] /fici", "Requisição de solicitação de apresentação de condutor...").build());
			DocumentoPortalResponse documentoPortalResponse = this.portalService.solicitarFici(documentoRecursoDTO);
			managerLog.showLog(new InfoLogBuilder("[POST] /fici", "A solicitação de apresentação de condutor foi salva com sucesso.").build());
			return ResponseFactory.created(documentoPortalResponse);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@POST
	@Path("/defesa-previa")
	@ApiOperation(value = "Criação de defesa prévia", notes = "Endpoint para solicitação de defesa prévia")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Defesa prévia criado com sucesso.", response = DocumentoPortalRequest.class),
			@ApiResponse(code = 204, message = "Não foi possível completar a solicitação."),
			@ApiResponse(code = 500, message = "Erro no servidor."),
	})
	public Response solicitarDefesaPrevia(
			@ApiParam(value = "DocumentoRecursoDTO") DocumentoPortalRequest documentoRecursoDTO,
			@HeaderParam("referer") String referer) {
		try {
			documentoRecursoDTO.setReferer(referer);
			managerLog.showLog(new InfoLogBuilder("[POST] /defesa-previa", "Requisição de solicitação de defesa prévia...").build());
			DocumentoPortalResponse documentoPortalResponse = this.portalService.solicitarDefesaPrevia(documentoRecursoDTO);
			managerLog.showLog(new InfoLogBuilder("[POST] /defesa-previa", "A solicitação de defesa prévia foi salva com sucesso.").build());
			return ResponseFactory.created(documentoPortalResponse);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cartao-idoso")
	@ApiOperation(value = "Criação de cartão do idoso", notes = "Endpoint para solicitação de cartão do idoso")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cartão do idoso criado com sucesso.", response = DocumentoPortalRequest.class),
			@ApiResponse(code = 204, message = "Não foi possível completar a solicitação."),
			@ApiResponse(code = 500, message = "Erro no servidor."),
	})
	public Response solicitarCartaoDoIdoso(
			@ApiParam(value = "DocumentoRecursoDTO") DocumentoPortalRequest documentoRecursoDTO,
			@HeaderParam("referer") String referer) {
		try {
			documentoRecursoDTO.setReferer(referer);
			managerLog.showLog(new InfoLogBuilder("[POST] /cartao-idoso", "Solicitando credencial de estacionamento do idoso...").build());
			DocumentoPortalResponse documentoPortalResponse = this.portalService.solicitarCartaoIdoso(documentoRecursoDTO);
			managerLog.showLog(new InfoLogBuilder("[POST] /cartao-idoso", "A solicitação de cartão do idoso foi salva com sucesso.").build());
			return ResponseFactory.created(documentoPortalResponse);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cartao-pcd")
	@ApiOperation(value = "Criação de cartão de PCD", notes = "Endpoint para solicitação de cartão de PCD")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cartão de PCD criado com sucesso.", response = DocumentoPortalRequest.class),
			@ApiResponse(code = 204, message = "Não foi possível completar a solicitação."),
			@ApiResponse(code = 500, message = "Erro no servidor."),
	})
	public Response solicitarCartaoDePcd(
			@ApiParam(value = "DocumentoRecursoDTO") DocumentoPortalRequest documentoRecursoDTO,
			@HeaderParam("referer") String referer) {
		try {
			documentoRecursoDTO.setReferer(referer);
			managerLog.showLog(new InfoLogBuilder("[POST] /cartao-pcd", "Solicitando credencial de estacionamento de PCD...").build());
			DocumentoPortalResponse documentoPortalResponse = this.portalService.solicitarCartaoPcd(documentoRecursoDTO);
			managerLog.showLog(new InfoLogBuilder("[POST] /cartao-pcd", "A solicitação de cartão de PCD foi salva com sucesso.").build());
			return ResponseFactory.created(documentoPortalResponse);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/vaga-especial")
	@ApiOperation(value = "Retorna uma lista de solicitações de vaga especial")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Solicitações encontradas", response = DocumentoPortalResponse[].class),
			@ApiResponse(code = 204, message = "Nenhuma solicitação foi encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response findSolicitacoes(
			@ApiParam(value = "Número do protocolo") @QueryParam("nrProtocolo") String nrProtocolo,
			@ApiParam(value = "Código do documento") @QueryParam("cdDocumento") int cdDocumento,
			@ApiParam(value = "Código do tipo documento") @QueryParam("tpDocumento") int tpDocumento,
			@ApiParam(value = "CPF do requerente") @QueryParam("nrCpfRequerente") String nrCpfRequerente) {
		try {
			SearchCriterios searchCriterios = new ProtocoloSearchBuilder()
					.setNrDocumento(nrProtocolo)
					.setNrCpfRequerente(nrCpfRequerente)
					.setCdDocumento(cdDocumento)
					.setTpDocumento(tpDocumento)
				.build();
			List<ProtocoloSolicitacaoDTO> solicitacoes = this.portalService.findSolicitacoes(searchCriterios);
			return ResponseFactory.ok(solicitacoes);
		} catch (NoContentException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.noContent(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/solicitacao-anexos")
	@ApiOperation(value = "Retorna uma lista de anexos de solicitação de vaga especial")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Anexos encontrados", response = Arquivo[].class),
			@ApiResponse(code = 204, message = "Nenhum anexo foi encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response finsAnexos(
			@ApiParam(value = "Código do documento") @QueryParam("cdDocumento") int cdDocumento) {
		try {
			SearchCriterios searchCriterios = new ProtocoloSearchBuilder()
					.setCdDocumento(cdDocumento)
					.build();
			List<Arquivo> anexos = this.portalService.findAnexos(searchCriterios);
			return ResponseFactory.ok(anexos);
		} catch (NoContentException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.noContent(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/cidades")
	@ApiOperation(value = "Retorna uma lista de cidades")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Cidades encontradas", response = CidadeDTO[].class),
			@ApiResponse(code = 204, message = "Nenhuma cidade foi encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response getCidades(
			@ApiParam(value = "Nome da cidade", required = true) @QueryParam("nmCidade") String nmCidade) {
		try {
			List<CidadeDTO> cidades = this.portalService.getCidades(nmCidade);
			return ResponseFactory.ok(cidades);
		} catch (NoContentException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.noContent(ve.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/segunda-via")
	@ApiOperation(value = "Geração de segunda via de notificação", 
	    notes = "Endpoint para gerar segunda via de notificação com base nos parâmetros fornecidos")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Segunda via de notificação gerada com sucesso"),
	        @ApiResponse(code = 400, message = "Parâmetros inválidos"),
	        @ApiResponse(code = 500, message = "Erro no servidor") })
	public Response gerarSegundaViaNotificacao(
			@ApiParam(name = "cdAit", value = "Código do AIT a ser buscado (obrigatório)") @QueryParam("cdAit") int cdAit,
			@ApiParam(name = "tpStatus", value = "Tipo de status da notificação (obrigatório)") @QueryParam("tpStatus") int tpStatus) {
		try {
			SegundaViaNotificacaoResponse segundaViaNotificacao = this.portalService.gerarSegundaViaNotificacao(cdAit,
					tpStatus);
			return Response.ok(segundaViaNotificacao).build();
		} catch (ValidacaoException e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e) {
			managerLog.showLog(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("/jaris")
	@ApiOperation(value = "Criação da JARI", notes = "Endpoint para solicitação da JARI")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Protocolo JARI criado com sucesso.", response = DocumentoPortalRequest.class),
			@ApiResponse(code = 204, message = "Não foi possível completar a solicitação."),
			@ApiResponse(code = 500, message = "Erro no servidor."),
	})
	public Response solicitarJari(
			@ApiParam(value = "DocumentoRecursoDTO") DocumentoPortalRequest documentoRecursoDTO,
			@HeaderParam("referer") String referer) {
		try {
			documentoRecursoDTO.setReferer(referer);
			managerLog.showLog(new InfoLogBuilder("[POST] /jaris", "Requisição de solicitação de recurso à JARI...").build());
			DocumentoPortalResponse documentoPortalResponse = this.portalService.solicitarJari(documentoRecursoDTO);
			managerLog.showLog(new InfoLogBuilder("[POST] /jaris", "A solicitação de recurso à JARI foi salva com sucesso.").build());
			return ResponseFactory.created(documentoPortalResponse);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@POST
	@Path("/cetrans")
	@ApiOperation(value = "Criação da Cetran", notes = "Endpoint para solicitação da Cetran")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Protocolo Cetran criado com sucesso.", response = DocumentoPortalRequest.class),
			@ApiResponse(code = 204, message = "Não foi possível completar a solicitação."),
			@ApiResponse(code = 500, message = "Erro no servidor."),
	})
	public Response solicitarCetran(
			@ApiParam(value = "DocumentoRecursoDTO") DocumentoPortalRequest documentoRecursoDTO,
			@HeaderParam("referer") String referer) {
		try {
			documentoRecursoDTO.setReferer(referer);
			managerLog.showLog(new InfoLogBuilder("[POST] /cetrans", "Requisição de solicitação de recurso ao Cetran...").build());
			DocumentoPortalResponse cetran = this.portalService.solicitarCetran(documentoRecursoDTO);
			managerLog.showLog(new InfoLogBuilder("[POST] /cetrans", "A solicitação de recurso ao Cetran foi salva com sucesso.").build());
			return ResponseFactory.created(cetran);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/aits/impressao/andamento")
	@ApiOperation(value = "Gerar impressão do andamento da AIT", 
	notes = "Endpoint para gerar a impressão doandamento da AIT com base no código do AIT fornecido")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Impressão do andamento da AIT gerada com sucesso"),
			@ApiResponse(code = 400, message = "Código do andamento da AIT inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response gerarImpressaoAit(
			@ApiParam(name = "cdAit", value = "Código do AIT a ser gerada a impressão do andamento. (obrigatório)") 
			@QueryParam("cdAit") int cdAit) {
		try {
			AndamentoAitResponse impressao = portalService.gerarImpressaoAit(cdAit);
			return ResponseFactory.ok(impressao);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/formularios/{tpDocumento}")
	@ApiOperation(value = "Solicitação de Formulário de recursos", notes = "Endpoint para solicitação de formulários")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Formulário impresso com sucesso."),
			@ApiResponse(code = 204, message = "Não foi possível completar a solicitação."),
			@ApiResponse(code = 500, message = "Erro no servidor."),
	})
	public Response solicitarFomulario(
			@ApiParam(value = "Tipo do Documento") @PathParam("tpDocumento") int tpDocumento) {
		try {
			FormularioPortalResponse response = new FormularioPortalResponse();
			response.setFormulario(this.portalService.imprimirFormulario(tpDocumento));
			return ResponseFactory.ok(response);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/protocolo")
	@ApiOperation(value = "Buscar AITs", notes = "Endpoint para buscar AITs com base nos parâmetros fornecidos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs encontrados", response = AitProtocoloResponse.class, responseContainer = "List"),
			@ApiResponse(code = 204, message = "Nenhum AIT encontrado"),
			@ApiResponse(code = 400, message = "Parâmetros inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response findAitProtocolo(
			@ApiParam(name = "idAit", value = "ID do AIT a ser buscado (obrigatório)", required = true) @QueryParam("idAit") String idAit,
			@ApiParam(name = "nrPlaca", value = "Número da placa do veículo (opcional)", required = false) @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(name = "nrRenavan", value = "Número do RENAVAM do veículo (opcional)", required = false) @QueryParam("nrRenavan") String nrRenavan) {
		try {
			SearchCriterios searchCriterios = new AitSearchBuilder().setIdAit(idAit).setNrRenavan(nrRenavan).build();
			managerLog.showLog(new InfoLogBuilder("[GET] /ait", "Requisição de busca de AITs...").build());
			AitProtocoloResponse aits = this.portalService.findAitProtocolo(nrPlaca, searchCriterios);
			managerLog.showLog(new InfoLogBuilder("[GET] /ait", "A busca de AITs foi realizada com sucesso.").build());
			return ResponseFactory.ok(aits);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/parametros/imagens")
	@ApiOperation(value = "Buscar Imagem Órgão", notes = "Endpoint para buscar imagem do órgão com base nos parâmetro fornecido")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Imagem encontradas"),
			@ApiResponse(code = 204, message = "Nenhum imagem encontrada"),
			@ApiResponse(code = 400, message = "Parâmetros inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response buscarImagem(
			@ApiParam(name = "nmParametro", value = "Parametro da imagem") @QueryParam("nmParametro") String nmParametro) {
		try {
			ParametroImagemResponse response = new ParametroImagemResponse();
			response.setImagem(this.portalService.buscarImagem(nmParametro));
			return ResponseFactory.ok(response);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (Exception e) {
			managerLog.showLog(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/parametros/instrucoes")
	@ApiOperation(value = "Buscar Imagem Órgão", notes = "Endpoint para buscar instrucões para lançar um protocolo com base nos parâmetro fornecido")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Instruções encontradas"),
			@ApiResponse(code = 204, message = "Nenhuma instrucão encontrada"),
			@ApiResponse(code = 400, message = "Parâmetros inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response buscarInstrucoes(
			@ApiParam(name = "nmParametro", value = "Parametro da instrução") @QueryParam("nmParametro") String nmParametro) {
		try {
			ParametroInstrucaoResponse response =  this.portalService.buscarInstrucoes(nmParametro);
			return ResponseFactory.ok(response);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (Exception e) {
			managerLog.showLog(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/parametros/contatos")
	@ApiOperation(value = "Buscar contatos do ógão", notes = "Endpoint para buscar os contatos do órgão")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Contatos encontrados", response = AitResponse.class, responseContainer = "List"),
			@ApiResponse(code = 204, message = "Nenhum contato encontrado"),
			@ApiResponse(code = 400, message = "Parâmetros inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response buscarContato() {
		try {
			ParametroContatoResponse contato = this.portalService.buscarContato();
			return ResponseFactory.ok(contato);
		} catch (NoContentException e) {
			managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/formularios/cartao-idoso")
	@ApiOperation(value = "Solicitação de Formulário de cartão do idoso", notes = "Endpoint para solicitação de formulário")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Formulário impresso com sucesso."),
			@ApiResponse(code = 204, message = "Não foi possível completar a solicitação."),
			@ApiResponse(code = 500, message = "Erro no servidor."),
	})
	public Response solicitarFomularioCartaoIdoso() {
		try {
			managerLog.showLog(new InfoLogBuilder("[GET] /formularios/cartao-idoso", "Baixando formulário do cartão do idoso...").build());
			byte[] formulario = this.portalService.imprimirFormularioCartaoIdoso();
			managerLog.showLog(new InfoLogBuilder("[GET] /formularios/cartao-idoso", "Frmulário baixado com sucesso.").build());
			return ResponseFactory.ok(formulario);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/formularios/cartao-pcd")
	@ApiOperation(value = "Solicitação de Formulário de cartão de pessoa com deficiência.", notes = "Endpoint para solicitação de formulário")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Formulário impresso com sucesso."),
			@ApiResponse(code = 204, message = "Não foi possível completar a solicitação."),
			@ApiResponse(code = 500, message = "Erro no servidor."),
	})
	public Response solicitarFomularioCartaoPcd() {
		try {
			managerLog.showLog(new InfoLogBuilder("[GET] /formularios/cartao-pcd", "Baixando formulário do cartão de pessoa com deficiência...").build());
			byte[] formulario = this.portalService.imprimirFormularioCartaoPcd();
			managerLog.showLog(new InfoLogBuilder("[GET] /formularios/cartao-pcd", "Frmulário baixado com sucesso.").build());
			return ResponseFactory.ok(formulario);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/credencial-estacionamento")
	@ApiOperation(value = "Criação da credencial de estacionamento", notes = "Endpoint para solicitação da credencial de estacionamento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Protocolo da credencial de estacionamento criado com sucesso.", response = CartaoEstacionamentoRequest.class),
			@ApiResponse(code = 204, message = "Não foi possível completar a solicitação."),
			@ApiResponse(code = 500, message = "Erro no servidor."),
	})
	public Response solicitarCartaoIdoso(@ApiParam(name = "documentoCartaoIdosoDTO", value = "Objeto principal da solicitação da credencial de estacionamento") CartaoEstacionamentoRequest documentoCartaoIdosoDTO){
		try {
			managerLog.showLog(new InfoLogBuilder("[POST] /credencial-estacionamento", "Solicitando credencial de estacionamento...").build());
			DocumentoPortalResponse documentoPortalResponse  = this.portalService.solicitarCredencialEstacionamento(documentoCartaoIdosoDTO);
			managerLog.showLog(new InfoLogBuilder("[POST] /credencial-estacionamento", "A solicitação da credencial de estacionamento foi salva com sucesso.").build());
			return ResponseFactory.ok(documentoPortalResponse);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/cartao-idoso/{cdDocumento}")
	@ApiOperation(value = "Gera cartão de estacionamento para pessoa idosa.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Cartão gerado com sucesso.", response = Documento.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado.", response = Documento.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido.", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor.", response = ResponseBody.class) 
	})
	@Produces({ "application/pdf", "application/json" })
	public Response getCartaoIdoso(
		@ApiParam(value = "Código de Documento") @PathParam("cdDocumento") int cdDocumento) {
	   try {
			managerLog.showLog(new InfoLogBuilder("[GET] /cartao-idoso/{cdDocumento}", "Baixando credencial de estacionamento do idoso...").build());
			byte[] cartaoIdoso = this.portalService.getCartaoIdoso(cdDocumento);
			managerLog.showLog(new InfoLogBuilder("[GET] /cartao-idoso/"+cdDocumento, "Credencial de estacionamento baixada com sucesso.").build());
			return ResponseFactory.ok(cartaoIdoso);
		}
		catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/cartao-pcd/{cdDocumento}")
	@ApiOperation(value = "Gera cartão de estacionamento para pessoa com deficiência.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Cartão gerado com sucesso.", response = Documento.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado.", response = Documento.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido.", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor.", response = ResponseBody.class) 
	})
	@Produces({ "application/pdf", "application/json" })
	public Response getCartaoPcd(
		@ApiParam(value = "Código de Documento") @PathParam("cdDocumento") int cdDocumento) {
	   try {
			managerLog.showLog(new InfoLogBuilder("[GET] /cartao-pcd/{cdDocumento}", "Baixando credencial de estacionamento de pessoa com deficiência...").build());
			byte[] cartaoIdoso = this.portalService.getCartaoPcd(cdDocumento);
			managerLog.showLog(new InfoLogBuilder("[GET] /cartao-pcd/"+cdDocumento, "Credencial de estacionamento baixada com sucesso.").build());
			return ResponseFactory.ok(cartaoIdoso);
		}
		catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/text-credencial-idoso")
	@ApiOperation(value = "Busca mensagem de exibição da tela de credencial do cartao do idoso.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Busca da mensagem realizada com sucesso.", response = ResponseBody.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = Documento.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getTextCredencialIdoso() {
	   try {
			String text = this.portalService.getTextIdoso();
			return ResponseFactory.ok(text);
		}
		catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/text-credencial-pcd")
	@ApiOperation(value = "Busca mensagem de exibição da tela de credencial do cartao do deficiênte.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Busca da mensagem realizada com sucesso.", response = ResponseBody.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = ResponseBody.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getTextCredencialPcd() {
	   try {
			String text = this.portalService.getTextPcd();
			return ResponseFactory.ok(text);
		}
		catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/cep-default")
	@ApiOperation(value = "Busca o cep padrão para solicitação da credencial de estacionamento.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Busca da mensagem realizada com sucesso.", response = ResponseBody.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = ResponseBody.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getIsCepDefault() {
	   try {
			CepDefault cepDefault = portalService.isCepDefault();
			return ResponseFactory.ok(cepDefault);
		}
		catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/parametros/nmOrgao")
	@ApiOperation(value = "Buscar Nome do Órgão", notes = "Endpoint para buscar nome do órgão com base no parâmetro fornecido")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Nome do órgão encontrado"),
			@ApiResponse(code = 204, message = "Nenhuma órgão encontrado"),
			@ApiResponse(code = 400, message = "Parâmetros inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response buscarNmOrgao(
			@ApiParam(name = "nmParametro", value = "Parâmetro do nome do órgão") @QueryParam("nmParametro") String nmParametro) {
		try {
			ParametroNmOrgaoResponse response =  this.portalService.buscarNmOrgao(nmParametro);
			return ResponseFactory.ok(response);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (Exception e) {
			managerLog.showLog(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/parametros/sgDepartamento/{nmParametro}")
	@ApiOperation(value = "Buscar sigla do departamento", notes = "Endpoint para buscar a sigla do departamento com base no parâmetro fornecido")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sigla do departamento encontrado"),
			@ApiResponse(code = 204, message = "Nenhuma sigla encontrada"),
			@ApiResponse(code = 400, message = "Parâmetros inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response buscarSgDepartamento(
			@ApiParam(name = "nmParametro", value = "Parâmetro do nome da sigla do departamento") @PathParam("nmParametro") String nmParametro) {
		try {
			return ResponseFactory.ok(this.portalService.buscarSgDepartamento(nmParametro));
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (Exception e) {
			managerLog.showLog(e);
			e.printStackTrace(System.out);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/parametros/{idParametro}/valor")
	@ApiOperation(value = "Buscar por parâmetro", notes = "Endpoint para buscar parâmetro por identificador fornecido.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parâmetro encontrado"),
			@ApiResponse(code = 204, message = "Nenhuma parâmetro encontrada"),
			@ApiResponse(code = 400, message = "Parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response getParametroValor(
			@ApiParam(name = "nmParametro", value = "identificado do parâmetro") @PathParam("idParametro") String idParametro) {
		try {
	        ParametroValorResponse response = this.portalService.getParametroValor(idParametro);
			return ResponseFactory.ok(response);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (Exception e) {
			managerLog.showLog(e);
			e.printStackTrace(System.out);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

}
