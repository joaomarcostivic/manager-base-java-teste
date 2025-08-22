package com.tivic.manager.acd;

import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
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

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.Util;

@Path("/acd/matriculadisciplina/")

public class MatriculaDisciplinaRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(MatriculaDisciplina matriculaDisciplina){
		try {
			Result result = MatriculaDisciplinaServices.save(matriculaDisciplina);
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
	public String remove(MatriculaDisciplina matriculaDisciplina){
		try {
			Result result = MatriculaDisciplinaServices.remove(matriculaDisciplina.getCdMatriculaDisciplina());
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
			ResultSetMap rsm = MatriculaDisciplinaServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/{cdMatricula}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAllDisciplinasByMatricula(@PathParam("cdMatricula") int cdMatricula) {
		try {
			
			ResultSetMap disciplinas = MatriculaDisciplinaServices.getAllByMatriculaSimples(cdMatricula, null);
			return ResponseFactory.ok(disciplinas);
			//return new JSONObject(disciplinas).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/getdisciplinas/{cdMatricula}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String get(@PathParam("cdMatricula") int cdMatricula) {
		try {
			
			ArrayList<MatriculaDisciplina> matriculasDisciplina = MatriculaDisciplinaDAO.getDisciplinasByMatricula(cdMatricula, null);
			
			JSONArray jsonArray = new JSONArray();
			for(MatriculaDisciplina disciplina : matriculasDisciplina) {
				jsonArray.put(new JSONObject(disciplina.toString()));
			}
			
			
			return jsonArray.toString();
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
			ResultSetMap rsm = MatriculaDisciplinaServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
