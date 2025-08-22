package com.tivic.manager.mob.lote.impressao.remessacorreios.factories;

public interface ISelecionadorNomeArquivoRelatorio {
	String getNomeRelatorio(int nrCodDetran, String nmDocumento) throws Exception;
}
