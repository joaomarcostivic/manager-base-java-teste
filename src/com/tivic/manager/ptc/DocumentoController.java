package com.tivic.manager.ptc;

import java.sql.Types;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Cartao;
import com.tivic.manager.mob.CartaoServices;
import com.tivic.manager.rest.request.filter.Criterios;
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

@Api(value = "Solicitações", tags = { "ptc" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v2/ptc/gratuidade")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DocumentoController {

	@GET
	@Path("")
	@ApiOperation(value = "Retorna a lista de solicitações")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Solicitações encontrados"),
			@ApiResponse(code = 204, message = "Nenhuma solicitação encontrada"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public static Response findSolicitacoes(
			@ApiParam(value = "Situacao da vistoria") @QueryParam("stDocumento") String stDocumento,
			@ApiParam(value = "Nome do solicitante") @QueryParam("cdPessoa") int cdPessoa,
			@ApiParam(value = "Número do Documento") @QueryParam("nrDocumento") String nrDocumento,
			@ApiParam(value = "Número do CPF") @QueryParam("nrCpf") String nrCpf,
			@ApiParam(value = "Data da solicitação dd/MM/yyyy") @QueryParam("dtProtocolo") String dtProtocolo,
			@ApiParam(value = "Com ou Sem pericia",  allowableValues = "0, 1") @QueryParam("tpPericia") @DefaultValue("-1") int tpPericia,
			@ApiParam(value = "Quantidade de registros") @QueryParam("limit") @DefaultValue("50") int nrLimite) {
		try {
			Criterios crt = new Criterios();
			
			if (nrLimite != 0) {
				crt.add("LIMIT", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);
			}

			if (cdPessoa != 0) {
				crt.add("B.CD_PESSOA", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if (stDocumento != null) {
				crt.add("F.NM_SITUACAO_DOCUMENTO", stDocumento, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nrDocumento != null) {
				crt.add("E.NR_DOCUMENTO", nrDocumento, ItemComparator.LIKE, Types.VARCHAR);
			}

			if (nrCpf != null) {
				crt.add("G.NR_CPF", nrCpf, ItemComparator.LIKE, Types.VARCHAR);
			}
			
			if(tpPericia > -1) {
				crt.add("E.TP_INTERNO_EXTERNO", Integer.toString(tpPericia), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if (dtProtocolo != null) {
				crt.add("E.DT_PROTOCOLO", dtProtocolo + " 00:00", ItemComparator.GREATER_EQUAL, Types.DATE);
				crt.add("E.DT_PROTOCOLO", dtProtocolo + " 23:59", ItemComparator.MINOR_EQUAL, Types.DATE);
			}

			List<DocumentoDTO> list = DocumentoServices.findSolicitacoesDTO(crt);
			

			if (list.isEmpty())
				return ResponseFactory.noContent("Nenhum cartão/solicitação encontrados");

			return ResponseFactory.ok(list);

		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
}
