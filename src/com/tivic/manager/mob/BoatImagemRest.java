package com.tivic.manager.mob;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.RestData;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.util.Util;
import java.util.Base64;

@Path("/mob/boatimagem/")

public class BoatImagemRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(BoatImagem boatImagem){
		try {
			Result result = BoatImagemServices.save(boatImagem);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/saveimages")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public static String saveImages(@FormDataParam("cdboat") FormDataBodyPart cdboat, @FormDataParam("files[]") FormDataBodyPart files) {
		Connection conexao = null;
		try {	
			Result result = BoatImagemServices.save(cdboat, files);			
			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (conexao != null)
				Conexao.desconectar(conexao);
		}
	}

	@DELETE
	@Path("/remove/{cdImagem}/{cdBoat}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(
			@PathParam("cdImagem") int cdImagem, 
			@PathParam("cdBoat") int cdBoat){
		try {
			Result result = BoatImagemServices.remove(cdImagem, cdBoat);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/file/{cdArquivo}/{cdBoat}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({"image/jpg"})
	@JWTIgnore
	public static Response file(@PathParam("cdArquivo") int cdImagem, @PathParam("cdBoat") int cdBoat) {
		try {			
			BoatImagem arquivo = BoatImagemDAO.get(cdImagem, cdBoat);		
						
			return Response.ok(new ByteArrayInputStream(arquivo.getBlbImagem())).build();
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return Response.status(Response.Status.CONFLICT).build();
	    }
	}

	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll() {
		try {
			ResultSetMap rsm = BoatImagemServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	} 
	
	@POST
	@Path("/getbyboat/{idBoat}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getByBoat(@PathParam("idBoat") int cdBoat) {
		try {
			ResultSetMap rsm = BoatImagemServices.getAllByBoat(cdBoat);
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
			ResultSetMap rsm = BoatImagemServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/upload")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String upload(RestData boatImagens){
		try {			
			Result result = BoatImagemServices.upload(boatImagens);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
}
