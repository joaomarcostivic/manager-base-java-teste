package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class SetorDAO{

	public static int insert(Setor objeto) {
		return insert(objeto, null);
	}

	public static int insert(Setor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_setor", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdSetor(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_setor (cd_setor,"+
			                                  "cd_setor_superior,"+
			                                  "cd_empresa,"+
			                                  "cd_responsavel,"+
			                                  "nm_setor,"+
			                                  "st_setor,"+
			                                  "nm_bairro,"+
			                                  "nm_logradouro,"+
			                                  "nr_cep,"+
			                                  "nr_endereco,"+
			                                  "nm_complemento,"+
			                                  "nr_telefone,"+
			                                  "nm_ponto_referencia,"+
			                                  "lg_estoque,"+
			                                  "nr_ramal,"+
			                                  "id_setor," +
			                                  "sg_setor," +
			                                  "tp_setor," +
			                                  "lg_recepcao," +
			                                  "nr_setor_externo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdSetorSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSetorSuperior());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdResponsavel());
			pstmt.setString(5,objeto.getNmSetor());
			pstmt.setInt(6,objeto.getStSetor());
			pstmt.setString(7,objeto.getNmBairro());
			pstmt.setString(8,objeto.getNmLogradouro());
			pstmt.setString(9,objeto.getNrCep());
			pstmt.setString(10,objeto.getNrEndereco());
			pstmt.setString(11,objeto.getNmComplemento());
			pstmt.setString(12,objeto.getNrTelefone());
			pstmt.setString(13,objeto.getNmPontoReferencia());
			pstmt.setInt(14,objeto.getLgEstoque());
			pstmt.setString(15,objeto.getNrRamal());
			pstmt.setString(16,objeto.getIdSetor());
			pstmt.setString(17, objeto.getSgSetor());
			pstmt.setInt(18, objeto.getTpSetor());
			pstmt.setInt(19, objeto.getLgRecepcao());
			pstmt.setString(20, objeto.getNrSetorExterno());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SetorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SetorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Setor objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Setor objeto, int cdSetorOld) {
		return update(objeto, cdSetorOld, null);
	}

	public static int update(Setor objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Setor objeto, int cdSetorOld, Connection connect){
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_setor SET cd_setor=?,"+
												      		   "cd_setor_superior=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_responsavel=?,"+
												      		   "nm_setor=?,"+
												      		   "st_setor=?,"+
												      		   "nm_bairro=?,"+
												      		   "nm_logradouro=?,"+
												      		   "nr_cep=?,"+
												      		   "nr_endereco=?,"+
												      		   "nm_complemento=?,"+
												      		   "nr_telefone=?,"+
												      		   "nm_ponto_referencia=?,"+
												      		   "lg_estoque=?,"+
												      		   "nr_ramal=?,"+
												      		   "id_setor=?," +
												      		   "sg_setor=?, " +
												      		   "tp_setor=?, " +
												      		   "lg_recepcao=?, "+
												      		   "nr_setor_externo=? WHERE cd_setor=?");
			pstmt.setInt(1,objeto.getCdSetor());
			if(objeto.getCdSetorSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSetorSuperior());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdResponsavel());
			pstmt.setString(5,objeto.getNmSetor());
			pstmt.setInt(6,objeto.getStSetor());
			pstmt.setString(7,objeto.getNmBairro());
			pstmt.setString(8,objeto.getNmLogradouro());
			pstmt.setString(9,objeto.getNrCep());
			pstmt.setString(10,objeto.getNrEndereco());
			pstmt.setString(11,objeto.getNmComplemento());
			pstmt.setString(12,objeto.getNrTelefone());
			pstmt.setString(13,objeto.getNmPontoReferencia());
			pstmt.setInt(14,objeto.getLgEstoque());
			pstmt.setString(15,objeto.getNrRamal());
			pstmt.setString(16,objeto.getIdSetor());
			pstmt.setString(17, objeto.getSgSetor());
			pstmt.setInt(18, objeto.getTpSetor());
			pstmt.setInt(19, objeto.getLgRecepcao());
			pstmt.setString(20, objeto.getNrSetorExterno());
			pstmt.setInt(21, cdSetorOld!=0 ? cdSetorOld : objeto.getCdSetor());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SetorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SetorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdSetor) {
		return delete(cdSetor, null);
	}

	public static int delete(int cdSetor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_setor WHERE cd_setor=?");
			pstmt.setInt(1, cdSetor);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SetorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SetorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Setor get(int cdSetor) {
		return get(cdSetor, null);
	}

	public static Setor get(int cdSetor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_setor WHERE cd_setor=?");
			pstmt.setInt(1, cdSetor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Setor(rs.getInt("cd_setor"),
						rs.getInt("cd_setor_superior"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_responsavel"),
						rs.getString("nm_setor"),
						rs.getInt("st_setor"),
						rs.getString("nm_bairro"),
						rs.getString("nm_logradouro"),
						rs.getString("nr_cep"),
						rs.getString("nr_endereco"),
						rs.getString("nm_complemento"),
						rs.getString("nr_telefone"),
						rs.getString("nm_ponto_referencia"),
						rs.getInt("lg_estoque"),
						rs.getString("nr_ramal"),
						rs.getString("id_setor"),
						rs.getString("sg_setor"),
						rs.getInt("tp_setor"),
						rs.getInt("lg_recepcao"),
						rs.getString("nr_setor_externo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SetorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SetorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_setor ORDER BY nm_setor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SetorDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_setor", "ORDER BY nm_setor", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
