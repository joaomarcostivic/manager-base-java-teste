package com.tivic.manager.seg;

import sol.dao.ResultSetMap;
import sol.util.RestData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.tivic.manager.util.Util;

@Path("/seg/release/")

public class ReleaseRest {
	
	@POST
	@Path("/getreleases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getReleases(RestData restData) {
		try {
			int nrReleases = Integer.parseInt(String.valueOf(restData.getArg("nrReleases")));
			ResultSetMap rsm = ReleaseServices.getReleases(nrReleases);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	

}

