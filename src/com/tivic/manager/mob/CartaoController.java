package com.tivic.manager.mob;

import java.sql.Types;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

@Api(value = "Cartao", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
			@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
	})
@Path("/v2/mob/cartoes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CartaoController {

	@GET
	@Path("/find-cartoes")
	@ApiOperation(
		value = "Retorna a lista de cartões"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Cartões encontrados"),
		@ApiResponse(code = 204, message = "Nenhum cartão encontrado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response findCartoes (
		@ApiParam(value = "Inicio do periodo de validade (dd/mm/yyyy)") @QueryParam("dtValidadeInicial") String dtValidadeInicial,
		@ApiParam(value = "Fim do periodo de validade (dd/mm/yyyy)") @QueryParam("dtValidadeFinal") String dtValidadeFinal,
		@ApiParam(value = "Situacao da vistoria") @QueryParam("stCartao") String stCartao,
		@ApiParam(value = "Nome do solicitante") @QueryParam("nmSolicitante") String nmSolicitante,
		@ApiParam(value = "Número do documento") @QueryParam("nrDocumento") String nrDocumento,
		@ApiParam(value = "Código CID") @QueryParam("nmCid") String nmCid,
		@ApiParam(value = "Número do CPF") @QueryParam("nrCpf") String nrCpf,
		@ApiParam(value = "Quantidade de registros") @QueryParam("limit") @DefaultValue("50")  int nrLimite
	) {
		try {
			Criterios crt = new Criterios();

			if(nrLimite != 0) {
				crt.add("LIMIT", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(nmSolicitante != null) {
				crt.add("B.NM_SOLICITANTE", nmSolicitante, ItemComparator.LIKE, Types.VARCHAR);				
			}

			if(dtValidadeInicial != null) {
				crt.add("A.DT_VALIDADE", dtValidadeInicial, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);
			}

			if(dtValidadeFinal != null) {
				crt.add("A.DT_VALIDADE", dtValidadeFinal, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			}
			
			if(stCartao != null) {
				crt.add("F.NM_SITUACAO_DOCUMENTO", stCartao, ItemComparator.LIKE, Types.VARCHAR);				
			}
			
			if(nrDocumento != null) {
				crt.add("E.NR_DOCUMENTO", nrDocumento, ItemComparator.LIKE, Types.VARCHAR);				
			}
		
			if(nmCid != null) {
				crt.add("H.NM_CID", nmCid, ItemComparator.LIKE, Types.VARCHAR);				
			}
			
			if(nrCpf != null) {
				crt.add("G.NR_CPF", nrCpf, ItemComparator.LIKE, Types.VARCHAR);				
			}
			
			ResultSetMap rsm = CartaoServices.findRelatorio(crt);
			
			if( rsm == null || rsm.getLines().size() == 0 )
				return ResponseFactory.noContent("Nenhum cartão/solicitação encontrados");
			
			return ResponseFactory.ok( new ResultSetMapper<Cartao>(rsm, Cartao.class));	
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
}

