package com.tivic.manager.grl;

import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.util.Util;

@Path("/grl/grupoequipamento/")

public class GrupoEquipamentoRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(GrupoEquipamento grupoEquipamento){
		try {
			Result result = GrupoEquipamentoServices.save(grupoEquipamento);
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
	public String remove(GrupoEquipamento grupoEquipamento){
		try {
			Result result = GrupoEquipamentoServices.remove(grupoEquipamento);
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
			ResultSetMap rsm = GrupoEquipamentoServices.getAll();
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
			ResultSetMap rsm = GrupoEquipamentoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/getAgrupado")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAgrupado(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = GrupoEquipamentoServices.find(criterios);
			ResultSetMap rsmGrupos = new ResultSetMap();
			
			while(rsm.next()) {
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NM_GRUPO", rsm.getString("NM_GRUPO"));
				register.put("TP_EQUIPAMENTO", rsm.getString("TP_EQUIPAMENTO"));
				register.put("LISTA", GrupoEquipamentoServices.getAllGrupoEquipamentosItem(rsm.getInt("CD_GRUPO")).getLines());
				rsmGrupos.addRegister(register);
			}
			
			return Util.rsmToJSON(rsmGrupos);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
