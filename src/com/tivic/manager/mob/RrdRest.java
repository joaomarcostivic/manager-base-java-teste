package com.tivic.manager.mob;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Path("/mob/rrd/")

public class RrdRest {
	
	private RrdServices rrdServices = new RrdServices();
	private ManagerLog managerLog;
	
	public RrdRest() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Rrd rrd){
		try {
			System.out.println(rrd);
			Result result = RrdServices.save(rrd);
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
	public String remove(Rrd rrd){
		try {
			Result result = RrdServices.remove(rrd.getCdRrd());
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
			ResultSetMap rsm = RrdServices.getAll();
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
			ResultSetMap rsm = RrdServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String get(int cdAit) {
		try {
			return new JSONObject(RrdServices.get(cdAit)).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/stats/count/{inicio}/{fim}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String statsQtRrd(@PathParam("inicio") String inicio, @PathParam("fim") String fim) {
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio.replaceAll("-", "/") +" 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim.replaceAll("-", "/")    +" 23:59:59");
			
			return new JSONObject(RrdServices.statsQtRrd(dtInicial, dtFinal)).toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/imprimir")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Solicitação de impressão de RRD", notes = "Endpoint para solicitação de impressão de RRD")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "RRD impresso com sucesso."),
	        @ApiResponse(code = 204, message = "Não foi possível completar a solicitação. O RRD não foi encontrado."),
	        @ApiResponse(code = 500, message = "Erro no servidor."),
	})
	public Response imprimirTrrav(@ApiParam(name = "cdRrd", value = "Código do RRD a ser impresso (Obrigatório).") @QueryParam("cdRrd") int cdRrd) {
	    try {
	        byte[] rrd = this.rrdServices.imprimirRrd(cdRrd);
	        return ResponseFactory.ok(rrd);
	    } catch (NoContentException e) {
	        managerLog.showLog(e);
	        return ResponseFactory.noContent(e.getMessage());
	    } catch (Exception e) {
	        managerLog.showLog(e);
	        return ResponseFactory.internalServerError(e.getMessage());
	    }
	}
}
