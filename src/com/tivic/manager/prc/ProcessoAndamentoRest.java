package com.tivic.manager.prc;

import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.Util;

@Path("/prc/processoandamento/")

public class ProcessoAndamentoRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(ProcessoAndamento processoAndamento){
		try {
			Result result = ProcessoAndamentoServices.save(processoAndamento);
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
	public String remove(ProcessoAndamento processoAndamento){
		try {
			Result result = ProcessoAndamentoServices.remove(processoAndamento.getCdAndamento(), processoAndamento.getCdProcesso(), null);
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
//			ResultSetMap rsm = ProcessoAndamentoServices.getAll();
//			return Util.rsmToJSON(rsm);
//		} catch(Exception e) {
//			e.printStackTrace(System.out);
//			return null;
//		}
//	}
//
	@GET
	@Path("/{cdProcesso}/andamentos/{cdAndamento}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAndamentos(@PathParam("cdProcesso") int cdProcesso, @PathParam("cdAndamento") int cdAndamento) { 
		try {			
			ResultSetMap rsm = ProcessoServices.getAndamentos(cdProcesso, cdAndamento, true, false, null);
			if(!rsm.next())
				return ResponseFactory.noContent("Nenhum Andamento");
			
			return Response.ok(rsm.getRegister()).build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
