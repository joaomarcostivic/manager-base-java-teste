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

import com.tivic.manager.fta.CategoriaVeiculo;
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

@Api(value = "Aferição catraca", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })

@Path("v2/mob/afericao/catracas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AfericaoCatracaController {

	@POST
	@Path("/")
	@ApiOperation(value = "Cria as aferições de catraca")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Registros criados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public static Response create(
			@ApiParam(value = "Aferição a ser registrada") AfericaoCatracaDTO afericaoCatracaDTO) {
		try {
			afericaoCatracaDTO.getAfericaoCatraca().setCdAfericaoCatraca(0);
			Result result = AfericaoCatracaServices.save(afericaoCatracaDTO.getAfericaoCatraca(), 
					ImpedimentoAfericaoServices.getCdImpedimento(afericaoCatracaDTO.getImpedimentosAfericao()),
					afericaoCatracaDTO.getConcessaoVeiculo().getNrPrefixo());

			if (result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());

			return ResponseFactory.ok(result.getObjects().get("AFERICAOCATRACA"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("")
	@ApiOperation(value = "Retorna uma lista de aferições")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Aferições encontradas", response = AfericaoCatraca[].class),
			@ApiResponse(code = 204, message = "Aferições não encontradas", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "100") @DefaultValue("100") @QueryParam("limit") int limit,
			@ApiParam(value = "Concessionario") @QueryParam("cdConcessionario") int cdConcessionario,
			@ApiParam(value = "Agente") @QueryParam("nmAgente") String nmAgente,
			@ApiParam(value = "Prefixo") @QueryParam("nrPrefixo") int nrPrefixo,
			@ApiParam(value = "Lacre") @QueryParam("idLacre") String idLacre,
			@ApiParam(value = "Lacre") @QueryParam("nmOrder") String nmOrder,
			@ApiParam(value = "Tipo de Leitura", allowableValues = "0, 1, 2, 3, 4, 5, 6, 7") @QueryParam("tpLeitura") @DefaultValue("-1") int tipoLeitura,
			@ApiParam(value = "Data Inicial do Perido (dd/MM/yyyy)") @QueryParam("dtAfericaoInicial") String dtAfericaoInicial,
			@ApiParam(value = "Data Final do Perido (dd/MM/yyyy)") @QueryParam("dtAfericaoFinal") String dtAfericaoFinal,
			@ApiParam(value = "Ordernar por data ou prefixo", allowableValues = "0, 1") @QueryParam("tpOrdernar") int tipoOrdenar,
			@ApiParam(value = "Busca em multiplos campos por esta palavra chave") @QueryParam("keyword") String keyword) {
		try {
			Criterios crt = new Criterios("qtLimite", Integer.toString(limit), 0, 0);

			if (cdConcessionario != 0) {
				crt.add("F.cd_concessionario", Integer.toString(cdConcessionario), ItemComparator.EQUAL, Types.INTEGER);

			}

			if (nmAgente != null) {
				crt.add("H.nm_agente", nmAgente, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if (dtAfericaoInicial != null) {
				crt.add("A.dt_afericao", dtAfericaoInicial + " 00:00:00", ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);
			}

			if (dtAfericaoFinal != null) {
				crt.add("A.dt_afericao", dtAfericaoFinal + " 23:59:59", ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			}

			if (nrPrefixo > 0) {
				crt.add("B.nr_prefixo", Integer.toString(nrPrefixo), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (idLacre != null) {
				crt.add("D.id_lacre", idLacre, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if (tipoLeitura > -1) {
				crt.add("A.tp_leitura", Integer.toString(tipoLeitura), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (nmOrder != null && !nmOrder.trim().equals("")) {
				crt.add("ORDERBY", nmOrder, ItemComparator.EQUAL, Types.VARCHAR);
			}

			if (keyword != null) {
				crt.add("keyword", keyword, ItemComparator.EQUAL, Types.VARCHAR);
			}

			List<AfericaoCatracaDTO> list = AfericaoCatracaServices.findDTO(crt);

			if (list.isEmpty()) {
				return ResponseFactory.noContent(list);
			}

			return ResponseFactory.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/dataPosterior/{nrPrefixo}")
	@ApiOperation(value = "Retorna a ultima data da aferição catraca dado pelo o prefixo")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Itens encontrados", response = AfericaoCatraca.class),
			@ApiResponse(code = 404, message = "Nenhuma data encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getDataPosterior(@ApiParam(value = "última data da aferição do prefixo") @PathParam("nrPrefixo") int nrPrefixo) {
		try {
			  if(nrPrefixo == 0) 
				  return ResponseFactory.badRequest("numero do Prefixo nulo ou invalido");
			 
			AfericaoCatraca result = AfericaoCatracaServices.getDataPosterior(nrPrefixo);

			if (result == null)
				return ResponseFactory.noContent("Nao existe afericao com o id indicado.");

			return ResponseFactory.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{id}")
	@ApiOperation(value = "Retorna uma Aferição da Catraca")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Itens encontrados", response = AfericaoCatraca.class),
			@ApiResponse(code = 404, message = "Nenhuma aferição de horario encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public static Response get(@ApiParam(value = "Código da aferição") @PathParam("id") int cdAfericaoCatraca) {
		try {
			Criterios criterios = new Criterios();
			criterios.add("A.cd_afericao_catraca", Integer.toString(cdAfericaoCatraca), ItemComparator.EQUAL,
					Types.INTEGER);

			List<AfericaoCatracaDTO> list = AfericaoCatracaServices.findDTO(criterios);

			AfericaoCatracaDTO dto = null;
			if (list != null && list.size() > 0)
				dto = list.get(0);

			if (dto == null)
				return ResponseFactory.noContent("Nao existe afericao com o id indicado.");

			return ResponseFactory.ok(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{id}")
	@ApiOperation(value = "Atualiza Afericao Catraca")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Itens atualizados", response = AfericaoCatraca.class),
			@ApiResponse(code = 404, message = "Itens invalídos", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public static Response update(@ApiParam(value = "Código da Aferição") @PathParam("id") int cdAfericaoCatraca,
			@ApiParam(value = "Aferição a ser atualizada") AfericaoCatracaDTO afericaoCatracaDTO) {
		try {
			afericaoCatracaDTO.getAfericaoCatraca().setCdAfericaoCatraca(cdAfericaoCatraca);

			Result result = AfericaoCatracaServices.save(afericaoCatracaDTO.getAfericaoCatraca());

			if (result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());

			return ResponseFactory.ok((AfericaoCatraca) result.getObjects().get("AFERICAOCATRACA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
