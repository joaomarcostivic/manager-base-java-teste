package com.tivic.manager.auth.mobile.validator;

import com.tivic.manager.auth.mobile.AuthMobile;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;

public class UsuarioPossuiAgenteValidator implements IAuthMobileValidator {
    
    private final ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;

    public UsuarioPossuiAgenteValidator() throws Exception {
        this.conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
    }

    @Override
    public void validate(AuthMobile auth, Equipamento equipamento) throws Exception, ValidacaoException {
        Usuario usuario = conversorBaseAntigaNovaFactory.getUsuarioRepository().getByLogin(auth.getLogin());
        if (usuario == null)
            throw new ValidacaoException("Usuário não encontrado.");
        Agente agente = conversorBaseAntigaNovaFactory.getAgenteRepository().getByCdUsuario(usuario.getCdUsuario());
        if (agente == null)
            throw new ValidacaoException("Usuário não possui agente vinculado.");
    }
}
