package com.tivic.manager.str;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.apache.commons.net.ftp.FTPClient;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class MarcaModeloServices {
	public static ResultSetMap getSyncData() {
		return getSyncData(null, null);
	}
	
	public static ResultSetMap getSyncData(GregorianCalendar dtInicial) {
		return getSyncData(dtInicial, null);
	}
	
	public static ResultSetMap getSyncData(GregorianCalendar dtInicial, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
					
			System.out.println("\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Sincronizando tabelas de marcas e modelos...");
			
			boolean str2mob = ManagerConf.getInstance().getAsBoolean("STR_TO_MOB");
			if(str2mob) {
				return com.tivic.manager.fta.MarcaModeloServices.getSyncData(dtInicial, connect);
			}
			
			String sql = "SELECT * FROM str_marca_modelo" + (dtInicial != null ? " where dt_atualizacao > '" + Util.convCalendarStringSql(dtInicial) + "'" : "");
					
			if(ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA")){
				sql = "SELECT A.*, A.cod_marca as cd_marca FROM MARCA_MODELO A" + (dtInicial != null ? " where A.dt_atualizacao is null or A.dt_atualizacao > '" + Util.convCalendarStringSql(dtInicial) + "'" : "");
				sql += " ORDER BY A.dt_atualizacao ASC";				
			}
				
			pstmt = connect.prepareStatement(sql);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			System.out.println("\tNº registro(s): "+rsm.size());
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	public static int updateData(GregorianCalendar dtAtualizacao, int cdMarca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE marca_modelo SET DT_ATUALIZACAO=?"+												      		   
												      		   " WHERE COD_MARCA=?");
			
			pstmt.setTimestamp(1,new Timestamp(dtAtualizacao.getTimeInMillis()));			
			pstmt.setInt(2,cdMarca);			
			pstmt.executeUpdate();
			System.out.println("COD_MARCA:" + cdMarca);
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static int updateDataNulas(GregorianCalendar dtNulas,Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE marca_modelo SET DT_ATUALIZACAO=?"+												      		   
												      		   " WHERE DT_ATUALIZACAO is null");
			
			int cont=0;
			
			pstmt.setTimestamp(1,new Timestamp(dtNulas.getTimeInMillis()));
			pstmt.executeUpdate();
			System.out.println("* - " + cont++);
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
		
	public static void corrigirMarcaModelo(ArrayList<Integer> maracaModeloTablet){
		GregorianCalendar dataAtualizacao = new GregorianCalendar(1999, 1, 1);
		GregorianCalendar dataNulas = new GregorianCalendar(2010, 1, 1);

		for (Integer codigoMarca : maracaModeloTablet) {	
			updateData(dataAtualizacao, codigoMarca, null);
		}
		
		updateDataNulas(dataNulas, null);
	}
	

	public static MarcaModelo getByMarcaModelo(String nmMarca, String nmModelo) {
		return getByMarcaModelo(nmMarca, nmModelo, null);
	}

	public static MarcaModelo getByMarcaModelo(String nmMarca, String nmModelo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			String sql = "SELECT * FROM fta_marca_modelo " +
						((nmMarca!=null && !nmMarca.isEmpty()) ? " WHERE (nm_marca=? OR nm_marca is null) " : " WHERE (nm_marca is null OR nm_marca = '') ") +
						((nmModelo!=null) ? " and nm_modelo=? " : " and nm_modelo is null ");
			
			if(lgBaseAntiga)
				sql = "SELECT * FROM marca_modelo " +
						((nmMarca!=null && !nmMarca.isEmpty()) ? " WHERE (nm_marca=? OR nm_marca is null) " : " WHERE (nm_marca is null OR nm_marca = '') ") +
						((nmModelo!=null) ? " and nm_modelo=? " : " and nm_modelo is null ");
			
			pstmt = connect.prepareStatement(sql);
			
			if(nmMarca!=null)
				pstmt.setString(1, nmMarca);
			
			if(nmModelo!=null)
				pstmt.setString((nmMarca!=null) ? 2 : 1, nmModelo);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(lgBaseAntiga)
					return new MarcaModelo(rs.getInt("cod_marca"),
							rs.getString("nm_marca"),
							rs.getString("nm_modelo"),
							(rs.getTimestamp("dt_atualizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao").getTime()));
				else
					return new MarcaModelo(rs.getInt("cd_marca"),
							rs.getString("nm_marca"),
							rs.getString("nm_modelo"),
							(rs.getTimestamp("dt_atualizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		ResultSetMap rsm = new ResultSetMap();
		
		if(!lgBaseAntiga) {
			rsm = Search.find(
					"SELECT A.*, A.nm_marca AS NM_MARCA_CARROCERIA, A.nm_modelo AS NM_MODELO_CARROCERIA, "
							+ "A.cd_marca AS CD_MARCA_CARROCERIA FROM str_marca_modelo A ", " ORDER BY cd_marca LIMIT 100 ",
					criterios, connect != null ? connect : Conexao.conectar(), connect == null);
		} else {
			rsm = Search.find(
					"SELECT * FROM marca_modelo A ", "ORDER BY cod_marca ASC LIMIT 100 ",
					criterios, connect != null ? connect : Conexao.conectar(), connect == null);
		}
		
		return rsm;
	}
	
	public static Result syncMarcaModelo() {
		try {
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
						
			file = new File("TabelaMarcaModelo.TXT");
			if (!file.exists())
				file.createNewFile();

			fos = new FileOutputStream(file);
			
			ftpClient.retrieveFile("TabelaMarcaModelo.TXT", fos);
			
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
	
	public static Result syncMarcaModelo(String fileName) { 
		return syncMarcaModelo(new File(fileName));
	}
	
	public static Result syncMarcaModelo(File file) {
		try {
			ArrayList<String> lines = new ArrayList<String>();
			BufferedReader raf = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
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
		}
	}
	
	public static Result syncMarcaModelo(ArrayList<String> lines) {
		return syncMarcaModelo(lines, null);
	}
	
	public static Result syncMarcaModelo(ArrayList<String> lines, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}				

			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			String sqlInsert = "";
			String sqlUpdate = "";
			String sqlSelect = "";
//			
			if(lgBaseAntiga) {
				sqlInsert = "INSERT INTO marca_modelo (cod_marca, nm_marca, nm_modelo, dt_atualizacao) VALUES (?, ?, ?, ?)";
				sqlUpdate = "UPDATE marca_modelo SET cod_marca=?, nm_marca=?,nm_modelo=?, dt_atualizacao=? WHERE cd_marca=?";
				sqlSelect = "SELECT * FROM marca_modelo WHERE cod_marca=?";
			} else {
				sqlInsert = "INSERT INTO fta_marca_modelo (cd_marca, nm_marca, nm_modelo, dt_atualizacao) VALUES (?, ?, ?, ?)";
				sqlUpdate = "UPDATE str_marca_modelo SET cd_marca=?, nm_marca=?,nm_modelo=?, dt_atualizacao=? WHERE cd_marca=?";
				sqlSelect = "SELECT * FROM fta_marca_modelo WHERE cd_marca=?";
			}
				
			//XXX:
			System.out.println("================================================================================");
			System.out.println("MARCA/MODELO ===================================================================");
			System.out.println();
						
			int total  = lines.size();
			int insert = 0;
			int count = 0;
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
					if(lgBaseAntiga)
						ps.setDate(4, new Date(dtAtualizacao.getTimeInMillis()));
					else
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
				
				System.out.print(".");
				if(count++ % 80 == 0)
					System.out.println();
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
	
	public static Result save(MarcaModelo marcaModelo){
		return save(marcaModelo, null, null);
	}

	public static Result save(MarcaModelo marcaModelo, AuthData authData){
		return save(marcaModelo, authData, null);
	}

	public static Result save(MarcaModelo marcaModelo, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(marcaModelo==null)
				return new Result(-1, "Erro ao salvar. MarcaModelo é nulo");

			int retorno;
			if(marcaModelo.getCdMarca()==0){
				retorno = MarcaModeloDAO.insert(marcaModelo, connect);
				marcaModelo.setCdMarca(retorno);
			}
			else {
				retorno = MarcaModeloDAO.update(marcaModelo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MARCAMODELO", marcaModelo);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(MarcaModelo marcaModelo) {
		return remove(marcaModelo.getCdMarca());
	}
	public static Result remove(int cdMarca){
		return remove(cdMarca, false, null, null);
	}
	public static Result remove(int cdMarca, boolean cascade){
		return remove(cdMarca, cascade, null, null);
	}
	public static Result remove(int cdMarca, boolean cascade, AuthData authData){
		return remove(cdMarca, cascade, authData, null);
	}
	public static Result remove(int cdMarca, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = MarcaModeloDAO.delete(cdMarca, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM str_marca_modelo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static void main(String[] args) {
		syncMarcaModelo();
	}
	
}