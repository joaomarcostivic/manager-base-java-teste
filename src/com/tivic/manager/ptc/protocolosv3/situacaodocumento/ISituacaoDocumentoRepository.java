package com.tivic.manager.ptc.protocolosv3.situacaodocumento;

import com.tivic.manager.ptc.SituacaoDocumento;

public interface ISituacaoDocumentoRepository {
	public SituacaoDocumento get(int id) throws Exception;
}
