package com.tivic.manager.mob;

import java.util.ArrayList;

import javax.swing.Timer;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.grl.equipamento.repository.EquipamentoDAO;
import com.tivic.manager.util.Util;

@Path("/grl/equipamento/")

public class EquipamentoRest {
	

    static Timer tmrVideoProcess;

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Equipamento equipamento){
		try {
			Result result = EquipamentoServices.save(equipamento);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@DELETE
	@Path("/remove/{cdEquipamento}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(@PathParam("cdEquipamento") int cdEquipamento){
		try {
			Equipamento equipamento = EquipamentoDAO.get(cdEquipamento);
			Result result = EquipamentoServices.remove(equipamento);
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
			ResultSetMap rsm = EquipamentoServices.getAll();
			return Util.rsmToJSONSync(rsm);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@POST
	@Path("/find")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String find(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = EquipamentoServices.find(criterios);
			return Util.rsmToJSONSync(rsm);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	@POST
	@Path("/borrowlist")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String loadToBorrow(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = EquipamentoServices.loadToBorrow(criterios);
			return Util.rsmToJSONSync(rsm);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@GET
	@Path("/stream")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({"image/jpg"})
	public static Response stream() {
		try {						
	        
			return Response.ok().build();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	@GET
	@Path("/info")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getInfo(@QueryParam("tpEquipamento") int tpEquipamento) {
		try {
			if(tpEquipamento == EquipamentoServices.RADAR_FIXO) {
				return Util.rsmToJSON(EquipamentoServices.getInfoRadar());
			}
	        
			return Util.rsmToJSON(new ResultSetMap());
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	


}