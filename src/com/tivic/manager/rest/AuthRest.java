package com.tivic.manager.rest;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.acd.Aluno;
import com.tivic.manager.acd.Instituicao;
import com.tivic.manager.acd.InstituicaoDAO;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.seg.Modulo;
import com.tivic.manager.seg.ModuloServices;
import com.tivic.manager.rest.auth.AuthController;
import com.tivic.manager.rest.auth.Credencial;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDTO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.Util;
import com.tivic.sol.auth.jwt.JWT;
import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ResultSetMap;
import sol.util.RestData;
import sol.util.Result;

@Path("/auth/")
public class AuthRest {
	
	
	
	@Context
	private HttpServletResponse servletResponse;   
	
	@POST
	@Path("/method")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTIgnore
	public String getMethod(){
		try {
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA") != null && Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			boolean lgRadarServer = Util.getConfManager().getProps().getProperty("LG_RADAR_SERVER") != null && Util.getConfManager().getProps().getProperty("LG_RADAR_SERVER").equals("1");
			boolean lgLoginModulo = Util.getConfManager().getProps().getProperty("LG_LOGIN_MODULO") != null && Util.getConfManager().getProps().getProperty("LG_LOGIN_MODULO").equals("1");
			
			Result result = new Result(1);
			result.addObject("METHOD", lgBaseAntiga ? "auth/login/old" : (lgLoginModulo ? "auth/loginEdf" : "auth/login"));
			result.addObject("ENCRYPTION", lgBaseAntiga);
			result.addObject("LG_RADAR_SERVER", lgRadarServer);
			
			return new JSONObject(result).toString();
		} catch(Exception e) {
	    	e.printStackTrace();
	    	return null;
		}
	}
	
	/**
	 * @see AuthController#edfLogin(com.tivic.manager.rest.auth.Credencial) -- @mauriciocordeiro
	 */
	@POST
	@Path("/edf")
	@JWTIgnore
	public Response authEdf(String body){
		try {	    				
			
			JSONObject json = new JSONObject(body);
			
			String nmLogin = String.valueOf(json.get("nmLogin"));
			String nmSenha = String.valueOf(json.get("nmSenha"));
			String idModulo = String.valueOf(json.get("idModulo"));
			
			Result login = UsuarioServices.autenticar(nmLogin, nmSenha, idModulo);			
			if(login.getCode() < 0){
				ResponseBuilder rBuild = Response.status(Integer.parseInt(String.valueOf(login.getObjects().get("httpStatusCode"))));
				return rBuild
						.type(MediaType.APPLICATION_JSON)
						.entity("{\"message\": \""+login.getMessage()+"\"}")
						.build();
				///return Response.status(Integer.parseInt(String.valueOf(login.getObjects().get("httpStatusCode"))), login.getMessage()).type(MediaType.APPLICATION_JSON).build();
			}
			String token = (String) login.getObjects().get("AUTHORIZATION");
			Usuario usuario = (Usuario)login.getObjects().get("USUARIO");
			//usuario.setPessoa((Pessoa)login.getObjects().get("PESSOA"));
			usuario.setToken(token);			
			this.servletResponse.addHeader("Authorization", token);
	    	return Response.ok(new JSONObject(usuario.toString()).toString()).build();
		} catch(Exception e) {
	    	e.printStackTrace();
	    	return Response.serverError().build();
		}
	}
	
	@POST
	@Path("/loginEdfAluno")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.APPLICATION_JSON)
	@JWTIgnore
	public Response authEdfAluno(String body){
         try {	    				
			
			JSONObject json = new JSONObject(body);
			
			System.out.println(json);
			
			String nmLogin = String.valueOf(json.get("nmLogin"));
			String nmSenha = String.valueOf(json.get("nmSenha"));
			String idModulo = String.valueOf(json.get("idModulo"));
			
			
			
			Result login = UsuarioServices.autenticar(nmLogin, nmSenha, idModulo);
			
	    	return Response.ok(new JSONObject(login).toString()).build();
		} catch(Exception e) {
	    	e.printStackTrace();
	    	return null;
		}
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTIgnore
	public Response auth(Usuario usuario){
		try {	    				
			
			Result login = UsuarioServices.autenticar(usuario.getNmLogin(), usuario.getNmSenha());
			
			String token = (String) login.getObjects().get("AUTHORIZATION");
			
			//login.getObjects().remove("AUTHORIZATION");
			
			this.servletResponse.addHeader("Authorization", token);
			
	    	return Response.ok(new JSONObject(login).toString()).build();
		} catch(Exception e) {
	    	e.printStackTrace();
	    	return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/login/old")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTIgnore
	public String authOld(Usuario usuario) {
		try {
			Result login = UsuarioServices.autenticarBaseAntiga(usuario.getNmLogin(), usuario.getNmSenha(), null);
			String token = (String) login.getObjects().get("AUTHORIZATION");

			login.getObjects().remove("AUTHORIZATION");

			this.servletResponse.addHeader("Authorization", token);

			login.addObject("PARAMETROS", com.tivic.manager.str.AgenteServices.getParametros(null));
			return new JSONObject(login).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@POST
	@Path("/logout")
	@ApiOperation(value = "Logout do eTransito")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok", response = UsuarioDTO.class),
			@ApiResponse(code = 400, message = "Erro na requisição", response = ResponseBody.class),
			@ApiResponse(code = 401, message = "Usuário não autorizado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Consumes(MediaType.APPLICATION_JSON)
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
	@Path("/access_token")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTIgnore
	public Response accessToken(){
		try {	    				
			/*
			 * Authorization Token
			 */
			HashMap<String, Object> headers = new HashMap<String, Object>();
			HashMap<String, Object> payload = new HashMap<String, Object>();
			GregorianCalendar issue = new GregorianCalendar();
			issue.add(Calendar.MINUTE, 60);
			
			headers.put("exp", issue);
			headers.put("sub", "tmp_token");

			JWT jwt = (JWT) BeansFactory.get(JWT.class);
			this.servletResponse.addHeader("Authorization", jwt.generate(headers, payload));
			
	    	return Response.ok().build();
		} catch(Exception e) {
	    	e.printStackTrace();
	    	return null;
		}
	}

}
