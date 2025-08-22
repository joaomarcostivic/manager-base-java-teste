package com.tivic.manager.prc;

import java.sql.Types;
import java.util.GregorianCalendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.Util;

import ch.qos.logback.core.filter.Filter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "ADVOGADO", tags = {"prc"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/prc/partes")
@Produces(MediaType.APPLICATION_JSON)
public class PartesController {
	
	@GET
	@Path("/")
	@ApiOperation(
		value = "Fornece lista de AIT"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista fornecida"),
		@ApiResponse(code = 204, message = "Sem resultado"),
		@ApiResponse(code = 400, message = "Parâmetro nulo ou inválido"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response get(
	@ApiParam(value = "Parte de nome das pessoas a serem retornadas") @QueryParam("nome") String nome,
	@ApiParam(value = "Tipo de parte a ser retornada") @QueryParam("tipo") String tipo,
	@ApiParam(value = "Quantidade máxima de registros", defaultValue = "100") @DefaultValue("100") @QueryParam("limit") int limit) {
		try {			
			int cdVinculo = 0;
			int cdVinculoCliente = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", -10, 0);
			int cdVinculoAdverso = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_ADVERSO", -20);

			Criterios crt = new Criterios()
				    .add("qtLimite", Integer.toString(limit), ItemComparator.EQUAL, Types.INTEGER);
			
			switch(tipo.trim()) {
				case "cliente": cdVinculo = cdVinculoCliente; break;
				case "adverso": cdVinculo = cdVinculoAdverso; break;
			}
			
			if(cdVinculo != 0) {
				crt.add("B.cd_vinculo", Integer.toString(cdVinculo), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(nome != null && !nome.trim().equals("")) {
				crt.add("A.nm_pessoa", nome, ItemComparator.LIKE_BEGIN, Types.VARCHAR);
			}
			
			ResultSetMap rsm = PessoaServices.findByVinculo(crt);
			
			if(rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent();
			}
			
			return ResponseFactory.ok(rsm);			
		} catch(Exception e) {
			System.out.println(e.toString());
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
}
