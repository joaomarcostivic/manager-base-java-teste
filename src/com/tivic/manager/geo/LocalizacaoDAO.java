package com.tivic.manager.geo;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class LocalizacaoDAO{

	public static int insert(Localizacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Localizacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_localizacao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_referencia");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdReferencia()));
			int code = Conexao.getSequenceCode("geo_localizacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLocalizacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO geo_localizacao (cd_localizacao,"+
			                                  "cd_referencia,"+
			                                  "cd_pais,"+
			                                  "cd_estado,"+
			                                  "cd_regiao,"+
			                                  "cd_cidade,"+
			                                  "cd_distrito,"+
			                                  "cd_bairro,"+
			                                  "cd_logradouro,"+
			                                  "cd_endereco,"+
			                                  "cd_pessoa,"+
			                                  "tp_localizacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdReferencia());
			if(objeto.getCdPais()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPais());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEstado());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdRegiao());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCidade());
			if(objeto.getCdDistrito()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdDistrito());
			if(objeto.getCdBairro()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdBairro());
			if(objeto.getCdLogradouro()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdLogradouro());
			if(objeto.getCdEndereco()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdEndereco());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdPessoa());
			pstmt.setInt(12,objeto.getTpLocalizacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Localizacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Localizacao objeto, int cdLocalizacaoOld, int cdReferenciaOld) {
		return update(objeto, cdLocalizacaoOld, cdReferenciaOld, null);
	}

	public static int update(Localizacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Localizacao objeto, int cdLocalizacaoOld, int cdReferenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE geo_localizacao SET cd_localizacao=?,"+
												      		   "cd_referencia=?,"+
												      		   "cd_pais=?,"+
												      		   "cd_estado=?,"+
												      		   "cd_regiao=?,"+
												      		   "cd_cidade=?,"+
												      		   "cd_distrito=?,"+
												      		   "cd_bairro=?,"+
												      		   "cd_logradouro=?,"+
												      		   "cd_endereco=?,"+
												      		   "cd_pessoa=?,"+
												      		   "tp_localizacao=? WHERE cd_localizacao=? AND cd_referencia=?");
			pstmt.setInt(1,objeto.getCdLocalizacao());
			pstmt.setInt(2,objeto.getCdReferencia());
			if(objeto.getCdPais()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPais());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEstado());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdRegiao());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCidade());
			if(objeto.getCdDistrito()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdDistrito());
			if(objeto.getCdBairro()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdBairro());
			if(objeto.getCdLogradouro()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdLogradouro());
			if(objeto.getCdEndereco()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdEndereco());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdPessoa());
			pstmt.setInt(12,objeto.getTpLocalizacao());
			pstmt.setInt(13, cdLocalizacaoOld!=0 ? cdLocalizacaoOld : objeto.getCdLocalizacao());
			pstmt.setInt(14, cdReferenciaOld!=0 ? cdReferenciaOld : objeto.getCdReferencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLocalizacao, int cdReferencia) {
		return delete(cdLocalizacao, cdReferencia, null);
	}

	public static int delete(int cdLocalizacao, int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM geo_localizacao WHERE cd_localizacao=? AND cd_referencia=?");
			pstmt.setInt(1, cdLocalizacao);
			pstmt.setInt(2, cdReferencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Localizacao get(int cdLocalizacao, int cdReferencia) {
		return get(cdLocalizacao, cdReferencia, null);
	}

	public static Localizacao get(int cdLocalizacao, int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM geo_localizacao WHERE cd_localizacao=? AND cd_referencia=?");
			pstmt.setInt(1, cdLocalizacao);
			pstmt.setInt(2, cdReferencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Localizacao(rs.getInt("cd_localizacao"),
						rs.getInt("cd_referencia"),
						rs.getInt("cd_pais"),
						rs.getInt("cd_estado"),
						rs.getInt("cd_regiao"),
						rs.getInt("cd_cidade"),
						rs.getInt("cd_distrito"),
						rs.getInt("cd_bairro"),
						rs.getInt("cd_logradouro"),
						rs.getInt("cd_endereco"),
						rs.getInt("cd_pessoa"),
						rs.getInt("tp_localizacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM geo_localizacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Localizacao> getList() {
		return getList(null);
	}

	public static ArrayList<Localizacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Localizacao> list = new ArrayList<Localizacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Localizacao obj = LocalizacaoDAO.get(rsm.getInt("cd_localizacao"), rsm.getInt("cd_referencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM geo_localizacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
