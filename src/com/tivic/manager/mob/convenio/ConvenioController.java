package com.tivic.manager.mob.convenio;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

@Api(value = "Controller de  Convênio", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/convenio")
@Produces(MediaType.APPLICATION_JSON)
public class ConvenioController {
	
	private ManagerLog managerLog;
	private IConvenioService convenioService;
	
	public ConvenioController() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		convenioService = (IConvenioService) BeansFactory.get(IConvenioService.class);
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Inserção de Convênio")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Registros salvos", response = ResponseBody.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response insertConvenio(
			@ApiParam(name = "convenio", value = "Objeto para inserir convênio") Convenio convenio) {
		try {	
			managerLog.showLog(new InfoLogBuilder("[POST]", "Inserindo Convênio...").build());
			convenioService.insert(convenio);;
			managerLog.showLog(new InfoLogBuilder("[POST]", "Convênio inserido com sucesso.").build());
			return ResponseFactory.created(convenio);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("")
	@ApiOperation(value = "Atualização de Convênio")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Registros salvos", response = ResponseBody.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response updateConvenio(
			@ApiParam(name = "convenio", value = "Objeto para atualizar convênio") Convenio convenio) {
		try {	
			managerLog.showLog(new InfoLogBuilder("[PUT]", "Atualizando Convênio...").build());
			convenioService.update(convenio);
			managerLog.showLog(new InfoLogBuilder("[PUT]", "Convênio atualizado com sucesso.").build());
			return ResponseFactory.created(convenio);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdConvenio}")
	@ApiOperation(value = "Busca por um convênio")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Registro encontrado", response = ResponseBody.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getConvenio(
			@ApiParam(name = "cdConvenio",value = "Código identificador do convênio") @PathParam("cdConvenio") int cdConvenio) {
		try {	
			managerLog.showLog(new InfoLogBuilder("[GET]", "Buscando convênio...").build());
			Convenio convenio = convenioService.get(cdConvenio);
			managerLog.showLog(new InfoLogBuilder("[GET]", "Convênio encontrado com sucesso.").build());
			return ResponseFactory.created(convenio);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Busca por convênios")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Registros encontrados", response = ResponseBody.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response findConvenio(
			@ApiParam(name = "nmConvenio",value = "Nome do convênio") @QueryParam("nmConvenio") String nmConvenio,
			@ApiParam(name = "lgDefault",value = "idenrificador do convênio default") @QueryParam("lgDefault") @DefaultValue("-1") int lgDefault) {
		try {	
			managerLog.showLog(new InfoLogBuilder("[GET]", "Buscando convênios...").build());
			
			SearchCriterios searchCriterios = new ConvenioSearchBuilder()
					.setNmConvenio(nmConvenio)
					.setlgDefaul(lgDefault)
					.build();
			List<Convenio> convenio = convenioService.find(searchCriterios);
			managerLog.showLog(new InfoLogBuilder("[GET]", "Convênios encontrado com sucesso.").build());
			return ResponseFactory.created(convenio);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("default/{cdConvenio}")
	@ApiOperation(value = "Atualização de Convênio default")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Registro atualizado", response = ResponseBody.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response setDefault(
			@ApiParam(name = "cdConvenio",value = "Código identificador do convênio") @PathParam("cdConvenio") int cdConvenio) {
		try {	
			managerLog.showLog(new InfoLogBuilder("[PUT]", "Atualizando Convênio default...").build());
			Convenio convenio = convenioService.applyConvenioDefault(cdConvenio);
			managerLog.showLog(new InfoLogBuilder("[PUT]", "Convênio default atualizado com sucesso.").build());
			return ResponseFactory.ok("Atualizado! O Convênio "+convenio.getNmConvenio()+" é o padrão." );
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
