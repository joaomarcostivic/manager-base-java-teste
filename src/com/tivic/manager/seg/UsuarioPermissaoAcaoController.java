package com.tivic.manager.seg;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Usuários", tags = {"seg"})
@Path("/v2/seg/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON) 
public class UsuarioPermissaoAcaoController {

	@GET
	@Path("/{id}/permissoes")
	@ApiOperation(
			value = "Retorna as permissoes de um usuário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Permissões encontradas", response = Object.class),
			@ApiResponse(code = 204, message = "Permissões não encontradas", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getPermissoes(
			@ApiParam(value = "Código do usuário") @PathParam("id") int cdUsuario,
			@ApiParam(value = "Cód. sistema", defaultValue = "0") @DefaultValue("0") @QueryParam("sistema") int cdSistema,
			@ApiParam(value = "Cód. módulo", defaultValue = "0") @DefaultValue("0") @QueryParam("modulo") int cdModulo,
			@ApiParam(value = "Cód. agrupamento", defaultValue = "0") @DefaultValue("0") @QueryParam("agrupamento") int cdAgrupamento
			) {
		try {			
			return ResponseFactory.ok(UsuarioServices.getPermissoesAcao(cdUsuario, cdSistema, cdModulo, cdAgrupamento));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}/permissoes")
	@ApiOperation(
			value = "Retorna as permissoes de um usuário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Permissão adicionada", response = UsuarioPermissaoAcao.class),
			@ApiResponse(code = 204, message = "Erro ao incluir permissão", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response setPermissao(
			@ApiParam(value = "Código do usuário") @PathParam("id") int cdUsuario,
			@ApiParam(value = "Permissão a ser atualizada") UsuarioPermissaoAcao permissao
			) {				
		try {			
			return ResponseFactory.ok(UsuarioServices.adicionarPermissaoAcao(cdUsuario, permissao.getCdAcao(), permissao.getCdModulo(), permissao.getCdSistema(), permissao.getLgNatureza()));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
}
