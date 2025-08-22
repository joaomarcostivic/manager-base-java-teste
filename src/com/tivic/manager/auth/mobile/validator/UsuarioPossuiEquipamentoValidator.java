package com.tivic.manager.auth.mobile.validator;

import com.tivic.manager.auth.mobile.AuthMobile;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.repository.EquipamentoRepository;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class UsuarioPossuiEquipamentoValidator implements IAuthMobileValidator {
	
	private ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
	private EquipamentoRepository equipamentoRepository;
	
	public UsuarioPossuiEquipamentoValidator() throws Exception {
		conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
		equipamentoRepository = (EquipamentoRepository) BeansFactory.get(EquipamentoRepository.class);
	}

	@Override
	public void validate(AuthMobile auth, Equipamento equipamento) throws Exception, ValidacaoException {
	    Usuario usuario = conversorBaseAntigaNovaFactory.getUsuarioRepository().getByLogin(auth.getLogin());
	    if (usuario != null && usuario.getCdEquipamento() > 0)
	        validarUsuarioVinculadoAEquipamento(usuario, equipamento);
	}

	private void validarUsuarioVinculadoAEquipamento(Usuario usuario, Equipamento equipamentoAtual) throws Exception {
	    Equipamento equipamentoVinculado = equipamentoRepository.get(usuario.getCdEquipamento(), new CustomConnection());

	    if (equipamentoVinculado == null)
	        throw new Exception("Equipamento não localizado para o usuário encontrado.");

	    Agente agente = conversorBaseAntigaNovaFactory.getAgenteRepository().getByCdUsuario(usuario.getCdUsuario());

	    if (agente != null) {
	        if (usuario.getCdEquipamento() != equipamentoAtual.getCdEquipamento()) {
	            throw new Exception(String.format(
	                "O agente '%s' já está autenticado no talonário '%s'",
	                agente.getNmAgente(),
	                equipamentoVinculado.getNmEquipamento()
	            ));
	        }
	    }
	}
}
