package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
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

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.validation.Validators;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "AIT", tags = { "mob" })
@Path("/v2/mob/aits")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AitMovimentoDocumentoController {
	
	private final AitMovimentoDocumentoServices aitMovimentoDocumentoService;
	
	public AitMovimentoDocumentoController() throws Exception {
		this.aitMovimentoDocumentoService = new AitMovimentoDocumentoServices();
	}
		
	@POST
	@Path("/{id}/documentos")
	@ApiOperation(value = "Registra um novo documento de AIT")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Documento registrado", response = AitMovimentoDocumentoDTO.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public Response create(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit,
			@ApiParam(value = "Documento a ser registrado", required = true) AitMovimentoDocumentoDTO dto) {
		try {
			
			if(dto.getMovimento() == null) 
				dto.setMovimento(new AitMovimento());
			
			dto.getMovimento().setCdAit(cdAit);
			
			Validators<AitMovimentoDocumentoDTO> validators = this.getInsertValidators(dto);
						
			AitMovimentoDocumentoDTO _dto = this.aitMovimentoDocumentoService.insert(dto, cdAit, validators);			

			return ResponseFactory.created(_dto);
		} catch (ValidationException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/documentos/externo")
	@ApiOperation(value = "Registra um novo protocolo de Entidade Externa")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Documento registrado", response = AitMovimentoDocumentoDTO.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public Response createExterno(@ApiParam(value = "Documento a ser registrado", required = true) AitMovimentoDocumentoDTO dto) {
		try {			
			if(dto.getMovimento() == null) 
				dto.setMovimento(new AitMovimento());
			
			AitMovimentoDocumentoDTO _dto = this.aitMovimentoDocumentoService.insertExterno(dto);
			byte[] report = this.aitMovimentoDocumentoService.getProtocoloExterno(_dto.getDocumento().getCdDocumento());

			return ResponseFactory.ok(report);
		} catch (ValidationException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/documentos/externo/impressao")
	@ApiOperation(value = "Imprime um novo protocolo de Entidade Externa")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Documento impresso", response = AitMovimentoDocumentoDTO.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public Response imprimirExterno(@ApiParam(value = "Documento a ser registrado", required = true) AitMovimentoDocumentoDTO dto) {
		try {			
			if(dto.getMovimento() == null) 
				dto.setMovimento(new AitMovimento());
						
			AitMovimentoDocumentoDTO _dto = this.aitMovimentoDocumentoService.insertExterno(dto);
			byte[] report = this.aitMovimentoDocumentoService.getProtocoloExterno(dto.getDocumento().getCdDocumento());

			return ResponseFactory.ok(report);
		} catch (ValidationException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/{id}/documentos/detran")
	@ApiOperation(value = "Envia protocolo ao DETRAN (MG)")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Documento enviado", response = AitMovimentoDocumentoDTO.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public Response sendDetran(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit,
			@ApiParam(value = "Documento a ser registrado", required = true) Documento documento) {
		try {
			DadosRetornoMG retorno = this.aitMovimentoDocumentoService.sendDetran(documento, cdAit, null);
			
			if(retorno.getCodigoRetorno() != 0)
				return ResponseFactory.badRequest(retorno.getMensagemRetorno());

			return ResponseFactory.ok(retorno.getMensagemRetorno());

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	private Validators<AitMovimentoDocumentoDTO> getInsertValidators(AitMovimentoDocumentoDTO dto) {
		
		if(dto.getDocumento().getCdTipoDocumento() == ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_APRESENTACAO_CONDUTOR", 0)) {
			return new Validators<AitMovimentoDocumentoDTO>(dto)
					.put(new AitMovimentoDocumentoInsertValidator())
					.put(new AitMovimentoDocumentoExistsValidator())
					.put(new AitMovimentoDocumentoApresValidator());
		}
		
		if(dto.getDocumento().getCdTipoDocumento() == ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_DEFESA_PREVIA", 0)) {
			return new Validators<AitMovimentoDocumentoDTO>(dto)
					.put(new AitMovimentoDocumentoInsertValidator())
					.put(new AitMovimentoDocumentoExistsValidator())
					.put(new AitMovimentoDocumentoDefPreviaValidator());
		}
		
		if(dto.getDocumento().getCdTipoDocumento() == ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_JARI", 0)) {
			return new Validators<AitMovimentoDocumentoDTO>(dto)
					.put(new AitMovimentoDocumentoInsertValidator())
					.put(new AitMovimentoDocumentoExistsValidator())
					.put(new AitMovimentoDocumentoJariValidator())
					.put(new AitMovimentoDocumentoAtaValidator());
		}
		
		if(dto.getDocumento().getCdTipoDocumento() == ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_CETRAN", 0)) {
			return new Validators<AitMovimentoDocumentoDTO>(dto)
					.put(new AitMovimentoDocumentoInsertValidator())
					.put(new AitMovimentoDocumentoExistsValidator())
					.put(new AitMovimentoDocumentoCetranValidator());
		}
		
		if(dto.getDocumento().getCdTipoDocumento() == ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_ADVERTENCIA_DEFESA", 0)) {
			return new Validators<AitMovimentoDocumentoDTO>(dto)
					.put(new AitMovimentoDocumentoInsertValidator())
					.put(new AitMovimentoDocumentoExistsValidator())
					.put(new AitMovimentoDocumentoAdvDefesaValidator());
		}
		
		return new Validators<AitMovimentoDocumentoDTO>(dto).put(new AitMovimentoDocumentoInsertValidator())
															.put(new AitMovimentoDocumentoExistsValidator());
		
	}
	
	@GET
	@Path("/{id}/documentos")
	@ApiOperation(value = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Documento registrado", response = AitMovimentoDocumentoDTO[].class),
			@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = AitMovimentoDocumentoDTO[].class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public Response getAll(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) {
		try {		
			
			List<AitMovimentoDocumentoDTO> list = this.aitMovimentoDocumentoService
					.find(new Criterios("B.cd_ait", Integer.toString(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		
			if(list.isEmpty())
				return ResponseFactory.noContent(list);

			return ResponseFactory.ok(list);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/documentos/{idDocumento}/impressao")
	@ApiOperation( value = "Impressão de protocolo lançado")
	@ApiResponses( value = {
			@ApiResponse(code = 200, message = "Documento impresso", response = AitMovimentoDocumentoDTO.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response print(@ApiParam( value = "Id do AIT") @PathParam( "id" ) int cdAit, @ApiParam( value = "Id do Documento" ) @PathParam( "idDocumento" ) int cdDocumento) {			
		try {
			return ResponseFactory.ok(this.aitMovimentoDocumentoService.getProtocolo(cdAit, cdDocumento));
		}catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}		
	}
	
	@GET
	@Path("/documentos/{idDocumento}")
	@ApiOperation( value = "Impressão de protocolo lançado")
	@ApiResponses( value = {
			@ApiResponse(code = 200, message = "Documento impresso", response = AitMovimentoDocumentoDTO.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDocumentoDTO(@ApiParam( value = "Id do Documento" ) @PathParam( "idDocumento" ) int cdDocumento) {	
		
		try {
			return ResponseFactory.ok(this.aitMovimentoDocumentoService.getDocumentoDto(cdDocumento));
		}catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}
	
	@GET
	@Path("/documentos")
	@ApiOperation(value = "Busca de Documentos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Documento encontrado", response = Ait[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response find(
			@ApiParam(value = "Identificador do AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Código do AIT") @QueryParam("cdAit") int cdAit,
			@ApiParam(value = "Numero da placa") @QueryParam("placa") String nrPlaca,
			@ApiParam(value = "Número do Protocolo") @QueryParam("documento") String nrDocumento,
			@ApiParam(value = "Fase do Documento") @QueryParam("fase") int cdFase,
			@ApiParam(value = "Tipo do Documento") @QueryParam("tipo") int cdTipoDocumento,
			@ApiParam(value = "Id. do Responsável") @QueryParam("idPessoa") int cdPessoa,
			@ApiParam(value = "Situação do Documento") @QueryParam("cdSituacaoDocumento") int cdSituacaoDocumento){
		try {	
			
			Criterios crt = new Criterios();			
			
			if(idAit != null && !idAit.equals("")) {
				crt.add("D.id_ait", idAit, ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			if(cdAit > 0) {
				crt.add("D.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(nrPlaca != null && !nrPlaca.equals("")) {
				crt.add("D.nr_placa", nrPlaca, ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			if(nrDocumento != null && !nrDocumento.equals("")) {
				crt.add("A.nr_documento", nrDocumento, ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			if(cdFase > 0) {
				crt.add("A.cd_fase", String.valueOf(cdFase), ItemComparator.EQUAL, Types.VARCHAR);
			}

			if(cdTipoDocumento > 0) {
				crt.add("A.cd_tipo_documento", String.valueOf(cdTipoDocumento), ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			if(cdSituacaoDocumento > 0) {
				crt.add("A.cd_situacao_documento", String.valueOf(cdSituacaoDocumento), ItemComparator.EQUAL, Types.INTEGER);
			}

			List<AitMovimentoDocumentoDTO> list = this.aitMovimentoDocumentoService.find(crt);
			
			if(list.isEmpty()) {
				return ResponseFactory.noContent("Nenhum registro");
			}
			
			return ResponseFactory.ok(list);

		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
	
	@GET
	@Path("/documentos/report")
	@ApiOperation(value = "Busca Relatório de Documentos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Relatório gerado", response = byte[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Produces("application/pdf")
	public Response getReport(
			@ApiParam(value = "Identificador do AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Código do AIT") @QueryParam("cdAit") int cdAit,
			@ApiParam(value = "Numero da placa") @QueryParam("placa") String nrPlaca,
			@ApiParam(value = "Número do Protocolo") @QueryParam("documento") String nrDocumento,
			@ApiParam(value = "Fase do Documento") @QueryParam("fase") int cdFase,
			@ApiParam(value = "Tipo do Documento") @QueryParam("tipo") int cdTipoDocumento,
			@ApiParam(value = "Situação do Documento") @QueryParam("cdSituacaoDocumento") int cdSituacaoDocumento,
			@ApiParam(value = "Id. do Responsável") @QueryParam("idPessoa") int cdPessoa) {
		try {	
			
			Criterios crt = new Criterios();		
			if(idAit != null && !idAit.equals("")) {
				crt.add("D.id_ait", idAit, ItemComparator.EQUAL, Types.VARCHAR);
			}
			if(cdAit > 0) {
				crt.add("D.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER);
			}
			if(nrPlaca != null && !nrPlaca.equals("")) {
				crt.add("D.nr_placa", nrPlaca, ItemComparator.EQUAL, Types.VARCHAR);
			}
			if(nrDocumento != null && !nrDocumento.equals("")) {
				crt.add("A.nr_documento", nrDocumento, ItemComparator.EQUAL, Types.VARCHAR);
			}
			if(cdFase > 0) {
				crt.add("A.cd_fase", String.valueOf(cdFase), ItemComparator.EQUAL, Types.VARCHAR);
			}
			if(cdTipoDocumento > 0) {
				crt.add("A.cd_tipo_documento", String.valueOf(cdTipoDocumento), ItemComparator.EQUAL, Types.VARCHAR);
			}
			if(cdSituacaoDocumento > 0) {
				crt.add("A.cd_situacao_documento", String.valueOf(cdSituacaoDocumento), ItemComparator.EQUAL, Types.INTEGER);
			} else {
				crt.add("A.cd_situacao_documento", String.valueOf(DocumentoServices.ST_ARQUIVADO), ItemComparator.EQUAL, Types.INTEGER);
			}

			byte[] report = this.aitMovimentoDocumentoService.getReport(crt, null);
			
			if(report == null)
				return ResponseFactory.badRequest("Critérios fornecidos não produziram nenhum resultado.");
			
			return ResponseFactory.ok(report);

		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
	
	@PUT
	@Path("/documentos/{cdDocumento}/{cdTipoDocumento}/{cdFase}")
	@ApiOperation(value = "Alteração de Documentos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Documento encontrado", response = Ait[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response updateDocumento(@ApiParam(value = "Número do Documento") @PathParam("cdDocumento") int cdDocumento, 
			@ApiParam(value = "Tipo Documento") @PathParam("cdTipoDocumento") int cdTipoDocumento, 
			@ApiParam(value = "Fase") @PathParam("cdFase")  int cdFase,
			@ApiParam(value = "Documento DTO") AitMovimentoDocumentoDTO documento) {
		try {
			AitMovimentoDocumentoDTO _dto = this.aitMovimentoDocumentoService.getDocumentoDto(documento.getDocumento().getCdDocumento());

			_dto.setDocumento(documento.getDocumento());
			_dto.getDocumento().setDtProtocolo(documento.getDocumento().getDtProtocolo());
			_dto.setCamposFormulario(documento.getCamposFormulario());
			_dto.setDocumentoPessoa(documento.getDocumentoPessoa());
			_dto.setDocumentoSuperior(documento.getDocumentoSuperior());
			
			AitMovimentoDocumentoDTO _movDocumento = this.aitMovimentoDocumentoService.updateDocumento(cdDocumento, cdTipoDocumento, cdFase, _dto);
			return ResponseFactory.ok(_movDocumento);
		} catch(ValidationException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/documentos/{cdDocumento}")
	@ApiOperation(value = "Cancelamento de Documentos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Documento cancelado", response = Ait[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response cancelarDocumento(@ApiParam(value = "Número do Documento") @PathParam("cdDocumento") int cdDocumento,
			@ApiParam(value = "Documento DTO") AitMovimentoDocumentoDTO documento) {
		try {
			documento.getDocumento().setCdDocumento(cdDocumento);
			Result result = this.aitMovimentoDocumentoService.cancelDocumento(documento);
			return ResponseFactory.ok(result.getMessage());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@GET
	@Path("/movimentos/historico/{cdAit}")
	@ApiOperation(value = "Exibição de Histório de movimentos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = Ait[].class),
			@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getHistorico(@ApiParam(value = "Código do Ait") @PathParam("cdAit") int cdAit) {
		
		try {
			List<Documento> documentos = this.aitMovimentoDocumentoService.getHistoricoAit(cdAit);
			
			if(documentos != null)
				return ResponseFactory.ok(documentos);
			
			return ResponseFactory.badRequest("Não foi encontrado histórico de movimentações para esse AIT");
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processo de requisição: " + e.getMessage());
		}
 	}
	
	@GET
	@Path("/documentos/boletim/{nrAta}")
	@ApiOperation(value = "Exibição de Histório de movimentos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = Ait[].class),
			@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response consultaAta(@ApiParam(value = "Código do Ait") @PathParam("nrAta") String nrAta) {
		
		try {
			ResultSetMap _resultados = this.aitMovimentoDocumentoService.getResultadosAta(nrAta);
			
			if(_resultados != null)
				return ResponseFactory.ok(_resultados);
			
			return ResponseFactory.badRequest("Não foram encontrados resultados para esse número de ATA");
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processo de requisição: " + e.getMessage());
		}
 	}
	
	@GET
	@Path("/documentos/boletim/{nrAta}/print")
	@ApiOperation(value = "Busca Relatório de Documentos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Relatório gerado", response = byte[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Produces("application/pdf")
	public Response getReport(
			@ApiParam(value = "Identificador da ATA") @PathParam("nrAta") String nrAta) {
		try {

			byte[] report = this.aitMovimentoDocumentoService.getBoletimJari(nrAta, null);
			
			if(report == null)
				return ResponseFactory.badRequest("Critérios fornecidos não produziram nenhum resultado.");
			
			return ResponseFactory.ok(report);

		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
	
	@GET
	@Path("/documentos/ata/validate/{nrAta}")
	@ApiOperation(value = "Busca Relatório de Documentos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "ATA Encontrada", response = byte[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response validateAta(@ApiParam(value = "Identificador da ATA") @PathParam("nrAta") String nrAta) {
		String res = this.aitMovimentoDocumentoService.validateAta(nrAta);
		
		if(res != null && !res.equals("0"))
			return ResponseFactory.ok(this.aitMovimentoDocumentoService.validateAta(nrAta));
		
		return ResponseFactory.badRequest("Não foram encontrados resultados");
	}
	
	@GET
	@Path("/documentos/relator/{cdPessoa}")
	@ApiOperation(value = "Busca de Documentos por Relator")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Documentos encontrados", response = Ait[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response findByRelator(@ApiParam(value = "Id. do Responsável") @PathParam("cdPessoa") int cdPessoa) {
		try {
			List<AitMovimentoDocumentoDTO> list = this.aitMovimentoDocumentoService.findByRelator(cdPessoa);
			
			if(list.isEmpty()) {
				return ResponseFactory.noContent("Nenhum registro");
			}
			
			return ResponseFactory.ok(list);

		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
	
	@GET
	@Path("/documentos/relator/semrelator")
	@ApiOperation(value = "Busca de Documentos sem Relator")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Documentos encontrados", response = Ait[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response findSemRelator() {
		try {
			List<AitMovimentoDocumentoDTO> list = this.aitMovimentoDocumentoService.findSemRelator();
			
			if(list.isEmpty() && list != null) {
				return ResponseFactory.noContent("Nenhum registro");
			}
			
			return ResponseFactory.ok(list);

		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
	
	@GET
	@Path("/documentos/processos")
	@ApiOperation(value = "Exibição de Histório de movimentos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = Ait[].class),
			@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response consultaRelatorioProcessos(
			@ApiParam(value = "Tipo de Documento") @QueryParam("tipoDocumento") int cdTipoDocumento,
			@ApiParam(value = "Data Inicial") @QueryParam("dataInicial") String dtInicial,
			@ApiParam(value = "Data Final") @QueryParam("dataFinal") String dtFinal,
			@ApiParam(value = "Fase do Documento") @QueryParam("fase") int cdFase
			) {
		
		try {
			
			Criterios crt = new Criterios();		
			
			if(dtInicial != null) 
				crt.add("D.dt_protocolo", dtInicial, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);
			
			if(dtFinal != null) 
				crt.add("D.dt_protocolo", dtFinal, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			else if(dtInicial != null)
				crt.add("D.dt_protocolo", dtInicial, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			
			if(cdFase > 0) 
				crt.add("F.cd_fase", String.valueOf(cdFase), ItemComparator.EQUAL, Types.VARCHAR);
			
			if(cdTipoDocumento > 0) 
				crt.add("E.cd_tipo_documento", String.valueOf(cdTipoDocumento), ItemComparator.EQUAL, Types.VARCHAR);
			
			ArrayList<AitMovimentoDocumentoDTO> _dtos = this.aitMovimentoDocumentoService.findProcessos(crt, null);			
			
			if(_dtos.isEmpty())
				return ResponseFactory.badRequest("Critérios fornecidos não produziram nenhum resultado.");
			
			return ResponseFactory.ok(_dtos);

		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
 	}
	
	@POST
	@Path("/documentos/processos/print")
	@ApiOperation(value = "Busca Relatório de Documentos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Relatório gerado", response = byte[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Produces("application/pdf")
	public Response getRelatorioProcessos(
			@ApiParam(value = "Tipo de Documento") @QueryParam("tipoDocumento") int cdTipoDocumento,
			@ApiParam(value = "Data Inicial") @QueryParam("dataInicial") String dtInicial,
			@ApiParam(value = "Data Final") @QueryParam("dataFinal") String dtFinal,
			@ApiParam(value = "Fase do Documento") @QueryParam("fase") int cdFase,
			@ApiParam(value = "Data de Publicacao") GregorianCalendar dtPublicacao
			) {
		try {
			
			Criterios crt = new Criterios();		
			
			if(dtInicial != null) 
				crt.add("D.dt_protocolo", dtInicial, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);
			
			if(dtFinal != null) 
				crt.add("D.dt_protocolo", dtFinal, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			else if(dtInicial != null)
				crt.add("D.dt_protocolo", dtInicial, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			
			if(cdFase > 0) 
				crt.add("F.cd_fase", String.valueOf(cdFase), ItemComparator.EQUAL, Types.VARCHAR);
			
			if(cdTipoDocumento > 0) 
				crt.add("E.cd_tipo_documento", String.valueOf(cdTipoDocumento), ItemComparator.EQUAL, Types.VARCHAR);
			
			List<AitMovimento> _movimentos = this.aitMovimentoDocumentoService.getMovimentosFromDTO(crt);
			AitMovimentoServices.setDtPublicacaoDO(_movimentos, dtPublicacao);
			
			byte[] report = this.aitMovimentoDocumentoService.printRelatorioProcessos(crt, null);			
			
			if(report == null)
				return ResponseFactory.badRequest("Critérios fornecidos não produziram nenhum resultado.");
			
			return ResponseFactory.ok(report);

		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}

}
