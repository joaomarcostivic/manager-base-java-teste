package com.tivic.manager.wsdl.detran.ba;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.ba.validators.OrgaoValidator;
import com.tivic.manager.wsdl.detran.ba.validators.OrigemSolicitacaoValidator;
import com.tivic.manager.wsdl.interfaces.DadosEntrada;

public class DadosEntradaBA implements DadosEntrada {

	protected LinkedHashMap<String, DadosItem> itens;
	private String xml;
	
	
	public DadosEntradaBA() {
		itens = new LinkedHashMap<String, DadosItem>();
	}
	
	public int getOrigemSolicitacao() {
		return Integer.parseInt(itens.get("origem_solicitacao").getValor());
	}

	public void setOrigemSolicitacao(int origemSolicitacao) {
		itens.put("origem_solicitacao", new DadosItem(String.valueOf(origemSolicitacao), 1, true, new OrigemSolicitacaoValidator()));
	}

	public int getOrgao() {
		return Integer.parseInt(itens.get("orgao").getValor());
	}

	public void setOrgao(int orgao) {
		itens.put("orgao", new DadosItem(String.valueOf(orgao), 6, true, new OrgaoValidator()));
	}
	
	public String getPlaca() {
		return itens.get("placa").getValor();
	}

	public void setPlaca(String placa) {
		itens.put("placa", new DadosItem(placa, 10, true));
	}
	
	public String getXml() {
		return xml;
	}
	
	public void setXml(String xml) {
		this.xml = xml;
	}
	
	public HashMap<String, DadosItem> getItens(){
		return itens;
	}
	
	@Override
	public String toString() {
		String valueToString = "{";
		for(String key : this.itens.keySet()){
			try{
				valueToString += "\"" + key + "\":" + Integer.parseInt(this.itens.get(key).getValor()) + ", ";
			} catch(NumberFormatException nfe){
				valueToString += "\"" + key + "\": \"" + this.itens.get(key).getValor() + "\", ";
			}
		}
		valueToString = valueToString.substring(0, valueToString.length()-2) + "}";
		return valueToString;
	}
	
}
