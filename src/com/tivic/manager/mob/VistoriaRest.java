package com.tivic.manager.mob;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.util.Util;

@Path("/mob/vistoria/")

public class VistoriaRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Vistoria vistoria) {
		try {
			Result result = VistoriaServices.save(vistoria);
			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@PUT
	@Path("/aprovar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String aprovar(RestData restData) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			Vistoria vistoria = objectMapper.convertValue(restData.getArg("vistoria"), Vistoria.class);
			ArrayList vistoriaItens = objectMapper.convertValue(restData.getArg("vistoriaItens"), ArrayList.class);

			Result result = VistoriaServices.save(vistoria, vistoriaItens);
			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@PUT
	@Path("/reprovar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String reprovar(RestData restData){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
						
			Vistoria vistoria = objectMapper.convertValue(restData.getArg("vistoria"), Vistoria.class);
			Date dtRetorno = objectMapper.convertValue(restData.getArg("dtRetorno"), Date.class);
			ArrayList<HashMap<String, Object>> listaVistoria = objectMapper.convertValue(restData.getArg("vistoriaItens"), ArrayList.class);
			
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dtRetorno);
			
			for(HashMap<String, Object> item : listaVistoria ) {
				if(item.get("DEFEITOS") != null) {
					ArrayList<HashMap<String, Object>> register = ((ArrayList<HashMap<String, Object>>)((LinkedHashMap)item.get("DEFEITOS")).get("lines"));
					ResultSetMap rsmDefeitos = new ResultSetMap();
					rsmDefeitos.setLines(register);
					item.put("DEFEITOS", rsmDefeitos);
					System.out.println("");
					System.out.println(rsmDefeitos);
					System.out.println("");
				}
			}
			
			ResultSetMap rsmPlanoVistoriaItens = new ResultSetMap();
			rsmPlanoVistoriaItens.setLines(listaVistoria);
			
			Result result = VistoriaServices.save(vistoria, rsmPlanoVistoriaItens, gc);
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
	public String remove(Vistoria vistoria) {
		try {
			Result result = VistoriaServices.remove(vistoria.getCdVistoria());
			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll() {
		try {
			ResultSetMap rsm = VistoriaServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch (Exception e) {
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
			ResultSetMap rsm = VistoriaServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
