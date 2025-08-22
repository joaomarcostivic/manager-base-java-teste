package com.tivic.manager.mob.radar.sync;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.util.date.DateUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/v2/radar")
@Produces(MediaType.APPLICATION_JSON)
public class SyncRadarController {

	private SyncRadarService sync;
	
	public SyncRadarController() throws Exception {
		sync = new SyncRadarService();
	}
	
	@POST
	@Path("/sync")
	@ApiOperation(value = "Sincroniza os eventos do radar com o cliente")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Sincronização realizada com sucesso", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response sincronizar() {
		try {
			sync.sincronizar();
			return ResponseFactory.ok("Sincronização realizada com sucesso");
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	

	
	@POST
	@Path("/verify")
	@ApiOperation(value = "Verifica os eventos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Verificação realizada com sucesso", response = ResponseBody.class),
			@ApiResponse(code = 400, message = "Data de verificação inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response verificar(
			@ApiParam(value = "Data da verificação") @QueryParam("dtVerificacao") String queryDtVerificacao) {
		try {
			GregorianCalendar dtVerificacao = DateUtil.convStringToCalendar(queryDtVerificacao);
			sync.verificarEventos(dtVerificacao);
			return ResponseFactory.ok("Verificação realizada com sucesso");
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	


	@GET
	@Path("/eventosnaoemitidos")
	@ApiOperation(value = "Retorna eventos não emitidos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eventos encontrados", response = EventoEquipamento[].class),
			@ApiResponse(code = 204, message = "Eventos não encontrados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response getEventosNaoEmitidos() {
		try {
			List<EventoEquipamento> eventos = sync.getEventosNaoEmitidos();
			return ResponseFactory.ok(eventos);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/eventos/aits")
	@ApiOperation(value = "Transforma os eventos recebidos em AITs")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eventos encontrados", response = AitSyncDTO.class),
			@ApiResponse(code = 204, message = "Eventos não encontrados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response converterEventosEmAits(
			@ApiParam(value = "Lista de eventos") List<EventoEquipamento> eventos, 
			@ApiParam(value = "Código do usuário") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			AitSyncDTO aitSyncDTO = sync.converterEventosAit(eventos, cdUsuario);
			return ResponseFactory.ok(aitSyncDTO);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	

}