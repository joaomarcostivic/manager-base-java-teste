package com.tivic.manager.pcb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class BicoHistoricoDAO{

	public static int insert(BicoHistorico objeto) {
		return insert(objeto, null);
	}

	public static int insert(BicoHistorico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			@SuppressWarnings("unchecked")
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_bico_historico");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_bico");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdBico()));
			int code = Conexao.getSequenceCode("pcb_bico_historico", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdBicoHistorico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_bico_historico (cd_bico_historico,"+
			                                  "cd_bico,"+
			                                  "tp_bico_historico,"+
			                                  "dt_bico_historico,"+
			                                  "cd_turno,"+
			                                  "id_bico_anterior,"+
			                                  "id_bico_novo,"+
			                                  "qt_encerrante_final,"+
			                                  "qt_encerrante_inicial,"+
			                                  "vl_encerrante_final,"+
			                                  "vl_encerrante_inicial,"+
			                                  "txt_observacao,"+
			                                  "nr_intervencao,"+
			                                  "ds_motivo_intervencao,"+
			                                  "cd_tanque_anterior,"+
			                                  "cd_tanque_novo,"+
			                                  "cd_usuario,"+
			                                  "cd_tecnico,"+
			                                  "cd_empresa_interventora) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdBico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdBico());
			pstmt.setInt(3,objeto.getTpBicoHistorico());
			if(objeto.getDtBicoHistorico()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtBicoHistorico().getTimeInMillis()));
			if(objeto.getCdTurno()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTurno());
			pstmt.setString(6,objeto.getIdBicoAnterior());
			pstmt.setString(7,objeto.getIdBicoNovo());
			pstmt.setFloat(8,objeto.getQtEncerranteFinal());
			pstmt.setFloat(9,objeto.getQtEncerranteInicial());
			pstmt.setFloat(10,objeto.getVlEncerranteFinal());
			pstmt.setFloat(11,objeto.getVlEncerranteInicial());
			pstmt.setString(12,objeto.getTxtObservacao());
			pstmt.setInt(13,objeto.getNrIntervencao());
			pstmt.setString(14,objeto.getDsMotivoIntervencao());
			if(objeto.getCdTanqueAnterior()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTanqueAnterior());
			if(objeto.getCdTanqueNovo()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTanqueNovo());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdUsuario());
			if(objeto.getCdTecnico()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdTecnico());
			if(objeto.getCdEmpresaInterventora()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdEmpresaInterventora());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BicoHistoricoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BicoHistorico objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(BicoHistorico objeto, int cdBicoHistoricoOld, int cdBicoOld) {
		return update(objeto, cdBicoHistoricoOld, cdBicoOld, null);
	}

	public static int update(BicoHistorico objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(BicoHistorico objeto, int cdBicoHistoricoOld, int cdBicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE pcb_bico_historico SET cd_bico_historico=?,"+
												      		   "cd_bico=?,"+
												      		   "tp_bico_historico=?,"+
												      		   "dt_bico_historico=?,"+
												      		   "cd_turno=?,"+
												      		   "id_bico_anterior=?,"+
												      		   "id_bico_novo=?,"+
												      		   "qt_encerrante_final=?,"+
												      		   "qt_encerrante_inicial=?,"+
												      		   "vl_encerrante_final=?,"+
												      		   "vl_encerrante_inicial=?,"+
												      		   "txt_observacao=?,"+
												      		   "nr_intervencao=?,"+
												      		   "ds_motivo_intervencao=?,"+
												      		   "cd_tanque_anterior=?,"+
												      		   "cd_tanque_novo=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_tecnico=?,"+
												      		   "cd_empresa_interventora=? WHERE cd_bico_historico=? AND cd_bico=?");
			pstmt.setInt(1,objeto.getCdBicoHistorico());
			pstmt.setInt(2,objeto.getCdBico());
			pstmt.setInt(3,objeto.getTpBicoHistorico());
			if(objeto.getDtBicoHistorico()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtBicoHistorico().getTimeInMillis()));
			if(objeto.getCdTurno()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTurno());
			pstmt.setString(6,objeto.getIdBicoAnterior());
			pstmt.setString(7,objeto.getIdBicoNovo());
			pstmt.setFloat(8,objeto.getQtEncerranteFinal());
			pstmt.setFloat(9,objeto.getQtEncerranteInicial());
			pstmt.setFloat(10,objeto.getVlEncerranteFinal());
			pstmt.setFloat(11,objeto.getVlEncerranteInicial());
			pstmt.setString(12,objeto.getTxtObservacao());
			pstmt.setInt(13,objeto.getNrIntervencao());
			pstmt.setString(14,objeto.getDsMotivoIntervencao());
			if(objeto.getCdTanqueAnterior()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTanqueAnterior());
			if(objeto.getCdTanqueNovo()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTanqueNovo());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdUsuario());
			if(objeto.getCdTecnico()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdTecnico());
			if(objeto.getCdEmpresaInterventora()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdEmpresaInterventora());
			pstmt.setInt(20, cdBicoHistoricoOld!=0 ? cdBicoHistoricoOld : objeto.getCdBicoHistorico());
			pstmt.setInt(21, cdBicoOld!=0 ? cdBicoOld : objeto.getCdBico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BicoHistoricoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BicoHistoricoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBicoHistorico, int cdBico) {
		return delete(cdBicoHistorico, cdBico, null);
	}

	public static int delete(int cdBicoHistorico, int cdBico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM pcb_bico_historico WHERE cd_bico_historico=? AND cd_bico=?");
			pstmt.setInt(1, cdBicoHistorico);
			pstmt.setInt(2, cdBico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BicoHistoricoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BicoHistoricoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BicoHistorico get(int cdBicoHistorico, int cdBico) {
		return get(cdBicoHistorico, cdBico, null);
	}

	public static BicoHistorico get(int cdBicoHistorico, int cdBico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM pcb_bico_historico WHERE cd_bico_historico=? AND cd_bico=?");
			pstmt.setInt(1, cdBicoHistorico);
			pstmt.setInt(2, cdBico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BicoHistorico(rs.getInt("cd_bico_historico"),
						rs.getInt("cd_bico"),
						rs.getInt("tp_bico_historico"),
						(rs.getTimestamp("dt_bico_historico")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_bico_historico").getTime()),
						rs.getInt("cd_turno"),
						rs.getString("id_bico_anterior"),
						rs.getString("id_bico_novo"),
						rs.getFloat("qt_encerrante_final"),
						rs.getFloat("qt_encerrante_inicial"),
						rs.getFloat("vl_encerrante_final"),
						rs.getFloat("vl_encerrante_inicial"),
						rs.getString("txt_observacao"),
						rs.getInt("nr_intervencao"),
						rs.getString("ds_motivo_intervencao"),
						rs.getInt("cd_tanque_anterior"),
						rs.getInt("cd_tanque_novo"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_tecnico"),
						rs.getInt("cd_empresa_interventora"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BicoHistoricoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BicoHistoricoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM pcb_bico_historico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BicoHistoricoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BicoHistoricoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM pcb_bico_historico", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
