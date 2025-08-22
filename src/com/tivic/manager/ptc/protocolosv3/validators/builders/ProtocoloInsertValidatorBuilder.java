package com.tivic.manager.ptc.protocolosv3.validators.builders;

import com.tivic.manager.ptc.protocolosv3.validators.documento.AitRegistradoDetranValidator;
import com.tivic.manager.ptc.protocolosv3.validators.documento.AitValidator;
import com.tivic.manager.ptc.protocolosv3.validators.documento.DataProtocoloValidator;
import com.tivic.manager.ptc.protocolosv3.validators.documento.ExisteProtocoloTipoDocumento;
import com.tivic.manager.ptc.protocolosv3.validators.documento.ExistsValidator;
import com.tivic.manager.ptc.protocolosv3.validators.documento.FaseValidator;
import com.tivic.manager.ptc.protocolosv3.validators.documento.NumeroDocumentoValidator;
import com.tivic.manager.ptc.protocolosv3.validators.documento.SituacaoValidator;
import com.tivic.manager.ptc.protocolosv3.validators.documento.UsuarioValidator;

public class ProtocoloInsertValidatorBuilder extends ProtocoloValidatorBuilder{
	
	public ProtocoloInsertValidatorBuilder() throws Exception {
		super();
		this.addValidator(new AitRegistradoDetranValidator());
		this.addValidator(new AitValidator());
		this.addValidator(new DataProtocoloValidator());
		this.addValidator(new ExistsValidator());
		this.addValidator(new FaseValidator());
		this.addValidator(new NumeroDocumentoValidator());
		this.addValidator(new SituacaoValidator());
		this.addValidator(new UsuarioValidator());
		this.addValidator(new ExisteProtocoloTipoDocumento());
	}
}
