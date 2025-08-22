package com.tivic.manager.mob;

import java.sql.Types;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "TalonarioAIT", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Produces(MediaType.APPLICATION_JSON)
@Path("/v2/mob/ait/talonariosait")
public class TalonarioAITController {

	@POST
	@Path("")
	@ApiOperation(value = "Registra um novo talonário")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Talonário Registrado", response = TalonarioAIT.class),
			@ApiResponse(code = 400, message = "O registro do talonário possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public static Response create(
			@ApiParam(value = "Talão a ser registrado", required = true) TalonarioAIT talonarioAit) {

		try {
			talonarioAit.setCdTalao(0);
			Result result = TalonarioAITServices.save(talonarioAit);
			if (result.getCode() < 0) {
				return ResponseFactory.badRequest("O registro do talão possui algum parâmetro inválido",
						result.getMessage());
			}

			return ResponseFactory.ok((TalonarioAIT) result.getObjects().get("TALONARIOAIT"));
		} catch (Exception e) {

			return ResponseFactory.internalServerError("Erro durante o processamento da requisição", e.getMessage());
		}
	}

	@PUT
	@Path("/{id}")
	@ApiOperation(value = "Registra um novo talonário")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Talonário não encontrado", response = TalonarioAIT.class),
			@ApiResponse(code = 204, message = "Talonario possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public static Response update(@ApiParam(value = "Talonário a ser registrado") TalonarioAIT talonarioAit,
			@ApiParam(value = "Código do Talonário") @PathParam("id") int cdTalao) {
		try {
			talonarioAit.setCdTalao(cdTalao);

			Result result = TalonarioAITServices.save(talonarioAit);
			if (result.getCode() < 0) {
				return ResponseFactory.badRequest("Talonario possui algum parâmetro inválido", result.getMessage());
			}

			return ResponseFactory.ok((TalonarioAIT) result.getObjects().get("TALONARIOAIT"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição", e.getMessage());
		}
	}

	@GET
	@Path("/{id}")
	@ApiOperation(value = "Retorna um talonário")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Talonário encontrado", response = TalonarioAIT.class),
			@ApiResponse(code = 400, message = "Talonário não localizado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamenteo da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public static Response get(@ApiParam(value = "Código do talonário") @PathParam("id") int cdTalao) {
		try {
			TalonarioAIT talonarioAit = TalonarioAITDAO.get(cdTalao);
			if (talonarioAit == null) {
				return ResponseFactory.noContent("Nenhum talonário encontrado");
			}

			return ResponseFactory.ok(talonarioAit);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição", e.getMessage());
		}
	}

	@GET
	@Path("")
	@ApiOperation(value = "Busca Talonários")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Talonário encontrado", response = TalonarioAIT[].class),
			@ApiResponse(code = 400, message = "Talonário não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(@ApiParam(value = "Código do talão") @QueryParam("cdTalao") int cdTalao,
			@ApiParam(value = "Nome do Agente") @QueryParam("nmAgente") String nmAgente,
			@ApiParam(value = "Número do talão") @QueryParam("nrTalao") Integer nrTalao,
			@ApiParam(value = "Número inicial") @QueryParam("nrInicial") Integer nrInicial,
			@ApiParam(value = "Número final") @QueryParam("nrFinal") Integer nrFinal,
			@ApiParam(value = "Data de entrega") @QueryParam("dtEntrega") String dtEntrega,
			@ApiParam(value = "Sigla do talão") @QueryParam("sgTalao") String sgTalao,
			@ApiParam(value = "Situação do talão", allowableValues = "0, 1, 2, 3, 4, 5") @QueryParam("stTalao") @DefaultValue("-1") int stTalao,
			@ApiParam(value = "Código do agente") @QueryParam("cdAgente") Integer cdAgente,
			@ApiParam(value = "Tipo de talão", allowableValues = "0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14") @QueryParam("tpTalao") @DefaultValue("-1") int tpTalao) {
		try {

			Criterios crt = new Criterios();

			if (nmAgente != null) {
				crt.add("B.nm_agente", nmAgente, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			if (nrTalao != null) {
				crt.add("A.nr_talao", Integer.toString(nrTalao), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (nrInicial != null) {
				crt.add("A.nr_inicial", Integer.toString(nrInicial), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (nrFinal != null) {
				crt.add("A.nr_final", Integer.toString(nrFinal), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (dtEntrega != null) {
				crt.add("A.dt_entrega", dtEntrega, ItemComparator.EQUAL, Types.TIMESTAMP);
			}

			if (sgTalao != null) {
				crt.add("A.sg_talao", sgTalao, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if (stTalao > -1) {
				crt.add("A.st_talao", Integer.toString(stTalao), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (cdAgente != null) {
				crt.add("A.cd_agente", Integer.toString(cdAgente), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (tpTalao > -1) {
				crt.add("A.tp_talao", Integer.toString(tpTalao), ItemComparator.EQUAL, Types.INTEGER);
			}

			ResultSetMap _rsm = TalonarioAITServices.find(crt);

			if (_rsm == null || _rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhum talonário encontrado.");

			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());

		}
	}
}
