package com.tivic.manager.auth.mobile.validator;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.auth.mobile.AuthMobile;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.mob.EquipamentoServices;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class EquipamentoStatusValidator implements IAuthMobileValidator {

    @Override
    public void validate(AuthMobile auth, Equipamento equipamento) throws Exception, ValidacaoException {
        if(equipamento.getStEquipamento() != EquipamentoServices.ATIVO) {
        	throw new BadRequestException("Este equipamento não está ativo.");
		}
    }
}
