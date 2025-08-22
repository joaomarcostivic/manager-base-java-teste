package com.tivic.manager.mob.talonario.factory.ultimodocumento;

import com.tivic.sol.connection.CustomConnection;

public interface UltimoNrDocumentoStrategy {
    int getUltimoNrDocumento(CustomConnection customConnection) throws Exception;
}
