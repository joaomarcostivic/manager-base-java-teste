package com.tivic.manager.wsdl.detran.sp.incluirautoinfracao;

import java.util.GregorianCalendar;

import com.tivic.manager.util.DoubleUtil;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.sp.DadosEntradaSP;
import com.tivic.manager.wsdl.detran.sp.validators.AutoEntregueValidator;
import com.tivic.manager.wsdl.detran.sp.validators.CorValidator;
import com.tivic.manager.wsdl.detran.sp.validators.EquipamentoFiscalizadorValidator;
import com.tivic.manager.wsdl.detran.sp.validators.EspecieValidator;
import com.tivic.sol.util.date.DateUtil;


public class IncluirAutoInfracaoDadosEntrada extends DadosEntradaSP {

	public String getTipoAuto() {
		return itens.get("TIPO_AUTO").getValor();
	}
	
	public void setTipoAuto(String tipoAuto) {
		itens.put("TIPO_AUTO", new DadosItem(Util.fillAlpha(tipoAuto, 1), 1, true));
	}

	public String getStatusAuto() {
		return itens.get("STATUS_AUTO").getValor();
	}
	
	public void setStatusAuto(Integer statusAuto) {
		itens.put("STATUS_AUTO", new DadosItem(String.valueOf(Util.fillNum(statusAuto, 4)), 4, true));
	}

	public String getNumeroAuto() {
		return itens.get("NUMERO_AUTO").getValor();
	}
	
	public void setNumeroAuto(String numeroAuto) {
		itens.put("NUMERO_AUTO", new DadosItem(Util.fillAlpha(numeroAuto, 11), 11, true));
	}

	public String getPlaca() {
		return itens.get("PLACA").getValor();
	}
	
	public void setPlaca(String placa) {
		itens.put("PLACA", new DadosItem(Util.fillAlpha(placa, 10), 10, false));
	}

	public String getCpfCnpjProprietario() {
		return itens.get("CPF_CNPJ_PROPRIETARIO").getValor();
	}
	
	public void setCpfCnpjProprietario(String cpfCnpjProprietario) {
		itens.put("CPF_CNPJ_PROPRIETARIO", new DadosItem(Util.fillAlpha(cpfCnpjProprietario, 14), 14, false));
	}

	public String getMarca() {
		return itens.get("MARCA").getValor();
	}
	
	public void setMarca(String marca) {
		itens.put("MARCA", new DadosItem(Util.fillAlpha(marca, 50), 50, false));
	}

	public String getModelo() {
		return itens.get("MODELO").getValor();
	}
	
	public void setModelo(String modelo) {
		itens.put("MODELO", new DadosItem(Util.fillAlpha(modelo, 50), 50, false));
	}

	public String getEspecie() {
		return itens.get("ESPECIE").getValor();
	}
	
	public void setEspecie(Integer especie) {
		itens.put("ESPECIE", new DadosItem(String.valueOf(Util.fillNum(especie, 2)), 2, false, new EspecieValidator()));
	}

	public String getMunicipioVeiculo() {
		return itens.get("MUNICIPIO_VEICULO").getValor();
	}
	
	public void setMunicipioVeiculo(String municipioVeiculo) {
		itens.put("MUNICIPIO_VEICULO", new DadosItem(String.valueOf(Util.fill(municipioVeiculo, 4, '0', 'E')), 4, false));
	}

	public String getCorVeiculo() {
		return itens.get("COR_VEICULO").getValor();
	}
	
	public void setCorVeiculo(Integer corVeiculo) {
		itens.put("COR_VEICULO", new DadosItem(String.valueOf(Util.fillNum(corVeiculo, 2)), 2, false, new CorValidator()));
	}

	public String getPaisVeiculo() {
		return itens.get("PAIS_VEICULO").getValor();
	}
	
	public void setPaisVeiculo(String paisVeiculo) {
		itens.put("PAIS_VEICULO", new DadosItem(Util.fillAlpha(paisVeiculo, 20), 20, false));
	}

	public String getDataInfracao() {
		return itens.get("DATA_INFRACAO").getValor();
	}
	
	public void setDataInfracao(GregorianCalendar dataInfracao) {
		itens.put("DATA_INFRACAO", new DadosItem(DateUtil.formatDate(dataInfracao, "yyyyMMdd"), 8, false));
	}

