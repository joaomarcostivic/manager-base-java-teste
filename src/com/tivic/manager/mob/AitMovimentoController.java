package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

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

@Api(value = "AIT", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/aits")
@Produces(MediaType.APPLICATION_JSON)
public class AitMovimentoController {

	@POST
	@Path("/{id}/movimentos")
	@ApiOperation(
			value = "Registra um novo movimento de AIT"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Movimento registrado"),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit, @ApiParam(value = "Movimento a ser registrado", required = true) AitMovimento movimento) {
		try {
			movimento.setCdAit(cdAit);
			movimento.setCdMovimento(0);
			
			Result result = AitMovimentoServices.save(movimento);
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.created((AitMovimento) result.getObjects().get("AITMOVIMENTO"));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}/movimentos/{idMovimento}")
	@ApiOperation(
			value = "Edita movimento de AIT"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Movimento editado"),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit, 
								  @ApiParam(value = "id do Movimento") @PathParam("idMovimento") int cdAitMovimento,
								  @ApiParam(value = "Movimento a ser editado", required = true) AitMovimento movimento) {
		try {
			movimento.setCdAit(cdAit);
			movimento.setCdMovimento(cdAitMovimento);
			
			Result result = AitMovimentoServices.save(movimento);
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.created((AitMovimento) result.getObjects().get("AITMOVIMENTO"));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/movimentos/{idMovimento}/arquivos")
	@ApiOperation(
		value = "Fornece lista de arquivos de movimento do AIT dado seu id e o id do movimento",
		notes = "Considere id = cdAit e idMovimento = cdMovimento"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Arquivos de movimento encontrados"),
		@ApiResponse(code = 204, message = "Nenhum arquivo de movimento"),
		@ApiResponse(code = 204, message = "Não existe AIT ou moviemento com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAllArquivosMovimento(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit, 
			  									   @ApiParam(value = "id do Movimento") @PathParam("idMovimento") int cdMovimento) {
		try {
			
			ResultSetMap rsm = AitMovimentoServices.getAllArquivosMovimento(cdAit, cdMovimento);
			
			if(rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent();
			}
				
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {	
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/movimentos")
	@ApiOperation(
		value = "Fornece lista de movimentos do AIT dado seu id",
		notes = "Considere id = cdAit"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Movimentos encontrados"),
		@ApiResponse(code = 204, message = "Nenhum movimento"),
		@ApiResponse(code = 204, message = "Não existe AIT com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAllMovimentos(@ApiParam(value = "Id do AIT") @PathParam("id") int cdAit) {
		try {
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_ait", Integer.toString(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = AitMovimentoServices.find(criterios);
			
			if(rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent();
			}
				
			return ResponseFactory.ok(new ResultSetMapper<AitMovimento>(rsm, AitMovimento.class));
		} catch(Exception e) {	
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}

	@GET
	@Path("/movimentos/pendentes")
	@ApiOperation(
		value = "Fornece lista de movimentos pendentes"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Movimentos pendentes encontrados"),
		@ApiResponse(code = 204, message = "Nenhum movimento pendente"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAllMovimentosPendentes(
		@ApiParam(value = "ID do Ait") @QueryParam("idAit") String idAit,
		@ApiParam(value = "Status do Movimento") @QueryParam("tpStatus") Integer tpStatus,
		@ApiParam(value = "Movimentos pendentes de correção para envio") @QueryParam("lgCorrecao") Integer lgCorrecao,
		@ApiParam(value = "Data de Vencimento do envio do movimento") @QueryParam("dtVencimento") String dtVencimento,
		@ApiParam(value = "Data de Vencimento do envio do movimento") @QueryParam("dtMovimento") String dtMovimento,
		@ApiParam(value = "Data de Vencimento do envio do movimento") @QueryParam("nrErro") String nrErro
	) {
		try {
			ArrayList<ItemComparator> criterios = AitMovimentoServices.getCriteriosRemessa();
			
			if(idAit != null)
				criterios.add(new ItemComparator("AIT.id_ait", idAit, ItemComparator.EQUAL, Types.VARCHAR));
			
			if(tpStatus != null) {
				criterios.add(new ItemComparator("A.tp_status", String.valueOf(tpStatus), ItemComparator.EQUAL, Types.INTEGER));
				
				if(dtVencimento != null)
					AitMovimentoServices.getDataMovimentoAVencer(tpStatus, criterios, dtVencimento);
			}
			
			if(lgCorrecao != null)
				criterios.add(new ItemComparator("A.nr_erro", null, ItemComparator.NOTISNULL, Types.CHAR));
			
			if(nrErro != null)
				criterios.add(new ItemComparator("A.nr_erro", nrErro, ItemComparator.EQUAL, Types.CHAR));
			
			if(dtMovimento != null) {
				criterios.add(new ItemComparator("A.dt_movimento", dtMovimento + " 00:00:00", ItemComparator.GREATER_EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("A.dt_movimento", dtMovimento + " 23:59:59", ItemComparator.MINOR_EQUAL, Types.VARCHAR));
			}
			
			System.out.println(criterios.toString());
			
			ResultSetMap rsm = AitMovimentoServices.findRemessa(criterios);
			
			if(rsm.getLines().size() <= 0)
				return ResponseFactory.noContent();
			
			return ResponseFactory.ok(new AitMovimentoDTO.ListBuilder(rsm).setAit(rsm).build());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/movimentos/{idMovimento}")
	@ApiOperation(
		value = "Fornece um movimento dado o id indicado",
		notes = "Considere id = cdAit, idMovimento = cdMovimento"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Movimento encontrado", response = AitDTO.class),
		@ApiResponse(code = 204, message = "Não existe Movimento com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit, 
			  @ApiParam(value = "id do Movimento") @PathParam("idMovimento") int cdMovimento) {
		try {	
			AitMovimento movimento = AitMovimentoDAO.get(cdMovimento, cdAit);
			
			if(movimento==null)
				return ResponseFactory.noContent("Não existe movimento com o id indicado.");
			
			return ResponseFactory.ok(movimento);
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}

	@POST
	@Path("/{id}/movimentos/penalidade")
	@ApiOperation(
			value = "Registra um novo movimento de AIT para penalidade"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Movimento de penalidade registrado"),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response imporPenalidade(
			@ApiParam(value = "id do AIT") @PathParam("id") int cdAit,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario
		) {
		try {
			if(cdAit == 0)
				return ResponseFactory.badRequest("ID do ait não passado");
			
			AitMovimento aitMovimento = AitMovimentoServices.imporPenalidade(cdAit, cdUsuario);
			if(aitMovimento == null) {
				return ResponseFactory.badRequest("Movimento de penalidade não gerado");
			}
			
			return ResponseFactory.created(aitMovimento);
		} catch (ValidacaoException ve) {
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/movimentos/remessa/{nrRemessa}")
	@ApiOperation(
			value = "Retorna movimentos pelo número da remessa"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Remessa encontrada"),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getAllByRemessa(
			@ApiParam(value = "Número da Remessa") @PathParam("nrRemessa") int nrRemessa
		) {
		try {
			if(nrRemessa == 0)
				return ResponseFactory.badRequest("Número da remessa inválido");
			
			ResultSetMap movimentos = AitMovimentoServices.getAllByRemessa(nrRemessa);
			if(movimentos == null) {
				return ResponseFactory.badRequest("Remessa não encontrada");
			}
			
			return ResponseFactory.ok(movimentos);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/movimentos/dashboard")
	@ApiOperation(
			value = "Retorna quantidade de NAIs, NIPs e Registros de Infração irão vencer na data atual"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Movimentos Encontrados"),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getContagemMovimentosDashboard() {
		try {
			
			MovimentosDashboard contagemMovimentos = AitMovimentoServices.getContagemMovimentosDashboard();
			if(contagemMovimentos == null) 
				return ResponseFactory.badRequest("Movimentos não encontrados");
			
			
			return ResponseFactory.ok(contagemMovimentos);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/movimentos/correcoes/dashboard")
	@ApiOperation(
			value = "Retorna quantidade de NAIs, NIPs e Registros de Infração aguardando correção"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Movimentos Encontrados"),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getContagemPendentesCorrecaoDashboard() {
		try {
			
			MovimentosDashboard contagemMovimentos = AitMovimentoServices.getMovimentosPendentesCorrecao();
			if(contagemMovimentos == null) {
				return ResponseFactory.badRequest("Movimentos não encontrados");
			}
			
			return ResponseFactory.ok(contagemMovimentos);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@POST
	@Path("/movimentos/nip/cancelamento")
	@ApiOperation(value = "Cancela o movimento de NIP de um AIT")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "NIP cancelada", response = AitMovimento.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public Response cancelarNip(@ApiParam(value = "Documento a ser registrado", required = true) AitMovimento nip) {
		try {
			
			if(nip == null || nip.getCdAit() <= 0)
				return ResponseFactory.badRequest("Movimento inválido");
						
			Result res = AitMovimentoServices.cancelarNip(nip);		

			return ResponseFactory.ok(res.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/movimentos/notificacao/publicacao")
	@ApiOperation(value = "Notificação de Publicação de Notificação (NAI/NIP) à PRODEMGE")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "NIP cancelada", response = AitMovimento.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public Response publicarNotificacao(@ApiParam( value = "Notificações a serem publicadas" ) List<AitMovimento> notificacoes) {
		try {
			if(notificacoes == null)
				return ResponseFactory.badRequest("Nenhum movimento a ser publicado");
			
			return ResponseFactory.ok(AitMovimentoServices.publicarNotificacoes(notificacoes));
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return ResponseFactory.badRequest(ex.getMessage());
		}
	}
	
}
