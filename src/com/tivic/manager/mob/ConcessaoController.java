package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;
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

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "Concessao", tags = { "mob" })
@Path("/v2/mob/concessao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConcessaoController {
	
	private final ConcessaoServices concessaoService;
	
	public ConcessaoController() {
		this.concessaoService = new ConcessaoServices();
	}
	
	/* ************************************************************************
	 * COLETIVO URBANO
	 */
	
	@GET
	@Path("/coletivourbano")
	@ApiOperation(value = "Busca de Concessões do Coletivo Urbano")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = Concessao[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response findColetivoUrbano (
			@ApiParam(value = "Prefixo") @QueryParam("nrPrefixo") int nrPrefixo,
			@ApiParam(value = "Nome do concessionario") @QueryParam("cdConcessionario") int cdConcessionario,
			@ApiParam(value = "Nome da linha") @QueryParam("cdLinha") int cdLinha,
			@ApiParam(value = "Situacao do concessao", allowableValues = "0, 1") @QueryParam("stConcessao") @DefaultValue("-1") int stConcessao,
			@ApiParam(value = "Nome do concessionario") @QueryParam("nmConcessionario") String nmConcessionario) {
		
		try {	
			Criterios crt = new Criterios();
			crt.add("A.tp_concessao", Integer.toString(ConcessaoServices.TP_COLETIVO_URBANO), ItemComparator.EQUAL, Types.INTEGER);
						
			if(nrPrefixo!=0) {
				crt.add("E.nr_prefixo", Integer.toString(nrPrefixo), ItemComparator.EQUAL, Types.INTEGER);
			}

			if(cdConcessionario>0) {
				crt.add("A.cd_concessionario", Integer.toString(cdConcessionario), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(cdLinha!=0) {
				crt.add("D.cd_linha", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(stConcessao > -1) {
				crt.add("A.st_concessao", Integer.toString(stConcessao), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(nmConcessionario != null) {				
				crt.add(new ItemComparator("B.nm_pessoa", nmConcessionario, ItemComparator.EQUAL, Types.VARCHAR));
			} 
						
			List<ConcessaoColetivoUrbanoDTO> list = this.concessaoService.findColetivoUrbano(crt);
		
			if(list.isEmpty())
				return ResponseFactory.noContent(list);

			return ResponseFactory.ok(list);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/coletivourbano/concessionario")
	@ApiOperation(value = "Todos os nomes concessionarios do tipo coletivo urbano")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = Concessao[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response findNomeConcessionarioColetivoUrbano (
			@ApiParam(value = "Nome do concessionário") @QueryParam("nmConcessionario") String nmConcessionario) {
		
		try {	
			Criterios crt = new Criterios();			
						
			if(nmConcessionario != null) {
				crt.add("B.nm_pessoa", nmConcessionario, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
						
			ResultSetMap rsm = ConcessaoServices.findNomeConcessionarioColetivoUrbano(crt);
		
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhum concessionario encontrado");

			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/coletivourbano/{id}")
	@ApiOperation(
		value = "Fornece uma concessao dado o id indicado",
		notes = "Considere id = cdConcessao"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = ConcessaoColetivoRuralDTO.class),
		@ApiResponse(code = 204, message = "Nao existe concessao com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getColetivoUrbano(@ApiParam(value = "id da concessao") @PathParam("id") int cdConcessao)  {
		try {
			Criterios crt = new Criterios();
			crt.add("A.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);
			
			List<ConcessaoColetivoUrbanoDTO> list = this.concessaoService.findColetivoUrbano(crt);
			
			ConcessaoColetivoUrbanoDTO dto = null;
			if (list!=null && list.size()>0)
				dto = list.get(0);
			
			if(dto==null)
				return ResponseFactory.noContent("Nao existe concessao com o id indicado.");
			
			return ResponseFactory.ok(dto);
			
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
	
	@POST
	@Path("/coletivourbano")
	@ApiOperation(
			value = "Registra uma nova concessao de Coletivo Urbano"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Concessao registrada", response = Concessao.class),
			@ApiResponse(code = 400, message = "Concessao inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Concessao a ser registrada") ConcessaoColetivoUrbanoDTO concessaoDTO) {
		try {
			concessaoDTO.getConcessao().setTpConcessao(ConcessaoServices.TP_COLETIVO_URBANO);
			concessaoDTO.getConcessao().setStConcessao(ConcessaoServices.ST_ATIVO);
			
			Result result = ConcessaoServices.insert(concessaoDTO.getConcessao(), concessaoDTO.getPessoa(), concessaoDTO.getPessoaJuridica(), concessaoDTO.getPessoaEndereco(), 3200, 18);
			
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Concessao)result.getObjects().get("CONCESSAO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/coletivourbano/{id}")
	@ApiOperation(
			value = "Atualiza uma concessao de Coletivo Urbano"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Concessao atualizada", response = Concessao.class),
			@ApiResponse(code = 400, message = "Concessao invalida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Codigo da concessao.") @PathParam("id") int cdConcessao,
			@ApiParam(value = "Concessao a ser atualizada.") ConcessaoColetivoUrbanoDTO concessaoDTO) {
		try {			
			concessaoDTO.getConcessao().setCdConcessao(cdConcessao);
			
			Result result = ConcessaoServices.update(concessaoDTO.getConcessao(), concessaoDTO.getPessoa(), concessaoDTO.getPessoaJuridica(), concessaoDTO.getPessoaEndereco());
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Concessao)result.getObjects().get("CONCESSAO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	

	
	/* ************************************************************************
	 * COLETIVO RURAL
	 */
	
	@GET
	@Path("/coletivorural")
	@ApiOperation(value = "Busca de Concessões do Coletivo Rural")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = Concessao[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response findColetivoRural (
			@ApiParam(value = "Placa") @QueryParam("nrPlaca")  String nrPlaca,
			@ApiParam(value = "Numero da permissao") @QueryParam("nrConcessao") String nrConcessao,
			@ApiParam(value = "Situacao do Concessao", allowableValues = "0, 1") @QueryParam("stConcessao") @DefaultValue("-1") int stConcessao,
			@ApiParam(value = "Codigo do Permissionario") @QueryParam("cdConcessionario") int cdConcessionario,	
			@ApiParam(value = "Codigo do motorista auxiliar") @QueryParam("cdMotoristaAuxiliar") int cdMotoristaAuxiliar,
			@ApiParam(value = "Quantidade maxima de registros", defaultValue = "100") @DefaultValue("100") @QueryParam("limit") int limit) {
		
		try {	
			Criterios crt = new Criterios();
//			crt.add("qtLimite", Integer.toString(limit), ItemComparator.EQUAL, Types.INTEGER);
			crt.add("A.tp_concessao", Integer.toString(ConcessaoServices.TP_COLETIVO_RURAL), ItemComparator.EQUAL, Types.INTEGER);
			
			if(nrPlaca != null && !nrPlaca.equals("")) {
				crt.add("F.nr_placa", nrPlaca, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(nrConcessao != null && !nrConcessao.equals("")) {
				crt.add("A.nr_concessao", nrConcessao, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(stConcessao > -1) {
				crt.add("A.st_concessao", String.valueOf(stConcessao), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(cdConcessionario > 0) {
				crt.add("A.cd_concessionario", String.valueOf(cdConcessionario), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(cdMotoristaAuxiliar > 0) {
				crt.add("D.cd_pessoa", String.valueOf(cdMotoristaAuxiliar), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			List<ConcessaoColetivoRuralDTO> list = this.concessaoService.findColetivoRural(crt);
		
			if(list.isEmpty())
				return ResponseFactory.noContent(list);

			return ResponseFactory.ok(list);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/coletivorural/{id}")
	@ApiOperation(
		value = "Fornece uma concessao dado o id indicado",
		notes = "Considere id = cdConcessao"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = ConcessaoColetivoRuralDTO.class),
		@ApiResponse(code = 204, message = "Nao existe concessao com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getColetivoRural(@ApiParam(value = "id da concessao") @PathParam("id") int cdConcessao) {
		try {
			Criterios crt = new Criterios();
			crt.add("A.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);

			List<ConcessaoColetivoRuralDTO> list = this.concessaoService.findColetivoRural(crt);
			
			ConcessaoColetivoRuralDTO dto = null;
			if (list!=null && list.size()>0)
				dto = list.get(0);
			
			if(dto==null)
				return ResponseFactory.noContent("Nao existe concessao com o id indicado.");
			
			return ResponseFactory.ok(dto);
			
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
	
	@POST
	@Path("/coletivorural")
	@ApiOperation(
			value = "Registra uma nova concessao de Coletivo Rural"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Concessao registrada", response = Concessao.class),
			@ApiResponse(code = 400, message = "Concessao inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Concessao a ser registrada") ConcessaoColetivoRuralDTO concessaoDTO) {
		try {
			concessaoDTO.getConcessao().setTpConcessao(ConcessaoServices.TP_COLETIVO_RURAL);
			concessaoDTO.getConcessao().setStConcessao(ConcessaoServices.ST_ATIVO);
			
			Result result = ConcessaoServices.insert(concessaoDTO.getConcessao(), concessaoDTO.getPessoa(), concessaoDTO.getPessoaJuridica(), concessaoDTO.getPessoaEndereco(), 3200, 18);
			
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Concessao)result.getObjects().get("CONCESSAO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/coletivorural/{id}")
	@ApiOperation(
			value = "Atualiza uma concessao de Coletivo Rural"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Concessao atualizada", response = Concessao.class),
			@ApiResponse(code = 400, message = "Concessao invalida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Codigo da concessao.") @PathParam("id") int cdConcessao,
			@ApiParam(value = "Concessao a ser atualizada.") ConcessaoColetivoRuralDTO concessaoDTO) {
		try {			
			concessaoDTO.getConcessao().setCdConcessao(cdConcessao);
			
			Result result = ConcessaoServices.update(concessaoDTO.getConcessao(), concessaoDTO.getPessoa(), concessaoDTO.getPessoaJuridica(), concessaoDTO.getPessoaEndereco());
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Concessao)result.getObjects().get("CONCESSAO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	/* ************************************************************************
	 * TAXI
	 */
	
	@GET
	@Path("/taxi")
	@ApiOperation(value = "Busca de Concessões de Táxi")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = ConcessaoTaxiDTO[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response findTaxi (
			@ApiParam(value = "Numero da permissao") @QueryParam("nrConcessao")  String nrConcessao,
			@ApiParam(value = "Numero do ponto") @QueryParam("nrPonto")  String nrPonto,
			@ApiParam(value = "Limite de registros") @QueryParam("limit") int qtLimite,
			@ApiParam(value = "Numero de ordem") @QueryParam("nrOrdem")  String nrOrdem,
			@ApiParam(value = "Numero de ordem") @QueryParam("nrPlaca")  String nrPlaca,
			@ApiParam(value = "Nome do concessionario") @QueryParam("cdConcessionario") int cdConcessionario,
			@ApiParam(value = "Codigo do motorista auxiliar") @QueryParam("cdMotoristaAuxiliar") int cdMotoristaAuxiliar,
			@ApiParam(value = "Situacao do Concessao", allowableValues = "0, 1") @QueryParam("stConcessao") @DefaultValue("-1") int stConcessao) {
		
		try {	
			
			Criterios crt = new Criterios();
			crt.add("A.TP_CONCESSAO", Integer.toString(ConcessaoServices.TP_TAXI), ItemComparator.EQUAL, Types.INTEGER);

			if(nrConcessao != null && !nrConcessao.equals("")) {
				crt.add("A.NR_CONCESSAO", nrConcessao, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(qtLimite > 0) {
				crt.add("qtLimite", String.valueOf(qtLimite), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(nrPonto != null && !nrPonto.equals("")) {
				crt.add("F.NM_GRUPO_PARADA", nrPonto, ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			if(nrPlaca != null && !nrPlaca.equals("")) {
				crt.add("G.NR_PLACA", nrPlaca, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(nrOrdem != null && !nrOrdem.equals("")) {
				crt.add("E.DS_REFERENCIA", nrOrdem, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(cdConcessionario > 0) {
				crt.add("A.CD_CONCESSIONARIO", String.valueOf(cdConcessionario), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(cdMotoristaAuxiliar!=0) {
				crt.add("C.CD_PESSOA", Integer.toString(cdMotoristaAuxiliar), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(stConcessao > -1) {
				crt.add("A.ST_CONCESSAO", Integer.toString(stConcessao), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			List<ConcessaoTaxiDTO> list = this.concessaoService.findTaxi(crt);
		
			if(list.isEmpty())
				return ResponseFactory.noContent(list);

			return ResponseFactory.ok(list);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/taxi/{id}")
	@ApiOperation(
		value = "Fornece uma concessao dado o id indicado",
		notes = "Considere id = cdConcessao"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = ConcessaoTaxiDTO.class),
		@ApiResponse(code = 204, message = "Nao existe concessao com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getTaxi(@ApiParam(value = "id da concessao") @PathParam("id") int cdConcessao) {
		try {
			Criterios crt = new Criterios();
			crt.add("A.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);

			List<ConcessaoTaxiDTO> list = this.concessaoService.findTaxi(crt);
			
			ConcessaoTaxiDTO dto = null;
			if (list!=null && list.size()>0)
				dto = list.get(0);
			
			if(dto==null)
				return ResponseFactory.noContent("Nao existe concessao com o id indicado.");
			
			return ResponseFactory.ok(dto);
			
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
	
	@POST
	@Path("/taxi")
	@ApiOperation(
			value = "Registra uma nova concessao de Taxi"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Concessao registrada", response = Concessao.class),
			@ApiResponse(code = 400, message = "Concessao inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Concessao a ser registrada") ConcessaoTaxiDTO concessaoDTO) {
		try {
			concessaoDTO.getConcessao().setTpConcessao(ConcessaoServices.TP_TAXI);
			concessaoDTO.getConcessao().setStConcessao(ConcessaoServices.ST_ATIVO);
			
			Result result = ConcessaoServices.insert(concessaoDTO.getConcessao(), concessaoDTO.getPessoa(), concessaoDTO.getPessoaFisica(), concessaoDTO.getPessoaEndereco(), 3200, 18);
			
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Concessao)result.getObjects().get("CONCESSAO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/taxi/{id}")
	@ApiOperation(
			value = "Atualiza uma concessao de Taxi"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Concessao atualizada", response = Concessao.class),
			@ApiResponse(code = 400, message = "Concessao invalida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Codigo da concessao.") @PathParam("id") int cdConcessao,
			@ApiParam(value = "Concessao a ser atualizada.") ConcessaoTaxiDTO concessaoDTO) {
		try {			
			concessaoDTO.getConcessao().setCdConcessao(cdConcessao);
			
			Result result = ConcessaoServices.update(concessaoDTO.getConcessao(), concessaoDTO.getPessoa(), concessaoDTO.getPessoaFisica(), concessaoDTO.getPessoaEndereco());
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Concessao)result.getObjects().get("CONCESSAO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	/* ************************************************************************
	 * GERAL
	 */
	
	@GET
	@Path("/{id}/auxiliares")
	@ApiOperation(value = "Busca a lista de auxiliares ligados a concessao.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Auxiliares encontrados", response = ConcessionarioPessoaDTO[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getAuxiliares (@ApiParam(value = "id da concessao") @PathParam("id") int cdConcessao) {
		
		try {	
			List<ConcessionarioPessoaDTO> list = ConcessionarioPessoaServices.findAuxiliares(cdConcessao);
		
			if(list.isEmpty())
				return ResponseFactory.noContent(list);

			return ResponseFactory.ok(list);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/representantes")
	@ApiOperation(value = "Busca a lista de representantes ligados a concessao.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Representantes encontrados", response = ConcessionarioPessoaDTO[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getRepresentantes (@ApiParam(value = "id da concessao") @PathParam("id") int cdConcessao) {
		
		try {	
			List<ConcessionarioPessoaDTO> list = ConcessionarioPessoaServices.findRepresentantes(cdConcessao);
		
			if(list.isEmpty())
				return ResponseFactory.noContent(list);

			return ResponseFactory.ok(list);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/veiculos")
	@ApiOperation(value = "Busca a lista de veiculos ligados a concessao.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Veiculos encontrados", response = ConcessaoVeiculoDTO[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getVeiculo (@ApiParam(value = "id da concessao") @PathParam("id") int cdConcessao) {
		
		try {
			List<ConcessaoVeiculoDTO> list = ConcessaoVeiculoServices.findDTO(cdConcessao);
		
			if(list.isEmpty())
				return ResponseFactory.noContent(list);

			return ResponseFactory.ok(list);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/linhas")
	@ApiOperation(value = "Busca a lista de linhas ligados a concessao.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Linhas encontradas", response = Linha[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getLinhas (@ApiParam(value = "id da concessao") @PathParam("id") int cdConcessao) {
		
		try {	
			Criterios criterios = new Criterios();
			criterios.add("A.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);
			
			ResultSetMap rsm = LinhaServices.find(criterios);
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhuma linha encontrada");
			
			return ResponseFactory.ok(new ResultSetMapper<Linha>(rsm, Linha.class));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/vistorias")
	@ApiOperation(value = "Busca a lista de vistorias ligadas a concessao.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Vistorias encontradas", response = Vistoria[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getVistorias (@ApiParam(value = "id da concessao") @PathParam("id") int cdConcessao) {
		try {	
			
			ResultSetMap rsm = VistoriaServices.findVistoriaByConcessao(cdConcessao);
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhuma vistoria encontrada.");
			
			return ResponseFactory.ok(new ResultSetMapper<Vistoria>(rsm, Vistoria.class));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{id}")
	@ApiOperation(
		value = "Fornece uma concessao dado o id indicado",
		notes = "Considere id = cdConcessao"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = Concessao.class),
		@ApiResponse(code = 204, message = "Nao existe concessao com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response findConcessao(@ApiParam(value = "id da concessao") @PathParam("id") int cdConcessao) {
		try {
			Criterios crt = new Criterios();
			crt.add("A.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);

			List<Concessao> list = this.concessaoService.findConcessao(crt);

			Concessao dto = null;
			if (list!=null && list.size()>0)
				dto = list.get(0);

			if(dto==null)
				return ResponseFactory.noContent("Nao existe concessao com o id indicado.");

			return ResponseFactory.ok(dto);

		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}

	@GET
	@Path("/find")
	@ApiOperation(
		value = "Fornece uma concessao"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = ConcessaoDTO.class),
		@ApiResponse(code = 204, message = "Nao existe concessao com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response find(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam( value = "Situaçao da concessão" ) @QueryParam("stConcessao") @DefaultValue("-1")  int stConcessao, 
			@ApiParam( value = "Tipo de Concessão") @QueryParam("tpConcessao") @DefaultValue("-1") int tpConcessao) {
		try {
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			
			if( stConcessao > -1)
				crt.add( new ItemComparator ("st_concessao", Integer.toString(stConcessao), ItemComparator.EQUAL, Types.INTEGER));
			
			if( tpConcessao > -1)
				crt.add( new ItemComparator ("tp_concessao", Integer.toString(tpConcessao), ItemComparator.EQUAL, Types.INTEGER));

			ResultSetMap rsm = ConcessaoServices.find( crt );
			
			if(rsm.getLines().size() <= 0 || rsm.getLines() == null) {
				return ResponseFactory.noContent("Nenhum registro");
			}

			return ResponseFactory.ok(rsm);

		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
		
	@GET
	@Path("")
	@ApiOperation(
				value = "Retorna todas concessões"
			)
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Concessões encontradas", response = Concessao.class ),
			@ApiResponse( code = 204, message = "Concessões não encontradas", response = ResponseBody.class ),
			@ApiResponse( code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class )
	})
	public static Response getAll() {
		
		try {
			
			ResultSetMap _rsm = ConcessaoServices.getAll();
			
			if( _rsm == null || _rsm.getLines().size() <= 0 ) 
				return ResponseFactory.noContent("Nenhuma concessão encontrada");
			
			return ResponseFactory.ok( _rsm );
		} catch ( Exception e ) {
			return ResponseFactory.internalServerError( e.getMessage() );
		}
	}
}
