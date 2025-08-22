package com.tivic.manager.ptc.protocolosv3;

import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorService;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoService;
import com.tivic.manager.ptc.protocolosv3.resultado.ProtocoloResultadoService;

public class ProtocoloServiceFactory {
	public ProtocoloService getService(ProtocoloDTO protocolo) {
		switch(getTipoDocumento(protocolo)) {
//			case APRESENTACAO_CONDUTOR:
//			return new FiciService();
//			
//			case EXTERNO:
//			return new ProtocoloExternoService();
//			
//			case RESULTADO:
//			return new ProtocoloResultadoService();
//				
//			default:
//			return new ProtocoloService();
		}
		
		return null;
	}

	private int getTipoDocumento(ProtocoloDTO protocolo) {
		// TODO Auto-generated method stub
		return 0;
	}
}
