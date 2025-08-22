package com.tivic.manager.mob;

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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.util.Util;

@Path("/mob/boatvitima/")

public class BoatVitimaRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(RestData args){
		try {			
			ObjectMapper objectMapper = new ObjectMapper();
			BoatVitima boatVitima = objectMapper.convertValue(args.getArg("boatVitima"), BoatVitima.class);
			Pessoa pessoa = objectMapper.convertValue(args.getArg("pessoa"), Pessoa.class);
			
			boatVitima.setPessoa(pessoa);
			
			Result result = BoatVitimaServices.save(boatVitima);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@Deprecated
	public String save(BoatVitima boatVitima){
		try {
			Result result = BoatVitimaServices.save(boatVitima);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@DELETE
	@Path("/remove/{cdBoat}/{cdPessoa}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(
			@PathParam("cdBoat") int cdBoat,
			@PathParam("cdPessoa") int cdPessoa){
		try {
			Result result = BoatVitimaServices.remove(cdBoat, cdPessoa);
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
			ResultSetMap rsm = BoatVitimaServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/getbyboat/{idBoat}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getByBoat(@PathParam("idBoat") int cdBoat) {
		try {
			ResultSetMap rsm = BoatVitimaServices.getAllVitimasByBoat(cdBoat);
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
			ResultSetMap rsm = BoatVitimaServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
