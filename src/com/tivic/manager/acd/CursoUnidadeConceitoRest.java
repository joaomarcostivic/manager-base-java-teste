package com.tivic.manager.acd;

import java.sql.Connection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class CursoUnidadeConceitoRest {
	
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getbyturma/{cdTurma}/{cdDisciplina}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getByTurmaDisciplina(@PathParam("cdTurma") int cdTurma, @PathParam("cdDisciplina") int cdDisciplina) {
		try {
			ResultSetMap rsmAlunos = CursoUnidadeConceitoServices.getByTurmaDisciplina(cdTurma, cdDisciplina);
			return new JSONObject(rsmAlunos).toString();
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
			ResultSetMap rsm = CursoUnidadeConceitoServices.getAll(null);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
