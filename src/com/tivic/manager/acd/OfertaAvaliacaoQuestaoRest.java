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

@Path("/acd/ofertaavaliacaoquestao/")

public class OfertaAvaliacaoQuestaoRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(OfertaAvaliacaoQuestao ofertaAvaliacaoQuestao){
		try {
			Result result = OfertaAvaliacaoQuestaoServices.save(ofertaAvaliacaoQuestao);
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
	public String remove(OfertaAvaliacaoQuestao ofertaAvaliacaoQuestao){
		try {
			Result result = OfertaAvaliacaoQuestaoServices.remove(ofertaAvaliacaoQuestao);
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
			ResultSetMap rsm = OfertaAvaliacaoQuestaoServices.getAll();
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
			ResultSetMap rsm = OfertaAvaliacaoQuestaoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallbyofertaavaliacao")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByOfertaAvaliacao(RestData restData) {
		try {
			
			int cdOferta = Integer.parseInt(String.valueOf(restData.getArg("cdOferta")));
			int cdOfertaAvaliacao = Integer.parseInt(String.valueOf(restData.getArg("cdOfertaAvaliacao")));
			int cdOfertaAvaliacaoQuestaoSuperior = 0;
			if(restData.getArg("cdOfertaAvaliacaoQuestaoSuperior") != null)
				cdOfertaAvaliacaoQuestaoSuperior = Integer.parseInt(String.valueOf(restData.getArg("cdOfertaAvaliacaoQuestaoSuperior")));
			
			ResultSetMap rsm = OfertaAvaliacaoQuestaoServices.getAllByOfertaAvaliacao(cdOferta, cdOfertaAvaliacao, cdOfertaAvaliacaoQuestaoSuperior);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}