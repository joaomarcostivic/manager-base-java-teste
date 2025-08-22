package com.tivic.manager.grl.cidade;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoDTO;
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

@Api(value = "Protocolo", tags = { "grl" })
@Path("/v3/grl/cidade")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CidadeController {

	private ICidadeService cidadeService;
	private ManagerLog managerLog;
	
	public CidadeController() throws Exception {
		this.cidadeService = (ICidadeService) BeansFactory.get(ICidadeService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Cidade buscada com sucesso", response = Cidade[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = Cidade[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response find(
			@ApiParam(value = "Codigo da cidade") @QueryParam("cdCidade") int cdCidade,
			@ApiParam(value = "Nome da cidade") @QueryParam("nmCidade") String nmCidade) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.cd_cidade", cdCidade, cdCidade > 0);
			searchCriterios.addCriteriosLikeAnyString("A.nm_cidade", nmCidade, nmCidade != null);
			List<Cidade> cidadeList = this.cidadeService.find(searchCriterios);
			return ResponseFactory.ok(cidadeList);
		} catch (NoContentException ne) {
			return ResponseFactory.noContent(ne.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdCidade}")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolo externo buscado com sucesso", response = Cidade.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = Cidade.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response get(@ApiParam(value = "Código da cidade") @PathParam("cdCidade") int cdCidade) {
		try {
			Cidade cidade = this.cidadeService.get(cdCidade);
			return ResponseFactory.ok(cidade);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
}
