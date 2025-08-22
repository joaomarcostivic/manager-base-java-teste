package com.tivic.manager.tasks.sincronizacao.tabelas.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.tasks.sincronizacao.tabelas.services.ITaskSincronizacaoTabelas;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



@Api(value = "Sincronizacao", tags = { "fta" })
@Path("/v3/fta/sincronizacao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SincronizacaoController {
	
	private ITaskSincronizacaoTabelas syncService;
	
	public SincronizacaoController() throws Exception {
		this.syncService = (ITaskSincronizacaoTabelas) BeansFactory.get(ITaskSincronizacaoTabelas.class);
	}
	@POST
	@Path("/geral")
	@ApiOperation(value = "Sincronização de Características de Veículos.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Características de Veículos Sincronizadas"),
            @ApiResponse(code = 400, message = "Erro na Sincronização de Características de Veículos", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response syncAll() {
		try {
			syncService.syncAll();
			return ResponseFactory.ok("Sincronização Completa Realizada");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cores")
	@ApiOperation(value = "Sincronização de Cores de Veículos.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Cores de Veículos Sincronizadas"),
            @ApiResponse(code = 400, message = "Erro na Sincronização de Cores de Veículos", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response syncCores() {
		try {
			syncService.syncCores();
			return ResponseFactory.ok("Cores Sincronizadas");
		} catch (IllegalStateException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/categorias")
	@ApiOperation(value = "Sincronização de Categorias de Veículos.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Características de Categorias Sincronizadas"),
            @ApiResponse(code = 400, message = "Erro na Sincronização de Categorias de Veículos", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response syncCategorias() {
		try {
			syncService.syncCategorias();
			return ResponseFactory.ok("Categorias de Veículos Sincronizadas");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/especies")
	@ApiOperation(value = "Sincronização de Especies de Veículos.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Características de Especies Sincronizadas"),
            @ApiResponse(code = 400, message = "Erro na Sincronização de Especies de Veículos", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response syncEspecies() {
		try {
			syncService.syncEspecies();
			return ResponseFactory.ok("Especies de Veículos Sincronizadas");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cidades")
	@ApiOperation(value = "Sincronização de Características de Veículos.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Cidades Sincronizadas"),
            @ApiResponse(code = 400, message = "Erro na Sincronização de Cidades", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response syncCidades() {
		try {
			syncService.syncCidades();
			return ResponseFactory.ok("Cidades Sincronizadas");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/marcas")
	@ApiOperation(value = "Sincronização de Marca/Modelo de Veículos.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Características de Marca/Modelo Sincronizadas"),
            @ApiResponse(code = 400, message = "Erro na Sincronização de Marca/Modelo de Veículos", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response syncMarcas() {
		try {
			syncService.syncMarcas();
			return ResponseFactory.ok("Marca/Modelos Sincronizadas");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/tipos")
	@ApiOperation(value = "Sincronização de Tipo de Veículos.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Tipos de Veículo Sincronizadas"),
            @ApiResponse(code = 400, message = "Erro na Sincronização de Tipos de Veículos", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response syncTipos() {
		try {
			syncService.syncTipos();
			return ResponseFactory.ok("Tipos de Veículos Sincronizadas");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/infracoes")
	@ApiOperation(value = "Sincronização de Infracoes.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Infrações Sincronizadas"),
            @ApiResponse(code = 400, message = "Erro na Sincronização de Infrações", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response syncInfracoes() {
		try {
			syncService.syncInfracoes();
			return ResponseFactory.ok("Infrações Sincronizadas");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
