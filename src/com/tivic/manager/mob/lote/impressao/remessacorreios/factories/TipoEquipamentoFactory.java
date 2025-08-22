package com.tivic.manager.mob.lote.impressao.remessacorreios.factories;

import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.lote.impressao.remessacorreios.SelecionadorNomeArquivoRelatorioNaiRadar;
import com.tivic.manager.mob.lote.impressao.remessacorreios.SelecionarNomeArquivoRelatorioDefault;

public class TipoEquipamentoFactory {

	public ISelecionadorNomeArquivoRelatorio getStrategy(int tpEquipamento) {
		if(tpEquipamento == EquipamentoEnum.RADAR_FIXO.getKey()) {
			return new SelecionadorNomeArquivoRelatorioNaiRadar();
		} else {
			return new SelecionarNomeArquivoRelatorioDefault();
		}	
	}
	
}
