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

@Path("/acd/planotopico/")

public class PlanoTopicoRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(PlanoTopico planoTopico){
		try {
			Result result = PlanoTopicoServices.save(planoTopico);
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
	public String remove(PlanoTopico planoTopico){
		try {
			Result result = PlanoTopicoServices.remove(planoTopico.getCdPlano(), planoTopico.getCdSecao(), planoTopico.getCdTopico());
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
			ResultSetMap rsm = PlanoTopicoServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	
	@POST
	@Path("/gettopicosarvore")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static String getTopicosArvore(RestData restData) {
		try {
			
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdCurso = Integer.parseInt(String.valueOf(restData.getArg("cdCurso")));
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdDisciplina = (restData.getArg("cdDisciplina") != null ? Integer.parseInt(String.valueOf(restData.getArg("cdDisciplina"))) : 0);
			
			ResultSetMap rsm = PlanoTopicoServices.getTopicosArvore(cdTurma, cdCurso, cdProfessor, cdDisciplina);
			
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/gettopicosbyplano")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static String getTopicosByPlano(RestData restData) {
		try {
			
			int cdPlano = Integer.parseInt(String.valueOf(restData.getArg("cdPlano")));
			int cdSecao = Integer.parseInt(String.valueOf(restData.getArg("cdSecao")));
			
			
			ResultSetMap rsm = PlanoTopicoServices.getTopicosByPlano(cdPlano, cdSecao);
			
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/alterarposicaotopico")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static String alterarPosicaoTopico(RestData restData) {
		try {
			
			int cdPlano = Integer.parseInt(String.valueOf(restData.getArg("cdPlano")));
			int cdSecao = Integer.parseInt(String.valueOf(restData.getArg("cdSecao")));
			int cdTopico = Integer.parseInt(String.valueOf(restData.getArg("cdTopico")));
			int sentido = Integer.parseInt(String.valueOf(restData.getArg("sentido")));
						
			Result result = PlanoTopicoServices.alterarPosicaoTopico(cdPlano, cdSecao, cdTopico, sentido);
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
			ResultSetMap rsm = PlanoTopicoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getplanotopicoconteudo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static String getPlanoTopicoConteudo(RestData restData) {
		try {
			
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdCurso = Integer.parseInt(String.valueOf(restData.getArg("cdCurso")));
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdDisciplina = Integer.parseInt(String.valueOf(restData.getArg("cdDisciplina")));
			
			ResultSetMap rsm = PlanoTopicoServices.getPlanoTopicoConteudo(cdTurma, cdCurso, cdProfessor, cdDisciplina);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
}
