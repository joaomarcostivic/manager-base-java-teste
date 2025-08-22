package com.tivic.manager.ptc.protocolosv3.publicacao;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lote.impressao.TipoLoteDocumentoEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class DocumentoPublicacaoMap {

	private HashMap<Integer, Integer> codigoLotePublicacaoMap;
	private HashMap<Integer, Integer> codigoLoteMovimentoMap;
	
	public DocumentoPublicacaoMap() {
		this.codigoLotePublicacaoMap = new LinkedHashMap<Integer, Integer>();
		this.codigoLoteMovimentoMap = new LinkedHashMap<Integer, Integer>();
		initCodigoPublicacao();
		initCodigoMovimento();
	}
	
	public int getCodPublicacao(int tpStatus) throws ValidacaoException {
		if (tpStatus == TipoStatusEnum.RECURSO_JARI.getKey()) {
			return this.codigoLotePublicacaoMap.get(TipoStatusEnum.RECURSO_JARI.getKey());
		} 
		if (tpStatus == TipoStatusEnum.DEFESA_PREVIA.getKey()) {
			return this.codigoLotePublicacaoMap.get(TipoStatusEnum.DEFESA_PREVIA.getKey());
		}
		if (tpStatus == TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey()) {
			return this.codigoLotePublicacaoMap.get(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey());
		} 
		else {
			throw new ValidacaoException("Tipo de lote não encontrado");
		}
	}
	
	public int getCodMovimento(int tpStatus) throws ValidacaoException {
		if (tpStatus == TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_JARI.getKey()) {
			return this.codigoLoteMovimentoMap.get(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_JARI.getKey());
		} 
		if (tpStatus == TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_DEFESA.getKey()) {
			return this.codigoLoteMovimentoMap.get(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_DEFESA.getKey());
		}
		if (tpStatus == TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_DEFESA_ADVERTENCIA.getKey()) {
			return this.codigoLoteMovimentoMap.get(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_DEFESA_ADVERTENCIA.getKey());
		} 
		else {
			throw new ValidacaoException("Tipo de lote não encontrado");
		}
	}
	
	public void initCodigoPublicacao() {
		this.codigoLotePublicacaoMap.put(TipoStatusEnum.RECURSO_JARI.getKey(), TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_JARI.getKey());
		this.codigoLotePublicacaoMap.put(TipoStatusEnum.DEFESA_PREVIA.getKey(), TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_DEFESA.getKey());
		this.codigoLotePublicacaoMap.put(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey(), TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_DEFESA_ADVERTENCIA.getKey());
	}
	
	public void initCodigoMovimento() {
		this.codigoLoteMovimentoMap.put(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_JARI.getKey(), TipoStatusEnum.RECURSO_JARI.getKey());
		this.codigoLoteMovimentoMap.put(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_DEFESA.getKey(), TipoStatusEnum.DEFESA_PREVIA.getKey());
		this.codigoLoteMovimentoMap.put(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_DEFESA_ADVERTENCIA.getKey(), TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey());
	}
	
}
