package com.tivic.manager.mob.aitmovimentodocumento.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.validators.AitValidator;
import com.tivic.manager.ptc.protocolos.validators.DtProtocoloValidator;
import com.tivic.manager.ptc.protocolos.validators.ExistsValidator;
import com.tivic.manager.ptc.protocolos.validators.FaseValidator;
import com.tivic.manager.ptc.protocolos.validators.IValidator;
import com.tivic.manager.ptc.protocolos.validators.NrDocumentoValidator;
import com.tivic.manager.ptc.protocolos.validators.SituacaoValidator;
import com.tivic.manager.ptc.protocolos.validators.UsuarioValidator;
import com.tivic.sol.connection.CustomConnection;

public class AitMovimentoDocumentoProtocoloValidator {
private List<IValidator<DadosProtocoloDTO>> validators;
	
	public AitMovimentoDocumentoProtocoloValidator() {
		this.validators = new ArrayList<IValidator<DadosProtocoloDTO>>();
		this.validators.add(new AitValidator());
		this.validators.add(new DtProtocoloValidator());
		this.validators.add(new ExistsValidator());
		this.validators.add(new NrDocumentoValidator());
		this.validators.add(new SituacaoValidator());
		this.validators.add(new UsuarioValidator());
		this.validators.add(new FaseValidator());
	}
	
	public void validate(DadosProtocoloDTO protocolo, CustomConnection connection) throws ValidationException, Exception {
		for(IValidator<DadosProtocoloDTO> validator: this.validators) {
			validator.validate(protocolo, connection);
		}
	}
}
