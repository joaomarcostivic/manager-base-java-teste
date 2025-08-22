package com.tivic.manager.ptc.protocolosv3.processos;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.ptc.protocolosv3.search.ProtocoloSearchDTO;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Processos", tags = { "ptc" })
@Path("/v3/ptc/processos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProcessoController {
	
	private ManagerLog managerLog;
	
	public ProcessoController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Fornece lista de relatório de JARI"
			)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolos buscados com sucesso", response = ProtocoloSearchDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = AitMovimentoDocumentoDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response find(
		@ApiParam(value = "Tipo de processo") @QueryParam("tipo") int cdTipoDocumento,
		@ApiParam(value = "Código do AIT") @QueryParam("idAit") String idAit,
		@ApiParam(value = "Código do AIT ou do Documento") @QueryParam("aitDocumento") String aitDocumento,
		@ApiParam(value = "Código do Fase") @QueryParam("fase") int cdFase,
		@ApiParam(value = "Data Inicial") @QueryParam("dataInicial") String dtInicial,
		@ApiParam(value = "Data Final") @QueryParam("dataFinal") String dtFinal,
		@ApiParam(value = "Número do processo") @QueryParam("documento") String nrDocumento,
		@ApiParam(value = "Placa do Veículo") @QueryParam("placa") String placaVeiculo,
		@ApiParam(value = "Situação do processo") @QueryParam("cdSituacaoDocumento") int situacaoDocumento,
		@ApiParam(value = "Tipo de processo") @QueryParam("tpStatus") int tpStatus,
		@ApiParam(value = "Situação atual do processo") @QueryParam("stAtual") Integer stAtual,
		@ApiParam(value = "Data Inicial Vencimento") @QueryParam("dtJulgamentoInicial") String dtInicialVencimento,
		@ApiParam(value = "Data Final Vencimento") @QueryParam("dtJulgamentoFinal") String dtFinalVencimento,
		@ApiParam(value = "Processo com movimento") @QueryParam("ctMovimento") @DefaultValue("-100") Integer ctMovimento,
		@ApiParam(value = "Exceto canceladas (true/false)") @DefaultValue("false") @QueryParam("lgVencido") Boolean lgVencido,
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite
	) {
		try {	
			SearchCriterios searchCriterios = new ProcessoSearchBuilder()
					.setAitDocumento(aitDocumento)
					.setCdTipoDocumento(cdTipoDocumento)
					.setIdAit(idAit)
					.setStAtual(stAtual)
					.setCdFase(cdFase)
					.setNrDocumento(nrDocumento)
					.setNrPlaca(placaVeiculo)
					.setCdSituacaoDocumento(situacaoDocumento)
					.setDtProtocoloInicial(dtInicial)
					.setDtProtocoloFinal(dtFinal)
					.setTpStatus(tpStatus)
					.setStatus(ctMovimento)
					.setDtInicialVencimento(dtInicialVencimento)
					.setDtFinalVencimento(dtFinalVencimento)
					.setDeslocamento(nrLimite, nrPagina)
					.setLimite(nrLimite)
					.build();
			
			PagedResponse<ProcessosSearchDTO> protocolos = new ProcessoService().find(searchCriterios, lgVencido);
			return ResponseFactory.ok(protocolos);
		} catch (NoContentException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.noContent(ve.getMessage());
		} catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
