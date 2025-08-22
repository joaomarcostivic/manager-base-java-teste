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
import sol.util.RestData;
import sol.util.Result;

@Path("/seg/agrupamentoacao/")

public class AgrupamentoAcaoRest {
	
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(AgrupamentoAcao agrupamentoAcao){
		try {
			Result result = AgrupamentoAcaoServices.save(agrupamentoAcao);
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
			ResultSetMap rsm = AgrupamentoAcaoServices.getAll();
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
	public String remove(AgrupamentoAcao agrupamentoAcao){
		try {
			Result result = AgrupamentoAcaoServices.remove(agrupamentoAcao.getCdAgrupamento(), agrupamentoAcao.getCdModulo(), agrupamentoAcao.getCdSistema());
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
			ResultSetMap rsm = AgrupamentoAcaoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getAllByModulo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByModulo(RestData restData) {
		try {
			
			int cdModulo = Integer.parseInt(String.valueOf(restData.getArg("cdModulo")));
			int cdSistema = Integer.parseInt(String.valueOf(restData.getArg("cdSistema")));
			int lgAtivo = Integer.parseInt(String.valueOf(restData.getArg("lgAtivo")));
			
			ResultSetMap rsm = AgrupamentoAcaoServices.getAllByModulo(cdModulo, cdSistema, lgAtivo);
			return Util.rsmToJSONSync(rsm);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
