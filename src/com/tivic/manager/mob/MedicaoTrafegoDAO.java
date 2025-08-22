package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class MedicaoTrafegoDAO{

	public static int insert(MedicaoTrafego objeto) {
		return insert(objeto, null);
	}

	public static int insert(MedicaoTrafego objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[4];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_medicao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_equipamento");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdEquipamento()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_via");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdVia()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_faixa");
			keys[3].put("IS_KEY_NATIVE", "NO");
			keys[3].put("FIELD_VALUE", new Integer(objeto.getCdFaixa()));
			int code = Conexao.getSequenceCode("mob_medicao_trafego", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMedicao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_medicao_trafego (cd_medicao,"+
			                                  "cd_equipamento,"+
			                                  "cd_via,"+
			                                  "cd_faixa,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "tp_veiculo,"+
			                                  "qt_veiculos,"+
			                                  "vl_velocidade_considerada,"+
			                                  "vl_velocidade_limite,"+
			                                  "vl_velocidade_medida,"+
			                                  "vl_velocidade_tolerada,"+
			                                  "vl_comprimento_veiculo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEquipamento());
			if(objeto.getCdVia()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVia());
			if(objeto.getCdFaixa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdFaixa());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(7,objeto.getTpVeiculo());
			pstmt.setInt(8,objeto.getQtVeiculos());
			pstmt.setDouble(9,objeto.getVlVelocidadeConsiderada());
			pstmt.setDouble(10,objeto.getVlVelocidadeLimite());
			pstmt.setDouble(11,objeto.getVlVelocidadeMedida());
			pstmt.setDouble(12,objeto.getVlVelocidadeTolerada());
			pstmt.setDouble(13,objeto.getVlComprimentoVeiculo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MedicaoTrafego objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(MedicaoTrafego objeto, int cdMedicaoOld, int cdEquipamentoOld, int cdViaOld, int cdFaixaOld) {
		return update(objeto, cdMedicaoOld, cdEquipamentoOld, cdViaOld, cdFaixaOld, null);
	}

	public static int update(MedicaoTrafego objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(MedicaoTrafego objeto, int cdMedicaoOld, int cdEquipamentoOld, int cdViaOld, int cdFaixaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_medicao_trafego SET cd_medicao=?,"+
												      		   "cd_equipamento=?,"+
												      		   "cd_via=?,"+
												      		   "cd_faixa=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "tp_veiculo=?,"+
												      		   "qt_veiculos=?,"+
												      		   "vl_velocidade_considerada=?,"+
												      		   "vl_velocidade_limite=?,"+
												      		   "vl_velocidade_medida=?,"+
												      		   "vl_velocidade_tolerada=?,"+
												      		   "vl_comprimento_veiculo=? WHERE cd_medicao=? AND cd_equipamento=? AND cd_via=? AND cd_faixa=?");
			pstmt.setInt(1,objeto.getCdMedicao());
			pstmt.setInt(2,objeto.getCdEquipamento());
			pstmt.setInt(3,objeto.getCdVia());
			pstmt.setInt(4,objeto.getCdFaixa());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(7,objeto.getTpVeiculo());
			pstmt.setInt(8,objeto.getQtVeiculos());
			pstmt.setDouble(9,objeto.getVlVelocidadeConsiderada());
			pstmt.setDouble(10,objeto.getVlVelocidadeLimite());
			pstmt.setDouble(11,objeto.getVlVelocidadeMedida());
			pstmt.setDouble(12,objeto.getVlVelocidadeTolerada());
			pstmt.setDouble(13,objeto.getVlComprimentoVeiculo());
			pstmt.setInt(14, cdMedicaoOld!=0 ? cdMedicaoOld : objeto.getCdMedicao());
			pstmt.setInt(15, cdEquipamentoOld!=0 ? cdEquipamentoOld : objeto.getCdEquipamento());
			pstmt.setInt(16, cdViaOld!=0 ? cdViaOld : objeto.getCdVia());
			pstmt.setInt(17, cdFaixaOld!=0 ? cdFaixaOld : objeto.getCdFaixa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa) {
		return delete(cdMedicao, cdEquipamento, cdVia, cdFaixa, null);
	}

	public static int delete(int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_medicao_trafego WHERE cd_medicao=? AND cd_equipamento=? AND cd_via=? AND cd_faixa=?");
			pstmt.setInt(1, cdMedicao);
			pstmt.setInt(2, cdEquipamento);
			pstmt.setInt(3, cdVia);
			pstmt.setInt(4, cdFaixa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MedicaoTrafego get(int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa) {
		return get(cdMedicao, cdEquipamento, cdVia, cdFaixa, null);
	}

	public static MedicaoTrafego get(int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_medicao_trafego WHERE cd_medicao=? AND cd_equipamento=? AND cd_via=? AND cd_faixa=?");
			pstmt.setInt(1, cdMedicao);
			pstmt.setInt(2, cdEquipamento);
			pstmt.setInt(3, cdVia);
			pstmt.setInt(4, cdFaixa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MedicaoTrafego(rs.getInt("cd_medicao"),
						rs.getInt("cd_equipamento"),
						rs.getInt("cd_via"),
						rs.getInt("cd_faixa"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getInt("tp_veiculo"),
						rs.getInt("qt_veiculos"),
						rs.getDouble("vl_velocidade_considerada"),
						rs.getDouble("vl_velocidade_limite"),
						rs.getDouble("vl_velocidade_medida"),
						rs.getDouble("vl_velocidade_tolerada"),
						rs.getDouble("vl_comprimento_veiculo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_medicao_trafego");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MedicaoTrafego> getList() {
		return getList(null);
	}

	public static ArrayList<MedicaoTrafego> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MedicaoTrafego> list = new ArrayList<MedicaoTrafego>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MedicaoTrafego obj = MedicaoTrafegoDAO.get(rsm.getInt("cd_medicao"), rsm.getInt("cd_equipamento"), rsm.getInt("cd_via"), rsm.getInt("cd_faixa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_medicao_trafego", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
