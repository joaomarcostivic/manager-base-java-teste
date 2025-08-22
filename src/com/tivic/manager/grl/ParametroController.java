package com.tivic.manager.grl;

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

import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "Parâmetros", tags = {"grl"})
@Path("/v2/grl/parametros")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ParametroController {
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna uma lista de Parâmetros"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parâmetros encontrados"),
			@ApiResponse(code = 400, message = "Requisição inválida"),
			@ApiResponse(code = 204, message = "Nenhum Parâmetro encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
		})
	public static Response get(
			@ApiParam(value = "Id. do parâmetro") @QueryParam("parametro") String idParametro,
			@ApiParam(value = "Id. do módulo", required = true) @QueryParam("modulo") String idModulo) {
		try {			
			if(idModulo == null) {
				return ResponseFactory.badRequest("É obrigatório informar o módulo");
			}
			
			List<String> ids = ParametroServices.getByModulo(idModulo, null);
			
			if(ids == null)
				throw new Exception("Erro ao buscar ids.");
			
			if(ids.size() == 0)
				return ResponseFactory.noContent("Nenhum parametro encontrado");
						
			String[] array = new String[ids.size()];
			array = ids.toArray(array);
			
			return ResponseFactory.ok(ParametroServices.getValues(array));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um Parâmetro"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parâmetros registrado", response = Parametro.class),
			@ApiResponse(code = 400, message = "Requisição inválida", response =  ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response =  ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Parâmetro a ser registrado") Parametro parametro) {
		try {
			
			ArrayList<ParametroValor> valores = new ArrayList<ParametroValor>();
			for (ParametroValor parametroValor : parametro.getValores()) {
				valores.add(parametroValor);
			}
			
			int r = ParametroServices.setValoresOfParametro(parametro.getCdParametro(), parametro.getCdEmpresa(), parametro.getCdPessoa(), valores);
			if(r < 0) {
				return ResponseFactory.internalServerError("Erro ao inserir parâmetro");
			}
			
			Parametro _parametro = ParametroDAO.get(parametro.getCdParametro());
			ArrayList<?> _list = ParametroServices.getValoresOfParametroAsArrayList(parametro.getNmParametro());
			ParametroValor[] _valores = new ParametroValor[_list.size()];
			_parametro.setValores(_list.toArray(_valores));
			
			return ResponseFactory.ok(_parametro);			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/initGet")
	@ApiOperation(
			value = "Inicializa e retorna os Parâmetros"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parâmetros encontrados"),
			@ApiResponse(code = 400, message = "Requisição inválida"),
			@ApiResponse(code = 204, message = "Nenhum Parâmetro encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
		})
	public static Response initGet() {
		try {
			ResultSetMap rsm = ParametroServices.getAll();
			if (!rsm.next()) {
				int init = ParametroServices.init();
				if(init > 0) {
					ResultSetMap rsmInit = ParametroServices.getAll();
					List<Parametro> parametros = new ResultSetMapper<Parametro>(rsmInit, Parametro.class).toList();
					return ResponseFactory.ok(parametros);
				} else
					return ResponseFactory.noContent("Nenhum registro");
			}
			
			List<Parametro> parametros = new ResultSetMapper<Parametro>(rsm, Parametro.class).toList();
			return ResponseFactory.ok(parametros);
			
		}  catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/getAll")
	@ApiOperation(
			value = "Retorna os Parâmetros"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parâmetros encontrados"),
			@ApiResponse(code = 400, message = "Requisição inválida"),
			@ApiResponse(code = 204, message = "Nenhum Parâmetro encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
		})
	public static Response getAll() {
		try {
			ResultSetMap rsm = ParametroServices.getAll();
			List<Parametro> parametros = new ResultSetMapper<Parametro>(rsm, Parametro.class).toList();
			for (Parametro parametro : parametros) {
				parametro.setNmRotulo(ParametroServices.getValorOfParametro(parametro.getNmParametro()));
			}
			return ResponseFactory.ok(parametros);
			
		}  catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/setValores")
	@ApiOperation(
			value = "Atualiza um Parâmetro"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parâmetros atualizado", response = Parametro.class),
			@ApiResponse(code = 400, message = "Requisição inválida", response =  ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response =  ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Parâmetro a ser atualizado") ParametroValor[] valores) {
		try {
			
			if(valores == null)
				return ResponseFactory.badRequest("Os valores não podem ser nulos");
			
			for(ParametroValor valor : valores) {
				Result tmp = ParametroServices.getValorParametro(valor.getCdParametro(), valor.getVlInicial());
				
				if(valor.getVlInicial().contains("data:image/png;base64,")) {
					valor.setBlbValor(ParametroServices.convertBase64(valor.getVlInicial()));
					valor.setVlInicial("");
				}
				
				int res;
				
				switch(tmp.getCode()) {
					case 1:
						ArrayList<ParametroValor> params = new ArrayList<ParametroValor>();
						params.add(valor);
						res = ParametroServices.setValoresOfParametro(valor.getCdParametro(), 0, 0, params);
						break;
					case 0:
						res = ParametroServices.insertValueOfParametro(valor.getCdParametro(), valor.getVlInicial(), valor.getBlbValor());
						break;
					default:
						res = -1;
						break;						
				}
				
				if(res == -1) {
					return ResponseFactory.badRequest("Erro ao inserir valores");
				}
			}
			
			return ResponseFactory.ok("Valores inseridos com sucesso !");
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/init")
	@ApiOperation(
			value = "Inicializa Parâmetros"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parâmetros inicializados", response = Parametro.class),
			@ApiResponse(code = 400, message = "Requisição inválida", response =  ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response =  ResponseBody.class)
		})
	public static Response init() {
		try {
			if(ParametroServices.init() <= 0)
				return ResponseFactory.internalServerError("Erro ao inicializar parâmetros.");
			
			return ResponseFactory.ok("Parâmetros inicializados com sucesso.");			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/mob/nao-categorizados")
	@ApiOperation(
			value = "Inicializa Parâmetros"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parâmetros inicializados", response = Parametro.class),
			@ApiResponse(code = 400, message = "Requisição inválida", response =  ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response =  ResponseBody.class)
		})
	public static Response naoCategorizados(@ApiParam(value = "Categorizados") String[] categorizados) {
		try {
			List<Parametro> parametros = ParametroServices.getNaoCategorizados(categorizados);
			
			return ResponseFactory.ok(parametros);			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/initMob")
	@ApiOperation(
			value = "Inicializa Parâmetros"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parâmetros inicializados", response = Parametro.class),
			@ApiResponse(code = 400, message = "Requisição inválida", response =  ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response =  ResponseBody.class)
		})
	public static Response initMob() {
		try {
			ParametroServices.initMob();
			
			return ResponseFactory.ok("Parâmetros inicializados com sucesso.");			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/mob")
	@ApiOperation(
			value = "Retorna parâmetros MOB"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parâmetros encontraados", response = Parametro.class),
			@ApiResponse(code = 400, message = "Requisição inválida", response =  ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response =  ResponseBody.class)
		})
	public static Response getMob() {
		try {
			List<Parametro> conv = ParametroServices.getAllMob();
			
			if(conv.size() <= 0)
				return ResponseFactory.badRequest("Não foram encontrados parâmetros.");
			
			return ResponseFactory.ok(conv);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/mob/{nmParametro}")
	@ApiOperation(
			value = "Retorna Opções de parâmetro MOB"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Opções encontraadas", response = Parametro.class),
			@ApiResponse(code = 400, message = "Requisição inválida", response =  ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response =  ResponseBody.class)
		})
	public static Response getMobOpcoes(@PathParam("nmParametro") String nmParametro) {
		try {
			ResultSetMap rsm = ParametroServices.getOpcoesParametroByNome(nmParametro);
			
			if(!rsm.next()) {
				return ResponseFactory.badRequest("Não foram encontradas opções");
			}
			
			//List<ParametroOpcao> opcoes = new ResultSetMapper<ParametroOpcao>(rsm, ParametroOpcao.class).toList();
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/mob/{nmParametro}/img")
	@ApiOperation(
			value = "Retorna Opções de parâmetro MOB"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Opções encontraadas", response = Parametro.class),
			@ApiResponse(code = 400, message = "Requisição inválida", response =  ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response =  ResponseBody.class)
		})
	public static Response getMobImages(@PathParam("nmParametro") String nmParametro) {
		try {
			List<String> url= ParametroServices.getImages(nmParametro);
			
			return ResponseFactory.ok(url);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{idParametro}/valor")
	public Response getParametroValor(@PathParam("idParametro") String idParametro, @QueryParam("legado") boolean legado) {
		try {
			if(Util.isStrBaseAntiga() && legado) {
				ParametroValor parametro = ParametroServices.getParamsBaseAntigaByNome(idParametro);
				if(parametro == null) { 
					return ResponseFactory.notFound("Parâmetro não encontrado");
				}
				return ResponseFactory.ok(parametro);
			}
			
			Parametro parametro = ParametroServices.getByName(idParametro);
			
			if(parametro == null) 
				return ResponseFactory.notFound("Parâmetro não encontrado");
			
			return ResponseFactory.ok(ParametroValorDAO.get(parametro.getCdParametro(), 1, null));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
