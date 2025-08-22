package com.tivic.manager.seg;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.RestData;
import sol.util.Result;

@Path("/seg/usuario/")

public class UsuarioRest {

	@Context
	private HttpServletResponse servletResponse;   
	
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Usuario usuario){
		try {
			Result result = UsuarioServices.save(usuario);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll() {
		try {
			ResultSetMap rsm = UsuarioServices.getAll();
			return Util.rsmToJSONSync(rsm);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(Usuario usuario){
		try {
			Result result = UsuarioServices.remove(usuario.getCdUsuario());
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/cadastrar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cadastrar(RestData args){
		try {
			ObjectMapper objectMapper   = new ObjectMapper();
			PessoaFisica pessoaFisica  = objectMapper.convertValue(args.getArg("pessoaFisica"), PessoaFisica.class);
			Usuario usuario  = objectMapper.convertValue(args.getArg("usuario"), Usuario.class);
			usuario.setTpUsuario(1);
			
			Result result = UsuarioServices.cadastrar(pessoaFisica, usuario);
			String token = (String) result.getObjects().get("AUTHORIZATION");
			
			result.getObjects().remove("AUTHORIZATION");
			
			this.servletResponse.addHeader("Authorization", token);
			
	    	return Response.ok(new JSONObject(result).toString()).build();
		} catch(Exception e) {
	    	e.printStackTrace();
	    	return null;
		}
	}
	
	@POST
	@Path("/autenticar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String autenticar(RestData args){
		try {
			ObjectMapper objectMapper   = new ObjectMapper();
			
			String nmLogin  = objectMapper.convertValue(args.getArg("userlogin"), String.class);
			String nmSenha  = objectMapper.convertValue(args.getArg("userpassword"), String.class);
			nmLogin = nmLogin.replaceAll("[^0-9]", "");
			
			Result result = UsuarioServices.autenticar(nmLogin, nmSenha);
			
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return new JSONObject(new Result(e.hashCode()*-1, e.getMessage())).toString();
		}
	}
	
	@POST
	@Path("/deslogar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String deslogar(Usuario usuario){
		try {
			int cdUsuario = UsuarioServices.retirarEquipamento(usuario.getCdUsuario());
			Result result = new Result(cdUsuario, cdUsuario > 0 ? "Usuário deslogado com sucesso." : "Erro ao deslogar usuário.");
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return new JSONObject(new Result(-1, "Erro ao deslogar usuário.")).toString();
		}
	}

	@POST
	@Path("/find")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String find(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = UsuarioServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getModulosByUsuario")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getModulosByUsuario(Usuario usuario) {
		try {
			ResultSetMap rsm = UsuarioServices.getModulosByUsuario(usuario.getCdUsuario());
			return Util.rsmToJSONSync(rsm);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	@POST
	@Path("/addPermissaoModulo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String addPermissaoModulo(RestData restData) {
		try {
			
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			int cdModulo = Integer.parseInt(String.valueOf(restData.getArg("cdModulo")));
			int cdSistema = Integer.parseInt(String.valueOf(restData.getArg("cdSistema")));
			int lgNatureza = Integer.parseInt(String.valueOf(restData.getArg("lgNatureza")));
			
			Result result = new Result(UsuarioServices.addPermissaoModulo(cdUsuario, cdModulo, cdSistema, lgNatureza));
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/dropPermissaoModulo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String dropPermissaoModulo(RestData restData) {
		try {
			
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			int cdModulo = Integer.parseInt(String.valueOf(restData.getArg("cdModulo")));
			int cdSistema = Integer.parseInt(String.valueOf(restData.getArg("cdSistema")));
			
			Result result = new Result(UsuarioServices.dropPermissaoModulo(cdUsuario, cdModulo, cdSistema));
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/testconn")
	@Produces(MediaType.APPLICATION_JSON)
	public static String checkConnect() {
		try {
			
			
			return "{\"response\": \"Success\"}";
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	
	@GET
	@Path("/remoteaddr")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getRemoteAddr(@Context HttpServletRequest req) {
		try {			
			return "{\"response\": \""+req.getRemoteAddr()+"\"}";
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getempresaofusuariomodulo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getEmpresaOfUsuarioModulo(RestData restData) {
		try {
			
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			int cdSistema = Integer.parseInt(String.valueOf(restData.getArg("cdSistema")));
			int cdModulo = Integer.parseInt(String.valueOf(restData.getArg("cdModulo")));
			int cdEmpresa = Integer.parseInt(String.valueOf(restData.getArg("cdEmpresa")));
			String idModulo = String.valueOf(restData.getArg("idModulo"));
			int tpModulo = Integer.parseInt(String.valueOf(restData.getArg("tpModulo")));
			
			ResultSetMap rsm = UsuarioServices.getEmpresaOfUsuarioModulo(cdUsuario, cdSistema, cdModulo, false, cdEmpresa, idModulo, tpModulo);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/permissoes/{cdUsuario}/{cdSistema}/{cdModulo}/{cdAgrupamento}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getPermissoes(@PathParam("cdUsuario") int cdUsuario, @PathParam("cdSistema") int cdSistema, @PathParam("cdModulo") int cdModulo, @PathParam("cdAgrupamento") int cdAgrupamento) {
		try {			
			return Util.rsmToJSON(UsuarioServices.getPermissoesAcao(cdUsuario, cdSistema, cdModulo, cdAgrupamento));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@GET
	@Path("/permissoes/{cdUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getPermissoes(@PathParam("cdUsuario") int cdUsuario) {
		try {			
			return Util.rsmToJSON(UsuarioServices.getPermissoesAcao(cdUsuario, 1, 0, 0));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	
	@GET
	@Path("/pessoa/{cdPessoa}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getUsuarioByPessoa(@PathParam("cdPessoa") int cdPessoa) {
		try {
			
			Result result = UsuarioServices.getUsuarioByPessoa(cdPessoa);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/tipos")
	@Produces(MediaType.APPLICATION_JSON)
	public static String[] getTipos() {
		try {			
			return UsuarioServices.tiposUsuario;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
}
