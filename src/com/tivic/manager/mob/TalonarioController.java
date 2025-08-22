package com.tivic.manager.mob;

import java.sql.Types;

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

@Api(value = "Talonários", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/talonarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TalonarioController {
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo talonário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Talonário registrado", response = Talonario.class),
			@ApiResponse(code = 400, message = "Talonário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Talonário a ser registrado") Talonario talonario) {
		try {			
			talonario.setCdTalao(0);
			
			Result result = TalonarioServices.save(talonario);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Talonario)result.getObjects().get("TALONARIO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/valid")
	@ApiOperation(
			value = "Registra um novo talonário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Talonário registrado", response = Talonario.class),
			@ApiResponse(code = 400, message = "Talonário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response createValid(@ApiParam(value = "Talonário a ser registrado") Talonario talonario) {
		try {			
			talonario.setCdTalao(0);
			
			Result result = TalonarioServices.save(talonario, null, TalonarioServices.getSaveTransitoValidators(talonario), null);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Talonario)result.getObjects().get("TALONARIO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um talonário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Talonário atualizado", response = Talonario.class),
			@ApiResponse(code = 400, message = "Talonário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Código do Talonário") @PathParam("id") int cdTalonario,
			@ApiParam(value = "Talonário a ser registrado") Talonario talonario) {
		try {			
			talonario.setCdTalao(cdTalonario);
			
			Result result = TalonarioServices.save(talonario);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Talonario)result.getObjects().get("TALONARIO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/valid/{id}")
	@ApiOperation(
			value = "Atualiza um talonário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Talonário atualizado", response = Talonario.class),
			@ApiResponse(code = 400, message = "Talonário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response updateValid(
			@ApiParam(value = "Código do Talonário") @PathParam("id") int cdTalonario,
			@ApiParam(value = "Talonário a ser registrado") Talonario talonario) {
		try {			
			talonario.setCdTalao(cdTalonario);
			
			Result result = TalonarioServices.save(talonario, null, TalonarioServices.getSaveTransitoValidators(talonario), null);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Talonario)result.getObjects().get("TALONARIO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/transporte/valid")
	@ApiOperation(
			value = "Registra um novo talonário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Talonário registrado", response = Talonario.class),
			@ApiResponse(code = 400, message = "Talonário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response createTransporteValid(@ApiParam(value = "Talonário a ser registrado") Talonario talonario) {
		try {			
			
			Result result = TalonarioServices.save(talonario, null, TalonarioServices.getSaveTransproteValidators(talonario), null);
			
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Talonario)result.getObjects().get("TALONARIO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/transporte/valid/{id}")
	@ApiOperation(
			value = "Atualiza um talonário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Talonário atualizado", response = Talonario.class),
			@ApiResponse(code = 400, message = "Talonário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response updateTransporteValid(
			@ApiParam(value = "Código do Talonário") @PathParam("id") int cdTalonario,
			@ApiParam(value = "Talonário a ser registrado") Talonario talonario) {
		try {	
			talonario.setCdTalao(cdTalonario);
			
			TalonarioServices.ativarTalao(talonario);
			Result result = TalonarioServices.save(talonario, null, TalonarioServices.getSaveTransproteValidators(talonario), null);
			
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Talonario)result.getObjects().get("TALONARIO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um talonário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Talonário encontrado", response = Talonario.class),
			@ApiResponse(code = 204, message = "Talonário não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código do Talonário") @PathParam("id") int cdTalonario) {
		try {			

			TalonarioDTO talonarioDto = new TalonarioDTO.Builder(cdTalonario, true).build();
			if(talonarioDto == null) {
				return ResponseFactory.noContent("Nenhum órgão encontrado");
			}
			
			return ResponseFactory.ok(talonarioDto);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Lista de talonários"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Talonários encontrados", response = Talonario[].class),
			@ApiResponse(code = 204, message = "Talonário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(name = "tipo", value = "\n"
											+ "\t 0: AIT Trânsito\n"
											+ "\t 1: AIT Trânsito (Eletrônico)\n"
											+ "\t 2: AIT Transporte\n"
											+ "\t 3: AIT Transporte (Eletrônico)\n"
											+ "\t 4: Talonário NIC\n"
											+ "\t 5: BOAT\n"
											+ "\t 6: BOAT (Eletrônico)\n"
											+ "\t 7: RRD\n"
											+ "\t 8: RRD (Eletrônico)\n"
											+ "\t 9: TRRAV\n"
											+ "\t10: TRRAV (Eletrônico)\n"
											+ "\t11: Transporte NIC\n"
											+ "\t12: AIT Video-monitoramento\n"
											+ "\t13: AIT Radar Estático\n"
											+ "\t14: AIT Radar Fixo\n", allowableValues = "0,1,2,4,3,5,6,7,8,9,10,11,12,13,14") 
				@DefaultValue("-1") 
				@QueryParam("tipo") int tpTalao,
			@ApiParam(name = "situacao", value = "\n"
												+ "\t 0: Inativo\n"
												+ "\t 1: Ativo\n"
												+ "\t 2: Concluído\n"
												+ "\t 3: Conferido\n"
												+ "\t 4: Divergente\n"
												+ "\t 5: Pendente\n", allowableValues = "0,1,2,3,4,5") 
				@DefaultValue("-1") 
				@QueryParam("situacao") int stTalao, 
			@ApiParam(value = "Número dentro da série do talonário", defaultValue = "0") @DefaultValue("0")  @QueryParam("numDaSerie") int nrDaSerie, 
			@ApiParam(value = "Sigla do talonário", defaultValue = "") @DefaultValue("")  @QueryParam("sigla") String sgTalao,
			@ApiParam(value = "Número do Talão", defaultValue = "-1") @DefaultValue("-1") @QueryParam("talao") int nrTalao,
			@ApiParam(value = "Número Inicial do Talão", defaultValue = "-1") @DefaultValue("-1") @QueryParam("nrInicial") int nrInicial,
			@ApiParam(value = "Número Final do Talão", defaultValue = "-1") @DefaultValue("-1") @QueryParam("nrFinal") int nrFinal,
			@ApiParam(value = "Data de entrega") @QueryParam("dtEntrega") String dtEntrega,
			@ApiParam(value = "Código Agente", defaultValue = "-1") @DefaultValue("-1") @QueryParam("agente") int cdAgente)
	{
		try {		
			Criterios criterios = new Criterios();
			
			if(nrTalao > -1)
            criterios.add("A.nr_talao", Integer.toString(nrTalao), ItemComparator.EQUAL, Types.INTEGER);
			if(tpTalao > -1)
				criterios.add("A.tp_talao", Integer.toString(tpTalao), ItemComparator.EQUAL, Types.INTEGER);
			if(stTalao > -1)
				criterios.add("A.st_talao", Integer.toString(stTalao), ItemComparator.EQUAL, Types.INTEGER);
			
			if(nrInicial > -1)
            criterios.add("A.nr_inicial", Integer.toString(nrInicial), ItemComparator.EQUAL, Types.INTEGER);
			if(nrFinal > -1)
            criterios.add("A.nr_final", Integer.toString(nrFinal), ItemComparator.EQUAL, Types.INTEGER);
			
			if(nrDaSerie > 0) {
				criterios.add("A.nr_inicial", Integer.toString(nrDaSerie), ItemComparator.MINOR_EQUAL, Types.INTEGER);
				criterios.add("A.nr_final", Integer.toString(nrDaSerie), ItemComparator.GREATER_EQUAL, Types.INTEGER);
			}
			
			if (dtEntrega != null) {
				criterios.add("A.dt_entrega", dtEntrega, ItemComparator.EQUAL, Types.TIMESTAMP);
			}

			if(cdAgente > -1) 
				criterios.add("A.cd_agente", Integer.toString(cdAgente), ItemComparator.EQUAL, Types.INTEGER);
			
			if(!sgTalao.equals(""))
				criterios.add("A.sg_talao", sgTalao, ItemComparator.EQUAL, Types.VARCHAR);
			
			ResultSetMap rsm = TalonarioServices.find(criterios);
			if(rsm == null || rsm.getLines().size() <= 0)
				return ResponseFactory.noContent("Nenhum talonário encontrado");
				
			return ResponseFactory.ok(new TalonarioDTO.ListBuilder(rsm).setAgente(rsm).build());
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("{tpTalonario}/proximo")
	@ApiOperation(
			value = "Lista de talonários"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Número de talão encontrado", response = Talonario[].class),
			@ApiResponse(code = 204, message = "Tipo de talonário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getUltimoTalao(@ApiParam ( value = "Tipo de talonário") @PathParam("tpTalonario") int tpTalonario) {
		
		try {
			Result res = TalonarioServices.getUltimoAit(tpTalonario);
			
			if(res != null)
				return ResponseFactory.ok(res);
			
			return ResponseFactory.badRequest("Tipo de Talão Inválido");
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}

}