	public String getHoraInfracao() {
		return itens.get("HORA_INFRACAO").getValor();
	}
	
	public void setHoraInfracao(GregorianCalendar horaInfracao) {
		itens.put("HORA_INFRACAO", new DadosItem(DateUtil.formatDate(horaInfracao, "HHmm"), 8, false));
	}

	public Integer getCodigoLogradouro() {
		return Integer.parseInt(itens.get("CODIGO_LOGRADOURO").getValor());
	}
	
	public void setCodigoLogradouro(Integer codigoLogradouro) {
		itens.put("CODIGO_LOGRADOURO", new DadosItem(String.valueOf(Util.fillNum(codigoLogradouro, 6)), 6, true));
	}

	public String getDescricaoLogradouro() {
		return itens.get("DESCRICAO_LOGRADOURO").getValor();
	}
	
	public void setDescricaoLogradouro(String descricaoLogradouro) {
		itens.put("DESCRICAO_LOGRADOURO", new DadosItem(Util.fillAlpha(descricaoLogradouro, 100), 100, false));
	}

	public String getReferencia() {
		return itens.get("REFERENCIA").getValor();
	}
	
	public void setReferencia(String referencia) {
		itens.put("REFERENCIA", new DadosItem(Util.fillAlpha(referencia, 20), 20, false));
	}

	public String getComplemento() {
		return itens.get("COMPLEMENTO").getValor();
	}
	
	public void setComplemento(String complemento) {
		itens.put("COMPLEMENTO", new DadosItem(Util.fillAlpha(complemento, 60), 60, false));
	}

	public Integer getCodigoLogradouroCruzamento() {
		return Integer.parseInt(itens.get("CODIGO_LOGRADOURO_CRUZAMENTO").getValor());
	}
	
	public void setCodigoLogradouroCruzamento(Integer codigoLogradouroCruzamento) {
		itens.put("CODIGO_LOGRADOURO_CRUZAMENTO", new DadosItem(String.valueOf(Util.fillAlpha((codigoLogradouroCruzamento > 0 ? codigoLogradouroCruzamento.toString() : ""), 6)), 6, false));
	}

	public String getInfracao() {
		return itens.get("INFRACAO").getValor();
	}
	
	public void setInfracao(String nrCodDetran) {
		String infracao = nrCodDetran.substring(0, nrCodDetran.length()-1);
		infracao = infracao.substring(0, infracao.length()-1) + "-" + infracao.substring(infracao.length()-1);
		itens.put("INFRACAO", new DadosItem(String.valueOf(Util.fillAlpha(infracao, 5)), 5, false));
	}

	public String getDesdobramento() {
		return itens.get("DESDOBRAMENTO").getValor();
	}
	
	public void setDesdobramento(String nrCodDetran) {
		String desdobramento = nrCodDetran.substring(nrCodDetran.length()-1);
		itens.put("DESDOBRAMENTO", new DadosItem(String.valueOf(Util.fillAlpha(desdobramento, 1)), 1, false));
	}

	public String getOrgaoAutuador() {
		return itens.get("ORGAO_AUTUADOR").getValor();
	}
	
	public void setOrgaoAutuador(Integer orgaoAutuador) {
		itens.put("ORGAO_AUTUADOR", new DadosItem(String.valueOf(Util.fillNum(orgaoAutuador, 6)), 6, true));
	}

	public String getTipoEquipamentoFiscalizador() {
		return itens.get("TIPO_EQUIPAMENTO_FISCALIZADOR").getValor();
	}
	
	public void setTipoEquipamentoFiscalizador(String tipoEquipamentoFiscalizador) {
		itens.put("TIPO_EQUIPAMENTO_FISCALIZADOR", new DadosItem(String.valueOf(Util.fillAlpha(tipoEquipamentoFiscalizador, 1)), 1, false, new EquipamentoFiscalizadorValidator()));
	}

	public String getNumeroEquipamentoFiscalizador() {
		return itens.get("NUMERO_EQUIPAMENTO_FISCALIZADOR").getValor();
	}
	
	public void setNumeroEquipamentoFiscalizador(String numeroEquipamentoFiscalizador) {
		itens.put("NUMERO_EQUIPAMENTO_FISCALIZADOR", new DadosItem(String.valueOf(Util.fillAlpha(numeroEquipamentoFiscalizador, 10)), 10, false));
	}

