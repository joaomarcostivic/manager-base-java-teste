package com.tivic.manager.mob;

import java.io.ByteArrayInputStream;
import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import org.json.JSONObject;

import com.tivic.manager.mob.ait.AitEfeitoSuspensivoDTOListBuilder;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.templates.AitFindEmissao;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.auth.jwt.JWTIgnore;
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
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "AIT", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v2/mob/aits")
@Produces(MediaType.APPLICATION_JSON)
public class AitController {
	
	private IAitService aitService;
	private ManagerLog managerLog;
	
	public AitController() throws Exception {
		this.aitService = (IAitService) BeansFactory.get(IAitService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Registra um novo AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "AIT registrado", response = Ait.class),
			@ApiResponse(code = 400, message = "AIT possui algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class) })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@ApiParam(value = "AIT a ser registrado", required = true) Ait ait) {
		try {	
			
			ait.setCdAit(0);

			Result r = AitServices.save(ait, null, null, null, null, null);
			if (r.getCode() < 0) {
				return ResponseFactory.badRequest("AIT possui algum parametro invalido", r.getMessage());
			}

			return ResponseFactory.ok((Ait) r.getObjects().get("AIT"));

		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@PUT
	@Path("/{id}")
	@ApiOperation(value = "Atualiza dados de um AIT", notes = "Considere id = cdAit")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "AIT atualizado", response = Ait.class),
			@ApiResponse(code = 400, message = "AIT é nulo ou invalido"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(
			@ApiParam(value = "Id do AIT a ser atualizado", required = true) @PathParam("id") int cdAit, 
			@ApiParam(value = "AIT a ser atualizado", required = true) Ait ait) {
		try {
			if (ait.getCdAit() == 0)
				return ResponseFactory.badRequest("AIT é nulo ou invalido");

			ait.setCdAit(cdAit);
			Result r = AitServices.save(ait);
			if (r.getCode() < 0)
				return ResponseFactory.badRequest("AIT é nulo ou invalido", r.getMessage());

			return ResponseFactory.ok((Ait) r.getObjects().get("AIT"));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@PUT
	@Path("/{id}/detran")
	@ApiOperation(value = "Atualiza dados de um AIT", notes = "Considere id = cdAit")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "AIT atualizado", response = Ait.class),
			@ApiResponse(code = 400, message = "AIT é nulo ou invalido"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateFromDetran(
			@ApiParam(value = "Id do AIT a ser atualizado", required = true) @PathParam("id") int cdAit) {
		try {
			if (cdAit == 0)
				return ResponseFactory.badRequest("AIT é nulo ou invalido");

			Ait ait = AitDAO.get(cdAit);

			Result result = AitServices.updateDetran(ait);
			if (result.getCode() < 0) {
				return ResponseFactory.internalServerError("Erro ao atualizar ait");
			}

			Ait _ait = (Ait) result.getObjects().get("AIT");

			return ResponseFactory.ok(_ait);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
		value = "Fornece lista de AIT",
		notes = "# Tipos \r\n"
				+ "## Situacao \r\n"
				+ "| `stAit` | Valor      |\r\n" 
				+ "|---------|------------|\r\n" 
				+ "| 0       | Cancelado  |\r\n" 
				+ "| 1       | Confirmado |\r\n" 
				+ "| 2       | Pendente   |"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AIT encontrado", response = Ait[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response retrieveAll(
			@ApiParam(value = "Identificador do AIT") @QueryParam("ait") String idAit,
			@ApiParam(value = "Inicio do periodo de autuacao (dd/mm/yyyy)") @QueryParam("inicioAutuacao") String dtInfracaoInicial,
			@ApiParam(value = "Fim do periodo de autuacao (dd/mm/yyyy)") @QueryParam("fimAutuacao") String dtInfracaoFinal,
			@ApiParam(value = "Numero da placa") @QueryParam("placa") String nrPlaca,
			@ApiParam(value = "Numero do RENAVAN") @QueryParam("renavan") String nrRenavan,
			@ApiParam(value = "Nome de proprietario") @QueryParam("proprietario") String nmProprietario,
			@ApiParam(value = "CPF proprietario") @QueryParam("cpfProprietario") String nrCpfProprietario,
			@ApiParam(value = "Nome do condutor") @QueryParam("condutor") String nmCondutor,
			@ApiParam(value = "CNH do condutor") @QueryParam("cnhCondutor") String nrCnhCondutor,
			@ApiParam(value = "Situacao do AIT", allowableValues = "0, 1, 2") @QueryParam("stAit") Integer stAit,
			@ApiParam(value = "Codigo de Infracao (DETRAN)") @QueryParam("codigoInfracao") @DefaultValue("0") int nrCodDetran,
			@ApiParam(value = "Numero de matricula do agente autuador") @QueryParam("matriculaAgente") String nrMatriculaAgente,
			@ApiParam(value = "Numero de processo (JARI)") @QueryParam("numeroProcesso") String nrProcesso,
			@ApiParam(value = "Numero de controle interno (DETRAN)") @QueryParam("nrControle") String nrControle,
			@ApiParam(value = "Local da infracao") @QueryParam("local") String dsLocalInfracao,
			@ApiParam(value = "CNPJ do Proprietário") @QueryParam("nrCnpjProprietario") String nrCnpjProprietario,
			@ApiParam(value = "Número de Protocolo") @QueryParam("nrProtocolo") String nrDocumento,
			@ApiParam(value = "Código do AIT de origem") @QueryParam("cdAitOrigem") int cdAitOrigem,
			@ApiParam(value = "Busca em multiplos campos por esta palavra chave") @QueryParam("keyword") String keyword,
			@ApiParam(value = "Quantidade maxima de registros", defaultValue = "100") @DefaultValue("100") @QueryParam("limit") int limit,
			@ApiParam(value = "Busca os ultimos registros inseridos na base") @QueryParam("last") String last) {
		try {
			Criterios crt = new Criterios();
			crt.add("qtLimite", Integer.toString(limit), ItemComparator.EQUAL, Types.INTEGER);

			if (last != null && !last.equals("false")) {
				crt.add("last", "true", ItemComparator.EQUAL, Types.BOOLEAN);
			}

			if (keyword != null) {
				crt.add("keyword", keyword, ItemComparator.EQUAL, Types.VARCHAR);
			}

			if (idAit != null && !idAit.equals("")) {
				idAit = idAit.toUpperCase();
				crt.add("A.id_ait", "%"  + idAit + "%" , ItemComparator.LIKE, Types.VARCHAR);
			}

			if (dtInfracaoInicial != null) {
				crt.add("A.dt_infracao", dtInfracaoInicial, ItemComparator.GREATER_EQUAL, Types.CHAR);
			}

			if (dtInfracaoFinal != null) {
				crt.add("A.dt_infracao", dtInfracaoFinal, ItemComparator.MINOR_EQUAL, Types.CHAR);
			} else if (dtInfracaoInicial != null) {
				crt.add("A.dt_infracao", dtInfracaoInicial, ItemComparator.MINOR_EQUAL, Types.CHAR);
			}

			if (nrPlaca != null && !nrPlaca.equals("")) {
				nrPlaca = nrPlaca.toUpperCase();
				crt.add("UPPER(A.nr_placa)", nrPlaca, ItemComparator.EQUAL, Types.VARCHAR);
			}

			if (nrRenavan != null && !nrRenavan.equals("")) {
				crt.add("A.nr_renavan", nrRenavan, ItemComparator.EQUAL, Types.VARCHAR);
			}

			if (nmProprietario != null && !nmProprietario.equals("")) {
				crt.add("A.nm_proprietario", nmProprietario, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if (nrCpfProprietario != null && !nrCpfProprietario.equals("")) {
				crt.add("A.nr_cpf_proprietario", nrCpfProprietario.replaceAll("[./-]", ""), ItemComparator.LIKE_ANY,
						Types.VARCHAR);
			}

			if (nrCnpjProprietario != null && !nrCnpjProprietario.trim().equals("")) {
				crt.add("A.nr_cpf_cnpj_proprietario", nrCnpjProprietario.replaceAll("[./-]", ""), ItemComparator.EQUAL,
						Types.VARCHAR);
			}

			if (nmCondutor != null && !nmCondutor.equals("")) {
				crt.add("A.nm_condutor", nmCondutor, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if (nrCnhCondutor != null && !nrCnhCondutor.equals("")) {
				crt.add("A.nr_cnh_condutor", nrCnhCondutor, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if (stAit != null) {
				crt.add("A.st_ait", Integer.toString(stAit), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (nrCodDetran > 0) {
				crt.add("B.nr_cod_detran", Integer.toString(nrCodDetran), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (nrMatriculaAgente != null && !nrMatriculaAgente.equals("")) {
				crt.add("J.nr_matricula", nrMatriculaAgente, ItemComparator.LIKE_BEGIN, Types.VARCHAR);
			}

			if (nrControle != null && !nrControle.trim().equals("")) {
				crt.add("A.nr_controle", nrControle, ItemComparator.EQUAL, Types.VARCHAR);
			}

			if (dsLocalInfracao != null) {
				crt.add("A.ds_local_infracao", dsLocalInfracao, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if (nrDocumento != null && !nrDocumento.equals(""))
				crt.add("DOC.nr_documento", nrDocumento, ItemComparator.LIKE_ANY, Types.VARCHAR);
			
			if (cdAitOrigem > 0) {
				crt.add("A.cd_ait_origem", Integer.toString(cdAitOrigem), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			ResultSetMap rsm = AitServices.find(crt);

			if (!rsm.next()) {
				return ResponseFactory.noContent("Nenhum registro");
			}

			return ResponseFactory.ok(new AitDtInfracaoDeserializeDTO.ListBuilder(rsm).setInfracao(rsm).setMovimentoAtual(rsm)
					.setEquipamento(rsm).setAgente(rsm).setTalonario(rsm).build());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	
	@GET
	@Path("/ativos")
	@ApiOperation(
		value = "Fornece AITs filtrados não cancelados"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AITs encontrados", response = AitDTO.class),
		@ApiResponse(code = 204, message = "Nao existe AITs com os critérios indicados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveNotCanceled(
			@ApiParam(value = "Identificador do AIT") @QueryParam("ait") String idAit,
			@ApiParam(value = "Numero da placa") @QueryParam("placa") String nrPlaca,
			@ApiParam(value = "Número do Documento") @QueryParam("nrDocumento") String nrDocumento,
			@ApiParam(value = "Status do Ait") @QueryParam("tpStatus") @DefaultValue("-1") int tpStatus,
			@ApiParam(value = "Quantidade maxima de registros", defaultValue = "10") @DefaultValue("10") @QueryParam("limit") int limit) {
		try {	
			SearchCriterios search = new SearchCriterios();
			search.addCriteriosEqualString("id_ait", idAit, idAit != null && !idAit.trim().equals(""));
			search.addCriteriosEqualString("nr_placa", nrPlaca, nrPlaca != null && !nrPlaca.trim().equals(""));
			search.addCriteriosEqualString("nr_documento", nrDocumento, nrDocumento != null && !nrDocumento.trim().equals(""));
			search.addCriteriosEqualInteger("A.tp_status", tpStatus, tpStatus > -1);
			search.addCriterios("A.tp_status", String.valueOf(TipoStatusEnum.CADASTRO_CANCELADO.getKey()), ItemComparator.NOTIN, Types.INTEGER, true);
			search.addCriterios("A.tp_status", String.valueOf(TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey()), ItemComparator.NOTIN, Types.INTEGER, true);
			search.addCriterios("A.tp_status", String.valueOf(TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey()), ItemComparator.NOTIN, Types.INTEGER, true);
			search.addCriterios("A.tp_status", String.valueOf(TipoStatusEnum.CANCELAMENTO_MULTA.getKey()), ItemComparator.NOTIN, Types.INTEGER, true);
			search.setQtLimite(limit);
			return ResponseFactory.ok(AitServices.buscarAitsAtivos(search));
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
		
	@GET
	@Path("/{id}")
	@ApiOperation(value = "Fornece um AIT dado o id indicado", notes = "Considere id = cdAit")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "AIT encontrado", response = AitDTO.class),
			@ApiResponse(code = 204, message = "Nao existe AIT com o id indicado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public static Response retrieveById(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) {
		try {
			Criterios crt = new Criterios();
			ResultSetMap rsm = AitServices.find(crt.add((Util.isStrBaseAntiga() ? "codigo_ait" : "A.cd_ait"),
					Integer.toString(cdAit), ItemComparator.EQUAL, Types.INTEGER));

			if (!rsm.next())
				return ResponseFactory.noContent("Nao existe AIT com o id indicado.");

			return ResponseFactory.ok(new AitDtInfracaoDeserializeDTO.Builder(rsm.getRegister()).setInfracao(rsm.getInt("cd_infracao"), true)
					.setMovimentoAtual(rsm.getInt("cd_movimento_atual"), rsm.getInt("cd_ait"), true)
					.setEquipamento(rsm.getInt("cd_equipamento"), false).setAgente(rsm.getInt("cd_agente"), true)
					.setTalonario(rsm.getInt("cd_talao"), true).build());

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/{id}/imagens")
	@ApiOperation(
		value = "Fornece um AIT dado o id indicado",
		notes = "Considere id = cdAit"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AIT encontrado", response = AitImagem[].class),
		@ApiResponse(code = 204, message = "Nao existe AIT com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response retrieveImagens(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) {
		try {	
			
			ResultSetMap rsm = AitImagemServices.getFromAit(cdAit);

			if (!rsm.next())
				return ResponseFactory.noContent("Nao existe AIT com o id indicado.");

			return ResponseFactory.ok(new ResultSetMapper<AitImagem>(rsm, AitImagem.class));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/{id}/imagens/{idImagem}")
	@ApiOperation(
		value = "Fornece um AIT dado o id indicado",
		notes = "Considere id = cdAit"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AIT encontrado", response = AitImagem.class),
		@ApiResponse(code = 204, message = "Nao existe AIT com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response retrieveImagem(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit, @ApiParam(value = "id da imagem") @PathParam("idImagem") int cdImagem) {
		try {	
			AitImagem imagem = AitImagemDAO.get(cdImagem, cdAit);

			if (imagem == null)
				return ResponseFactory.noContent("Nao existe imagem com o id indicado.");

			return ResponseFactory.ok(imagem);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/{id}/imagens/file/{idImagem}")
	@ApiOperation(value = "Fornece um AIT dado o id indicado", notes = "Considere id = cdAit")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "AIT encontrado", response = AitImagem.class),
			@ApiResponse(code = 204, message = "Nao existe AIT com o id indicado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	@Produces({ "image/jpg" })
	@JWTIgnore
	public Response retrieveImagemBlob(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit, @ApiParam(value = "id da imagem") @PathParam("idImagem") int cdImagem) {
		try {	
			AitImagem imagem = AitImagemDAO.get(cdImagem, cdAit);

			if (imagem == null)
				return ResponseFactory.noContent("Nao existe imagem com o id indicado.");

			return ResponseFactory.ok(new ByteArrayInputStream(imagem.getBlbImagem()));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/{id}/cancelar/{idUsuario}/{idOcorrencia}")
	@ApiOperation(
		value = "Cancela um AIT dado o id indicado",
		notes = "Considere id = cdAit e idOcorrencia = cdOcorrencia"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = AitImagem.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	public Response cancelar(
			@ApiParam(value = "id do AIT") @PathParam("id") int cdAit,
			@ApiParam(value = "id do usuario que realizou o cancelamento") @PathParam("idUsuario") int cdUsuario,
			@ApiParam(value = "id da ocorrencia") @PathParam("idOcorrencia") int cdOcorrencia) {
		try {
			return ResponseFactory.ok(AitServices.cancelarAit(cdAit, cdUsuario, cdOcorrencia));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@POST
	@Path("/{id}/cancelar/{idUsuario}/{idOcorrencia}")
	@ApiOperation(
		value = "Cancela um AIT dado o id indicado",
		notes = "Considere id = cdAit e idOcorrencia = cdOcorrencia"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = AitImagem.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	public Response cancelar(
			@ApiParam(value = "Cancelamento a ser registrado") AitMovimento aitMovimento) {
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

	@GET
	@Path("/estatisticas")
	@ApiOperation(value = "Estatisticas de AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Nao ha estatistica para os parametros fornecidos."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public Response getEstatistica(
			@ApiParam(value = "Inicio do periodo (dd/mm/yyyy)", required = true) @QueryParam("inicio") String inicio,
			@ApiParam(value = "Fim do periodo (dd/mm/yyyy)", required = true) @QueryParam("fim") String fim,
			@ApiParam(value = "Relacao", allowableValues = "tipo, agente, equipamento, natureza, valor", required = true) @QueryParam("relacao") String relacao,
			@ApiParam(value = "Agrupamento", allowableValues = "diario, mensal, periodo") @QueryParam("agrupamento") String agrupamento,
			@ApiParam(value = "Quantidade maxima de registros", defaultValue = "10") @DefaultValue("10") @QueryParam("limit") int limit) {
		try {

			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio + " 00:00:00");
			GregorianCalendar dtFinal = Util.convStringToCalendar(fim + " 23:59:59");

			switch (relacao) {
			case "tipo":
				return ResponseFactory.ok(AitBIServices.statsRankingInfracao(dtInicial, dtFinal, 5));
			case "agente":
				return ResponseFactory.ok(AitBIServices.statsAitAgente(dtInicial, dtFinal, limit));
			case "equipamento":
				if (agrupamento == null)
					return ResponseFactory.ok(AitBIServices.statsAitEquipamentoAgrupado(dtInicial, dtFinal));
				else if (agrupamento.equals("periodo"))
					return ResponseFactory.ok(AitBIServices.statsProducaoMensal(dtInicial, dtFinal));
				else if (agrupamento.equals("mensal"))
					return ResponseFactory.ok(AitBIServices.statsProducaoMensal(dtInicial, dtFinal));
				else if (agrupamento.equals("diario"))
					return ResponseFactory.ok(AitBIServices.statsProducaoDiario(dtInicial, dtFinal));
				else
					return ResponseFactory.noContent("Nao ha estatistica para os parametros fornecidos.");
			case "natureza":
				return ResponseFactory.ok(AitBIServices.statsRankingNatureza(dtInicial, dtFinal));
			case "valor":
				if (agrupamento == null)
					return ResponseFactory.ok(AitBIServices.statsAitValor(dtInicial, dtFinal).getObjects());
				else if (agrupamento.equals("mensal"))
					return ResponseFactory.ok(AitBIServices.statsAitValorMensal(dtInicial, dtFinal));
				else
					return ResponseFactory.noContent("Nao ha estatistica para os parametros fornecidos.");
			default:
				return ResponseFactory.noContent("Nao ha estatistica para os parametros fornecidos.");
			}
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.",
					e.getLocalizedMessage());
		}
	}

	@GET
	@Path("/estatisticas/radar")
	@ApiOperation(value = "Estatisticas de AIT emitidas por radar")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Nao ha estatistica para os parametros fornecidos."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public Response getEstatisticaRadar(
			@ApiParam(value = "Inicio do periodo (dd/mm/yyyy)", required = true) @QueryParam("inicio") String inicio,
			@ApiParam(value = "Fim do periodo (dd/mm/yyyy)", required = true) @QueryParam("fim") String fim,
			@ApiParam(value = "Agrupamento", allowableValues = "velocidade, faixa, especie") @QueryParam("agrupamento") String agrupamento,
			@ApiParam(value = "Quantidade maxima de registros", defaultValue = "10") @DefaultValue("10") @QueryParam("limit") int limit) {
		try {

			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio + " 00:00:00");
			GregorianCalendar dtFinal = Util.convStringToCalendar(fim + " 23:59:59");

			switch (agrupamento) {
			case "velocidade":
				return ResponseFactory.ok(AitBIServices.statsInfracaoRadarVelocidade(dtInicial, dtFinal));
			case "faixa":
				return ResponseFactory.ok(AitBIServices.statsInfracaoRadarFaixa(dtInicial, dtFinal));
			case "especie":
				return ResponseFactory.ok(AitBIServices.statsInfracaoRadarEspecie(dtInicial, dtFinal));
			default:
				return ResponseFactory.noContent("Nao ha estatistica para os parametros fornecidos.");
			}
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.",
					e.getLocalizedMessage());
		}
	}

	@GET
	@Path("/estatisticas/talonario")
	@ApiOperation(value = "Estatisticas de AIT emitidas por talonarios")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Nao ha estatistica para os parametros fornecidos."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public Response getEstatisticaTalonario(
			@ApiParam(value = "Inicio do periodo (dd/mm/yyyy)", required = true) @QueryParam("inicio") String inicio,
			@ApiParam(value = "Fim do periodo (dd/mm/yyyy)", required = true) @QueryParam("fim") String fim,
			@ApiParam(value = "Agrupamento", allowableValues = "agente, cidade, especie") @QueryParam("agrupamento") String agrupamento,
			@ApiParam(value = "Quantidade maxima de registros", defaultValue = "10") @DefaultValue("10") @QueryParam("limit") int limit) {
		try {

			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio + " 00:00:00");
			GregorianCalendar dtFinal = Util.convStringToCalendar(fim + " 23:59:59");

			switch (agrupamento) {
			case "agente":
				return ResponseFactory.ok(AitBIServices.statsInfracaoTalonarioAgente(dtInicial, dtFinal));
			case "cidade":
				return ResponseFactory.ok(AitBIServices.statsInfracaoTalonarioCidade(dtInicial, dtFinal));
			case "especie":
				return ResponseFactory.ok(AitBIServices.statsInfracaoTalonarioEspecie(dtInicial, dtFinal));
			default:
				return ResponseFactory.noContent("Nao ha estatistica para os parametros fornecidos.");
			}
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.",
					e.getLocalizedMessage());
		}
	}

	@GET
	@Path("/estatisticas/geo")
	@ApiOperation(value = "Estatisticas geolocalizadas de AIT emitidas")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Nao ha estatistica para os parametros fornecidos."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public Response getEstatisticaGeo(
			@ApiParam(value = "Inicio do periodo (dd/mm/yyyy)", required = true) @QueryParam("inicio") String inicio,
			@ApiParam(value = "Fim do periodo (dd/mm/yyyy)", required = true) @QueryParam("fim") String fim,
			@ApiParam(value = "Agrupamento", allowableValues = "velocidade, faixa, especie") @QueryParam("agrupamento") String agrupamento,
			@ApiParam(value = "Quantidade maxima de registros", defaultValue = "10") @DefaultValue("10") @QueryParam("limit") int limit) {
		try {

			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio + " 00:00:00");
			GregorianCalendar dtFinal = Util.convStringToCalendar(fim + " 23:59:59");

			switch (agrupamento) {
			case "cluster":
				return ResponseFactory.ok(AitBIServices.statsInfracaoCluster(dtInicial, dtFinal));
			default:
				return ResponseFactory.noContent("Nao ha estatistica para os parametros fornecidos.");
			}
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.",
					e.getLocalizedMessage());
		}
	}

	@GET
	@Path("/estatisticas/relatorio")
	@ApiOperation(value = "Estatisticas de AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Não ha estatistica para os parametros fornecidos."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public Response getEstatisticaAit
	(
			@ApiParam(value = "Inicio do periodo (dd/mm/yyyy)", required = true) @QueryParam("dtInicial") String inicialAit,
			@ApiParam(value = "Fim do periodo (dd/mm/yyyy)", required = true) @QueryParam("dtFinal") String finalAit,
			@ApiParam(value = "Tipo do Ait", required = true) @QueryParam("tpAit") int tpAit) {
		Response estatistica = null;

		try {
			int tpEstatisticaPorInfracao = 1;
			int tpEstatisticaPorAgentes = 2;
			int tpEstatisticaEvolucaoMensal = 3;
			int tpEstatisticaGravidade = 4;
			int tpEstatisticaPorInfracaoMoto = 5;
			int tpEstatisticaNaiNip = 6;
			int tpEstatisticaCompetenciaEstado = 7;

			GregorianCalendar dtInicialAit = Util.convStringToCalendar(inicialAit + " 00:00:00");
			GregorianCalendar dtFinalAit = Util.convStringToCalendar(finalAit + " 23:59:59");

			if (tpAit == tpEstatisticaPorInfracao) {
				estatistica = ResponseFactory.ok(AitServices.estatisticaPorInfracao(dtInicialAit, dtFinalAit, null));
			} else if (tpAit == tpEstatisticaPorAgentes) {
				estatistica = ResponseFactory.ok(AitServices.estatisticaPorAgente(dtInicialAit, dtFinalAit, null));
			} else if (tpAit == tpEstatisticaEvolucaoMensal) {
				estatistica = ResponseFactory.ok(AitServices.estatisticaAitMensal(dtInicialAit, dtFinalAit, null));
			} else if (tpAit == tpEstatisticaGravidade) {
				estatistica = ResponseFactory.ok(AitServices.estatisticaAitGravidade(dtInicialAit, dtFinalAit, null));
			} else if (tpAit == tpEstatisticaPorInfracaoMoto) {
				estatistica = ResponseFactory
						.ok(AitServices.estatisticaAitInfracoesMotos(dtInicialAit, dtFinalAit, null));
			} else if (tpAit == tpEstatisticaNaiNip) {
				estatistica = ResponseFactory
						.ok(AitServices.estatisticaAitInfracoesNaiNip(dtInicialAit, dtFinalAit, null));
			} else if (tpAit == tpEstatisticaCompetenciaEstado) {
				estatistica = ResponseFactory
						.ok(AitServices.estatisticaAitInfracoesCompetenciaEstado(dtInicialAit, dtFinalAit, null));
			}

			return estatistica;

		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.",
					e.getLocalizedMessage());
		}
	}

	@GET
	@Path("/naoentregues/aits")
	@ApiOperation(value = "Listar AITs com creiterios para publicação no diário oficial")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Não ha estatistica para os parametros fornecidos."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public Response getDocumentoNaoEntregues
	(
			@ApiParam(value = "Inicio do periodo (dd/mm/yyyy)", required = true) @QueryParam("dtInicial") String inicialAit,
			@ApiParam(value = "Fim do periodo (dd/mm/yyyy)", required = true) @QueryParam("dtFinal") String finalAit,
			@ApiParam(value = "Tipo do Ait", required = true) @QueryParam("tpAit") int tpAit) {
		Response documentosNaoEntregues = null;
		try {
			GregorianCalendar dtInicialAit = inicialAit != null ? Util.convStringToCalendar(inicialAit + " 00:00:00")
					: Util.convStringToCalendar("01/01/2000 00:00:00");
			GregorianCalendar dtFinalAit = finalAit != null ? Util.convStringToCalendar(finalAit + " 23:59:59")
					: new GregorianCalendar();
			if (tpAit == AitMovimentoServices.NAI_ENVIADO) {
				documentosNaoEntregues = ResponseFactory.ok(AitServices.listarDocumentosNaoEntregues(dtInicialAit,
						dtFinalAit, AitMovimentoServices.NAI_ENVIADO));
			} else if (tpAit == AitMovimentoServices.NIP_ENVIADA) {
				documentosNaoEntregues = ResponseFactory.ok(AitServices.listarDocumentosNaoEntregues(dtInicialAit,
						dtFinalAit, AitMovimentoServices.NIP_ENVIADA));
			}
			return documentosNaoEntregues;
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.",
					e.getLocalizedMessage());
		}
	}

	@GET
	@Path("/{id}/impressoes/condutor")
	@ApiOperation(value = "Gera arquivos de impressao", notes = "Considere id = cdAit e idRelatorio o identificador de relatorio")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces("application/pdf")
	public Response getPrint(
			@ApiParam(value = "id do AIT") @PathParam("id") int cdAit
		) {
		try 
		{		
			return ResponseFactory.ok(AitReportServices.getCondutor(cdAit));
		} catch (ValidacaoException e) {
			return ResponseFactory.badRequest("Erro ao gerar PDF");
		}

		catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/impressoes/documentosnaoentregue")
	@ApiOperation(value = "Gera arquivos de impressao", notes = "Considere id = cdAit e idRelatorio o identificador de relatorio")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces("application/pdf")
	public Response printDocumentosNaoEntregues(
			@ApiParam(value = "Tipo de movimento") @QueryParam("tpAit") int tpAit,
			@ApiParam(value = "Inicio do periodo (dd/mm/yyyy)", required = true) @QueryParam("dtInicial") String inicialAit,
			@ApiParam(value = "Fim do periodo (dd/mm/yyyy)", required = true) @QueryParam("dtFinal") String finalAit) {
		try {
			int isNai = 1;
			int isNip = 2;

			GregorianCalendar dtInicialAit = inicialAit != null ? Util.convStringToCalendar(inicialAit + " 00:00:00")
					: Util.convStringToCalendar("01/01/2000 00:00:00");
			GregorianCalendar dtFinalAit = finalAit != null ? Util.convStringToCalendar(finalAit + " 23:59:59")
					: new GregorianCalendar();

			if (tpAit == isNai) {
				return ResponseFactory.ok(AitReportServices
						.printDocumentosNaoEntregues(AitMovimentoServices.NAI_ENVIADO, dtInicialAit, dtFinalAit));
			} else if (tpAit == isNip) {
				return ResponseFactory.ok(AitReportServices
						.printDocumentosNaoEntregues(AitMovimentoServices.NIP_ENVIADA, dtInicialAit, dtFinalAit));
			}

			return ResponseFactory.ok("ok");
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{id}/impressoes/nai/{idRelatorio}")
	@ApiOperation(value = "Gera arquivos de impressao de NAI", notes = "impressão de nai")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response getPrintNai(
			@ApiParam(value = "id do AIT") @PathParam("id") int cdAit,
			@ApiParam(value = "id do relatorio") @PathParam("idRelatorio") String idReport,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			JSONObject response = new JSONObject();
			switch (idReport) {
			case "nai1":
				response.put("code", 1);
				response.put("blob", AitReportServices.getNAIPrimeiraVia(cdAit, cdUsuario));
				return ResponseFactory.ok(response);
			case "nai2":
				response.put("code", 1);
				response.put("blob", AitReportServices.getNAISegundaVia(cdAit));
				return ResponseFactory.ok(response);
			default:
				return ResponseFactory.noContent("Nao ha relatorio com o id indicado.");
			}

		} catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (AitReportErrorException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}

	@GET
	@Path("/{id}/impressoes/nip/{idRelatorio}")
	@ApiOperation(value = "Gera arquivos de impressao de NIP", notes = "impressão de nip")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response getPrintNip(
			@ApiParam(value = "id do AIT") @PathParam("id") int cdAit,
			@ApiParam(value = "id do relatorio") @PathParam("idRelatorio") String idReport,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario)
			throws ValidacaoException {

		try {
			JSONObject response = new JSONObject();

			switch (idReport) {
			case "nip1":
				response.put("code", 1);
				response.put("blob", AitReportServices.getNIPPrimeiraVia(cdAit, cdUsuario));
				return ResponseFactory.ok(response);
			case "nip2":
				response.put("code", 1);
				response.put("blob", AitReportServices.getNIPSegundaVia(cdAit));
				return ResponseFactory.ok(response);
			default:
				return ResponseFactory.noContent("Nao ha relatorio com o id indicado.");
			}
		} catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		}

		catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	/* Implementando NIC */

	@GET
	@Path("/{id}/impressoes/nic/{idRelatorio}")
	@ApiOperation(value = "Gera arquivos de impressao de NIC", notes = "impressão de nic")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response getPrintNic(
			@ApiParam(value = "id do AIT") @PathParam("id") int cdAit,
			@ApiParam(value = "id do relatorio") @PathParam("idRelatorio") String idReport,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario)
			throws ValidacaoException {

		try {
			AitReportServices reportServices = new AitReportServices();
			JSONObject response = new JSONObject();

			switch (idReport) {
			case "nic1":
				response.put("code", 1);
				response.put("blob", reportServices.getNICPrimeiraVia(cdAit, cdUsuario));
				return ResponseFactory.ok(response);
			case "nic2":
				response.put("code", 1);
				response.put("blob", reportServices.getNICSegundaVia(cdAit));
				return ResponseFactory.ok(response);
			default:
				return ResponseFactory.noContent("Nao ha relatorio com o id indicado.");
			}
		} catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		}

		catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	/******************/

	@GET
	@Path("/{id}/reimpressao/nip/")
	@ApiOperation(value = "Imprime a segunda via de NIP com os dados originais da primeira", notes = "Reimpressão de nip")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response reimpressaoDadosNip (@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) throws ValidacaoException 
	{
		try 
		{		
			JSONObject response = new JSONObject();

			response.put("code", 1);
			response.put("blob", AitReportServices.reimpressaoDadosNip(cdAit));
			return ResponseFactory.ok(response);

		} catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		}

		catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{id}/verificarvencimento/nip/")
	@ApiOperation(value = "Verifica se a nip gerada esta vencida", notes = "Verificação de vencimento nip")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces("application/json")
	public Response verificarVencimentoNip (@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) throws ValidacaoException 
	{
		try 
		{		
			JSONObject response = new JSONObject();

			response.put("code", 1);
			response.put("blob", AitReportServices.verificarVencimentoNip(cdAit));
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{id}/verificarvencimento/nic/")
	@ApiOperation(value = "Verifica se a nip gerada esta vencida", notes = "Verificação de vencimento nip")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces("application/json")
	public Response verificarVencimentoNic (@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) throws ValidacaoException 
	{
		try 
		{		
			JSONObject response = new JSONObject();

			response.put("code", 1);
			response.put("blob", AitReportServicesNic.verificarVencimentoNic(cdAit));
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/report")
	@ApiOperation(value = "Fornece lista de AIT", notes = "# Tipos \r\n" + "## Situacao \r\n"
			+ "| `stAit` | Valor      |\r\n" + "|---------|------------|\r\n" + "| 0       | Cancelado  |\r\n"
			+ "| 1       | Confirmado |\r\n" + "| 2       | Pendente   |")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "AIT encontrado"),
			@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	@Produces("application/pdf")
	public Response retrieveReport(
			@ApiParam(value = "Identificador do AIT") @QueryParam("ait") String idAit,
			@ApiParam(value = "Inicio do periodo de autuacao (dd/mm/yyyy)") @QueryParam("inicioAutuacao") String dtInfracaoInicial,
			@ApiParam(value = "Fim do periodo de autuacao (dd/mm/yyyy)") @QueryParam("fimAutuacao") String dtInfracaoFinal,
			@ApiParam(value = "Numero da placa") @QueryParam("placa") String nrPlaca,
			@ApiParam(value = "Numero do RENAVAN") @QueryParam("renavan") String nrRenavan,
			@ApiParam(value = "Nome de proprietario") @QueryParam("proprietario") String nmProprietario,
			@ApiParam(value = "CPF proprietario") @QueryParam("cpfProprietario") String nrCpfProprietario,
			@ApiParam(value = "Nome do condutor") @QueryParam("condutor") String nmCondutor,
			@ApiParam(value = "CNH do condutor") @QueryParam("cnhCondutor") String nrCnhCondutor,
			@ApiParam(value = "Situacao do AIT", allowableValues = "0, 1, 2") @QueryParam("situacao") @DefaultValue("-1") int stAit,
			@ApiParam(value = "Codigo de Infracao (DETRAN)") @QueryParam("codigoInfracao") @DefaultValue("0") int nrCodDetran,
			@ApiParam(value = "Numero de matricula do agente autuador") @QueryParam("matriculaAgente") String nrMatriculaAgente,
			@ApiParam(value = "Numero de processo (JARI)") @QueryParam("numeroProcesso") String nrProcesso,
			@ApiParam(value = "Numero de controle interno (DETRAN)") @QueryParam("numeroControle") @DefaultValue("0") int nrControle,
			@ApiParam(value = "Local da infracao") @QueryParam("local") String dsLocalInfracao,

			@ApiParam(value = "Busca em multiplos campos por esta palavra chave") @QueryParam("keyword") String keyword,
			@ApiParam(value = "Quantidade maxima de registros", defaultValue = "100") @DefaultValue("100") @QueryParam("limit") int limit,
			@ApiParam(value = "Busca os ultimos registros inseridos na base") @QueryParam("last") String last) {
		try {

			Criterios crt = new Criterios();
//			crt.add("qtLimite", Integer.toString(limit), ItemComparator.EQUAL, Types.INTEGER);

			if (last != null && !last.equals("false")) {
				crt.add("last", "true", ItemComparator.EQUAL, Types.BOOLEAN);
			}

			if (keyword != null) {
				crt.add("keyword", keyword, ItemComparator.EQUAL, Types.VARCHAR);
			}

			if (idAit != null && !idAit.equals("")) {
				crt.add("A.id_ait", idAit, ItemComparator.EQUAL, Types.VARCHAR);
			}

			if (dtInfracaoInicial != null) {
				crt.add("A.dt_infracao", dtInfracaoInicial + " 00:00:00", ItemComparator.GREATER_EQUAL,
						Types.TIMESTAMP);
			}

			if (dtInfracaoFinal != null) {
				crt.add("A.dt_infracao", dtInfracaoFinal + " 23:59:59", ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			} else if (dtInfracaoInicial != null) {
				crt.add("A.dt_infracao", dtInfracaoInicial + " 23:59:59", ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			}

			// if(nrPlaca != null && Util.validarPlaca(nrPlaca)) { //validacao de placa
			// falhando
			if (nrPlaca != null && !nrPlaca.equals("")) {
				crt.add("A.nr_placa", nrPlaca, ItemComparator.EQUAL, Types.VARCHAR);
			}

			if (nrRenavan != null && !nrRenavan.equals("")) {
				crt.add("A.nr_renavan", nrRenavan, ItemComparator.EQUAL, Types.VARCHAR);
			}

			if (nmProprietario != null && !nmProprietario.equals("")) {
				crt.add("A.nm_proprietario", nmProprietario, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if (nrCpfProprietario != null && !nrCpfProprietario.equals("")) {
				crt.add("A.nr_cpf_proprietario", nrCpfProprietario, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if (nmCondutor != null && !nmCondutor.equals("")) {
				crt.add("A.nm_condutor", nmCondutor, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if (nrCnhCondutor != null && !nrCnhCondutor.equals("")) {
				crt.add("A.nr_cnh_condutor", nrCnhCondutor, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if (stAit > -1) {
				crt.add("A.st_ait", Integer.toString(stAit), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (nrCodDetran > 0) {
				crt.add("B.nr_cod_detran", Integer.toString(nrCodDetran), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (nrMatriculaAgente != null && !nrMatriculaAgente.equals("")) {
				crt.add("J.nr_matricula", nrMatriculaAgente, ItemComparator.LIKE_BEGIN, Types.VARCHAR);
			}

//			if(nrProcesso != null && !nrProcesso.equals("")) {
//				crt.add("K.nr_processo", nrProcesso, ItemComparator.LIKE, Types.VARCHAR);
//			}

			if (nrControle > 0) {
				crt.add("A.nr_controle", Integer.toString(nrControle), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (dsLocalInfracao != null) {
				crt.add("A.ds_local_infracao", dsLocalInfracao, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			byte[] report = AitReportServices.getAitReport(crt, null);

			return ResponseFactory.ok(report);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	// ======================

	@DELETE
	@Path("/{id}")
	@ApiOperation(
		value = "Apaga um AIT",
		notes = "Considere id = cdAit"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AIT apagado"),
		@ApiResponse(code = 405, message = "Nao e permitido apagar um AIT")
	})
	public Response delete(@ApiParam(value = "Id do AIT a ser apagado", required = true) @PathParam("id") int cdAit) {
		return ResponseFactory.methodNotAllowed("Nao e permitido apagar um AIT");
	}

	/*
	 * ************************************************************************ RRD
	 */
	@POST
	@Path("/{id}/rrds")
	@ApiOperation(value = "Registra um novo RRD")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "RRD registrado"),
			@ApiResponse(code = 400, message = "RRD possui algum parametro invalido"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.") })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createRrd(
			@ApiParam(value = "Id do AIT a ser apagado", required = true) @PathParam("id") int cdAit,
			@ApiParam(value = "RRD a ser registrado", required = true) Rrd rrd) {
		try {
			rrd.setCdRrd(0);
			Result r = RrdServices.sync(rrd, null);
			if (r.getCode() < 0) {
				return ResponseFactory.badRequest("RRD possui algum parametro invalido", r.getMessage());
			}

			rrd.setCdRrd(r.getCode());

			return ResponseFactory.ok(rrd);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@PUT
	@Path("/{id}/rrds/{idRrd}")
	@ApiOperation(value = "Edita um RRD")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "RRD atualizado"),
			@ApiResponse(code = 400, message = "RRD possui algum parametro invalido"),
			@ApiResponse(code = 204, message = "RRD nao encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.") })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRrd(
			@ApiParam(value = "Id do AIT", required = true) @PathParam("id") int cdAit,
			@ApiParam(value = "Id do RRD", required = true) @PathParam("idRrd") int cdRrd,
			@ApiParam(value = "RRD a ser atualizado", required = true) Rrd rrd) {
		try {
			if (cdRrd == 0)
				return ResponseFactory.noContent("RRD nao encontrado");

			rrd.setCdRrd(cdRrd);
			Result r = RrdServices.save(rrd);
			if (r.getCode() < 0) {
				return ResponseFactory.badRequest("RRD possui algum parametro invalido", r.getMessage());
			}
			return ResponseFactory.ok(r.getObjects().get("Rrd"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("{id}/rrds")
	@ApiOperation(value = "Lista RRDs de um AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "RRD atualizado"),
			@ApiResponse(code = 400, message = "RRD possui algum parametro invalido"),
			@ApiResponse(code = 204, message = "RRD nao encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.") })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response retrieveRrd(@ApiParam(value = "Id do AIT", required = true) @PathParam("id") int cdAit) {
		try {

			ResultSetMap rsm = RrdServices.getAllByAit(cdAit, null);
			if (rsm == null || !rsm.next()) {
				return ResponseFactory.noContent("Nenhum resultado");
			}

			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@DELETE
	@Path("{id}/rrds/{idRrd}")
	@ApiOperation(value = "Apaga um RRD")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "RRD apagado"),
			@ApiResponse(code = 400, message = "RRD possui algum parametro invalido"),
			@ApiResponse(code = 204, message = "RRD nao encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.") })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteRrd(
			@ApiParam(value = "Id do AIT", required = true) @PathParam("id") int cdAit,
			@ApiParam(value = "Id do RRD", required = true) @PathParam("idRrd") int cdRrd) {
		try {
			Result r = RrdServices.remove(cdRrd);
			if (r.getCode() < 0) {
				return ResponseFactory.internalServerError(r.getMessage());
			}

			return ResponseFactory.ok("RRD apagado");
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}

	}

	/*
	 * ************************************************************************
	 * TRRAV
	 */
	@POST
	@Path("/{id}/trravs")
	@ApiOperation(value = "Registra um novo TRRAV")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TRRAV registrado"),
			@ApiResponse(code = 400, message = "TRRAV possui algum parametro invalido"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.") })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTrrav(
			@ApiParam(value = "Id do AIT a ser apagado", required = true) @PathParam("id") int cdAit,
			@ApiParam(value = "TRRAV", required = true) Trrav trrav) {
		try {
			trrav.setCdTrrav(0);

			Result r = TrravServices.sync(trrav, null);
			if (r.getCode() < 0) {
				return ResponseFactory.badRequest("TRRAV possui algum parametro invalido", r.getMessage());
			}

			trrav.setCdTrrav(r.getCode());

			return ResponseFactory.ok(trrav);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@PUT
	@Path("/{id}/trravs/{idTrrav}")
	@ApiOperation(value = "Edita um TRRAV")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TRRAV atualizado"),
			@ApiResponse(code = 400, message = "TRRAV possui algum parametro invalido"),
			@ApiResponse(code = 204, message = "TRRAV nao encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.") })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTrrav(
			@ApiParam(value = "Id do AIT", required = true) @PathParam("id") int cdAit,
			@ApiParam(value = "Id do TRRAV", required = true) @PathParam("idTrrav") int cdTrrav,
			@ApiParam(value = "TRRAV a ser atualizado", required = true) Trrav trrav) {
		try {
			if (cdTrrav == 0)
				return ResponseFactory.noContent("TRRAV nao encontrado");

			trrav.setCdTrrav(cdTrrav);
			Result r = TrravServices.save(trrav);
			if (r.getCode() < 0) {
				return ResponseFactory.badRequest("TRRAV possui algum parametro invalido", r.getMessage());
			}
			return ResponseFactory.ok(r.getObjects().get("TRRAV"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("{id}/trravs")
	@ApiOperation(value = "Lista TRRAVs de um AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TRRAV atualizado"),
			@ApiResponse(code = 400, message = "TRRAV possui algum parametro invalido"),
			@ApiResponse(code = 204, message = "TRRAV nao encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.") })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response retrieveTrrav(@ApiParam(value = "Id do AIT", required = true) @PathParam("id") int cdAit) {
		try {

			ResultSetMap rsm = TrravServices.getAllByAit(cdAit, null);
			if (rsm == null || !rsm.next()) {
				return ResponseFactory.noContent("Nenhum resultado");
			}

			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@DELETE
	@Path("{id}/trravs/{idTrrav}")
	@ApiOperation(value = "Apaga um TRRAV")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TRRAV apagado"),
			@ApiResponse(code = 400, message = "TRRAV possui algum parametro invalido"),
			@ApiResponse(code = 204, message = "TRRAV nao encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.") })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTrrav(
			@ApiParam(value = "Id do AIT", required = true) @PathParam("id") int cdAit,
			@ApiParam(value = "Id do TRRAV", required = true) @PathParam("idTrrav") int cdTrrav) {
		try {
			Result r = TrravServices.remove(cdTrrav);
			if (r.getCode() < 0) {
				return ResponseFactory.internalServerError(r.getMessage());
			}

			return ResponseFactory.ok("TRRAV apagado");
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}

	}

	@GET
	@Path("/findemissao")
	@ApiOperation(value = "Gera arquivos de impressao de NAI", notes = "impressão de nai")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "AIT Encontrado"),
			@ApiResponse(code = 204, message = "Nao ha AIT com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public AitFindEmissao findEmissao(
			@ApiParam(value = "idAit") @QueryParam("idAit") String idAit
		) {
		try 
		{		
			
			return AitReportServices.findeEmissao(idAit);

		} catch (Exception e) {
			return null;
		}
	}

	@GET
	@Path("/{id}/emitir/aitnic")
	@ApiOperation(value = "Emiti o AIT de nic")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "AIT de NIC gerado", response = Object[].class),
			@ApiResponse(code = 204, message = "AIT de NIC não gerado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response emitirAitNic(
				@ApiParam(value = "id do AIT") @PathParam("id") int cdAit,
				@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario
			) 
	{

		try 
		{
			AitReportServicesNic servicesNic = new AitReportServicesNic();
			String cdNic = servicesNic.gerarAitNic(cdAit, cdUsuario);
			return ResponseFactory.ok(String.valueOf(cdNic));
		} catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@SuppressWarnings("static-access")
	@GET
	@Path("/{id}/verificarperiodopandemia/nai")
	@ApiOperation(value = "Verifica se a nip gerada esta vencida", notes = "Verificação de vencimento nip")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces("application/json")
	public Response verificarPeriodoPandemiaNai (@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) throws ValidacaoException 
	{
		try 
		{		
			JSONObject response = new JSONObject();
			AitReportValidatorsNAI validatorsNai = new AitReportValidatorsNAI();
			Ait ait = AitDAO.get(cdAit);
			response.put("code", 1);
			response.put("blob", validatorsNai.verificarPeriodoPandemia(ait));
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{id}/impressoespandemia/nai/{idRelatorio}")
	@ApiOperation(value = "Gera arquivos de impressao de NAI", notes = "impressão de nai")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response getPrintNaiPandemia(
			@ApiParam(value = "id do AIT") @PathParam("id") int cdAit,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario
		) {
		try 
		{		
			JSONObject response = new JSONObject();
			response.put("code", 1);
			response.put("blob", AitReportServices.getNaiPandemia(cdAit, cdUsuario));
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}

	@GET
	@Path("/{id}/reimpressao/nic/")
	@ApiOperation(value = "Imprime a segunda via de NIP com os dados originais da primeira", notes = "Reimpressão de nip")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response reimpressaoDadosNic (@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) throws ValidacaoException 
	{
		try 
		{		
			JSONObject response = new JSONObject();

			response.put("code", 1);
			response.put("blob", AitReportServices.reimpressaoDadosNic(cdAit));
			return ResponseFactory.ok(response);

		} catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		}

		catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/idequipamento/{id}/")
	@ApiOperation(value = "Busca o id do equipamento da infração", notes = "Busca o id do equipamento da infração")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "ID encontrado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces("application/json")
	public Response getIdEquipamento (@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) throws ValidacaoException 
	{
		try 
		{		
			AitReportServices reportServices = new AitReportServices();
			return ResponseFactory.ok(reportServices.getIdEquipamento(cdAit));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/postagem/documentosnaoentregue/")
	@ApiOperation(value = "Registra e faz a publicação de documentos não entregues no Diário Oficial")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Não ha arquivos para os parametros fornecidos."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public Response postagemDocumentosNaoEntregues
	(
			@ApiParam(value = "Inicio do periodo (dd/mm/yyyy)", required = true) @QueryParam("dtInicial") String inicialAit,
			@ApiParam(value = "Fim do periodo (dd/mm/yyyy)", required = true) @QueryParam("dtFinal") String finalAit,
			@ApiParam(value = "Data para publicação (dd/mm/yyyy)", required = true) @QueryParam("dtPublicacao") String dtPublicacao,
			@ApiParam(value = "Tipo do Ait", required = true) @QueryParam("tpAit") int tpAit) {
		AitMovimentoServices movimentoServices = new AitMovimentoServices();
		GregorianCalendar dtInicialAit = inicialAit != null ? Util.convStringToCalendar(inicialAit + " 00:00:00")
				: Util.convStringToCalendar("01/01/2000 00:00:00");
		GregorianCalendar dtFinalAit = finalAit != null ? Util.convStringToCalendar(finalAit + " 23:59:59")
				: new GregorianCalendar();
		GregorianCalendar dtPublicacaoAit = dtPublicacao != null ? Util.convStringToCalendar(dtPublicacao)
				: new GregorianCalendar();
		try {
			AitReportServices reportServices = new AitReportServices();
			AitServices aitServices = new AitServices();
			byte[] out = null;
			if (tpAit == AitMovimentoServices.NAI_ENVIADO) {
				ResultSetMap documentosNaoEntregues = aitServices.buscarDocumentosNaoEntregues(dtInicialAit, dtFinalAit,
						AitMovimentoServices.NAI_ENVIADO);
				out = reportServices.gerarEditalAitsNaoEntregues(tpAit, documentosNaoEntregues);
				movimentoServices.inserirPostagemMovimento(documentosNaoEntregues, AitMovimentoServices.NAI_ENVIADO,
						dtPublicacaoAit);
			} else if (tpAit == AitMovimentoServices.NIP_ENVIADA) {
				ResultSetMap documentosNaoEntregues = aitServices.buscarDocumentosNaoEntregues(dtInicialAit, dtFinalAit,
						AitMovimentoServices.NIP_ENVIADA);
				out = reportServices.gerarEditalAitsNaoEntregues(tpAit, documentosNaoEntregues);
				movimentoServices.inserirPostagemMovimento(documentosNaoEntregues, AitMovimentoServices.NIP_ENVIADA,
						dtPublicacaoAit);
			}
			JSONObject response = new JSONObject();
			response.put("blob", out);
			return ResponseFactory.ok(response);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.",
					e.getLocalizedMessage());
		}
	}

	@GET
	@Path("/{id}/impressoes/ait")
	@ApiOperation(value = "Gera arquivos de impressao de NAI", notes = "impressão de nai")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response getPrintSegundaViaAit(
			@ApiParam(value = "id do AIT") @PathParam("id") int cdAit
		) {
		try 
		{		
			JSONObject response = new JSONObject();

			response.put("code", 1);
			response.put("blob", AitReportServices.getSegundaViaAIT(cdAit));
			return ResponseFactory.ok(response);

		} catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}

	@GET
	@Path("/impressao/{id}")
	@ApiOperation(value = "Imprime o AIT com os dados presentes na tela do eTransito ADM", notes = "Reimpressão de AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response reimpressaoAit (@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) throws ValidacaoException 
	{
		try {

			return ResponseFactory.ok(AitReportServices.getImpressaoAit(cdAit));

		} catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/efeitosuspensivo")
	@ApiOperation(value = "Lista os autos com base nos filtros para efeito suspensivo")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Autos encontrados"),
			@ApiResponse(code = 204, message = "Não há auto"), @ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces(MediaType.APPLICATION_JSON)
	public Response findEfeitoSuspensivo(@ApiParam(value = "Id do AIT") @QueryParam("idAit") String idAit,
			@ApiParam(name = "Período inicial da data de entrada do recurso", value = "Data Inicial de entrada do recurso") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(name = "Período final da data de entrada do recurso", value = "Data Final de entrada do recurso") @QueryParam("dtFinal") String dtFinal,
			@ApiParam(name = "Tipo de status", allowableValues = "10, 51") @DefaultValue("-2") @QueryParam("tpStatus") int tpStatus,
			@ApiParam(value = "Número do documento") @QueryParam("nrDocumento") String nrDocumento,
			@QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosLikeAnyString("A.id_ait", idAit, idAit != null);
			searchCriterios.addCriteriosLikeAnyString("D.nr_documento", nrDocumento, nrDocumento != null);
			searchCriterios.addCriteriosGreaterDate("B.dt_movimento", dtInicial, dtInicial != null);
			searchCriterios.addCriteriosMinorDate("B.dt_movimento", dtFinal, dtFinal != null);
			searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus, tpStatus >= AitMovimentoServices.CADASTRO_CANCELADO);
			searchCriterios.addCriteriosEqualInteger("B.tp_status", tpStatus, tpStatus >= AitMovimentoServices.CADASTRO_CANCELADO);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			Search<EfeitoSuspensivoDTO> search = aitService.findEfeitoSuspensivo(searchCriterios);
			return ResponseFactory.ok(new AitEfeitoSuspensivoDTOListBuilder(search.getList(EfeitoSuspensivoDTO.class), search.getRsm().getTotal()).build());
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	

	@POST
	@Path("/efeitosuspensivo")
	@ApiOperation(value = "Lista de autos para serem suspensos")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Autos atualizados"),
			@ApiResponse(code = 400, message = "Algum parametro invalido"),
			@ApiResponse(code = 204, message = "Auto nao encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.") })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMovimentoEfeitoSuspensivo(@ApiParam(value = "Lista de autos a serem atualizados", required = true) List<EfeitoSuspensivoDTO> efeitoSuspensivoListDTO) {
		try {
			return ResponseFactory.ok(aitService.SuspenderInfracao(efeitoSuspensivoListDTO));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
}
