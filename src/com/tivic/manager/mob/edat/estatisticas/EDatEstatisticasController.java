package com.tivic.manager.mob.edat.estatisticas;

import java.sql.Types;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.manager.mob.DeclaranteTipoRelacaoEnum;
import com.tivic.manager.mob.edat.IEDatService;
import com.tivic.manager.mob.edat.dto.EDatEstatisticaCategoriaVeiculoDTO;
import com.tivic.manager.mob.edat.dto.EDatEstatisticaCidadeDTO;
import com.tivic.manager.mob.edat.dto.EDatEstatisticaTipoAcidenteDTO;
import com.tivic.manager.mob.edat.dto.EDatEstatisticaTipoVeiculoDTO;
import com.tivic.manager.mob.edat.dto.EdatEstatisticaEspecieVeiculoDTO;
import com.tivic.manager.mob.edat.dto.EdatEstatisticaGeneroDTO;
import com.tivic.manager.mob.edat.dto.EdatEstatisticaIdadeDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;

@Api(value = "BOAT", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/edat/estatisticas")
public class EDatEstatisticasController {
	
	IEDatService eDatService;
	
	public EDatEstatisticasController() throws Exception {
		eDatService = (IEDatService) BeansFactory.get(IEDatService.class);
	}
	
	@GET
	@Path("/genero")
	@ApiOperation(
		value = "Fornece um quantitativo de acidentes por gênero"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Declarações encontradas", response = List.class),
		@ApiResponse(code = 204, message = "Nao existe declarações com os critérios indicados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstatisticaPorGenero(
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String dtFinal) {
		try {	
			
			SearchCriterios search = new SearchCriterios();
			search.addCriteriosGreaterDate("A.dt_comunicacao", dtInicial, true);
			search.addCriteriosMinorDate("A.dt_comunicacao", dtFinal, true);
			
			List<EdatEstatisticaGeneroDTO> list = eDatService.getEstatisticaPorGenero(search, new CustomConnection());			
			
			return ResponseFactory.ok(list);			
		} catch(NoContentException e) {		
			return ResponseFactory.noContent();
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/idade")
	@ApiOperation(
		value = "Fornece um quantitativo de acidentes por idade do condutor/proprietário"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Declarações encontradas", response = List.class),
		@ApiResponse(code = 204, message = "Nao existe declarações com os critérios indicados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstatisticaPorIdade(
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String dtFinal) {
		try {	
			
			SearchCriterios search = new SearchCriterios();
			search.addCriteriosGreaterDate("A.dt_comunicacao", dtInicial, true);
			search.addCriteriosMinorDate("A.dt_comunicacao", dtFinal, true);
			
			String in = DeclaranteTipoRelacaoEnum.TP_CONDUTOR.getKey()+","+DeclaranteTipoRelacaoEnum.TP_CONDUTOR_E_PROPRIETARIO.getKey();
			search.addCriterios("B.tp_relacao", in, ItemComparator.IN, Types.VARCHAR);
			
			List<EdatEstatisticaIdadeDTO> list = eDatService.getEstatisticaPorIdade(search, new CustomConnection());
			
			return ResponseFactory.ok(list);			
		} catch(NoContentException e) {		
			return ResponseFactory.noContent();
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/cidade")
	@ApiOperation(
		value = "Fornece um quantitativo de acidentes por cidade"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Declarações encontradas", response = List.class),
		@ApiResponse(code = 204, message = "Nao existe declarações com os critérios indicados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstatisticaPorCidade(
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String dtFinal) {
		try {	
			
			SearchCriterios search = new SearchCriterios();
			search.addCriteriosGreaterDate("A.dt_comunicacao", dtInicial, true);
			search.addCriteriosMinorDate("A.dt_comunicacao", dtFinal, true);		
			search.setQtLimite(10);
			
			List<EDatEstatisticaCidadeDTO> list = eDatService.getEstatisticaPorCidade(search, new CustomConnection());
						
			return ResponseFactory.ok(list);			
		} catch(NoContentException e) {		
			return ResponseFactory.noContent();
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/especie-veiculo")
	@ApiOperation(
		value = "Fornece um quantitativo de acidentes por espécie de veículo"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Declarações encontradas", response = List.class),
		@ApiResponse(code = 204, message = "Nao existe declarações com os critérios indicados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstatisticaPorEspecieVeiculo(
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String dtFinal) {
		try {	
			
			SearchCriterios search = new SearchCriterios();
			search.addCriteriosGreaterDate("A.dt_comunicacao", dtInicial, true);
			search.addCriteriosMinorDate("A.dt_comunicacao", dtFinal, true);
			
			List<EdatEstatisticaEspecieVeiculoDTO> list = eDatService.getEstatisticaEspecieVeiculo(search, new CustomConnection());			
			
			return ResponseFactory.ok(list);			
		} catch(NoContentException e) {		
			return ResponseFactory.noContent();
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/categoria-veiculo")
	@ApiOperation(
		value = "Fornece um quantitativo de acidentes por categoria de veículo"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Declarações encontradas", response = List.class),
		@ApiResponse(code = 204, message = "Nao existe declarações com os critérios indicados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstatisticaPorCategoriaVeiculo(
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String dtFinal) {
		try {	
			
			SearchCriterios search = new SearchCriterios();
			search.addCriteriosGreaterDate("A.dt_comunicacao", dtInicial, true);
			search.addCriteriosMinorDate("A.dt_comunicacao", dtFinal, true);
			
			List<EDatEstatisticaCategoriaVeiculoDTO> list = eDatService.getEstatisticaCategoriaVeiculo(search, new CustomConnection());
						
			return ResponseFactory.ok(list);			
		} catch(NoContentException e) {		
			return ResponseFactory.noContent();
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/tipo-veiculo")
	@ApiOperation(
		value = "Fornece um quantitativo de acidentes por tipo de veículo"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Declarações encontradas", response = List.class),
		@ApiResponse(code = 204, message = "Nao existe declarações com os critérios indicados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstatisticaPorTipoVeiculo(
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String dtFinal) {
		try {	
			
			SearchCriterios search = new SearchCriterios();
			search.addCriteriosGreaterDate("A.dt_comunicacao", dtInicial, true);
			search.addCriteriosMinorDate("A.dt_comunicacao", dtFinal, true);
			
			List<EDatEstatisticaTipoVeiculoDTO> list = eDatService.getEstatisticaTipoVeiculo(search, new CustomConnection());
						
			return ResponseFactory.ok(list);			
		} catch(NoContentException e) {		
			return ResponseFactory.noContent();
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/tipo-acidente")
	@ApiOperation(
		value = "Fornece um quantitativo de acidentes por tipo de acidente"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Declarações encontradas", response = List.class),
		@ApiResponse(code = 204, message = "Nao existe declarações com os critérios indicados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstatisticaPorTipoAcidente(
			@ApiParam(value = "Data Inicio") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data Final") @QueryParam("dtFinal") String dtFinal) {
		try {	
			
			SearchCriterios search = new SearchCriterios();
			search.addCriteriosGreaterDate("A.dt_comunicacao", dtInicial, true);
			search.addCriteriosMinorDate("A.dt_comunicacao", dtFinal, true);
			
			List<EDatEstatisticaTipoAcidenteDTO> list = eDatService.getEstatisticaTipoAcidente(search, new CustomConnection());
						
			return ResponseFactory.ok(list);			
		} catch(NoContentException e) {		
			return ResponseFactory.noContent();
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
}