	public String getNumeroCertificadoAfericao() {
		return itens.get("NUMERO_CERTIFICADO_AFERICAO").getValor();
	}
	
	public void setNumeroCertificadoAfericao(String numeroCertificadoAfericao) {
		itens.put("NUMERO_CERTIFICADO_AFERICAO", new DadosItem(String.valueOf(Util.fillAlpha(numeroCertificadoAfericao, 10)), 10, false));
	}

	public String getMarcaModeloEquipamento() {
		return itens.get("MARCA_MODELO_EQUIPAMENTO").getValor();
	}
	
	public void setMarcaModeloEquipamento(String marcaModeloEquipamento) {
		itens.put("MARCA_MODELO_EQUIPAMENTO", new DadosItem(String.valueOf(Util.fillAlpha(marcaModeloEquipamento, 100)), 100, false));
	}

	public String getTipoEquipamento() {
		return itens.get("TIPO_EQUIPAMENTO").getValor();
	}
	
	public void setTipoEquipamento(String tipoEquipamento) {
		itens.put("TIPO_EQUIPAMENTO", new DadosItem(String.valueOf(Util.fillAlpha(tipoEquipamento, 20)), 20, false));
	}

	public String getDataAfericaoEquipamento() {
		return itens.get("DATA_AFERICAO_EQUIPAMENTO").getValor();
	}
	
	public void setDataAfericaoEquipamento(GregorianCalendar dataAfericaoEquipamento) {
		if(dataAfericaoEquipamento != null)
			itens.put("DATA_AFERICAO_EQUIPAMENTO", new DadosItem(DateUtil.formatDate(dataAfericaoEquipamento, "yyyyMMdd"), 8, false));
		else
			itens.put("DATA_AFERICAO_EQUIPAMENTO", new DadosItem(Util.fillAlpha("", 8), 8, false));
	}

	public String getMedidaPermitida() {
		return itens.get("MEDIDA_PERMITIDA").getValor();
	}
	
	public void setMedidaPermitida(Double medidaPermitida) {
		if(medidaPermitida > 0) {
			String doubleFormatted = DoubleUtil.formatPrint(medidaPermitida, 2);
			itens.put("MEDIDA_PERMITIDA", new DadosItem(Util.fill(doubleFormatted, 5, '0', 'E'), 5, false));
		}
		else {
			itens.put("MEDIDA_PERMITIDA", new DadosItem(Util.fillAlpha("", 5), 5, false));
		}
	}

	public String getMedidaConsiderada() {
		return itens.get("MEDIDA_CONSIDERADA").getValor();
	}
	
	public void setMedidaConsiderada(Double medidaConsiderada) {
		if(medidaConsiderada > 0) {
			String doubleFormatted = DoubleUtil.formatPrint(medidaConsiderada+7, 2);
			itens.put("MEDIDA_CONSIDERADA", new DadosItem(Util.fill(doubleFormatted, 5, '0', 'E'), 5, false));
		}
		else {
			itens.put("MEDIDA_CONSIDERADA", new DadosItem(Util.fillAlpha("", 5), 5, false));
		}
	}

	public String getMedidaAferida() {
		return itens.get("MEDIDA_AFERIDA").getValor();
	}
	
	public void setMedidaAferida(Double medidaAferida) {
		if(medidaAferida > 0) {
			String doubleFormatted = DoubleUtil.formatPrint(medidaAferida, 2);
			itens.put("MEDIDA_AFERIDA", new DadosItem(Util.fill(doubleFormatted, 5, '0', 'E'), 5, false));
		}
		else {
			itens.put("MEDIDA_AFERIDA", new DadosItem(Util.fillAlpha("", 5), 5, false));
		}
	}

	public String getCnhCondutor() {
		return itens.get("CNH_CONDUTOR").getValor();
	}
	
	public void setCnhCondutor(String cnhCondutor) {
		itens.put("CNH_CONDUTOR", new DadosItem(String.valueOf(Util.fillAlpha(cnhCondutor, 11)), 11, false));
	}

	public String getUfCnhCondutor() {
		return itens.get("UF_CNH_CONDUTOR").getValor();
	}
	
	public void setUfCnhCondutor(String cnhCondutor) {
		itens.put("UF_CNH_CONDUTOR", new DadosItem(String.valueOf(Util.fillAlpha(cnhCondutor, 2)), 2, false));
	}

