package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDTO;
import com.tivic.manager.str.ait.veiculo.ConvertPlaca;
import com.tivic.manager.util.Util;
import com.tivic.sol.auth.jwt.JWTIgnore;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.Conexao;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "AIT", tags = {"str"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/str/aits")
@Produces(MediaType.APPLICATION_JSON)
public class AitController {
	
	@POST
	@Path("/auth")
	@JWTIgnore
	@ApiOperation(
			value = "Autentica um usuário",
			notes = "Autenticação baseada em 'Bearer Authentication' com [JWT](https://jwt.io/introduction/)"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Autenticado", response = com.tivic.sol.response.ResponseBody.class),
			@ApiResponse(code = 403, message = "Não autenticado"),
			@ApiResponse(code = 500, message = "Erro no servidor"),
			@ApiResponse(code = 501, message = "Recurso ainda não disponível")
	})
	public static Response auth(@ApiParam(value = "Dados para autenticação", required = true) CredencialVeiculo body) {
		try {
			
			
			String token = new AuthPortalService().authPortal(body.nrPlaca, body.nrRenavan);
			if(token == null) {
				return Response
						.status(Status.FORBIDDEN)
						.entity(new com.tivic.sol.response.ResponseBody(403, "Forbidden", "Não há registros relacionados aos dados fornecidos que permitam o acesso ao sistema."))
						.build();
			}
			
			return Response
					.status(Status.OK)
					.entity(new com.tivic.sol.response.ResponseBody(200, "Ok", token))
					.build();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		}
	}
	
	@POST
	@Path("/")
	@ApiOperation(
		value = "Registra um novo AIT"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AIT registrado"),
		@ApiResponse(code = 501, message = "Recurso ainda não disponível")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "AIT a ser registrado", required = true) Ait ait) {
		return Response
				.status(Status.NOT_IMPLEMENTED)
				.entity(new com.tivic.sol.response.ResponseBody(501, "Not Implemented", "Não é possível registrar um AIT."))
				.build();
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
		value = "Atualiza dados de um AIT",
		notes = "Considere id = cdAit"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AIT atualizado"),
		@ApiResponse(code = 400, message = "AIT é nulo ou inválido"),
		@ApiResponse(code = 204, message = "AIT não encontrado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id do AIT a ser atualizado", required = true) @PathParam("id") int cdAit, 
			@ApiParam(value = "AIT a ser atualizado", required = true) Ait ait) {
		
		try {
			if(ait == null) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Ait não existe na base de dados."))
						.build();
			}
			
			if(AitServices.get(cdAit, null) == null) {
				return Response
						.status(Status.NOT_FOUND)
						.entity(new com.tivic.sol.response.ResponseBody(404, "Not Found", "Ait não existe na base de dados."))
						.build();
			}
			
			ait.setCdAit(cdAit);
			
			int r = AitDAO.update(ait);
			if(r < 0) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Erro ao atualizar AIT."))
						.build();
			}
			
			return Response
					.status(Status.OK)
					.entity(new com.tivic.sol.response.ResponseBody(200, "Ok", "AIT atualizado."))
					.build();
		} catch(Exception e) {
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		}
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(
		value = "Apaga um AIT",
		notes = "Considere id = cdAit"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AIT apagado"),
		@ApiResponse(code = 405, message = "Não é permitido apagar um AIT")
	})
	public static Response delete(@ApiParam(value = "Id do AIT a ser atualizado", required = true) @PathParam("id") int cdAit) {
		return Response
				.status(Status.METHOD_NOT_ALLOWED)
				.entity(new com.tivic.sol.response.ResponseBody(405, "Method Not Allowed", "Não é possível excluir um AIT"))
				.build();
	}

	@GET
	@Path("/")
	@ApiOperation(
		value = "Fornece lista de AIT"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista fornecida"),
		@ApiResponse(code = 204, message = "Sem resultado"),
		@ApiResponse(code = 400, message = "Parâmetro nulo ou inválido"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response retrieve(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "100") @DefaultValue("100") @QueryParam("limit") int limit, 
			@ApiParam(value = "Placa do veículo autuado", required = true) @QueryParam("placa") String nrPlaca, 
			@ApiParam(value = "No. do AIT") @QueryParam("ait") String nrAit,
			@ApiParam(value = "No. do RENAVAM") @QueryParam("renavam") String nrRenavam) {
		
		try {
			
			// validar limit
			if(limit < 0 || limit > 100) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Limite máximo de 100 registros."))
						.build();
			}

			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			crt.add(new ItemComparator("qtLimite", Integer.toString(limit), ItemComparator.EQUAL, Types.INTEGER));
			
			// validar placa
			if(nrPlaca != null) {
				if(nrPlaca.length() < 7 || nrPlaca.length() > 8) {
					return Response
							.status(Status.BAD_REQUEST)
							.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Placa inválida."))
							.build();
				}
				
				nrPlaca = nrPlaca.replaceAll("-", "").toUpperCase();
				
				String placaBR = "^[A-Z]{3}[0-9]{4}$";
				String placaMercosul = "^[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}$";	
				
				if(!Pattern.matches(placaBR, nrPlaca) && !Pattern.matches(placaMercosul, nrPlaca)) {
					return Response
							.status(Status.BAD_REQUEST)
							.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Placa inválida."))
							.build();
				}
				String nrPlacaConvertida = ConvertPlaca.convertPlaca(nrPlaca);
				String placas = "'" + nrPlaca + "'" + "," + "'" + nrPlacaConvertida + "'";
				crt.add(new ItemComparator("A.nr_placa", placas, ItemComparator.IN, Types.VARCHAR));
			} else {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Placa inválida."))
						.build();
			}
			
			// ait
			if(nrAit != null) {				
				crt.add(new ItemComparator("A.nr_ait", nrAit, ItemComparator.EQUAL, Types.VARCHAR));
			}
			
			// renavam
			if(nrRenavam != null) {				
				crt.add(new ItemComparator("A.cd_renavan", nrRenavam, ItemComparator.EQUAL, Types.BIGINT));
			}
			
			ResultSetMap rsm = com.tivic.manager.mob.AitServices.find(crt);
			
			if(rsm.getLines().size() <= 0) {
				return Response
						.status(Status.NO_CONTENT)
						.build();
			}
			
			return Response
					.status(Status.OK)
					.entity(Util.rsm2Json(rsm).toString())
					.build();
		} catch(Exception e) {			
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		}	
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
		value = "Fornece um AIT dado o id indicado",
		notes = "Considere id = cdAit"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AIT encontrado"),
		@ApiResponse(code = 204, message = "Não existe AIT com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response retrieveById(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) {
		try {
						
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			if(Util.isStrBaseAntiga()) {
				crt.add(new ItemComparator("codigo_ait", Integer.toString(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			} else {
				crt.add(new ItemComparator("cd_ait", Integer.toString(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			}
			ResultSetMap rsm = com.tivic.manager.mob.AitServices.find(crt);
			
			if(!rsm.next()) {
				return Response
						.status(Status.NOT_FOUND)
						.entity(new com.tivic.sol.response.ResponseBody(404, "Not Found", "Não existe AIT com o id indicado."))
						.build();
			}
									
			return Response
					.status(Status.OK)
					.entity(Util.map2Json(rsm.getRegister()).toString())
					.build();
		} catch(Exception e) {			
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
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
		Connection connection = null;
		try {
			connection = Conexao.conectar();
			
//			if(!AitServices.hasAit(cdAit, connection)) {
//				return Response
//						.status(Status.BAD_REQUEST)
//						.entity(new com.tivic.manager.rest.Response(400, "Bad Request", "Não existe AIT com o id indicado."))
//						.build();
//			}
			
			ResultSetMap rsm = AitServices.getMovimentosByCdAit(cdAit, connection);
			
			if(rsm.getLines().size() <= 0) {
				return Response
						.status(Status.NO_CONTENT)
						.build();
			}
									
			return Response
					.status(Status.OK)
					.entity(Util.rsm2Json(rsm).toString())
					.build();
		} catch(Exception e) {			
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		} finally {
			Conexao.desconectar(connection);
		}
	}
	
	
	@POST
	@Path("/{id}/processos")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Registra um processo no AIT indicado",
			notes =   "# Tipos \r\n"
					+ "## Processo \r\n"
					+ "|  `tpProcesso` |                     | `stProcesso` |                        |\r\n" 
					+ "|---------------|---------------------|--------------|------------------------|\r\n" 
					+ "| **valor**     | **descrição**       | **valor**    | **descrição**          |\r\n" 
					+ "| 7             | Defesa Prévia       | 0            | Pendente               |\r\n" 
					+ "| 10            | Recurso JARI        | 1            | Em Julgamento          |\r\n" 
					+ "| 19            | Apresentar Condutor | 2            | Indeferido             |\r\n" 
					+ "| 33            | Reembolso           | 3            | Deferido               |\r\n" 
					+ "| 51            | Recurso CETRAN      | 4            | Deferido (Advertência) |\r\n"
					+ "| ---           | ---                 | 5            | Entregue       	     |\r\n"
					+ "## Requerente \r\n"
					+ "| `tpRequerente` |               |\r\n" 
					+ "|----------------|---------------|\r\n"  
					+ "| **valor**      | **descrição** |\r\n"  
					+ "| 1              | Proprietário  |\r\n"  
					+ "| 2              | Condutor      |\r\n"
					+ "---\r\n"
					+ "# Considere \r\n"
					+ "* Todos os processos registrados terão status \"Entregue\"\r\n"
					+ "* `nrProcesso` é gerado automaticamente com base no `tpProcesso`.\r\n"
					+ "* Lista de arquivos não é obrigatória\r\n"
					+ "* `id = cdAit` \r\n"
					+ "* O requerente é o proprietário do veículo. Em \"Apresentação de Condutor\", preencha também os campos correspondentes ao condutor. \r\n"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Processo registrado"),
			@ApiResponse(code = 400, message = "Processo inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response addProcessoAit(@ApiParam(value = "Id do AIT") @PathParam("id") int cdAit, @ApiParam(value = "Processo a ser registrado") ProcessoDTO processo) {
		try {			
			processo.setCdAit(cdAit);
			processo.setStProcesso(ProcessoServices.ST_PENDENTE);
			processo.setCdUsuario(ProcessoServices.CD_USUARIO_PORTAL);
			
			if(processo.getDtProcesso() == null) {
				processo.setDtProcesso(new GregorianCalendar());
			}
			
			List<Arquivo> arquivos = new ArrayList<Arquivo>();
			List<ArquivoDTO> dtoArquivos = processo.getArquivos();
			for (ArquivoDTO dtoArquivo : dtoArquivos) {				
				arquivos.add(dtoArquivo);
			}
			
			Result result = ProcessoServices.save(processo, arquivos, processo.getRequerente(), null, null);
			
			if(result.getCode() <= 0) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", result.getMessage()))
						.build();
			}
									
			return Response
					.status(Status.OK)
					.entity(processo)
					.build();
		} catch(Exception e) {			
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		}
	}
	
	@PUT
	@Path("/{id}/processos/{idProcesso}")
	@ApiOperation(
		value = "Atualiza dados de um Processo",
		notes = "# Tipos \r\n"
				+ "|  `tpProcesso` |                     | `stProcesso` |                        |\r\n" 
				+ "|---------------|---------------------|--------------|------------------------|\r\n" 
				+ "| **valor**     | **descrição**       | **valor**    | **descrição**          |\r\n" 
				+ "| 7             | Defesa Prévia       | 0            | Pendente               |\r\n" 
				+ "| 10            | Recurso JARI        | 1            | Em Julgamento          |\r\n" 
				+ "| 19            | Apresentar Condutor | 2            | Indeferido             |\r\n" 
				+ "| 33            | Reembolso           | 3            | Deferido               |\r\n" 
				+ "| 51            | Recurso CETRAN      | 4            | Deferido (Advertência) |\r\n"
				+ "| ---           | ---                 | 5            | Entregue       	     |\r\n"
				+ "---\r\n"
				+ "# Considere\r\n"
				+ "* Lista de arquivos não é obrigatória"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AIT atualizado"),
		@ApiResponse(code = 400, message = "Parâmetro é nulo ou inválido"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response updateProcesso(
			@ApiParam(value = "Id do AIT", required = true) @PathParam("id") int cdAit, 
			@ApiParam(value = "Id do processo a ser atualizado", required = true) @PathParam("idProcesso") int cdProcesso, 
			@ApiParam(value = "Processo a ser atualizado", required = true) Processo processo) {
		
		try {
			if(processo == null) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Processo não existe na base de dados."))
						.build();
			}
			
			if(ProcessoDAO.get(cdProcesso) == null) {
				return Response
						.status(Status.NOT_FOUND)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Processo não existe na base de dados."))
						.build();
			}
			
			processo.setCdProcesso(cdProcesso);
			
			Result r = ProcessoServices.save(processo);
			if(r.getCode() <= 0) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", r.getMessage()))
						.build();
			}
			
			return Response
					.status(Status.OK)
					.entity(new com.tivic.sol.response.ResponseBody(200, "Ok", "Processo atualizado."))
					.build();
		} catch(Exception e) {
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		}
	}
	
	@GET
	@Path("/{id}/processos")
	@ApiOperation(
		value = "Fornece lista de processos do AIT dado seu id",
		notes = "Considere id = cdAit"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Processos encontrados"),
		@ApiResponse(code = 204, message = "Nenhum processo"),
		@ApiResponse(code = 400, message = "AIT não existe"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAllProcessos(@ApiParam(value = "Id do AIT") @PathParam("id") int cdAit,
			@ApiParam(value = "Status do processo") @DefaultValue("-1") @QueryParam("status") int stProcesso) {
		Connection connection = null;
		try {
			connection = Conexao.conectar();
						
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			crt.add(new ItemComparator("A.cd_ait", Integer.toString(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			
			if(stProcesso >= 0) {
				crt.add(new ItemComparator("A.st_processo", Integer.toString(stProcesso), ItemComparator.EQUAL, Types.INTEGER));
			}
			
			ResultSetMap rsm = ProcessoServices.find(crt, connection);
			
			if(rsm.getLines().size() <= 0) {
				return Response
						.status(Status.NO_CONTENT)
						.build();
			}
									
			return Response
					.status(Status.OK)
					.entity(Util.rsm2Json(rsm).toString())
					.build();
		} catch(Exception e) {			
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		} finally {
			Conexao.desconectar(connection);
		}
	}
	
	@GET
	@Path("/{id}/processos/{idProcesso}/arquivos")
	@ApiOperation(
		value = "Fornece lista de arquivos do processo",
		notes = "# Considere\r\n"
			  + "* A lista de arquivos não possui os bytes. Para isso, acesse `/str/aits/{id}/processos/{idProcesso}/arquivos/{idArquivo}`."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Arquivos encontrados"),
		@ApiResponse(code = 204, message = "Nenhum arquivo"),
		@ApiResponse(code = 400, message = "Arquivo não existe"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAllProcessoArquivos(
			@ApiParam(value = "Id do AIT") @PathParam("id") int cdAit, 
			@ApiParam(value = "Id do Processo") @PathParam("idProcesso") int cdProcesso,
			@ApiParam(value = "Baixar arquivos") @QueryParam("download") boolean download) {
		Connection connection = null;
		byte[] zip = null;
		try {
			connection = Conexao.conectar();	
			
			ResultSetMap rsm = ProcessoServices.getArquivos(cdProcesso, connection);
			
			if(download) {
				zip = ProcessoServices.getArquivosZip(cdProcesso, connection);
				Processo processo = ProcessoDAO.get(cdProcesso, connection);				
				String filename = Util.limparFormatos(processo.getNrProcesso());
				
				return Response
					.status(Status.OK)
					.header("Content-Type", "application/octet-stream")
					.header("Content-Disposition",  "attachment; filename=\""+filename.replaceAll("\\s+","")+"-arquivos.zip\"")
					.entity(zip)
					.build();
			}
			
			if(rsm.getLines().size() <= 0) {
				return Response
						.status(Status.NO_CONTENT)
						.build();
			}
									
			return Response
					.status(Status.OK)
					.entity(Util.rsm2Json(rsm).toString())
					.build();
		} catch(Exception e) {	
			e.printStackTrace(System.out);
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		}  finally {
			zip = null;
			Conexao.desconectar(connection);
		}
	}
	
	@GET
	@Path("/{id}/processos/{idProcesso}/requerentes")
	@ApiOperation(
		value = "Fornece lista de requerentes do processo",
		notes = "# Considere \r\n"
			  + "* Cada processo tem um registro de requerente"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Arquivos encontrados"),
		@ApiResponse(code = 204, message = "Nenhum requerente"),
		@ApiResponse(code = 400, message = "Requerente não existe"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAllProcessoRequerentes(@ApiParam(value = "Id do AIT") @PathParam("id") int cdAit, @ApiParam(value = "Id do Processo") @PathParam("idProcesso") int cdProcesso) {
		Connection connection = null;
		try {
			connection = Conexao.conectar();
						
			ResultSetMap rsm = ProcessoServices.getRequerentes(cdProcesso, connection);
			
			if(rsm.getLines().size() <= 0) {
				return Response
						.status(Status.NO_CONTENT)
						.build();
			}
									
			return Response
					.status(Status.OK)
					.entity(Util.rsm2Json(rsm).toString())
					.build();
		} catch(Exception e) {			
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		} finally {
			Conexao.desconectar(connection);
		}
	}
	
	@GET
	@Path("/{id}/processos/{idProcesso}/arquivos/{idArquivo}")
	@ApiOperation(
		value = "Fornece arquivo do processo"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Arquivos encontrados"),
		@ApiResponse(code = 400, message = "Ait indicado não existe"),
		@ApiResponse(code = 204, message = "Arquivo não existe"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getProcessoArquivo(
			@ApiParam(value = "Id do AIT") @PathParam("id") int cdAit, 
			@ApiParam(value = "Id do Processo") @PathParam("idProcesso") int cdProcesso,
			@ApiParam(value = "Id do Arquivo") @PathParam("idArquivo") int cdArquivo) {
		
		Connection connection = null;
		try {
			connection = Conexao.conectar();
						
			Arquivo arquivo = ProcessoServices.getArquivo(cdProcesso, cdArquivo, connection);
			if(arquivo == null) {
				return Response
						.status(Status.NOT_FOUND)
						.entity(new com.tivic.sol.response.ResponseBody(404, "No Content", "O arquivo buscado não existe."))
						.build();
			}
			
			return Response
					.status(Status.OK)
					.entity(arquivo)
					.build();
		} catch(Exception e) {			
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		} finally {
			Conexao.desconectar(connection);
		}
	}

	@GET
	@Path("/{id}/processos/andamento")
	@ApiOperation(
			value = "Fornece um formulário do processo indicado",
			notes = "# Tipos \r\n"
					+ "| `tpProcesso` |                     |\r\n" 
					+ "|--------------|---------------------|\r\n" 
					+ "| **valor**    | **descrição**       |\r\n" 
					+ "| 7            | Defesa Prévia       |\r\n" 
					+ "| 10           | Recurso JARI        |\r\n" 
					+ "| 19           | Apresentar Condutor |\r\n" 
					+ "| 33           | Reembolso           |\r\n" 
					+ "| 51           | Recurso CETRAN      |\r\n"
			
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Formulário gerado"),
			@ApiResponse(code = 400, message = "Ait indicado não existe"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAndamento(
			@ApiParam(value = "Id do AIT") @PathParam("id") int cdAit) {
		try {
			Ait ait = AitServices.get(cdAit, null);
			if(ait == null) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "AIT indicado não existe."))
						.build();
			}
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			crt.add(new ItemComparator("A.nr_placa", ait.getNrPlaca(), ItemComparator.EQUAL, Types.VARCHAR));
			
			crt.add(new ItemComparator("A.nr_ait", Integer.toString(ait.getNrAit()), ItemComparator.EQUAL, Types.VARCHAR));
			
			crt.add(new ItemComparator("A.cd_renavan", ait.getNrRenavan(), ItemComparator.EQUAL, Types.BIGINT));
			
			ResultSetMap rsm = com.tivic.manager.mob.AitServices.find(crt);

			ArrayList<ItemComparator> crtProcessos = new ArrayList<ItemComparator>();
			crtProcessos.add(new ItemComparator("A.cd_ait", Integer.toString(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmProcessos = ProcessoServices.find(crtProcessos);
					
			Result result = AitServices.getFormularioAndamento(cdAit, rsm, rsmProcessos, null);
			
			if(result.getCode() < 0) {
				return Response
						.status(Status.INTERNAL_SERVER_ERROR)
						.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", result.getMessage()))
						.build();
			}
			
			return Response
					.status(Status.OK)
					.entity((byte[]) result.getObjects().get("BLB_ARQUIVO"))
					.build();
		} catch (Exception e) {
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		}
	}
	
	@POST
	@Path("/{id}/processos/formularios/{tpProcesso}")
	@ApiOperation(
			value = "Fornece um formulário do processo indicado",
			notes = "# Tipos \r\n"
					+ "| `tpProcesso` |                     |\r\n" 
					+ "|--------------|---------------------|\r\n" 
					+ "| **valor**    | **descrição**       |\r\n" 
					+ "| 7            | Defesa Prévia       |\r\n" 
					+ "| 10           | Recurso JARI        |\r\n" 
					+ "| 19           | Apresentar Condutor |\r\n" 
					+ "| 33           | Reembolso           |\r\n" 
					+ "| 51           | Recurso CETRAN      |\r\n"
			
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Formulário gerado"),
			@ApiResponse(code = 400, message = "Ait indicado não existe"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getFormulario(
			@ApiParam(value = "Id do AIT") @PathParam("id") int cdAit, 
			@ApiParam(value = "Tipo do processo") @PathParam("tpProcesso") int tpProcesso, ProcessoRequerente requerente) {
		try {
			
			if(AitServices.get(cdAit, null) == null) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "AIT indicado não existe."))
						.build();
			}
			
			Result result = AitServices.getFormularioProcesso(cdAit, tpProcesso, requerente, null);
			if(result.getCode() < 0) {
				return Response
						.status(Status.INTERNAL_SERVER_ERROR)
						.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", result.getMessage()))
						.build();
			}
			
			return Response
					.status(Status.OK)
					.entity((byte[]) result.getObjects().get("BLB_ARQUIVO"))
					.build();
			
		} catch(Exception e) {			
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		}
	}
	
	/**
	 * Dados para autenticação
	 * 
	 * @author Maurício Cordeiro <mauricio@tivic.com.br>
	 *
	 */
	static class CredencialVeiculo {
		private String nrPlaca;
		private String nrRenavan;
		
		public CredencialVeiculo() {
			super();
		}
		
		public CredencialVeiculo(String nrPlaca, String nrRenavan) {
			super();
			this.nrPlaca = nrPlaca;
			this.nrRenavan = nrRenavan;
		}
		
		public String getNrPlaca() {
			return nrPlaca;
		}
		public void setNrPlaca(String nrPlaca) {
			this.nrPlaca = nrPlaca;
		}
		public String getNrRenavan() {
			return nrRenavan;
		}
		public void setNrRenavan(String nrRenavan) {
			this.nrRenavan = nrRenavan;
		}
	}
}
