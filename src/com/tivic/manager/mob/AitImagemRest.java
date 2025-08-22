package com.tivic.manager.mob;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;

import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

@Path("/mob/aitimagem/")

public class AitImagemRest {

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(AitImagem aitImagem){
		try {
			Result result = AitImagemServices.save(aitImagem);
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
	public String remove(AitImagem aitImagem){
		try {
			Result result = AitImagemServices.remove(aitImagem);
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
			ResultSetMap rsm = AitImagemServices.getAll();
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
			ResultSetMap rsm = AitImagemServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getFrom")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getFromAit(int cdAit) {
		try {
			
			ResultSetMap rsm = AitImagemServices.getFromAit(cdAit);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/imagem/{cdImagem}/{cdAit}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({"image/jpg"})
	@JWTIgnore
	public static Response file(@PathParam("cdImagem") int cdImagem, @PathParam("cdAit") int cdAit) {
		try {			
			AitImagem arquivo = AitImagemDAO.get(cdImagem, cdAit);		
						
			return Response.ok(new ByteArrayInputStream(arquivo.getBlbImagem())).build();
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return Response.status(Response.Status.CONFLICT).build();
	    }
	}
	
	@POST
	@Path("/saveimages")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public static String saveImages(@FormDataParam("cdait") FormDataBodyPart cdAit, @FormDataParam("files[]") FormDataBodyPart files) {
		Connection conexao = null;
		try {	
			Result result = AitImagemServices.save(cdAit, files);			
			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (conexao != null)
				Conexao.desconectar(conexao);
		}
	}

}
