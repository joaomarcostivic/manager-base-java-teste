package com.tivic.manager.acd;

import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.RestData;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.util.Util;

@Path("/acd/instituicaoperiodo")

public class InstituicaoPeriodoRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(RestData restData){
		try {
			
			InstituicaoPeriodo instituicaoPeriodo = (InstituicaoPeriodo)(restData.getArg("instituicaoPeriodo"));
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			
			Result result = InstituicaoPeriodoServices.save(instituicaoPeriodo, cdUsuario);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(InstituicaoPeriodo instituicaoPeriodo){
		try {
			Result result = InstituicaoPeriodoServices.remove(instituicaoPeriodo.getCdPeriodoLetivo());
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
			ResultSetMap rsm = InstituicaoPeriodoServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getAllPrincipal")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllPrincipal() {
		try {
			ResultSetMap rsm = InstituicaoPeriodoServices.getAllPrincipal();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/find")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String find(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = InstituicaoPeriodoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/{cdPeriodoLetivo}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response get(@PathParam("cdPeriodoLetivo") int cdPeriodoLetivo) {
		try {
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo);
			return Response.ok(new JSONObject(instituicaoPeriodo.toORM()).toString()).build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Response.serverError().build();
		}
	}
	
	@POST
	@Path("/getallprincipalatualrecente")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllPrincipalAtualRecente() {
		try {
			ResultSetMap rsm = InstituicaoPeriodoServices.getAllPrincipalAtualRecente();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getperiodoofinstituicao")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getPeriodoOfInstituicao(RestData restData) {
		try {
			
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			
			ResultSetMap rsm = InstituicaoPeriodoServices.getPeriodoOfInstituicao(cdInstituicao);
			
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/progressaomatricula9ano")
	@Produces(MediaType.APPLICATION_JSON)
	public static String progressaoMatricula9Ano(RestData restData) {
		try {
			
			int cdMatricula = Integer.parseInt(String.valueOf(restData.getArg("cdMatricula")));
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			
			Result result = InstituicaoPeriodoServices.progressaoMatricula9Ano(cdMatricula, cdUsuario);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
