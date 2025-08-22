package com.tivic.manager.mob.publicacao.edital;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.publicacao.build.EditalSearchBuilder;
import com.tivic.manager.mob.publicacao.dto.ArquivoEditalDTO;
import com.tivic.manager.mob.publicacao.dto.ArquivoEditalPortalDTO;
import com.tivic.manager.ptc.protocolosv3.protocoloarquivos.ArquivoDownload;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
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

@Api(value = "EditalPublicacao", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/publicacao/edital")
@Produces(MediaType.APPLICATION_JSON)
public class EditalPublicacaoController {
	
	private ManagerLog managerLog;
	private IEditalPublicacaoService editalPublicacaoService;
	
	public EditalPublicacaoController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.editalPublicacaoService = (IEditalPublicacaoService) BeansFactory.get(IEditalPublicacaoService.class);
	}
	
	@POST
	@Path("/upload")
	@ApiOperation(value = "Upload de arquivo de edital de publicação de julgamentos.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Arquivo inserido", response = ArquivoEditalDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do Arquivo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response create(ArquivoEditalDTO arquivoEditalDTO) {
		try {
			arquivoEditalDTO = this.editalPublicacaoService.insert(arquivoEditalDTO);
			return ResponseFactory.ok(arquivoEditalDTO);
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
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
			ArquivoDownload arquivo = this.editalPublicacaoService.download(cdArquivo);
			return ResponseFactory.ok(arquivo);	
		} catch (ValidacaoException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/listagem")
	@ApiOperation(value = "Buscar AITs", notes = "Endpoint para buscar arquivos de edital de publicação com base nos parâmetros fornecidos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivos encontrados", response = ArquivoEditalPortalDTO.class),
			@ApiResponse(code = 204, message = "Nenhum arquivo encontrado"),
			@ApiResponse(code = 400, message = "Parâmetros inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response find(
			@ApiParam(name = "tpDocumento", value = "Tipo de documento") @QueryParam("tpDocumento") int tpDocumento,
			@ApiParam(name = "dtInicialPublicacao", value = "Data inicial de publicação") @QueryParam("dtInicialPublicacao") String dtInicialPublicacao,
			@ApiParam(name = "dtFinalPublicacao", value = "Data final de publicação") @QueryParam("dtFinalPublicacao") String dtFinalPublicacao,
			@ApiParam(value = "Limite") @QueryParam("limit") int limit,
			@ApiParam(value = "Páginas") @QueryParam("page") int page) {
		try {
			SearchCriterios searchCriterios = new EditalSearchBuilder()
					.setTpDocumento(tpDocumento)
					.setDtInicialPublicacao(dtInicialPublicacao)
					.setDtFinalPublicacao(dtFinalPublicacao)
					.setDeslocamento(limit, page)
					.setLimit(limit)
					.build();
			managerLog.showLog(new InfoLogBuilder("[GET] /edital", "Requisição de busca de Editais de publicação...").build());
			PagedResponse<ArquivoEditalPortalDTO> arquivosEdital= this.editalPublicacaoService.find(searchCriterios);
			managerLog.showLog(new InfoLogBuilder("[GET] /edital", "A busca de Editais de publicação foi realizada com sucesso.").build());
			return ResponseFactory.ok(arquivosEdital);
		} catch (NoContentException e) {
			managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
