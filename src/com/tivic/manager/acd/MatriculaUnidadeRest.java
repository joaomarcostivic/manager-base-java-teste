package com.tivic.manager.acd;

import java.sql.Types;
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

import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.Util;

@Path("/acd/matriculaunidade/")

public class MatriculaUnidadeRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(RestData restData){
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			MatriculaUnidade matriculaUnidade = objectMapper.convertValue(restData.getArg("matriculaunidade"), MatriculaUnidade.class);
			
			Result result = MatriculaUnidadeServices.save(matriculaUnidade);
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
	public String remove(MatriculaUnidade matriculaUnidade){
		try {
			Result result = MatriculaUnidadeServices.remove(matriculaUnidade);
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
			ResultSetMap rsm = MatriculaUnidadeServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static String get(RestData restData) {
		try {

			int cdMatricula = Integer.parseInt(String.valueOf(restData.getArg("cdMatricula")));
			int cdUnidade = Integer.parseInt(String.valueOf(restData.getArg("cdUnidade")));
			int cdCurso = Integer.parseInt(String.valueOf(restData.getArg("cdCurso")));
			
			MatriculaUnidade matriculaUnidade = MatriculaUnidadeDAO.get(cdMatricula, cdUnidade, cdCurso);
			
			Result result = new Result((matriculaUnidade == null ? -1 : 1), (matriculaUnidade == null ? "Erro ao encontrar matricula unidade" : "Sucesso ao encontrar matricula unidade"), "MATRICULAUNIDADE", matriculaUnidade);
			
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/getparecerunidade/{cdMatricula}/{cdUnidade}/{cdCurso}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static String getParecerUnidade(@PathParam("cdMatricula") int cdMatricula, @PathParam("cdUnidade") int cdUnidade, @PathParam("cdCurso") int cdCurso) {
		try {

			
			MatriculaUnidade parecerUnidade = MatriculaUnidadeDAO.getParecer(cdMatricula, cdUnidade, cdCurso, null);
			
		
			return new JSONObject(parecerUnidade).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/getpareceraluno/{cdMatricula}/{cdCurso}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getParecerAluno(@PathParam("cdMatricula") int cdMatricula,@PathParam("cdCurso") int cdCurso) {
		try {
	        ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			
			criterios.add(new ItemComparator("cd_matricula", "" + cdMatricula, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
			
			
			ResultSetMap parecerUnidade = MatriculaUnidadeServices.findParecer(criterios, null);
			
		
			return ResponseFactory.ok(parecerUnidade);
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
			ResultSetMap rsm = MatriculaUnidadeServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}