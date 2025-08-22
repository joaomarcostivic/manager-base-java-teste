package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProcessoItemDAO{

	public static int insert(ProcessoItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProcessoItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_processo_item", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProcessoItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_processo_item (cd_processo_item,"+
			                                  "cd_processo,"+
			                                  "dt_processo,"+
			                                  "nr_processo,"+
			                                  "st_processo,"+
			                                  "cd_cliente,"+
			                                  "cd_responsavel,"+
			                                  "cd_produto_servico) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProcesso());
			if(objeto.getDtProcesso()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtProcesso().getTimeInMillis()));
			pstmt.setString(4,objeto.getNrProcesso());
			pstmt.setInt(5,objeto.getStProcesso());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCliente());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdResponsavel());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProcessoItem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ProcessoItem objeto, int cdProcessoItemOld) {
		return update(objeto, cdProcessoItemOld, null);
	}

	public static int update(ProcessoItem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ProcessoItem objeto, int cdProcessoItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_processo_item SET cd_processo_item=?,"+
												      		   "cd_processo=?,"+
												      		   "dt_processo=?,"+
												      		   "nr_processo=?,"+
												      		   "st_processo=?,"+
												      		   "cd_cliente=?,"+
												      		   "cd_responsavel=?,"+
												      		   "cd_produto_servico=? WHERE cd_processo_item=?");
			pstmt.setInt(1,objeto.getCdProcessoItem());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProcesso());
			if(objeto.getDtProcesso()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtProcesso().getTimeInMillis()));
			pstmt.setString(4,objeto.getNrProcesso());
			pstmt.setInt(5,objeto.getStProcesso());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCliente());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdResponsavel());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdProdutoServico());
			pstmt.setInt(9, cdProcessoItemOld!=0 ? cdProcessoItemOld : objeto.getCdProcessoItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProcessoItem) {
		return delete(cdProcessoItem, null);
	}

	public static int delete(int cdProcessoItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_processo_item WHERE cd_processo_item=?");
			pstmt.setInt(1, cdProcessoItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProcessoItem get(int cdProcessoItem) {
		return get(cdProcessoItem, null);
	}

	public static ProcessoItem get(int cdProcessoItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_processo_item WHERE cd_processo_item=?");
			pstmt.setInt(1, cdProcessoItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProcessoItem(rs.getInt("cd_processo_item"),
						rs.getInt("cd_processo"),
						(rs.getTimestamp("dt_processo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_processo").getTime()),
						rs.getString("nr_processo"),
						rs.getInt("st_processo"),
						rs.getInt("cd_cliente"),
						rs.getInt("cd_responsavel"),
						rs.getInt("cd_produto_servico"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_processo_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_processo_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
