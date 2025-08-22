package com.tivic.manager.ctb;

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

	public static int insert(Empresa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.EmpresaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEmpresa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_empresa (cd_empresa,"+
			                                  "nr_registro_cartorio,"+
			                                  "nr_nire,"+
			                                  "nr_inscricao_suframa,"+
			                                  "dt_ultima_auditoria,"+
			                                  "nr_oab,"+
			                                  "nr_junta_comercial,"+
			                                  "dt_junta_comercial) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setString(2,objeto.getNrRegistroCartorio());
			pstmt.setString(3,objeto.getNrNire());
			pstmt.setString(4,objeto.getNrInscricaoSuframa());
			if(objeto.getDtUltimaAuditoria()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtUltimaAuditoria().getTimeInMillis()));
			pstmt.setString(6,objeto.getNrOab());
			pstmt.setString(7,objeto.getNrJuntaComercial());
			if(objeto.getDtJuntaComercial()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtJuntaComercial().getTimeInMillis()));
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.insert: " +  e);
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
			Empresa objetoTemp = get(objeto.getCdEmpresa(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO ctb_empresa (cd_empresa,"+
			                                  "nr_registro_cartorio,"+
			                                  "nr_nire,"+
			                                  "nr_inscricao_suframa,"+
			                                  "dt_ultima_auditoria,"+
			                                  "nr_oab,"+
			                                  "nr_junta_comercial,"+
			                                  "dt_junta_comercial) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE ctb_empresa SET cd_empresa=?,"+
												      		   "nr_registro_cartorio=?,"+
												      		   "nr_nire=?,"+
												      		   "nr_inscricao_suframa=?,"+
												      		   "dt_ultima_auditoria=?,"+
												      		   "nr_oab=?,"+
												      		   "nr_junta_comercial=?,"+
												      		   "dt_junta_comercial=? WHERE cd_empresa=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setString(2,objeto.getNrRegistroCartorio());
			pstmt.setString(3,objeto.getNrNire());
			pstmt.setString(4,objeto.getNrInscricaoSuframa());
			if(objeto.getDtUltimaAuditoria()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtUltimaAuditoria().getTimeInMillis()));
			pstmt.setString(6,objeto.getNrOab());
			pstmt.setString(7,objeto.getNrJuntaComercial());
			if(objeto.getDtJuntaComercial()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtJuntaComercial().getTimeInMillis()));
			if (objetoTemp != null) {
				pstmt.setInt(9, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.EmpresaDAO.update(objeto, connect)<=0) {
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_empresa WHERE cd_empresa=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.EmpresaDAO.delete(cdEmpresa, connect)<=0) {
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_empresa A, grl_empresa B, grl_pessoa_juridica C, grl_pessoa D " +
					"WHERE A.cd_empresa=B.cd_empresa AND B.cd_empresa = C.cd_pessoa AND C.cd_pessoa = D.cd_pessoa AND A.cd_empresa=?");
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
						rs.getInt("cd_tabela_cat_economica"),
						rs.getString("nr_registro_cartorio"),
						rs.getString("nr_nire"),
						rs.getString("nr_inscricao_suframa"),
						(rs.getTimestamp("dt_ultima_auditoria")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ultima_auditoria").getTime()),
						rs.getString("nr_oab"),
						rs.getString("nr_junta_comercial"),
						(rs.getTimestamp("dt_junta_comercial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_junta_comercial").getTime()));
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_empresa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaDAO.getAll: " + sqlExpt);
			return null;
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
		return Search.find("SELECT * FROM ctb_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
