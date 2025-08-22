package com.tivic.manager.mob.tabelashorarios.relatorio;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.report.ReportServices;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.report.Report;
import com.tivic.sol.search.SearchCriterios;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ResultSetMap;

@Api(value = "Reletório Horário Aferição", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v2/mob/relatoriohorarios")
public class RelatorioHorarioController {
	
	@GET
	@Path("/")
	@ApiOperation(
			value = "Retorna lista de Horarios aferidos"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Itens encontrados", response = RelatorioHorarioDTO[].class),
			@ApiResponse(code = 204, message = "Nenhuma horario encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response find(
			@ApiParam(value = "Nome do Concessionario") @QueryParam("nmConcessionario") String nmConcessionario,
			@ApiParam(value = "Código do Concessionario") @QueryParam("cdConcessionario") @DefaultValue("-1") int cdConcessionario,
			@ApiParam(value = "Situação do horário aferido") @QueryParam("stHorarioAfericao") Integer stHorarioAfericao,
			@ApiParam(value = "Número do Prefixo") @QueryParam("nrPrefixo") Integer nrPrefixo,
			@ApiParam(value = "Número da Linha") @QueryParam("nrLinha") String nrLinha,
			@ApiParam(value = "Data de Lançamento") @QueryParam("dtLancamento") String dtLancamento,
			@ApiParam(value = "Data de lançamento Inicial (dd/mm/yyyy)") @QueryParam("dtLancamentoInicial") String dtLancamentoInicial,
			@ApiParam(value = "Data de lançamento Final (dd/mm/yyyy)") @QueryParam("dtLancamentoFinal") String dtLancamentoFinal,
			@ApiParam(value = "Hora Prevista de chegada") @QueryParam("hrPrevisto") String hrPrevisto,
			@ApiParam(value = "Hora de chegada") @QueryParam("hrChegada") String hrChegada,
			@ApiParam(value = "Hora de saída") @QueryParam("hrPartida") String hrPartida,
			@ApiParam(value = "Nome do Agente") @QueryParam("nmAgente") String nmAgente,
			@ApiParam(value = "Nome da Empresa") @QueryParam("idEmpresa") String idEmpresa,
			@QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite) {
		
		try {
			SearchCriterios searchCriterios = new SearchCriterios();

			searchCriterios.addCriteriosEqualInteger("G.CD_PESSOA", cdConcessionario, cdConcessionario > -1);
			searchCriterios.addCriteriosEqualInteger("A.ST_HORARIO_AFERICAO", stHorarioAfericao, stHorarioAfericao != null);
			searchCriterios.addCriteriosEqualInteger("B.NR_PREFIXO", nrPrefixo, nrPrefixo != null);
			searchCriterios.addCriteriosEqualString("E.NR_LINHA", nrLinha, nrLinha != null);
			searchCriterios.addCriteriosEqualString("G.NM_PESSOA", nmConcessionario, nmConcessionario != null);
			searchCriterios.addCriteriosEqualString("A.DT_LANCAMENTO", dtLancamento, dtLancamento != null);
			searchCriterios.addCriteriosGreaterDate("A.DT_LANCAMENTO", dtLancamentoInicial, dtLancamentoInicial != null );
			searchCriterios.addCriteriosMinorDate("A.DT_LANCAMENTO", dtLancamentoFinal, dtLancamentoFinal != null);
			searchCriterios.addCriteriosEqualString("A.HR_PREVISTO", hrPrevisto, hrPrevisto != null);
			searchCriterios.addCriteriosEqualString("A.HR_CHEGADA", hrChegada, hrChegada != null);
			searchCriterios.addCriteriosEqualString("A.HR_PARTIDA", hrPartida, hrPartida != null);
			searchCriterios.addCriteriosEqualString("C.NM_PESSOA", nmAgente, nmAgente != null);
			searchCriterios.addCriteriosEqualString("I.ID_EMPRESA", idEmpresa, idEmpresa != null);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			searchCriterios.setOrderBy("A.DT_LANCAMENTO DESC");
				
			ResultSetMap rsm = RelatorioHorarioServices.find(searchCriterios);
			return ResponseFactory.ok(new RelatorioHorarioDTOListBuilder(rsm, rsm.getTotal()).build());			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/relatorio")
	@ApiOperation(
		value = "Fornece uma concessao"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = RelatorioHorarioDTO.class),
		@ApiResponse(code = 204, message = "Nao existe concessao com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Produces("application/pdf")
	public Response getRelatorioAfericaoHorario(
			@ApiParam(value = "Concessionario") @QueryParam("pessoa") String pessoa,
			@ApiParam(value = "Concessionario") @QueryParam("nmConcessionario") String nmConcessionario,
			@ApiParam(value = "Situação") @QueryParam("stHorarioAfericao") Integer stHorarioAfericao,
			@ApiParam(value = "Data de lançamento Inicial (dd/mm/yyyy)") @QueryParam("dtLancamentoInicial") String dtLancamentoInicial,
			@ApiParam(value = "Data de lançamento Final (dd/mm/yyyy)") @QueryParam("dtLancamentoFinal") String dtLancamentoFinal,
			@ApiParam(value = "Veiculo") @QueryParam("nrPrefixo") Integer nrPrefixo,
			@ApiParam(value = "Linha") @QueryParam("nrLinha") String nrLinha
		) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("C.NM_AGENTE", pessoa, pessoa != null);
			searchCriterios.addCriteriosEqualString("G.NM_PESSOA", nmConcessionario, nmConcessionario != null);
			searchCriterios.addCriteriosEqualString("E.NR_LINHA", nrLinha, nrLinha != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_lancamento", dtLancamentoInicial, dtLancamentoInicial != null);
			searchCriterios.addCriteriosMinorDate("A.dt_lancamento", dtLancamentoFinal, dtLancamentoFinal != null);
			searchCriterios.addCriteriosEqualInteger("B.nr_prefixo", nrPrefixo, nrPrefixo != null);
			searchCriterios.addCriteriosEqualInteger("A.st_horario_afericao", stHorarioAfericao, stHorarioAfericao != null);
			
			Report report = RelatorioHorarioServices.findRelatorio(searchCriterios);
			return ResponseFactory.ok(ReportServices.getPdfReport("mob/relatorio_afericao_horario", report.getParams(), report.getRsm()));
		} catch ( Exception e ) {
			return ResponseFactory.internalServerError( e.getMessage() );
		}
	}
	
}
