package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class OcorrenciaConcessaoVeiculoDAO{

	public static int insert(OcorrenciaConcessaoVeiculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(OcorrenciaConcessaoVeiculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.OcorrenciaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ocorrencia_concessao_veiculo (cd_ocorrencia,"+
			                                  "cd_concessao_veiculo,"+
			                                  "lg_manutencao_anterior,"+
			                                  "lg_manutencao_posterior) VALUES (?, ?, ?, ?)");
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOcorrencia());
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessaoVeiculo());
			pstmt.setInt(3,objeto.getLgManutencaoAnterior());
			pstmt.setInt(4,objeto.getLgManutencaoPosterior());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaConcessaoVeiculoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaConcessaoVeiculoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OcorrenciaConcessaoVeiculo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(OcorrenciaConcessaoVeiculo objeto, int cdOcorrenciaOld) {
		return update(objeto, cdOcorrenciaOld, null);
	}

	public static int update(OcorrenciaConcessaoVeiculo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(OcorrenciaConcessaoVeiculo objeto, int cdOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			OcorrenciaConcessaoVeiculo objetoTemp = get(objeto.getCdOcorrencia(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO mob_ocorrencia_concessao_veiculo (cd_ocorrencia,"+
			                                  "cd_concessao_veiculo,"+
			                                  "lg_manutencao_anterior,"+
			                                  "lg_manutencao_posterior) VALUES (?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE mob_ocorrencia_concessao_veiculo SET cd_ocorrencia=?,"+
												      		   "cd_concessao_veiculo=?,"+
												      		   "lg_manutencao_anterior=?,"+
												      		   "lg_manutencao_posterior=? WHERE cd_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdOcorrencia());
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessaoVeiculo());
			pstmt.setInt(3,objeto.getLgManutencaoAnterior());
			pstmt.setInt(4,objeto.getLgManutencaoPosterior());
			if (objetoTemp != null) {
				pstmt.setInt(5, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.OcorrenciaDAO.update(objeto, connect)<=0) {
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
			System.err.println("Erro! OcorrenciaConcessaoVeiculoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaConcessaoVeiculoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOcorrencia) {
		return delete(cdOcorrencia, null);
	}

	public static int delete(int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ocorrencia_concessao_veiculo WHERE cd_ocorrencia=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.OcorrenciaDAO.delete(cdOcorrencia, connect)<=0) {
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
			System.err.println("Erro! OcorrenciaConcessaoVeiculoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaConcessaoVeiculoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OcorrenciaConcessaoVeiculo get(int cdOcorrencia) {
		return get(cdOcorrencia, null);
	}

	public static OcorrenciaConcessaoVeiculo get(int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ocorrencia_concessao_veiculo A, grl_ocorrencia B WHERE A.cd_ocorrencia=B.cd_ocorrencia AND A.cd_ocorrencia=?");
			pstmt.setInt(1, cdOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OcorrenciaConcessaoVeiculo(rs.getInt("cd_ocorrencia"),
						rs.getInt("cd_pessoa"),
						rs.getString("txt_ocorrencia"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getInt("cd_tipo_ocorrencia"),
						rs.getInt("st_ocorrencia"),
						rs.getInt("cd_sistema"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_concessao_veiculo"),
						rs.getInt("lg_manutencao_anterior"),
						rs.getInt("lg_manutencao_posterior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaConcessaoVeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaConcessaoVeiculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ocorrencia_concessao_veiculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaConcessaoVeiculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaConcessaoVeiculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OcorrenciaConcessaoVeiculo> getList() {
		return getList(null);
	}

	public static ArrayList<OcorrenciaConcessaoVeiculo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OcorrenciaConcessaoVeiculo> list = new ArrayList<OcorrenciaConcessaoVeiculo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OcorrenciaConcessaoVeiculo obj = OcorrenciaConcessaoVeiculoDAO.get(rsm.getInt("cd_ocorrencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaConcessaoVeiculoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ocorrencia_concessao_veiculo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}