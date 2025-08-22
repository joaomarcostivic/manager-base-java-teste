package com.tivic.manager.ptc.documento;

import javax.ws.rs.BadRequestException;

public interface IDocumentoService {
	boolean isDocumentoDeferido(int cdDocumento) throws Exception, BadRequestException;
}
