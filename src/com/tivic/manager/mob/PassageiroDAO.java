package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class PassageiroDAO{

	public static int insert(Passageiro objeto) {
		return insert(objeto, null);
	}

	public static int insert(Passageiro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_passageiro (cd_concessionario_pessoa,"+
			                                  "cd_horario,"+
			                                  "cd_tabela_horario,"+
			                                  "cd_linha,"+
			                                  "cd_rota,"+
			                                  "cd_trecho,"+
			                                  "dt_vinculacao,"+
			                                  "st_passageiro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdConcessionarioPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConcessionarioPessoa());
			if(objeto.getCdHorario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdHorario());
			if(objeto.getCdTabelaHorario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTabelaHorario());
			if(objeto.getCdLinha()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdLinha());
			if(objeto.getCdRota()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdRota());
			if(objeto.getCdTrecho()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTrecho());
			if(objeto.getDtVinculacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtVinculacao().getTimeInMillis()));
			pstmt.setInt(8,objeto.getStPassageiro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PassageiroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Passageiro objeto) {
		return update(objeto, 0, 0, 0, 0, 0, 0, null);
	}

	public static int update(Passageiro objeto, int cdConcessionarioPessoaOld, int cdHorarioOld, int cdTabelaHorarioOld, int cdLinhaOld, int cdRotaOld, int cdTrechoOld) {
		return update(objeto, cdConcessionarioPessoaOld, cdHorarioOld, cdTabelaHorarioOld, cdLinhaOld, cdRotaOld, cdTrechoOld, null);
	}

	public static int update(Passageiro objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, 0, connect);
	}

	public static int update(Passageiro objeto, int cdConcessionarioPessoaOld, int cdHorarioOld, int cdTabelaHorarioOld, int cdLinhaOld, int cdRotaOld, int cdTrechoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_passageiro SET cd_concessionario_pessoa=?,"+
												      		   "cd_horario=?,"+
												      		   "cd_tabela_horario=?,"+
												      		   "cd_linha=?,"+
												      		   "cd_rota=?,"+
												      		   "cd_trecho=?,"+
												      		   "dt_vinculacao=?,"+
												      		   "st_passageiro=? WHERE cd_concessionario_pessoa=? AND cd_horario=? AND cd_tabela_horario=? AND cd_linha=? AND cd_rota=? AND cd_trecho=?");
			pstmt.setInt(1,objeto.getCdConcessionarioPessoa());
			pstmt.setInt(2,objeto.getCdHorario());
			pstmt.setInt(3,objeto.getCdTabelaHorario());
			pstmt.setInt(4,objeto.getCdLinha());
			pstmt.setInt(5,objeto.getCdRota());
			pstmt.setInt(6,objeto.getCdTrecho());
			if(objeto.getDtVinculacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtVinculacao().getTimeInMillis()));
			pstmt.setInt(8,objeto.getStPassageiro());
			pstmt.setInt(9, cdConcessionarioPessoaOld!=0 ? cdConcessionarioPessoaOld : objeto.getCdConcessionarioPessoa());
			pstmt.setInt(10, cdHorarioOld!=0 ? cdHorarioOld : objeto.getCdHorario());
			pstmt.setInt(11, cdTabelaHorarioOld!=0 ? cdTabelaHorarioOld : objeto.getCdTabelaHorario());
			pstmt.setInt(12, cdLinhaOld!=0 ? cdLinhaOld : objeto.getCdLinha());
			pstmt.setInt(13, cdRotaOld!=0 ? cdRotaOld : objeto.getCdRota());
			pstmt.setInt(14, cdTrechoOld!=0 ? cdTrechoOld : objeto.getCdTrecho());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PassageiroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho) {
		return delete(cdConcessionarioPessoa, cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, null);
	}

	public static int delete(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_passageiro WHERE cd_concessionario_pessoa=? AND cd_horario=? AND cd_tabela_horario=? AND cd_linha=? AND cd_rota=? AND cd_trecho=?");
			pstmt.setInt(1, cdConcessionarioPessoa);
			pstmt.setInt(2, cdHorario);
			pstmt.setInt(3, cdTabelaHorario);
			pstmt.setInt(4, cdLinha);
			pstmt.setInt(5, cdRota);
			pstmt.setInt(6, cdTrecho);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PassageiroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Passageiro get(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho) {
		return get(cdConcessionarioPessoa, cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, null);
	}

	public static Passageiro get(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_passageiro WHERE cd_concessionario_pessoa=? AND cd_horario=? AND cd_tabela_horario=? AND cd_linha=? AND cd_rota=? AND cd_trecho=?");
			pstmt.setInt(1, cdConcessionarioPessoa);
			pstmt.setInt(2, cdHorario);
			pstmt.setInt(3, cdTabelaHorario);
			pstmt.setInt(4, cdLinha);
			pstmt.setInt(5, cdRota);
			pstmt.setInt(6, cdTrecho);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Passageiro(rs.getInt("cd_concessionario_pessoa"),
						rs.getInt("cd_horario"),
						rs.getInt("cd_tabela_horario"),
						rs.getInt("cd_linha"),
						rs.getInt("cd_rota"),
						rs.getInt("cd_trecho"),
						(rs.getTimestamp("dt_vinculacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vinculacao").getTime()),
						rs.getInt("st_passageiro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PassageiroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_passageiro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PassageiroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Passageiro> getList() {
		return getList(null);
	}

	public static ArrayList<Passageiro> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Passageiro> list = new ArrayList<Passageiro>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Passageiro obj = PassageiroDAO.get(rsm.getInt("cd_concessionario_pessoa"), rsm.getInt("cd_horario"), rsm.getInt("cd_tabela_horario"), rsm.getInt("cd_linha"), rsm.getInt("cd_rota"), rsm.getInt("cd_trecho"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_passageiro", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}