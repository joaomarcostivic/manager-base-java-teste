package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.util.Util;

@Path("/mob/infracao")

public class InfracaoRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Infracao infracao){
		try {
			Result result = InfracaoServices.save(infracao);
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
	public String remove(Infracao infracao){
		try {
			Result result = InfracaoServices.remove(infracao.getCdInfracao());
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
			ResultSetMap rsm = InfracaoServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/getBy/coddetran")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getInfracaoByCodDetran(Infracao infracao) {
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("NR_COD_DETRAN", String.valueOf(infracao.getNrCodDetran()), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = InfracaoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/getBy/tipoEvento/{cdTipoEvento}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getInfracaoByTipoEvento(@PathParam("cdTipoEvento") Integer cdTipoEvento) {
		try {
			TipoEvento tipoEvento = null;
			Infracao infracao = null;
			ObjectMapper objectMapper = new ObjectMapper();
			
			if(cdTipoEvento != 0)
				tipoEvento = TipoEventoDAO.get(cdTipoEvento);
			
			if(tipoEvento != null && tipoEvento.getCdInfracao() > 0) 
				infracao = InfracaoServices.getVigente(tipoEvento.getCdInfracao(), null);
			

			return objectMapper.writeValueAsString(infracao);
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
			ResultSetMap rsm = InfracaoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
