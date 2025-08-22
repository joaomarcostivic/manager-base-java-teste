package com.tivic.manager.ptc;

import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.util.Util;

@Path("/ptc/documentacaoobrigatoria/")

public class DocumentacaoObrigatoriaRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(DocumentacaoObrigatoria documentacaoObrigatoria){
		try {
			Result result = DocumentacaoObrigatoriaServices.save(documentacaoObrigatoria);
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
	public String remove(DocumentacaoObrigatoria documentacaoObrigatoria){
		try {
			Result result = DocumentacaoObrigatoriaServices.remove(documentacaoObrigatoria.getCdTipoDocumentacao(), documentacaoObrigatoria.getCdTipoDocumento());
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
			ResultSetMap rsm = DocumentacaoObrigatoriaServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/getByTipoDocumento/{cdTipoDocumento}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getByTipoDocumento(@PathParam("cdTipoDocumento") int cdTipoDocumento) {
		try {
			ResultSetMap rsm = DocumentacaoObrigatoriaServices.getAllByTipoDocumento(cdTipoDocumento);
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
			ResultSetMap rsm = DocumentacaoObrigatoriaServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
