package com.tivic.manager.mob.inconsistencias;

import java.sql.Types;
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
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;

@Api(value = "Listagem de Inconsistências", tags = {"mob"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		}) 
})

@Path("/v3/mob/inconsistencias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class InconsistenciaController {
	
	private IInconsistenciaService inconsistenciaService;
	private IAitInconsistenciaService aitInconsistenciaService; 
	private ManagerLog managerLog;
	 
	public InconsistenciaController() throws Exception {	
		this.inconsistenciaService = (IInconsistenciaService) BeansFactory.get(IInconsistenciaService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.aitInconsistenciaService = (IAitInconsistenciaService) BeansFactory.get(IAitInconsistenciaService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
		
	@GET
	@Path("/listar")
	@ApiOperation(
			value = "Retorna AIT's que possuem movimentos com inconsistências para listagem"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Listagem de AIT's com inconsistências encontrada.", response = AitInconsistenciaDTO[].class),
			@ApiResponse(code = 204, message = "Listagem de AIT's com inconsistências não encontrada.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	}) 
	public Response buscarAitsInconsistencias(
			@ApiParam(value = "Cd. AIT") @QueryParam("cdAit") Integer cdAit,
			@ApiParam(value = "Cd. Inconsitência") @QueryParam("cdInconsistencia") Integer cdInconsistencia,
			@ApiParam(value = "Id do AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Movimento Pretendido") @QueryParam("tpStatusPretendido") @DefaultValue("-1") Integer tpStatusPretendido,
			@ApiParam(value = "Nome da Inconsistência") @QueryParam("nmInconsistencia") String nmInconsistencia,
			@ApiParam(value = "Data Inicial de Movimento") @QueryParam("dtInicialMovimento") String dtInicialMovimento,
			@ApiParam(value = "Data Final de Movimento") @QueryParam("dtFinalMovimento") String dtFinalMovimento,
			@ApiParam(value = "Data Inicial da infração") @QueryParam("dtInicialInfracao") String dtInicialInfracao,
			@ApiParam(value = "Data Final de infração") @QueryParam("dtFinalInfracao") String dtFinalInfracao,
			@ApiParam(value = "Tipo de Inconsistência") @QueryParam("tpInconsistencia") @DefaultValue("-1") Integer tpInconsistencia,
			@ApiParam(value = "Situação da Inconsistência") @QueryParam("stInconsistencia") @DefaultValue("-1") Integer stInconsistencia,
			@ApiParam(value = "Data Inicial de Resolução") @QueryParam("dtInicialResolucao") String dtInicialResolucao,
			@ApiParam(value = "Data Final de Resolução") @QueryParam("dtFinalResolucao") String dtFinalResolucao,
			@QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriterios("C.id_ait", "%"  + idAit + "%" , ItemComparator.LIKE, Types.VARCHAR, idAit != null);
			searchCriterios.addCriteriosEqualInteger("B.cd_inconsistencia", cdInconsistencia, cdInconsistencia != null);
			searchCriterios.addCriteriosEqualInteger("A.tp_status_pretendido", tpStatusPretendido, tpStatusPretendido > -1);
			searchCriterios.addCriterios("B.nm_inconsistencia", nmInconsistencia, ItemComparator.LIKE, Types.VARCHAR, nmInconsistencia != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_inclusao_inconsistencia", dtInicialMovimento, dtInicialMovimento != null);
			searchCriterios.addCriteriosMinorDate("A.dt_inclusao_inconsistencia", dtFinalMovimento, dtFinalMovimento != null);
			searchCriterios.addCriteriosGreaterDate("C.dt_infracao", dtInicialInfracao, dtInicialInfracao != null);
			searchCriterios.addCriteriosMinorDate("C.dt_infracao", dtFinalInfracao, dtFinalInfracao != null);
			searchCriterios.addCriteriosEqualInteger("B.tp_inconsistencia", tpInconsistencia, tpInconsistencia > -1);
			searchCriterios.addCriteriosEqualInteger("A.st_inconsistencia", stInconsistencia, stInconsistencia > -1);
			searchCriterios.addCriteriosGreaterDate("A.dt_resolucao_inconsistencia", dtInicialResolucao, dtInicialResolucao != null);
			searchCriterios.addCriteriosMinorDate("A.dt_resolucao_inconsistencia", dtFinalResolucao, dtFinalResolucao != null);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			return ResponseFactory.ok(inconsistenciaService.filtrarAitsComInconsistencias(searchCriterios));	
		} catch (ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/tipos")
	@ApiOperation(value = "Retorna a lista  de parâmetros de tipos de inconsistências")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Inconsistência encontrada", response= TiposInconsistenciasDTO[].class),
			@ApiResponse(code = 204, message = "Inconsistência não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	
	public Response getTipo(
			@ApiParam(value = "Código da inconsistencia") @QueryParam("cdInconsistencia") String cdInconsistencia,
			@ApiParam(value = "Nome da inconsistência") @QueryParam("nmInconsistencia") String nmInconsistencia){	
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriterios("cd_inconsistencia", cdInconsistencia, ItemComparator.LIKE, Types.VARCHAR, cdInconsistencia != null);
			searchCriterios.addCriterios("nm_inconsistencia", nmInconsistencia, ItemComparator.LIKE, Types.VARCHAR, nmInconsistencia != null);
			return ResponseFactory.ok(inconsistenciaService.buscarTiposInconsistencias(searchCriterios).getList(TiposInconsistenciasDTO.class));
		} catch (ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}	
	}
	
	@POST
	@Path("/cancelar")
	@ApiOperation(value = "Cancela inconsistências")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Inconsistência Cancelada", response= AitInconsistenciaDTO[].class),
			@ApiResponse(code = 204, message = "Inconsistência não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	
	public Response cancelarInconsistencias(
			@ApiParam(value = "Código do Ait") List<AitInconsistenciaDTO> aitInconsistenciaDTOList){	
		try {
			this.inconsistenciaService.cancelarListaInconsistencia(aitInconsistenciaDTOList);
			return ResponseFactory.ok(aitInconsistenciaDTOList);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}	
	
	@GET
	@Path("/{cdAit}/{cdInconsistencia}")
	@ApiOperation(value = "Busca de uma inconsistência")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Inconsistência encontrada", response = AitInconsistenciaDTO.class),
			@ApiResponse(code = 204, message = "Inconsistência não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public Response getAitInsonsistencia(
			@ApiParam(value = "Código do AIT") @PathParam("cdAit") Integer cdAit,
			@ApiParam(value = "Código da inconsistência") @PathParam("cdInconsistencia") Integer cdInconsistencia){
		try {
			AitInconsistenciaDTO aitInconsistencialDTO = this.aitInconsistenciaService.getAitInconsistencia(cdAit, cdInconsistencia);
			return ResponseFactory.ok(aitInconsistencialDTO);
		} catch (NoContentException e) {
			managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}
	
	@PUT
	@Path("/{cdAit}")
	@ApiOperation(value = "Atualização de inconsistência")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Inconsistência atualizada", response = AitInconsistenciaDTO.class),
			@ApiResponse(code = 204, message = "InconsistenciaDTO não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response update(@ApiParam(value = "Código do AIT") @PathParam("cdAit") Integer cdAit,
			@ApiParam(value = "Código do AIT Inconsistência") @QueryParam("cdInconsistencia") Integer cdInconsistencia,
			@ApiParam(value = "InconsistenciaDTO a ser lançado") AitInconsistenciaDTO aitInconsistenciaDTO) {
		try {
			this.aitInconsistenciaService.updateSituacao(aitInconsistenciaDTO);
			return ResponseFactory.ok(aitInconsistenciaDTO);
		} catch (BadRequestException e) {	
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/historico")
	@ApiOperation(
			value = "Retorna todas as inconsistências de um AIT"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "inconsistências do AIT encontrada.", response = AitInconsistenciaDTO[].class),
			@ApiResponse(code = 204, message = "inconsistências do AIT não encontrada.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	}) 
	public Response findInconsistenciasAit(
			@ApiParam(value = "Cd. AIT") @QueryParam("cdAit") Integer cdAit,
			@ApiParam(value = "Código da inconsistência") @QueryParam("cdInconsistencia") Integer cdInconsistencia,
			@QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, cdAit != null);
			searchCriterios.addCriteriosEqualInteger("A.cd_inconsistencia", cdInconsistencia, cdInconsistencia != null);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			return ResponseFactory.ok(aitInconsistenciaService.findInconsistenciasAit(searchCriterios));	
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
