package com.tivic.manager.seg.usuario;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
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

import com.tivic.manager.seg.UsuarioPessoaDTO;
import com.tivic.manager.seg.usuario.builders.UsuarioPessoaSearchBuilder;
import com.tivic.sol.auth.usuario.IUsuarioService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.task.Task;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "UsuarioPessoa", tags = { "sis" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v3/seg/usuariopessoa")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioPessoaController {
	
	private IUsuarioPessoaService usuarioPessoaService;
	private IUsuarioService usuarioService;
	private ManagerLog managerLog;

	public UsuarioPessoaController() throws Exception {
		this.usuarioPessoaService = (IUsuarioPessoaService) BeansFactory.get(IUsuarioPessoaService.class);
		this.usuarioService = (IUsuarioService) BeansFactory.get(IUsuarioService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@POST
	@Path("/")
	@ApiOperation(value = "Registra um novo usuario")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Usuário registrado", response = Task.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response create(@ApiParam(value = "Usuário a ser registrado") UsuarioPessoaDTO usuarioPessoaDTO) {
		try {
			this.usuarioPessoaService.insert(usuarioPessoaDTO);
			return ResponseFactory.ok(usuarioPessoaDTO);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/")
	@ApiOperation(value = "Atualiza um usuario")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Usuário atualizado", response = Task.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response update(@ApiParam(value = "Usuário a ser atualizado") UsuarioPessoaDTO usuarioPessoaDTO) {
		try {
			this.usuarioPessoaService.update(usuarioPessoaDTO);
			return ResponseFactory.ok(usuarioPessoaDTO);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{cdUsuario}/pass")
	@ApiOperation(value = "Atualiza um usuario")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Usuário atualizado", response = Task.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response changePassword(
		@PathParam(value = "cdUsuario") int cdUsuario,
		@ApiParam(value = "Nova senha") String novaSenha
	) {
		try {
			this.usuarioService.changePassword(cdUsuario, novaSenha);
			return ResponseFactory.ok("Senha atualizada com sucesso");
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{cdUsuario}")
	@ApiOperation(value = "Busca um usuario")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Usuário buscado", response = Task.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response get(@ApiParam(value = "Código do usuário a ser buscado") @PathParam(value = "cdUsuario") int cdUsuario) {
		try {
			UsuarioPessoaDTO usuarioPessoaDTO = this.usuarioPessoaService.get(cdUsuario);
			return ResponseFactory.ok(usuarioPessoaDTO);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/")
	@ApiOperation(value = "Busca vários usuarios")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Usuários buscados", response = Task.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response find(
		@ApiParam(value = "Nome da Pessoa") @QueryParam("pessoa") String nmPessoa,
		@ApiParam(value = "Nome de Usuário") @QueryParam("usuario") String nmUsuario,
		@ApiParam(value = "Email da Pessoa") @QueryParam("email") String nmEmail
	) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("nm_login", nmUsuario, nmUsuario != null);
			searchCriterios.addCriteriosEqualString("nm_pessoa", nmPessoa);
			searchCriterios.addCriteriosEqualString("nm_email", nmEmail);
			List<UsuarioPessoaDTO> listUsuarioPessoaDTO = this.usuarioPessoaService.find(searchCriterios);
			return ResponseFactory.ok(listUsuarioPessoaDTO);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/findpessoa")
	@ApiOperation(value = "Busca vários usuarios e pessoas")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Usuários buscados", response = Task.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response findPessoa(
		@ApiParam(value = "Nome de Usuário") @QueryParam("usuario") String nmUsuario
	) {
		try {
			List<UsuarioPessoaDTO> listUsuarioPessoaDTO = this.usuarioPessoaService.findPessoas(nmUsuario);
			return ResponseFactory.ok(listUsuarioPessoaDTO);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			this.managerLog.error("Erro ao cancelar inconsistências: ", e.getMessage());
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}	
}
