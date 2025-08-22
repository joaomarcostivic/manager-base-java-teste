package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.report.ReportServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.RestData;
import sol.util.Result;

@Path("/mob/boat/")

public class BoatRest {

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Boat boat){
		try {
			Result result = BoatServices.save(boat);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@PUT
	@Path("/saveDat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(RestData args){
		try {			
			ObjectMapper objectMapper   = new ObjectMapper();
			
			Boat boat = objectMapper.convertValue(args.getArg("boat"), Boat.class);
			
			ArrayList<BoatVeiculo> arrayVeiculos = objectMapper.convertValue(args.getArg("arrayVeiculos"), new TypeReference<ArrayList<BoatVeiculo>>() { });	
			
			for(BoatVeiculo boatVeiculo : arrayVeiculos) {
				boatVeiculo.setNrPlaca(boatVeiculo.getNrPlaca().replaceAll("[^a-zA-Z0-9]", ""));
			}
			
			boat.setVeiculos(arrayVeiculos);

			Declarante 	   	   declarante		  = objectMapper.convertValue(args.getArg("declarante"), Declarante.class);
			BoatDeclarante 	   boatDeclarante	  = objectMapper.convertValue(args.getArg("boatDeclarante"), BoatDeclarante.class);
			DeclaranteEndereco declaranteEndereco = objectMapper.convertValue(args.getArg("declaranteEndereco"), DeclaranteEndereco.class);
			
			Result result = BoatServices.saveDAT(boat, declarante, boatDeclarante, declaranteEndereco);

			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@DELETE
	@Path("/remove/{cdBoat}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(@PathParam("cdBoat") int cdBoat){
		try {
			Result result = BoatServices.deleteCascade(cdBoat);
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
			ResultSetMap rsm = BoatServices.getAll();
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
			ResultSetMap rsm = BoatServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/validar/{nrProtocolo}/{nrHash}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTIgnore
	public static String validarHash(@PathParam("nrProtocolo") String nrProtocolo, @PathParam("nrHash") String nrHash) {
		try {
			return new JSONObject(BoatServices.validarHash(nrProtocolo, nrHash)).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@GET
	@Path("/get/dat/{nrProtocolo}/{nrCpf}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getDAT(@PathParam("nrProtocolo") String nrProtocolo, @PathParam("nrCpf") String nrCpf) {
		try {
//			ObjectMapper obj = new ObjectMapper();
//			return obj.writeValueAsString(BoatServices.get(nrProtocolo, nrHash));
			
			return new JSONObject(BoatServices.get(nrProtocolo, nrCpf)).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	

	@GET
	@Path("/get/edit/dat/{nrProtocolo}/{nrCpf}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getEdit(@PathParam("nrProtocolo") String nrProtocolo, @PathParam("nrCpf") String nrCpf) {
		try {
			
			return new JSONObject(BoatServices.get(nrProtocolo, nrCpf)).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/count/{inicio}/{fim}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsQtBoat(@PathParam("inicio") String inicio, @PathParam("fim") String fim) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			return new JSONObject(BoatServices.statsQtBoat(dtInicial, dtFinal)).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@GET
	@Path("/get/chave/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String print(@PathParam("id") int cdBoat) {
		try {
			
			Result result = BoatServices.getHmac(cdBoat);
			return new JSONObject(result).toString();
		} catch (Exception e) {
			
			return null;
		}
	}

	@GET
	@Path("/{id}/declarante")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getDeclarante(@PathParam("id") int cdBoat) {
		try {			
			Criterios criterios = new Criterios();
			criterios.add(new ItemComparator("A.cd_boat", String.valueOf(cdBoat), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = BoatDeclaranteServices.find(criterios);
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {			
			return null;
		}
	}
	
	@POST
	@Path("/imprimir")
	@JWTIgnore
	public static Response printRelatorio(String nrProtocolo) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Result result = BoatServices.imprimirReport(nrProtocolo);
			if(result != null) {
				String reportName =  "mob/relatorio_boat";
				ResultSetMap rsm  = (ResultSetMap) result.getObjects().get("rsm");
				@SuppressWarnings("unchecked")
				HashMap<String, Object> paramns =  objectMapper.readValue(result.getObjects().get("jsonBoat").toString(), HashMap.class);
								
				paramns.put("LOGO_1", ImagemServices.getArrayBytesFromArrayList((ArrayList<Byte>)paramns.get("LOGO_1")));
				paramns.put("LOGO_2", ImagemServices.getArrayBytesFromArrayList((ArrayList<Byte>)paramns.get("LOGO_2")));
				
				
				byte[] print = ReportServices.getPdfReport(reportName, paramns, rsm);			
				
				return Response.ok(print).build();
			} else {
				return Response.serverError().build();
			}

		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
}
