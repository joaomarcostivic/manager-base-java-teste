package com.tivic.manager.pcb;

import java.sql.*;

import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class TanqueHistoricoDAO{

	public static int insert(TanqueHistorico objeto) {
		return insert(objeto, null);
	}

	public static int insert(TanqueHistorico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			@SuppressWarnings("unchecked")
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tanque_historico");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_tanque");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdTanque()));
			int code = Conexao.getSequenceCode("pcb_tanque_historico", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTanqueHistorico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO pcb_tanque_historico (cd_tanque_historico,"+
			                                  "cd_tanque,"+
			                                  "tp_tanque_historico,"+
			                                  "dt_tanque_historico,"+
			                                  "qt_lastro,"+
			                                  "st_tanque,"+
			                                  "dt_cadastro,"+
			                                  "dt_instalacao,"+
			                                  "txt_observacao,"+
			                                  "cd_tipo_tanque,"+
			                                  "nm_local_armazenamento," +
			                                  "id_local_armazenamento," +
			                                  "cd_combustivel_anterior," +
			                                  "cd_combustivel_novo," +
			                                  "cd_turno," +
			                                  "cd_usuario," +
			                                  "nr_intervencao," +
			                                  "ds_motivo_intervencao," +
			                                  "cd_tecnico," +
			                                  "cd_empresa_interventora) VALUES (?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTanque()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTanque());
			pstmt.setInt(3,objeto.getTpTanqueHistorico());
			if(objeto.getDtTanqueHistorico()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtTanqueHistorico().getTimeInMillis()));
			pstmt.setFloat(5,objeto.getQtLastro());
			pstmt.setInt(6,objeto.getStTanque());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			pstmt.setString(9,objeto.getTxtObservacao());
			if(objeto.getCdTipoTanque()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTipoTanque());
			pstmt.setString(11, objeto.getNmLocalArmazenamento());
			pstmt.setString(12, objeto.getIdLocalArmazenamento());
			if(objeto.getCdCombustivelAnterior()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdCombustivelAnterior());
			if(objeto.getCdCombustivelNovo()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdCombustivelNovo());
			if(objeto.getCdTurno()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTurno());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdUsuario());
			pstmt.setInt(17,objeto.getNrIntervencao());
			pstmt.setString(18,objeto.getDsMotivoIntervencao());
			if(objeto.getCdTecnico()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdTecnico());
			if(objeto.getCdEmpresaInterventora()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdEmpresaInterventora());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TanqueHistoricoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TanqueHistorico objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TanqueHistorico objeto, int cdTanqueHistoricoOld, int cdTanqueOld) {
		return update(objeto, cdTanqueHistoricoOld, cdTanqueOld, null);
	}

	public static int update(TanqueHistorico objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TanqueHistorico objeto, int cdTanqueHistoricoOld, int cdTanqueOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE pcb_tanque_historico SET cd_tanque_historico=?,"+
												      		   "cd_tanque=?,"+
												      		   "tp_tanque_historico=?,"+
												      		   "dt_tanque_historico=?,"+
												      		   "qt_lastro=?,"+
												      		   "st_tanque=?,"+
												      		   "dt_cadastro=?,"+
												      		   "dt_instalacao=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_tipo_tanque=?,"+
												      		   "cd_combustivel_anterior=?," +
												      		   "cd_combustivel_novo=?," +
												      		   "cd_turno=?," +
												      		   "cd_usuario=?," +
												      		   "nr_intervencao=?," +
												      		   "ds_motivo_intervencao=?," +
												      		   "cd_tecnico=?," +
												      		   "cd_empresa_interventora=? WHERE cd_tanque_historico=? AND cd_tanque=?");
			pstmt.setInt(1,objeto.getCdTanqueHistorico());
			pstmt.setInt(2,objeto.getCdTanque());
			pstmt.setInt(3,objeto.getTpTanqueHistorico());
			if(objeto.getDtTanqueHistorico()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtTanqueHistorico().getTimeInMillis()));
			pstmt.setFloat(5,objeto.getQtLastro());
			pstmt.setInt(6,objeto.getStTanque());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			pstmt.setString(9,objeto.getTxtObservacao());
			if(objeto.getCdTipoTanque()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTipoTanque());
			if(objeto.getCdCombustivelAnterior()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdCombustivelAnterior());
			if(objeto.getCdCombustivelNovo()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdCombustivelNovo());
			if(objeto.getCdTurno()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdTurno());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdUsuario());
			pstmt.setInt(15,objeto.getNrIntervencao());
			pstmt.setString(16,objeto.getDsMotivoIntervencao());
			if(objeto.getCdTecnico()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdTecnico());
			if(objeto.getCdEmpresaInterventora()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdEmpresaInterventora());
			pstmt.setInt(19, cdTanqueHistoricoOld!=0 ? cdTanqueHistoricoOld : objeto.getCdTanqueHistorico());
			pstmt.setInt(20, cdTanqueOld!=0 ? cdTanqueOld : objeto.getCdTanque());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TanqueHistoricoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTanqueHistorico, int cdTanque) {
		return delete(cdTanqueHistorico, cdTanque, null);
	}

	public static int delete(int cdTanqueHistorico, int cdTanque, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM pcb_tanque_historico WHERE cd_tanque_historico=? AND cd_tanque=?");
			pstmt.setInt(1, cdTanqueHistorico);
			pstmt.setInt(2, cdTanque);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TanqueHistoricoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TanqueHistorico get(int cdTanqueHistorico, int cdTanque) {
		return get(cdTanqueHistorico, cdTanque, null);
	}

	public static TanqueHistorico get(int cdTanqueHistorico, int cdTanque, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM pcb_tanque_historico WHERE cd_tanque_historico=? AND cd_tanque=?");
			pstmt.setInt(1, cdTanqueHistorico);
			pstmt.setInt(2, cdTanque);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TanqueHistorico(rs.getInt("cd_tanque_historico"),
						rs.getInt("cd_tanque"),
						rs.getInt("tp_tanque_historico"),
						(rs.getTimestamp("dt_tanque_historico")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_tanque_historico").getTime()),
						rs.getInt("qt_lastro"),
						rs.getInt("st_tanque"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						(rs.getTimestamp("dt_instalacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_instalacao").getTime()),
						rs.getString("txt_observacao"),
						rs.getInt("cd_tipo_tanque"),
						rs.getString("nm_local_armazenamento"),
						rs.getString("id_local_armazenamento"),
						rs.getInt("cd_combustivel_anterior"),
						rs.getInt("cd_combustivel_novo"),
						rs.getInt("cd_turno"),
						rs.getInt("cd_usuario"),
						rs.getInt("nr_intervencao"),
						rs.getString("ds_motivo_intervencao"),
						rs.getInt("cd_tecnico"),
						rs.getInt("cd_empresa_interventora"));
			}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TanqueHistoricoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM pcb_tanque_historico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TanqueHistoricoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM pcb_tanque_historico", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
