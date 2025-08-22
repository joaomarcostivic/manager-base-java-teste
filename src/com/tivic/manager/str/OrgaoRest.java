package com.tivic.manager.str;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

@Path("/str/orgao/")

public class OrgaoRest {

	@GET
	@Path("/get/municipio/{codMunicipio}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getByCidade(@PathParam("codMunicipio") int codMunicipio) {
		try {
			
//			boolean isRemoteTracker = ParametroServices.getValorOfParametroAsInteger("MOB_LG_TRACKER_REMOTO", 0)==1;
//			String urlRemoteTracker = ParametroServices.getValorOfParametroAsString("MOB_URL_TRACKER_REMOTO", null);
			
			boolean isRemoteTracker = Util.getConfManager().getProps().getProperty("MOB_LG_TRACKER_REMOTO").equals("1");
			String urlRemoteTracker = Util.getConfManager().getProps().getProperty("MOB_URL_TRACKER_REMOTO");
			
			//XXX:
//			isRemoteTracker  = true;
//			urlRemoteTracker = "http://localhost:8080/tracker/rws";
			if(isRemoteTracker) {
				Client client = ClientBuilder.newClient();
				WebTarget target = client.target(urlRemoteTracker+"/str/orgao/get/municipio/"+codMunicipio);

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
				return Util.rsmToJSON(OrgaoServices.getAllByCidade(codMunicipio));
			}
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
