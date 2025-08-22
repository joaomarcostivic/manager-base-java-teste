package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class ServicoRecorteOcorrenciaDAO{

	public static int insert(ServicoRecorteOcorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(ServicoRecorteOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ocorrencia");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_servico");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdServico()));
			int code = Conexao.getSequenceCode("prc_servico_recorte_ocorrencia", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_servico_recorte_ocorrencia (cd_ocorrencia,"+
			                                  "cd_servico,"+
			                                  "dt_ocorrencia,"+
			                                  "tp_ocorrencia,"+
			                                  "id_resposta_servico,"+
			                                  "ds_resposta_servico,"+
			                                  "txt_resposta_servico,"+
			                                  "cd_processo,"+
			                                  "cd_recorte) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdServico());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpOcorrencia());
			pstmt.setInt(5,objeto.getIdRespostaServico());
			pstmt.setString(6,objeto.getDsRespostaServico());
			pstmt.setString(7,objeto.getTxtRespostaServico());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdProcesso());
			if(objeto.getCdRecorte()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdRecorte());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ServicoRecorteOcorrencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ServicoRecorteOcorrencia objeto, int cdOcorrenciaOld, int cdServicoOld) {
		return update(objeto, cdOcorrenciaOld, cdServicoOld, null);
	}

	public static int update(ServicoRecorteOcorrencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ServicoRecorteOcorrencia objeto, int cdOcorrenciaOld, int cdServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_servico_recorte_ocorrencia SET cd_ocorrencia=?,"+
												      		   "cd_servico=?,"+
												      		   "dt_ocorrencia=?,"+
												      		   "tp_ocorrencia=?,"+
												      		   "id_resposta_servico=?,"+
												      		   "ds_resposta_servico=?,"+
												      		   "txt_resposta_servico=?,"+
												      		   "cd_processo=?,"+
												      		   "cd_recorte=? WHERE cd_ocorrencia=? AND cd_servico=?");
			pstmt.setInt(1,objeto.getCdOcorrencia());
			pstmt.setInt(2,objeto.getCdServico());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpOcorrencia());
			pstmt.setInt(5,objeto.getIdRespostaServico());
			pstmt.setString(6,objeto.getDsRespostaServico());
			pstmt.setString(7,objeto.getTxtRespostaServico());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdProcesso());
			if(objeto.getCdRecorte()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdRecorte());
			pstmt.setInt(10, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			pstmt.setInt(11, cdServicoOld!=0 ? cdServicoOld : objeto.getCdServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOcorrencia, int cdServico) {
		return delete(cdOcorrencia, cdServico, null);
	}

	public static int delete(int cdOcorrencia, int cdServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_servico_recorte_ocorrencia WHERE cd_ocorrencia=? AND cd_servico=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.setInt(2, cdServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ServicoRecorteOcorrencia get(int cdOcorrencia, int cdServico) {
		return get(cdOcorrencia, cdServico, null);
	}

	public static ServicoRecorteOcorrencia get(int cdOcorrencia, int cdServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_servico_recorte_ocorrencia WHERE cd_ocorrencia=? AND cd_servico=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.setInt(2, cdServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ServicoRecorteOcorrencia(rs.getInt("cd_ocorrencia"),
						rs.getInt("cd_servico"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getInt("tp_ocorrencia"),
						rs.getInt("id_resposta_servico"),
						rs.getString("ds_resposta_servico"),
						rs.getString("txt_resposta_servico"),
						rs.getInt("cd_processo"),
						rs.getInt("cd_recorte"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_servico_recorte_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ServicoRecorteOcorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<ServicoRecorteOcorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ServicoRecorteOcorrencia> list = new ArrayList<ServicoRecorteOcorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ServicoRecorteOcorrencia obj = ServicoRecorteOcorrenciaDAO.get(rsm.getInt("cd_ocorrencia"), rsm.getInt("cd_servico"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_servico_recorte_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
