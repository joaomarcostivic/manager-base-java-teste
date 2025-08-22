package com.tivic.manager.adm;

import java.io.InputStream;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.RestData;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;
import org.json.JSONArray;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.mob.BoatImagem;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.Util;

@Path("/adm/contareceber/")

public class ContaReceberRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(ContaReceber contaReceber){
		try {
			Result result = ContaReceberServices.save(contaReceber);
			return new JSONObject(result).toString();
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
			ResultSetMap rsm = ContaReceberServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findContas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findContas(ArrayList<ItemComparator> criterios) {
		try {
		
			ResultSetMap rsm = ContaReceberServices.findContas(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findParcelas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findParcelas(ArrayList<ItemComparator> criterios) {
		try {
		
			ResultSetMap rsm = ContaReceberServices.getCodigo(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/**
	 * @param data
	 * @param body
	 * @return
	 */
	@POST
	@Path("/salvarArquivo")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public static String salvarArquivo(@FormDataParam("restData") FormDataBodyPart data,
			@FormDataParam("files[]") FormDataBodyPart body) {
		try {
			data.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			String restString = data.getValueAs(String.class);
			
			JSONObject jsonContas = objectMapper.convertValue(restString, JSONObject.class);
			JSONObject args = objectMapper.convertValue(jsonContas.get("args"), JSONObject.class);
			
			JSONArray contas = objectMapper.convertValue(args.get("contas"), JSONArray.class);
			
			int index = 0;
			
			for (org.glassfish.jersey.media.multipart.BodyPart part : body.getParent().getBodyParts()) {
				
				org.glassfish.jersey.media.multipart.ContentDisposition meta = part.getContentDisposition();
				InputStream is = part.getEntityAs(InputStream.class);
				
				if (meta.getFileName() != null) {
					
					JSONObject conta = (JSONObject) contas.get(index);
					JSONObject arquivoJSON = (JSONObject) conta.get("arquivo");

					Arquivo arquivo = new Arquivo();
					arquivo.setNmArquivo(arquivoJSON.getString("nmArquivo"));
					arquivo.setNmDocumento(arquivoJSON.getString("nmDocumento"));

					byte[] file = ImagemServices.writeToByteArray(is).toByteArray();
					arquivo.setBlbArquivo(file);
					
					JSONArray cd_contas = objectMapper.convertValue(conta.get("CD_CONTA_RECEBER"), JSONArray.class);
					Result result = ContaReceberServices.saveArquivo(arquivo);
					if (cd_contas != null) { 
					   int len = cd_contas.length();
					   for (int i=0;i<len;i++){ 
						   int cd_conta = (int) cd_contas.get(i);
						   ContaReceberServices.saveRegistro(result.getCode(), cd_conta);
					   }
					}
				}
				index++;
			}
			
//			
			return "{\"codigo\": 1}";
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
