package com.tivic.manager.mob.lotes.controllers.dividaativa;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lotes.builders.dividaativa.DividaAtivaSearchBuilder;
import com.tivic.manager.mob.lotes.builders.dividaativa.LoteDividaAtivaSerachBuilder;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaDTO;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaImportacaoDTO;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaRetornoDTO;
import com.tivic.manager.mob.lotes.dto.dividaativa.LoteDividaAtivaDTO;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.repository.dividaativa.LoteDividaAtivaRepository;
import com.tivic.manager.mob.lotes.service.dividaativa.ILoteDividaAtivaService;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.log.builders.InfoLogBuilder;
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

@Api(value = "DIVIDA ATIVA", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })

@Path("/v3/mob/divida-ativa")
public class DividaAtivaController {
	
	private ManagerLog managerLog;
	private ILoteDividaAtivaService dividaAtivaService;
	private LoteDividaAtivaRepository loteDividaAtivaRepository;
	
	public DividaAtivaController() throws Exception {		
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		dividaAtivaService = (ILoteDividaAtivaService) BeansFactory.get(ILoteDividaAtivaService.class);
		loteDividaAtivaRepository = (LoteDividaAtivaRepository) BeansFactory.get(LoteDividaAtivaRepository.class);
	}
	
	@GET
	@Path("/lote")
	@ApiOperation(value = "Buscar Lotes de dívida ativa", notes = "Endpoint para buscar Lotes com base nos parâmetros fornecidos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lotes encontrados", response = LoteImpressao.class),
			@ApiResponse(code = 204, message = "Nenhum AIT encontrado"),
			@ApiResponse(code = 400, message = "Parâmetros inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response findLotesDividaAtiva(
			@ApiParam(name = "idAit", value = "ID do AIT a ser buscado dentro do lote (opcional)") @QueryParam("idAit") String idAit,
			@ApiParam(name = "idLote", value = "ID do Lote a ser buscado (opcional)") @QueryParam("idLote") String idLote,
			@ApiParam(name = "dtCriacaoInicial", value = "Data inicial de ciração do lote (opcional)") @QueryParam("dtCriacaoInicial") String dtCriacaoInicial,
			@ApiParam(name = "dtCriacaoFinal", value = "Data final de criação do lote (opcional)") @QueryParam("dtCriacaoFinal") String dtCriacaoFinal,
			@ApiParam(name = "dtConfirmacaoInicial", value = "Data inicial de confirmação do lote (opcional)") @QueryParam("dtConfirmacaoInicial") String dtEnvioInicial,
			@ApiParam(name = "dtConfirmacaoFinal", value = "Data final de confirmação do lote (opcional)") @QueryParam("dtConfirmacaoFinal") String dtEnvioFinal,
			@ApiParam(name = "stLote", value = "Situação do lote (obrigatório)") @QueryParam("stLote") int stLote,
			@QueryParam("limit") int limit,
			@QueryParam("page") int page) {
		try {
			SearchCriterios searchCriterios = new LoteDividaAtivaSerachBuilder()
					.setIdAit(idAit)
					.setIdLote(idLote)
					.setDtCriacaoInicial(dtCriacaoInicial)
					.setDtCriacaoFinal(dtCriacaoFinal)
					.setDtEnvioInicial(dtEnvioInicial)
					.setDtEnvioFinal(dtEnvioFinal)
					.setDeslocamento(limit, page)
					.setLimit(limit)
					.build();

			managerLog.showLog(new InfoLogBuilder("[GET] /lote", "Requisição de busca de Lotes de Divida Ativa...").build());
			PagedResponse<LoteDividaAtivaDTO> lotesDivida = loteDividaAtivaRepository.findLotesDividaAtiva(searchCriterios);
			managerLog.showLog(new InfoLogBuilder("[GET] /lote", "A busca de Lotes de Divida foi realizada com sucesso.").build());
			return ResponseFactory.ok(lotesDivida);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Buscar AITs cadidatos a dívida ativa", notes = "Endpoint para buscar AITs cadidatos com base nos parâmetros fornecidos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs encontrados", response = DividaAtivaDTO.class),
			@ApiResponse(code = 204, message = "Nenhum AIT encontrado"),
			@ApiResponse(code = 400, message = "Parâmetros inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response findCandidatosDividaAtiva(
			@ApiParam(name = "idAit", value = "ID do AIT a ser buscado (opcional)") @QueryParam("idAit") String idAit,
			@ApiParam(name = "nrPlaca", value = "Número da placa do veículo (opcional)") @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(name = "cpfCnpjProprietario", value = "Número do cpf ou cnpj (opcional)") @QueryParam("cpfCnpjProprietario") String nrCpfCnpjProprietario,
			@ApiParam(name = "dtEmissaoInicialNp", value = "Data inicial da emissão da NP (opcional)") @QueryParam("dtEmissaoInicialNp") String dtEmissaoInicialNp,
			@ApiParam(name = "dtEmissaoFinalNp", value = "Data final da emissão da NP (opcional)") @QueryParam("dtEmissaoFinalNp") String dtEmissaoFinalNp,
			@ApiParam(name = "dtValidadeInicialNp", value = "Data inicial da validade da NP (opcional)") @QueryParam("dtValidadeInicialNp") String dtValidadeInicialNp,
			@ApiParam(name = "dtValidadeFinalNp", value = "Data final da validade da NP (opcional)") @QueryParam("dtValidadeFinalNp") String dtValidadeFinalNp,
			@ApiParam(name = "vlMulta", value = "valor da multa Inicial (opcional)") @QueryParam("vlMulta") String vlMulta,
			@ApiParam(name = "vlMultaFinal", value = "valor da multa Final (opcional)") @QueryParam("vlMultaFinal") String vlMultaFinal
			) {
		try {
			SearchCriterios searchCriterios = new DividaAtivaSearchBuilder()
					.setIdAit(idAit)
					.setNrPlaca(nrPlaca)
					.setDtEmissaoInicial(dtEmissaoInicialNp)
					.setDtEmissaoFinal(dtEmissaoFinalNp)
					.setDtVencimentoInicial(dtValidadeInicialNp)
					.setDtVencimentoFinal(dtValidadeFinalNp)
					.setvlMultaInicial(vlMulta)
					.setvlMultaFinal(vlMultaFinal)
					.setNrCpfCnpjProprietario(nrCpfCnpjProprietario)
					.setTpStatus()
					.build();

			managerLog.showLog(new InfoLogBuilder("[GET]", "Requisição de busca de AITs Candidatos...").build());
			PagedResponse<DividaAtivaDTO> aitsCandidatos = loteDividaAtivaRepository.findCandidatos(searchCriterios);
			managerLog.showLog(new InfoLogBuilder("[GET]", "A busca de AITs Candidatos foi realizada com sucesso.").build());
			return ResponseFactory.ok(aitsCandidatos);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/lote/{cdLote}")
	@ApiOperation(value = "Buscar aits de um lote de dívida ativa", notes = "Endpoint para buscar aits de um lote com base nos parâmetros fornecidos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ait encontrados", response = LoteImpressao.class),
			@ApiResponse(code = 204, message = "Nenhum AIT encontrado"),
			@ApiResponse(code = 400, message = "Parâmetros inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response findDetalhesLoteDividaAtiva(
			@ApiParam(name = "cdLote", value = "código do Lote") @PathParam("cdLote") int cdLote,
			@ApiParam(name = "idAit", value = "ID do AIT a ser buscado dentro do lote (opcional)") @QueryParam("idAit") String idAit,
			@ApiParam(name = "nrPlaca", value = "Número da placa") @QueryParam("nrPlaca") String nrPlaca)
	{
		try {
			SearchCriterios searchCriterios = new LoteDividaAtivaSerachBuilder()
					.setIdAit(idAit)
					.setCdLote(cdLote)
					.setNrPlaca(nrPlaca)
					.build();

			managerLog.showLog(new InfoLogBuilder("[GET] /lote/"+cdLote, "Buscando aits no lote de Divida Ativa...").build());
			List<DividaAtivaDTO> lotesDivida = dividaAtivaService.searchInLoteDividaAtiva(searchCriterios);
			managerLog.showLog(new InfoLogBuilder("[GET] /lote/"+cdLote, "A busca por aits no lote de Divida ativa foi realizada com sucesso.").build());
			return ResponseFactory.ok(lotesDivida);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/{cdUsuario}")
	@ApiOperation(value = "Gerar lotes AITs cadidatos a dívida ativa", notes = "Endpoint para gerar lotes de AITs cadidatos a divida ativa.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote Gerado com sucesso.", response = LoteImpressao.class),
			@ApiResponse(code = 400, message = "Parâmetros inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response gerarLote(
			@ApiParam(name = "aiList", value = "Lista de Aits") List<DividaAtivaDTO> aitList,
			@ApiParam(name = "cdUsuario", value = "código do usuário") @PathParam("cdUsuario") int cdUsuario)
	{
		try {
			managerLog.showLog(new InfoLogBuilder("[POST]", "Gerando lotes de AITs candidatos a divida ativa...").build());
			Lote lote = dividaAtivaService.gerarLoteDividaAtiva(aitList, cdUsuario);
			managerLog.showLog(new InfoLogBuilder("[POST]", "Lote gerado com sucesso.").build());
			return ResponseFactory.created(lote);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/arquivo/{cdArquivo}")
	@ApiOperation(value = "Baixa o arquivo CSV referente ao lotes de dívida ativa", notes = "EndPoint baixar o arquivo CSV referente ao lotes de dívida ativa.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo baixado com sucesso.", response = ResponseBody.class),
			@ApiResponse(code = 400, message = "Parâmetros inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces({"application/txt", "application/json"})
	public Response getArquivoEnvio(
			@ApiParam(name = "cdArquivo", value = "código do arquivo") @PathParam("cdArquivo") int cdArquivo)
	{
		try {
			managerLog.showLog(new InfoLogBuilder("[GET]", "Baixando arquivo CSV do lote divida ativa...").build());
			byte[] arquivoLote = dividaAtivaService.getArquivo(cdArquivo);
			managerLog.showLog(new InfoLogBuilder("[GET]", "Arquivo baixado com sucesso.").build());
			return ResponseFactory.ok(arquivoLote);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/inscrever/{cdUsuario}")
	@ApiOperation(value = "Confirma os aits candidatos na dívida ativa", notes = "Endpoint para confirma os aits candidatos na dívida ativa.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Aits confirmados com sucesso.", response = ResponseBody.class),
			@ApiResponse(code = 400, message = "Parâmetros inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response inserirDividaAtiva(
			@ApiParam(name = "aits", value = "lista de id_ait") DividaAtivaImportacaoDTO dividasRetorno,
			@ApiParam(name = "cdUsuario", value = "código do usuário") @PathParam("cdUsuario") int cdUsuario)
	{
		try {
			managerLog.showLog(new InfoLogBuilder("[POST]", "Confirmando aits na divida ativa...").build());
			DividaAtivaImportacaoDTO dividasConfirmadas = dividaAtivaService.confirmarDividaAtiva(dividasRetorno, cdUsuario);
			managerLog.showLog(new InfoLogBuilder("[POST]", "Aits confirmados com sucesso.").build());
			return ResponseFactory.ok(dividasConfirmadas);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