	public String getCpfCondutor() {
		return itens.get("CPF_CONDUTOR").getValor();
	}
	
	public void setCpfCondutor(String cpfCondutor) {
		itens.put("CPF_CONDUTOR", new DadosItem(String.valueOf(Util.fillAlpha(cpfCondutor, 14)), 14, false));
	}

	public String getNomeCondutor() {
		return itens.get("NOME_CONDUTOR").getValor();
	}
	
	public void setNomeCondutor(String nomeCondutor) {
		itens.put("NOME_CONDUTOR", new DadosItem(String.valueOf(Util.fillAlpha(nomeCondutor, 60)), 60, false));
	}

	public String getRgCondutor() {
		return itens.get("RG_CONDUTOR").getValor();
	}
	
	public void setRgCondutor(String rgCondutor) {
		itens.put("RG_CONDUTOR", new DadosItem(String.valueOf(Util.fillAlpha(rgCondutor, 11)), 11, false));
	}

	public String getUfRgCondutor() {
		return itens.get("UF_RG_CONDUTOR").getValor();
	}
	
	public void setUfRgCondutor(String ufRgCondutor) {
		itens.put("UF_RG_CONDUTOR", new DadosItem(String.valueOf(Util.fillAlpha(ufRgCondutor, 2)), 2, false));
	}

	public String getObservacaoCondutor() {
		return itens.get("OBSERVACAO_CONDUTOR").getValor();
	}
	
	public void setObservacaoCondutor(String observacaoCondutor) {
		itens.put("OBSERVACAO_CONDUTOR", new DadosItem(String.valueOf(Util.fillAlpha(observacaoCondutor, 200)), 200, false));
	}

	public String getObservacaoAutoInfracao() {
		return itens.get("OBSERVACAO_AUTO_INFRACAO").getValor();
	}
	
	public void setObservacaoAutoInfracao(String observacaoAutoInfracao) {
		observacaoAutoInfracao = observacaoAutoInfracao.replaceAll("\n", "");
		observacaoAutoInfracao = observacaoAutoInfracao.replaceAll("\\n", "");
		observacaoAutoInfracao = observacaoAutoInfracao.replaceAll("\r", "");
		observacaoAutoInfracao = observacaoAutoInfracao.replaceAll("\\r", "");
		observacaoAutoInfracao = observacaoAutoInfracao.replaceAll("\n\r", "");
		observacaoAutoInfracao = observacaoAutoInfracao.replaceAll("\\n\\r", "");
		itens.put("OBSERVACAO_AUTO_INFRACAO", new DadosItem(String.valueOf(Util.fillAlpha(observacaoAutoInfracao, 200)), 200, false));
	}

	public String getAgenteAutuador() {
		return itens.get("AGENTE_AUTUADOR").getValor();
	}
	
	public void setAgenteAutuador(String agenteAutuador) {
		itens.put("AGENTE_AUTUADOR", new DadosItem(String.valueOf(Util.fillAlpha(agenteAutuador, 12)), 12, false));
	}

	public String getIdEquipamento() {
		return itens.get("ID_EQUIPAMENTO").getValor();
	}
	
	public void setIdEquipamento(String idEquipamento) {
		itens.put("ID_EQUIPAMENTO", new DadosItem(String.valueOf(Util.fillAlpha(idEquipamento, 50)), 50, true));
	}

	public String getIdImpressora() {
		return itens.get("ID_IMPRESSORA").getValor();
	}
	
	public void setIdImpressora(String idImpressora) {
		itens.put("ID_IMPRESSORA", new DadosItem(String.valueOf(Util.fillAlpha(idImpressora, 30)), 30, true));
	}

	public String getAutoEntregue() {
		return itens.get("AUTO_ENTREGUE").getValor();
	}
	
	public void setAutoEntregue(Integer autoEntregue) {
		itens.put("AUTO_ENTREGUE", new DadosItem(Util.fillNum(autoEntregue, 2), 2, false, new AutoEntregueValidator()));
	}

	public String getLatitude() {
		return itens.get("LATITUDE").getValor();
	}
	
	public void setLatitude(String latitude) {
		itens.put("LATITUDE", new DadosItem(String.valueOf(Util.fillAlpha(latitude, 20)), 20, false));
	}

	public String getLongitude() {
		return itens.get("LONGITUDE").getValor();
	}
	
