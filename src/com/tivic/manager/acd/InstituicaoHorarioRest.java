package com.tivic.manager.acd;

import java.util.ArrayList;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.util.Util;

@Path("/acd/instituicaohorario/")

public class InstituicaoHorarioRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(RestData restData){
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			InstituicaoHorario instituicaoHorario = objectMapper.convertValue(restData.getArg("instituicaoHorario"), InstituicaoHorario.class);
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			
			Result result = InstituicaoHorarioServices.save(instituicaoHorario, cdUsuario);
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
	public String remove(InstituicaoHorario instituicaoHorario){
		try {
			Result result = InstituicaoHorarioServices.remove(instituicaoHorario.getCdInstituicao(), instituicaoHorario.getCdHorario());
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
			ResultSetMap rsm = InstituicaoHorarioServices.getAll();
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
			ResultSetMap rsm = InstituicaoHorarioServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@POST
	@Path("/getallbyinstituicao")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByInstituicao(RestData restData){
		try {
			
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			int nrDiaSemana = Integer.parseInt(String.valueOf(restData.getArg("nrDiaSemana")));
			
			ResultSetMap rsm = InstituicaoHorarioServices.getAllByInstituicao(cdInstituicao, cdProfessor, -1, cdPeriodoLetivo, nrDiaSemana, null);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}


}
