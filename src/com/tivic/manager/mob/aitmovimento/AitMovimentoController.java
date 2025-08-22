package com.tivic.manager.mob.aitmovimento;

import java.sql.Types;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ait.AitService;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.ait.cancelamento.AitArquivoDTO;
import com.tivic.manager.mob.aitmovimento.calculomediamovimento.CalculoMediaDTO;
import com.tivic.manager.mob.aitmovimento.calculomediamovimento.ICalculoMediaMovimentoService;
import com.tivic.manager.mob.ait.aitArquivo.AitArquivo;
import com.tivic.manager.mob.ait.aitArquivo.TipoArquivoDTO;
import com.tivic.manager.mob.aitmovimento.cancelamentomovimentos.ArquivoAitDTO;
import com.tivic.manager.mob.aitmovimento.cancelamentomovimentos.ICancelaRevertePenalidade;
import com.tivic.manager.ptc.protocolosv3.protocoloarquivos.ArquivoDownload;
import com.tivic.manager.util.StringUtil;
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

@Api(value = "AIT", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/aits")
@Produces(MediaType.APPLICATION_JSON)
public class AitMovimentoController {
	IAitMovimentoService aitMovimentoService;
	private ManagerLog managerLog;
	private IAitService aitService;
	private ICancelaRevertePenalidade cancelaRevertePenalidade;
	private ICalculoMediaMovimentoService calculoMediaMovimentoService;
	
	public AitMovimentoController() throws Exception{
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.aitService = (IAitService) BeansFactory.get(IAitService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.cancelaRevertePenalidade = (ICancelaRevertePenalidade) BeansFactory.get(ICancelaRevertePenalidade.class);
		this.calculoMediaMovimentoService = (ICalculoMediaMovimentoService) BeansFactory.get(ICalculoMediaMovimentoService.class);
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
	public Response getAllMovimentosPendentes(
		@ApiParam(value = "ID do Ait") @QueryParam("idAit") String idAit,
		@ApiParam(value = "Status do Movimento") @QueryParam("tpStatus") Integer tpStatus,
		@ApiParam(value = "Movimentos pendentes de correção para envio") @QueryParam("lgCorrecao") Boolean lgCorrecao,
		@ApiParam(value = "Movimentos que não foram enviados para o detran (true/false)") @DefaultValue("false") @QueryParam("lgNaoEnviado") Boolean lgNaoEnviado,
		@ApiParam(value = "Data de movimento") @QueryParam("dtMovimento") String dtMovimento,
		@ApiParam(value = "Número de erro") @QueryParam("nrErro") String nrErro,
		@QueryParam("limit") int nrLimite) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriterios("B.id_ait","%"  + idAit + "%", ItemComparator.LIKE, Types.VARCHAR, idAit != null);
			searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus, tpStatus != null);
			searchCriterios.addCriterios("A.nr_erro", null, ItemComparator.NOTISNULL, Types.CHAR, lgCorrecao != null);
			searchCriterios.addCriteriosEqualString("A.nr_erro", StringUtil.retirarZerosAEsquerda(nrErro), nrErro != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_movimento", dtMovimento, dtMovimento != null);
			searchCriterios.addCriteriosMinorDate("A.dt_movimento", dtMovimento, dtMovimento != null);
			searchCriterios.setQtLimite(nrLimite);
			
			return ResponseFactory.ok(aitMovimentoService.findRemessa(searchCriterios, lgNaoEnviado));
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/movimento/{cdAit}/{tpStatus}")
	@ApiOperation(
		value = "Fornece um movimento especifico."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Movimentos pendentes encontrados"),
		@ApiResponse(code = 204, message = "Nenhum movimento pendente"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public Response find(
		@ApiParam(value = "Código do Ait") @PathParam("cdAit") int cdAit,
		@ApiParam(value = "Status do Movimento") @PathParam("tpStatus") int tpStatus) {
		try {			
			return ResponseFactory.ok(aitMovimentoService.getStatusMovimento(cdAit, tpStatus));
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cancelar/nai")
	@ApiOperation(
		value = "Cancela uma nai dado o cd indicado"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = AitMovimento.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	public Response cancelarNai(@ApiParam(value = "AitMovimento") AitMovimento aitMovimento) {
		try {
			this.aitMovimentoService.cancelarAutuacaoAit(aitMovimento);
			return ResponseFactory.ok(aitMovimento);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cancelar")
	@ApiOperation(
		value = "Cancela movimentos de ait"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = AitService.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	public Response cancelarAit(@ApiParam(value = "AitMovimento") AitMovimento aitMovimento) {
		try {
			this.aitService.cancelarAit(aitMovimento);
			return ResponseFactory.ok(aitMovimento);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/anexo")
	@ApiOperation(value = "Anexo de arquivo.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Arquivo inserido", response = ArquivoAitDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do arquivo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response update(@ApiParam(value = "ArquivoDTO")ArquivoAitDTO arquivoAitDTO) {
		try {
			arquivoAitDTO = this.aitMovimentoService.insertArquivo(arquivoAitDTO);
			return ResponseFactory.ok(arquivoAitDTO);
		} catch (ValidacaoException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}	
	
	@GET
	@Path("/arquivos")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Arquivos buscados com sucesso", response = AitArquivo[].class),
		@ApiResponse(code = 204, message = "Nenhum arquivo encontrado", response = TipoArquivoDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response find(
		@ApiParam(value = "Código do Documento") @QueryParam("cdAit") int cdAit
	) {
		try {
			List<TipoArquivoDTO> arquivoList = this.aitMovimentoService.buscarArquivos(cdAit);
			return ResponseFactory.ok(arquivoList);
		} catch (ValidacaoException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/download/{cdArquivo}")
	@ApiOperation(value = "Download de arquivo de documento de AIT cancelado.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Download encontrado com sucesso", response = ArquivoDownload.class),
		@ApiResponse(code = 204, message = "Nenhum download encontrado", response = ArquivoDownload.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response download(
		@ApiParam(value = "Código do Arquivo") @PathParam("cdArquivo") int cdArquivo
	) {
		try {
			ArquivoDownload arquivo = this.aitMovimentoService.download(cdArquivo);
			return ResponseFactory.ok(arquivo);	
		} catch (ValidacaoException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@DELETE
	@Path("/delete/{cdArquivo}")
	@ApiOperation(value = "Exclusão de arquivo de documento de AIT cancelado.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Arquivo excluído", response = Arquivo.class),
            @ApiResponse(code = 400, message = "Erro co excluir arquivo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response delete(
			@ApiParam(value = "Código do arquivo") @PathParam("cdArquivo") int cdArquivo
		) {
		try {
			this.aitMovimentoService.delete(cdArquivo);
			return ResponseFactory.noContent();
		} catch (ValidacaoException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cancelar/registroinfracao")
	@ApiOperation(
		value = "Cancela um registro de infração dado o id indicado"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = AitMovimento.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	public Response cancelarRegistroInfracao(@ApiParam(value = "AitMovimento") AitMovimento aitMovimento) {
		try {
			this.aitMovimentoService.cancelarRegistroAit(aitMovimento);
			return ResponseFactory.ok(aitMovimento);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cancelar/nip")
	@ApiOperation(
		value = "Cancela uma nip dado o cd indicado"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = AitMovimento.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	public Response cancelarNip(@ApiParam(value = "AitMovimento") AitMovimento aitMovimento,
			@ApiParam(value = "Usuário") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			this.aitMovimentoService.cancelarNip(aitMovimento, cdUsuario);
			return ResponseFactory.ok(aitMovimento);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/reversao/nip")
	@ApiOperation(
		value = "Cancela a NP de Advertência e converte em NP"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = AitMovimento.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	public Response reverterEmNip(@ApiParam(value = "AitMovimento") AitMovimento aitMovimento,
			@ApiParam(value = "Usuário") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			this.cancelaRevertePenalidade.cancelarEReverterPenalidade(aitMovimento, cdUsuario);
			return ResponseFactory.ok(aitMovimento);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/recalculo-juros/jari/{cdAit}")
	@ApiOperation(
		value = "Verifica se tem o movimento de recurso JARI e se é tesmpestiva"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Movimento JARI encontrado"),
		@ApiResponse(code = 204, message = "Movimento JARI não encontrado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public Response isRecalculoJurosBaseOnJari(
			@ApiParam(value = "Código do Ait") @PathParam("cdAit") int cdAit) {
		try {			
			JSONObject response = new JSONObject();
			response.put("response", aitMovimentoService.isRecalculoJurosBaseOnJari(cdAit));
			return ResponseFactory.ok(response);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("mediajulgamento")
	@ApiOperation(
		value = "Busca informações da media de julgamento"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Movimento JARI encontrado"),
		@ApiResponse(code = 204, message = "Movimento JARI não encontrado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public Response mediaJulgamento() {
		try {
			CalculoMediaDTO calculoMediaDTO = this.calculoMediaMovimentoService.mediaJulgamento();
			return ResponseFactory.ok(calculoMediaDTO);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("mediaespera")
	@ApiOperation(
		value = "Busca informações da media de julgamento"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Movimento JARI encontrado"),
		@ApiResponse(code = 204, message = "Movimento JARI não encontrado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public Response mediaEspera() {
		try {
			CalculoMediaDTO calculoMediaDTO = this.calculoMediaMovimentoService.mediaEspera();
			return ResponseFactory.ok(calculoMediaDTO);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("taloesdisponiveis")
	@ApiOperation(
		value = "Busca informações da media de julgamento"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Movimento JARI encontrado"),
		@ApiResponse(code = 204, message = "Movimento JARI não encontrado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public Response qtTaloesDisponiveis() {
		try {
			CalculoMediaDTO calculoMediaDTO = this.calculoMediaMovimentoService.qtTaloesDisponiveis();
			return ResponseFactory.ok(calculoMediaDTO);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
