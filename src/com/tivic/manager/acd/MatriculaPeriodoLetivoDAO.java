package com.tivic.manager.acd;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.ArrayList;

public class MatriculaPeriodoLetivoDAO{

	public static int insert(MatriculaPeriodoLetivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(MatriculaPeriodoLetivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_matricula_periodo_letivo (cd_matricula,"+
			                                  "cd_periodo_letivo,"+
			                                  "cd_contrato,"+
			                                  "dt_matricula,"+
			                                  "st_matricula,"+
			                                  "cd_motivo_trancamento,"+
			                                  "cd_pedido_venda,"+
			                                  "cd_rota) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMatricula());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPeriodoLetivo());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContrato());
			if(objeto.getDtMatricula()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtMatricula().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStMatricula());
			if(objeto.getCdMotivoTrancamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdMotivoTrancamento());
			if(objeto.getCdPedidoVenda()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPedidoVenda());
			if(objeto.getCdRota()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdRota());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MatriculaPeriodoLetivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MatriculaPeriodoLetivo objeto, int cdMatriculaOld, int cdPeriodoLetivoOld) {
		return update(objeto, cdMatriculaOld, cdPeriodoLetivoOld, null);
	}

	public static int update(MatriculaPeriodoLetivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MatriculaPeriodoLetivo objeto, int cdMatriculaOld, int cdPeriodoLetivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_matricula_periodo_letivo SET cd_matricula=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "cd_contrato=?,"+
												      		   "dt_matricula=?,"+
												      		   "st_matricula=?,"+
												      		   "cd_motivo_trancamento=?,"+
												      		   "cd_pedido_venda=?,"+
												      		   "cd_rota=? WHERE cd_matricula=? AND cd_periodo_letivo=?");
			pstmt.setInt(1,objeto.getCdMatricula());
			pstmt.setInt(2,objeto.getCdPeriodoLetivo());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContrato());
			if(objeto.getDtMatricula()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtMatricula().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStMatricula());
			if(objeto.getCdMotivoTrancamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdMotivoTrancamento());
			if(objeto.getCdPedidoVenda()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPedidoVenda());
			if(objeto.getCdRota()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdRota());
			pstmt.setInt(9, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.setInt(10, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula, int cdPeriodoLetivo) {
		return delete(cdMatricula, cdPeriodoLetivo, null);
	}

	public static int delete(int cdMatricula, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_matricula_periodo_letivo WHERE cd_matricula=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdPeriodoLetivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MatriculaPeriodoLetivo get(int cdMatricula, int cdPeriodoLetivo) {
		return get(cdMatricula, cdPeriodoLetivo, null);
	}

	public static MatriculaPeriodoLetivo get(int cdMatricula, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_periodo_letivo WHERE cd_matricula=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaPeriodoLetivo(rs.getInt("cd_matricula"),
						rs.getInt("cd_periodo_letivo"),
						rs.getInt("cd_contrato"),
						(rs.getTimestamp("dt_matricula")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_matricula").getTime()),
						rs.getInt("st_matricula"),
						rs.getInt("cd_motivo_trancamento"),
						rs.getInt("cd_pedido_venda"),
						rs.getInt("cd_rota"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_periodo_letivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MatriculaPeriodoLetivo> getList() {
		return getList(null);
	}

	public static ArrayList<MatriculaPeriodoLetivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MatriculaPeriodoLetivo> list = new ArrayList<MatriculaPeriodoLetivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MatriculaPeriodoLetivo obj = MatriculaPeriodoLetivoDAO.get(rsm.getInt("cd_matricula"), rsm.getInt("cd_periodo_letivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaPeriodoLetivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_periodo_letivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}