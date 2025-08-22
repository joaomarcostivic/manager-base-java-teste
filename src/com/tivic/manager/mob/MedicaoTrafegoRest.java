package com.tivic.manager.mob;

import java.util.ArrayList;
import java.util.GregorianCalendar;

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

@Path("/mob/medicaotrafego/")

public class MedicaoTrafegoRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(MedicaoTrafego medicaoTrafego){
		try {
			Result result = MedicaoTrafegoServices.save(medicaoTrafego);
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
	public String remove(MedicaoTrafego medicaoTrafego){
		try {
			Result result = MedicaoTrafegoServices.remove(medicaoTrafego);
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
			ResultSetMap rsm = MedicaoTrafegoServices.getAll();
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
			ResultSetMap rsm = MedicaoTrafegoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	// ========================================================================
	// ========================================================================
	// ==                                                                    ==
	// ==                             STATS                                  ==
	// ==                                                                    ==
	// ========================================================================
	// ========================================================================
	
	@GET
	@Path("/stats/trafego/horario/{cdOrgao}/{inicio}/{fim}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getTrafegoHorario(@PathParam("cdOrgao") int cdOrgao, @PathParam("inicio") String inicio, @PathParam("fim") String fim) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			return Util.rsmToJSON(MedicaoTrafegoServices.getTrafegoHorario(cdOrgao, dtInicial, dtFinal));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/trafego/horario/tpVeiculo/{cdOrgao}/{inicio}/{fim}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getTrafegoHorarioTipoVeiculo(@PathParam("cdOrgao") int cdOrgao, @PathParam("inicio") String inicio, @PathParam("fim") String fim) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			return Util.rsmToJSON(MedicaoTrafegoServices.getTrafegoHorarioTipoVeiculo(cdOrgao, dtInicial, dtFinal));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/trafego/tpVeiculo/{cdOrgao}/{inicio}/{fim}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getTrafegoTipoVeiculo(@PathParam("cdOrgao") int cdOrgao, @PathParam("inicio") String inicio, @PathParam("fim") String fim) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			return Util.rsmToJSON(MedicaoTrafegoServices.getTrafegoVeiculo(cdOrgao, dtInicial, dtFinal));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
