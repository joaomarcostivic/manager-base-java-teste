package com.tivic.manager.mob.correios;

import java.util.List;

import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.ArquivoRetornoCorreiosDTO;

public interface IFTPSService {
	public void connect(String remoteDir) throws Exception;
	public List<ArquivoRetornoCorreiosDTO> getFiles(String fileExtension) throws Exception;
	public void disconnect() throws Exception;
	public void deleteFile(String fileName) throws Exception;
	public boolean isConnected() throws Exception;
}
