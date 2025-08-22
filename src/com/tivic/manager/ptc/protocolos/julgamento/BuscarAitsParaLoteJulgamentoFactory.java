package com.tivic.manager.ptc.protocolos.julgamento;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class BuscarAitsParaLoteJulgamentoFactory {
	
	public IBuscarAitsParaLoteJulgamento getStrategy(int tpDocumento) throws Exception {
		if (tpDocumento == TipoLoteJulgamentoEnum.LOTE_DEFESA_DEFERIDA.getKey()) {
			return new BuscarAitsParaLoteDefesaDeferida();
		} 
		else if(tpDocumento == TipoLoteJulgamentoEnum.LOTE_JARI_COM_PROVIMENTO.getKey()) {
			return new BuscarAitsParaLoteJariDeferida();
		}
		else if(tpDocumento == TipoLoteJulgamentoEnum.LOTE_JARI_SEM_PROVIMENTO.getKey()) {
			return new BuscarAitsParaLoteJariIndeferida();
		}
		else {
			throw new ValidacaoException("Tipo de Movimento não encontrado.");
		}
	}
	
}
