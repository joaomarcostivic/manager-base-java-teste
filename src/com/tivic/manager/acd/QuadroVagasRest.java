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

@Path("/acd/quadrovagas/")

public class QuadroVagasRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(QuadroVagas quadroVagas){
		try {
			Result result = QuadroVagasServices.save(quadroVagas);
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
	public String remove(QuadroVagas quadroVagas){
		try {
			Result result = QuadroVagasServices.remove(quadroVagas.getCdQuadroVagas(), quadroVagas.getCdInstituicao());
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
			ResultSetMap rsm = QuadroVagasServices.getAll();
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
			ResultSetMap rsm = QuadroVagasServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getquadrovagasatual")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getQuadroVagasAtual(RestData restData) {
		try {
			
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdCurso = Integer.parseInt(String.valueOf(restData.getArg("cdCurso")));
			
			ResultSetMap rsm = QuadroVagasServices.getQuadroVagasAtual(cdInstituicao, cdCurso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getquadrovagasrecente")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getQuadroVagasRecente(RestData restData) {
		try {
			
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdCurso = Integer.parseInt(String.valueOf(restData.getArg("cdCurso")));
			
			ResultSetMap rsm = QuadroVagasServices.getQuadroVagasRecente(cdInstituicao, cdCurso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
