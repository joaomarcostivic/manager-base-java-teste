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

@Path("/acd/ofertahorario/")

public class OfertaHorarioRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(RestData restData){
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			OfertaHorario ofertaHorario = objectMapper.convertValue(restData.getArg("ofertaHorario"), OfertaHorario.class);
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			
			Result result = OfertaHorarioServices.save(ofertaHorario, cdInstituicao);
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
	public String remove(OfertaHorario ofertaHorario){
		try {
			Result result = OfertaHorarioServices.remove(ofertaHorario.getCdHorario(), ofertaHorario.getCdOferta());
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
			ResultSetMap rsm = OfertaHorarioServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallbyoferta")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByOferta(RestData restData) {
		try {
			
			int cdOferta = Integer.parseInt(String.valueOf(restData.getArg("cdOferta")));
			
			ResultSetMap rsm = OfertaHorarioServices.getAllByOferta(cdOferta);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallbyturmadisciplina")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByTurmaDisciplina(RestData restData) {
		try {
			
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdDisciplina = (restData.getArg("cdDisciplina") != null ? Integer.parseInt(String.valueOf(restData.getArg("cdDisciplina"))) : 0);
			int nrDiaSemana = Integer.parseInt(String.valueOf(restData.getArg("nrDiaSemana")));
			
			ResultSetMap rsm = OfertaHorarioServices.getAllByTurmaDisciplina(cdTurma, cdDisciplina, nrDiaSemana);
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
			ResultSetMap rsm = OfertaHorarioServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}