package com.tivic.manager.mob.bi.graficos.condutorveiculo;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

@Api(value = "BI", tags = { "mob" })
@Path("/v2/mob/bi/condutorveiculo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class CondutorVeiculoController {
	
	public CondutorVeiculoController() {
	}
	
	/* ************************************************************************
	 * Graficos Condutor Veiculo
	 */
	
	@GET
	@Path("/genero")
	@ApiOperation(value = "Retorna dados do Bolt")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Bolt BI encontrado", response = CondutorVeiculoDTO.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findCondutorVeiculoBoltGenero (
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String Inicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String Final
			)
	{
		try {	
			
			Criterios crt = new Criterios();
			if(Inicial != null && Final != null) {
				if(Util.convStringToCalendar(Inicial + " 00:00:00").getTimeInMillis() > Util.convStringToCalendar(Final + " 23:59:59").getTimeInMillis()) {
					return ResponseFactory.internalServerError("Data inicial n�o pode ser maior que a final");
				}
			}
			if(Inicial != null) {
				String inicialData = Util.formatDate(Util.convCalendarToTimestamp(Util.convStringToCalendar(Inicial + " 00:00:00")), "dd/MM/yyyy HH:mm:ss");
				crt.add("A.dt_ocorrencia", inicialData, ItemComparator.GREATER_EQUAL, Types.DATE);
			}
			if(Final != null) {
				String finalData = Util.formatDate(Util.convCalendarToTimestamp(Util.convStringToCalendar(Final + " 23:59:59")), "dd/MM/yyyy HH:mm:ss");
				crt.add("A.dt_ocorrencia", finalData, ItemComparator.MINOR_EQUAL, Types.DATE);
			}
			
			ResultSetMap biGenero = CondutorVeiculoServices.findBoatByGenero(crt);
			
			return ResponseFactory.ok(biGenero); 
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	@GET
	@Path("/idade")
	@ApiOperation(value = "Retorna dados do Bolt")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Bolt BI encontrado", response = CondutorVeiculoDTO.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findCondutorVeiculoBoltIdade (
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String Inicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String Final
			)
	{
		try {	
			
			Criterios crt = new Criterios();
			if(Inicial != null && Final != null) {
				if(Util.convStringToCalendar(Inicial + " 00:00:00").getTimeInMillis() > Util.convStringToCalendar(Final + " 23:59:59").getTimeInMillis()) {
					return ResponseFactory.internalServerError("Data inicial n�o pode ser maior que a final");
				}
			}
			if(Inicial != null) {
				String inicialData = Util.formatDate(Util.convCalendarToTimestamp(Util.convStringToCalendar(Inicial + " 00:00:00")), "dd/MM/yyyy HH:mm:ss");
				crt.add("A.dt_ocorrencia", inicialData, ItemComparator.GREATER_EQUAL, Types.DATE);
			}
			if(Final != null) {
				String finalData = Util.formatDate(Util.convCalendarToTimestamp(Util.convStringToCalendar(Final + " 23:59:59")), "dd/MM/yyyy HH:mm:ss");
				crt.add("A.dt_ocorrencia", finalData, ItemComparator.MINOR_EQUAL, Types.DATE);
			}
			
			ResultSetMap biIdade = CondutorVeiculoServices.findBoatByIdade(crt);
			
			return ResponseFactory.ok(biIdade); 
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	@GET
	@Path("/especie")
	@ApiOperation(value = "Retorna dados do Bolt")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Bolt BI encontrado", response = CondutorVeiculoDTO.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findCondutorVeiculoBoltEspecie (
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String Inicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String Final
			)
	{
		try {	
			
			Criterios crt = new Criterios();
			if(Inicial != null && Final != null) {
				if(Util.convStringToCalendar(Inicial + " 00:00:00").getTimeInMillis() > Util.convStringToCalendar(Final + " 23:59:59").getTimeInMillis()) {
					return ResponseFactory.internalServerError("Data inicial n�o pode ser maior que a final");
				}
			}
			if(Inicial != null) {
				String inicialData = Util.formatDate(Util.convCalendarToTimestamp(Util.convStringToCalendar(Inicial + " 00:00:00")), "dd/MM/yyyy HH:mm:ss");
				crt.add("A.dt_ocorrencia", inicialData, ItemComparator.GREATER_EQUAL, Types.DATE);
			}
			if(Final != null) {
				String finalData = Util.formatDate(Util.convCalendarToTimestamp(Util.convStringToCalendar(Final + " 23:59:59")), "dd/MM/yyyy HH:mm:ss");
				crt.add("A.dt_ocorrencia", finalData, ItemComparator.MINOR_EQUAL, Types.DATE);
			}
			
			ResultSetMap biEspecie = CondutorVeiculoServices.findBoatByEspecie(crt);
			
			return ResponseFactory.ok(biEspecie); 
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	@GET
	@Path("/cidade")
	@ApiOperation(value = "Retorna dados do Bolt")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Bolt BI encontrado", response = CondutorVeiculoDTO.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findCondutorVeiculoBoltCidade (
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String Inicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String Final
			)
	{
		try {	
			
			Criterios crt = new Criterios();
			if(Inicial != null && Final != null) {
				if(Util.convStringToCalendar(Inicial + " 00:00:00").getTimeInMillis() > Util.convStringToCalendar(Final + " 23:59:59").getTimeInMillis()) {
					return ResponseFactory.internalServerError("Data inicial n�o pode ser maior que a final");
				}
			}
			if(Inicial != null) {
				String inicialData = Util.formatDate(Util.convCalendarToTimestamp(Util.convStringToCalendar(Inicial + " 00:00:00")), "dd/MM/yyyy HH:mm:ss");
				crt.add("A.dt_ocorrencia", inicialData, ItemComparator.GREATER_EQUAL, Types.DATE);
			}
			if(Final != null) {
				String finalData = Util.formatDate(Util.convCalendarToTimestamp(Util.convStringToCalendar(Final + " 23:59:59")), "dd/MM/yyyy HH:mm:ss");
				crt.add("A.dt_ocorrencia", finalData, ItemComparator.MINOR_EQUAL, Types.DATE);
			}
			
			ResultSetMap biCidade = CondutorVeiculoServices.findBoatByCidade(crt);
			
			return ResponseFactory.ok(biCidade); 
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	@GET
	@Path("/categoria")
	@ApiOperation(value = "Retorna dados do Bolt")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Bolt BI encontrado", response = CondutorVeiculoDTO.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findCondutorVeiculoBoltCategoria (
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String Inicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String Final
			)
	{
		try {	
			
			Criterios crt = new Criterios();
			if(Inicial != null && Final != null) {
				if(Util.convStringToCalendar(Inicial + " 00:00:00").getTimeInMillis() > Util.convStringToCalendar(Final + " 23:59:59").getTimeInMillis()) {
					return ResponseFactory.internalServerError("Data inicial n�o pode ser maior que a final");
				}
			}
			if(Inicial != null) {
				String inicialData = Util.formatDate(Util.convCalendarToTimestamp(Util.convStringToCalendar(Inicial + " 00:00:00")), "dd/MM/yyyy HH:mm:ss");
				crt.add("A.dt_ocorrencia", inicialData, ItemComparator.GREATER_EQUAL, Types.DATE);
			}
			if(Final != null) {
				String finalData = Util.formatDate(Util.convCalendarToTimestamp(Util.convStringToCalendar(Final + " 23:59:59")), "dd/MM/yyyy HH:mm:ss");
				crt.add("A.dt_ocorrencia", finalData, ItemComparator.MINOR_EQUAL, Types.DATE);
			}
			
			ResultSetMap biCategoria = CondutorVeiculoServices.findBoatByCategoria(crt);
			
			return ResponseFactory.ok(biCategoria); 
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
