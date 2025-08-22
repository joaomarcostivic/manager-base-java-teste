package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class EstatisticaDAO{

	public static int insert(Estatistica objeto) {
		return insert(objeto, null);
	}

	public static int insert(Estatistica objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_apuracao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_periodo_letivo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdPeriodoLetivo()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_instituicao");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdInstituicao()));
			int code = Conexao.getSequenceCode("acd_estatistica", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdApuracao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_estatistica (cd_apuracao,"+
			                                  "cd_periodo_letivo,"+
			                                  "cd_instituicao,"+
			                                  "dt_apuracao,"+
			                                  "qt_total_matriculas,"+
			                                  "qt_matriculas_zona_rural,"+
			                                  "qt_matriculas_zona_urbana,"+
			                                  "qt_vagas,"+
			                                  "qt_vagas_zona_rural,"+
			                                  "qt_vagas_zona_urbana,"+
			                                  "qt_turmas) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPeriodoLetivo());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdInstituicao());
			if(objeto.getDtApuracao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtApuracao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getQtTotalMatriculas());
			pstmt.setInt(6,objeto.getQtMatriculasZonaRural());
			pstmt.setInt(7,objeto.getQtMatriculasZonaUrbana());
			pstmt.setInt(8,objeto.getQtVagas());
			pstmt.setInt(9,objeto.getQtVagasZonaRural());
			pstmt.setInt(10,objeto.getQtVagasZonaUrbana());
			pstmt.setInt(11,objeto.getQtTurmas());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Estatistica objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(Estatistica objeto, int cdApuracaoOld, int cdPeriodoLetivoOld, int cdInstituicaoOld) {
		return update(objeto, cdApuracaoOld, cdPeriodoLetivoOld, cdInstituicaoOld, null);
	}

	public static int update(Estatistica objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(Estatistica objeto, int cdApuracaoOld, int cdPeriodoLetivoOld, int cdInstituicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_estatistica SET cd_apuracao=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "cd_instituicao=?,"+
												      		   "dt_apuracao=?,"+
												      		   "qt_total_matriculas=?,"+
												      		   "qt_matriculas_zona_rural=?,"+
												      		   "qt_matriculas_zona_urbana=?,"+
												      		   "qt_vagas=?,"+
												      		   "qt_vagas_zona_rural=?,"+
												      		   "qt_vagas_zona_urbana=?,"+
												      		   "qt_turmas=? WHERE cd_apuracao=? AND cd_periodo_letivo=? AND cd_instituicao=?");
			pstmt.setInt(1,objeto.getCdApuracao());
			pstmt.setInt(2,objeto.getCdPeriodoLetivo());
			pstmt.setInt(3,objeto.getCdInstituicao());
			if(objeto.getDtApuracao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtApuracao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getQtTotalMatriculas());
			pstmt.setInt(6,objeto.getQtMatriculasZonaRural());
			pstmt.setInt(7,objeto.getQtMatriculasZonaUrbana());
			pstmt.setInt(8,objeto.getQtVagas());
			pstmt.setInt(9,objeto.getQtVagasZonaRural());
			pstmt.setInt(10,objeto.getQtVagasZonaUrbana());
			pstmt.setInt(11,objeto.getQtTurmas());
			pstmt.setInt(12, cdApuracaoOld!=0 ? cdApuracaoOld : objeto.getCdApuracao());
			pstmt.setInt(13, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.setInt(14, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdApuracao, int cdPeriodoLetivo, int cdInstituicao) {
		return delete(cdApuracao, cdPeriodoLetivo, cdInstituicao, null);
	}

	public static int delete(int cdApuracao, int cdPeriodoLetivo, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_estatistica WHERE cd_apuracao=? AND cd_periodo_letivo=? AND cd_instituicao=?");
			pstmt.setInt(1, cdApuracao);
			pstmt.setInt(2, cdPeriodoLetivo);
			pstmt.setInt(3, cdInstituicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Estatistica get(int cdApuracao, int cdPeriodoLetivo, int cdInstituicao) {
		return get(cdApuracao, cdPeriodoLetivo, cdInstituicao, null);
	}

	public static Estatistica get(int cdApuracao, int cdPeriodoLetivo, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_estatistica WHERE cd_apuracao=? AND cd_periodo_letivo=? AND cd_instituicao=?");
			pstmt.setInt(1, cdApuracao);
			pstmt.setInt(2, cdPeriodoLetivo);
			pstmt.setInt(3, cdInstituicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Estatistica(rs.getInt("cd_apuracao"),
						rs.getInt("cd_periodo_letivo"),
						rs.getInt("cd_instituicao"),
						(rs.getTimestamp("dt_apuracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_apuracao").getTime()),
						rs.getInt("qt_total_matriculas"),
						rs.getInt("qt_matriculas_zona_rural"),
						rs.getInt("qt_matriculas_zona_urbana"),
						rs.getInt("qt_vagas"),
						rs.getInt("qt_vagas_zona_rural"),
						rs.getInt("qt_vagas_zona_urbana"),
						rs.getInt("qt_turmas"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_estatistica");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Estatistica> getList() {
		return getList(null);
	}

	public static ArrayList<Estatistica> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Estatistica> list = new ArrayList<Estatistica>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Estatistica obj = EstatisticaDAO.get(rsm.getInt("cd_apuracao"), rsm.getInt("cd_periodo_letivo"), rsm.getInt("cd_instituicao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_estatistica", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}