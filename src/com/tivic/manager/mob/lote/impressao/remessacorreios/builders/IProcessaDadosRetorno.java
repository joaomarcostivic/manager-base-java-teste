package com.tivic.manager.mob.lote.impressao.remessacorreios.builders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.DadosRetornoCorreioDto;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IProcessaDadosRetorno {
	List<DadosRetornoCorreioDto> obterDados(int tpRemessa, BufferedReader reader) throws ValidacaoException, IOException;
}
