package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class CentralAtendimentoDAO{

	public static int insert(CentralAtendimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(CentralAtendimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_central_atendimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCentral(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_central_atendimento (cd_central,"+
			                                  "cd_empresa,"+
			                                  "nm_central,"+
			                                  "ds_central,"+
			                                  "id_central,"+
			                                  "txt_mensagem_inicial) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setString(3,objeto.getNmCentral());
			pstmt.setString(4,objeto.getDsCentral());
			pstmt.setString(5,objeto.getIdCentral());
			pstmt.setString(6,objeto.getTxtMensagemInicial());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CentralAtendimento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CentralAtendimento objeto, int cdCentralOld) {
		return update(objeto, cdCentralOld, null);
	}

	public static int update(CentralAtendimento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CentralAtendimento objeto, int cdCentralOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_central_atendimento SET cd_central=?,"+
												      		   "cd_empresa=?,"+
												      		   "nm_central=?,"+
												      		   "ds_central=?,"+
												      		   "id_central=?,"+
												      		   "txt_mensagem_inicial=? WHERE cd_central=?");
			pstmt.setInt(1,objeto.getCdCentral());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setString(3,objeto.getNmCentral());
			pstmt.setString(4,objeto.getDsCentral());
			pstmt.setString(5,objeto.getIdCentral());
			pstmt.setString(6,objeto.getTxtMensagemInicial());
			pstmt.setInt(7, cdCentralOld!=0 ? cdCentralOld : objeto.getCdCentral());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCentral) {
		return delete(cdCentral, null);
	}

	public static int delete(int cdCentral, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_central_atendimento WHERE cd_central=?");
			pstmt.setInt(1, cdCentral);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CentralAtendimento get(int cdCentral) {
		return get(cdCentral, null);
	}

	public static CentralAtendimento get(int cdCentral, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_central_atendimento WHERE cd_central=?");
			pstmt.setInt(1, cdCentral);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CentralAtendimento(rs.getInt("cd_central"),
						rs.getInt("cd_empresa"),
						rs.getString("nm_central"),
						rs.getString("ds_central"),
						rs.getString("id_central"),
						rs.getString("txt_mensagem_inicial"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_central_atendimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralAtendimentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_central_atendimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
