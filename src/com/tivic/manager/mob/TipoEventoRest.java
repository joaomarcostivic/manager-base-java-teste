package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.sol.response.ResponseFactory;

@Path("/mob/tipoevento/")

public class TipoEventoRest {
	
	@GET
	@Path("/{cdTipoEvento}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response save(@PathParam("cdTipoEvento") int cdTipoEvento){
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_TIPO_EVENTO", String.valueOf(cdTipoEvento), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = TipoEventoServices.find(criterios);
			
			if(rsm.size() == 0) {
				return ResponseFactory.noContent();
			}
			
			return ResponseFactory.ok(rsm.getLines().get(0));
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(TipoEvento tipoEvento){
		try {
			Result result = TipoEventoServices.save(tipoEvento);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(TipoEvento tipoEvento){
		try {
			Result result = TipoEventoServices.remove(tipoEvento);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll() {
		try {
			ResultSetMap rsm = TipoEventoServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Path("/find")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String find(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = TipoEventoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

}