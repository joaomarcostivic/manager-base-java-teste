package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.ResultSetMapper;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.List;

public class AitMovimentoDocumentoDAO{

	public static int insert(AitMovimentoDocumento objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitMovimentoDocumento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_movimento_documento (cd_ait,"+
			                                  "cd_movimento,"+
			                                  "cd_documento) VALUES (?, ?, ?)");
			if(objeto.getCdAit()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAit());
			if(objeto.getCdMovimento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMovimento());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitMovimentoDocumento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AitMovimentoDocumento objeto, int cdAitOld, int cdDocumentoOld) {
		return update(objeto, cdAitOld, cdDocumentoOld, null);
	}

	public static int update(AitMovimentoDocumento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AitMovimentoDocumento objeto, int cdAitOld, int cdDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_movimento_documento SET cd_ait=?,"+
												      		   "cd_movimento=?,"+
												      		   "cd_documento=? WHERE cd_ait=? AND cd_documento=?");
			pstmt.setInt(1,objeto.getCdAit());
			if(objeto.getCdMovimento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMovimento());
			pstmt.setInt(3,objeto.getCdDocumento());
			pstmt.setInt(4, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.setInt(5, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAit, int cdDocumento) {
		return delete(cdAit, cdDocumento, null);
	}

	public static int delete(int cdAit, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait_movimento_documento WHERE cd_ait=? AND cd_documento=?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitMovimentoDocumento get(int cdAit, int cdDocumento) {
		return get(cdAit, cdDocumento, null);
	}

	public static AitMovimentoDocumento get(int cdAit, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento_documento WHERE cd_ait=? AND cd_documento=?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitMovimentoDocumento(rs.getInt("cd_ait"),
						rs.getInt("cd_movimento"),
						rs.getInt("cd_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimentoDocumento get(int cdAit, int cdMovimento, int cdDocumento) {
		return get(cdAit, cdMovimento, cdDocumento, null);
	}

	public static AitMovimentoDocumento get(int cdAit, int cdMovimento, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento_documento WHERE cd_ait=? AND cd_movimento = ? AND cd_documento=?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdMovimento);
			pstmt.setInt(3, cdDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitMovimentoDocumento(rs.getInt("cd_ait"),
						rs.getInt("cd_movimento"),
						rs.getInt("cd_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimentoDocumento getAit(int cdAit, int cdMovimento) {
		return getAit(cdAit, cdMovimento, null);
	}

	public static AitMovimentoDocumento getAit(int cdAit, int cdMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento_documento WHERE cd_ait=? AND cd_movimento = ?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdMovimento);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				return new AitMovimentoDocumento(
						rs.getInt("cd_ait"),
						rs.getInt("cd_movimento"),
						rs.getInt("cd_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.getAit: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.getAit: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ArrayList<AitMovimentoDocumento> getList() {
		return getList(null);
	}

	public static ArrayList<AitMovimentoDocumento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitMovimentoDocumento> list = new ArrayList<AitMovimentoDocumento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitMovimentoDocumento obj = AitMovimentoDocumentoDAO.get(rsm.getInt("cd_ait"), rsm.getInt("cd_movimento"), rsm.getInt("cd_documento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDocumentoDAO.getList: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimentoDocumento getByDocumento(int cdDocumento) {
		return getByDocumento(cdDocumento, null);
	}
	
	public static AitMovimentoDocumento getByDocumento(int cdDocumento, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM MOB_AIT_MOVIMENTO_DOCUMENTO WHERE CD_DOCUMENTO = ?");
			pstmt.setInt(1, cdDocumento);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				List<AitMovimentoDocumento> list = new ResultSetMapper<AitMovimentoDocumento>(rsm, AitMovimentoDocumento.class).toList();
				AitMovimentoDocumento aitMovDocumento = list.get(list.size() - 1);
				
				return aitMovDocumento;
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_ait_movimento_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
