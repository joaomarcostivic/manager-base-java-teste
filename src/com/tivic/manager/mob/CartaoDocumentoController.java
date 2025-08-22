package com.tivic.manager.mob;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
import javax.ws.rs.core.Response;


import org.restlet.util.DateUtils;
import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.fta.CategoriaVeiculoServices;
import com.tivic.manager.grl.Doenca;
import com.tivic.manager.mob.cartaodocumento.CartaoDocumentoService;
import com.tivic.sol.report.ReportServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoOcorrenciaServices;
import com.tivic.manager.ptc.DocumentoTramitacaoServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.DateUtil;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import javax.ws.rs.BadRequestException;

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


@Api(value = "Solicitações", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v2/mob/cartoesdocumentos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CartaoDocumentoController {

	CartaoDocumentoService cartaoDocumentoService;

	public CartaoDocumentoController() {
		this.cartaoDocumentoService = new CartaoDocumentoService();
	}

	@POST
	@Path("/")
	@ApiOperation(value = "Cria solicitações de gratuidade")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Registros criados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response create(
			@ApiParam(value = "Solicitação a ser registrada") CartaoDocumentoDTO cartaoDocumentoDTO) {
		try {
			cartaoDocumentoDTO.getDocumento().setCdDocumento(0);
			cartaoDocumentoDTO = cartaoDocumentoService.saveSolicitacao(cartaoDocumentoDTO.getCartaoDocumento(),
					cartaoDocumentoDTO.getPessoa(), cartaoDocumentoDTO.getPessoaFisica(),
					cartaoDocumentoDTO.getPessoaEndereco(), cartaoDocumentoDTO.getDocumento(),
					cartaoDocumentoDTO.getDoencas(), cartaoDocumentoDTO.getPessoaFichaMedica(),
					cartaoDocumentoDTO.getDocumentoOcorrencia().getCdUsuario());

			if (cartaoDocumentoDTO == null)
				return ResponseFactory.badRequest("Cartão documento não foi identificado");

			return ResponseFactory.ok(cartaoDocumentoDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{id}")
	@ApiOperation(value = "Atualizar uma solicitação")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Solicitação atualizada com sucesso", response = CartaoDocumento.class),
			@ApiResponse(code = 404, message = "Solicitação não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@ApiParam(value = "Código do cartão documento") @PathParam("id") int cdCartaoDocumento,
			@ApiParam(value = "Valores em cartao documento") CartaoDocumentoDTO cartaoDocumentoDTO) {
		try {
			cartaoDocumentoDTO.getCartaoDocumento().setCdCartaoDocumento(cdCartaoDocumento);
			 cartaoDocumentoDTO = cartaoDocumentoService.saveSolicitacao(cartaoDocumentoDTO.getCartaoDocumento());
			
			if (cartaoDocumentoDTO == null)
				return ResponseFactory.badRequest("Cartão documento não foi identificado");
			
			return ResponseFactory.ok(cartaoDocumentoDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("")
	@ApiOperation(value = "Retorna a lista de solicitações")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Solicitações encontrados"),
			@ApiResponse(code = 204, message = "Nenhuma solicitação encontrada"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public static Response findSolicitacoes(
			@ApiParam(value = "Situacao da vistoria") @QueryParam("stDocumento") String stDocumento,
			@ApiParam(value = "Nome do solicitante") @QueryParam("cdPessoa") int cdPessoa,
			@ApiParam(value = "Número do Documento") @QueryParam("nrDocumento") String nrDocumento,
			@ApiParam(value = "Número do CPF") @QueryParam("nrCpf") String nrCpf,
			@ApiParam(value = "Data da solicitação dd/MM/yyyy") @QueryParam("dtProtocoloInicial") String dtProtocoloInicial,
			@ApiParam(value = "Data da solicitação dd/MM/yyyy") @QueryParam("dtProtocoloFinal") String dtProtocoloFinal,
			@ApiParam(value = "Com ou Sem pericia", allowableValues = "0, 1") @QueryParam("tpPericia") @DefaultValue("-1") int tpPericia,
			@ApiParam(value = "Quantidade de registros") @QueryParam("limit") int nrLimite,
			@ApiParam(value = "Número da página") @QueryParam("page") int nrPagina) {
		try {

			Criterios crt = new Criterios();

			if (nrPagina != 0) {
				crt.add("qtDeslocamento", Integer.toString((nrLimite * nrPagina) - nrLimite), ItemComparator.EQUAL,
						Types.INTEGER);
			}

			if (nrLimite != 0) {
				crt.add("QTLIMITE", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (cdPessoa != 0) {
				crt.add("E.CD_PESSOA", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (stDocumento != null) {
				crt.add("D.NM_SITUACAO_DOCUMENTO", stDocumento, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nrDocumento != null) {
				crt.add("B.NR_DOCUMENTO", nrDocumento, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if (nrCpf != null) {
				crt.add("F.NR_CPF", nrCpf, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (tpPericia > -1) {
				crt.add("B.TP_INTERNO_EXTERNO", Integer.toString(tpPericia), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (dtProtocoloInicial != null) {
				crt.add("B.DT_PROTOCOLO", dtProtocoloInicial + " 00:00", ItemComparator.GREATER_EQUAL, Types.DATE);
			}

			if (dtProtocoloFinal != null) {
				crt.add("B.DT_PROTOCOLO", dtProtocoloFinal + " 23:59", ItemComparator.MINOR_EQUAL, Types.DATE);
			}

			ResultSetMap rsm = CartaoDocumentoServices.findSolicitacoes(crt);

			return ResponseFactory.ok(new CartaoDocumentoDTO.ListBuilder(rsm, rsm.getTotal()).build());

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}

	@GET
	@Path("/{id}")
	@ApiOperation(value = "Retorna uma solicitação de pne")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Solicitação encontrada"),
			@ApiResponse(code = 204, message = "Nenhuma solicitação encontrada"),
			@ApiResponse(code = 500, message = "Erro no servidor") })

	public static Response getSolicitacao(
			@ApiParam(value = "Código do cartão documento") @PathParam("id") int cdCartaoDocumento) {
		try {
			Criterios criterios = new Criterios();
			criterios.add("A.cd_cartao_documento", Integer.toString(cdCartaoDocumento), ItemComparator.EQUAL,
					Types.INTEGER);

			ResultSetMap rsm = CartaoDocumentoServices.findSolicitacoes(criterios);

			if (rsm == null)
				return ResponseFactory.noContent("Nao existe solicitação com o id indicado.");

			return ResponseFactory.ok(new CartaoDocumentoDTO.ListBuilder(rsm, rsm.getTotal()).build());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{id}/deficiencia")
	@ApiOperation(value = "Fornece a deficiência dado pelo o id do documento")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "PNE encontrada", response = Doenca.class),
			@ApiResponse(code = 204, message = "Nao existe PNE com o id indicado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })

	public static Response getDeficiencia(@ApiParam(value = "id do documento") @PathParam("id") int cdCartaoDocumento) {
		try {
			Criterios crt = new Criterios();
			crt.add("A.cd_cartao_documento", Integer.toString(cdCartaoDocumento), ItemComparator.EQUAL, Types.INTEGER);

			List<Doenca> list = CartaoDocumentoServices.getDeficienciaByPessoa(cdCartaoDocumento);

			if (list.isEmpty())
				return ResponseFactory.noContent("Nenhuma PNE encontrada");
			

			return ResponseFactory.ok(list);

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/{id}/ocorrencias")
	@ApiOperation(value = "Fornece a ocorrencias dado pelo o id do documento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tramitação encontrada", response = CartaoDocumentoDTO.class),
			@ApiResponse(code = 204, message = "Nao existe ocorrência com o id indicado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })

	public static Response getOcorrencias(
			@ApiParam(value = "id do cartao documento") @PathParam("id") int cdCartaoDocumento) {
		try {

			ResultSetMap rsm = CartaoDocumentoServices.getOcorrenciasByPessoa(cdCartaoDocumento);
			if (rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhuma Ocorrência encontrada.");

			return ResponseFactory.ok(new ResultSetMapper<CartaoDocumentoDTO>(rsm, CartaoDocumentoDTO.class));

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/{id}/cartao")
	@ApiOperation(value = "Fornece os dados do cartão pelo o id do cartão documento")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Tramitação encontrada", response = Cartao.class),
			@ApiResponse(code = 204, message = "Nao existe tramitação com o id indicado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })

	public static Response getCartao(
			@ApiParam(value = "id do cartao documento") @PathParam("id") int cdCartaoDocumento) {
		try {

			Cartao cartao = CartaoDocumentoServices.getCartao(cdCartaoDocumento);
			if (cartao == null)
				return ResponseFactory.noContent("Nenhum cartão encontrado.");

			return ResponseFactory.ok(cartao);

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/{id}/enderecos")
	@ApiOperation(value = "Fornece a informações de pessoa dado pelo o id do cartao documento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Informações de pessoa encontrada", response = CartaoDocumentoDTO.class),
			@ApiResponse(code = 204, message = "Nao existe informações de pessoa com o id indicado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })

	public static Response getPessoaByCartaoDocumento(
			@ApiParam(value = "id do cartao documento") @PathParam("id") int cdCartaoDocumento) {
		try {

			List<CartaoDocumentoDTO> list = CartaoDocumentoServices.getPessoaByCartaoDocumento(cdCartaoDocumento);
			if (list.isEmpty())
				return ResponseFactory.noContent("Nenhuma Informação encontrada.");

			return ResponseFactory.ok(list);

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/pessoa/{nrCpf}")
	@ApiOperation(value = "Retornar pessoa pelo o numero do cpf")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Pessoa encontrada", response = ConcessaoVeiculo.class),
			@ApiResponse(code = 204, message = "Pessoa não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })

	public static Response getPessoaByCpf(@ApiParam(value = "Informações da pessoa") @PathParam("nrCpf") String nrCpf) {
		try {
			if (nrCpf == null)
				return ResponseFactory.badRequest("Número do cpf obrigatório.");

			List<CartaoDocumentoDTO> list = CartaoDocumentoServices.getPessoaByCpf(nrCpf);

			if (list.isEmpty())
				return ResponseFactory.noContent("Não existe pessoa com esse cpf.");

			return ResponseFactory.ok(list);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/solicitacaodocumento")
	@ApiOperation(value = "Retorna a lista de solicitações de documento")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Solicitações encontrados"),
			@ApiResponse(code = 204, message = "Nenhuma solicitação encontrada"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public static Response find(@ApiParam(value = "Situacao da vistoria") @QueryParam("stDocumento") String stDocumento,
			@ApiParam(value = "Código do solicitante") @QueryParam("cdSolicitante") int cdSolicitante,
			@ApiParam(value = "Nome do solicitante") @QueryParam("nmSolicitante") String nmSolicitante,
			@ApiParam(value = "Nome do atendente") @QueryParam("cdAtendente") int cdAtendente,
			@ApiParam(value = "Código do atendente") @QueryParam("nmAtendente") String nmAtendente,
			@ApiParam(value = "Nome da situacao do documento") @QueryParam("nmSituacaoDocumento") String nmSituacaoDocumento,
			@ApiParam(value = "Código do CID") @QueryParam("idDoenca") String idDoenca,
			@ApiParam(value = "Número do Documento") @QueryParam("nrDocumento") String nrDocumento,
			@ApiParam(value = "Número do RG") @QueryParam("nrRG") String nrRG,
			@ApiParam(value = "Data de validade inicial dd/MM/yyyy") @QueryParam("dtValidadeInicial") String dtValidadeInicial,
			@ApiParam(value = "Data de validade final dd/MM/yyyy") @QueryParam("dtValidadeFinal") String dtValidadeFinal,
			@ApiParam(value = "Com ou Sem pericia", allowableValues = "0, 1") @QueryParam("tpInternoExterno") @DefaultValue("-1") int tpInternoExterno,
			@ApiParam(value = "Quantidade de registros") @QueryParam("limit") @DefaultValue("50") int nrLimite) {

		try {

			Criterios crt = new Criterios();

			if (nrLimite != 0) {
				crt.add("QTLIMITE", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (cdSolicitante != 0) {
				crt.add("F.CD_PESSOA", Integer.toString(cdSolicitante), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (cdAtendente != 0) {
				crt.add("H.CD_PESSOA", Integer.toString(cdAtendente), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (idDoenca != null) {
				crt.add("IDDOENCA", idDoenca, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (stDocumento != null) {
				crt.add("D.NM_SITUACAO_DOCUMENTO", stDocumento, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nrDocumento != null) {
				crt.add("A.NR_DOCUMENTO", nrDocumento, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nrRG != null) {
				crt.add("F.NR_RG", nrRG, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nmSolicitante != null) {
				crt.add("E.NM_PESSOA", nmSolicitante, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nmAtendente != null) {
				crt.add("H.NM_PESSOA", nmAtendente, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nmSituacaoDocumento != null) {
				crt.add("D.NM_SITUACAO_DOCUMENTO", nmSituacaoDocumento, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (tpInternoExterno > -1) {
				crt.add("A.TP_INTERNO_EXTERNO", Integer.toString(tpInternoExterno), ItemComparator.EQUAL,
						Types.INTEGER);
			}

			if (dtValidadeInicial != null) {
				crt.add("I.DT_VALIDADE", dtValidadeInicial, ItemComparator.GREATER_EQUAL, Types.DATE);
			}

			if (dtValidadeFinal != null) {
				crt.add("I.DT_VALIDADE", dtValidadeFinal, ItemComparator.MINOR_EQUAL, Types.DATE);
			}

			ResultSetMap rsm = CartaoDocumentoServices.findCartaoDocumento(crt);

			if (rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhum cartão/solicitação encontrados");

			return ResponseFactory.ok(new ResultSetMapper<CartaoDocumento>(rsm, CartaoDocumento.class));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}

	@GET
	@Path("relatorio/solicitacao")
	@ApiOperation(value = "Retorna a lista de solicitações")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Solicitações encontrados"),
			@ApiResponse(code = 204, message = "Nenhuma solicitação encontrada"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces("application/pdf")
	public Response getRelatorioSolicitacao(
			@ApiParam(value = "Situacao da vistoria") @QueryParam("stDocumento") String stDocumento,
			@ApiParam(value = "Nome do solicitante") @QueryParam("nmSolicitante") String nmSolicitante,
			@ApiParam(value = "Código do atendente") @QueryParam("nmAtendente") String nmAtendente,
			@ApiParam(value = "Nome da situacao do documento") @QueryParam("nmSituacaoDocumento") String nmSituacaoDocumento,
			@ApiParam(value = "Código do CID") @QueryParam("idDoenca") String idDoenca,
			@ApiParam(value = "Número do Documento") @QueryParam("nrDocumento") String nrDocumento,
			@ApiParam(value = "Número do Documento") @QueryParam("dtProtocolo") String dtProtocolo,
			@ApiParam(value = "Número do RG") @QueryParam("nrRG") String nrRG,
			@ApiParam(value = "Data de validade inicial dd/MM/yyyy") @QueryParam("dtValidadeInicial") String dtValidadeInicial,
			@ApiParam(value = "Data de validade final dd/MM/yyyy") @QueryParam("dtValidadeFinal") String dtValidadeFinal,
			@ApiParam(value = "Com ou Sem pericia", allowableValues = "0, 1") @QueryParam("tpInternoExterno") @DefaultValue("-1") int tpInternoExterno

	) {
		try {
			Criterios crt = new Criterios();

			if (idDoenca != null) {
				crt.add("ID_DOENCA", idDoenca, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (stDocumento != null) {
				crt.add("D.NM_SITUACAO_DOCUMENTO", stDocumento, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nrDocumento != null) {
				crt.add("A.NR_DOCUMENTO", nrDocumento, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nrRG != null) {
				crt.add("F.NR_RG", nrRG, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nmSolicitante != null) {
				crt.add("E.NM_PESSOA", nmSolicitante, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nmAtendente != null) {
				crt.add("H.NM_PESSOA", nmAtendente, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nmSituacaoDocumento != null) {
				crt.add("D.NM_SITUACAO_DOCUMENTO", nmSituacaoDocumento, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (tpInternoExterno > -1) {
				crt.add("A.TP_INTERNO_EXTERNO", Integer.toString(tpInternoExterno), ItemComparator.EQUAL,
						Types.INTEGER);
			}

			if (dtValidadeInicial != null) {
				crt.add("I.DT_VALIDADE", dtValidadeInicial, ItemComparator.GREATER_EQUAL, Types.DATE);
			}

			if (dtValidadeFinal != null) {
				crt.add("I.DT_VALIDADE", dtValidadeFinal, ItemComparator.MINOR_EQUAL, Types.DATE);
			}

			ResultSetMap _rsm = CartaoDocumentoServices.findRelatorioSolicitacoes(crt);

			if (_rsm == null || _rsm.size() <= 0)
				return ResponseFactory.noContent("Nenhuma concessão encontrada");

			HashMap<String, Object> params = new HashMap<String, Object>();

			String NM_SOLICITANTE = "";
			String NM_ATENDENTE = "";
			String Pericia = "";

			if (nmSolicitante != null) {
				NM_SOLICITANTE = nmSolicitante;
			}

			if (nmAtendente != null) {
				NM_ATENDENTE = nmAtendente;
			}

			if (tpInternoExterno == 1)
				Pericia = "Sem";
			if (tpInternoExterno == 0)
				Pericia = "Com";

			params.put("DS_TITULO_1", "PREFEITURA MUNICIPAL DE VITÓRIA DA CONQUISTA - BA");

			if (nmSolicitante != null)
				params.put("NM_SOLICITANTE", "Solicitante: " + NM_SOLICITANTE);

			if (nmAtendente != null)
				params.put("NM_ATENDENTE", "Atendente: " + NM_ATENDENTE);

			if (Pericia != "")
				params.put("TP_INTERNO_EXTERNO", "Perícia: " + Pericia);

			if (nmSituacaoDocumento != null)
				params.put("NM_SITUACAO_DOCUMENTO", "Situação: " + nmSituacaoDocumento);

			if (idDoenca != null)
				params.put("ID_DOENCA", "CID: " + idDoenca);

			if (dtValidadeInicial != null) {
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dtValidadeInicial);
				params.put("DT_INICIAL", date1);
			}

			if (dtValidadeFinal != null) {
				Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(dtValidadeFinal);
				params.put("DT_FINAL", date2);
			}

			if (dtProtocolo != null) {
				Date date3 = new SimpleDateFormat("dd/MM/yyyy").parse(dtProtocolo);
				params.put("DT_PROTOCOLO", date3);
			}

			_rsm.beforeFirst();
			return ResponseFactory.ok(ReportServices.getPdfReport("grl/relatorio_documento_solicitacao", params, _rsm));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{id}/deferimento")
	@ApiOperation(value = "Deferir um documento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Documento deferido com sucesso", response = CartaoDocumento.class),
			@ApiResponse(code = 404, message = "Documento não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public Response deferir(@ApiParam(value = "Código do cartão documento") @PathParam("id") int cdCartaoDocumento,
			@ApiParam(value = "Valores em cartao documento") CartaoDocumentoDTO cartaoDocumentoDTO) {
		try {
			CartaoDocumento cartaoDocumento = cartaoDocumentoService.deferir(cartaoDocumentoDTO);
			if (cartaoDocumento == null)
				return ResponseFactory.badRequest("Cartão documento não foi identificado");
			return ResponseFactory.ok(cartaoDocumento);
		} catch (BadRequestException e) {
			e.printStackTrace();
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{id}/indeferimento")
	@ApiOperation(value = "Indeferir um documento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Documento indeferido com sucesso", response = CartaoDocumento.class),
			@ApiResponse(code = 404, message = "Documento não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public Response indeferir(@ApiParam(value = "Código do cartão documento") @PathParam("id") int cdCartaoDocumento,
			@ApiParam(value = "Valores em cartao documento") CartaoDocumentoDTO cartaoDocumentoDTO) {
		try {
			CartaoDocumento cartaoDocumento = cartaoDocumentoService.indeferir(cdCartaoDocumento,
					cartaoDocumentoDTO.getDocumentoOcorrencia().getCdUsuario());
			if (cartaoDocumento == null)
				return ResponseFactory.badRequest("Cartão documento não foi identificado");
			return ResponseFactory.ok(cartaoDocumento);
		} catch (BadRequestException e) {
			e.printStackTrace();
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{id}/anulacao")
	@ApiOperation(value = "Anulação um documento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Documento anulado com sucesso", response = CartaoDocumento.class),
			@ApiResponse(code = 404, message = "Documento não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public Response anular(@ApiParam(value = "Código do cartão documento") @PathParam("id") int cdCartaoDocumento,
			@ApiParam(value = "Valores em cartao documento") CartaoDocumentoDTO cartaoDocumentoDTO) {
		try {
			CartaoDocumento cartaoDocumento = cartaoDocumentoService.anular(cdCartaoDocumento, cartaoDocumentoDTO);
			
			if (cartaoDocumento == null)
				return ResponseFactory.badRequest("Cartão documento não foi identificado");
			return ResponseFactory.ok(cartaoDocumento);
		} catch (BadRequestException e) {
			e.printStackTrace();
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{id}/marcarpericia")
	@ApiOperation(value = "Marcar Pericia de uma solicitação")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Marcada a perícia com sucesso", response = CartaoDocumento.class),
			@ApiResponse(code = 404, message = "Perícia não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.APPLICATION_JSON)
	public Response marcarPericia(
			@ApiParam(value = "Código do cartão documento") @PathParam("id") int cdCartaoDocumento,
			@ApiParam(value = "Valores em cartao documento") CartaoDocumentoDTO cartaoDocumentoDTO) {
		try {
			CartaoDocumento cartaoDocumento = cartaoDocumentoService.encaminharPericia(cdCartaoDocumento, cartaoDocumentoDTO);
			if (cartaoDocumento == null)
				return ResponseFactory.badRequest("Cartão documento não foi identificado");
			return ResponseFactory.ok(cartaoDocumento);
		} catch (BadRequestException e) {
			e.printStackTrace();
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}