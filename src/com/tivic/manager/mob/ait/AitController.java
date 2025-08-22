package com.tivic.manager.mob.ait;

import java.sql.Types;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitDTO;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitSneDTO;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaDTO;
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

@Path("/v3/mob/ait")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class AitController {
	
	private ManagerLog managerLog;
	private IAitService aitService;
	
	public AitController() throws Exception {
		this.aitService = (IAitService) BeansFactory.get(IAitService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/listar")
	@ApiOperation(
			value = "Retorna AIT's para listagem"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Listagem de AIT's encontrada.", response = RelatorioAitDTO[].class),
			@ApiResponse(code = 204, message = "Listagem de AIT's não encontrada.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	}) 
	public Response gerarRelatorioAits(
			@ApiParam(value = "Cd. AIT") @QueryParam("cdAit") Integer cdAit,
			@ApiParam(value = "Situação atual do documento") @QueryParam("stAtual") Integer stAtual,
			@ApiParam(value = "Documento com movimento") @QueryParam("ctMovimento") Integer ctMovimento,
			@ApiParam(value = "Data Final de movimento") @QueryParam("dtFinalMovimento") String dtFinalMovimento,
			@ApiParam(value = "Data Incial de movimento") @QueryParam("dtInicialMovimento") String dtInicialMovimento,
			@ApiParam(value = "Documento sem movimento") @QueryParam("tpNaoContemMovimento") Integer tpNaoContemMovimento,
			@ApiParam(value = "Data Final de movimento não contido") @QueryParam("dtFinalMovimentoNaoContido") String dtFinalMovimentoNaoContido,
			@ApiParam(value = "Data Incial de movimento não contido") @QueryParam("dtInicialMovimentoNaoContido") String dtInicialMovimentoNaoContido,
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
			@ApiParam(value = "Estado de origem do veículo") @QueryParam("sgUfVeiculo") String sgUfVeiculo,
			@ApiParam(value = "Código da ocorrência") @QueryParam("cdOcorrencia") @DefaultValue("-1") Integer cdOcorrencia,
			@ApiParam(value = "Renavam") @QueryParam("nrRenavam") String nrRenavam,
			@ApiParam(value = "Login") @QueryParam("nmLogin") String nmLogin,
			@ApiParam(value = "Natureza da infração") @QueryParam("nmNatureza") String nmNatureza,
			@ApiParam(value = "Enviado ao Detran (true/false)") @DefaultValue("false") @QueryParam("lgEnviadoDetran") Boolean lgEnviadoDetran,
			@ApiParam(value = "Lista de radares em formato JSON") @QueryParam("radar") String radar,
			@ApiParam(value = "Consistencia do auto") @QueryParam("stAit") @DefaultValue("-1") int stAit,
			@QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.tp_status", stAtual, stAtual != null);
			searchCriterios.addCriteriosEqualInteger("ctMovimento", ctMovimento, ctMovimento != null);
			searchCriterios.addCriteriosGreaterDate("dtInicialContemMovimento", dtInicialMovimento, dtInicialMovimento != null);
			searchCriterios.addCriteriosMinorDate("dtFinalContemMovimento", dtFinalMovimento, dtFinalMovimento != null);
			searchCriterios.addCriterios("tpNaoContemMovimento", String.valueOf(tpNaoContemMovimento), ItemComparator.DIFFERENT, Types.INTEGER, tpNaoContemMovimento != null);
			searchCriterios.addCriteriosGreaterDate("dtInicialMovimentoNaoContido", dtInicialMovimentoNaoContido, dtInicialMovimentoNaoContido != null);
			searchCriterios.addCriteriosMinorDate("dtFinalMovimentoNaoContido", dtFinalMovimentoNaoContido, dtFinalMovimentoNaoContido != null);
			searchCriterios.addCriteriosEqualInteger("A.cd_usuario", cdUsuario, cdUsuario > -1);
			searchCriterios.addCriteriosGreaterDate("A.dt_digitacao", dtInicialDigitacao, dtInicialDigitacao != null);
			searchCriterios.addCriteriosMinorDate("A.dt_digitacao", dtFinalDigitacao, dtFinalDigitacao != null);
			searchCriterios.addCriterios("A.nr_placa", "%"  + nrPlaca + "%" , ItemComparator.LIKE, Types.VARCHAR, nrPlaca != null);
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
			searchCriterios.addCriteriosEqualString("D.nm_natureza", nmNatureza, nmNatureza != null);
			searchCriterios.addCriterios("A.sg_uf_veiculo", "%" +  sgUfVeiculo + "%", ItemComparator.LIKE, Types.VARCHAR, sgUfVeiculo != null);
			searchCriterios.addCriteriosEqualInteger("B.cd_ocorrencia", cdOcorrencia, cdOcorrencia > -1);
			searchCriterios.addCriteriosEqualInteger("B.lg_enviado_detran", lgEnviadoDetran ? EnviadoDetranEnum.LG_DETRAN_ENVIADA.getKey() : EnviadoDetranEnum.LG_DETRAN_NAO_ENVIADA.getKey());
			searchCriterios.addCriteriosEqualInteger("A.st_ait", stAit, stAit > -1);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			return ResponseFactory.ok(aitService.filtrarListagemAits(searchCriterios, lgExcetoCanceladas, tpEquipamento, radar));	
		} catch (ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/listar/opcao/sne")
	@ApiOperation(
			value = "Retorna AIT's por tipo de opção a SNE"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Opção a SNE dos AIT's encontrada.", response = RelatorioAitSneDTO[].class),
			@ApiResponse(code = 204, message = "Opção a SNE dos AIT's não encontrada.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	}) 
	public Response aitsOpcaoSne(
			@ApiParam(value = "Situação atual do documento") @QueryParam("stAtual") Integer stAtual,
			@ApiParam(value = "movimento") @QueryParam("tpStatus") @DefaultValue("-1") Integer tpStatus,
			@ApiParam(value = "Documento com movimento") @QueryParam("ctMovimento") @DefaultValue("-100") Integer ctMovimento,
			@ApiParam(value = "Data Incial de movimento") @QueryParam("dtInicialMovimento") @DefaultValue("2000-01-01") String dtInicialMovimento,
			@ApiParam(value = "Data Final de movimento") @QueryParam("dtFinalMovimento") @DefaultValue("-1") String dtFinalMovimento,
			@ApiParam(value = "Data inicial de infração") @QueryParam("dtInicialInfracao") String dtInicialInfracao,
			@ApiParam(value = "Data final de infração") @QueryParam("dtFinalInfracao") String dtFinalInfracao,
			@ApiParam(value = "Placa do veículo") @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(value = "Opção de SNE") @QueryParam("stSne") int stSne,
			@QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			dtFinalMovimento = dtFinalMovimento.equals("-1") ? Util.formatDate(Util.getDataAtual(), "yyyy-MM-dd") : dtFinalMovimento;
			searchCriterios.addCriteriosEqualInteger("A.tp_status", stAtual, stAtual != null);
			searchCriterios.addCriteriosEqualInteger("B.tp_status", ctMovimento, ctMovimento != null);
			searchCriterios.addCriteriosGreaterDate("B.dt_movimento_inicial", dtInicialMovimento, dtInicialMovimento != null);
			searchCriterios.addCriteriosMinorDate("B.dt_movimento_final", dtFinalMovimento, dtFinalMovimento != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_infracao", dtInicialInfracao, dtInicialInfracao != null);
			searchCriterios.addCriteriosMinorDate("A.dt_infracao", dtFinalInfracao, dtFinalInfracao != null);
			searchCriterios.addCriterios("A.nr_placa", nrPlaca, ItemComparator.LIKE, Types.VARCHAR, nrPlaca != null);
			searchCriterios.addCriteriosEqualInteger("B.st_adesao_sne", stSne, stSne > -1);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			return ResponseFactory.ok(aitService.filtrarAitsOpcaoSne(searchCriterios));	
		} catch (ValidacaoException ve) {
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@POST
	@Path("/cancelar")
	@ApiOperation(value = "Cancela lista de ait")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ait Cancelada", response= AitInconsistenciaDTO[].class),
			@ApiResponse(code = 204, message = "Ait não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	
	public Response cancelarAitList(
			@ApiParam(value = "Código do Ait") List<AitMovimento> aitMovimentoList) {
		try {
			this.aitService.cancelarListaAit(aitMovimentoList);
			return ResponseFactory.ok(aitMovimentoList);
		} catch(NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@PUT
	@Path("/inconsistencia")
	@ApiOperation(value = "Torna um ait inconsistente")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ait", response= Ait[].class),
			@ApiResponse(code = 204, message = "Ait não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	
	public Response definirComoInconsistente(
			@ApiParam(value = "Código do Ait") Ait ait) {
		try {
			this.aitService.definirComoInconsistente(ait);
			return ResponseFactory.ok(ait);
		} catch(NoContentException e) {
			this.managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			 e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/reverter-consistencia")
	@ApiOperation(value = "Torna um ait inconsistente")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ait", response= Ait[].class),
			@ApiResponse(code = 204, message = "Ait não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	
	public Response reverterInconsistencia(
			@ApiParam(value = "Código do Ait") Ait ait) {
		try {
			this.aitService.reverterConsistencia(ait);
			return ResponseFactory.ok(ait);
		} catch(NoContentException e) {
			this.managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			 e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
