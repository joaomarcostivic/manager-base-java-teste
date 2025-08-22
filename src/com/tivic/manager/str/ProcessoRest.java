package com.tivic.manager.str;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.response.ResponseFactory;

@Path("/str/processo/")

public class ProcessoRest {

	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(Processo processo) {
		try {
			
			return ResponseFactory.ok("");
		} catch (Exception e) {
			return ResponseFactory.internalServerError("");
		}
	}
	
	@GET
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(Processo processo) {
		try {
			
			return ResponseFactory.ok("");
		} catch (Exception e) {
			return ResponseFactory.internalServerError("");
		}
	}


	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response save(Processo processo) {
		try {
			
			return ResponseFactory.ok("");
		} catch (Exception e) {
			return ResponseFactory.internalServerError("");
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(Processo processo) {
		try {
			
			return ResponseFactory.ok("");
		} catch (Exception e) {
			return ResponseFactory.internalServerError("");
		}
	}

	@GET
	@Path("/{id}/movimentos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response movimentos(Processo processo) {
		try {
			
			return ResponseFactory.ok("");
		} catch (Exception e) {
			return ResponseFactory.internalServerError("");
		}
	}

	@GET
	@Path("/{id}/requerente")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response requerente(Processo processo) {
		try {
			
			return ResponseFactory.ok("");
		} catch (Exception e) {
			return ResponseFactory.internalServerError("");
		}
	}

	@GET
	@Path("/{id}/ait")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ait(Processo processo) {
		try {
			
			return ResponseFactory.ok("");
		} catch (Exception e) {
			return ResponseFactory.internalServerError("");
		}
	}

}
