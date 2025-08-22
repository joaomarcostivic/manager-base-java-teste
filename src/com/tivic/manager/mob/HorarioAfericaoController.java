package com.tivic.manager.mob;

import java.sql.Types;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.report.ReportServices;
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

@Api(value = "Horario Aferição", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})

@Path("/v2/mob/horario/afericoes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class HorarioAfericaoController {
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna lista de Horarios aferidos"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Itens encontrados", response = HorarioAfericao[].class),
			@ApiResponse(code = 204, message = "Nenhuma horario encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Concessionario") @QueryParam("pessoa") String pessoa,
			@ApiParam(value = "Concessionario") @QueryParam("cdConcessionario") int cdConcessionario,
			@ApiParam(value = "Situação", allowableValues = "0, 1, 2") @QueryParam("stHorarioAfericao") @DefaultValue("-1") int stHorarioAfericao,
			@ApiParam(value = "Data de lançamento Inicial (dd/mm/yyyy)") @QueryParam("dtLancamentoInicial") String dtLancamentoInicial,
			@ApiParam(value = "Data de lançamento Final (dd/mm/yyyy)") @QueryParam("dtLancamentoFinal") String dtLancamentoFinal,
			@ApiParam(value = "Veiculo") @QueryParam("nrPrefixo") int nrPrefixo,
			@ApiParam(value = "Linha") @QueryParam("nrLinha") String nrLinha,
			@ApiParam(value  = "Busca em multiplos campos por esta palavra chave") @QueryParam("keyword") String keyword) {
		try {
			
			Criterios crt = new Criterios("qtLimite", Integer.toString(limit), 0, 0);
			
      if(pessoa != null) {
				crt.add("C.nm_agente", pessoa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
      
			if(cdConcessionario != 0) {
				crt.add("D.cd_concessionario", Integer.toString(cdConcessionario), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(dtLancamentoInicial != null) {
				crt.add("A.dt_lancamento", dtLancamentoInicial, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);
			}
			
			if(dtLancamentoFinal != null) {
				crt.add("A.dt_lancamento", dtLancamentoFinal, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			}
			
			if(nrPrefixo > 0) {
				crt.add("B.nr_prefixo",Integer.toString(nrPrefixo), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(stHorarioAfericao > -1) {
				crt.add("A.st_horario_afericao", Integer.toString(stHorarioAfericao),  ItemComparator.EQUAL, Types.INTEGER);
			}

			if(nrLinha != null) {
				crt.add("nr_linha", nrLinha, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if(keyword != null) {
				crt.add("keyword", keyword, ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			List<HorarioAfericaoDTO> list = HorarioAfericaoServices.findDTO(crt);

			
			if(list.isEmpty()) {
				return ResponseFactory.noContent(list);
			}
			
			return ResponseFactory.ok(list);			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/find")
	@ApiOperation(
			value = "Retorna lista de Horarios aferidos"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Itens encontrados", response = HorarioAfericao[].class),
			@ApiResponse(code = 204, message = "Nenhuma horario encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response find(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Concessionario") @QueryParam("nmConcessionario") String nmConcessionario,
			@ApiParam(value = "Concessionario") @QueryParam("cdConcessionario") int cdConcessionario,
			@ApiParam(value = "Situação") @QueryParam("stHorarioAfericao") @DefaultValue("-1") int stHorarioAfericao,
			@ApiParam(value = "Data de lançamento Inicial (dd/mm/yyyy)") @QueryParam("dtLancamentoInicial") String dtLancamentoInicial,
			@ApiParam(value = "Data de lançamento Final (dd/mm/yyyy)") @QueryParam("dtLancamentoFinal") String dtLancamentoFinal,
			@ApiParam(value = "Veiculo") @QueryParam("nrPrefixo") @DefaultValue("-1") int nrPrefixo,
			@ApiParam(value = "Linha") @QueryParam("nrLinha") String nrLinha) {
		try {

			Criterios crt = new Criterios("qtLimite", Integer.toString(limit), 0, 0);
			
			if(nmConcessionario != null) {
				crt.add("G.NM_PESSOA", nmConcessionario, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(cdConcessionario != 0) {
				crt.add("G.CD_PESSOA", Integer.toString(cdConcessionario), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(dtLancamentoInicial != null) {
				crt.add("A.DT_LANCAMENTO", dtLancamentoInicial, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);
			}
			
			if(dtLancamentoFinal != null) {
				crt.add("A.DT_LANCAMENTO", dtLancamentoFinal, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			}
			
			if(nrPrefixo > -1) {
				crt.add("B.NR_PREFIXO",Integer.toString(nrPrefixo), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(stHorarioAfericao > -1) {
				crt.add("A.ST_HORARIO_AFERICAO", Integer.toString(stHorarioAfericao),  ItemComparator.EQUAL, Types.INTEGER);
			}

			if(nrLinha != null) {
				crt.add("E.NR_LINHA", nrLinha, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			ResultSetMap rsm = HorarioAfericaoServices.find(crt);
			
			if(rsm == null) {
				return ResponseFactory.notFound("Nenhum registro encontrado");
			}
			
			return ResponseFactory.ok(rsm);			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna Horario Aferição"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Itens encontrados", response = HorarioAfericao.class),
			@ApiResponse(code = 404, message = "Nenhuma aferição de horario encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response get(@ApiParam(value = "Código da afericao") @PathParam("id") int cdControle) {
		try {
			HorarioAfericao afericao = HorarioAfericaoDAO.get(cdControle);
			
			if(afericao == null)
				return ResponseFactory.notFound("Nenhum registro encontrado");
			
			return ResponseFactory.ok(afericao);			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza Horario Afericao"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Itens atualizados", response = HorarioAfericao.class),
		@ApiResponse(code = 404, message = "Aferição do horario invalídos", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Código da Aferição") @PathParam("id") int cdControle,
			@ApiParam(value = "Aferição a ser atualizada") HorarioAfericao afericao) {
		try {
			afericao.setCdControle(cdControle);
			
			Result result = HorarioAfericaoServices.save(afericao);
			if(result.getCode() < 0) 
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((HorarioAfericao)result.getObjects().get("HORARIOAFERICAO"));
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/relatorio/afericao-horario")
	@ApiOperation(
		value = "Fornece uma concessao"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = ConcessaoDTO.class),
		@ApiResponse(code = 204, message = "Nao existe concessao com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Produces("application/pdf")
	public Response getRelatorioAfericaoHorario(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Concessionario") @QueryParam("pessoa") String pessoa,
			@ApiParam(value = "Situação", allowableValues = "0, 1, 2") @QueryParam("stHorarioAfericao") @DefaultValue("-1") int stHorarioAfericao,
			@ApiParam(value = "Data de lançamento Inicial (dd/mm/yyyy)") @QueryParam("dtLancamentoInicial") String dtLancamentoInicial,
			@ApiParam(value = "Data de lançamento Final (dd/mm/yyyy)") @QueryParam("dtLancamentoFinal") String dtLancamentoFinal,
			@ApiParam(value = "Veiculo") @QueryParam("nrPrefixo") int nrPrefixo,
			@ApiParam(value = "Linha") @QueryParam("nrLinha") String nrLinha,
			@ApiParam(value  = "Busca em multiplos campos por esta palavra chave") @QueryParam("keyword") String keyword
		) {
		try {
			Criterios crt = new Criterios();
			
			if(pessoa != null) {
				crt.add("C.nm_agente", pessoa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(dtLancamentoInicial != null) {
				crt.add("A.dt_lancamento", dtLancamentoInicial, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);
			}
			
			if(dtLancamentoFinal != null) {
				crt.add("A.dt_lancamento", dtLancamentoFinal, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			}
			
			if(nrPrefixo > 0) {
				crt.add("B.nr_prefixo",Integer.toString(nrPrefixo), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(stHorarioAfericao > -1) {
				crt.add("A.st_horario_afericao", Integer.toString(stHorarioAfericao),  ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(nrLinha != null) {
				crt.add("nr_linha", nrLinha, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if(keyword != null) {
				crt.add("keyword", keyword, ItemComparator.EQUAL, Types.VARCHAR);
			}

			ResultSetMap _rsm = HorarioAfericaoServices.find(crt);
			
			if( _rsm == null || _rsm.getLines().size() <= 0 ) 
				return ResponseFactory.noContent("Nenhuma concessão encontrada");
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("DS_TITULO_1", "PREFEITURA MUNICIPAL DE VITÓRIA DA CONQUISTA - BA");
			params.put("DS_TITULO_2", "SECRETARIA DE MOBILIDADE URBANA - SEMOB");
			params.put("DS_TITULO_3", "COORDENAÇÃO DE TRANSPORTE PÚBLICO");
						
			return ResponseFactory.ok(ReportServices.getPdfReport("mob/relatorio_afericao_horario", params, _rsm));
		} catch ( Exception e ) {
			return ResponseFactory.internalServerError( e.getMessage() );
		}
	}
}
