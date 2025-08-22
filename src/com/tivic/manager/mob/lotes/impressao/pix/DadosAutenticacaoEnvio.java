package com.tivic.manager.mob.lotes.impressao.pix;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tivic.manager.wsdl.DadosItem;

public class DadosAutenticacaoEnvio {
	protected LinkedHashMap<String, DadosItem> itens;

	public DadosAutenticacaoEnvio() {
		itens = new LinkedHashMap<String, DadosItem>();
	}
	
	public int getEmail() {
		return Integer.parseInt(itens.get("email").getValor());
	}

	public void setEmail(String email) {
		itens.put("email", new DadosItem(email, 256, true));
	}
	
	public int getSenha() {
		return Integer.parseInt(itens.get("senha").getValor());
	}

	public void setSenha(String senha) {
		itens.put("senha", new DadosItem(senha, 256, true));
	}
	
	public HashMap<String, String> getItens() {
	    HashMap<String, String> simples = new HashMap<>();
	    for (String chave : itens.keySet()) {
	        simples.put(chave, itens.get(chave).getValor());
	    }
	    return simples;
	}
}
