package com.tivic.manager.ptc.protocolosv3.protocoloarquivos;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.exception.ValidationException;
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

@Api(value = "Protocolo", tags = { "ptc" })
@Path("/v3/ptc/protocolos/arquivos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProtocoloArquivosController {

	private IProtocoloArquivoService arquivoService;
	private ManagerLog managerLog;
	
	public ProtocoloArquivosController() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.arquivoService = (IProtocoloArquivoService) BeansFactory.get(IProtocoloArquivoService.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Arquivos buscados com sucesso", response = ArquivoSimples[].class),
		@ApiResponse(code = 204, message = "Nenhum arquivo encontrado", response = ArquivoSimples[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response find(
		@ApiParam(value = "Código do Documento") @QueryParam("cdDocumento") Integer cdDocumento
	) {
		try {
			if(cdDocumento == null)
				throw new ValidationException("Número de documento inválido.");
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("C.cd_documento", cdDocumento, cdDocumento > 0);
			List<ArquivoSimples> arquivos = arquivoService.getArquivos(searchCriterios);
			return ResponseFactory.ok(arquivos);
			
		} catch (ValidacaoException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/download")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Download encontrado com sucesso", response = ArquivoDownload.class),
		@ApiResponse(code = 204, message = "Nenhum download encontrado", response = ArquivoDownload.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response download(
		@ApiParam(value = "Código do Arquivo") @QueryParam("cdArquivo") Integer cdArquivo
	) {
		try {
			if(cdArquivo == null)
				throw new ValidationException("Código do arquivo inválido.");
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("cd_arquivo", cdArquivo, cdArquivo > 0);
			ArquivoDownload arquivo = arquivoService.download(searchCriterios);
			return ResponseFactory.ok(arquivo);
			
		} catch (ValidacaoException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/download/arquivo-credencial")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Arquivo encontrado com sucesso", response = ArquivoDownload.class),
		@ApiResponse(code = 204, message = "Nenhum Arquivo encontrado", response = ArquivoDownload.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response downloadArquivoCredencialSolicitacao(
		@ApiParam(value = "Código do Arquivo") @QueryParam("cdArquivo") Integer cdArquivo
	) {
		try {
			if(cdArquivo == null)
				throw new ValidationException("Código do arquivo inválido.");
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("cd_arquivo", cdArquivo, cdArquivo > 0);
			ArquivoDownload arquivo = arquivoService.downloadArquivoCredencial(searchCriterios);
			return ResponseFactory.ok(arquivo);
			
		}  catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Anexo de arquivo.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Arquivo Inserido", response = ArquivoDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do Arquivo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response create(ArquivoDTO arquivo) {
		try {
			arquivo = arquivoService.insert(arquivo);
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
	@Path("")
	@ApiOperation(value = "Deleção de arquivo.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Arquivo Deletado", response = ArquivoDTO.class),
            @ApiResponse(code = 400, message = "Erro na deleção do Arquivo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response delete(
			@ApiParam(value = "Código do Arquivo") @QueryParam("cdArquivo") Integer cdArquivo,
			@ApiParam(value = "Código do Documento") @QueryParam("cdDocumento") Integer cdDocumento
		) {
		try {
			arquivoService.delete(cdArquivo, cdDocumento);
			return ResponseFactory.ok("Arquivo de código: "+ cdArquivo + " deletado com sucesso");
		} catch (ValidacaoException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
