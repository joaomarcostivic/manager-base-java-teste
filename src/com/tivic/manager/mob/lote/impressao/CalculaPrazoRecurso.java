package com.tivic.manager.mob.lote.impressao;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class CalculaPrazoRecurso {
	
	public IPrazoRecurso getStrategy(int tpRecurso) throws Exception {
		if (tpRecurso == TipoStatusEnum.DEFESA_PREVIA.getKey()  || tpRecurso == TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey()) {
			return new PrazoRecursoDefesa();
		} 
		else {
			throw new ValidacaoException("Tipo de recurso para calcular prazo não encontrado.");
		}
	}
	
}
