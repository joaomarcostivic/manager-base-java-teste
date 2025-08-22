package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RegraFatCidadeDAO{

	public static int insert(RegraFatCidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegraFatCidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_REGRA_FAT_CIDADE (CD_REGRA_FATURAMENTO,"+
			                                  "CD_CIDADE) VALUES (?, ?)");
			if(objeto.getCdRegraFaturamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRegraFaturamento());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatCidadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatCidadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegraFatCidade objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RegraFatCidade objeto, int cdCidadeOld, int cdRegraFaturamentoOld) {
		return update(objeto, cdCidadeOld, cdRegraFaturamentoOld, null);
	}

	public static int update(RegraFatCidade objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RegraFatCidade objeto, int cdCidadeOld, int cdRegraFaturamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_REGRA_FAT_CIDADE SET CD_REGRA_FATURAMENTO=?,"+
												      		   "CD_CIDADE=? WHERE CD_CIDADE=? AND CD_REGRA_FATURAMENTO=?");
			pstmt.setInt(1,objeto.getCdRegraFaturamento());
			pstmt.setInt(2,objeto.getCdCidade());
			pstmt.setInt(3, cdCidadeOld!=0 ? cdCidadeOld : objeto.getCdCidade());
			pstmt.setInt(4, cdRegraFaturamentoOld!=0 ? cdRegraFaturamentoOld : objeto.getCdRegraFaturamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatCidadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatCidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCidade, int cdRegraFaturamento) {
		return delete(cdCidade, cdRegraFaturamento, null);
	}

	public static int delete(int cdCidade, int cdRegraFaturamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_REGRA_FAT_CIDADE WHERE CD_CIDADE=? AND CD_REGRA_FATURAMENTO=?");
			pstmt.setInt(1, cdCidade);
			pstmt.setInt(2, cdRegraFaturamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatCidadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatCidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegraFatCidade get(int cdCidade, int cdRegraFaturamento) {
		return get(cdCidade, cdRegraFaturamento, null);
	}

	public static RegraFatCidade get(int cdCidade, int cdRegraFaturamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_CIDADE WHERE CD_CIDADE=? AND CD_REGRA_FATURAMENTO=?");
			pstmt.setInt(1, cdCidade);
			pstmt.setInt(2, cdRegraFaturamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegraFatCidade(rs.getInt("CD_REGRA_FATURAMENTO"),
						rs.getInt("CD_CIDADE"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatCidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatCidadeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_CIDADE");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatCidadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatCidadeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RegraFatCidade> getList() {
		return getList(null);
	}

	public static ArrayList<RegraFatCidade> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RegraFatCidade> list = new ArrayList<RegraFatCidade>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RegraFatCidade obj = RegraFatCidadeDAO.get(rsm.getInt("CD_CIDADE"), rsm.getInt("CD_REGRA_FATURAMENTO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatCidadeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_REGRA_FAT_CIDADE", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
