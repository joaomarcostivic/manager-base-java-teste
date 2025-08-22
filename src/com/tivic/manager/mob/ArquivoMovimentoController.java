package com.tivic.manager.mob;

import java.sql.Types;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

@Api(value = "ArquivoMovimento", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/arquivosmovimento")
@Produces(MediaType.APPLICATION_JSON)
public class ArquivoMovimentoController {

	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Arquivo Movimento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivos encontrados", response = ArquivoMovimento[].class),
			@ApiResponse(code = 204, message = "Arquivos não encontrados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "ID do Ait") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Status do Movimento") @QueryParam("tpStatus") Integer tpStatus,
			@ApiParam(value = "Tipo de retorno") @QueryParam("nrErro") String nrErro,
			@ApiParam(value = "Data de Envio") @QueryParam("dtEnvio") String dtEnvio ) {
		try {
			
			Criterios criterios = new Criterios();
			if(idAit != null) {
				criterios.add("AIT.id_ait","%"  + idAit + "%" , ItemComparator.LIKE, Types.VARCHAR);
			}
			if(tpStatus != null) {
				criterios.add("AIT_MOV.tp_status", String.valueOf(tpStatus), ItemComparator.EQUAL, Types.INTEGER);
			}	
			if(nrErro != null) {
				criterios.add("ARQ_MOV.nr_erro", String.valueOf(Integer.parseInt(nrErro)), ItemComparator.EQUAL, Types.VARCHAR);
			}	
			if(dtEnvio != null) {
				criterios.addEqualDate("ARQ_MOV.dt_arquivo", Util.convStringToCalendar(dtEnvio));
			}
			
			criterios.add("ERRO.uf", "MG", ItemComparator.EQUAL, Types.CHAR);
			
			
			ResultSetMap _rsm = ArquivoMovimentoServices.find(criterios);
			
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum arquivo encontrado");
			}
			
			return ResponseFactory.ok(
					new ArquivoMovimentoDTO.ListBuilder(_rsm)
					.setAit(_rsm)
					.setMovimento(_rsm)
					.setErro(_rsm).build());
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	private static String tratarNrErro(String nrErro){
		String primeiroCaractere = "0";
		while(primeiroCaractere.equals("0") && nrErro.length() > 1){
			primeiroCaractere = nrErro.substring(0, 1);
			nrErro = nrErro.substring(1);
		}
		
		if(!primeiroCaractere.equals("0"))
			nrErro = primeiroCaractere + nrErro;
		
		return nrErro;
	}
}
