package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class OcorrenciaTrafegoDAO{

	public static int insert(OcorrenciaTrafego objeto) {
		return insert(objeto, null);
	}

	public static int insert(OcorrenciaTrafego objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[5];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ocorrencia");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_medicao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdMedicao()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_equipamento");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdEquipamento()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_via");
			keys[3].put("IS_KEY_NATIVE", "NO");
			keys[3].put("FIELD_VALUE", new Integer(objeto.getCdVia()));
			keys[4] = new HashMap<String,Object>();
			keys[4].put("FIELD_NAME", "cd_faixa");
			keys[4].put("IS_KEY_NATIVE", "NO");
			keys[4].put("FIELD_VALUE", new Integer(objeto.getCdFaixa()));
			int code = Conexao.getSequenceCode("mob_ocorrencia_trafego", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ocorrencia_trafego (cd_ocorrencia,"+
			                                  "cd_medicao,"+
			                                  "cd_equipamento,"+
			                                  "cd_via,"+
			                                  "cd_faixa,"+
			                                  "dt_ocorrencia,"+
			                                  "nr_placa,"+
			                                  "vl_velocidade_medida,"+
			                                  "vl_comprimento_veiculo,"+
			                                  "tp_ocorrencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMedicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMedicao());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEquipamento());
			if(objeto.getCdVia()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdVia());
			if(objeto.getCdFaixa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFaixa());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(7,objeto.getNrPlaca());
			pstmt.setDouble(8,objeto.getVlVelocidadeMedida());
			pstmt.setDouble(9,objeto.getVlComprimentoVeiculo());
			pstmt.setInt(10,objeto.getTpOcorrencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OcorrenciaTrafego objeto) {
		return update(objeto, 0, 0, 0, 0, 0, null);
	}

	public static int update(OcorrenciaTrafego objeto, int cdOcorrenciaOld, int cdMedicaoOld, int cdEquipamentoOld, int cdViaOld, int cdFaixaOld) {
		return update(objeto, cdOcorrenciaOld, cdMedicaoOld, cdEquipamentoOld, cdViaOld, cdFaixaOld, null);
	}

	public static int update(OcorrenciaTrafego objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, connect);
	}

	public static int update(OcorrenciaTrafego objeto, int cdOcorrenciaOld, int cdMedicaoOld, int cdEquipamentoOld, int cdViaOld, int cdFaixaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ocorrencia_trafego SET cd_ocorrencia=?,"+
												      		   "cd_medicao=?,"+
												      		   "cd_equipamento=?,"+
												      		   "cd_via=?,"+
												      		   "cd_faixa=?,"+
												      		   "dt_ocorrencia=?,"+
												      		   "nr_placa=?,"+
												      		   "vl_velocidade_medida=?,"+
												      		   "vl_comprimento_veiculo=?,"+
												      		   "tp_ocorrencia=? WHERE cd_ocorrencia=? AND cd_medicao=? AND cd_equipamento=? AND cd_via=? AND cd_faixa=?");
			pstmt.setInt(1,objeto.getCdOcorrencia());
			pstmt.setInt(2,objeto.getCdMedicao());
			pstmt.setInt(3,objeto.getCdEquipamento());
			pstmt.setInt(4,objeto.getCdVia());
			pstmt.setInt(5,objeto.getCdFaixa());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(7,objeto.getNrPlaca());
			pstmt.setDouble(8,objeto.getVlVelocidadeMedida());
			pstmt.setDouble(9,objeto.getVlComprimentoVeiculo());
			pstmt.setInt(10,objeto.getTpOcorrencia());
			pstmt.setInt(11, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			pstmt.setInt(12, cdMedicaoOld!=0 ? cdMedicaoOld : objeto.getCdMedicao());
			pstmt.setInt(13, cdEquipamentoOld!=0 ? cdEquipamentoOld : objeto.getCdEquipamento());
			pstmt.setInt(14, cdViaOld!=0 ? cdViaOld : objeto.getCdVia());
			pstmt.setInt(15, cdFaixaOld!=0 ? cdFaixaOld : objeto.getCdFaixa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOcorrencia, int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa) {
		return delete(cdOcorrencia, cdMedicao, cdEquipamento, cdVia, cdFaixa, null);
	}

	public static int delete(int cdOcorrencia, int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ocorrencia_trafego WHERE cd_ocorrencia=? AND cd_medicao=? AND cd_equipamento=? AND cd_via=? AND cd_faixa=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.setInt(2, cdMedicao);
			pstmt.setInt(3, cdEquipamento);
			pstmt.setInt(4, cdVia);
			pstmt.setInt(5, cdFaixa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OcorrenciaTrafego get(int cdOcorrencia, int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa) {
		return get(cdOcorrencia, cdMedicao, cdEquipamento, cdVia, cdFaixa, null);
	}

	public static OcorrenciaTrafego get(int cdOcorrencia, int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ocorrencia_trafego WHERE cd_ocorrencia=? AND cd_medicao=? AND cd_equipamento=? AND cd_via=? AND cd_faixa=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.setInt(2, cdMedicao);
			pstmt.setInt(3, cdEquipamento);
			pstmt.setInt(4, cdVia);
			pstmt.setInt(5, cdFaixa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OcorrenciaTrafego(rs.getInt("cd_ocorrencia"),
						rs.getInt("cd_medicao"),
						rs.getInt("cd_equipamento"),
						rs.getInt("cd_via"),
						rs.getInt("cd_faixa"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getString("nr_placa"),
						rs.getDouble("vl_velocidade_medida"),
						rs.getDouble("vl_comprimento_veiculo"),
						rs.getInt("tp_ocorrencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ocorrencia_trafego");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OcorrenciaTrafego> getList() {
		return getList(null);
	}

	public static ArrayList<OcorrenciaTrafego> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OcorrenciaTrafego> list = new ArrayList<OcorrenciaTrafego>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OcorrenciaTrafego obj = OcorrenciaTrafegoDAO.get(rsm.getInt("cd_ocorrencia"), rsm.getInt("cd_medicao"), rsm.getInt("cd_equipamento"), rsm.getInt("cd_via"), rsm.getInt("cd_faixa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTrafegoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ocorrencia_trafego", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
