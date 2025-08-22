package com.tivic.manager.ptc.protocolosv3.factories;

import com.tivic.manager.ptc.protocolosv3.IProtocoloDTOGet;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.ptc.protocolosv3.services.DefesaServices;

public class ProtocoloGetFactory {
	public IProtocoloDTOGet strategy(int tpDocumento) throws Exception{
		if(tpDocumento == TipoDocumentoProtocoloEnum.DEFESA_PREVIA.getKey()) {
			return new DefesaServices();
		}
		else if(tpDocumento == TipoDocumentoProtocoloEnum.DEFESA_PREVIA_ADVERTENCIA.getKey()) {
			return new DefesaServices();
		}
		else if(tpDocumento == TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey()) {
			return new DefesaServices();
		}
		else if(tpDocumento == TipoDocumentoProtocoloEnum.RECURSO_CETRAN.getKey()) {
			return new DefesaServices();
		}

		throw new Exception("Serviço não implementado");

	}
}
