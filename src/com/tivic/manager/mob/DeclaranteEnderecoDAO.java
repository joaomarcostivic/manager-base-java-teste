package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class DeclaranteEnderecoDAO{

	public static int insert(DeclaranteEndereco objeto) {
		return insert(objeto, null);
	}

	public static int insert(DeclaranteEndereco objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_declarante_endereco (cd_declarante,"+
			                                  "cd_cidade,"+
			                                  "nr_cep,"+
			                                  "nm_logradouro,"+
			                                  "nr_endereco,"+
			                                  "nm_bairro,"+
			                                  "ds_complemento) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdDeclarante()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDeclarante());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCidade());
			pstmt.setString(3,objeto.getNrCep());
			pstmt.setString(4,objeto.getNmLogradouro());
			pstmt.setString(5,objeto.getNrEndereco());
			pstmt.setString(6,objeto.getNmBairro());
			pstmt.setString(7,objeto.getDsComplemento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DeclaranteEndereco objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(DeclaranteEndereco objeto, int cdDeclaranteOld, int cdCidadeOld) {
		return update(objeto, cdDeclaranteOld, cdCidadeOld, null);
	}

	public static int update(DeclaranteEndereco objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(DeclaranteEndereco objeto, int cdDeclaranteOld, int cdCidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_declarante_endereco SET cd_declarante=?,"+
												      		   "cd_cidade=?,"+
												      		   "nr_cep=?,"+
												      		   "nm_logradouro=?,"+
												      		   "nr_endereco=?,"+
												      		   "nm_bairro=?,"+
												      		   "ds_complemento=? WHERE cd_declarante=? AND cd_cidade=?");
			pstmt.setInt(1,objeto.getCdDeclarante());
			pstmt.setInt(2,objeto.getCdCidade());
			pstmt.setString(3,objeto.getNrCep());
			pstmt.setString(4,objeto.getNmLogradouro());
			pstmt.setString(5,objeto.getNrEndereco());
			pstmt.setString(6,objeto.getNmBairro());
			pstmt.setString(7,objeto.getDsComplemento());
			pstmt.setInt(8, cdDeclaranteOld!=0 ? cdDeclaranteOld : objeto.getCdDeclarante());
			pstmt.setInt(9, cdCidadeOld!=0 ? cdCidadeOld : objeto.getCdCidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDeclarante, int cdCidade) {
		return delete(cdDeclarante, cdCidade, null);
	}

	public static int delete(int cdDeclarante, int cdCidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_declarante_endereco WHERE cd_declarante=? AND cd_cidade=?");
			pstmt.setInt(1, cdDeclarante);
			pstmt.setInt(2, cdCidade);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DeclaranteEndereco get(int cdDeclarante, int cdCidade) {
		return get(cdDeclarante, cdCidade, null);
	}

	public static DeclaranteEndereco get(int cdDeclarante, int cdCidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_declarante_endereco WHERE cd_declarante=? AND cd_cidade=?");
			pstmt.setInt(1, cdDeclarante);
			pstmt.setInt(2, cdCidade);

			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DeclaranteEndereco(rs.getInt("cd_declarante"),
						rs.getInt("cd_cidade"),
						rs.getString("nr_cep"),
						rs.getString("nm_logradouro"),
						rs.getString("nr_endereco"),
						rs.getString("nm_bairro"),
						rs.getString("ds_complemento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_declarante_endereco");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DeclaranteEndereco> getList() {
		return getList(null);
	}

	public static ArrayList<DeclaranteEndereco> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DeclaranteEndereco> list = new ArrayList<DeclaranteEndereco>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DeclaranteEndereco obj = DeclaranteEnderecoDAO.get(rsm.getInt("cd_declarante"), rsm.getInt("cd_cidade"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteEnderecoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_declarante_endereco", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
