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

@Path("/acd/aulatopico/")

public class AulaTopicoRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(AulaTopico aulaTopico){
		try {
			Result result = AulaTopicoServices.save(aulaTopico);
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
	public String remove(AulaTopico aulaTopico){
		try {
			Result result = AulaTopicoServices.remove(aulaTopico);
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
			ResultSetMap rsm = AulaTopicoServices.getAll();
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
			ResultSetMap rsm = AulaTopicoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/getaulastopico")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static String getAulasTopico(RestData restData) {
		try {
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdCurso = Integer.parseInt(String.valueOf(restData.getArg("cdCurso")));
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdDisciplina = (restData.getArg("cdDisciplina") != null ? Integer.parseInt(String.valueOf(restData.getArg("cdDisciplina"))) : 0);
			int cdAula = Integer.parseInt(String.valueOf(restData.getArg("cdAula")));
			
			ResultSetMap rsm = AulaTopicoServices.getAulasTopico(cdTurma, cdCurso, cdProfessor, cdDisciplina, cdAula);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getaulastopicobyaula")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static String getAulasTopicoByAula(RestData restData) {
		try {
			int cdAula = Integer.parseInt(String.valueOf(restData.getArg("cdAula")));
			
			ResultSetMap rsm = AulaTopicoServices.getAulasTopicoByAula(cdAula);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}