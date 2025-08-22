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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import com.tivic.manager.util.Util;

@Path("/acd/professordisciplina/")

public class ProfessorDisciplinaRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(ProfessorDisciplina professorDisciplina){
		try {
			Result result = ProfessorDisciplinaServices.save(professorDisciplina);
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
	public String remove(ProfessorDisciplina professorDisciplina){
		try {
			Result result = ProfessorDisciplinaServices.remove(professorDisciplina.getCdProfessor(), professorDisciplina.getCdDisciplina());
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
			ResultSetMap rsm = ProfessorDisciplinaServices.getAll();
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
			ResultSetMap rsm = ProfessorDisciplinaServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getdisciplinasbyprofessor")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getDisciplinasByProfessor(RestData restData) {
		try {
			
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			
			ResultSetMap rsm = ProfessorDisciplinaServices.getDisciplinasByProfessor(cdProfessor);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}