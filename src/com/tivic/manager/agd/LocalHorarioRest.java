package com.tivic.manager.agd;

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

@Path("/agd/localhorario/")

public class LocalHorarioRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(LocalHorario localHorario){
		try {
			Result result = LocalHorarioServices.save(localHorario);
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
	public String remove(LocalHorario localHorario){
		try {
			Result result = LocalHorarioServices.remove(localHorario.getCdHorario());
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
			ResultSetMap rsm = LocalHorarioServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getHorariosDisponiveis")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getHorariosDisponiveis(RestData restData) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			int dia = mapper.convertValue(restData.getArg("nrDia"), Integer.class);
			int mes = mapper.convertValue(restData.getArg("nrMes"), Integer.class);
			int ano = mapper.convertValue(restData.getArg("nrAno"), Integer.class);
			int nrDiaSemana = mapper.convertValue(restData.getArg("nrDiaSemana"), Integer.class);
			
			ResultSetMap rsm = LocalHorarioServices.getDisponiveis(dia, mes, ano, nrDiaSemana, 1, LocalHorarioServices.ST_PUBLICO);
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
			ResultSetMap rsm = LocalHorarioServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
