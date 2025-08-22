package com.tivic.manager.str;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.Result;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.mob.EventoArquivoServices;
import com.tivic.manager.util.Util;

@Path("/str/aitimagem/")

public class AitImagemRest {

//	@PUT
//	@Path("/save")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public String save(ArrayList<AitImagem> aitImagem){
//		try {
//			Result result = AitImagemServices.save(aitImagem);
//			return new JSONObject(result).toString();
//		} catch(Exception e) {
//			e.printStackTrace(System.out);
//			return null;
//		}
//	}
//
//	@DELETE
//	@Path("/remove")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public String remove(AitImagem aitImagem){
//		try {
//			Result result = AitImagemServices.remove(aitImagem);
//			return new JSONObject(result).toString();
//		} catch(Exception e) {
//			e.printStackTrace(System.out);
//			return null;
//		}
//	}
//
//	@POST
//	@Path("/getAll")
//	@Produces(MediaType.APPLICATION_JSON)
//	public static String getAll() {
//		try {
//			ResultSetMap rsm = AitImagemServices.getAll();
//			return Util.rsmToJSON(rsm);
//		} catch(Exception e) {
//			e.printStackTrace(System.out);
//			return null;
//		}
//	}
	
	@GET
	@Path("/file/{cdArquivo}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({"image/jpg"})
	public static Response file(@PathParam("cdArquivo") int cdAItImagem) {
		try {			

			AitImagem arquivo = AitImagemDAO.get(cdAItImagem);			
			
			return Response.ok(arquivo.getBlbImagem()).build();
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return Response.status(Response.Status.CONFLICT).build();
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

}
