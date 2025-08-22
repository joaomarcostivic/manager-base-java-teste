package com.tivic.manager.fix.mob;

import java.io.IOException;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.fix.mob.ait.AitPagamentoFix;
import com.tivic.manager.fix.mob.ait.FixAitServices;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;

@Api(value = "fix", tags = {"fix"})
@Path("/v2/fix/mob")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FixController {

	@PUT
	@Path("/ait/bairro")
	public Response fixAitBairro() {
		
		FixAitServices services = new FixAitServices();
		
		Optional<String> error = services.fixAitBairro();
		
		if(error.isPresent())
			return ResponseFactory.internalServerError(error.get());
		
		
		return ResponseFactory.ok("Fixed");
	}
	
	@POST
	@Path("/ait/cancelamentos")
	public Response fixAitCancelamento() throws IOException {
		FixAitServices services = new FixAitServices();
		Optional<String> error = services.fixCancelaAit();
		
		if(error.isPresent())
			return ResponseFactory.internalServerError(error.get());
		
		
		return ResponseFactory.ok("Fixed");
	}
	
	@POST
	@Path("/aits/pagamentos")
	public Response setPagamento(AitPagamentoFix pagamento) {
		try {

			FixAitServices services = new FixAitServices();
			
			Optional<String> err = services.setPagamento(pagamento);
			
			if(err.isPresent())
				return ResponseFactory.badRequest(err.get());
						
			return ResponseFactory.ok("Lançado!");
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/aits/detran")
	public Response updateAitDetran() {
		try {
			FixAitServices services = new FixAitServices();
			
			Optional<String> err = services.updateAitDetran();
			if(err.isPresent())
				return ResponseFactory.internalServerError(err.get());
						
			return ResponseFactory.ok("Concluído!");
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/aits/bairro")
	public Response updateAitsSemBairro() {
		try {
			FixAitServices services = new FixAitServices();
			
			Optional<String> err = services.updateAitsSemBairro();
			if(err.isPresent())
				return ResponseFactory.internalServerError(err.get());
			
			System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
						
			return ResponseFactory.ok("Fix Concluído !");
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	

}
