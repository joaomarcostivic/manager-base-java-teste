package com.tivic.manager.mob.lotes.publicacao.map;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lote.impressao.TipoLoteDocumentoEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class TipoPublicacaoMap {
	private HashMap<Integer, Integer> tpLoteToStatusNotificacao;
	private HashMap<Integer, Integer> tpNotificacaoToStatusPublicacao;
	
	public TipoPublicacaoMap() {
		this.tpLoteToStatusNotificacao = new LinkedHashMap<Integer, Integer>();
		this.tpNotificacaoToStatusPublicacao = new LinkedHashMap<Integer, Integer>();
		initCodMovimentoNotificacao();
		initCodMovimentoPublicacao();
	}
	
	private void initCodMovimentoNotificacao() {
		this.tpLoteToStatusNotificacao.put(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NAI.getKey(), TipoStatusEnum.NAI_ENVIADO.getKey());
		this.tpLoteToStatusNotificacao.put(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NIP.getKey(), TipoStatusEnum.NIP_ENVIADA.getKey());
	}
	
	private void initCodMovimentoPublicacao() {
		this.tpNotificacaoToStatusPublicacao.put(TipoStatusEnum.NAI_ENVIADO.getKey(), TipoStatusEnum.PUBLICACAO_NAI.getKey());
		this.tpNotificacaoToStatusPublicacao.put(TipoStatusEnum.NIP_ENVIADA.getKey(), TipoStatusEnum.PUBLICACAO_NIP.getKey());
	}
	
	public int getStatusMovimentoNotificacao(int tpDocumento) throws ValidacaoException {
		if (tpDocumento == TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NAI.getKey()) {
			return this.tpLoteToStatusNotificacao.get(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NAI.getKey());
		} 
		else if (tpDocumento == TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NIP.getKey()) {
			return this.tpLoteToStatusNotificacao.get(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NIP.getKey());
		}
		else {
			throw new ValidacaoException("Tipo de lote não encontrado");
		}
	}
	
	public int getStatusMovimentoPublicacao(int tpStatus) throws ValidacaoException {
		if (tpStatus == TipoStatusEnum.NAI_ENVIADO.getKey()) {
			return this.tpNotificacaoToStatusPublicacao.get( TipoStatusEnum.NAI_ENVIADO.getKey());
		} 
		else if (tpStatus == TipoStatusEnum.NIP_ENVIADA.getKey()) {
			return this.tpNotificacaoToStatusPublicacao.get(TipoStatusEnum.NIP_ENVIADA.getKey());
		}
		else {
			throw new ValidacaoException("Tipo de movimento não encontrado");
		}
	}
}
