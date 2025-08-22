package com.tivic.manager.mob;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.util.Result;

@Api(value = "OrgaoServico", tags = { "mob" })
@Path("/v2/mob/orgaoservico")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrgaoServicoController {
	
	private final OrgaoServicoServices _service;
	
	public OrgaoServicoController () {
		this._service = new OrgaoServicoServices();
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Registra um novo serviço de um órgão")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Serviço registrado", response = OrgaoServico.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
			})
	public Response create(@ApiParam(value = "Serviço do Orgão a ser registrado") OrgaoServico orgaoServico) {
		try {
			orgaoServico.setCdOrgaoServico(0);
			
			Result res = this._service.save(orgaoServico);
			if(res.getCode() <= 0)
				return ResponseFactory.badRequest(res.getMessage());
			
			return ResponseFactory.ok((OrgaoServico) res.getObjects().get("ORGAO_SERVICO"));
		} catch(Exception ex) {
			return this.printError(ex);
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(value = "Atualiza um serviço de um órgão")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Serviço atualizado", response = OrgaoServico.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
			})
	public Response update( @ApiParam(value = "Id do Serviço") @PathParam("id") int cdOrgaoServico,
							@ApiParam(value = "Serviço do Orgão a ser registrado") OrgaoServico orgaoServico) {
		try {			
			Result res = this._service.save(orgaoServico);
			if(res.getCode() <= 0)
				return ResponseFactory.badRequest(res.getMessage());
			
			return ResponseFactory.ok((OrgaoServico) res.getObjects().get("ORGAO_SERVICO"));
		} catch(Exception ex) {
			return this.printError(ex);
		}
	}
	
	@GET
	@Path("/orgao/{id}")
	@ApiOperation(value = "Retorna os serviços de um Órgão")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Serviço encontrado", response = OrgaoServico.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
			})
	public Response getByOrgao(@ApiParam("Id do Órgão") @PathParam("id") int cdOrgao) {
		try {
			List<OrgaoServico> _list = this._service.getByOrgao(cdOrgao);
			if(_list != null) {
				ArrayList<OrgaoServico> servicos = new ArrayList<OrgaoServico>();
				servicos.addAll(_list);
				return ResponseFactory.ok(servicos);
			}
			
			return ResponseFactory.badRequest("Não foram encontrados serviços para este órgão");
		} catch(Exception ex) {
			return this.printError(ex);
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(value = "Retorna um determinado serviço")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Serviço encontrado", response = OrgaoServico.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
			})
	public Response getByCdOrgaoServico(@ApiParam("Id do Serviço") @PathParam("id") int cdOrgaoServico) {
		try {
			List<OrgaoServico> _list = this._service.getByCdOrgaoServico(cdOrgaoServico);
			if(_list != null) {
				ArrayList<OrgaoServico> servicos = new ArrayList<OrgaoServico>();
				servicos.addAll(_list);
				return ResponseFactory.ok(servicos);
			}
			
			return ResponseFactory.badRequest("Não foram encontrados serviços");
		} catch(Exception ex) {
			return this.printError(ex);
		}
	}
	
	private Response printError(Exception ex) {
		ex.printStackTrace(System.out);
		return ResponseFactory.internalServerError(ex.getMessage());
	}

}
