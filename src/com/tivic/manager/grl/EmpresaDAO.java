package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class EmpresaDAO{

	public static int insert(Empresa objeto) {
		return insert(objeto, null);
	}

	public static int insert(Empresa objeto, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = PessoaJuridicaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPessoa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_empresa (cd_empresa,"+
			                                  "lg_matriz,"+
			                                  "img_logomarca,"+
			                                  "id_empresa, " +
			                                  "cd_tabela_cat_economica) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getLgMatriz());
			if(objeto.getImgLogomarca()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getImgLogomarca());
			pstmt.setString(4,objeto.getIdEmpresa());
			if(objeto.getCdTabelaCatEconomica()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTabelaCatEconomica());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Empresa objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Empresa objeto, int cdEmpresaOld) {
		return update(objeto, cdEmpresaOld, null);
	}

	public static int update(Empresa objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Empresa objeto, int cdEmpresaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Empresa objetoTemp = get(objeto.getCdPessoa(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO grl_empresa (cd_empresa,"+
			                                  "lg_matriz,"+
			                                  "img_logomarca,"+
			                                  "id_empresa, " +
			                                  "cd_tabela_cat_economica) VALUES (?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE grl_empresa SET cd_empresa=?,"+
												      		   "lg_matriz=?,"+
												      		   "img_logomarca=?,"+
												      		   "id_empresa=?, " +
												      		   "cd_tabela_cat_economica=? WHERE cd_empresa=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getLgMatriz());
			if(objeto.getImgLogomarca()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getImgLogomarca());
			pstmt.setString(4,objeto.getIdEmpresa());
			if(objeto.getCdTabelaCatEconomica()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTabelaCatEconomica());
			if (objetoTemp != null) {
				pstmt.setInt(6, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdPessoa());
			}
			pstmt.executeUpdate();
			if (PessoaJuridicaDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpresa) {
		return delete(cdEmpresa, null);
	}

	public static int delete(int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_empresa WHERE cd_empresa=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.executeUpdate();
			if (PessoaJuridicaDAO.delete(cdEmpresa, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Empresa get(int cdEmpresa) {
		return get(cdEmpresa, null);
	}

	public static Empresa get(int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_empresa A, grl_pessoa_juridica B, grl_pessoa C WHERE A.cd_empresa=B.cd_pessoa AND B.cd_pessoa = C.cd_pessoa AND A.cd_empresa=?");
			pstmt.setInt(1, cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Empresa(rs.getInt("cd_pessoa"),
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
						rs.getString("nr_cnpj"),
						rs.getString("nm_razao_social"),
						rs.getString("nr_inscricao_estadual"),
						rs.getString("nr_inscricao_municipal"),
						rs.getInt("nr_funcionarios"),
						(rs.getTimestamp("dt_inicio_atividade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_atividade").getTime()),
						rs.getInt("cd_natureza_juridica"),
						rs.getInt("tp_empresa"),
						(rs.getTimestamp("dt_termino_atividade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_termino_atividade").getTime()),
						rs.getInt("lg_matriz"),
						rs.getBytes("img_logomarca")==null?null:rs.getBytes("img_logomarca"),
						rs.getString("id_empresa"),
						rs.getInt("cd_tabela_cat_economica"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_empresa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

