package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PessoaEnderecoDAO{

	public static int insert(PessoaEndereco objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaEndereco objeto, Connection connect) {
		return insert(objeto, connect, false);
	}
	
	@SuppressWarnings("unchecked")
	public static int insert(PessoaEndereco objeto, Connection connect, boolean sync){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_endereco");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_pessoa");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdPessoa()));
			int code = sync ? Conexao.getSequenceCodeSync("grl_pessoa_endereco", keys, connect) : Conexao.getSequenceCode("grl_pessoa_endereco", keys, connect);
			
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEndereco(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_endereco (cd_endereco,"+
			                                  "cd_pessoa,"+
			                                  "ds_endereco,"+
			                                  "cd_tipo_logradouro,"+
			                                  "cd_tipo_endereco,"+
			                                  "cd_logradouro,"+
			                                  "cd_bairro,"+
			                                  "cd_cidade,"+
			                                  "nm_logradouro,"+
			                                  "nm_bairro,"+
			                                  "nr_cep,"+
			                                  "nr_endereco,"+
			                                  "nm_complemento,"+
			                                  "nr_telefone,"+
			                                  "nm_ponto_referencia,"+
			                                  "lg_cobranca,"+
			                                  "lg_principal,"+
			                                  "tp_zona,"+
			                                  "tp_localizacao_diferenciada) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getDsEndereco());
			if(objeto.getCdTipoLogradouro()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoLogradouro());
			if(objeto.getCdTipoEndereco()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoEndereco());
			if(objeto.getCdLogradouro()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdLogradouro());
			if(objeto.getCdBairro()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdBairro());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdCidade());
			pstmt.setString(9,objeto.getNmLogradouro());
			pstmt.setString(10,objeto.getNmBairro());
			pstmt.setString(11,objeto.getNrCep());
			pstmt.setString(12,objeto.getNrEndereco());
			pstmt.setString(13,objeto.getNmComplemento());
			pstmt.setString(14,objeto.getNrTelefone());
			pstmt.setString(15,objeto.getNmPontoReferencia());
			pstmt.setInt(16,objeto.getLgCobranca());
			pstmt.setInt(17,objeto.getLgPrincipal());
			pstmt.setInt(18,objeto.getTpZona());
			pstmt.setInt(19,objeto.getTpLocalizacaoDiferenciada());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaEndereco objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PessoaEndereco objeto, int cdEnderecoOld, int cdPessoaOld) {
		return update(objeto, cdEnderecoOld, cdPessoaOld, null);
	}

	public static int update(PessoaEndereco objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PessoaEndereco objeto, int cdEnderecoOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_endereco SET cd_endereco=?,"+
												      		   "cd_pessoa=?,"+
												      		   "ds_endereco=?,"+
												      		   "cd_tipo_logradouro=?,"+
												      		   "cd_tipo_endereco=?,"+
												      		   "cd_logradouro=?,"+
												      		   "cd_bairro=?,"+
												      		   "cd_cidade=?,"+
												      		   "nm_logradouro=?,"+
												      		   "nm_bairro=?,"+
												      		   "nr_cep=?,"+
												      		   "nr_endereco=?,"+
												      		   "nm_complemento=?,"+
												      		   "nr_telefone=?,"+
												      		   "nm_ponto_referencia=?,"+
												      		   "lg_cobranca=?,"+
												      		   "lg_principal=?,"+
												      		   "tp_zona=?,"+
												      		   "tp_localizacao_diferenciada=? WHERE cd_endereco=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdEndereco());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getDsEndereco());
			if(objeto.getCdTipoLogradouro()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoLogradouro());
			if(objeto.getCdTipoEndereco()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoEndereco());
			if(objeto.getCdLogradouro()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdLogradouro());
			if(objeto.getCdBairro()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdBairro());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdCidade());
			pstmt.setString(9,objeto.getNmLogradouro());
			pstmt.setString(10,objeto.getNmBairro());
			pstmt.setString(11,objeto.getNrCep());
			pstmt.setString(12,objeto.getNrEndereco());
			pstmt.setString(13,objeto.getNmComplemento());
			pstmt.setString(14,objeto.getNrTelefone());
			pstmt.setString(15,objeto.getNmPontoReferencia());
			pstmt.setInt(16,objeto.getLgCobranca());
			pstmt.setInt(17,objeto.getLgPrincipal());
			pstmt.setInt(18,objeto.getTpZona());
			pstmt.setInt(19,objeto.getTpLocalizacaoDiferenciada());
			pstmt.setInt(20, cdEnderecoOld!=0 ? cdEnderecoOld : objeto.getCdEndereco());
			pstmt.setInt(21, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			return pstmt.executeUpdate();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEndereco, int cdPessoa) {
		return delete(cdEndereco, cdPessoa, null);
	}

	public static int delete(int cdEndereco, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_endereco WHERE cd_endereco=? AND cd_pessoa=?");
			pstmt.setInt(1, cdEndereco);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaEndereco get(int cdEndereco, int cdPessoa) {
		return get(cdEndereco, cdPessoa, null);
	}

	public static PessoaEndereco get(int cdEndereco, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_endereco WHERE cd_endereco=? AND cd_pessoa=?");
			pstmt.setInt(1, cdEndereco);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaEndereco(rs.getInt("cd_endereco"),
						rs.getInt("cd_pessoa"),
						rs.getString("ds_endereco"),
						rs.getInt("cd_tipo_logradouro"),
						rs.getInt("cd_tipo_endereco"),
						rs.getInt("cd_logradouro"),
						rs.getInt("cd_bairro"),
						rs.getInt("cd_cidade"),
						rs.getString("nm_logradouro"),
						rs.getString("nm_bairro"),
						rs.getString("nr_cep"),
						rs.getString("nr_endereco"),
						rs.getString("nm_complemento"),
						rs.getString("nr_telefone"),
						rs.getString("nm_ponto_referencia"),
						rs.getInt("lg_cobranca"),
						rs.getInt("lg_principal"),
						rs.getInt("tp_zona"),
						rs.getInt("tp_localizacao_diferenciada"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static PessoaEndereco get(int cdPessoa) {
		return get(cdPessoa, null);
	}

	public static PessoaEndereco get(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_endereco WHERE cd_pessoa=?");
			pstmt.setInt(1, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaEndereco(rs.getInt("cd_endereco"),
						rs.getInt("cd_pessoa"),
						rs.getString("ds_endereco"),
						rs.getInt("cd_tipo_logradouro"),
						rs.getInt("cd_tipo_endereco"),
						rs.getInt("cd_logradouro"),
						rs.getInt("cd_bairro"),
						rs.getInt("cd_cidade"),
						rs.getString("nm_logradouro"),
						rs.getString("nm_bairro"),
						rs.getString("nr_cep"),
						rs.getString("nr_endereco"),
						rs.getString("nm_complemento"),
						rs.getString("nr_telefone"),
						rs.getString("nm_ponto_referencia"),
						rs.getInt("lg_cobranca"),
						rs.getInt("lg_principal"),
						rs.getInt("tp_zona"),
						rs.getInt("tp_localizacao_diferenciada"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_endereco");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEnderecoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_endereco", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
