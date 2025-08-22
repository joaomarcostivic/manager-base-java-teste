package com.tivic.manager.grl;

import java.sql.Types;
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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.util.Result;

@Path("/v2/grl/medicos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicoController {
	
	@POST
	@Path("/")
	@ApiOperation(
		value = "Grava um novo médico"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Médico salvo com sucesso"),
		@ApiResponse(code = 501, message = "Recurso ainda não disponível")
	})
	
	public static Response create(@ApiParam(value = "MEDICO a ser registrado", required = true) MedicoDTO medicoDTO) {
		try {	
			medicoDTO.setCdPessoa(0);
			
			Result r = MedicoServices.create(medicoDTO);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("MEDICO possui algum parâmetro inválido", r.getMessage());
			}
			
			return ResponseFactory.ok((MedicoDTO)r.getObjects().get("MEDICO"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
		value = "Atualiza dados de uma pessoa",
		notes = "Considere id = cdPessoa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Pessoa atualizada"),
		@ApiResponse(code = 400, message = "Pessoa nula ou inválida"),
		@ApiResponse(code = 204, message = "Pessoa não encontrada"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id do médico a ser atualizado", required = true) @PathParam("id") int cdPessoa, 
			@ApiParam(value = "Médico a ser atualizado", required = true) MedicoDTO medicoDTO) {
		
		try {
			if(medicoDTO == null) {
				return ResponseFactory.badRequest("Médico não existente na base de dados.");
			}
			
			if(PessoaServices.get(cdPessoa, null) == null) {
				return ResponseFactory.notFound("Médico não existente na base de dados.");
			}
			
			medicoDTO.setCdPessoa(cdPessoa);
			int r = MedicoServices.update(medicoDTO);
			if(r < 0) {
				return ResponseFactory.badRequest("Erro ao atualizar médico.");
			}
			
			return ResponseFactory.ok("Pessoa atualizada.");
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Internal Server Error");
		}
	}
	
	@GET
	@Path("/")
	@ApiOperation(
			value = "Retorna Médico"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Médico encontrado", response = MedicoDTO[].class),
			@ApiResponse(code = 204, message = "Médico não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response find(
			@ApiParam(value = "Código do Vínculo") @QueryParam("cdVinculo") int cdVinculo,
			@ApiParam(value = "Nome do Médico") @QueryParam("nmPessoa") String nmPessoa,
			@ApiParam(value = "Quantidade máxima de registros") @DefaultValue("50") @QueryParam("qtLimite") int qtLimite,
			@ApiParam(value = "Código do Médico") @QueryParam("cdPessoa") int cdPessoa,
			@ApiParam(value = "Código da Empresa") @QueryParam("cdEmpresa") int cdEmpresa
		) {
		try {			
			Criterios criterios = new Criterios();

			if(nmPessoa != null) {
				criterios.add("A.nm_pessoa", nmPessoa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(cdPessoa > 0) {
				criterios.add("A.cd_pessoa", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER);
			}	
			
			if(cdEmpresa > 0) {
				criterios.add("B.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER);
			}	
			
			if(cdVinculo > 0) {
				criterios.add("B.cd_vinculo", Integer.toString(cdVinculo), ItemComparator.EQUAL, Types.INTEGER);
			}
				
			List<MedicoDTO> list = MedicoServices.find(criterios);
			if(list.isEmpty()) {
				return ResponseFactory.noContent("Nenhum Pessoa encontrada");
			}
			
			return ResponseFactory.ok(list);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
		value = "Fornece um médico dado o id indicado",
		notes = "Considere id = cdPessoa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Medico encontrado", response = MedicoDTO.class),
		@ApiResponse(code = 204, message = "Não existe médico com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response get(@ApiParam(value = "id do médico") @PathParam("id") int cdPessoa) {
		try {
			Criterios crt = new Criterios();
			crt.add("A.cd_pessoa", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER);

			List<MedicoDTO> list = MedicoServices.find(crt);
			
			MedicoDTO dto = null;
			if (list!=null && list.size()>0)
				dto = list.get(0);
			
			if(dto==null)
				return ResponseFactory.noContent("Não existe médico com o id indicado.");
			
			return ResponseFactory.ok(dto);
			
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
}
