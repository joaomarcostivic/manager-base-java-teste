package com.tivic.manager.wsdl.detran.mg.recursofici;

import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class RecursoFiciDadosEntrada extends DadosEntradaMG {

	public String getAit() {
		return itens.get("ait").getValor();
	}
	
	public void setAit(String ait) {
		itens.put("ait", new DadosItem(ait, 10, true));
	}
	
	public String getNumeroProcessamento() {
		return itens.get("numero_processamento").getValor();
	}
	
	public void setNumeroProcessamento(String numeroProcessamento) {
		itens.put("numero_processamento", new DadosItem(numeroProcessamento, 9, true));
	}

	public String getCodigoRenainf() {
		return itens.get("codigo_renainf").getValor();
	}
	
	public void setCodigoRenainf(String codigoRenainf) {
		itens.put("codigo_renainf", new DadosItem(codigoRenainf, 11, false));
	}

	public String getCodigoInfracao() {
		return itens.get("codigo_infracao").getValor();
	}
	
	public void setCodigoInfracao(String codigoInfracao) {
		itens.put("codigo_infracao", new DadosItem(codigoInfracao, 4, true));
	}

	public String getCodigoDesdobramentoInfracao() {
		return itens.get("codigo_desdobramento_infracao").getValor();
	}
	
	public void setCodigoDesdobramentoInfracao(String codigoDesdobramentoInfracao) {
		itens.put("codigo_desdobramento_infracao", new DadosItem(codigoDesdobramentoInfracao, 2, true));
	}

	public int getCodigoMovimentacao() {
		return Integer.parseInt(itens.get("codigo_movimentacao").getValor());
	}
	
	public void setCodigoMovimentacao(int codigoMovimentacao) {
		itens.put("codigo_movimentacao", new DadosItem(String.valueOf(codigoMovimentacao), 3, true));
	}

	public int getModeloCnh() {
		return Integer.parseInt(itens.get("modelo_cnh").getValor());
	}
	
	public void setModeloCnh(int modeloCnh) {
		itens.put("modelo_cnh", new DadosItem(String.valueOf(modeloCnh), 1, false));
	}

	public String getNumeroCnh() {
		return itens.get("numero_cnh").getValor();
	}
	
	public void setNumeroCnh(String numeroCnh) {
		itens.put("numero_cnh", new DadosItem(numeroCnh, 11, false));
	}

	public String getUfCnh() {
		return itens.get("uf_cnh").getValor();
	}
	
	public void setUfCnh(String ufCnh) {
		itens.put("uf_cnh", new DadosItem(ufCnh, 2, true));
	}

	public int getCodigoPaisCnh() {
		return Integer.parseInt(itens.get("codigo_pais_cnh").getValor());
	}
	
	public void setCodigoPaisCnh(int codigoPaisCnh) {
		itens.put("codigo_pais_cnh", new DadosItem(String.valueOf(codigoPaisCnh), 2, false));
	}

	public String getNomeCondutor() {
		return itens.get("nome_condutor").getValor();
	}
	
	public void setNomeCondutor(String nomeCondutor) {
		itens.put("nome_condutor", new DadosItem(nomeCondutor, 60, false));
	}

	public String getRgCondutor() {
		return itens.get("rg_condutor").getValor();
	}
	
	public void setRgCondutor(String rgCondutor) {
		itens.put("rg_condutor", new DadosItem(rgCondutor, 13, false));
	}

	public String getOrgaoRg() {
		return itens.get("orgao_rg").getValor();
	}
	
	public void setOrgaoRg(String orgaoRg) {
		itens.put("orgao_rg", new DadosItem(orgaoRg, 5, false));
	}

	public String getUfRg() {
		return itens.get("uf_rg").getValor();
	}
	
	public void setUfRg(String ufRg) {
		itens.put("uf_rg", new DadosItem(ufRg, 2, false));
	}

	public String getCpfCondutor() {
		return itens.get("cpf_condutor").getValor();
	}
	
	public void setCpfCondutor(String cpfCondutor) {
		itens.put("cpf_condutor", new DadosItem(cpfCondutor, 11, false));
	}

	public String getNumeroProtocolo() {
		return itens.get("numero_protocolo").getValor();
	}
	
	public void setNumeroProtocolo(String numeroProtocolo) {
		itens.put("numero_protocolo", new DadosItem(numeroProtocolo, 16, false));
	}

	public String getParecerFici() {
		return itens.get("parecer_fici").getValor();
	}
	
	public void setParecerFici(String parecerFici) {
		itens.put("parecer_fici", new DadosItem(parecerFici, 1, true));
	}

	public GregorianCalendar getDataParecerFici() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_parecer_fici").getValor());
	}
	
	public void setDataParecerFici(GregorianCalendar dataParecerFici) {
		itens.put("data_parecer_fici", new DadosItem(Util.formatDate(dataParecerFici, "yyyyMMdd"), 8, true));
	}

	public String getCodigoParecerFici() {
		return itens.get("codigo_parecer_fici").getValor();
	}
	
	public void setCodigoParecerFici(String codigoParecerFici) {
		itens.put("codigo_parecer_fici", new DadosItem(codigoParecerFici, 3, false));
	}

	public GregorianCalendar getDataProtocolo() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_protocolo").getValor());
	}
	
	public void setDataProtocolo(GregorianCalendar dataProtocolo) {
		itens.put("data_protocolo", new DadosItem(Util.formatDate(dataProtocolo, "yyyyMMdd"), 8, true));
	}

	public String getCampoReservado() {
		return itens.get("Campo_reservado").getValor();
	}
	
	public void setCampoReservado(String campoReservado) {
		itens.put("Campo_reservado", new DadosItem(campoReservado, 100, false));
	}
	
	
	
	

}
