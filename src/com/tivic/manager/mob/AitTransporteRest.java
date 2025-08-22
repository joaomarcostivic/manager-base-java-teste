package com.tivic.manager.mob;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.RestData;
import sol.util.Result;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.Util;

@Path("/mob/aittransporte/")

public class AitTransporteRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(AitTransporte aitTransporte) {
		try {
			Result result = AitTransporteServices.save(aitTransporte, -1);
			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(AitTransporte aitTransporte) {
		try {
			Result result = AitTransporteServices.remove(aitTransporte.getCdAit());
			return new JSONObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll() {
		try {
			ResultSetMap rsm = AitTransporteServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch (Exception e) {
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
			ResultSetMap rsm = AitTransporteServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	

	@POST
	@Path("/sync")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response sync(
			@FormDataParam("aitTransporte") FormDataBodyPart body,
			@FormDataParam("files[]") FormDataBodyPart files) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			body.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			
			JSONObject resError = new JSONObject();
			ArrayList<AitTransporte> aitTransporte = new ArrayList<AitTransporte>();
			ArrayList<AitTransporteImagem> imagens = new ArrayList<AitTransporteImagem>();
			String json = body.getValueAs(String.class);

			if (!json.equals("")) {
				aitTransporte = mapper.convertValue(mapper.readTree(json), new TypeReference<ArrayList<AitTransporte>>() {});
			}

			if(files != null && files.getParent().getBodyParts().size() > 0) {
				for (BodyPart part : files.getParent().getBodyParts()) {
					InputStream is = part.getEntityAs(InputStream.class);
					ContentDisposition meta = part.getContentDisposition();
					AitTransporteImagem imagem = new AitTransporteImagem();

					if (meta.getFileName() != null) {
						String[] parts = meta.getFileName().replaceAll("(.[a-zA-Z])+", "").split("_");
						
						imagem.setCdAit(Integer.valueOf(parts[0]));
						imagem.setBlbImagem(ImagemServices.writeToByteArray(is).toByteArray());
						imagens.add(imagem);
					}

				}
			}

			Result response = AitTransporteServices.getSyncData(aitTransporte, imagens);			

			if(response.getCode() < 0) {
				throw new Exception(response.getMessage());
			}

			return Response.status(200).entity(
				mapper.writeValueAsString(response.getObjects().get("RESULT")
			)).build();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("[AIT_SYNC.ERROR] Nï¿½o foi possï¿½vel completar a sincronizaï¿½ï¿½o", e.getMessage());
		}
	}

}
