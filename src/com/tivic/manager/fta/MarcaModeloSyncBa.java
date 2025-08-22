package com.tivic.manager.fta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.apache.commons.net.ftp.FTPClient;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.log.console.ConsoleLogger;
import com.tivic.manager.util.Util;

import sol.util.Result;

public class MarcaModeloSyncBa implements MarcaModeloSync {
	
	public Result sync() {
		try {
			
			String workDir = ManagerConf.getInstance().get("TOMCAT_WORK_DIR", "/tivic");
			
			ArrayList<String> lines = new ArrayList<String>();
			
			FTPClient ftpClient = new FTPClient();
			FileOutputStream fos = null;    
			File file = null;
			
			String ftpAddress = ParametroServices.getValorOfParametroAsString("MOB_URL_FTP_DETRAN", "");
			String ftpUser = ParametroServices.getValorOfParametroAsString("MOB_NM_USUARIO_FTP_DETRAN", ""); 
			String ftpPass = ParametroServices.getValorOfParametroAsString("MOB_NM_SENHA_FTP_DETRAN", ""); 
			
			ftpClient.connect("ftp.prodeb.gov.br");
			ftpClient.login("pmvc", "pmvc159");
			
			if(!ftpClient.isConnected()) {
				return new Result(-1, "Erro ao conectar com o servidor FTP do DETRAN.");
			}
						
			file = new File(workDir+"/TabelaMarcaModelo.TXT");
			if (!file.exists())
				file.createNewFile();

			fos = new FileOutputStream(file);
			
			ftpClient.retrieveFile("/TabelaMarcaModelo.TXT", fos);
			
			fos.flush();
			fos.close();
						
			ftpClient.logout();
			ftpClient.disconnect();
			
			return syncMarcaModelo(file);
		}
		catch(Exception e) {			
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public Result syncMarcaModelo(String fileName) throws IOException { 
		return syncMarcaModelo(new File(fileName));
	}
	
	public Result syncMarcaModelo(File file) throws IOException {
		BufferedReader raf = null;
		try {
			ArrayList<String> lines = new ArrayList<String>();
			raf = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			String line = "";
			
			while ((line = raf.readLine()) != null) {
				lines.add(line);
			} 
			
			return syncMarcaModelo(lines);
		}
		catch(Exception e) {			
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(raf != null)
				raf.close();
		}
	}
	
	public Result syncMarcaModelo(ArrayList<String> lines) {
		return syncMarcaModelo(lines, null);
	}
	
	public Result syncMarcaModelo(ArrayList<String> lines, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}			

			String sqlInsert = "INSERT INTO fta_marca_modelo (cd_marca, nm_marca, nm_modelo, dt_atualizacao) VALUES (?, ?, ?, ?)";
			String sqlSelect = "SELECT * FROM fta_marca_modelo WHERE cd_marca=?";
				
			//XXX:
			System.out.println("================================================================================");
			System.out.println("MARCA/MODELO ===================================================================");
			System.out.println();
						
			int total  = lines.size();
			int insert = 0;
			int count = 0;
		    long startTime = System.currentTimeMillis();
			for (String line : lines) {
				String[] parts = line.split("#");
				int cdMarca = Integer.parseInt(parts[0].trim());
				String nmMarca = parts[2].trim();
				String nmModelo = parts[1].trim();
				GregorianCalendar dtAtualizacao = new GregorianCalendar();
				
				int retorno = 0;
				
				PreparedStatement psSelect = connect.prepareStatement(sqlSelect);
				psSelect.setInt(1, cdMarca);
				if(!psSelect.executeQuery().next()) {

					PreparedStatement ps = connect.prepareStatement(sqlInsert);
					ps.setInt(1, cdMarca);
					ps.setString(2, new String(nmMarca.getBytes("UTF-8"), "ISO-8859-1"));
					ps.setString(3, new String(nmModelo.getBytes("UTF-8"), "ISO-8859-1"));
					ps.setTimestamp(4, Util.convCalendarToTimestamp(dtAtualizacao));
					
					retorno = ps.executeUpdate();
					
					if(retorno < 0) {
						if(isConnectionNull)
							connect.rollback();
						System.out.println("ERRO!\n\t"+line);
						return new Result(-2, "Erro ao cadastrar Marca/Modelo.", "LINHA_ERRO", line);
					}
					
					insert++;
				}	
				
				count++;
				
				ConsoleLogger.getInstance().printProgress(startTime, total, count, "sync marca/modelo");
				
			}
			
			if(isConnectionNull)
				connect.commit();
			
			//XXX:
			System.out.println("\n\tTOTAL: "+total+"\tINSERIDOS: "+insert);
			System.out.println("================================================================================");
			System.out.println();
						
			Result result = new Result(1, "Tabela de Marca/Modelo atualizada com sucesso!");
			result.addObject("TOTAL", total);
			result.addObject("INSERT", insert);
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloServices.syncMarcaModelo: " + e);
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
