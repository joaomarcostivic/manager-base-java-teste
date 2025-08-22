package com.tivic.manager.fta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.DetranUtils;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
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

@Api(value = "Veículo", tags = {"fta"}, authorizations = {
			@Authorization(value = "Bearer Auth", scopes = {
					@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/fta/veiculos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VeiculoController {
	
	private ManagerLog managerLog;
	
	public VeiculoController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
		value = "Fornece o registro de um veículo dado seu id",
		notes = "Considere id = cdVeiculo"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Veículo encontrados"),
		@ApiResponse(code = 204, message = "Nenhum veículo"),
		@ApiResponse(code = 204, message = "Não existe Veículo com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getVeiculo(@ApiParam(value = "Id do Veículo") @PathParam("id") int cdVeiculo) {
		try {
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_veiculo", Integer.toString(cdVeiculo), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = VeiculoServices.find(criterios);
			
	        VeiculoDTO veiculoDTO = new VeiculoDTO.Builder(rsm.getLines().get(0)).build();
	        
	        if(veiculoDTO == null) {
				return ResponseFactory.noContent("Nenhum veículo encontrado");
			}
			
			return ResponseFactory.ok(veiculoDTO);
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}

	@GET
	@Path("/detran/{placa}")
	@ApiOperation(
			value = "Retorna dados de pesquisa de veiculo por placa do detran"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Dados encontrados", response = DetranDTO.class),
			@ApiResponse(code = 204, message = "Nenhum veiculo encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response pesquisaDetran (
			@ApiParam(value = "Placa do veículo") @PathParam("placa") String nrPlaca,
			@ApiParam(value = "Identificador do usuário") @QueryParam("user") @DefaultValue("zonaazul") String nmLogin,
			@ApiParam(value = "Senha do usuário") @QueryParam("pass") @DefaultValue("z0n4@4zul") String nmSenha,
			@ApiParam(value = "Identificador do orgão") @QueryParam("orgao") @DefaultValue("") String idOrgao){
		try {
			String URL = "http://detran.tivic.com.br/consulta/" + idOrgao + "/detrancon";
			nrPlaca = nrPlaca.replaceAll("-", "");
			URL += "?p=" + nrPlaca;
			URL url = new URL(URL);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 
	        
	        ObjectMapper mapper = new ObjectMapper();

	        @SuppressWarnings("unchecked")
	        DetranDTO detranDto = new DetranDTO.Builder(
	        		mapper.readValue(buffer.toString(), Map.class), true
	        		).build();
	        
	        if(detranDto == null) {
				return ResponseFactory.noContent("Nenhum veículo encontrado");
			}
			
			return ResponseFactory.ok(detranDto);	        		
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/detran")
	@ApiOperation(
			value = "Retorna dados de pesquisa de veiculo por placa do detran"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Dados encontrados", response = DetranDTO.class),
			@ApiResponse(code = 204, message = "Nenhum veiculo encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response pesquisaPlacaDetran (
			@ApiParam(value = "Placa do veículo") @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(value = "Identificador do usuário") @QueryParam("user") @DefaultValue("zonaazul") String nmLogin,
			@ApiParam(value = "Senha do usuário") @QueryParam("pass") @DefaultValue("z0n4@4zul") String nmSenha,
			@ApiParam(value = "Identificador do orgão") @QueryParam("cdOrgao") int cdOrgao){
		try {
			String URL = "http://detran.tivic.com.br/consulta/" + cdOrgao + "/detrancon";
			nrPlaca = nrPlaca.replaceAll("-", "");
			URL += "?p=" + nrPlaca;
			URL += "&incluirCpfCnpj=true";
			URL url = new URL(URL);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 
	        
	        ObjectMapper mapper = new ObjectMapper();
	        @SuppressWarnings("unchecked")
	        DetranDTO detranDto = new DetranDTO.Builder(
	        		mapper.readValue(buffer.toString(), Map.class), true
	        		).build();
	        
	        if(detranDto == null) {
				return ResponseFactory.noContent("Nenhum veículo encontrado");
			}
			
			return ResponseFactory.ok(detranDto);	        		
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/detran/chassi")
	@ApiOperation(
			value = "Retorna dados de pesquisa de veiculo por chassi do detran"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Dados encontrados", response = DetranDTO.class),
			@ApiResponse(code = 204, message = "Nenhum veiculo encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response pesquisaChassiDetran (
			@ApiParam(value = "Chassi do veículo") @QueryParam("nrChassi") String nrChassi,
			@ApiParam(value = "Identificador do usuário") @QueryParam("user") @DefaultValue("zonaazul") String nmLogin,
			@ApiParam(value = "Senha do usuário") @QueryParam("pass") @DefaultValue("z0n4@4zul") String nmSenha,
			@ApiParam(value = "Identificador do orgão") @QueryParam("cdOrgao") int cdOrgao){
		try {
			String URL = "http://detran.tivic.com.br/consulta/" + cdOrgao + "/detrancon";
			nrChassi = nrChassi.replaceAll("-", "");
			URL += "?nrChassi=" + nrChassi;
			URL += "&incluirCpfCnpj=true";
			URL url = new URL(URL);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 
	        
	        ObjectMapper mapper = new ObjectMapper();
	        @SuppressWarnings("unchecked")
	        DetranDTO detranDto = new DetranDTO.Builder(
	        		mapper.readValue(buffer.toString(), Map.class), true
	        		).build();
	        
	        if(detranDto == null) {
				return ResponseFactory.noContent("Nenhum veículo encontrado");
			}
			
			return ResponseFactory.ok(detranDto);	        		
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}

	@POST
	@Path("/caracteristicas")
	@ApiOperation(
			value = "Busca dados resumidos de um veículo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Veículo econtrado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/json"})
	public static Response getVeiculo(
			@Context HttpServletRequest request,
			@QueryParam("placa") String nrPlaca
		) {
		try {			
			DetranUtils detranUtils = new DetranUtils();

			JSONObject response = detranUtils.getVeiculo(nrPlaca).asJSON();
			JSONObject filter   = new JSONObject();
			if(response == null) {
				return ResponseFactory.internalServerError("Não foi possível completar a busca das características do veículo.");
			}
		
			filter.put("especieVeiculo", response.get("especie"));
			filter.put("tipoVeiculo", response.get("tipoVeiculo"));
			filter.put("categoriaVeiculo", response.get("categoriaVeiculo"));
			filter.put("marcaModelo", response.get("marcaModelo"));
			DetranResumidoDTO detranResumidoDTO = new DetranResumidoDTO.Builder(filter, true).build();
			return ResponseFactory.ok(detranResumidoDTO);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
 
}
