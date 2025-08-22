package com.tivic.manager.mob.lote.impressao.remessacorreios.builders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.DadosRetornoCorreioDto;

public interface IDadosRetornoBuilder {
	void obterDados(BufferedReader reader) throws IOException;
	List<DadosRetornoCorreioDto> build();
}
