package com.tivic.manager.auth.mobile;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.auth.mobile.validator.AuthMobileValidatorBuilder;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.IEquipamentoService;
import com.tivic.manager.mob.acaousuario.AcaoUsuarioDTO;
import com.tivic.manager.mob.acaousuario.IAcaoUsuarioService;
import com.tivic.manager.mob.ait.sync.builders.AitSyncDTOBuilder;
import com.tivic.manager.mob.ait.sync.entities.UserManagerDTO;
import com.tivic.sol.auth.IAuthService;
import com.tivic.sol.auth.UserManager;
import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.auth.mobile.AuthorizationException;
import com.tivic.sol.auth.usuario.IUsuarioService;
import com.tivic.sol.auth.usuario.Usuario;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.cdi.ScopeEnum;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.log.builders.InfoLogBuilder;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Login mobile", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/auth/mobile")
@Produces(MediaType.APPLICATION_JSON)
public class AuthMobileController {
	
	private ManagerLog managerLog;
	private IUsuarioService usuarioService;
	private IAuthService authMobileService;
	private IAcaoUsuarioService acaoUsuarioService;
	private IEquipamentoService equipamentoSerivce;
	
	public AuthMobileController() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		usuarioService = (IUsuarioService) BeansFactory.get(IUsuarioService.class);
		authMobileService = (IAuthService) BeansFactory.get(IAuthService.class, ScopeEnum.MOBILE);
		acaoUsuarioService = (IAcaoUsuarioService) BeansFactory.get(IAcaoUsuarioService.class);
		equipamentoSerivce = (IEquipamentoService) BeansFactory.get(IEquipamentoService.class);
	}
	
	@POST
	@Path("/login")
	@ApiOperation(value = "Login no app")
	@JWTIgnore
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Login realizado com sucesso", response = ResponseBody.class),
		@ApiResponse(code = 204, message = "Usuário não encontrado", response = ResponseBody.class),
		@ApiResponse(code = 401, message = "Login ou senha inválida", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response Login(AuthMobile auth){
		try {
			managerLog.showLog(new InfoLogBuilder("[POST] /login", "Validando usuário...").build());
			Equipamento equipamento = equipamentoSerivce.getByIdEquipamento(auth.getIdEquipamento());
			new AuthMobileValidatorBuilder().validate(auth, equipamento);
			auth.setCdEquipamento(equipamento.getCdEquipamento());
			UserManager userManager = authMobileService.login(auth);
			UserManagerDTO user = new AitSyncDTOBuilder().setUser(userManager, equipamento.getCdEquipamento());
			managerLog.showLog(new InfoLogBuilder("[POST] /login", "Usuário logado com sucesso...").build());
			return ResponseFactory.ok(user);
		} catch (AuthorizationException e) {
			e.printStackTrace();
			managerLog.showLog(e);
			return ResponseFactory.unauthorized(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		} 
	}	
	
	@POST
	@Path("/logout")
	@ApiOperation(value = "Logout no app")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Logout realizado com sucesso", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response Logout(@ApiParam(value = "Objeto de ação do usuário") List<AcaoUsuarioDTO> acaoUsuarioList){
		try {
			managerLog.showLog(new InfoLogBuilder("[POST] /logout", "Saindo do sistema...").build());
			acaoUsuarioService.insertAll(acaoUsuarioList);
			Usuario usuario = usuarioService.get(acaoUsuarioList.get(0).getCdUsuario());
			authMobileService.logout(usuario);
			managerLog.showLog(new InfoLogBuilder("[POST] /logout", "Usuario deslogado com sucesso...").build());
			return ResponseFactory.ok("Usuário deslogado com sucesso!" );
		} catch (AuthorizationException e) {
			managerLog.showLog(e);
			return ResponseFactory.unauthorized(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