	public void setLongitude(String longitude) {
		itens.put("LONGITUDE", new DadosItem(String.valueOf(Util.fillAlpha(longitude, 20)), 20, false));
	}

	public String getDataCancelamento() {
		return itens.get("DATA_CANCELAMENTO").getValor();
	}
	
	public void setDataCancelamento(GregorianCalendar dataCancelamento) {
		itens.put("DATA_CANCELAMENTO", new DadosItem(Util.fillAlpha(DateUtil.formatDate(dataCancelamento, "yyyyMMdd HHmmss"), 15), 15, false));
	}

	public String getObservacaoCancelamento() {
		return itens.get("OBSERVACAO_CANCELAMENTO").getValor();
	}
	
	public void setObservacaoCancelamento(String observacaoCancelamento) {
		itens.put("OBSERVACAO_CANCELAMENTO", new DadosItem(String.valueOf(Util.fillAlpha(observacaoCancelamento, 200)), 200, false));
	}

	public String getNumeroNovoAuto() {
		return itens.get("NUMERO_NOVO_AUTO").getValor();
	}
	
	public void setNumeroNovoAuto(String numeroNovoAuto) {
		itens.put("NUMERO_NOVO_AUTO", new DadosItem(String.valueOf(Util.fillAlpha(numeroNovoAuto, 11)), 11, false));
	}

	public String getNomeEmbarcador() {
		return itens.get("NOME_EMBARCADOR").getValor();
	}
	
	public void setNomeEmbarcador(String nomeEmbarcador) {
		itens.put("NOME_EMBARCADOR", new DadosItem(String.valueOf(Util.fillAlpha(nomeEmbarcador, 60)), 60, false));
	}

	public String getCpfCnpjEmbarcador() {
		return itens.get("CPF_CNPJ_EMBARCADOR").getValor();
	}
	
	public void setCpfCnpjEmbarcador(String cpfCnpjEmbarcador) {
		itens.put("CPF_CNPJ_EMBARCADOR", new DadosItem(String.valueOf(Util.fillAlpha(cpfCnpjEmbarcador, 14)), 14, false));
	}

	public String getNomeTransportador() {
		return itens.get("NOME_TRANSPORTADOR").getValor();
	}
	
	public void setNomeTransportador(String nomeTransportador) {
		itens.put("NOME_TRANSPORTADOR", new DadosItem(String.valueOf(Util.fillAlpha(nomeTransportador, 60)), 60, false));
	}

	public String getCnpjTransportador() {
		return itens.get("CNPJ_TRANSPORTADOR").getValor();
	}
	
	public void setCnpjTransportador(String cnpjTransportador) {
		itens.put("CNPJ_TRANSPORTADOR", new DadosItem(String.valueOf(Util.fillAlpha(cnpjTransportador, 14)), 14, false));
	}

	public String getObservacaoEmbarcador() {
		return itens.get("OBSERVACAO_EMBARCADOR").getValor();
	}
	
	public void setObservacaoEmbarcador(String observacaoEmbarcador) {
		itens.put("OBSERVACAO_EMBARCADOR", new DadosItem(String.valueOf(Util.fillAlpha(observacaoEmbarcador, 200)), 200, false));
	}

	public String getDataValidacao() {
		return itens.get("DATA_VALIDACAO").getValor();
	}
	
	public void setDataValidacao(GregorianCalendar dataValidacao) {
		itens.put("DATA_VALIDACAO", new DadosItem(DateUtil.formatDate(dataValidacao, "yyyyMMdd"), 8, false));
	}

	public String getQuantidadeImagens() {
		return itens.get("QUANTIDADE_IMAGENS").getValor();
	}
	
	public void setQuantidadeImagens(Integer quantidadeImagens) {
		itens.put("QUANTIDADE_IMAGENS", new DadosItem(Util.fillNum(quantidadeImagens, 3), 3, false));
	}

	public String getNumeroAutoInfracaoDetran() {
		return itens.get("NUMERO_AUTO_INFRACAO_DETRAN").getValor();
	}
	
	public void setNumeroAutoInfracaoDetran(String numeroAutoInfracaoDetran) {
		itens.put("NUMERO_AUTO_INFRACAO_DETRAN", new DadosItem(String.valueOf(Util.fillAlpha(numeroAutoInfracaoDetran, 9)), 9, false));
	}

}
