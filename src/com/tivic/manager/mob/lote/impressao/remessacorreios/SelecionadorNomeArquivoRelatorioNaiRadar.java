package com.tivic.manager.mob.lote.impressao.remessacorreios;

import com.tivic.manager.grl.parametro.ParametroService;
import com.tivic.manager.mob.lote.impressao.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lote.impressao.remessacorreios.factories.ISelecionadorNomeArquivoRelatorio;

public class SelecionadorNomeArquivoRelatorioNaiRadar implements ISelecionadorNomeArquivoRelatorio {
	@Override
	public String getNomeRelatorio(int nrCodDetran, String nmDocumento) throws Exception {
		if(new ParametroService().getValorOfParametroAsInt("MOB_TIPO_ENVIO_CORREIOS_NAI").getNrValorParametro() != TipoRemessaCorreiosEnum.CARTA_SIMPLES.getKey()) {
			return nmDocumento;
		}	
		if(nrCodDetran == new ParametroService().getValorOfParametroAsInt("NR_CODIGO_PARADA_SOBRE_FAIXA").getNrValorParametro() ||
			nrCodDetran == new ParametroService().getValorOfParametroAsInt("NR_CODIGO_AVANCO_SINAL_VERMELHO").getNrValorParametro()) {
			return "mob/nai_infracao_duas_img";
		}
		return "mob/nai_v2";
	}
}
