package com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor;

import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class ConsultarPontuacaoDadosCondutorDadosEntrada extends DadosEntradaMG {

	public String getDocumento() {
		return itens.get("documento").getValor();
	}
	
	public void setDocumento(String documento) {
		itens.put("documento", new DadosItem(documento, 11, true));
	}
	
	public int getTipoDocumento() {
		return Integer.parseInt(itens.get("tipo_documento").getValor());
	}
	
	public void setTipoDocumento(int TipoDocumento) {
		itens.put("tipo_documento", new DadosItem(String.valueOf(TipoDocumento), 1, true));
	}
	
}
