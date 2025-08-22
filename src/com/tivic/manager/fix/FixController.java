package com.tivic.manager.fix;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.fix.Fix;
import com.tivic.sol.fix.IFixService;
import com.tivic.sol.permission.IdentifierPermission;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "fix", tags = {"fix"})
@Path("/v3/sis/fix")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FixController {

	private FixMapper fixMapper;
	private IFixService fixService;
	
	public FixController() throws Exception {
		this.fixMapper = new FixMapper();
		this.fixService = (IFixService) BeansFactory.get(IFixService.class);
	}
	
	@POST
	@Path("/executor")
	@IdentifierPermission(identifier = "SEG.PERMISSAO_FIX")
	public Response fix(
		@QueryParam(value = "idFix") String idFix
	) throws IOException {
		try {
			this.fixMapper.execute(idFix);
			return ResponseFactory.ok("Fixed");
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@POST
	@Path("/")
	@ApiOperation(value = "Registra uma nova fix")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fix registrada", response = Fix.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response create(@ApiParam(value = "Fix a ser registrada") Fix fix) {
		try {
			this.fixService.insert(fix);
			return ResponseFactory.ok(fix);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{cdFix}")
	@ApiOperation(value = "Atualiza uma fix")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fix atualizada", response = Fix.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response update(
			@ApiParam(value = "Codigo da fix a ser atualizada") @PathParam("cdFix") int cdFix, 
			@ApiParam(value = "Fix a ser atualizada") Fix fix) {
		try {
			fix.setCdFix(cdFix);
			this.fixService.update(fix);
			return ResponseFactory.ok(fix);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{cdFix}")
	@ApiOperation(value = "Busca uma fix")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fix buscada", response = Fix.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response get(@ApiParam(value = "Codigo da fix a ser buscada") @PathParam("cdFix") int cdFix) {
		try {
			Fix fix = this.fixService.get(cdFix);
			return ResponseFactory.ok(fix);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/")
	@ApiOperation(value = "Busca uma lista de fixs")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fixs buscadas", response = Fix.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response find() {
		try {
			List<Fix> fixs = this.fixService.find(new SearchCriterios());
			return ResponseFactory.ok(fixs);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@DELETE
	@Path("/{cdFix}")
	@ApiOperation(value = "Busca uma fix")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fix buscada", response = Fix.class),
							@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) 
	})
	public Response delete(
			@ApiParam(value = "Codigo da fix a ser removida") @PathParam("cdFix") int cdFix) {
		try {
			Fix fix = this.fixService.delete(cdFix);
			return ResponseFactory.ok(fix);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	
}
