package com.tivic.manager.wsdl.detran.mg.consultaautobasenacional;

import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class ConsultaAutoBaseNacionalDadosEntrada extends DadosEntradaMG {

	public String getAit() {
		return itens.get("ait").getValor();
	}
	
	public void setAit(String ait) {
		itens.put("ait", new DadosItem(ait, 10, true));
	}
	
	public String getCodigoInfracao() {
		return itens.get("codigo_infracao").getValor();
	}
	
	public void setCodigoInfracao(String codigoInfracao) {
		itens.put("codigo_infracao", new DadosItem(codigoInfracao, 4, true));
	}
	
}
