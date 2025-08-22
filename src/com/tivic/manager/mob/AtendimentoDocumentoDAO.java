package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AtendimentoDocumentoDAO{

	public static int insert(AtendimentoDocumento objeto) {
		return insert(objeto, null);
	}

	public static int insert(AtendimentoDocumento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_atendimento_documento (cd_concessao,"+
			                                  "cd_documento,"+
			                                  "cd_linha,"+
			                                  "cd_rota,"+
			                                  "cd_veiculo) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConcessao());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumento());
			if(objeto.getCdLinha()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdLinha());
			pstmt.setInt(4,objeto.getCdRota());
			pstmt.setInt(5,objeto.getCdVeiculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AtendimentoDocumento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AtendimentoDocumento objeto, int cdConcessaoOld, int cdDocumentoOld) {
		return update(objeto, cdConcessaoOld, cdDocumentoOld, null);
	}

	public static int update(AtendimentoDocumento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AtendimentoDocumento objeto, int cdConcessaoOld, int cdDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_atendimento_documento SET cd_concessao=?,"+
												      		   "cd_documento=?,"+
												      		   "cd_linha=?,"+
												      		   "cd_rota=?,"+
												      		   "cd_veiculo=? WHERE cd_concessao=? AND cd_documento=?");
			pstmt.setInt(1,objeto.getCdConcessao());
			pstmt.setInt(2,objeto.getCdDocumento());
			if(objeto.getCdLinha()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdLinha());
			pstmt.setInt(4,objeto.getCdRota());
			pstmt.setInt(5,objeto.getCdVeiculo());
			pstmt.setInt(6, cdConcessaoOld!=0 ? cdConcessaoOld : objeto.getCdConcessao());
			pstmt.setInt(7, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConcessao, int cdDocumento) {
		return delete(cdConcessao, cdDocumento, null);
	}

	public static int delete(int cdConcessao, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_atendimento_documento WHERE cd_concessao=? AND cd_documento=?");
			pstmt.setInt(1, cdConcessao);
			pstmt.setInt(2, cdDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AtendimentoDocumento get(int cdConcessao, int cdDocumento) {
		return get(cdConcessao, cdDocumento, null);
	}

	public static AtendimentoDocumento get(int cdConcessao, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_atendimento_documento WHERE cd_concessao=? AND cd_documento=?");
			pstmt.setInt(1, cdConcessao);
			pstmt.setInt(2, cdDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AtendimentoDocumento(rs.getInt("cd_concessao"),
						rs.getInt("cd_documento"),
						rs.getInt("cd_linha"),
						rs.getInt("cd_rota"),
						rs.getInt("cd_veiculo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_atendimento_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AtendimentoDocumento> getList() {
		return getList(null);
	}

	public static ArrayList<AtendimentoDocumento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AtendimentoDocumento> list = new ArrayList<AtendimentoDocumento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AtendimentoDocumento obj = AtendimentoDocumentoDAO.get(rsm.getInt("cd_concessao"), rsm.getInt("cd_documento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_atendimento_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}