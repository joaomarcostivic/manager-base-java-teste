package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class DeclaranteDAO{

	public static int insert(Declarante objeto) {
		return insert(objeto, null);
	}

	public static int insert(Declarante objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_declarante", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDeclarante(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_declarante (cd_declarante,"+
			                                  "nm_declarante,"+
			                                  "nr_cpf,"+
			                                  "dt_nascimento,"+
			                                  "tp_sexo,"+
			                                  "nr_telefone,"+
			                                  "nr_celular,"+
			                                  "nm_email,"+
			                                  "nr_cnh,"+
			                                  "sg_uf_cnh,"+
			                                  "nm_profissao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmDeclarante());
			pstmt.setString(3,objeto.getNrCpf());
			if(objeto.getDtNascimento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtNascimento().getTimeInMillis()));
			pstmt.setInt(5,objeto.getTpSexo());
			pstmt.setString(6,objeto.getNrTelefone());
			pstmt.setString(7,objeto.getNrCelular());
			pstmt.setString(8,objeto.getNmEmail());
			pstmt.setString(9,objeto.getNrCnh());
			pstmt.setString(10,objeto.getSgUfCnh());
			pstmt.setString(11,objeto.getNmProfissao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Declarante objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Declarante objeto, int cdDeclaranteOld) {
		return update(objeto, cdDeclaranteOld, null);
	}

	public static int update(Declarante objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Declarante objeto, int cdDeclaranteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_declarante SET cd_declarante=?,"+
												      		   "nm_declarante=?,"+
												      		   "nr_cpf=?,"+
												      		   "dt_nascimento=?,"+
												      		   "tp_sexo=?,"+
												      		   "nr_telefone=?,"+
												      		   "nr_celular=?,"+
												      		   "nm_email=?,"+
												      		   "nr_cnh=?,"+
												      		   "sg_uf_cnh=?,"+
												      		   "nm_profissao=? WHERE cd_declarante=?");
			pstmt.setInt(1,objeto.getCdDeclarante());
			pstmt.setString(2,objeto.getNmDeclarante());
			pstmt.setString(3,objeto.getNrCpf());
			if(objeto.getDtNascimento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtNascimento().getTimeInMillis()));
			pstmt.setInt(5,objeto.getTpSexo());
			pstmt.setString(6,objeto.getNrTelefone());
			pstmt.setString(7,objeto.getNrCelular());
			pstmt.setString(8,objeto.getNmEmail());
			pstmt.setString(9,objeto.getNrCnh());
			pstmt.setString(10,objeto.getSgUfCnh());
			pstmt.setString(11,objeto.getNmProfissao());
			pstmt.setInt(12, cdDeclaranteOld!=0 ? cdDeclaranteOld : objeto.getCdDeclarante());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDeclarante) {
		return delete(cdDeclarante, null);
	}

	public static int delete(int cdDeclarante, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_declarante WHERE cd_declarante=?");
			pstmt.setInt(1, cdDeclarante);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Declarante get(int cdDeclarante) {
		return get(cdDeclarante, null);
	}

	public static Declarante get(int cdDeclarante, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_declarante WHERE cd_declarante=?");
			pstmt.setInt(1, cdDeclarante);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Declarante(rs.getInt("cd_declarante"),
						rs.getString("nm_declarante"),
						rs.getString("nr_cpf"),
						(rs.getTimestamp("dt_nascimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_nascimento").getTime()),
						rs.getInt("tp_sexo"),
						rs.getString("nr_telefone"),
						rs.getString("nr_celular"),
						rs.getString("nm_email"),
						rs.getString("nr_cnh"),
						rs.getString("sg_uf_cnh"),
						rs.getString("nm_profissao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_declarante");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Declarante> getList() {
		return getList(null);
	}

	public static ArrayList<Declarante> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Declarante> list = new ArrayList<Declarante>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Declarante obj = DeclaranteDAO.get(rsm.getInt("cd_declarante"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_declarante", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}