package com.tivic.manager.mob.orgaoexterno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tivic.sol.connection.Conexao;

public class OrgaoExternoDAO {

	public static int insert(OrgaoExterno objeto) {
		return insert(objeto, null);
	}

	public static int insert(OrgaoExterno objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_orgao_externo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOrgaoExterno(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_orgao_externo (cd_orgao_externo,"+
			                                  "nm_orgao_externo,"+
			                                  "sg_orgao_externo,"+
			                                  "cd_tipo_logradouro,"+
			                                  "nm_logradouro,"+
			                                  "cd_cidade,"+
			                                  "nm_bairro,"+
			                                  "nr_cep,"+
			                                  "nr_endereco) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmOrgaoExterno());
			pstmt.setString(3,objeto.getSgOrgaoExterno());
			pstmt.setInt(4,objeto.getCdTipoLogradouro());
			pstmt.setString(5,objeto.getNmLogradouro());
			pstmt.setInt(6,objeto.getCdCidade());
			pstmt.setString(7,objeto.getNmBairro());
			pstmt.setString(8,objeto.getNrCep());
			pstmt.setString(9,objeto.getNrEndereco());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoExternoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoExternoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int update(OrgaoExterno objeto) {
		return update(objeto, 0, null);
	}

	public static int update(OrgaoExterno objeto, int cdOrgaoExternoOld) {
		return update(objeto, cdOrgaoExternoOld, null);
	}

	public static int update(OrgaoExterno objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(OrgaoExterno objeto, int cdOrgaoExternoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_orgao_externo SET cd_orgao_externo=?,"+
												      		   "nm_orgao_externo=?,"+
												      		   "sg_orgao_externo=?,"+
												      		   "cd_tipo_logradouro=?,"+
												      		   "nm_logradouro=?,"+
												      		   "cd_cidade=?,"+
												      		   "nm_bairro=?,"+
												      		   "nr_cep=?,"+
												      		   "nr_endereco=?"+
												      		   "WHERE cd_orgao_externo=?");
			pstmt.setInt(1,objeto.getCdOrgaoExterno());
			pstmt.setString(2,objeto.getNmOrgaoExterno());
			pstmt.setString(3,objeto.getSgOrgaoExterno());
			pstmt.setInt(4,objeto.getCdTipoLogradouro());
			pstmt.setString(5,objeto.getNmLogradouro());
			pstmt.setInt(6,objeto.getCdCidade());
			pstmt.setString(7,objeto.getNmBairro());
			pstmt.setString(8,objeto.getNrCep());
			pstmt.setString(9,objeto.getNrEndereco());
			pstmt.setInt(10, cdOrgaoExternoOld!=0 ? cdOrgaoExternoOld : objeto.getCdOrgaoExterno());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoExternoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoExternoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static OrgaoExterno get(int cdOrgaoExterno) {
		return get(cdOrgaoExterno, null);
	}

	public static OrgaoExterno get(int cdOrgaoExterno, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_orgao_externo WHERE cd_orgao_externo=?");
			pstmt.setInt(1, cdOrgaoExterno);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrgaoExterno(rs.getInt("cd_orgao_externo"),
						rs.getString("nm_orgao_externo"),
						rs.getString("sg_orgao_externo"),
						rs.getInt("cd_tipo_logradouro"),
						rs.getString("nm_logradouro"),
						rs.getInt("cd_cidade"),
						rs.getString("nm_bairro"),
						rs.getString("nr_cep"),
						rs.getString("nr_endereco"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrgaoExternoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoExternoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
