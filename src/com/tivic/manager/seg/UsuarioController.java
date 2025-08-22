package com.tivic.manager.seg;

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

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.manager.grl.PessoaEmpresaServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.Vinculo;
import com.tivic.manager.grl.VinculoDAO;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "Usuários", tags = {"seg"})
@Path("/v2/seg/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON) 
public class UsuarioController {

	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo Usuário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuário registrado", response = Usuario.class),
			@ApiResponse(code = 400, message = "Usuário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Usuário a ser registrado") Usuario usuario) {
		try {
			usuario.setCdUsuario(0);
			
			Usuario res = UsuarioServices.getByNmLogin(usuario.getNmLogin(), null);
			
			if(res != null)
				return ResponseFactory.badRequest("Já existe um usuário com esse nome");
			
			Result result = UsuarioServices.save(usuario);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			
			return ResponseFactory.ok((Usuario)result.getObjects().get("USUARIO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/usuariopessoa")
	@ApiOperation(
			value = "Registra um novo Usuário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuário registrado", response = Usuario.class),
			@ApiResponse(code = 400, message = "Usuário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Usuário a ser registrado") UsuarioPessoaDTO _dto) {
		Result _res = UsuarioServices.insertDTO(_dto);
		
		if(_res.getCode() < 0)
			return ResponseFactory.badRequest(_res.getMessage());
		
		return ResponseFactory.ok(_dto);
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um Usuário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuário atualizado", response = Usuario.class),
			@ApiResponse(code = 400, message = "Usuário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Código do usuário") @PathParam("id") int cdUsuario,
			@ApiParam(value = "Usuário a ser atualizado") Usuario usuario) {
		try {
			usuario.setCdUsuario(cdUsuario);
			
			Usuario res = UsuarioServices.getByNmLogin(usuario.getNmLogin(), null);
			
			if(res != null && res.getCdUsuario() != usuario.getCdUsuario())
				return ResponseFactory.badRequest("Já existe um usuário com esse nome");
			
			Result result = UsuarioServices.save(usuario);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			
			return ResponseFactory.ok((Usuario)result.getObjects().get("USUARIO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um Usuário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuário encontrado", response = Usuario.class),
			@ApiResponse(code = 204, message = "Usuário não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código do usuário") @PathParam("id") int cdUsuario) {
		try {
			Usuario usuario = UsuarioDAO.get(cdUsuario);
			if(usuario == null)
				return ResponseFactory.noContent("Usuário não encontrado");
			
//			ResultSetMap rsm = UsuarioServices.find(new Criterios("A.cd_usuario", Integer.toString(cdUsuario), ItemComparator.EQUAL, Types.INTEGER));
//			if(!rsm.next())
//				return ResponseFactory.noContent("Usuário não encontrado");
			
			return ResponseFactory.ok(usuario);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/dto/{id}")
	@ApiOperation(
			value = "Retorna um Usuário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuário encontrado", response = Usuario.class),
			@ApiResponse(code = 204, message = "Usuário não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getDto(@ApiParam(value = "Código do usuário") @PathParam("id") int cdUsuario) {
		try {
			Usuario usuario = UsuarioDAO.get(cdUsuario);
			UsuarioPessoaDTO _dto = new UsuarioPessoaDTO();
			
			if(usuario.getCdPessoa() > 0) {
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa());
				PessoaFisica pessoaFisica = PessoaFisicaDAO.get(pessoa.getCdPessoa());
				PessoaEmpresa pessoaEmpresa = PessoaEmpresaServices.getPessoaEmpresaByPessoa(pessoa.getCdPessoa());
				if(pessoaEmpresa != null) {
					Vinculo vinculo = VinculoDAO.get(pessoaEmpresa.getCdVinculo());
					_dto.setVinculo(vinculo);
				}
				
				_dto.setPessoa(pessoa);
				_dto.setPessoaFisica(pessoaFisica);
				_dto.setUsuario(usuario);
			} else {
				_dto.setUsuario(usuario);
			}
			
			if(usuario == null)
				return ResponseFactory.noContent("Usuário não encontrado");
			
			return ResponseFactory.ok(_dto);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
		
	@GET
	@Path("/{cdUsuario}/{idModulo}/getempresasofusuariomodulo")
	@ApiOperation(
			value = "Busca de empresas"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = UsuarioDTO.class),
			@ApiResponse(code = 400, message = "Erro na requisição", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Nenhuma empresa encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getEmpresaOfUsuarioModulo(
			//@ApiParam(value = "Código do usuário") @PathParam("cdUsuario") int cdUsuario,
			//@ApiParam(value = "Código do usuário") @PathParam("idModulo") String idModulo
			@QueryParam("cdUsuario") int cdUsuario, 
			@QueryParam("idModulo") String idModulo
		
	){
		try {	
			
			if(cdUsuario <= 0) 
				return ResponseFactory.badRequest("Código do usuário é nulo ou inválido");
			if(idModulo.equals("")) 
				return ResponseFactory.badRequest("ID do módulo é nulo ou inválido");
			
			Modulo modulo = ModuloServices.getModuloById(idModulo);
			ResultSetMap rsm = UsuarioServices.getEmpresaOfUsuarioModulo(cdUsuario, modulo.getCdSistema(), modulo.getCdModulo(), false, 0, idModulo, 0, null);
			if(rsm == null){
				return ResponseFactory.noContent("Nenhuma empresa encontrada");
			}
			
			//ArrayList<InstituicaoDTO> instituicaoDto = (ArrayList<InstituicaoDTO>)new InstituicaoDTO.ListBuilder(rsm).setEmpresa(rsm).build();
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
	    	e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna uma lista de Usuários"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuários encontrados", response = UsuarioDTO[].class),
			@ApiResponse(code = 400, message = "Requisição inválida", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Nenhum Usuário encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Nome da Pessoa") @QueryParam("pessoa") String nmPessoa,
			@ApiParam(value = "Nome de Usuário") @QueryParam("usuario") String nmUsuario ) {
		try {
			
			Criterios crt = new Criterios();
			
			if(nmPessoa != null) {
				crt.add("B.nm_pessoa", nmPessoa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if(nmUsuario != null) {
				crt.add("A.nm_login", nmUsuario, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			ResultSetMap rsm = UsuarioServices.find(crt, null);
			if(!rsm.next())
				return ResponseFactory.noContent("Usuário não encontrado");
			
			List<UsuarioDTO> usuarios = new UsuarioDTO.ListBuilder(rsm).setPessoa(rsm).setAgente(rsm).build();
			
			return ResponseFactory.ok(usuarios);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/dto")
	@ApiOperation(
			value = "Retorna uma lista de Usuários"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuários encontrados", response = UsuarioDTO[].class),
			@ApiResponse(code = 400, message = "Requisição inválida", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Nenhum Usuário encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAllDto(
			@ApiParam(value = "Nome da Pessoa") @QueryParam("pessoa") String nmPessoa,
			@ApiParam(value = "Nome de Usuário") @QueryParam("usuario") String nmUsuario,
			@ApiParam(value = "Email da Pessoa") @QueryParam("email") String nmEmail,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "200") @DefaultValue("200") @QueryParam("limit") int limit) {
		try {
			
			if(limit <= 0)
				return ResponseFactory.badRequest("Limite deve ser maior que 0.");
			
			Criterios crt = new Criterios("limit", Integer.toString(limit), 0, 0);

			if(nmUsuario != null) 
				crt.add("A.nm_login", nmUsuario, ItemComparator.LIKE_ANY, Types.VARCHAR);
			
			if(nmEmail != null) 
				crt.add("B.nm_email", nmEmail, ItemComparator.LIKE_ANY, Types.VARCHAR);
			
			if(nmPessoa != null) 
				crt.add("B.nm_pessoa", nmPessoa, ItemComparator.LIKE_ANY, Types.VARCHAR);			
			
			ResultSetMap rsm = UsuarioServices.findDto(crt, null);
				
			if(!rsm.next())
				return ResponseFactory.noContent("Usuário não encontrado");
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}/pass")
	@ApiOperation(
			value = "Alteração de Senha de Usuário"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Senha alterada", response = UsuarioDTO[].class),
			@ApiResponse(code = 400, message = "Requisição inválida", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Nenhum Usuário encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response setPassword(@ApiParam( value = "Código do Usuário" ) @PathParam("id") int cdUsuario, @ApiParam (value = "Usuário") Usuario usuario) {
		try {
			Usuario upd = UsuarioServices.getByCdUsuario(cdUsuario);
			
			int result = UsuarioServices.setNovaSenha(usuario.getNmLogin(), upd.getNmSenha(), usuario.getNmSenha());
			
			return ResponseFactory.ok(result == 1 ? "Senha alterada com Sucesso" : "Erro ao alterar Senha");
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/vinculo")
	@ApiOperation(
			value = "Retorna uma lista de Usuários"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuários encontrados", response = UsuarioDTO[].class),
			@ApiResponse(code = 400, message = "Requisição inválida", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Nenhum Usuário encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAllByRelator() {
		try {
			
			ResultSetMap rsm = UsuarioServices.getAllByVinculo("CD_VINCULO_RELATOR");
				
			if(!rsm.next())
				return ResponseFactory.noContent("Usuários não encontrados");
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
		
	
}
