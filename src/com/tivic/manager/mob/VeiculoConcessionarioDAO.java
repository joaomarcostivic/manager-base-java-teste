package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class VeiculoConcessionarioDAO{

	public static int insert(VeiculoConcessionario objeto) {
		return insert(objeto, null);
	}

	public static int insert(VeiculoConcessionario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_veiculo_concessionario (cd_concessao_veiculo,"+
			                                  "cd_concessionario_pessoa,"+
			                                  "dt_vinculacao,"+
			                                  "st_veiculo_concessionario) VALUES (?, ?, ?, ?)");
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConcessaoVeiculo());
			if(objeto.getCdConcessionarioPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessionarioPessoa());
			if(objeto.getDtVinculacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtVinculacao().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStVeiculoConcessionario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VeiculoConcessionario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(VeiculoConcessionario objeto, int cdConcessaoVeiculoOld, int cdConcessionarioPessoaOld) {
		return update(objeto, cdConcessaoVeiculoOld, cdConcessionarioPessoaOld, null);
	}

	public static int update(VeiculoConcessionario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(VeiculoConcessionario objeto, int cdConcessaoVeiculoOld, int cdConcessionarioPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_veiculo_concessionario SET cd_concessao_veiculo=?,"+
												      		   "cd_concessionario_pessoa=?,"+
												      		   "dt_vinculacao=?,"+
												      		   "st_veiculo_concessionario=? WHERE cd_concessao_veiculo=? AND cd_concessionario_pessoa=?");
			pstmt.setInt(1,objeto.getCdConcessaoVeiculo());
			pstmt.setInt(2,objeto.getCdConcessionarioPessoa());
			if(objeto.getDtVinculacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtVinculacao().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStVeiculoConcessionario());
			pstmt.setInt(5, cdConcessaoVeiculoOld!=0 ? cdConcessaoVeiculoOld : objeto.getCdConcessaoVeiculo());
			pstmt.setInt(6, cdConcessionarioPessoaOld!=0 ? cdConcessionarioPessoaOld : objeto.getCdConcessionarioPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConcessaoVeiculo, int cdConcessionarioPessoa) {
		return delete(cdConcessaoVeiculo, cdConcessionarioPessoa, null);
	}

	public static int delete(int cdConcessaoVeiculo, int cdConcessionarioPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_veiculo_concessionario WHERE cd_concessao_veiculo=? AND cd_concessionario_pessoa=?");
			pstmt.setInt(1, cdConcessaoVeiculo);
			pstmt.setInt(2, cdConcessionarioPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VeiculoConcessionario get(int cdConcessaoVeiculo, int cdConcessionarioPessoa) {
		return get(cdConcessaoVeiculo, cdConcessionarioPessoa, null);
	}

	public static VeiculoConcessionario get(int cdConcessaoVeiculo, int cdConcessionarioPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_veiculo_concessionario WHERE cd_concessao_veiculo=? AND cd_concessionario_pessoa=?");
			pstmt.setInt(1, cdConcessaoVeiculo);
			pstmt.setInt(2, cdConcessionarioPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VeiculoConcessionario(rs.getInt("cd_concessao_veiculo"),
						rs.getInt("cd_concessionario_pessoa"),
						(rs.getTimestamp("dt_vinculacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vinculacao").getTime()),
						rs.getInt("st_veiculo_concessionario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_veiculo_concessionario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<VeiculoConcessionario> getList() {
		return getList(null);
	}

	public static ArrayList<VeiculoConcessionario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<VeiculoConcessionario> list = new ArrayList<VeiculoConcessionario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				VeiculoConcessionario obj = VeiculoConcessionarioDAO.get(rsm.getInt("cd_concessao_veiculo"), rsm.getInt("cd_concessionario_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_veiculo_concessionario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}