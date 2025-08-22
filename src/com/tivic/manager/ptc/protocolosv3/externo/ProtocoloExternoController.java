package com.tivic.manager.ptc.protocolosv3.externo;

import java.util.List;

import javax.ws.rs.BadRequestException;
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

import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.ptc.protocolosv3.externo.impressao.IGeraImpressaoProtocoloExterno;
import com.tivic.manager.ptc.protocolosv3.externo.oficio.IGeraOficioProtocoloExterno;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.util.Result;

@Api(value = "Protocolo", tags = { "ptc" })
@Path("/v3/ptc/protocolos/externo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProtocoloExternoController {

	private IProtocoloExternoService protocoloExternoService;
	private IGeraImpressaoProtocoloExterno geraImpressaoProtocoloExterno;
	private IGeraOficioProtocoloExterno geraOficioProtocoloExterno;
	private ManagerLog managerLog;

	public ProtocoloExternoController() throws Exception {
		this.protocoloExternoService = (IProtocoloExternoService) BeansFactory.get(IProtocoloExternoService.class);
		this.geraImpressaoProtocoloExterno = (IGeraImpressaoProtocoloExterno) BeansFactory.get(IGeraImpressaoProtocoloExterno.class);
		this.geraOficioProtocoloExterno = (IGeraOficioProtocoloExterno) BeansFactory.get(IGeraOficioProtocoloExterno.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Criação do Protocolo externo.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo externo Criado", response = ProtocoloExternoDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do protocolo externo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response insert(ProtocoloExternoDTO protocoloExternoDTO) {
		try {
			return ResponseFactory.ok(this.protocoloExternoService.insert(protocoloExternoDTO));
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{cdDocumentoExterno}/{cdDocumento}")
	@ApiOperation(value = "Atualização do protocolo externo.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo externo atualizado", response = ProtocoloExternoDTO.class),
            @ApiResponse(code = 400, message = "Erro na atualização do protocolo externo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response update(ProtocoloExternoDTO protocoloExternoDTO, @ApiParam(value = "Código do protocolo externo") @PathParam("cdDocumentoExterno") int cdDocumentoExterno,
			@ApiParam(value = "Código do protocolo externo") @PathParam("cdDocumento") int cdDocumento) {
		try {
			return ResponseFactory.ok(this.protocoloExternoService.update(protocoloExternoDTO, cdDocumentoExterno, cdDocumento));
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdDocumentoExterno}/{cdDocumento}")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolo externo buscado com sucesso", response = ProtocoloExternoDTO.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = ProtocoloExternoDTO.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response get(@ApiParam(value = "Código do protocolo externo") @PathParam("cdDocumentoExterno") int cdDocumentoExterno,
			@ApiParam(value = "Código do protocolo externo") @PathParam("cdDocumento") int cdDocumento) {
		try {
			return ResponseFactory.ok(this.protocoloExternoService.get(cdDocumentoExterno, cdDocumento));
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolo externo buscado com sucesso", response = ProtocoloExternoDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = ProtocoloExternoDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response find(
			@ApiParam(value = "Número da placa") @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(value = "Data do protocolo") @QueryParam("dtInicioProtocolo") String dtInicioProtocolo,
			@ApiParam(value = "Data do protocolo") @QueryParam("dtFinalProtocolo") String dtFinalProtocolo,
			@ApiParam(value = "Identificador do AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Número do documento") @QueryParam("nrDocumento") String nrDocumento,
			@ApiParam(value = "Código do tipo de documento") @QueryParam("cdTipoDocumento") int cdTipoDocumento,
			@ApiParam(value = "Código do orgão externo") @QueryParam("cdOrgaoExterno") int cdOrgaoExterno,
			@ApiParam(value = "Busca através da tela de geração de oficio") @QueryParam("geracaoOficio") @DefaultValue("false") Boolean geracaoOficio,
			@QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosLikeAnyString("B.id_ait", idAit, idAit != null);
			searchCriterios.addCriteriosEqualInteger("A.cd_tipo_documento", cdTipoDocumento, cdTipoDocumento > 0);
			searchCriterios.addCriteriosEqualString("A.nr_documento", nrDocumento, nrDocumento != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_protocolo", dtInicioProtocolo, dtInicioProtocolo != null);
			searchCriterios.addCriteriosMinorDate("A.dt_protocolo", dtFinalProtocolo, dtFinalProtocolo != null);
			searchCriterios.addCriteriosEqualInteger("B.cd_orgao_externo", cdOrgaoExterno, cdOrgaoExterno > 0);
			searchCriterios.addCriteriosEqualString("geracaoOficio", geracaoOficio.toString());
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			return ResponseFactory.ok(this.protocoloExternoService.find(searchCriterios));
		} catch (NoContentException ne) {
			return ResponseFactory.noContent(ne.getMessage());
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdDocumentoExterno}/{cdDocumento}/impressao")
	@ApiOperation(value = "Atualização do protocolo externo.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo externo atualizado", response = ProtocoloExternoDTO.class),
            @ApiResponse(code = 400, message = "Erro na atualização do protocolo externo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	@Produces({"application/pdf", "application/json"})
	public Response impressao(@ApiParam(value = "Código do protocolo externo") @PathParam("cdDocumentoExterno") int cdDocumentoExterno,
			@ApiParam(value = "Código do protocolo externo") @PathParam("cdDocumento") int cdDocumento) {
		try {
			byte[] protocoloExterno = this.geraImpressaoProtocoloExterno.gerar(cdDocumentoExterno, cdDocumento);
			return ResponseFactory.ok(protocoloExterno);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@POST
	@Path("/oficio")
	@ApiOperation(value = "Gera Ofício CETRAN")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo gerado com sucesso", response = Result.class),
			@ApiResponse(code = 400, message = "Os protocolos possuem algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces({"application/pdf", "application/json"})
	public Response gerarOficio(
			@ApiParam(value = "Documentos externos para geração de ofício") List<ProtocoloExternoDTO> protocoloExternoDTO) {
		try {
			LoteImpressao arquvioOficio = this.geraOficioProtocoloExterno.gerarOficio(protocoloExternoDTO);
			return ResponseFactory.ok(arquvioOficio);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}