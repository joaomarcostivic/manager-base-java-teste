package com.tivic.manager.grl;

import java.sql.Types;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.Util;

@Path("/grl/cidade/")

public class CidadeRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Cidade cidade){
		try {
			Result result = CidadeServices.save(cidade);
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
	public String remove(Cidade cidade){
		try {
			Result result = CidadeServices.remove(cidade.getCdCidade());
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/getAll{qtd: (/\\\\d+)?}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll(@PathParam("qtd") int qtd) {
		try {
			ResultSetMap rsm = CidadeServices.getAll(qtd);
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
			ResultSetMap rsm = CidadeServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/getall/{sgEstado}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAllSgEstado(@PathParam("sgEstado") String sg) {
		try {
			return ResponseFactory.ok(CidadeServices.find(new Criterios().add("B.SG_ESTADO", sg, ItemComparator.EQUAL, Types.VARCHAR)));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/get/uf/municipio/{codMunicipio}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getUfMunicipio(@PathParam("codMunicipio") int codMunicipio) {
		try {
			Cidade cidades = CidadeServices.getByCodMunicipio(codMunicipio);
			return new JSONObject(cidades.toString()).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	// TRACKER ======================================================================================================
	
	@GET
	@Path("/get/{codMunicipio}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getCidade(@PathParam("codMunicipio") int codMunicipio) { 
		try {
//			boolean isRemoteTracker = ParametroServices.getValorOfParametroAsInteger("MOB_LG_TRACKER_REMOTO", 0)==1;
//			String urlRemoteTracker = ParametroServices.getValorOfParametroAsString("MOB_URL_TRACKER_REMOTO", null);
			
			boolean isRemoteTracker = Util.getConfManager().getProps().getProperty("MOB_LG_TRACKER_REMOTO").equals("1");
			String urlRemoteTracker = Util.getConfManager().getProps().getProperty("MOB_URL_TRACKER_REMOTO");

			//XXX
//			isRemoteTracker  = true;
//			urlRemoteTracker = "http://localhost:8080/tracker/rws";
			
			if(isRemoteTracker) {
				//XXX
				System.out.println("GETTING FROM REMOTE TRACKER");
				
				Client client = ClientBuilder.newClient();
				WebTarget target = client.target(urlRemoteTracker+"/grl/cidade/get/"+codMunicipio);

				Response response = target.request().accept(MediaType.APPLICATION_JSON).get();
				response.bufferEntity();
				
				if(response.getStatus() != 200) {
					return response.toString();
				}
				
				return response.readEntity(String.class);
			} else {				
				Cidade cidades = CidadeServices.getCidade(codMunicipio);
				return new JSONObject(cidades.toString()).toString();
			}
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getcidades")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllCidades() {
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
				WebTarget target = client.target(urlRemoteTracker+"/grl/cidade/getcidades");

				Response response = target.request().accept(MediaType.APPLICATION_JSON).get();
				response.bufferEntity();
				
				if(response.getStatus() != 200) {
					return response.toString();
				}
				
				return response.readEntity(String.class);
			}
			else {				
				ResultSetMap rsm = CidadeServices.getAllCidadeOrgao();
				return Util.rsmToJSON(rsm);
			}
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
