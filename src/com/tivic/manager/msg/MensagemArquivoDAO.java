package com.tivic.manager.msg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class MensagemArquivoDAO{

	public static int insert(MensagemArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(MensagemArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO MSG_MENSAGEM_ARQUIVO (CD_MENSAGEM,"+
			                                  "NM_ARQUIVO,"+
			                                  "BLB_ARQUIVO) VALUES (?, ?, ?)");
			if(objeto.getCdMensagem()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMensagem());
			pstmt.setString(2,objeto.getNmArquivo());
			if(objeto.getBlbArquivo()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MensagemArquivo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(MensagemArquivo objeto, int cdMensagemOld) {
		return update(objeto, cdMensagemOld, null);
	}

	public static int update(MensagemArquivo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(MensagemArquivo objeto, int cdMensagemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE MSG_MENSAGEM_ARQUIVO SET CD_MENSAGEM=?,"+
												      		   "NM_ARQUIVO=?,"+
												      		   "BLB_ARQUIVO=? WHERE CD_MENSAGEM=?");
			pstmt.setInt(1,objeto.getCdMensagem());
			pstmt.setString(2,objeto.getNmArquivo());
			if(objeto.getBlbArquivo()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbArquivo());
			pstmt.setInt(4, cdMensagemOld!=0 ? cdMensagemOld : objeto.getCdMensagem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMensagem) {
		return delete(cdMensagem, null);
	}

	public static int delete(int cdMensagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM MSG_MENSAGEM_ARQUIVO WHERE CD_MENSAGEM=?");
			pstmt.setInt(1, cdMensagem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MensagemArquivo get(int cdMensagem) {
		return get(cdMensagem, null);
	}

	public static MensagemArquivo get(int cdMensagem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM MSG_MENSAGEM_ARQUIVO WHERE CD_MENSAGEM=?");
			pstmt.setInt(1, cdMensagem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MensagemArquivo(rs.getInt("CD_MENSAGEM"),
						rs.getString("NM_ARQUIVO"),
						rs.getBytes("BLB_ARQUIVO")==null?null:rs.getBytes("BLB_ARQUIVO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemArquivoDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM MSG_MENSAGEM_ARQUIVO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemArquivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MensagemArquivo> getList() {
		return getList(null);
	}

	public static ArrayList<MensagemArquivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MensagemArquivo> list = new ArrayList<MensagemArquivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MensagemArquivo obj = MensagemArquivoDAO.get(rsm.getInt("CD_MENSAGEM"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM MSG_MENSAGEM_ARQUIVO", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
