package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AfericaoCatracaDAO{

	public static int insert(AfericaoCatraca objeto) {
		return insert(objeto, null);
	}

	public static int insert(AfericaoCatraca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_afericao_catraca", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAfericaoCatraca(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_afericao_catraca (cd_afericao_catraca,"+
			                                  "cd_concessao_veiculo,"+
			                                  "cd_lacre,"+
			                                  "dt_afericao,"+
			                                  "qt_aferido,"+
			                                  "qt_hodometro,"+
			                                  "lg_hodometro_ilegivel,"+
			                                  "txt_observacao,"+
			                                  "cd_agente,"+
			                                  "cd_usuario,"+
			                                  "tp_leitura) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessaoVeiculo());
			if(objeto.getCdLacre()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdLacre());
			if(objeto.getDtAfericao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4, new Timestamp(objeto.getDtAfericao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getQtAferido());
			pstmt.setDouble(6,objeto.getQtHodometro());
			pstmt.setInt(7,objeto.getLgHodometroIlegivel());
			pstmt.setString(8,objeto.getTxtObservacao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdAgente());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdUsuario());
			pstmt.setInt(11, objeto.getTpLeitura());
			pstmt.executeUpdate();
		
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AfericaoCatraca objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AfericaoCatraca objeto, int cdAfericaoCatracaOld) {
		return update(objeto, cdAfericaoCatracaOld, null);
	}

	public static int update(AfericaoCatraca objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AfericaoCatraca objeto, int cdAfericaoCatracaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_afericao_catraca SET cd_afericao_catraca=?,"+
												      		   "cd_concessao_veiculo=?,"+
												      		   "cd_lacre=?,"+
												      		   "dt_afericao=?,"+
												      		   "qt_aferido=?,"+
												      		   "qt_hodometro=?,"+
												      		   "lg_hodometro_ilegivel=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_agente=?,"+
												      		   "cd_usuario=?,"+
												      		   "tp_leitura=? WHERE cd_afericao_catraca=?");
			pstmt.setInt(1,objeto.getCdAfericaoCatraca());
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessaoVeiculo());
			if(objeto.getCdLacre()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdLacre());
			if(objeto.getDtAfericao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAfericao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getQtAferido());
			if(Double.isNaN(objeto.getQtHodometro()))
				pstmt.setNull(6, Types.DOUBLE);
			else
				pstmt.setDouble(6,objeto.getQtHodometro());
			pstmt.setInt(7,objeto.getLgHodometroIlegivel());
			pstmt.setString(8,objeto.getTxtObservacao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdAgente());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdUsuario());
			pstmt.setInt(11, objeto.getTpLeitura());
			pstmt.setInt(12, cdAfericaoCatracaOld!=0 ? cdAfericaoCatracaOld : objeto.getCdAfericaoCatraca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAfericaoCatraca) {
		return delete(cdAfericaoCatraca, null);
	}

	public static int delete(int cdAfericaoCatraca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_afericao_catraca WHERE cd_afericao_catraca=?");
			pstmt.setInt(1, cdAfericaoCatraca);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AfericaoCatraca get(int cdAfericaoCatraca) {
		return get(cdAfericaoCatraca, null);
	}

	public static AfericaoCatraca get(int cdAfericaoCatraca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_afericao_catraca WHERE cd_afericao_catraca=?");
			pstmt.setInt(1, cdAfericaoCatraca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AfericaoCatraca(rs.getInt("cd_afericao_catraca"),
						rs.getInt("cd_concessao_veiculo"),
						rs.getInt("cd_lacre"),
						(rs.getTimestamp("dt_afericao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_afericao").getTime()),
						rs.getInt("qt_aferido"),
						rs.getInt("qt_hodometro"),
						rs.getInt("lg_hodometro_ilegivel"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_agente"),
						rs.getInt("cd_usuario"),
						rs.getInt("tp_leitura"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_afericao_catraca");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AfericaoCatraca> getList() {
		return getList(null);
	}

	public static ArrayList<AfericaoCatraca> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AfericaoCatraca> list = new ArrayList<AfericaoCatraca>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AfericaoCatraca obj = AfericaoCatracaDAO.get(rsm.getInt("cd_afericao_catraca"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_afericao_catraca", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
