package com.tivic.manager.ptc.protocolosv3.publicacao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class MovimentoJulgamentoMap {

	private HashMap<Integer, Integer> julgamentoDeferidoMap;
	private HashMap<Integer, Integer> julgamentoIndeferidoMap;
	
	public MovimentoJulgamentoMap() {
		this.julgamentoDeferidoMap = new LinkedHashMap<Integer, Integer>();
		this.julgamentoIndeferidoMap = new LinkedHashMap<Integer, Integer>();
		initCodigoMovimentoDeferido();
		initCodigoMovimentoIndeferido();
	}
	
	private void initCodigoMovimentoDeferido() {
		this.julgamentoDeferidoMap.put(TipoStatusEnum.DEFESA_PREVIA.getKey(), TipoStatusEnum.DEFESA_DEFERIDA.getKey());
		this.julgamentoDeferidoMap.put(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey(), TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey());
		this.julgamentoDeferidoMap.put(TipoStatusEnum.RECURSO_JARI.getKey(), TipoStatusEnum.JARI_COM_PROVIMENTO.getKey());
	}
	
	private void initCodigoMovimentoIndeferido() {
		this.julgamentoIndeferidoMap.put(TipoStatusEnum.DEFESA_PREVIA.getKey(), TipoStatusEnum.DEFESA_INDEFERIDA.getKey());
		this.julgamentoIndeferidoMap.put(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey(), TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey());
		this.julgamentoIndeferidoMap.put(TipoStatusEnum.RECURSO_JARI.getKey(), TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey());
	}
	
	public int get(int tpMovimento, int cdSituacaoDocumento) throws ValidacaoException {
		int codigoSituacaoDeferida = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_DEFERIDO", 0);
		int codigoSituacaoIndeferida = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_INDEFERIDO", 0);
		if (cdSituacaoDocumento == codigoSituacaoDeferida) {
			return getMovimentoDeferido(tpMovimento);
		}
		else if (cdSituacaoDocumento == codigoSituacaoIndeferida){
			return getMovimentoIndeferido(tpMovimento); 
		} 
		else {
			throw new ValidacaoException("Situação do Documento não identificada.");
		}
	}
	
	private int getMovimentoDeferido(int tpStatus) throws ValidacaoException {
		if (tpStatus == TipoStatusEnum.RECURSO_JARI.getKey()) {
			return this.julgamentoDeferidoMap.get(TipoStatusEnum.RECURSO_JARI.getKey());
		} 
		if (tpStatus == TipoStatusEnum.DEFESA_PREVIA.getKey()) {
			return this.julgamentoDeferidoMap.get(TipoStatusEnum.DEFESA_PREVIA.getKey());
		}
		if (tpStatus == TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey()) {
			return this.julgamentoDeferidoMap.get(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey());
		}
		else {
			throw new ValidacaoException("Tipo de movimento não encontrado");
		}
	}
	
	private int getMovimentoIndeferido(int tpStatus) throws ValidacaoException {
		if (tpStatus == TipoStatusEnum.RECURSO_JARI.getKey()) {
			return this.julgamentoIndeferidoMap.get(TipoStatusEnum.RECURSO_JARI.getKey());
		} 
		if (tpStatus == TipoStatusEnum.DEFESA_PREVIA.getKey()) {
			return this.julgamentoIndeferidoMap.get(TipoStatusEnum.DEFESA_PREVIA.getKey());
		}
		if (tpStatus == TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey()) {
			return this.julgamentoIndeferidoMap.get(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey());
		}
		else {
			throw new ValidacaoException("Tipo de movimento não encontrado.");
		}
	}
	
	
}
