package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ComponenteVeiculoDAO{

	public static int insert(ComponenteVeiculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(ComponenteVeiculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.bpm.ComponenteReferenciaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdComponente(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_componente_veiculo (cd_componente,"+
			                                  "cd_tipo_componente,"+
			                                  "cd_marca,"+
			                                  "qt_hodometro_ultima_manutencao,"+
			                                  "qt_hodometro_validade,"+
			                                  "qt_hodometro_manutencao,"+
			                                  "tp_recorrencia_manutencao,"+
			                                  "qt_intervalo_recorrencia,"+
			                                  "dt_instalacao,"+
			                                  "txt_observacao,"+
			                                  "dt_inicio_recorrencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdComponente()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdComponente());
			if(objeto.getCdTipoComponente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoComponente());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdMarca());
			pstmt.setFloat(4,objeto.getQtHodometroUltimaManutencao());
			pstmt.setFloat(5,objeto.getQtHodometroValidade());
			pstmt.setFloat(6,objeto.getQtHodometroManutencao());
			pstmt.setInt(7,objeto.getTpRecorrenciaManutencao());
			pstmt.setInt(8,objeto.getQtIntervaloRecorrencia());
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			pstmt.setString(10,objeto.getTxtObservacao());
			if(objeto.getDtInicioRecorrencia()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtInicioRecorrencia().getTimeInMillis()));
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteVeiculoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteVeiculoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ComponenteVeiculo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ComponenteVeiculo objeto, int cdComponenteOld) {
		return update(objeto, cdComponenteOld, null);
	}

	public static int update(ComponenteVeiculo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ComponenteVeiculo objeto, int cdComponenteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			ComponenteVeiculo objetoTemp = get(objeto.getCdComponente(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO fta_componente_veiculo (cd_componente,"+
			                                  "cd_tipo_componente,"+
			                                  "cd_marca,"+
			                                  "qt_hodometro_ultima_manutencao,"+
			                                  "qt_hodometro_validade,"+
			                                  "qt_hodometro_manutencao,"+
			                                  "tp_recorrencia_manutencao,"+
			                                  "qt_intervalo_recorrencia,"+
			                                  "dt_instalacao,"+
			                                  "txt_observacao,"+
			                                  "dt_inicio_recorrencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE fta_componente_veiculo SET cd_componente=?,"+
												      		   "cd_tipo_componente=?,"+
												      		   "cd_marca=?,"+
												      		   "qt_hodometro_ultima_manutencao=?,"+
												      		   "qt_hodometro_validade=?,"+
												      		   "qt_hodometro_manutencao=?,"+
												      		   "tp_recorrencia_manutencao=?,"+
												      		   "qt_intervalo_recorrencia=?,"+
												      		   "dt_instalacao=?,"+
												      		   "txt_observacao=?,"+
												      		   "dt_inicio_recorrencia=? WHERE cd_componente=?");
			pstmt.setInt(1,objeto.getCdComponente());
			if(objeto.getCdTipoComponente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoComponente());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdMarca());
			pstmt.setFloat(4,objeto.getQtHodometroUltimaManutencao());
			pstmt.setFloat(5,objeto.getQtHodometroValidade());
			pstmt.setFloat(6,objeto.getQtHodometroManutencao());
			pstmt.setInt(7,objeto.getTpRecorrenciaManutencao());
			pstmt.setInt(8,objeto.getQtIntervaloRecorrencia());
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			pstmt.setString(10,objeto.getTxtObservacao());
			if(objeto.getDtInicioRecorrencia()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtInicioRecorrencia().getTimeInMillis()));
			if (objetoTemp != null) {
				pstmt.setInt(12, cdComponenteOld!=0 ? cdComponenteOld : objeto.getCdComponente());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.bpm.ComponenteReferenciaDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteVeiculoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteVeiculoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdComponente) {
		return delete(cdComponente, null);
	}

	public static int delete(int cdComponente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_componente_veiculo WHERE cd_componente=?");
			pstmt.setInt(1, cdComponente);
			pstmt.executeUpdate();
			if (com.tivic.manager.bpm.ComponenteReferenciaDAO.delete(cdComponente, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteVeiculoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteVeiculoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ComponenteVeiculo get(int cdComponente) {
		return get(cdComponente, null);
	}

	public static ComponenteVeiculo get(int cdComponente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_componente_veiculo A, bpm_componente_referencia B WHERE A.cd_componente=B.cd_componente AND A.cd_componente=?");
			pstmt.setInt(1, cdComponente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ComponenteVeiculo(rs.getInt("cd_componente"),
						rs.getInt("cd_referencia"),
						rs.getString("nm_componente"),
						(rs.getTimestamp("dt_garantia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_garantia").getTime()),
						(rs.getTimestamp("dt_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade").getTime()),
						(rs.getTimestamp("dt_aquisicao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_aquisicao").getTime()),
						(rs.getTimestamp("dt_baixa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_baixa").getTime()),
						rs.getString("nr_serie"),
						rs.getInt("st_componente"),
						rs.getInt("cd_tipo_componente"),
						rs.getInt("cd_marca"),
						rs.getFloat("qt_hodometro_ultima_manutencao"),
						rs.getFloat("qt_hodometro_validade"),
						rs.getFloat("qt_hodometro_manutencao"),
						rs.getInt("tp_recorrencia_manutencao"),
						rs.getInt("qt_intervalo_recorrencia"),
						(rs.getTimestamp("dt_instalacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_instalacao").getTime()),
						rs.getString("txt_observacao"),
						(rs.getTimestamp("dt_inicio_recorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_recorrencia").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteVeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteVeiculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_componente_veiculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteVeiculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteVeiculoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_componente_veiculo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
