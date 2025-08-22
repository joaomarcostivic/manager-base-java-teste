package com.tivic.manager.fta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

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
import org.json.JSONObject;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.util.Util;

@Path("/fta/{a:veiculo|veiculos}/")

public class VeiculoRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Veiculo veiculo){
		try {
			Result result = VeiculoServices.save(veiculo);
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
	public String remove(Veiculo veiculo){
		try {
			Result result = VeiculoServices.remove(veiculo.getCdVeiculo());
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
			ResultSetMap rsm = VeiculoDAO.getAll();
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
			ResultSetMap rsm = VeiculoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/veiculo")
	@Produces(MediaType.APPLICATION_JSON)
	public static String detran(Veiculo veiculo) {
		try {
			String nrPlaca 	= veiculo.getNrPlaca().replaceAll("-", "");
			String urlCons	= "http://detran.tivic.com.br/etransito/detrancon?p="+nrPlaca;
			URL url = new URL(urlCons);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1) {
	            buffer.append(chars, 0, read); 
	        }
			return new JSONObject(new Result(1, "", "VEICULO", new JSONObject(buffer.toString()))).toString();
		} catch(Exception e) {
			e.printStackTrace();
			return new JSONObject(new Result(-1, "Erro ao realizar consulta ao DETRAN.")).toString();
		}
	}

}
