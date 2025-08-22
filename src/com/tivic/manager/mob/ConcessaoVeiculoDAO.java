package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ConcessaoVeiculoDAO{

	public static int insert(ConcessaoVeiculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(ConcessaoVeiculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_concessao_veiculo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdConcessaoVeiculo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_concessao_veiculo (cd_concessao_veiculo,"+
			                                  "cd_concessao,"+
			                                  "cd_veiculo,"+
			                                  "nr_prefixo,"+
			                                  "dt_assinatura,"+
			                                  "dt_inicio_operacao,"+
			                                  "dt_final_operacao,"+
			                                  "tp_frota,"+
			                                  "st_concessao_veiculo,"+
			                                  "lg_manutencao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessao());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVeiculo());
			pstmt.setInt(4,objeto.getNrPrefixo());
			if(objeto.getDtAssinatura()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAssinatura().getTimeInMillis()));
			if(objeto.getDtInicioOperacao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtInicioOperacao().getTimeInMillis()));
			if(objeto.getDtFinalOperacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtFinalOperacao().getTimeInMillis()));
			pstmt.setInt(8,objeto.getTpFrota());
			pstmt.setInt(9,objeto.getStConcessaoVeiculo());
			if(objeto.getLgManutencao()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getLgManutencao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			System.err.println("Erro! ConcessaoVeiculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			System.err.println("Erro! ConcessaoVeiculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ConcessaoVeiculo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ConcessaoVeiculo objeto, int cdConcessaoVeiculoOld) {
		return update(objeto, cdConcessaoVeiculoOld, null);
	}

	public static int update(ConcessaoVeiculo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ConcessaoVeiculo objeto, int cdConcessaoVeiculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_concessao_veiculo SET cd_concessao_veiculo=?,"+
												      		   "cd_concessao=?,"+
												      		   "cd_veiculo=?,"+
												      		   "nr_prefixo=?,"+
												      		   "dt_assinatura=?,"+
												      		   "dt_inicio_operacao=?,"+
												      		   "dt_final_operacao=?,"+
												      		   "tp_frota=?,"+
												      		   "st_concessao_veiculo=?,"+
												      		   "lg_manutencao=? WHERE cd_concessao_veiculo=?");
			pstmt.setInt(1,objeto.getCdConcessaoVeiculo());
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessao());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVeiculo());
			pstmt.setInt(4,objeto.getNrPrefixo());
			if(objeto.getDtAssinatura()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAssinatura().getTimeInMillis()));
			if(objeto.getDtInicioOperacao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtInicioOperacao().getTimeInMillis()));
			if(objeto.getDtFinalOperacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtFinalOperacao().getTimeInMillis()));
			pstmt.setInt(8,objeto.getTpFrota());
			pstmt.setInt(9,objeto.getStConcessaoVeiculo());
			pstmt.setInt(10, objeto.getLgManutencao());
			pstmt.setInt(11, cdConcessaoVeiculoOld!=0 ? cdConcessaoVeiculoOld : objeto.getCdConcessaoVeiculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConcessaoVeiculo) {
		return delete(cdConcessaoVeiculo, null);
	}

	public static int delete(int cdConcessaoVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_concessao_veiculo WHERE cd_concessao_veiculo=?");
			pstmt.setInt(1, cdConcessaoVeiculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ConcessaoVeiculo get(int cdConcessaoVeiculo) {
		return get(cdConcessaoVeiculo, null);
	}

	public static ConcessaoVeiculo get(int cdConcessaoVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_veiculo WHERE cd_concessao_veiculo=?");
			pstmt.setInt(1, cdConcessaoVeiculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ConcessaoVeiculo(rs.getInt("cd_concessao_veiculo"),
						rs.getInt("cd_concessao"),
						rs.getInt("cd_veiculo"),
						rs.getInt("nr_prefixo"),
						(rs.getTimestamp("dt_assinatura")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_assinatura").getTime()),
						(rs.getTimestamp("dt_inicio_operacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_operacao").getTime()),
						(rs.getTimestamp("dt_final_operacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_operacao").getTime()),
						rs.getInt("tp_frota"),
						rs.getInt("st_concessao_veiculo"),
						rs.getInt("lg_manutencao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_veiculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ConcessaoVeiculo> getList() {
		return getList(null);
	}

	public static ArrayList<ConcessaoVeiculo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ConcessaoVeiculo> list = new ArrayList<ConcessaoVeiculo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ConcessaoVeiculo obj = ConcessaoVeiculoDAO.get(rsm.getInt("cd_concessao_veiculo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoDAO.getList: " + e);
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
		return Search.find("SELECT A.*, B.nr_concessao FROM mob_concessao_veiculo A LEFT OUTER JOIN mob_concessao B ON (A.cd_concessao = B.cd_concessao)", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
