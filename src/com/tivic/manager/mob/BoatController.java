package com.tivic.manager.mob;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import javax.servlet.ServletContext;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.ImagemServices;
import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.report.ReportServices;
import com.tivic.sol.response.ResponseFactory;

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

@Api(value = "BOAT", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/boats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON) 
public class BoatController {

    @Context ServletContext servletContext;
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo BOAT"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "BOAT registrado"),
			@ApiResponse(code = 400, message = "BOAT"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
		})
	public static Response create(@ApiParam(value = "BOAT", required = true) Boat boat) throws ValidationException {
		try {			
			Result result;
			if(boat.getDeclarante()==null) {
				result = BoatServices.save(boat);
			} else {
				result = BoatServices.saveDAT(boat, boat.getDeclarante(), boat.getBoatDeclarante(), boat.getDeclaranteEndereco());
			}
			
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.ok(result.getObjects().get("BOAT"));
		} catch(BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		}  catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza BOAT"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "BOAT atualizado"),
			@ApiResponse(code = 400, message = "BOAT inválido"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
		})
	public static Response update(
			@ApiParam(value = "Id do BOAT a ser atualizado", required = true) @PathParam("id") int cdBoat, 
			@ApiParam(value = "BOAT a ser atualizado", required = true) Boat boat) {
		try {
			if(cdBoat == 0)
				return ResponseFactory.noContent("Nenhum BOAT encontrado.");
			
			boat.setCdBoat(cdBoat);
			
			Result result = BoatServices.save(boat);
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.ok(result.getObjects().get("BOAT"));			
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Fornece lista de BOAT"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "BOAT encontrado"),
		@ApiResponse(code = 204, message = "Sem resultados"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response retrieveAll(
			@ApiParam(value = "Quantidade máxima de registros por página", defaultValue = "100") @DefaultValue("100") @QueryParam("limit") int limit,
			@ApiParam(value = "Situação do BOAT") @DefaultValue("-1") @QueryParam("page") int page,
			@ApiParam(value = "Nº do BOAT") @QueryParam("boat") int nrBoat,
			@ApiParam(value = "Nº da placa") @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(value = "Ind. do protocolo") @QueryParam("nrProtocolo") String nrProtocolo,
			@ApiParam(value = "Dt. da ocorrencia") @QueryParam("dtOcorrencia") String dtOcorrencia,
			@ApiParam(value = "Dt. da declaração") @QueryParam("dtComunicacao") String dtComunicacao,
			@ApiParam(value = "Nº CPF do declarante") @QueryParam("nrCpfDeclarante") String nrCpfDeclarante,
			@ApiParam(value = "Nome do declarante") @QueryParam("nmDeclarante") String nmDeclarante,
			@ApiParam(value = "E-mail do declarante") @QueryParam("nmEmail") String nmEmail,
			@ApiParam(value = "Descrição do local do acidente") @QueryParam("dsLocalidade") String dsLocalidade,
			@ApiParam(value = "Situação do BOAT") @QueryParam("stBoat") @DefaultValue("-1") int status,
			@ApiParam(value = "Situação do BOAT") @QueryParam("tipo") String tipo
	) {
		try {
			ResultSetMap rsm = new ResultSetMap();
			Criterios criterios = new Criterios();
			criterios.add("qtLimite", Integer.toString(limit), ItemComparator.EQUAL, Types.INTEGER);
			
			if(nrBoat > 0) 
				criterios.add("A.nr_boat", Integer.toString(nrBoat), ItemComparator.EQUAL, Types.INTEGER);
			
			if(page > 0) {
				criterios.add("qtDeslocamento", Integer.toString((limit * page) - limit), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(tipo != null && tipo.equals("dat")) {
				criterios.add("C.cd_declarante", "", ItemComparator.NOTISNULL, Types.INTEGER);
			}
			
			if(nrPlaca != null) {
				criterios.add("B.nr_placa", nrPlaca.toUpperCase(), ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(status >= 0) {
				criterios.add("A.st_boat", Integer.toString(status), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(nrProtocolo != null && !nrProtocolo.trim().equals("")) {
				criterios.add("A.nr_protocolo", nrProtocolo, ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			if(dtOcorrencia != null && !dtOcorrencia.trim().equals("")) {
				criterios.add("A.dt_ocorrencia", dtOcorrencia + " 00:00:00", ItemComparator.GREATER_EQUAL, Types.VARCHAR);
				criterios.add("A.dt_ocorrencia", dtOcorrencia + " 23:59:59", ItemComparator.MINOR_EQUAL, Types.VARCHAR);
			}
			
			if(dtComunicacao != null && !dtComunicacao.trim().equals("")) {
				criterios.add("A.dt_comunicacao", dtComunicacao + " 00:00:00", ItemComparator.GREATER_EQUAL, Types.VARCHAR);
				criterios.add("A.dt_comunicacao", dtComunicacao + " 23:59:59", ItemComparator.MINOR_EQUAL, Types.VARCHAR);
			}
			
			if(nrCpfDeclarante != null && !nrCpfDeclarante.trim().equals("")) {
				criterios.add("D.nr_cpf", nrCpfDeclarante, ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			if(nmDeclarante != null && !nmDeclarante.trim().equals("")) {
				criterios.add("D.nm_declarante", nmDeclarante, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(dsLocalidade != null && !dsLocalidade.trim().equals("")) {
				criterios.add("A.ds_local_ocorrencia", dsLocalidade, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(nmEmail != null && !nmEmail.trim().equals("")) {
				criterios.add("D.nm_email", nmEmail, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			rsm = BoatServices.find(criterios);
			
			if(!rsm.next())
				return ResponseFactory.noContent("Sem resultados");
			
			if(page > 0) {
				return ResponseFactory.ok(new EDatDTO.ListBuilder(rsm, rsm.getTotal()).build());
			}
			
			return ResponseFactory.ok(rsm);	
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
		value = "Fornece um BOAT dado o id indicado",
		notes = "Considere id = cdAit"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "BOAT encontrado"),
		@ApiResponse(code = 204, message = "Não existe BOAT com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response retrieveById(@ApiParam(value = "id do BOAT") @PathParam("id") int cdBoat) {
		Connection connect = Conexao.conectar();
		try {
			Criterios criterios = new Criterios()
					.add("A.cd_boat", Integer.toString(cdBoat), ItemComparator.EQUAL, Types.INTEGER);
			ResultSetMap rsm = BoatServices.find(criterios, connect);
			if(!rsm.next())
				return ResponseFactory.noContent("Sem resultados");
			EDatDTO eDatDTO = new EDatDTO.Builder(rsm.getRegister(), connect).build();
			return ResponseFactory.ok(eDatDTO);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(
		value = "Apaga um BOAT"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "BOAT apagado"),
		@ApiResponse(code = 405, message = "Não é permitido apagar um BOAT")
	})
	public static Response delete(@ApiParam(value = "Id do BOAT a ser apagado", required = true) @PathParam("id") int cdBoat) {
		return ResponseFactory.methodNotAllowed("Não é permitido apagar um BOAT");
	}
		
	@GET
	@Path("/{id}/imagem")
	@ApiOperation(
		value = "Fornece um BOAT dado o id indicado",
		notes = "Considere id = cdAit"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "BOAT encontrado"),
		@ApiResponse(code = 204, message = "Não existe BOAT com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@JWTIgnore
	@Produces({"image/jpeg", "image/png"})
	public Response retrieveImageByBoat(
			@ApiParam(value = "id do BOAT") @PathParam("id") int cdBoat,
			@ApiParam(value = "Id do Veículo") @QueryParam("veiculo") int cdBoatVeiculo,
			@ApiParam(value = "Id da Imagem") @QueryParam("tpPosicao") int tpPosicao
			
		) throws FileNotFoundException {
		try {
			Connection connect = Conexao.conectar();
			Criterios criterios = new Criterios().add("cd_boat", Integer.toString(cdBoat), ItemComparator.EQUAL, Types.INTEGER);			
			criterios.add("tp_posicao", Integer.toString(tpPosicao), ItemComparator.EQUAL, Types.INTEGER);
			
			if(cdBoatVeiculo > 0) {
				criterios.add("cd_boat_veiculo", Integer.toString(cdBoatVeiculo), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			ResultSetMap rsm = BoatImagemServices.find(criterios, connect);
			String noImage = servletContext.getRealPath("/imagens/placeholder-image.png");

			if(!rsm.next()) {
				byte[] bytes = Files.readAllBytes(Paths.get(noImage));
				return Response.ok(bytes).build();
			}
			
			byte[] file = (byte[]) rsm.getRegister().get("BLB_IMAGEM");
						
			return Response.ok(file).build();
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}/deferir")
	@ApiOperation(
		value = "Analisa um eDAT dado o id indicado",
		notes = "Considere id = cdBoat"
	)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "BOAT encontrado"),
		@ApiResponse(code = 204, message = "Não existe eDAT com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response deferir(
			@ApiParam(value = "id do BOAT") @PathParam("id") int cdBoat,
			@ApiParam(value = "Boat a ser analisado") EDatDTO edat
		) throws SQLException {
		
		Connection connect = Conexao.conectar();
		try {		
			connect.setAutoCommit(false);
			if(edat.getStBoat() == BoatServices.ST_DEFERIDA || edat.getStBoat() == BoatServices.ST_INDEFERIDA) {
				return ResponseFactory.badRequest("Esta declaração já foi analisada.");
			}
			
			if(edat.getCdAgente() == 0 || AgenteDAO.get(edat.getCdAgente(), connect) == null) {
				return ResponseFactory.badRequest("Não foi possível reconhecer o agente validador.");
			}
			Result result = BoatServices.deferir(edat, connect);
			
			connect.commit();
			
			if(result.getCode() <= 0) {
				return ResponseFactory.badRequest(result.getMessage());				
			}
			return ResponseFactory.ok(result);
		} catch(Exception e) {
			connect.rollback();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	
	@PUT
	@Path("/{id}/indeferir")
	@ApiOperation(
		value = "Indefere um eDAT dado o id indicado",
		notes = "Considere id = cdBoat"
	)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "BOAT encontrado"),
		@ApiResponse(code = 204, message = "Não existe eDAT com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response indeferir(
			@ApiParam(value = "id do BOAT") @PathParam("id") int cdBoat,
			@ApiParam(value = "Boat a ser analisado") EDatDTO edat
		) throws SQLException {
		try {

			if(edat.getStBoat() == BoatServices.ST_DEFERIDA || edat.getStBoat() == BoatServices.ST_INDEFERIDA) {
				return ResponseFactory.badRequest("Esta solicitação já foi analisada.");
			}
			
			if(edat.getInconsistencias() == null || edat.getInconsistencias().size() == 0) {
				return ResponseFactory.badRequest("É necessário informar as inconsistências da declaração.");
			}
			
			if(edat.getTxtDescricaoSumaria() == null || edat.getTxtDescricaoSumaria().trim().equals("")) {
				return ResponseFactory.badRequest("É necessário justificar o indeferimento.");
			}
			
			if(edat.getCdAgente() == 0 || AgenteDAO.get(edat.getCdAgente(), null) == null) {
				return ResponseFactory.badRequest("Não foi possível reconhecer o Agente validador.");
			}
						
			Result result = BoatServices.indeferir(edat, edat.getInconsistencias(), null);
			
			if(result.getCode() <= 0) {
				return ResponseFactory.badRequest(result.getMessage());				
			}
			
			return ResponseFactory.ok(result);
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}/correcao")
	@ApiOperation(
		value = "Analisa um eDAT dado o id indicado",
		notes = "Considere id = cdBoat"
	)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "BOAT encontrado"),
		@ApiResponse(code = 204, message = "Não existe eDAT com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response correcao(
			@ApiParam(value = "id do BOAT") @PathParam("id") int cdBoat,
			@ApiParam(value = "Boat a ser analisado") EDatDTO edat
		) throws SQLException {		
		try {
						
			if(edat.getStBoat() != BoatServices.ST_EM_ANALISE) {
				return ResponseFactory.badRequest("Só é possível solicitar correções de declarações na situação Em Análise.");
			}
			
			if(edat.getInconsistencias() == null || edat.getInconsistencias().size() == 0) {
				return ResponseFactory.badRequest("É necessário informar as inconsistências da declaração.");
			}
			
			if(edat.getTxtDescricaoSumaria() == null || edat.getTxtDescricaoSumaria().trim().equals("")) {
				return ResponseFactory.badRequest("É necessário justificar o indeferimento.");
			}
			if(edat.getCdAgente() == 0 || AgenteDAO.get(edat.getCdAgente(), null) == null) {
				return ResponseFactory.badRequest("Não foi possível reconhecer o Agente validador.");
			}
			
			Result result = BoatServices.solicitarCorrecao(edat, edat.getInconsistencias(), null);

			if(result.getCode() <= 0) {
				return ResponseFactory.badRequest(result.getMessage());				
			}
			
			return ResponseFactory.ok(result);
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}/relacionar")
	@ApiOperation(
		value = "Analisa um eDAT dado o id indicado",
		notes = "Considere id = cdBoat"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "BOAT encontrado"),
		@ApiResponse(code = 204, message = "Não existe eDAT com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response relacionar(
			@ApiParam(value = "id do BOAT") @PathParam("id") int cdBoat,
			@ApiParam(value = "id a ser relacionado") @QueryParam("nrBoatRelacao") int nrBoat,
			@ApiParam(value = "id a ser relacionado") @QueryParam("idAgente") int cdAgente
		) {
		try {			
			Result result = BoatServices.relacionarBoat(cdBoat, nrBoat, cdAgente, null);
			
			if(result.getCode() <= 0) {
				return ResponseFactory.badRequest(result.getMessage());				
			}
			
			return ResponseFactory.ok(result);
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/validar")
	@ApiOperation(
			value = "Apaga um BOAT"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Validade verificada"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Response validarHash(
			@ApiParam(value = "No. Protocolo", required = true) @QueryParam("protocolo") String nrProtocolo, 
			@ApiParam(value = "No. Hash", required = true) @QueryParam("hash") String nrHash) {
		try {
			
			Result result = BoatServices.validarHash(nrProtocolo, nrHash);
			JSONObject json = new JSONObject();
			json.put("lgValido", result.getObjects().get("LG_VALIDO"));
			
			if(result.getCode() <= 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
						
			return ResponseFactory.ok(json);		
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/dat")
	@ApiOperation( value = "Fornece DAT" )
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "DAT encontrado"),
		@ApiResponse(code = 204, message = "Sem resultados"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response retrieveDat(
			@ApiParam(value = "No. do Protocolo", required = true) @QueryParam("protocolo") String nrProtocolo,
			@ApiParam(value = "No. do CPF do declarante", required = true) @QueryParam("cpf") String nrCpf) {
		Connection connect = Conexao.conectar();
		try {
			if(nrProtocolo == null || nrCpf == null) {
				return ResponseFactory.badRequest("Os parâmetros necessários não foram informados: nrProtocolo, nrCpf");
			}

			Criterios criterios = new Criterios();

			criterios.add("A.nr_protocolo", nrProtocolo, ItemComparator.LIKE_ANY, Types.VARCHAR);
			criterios.add("D.nr_cpf", nrCpf.replaceAll("[^0-9]", ""), ItemComparator.LIKE_ANY, Types.VARCHAR);
			ResultSetMap rsm = BoatServices.find(criterios);
			
			if(!rsm.next()) {
				return ResponseFactory.badRequest("Nenhuma declaração encontrada com os dados informados.");				
			}
			
			if((int)rsm.getRegister().get("ST_BOAT") == BoatServices.ST_DEFERIDA || (int)rsm.getRegister().get("ST_BOAT") == BoatServices.ST_INDEFERIDA) {
				return ResponseFactory.forbidden("Esta declaração já foi analisada, não é possível efetuar retificação");		
			}

			return ResponseFactory.ok(new EDatDTO.Builder(rsm.getRegister(), true, connect).build());	
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		} finally {
			Conexao.desconectar(connect);
		}
	}
	

	@GET
	@Path("/dat/impressao")
	@ApiOperation( value = "Fornece DAT" )
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "DAT encontrado"),
		@ApiResponse(code = 204, message = "Sem resultados"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response retrieveDatImpressao(
			@ApiParam(value = "No. do Protocolo", required = true) @QueryParam("protocolo") String nrProtocolo,
			@ApiParam(value = "No. do CPF do declarante", required = true) @QueryParam("cpf") String nrCpf) {
		try {
			ObjectMapper o = new ObjectMapper();
			Result result = BoatServices.get(nrProtocolo, nrCpf);
			
			if(result.getCode() <= 0)
				return ResponseFactory.noContent("Sem resultados");
			
			JSONObject json = new JSONObject();
			json.put("boat", new JSONObject(o.writeValueAsString(result.getObjects().get("boat"))));
			json.put("declarante", new JSONObject(o.writeValueAsString(result.getObjects().get("declarante"))));
			json.put("boatDeclarante", new JSONObject(o.writeValueAsString(result.getObjects().get("boatDeclarante"))));
			json.put("declaranteEndereco", new JSONObject(o.writeValueAsString(result.getObjects().get("declaranteEndereco"))));
			json.put("veiculos", ((ResultSetMap)result.getObjects().get("rsmVeiculo")).getLines());
			json.put("imagens", result.getObjects().get("arrayImagens"));
			json.put("hash", result.getObjects().get("nrHash"));
			

			json.put("jsonDeclaranteEndereco", result.getObjects().get("jsonDeclaranteEndereco"));
			json.put("jsonBoat", result.getObjects().get("jsonBoat"));
			

			return ResponseFactory.ok(json);	
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/hash")
	@ApiOperation(
			value = "Gera chave de verificação"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Chave gerada"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Response validarHash(@ApiParam(value = "Id do BOAT", required = true) @PathParam("id") int cdBoat) {
		try {
			
			Result result = BoatServices.getHmac(cdBoat);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			
			JSONObject json = new JSONObject();
			json.put("hash", result.getMessage());
			return ResponseFactory.ok(json);		
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	

	@GET
	@Path("/inconsistencias")
	@ApiOperation(
			value = "Gera BOAT para impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de inconsistências"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getInconsistencias(@ApiParam(value = "Tipo de Inconsistência", required = true) @QueryParam("tipo") int tpInconsistencia) {
		try {
			
			Criterios criterios = new Criterios();
			
			if(tpInconsistencia > 0) {
				criterios.add("tp_inconsistencia", Integer.toString(tpInconsistencia), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			ResultSetMap rsm = InconsistenciaServices.find(criterios);
									
			return ResponseFactory.ok(rsm);
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@POST
	@Path("/inconsistencias")
	@ApiOperation(
			value = "Cadastra um novo registro de Inconsistencia"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de inconsistências"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response adicionaInconsistencia(Inconsistencia inconsistencia) {
		try {									
			return ResponseFactory.ok(InconsistenciaServices.save(inconsistencia));			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@DELETE
	@Path("/inconsistencias/{id}")
	@ApiOperation(
			value = "Remove um registro de Inconsistencia"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de inconsistências"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response removeInconsistencia(@ApiParam(value = "Id da Inconsistência", required = true) @PathParam("id") int id) {
		try {									
			
			Result result = InconsistenciaServices.remove(id);
			
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest(result.getMessage());					
			}
			
			return ResponseFactory.ok(result.getMessage());			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/impresso")
	@ApiOperation(
			value = "Gera BOAT para impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces("application/pdf")
	public static Response getPrint(@ApiParam(value = "No. do Protocolo", required = true) @QueryParam("protocolo") String nrProtocolo) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Result result = BoatServices.imprimirReport(nrProtocolo);
			if(result != null) {
				String reportName =  "mob/relatorio_boat";
				ResultSetMap rsm  = (ResultSetMap) result.getObjects().get("rsm");
				
				HashMap<String, Object> paramns =  objectMapper.readValue(result.getObjects().get("jsonBoat").toString(), HashMap.class);
				paramns.put("LOGO_1", ImagemServices.getArrayBytesFromArrayList((ArrayList<Byte>)paramns.get("LOGO_1")));
				paramns.put("LOGO_2", ImagemServices.getArrayBytesFromArrayList((ArrayList<Byte>)paramns.get("LOGO_2")));
								
				byte[] print = ReportServices.getPdfReport(reportName, paramns, rsm);			
				
				return Response.ok(print).header("Content-Disposition", "inline; filename=" + nrProtocolo + ".pdf").build();
			} else {
				return ResponseFactory.internalServerError("Erro ao gerar arquivo.");
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/dat/impresso")
	@ApiOperation(
			value = "Gera DAT para impressão"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@Produces({"application/json"})
	public static Response getPrintDat(HashMap<String, Object> json) {
		try {
			String nrProtocolo = String.valueOf(json.get("protocolo"));
			String nrCpf = String.valueOf(json.get("cpf"));
			
			Result result = BoatServices.get(nrProtocolo, nrCpf);
			
			if(result.getCode() <= 0) {
				return ResponseFactory.badRequest("Não foi encontrada uma declaração com os dados informados.");
			}
						
			byte[] pdf = BoatServices.imprimirDAT(result.getObjects(), null);
			
			HashMap<String, Object> response = new HashMap<String, Object>();
			response.put("code", "success");
			response.put("file", Base64.getEncoder().encodeToString(pdf));
			
			return ResponseFactory.ok(response);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cidadao/impresso")
	@ApiOperation(
			value = "Gera BOAT para impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/json"})
	public static Response getPrintCidadao(
			@ApiParam(value = "No. do Protocolo", required = true) HashMap<String, Object> json
		) {
		try {			
			String nrProtocolo = String.valueOf(json.get("protocolo"));
			String nrCpf = String.valueOf(json.get("cpf"));
			
			Result result = BoatServices.get(nrProtocolo, nrCpf);
			Boat boat = (Boat) result.getObjects().get("boat");
			
			if(result.getCode() <= 0) {
				return ResponseFactory.badRequest("Não foi encontrada uma declaração com os dados informados.");
			}
			
			if(boat.getStBoat() != BoatServices.ST_DEFERIDA) {
				return ResponseFactory.badRequest("Esta declaração ainda não está disponível para impressão.");				
			}
			
			byte[] pdf = BoatServices.imprimirDAT(result.getObjects(), null);
			
			HashMap<String, Object> response = new HashMap<String, Object>();
			response.put("code", "success");
			response.put("file", Base64.getEncoder().encodeToString(pdf));
			
			return ResponseFactory.ok(response);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	
	}
	
	@POST
	@Path("/cidadao/impresso/protocolo")
	@ApiOperation(
			value = "Gera BOAT para impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/json"})
	public static Response getPrintProtocolo(
			@ApiParam(value = "No. do Protocolo", required = true) HashMap<String, Object> json
		) {
		try {			
			String nrProtocolo = String.valueOf(json.get("protocolo"));
			String nrCpf = String.valueOf(json.get("cpf"));
			
			Result result = BoatServices.get(nrProtocolo, nrCpf);
			
			if(result.getCode() <= 0) {
				return ResponseFactory.badRequest("Não foi encontrada uma declaração com os dados informados.");
			}
			
			byte[] pdf = BoatServices.imprimirProtocolo(result.getObjects(), null);
			
			HashMap<String, Object> response = new HashMap<String, Object>();
			response.put("code", "success");
			response.put("file", Base64.getEncoder().encodeToString(pdf));
			
			return ResponseFactory.ok(response);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	
	}
}

