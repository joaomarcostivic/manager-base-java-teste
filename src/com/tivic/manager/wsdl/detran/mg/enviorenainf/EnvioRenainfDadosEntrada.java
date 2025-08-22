package com.tivic.manager.wsdl.detran.mg.enviorenainf;

import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class EnvioRenainfDadosEntrada extends DadosEntradaMG {

	public String getAit() {
		return itens.get("ait").getValor();
	}
	
	public void setAit(String ait) {
		itens.put("ait", new DadosItem(ait, 10, true));
	}
	
	public int getCodigoInfracao() {
		return Integer.parseInt(itens.get("codigo_infracao").getValor());
	}
	
	public void setCodigoInfracao(String codigoInfracao) {
		itens.put("codigo_infracao", new DadosItem(String.valueOf(codigoInfracao), 4, true));
	}
	
	public String getCodigoMovimentacao() {
		return itens.get("codigo_movimentacao").getValor();
	}
	
	public void setCodigoMovimentacao(String codigoMovimentacao) {
		itens.put("codigo_movimentacao", new DadosItem(codigoMovimentacao, 3, true));
	}

	public String getTipoProcessamento() {
		return itens.get("tipo_processamento").getValor();
	}
	
	public void setTipoProcessamento(String tipoProcessamento) {
		itens.put("tipo_processamento", new DadosItem(String.valueOf(tipoProcessamento), 1, true));
	}
		
	
}
