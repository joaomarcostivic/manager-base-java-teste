package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class LaudoDAO{

	public static int insert(Laudo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Laudo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ord_laudo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLaudo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_laudo (cd_laudo,"+
			                                  "cd_tipo_laudo,"+
			                                  "cd_ordem_servico,"+
			                                  "cd_ordem_servico_item,"+
			                                  "nm_laudo,"+
			                                  "txt_laudo,"+
			                                  "dt_laudo,"+
			                                  "cd_documento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTipoLaudo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoLaudo());
			if(objeto.getCdOrdemServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOrdemServico());
			if(objeto.getCdOrdemServicoItem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdOrdemServicoItem());
			pstmt.setString(5,objeto.getNmLaudo());
			pstmt.setString(6,objeto.getTxtLaudo());
			if(objeto.getDtLaudo()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtLaudo().getTimeInMillis()));
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdDocumento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LaudoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LaudoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Laudo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Laudo objeto, int cdLaudoOld) {
		return update(objeto, cdLaudoOld, null);
	}

	public static int update(Laudo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Laudo objeto, int cdLaudoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_laudo SET cd_laudo=?,"+
												      		   "cd_tipo_laudo=?,"+
												      		   "cd_ordem_servico=?,"+
												      		   "cd_ordem_servico_item=?,"+
												      		   "nm_laudo=?,"+
												      		   "txt_laudo=?,"+
												      		   "dt_laudo=?,"+
												      		   "cd_documento=? WHERE cd_laudo=?");
			pstmt.setInt(1,objeto.getCdLaudo());
			if(objeto.getCdTipoLaudo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoLaudo());
			if(objeto.getCdOrdemServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOrdemServico());
			if(objeto.getCdOrdemServicoItem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdOrdemServicoItem());
			pstmt.setString(5,objeto.getNmLaudo());
			pstmt.setString(6,objeto.getTxtLaudo());
			if(objeto.getDtLaudo()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtLaudo().getTimeInMillis()));
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdDocumento());
			pstmt.setInt(9, cdLaudoOld!=0 ? cdLaudoOld : objeto.getCdLaudo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LaudoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LaudoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLaudo) {
		return delete(cdLaudo, null);
	}

	public static int delete(int cdLaudo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_laudo WHERE cd_laudo=?");
			pstmt.setInt(1, cdLaudo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LaudoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LaudoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Laudo get(int cdLaudo) {
		return get(cdLaudo, null);
	}

	public static Laudo get(int cdLaudo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_laudo WHERE cd_laudo=?");
			pstmt.setInt(1, cdLaudo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Laudo(rs.getInt("cd_laudo"),
						rs.getInt("cd_tipo_laudo"),
						rs.getInt("cd_ordem_servico"),
						rs.getInt("cd_ordem_servico_item"),
						rs.getString("nm_laudo"),
						rs.getString("txt_laudo"),
						(rs.getTimestamp("dt_laudo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_laudo").getTime()),
						rs.getInt("cd_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LaudoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LaudoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_laudo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LaudoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LaudoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Laudo> getList() {
		return getList(null);
	}

	public static ArrayList<Laudo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Laudo> list = new ArrayList<Laudo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Laudo obj = LaudoDAO.get(rsm.getInt("cd_laudo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LaudoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_laudo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
