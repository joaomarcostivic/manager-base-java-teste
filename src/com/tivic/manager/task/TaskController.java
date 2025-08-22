package com.tivic.manager.task;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.task.ITaskService;
import com.tivic.sol.task.Task;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Task", tags = { "sis" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v3/sis/tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaskController {

	private ITaskService taskService;
	private ITaskExecucao taskExecucao;
	private ManagerLog managerLog;

	public TaskController() throws Exception {
		this.taskService = (ITaskService) BeansFactory.get(ITaskService.class);
		this.taskExecucao = (ITaskExecucao) BeansFactory.get(ITaskExecucao.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@POST
	@Path("/")
	@ApiOperation(value = "Registra uma nova task")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Task registrada", response = Task.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response create(@ApiParam(value = "Task a ser registrada") Task task) {
		try {
			this.taskService.insert(task);
			return ResponseFactory.ok(task);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{cdTask}")
	@ApiOperation(value = "Atualiza uma task")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Task atualizada", response = Task.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response update(
			@ApiParam(value = "Codigo da task a ser atualizada") @PathParam("cdTask") int cdTask, 
			@ApiParam(value = "Task a ser atualizada") Task task) {
		try {
			task.setCdTask(cdTask);
			this.taskService.update(task);
			return ResponseFactory.ok(task);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{cdTask}")
	@ApiOperation(value = "Busca uma task")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Task buscada", response = Task.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response get(@ApiParam(value = "Codigo da task a ser buscada") @PathParam("cdTask") int cdTask) {
		try {
			Task task = this.taskService.get(cdTask);
			return ResponseFactory.ok(task);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/")
	@ApiOperation(value = "Busca uma lista de tasks")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Tasks buscadas", response = Task.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response find(
			@ApiParam(value = "Nome da task") @QueryParam("nmMethod") String nmMethod,
			@ApiParam(value = "Situação da task") @QueryParam("lgTask") Integer lgTask,
			@ApiParam(value = "Data de execução da task") @QueryParam("hrInicial") String hrInicial
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosLikeAnyString("nm_method", nmMethod, nmMethod != null);
			searchCriterios.addCriteriosEqualInteger("lg_task", lgTask, lgTask != null);
			searchCriterios.addCriteriosGreaterDate("hr_inicial", hrInicial, hrInicial != null);
			searchCriterios.addCriteriosMinorDate("hr_inicial", hrInicial, hrInicial != null);
			List<Task> tasks = this.taskService.find(searchCriterios);
			return ResponseFactory.ok(tasks);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@DELETE
	@Path("/{cdTask}")
	@ApiOperation(value = "Busca uma task")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Task buscada", response = Task.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response delete(
			@ApiParam(value = "Codigo da task a ser removida") @PathParam("cdTask") int cdTask) {
		try {
			Task task = this.taskService.delete(cdTask);
			return ResponseFactory.ok(task);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/{cdTask}")
	@ApiOperation(value = "Executa uma task")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Task em execução", response = Task.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response executar(
			@ApiParam(value = "Codigo da task a ser executada") @PathParam("cdTask") int cdTask, 
			@ApiParam(value = "Task a ser atualizada") Task task) {
		try {
			this.taskExecucao.executarTask(cdTask);
			return ResponseFactory.ok(task);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
