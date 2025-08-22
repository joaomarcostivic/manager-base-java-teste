package com.tivic.manager.ptc.protocolosv3.recursos;

import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.ptc.protocolosv3.recursos.mg.DefesaRecursoServices;

public class ProtocoloRecursoFactoryMG implements IProtocoloRecursoFactory {

	@Override
	public IProtocoloRecursoServices gerarServico(int tpStatus) throws Exception{
		switch (tpStatus) {				
			case AitMovimentoServices.DEFESA_PREVIA:
			case AitMovimentoServices.RECURSO_JARI:
			case AitMovimentoServices.APRESENTACAO_CONDUTOR:
			case AitMovimentoServices.RECURSO_CETRAN:
			case AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA:
				return new DefesaRecursoServices();
				
			default:
				throw new Exception("Serviço não implementado");
		}
	}

}
