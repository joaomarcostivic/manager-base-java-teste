package com.tivic.manager.mob.lotes.impressao.pix;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.DadosItem;

public class DadosArrecadacaoEnvio {
	private String url;
	protected LinkedHashMap<String, DadosItem> itens;

	public DadosArrecadacaoEnvio() {
		itens = new LinkedHashMap<String, DadosItem>();
	}

	public int getNrChavePix() {
		return Integer.parseInt(itens.get("nrChavePix").getValor());
	}

	public void setNrChavePix(String nrChavePix) {
		itens.put("nrChavePix", new DadosItem(nrChavePix, 256, true));
	}

	public int getDiasVencimento() {
		return Integer.parseInt(itens.get("diasVencimento").getValor());
	}

	public void setDiasVencimento(String diasVencimento) {
		itens.put("diasVencimento", new DadosItem(String.valueOf(diasVencimento), 3, true));
	}
	
	public String getCodigoBarras() {
        return itens.get("codigoBarras").getValor();
    }

    public void setCodigoBarras(String codigoBarras) {
        itens.put("codigoBarras", new DadosItem(codigoBarras, 44, true));
    }

	public double getVlCobranca() {
		return Double.parseDouble(itens.get("vlCobranca").getValor());
	}

	public void setVlCobranca(double vlCobranca) {
		itens.put("vlCobranca", new DadosItem(Util.formatNumber(vlCobranca, 2).replaceAll(",", "."), 11, true));
	}

	public String getTxtDescricao() {
		return itens.get("txtDescricao").getValor();
	}

	public void setTxtDescricao(String txtDescricao) {
		itens.put("txtDescricao", new DadosItem(txtDescricao, 210, false));
	}

	public String getNmDevedor() {
		return itens.get("nmDevedor").getValor();
	}

	public void setNmDevedor(String nmDevedor) {
		itens.put("nmDevedor", new DadosItem(nmDevedor, 60, false));
	}

	public String getCpfDevedor() {
		return itens.get("cpfDevedor").getValor();
	}

	public void setCpfDevedor(String cpfDevedor) {
		itens.put("cpfDevedor", new DadosItem(cpfDevedor, 11, false));
	}
	
	public String getCnpjDevedor() {
		return itens.get("cnpjDevedor").getValor();
	}
	
	public void setCnpjDevedor(String cnpjDevedor) {
		itens.put("cnpjDevedor", new DadosItem(cnpjDevedor, 14, false));
	}

	public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	public HashMap<String, String> getItens() {
	    HashMap<String, String> simples = new HashMap<>();
	    for (String chave : itens.keySet()) {
	        simples.put(chave, itens.get(chave).getValor());
	    }
	    return simples;
	}

}

