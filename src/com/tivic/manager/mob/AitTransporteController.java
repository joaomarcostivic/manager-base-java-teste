package com.tivic.manager.mob;

import java.sql.Types;
import java.util.List;

import javax.ws.rs.Consumes;
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

@Api(value = "AITTRANSPORTE", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v2/mob/ait-transporte")
@Produces(MediaType.APPLICATION_JSON)
public class AitTransporteController {

	private final AitTransporteServices aitTransporteServices;

	public AitTransporteController() {
		this.aitTransporteServices = new AitTransporteServices();
	}

	@GET
	@Path("")
	@ApiOperation(value = "Busca de AITs")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "AIT encontrado", response = AitTransporte[].class),
			@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })

	public Response findAIT(@ApiParam(value = "id do AIT") @QueryParam("nrAit") String nrAit,
			@ApiParam(value = "tipo do AIT") @DefaultValue("-1") @QueryParam("tpAit") int tpAit,
			@ApiParam(value = "situação do AIT") @DefaultValue("-1") @QueryParam("stAit") int stAit,
			@ApiParam(value = "situação do AIT") @DefaultValue("-1") @QueryParam("cdAgente") int cdAgente ){
		try {
			Criterios criterios = new Criterios();

			if (nrAit != null && !nrAit.trim().equals("")) {
				criterios.add("nr_ait", nrAit, ItemComparator.EQUAL, Types.VARCHAR);
			}

			if (tpAit > -1) {
				criterios.add("tp_ait", Integer.toString(tpAit), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (stAit > -1) {
				criterios.add("st_ait", Integer.toString(stAit), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (cdAgente > -1) {
				criterios.add("A.cd_agente", Integer.toString(cdAgente), ItemComparator.EQUAL, Types.INTEGER);
			}

			List<AitTransporteDTO> aits = aitTransporteServices.findAit(criterios);

			if (aits == null || aits.size() <= 0)
				return ResponseFactory.noContent("Nao existe AIT com o id indicado.");

			return ResponseFactory.ok(aits);

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@POST
	@Path("/{tpTalao}")
	@ApiOperation(value = "Registra um novo AITTRANSPORTE")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "AITTRANSPORTE registrado", response = Ait.class),
			@ApiResponse(code = 400, message = "AITTRANSPORTE possui algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class) })
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "AIT a ser registrado", required = true) AitTransporte aitTransporte,
			@ApiParam(value = "tipo de Talão") @PathParam("tpTalao") int tpTalao) {
		try {

			Result r = AitTransporteServices.save(aitTransporte, tpTalao);
			if (r.getCode() < 0) {
				return ResponseFactory.badRequest("AITTRANSPORTE possui algum parametro invalido", r.getMessage());
			}

			return ResponseFactory.ok((AitTransporte) r.getObjects().get("AITTRANSPORTE"));

		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@PUT
	@Path("/{cdAit}/{tpTalao}")
	@ApiOperation(value = "Atualiza dados de um AITTRANSPORTE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITTRANSPORTE atualizado", response = AitTransporte.class),
			@ApiResponse(code = 400, message = "AITTRANSPORTE é nulo ou invalido"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id do AIT a ser atualizado", required = true) @PathParam("tpTalao") int tpTalao,
			@ApiParam(value = "Id do AIT a ser atualizado", required = true) @PathParam("cdAit") int cdAit,
			@ApiParam(value = "AITTRANSPORTE a ser atualizado", required = true) AitTransporte aitTransporte) {
		try {

			if (cdAit == 0)
				return ResponseFactory.badRequest("AIT é nulo ou invalido");

			Result r = AitTransporteServices.save(aitTransporte, tpTalao);
			if (r.getCode() < 0)
				return ResponseFactory.badRequest("AITTRANSPORTE é nulo ou invalido", r.getMessage());

			return ResponseFactory.ok((AitTransporte) r.getObjects().get("AITTRANSPORTE"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/{nrAit}")
	@ApiOperation(value = "Fornece um AIT dado o número indicado")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "AIT TRANSPORTE encontrado", response = AitImagem.class),
			@ApiResponse(code = 204, message = "Nao existe AIT TRANSPORTE com o número indicado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public static Response getAit(@ApiParam(value = "Número do AIT") @PathParam("nrAit") String nrAit) {
		try {
			Criterios crt = new Criterios();
			crt.add("nr_ait", nrAit,  ItemComparator.EQUAL, Types.VARCHAR);
			ResultSetMap rsm = AitTransporteServices.find(crt);

			if (!rsm.next())
				return ResponseFactory.noContent("Nao existe AIT com o número indicado.");
			
			AitTransporteDTO aitTransporteBuilder = new AitTransporteBuilder(rsm.getRegister())
					.setTalonario(rsm.getInt("cd_talao"), true)
					.setAgente(rsm.getInt("cd_agente"), true)
					.setInfracaoTransporte(rsm.getInt("cd_infracao"), true)
					.setConcessionario(rsm.getInt("cd_concessionario"), true)
					.setConcessaoVeiculo(rsm.getInt("cd_concessao_veiculo"), true)
					.build();
			return ResponseFactory.ok(aitTransporteBuilder);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
}