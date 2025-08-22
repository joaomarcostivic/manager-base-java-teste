package com.tivic.manager.tasks.limpeza.lotes;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.tasks.limpeza.lotes.exceptions.LimpezaLoteDataMaiorException;
import com.tivic.manager.tasks.limpeza.lotes.exceptions.LimpezaSemCriteriosException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "LimpezaLotes", tags = { "mob" })
@Path("/v3/mob/limpeza/lotes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LimpezaLotesController {

	private ILimpezaLotesService limpezaLotesService;
	
	public LimpezaLotesController() throws Exception {
		this.limpezaLotesService = (ILimpezaLotesService) BeansFactory.get(ILimpezaLotesService.class);
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Limpeza de lotes de impressão.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lotes removidos com sucesso"),
            @ApiResponse(code = 400, message = "Erro na limpeza de lotes", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response limpar(
		@ApiParam(value = "Códigos dos lotes de impressão") @QueryParam("codigosLote") List<Integer> codigosLote,
		@ApiParam(value = "Data de criação do lote") @QueryParam("dataCriacao") String dtCriacao,
		@ApiParam(value = "Limite de lotes") @QueryParam("limit") int limite
	) {
		try {
			limpezaLotesService.limparLotes(new LimpezaLotesDTOBuilder()
				.codigosLote(codigosLote)
				.dataCriacao(dtCriacao)
				.limite(limite)
			.build());
			return ResponseFactory.ok("Limpeza realizada com sucesso");
		} catch (LimpezaSemCriteriosException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (LimpezaLoteDataMaiorException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
}
