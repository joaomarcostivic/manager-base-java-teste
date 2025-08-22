package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PessoaJuridicaDAO{

	public static int insert(PessoaJuridica objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaJuridica objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = PessoaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				System.out.println("PessoaJuridica.insert: Erro ao tentar incluir Pessoa Jurídica. "+objeto);
				return -1;
			}
			objeto.setCdPessoa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_juridica (cd_pessoa,"+
			                                  "nr_cnpj,"+
			                                  "nm_razao_social,"+
			                                  "nr_inscricao_estadual,"+
			                                  "nr_inscricao_municipal,"+
			                                  "nr_funcionarios,"+
			                                  "dt_inicio_atividade,"+
			                                  "cd_natureza_juridica,"+
			                                  "tp_empresa,"+
			                                  "dt_termino_atividade," +
			                                  "cd_naturalidade," +
			                                  "nr_cnae,"+ 
			                                  "id_serventia,"+
			                                  "lg_documento_ausente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setString(2,objeto.getNrCnpj());
			pstmt.setString(3,objeto.getNmRazaoSocial());
			pstmt.setString(4,objeto.getNrInscricaoEstadual());
			pstmt.setString(5,objeto.getNrInscricaoMunicipal());
			pstmt.setInt(6,objeto.getNrFuncionarios());
			if(objeto.getDtInicioAtividade()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicioAtividade().getTimeInMillis()));
			if(objeto.getCdNaturezaJuridica()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdNaturezaJuridica());
			pstmt.setInt(9,objeto.getTpEmpresa());
			if(objeto.getDtTerminoAtividade()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtTerminoAtividade().getTimeInMillis()));
			pstmt.setInt(11,objeto.getCdNaturalidade());
			pstmt.setString(12,objeto.getNrCnae());
			pstmt.setString(13,objeto.getIdServentia());
			pstmt.setInt(14,objeto.getLgDocumentoAusente());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaJuridicaDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaJuridica objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PessoaJuridica objeto, int cdPessoaOld) {
		return update(objeto, cdPessoaOld, null);
	}

	public static int update(PessoaJuridica objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PessoaJuridica objeto, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			PessoaJuridica objetoTemp = get(objeto.getCdPessoa(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_juridica (cd_pessoa,"+
			                                  "nr_cnpj,"+
			                                  "nm_razao_social,"+
			                                  "nr_inscricao_estadual,"+
			                                  "nr_inscricao_municipal,"+
			                                  "nr_funcionarios,"+
			                                  "dt_inicio_atividade,"+
			                                  "cd_natureza_juridica,"+
			                                  "tp_empresa,"+
			                                  "dt_termino_atividade," +
			                                  "cd_naturalidade," +
			                                  "nr_cnae,"+
			                                  "id_serventia,"+
			                                  "lg_documento_ausente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE grl_pessoa_juridica SET cd_pessoa=?,"+
												      		   "nr_cnpj=?,"+
												      		   "nm_razao_social=?,"+
												      		   "nr_inscricao_estadual=?,"+
												      		   "nr_inscricao_municipal=?,"+
												      		   "nr_funcionarios=?,"+
												      		   "dt_inicio_atividade=?,"+
												      		   "cd_natureza_juridica=?,"+
												      		   "tp_empresa=?,"+
												      		   "dt_termino_atividade=?," +
												      		   "cd_naturalidade=?," +
												      		   "nr_cnae=?,"+
												      		   "id_serventia=?,"+
												      		   "lg_documento_ausente=? WHERE cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setString(2,objeto.getNrCnpj());
			pstmt.setString(3,objeto.getNmRazaoSocial());
			pstmt.setString(4,objeto.getNrInscricaoEstadual());
			pstmt.setString(5,objeto.getNrInscricaoMunicipal());
			pstmt.setInt(6,objeto.getNrFuncionarios());
			if(objeto.getDtInicioAtividade()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicioAtividade().getTimeInMillis()));
			if(objeto.getCdNaturezaJuridica()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdNaturezaJuridica());
			pstmt.setInt(9,objeto.getTpEmpresa());
			if(objeto.getDtTerminoAtividade()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtTerminoAtividade().getTimeInMillis()));
			pstmt.setInt(11,objeto.getCdNaturalidade());
			pstmt.setString(12,objeto.getNrCnae());
			pstmt.setString(13,objeto.getIdServentia());
			pstmt.setInt(14,objeto.getLgDocumentoAusente());
			if (objetoTemp != null) {
				pstmt.setInt(15, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			}
			pstmt.executeUpdate();
			if (PessoaDAO.update(objeto, connect)<=0) {
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
			System.err.println("Erro! PessoaJuridicaDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaJuridicaDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
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
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_juridica WHERE cd_pessoa=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.executeUpdate();
			if (PessoaDAO.delete(cdPessoa, connect)<=0) {
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
			System.err.println("Erro! PessoaJuridicaDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaJuridicaDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaJuridica get(int cdPessoa) {
		return get(cdPessoa, null);
	}

	public static PessoaJuridica get(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_juridica A, grl_pessoa B WHERE A.cd_pessoa=B.cd_pessoa AND A.cd_pessoa=?");
			pstmt.setInt(1, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaJuridica(rs.getInt("cd_pessoa"),
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
						rs.getString("nr_cnpj"),
						rs.getString("nm_razao_social"),
						rs.getString("nr_inscricao_estadual"),
						rs.getString("nr_inscricao_municipal"),
						rs.getInt("nr_funcionarios"),
						(rs.getTimestamp("dt_inicio_atividade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_atividade").getTime()),
						rs.getInt("cd_natureza_juridica"),
						rs.getInt("tp_empresa"),
						(rs.getTimestamp("dt_termino_atividade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_termino_atividade").getTime()),
						rs.getInt("cd_naturalidade"),
						rs.getString("nr_cnae"),
						rs.getString("id_serventia"),
						rs.getInt("lg_documento_ausente"),
						rs.getString("nr_oab"),
						rs.getString("nm_parceiro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaJuridicaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaJuridicaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_juridica");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaJuridicaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaJuridicaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_juridica", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
