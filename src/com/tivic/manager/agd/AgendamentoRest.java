package com.tivic.manager.agd;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.grl.EquipamentoPessoa;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.RestData;
import sol.util.Result;

@Path("/agd/agendamento/")

public class AgendamentoRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Agendamento agendamento){
		try {
			Result result = AgendamentoServices.save(agendamento);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@PUT
	@Path("/fullsave")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String fullSave(RestData args){
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();			
			Agendamento agendamento		= objectMapper.convertValue(args.getArg("agendamento"), Agendamento.class);
			ArrayList listParticipantes = objectMapper.convertValue(args.getArg("listParticipantes"), ArrayList.class);
			ArrayList listEquipamentos  = objectMapper.convertValue(args.getArg("listEquipamentos"), ArrayList.class);
			
			Result result = AgendamentoServices.save(agendamento, listParticipantes, listEquipamentos, null, null);
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
	public String remove(Agendamento agendamento){
		try {
			Result result = AgendamentoServices.remove(agendamento);
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
			ResultSetMap rsm = AgendamentoServices.getAll();
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
			ResultSetMap rsm = AgendamentoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/loadpessoaequipe")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String loadPessoaEquipe(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = AgendamentoServices.loadPessoaEquipe(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/ocorrencias/{cdAgendamento}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getOcorrencias(@PathParam("cdAgendamento") int cdAgendamento) {
		try {
			ResultSetMap rsm = AgendamentoServices.getOcorrencias(cdAgendamento);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/finalizar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String finalizar(RestData args) {
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();			
			Integer cdAgendamento = objectMapper.convertValue(args.getArg("cdAgendamento"), Integer.class);
			AuthData auth = objectMapper.convertValue(args.getArg("auth"), AuthData.class);
			
			Result result = AgendamentoServices.finalizar(cdAgendamento, auth);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
