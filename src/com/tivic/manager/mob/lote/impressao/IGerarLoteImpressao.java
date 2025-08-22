package com.tivic.manager.mob.lote.impressao;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface IGerarLoteImpressao {
	LoteImpressao gerarLoteImpressao(List<Ait> aitList, int cdUsuario, CustomConnection customConnection) throws ValidacaoException, Exception;
}
