package com.tivic.manager.mob;

import java.sql.Connection;
import java.util.ArrayList;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.sol.connection.Conexao;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.util.FixServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Path("/mob/eventoarquivo/")

public class EventoArquivoRest {
	
	@GET
	@Path("/check")
	public void check() {
		try {
			FixServices.fixImagemRadar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(EventoArquivo eventoArquivo){
		try {
			Result result = EventoArquivoServices.save(eventoArquivo);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@POST
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(EventoArquivo eventoArquivo){
		try {
			Result result = EventoArquivoServices.remove(eventoArquivo);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@GET
	@Path("/remove/{cdEvento}/{cdArquivo}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(@PathParam("cdEvento") int cdEvento, @PathParam("cdArquivo") int cdArquivo){
		
		try {
			Connection connect = Conexao.conectar();
			connect.setAutoCommit(false);
			
			EventoArquivo eventoArquivo = EventoArquivoDAO.get(cdEvento, cdArquivo, connect);
			
			Result result = EventoArquivoServices.remove(eventoArquivo.getCdEvento(), eventoArquivo.getCdArquivo(), false, null, connect);
			
			if(result.getCode() > 0) {
				result = ArquivoServices.remove(cdArquivo, false, connect);
				
				if(result.getCode() > 0) {
					connect.commit();
				}
			}
			return new JSONObject(result).toString();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll() {
		try {
			ResultSetMap rsm = EventoArquivoServices.getAll();
			return new JSONObject(rsm.getLines()).toString();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@PUT
	@Path("/principal")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response definirImagemPrincipal(@QueryParam("cdEvento") int cdEvento, @QueryParam("cdArquivo") int cdArquivo) {
		try {
			if(cdEvento == 0 || cdArquivo == 0) {
				return ResponseFactory.badRequest("É necessário informar o evento e a imagem a ser definida como principal.");
			}
			
			Result result = EventoArquivoServices.definirImagemImpressao(cdEvento, cdArquivo);
			return ResponseFactory.ok(result);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@POST
	@Path("/find")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String find(ArrayList<ItemComparator> criterios) {
		try {					
			ResultSetMap rsm = EventoArquivoServices.find(criterios);
			
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(rsm.getLines());
			return json;
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

}