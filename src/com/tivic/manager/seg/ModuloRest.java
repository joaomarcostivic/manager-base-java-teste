package com.tivic.manager.seg;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Path("/seg/modulo/")

public class ModuloRest {
	
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Modulo modulo){
		try {
			Result result = ModuloServices.save(modulo);
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
			ResultSetMap rsm = ModuloServices.getAll();
			return Util.rsmToJSONSync(rsm);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(Modulo modulo){
		try {
			Result result = ModuloServices.remove(modulo.getCdModulo(), modulo.getCdSistema());
			return new JSONObject(result).toString();
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
			ResultSetMap rsm = ModuloServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getAllAtivos")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllAtivos() {
		try {
			ResultSetMap rsm = ModuloServices.getAllAtivos();
			return Util.rsmToJSONSync(rsm);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	@POST
	@Path("/getAllBySistema")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllBySistema(Sistema sistema) {
		try {
			ResultSetMap rsm = ModuloServices.getAllBySistema(sistema.getCdSistema());
			return Util.rsmToJSONSync(rsm);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
}
