package com.tivic.manager.ptc.protocolos;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.ptc.protocolos.relatorio.IGerarBoletimDefesaAutuacao;
import com.tivic.manager.ptc.protocolos.relatorio.IGerarFormularioCondutorProtocolo;
import com.tivic.manager.ptc.protocolos.relatorio.IGerarFormularioDefesaProtocolo;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Protocolo", tags = { "ptc" })
@Path("/v2/ptc/protocolos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProtocoloController {
	private ProtocoloServices protocoloServices;
	private IGerarFormularioCondutorProtocolo gerarFormularioCondutorProtocolo;
	private IGerarFormularioDefesaProtocolo gerarFormularioDefesaProtocolo;
	private IGerarBoletimDefesaAutuacao gerarBoletimDefesaAutuacao;
	
	public ProtocoloController() throws Exception {
		protocoloServices = new ProtocoloServices();
		gerarFormularioCondutorProtocolo = (IGerarFormularioCondutorProtocolo) BeansFactory.get(IGerarFormularioCondutorProtocolo.class);
		gerarFormularioDefesaProtocolo = (IGerarFormularioDefesaProtocolo) BeansFactory.get(IGerarFormularioDefesaProtocolo.class);
		gerarBoletimDefesaAutuacao = (IGerarBoletimDefesaAutuacao) BeansFactory.get(IGerarBoletimDefesaAutuacao.class);
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Geração de Protocolo Refatorada.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo Criado", response = DadosProtocoloDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response create(DadosProtocoloDTO dadosProtocolo) {
		try {
			protocoloServices.createProtocolo(dadosProtocolo);
			return ResponseFactory.ok(dadosProtocolo);
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
	@ApiOperation(value = "Geração de Protocolo Refatorada.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo Atualizado", response = DadosProtocoloDTO.class),
            @ApiResponse(code = 400, message = "Erro na atualização do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response update(DadosProtocoloDTO dadosProtocolo) {
		try {
			protocoloServices.updateProtocolo(dadosProtocolo);
			return ResponseFactory.ok(dadosProtocolo);
		} catch (ValidationException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/fici")
	@ApiOperation(value = "Geração de Protocolo Refatorada.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo Criado", response = DadosProtocoloDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response createFici(AitMovimentoDocumentoDTO dadosProtocolo) {
		try {
			return ResponseFactory.ok(protocoloServices.createProtocolo(dadosProtocolo));
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
			@ApiParam(value = "Documento DTO") DadosProtocoloDTO documento) {
		try {
			return ResponseFactory.ok(protocoloServices.cancelaProtocolo(documento));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@GET
	@Path("/formulario/condutor")
	@ApiOperation(value = "Gera formulário de Apresentação Condutor", notes = "geração de formulário")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha formulário"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces({ "application/pdf", "application/json" })
	public Response getFormularioCondutor() throws ValidacaoException, Exception {
		try {
			JSONObject response = new JSONObject();
			response.put("code", 1);
			response.put("blob", gerarFormularioCondutorProtocolo.gerarDocumentosCondutor());
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			ve.printStackTrace();
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/formulario/defesa")
	@ApiOperation(value = "Gera formulário de Defesa", notes = "geração de formulário")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha formulário"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces({ "application/pdf", "application/json" })
	public Response getFormularioDefesa() throws ValidacaoException, Exception {
		try {
			JSONObject response = new JSONObject();
			response.put("code", 1);
			response.put("blob", gerarFormularioDefesaProtocolo.gerarDocumentosDefesa());
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			ve.printStackTrace();
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/boletim/defesa")
	@ApiOperation(value = "Gera Boletim de Resultados de Julgamento de Defesa", notes = "geração de boletim")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não há boletim"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces({ "application/pdf", "application/json" })
	public Response gerarBoletimDefesa() throws ValidacaoException, Exception {
		try {
			JSONObject response = new JSONObject();
			response.put("code", 1);
			response.put("blob", gerarBoletimDefesaAutuacao.gerarBoletimDefesa());
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			ve.printStackTrace();
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
