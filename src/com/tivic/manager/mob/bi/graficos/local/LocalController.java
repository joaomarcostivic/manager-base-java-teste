package com.tivic.manager.mob.bi.graficos.local;

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

import com.sun.jna.platform.win32.Sspi.TimeStamp;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.mob.AitMovimentoDTO;
import com.tivic.manager.mob.BoatDAO;
import com.tivic.manager.mob.ConcessionarioPessoaDTO;
import com.tivic.manager.mob.bi.graficos.condutorveiculo.CondutorVeiculoDTO;
import com.tivic.manager.mob.bi.graficos.condutorveiculo.CondutorVeiculoServices;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.mob.BoatVeiculo;
import com.tivic.manager.mob.BoatVeiculoDAO;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.mob.bi.graficos.local.LocalServices;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "BI", tags = { "mob" })
@Path("/v2/mob/bi/local")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class LocalController {
	
	public LocalController() {
	}
	
	/*local*/
	@GET
	@Path("/tipo")
	@ApiOperation(value = "Retorna dados do Bolt")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Bolt BI encontrado", response = CondutorVeiculoDTO.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findBoltTipo (
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
			
			ResultSetMap biTipo = LocalServices.findBoatByTipo(crt);
			
			return ResponseFactory.ok(biTipo); 
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/boletim")
	@ApiOperation(value = "Retorna dados do Bolt")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Bolt BI encontrado", response = CondutorVeiculoDTO.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findBoltBoletim (
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
			
			ResultSetMap biBoletim = LocalServices.findBoatBoletim(crt);
			
			return ResponseFactory.ok(biBoletim); 
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/local")
	@ApiOperation(value = "Retorna dados do Bolt")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Bolt BI encontrado", response = CondutorVeiculoDTO.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findBoltLocal (
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
			
			ResultSetMap biLocal = LocalServices.findBoatLocal(crt);
			
			return ResponseFactory.ok(biLocal); 
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/localhora")
	@ApiOperation(value = "Retorna dados do Bolt")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Bolt BI encontrado", response = CondutorVeiculoDTO.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findBoltLocalHora (
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String Inicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String Final,
			@ApiParam(value = "Local") @QueryParam("dsLocal") String Local
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
			if(Local != null) {
				crt.add("A.ds_local_ocorrencia", Local.toUpperCase(), ItemComparator.LIKE_ANY, Types.CHAR);
			}
			
			ResultSetMap biLocalHora = LocalServices.findBoatLocalHora(crt);
			
			return ResponseFactory.ok(biLocalHora); 
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/localdia")
	@ApiOperation(value = "Retorna dados do Bolt")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Bolt BI encontrado", response = CondutorVeiculoDTO.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findBoltLocalDia (
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String Inicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String Final,
			@ApiParam(value = "Local") @QueryParam("dsLocal") String Local
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
			if(Local != null) {
				crt.add("A.ds_local_ocorrencia", Local.toUpperCase(), ItemComparator.LIKE_ANY, Types.CHAR);
			}
			
			ResultSetMap biLocalDia = LocalServices.findBoatLocaldia(crt);
			
			return ResponseFactory.ok(biLocalDia); 
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}