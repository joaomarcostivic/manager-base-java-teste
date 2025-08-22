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
import org.json.JSONObject;

import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Path("/agd/agendaitem/")

public class AgendaItemRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(AgendaItem agendaItem){
		try {
			Result result = AgendaItemServices.save(agendaItem);
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
	public String remove(AgendaItem agendaItem){
		try {
			Result result = AgendaItemServices.remove(agendaItem.getCdAgendaItem());
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

//	@POST
//	@Path("/getAll")
//	@Produces(MediaType.APPLICATION_JSON)
//	public static String getAll() {
//		try {
//			ResultSetMap rsm = AgendaItemServices.getAll();
//			return Util.rsmToJSON(rsm);
//		} catch(Exception e) {
//			e.printStackTrace(System.out);
//			return null;
//		}
//	}

	@POST
	@Path("/find")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String find(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = AgendaItemServices.getList(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getlist/mobilidade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getListMobilidade(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = AgendaItemServices.getListMobilidade(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/get/responsavel")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getResponsaveis() {
		try {
			ResultSetMap rsm = AgendaItemServices.getResponsaveis();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/find/responsavel")
	@Produces(MediaType.APPLICATION_JSON)
	public static String findResponsaveis(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = AgendaItemServices.findResponsavel(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/chart/agrupado/grupotrabalho/{tpAgendaItem}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getGraficoAudienciaAgrupado(@PathParam("tpAgendaItem") int tpAgendaItem) {
		try {			
			return Util.rsmToJSON(AgendaItemServices.getAgendasAgrupado(tpAgendaItem));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
