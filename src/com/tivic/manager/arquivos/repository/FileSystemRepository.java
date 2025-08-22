package com.tivic.manager.arquivos.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.ptc.protocolosv3.protocoloarquivos.ArquivoDownload;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.conf.ManagerConf;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.log.builders.InfoLogBuilder;

public class FileSystemRepository implements IFileSystemRepository {
	
	private ManagerLog managerLog;
	private final String BASE_URL;
	
	public FileSystemRepository() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.BASE_URL = ManagerConf.getInstance().get("BASE_URL").replace("\"", "");
	}
	
	@Override
	public void insert(Arquivo arquivo, String diretorio, int idDiretorio, CustomConnection customConnection) throws Exception {
	    try {
	        verificarPath();
	        String folderPath = construirCaminhoDiretorio(diretorio, idDiretorio);
	        criarDiretorioSeNecessario(folderPath);
	        arquivo.setTxtCaminhoArquivo(folderPath);
	        String caminhoArquivo = construirCaminhoArquivo(arquivo);
	        if (verificarArquivoDuplicado(caminhoArquivo)) {
	            String nomeUnico = gerarNomeArquivo(arquivo.getNmArquivo(), folderPath);
	            arquivo.setNmArquivo(nomeUnico);
	            caminhoArquivo = construirCaminhoArquivo(arquivo);
	        }
	        salvarArquivoNoDiretorio(caminhoArquivo, arquivo.getBlbArquivo());
	        arquivo.setBlbArquivo(null);
	    } catch (Exception e) {
	        managerLog.showLog(new InfoLogBuilder("Erro ao inserir o arquivo no diretório: ", e.getMessage()).build());
	        throw new Exception("Erro ao inserir arquivo", e);
	    }
	}

	private String construirCaminhoDiretorio(String diretorio, int idDiretorio) {
	    return Paths.get(BASE_URL, diretorio, String.valueOf(idDiretorio)).toString();
	}

	private void criarDiretorioSeNecessario(String path) throws Exception {
	    File folder = new File(path);
	    if (!folder.exists()) {
	        boolean created = folder.mkdirs();
	        if (!created || !folder.exists()) {
	            throw new Exception("Falha ao criar diretório: " + path);
	        }
	    }
	}

	private String construirCaminhoArquivo(Arquivo arquivo) {
	    return Paths.get(arquivo.getTxtCaminhoArquivo(), arquivo.getNmArquivo()).toString();
	}
	
	private boolean verificarArquivoDuplicado(String caminhoArquivo) {
	    File file = new File(caminhoArquivo);
	    return file.exists();
	}

	private String gerarNomeArquivo(String nomeOriginal, String diretorio) {
	    String nomeSemExtensao = nomeOriginal;
	    String extensao = "";
	    int ultimoPonto = nomeOriginal.lastIndexOf('.');
	    if (ultimoPonto != -1) {
	        nomeSemExtensao = nomeOriginal.substring(0, ultimoPonto);
	        extensao = nomeOriginal.substring(ultimoPonto);
	    }
	    int contador = 1;
	    String novoNome;
	    File file;
	    do {
	        StringBuilder sb = new StringBuilder();
	        sb.append(nomeSemExtensao)
	          .append("_")
	          .append(contador)
	          .append(extensao);
	        
	        novoNome = sb.toString();
	        file = new File(Paths.get(diretorio, novoNome).toString());
	        contador++;
	    } while (file.exists());
	    return novoNome;
	}

	private void salvarArquivoNoDiretorio(String caminhoArquivo, byte[] dados) throws Exception {
	    File file = new File(caminhoArquivo);
	    file.createNewFile();
	    try (FileOutputStream fos = new FileOutputStream(file)) {
	        fos.write(dados);
	    }
	}

	private void verificarPath() throws Exception {
	    String caminhoBaseArquivos = BASE_URL;
	    if (!caminhoBaseArquivos.replace("\\", "/").toLowerCase().endsWith("/arquivos")) {
	        throw new Exception("BASE_URL deve terminar com '/Arquivos'. Valor atual: " + BASE_URL);
	    }
	}
	
	@Override
	public ArquivoDownload get(Arquivo arquivo) throws Exception {
		try {
			String txtCaminhoArquivo = Paths.get(arquivo.getTxtCaminhoArquivo(), arquivo.getNmArquivo()).toString();
			byte[] blb;
			try (FileInputStream fis = new FileInputStream(txtCaminhoArquivo)) {
				File file = new File(txtCaminhoArquivo);
				blb = new byte[(int) file.length()];
				fis.read(blb);
			}		
			ArquivoDownload arquivoRetorno = new ArquivoDownload();
			arquivoRetorno.setBlbArquivo(blb);
			arquivoRetorno.setCdArquivo(arquivo.getCdArquivo());
			arquivoRetorno.setNmArquivo(arquivo.getNmArquivo());
			return arquivoRetorno;
		} catch (Exception e) {
			throw new Exception("Arquivo não encontrado.");
		}
	}
	
	@Override
	public void deleteArquivo(Arquivo arquivo) throws Exception {
	    try {
	        String caminhoArquivo = construirCaminhoArquivo(arquivo);
	        File file = new File(caminhoArquivo);
	        if (!file.exists()) {
	            throw new Exception("Arquivo não encontrado no caminho: " + caminhoArquivo);
	        }
	        boolean deleted = file.delete();
	        if (!deleted) {
	            throw new Exception("Não foi possível excluir o arquivo: " + arquivo.getNmArquivo());
	        }
	    } catch (Exception e) {
	        managerLog.showLog(new InfoLogBuilder("Erro: ", e.getMessage()).build());
	        throw new Exception("Erro ao excluir arquivo", e);
	    }
	}
}
