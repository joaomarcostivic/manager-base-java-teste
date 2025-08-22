package com.tivic.manager.str;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.util.Result;

@Path("/str/agente/")

public class AgenteRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Agente agente){
		try {
			Result result = AgenteServices.save(agente);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/orgao/{idOrgao}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAgenteOrgao(@PathParam("idOrgao") String idOrgao) {
		try {
			
//			boolean isRemoteTracker = ParametroServices.getValorOfParametroAsInteger("MOB_LG_TRACKER_REMOTO", 0)==1;
//			String urlRemoteTracker = ParametroServices.getValorOfParametroAsString("MOB_URL_TRACKER_REMOTO", null);
			
			boolean isRemoteTracker = Util.getConfManager().getProps().getProperty("MOB_LG_TRACKER_REMOTO").equals("1");
			String urlRemoteTracker = Util.getConfManager().getProps().getProperty("MOB_URL_TRACKER_REMOTO");
			
			//XXX
//			isRemoteTracker  = true;
//			urlRemoteTracker = "http://localhost:8080/tracker/rws";
			if(isRemoteTracker) {
				Client client = ClientBuilder.newClient();
				WebTarget target = client.target(urlRemoteTracker+"/str/agente/get/orgao/"+idOrgao);

				Response response = target.request().accept(MediaType.APPLICATION_JSON).get();
				response.bufferEntity();
				
				if(response.getStatus() != 200) {
					return response.toString();
				}

				if(response.getStatus() != 200) {
					return response.toString();
				}
				
				return response.readEntity(String.class);
			}
			else {
				ResultSetMap rsm = AgenteServices.getAllByIdOrgao(idOrgao);
				return Util.rsmToJSON(rsm);
			}
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

//	@DELETE
//	@Path("/remove")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public String remove(Agente agente){
//		try {
//			Result result = AgenteServices.remove(agente.getCdAgente());
//			return new JSONObject(result).toString();
//		} catch(Exception e) {
//			e.printStackTrace(System.out);
//			return null;
//		}
//	}
//
	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll() {
		try {
			ResultSetMap rsm = AgenteServices.getAllAgentes();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
//
//	@POST
//	@Path("/find")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public static String find(ArrayList<ItemComparator> criterios) {
//		try {
//			ResultSetMap rsm = AgenteServices.find(criterios);
//			return Util.rsmToJSON(rsm);
//		} catch(Exception e) {
//			e.printStackTrace(System.out);
//			return null;
//		}
//	}

}
