package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ViagemDAO{

	public static int insert(Viagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(Viagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_viagem", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdViagem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_viagem (cd_viagem,"+
			                                  "dt_viagem,"+
			                                  "hr_partida,"+
			                                  "hr_chegada,"+
			                                  "tp_viagem,"+
			                                  "st_viagem,"+
			                                  "cd_instituicao,"+
			                                  "cd_setor,"+
			                                  "txt_descricao,"+
			                                  "cd_concessao_veiculo,"+
			                                  "cd_motorista,"+
			                                  "lg_manutencao,"+
			                                  "cd_viagem_anterior,"+
			                                  "nr_capacidade,"+
			                                  "cd_viagem_agendamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtViagem()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtViagem().getTimeInMillis()));
			if(objeto.getHrPartida()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getHrPartida().getTimeInMillis()));
			if(objeto.getHrChegada()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrChegada().getTimeInMillis()));
			pstmt.setInt(5,objeto.getTpViagem());
			pstmt.setInt(6,objeto.getStViagem());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdInstituicao());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdSetor());
			pstmt.setString(9,objeto.getTxtDescricao());
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdConcessaoVeiculo());
			if(objeto.getCdMotorista()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdMotorista());
			pstmt.setInt(12,objeto.getLgManutencao());
			if(objeto.getCdViagemAnterior()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdViagemAnterior());
			pstmt.setInt(14,objeto.getNrCapacidade());
			if(objeto.getCdViagemAgendamento()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdViagemAgendamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Viagem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Viagem objeto, int cdViagemOld) {
		return update(objeto, cdViagemOld, null);
	}

	public static int update(Viagem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Viagem objeto, int cdViagemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_viagem SET cd_viagem=?,"+
												      		   "dt_viagem=?,"+
												      		   "hr_partida=?,"+
												      		   "hr_chegada=?,"+
												      		   "tp_viagem=?,"+
												      		   "st_viagem=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_setor=?,"+
												      		   "txt_descricao=?,"+
												      		   "cd_concessao_veiculo=?,"+
												      		   "cd_motorista=?,"+
												      		   "lg_manutencao=?,"+
												      		   "cd_viagem_anterior=?,"+
												      		   "nr_capacidade=?,"+
												      		   "cd_viagem_agendamento=? WHERE cd_viagem=?");
			pstmt.setInt(1,objeto.getCdViagem());
			if(objeto.getDtViagem()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtViagem().getTimeInMillis()));
			if(objeto.getHrPartida()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getHrPartida().getTimeInMillis()));
			if(objeto.getHrChegada()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrChegada().getTimeInMillis()));
			pstmt.setInt(5,objeto.getTpViagem());
			pstmt.setInt(6,objeto.getStViagem());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdInstituicao());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdSetor());
			pstmt.setString(9,objeto.getTxtDescricao());
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdConcessaoVeiculo());
			if(objeto.getCdMotorista()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdMotorista());
			pstmt.setInt(12,objeto.getLgManutencao());
			if(objeto.getCdViagemAnterior()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdViagemAnterior());
			pstmt.setInt(14,objeto.getNrCapacidade());
			if(objeto.getCdViagemAgendamento()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdViagemAgendamento());
			pstmt.setInt(16, cdViagemOld!=0 ? cdViagemOld : objeto.getCdViagem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdViagem) {
		return delete(cdViagem, null);
	}

	public static int delete(int cdViagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_viagem WHERE cd_viagem=?");
			pstmt.setInt(1, cdViagem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Viagem get(int cdViagem) {
		return get(cdViagem, null);
	}

	public static Viagem get(int cdViagem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_viagem WHERE cd_viagem=?");
			pstmt.setInt(1, cdViagem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Viagem(rs.getInt("cd_viagem"),
						(rs.getTimestamp("dt_viagem")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_viagem").getTime()),
						(rs.getTimestamp("hr_partida")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_partida").getTime()),
						(rs.getTimestamp("hr_chegada")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_chegada").getTime()),
						rs.getInt("tp_viagem"),
						rs.getInt("st_viagem"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_setor"),
						rs.getString("txt_descricao"),
						rs.getInt("cd_concessao_veiculo"),
						rs.getInt("cd_motorista"),
						rs.getInt("lg_manutencao"),
						rs.getInt("cd_viagem_anterior"),
						rs.getInt("nr_capacidade"),
						rs.getInt("cd_viagem_agendamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_viagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Viagem> getList() {
		return getList(null);
	}

	public static ArrayList<Viagem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Viagem> list = new ArrayList<Viagem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Viagem obj = ViagemDAO.get(rsm.getInt("cd_viagem"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_viagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}