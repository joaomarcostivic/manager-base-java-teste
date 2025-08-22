package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.tivic.manager.util.ResultSetMapper;

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


@Api(value = "LinhaRota", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
			@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
	})
@Path("/v2/mob/linharotas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LinhaRotaController {
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova LinhaRota"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "LinhaRota a registrada", response = Linha.class),
			@ApiResponse(code = 400, message = "LinhaRota inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Rota a ser registrada") LinhaRota linhaRota) {
		try {			
			linhaRota.setCdRota(0);
			
			Result result = LinhaRotaServices.save(linhaRota);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((LinhaRota)result.getObjects().get("LINHAROTA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{cdLinha}/{cdRota}")
	@ApiOperation(
			value = "Atualiza uma LinhaRota"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "LinhaRota a ser atualizada", response = Linha.class),
			@ApiResponse(code = 400, message = "LinhaRota inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Código da Linha") @PathParam("cdLinha") int cdLinha,
			@ApiParam(value = "Código da Rota") @PathParam("cdRota") int cdRota,
			@ApiParam(value = "LinhaRota a ser registrado") LinhaRota linhaRota) {
		try {
			linhaRota.setCdLinha(cdLinha);
			linhaRota.setCdRota(cdRota);

			Result result = LinhaRotaServices.save(linhaRota);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((LinhaRota)result.getObjects().get("LINHAROTA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/linhaRotaDTO")
	@ApiOperation(
			value = "Registra uma nova LinhaRotaDTO"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "LinhaRotaDTO a registrada", response = Linha.class),
			@ApiResponse(code = 400, message = "LinhaRotaDTO inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response createLinhaRotaDTO(@ApiParam(value = "LinhaRotaDTO a ser registrada") LinhaRotaDTO linhaRotaDTO) {
		try {			
			linhaRotaDTO.getLinhaRota().setCdRota(0);
			
			Result resultRota = LinhaRotaServices.save(linhaRotaDTO.getLinhaRota());
			
			if(resultRota.getCode() < 0)
				return ResponseFactory.badRequest(resultRota.getMessage());
			
			LinhaRota linhaRota = (LinhaRota)resultRota.getObjects().get("LINHAROTA");
			
			List<LinhaTrecho> linhasTrechos = new ArrayList<LinhaTrecho>();
			
			for(LinhaTrechoDTO lt: linhaRotaDTO.getLinhaTrechos()) {
				linhasTrechos.add(lt.getLinhaTrecho());
			}
			Result resultTrechos = LinhaTrechoServices.saveTrechosOrdenandoPorList(linhaRota.getCdLinha(), linhaRota.getCdRota(), linhasTrechos);
			
			if(resultTrechos.getCode() < 0)
				return ResponseFactory.badRequest(resultTrechos.getMessage());
				
			
			return ResponseFactory.ok(linhaRota);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/linhaRotaDTO/{cdLinha}/{cdRota}")
	@ApiOperation(
			value = "Atualiza uma LinhaRota"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "LinhaRota a ser atualizada", response = Linha.class),
			@ApiResponse(code = 400, message = "LinhaRota inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response updateLinhaRotaDTO(
			@ApiParam(value = "Código da Linha") @PathParam("cdLinha") int cdLinha,
			@ApiParam(value = "Código da Rota") @PathParam("cdRota") int cdRota,
			@ApiParam(value = "LinhaRota a ser registrado") LinhaRotaDTO linhaRotaDTO) {
		try {
			linhaRotaDTO.getLinhaRota().setCdLinha(cdLinha);
			linhaRotaDTO.getLinhaRota().setCdRota(cdRota);

			Result resultRota = LinhaRotaServices.save(linhaRotaDTO.getLinhaRota());
			
			if(resultRota.getCode() < 0)
				return ResponseFactory.badRequest(resultRota.getMessage());
			
			LinhaRota linhaRota = (LinhaRota)resultRota.getObjects().get("LINHAROTA");
			
			List<LinhaTrecho> linhasTrechos = new ArrayList<LinhaTrecho>();
			
			for(LinhaTrechoDTO lt: linhaRotaDTO.getLinhaTrechos()) {
				linhasTrechos.add(lt.getLinhaTrecho());
			}

			Result resultTrechos = LinhaTrechoServices.saveTrechosOrdenandoPorList(linhaRota.getCdLinha(), linhaRota.getCdRota(), linhasTrechos);
			
			if(resultTrechos.getCode() < 0)
				return ResponseFactory.badRequest(resultTrechos.getMessage());
				
				
			return ResponseFactory.ok((LinhaRota)resultRota.getObjects().get("LINHAROTA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdLinha}/{cdRota}/paradas")
	@ApiOperation(
		value = "Fornece o registro de rotas de uma Linha dado seu id",
		notes = "Considere id = cdLinha"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Linhas encontradas"),
			@ApiResponse(code = 204, message = "Nenhuma linha"),
			@ApiResponse(code = 204, message = "Não existe linhas com o id indicado"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getParadasRota(
			@ApiParam(value = "Id da Linha") @PathParam("cdLinha") int cdLinha,
			@ApiParam(value = "Id da Linha") @PathParam("cdRota") int cdRota
	) {
		try {
			ResultSetMap rsm = LinhaRotaServices.getParadasOrdenadas(cdLinha, cdRota, true);
			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe paradas para a linha com o id indicado.");
			
			return ResponseFactory.ok(new LinhaTrechoDTO.ListBuilder(rsm).build());
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdLinha}/{tpRota}/rota")
	@ApiOperation(
		value = "Fornece o registro de rotas de uma Linha dado seu id",
		notes = "Considere id = cdLinha"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Linhas encontradas"),
			@ApiResponse(code = 204, message = "Nenhuma linha"),
			@ApiResponse(code = 204, message = "Não existe linhas com o id indicado"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getRotasLinha(
			@ApiParam(value = "Id da Linha") @PathParam("cdLinha") int cdLinha,
			@ApiParam(value = "Id da Linha") @PathParam("tpRota") int tpRota
	) {
		try {
			Criterios crt = new Criterios();
			ResultSetMap rsm = LinhaRotaServices.find(
				   crt.add("A.cd_linha", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER)
				   .add("A.tp_rota", Integer.toString(tpRota), ItemComparator.EQUAL, Types.INTEGER)
			);
			
			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe rotas para a linha com o id indicado.");
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/")
	@ApiOperation(
		value = "Fornece todos os registros de rota de uma linha",
		notes = "Considere id = cdLinha"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "LinhaRotas encontradas"),
		@ApiResponse(code = 204, message = "Nenhuma linha"),
		@ApiResponse(code = 204, message = "Não existe linhas com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAll(
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite,
		@QueryParam("cdLinha") int cdLinha,
		@DefaultValue("-1") @QueryParam("tpRota") int tpRota,
		@QueryParam("cdRota") int cdRota,
		@QueryParam("stRota") int stRota
	) {
		try {
			Criterios    crt = new Criterios();
			
			if(nrPagina != 0) {
				crt.add("qtDeslocamento", Integer.toString((nrLimite * nrPagina) - nrLimite), ItemComparator.EQUAL, Types.INTEGER);	
			}
			
			if(nrLimite != 0) {
				crt.add("qtLimite", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(cdLinha != 0) {
				crt.add("A.cd_linha", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(tpRota >= 0) {
				crt.add("A.tp_rota", Integer.toString(tpRota), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(cdRota != 0) {
				crt.add("A.cd_rota", Integer.toString(cdRota), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(stRota != 0) {
				crt.add("A.st_rota", Integer.toString(stRota), ItemComparator.EQUAL, Types.INTEGER);				
			}
						
			ResultSetMap rsm = LinhaRotaServices.find(crt);
			
			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe rotas com os filtros indicados.");

			
			return ResponseFactory.ok(new LinhaRotaDTO.ListBuilder(rsm).build());
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	

}
