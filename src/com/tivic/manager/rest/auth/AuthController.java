package com.tivic.manager.rest.auth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.acd.InstituicaoDTO;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.seg.Modulo;
import com.tivic.manager.seg.ModuloServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDTO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.sol.auth.jwt.JWT;
import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.manager.str.AuthPortalService;
import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "Auth", tags = {"auth"})
@Path("/v2")
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {
	
	@POST
	@Path("/mob/login")
	@ApiOperation(
			value = "Login do eTransito"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = UsuarioDTO.class),
			@ApiResponse(code = 400, message = "Erro na requisição", response = ResponseBody.class),
			@ApiResponse(code = 401, message = "Usuário não autorizado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	@JWTIgnore
	public static Response mobLogin(Credencial credencial) {
		try {
			
			Result result = UsuarioServices.mobAutenticar(credencial.getUsuario(), credencial.getSenha(), null);
			
			if(result.getCode() == UsuarioServices.ERR_NAO_IDENTIFICADO)
				return ResponseFactory.internalServerError(result.getMessage());
			else if(result.getCode() < 0)
				return ResponseFactory.unauthorized(result.getMessage());
			
			Usuario usuario = (Usuario) result.getObjects().get("USUARIO");
			Agente agente = (Agente) result.getObjects().get("AGENTE");
			Pessoa pessoa = (Pessoa) result.getObjects().get("PESSOA");
						
			UsuarioDTO dto = new UsuarioDTO.Builder(usuario).setPessoa(pessoa).setAgente(agente).setStLogin(usuario.getStLogin()).build();
			
			return ResponseFactory.ok(dto);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}
	
	@POST
	@Path("/mob/logout")
	@ApiOperation(
			value = "Logout do eTransito"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = UsuarioDTO.class),
			@ApiResponse(code = 400, message = "Erro na requisição", response = ResponseBody.class),
			@ApiResponse(code = 401, message = "Usuário não autorizado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	@JWTIgnore
	public static Result mobLogout(Credencial credencial) {
		try {
			
			Result result = UsuarioServices.mobLogout(credencial.getUsuario(), null);
			return result;
			
		} catch (Exception e) {
			
			e.printStackTrace(System.out);
			
			return new Result(-1, "Não é possível efetuar logout");
		}
	}
	
	@POST
	@Path("/login")
	@ApiOperation(
			value = "Login Geral"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = UsuarioDTO.class),
			@ApiResponse(code = 400, message = "Erro na requisição", response = ResponseBody.class),
			@ApiResponse(code = 401, message = "Usuário não autorizado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	@JWTIgnore
	public static Response login(Credencial credencial) {
		try {
			
			Result result = UsuarioServices.autenticar(credencial.getUsuario(), credencial.getSenha(), null);
			
			if(result.getCode() == UsuarioServices.ERR_NAO_IDENTIFICADO)
				return ResponseFactory.internalServerError(result.getMessage());
			else if(result.getCode() < 0)
				return ResponseFactory.unauthorized(result.getMessage());
			
			Usuario usuario = (Usuario) result.getObjects().get("USUARIO");
			Pessoa pessoa = (Pessoa) result.getObjects().get("PESSOA");
						
			UsuarioDTO dto = new UsuarioDTO.Builder(usuario).setPessoa(pessoa).build();
			
			return ResponseFactory.ok(dto);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}
	
	@POST
	@Path("/edf/login")
	@ApiOperation(
			value = "Login do EDF"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = UsuarioDTO.class),
			@ApiResponse(code = 400, message = "Erro na requisição", response = ResponseBody.class),
			@ApiResponse(code = 401, message = "Usuário não autorizado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	@JWTIgnore
	public static Response edfLogin(Credencial credencial) {
		try {
			Result result = UsuarioServices.autenticar(credencial.getUsuario(), credencial.getSenha(), credencial.getModulo());
			if(result.getCode() == UsuarioServices.ERR_DENIED_LOGIN_INVALIDO)
				return ResponseFactory.unauthorized("Login inválido");
			else if(result.getCode() == UsuarioServices.ERR_DENIED_SENHA_INVALIDA)
				return ResponseFactory.unauthorized("Senha inválida");
			else if(result.getCode() == UsuarioServices.ERR_DENIED_USUARIO_MODULO)
				return ResponseFactory.unauthorized("Usuário sem acesso ao módulo");
			
			Usuario _usuario = (Usuario)result.getObjects().get("USUARIO");
			Pessoa pessoa = (Pessoa) result.getObjects().get("PESSOA");
			UsuarioDTO usuario = new UsuarioDTO.Builder(_usuario).setPessoa(pessoa).build();
			
			return ResponseFactory.ok(usuario);
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/getempresasofusuariomodulo")
	@ApiOperation(
			value = "Busca de empresas"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = UsuarioDTO.class),
			@ApiResponse(code = 400, message = "Erro na requisição", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Nenhuma empresa encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getEmpresaOfUsuarioModulo(
		@QueryParam("cdUsuario") int cdUsuario, 
		@QueryParam("idModulo") String idModulo
	){
		try {	
			
			if(cdUsuario <= 0) 
				return ResponseFactory.badRequest("Código do usuário é nulo ou inválido");
			if(idModulo.equals("")) 
				return ResponseFactory.badRequest("ID do módulo é nulo ou inválido");
			
			Modulo modulo = ModuloServices.getModuloById(idModulo);
			ResultSetMap rsm = UsuarioServices.getEmpresaOfUsuarioModulo(cdUsuario, modulo.getCdSistema(), modulo.getCdModulo(), false, 0, idModulo, 0, null);
			if(rsm == null){
				return ResponseFactory.noContent("Nenhuma empresa encontrada");
			}
			
			ArrayList<InstituicaoDTO> instituicaoDto = (ArrayList<InstituicaoDTO>)new InstituicaoDTO.ListBuilder(rsm).setEmpresa(rsm).build();
			return ResponseFactory.ok(instituicaoDto);
		} catch(Exception e) {
	    	e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/login/access-token")
	@JWTIgnore
	@ApiOperation(
			value = "Busca de empresas"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = UsuarioDTO.class),
			@ApiResponse(code = 400, message = "Erro na requisição", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Nenhuma empresa encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAccessToken() {
		try {
			String token = new AuthPortalService().authEdat();
	    	return Response.ok().header("Authorization", token).build();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
