package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class VeiculoCheckupDAO{

	public static int insert(VeiculoCheckup objeto) {
		return insert(objeto, null);
	}

	public static int insert(VeiculoCheckup objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_veiculo_checkup", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCheckup(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_veiculo_checkup (cd_checkup,"+
			                                  "cd_tipo_checkup,"+
			                                  "cd_veiculo,"+
			                                  "dt_checkup,"+
			                                  "txt_observacao,"+
			                                  "cd_viagem,"+
			                                  "cd_agendamento,"+
			                                  "txt_diagnostico,"+
			                                  "st_checkup,"+
			                                  "tp_origem,"+
			                                  "dt_prazo_conclusao,"+
			                                  "cd_usuario,"+
			                                  "cd_usuario_responsavel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTipoCheckup()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoCheckup());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVeiculo());
			if(objeto.getDtCheckup()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCheckup().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtObservacao());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdViagem());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdAgendamento());
			pstmt.setString(8,objeto.getTxtDiagnostico());
			pstmt.setInt(9,objeto.getStCheckup());
			pstmt.setInt(10,objeto.getTpOrigem());
			if(objeto.getDtPrazoConclusao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtPrazoConclusao().getTimeInMillis()));
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdUsuario());
			if(objeto.getCdUsuarioResponsavel()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdUsuarioResponsavel());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VeiculoCheckup objeto) {
		return update(objeto, 0, null);
	}

	public static int update(VeiculoCheckup objeto, int cdCheckupOld) {
		return update(objeto, cdCheckupOld, null);
	}

	public static int update(VeiculoCheckup objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(VeiculoCheckup objeto, int cdCheckupOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_veiculo_checkup SET cd_checkup=?,"+
												      		   "cd_tipo_checkup=?,"+
												      		   "cd_veiculo=?,"+
												      		   "dt_checkup=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_viagem=?,"+
												      		   "cd_agendamento=?,"+
												      		   "txt_diagnostico=?,"+
												      		   "st_checkup=?,"+
												      		   "tp_origem=?,"+
												      		   "dt_prazo_conclusao=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_usuario_responsavel=? WHERE cd_checkup=?");
			pstmt.setInt(1,objeto.getCdCheckup());
			if(objeto.getCdTipoCheckup()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoCheckup());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVeiculo());
			if(objeto.getDtCheckup()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCheckup().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtObservacao());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdViagem());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdAgendamento());
			pstmt.setString(8,objeto.getTxtDiagnostico());
			pstmt.setInt(9,objeto.getStCheckup());
			pstmt.setInt(10,objeto.getTpOrigem());
			if(objeto.getDtPrazoConclusao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtPrazoConclusao().getTimeInMillis()));
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdUsuario());
			if(objeto.getCdUsuarioResponsavel()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdUsuarioResponsavel());
			pstmt.setInt(14, cdCheckupOld!=0 ? cdCheckupOld : objeto.getCdCheckup());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCheckup) {
		return delete(cdCheckup, null);
	}

	public static int delete(int cdCheckup, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_veiculo_checkup WHERE cd_checkup=?");
			pstmt.setInt(1, cdCheckup);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VeiculoCheckup get(int cdCheckup) {
		return get(cdCheckup, null);
	}

	public static VeiculoCheckup get(int cdCheckup, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_veiculo_checkup WHERE cd_checkup=?");
			pstmt.setInt(1, cdCheckup);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VeiculoCheckup(rs.getInt("cd_checkup"),
						rs.getInt("cd_tipo_checkup"),
						rs.getInt("cd_veiculo"),
						(rs.getTimestamp("dt_checkup")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_checkup").getTime()),
						rs.getString("txt_observacao"),
						rs.getInt("cd_viagem"),
						rs.getInt("cd_agendamento"),
						rs.getString("txt_diagnostico"),
						rs.getInt("st_checkup"),
						rs.getInt("tp_origem"),
						(rs.getTimestamp("dt_prazo_conclusao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_prazo_conclusao").getTime()),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_usuario_responsavel"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_veiculo_checkup");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_veiculo_checkup", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
