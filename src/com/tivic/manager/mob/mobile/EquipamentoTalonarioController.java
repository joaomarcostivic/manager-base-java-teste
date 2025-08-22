package com.tivic.manager.mob.mobile;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.mob.mobile.dto.QrCodeResponseDTO;
import com.tivic.manager.mob.mobile.dto.VinculacaoEquipamentoDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "LoteMovimentos", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/mobile")
@Produces(MediaType.APPLICATION_JSON)
public class EquipamentoTalonarioController {
	
	IEquipamentoTalonarioService equipamentoTalonarioService;
	private ManagerLog managerLog;
	
	public EquipamentoTalonarioController() throws Exception {
		this.equipamentoTalonarioService = (IEquipamentoTalonarioService) BeansFactory.get(IEquipamentoTalonarioService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/autenticacao")
	@Produces(MediaType.APPLICATION_JSON)
	public Response gerarQrCodeComParametros(@QueryParam("cdEquipamento") @DefaultValue("0") int cdEquipamento) {
	    try {
	        QrCodeResponseDTO responseDTO = equipamentoTalonarioService.gerarQrCodeComParametros(cdEquipamento);
	        return Response.ok(responseDTO).build();
	    } catch (ValidacaoException ve) {
	        managerLog.showLog(ve);
	        return ResponseFactory.badRequest(ve.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace(System.out);
	        return ResponseFactory.internalServerError(e.getMessage());
	    }
	}
	
	@POST
	@Path("/equipamento")
	@ApiOperation(
			value = "Registra um novo equipamento de Talonário Eletrônico"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Equipamento registrado", response = VinculacaoEquipamentoDTO.class),
			@ApiResponse(code = 400, message = "Equipamento inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response create(@ApiParam(value = "Equipamento a ser registrado") Equipamento equipamento) {
		try {			
	        VinculacaoEquipamentoDTO vinculacaoEquipamentoDTO = equipamentoTalonarioService.insert(equipamento);
			return ResponseFactory.ok(vinculacaoEquipamentoDTO);
		} catch(BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
	        e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
