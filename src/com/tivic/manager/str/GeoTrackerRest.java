package com.tivic.manager.str;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Path("/str/geotracker/")

public class GeoTrackerRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(GeoTracker geoTracker){
		try {
			Result result = GeoTrackerServices.save(geoTracker);
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
	public String remove(GeoTracker geoTracker){
		try {
			Result result = GeoTrackerServices.remove(geoTracker);
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
			ResultSetMap rsm = GeoTrackerServices.getAll();
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
			ResultSetMap rsm = GeoTrackerServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/trackagente")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String trackAgente(String args) {
		try {			
//			boolean isRemoteTracker = ParametroServices.getValorOfParametroAsInteger("MOB_LG_TRACKER_REMOTO", 0)==1;
//			String urlRemoteTracker = ParametroServices.getValorOfParametroAsString("MOB_URL_TRACKER_REMOTO", null);
			
			boolean isRemoteTracker = Util.getConfManager().getProps().getProperty("MOB_LG_TRACKER_REMOTO").equals("1");
			String urlRemoteTracker = Util.getConfManager().getProps().getProperty("MOB_URL_TRACKER_REMOTO");
			
			JSONObject jsonArgs = new JSONObject(args);
			
			//XXX
//			isRemoteTracker  = true;
//			urlRemoteTracker = "http://localhost:8080/tracker/rws";
									
			if(isRemoteTracker) {
				Client client = ClientBuilder.newClient();
				WebTarget target = client.target(urlRemoteTracker+"/str/geotracker/trackagente");

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
				
				String idOrgao	   			= jsonArgs.getString("idOrgao")!=null ? jsonArgs.getString("idOrgao") : null;
				String nrMatricula 			= jsonArgs.getString("nrMatricula")!=null ? jsonArgs.getString("nrMatricula") : null;
				GregorianCalendar dtInicial = jsonArgs.getString("dtInicial")!=null ? Util.convStringToCalendar(jsonArgs.getString("dtInicial")) : null;
				GregorianCalendar dtFinal   = jsonArgs.getString("dtFinal")!=null ? Util.convStringToCalendar(jsonArgs.getString("dtFinal")) : null;
				
				ResultSetMap rsm = GeoTrackerServices
						.track(idOrgao, nrMatricula, dtInicial, dtFinal, jsonArgs.getBoolean("lastPosition"));
								
				return Util.rsmToJSON(rsm);
			}
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
