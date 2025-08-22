package com.tivic.manager.wsdl.detran.mg.consultarrecursos;

import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class ConsultarRecursosDadosEntrada extends DadosEntradaMG {

	public String getAit() {
		return itens.get("ait").getValor();
	}
	
	public void setAit(String ait) {
		itens.put("ait", new DadosItem(ait, 10, true));
	}

}
