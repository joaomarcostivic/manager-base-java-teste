package com.tivic.manager.mob.ait.relatorios;

import java.sql.Types;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
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
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;

@Api(value = "Relatório de AITs", tags = {"mob"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		}) 
})

@Path("/v3/mob/ait/relatorios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class RelatorioAitController {
	
	GerarRelatorioAit gerarRelatorioAit;
	IRelatorioAitCompetenciaEstadual relatorioAitCompetenciaEstadual;
	private ManagerLog managerLog;
	IGeraSegundaViaNaiService geraSegundaViaNaiService;
	
	public RelatorioAitController() throws Exception {	
		gerarRelatorioAit = new GerarRelatorioAit();
		this.relatorioAitCompetenciaEstadual = (IRelatorioAitCompetenciaEstadual) BeansFactory.get(IRelatorioAitCompetenciaEstadual.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		geraSegundaViaNaiService = (IGeraSegundaViaNaiService) BeansFactory.get(IGeraSegundaViaNaiService.class);
	}
	
	@POST
	@Path("/impressao")
	@ApiOperation(value = "Imprime o relatório de AITs filtrados na tela", notes = "Reimpressão de AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não ha relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response imprimirRelatorioAits(
			@ApiParam(value = "Lista de AITs buscados na tela") List<RelatorioAitDTO> aitsList,
			@ApiParam(value = "Filtros utilizados na busca") @QueryParam("filterList") String filterList
			) throws ValidacaoException {
		try {
			return ResponseFactory.ok(gerarRelatorioAit.gerarRelatorio(aitsList, filterList));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/print/relatorio/aits")
	@ApiOperation(
			value = "Retorna AITs para geração de relatório"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Listagem de AITs encontrada.", response = RelatorioAitDTO[].class),
			@ApiResponse(code = 204, message = "Listagem de AITs não encontrada.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	}) 
	@Produces({ "application/pdf", "application/json" })
	public Response gerarRelatorioAits(
			@ApiParam(value = "Cd. AIT") @QueryParam("cdAit") Integer cdAit,
			@ApiParam(value = "Situação atual do documento") @QueryParam("stAtual") Integer stAtual,
			@ApiParam(value = "Documento com movimento") @QueryParam("ctMovimento") @DefaultValue("-100") Integer ctMovimento,
			@ApiParam(value = "Data Final de movimento") @QueryParam("dtFinalMovimento") @DefaultValue("-1") String dtFinalMovimento,
			@ApiParam(value = "Data Incial de movimento") @QueryParam("dtInicialMovimento") @DefaultValue("2000-01-01") String dtInicialMovimento,
			@ApiParam(value = "Nome do usuário") @QueryParam("cdUsuario") @DefaultValue("-1") Integer cdUsuario,
			@ApiParam(value = "Data inicial de digitação") @QueryParam("dtInicialDigitacao") String dtInicialDigitacao,
			@ApiParam(value = "Data final de digitação") @QueryParam("dtFinalDigitacao") String dtFinalDigitacao,
			@ApiParam(value = "Placa do veículo") @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(value = "Nº do CPF ou CNPJ do proprietário") @QueryParam("nrCpfCnpjProprietario") String nrCpfCnpjProprietario,
			@ApiParam(value = "CPF do condutor") @QueryParam("nrCpfCondutor") String nrCpfCondutor,
			@ApiParam(value = "Tipo de AIT") @QueryParam("tpTalao") @DefaultValue("-1") Integer tpTalao,
			@ApiParam(value = "Data inicial de infração") @QueryParam("dtInicialInfracao") String dtInicialInfracao,
			@ApiParam(value = "Data final de infração") @QueryParam("dtFinalInfracao") String dtFinalInfracao,
			@ApiParam(value = "Tipo de equipamento") @QueryParam("tpEquipamento") Integer tpEquipamento,
			@ApiParam(value = "Orgão competente") @QueryParam("tpCompetencia") @DefaultValue("-1") Integer tpCompetencia,
			@ApiParam(value = "Código do agente autuador") @QueryParam("cdAgente") @DefaultValue("-1") Integer cdAgente,
			@ApiParam(value = "Nome do agente autuador") @QueryParam("nmAgente") String nmAgente,
			@ApiParam(value = "Id do AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Código do Detran") @QueryParam("nrCodDetran") @DefaultValue("-1") Integer nrCodDetran,
			@ApiParam(value = "Tipo de convênio") @QueryParam("tpConvenio") @DefaultValue("-1") Integer tpConvenio,
			@ApiParam(value = "Nome do proprietário") @QueryParam("nmProprietario") String nmProprietario,
			@ApiParam(value = "Auto assinado") @QueryParam("lgAutoAssinado") @DefaultValue("-1") Integer lgAutoAssinado,
			@ApiParam(value = "Exceto canceladas (true/false)") @DefaultValue("false") @QueryParam("lgExcetoCanceladas") Boolean lgExcetoCanceladas,
			@ApiParam(value = "Renavam") @QueryParam("nrRenavam") String nrRenavam,
			@ApiParam(value = "Estado de origem do veículo") @QueryParam("sgUfVeiculo") String sgUfVeiculo,
			@ApiParam(value = "Código da ocorrência") @QueryParam("cdOcorrencia") @DefaultValue("-1") Integer cdOcorrencia
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			dtFinalMovimento = dtFinalMovimento.equals("-1") ? Util.formatDate(Util.getDataAtual(), "yyyy-MM-dd") :  dtFinalMovimento;
			searchCriterios.addCriteriosEqualInteger("A.tp_status", stAtual, stAtual != null);
			searchCriterios.addCriteriosEqualInteger("B.tp_status", ctMovimento, ctMovimento != null);
			searchCriterios.addCriteriosGreaterDate("B.dt_movimento_inicial", dtInicialMovimento, dtInicialMovimento != null);
			searchCriterios.addCriteriosMinorDate("B.dt_movimento_final", dtFinalMovimento, dtFinalMovimento != null);
			searchCriterios.addCriteriosEqualInteger("A.cd_usuario", cdUsuario, cdUsuario > -1);
			searchCriterios.addCriteriosGreaterDate("A.dt_digitacao", dtInicialDigitacao, dtInicialDigitacao != null);
			searchCriterios.addCriteriosMinorDate("A.dt_digitacao", dtFinalDigitacao, dtFinalDigitacao != null);
			searchCriterios.addCriterios("A.nr_placa", nrPlaca, ItemComparator.LIKE, Types.VARCHAR, nrPlaca != null);
			searchCriterios.addCriterios("A.nr_cpf_cnpj_proprietario", nrCpfCnpjProprietario, ItemComparator.LIKE, Types.VARCHAR, nrCpfCnpjProprietario != null);
			searchCriterios.addCriterios("A.nr_cpf_condutor", nrCpfCondutor, ItemComparator.LIKE, Types.VARCHAR, nrCpfCondutor != null);
			searchCriterios.addCriteriosEqualInteger("E.tp_talao", tpTalao, tpTalao > -1);
			searchCriterios.addCriteriosGreaterDate("A.dt_infracao", dtInicialInfracao, dtInicialInfracao != null);
			searchCriterios.addCriteriosMinorDate("A.dt_infracao", dtFinalInfracao, dtFinalInfracao != null);
			searchCriterios.addCriteriosEqualInteger("D.tp_competencia", tpCompetencia, tpCompetencia > -1);
			searchCriterios.addCriteriosEqualInteger("A.cd_agente", cdAgente, cdAgente > -1);
			searchCriterios.addCriteriosEqualInteger("D.nr_cod_detran", nrCodDetran, nrCodDetran > -1);
			searchCriterios.addCriteriosEqualInteger("A.tp_convenio", tpConvenio, tpConvenio > -1);
			searchCriterios.addCriteriosEqualInteger("A.lg_auto_assinado", lgAutoAssinado, lgAutoAssinado > -1);
			searchCriterios.addCriterios("A.nr_renavan", nrRenavam, ItemComparator.LIKE, Types.VARCHAR, nrRenavam != null);
			searchCriterios.addCriteriosEqualInteger("F.tp_equipamento", tpEquipamento, tpEquipamento != null);
			searchCriterios.addCriterios("A.sg_uf_veiculo", "%" +  sgUfVeiculo + "%", ItemComparator.LIKE, Types.VARCHAR, sgUfVeiculo != null);
			searchCriterios.addCriteriosEqualInteger("B.cd_ocorrencia", cdOcorrencia, cdOcorrencia > -1);
			return ResponseFactory.ok(gerarRelatorioAit.gerar(searchCriterios, lgExcetoCanceladas, tpEquipamento));
		} catch (ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}
	
	@GET
	@Path("/competencia-estadual/lista")
	@ApiOperation(
			value = "Retorna AITs cadastrados de competencia estadual para listagem"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Listagem de AITs de competencia estadual encontrada.", response = RelatorioAitDTO[].class),
			@ApiResponse(code = 204, message = "Listagem de AITs de competencia estadual não encontrada.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	}) 
	public Response gerarRelatorioAits(
			@ApiParam(value = "Documento com movimento") @QueryParam("ctMovimento") int ctMovimento,
			@ApiParam(value = "Data inicial de infração") @QueryParam("dtInicialInfracao") String dtInicialInfracao,
			@ApiParam(value = "Data final de infração") @QueryParam("dtFinalInfracao") String dtFinalInfracao,
			@ApiParam(value = "Orgão competente") @QueryParam("tpCompetencia") @DefaultValue("-1") int tpCompetencia
			) {
		try {
			RelatorioAitCompetenciaEstadualSearch relatorioAitCompetenciaEstadualSearch = new RelatorioAitCompetenciaEstadualSearchBuilder()
					.setCtMovimento(ctMovimento)
					.setDtInicialInfracao(dtInicialInfracao)
					.setDtFinalInfracao(dtFinalInfracao)
					.setTpCompetencia(tpCompetencia)
					.build();
			return ResponseFactory.ok(relatorioAitCompetenciaEstadual.filtrarListagemAits(relatorioAitCompetenciaEstadualSearch));	
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/competencia-estadual/movimentacoes-ait")
	@ApiOperation(value = "Imprime o lote movimentacoes AIT de competência estadual com os dados presentes na tela do eTransito ADM", notes = "Reimpressão de AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response impressaoMovimentacoesAit(@ApiParam(value = "ids dos AITs") int[] cdAits) throws ValidacaoException {
		try {
			return ResponseFactory.ok(relatorioAitCompetenciaEstadual.gerarMovimentacoesAit(cdAits));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/competencia-estadual/segunda-via")
	@ApiOperation(value = "Imprime o lote segunda via de AIT de competência estadual com os dados presentes na tela do eTransito ADM", notes = "Reimpressão de AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response impressaoSegundaVia(@ApiParam(value = "ids dos AITs") int[] cdAits) throws ValidacaoException {
		try {
			return ResponseFactory.ok(relatorioAitCompetenciaEstadual.gerarSegudaVia(cdAits));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/nai/segunda-via")
	@ApiOperation(value = "Imprime um lote segunda via de NAIs")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não há relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response impressaoSegundaViaNai(@ApiParam(value = "ids dos AITs") int[] cdAits)  {
		try {
			return ResponseFactory.ok(this.geraSegundaViaNaiService.gerarSegundaViaNai(cdAits));
		} catch (BadRequestException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
