package com.tivic.manager.mob.aitmovimentodocumento;

import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.sol.cdi.BeansFactory;
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
import sol.dao.ItemComparator;

@Api(value = "Documento Movimento", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/aits")
@Produces(MediaType.APPLICATION_JSON)
public class AitMovimentoDocumentoController {
	private IAitMovimentoDocumentoService aitMovimentoDocumentoService;
	private IAitMovimentoService aitMovimentoservice;
	
	public AitMovimentoDocumentoController() throws Exception {
		aitMovimentoDocumentoService = (IAitMovimentoDocumentoService) BeansFactory.get(IAitMovimentoDocumentoService.class);
		aitMovimentoservice = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	
	@GET
	@Path("/documentos/processos")
	@ApiOperation(value = "Exibição de Histório de movimentos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = Ait[].class),
			@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response consultaRelatorioProcessos(
			@ApiParam(value = "Tipo de Documento") @QueryParam("tipoDocumento") @DefaultValue("0") int cdTipoDocumento,
			@ApiParam(value = "Data Inicial") @QueryParam("dataInicial") String dtInicial,
			@ApiParam(value = "Data Final") @QueryParam("dataFinal") String dtFinal,
			@ApiParam(value = "Fase do Documento") @QueryParam("fase") @DefaultValue("0") int cdFase,
			@ApiParam(value = "Situação do Documento") @QueryParam("cdSituacaoDocumento") int situacaoDocumento
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosGreaterDate("D.dt_protocolo", dtInicial, dtInicial != null);
			searchCriterios.addCriteriosMinorDate("D.dt_protocolo", dtFinal, dtFinal != null);
			searchCriterios.addCriteriosEqualInteger("F.cd_fase", cdFase, cdFase > 0);
			searchCriterios.addCriteriosEqualInteger("E.cd_tipo_documento", cdTipoDocumento, cdTipoDocumento > 0);
			searchCriterios.addCriterios("A.dt_publicacao_do", "", ItemComparator.ISNULL, Types.TIME_WITH_TIMEZONE);
			searchCriterios.addCriteriosEqualInteger("D.cd_situacao_documento", situacaoDocumento, situacaoDocumento > 0);
			return ResponseFactory.ok(aitMovimentoDocumentoService.findProcessosPublicacao(searchCriterios));
		} 
		catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
 	}
	
	@GET
	@Path("/documentos/protocolos")
	@ApiOperation(value = "Exibição de Protocolos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = Ait[].class),
			@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response findProtocolos(
			@ApiParam(value = "Tipo de Documento") @QueryParam("tipo") @DefaultValue("0") int cdTipoDocumento,
			@ApiParam(value = "Código de AIT") @QueryParam("cdAit") @DefaultValue("0") int cdAit,
			@ApiParam(value = "Indentificador do AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Código da Apresentação do Condutor") @DefaultValue("0") @QueryParam("cdFici") int cdFici,
			@ApiParam(value = "Código do Documento") @DefaultValue("0") @QueryParam("cdDocumento") int cdDocumento
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("E.cd_tipo_documento", cdTipoDocumento, cdTipoDocumento > 0);
			searchCriterios.addCriteriosEqualInteger("B.cd_ait", cdAit, cdAit > 0);
			searchCriterios.addCriteriosEqualString("B.id_ait", idAit, idAit != null);
			searchCriterios.addCriteriosEqualInteger("G.cd_apresentacao_condutor", cdFici, cdFici > 0);
			searchCriterios.addCriteriosEqualInteger("D.cd_documento", cdDocumento, cdDocumento > 0);
			return ResponseFactory.ok(aitMovimentoDocumentoService.findProcessos(searchCriterios));
		} 
		catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
 	}
	
	@GET
	@Path("/documentos/protocolos/search")
	@ApiOperation(value = "Busca de Protocolos Paginados")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = Ait[].class),
			@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response searchProtocolos(
			@ApiParam(value = "Tipo de Documento") @QueryParam("tipo") int cdTipoDocumento,
			@ApiParam(value = "Código do AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Código do Fase") @QueryParam("fase") int cdFase,
			@ApiParam(value = "Número do Documento") @QueryParam("documento") String nrDocumento,
			@ApiParam(value = "Placa do Veículo") @QueryParam("placa") String placaVeiculo,
			@ApiParam(value = "Situação do Documento") @QueryParam("cdSituacaoDocumento") int situacaoDocumento,
	        @QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite)
			{
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("E.cd_tipo_documento", cdTipoDocumento, cdTipoDocumento > 0);
			searchCriterios.addCriteriosEqualString("B.id_ait", idAit, idAit != null);
			searchCriterios.addCriteriosEqualInteger("F.cd_fase", cdFase, cdFase > 0);
			searchCriterios.addCriteriosEqualString("D.nr_documento", nrDocumento, nrDocumento != null);
			searchCriterios.addCriteriosEqualString("B.nr_placa", placaVeiculo, placaVeiculo != null);
			searchCriterios.addCriteriosEqualInteger("D.cd_situacao_documento", situacaoDocumento, situacaoDocumento > 0);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			return ResponseFactory.ok(aitMovimentoDocumentoService.findProtocolos(searchCriterios));
		} 
		catch (NoContentException e) { 
			return ResponseFactory.noContent(e.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
 	}
	
	@POST
	@Path("/documentos/resultado/defesa")
	@ApiOperation(value = "Lançamento de Resultados")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Resultado Criado", response = AitMovimentoDocumentoDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do resultado", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response resultadoDefesa(AitMovimentoDocumentoDTO documento) {
		try {
			documento = aitMovimentoDocumentoService.saveResultado(documento);
			return ResponseFactory.created(documento);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (ValidationException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
 	}
	
	
	@POST
	@Path("/documentos/processos/print")
	@ApiOperation(value = "Busca Relatório de Documentos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Relatório gerado", response = byte[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Produces("application/pdf")
	public Response getRelatorioProcessos(
			@ApiParam(value = "Tipo de Documento") @QueryParam("tipoDocumento") @DefaultValue("0") int cdTipoDocumento,
			@ApiParam(value = "Data Inicial") @QueryParam("dataInicial") String dtInicial,
			@ApiParam(value = "Data Final") @QueryParam("dataFinal") String dtFinal,
			@ApiParam(value = "Fase do Documento") @QueryParam("fase") @DefaultValue("0") int cdFase,
			@ApiParam(value = "Data de Publicacao") GregorianCalendar dtPublicacao
			
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosGreaterDate("D.dt_protocolo", dtInicial, dtInicial != null);
			searchCriterios.addCriteriosMinorDate("D.dt_protocolo", dtFinal, dtFinal != null);
			searchCriterios.addCriteriosEqualInteger("F.cd_fase", cdFase, cdFase > 0);
			searchCriterios.addCriteriosEqualInteger("E.cd_tipo_documento", cdTipoDocumento, cdTipoDocumento > 0);
			List<DocumentoProcesso> listDocumentoProcessos = aitMovimentoDocumentoService.getMovimentosFromDTO(searchCriterios);
			aitMovimentoservice.setDtPublicacaoDO(listDocumentoProcessos, dtPublicacao);
			return ResponseFactory.ok(aitMovimentoDocumentoService.printRelatorioProcessos(listDocumentoProcessos, dtPublicacao, searchCriterios));
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
	
	@PUT
	@Path("/fici")
	@ApiOperation(value = "Atualização de Apresentação de Condutor")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Apresentação de Condutor atualizada", response = Ait[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response updateApresentacaoDeCondutor(
			@ApiParam(value = "Documento DTO") AitMovimentoDocumentoDTO documento) {
		try {
			return ResponseFactory.ok(aitMovimentoDocumentoService.updateFici(documento));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@POST
	@Path("/protocolo")
	@ApiOperation(value = "Criação de Protocolo")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolo Inserido", response = Ait[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response createProtocolo(
			@ApiParam(value = "Documento DTO") AitMovimentoDocumentoDTO documento) {
		try {
			return ResponseFactory.ok(aitMovimentoDocumentoService.updateFici(documento));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@POST
	@Path("/cancelamento/fici")
	@ApiOperation(value = "Cancelamento de FICI")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "FICI cancelada", response = Ait[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response cancelamentoFici(
			@ApiParam(value = "Documento DTO") AitMovimentoDocumentoDTO documento) {
		try {
			return ResponseFactory.ok(aitMovimentoDocumentoService.cancelaFici(documento));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
}
