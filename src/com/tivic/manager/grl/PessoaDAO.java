package com.tivic.manager.grl;

import java.sql.*;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PessoaDAO{

	public static int insert(Pessoa objeto) {
		return insert(objeto, null);
	}

	public static int insert(Pessoa objeto, Connection connect) {
		return insert(objeto, null, false);
	}
	
	public static int insert(Pessoa objeto, Connection connect, boolean sync){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			if(objeto.getCdPessoa()<=0)	{
				int code = sync ? Conexao.getSequenceCodeSync("grl_pessoa", connect) : Conexao.getSequenceCode("grl_pessoa", connect); 
				if (code <= 0)
					return -1;
				objeto.setCdPessoa(code);
			}
			try	{
				// Corrigindo números de telefone
				if(objeto.getNrTelefone1()!=null)
					objeto.setNrTelefone1(objeto.getNrTelefone1().replaceAll(" ", "").trim());
					
				if(objeto.getNrTelefone2()!=null)
					objeto.setNrTelefone2(objeto.getNrTelefone2().replaceAll(" ", "").trim());
					
				if(objeto.getNrCelular()!=null)
					objeto.setNrCelular(objeto.getNrCelular().replaceAll(" ", "").trim());
					
				if(objeto.getNrCelular2()!=null)
					objeto.setNrCelular2(objeto.getNrCelular2().replaceAll(" ", "").trim());
					
				if(objeto.getNrFax()!=null);
					objeto.setNrFax(objeto.getNrFax().replaceAll(" ", "").trim());
			} catch(Exception e){}		
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa (cd_pessoa,"+
			                                  "cd_pessoa_superior,"+
			                                  "cd_pais,"+
			                                  "nm_pessoa,"+
			                                  "nr_telefone1,"+
			                                  "nr_telefone2,"+
			                                  "nr_celular,"+
			                                  "nr_fax,"+
			                                  "nm_email,"+
			                                  "dt_cadastro,"+
			                                  "gn_pessoa,"+
			                                  "img_foto,"+
			                                  "st_cadastro,"+
			                                  "nm_url,"+
			                                  "nm_apelido,"+
			                                  "txt_observacao,"+
			                                  "lg_notificacao,"+
			                                  "id_pessoa,"+
			                                  "cd_classificacao,"+
			                                  "cd_forma_divulgacao,"+
			                                  "dt_chegada_pais,"+
			                                  "cd_usuario_cadastro,"+
			                                  "nr_oab,"+
			                                  "nm_parceiro,"+
			                                  "nr_celular2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdPessoa());
			if(objeto.getCdPessoaSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoaSuperior());
			if(objeto.getCdPais()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPais());
			pstmt.setString(4,objeto.getNmPessoa());
			pstmt.setString(5,objeto.getNrTelefone1());
			pstmt.setString(6,objeto.getNrTelefone2());
			pstmt.setString(7,objeto.getNrCelular());
			pstmt.setString(8,objeto.getNrFax());
			pstmt.setString(9,objeto.getNmEmail());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(11,objeto.getGnPessoa());
			if(objeto.getImgFoto()==null)
				pstmt.setNull(12, Types.BINARY);
			else
				pstmt.setBytes(12,objeto.getImgFoto());
			pstmt.setInt(13,objeto.getStCadastro());
			pstmt.setString(14,objeto.getNmUrl());
			pstmt.setString(15,objeto.getNmApelido());
			pstmt.setString(16,objeto.getTxtObservacao());
			pstmt.setInt(17,objeto.getLgNotificacao());
			pstmt.setString(18,objeto.getIdPessoa());
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdClassificacao());
			if(objeto.getCdFormaDivulgacao()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdFormaDivulgacao());
			if(objeto.getDtChegadaPais()==null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21,new Timestamp(objeto.getDtChegadaPais().getTimeInMillis()));
			if(objeto.getCdUsuarioCadastro()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdUsuarioCadastro());
			pstmt.setString(23,objeto.getNrOab());
			pstmt.setString(24,objeto.getNmParceiro());
			pstmt.setString(25,objeto.getNrCelular2());
			pstmt.executeUpdate();
			return objeto.getCdPessoa();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDAO.insert: " +  e);
			return -1;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Pessoa objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Pessoa objeto, int cdPessoaOld) {
		return update(objeto, cdPessoaOld, null);
	}

	public static int update(Pessoa objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Pessoa objeto, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa SET cd_pessoa=?,"+
												      		   "cd_pessoa_superior=?,"+
												      		   "cd_pais=?,"+
												      		   "nm_pessoa=?,"+
												      		   "nr_telefone1=?,"+
												      		   "nr_telefone2=?,"+
												      		   "nr_celular=?,"+
												      		   "nr_fax=?,"+
												      		   "nm_email=?,"+
												      		   "dt_cadastro=?,"+
												      		   "gn_pessoa=?,"+
												      		   "img_foto=?,"+
												      		   "st_cadastro=?,"+
												      		   "nm_url=?,"+
												      		   "nm_apelido=?,"+
												      		   "txt_observacao=?,"+
												      		   "lg_notificacao=?,"+
												      		   "id_pessoa=?,"+
												      		   "cd_classificacao=?,"+
												      		   "cd_forma_divulgacao=?,"+
												      		   "dt_chegada_pais=?,"+
												      		   "cd_usuario_cadastro=?,"+
												      		   "nr_oab=?, "+
												      		   "nm_parceiro=?,"+
												      		   "nr_celular2=? WHERE cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdPessoaSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoaSuperior());
			if(objeto.getCdPais()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPais());
			pstmt.setString(4,objeto.getNmPessoa());
			pstmt.setString(5,objeto.getNrTelefone1());
			pstmt.setString(6,objeto.getNrTelefone2());
			pstmt.setString(7,objeto.getNrCelular());
			pstmt.setString(8,objeto.getNrFax());
			pstmt.setString(9,objeto.getNmEmail());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(11,objeto.getGnPessoa());
			if(objeto.getImgFoto()==null)
				pstmt.setNull(12, Types.BINARY);
			else
				pstmt.setBytes(12,objeto.getImgFoto());
			pstmt.setInt(13,objeto.getStCadastro());
			pstmt.setString(14,objeto.getNmUrl());
			pstmt.setString(15,objeto.getNmApelido());
			pstmt.setString(16,objeto.getTxtObservacao());
			pstmt.setInt(17,objeto.getLgNotificacao());
			pstmt.setString(18,objeto.getIdPessoa());
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdClassificacao());
			if(objeto.getCdFormaDivulgacao()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdFormaDivulgacao());
			if(objeto.getDtChegadaPais()==null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21,new Timestamp(objeto.getDtChegadaPais().getTimeInMillis()));
			if(objeto.getCdUsuarioCadastro()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdUsuarioCadastro());
			pstmt.setString(23,objeto.getNrOab());
			pstmt.setString(24,objeto.getNmParceiro());
			pstmt.setString(25,objeto.getNrCelular2());
			pstmt.setInt(26, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);	
			System.err.println("Erro! PessoaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa) {
		return delete(cdPessoa, null);
	}

	public static int delete(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa WHERE cd_pessoa=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Pessoa get(int cdPessoa) {
		return get(cdPessoa, null);
	}

	public static Pessoa get(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa WHERE cd_pessoa=?");
			pstmt.setInt(1, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Pessoa(rs.getInt("cd_pessoa"),
						rs.getInt("cd_pessoa_superior"),
						rs.getInt("cd_pais"),
						rs.getString("nm_pessoa"),
						rs.getString("nr_telefone1"),
						rs.getString("nr_telefone2"),
						rs.getString("nr_celular"),
						rs.getString("nr_fax"),
						rs.getString("nm_email"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getInt("gn_pessoa"),
						rs.getBytes("img_foto")==null?null:rs.getBytes("img_foto"),
						rs.getInt("st_cadastro"),
						rs.getString("nm_url"),
						rs.getString("nm_apelido"),
						rs.getString("txt_observacao"),
						rs.getInt("lg_notificacao"),
						rs.getString("id_pessoa"),
						rs.getInt("cd_classificacao"),
						rs.getInt("cd_forma_divulgacao"),
						(rs.getTimestamp("dt_chegada_pais")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_chegada_pais").getTime()),
						rs.getInt("cd_usuario_cadastro"),
						rs.getString("nr_oab"),
						rs.getString("nm_parceiro"),
						rs.getString("nr_celular2"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
