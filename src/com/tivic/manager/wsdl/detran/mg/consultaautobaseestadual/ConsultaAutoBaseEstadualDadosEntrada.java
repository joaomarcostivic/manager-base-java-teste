package com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual;

import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class ConsultaAutoBaseEstadualDadosEntrada extends DadosEntradaMG {
	
	public String getAit() {
		return itens.get("ait").getValor();
	}
	
	public void setAit(String ait) {
		itens.put("ait", new DadosItem(ait, 10, true));
	}

}
