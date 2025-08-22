package com.tivic.manager.mob.lote.impressao.remessacorreios.builders;

import java.io.BufferedReader;
import java.io.IOException;

public class DirectorRetorno {
	
	public void construtuctorDadosRetorno(IDadosRetornoBuilder dadosRetornoBuilder, BufferedReader reader) throws IOException {
		dadosRetornoBuilder.obterDados(reader);
	}
	
}
