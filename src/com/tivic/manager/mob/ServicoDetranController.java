package com.tivic.manager.mob;

import java.sql.Types;
import java.util.GregorianCalendar;
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
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tivic.manager.util.StringUtil;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.detran.mg.AitSyncDTO;
import com.tivic.manager.wsdl.detran.mg.consultainfracoes.ConsultarInfracoesDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.PontuacaoDadosCondutorDTO;
import com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso.AlteraPrazoRecursoDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
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

@Api(value = "ServicoDetran", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/detran")
@Produces(MediaType.APPLICATION_JSON)
public class ServicoDetranController {
	ServicoDetranConsultaServices servicoDetranConsultaServices;
	ServicoDetranServices servicoDetranServices;
	private ManagerLog managerLog;
	
	public ServicoDetranController() throws Exception {
		servicoDetranConsultaServices = new ServicoDetranConsultaServices();
		servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/consulta/placa")
	@ApiOperation(
			value = "Consulta uma determinada placa no Detran"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Consulta realizada com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Placa não enviada"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response consultarPlaca(@QueryParam("nrPlaca") String nrPlaca) {
		try {
			
			if(nrPlaca == null || nrPlaca.equals("")){
				return ResponseFactory.badRequest("Placa não enviada");
			}
			
			ServicoDetranObjeto servicoDetranObjeto = servicoDetranConsultaServices.consultarPlaca(nrPlaca);
			return ResponseFactory.ok(servicoDetranObjeto.getDadosRetorno());
			
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/consulta/ait/nacional")
	@ApiOperation(
			value = "Consulta uma Auto de Infração na Base Nacional"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Consulta realizada com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Id do AIT não enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response consultarAutoBaseNacional(@QueryParam("idAit") String idAit) {
		try {
			
			if(idAit == null || idAit.equals("")){
				return ResponseFactory.badRequest("Id do AIT não enviado");
			}
			
			ServicoDetranObjeto servicoDetranObjeto = servicoDetranConsultaServices.consultarAutoBaseNacional(idAit);
			return ResponseFactory.ok(new JSONObject(servicoDetranObjeto.getDadosRetorno().toString()));
			
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@GET
	@Path("/consulta/ait/estadual")
	@ApiOperation(
			value = "Consulta uma Auto de Infração na Base Estadual"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Consulta realizada com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Id do AIT não enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response consultarAutoBaseEstadual(@QueryParam("idAit") String idAit) {
		try {
			
			if(idAit == null || idAit.equals("")){
				return ResponseFactory.badRequest("Id do AIT não enviado");
			}
			
			ServicoDetranObjeto servicoDetranObjeto = servicoDetranConsultaServices.consultarAutoBaseEstadual(idAit);
			return ResponseFactory.ok(new JSONObject(servicoDetranObjeto.getDadosRetorno().toString()));
			
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/consulta/movimentacoes")
	@ApiOperation(
			value = "Consulta as movimentações de um ait"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Consulta realizada com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Placa não enviada"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response consultarMovimentacoes(@QueryParam("nrPlaca") String nrPlaca, @QueryParam("idAit") String idAit) {
		try {
			
			if(nrPlaca == null || nrPlaca.equals("")){
				return ResponseFactory.badRequest("Placa não enviada");
			}

			if(idAit == null || idAit.equals("")){
				return ResponseFactory.badRequest("Ait não enviado");
			}
			
			ServicoDetranObjeto servicoDetranObjeto = servicoDetranConsultaServices.consultarMovimentacoes(nrPlaca, idAit);
			return ResponseFactory.ok(servicoDetranObjeto.getDadosRetorno());
			
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/consulta/recursos")
	@ApiOperation(
			value = "Consulta os recursos e defesas de um ait"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Consulta realizada com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Placa não enviada"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response consultarRecursos(@QueryParam("nrPlaca") String nrPlaca, @QueryParam("idAit") String idAit) {
		try {
			
			if(nrPlaca == null || nrPlaca.equals("")){
				return ResponseFactory.badRequest("Placa não enviada");
			}

			if(idAit == null || idAit.equals("")){
				return ResponseFactory.badRequest("Ait não enviado");
			}
			
			ServicoDetranObjeto servicoDetranObjeto = servicoDetranConsultaServices.consultarRecursos(nrPlaca, idAit);
			return ResponseFactory.ok(servicoDetranObjeto.getDadosRetorno());
			
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	
	@POST
	@Path("remessa")
	@ApiOperation(
			value = "Envia uma remessa dos AITs pendentes para o Detran"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs enviados com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Nenhum movimento enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response remessa(List<AitMovimento> movimentos) {
		try {
			if(movimentos == null || movimentos.size() == 0) {
				return ResponseFactory.badRequest("Nenhum movimento enviado");
			}
			
			List<ServicoDetranDTO> servicosDetranDTO = servicoDetranServices.remessa(movimentos);
			return ResponseFactory.ok(construirRetorno(servicosDetranDTO));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@POST
	@Path("renainf")
	@ApiOperation(
			value = "Envia uma remessa dos AITs pendentes para o Detran"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs enviados com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Nenhum movimento enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renainf(List<AitMovimento> movimentos) {
		try {
			if(movimentos == null || movimentos.size() == 0) {
				return ResponseFactory.badRequest("Nenhum movimento enviado");
			}
			
			List<ServicoDetranDTO> servicosDetranDTO = servicoDetranServices.renainf(movimentos);
			return ResponseFactory.ok(construirRetorno(servicosDetranDTO));
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	private JSONArray construirRetorno(List<ServicoDetranDTO> servicosDetranDTO) {
		JSONArray jsonArray = new JSONArray();
		for(ServicoDetranDTO servicoDetranDTO : servicosDetranDTO) {
			try {
				jsonArray.put(new JSONObject(servicoDetranDTO.toString()));
			}
			catch(JSONException je) {je.printStackTrace();}
		}
		return jsonArray;
	}
	
	@POST
	@Path("/sync/ait/estadual")
	@ApiOperation(
			value = "Consulta uma Auto de Infração na Base Estadual e sincronizar"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Consulta realizada com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Id do AIT não enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response syncAutoBaseEstadual(
			@QueryParam("ait") String idAit, 
			@QueryParam("placa") String nrPlaca, 
			@ApiParam(value = "Usuário que importou o auto") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			if(idAit == null || idAit.trim().equals(""))
				return ResponseFactory.badRequest("Id do AIT não enviado");
			if(nrPlaca == null || nrPlaca.trim().equals(""))
				return ResponseFactory.badRequest("Placa não informada");
			Ait ait = servicoDetranServices.incluirAitSync(idAit, nrPlaca, cdUsuario);			
			return ResponseFactory.ok(ait);
		} catch (ValidacaoException ve) {
			return ResponseFactory.badRequest(ve.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@PUT
	@Path("/sync/ait/estadual")
	@ApiOperation(
			value = "Consulta uma Auto de Infração na Base Estadual e sincronizar"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Consulta realizada com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Id do AIT não enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response atualizarAutoBaseEstadual(
			@QueryParam("cdAit") int cdAit) {
		try {
			if(cdAit == 0)
				return ResponseFactory.badRequest("Código do AIT não enviado");
			Ait ait = servicoDetranServices.atualizarAitSync(cdAit);			
			return ResponseFactory.ok(ait);
		} catch (ValidacaoException ve) {
			return ResponseFactory.badRequest(ve.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/sync/ait/{cdAit}/estadual")
	@ApiOperation(
			value = "Consulta uma Auto de Infração na Base Estadual e sincronizar"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Consulta realizada com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Id do AIT não enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response verificarAutoBaseEstadual(
		@PathParam("cdAit") int cdAit
	) {
		try {
			AitSyncDTO aitSyncDto = servicoDetranServices.verificarAitSync(cdAit);
			return ResponseFactory.ok(aitSyncDto);
		} catch (ValidacaoException ve) {
			return ResponseFactory.badRequest(ve.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@GET
	@Path("/consulta/pontuacaodadoscondutor")
	@ApiOperation(
			value = "Consulta os Dados do Condutor"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Consulta realizada com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Documento não enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response consultarPontuacaoDadosCondutor(
			@ApiParam(value = "documento do condutor") @QueryParam("documento") String documento,
			@ApiParam(value = "tipo do documento (RENACH, PGU ou CPF)") @QueryParam("tpDocumento") int tpDocumento
	) {
		try {
			PontuacaoDadosCondutorDTO pontuacaoDadosCondutorDTO = servicoDetranServices.consultarPontuacaoDadosCondutor(documento, tpDocumento);
			return ResponseFactory.ok(pontuacaoDadosCondutorDTO);
		} 
		catch (ValidacaoException ve) {
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch(Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/consulta/infracoes")
	@ApiOperation(
			value = "Consulta as infrações"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Consulta realizada com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Documento não enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response consultarInfracoes(
			@ApiParam(value = "Documento do infrator") @QueryParam("documento") String documento,
			@ApiParam(value = "Tipo do documento (CNH ou CPF)") @QueryParam("tpDocumento") int tpDocumento,
			@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal
	) {
		try {
			ConsultarInfracoesDadosRetorno consultarInfracoesDadosRetorno = servicoDetranServices.consultarInfracoes(documento, tpDocumento, dtInicial, dtFinal);
			return ResponseFactory.ok(consultarInfracoesDadosRetorno.getListConsultaInfracaoOcorrenciaDTO());
		} 
		catch (ValidacaoException ve) {
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch(Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
		
	@PUT
	@Path("datalimiterecurso/{cdAit}")
	@ApiOperation(
			value = "Altera a data limite do recurso"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Alteração realizada com sucesso"),
			@ApiResponse(code = 400, message = "Id do AIT não enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alterarDataLimiteRecurso(
			@ApiParam(value = "Código do AIT") @PathParam("cdAit") int cdAit,
			@ApiParam(value = "Data Novo Prazo Recurso") GregorianCalendar novaDataLimiteRecurso)
	{
		try {
			return ResponseFactory.ok(servicoDetranServices.alterarDataLimiteRecurso(cdAit, novaDataLimiteRecurso));
		} catch (ValidacaoException ve) {
			return ResponseFactory.badRequest(ve.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@PUT
	@Path("alterarprazorecurso/{cdAit}")
	@ApiOperation(
			value = "Altera a data limite dos recursos (Defesa ou JARI)"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Alteração realizada com sucesso"),
			@ApiResponse(code = 400, message = "Id do AIT não enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alterarDataLimiteRecurso(
			@ApiParam(value = "Dados Para Novo Prazo", required = true) AlteraPrazoRecursoDTO alteraPrazoRecursoDTO)
	{
		try {
			Ait ait = servicoDetranServices.alterarPrazoRecurso(alteraPrazoRecursoDTO);
			return ResponseFactory.ok(ait);
		} catch (ValidacaoException ve) {
			managerLog.error("Erro ao alterar prazo recurso: ", ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} catch(Exception e) {
			managerLog.error("Erro ao alterar prazo recurso: ", e.getMessage());
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}	
	
	@POST
	@Path("auto/remessa")
	@ApiOperation(
			value = "Envia uma remessa dos AITs pendentes para o Detran"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs enviados com sucesso", response = ServicoDetranDTO.class),
			@ApiResponse(code = 400, message = "Nenhum movimento enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response envioManual(
			@ApiParam(value = "ID do Ait") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Status do Movimento") @QueryParam("tpStatus") Integer tpStatus,
			@ApiParam(value = "Movimentos pendentes de correção para envio") @QueryParam("lgCorrecao") Boolean lgCorrecao,
			@ApiParam(value = "Movimentos que não foram enviados para o detran (true/false)") @DefaultValue("false") @QueryParam("lgNaoEnviado") Boolean lgNaoEnviado,
			@ApiParam(value = "Data de movimento") @QueryParam("dtMovimento") String dtMovimento,
			@ApiParam(value = "Número de erro") @QueryParam("nrErro") String nrErro
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriterios("B.id_ait","%"  + idAit + "%", ItemComparator.LIKE, Types.VARCHAR, idAit != null);
			searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus, tpStatus != null);
			searchCriterios.addCriterios("A.nr_erro", null, ItemComparator.NOTISNULL, Types.CHAR, lgCorrecao != null);
			searchCriterios.addCriteriosEqualString("A.nr_erro", StringUtil.retirarZerosAEsquerda(nrErro), nrErro != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_movimento", dtMovimento, dtMovimento != null);
			searchCriterios.addCriteriosMinorDate("A.dt_movimento", dtMovimento, dtMovimento != null);
			servicoDetranServices.envioAutomativo(searchCriterios, lgNaoEnviado);
			return ResponseFactory.ok("Envios finalizados!");
		} catch (NoContentException nce) {
			this.managerLog.showLog(nce);
			return ResponseFactory.noContent();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@PUT
	@Path("loteimpressao/alterarprazorecurso")
	@ApiOperation(
			value = "Altera a data limite dos recursos (Defesa ou JARI) de um lote de impressão"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Alteração realizada com sucesso"),
			@ApiResponse(code = 400, message = "Id do AIT não enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alterarDataLimiteRecursoLoteImpressao(
			@ApiParam(value = "Dados Para Novo Prazo", required = true) AlteraPrazoRecursoDTO alteraPrazoRecursoDTO)
	{
		try {
			servicoDetranServices.alterarPrazoRecursoLoteImpressao(alteraPrazoRecursoDTO);
			return ResponseFactory.ok("Datas alteradas");
		} catch (ValidacaoException ve) {
			managerLog.error("Erro ao alterar prazo recurso: ", ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@PUT
	@Path("aits/alterarprazorecurso")
	@ApiOperation(
			value = "Altera a data limite dos recursos (Defesa ou JARI) de uma lista de AITs"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Alteração realizada com sucesso"),
			@ApiResponse(code = 400, message = "Id do AIT não enviado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alterarDataLimiteRecursoLoteAit(
			@ApiParam(value = "Data para alteração", required = true) AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) {
		try {
			servicoDetranServices.alterarPrazoRecursoLoteAits(alteraPrazoRecursoDTO);
			return ResponseFactory.ok("Datas alteradas com sucesso!");
		} catch (ValidacaoException ve) {
			managerLog.error("Erro ao alterar prazo recurso: ", ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
}
