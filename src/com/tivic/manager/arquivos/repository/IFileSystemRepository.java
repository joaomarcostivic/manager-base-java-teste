package com.tivic.manager.arquivos.repository;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.ptc.protocolosv3.protocoloarquivos.ArquivoDownload;
import com.tivic.sol.connection.CustomConnection;

public interface IFileSystemRepository {
	public void insert(Arquivo arquivo, String diretorio, int idDiretorio, CustomConnection customConnection) throws Exception;
	public ArquivoDownload get(Arquivo arquivo) throws Exception;
	public void deleteArquivo(Arquivo arquivo) throws Exception;
}
