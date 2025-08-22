package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoDocumentacaoDAO{

	public static int insert(TipoDocumentacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoDocumentacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("GRL_TIPO_DOCUMENTACAO", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoDocumentacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO GRL_TIPO_DOCUMENTACAO (CD_TIPO_DOCUMENTACAO,"+
			                                  "NM_TIPO_DOCUMENTACAO,"+
			                                  "SG_TIPO_DOCUMENTACAO,"+
			                                  "CD_FORMULARIO,"+
			                                  "ID_TIPO_DOCUMENTACAO) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoDocumentacao());
			pstmt.setString(3,objeto.getSgTipoDocumentacao());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdFormulario());
			pstmt.setString(5,objeto.getIdTipoDocumentacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoDocumentacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoDocumentacao objeto, int cdTipoDocumentacaoOld) {
		return update(objeto, cdTipoDocumentacaoOld, null);
	}

	public static int update(TipoDocumentacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoDocumentacao objeto, int cdTipoDocumentacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE GRL_TIPO_DOCUMENTACAO SET CD_TIPO_DOCUMENTACAO=?,"+
												      		   "NM_TIPO_DOCUMENTACAO=?,"+
												      		   "SG_TIPO_DOCUMENTACAO=?,"+
												      		   "CD_FORMULARIO=?,"+
												      		   "ID_TIPO_DOCUMENTACAO=? WHERE CD_TIPO_DOCUMENTACAO=?");
			pstmt.setInt(1,objeto.getCdTipoDocumentacao());
			pstmt.setString(2,objeto.getNmTipoDocumentacao());
			pstmt.setString(3,objeto.getSgTipoDocumentacao());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdFormulario());
			pstmt.setString(5,objeto.getIdTipoDocumentacao());
			pstmt.setInt(6, cdTipoDocumentacaoOld!=0 ? cdTipoDocumentacaoOld : objeto.getCdTipoDocumentacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDocumentacao) {
		return delete(cdTipoDocumentacao, null);
	}

	public static int delete(int cdTipoDocumentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM GRL_TIPO_DOCUMENTACAO WHERE CD_TIPO_DOCUMENTACAO=?");
			pstmt.setInt(1, cdTipoDocumentacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoDocumentacao get(int cdTipoDocumentacao) {
		return get(cdTipoDocumentacao, null);
	}

	public static TipoDocumentacao get(int cdTipoDocumentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM GRL_TIPO_DOCUMENTACAO WHERE CD_TIPO_DOCUMENTACAO=?");
			pstmt.setInt(1, cdTipoDocumentacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoDocumentacao(rs.getInt("CD_TIPO_DOCUMENTACAO"),
						rs.getString("NM_TIPO_DOCUMENTACAO"),
						rs.getString("SG_TIPO_DOCUMENTACAO"),
						rs.getInt("CD_FORMULARIO"),
						rs.getString("ID_TIPO_DOCUMENTACAO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM GRL_TIPO_DOCUMENTACAO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoDocumentacao> getList() {
		return getList(null);
	}

	public static ArrayList<TipoDocumentacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoDocumentacao> list = new ArrayList<TipoDocumentacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoDocumentacao obj = TipoDocumentacaoDAO.get(rsm.getInt("CD_TIPO_DOCUMENTACAO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM GRL_TIPO_DOCUMENTACAO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
