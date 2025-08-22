package com.tivic.manager.ptc.protocolosv3.documento.situacao;

import com.tivic.manager.ptc.Documento;
import com.tivic.sol.connection.CustomConnection;

public interface IUpdateSituacaoDocumento {
	public Documento setSituacaoDocumento(int cdDocumento, String dsAssunto, CustomConnection customConnection) throws Exception;
	public int getCdSituacaoDocumento(CustomConnection customConnection) throws Exception;
}
