package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class PassageiroHorarioDAO{

	public static int insert(PassageiroHorario objeto) {
		return insert(objeto, null);
	}

	public static int insert(PassageiroHorario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[7];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_concessionario_pessoa");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdConcessionarioPessoa()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_horario");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdHorario()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_tabela_horario");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdTabelaHorario()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_linha");
			keys[3].put("IS_KEY_NATIVE", "NO");
			keys[3].put("FIELD_VALUE", new Integer(objeto.getCdLinha()));
			keys[4] = new HashMap<String,Object>();
			keys[4].put("FIELD_NAME", "cd_rota");
			keys[4].put("IS_KEY_NATIVE", "NO");
			keys[4].put("FIELD_VALUE", new Integer(objeto.getCdRota()));
			keys[5] = new HashMap<String,Object>();
			keys[5].put("FIELD_NAME", "cd_trecho");
			keys[5].put("IS_KEY_NATIVE", "NO");
			keys[5].put("FIELD_VALUE", new Integer(objeto.getCdTrecho()));
			keys[6] = new HashMap<String,Object>();
			keys[6].put("FIELD_NAME", "cd_controle");
			keys[6].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mob_passageiro_horario", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdControle(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_passageiro_horario (cd_concessionario_pessoa,"+
			                                  "cd_horario,"+
			                                  "cd_tabela_horario,"+
			                                  "cd_linha,"+
			                                  "cd_rota,"+
			                                  "cd_trecho,"+
			                                  "cd_controle,"+
			                                  "lg_presenca) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
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
			pstmt.setInt(7, code);
			pstmt.setInt(8,objeto.getLgPresenca());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PassageiroHorario objeto) {
		return update(objeto, 0, 0, 0, 0, 0, 0, 0, null);
	}

	public static int update(PassageiroHorario objeto, int cdConcessionarioPessoaOld, int cdHorarioOld, int cdTabelaHorarioOld, int cdLinhaOld, int cdRotaOld, int cdTrechoOld, int cdControleOld) {
		return update(objeto, cdConcessionarioPessoaOld, cdHorarioOld, cdTabelaHorarioOld, cdLinhaOld, cdRotaOld, cdTrechoOld, cdControleOld, null);
	}

	public static int update(PassageiroHorario objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, 0, 0, connect);
	}

	public static int update(PassageiroHorario objeto, int cdConcessionarioPessoaOld, int cdHorarioOld, int cdTabelaHorarioOld, int cdLinhaOld, int cdRotaOld, int cdTrechoOld, int cdControleOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_passageiro_horario SET cd_concessionario_pessoa=?,"+
												      		   "cd_horario=?,"+
												      		   "cd_tabela_horario=?,"+
												      		   "cd_linha=?,"+
												      		   "cd_rota=?,"+
												      		   "cd_trecho=?,"+
												      		   "cd_controle=?,"+
												      		   "lg_presenca=? WHERE cd_concessionario_pessoa=? AND cd_horario=? AND cd_tabela_horario=? AND cd_linha=? AND cd_rota=? AND cd_trecho=? AND cd_controle=?");
			pstmt.setInt(1,objeto.getCdConcessionarioPessoa());
			pstmt.setInt(2,objeto.getCdHorario());
			pstmt.setInt(3,objeto.getCdTabelaHorario());
			pstmt.setInt(4,objeto.getCdLinha());
			pstmt.setInt(5,objeto.getCdRota());
			pstmt.setInt(6,objeto.getCdTrecho());
			pstmt.setInt(7,objeto.getCdControle());
			pstmt.setInt(8,objeto.getLgPresenca());
			pstmt.setInt(9, cdConcessionarioPessoaOld!=0 ? cdConcessionarioPessoaOld : objeto.getCdConcessionarioPessoa());
			pstmt.setInt(10, cdHorarioOld!=0 ? cdHorarioOld : objeto.getCdHorario());
			pstmt.setInt(11, cdTabelaHorarioOld!=0 ? cdTabelaHorarioOld : objeto.getCdTabelaHorario());
			pstmt.setInt(12, cdLinhaOld!=0 ? cdLinhaOld : objeto.getCdLinha());
			pstmt.setInt(13, cdRotaOld!=0 ? cdRotaOld : objeto.getCdRota());
			pstmt.setInt(14, cdTrechoOld!=0 ? cdTrechoOld : objeto.getCdTrecho());
			pstmt.setInt(15, cdControleOld!=0 ? cdControleOld : objeto.getCdControle());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, int cdControle) {
		return delete(cdConcessionarioPessoa, cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, cdControle, null);
	}

	public static int delete(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, int cdControle, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_passageiro_horario WHERE cd_concessionario_pessoa=? AND cd_horario=? AND cd_tabela_horario=? AND cd_linha=? AND cd_rota=? AND cd_trecho=? AND cd_controle=?");
			pstmt.setInt(1, cdConcessionarioPessoa);
			pstmt.setInt(2, cdHorario);
			pstmt.setInt(3, cdTabelaHorario);
			pstmt.setInt(4, cdLinha);
			pstmt.setInt(5, cdRota);
			pstmt.setInt(6, cdTrecho);
			pstmt.setInt(7, cdControle);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PassageiroHorario get(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, int cdControle) {
		return get(cdConcessionarioPessoa, cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, cdControle, null);
	}

	public static PassageiroHorario get(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, int cdControle, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_passageiro_horario WHERE cd_concessionario_pessoa=? AND cd_horario=? AND cd_tabela_horario=? AND cd_linha=? AND cd_rota=? AND cd_trecho=? AND cd_controle=?");
			pstmt.setInt(1, cdConcessionarioPessoa);
			pstmt.setInt(2, cdHorario);
			pstmt.setInt(3, cdTabelaHorario);
			pstmt.setInt(4, cdLinha);
			pstmt.setInt(5, cdRota);
			pstmt.setInt(6, cdTrecho);
			pstmt.setInt(7, cdControle);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PassageiroHorario(rs.getInt("cd_concessionario_pessoa"),
						rs.getInt("cd_horario"),
						rs.getInt("cd_tabela_horario"),
						rs.getInt("cd_linha"),
						rs.getInt("cd_rota"),
						rs.getInt("cd_trecho"),
						rs.getInt("cd_controle"),
						rs.getInt("lg_presenca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_passageiro_horario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PassageiroHorario> getList() {
		return getList(null);
	}

	public static ArrayList<PassageiroHorario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PassageiroHorario> list = new ArrayList<PassageiroHorario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PassageiroHorario obj = PassageiroHorarioDAO.get(rsm.getInt("cd_concessionario_pessoa"), rsm.getInt("cd_horario"), rsm.getInt("cd_tabela_horario"), rsm.getInt("cd_linha"), rsm.getInt("cd_rota"), rsm.getInt("cd_trecho"), rsm.getInt("cd_controle"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_passageiro_horario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}