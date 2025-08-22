package com.tivic.manager.mob.lotes.impressao.prazorecurso;
import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class PrazoRecursoStrategy {

	private static final Map<Integer, IPrazoRecurso> map = new HashMap<>();

	static {
	    map.put(TipoStatusEnum.DEFESA_PREVIA.getKey(), new PrazoRecursoDefesaFactory());
	    map.put(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey(), new PrazoRecursoDefesaFactory());
	}

	public IPrazoRecurso getStrategy(int tpRecurso) throws ValidacaoException {
	    IPrazoRecurso prazoRecurso = map.get(tpRecurso);
	    if (prazoRecurso == null) {
	        throw new ValidacaoException("Tipo de recurso para calcular prazo não encontrado.");
	    }
	    return prazoRecurso;
	}
}
