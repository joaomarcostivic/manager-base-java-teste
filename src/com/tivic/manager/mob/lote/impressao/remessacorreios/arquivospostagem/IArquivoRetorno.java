package com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;

public interface IArquivoRetorno {
	List<DadosRetornoCorreioDto> ler(ArquivoRetornoCorreiosDTO arquivoRetornoCorreios, int cdUsuario, CustomConnection customConnection) throws Exception;
}
