package com.tivic.manager.mob;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;

public class NumeroProtocoloGeneratorFactory {
	
	public NumeroProtocoloGeneratorFactory() {}
	
	public IGerarNumeroProtocoloGenerator gerarNumero(Cidade cidade) throws Exception {
		int gerarComRegras = ParametroServices.getValorOfParametroAsInteger("LG_GERAR_PROTOCOLO_REGRAS_ORGAO", 0);
		if(gerarComRegras == 1) {
			if(cidade.getNmCidade().equalsIgnoreCase("MARIANA") || cidade.getNmCidade().equalsIgnoreCase("ITAUNA") || cidade.getNmCidade().equalsIgnoreCase("TEOFILO OTONI"))
				return new NumeroProtocoloGenerator();
		}
		return new NumeroProtocoloGeneratorDefault();
	}
}
