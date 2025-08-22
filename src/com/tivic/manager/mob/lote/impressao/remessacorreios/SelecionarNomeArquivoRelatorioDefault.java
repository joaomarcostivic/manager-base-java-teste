package com.tivic.manager.mob.lote.impressao.remessacorreios;

import com.tivic.manager.mob.lote.impressao.remessacorreios.factories.ISelecionadorNomeArquivoRelatorio;

public class SelecionarNomeArquivoRelatorioDefault implements ISelecionadorNomeArquivoRelatorio {
	@Override
	public String getNomeRelatorio(int nrCodDetran, String nmDocumento) throws Exception {
		return "mob/nai_v2";
	}

}
