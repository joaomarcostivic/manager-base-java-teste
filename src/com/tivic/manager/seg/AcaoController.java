package com.tivic.manager.seg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jdt.internal.compiler.util.Util;
import org.xml.sax.InputSource;

import com.tivic.manager.seg.Acao;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

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

@Api(value = "Ações", tags = {"seg"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})

@Path("/v2/seg/acoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AcaoController {
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna lista de Ações"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ações encontradas", response = Acao.class),
			@ApiResponse(code = 400, message = "Nenhuma ação encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response getAll() {
		try {
			ResultSetMap rsm = AcaoServices.getAll();
			
			ResultSetMapper<Acao> rsmConv = new ResultSetMapper<Acao>(rsm, Acao.class);			

			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhuma ação encontrada");
			}
			
			return ResponseFactory.ok(rsmConv);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/find")
	@ApiOperation(
			value = "Retorna uma lista de ações filtrada"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ações econtradas", response = Acao.class),
			@ApiResponse(code = 400, message = "Nenhuma ação encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static ResultSetMap get(ArrayList<ItemComparator> criterios) {		
		try {

			ResultSetMap rsm = AcaoServices.find(criterios);
			return rsm;			

		} catch (Exception e) {
			
			e.printStackTrace(System.out);
			return null;
			
		}
	}
	
	@GET
	@Path("/findByGrupo/{sistema}/{modulo}/{agrupamento}")
	@ApiOperation(
			value = "Retorna uma lista de ações filtrada por agrupamento"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ações econtrada", response = Acao.class),
			@ApiResponse(code = 400, message = "Nenhuma ação encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response get(@PathParam("sistema") int cdSistema, @PathParam("modulo") int cdModulo, @PathParam("agrupamento") int cdAgrupamento) {		
		try {

			ResultSetMap rsm = AcaoServices.findByAgrupamento(cdSistema, cdModulo, cdAgrupamento);
			
			if(rsm.getLines().size() <= 0)
				return ResponseFactory.noContent("Não foram encontradas ações");
			
			ResultSetMapper<Acao> rsmConv = new ResultSetMapper<Acao>(rsm, Acao.class);
			
			return ResponseFactory.ok(rsmConv);			

		} catch (Exception e) {
			
			return ResponseFactory.internalServerError(e.getMessage());
			
		}
	}
	
	@GET
	@Path("/find/{id}/{sistema}/{modulo}/{agrupamento}")
	@ApiOperation(
			value = "Retorna uma lista de ações filtrada por agrupamento"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ações econtrada", response = Acao.class),
			@ApiResponse(code = 400, message = "Nenhuma ação encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response find(@PathParam("id") int cdUsuario, @PathParam("sistema") int cdSistema, @PathParam("modulo") int cdModulo, @PathParam("agrupamento") int cdAgrupamento) {		
		try {

			ResultSetMap rsm = UsuarioServices.getPermissoesAcao(cdUsuario, cdSistema, cdModulo, cdAgrupamento);
			
			if(rsm.getLines().size() <= 0)
				return ResponseFactory.noContent("Não foram encontradas ações");
			
			ResultSetMapper<Acao> rsmConv = new ResultSetMapper<Acao>(rsm, Acao.class);
			
			return ResponseFactory.ok(rsmConv);			

		} catch (Exception e) {
			
			return ResponseFactory.internalServerError(e.getMessage());
			
		}
	}
	
	@GET
	@Path("/permissao/{cdUsuario}/{idAcao}")
	@ApiOperation(
			value = "Retorna uma ação por usuário e id"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ações econtrada", response = Acao.class),
			@ApiResponse(code = 400, message = "Nenhuma ação encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response getPermissao(@PathParam("cdUsuario") int cdUsuario, @PathParam("idAcao") String idAcao) {		
		try {
			System.out.println("cdUsuario: " + cdUsuario + " | idAcao: " + idAcao);			

			boolean permissao = UsuarioServices.getPermissao(cdUsuario, idAcao);
			
			return ResponseFactory.ok(permissao ? "true" : "false");			

		} catch (Exception e) {
			
			return ResponseFactory.internalServerError(e.getMessage());
			
		}
	}
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova ação"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ação registrada", response = Acao.class),
			@ApiResponse(code = 400, message = "Ação inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response create(@ApiParam(value = "Ação a ser registrada") Acao acao) {		
		try {
			Result result = AcaoServices.save(acao);
			
			if(result.getCode() < 0) 
				return ResponseFactory.badRequest(result.getMessage());
			
			return ResponseFactory.ok(result.getObjects().get("ACAO"));			

		} catch (Exception e) {			
			return ResponseFactory.internalServerError(e.getMessage());			
		}
	}
	
	@POST
	@Path("/sync")
	@ApiOperation(
			value = "Sincroniza Operações"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ações sincronizadas", response = Acao.class),
			@ApiResponse(code = 400, message = "Erro ao sincronizar", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static boolean syncPermissoes() {		
		try {
			String path = "/com/tivic/manager/xml/permissoes-mob.xml";
			
			URL url = AcaoServices.class.getResource(path);			
			String np = url.toString().replace("file:/", "");
			
			File file = new File(np);
						
			AcaoServices.initPermissoesMob(1, 27, file, null);
			
			return true;			

		} catch (Exception e1) {
			System.out.println(e1.getMessage());
			return false;
		}
	}

}
