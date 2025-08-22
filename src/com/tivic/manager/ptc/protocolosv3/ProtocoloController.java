package com.tivic.manager.ptc.protocolosv3;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.portal.credencialestacionamento.ICredencialEstacionamentoService;
import com.tivic.manager.ptc.portal.credencialestacionamento.JulgamentoProtocoloEstacionamento;
import com.tivic.manager.ptc.portal.vagaespecial.ArquivoDTO;
import com.tivic.manager.ptc.protocolosv3.factories.ProtocoloGetFactory;
import com.tivic.manager.ptc.protocolosv3.search.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.search.ProtocoloSearchDTO;
import com.tivic.manager.util.pagination.PagedResponse;
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

@Api(value = "Protocolo", tags = { "ptc" })
@Path("/v3/ptc/protocolos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProtocoloController {

	private IProtocoloService protocoloService;
	private ICredencialEstacionamentoService credencialEstacionamentoService;
	private ManagerLog managerLog;
	
	public ProtocoloController() throws Exception {
		this.protocoloService = (IProtocoloService) BeansFactory.get(IProtocoloService.class);
		credencialEstacionamentoService = (ICredencialEstacionamentoService) BeansFactory.get(ICredencialEstacionamentoService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolos buscados com sucesso", response = ProtocoloSearchDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = AitMovimentoDocumentoDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response find(
		@ApiParam(value = "Tipo de Documento") @QueryParam("tipo") int cdTipoDocumento,
		@ApiParam(value = "Código do AIT") @QueryParam("idAit") String idAit,
		@ApiParam(value = "Código do Fase") @QueryParam("fase") int cdFase,
		@ApiParam(value = "Data Inicial") @QueryParam("dataInicial") String dtInicial,
		@ApiParam(value = "Data Final") @QueryParam("dataFinal") String dtFinal,
		@ApiParam(value = "Número do Documento") @QueryParam("documento") String nrDocumento,
		@ApiParam(value = "Placa do Veículo") @QueryParam("nrPlaca") String placaVeiculo,
		@ApiParam(value = "Situação do Documento") @QueryParam("cdSituacaoDocumento") int situacaoDocumento,
		@ApiParam(value = "Tipo de Documento") @QueryParam("tpStatus") int tpStatus,
		@ApiParam(value = "Data inicial de julgamento") @QueryParam("dtPrazoInicial") String dtPrazoInicial,
		@ApiParam(value = "Data final de julgamento") @QueryParam("dtPrazoFinal") String dtPrazoFinal,
		@ApiParam(value = "Situação registro DETRAN") @QueryParam("stDetran") @DefaultValue("-1") int lgEnviadoDetran,
		@ApiParam(value = "Tempestividade (true/false)") @DefaultValue("false") @QueryParam("lgTempestividade") Boolean lgTempestividade,
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite
	) {
		try {	
			SearchCriterios searchCriterios = new ProtocoloSearchBuilder()
					.setCdTipoDocumento(cdTipoDocumento)
					.setIdAit(idAit)
					.setCdFase(cdFase)
					.setNrDocumento(nrDocumento)
					.setNrPlaca(placaVeiculo)
					.setCdSituacaoDocumento(situacaoDocumento)
					.setDtProtocoloInicial(dtInicial)
					.setDtProtocoloFinal(dtFinal)
					.setTpStatus(tpStatus)
					.setDtPrazoInicial(dtPrazoInicial)
					.setDtPrazoFinal(dtPrazoFinal)
					.setLgEnviadoDetran(lgEnviadoDetran)
					.setLgTempestividade(lgTempestividade.toString())
					.setQtDeslocamento(nrLimite, nrPagina)
					.setQtLimite(nrLimite)
					.build();
			PagedResponse<ProtocoloSearchDTO> protocolos = protocoloService.find(searchCriterios);
			return ResponseFactory.ok(protocolos);
		} catch (NoContentException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.noContent(ve.getMessage());
		} catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@GET
	@Path("/documento/")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolo buscado com sucesso", response = ProtocoloSearchDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = AitMovimentoDocumentoDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getDocumento(
		@ApiParam(value = "Código de Documento") @QueryParam("cdDocumento") int cdDocumento,
		@ApiParam(value = "Tipo de Documento") @QueryParam("tpDocumento") int cdTipoDocumento
	) {
	   try {
		    IProtocoloDTOGet protocoloGetService = new ProtocoloGetFactory().strategy(cdTipoDocumento);
			ProtocoloDTO protocolo = protocoloGetService.get(cdDocumento);
			return ResponseFactory.ok(protocolo);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@POST
	@Path("")
	@ApiOperation(value = "Criação do Protocolo.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo Criado", response = ProtocoloDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response create(ProtocoloInsertDTO protocolo) {
		try {
			ProtocoloDTO protocoloInserido = protocoloService.insert(protocolo);
			return ResponseFactory.ok(protocoloInserido);
		} catch (ValidationException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("")
	@ApiOperation(value = "Atualização do Protocolo.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo atualizado", response = DadosProtocoloDTO.class),
            @ApiResponse(code = 400, message = "Erro na atualização do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response update(ProtocoloDTO protocolo) {
		try {
			protocolo = protocoloService.update(protocolo);
			return ResponseFactory.ok(protocolo);
		} catch (ValidationException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@POST
	@Path("{cdDocumento}/publicacao")
	@ApiOperation(value = "Publicar protocolo.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo publicado", response = DadosProtocoloDTO.class),
            @ApiResponse(code = 400, message = "Erro na publicação do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response publicar(ProtocoloDTO protocolo) {
		try {
			protocolo = protocoloService.publicar(protocolo);
			return ResponseFactory.ok(protocolo);
		} catch (ValidationException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cancelamento")
	@ApiOperation(value = "Cancelamento de Documento")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Documento cancelado", response = Ait[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response cancelamento(
			@ApiParam(value = "Documento DTO") ProtocoloDTO documento) {
		try {
			ProtocoloDTO protocolo = protocoloService.cancelar(documento);
			return ResponseFactory.ok(protocolo);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@GET
	@Path("/documento/{cdDocumento}")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Documento buscado com sucesso", response = Documento.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = Documento.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getDocumentoo(
		@ApiParam(value = "Código de Documento") @PathParam("cdDocumento") int cdDocumento) {
	   try {
			Documento documento =  this.protocoloService.getDocumento(cdDocumento);
			return ResponseFactory.ok(documento);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@POST
	@Path("/credencial-estacionamento/deferir")
	@ApiOperation(value = "Defere credencial de estacionamento")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Cartão do idoso deferido", response = ResponseBody.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response defereCredencialEstacionamento(
			@ApiParam(value = "Objeto de julgamento da credencial de estacionamento") JulgamentoProtocoloEstacionamento julgamentoEstacionamento) {
		try {
			managerLog.showLog(new InfoLogBuilder("[POST] /credencial-estacionamento/deferir", "Deferindo...").build());
			credencialEstacionamentoService.deferir(julgamentoEstacionamento);
			managerLog.showLog(new InfoLogBuilder("[POST] /credencial-estacionamento/deferir", "Deferido com sucesso.").build());
			return ResponseFactory.ok("Credencial de estacionamento deferido.");
		} catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/credencial-estacionamento/indeferir")
	@ApiOperation(value = "Indefere credencial de estacionamento")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Credencial de estacionamento indeferido", response = ResponseBody.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response indefereCredencialEstacionamento(
			@ApiParam(value = "Objeto de julgamento da credencial de estacionamento") JulgamentoProtocoloEstacionamento julgamentoEstacionamento) {
		try {
			managerLog.showLog(new InfoLogBuilder("[POST] /credencial-estacionamento/indeferir", "Indeferindo...").build());
			credencialEstacionamentoService.indeferir(julgamentoEstacionamento);
			managerLog.showLog(new InfoLogBuilder("[POST] /credencial-estacionamento/indeferir", "Indeferido com sucesso.").build());
			return ResponseFactory.ok("Credencial de estacionamento indeferido.");
		} catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/protocolo-credencial-estacionamento")
	@ApiOperation(value = "Lista todos os protocolos de credencial de estacionamento.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolos de credencial de estacionamento buscados com sucesso", response = ProtocoloSearchDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = AitMovimentoDocumentoDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response findProtocoloIdoso(
		@ApiParam(value = "Tipo de Documento") @QueryParam("tipo") int cdTipoDocumento,
		@ApiParam(value = "Código do Fase") @QueryParam("fase") int cdFase,
		@ApiParam(value = "Data Inicial") @QueryParam("dataInicial") String dtInicial,
		@ApiParam(value = "Data Final") @QueryParam("dataFinal") String dtFinal,
		@ApiParam(value = "Número do Documento") @QueryParam("documento") String nrDocumento,
		@ApiParam(value = "Placa do Veículo") @QueryParam("nrPlaca") String placaVeiculo,
		@ApiParam(value = "Situação do Documento") @QueryParam("cdSituacaoDocumento") int situacaoDocumento,
		@ApiParam(value = "Tipo de Documento") @QueryParam("tpStatus") int tpStatus,
		@ApiParam(value = "Data inicial de julgamento") @QueryParam("dtPrazoInicial") String dtPrazoInicial,
		@ApiParam(value = "Data final de julgamento") @QueryParam("dtPrazoFinal") String dtPrazoFinal,
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite
	) {
		try {		
			SearchCriterios searchCriterios = new ProtocoloSearchBuilder()
					.setCdFase(cdFase)
					.setNrDocumento(nrDocumento)
					.setCdSituacaoDocumento(situacaoDocumento)
					.setDtProtocoloInicial(dtInicial)
					.setDtProtocoloFinal(dtFinal)
					.setTpStatus(tpStatus)
					.setDtPrazoInicial(dtPrazoInicial)
					.setDtPrazoFinal(dtPrazoFinal)
					.setQtDeslocamento(nrLimite, nrPagina)
					.setQtLimite(nrLimite)
					.build();
			PagedResponse<ProtocoloSearchDTO> protocolos = protocoloService.findProtocoloCredencialEstacionamento(searchCriterios);
			return ResponseFactory.ok(protocolos);
		} catch (NoContentException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.noContent(ve.getMessage());
		} catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/credencial-estacionamento")
	@ApiOperation(value = "Busca um protocolo de credencial de estacionamento.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolos de credencial de estacionameto buscados com sucesso", response = ProtocoloSearchDTO.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = AitMovimentoDocumentoDTO.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getProtocoloCredencialEstacionamento(
			@ApiParam(value = "Código do documento") @QueryParam("cdDocumento") int cdDocumento
	) {
		try {		
			SearchCriterios searchCriterios = new ProtocoloSearchBuilder()
					.setCdDocumento(cdDocumento)
					.build();
			
			ProtocoloSearchDTO protocolo = protocoloService.getProtocoloCredencialEstacionamento(searchCriterios);
			return ResponseFactory.ok(protocolo);
		} catch (NoContentException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.noContent(ve.getMessage());
		} catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/protocolo-arquivo-estacinamento/{cdDocumento}")
	@ApiOperation(value = "Lista dos os arquivos de um protocolo de credencial de estacionamento.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Arquivo de credencial de estacionameto buscados com sucesso", response = ArquivoDTO.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = AitMovimentoDocumentoDTO.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getProtocoloArquivoEstacinamento(
		@ApiParam(value = "Código do documento") @PathParam("cdDocumento") int cdDocumento
	) {
		try {		
			List<ArquivoDTO> arquivos = protocoloService.getArquivoProtocolo(cdDocumento);
			return ResponseFactory.ok(arquivos);
		} catch (NoContentException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.noContent(ve.getMessage());
		} catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
