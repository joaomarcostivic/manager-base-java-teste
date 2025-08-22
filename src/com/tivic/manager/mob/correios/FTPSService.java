package com.tivic.manager.mob.correios;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.ArquivoRetornoCorreiosDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;

public class FTPSService implements IFTPSService {
	
	private FTPClient ftpClient;
	private ManagerLog managerLog;
	private IParametroRepository parametroRepository;
	
	public FTPSService() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	@Override
    public void connect(String remoteDir) throws Exception {
    	try {
    		HashMap<String, String> credenciais = getCredenciais();
    		ftpClient = new FTPClient();
            ftpClient.setStrictReplyParsing(false);
            managerLog.info("Iniciando conexão ao serviço EDI...", "");
            ftpClient.connect(credenciais.get("server"));
            managerLog.info("Conectado ao serviço EDI.", "");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setDefaultTimeout(60000);
            ftpClient.setSoTimeout(60000);
            ftpClient.login(credenciais.get("username"), credenciais.get("password"));
            if (!ftpClient.changeWorkingDirectory(remoteDir)) {
                throw new Exception("Falha ao acessar o diretório remoto: " + remoteDir);
            }
    	} catch (Exception ex) {
    		managerLog.showLog(ex);
    	}
    }
    
    private HashMap<String, String> getCredenciais() throws Exception {
    	HashMap<String, String> credenciais = new HashMap<String, String>();
    	String server = parametroRepository.getValorOfParametroAsString("LINK_FTPS_CORREIOS_EDI");
        String username = parametroRepository.getValorOfParametroAsString("USUARIO_FTPS_CORREIOS_EDI");
        String password = parametroRepository.getValorOfParametroAsString("PASSWORD_CORREIOS_EDI");
        credenciais.put("server", validarParametro(server, "LINK_FTPS_CORREIOS_EDI"));
        credenciais.put("username", validarParametro(username, "USUARIO_FTPS_CORREIOS_EDI"));
        credenciais.put("password",  validarParametro(password, "PASSWORD_CORREIOS_EDI"));
    	return credenciais;
    }
    
    private String validarParametro(String valor, String nmParametro) throws Exception{
    	if(valor == null || valor.isEmpty() || valor.equals("0")) {
    		throw new Exception("O parâmetro " + nmParametro + " não foi configurado.");
    	}
    	return valor;
    }
	
    public List<ArquivoRetornoCorreiosDTO> getFiles(String fileExtension) throws Exception {
        if (ftpClient == null || !ftpClient.isConnected()) {
            throw new Exception("Cliente FTPS não está conectado.");
        }
        String[] files = ftpClient.listNames();
        List<ArquivoRetornoCorreiosDTO> arquivoRetornoCorreios =  new ArrayList<>();
        if (files == null || files.length == 0) {
            throw new Exception("Nenhum arquivo encontrado no diretório.");
        }
        for (String file : files) {
        	ArquivoRetornoCorreiosDTO arquivoDTO = new ArquivoRetornoCorreiosDTO();
            if (file.endsWith(fileExtension)) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                if (ftpClient.retrieveFile(file, outputStream)) {
                    String fileBase64 = "data:text/plain;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
                    arquivoDTO.setArquivoRetorno(fileBase64);
                    arquivoDTO.setNmArquivo(file);
                    arquivoRetornoCorreios.add(arquivoDTO);
                } else {
                	throw new Exception("Falha ao baixar o arquivo: " + file);
                }
            }
        }
        return arquivoRetornoCorreios;
    }
    
    @Override
    public void deleteFile(String fileName) throws Exception {
        if (ftpClient.deleteFile(fileName)) {
        	managerLog.info("Arquivo excluído com sucesso: ", fileName);
        } else {
            throw new Exception("Falha ao excluir o arquivo: " + fileName);
        }
    }
    
    @Override
    public void disconnect() throws Exception {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
                managerLog.info("Desconectado do serviço EDI.", "");
            } catch (IOException ex) {
            	managerLog.showLog(ex);
            }
        }
    }
    
    @Override
    public boolean isConnected() throws Exception {
        return ftpClient != null && ftpClient.isConnected();
    }
}
