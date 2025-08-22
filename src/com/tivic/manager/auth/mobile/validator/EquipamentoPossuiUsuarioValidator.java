package com.tivic.manager.auth.mobile.validator;

import java.util.List;

import com.tivic.manager.auth.mobile.AuthMobile;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.SearchCriterios;

public class EquipamentoPossuiUsuarioValidator implements IAuthMobileValidator {
    
    private ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
    
    public EquipamentoPossuiUsuarioValidator() throws Exception {
        conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
    }

    @Override
    public void validate(AuthMobile auth, Equipamento equipamento) throws Exception, ValidacaoException {
        Usuario usuario = verificarEquipamentoVinculadoAOutroUsuario(equipamento.getCdEquipamento());
        if (usuario != null && usuario.getNmLogin() != null && !usuario.getNmLogin().equalsIgnoreCase(auth.getLogin()))
            validarEquipamentoVinculadoAOutroUsuario(usuario, equipamento);
    }


    private Usuario verificarEquipamentoVinculadoAOutroUsuario(int cdEquipamento) throws Exception {
        SearchCriterios searchCriterios = new SearchCriterios();
        searchCriterios.addCriteriosEqualInteger("cd_equipamento", cdEquipamento);
        List<Usuario> usuarioList = conversorBaseAntigaNovaFactory.getUsuarioRepository().find(searchCriterios);
        
        return usuarioList.isEmpty() ? null : usuarioList.get(0);
    }

    private void validarEquipamentoVinculadoAOutroUsuario(Usuario usuario, Equipamento equipamento) throws Exception {
        Agente agente = conversorBaseAntigaNovaFactory.getAgenteRepository().getByCdUsuario(usuario.getCdUsuario());
        if (agente != null) {
            throw new Exception(String.format(
                "O equipamento '%s' já está vinculado ao agente de matrícula '%s'",
                equipamento.getNmEquipamento(),
                agente.getNrMatricula()
            ));
        }
    }
}
