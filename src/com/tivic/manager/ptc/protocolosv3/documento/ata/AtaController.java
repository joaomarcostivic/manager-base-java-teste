package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.sql.Types;
import java.util.List;

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

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;

@Api(value = "Ata", tags = {"ptc"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/ptc/ata")
@Produces(MediaType.APPLICATION_JSON)
public class AtaController {
	
	private IAtaService ataService;
	private ManagerLog managerLog;
	
	public AtaController() throws Exception {
		ataService = (IAtaService) BeansFactory.get(IAtaService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@GET
	@Path("")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Atas buscadas com sucesso", response = Ata[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = Ata[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response find(
		@ApiParam(value = "Número da Ata") @QueryParam("idAta") String idAta,
		@ApiParam(value = "Data de cadastro do auto") @QueryParam("dtCadastro") String dtCadastro,
		@ApiParam(value = "Código do usuário") @QueryParam("cdUsuario") int cdUsuario,
		@ApiParam(value = "Data de cadastro da ata") @QueryParam("dtAta") String dtAta,
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite
	) {
		try {		
			SearchCriterios searchCriterios = new SearchCriterios();
			
			searchCriterios.addCriteriosEqualString("id_ata", idAta, idAta != null);
			searchCriterios.addCriteriosGreaterDate("dt_cadastro", dtCadastro, dtCadastro != null);
			searchCriterios.addCriteriosMinorDate("dt_cadastro", dtCadastro, dtCadastro != null);
			searchCriterios.addCriteriosEqualInteger("cd_usuario", cdUsuario, cdUsuario > 0);
			searchCriterios.addCriteriosGreaterDate("dt_ata", dtAta, dtAta != null);
			searchCriterios.addCriteriosMinorDate("dt_ata", dtAta, dtAta != null);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			Search<Ata> search = ataService.find(searchCriterios);
			return ResponseFactory.ok(new AtaSearchPaginatorBuilder(search.getList(Ata.class), search.getList(Ata.class).size()).build());
		}
		catch(Exception e) {
			managerLog.error("Erro ao buscar dados da Ata:", e.getMessage());
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/atadto")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AtaDTO encontrada com sucesso", response = AtaDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = AtaDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response findAtaDTO(
		@ApiParam(value = "Número da Ata") @QueryParam("idAta") String idAta,
		@ApiParam(value = "Identificador do AIT") @QueryParam("idAit") String idAit,
		@ApiParam(value = "Número do Documento") @QueryParam("nrDocumento") String nrDocumento,
		@ApiParam(value = "Período da data inicial de cadastro da ata") @QueryParam("dtAtaInicial") String dtAtaInicial,
		@ApiParam(value = "Período da data final de cadastro da ata") @QueryParam("dtAtaFinal") String dtAtaFinal,
		@ApiParam(value = "Data de cadastro do auto") @QueryParam("dtCadastro") String dtCadastro,
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite
	) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("A.id_ata", idAta, idAta != null);
			searchCriterios.addCriterios("E.id_ait","%"  + idAit + "%", ItemComparator.LIKE, Types.VARCHAR, idAit != null);
			searchCriterios.addCriteriosEqualString("F.nr_documento", nrDocumento, nrDocumento != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_ata", dtAtaInicial, dtAtaInicial != null);
			searchCriterios.addCriteriosMinorDate("A.dt_ata", dtAtaFinal, dtAtaFinal != null);
			searchCriterios.addCriteriosMinorDate("A.dt_cadastro", dtCadastro, dtCadastro != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_cadastro", dtCadastro, dtCadastro != null);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			Search<AtaDTO> search = ataService.findAtaDTO(searchCriterios);
			return ResponseFactory.ok(new AtaDTOSearchPaginatorBuilder(search.getList(AtaDTO.class), search.getList(AtaDTO.class).size()).build());
		}
		catch(Exception e) {
			managerLog.error("Erro ao buscar dados da Ata:", e.getMessage());
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@GET
	@Path("/atadto-ocorrencia")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AtaDTO encontrada com sucesso", response = AtaDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = AtaDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response findAtaDTO(
		@ApiParam(value = "Número da Ata") @QueryParam("cdDocumento") int cdDocumento,
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite
	) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.cd_documento", cdDocumento, true);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			Search<AtaDTO> search = ataService.findOcorrenciaDTO(searchCriterios);
			return ResponseFactory.ok(new AtaDTOSearchPaginatorBuilder(search.getList(AtaDTO.class), search.getList(AtaDTO.class).size()).build());
		}
		catch(Exception e) {
			managerLog.error("Erro ao buscar dados da Ata:", e.getMessage());
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@POST
	@Path("/")
	@ApiOperation(
			value = "Registra uma lista de Ata Jari."
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ata Jari Registrada", response = AtaDTO[].class),
			@ApiResponse(code = 400, message = "Ata Jari Inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public Response create(
			@ApiParam(value = "Ata a ser criada", required = true) List<AtaDTO> listAtaDto) {
		try {
			return ResponseFactory.ok(ataService.insertList(listAtaDto));
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.error("Erro ao buscar dados da AtaDTO:", e.getMessage());
			return ResponseFactory.internalServerError("Erro na criação da Ata: ", e.getMessage());
		}
	}
	
	@POST
	@Path("/individual")
	@ApiOperation(
			value = "Registra uma nova Ata Jari."
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ata Jari Registrada", response = AtaDTO.class),
			@ApiResponse(code = 400, message = "Ata Jari Inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public Response createIndividual(
			@ApiParam(value = "Ata a ser criada", required = true) AtaDTO ataDto) {
		try {
			return ResponseFactory.ok(ataService.insert(ataDto));
		} catch (NoContentException e) {
			managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/adicionar-auto/ata")
	@ApiOperation(value = "Adicionando Auto na Ata.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Auto adicionado na Ata", response = AtaDTO.class),
            @ApiResponse(code = 400, message = "Erro na atualização da Ata", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response updateListAta( @ApiParam(value = "Auto a ser adicionada", required = true) AtaDTO ataDto) {
		try {
			return ResponseFactory.ok(ataService.updateListAta(ataDto));
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.error("Erro ao atualizar os dados da Ata:", e.getMessage());
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro ao atualizar a Ata: ", e.getMessage());
		}
	}
	
	@GET
	@Path("/jari")
	@ApiOperation(
			value = "Retorna uma AtaDTO"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Dados do AIT encontrados", response = AtaDTO.class),
			@ApiResponse(code = 204, message = "Dados do AIT não encontrados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response getAtaDTOJari(
			@ApiParam(value = "Identificador do AIT") @QueryParam("idAit") String idAit) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("A.id_ait", idAit, idAit != null);
			searchCriterios.addCriteriosEqualInteger("G.cd_tipo_documento",  ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_JARI", 0));
			AtaDTO ataDto = ataService.getAtaDTOJari(searchCriterios);
			return ResponseFactory.ok(ataDto);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch(Exception e) {
			managerLog.error("Erro ao buscar dados da AtaDTO:", e.getMessage());
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@GET
	@Path("/relatorio/boletim/{cdAta}")
	@ApiOperation(value = "Busca Boletim de Documentos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Relatório gerado", response = byte[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Produces("application/pdf")
	public Response getBoletimReport(
			@ApiParam(value = "Tipo de Documento") @PathParam("cdAta") Integer cdAta) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.cd_ata", cdAta, cdAta != null);
			byte[] report = this.ataService.getBoletimReport(searchCriterios);
			
			if(report == null)
				return ResponseFactory.badRequest("Critérios fornecidos não produziram nenhum resultado.");
			
			return ResponseFactory.ok(report);

		} catch(Exception e) {
			managerLog.error("Erro ao gerar o Boletim:", e.getMessage());
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
	
	@GET
	@Path("/relatorio/ata/{cdAta}")
	@ApiOperation(value = "Busca Relatório de Documentos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Relatório gerado", response = byte[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Produces("application/pdf")
	public Response getAtaReport(
			@ApiParam(value = "Tipo de Documento") @PathParam("cdAta") Integer cdAta) {
		try {
			byte[] report = this.ataService.printAta(cdAta);
			
			if(report == null)
				return ResponseFactory.badRequest("Critérios fornecidos não produziram nenhum resultado.");
			
			return ResponseFactory.ok(report);

		} catch(Exception e) {
			managerLog.error("Erro ao gerar o relatório da Ata:", e.getMessage());
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
	
	@GET
	@Path("/documentos/ata/{cdAta}")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Documentos buscadas com sucesso", response = AtaDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response findDocumentosAta(
		@ApiParam(value = "Código da Ata") @PathParam("cdAta") Integer cdAta) throws Exception {
		try {		
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.cd_ata", cdAta, cdAta != null);
			Search<AtaDTO> search = ataService.findDocumentosAta(searchCriterios);
			return ResponseFactory.ok(search.getList(AtaDTO.class));
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.error("Erro ao buscar dados da Ata:", e.getMessage());
			return ResponseFactory.internalServerError("Erro ao atualizar a Ata: ", e.getMessage());
		}
	}
	
	@PUT
	@Path("/aits")
	@ApiOperation(value = "Adicionando Auto na Ata.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Auto adicionado na Ata", response = AtaDTO[].class),
            @ApiResponse(code = 400, message = "Erro na atualização da Ata", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response updateListAta( @ApiParam(value = "Ata a ser atualizada", required = true) List<AtaDTO> listAtaDto) {
		try {
			return ResponseFactory.ok(ataService.updateAtaJari(listAtaDto));
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.error("Erro ao atualizar os dados da Ata:", e.getMessage());
			return ResponseFactory.internalServerError("Erro ao atualizar a Ata: ", e.getMessage());
		}
	}
	
	@GET
	@Path("/registro")
	@ApiOperation(
			value = "Retorna uma Ata"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ata encontrada", response = Ata.class),
			@ApiResponse(code = 204, message = "Dados da Ata não encontrados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response getAtaById(
			@ApiParam(value = "Identificador da Ata") @QueryParam("idAta") String idAta) {
		try {
			Ata ata = ataService.getAtaById(idAta);
			return ResponseFactory.ok(ata);
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} 
		catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
