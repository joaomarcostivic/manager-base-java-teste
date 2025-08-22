package com.tivic.manager.util.comunicacao.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpServices {
	
	static FTPClient ftpCliente = new FTPClient();
	
	public void connect (ConfigFTP config) 
	{	
		try 
		{
			
			//Iguinora erro comun de conf em servidor
			ftpCliente.setStrictReplyParsing(false);
			ftpCliente.setActivePortRange(55000 , 65535);
			ftpCliente.connect(config.getHost(), config.getPort());
			ftpCliente.login(config.getUserName(), config.getPassword());
			
			//Direciona para a pasta correta
			//ftpCliente.cwd("/e-carta"); OR ftpCliente.changeWorkingDirectory("/upload");
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void disconnect() 
	{
		try 
		{
			ftpCliente.logout();
			ftpCliente.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public String sendFile (String path) 
	{
		File file = new File(path);
		try 
		{
			FileInputStream fileSend = new FileInputStream(file.getAbsolutePath());
			ftpCliente.setFileType(ftpCliente.BINARY_FILE_TYPE);

			if (ftpCliente.storeFile(file.getName(), fileSend))
				return "Arquivo armazenado com sucesso";
			else
				return "Falha ao enviar arquivo via FTP";
		} 
		catch ( IOException e) 
		{
			System.out.println("Erro ao enviar: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public String[] listFiles() {
		try 
		{
			
			String[] files;
			files = ftpCliente.listNames();
			
			for (String file: files)
			{
				System.out.println("Name file: " + file);
			}
			
			return files;
			
		} 
		catch (IOException e)
		{	
			e.printStackTrace();
			return null;
		} 
	}
	
	public File downloadFile(String nameFile) throws IOException 
	{
        //String[] arq = ftpCliente.listNames();  
        File tempFile = File.createTempFile("tempFile", "TESTE.ZIP");       
        FileOutputStream fos =
                new FileOutputStream(tempFile);

        if (ftpCliente.retrieveFile( nameFile, fos ))
        	System.out.println("Download efetuado com sucesso!");
        else
        	System.out.println ("Erro ao efetuar download do arquivo.");

        //=====Tentando acessar o tempFile
        System.out.println("Listando arquivo em temp: " + tempFile.getName());

		return tempFile;
	}
	
}
