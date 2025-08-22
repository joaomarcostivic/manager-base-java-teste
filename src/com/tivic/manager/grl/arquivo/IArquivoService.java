package com.tivic.manager.grl.arquivo;

import com.tivic.manager.grl.Arquivo;
import com.tivic.sol.connection.CustomConnection;

public interface IArquivoService {
	Arquivo save(Arquivo arquivo) throws Exception;
	Arquivo save(Arquivo arquivo, CustomConnection customConnection) throws Exception;
	Arquivo get(int cdArquivo) throws Exception;
	Arquivo get(int cdArquivo, CustomConnection customConnection) throws Exception;
}
