package com.tivic.manager.mob.correios;

import com.tivic.manager.mob.StCorreiosLote;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class UpdateLoteEtiquetaFactory {
	public IUpdateLoteEtiqueta getStrategy(int stLote) throws Exception {
		if(stLote == StCorreiosLote.CANCELADO) {
			return new UpdateLoteCancelado();
		} else if(stLote == StCorreiosLote.CONCLUIDO || stLote == StCorreiosLote.DISPONIVEL || stLote == StCorreiosLote.VENCIDO) {
			return new UpdateLote();
		} else {
			throw new ValidacaoException("Situação de lote não encontrada.");
		}
	}

}
