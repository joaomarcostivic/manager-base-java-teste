package com.tivic.manager.wsdl.detran.mg.incluirautoinfracao;

import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class IncluirAutoInfracaoDadosEntrada extends DadosEntradaMG {

	public String getUf() {
		return itens.get("uf").getValor();
	}
	
	public void setUf(String uf) {
		itens.put("uf", new DadosItem(uf, 2, true));
	}
	
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
	
	public int getCodigoDesdobramentoInfracao() {
		return Integer.parseInt(itens.get("codigo_desdobramento_infracao").getValor());
	}
	
	public void setCodigoDesdobramentoInfracao(String codigoDesdobramentoInfracao) {
		itens.put("codigo_desdobramento_infracao", new DadosItem(String.valueOf(codigoDesdobramentoInfracao), 2, true));
	}
	
	public String getFiscalizacaoEletronica() {
		return itens.get("fiscalizacao_eletronica").getValor();
	}
	
	public void setFiscalizacaoEletronica(String fiscalizacaoEletronica) {
		itens.put("fiscalizacao_eletronica", new DadosItem(fiscalizacaoEletronica, 1, false));
	}
	
	public String getCodigoAgente() {
		return itens.get("codigo_agente").getValor();
	}
	
	public void setCodigoAgente(String codigoAgente) {
		itens.put("codigo_agente", new DadosItem(codigoAgente, 15, true));
	}
	
	public int getTipoAgente() {
		return Integer.parseInt(itens.get("tipo_agente").getValor());
	}
	
	public void setTipoAgente(int tipoAgente) {
		itens.put("tipo_agente", new DadosItem(String.valueOf(tipoAgente), 1, true));
	}
	
	public GregorianCalendar getDataAit() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_ait").getValor());
	}
	
	public void setDataAit(GregorianCalendar dataAit) {
		itens.put("data_ait", new DadosItem(Util.formatDate(dataAit, "yyyyMMdd"), 8, true));
	}
	
	public GregorianCalendar getHoraAit() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendarHora(itens.get("hora_ait").getValor());
	}
	
	public void setHoraAit(GregorianCalendar horaAit) {
		itens.put("hora_ait", new DadosItem(Util.formatDateTime(horaAit, "HHmmss"), 6, true));
	}
	
	public String getLocalAit() {
		return itens.get("local_ait").getValor();
	}
	
	public void setLocalAit(String localAit) {
		itens.put("local_ait", new DadosItem(localAit, 80, true));
	}
	
	public String getCodigoMunicipio() {
		return itens.get("codigo_municipio").getValor();
	}
	
	public void setCodigoMunicipio(String codigoMunicipio) {
		itens.put("codigo_municipio", new DadosItem(codigoMunicipio, 5, true));
	}
	
	public String getUfMunicipio() {
		return itens.get("uf_municipio").getValor();
	}
	
	public void setUfMunicipio(String ufMunicipio) {
		itens.put("uf_municipio", new DadosItem(ufMunicipio, 2, true));
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
	
	public int getTipoDocumento() {
		return Integer.parseInt(itens.get("tipo_documento").getValor());
	}
	
	public void setTipoDocumento(int tipoDocumento) {
		itens.put("tipo_documento", new DadosItem(String.valueOf(tipoDocumento), 1, false));
	}
	
	public String getNumeroDocumento() {
		return itens.get("numero_documento").getValor();
	}
	
	public void setNumeroDocumento(String numeroDocumento) {
		itens.put("numero_documento", new DadosItem(numeroDocumento, 14, false));
	}
	
	public String getCpfCondutor() {
		return itens.get("cpf_condutor").getValor();
	}
	
	public void setCpfCondutor(String cpfCondutor) {
		itens.put("cpf_condutor", new DadosItem(cpfCondutor, 11, false));
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
		itens.put("uf_cnh", new DadosItem(ufCnh, 2, false));
	}
	
	public int getCodigoPaisCnh() {
		return Integer.parseInt(itens.get("codigo_pais_cnh").getValor());
	}
	
	public void setCodigoPaisCnh(int codigoPaisCnh) {
		itens.put("codigo_pais_cnh", new DadosItem(String.valueOf(codigoPaisCnh), 2, false));
	}
	
	public int getAssinatura() {
		return Integer.parseInt(itens.get("assinatura").getValor());
	}
	
	public void setAssinatura(int assinatura) {
		itens.put("assinatura", new DadosItem(String.valueOf(assinatura), 1, true));
	}
	
	public String getCodigoMarcaModelo() {
		return itens.get("codigo_marca_modelo").getValor();
	}
	
	public void setCodigoMarcaModelo(String codigoMarcaModelo) {
		itens.put("codigo_marca_modelo", new DadosItem(codigoMarcaModelo, 6, false));
	}
	
	public String getDescricaoMarcaModelo() {
		return itens.get("descricao_marca_modelo").getValor();
	}
	
	public void setDescricaoMarcaModelo(String descricaoMarcaModelo) {
		itens.put("descricao_marca_modelo", new DadosItem(descricaoMarcaModelo, 25, true));
	}
	
	public String getCor() {
		return itens.get("cor").getValor();
	}
	
	public void setCor(String cor) {
		itens.put("cor", new DadosItem(cor, 15, true));//Alterado de true para false, pois no manual está errado
	}
	
	public double getValorAit() {
		return Double.parseDouble(itens.get("valor_ait").getValor());
	}
	
	public void setValorAit(double valorAit) {
		itens.put("valor_ait", new DadosItem(Util.formatNumber(valorAit, 2).replaceAll(",", "."), 11, false));
	}
	
	public double getMedicaoReal() {
		return Double.parseDouble(itens.get("medicao_real").getValor());
	}
	
	public void setMedicaoReal(double medicaoReal) {
		itens.put("medicao_real", new DadosItem(Util.formatNumber(medicaoReal, 2), 9, false));
	}
	
	public double getLimite() {
		return Double.parseDouble(itens.get("limite").getValor());
	}
	
	public void setLimite(double limite) {
		itens.put("limite", new DadosItem(Util.formatNumber(limite, 2), 9, false));
	}
	
	public double getMedicaoConsiderada() {
		return Double.parseDouble(itens.get("medicao_considerada").getValor());
	}
	
	public void setMedicaoConsiderada(double medicaoConsiderada) {
		itens.put("medicao_considerada", new DadosItem(Util.formatNumber(medicaoConsiderada, 2), 9, false));
	}
	
	public int getUnidadeMedida() {
		return Integer.parseInt(itens.get("unidade_medida").getValor());
	}
	
	public void setUnidadeMedida(int unidadeMedida) {
		itens.put("unidade_medida", new DadosItem(String.valueOf(unidadeMedida), 2, false));
	}
	
	public String getNomeEmbarcador() {
		return itens.get("nome_embarcador").getValor();
	}
	
	public void setNomeEmbarcador(String nomeEmbarcador) {
		itens.put("nome_embarcador", new DadosItem(nomeEmbarcador, 60, false));
	}
	
	public String getDocumentoEmbarcador() {
		return itens.get("documento_embarcador").getValor();
	}
	
	public void setDocumentoEmbarcador(String documentoEmbarcador) {
		itens.put("documento_embarcador", new DadosItem(documentoEmbarcador, 14, false));
	}
	
	public String getNomeTransportador() {
		return itens.get("nome_transportador").getValor();
	}
	
	public void setNomeTransportador(String nomeTransportador) {
		itens.put("nome_transportador", new DadosItem(nomeTransportador, 60, false));
	}
	
	public String getDocumentoTransportador() {
		return itens.get("documento_transportador").getValor();
	}
	
	public void setDocumentoTransportador(String documentoTransportador) {
		itens.put("documento_transportador", new DadosItem(documentoTransportador, 14, false));
	}
	
	public String getEspecieVeiculo() {
		return itens.get("especie_veiculo").getValor();
	}
	
	public void setEspecieVeiculo(String especieVeiculo) {
		itens.put("especie_veiculo", new DadosItem(especieVeiculo, 1, false));
	}
	
	public String getObservacao() {
		return itens.get("observacao").getValor();
	}
	
	public void setObservacao(String observacao) {
		if(observacao != null)
			itens.put("observacao", new DadosItem(observacao.length() <= 210 ? observacao : observacao.substring(0, 210), 210, false));
	}
	
	public String getCampoReservado() {
		return itens.get("Campo_reservado").getValor();
	}
	
	public void setCampoReservado(String campoReservado) {
		itens.put("Campo_reservado", new DadosItem(campoReservado, 210, false));
	}
	
	
}
