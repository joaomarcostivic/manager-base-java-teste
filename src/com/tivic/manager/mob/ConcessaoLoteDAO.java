package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ConcessaoLoteDAO{

	public static int insert(ConcessaoLote objeto) {
		return insert(objeto, null);
	}

	public static int insert(ConcessaoLote objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_concessao_lote", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdConcessaoLote(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_concessao_lote (cd_concessao_lote,"+
			                                  "cd_concessao,"+
			                                  "nr_lote,"+
			                                  "tp_veiculo,"+
			                                  "nr_capacidade_tipo_veiculo,"+
			                                  "vl_km_mes,"+
			                                  "vl_unitario_km,"+
			                                  "vl_mensal,"+
			                                  "txt_historico,"+
			                                  "tp_transportados,"+
			                                  "tp_turno,"+
			                                  "cd_linha,"+
			                                  "cd_distrito,"+
			                                  "cd_cidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessao());
			pstmt.setInt(3,objeto.getNrLote());
			pstmt.setInt(4,objeto.getTpVeiculo());
			pstmt.setInt(5,objeto.getNrCapacidadeTipoVeiculo());
			pstmt.setInt(6,objeto.getVlKmMes());
			pstmt.setDouble(7,objeto.getVlUnitarioKm());
			pstmt.setDouble(8,objeto.getVlMensal());
			pstmt.setString(9,objeto.getTxtHistorico());
			pstmt.setInt(10,objeto.getTpTransportados());
			pstmt.setInt(11,objeto.getTpTurno());
			if(objeto.getCdLinha()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdLinha());
			if(objeto.getCdDistrito()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdDistrito());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdCidade());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ConcessaoLote objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ConcessaoLote objeto, int cdConcessaoLoteOld) {
		return update(objeto, cdConcessaoLoteOld, null);
	}

	public static int update(ConcessaoLote objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ConcessaoLote objeto, int cdConcessaoLoteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_concessao_lote SET cd_concessao_lote=?,"+
												      		   "cd_concessao=?,"+
												      		   "nr_lote=?,"+
												      		   "tp_veiculo=?,"+
												      		   "nr_capacidade_tipo_veiculo=?,"+
												      		   "vl_km_mes=?,"+
												      		   "vl_unitario_km=?,"+
												      		   "vl_mensal=?,"+
												      		   "txt_historico=?,"+
												      		   "tp_transportados=?,"+
												      		   "tp_turno=?,"+
												      		   "cd_linha=?,"+
												      		   "cd_distrito=?,"+
												      		   "cd_cidade=? WHERE cd_concessao_lote=?");
			pstmt.setInt(1,objeto.getCdConcessaoLote());
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessao());
			pstmt.setInt(3,objeto.getNrLote());
			pstmt.setInt(4,objeto.getTpVeiculo());
			pstmt.setInt(5,objeto.getNrCapacidadeTipoVeiculo());
			pstmt.setInt(6,objeto.getVlKmMes());
			pstmt.setDouble(7,objeto.getVlUnitarioKm());
			pstmt.setDouble(8,objeto.getVlMensal());
			pstmt.setString(9,objeto.getTxtHistorico());
			pstmt.setInt(10,objeto.getTpTransportados());
			pstmt.setInt(11,objeto.getTpTurno());
			if(objeto.getCdLinha()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdLinha());
			if(objeto.getCdDistrito()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdDistrito());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdCidade());
			pstmt.setInt(15, cdConcessaoLoteOld!=0 ? cdConcessaoLoteOld : objeto.getCdConcessaoLote());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConcessaoLote) {
		return delete(cdConcessaoLote, null);
	}

	public static int delete(int cdConcessaoLote, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_concessao_lote WHERE cd_concessao_lote=?");
			pstmt.setInt(1, cdConcessaoLote);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ConcessaoLote get(int cdConcessaoLote) {
		return get(cdConcessaoLote, null);
	}

	public static ConcessaoLote get(int cdConcessaoLote, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_lote WHERE cd_concessao_lote=?");
			pstmt.setInt(1, cdConcessaoLote);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ConcessaoLote(rs.getInt("cd_concessao_lote"),
						rs.getInt("cd_concessao"),
						rs.getInt("nr_lote"),
						rs.getInt("tp_veiculo"),
						rs.getInt("nr_capacidade_tipo_veiculo"),
						rs.getInt("vl_km_mes"),
						rs.getDouble("vl_unitario_km"),
						rs.getDouble("vl_mensal"),
						rs.getString("txt_historico"),
						rs.getInt("tp_transportados"),
						rs.getInt("tp_turno"),
						rs.getInt("cd_linha"),
						rs.getInt("cd_distrito"),
						rs.getInt("cd_cidade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_lote");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ConcessaoLote> getList() {
		return getList(null);
	}

	public static ArrayList<ConcessaoLote> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ConcessaoLote> list = new ArrayList<ConcessaoLote>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ConcessaoLote obj = ConcessaoLoteDAO.get(rsm.getInt("cd_concessao_lote"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_concessao_lote", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}