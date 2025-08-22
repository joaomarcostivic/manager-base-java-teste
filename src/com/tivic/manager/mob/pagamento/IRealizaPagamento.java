package com.tivic.manager.mob.pagamento;

import com.tivic.manager.mob.RetornoBancoDTO;
import com.tivic.sol.connection.CustomConnection;

public interface IRealizaPagamento {
	RetornoBancoDTO pagar(PagamentoDTO pagamentoDTO,BancoDadosRetorno bancoDadosRetorno, int cdUsuario, CustomConnection customConnection) throws Exception;
	void registrarDuplicado(PagamentoDTO pagamentoDTO,BancoDadosRetorno bancoDadosRetorno, int cdUsuario, CustomConnection customConnection) throws Exception;
}
