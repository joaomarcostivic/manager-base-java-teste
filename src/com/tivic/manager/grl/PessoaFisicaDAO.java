package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class PessoaFisicaDAO{

	public static int insert(PessoaFisica objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaFisica objeto, Connection connect){
	    return insert(objeto, connect, false);
	}
	
	public static int insert(PessoaFisica objeto, Connection connect, boolean sync){
	    boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = sync ? PessoaDAO.insert(objeto, connect, true) : PessoaDAO.insert(objeto, connect);
			
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPessoa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_fisica (cd_pessoa,"+
			                                  "cd_naturalidade,"+
			                                  "cd_escolaridade,"+
			                                  "dt_nascimento,"+
			                                  "nr_cpf,"+
			                                  "sg_orgao_rg,"+
			                                  "nm_mae,"+
			                                  "nm_pai,"+
			                                  "tp_sexo,"+
			                                  "st_estado_civil,"+
			                                  "nr_rg,"+
			                                  "nr_cnh,"+
			                                  "dt_validade_cnh,"+
			                                  "dt_primeira_habilitacao,"+
			                                  "tp_categoria_habilitacao,"+
			                                  "tp_raca,"+
			                                  "lg_deficiente_fisico,"+
			                                  "nm_forma_tratamento,"+
			                                  "cd_estado_rg,"+
			                                  "dt_emissao_rg,"+
			                                  "blb_fingerprint," +
			                                  "cd_conjuge,"+
			                                  "qt_membros_familia,"+
			                                  "vl_renda_familiar_per_capta,"+
			                                  "tp_nacionalidade,"+
			                                  "tp_filiacao_pai,"+
			                                  "lg_documento_ausente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdNaturalidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdNaturalidade());
			if(objeto.getCdEscolaridade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEscolaridade());
			if(objeto.getDtNascimento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtNascimento().getTimeInMillis()));
			pstmt.setString(5,objeto.getNrCpf());
			pstmt.setString(6,objeto.getSgOrgaoRg());
			pstmt.setString(7,objeto.getNmMae());
			pstmt.setString(8,objeto.getNmPai());
			pstmt.setInt(9,objeto.getTpSexo());
			pstmt.setInt(10,objeto.getStEstadoCivil());
			pstmt.setString(11,objeto.getNrRg());
			pstmt.setString(12,objeto.getNrCnh());
			if(objeto.getDtValidadeCnh()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtValidadeCnh().getTimeInMillis()));
			if(objeto.getDtPrimeiraHabilitacao()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtPrimeiraHabilitacao().getTimeInMillis()));
			pstmt.setInt(15,objeto.getTpCategoriaHabilitacao());
			pstmt.setInt(16,objeto.getTpRaca());
			pstmt.setInt(17,objeto.getLgDeficienteFisico());
			pstmt.setString(18,objeto.getNmFormaTratamento());
			if(objeto.getCdEstadoRg()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdEstadoRg());
			if(objeto.getDtEmissaoRg()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtEmissaoRg().getTimeInMillis()));
			if(objeto.getBlbFingerprint()==null)
				pstmt.setNull(21, Types.BINARY);
			else
				pstmt.setBytes(21,objeto.getBlbFingerprint());
			if(objeto.getCdConjuge()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdConjuge());
			pstmt.setInt(23,objeto.getQtMembrosFamilia());
			pstmt.setFloat(24,objeto.getVlRendaFamiliarPerCapta());
			pstmt.setInt(25,objeto.getTpNacionalidade());
			pstmt.setInt(26,objeto.getTpFiliacaoPai());
			pstmt.setInt(27,objeto.getLgDocumentoAusente());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaFisica objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PessoaFisica objeto, int cdPessoaOld) {
		return update(objeto, cdPessoaOld, null);
	}

	public static int update(PessoaFisica objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PessoaFisica objeto, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			PessoaFisica objetoTemp = get(objeto.getCdPessoa(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_fisica (cd_pessoa,"+
			                                  "cd_naturalidade,"+
			                                  "cd_escolaridade,"+
			                                  "dt_nascimento,"+
			                                  "nr_cpf,"+
			                                  "sg_orgao_rg,"+
			                                  "nm_mae,"+
			                                  "nm_pai,"+
			                                  "tp_sexo,"+
			                                  "st_estado_civil,"+
			                                  "nr_rg,"+
			                                  "nr_cnh,"+
			                                  "dt_validade_cnh,"+
			                                  "dt_primeira_habilitacao,"+
			                                  "tp_categoria_habilitacao,"+
			                                  "tp_raca,"+
			                                  "lg_deficiente_fisico,"+
			                                  "nm_forma_tratamento,"+
			                                  "cd_estado_rg,"+
			                                  "dt_emissao_rg,"+
			                                  "blb_fingerprint," +
			                                  "cd_conjuge,"+
			                                  "qt_membros_familia,"+
			                                  "vl_renda_familiar_per_capta,"+
			                                  "tp_nacionalidade,"+
			                                  "tp_filiacao_pai,"+
			                                  "lg_documento_ausente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE grl_pessoa_fisica SET cd_pessoa=?,"+
												      		   "cd_naturalidade=?,"+
												      		   "cd_escolaridade=?,"+
												      		   "dt_nascimento=?,"+
												      		   "nr_cpf=?,"+
												      		   "sg_orgao_rg=?,"+
												      		   "nm_mae=?,"+
												      		   "nm_pai=?,"+
												      		   "tp_sexo=?,"+
												      		   "st_estado_civil=?,"+
												      		   "nr_rg=?,"+
												      		   "nr_cnh=?,"+
												      		   "dt_validade_cnh=?,"+
												      		   "dt_primeira_habilitacao=?,"+
												      		   "tp_categoria_habilitacao=?,"+
												      		   "tp_raca=?,"+
												      		   "lg_deficiente_fisico=?,"+
												      		   "nm_forma_tratamento=?,"+
												      		   "cd_estado_rg=?,"+
												      		   "dt_emissao_rg=?,"+
												      		   "blb_fingerprint=?," +
												      		   "cd_conjuge=?,"+
												      		   "qt_membros_familia=?,"+
												      		   "vl_renda_familiar_per_capta=?,"+
												      		   "tp_nacionalidade=?,"+
												      		   "tp_filiacao_pai=?,"+
												      		   "lg_documento_ausente=? WHERE cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdNaturalidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdNaturalidade());
			if(objeto.getCdEscolaridade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEscolaridade());
			if(objeto.getDtNascimento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtNascimento().getTimeInMillis()));
			pstmt.setString(5,objeto.getNrCpf());
			pstmt.setString(6,objeto.getSgOrgaoRg());
			pstmt.setString(7,objeto.getNmMae());
			pstmt.setString(8,objeto.getNmPai());
			pstmt.setInt(9,objeto.getTpSexo());
			pstmt.setInt(10,objeto.getStEstadoCivil());
			pstmt.setString(11,objeto.getNrRg());
			pstmt.setString(12,objeto.getNrCnh());
			if(objeto.getDtValidadeCnh()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtValidadeCnh().getTimeInMillis()));
			if(objeto.getDtPrimeiraHabilitacao()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtPrimeiraHabilitacao().getTimeInMillis()));
			pstmt.setInt(15,objeto.getTpCategoriaHabilitacao());
			pstmt.setInt(16,objeto.getTpRaca());
			pstmt.setInt(17,objeto.getLgDeficienteFisico());
			pstmt.setString(18,objeto.getNmFormaTratamento());
			if(objeto.getCdEstadoRg()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdEstadoRg());
			if(objeto.getDtEmissaoRg()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtEmissaoRg().getTimeInMillis()));
			if(objeto.getBlbFingerprint()==null)
				pstmt.setNull(21, Types.BINARY);
			else
				pstmt.setBytes(21,objeto.getBlbFingerprint());
			if(objeto.getCdConjuge()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdConjuge());
			pstmt.setInt(23,objeto.getQtMembrosFamilia());
			pstmt.setFloat(24,objeto.getVlRendaFamiliarPerCapta());
			pstmt.setInt(25,objeto.getTpNacionalidade());
			pstmt.setInt(26,objeto.getTpFiliacaoPai());
			pstmt.setInt(27,objeto.getLgDocumentoAusente());
			if (objetoTemp != null) {
				pstmt.setInt(28, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
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
			System.err.println("Erro! PessoaFisicaDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_fisica WHERE cd_pessoa=?");
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
			System.err.println("Erro! PessoaFisicaDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaFisica get(int cdPessoa) {
		return get(cdPessoa, null);
	}

	public static PessoaFisica get(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica A, grl_pessoa B WHERE A.cd_pessoa=B.cd_pessoa AND A.cd_pessoa=?");
			pstmt.setInt(1, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaFisica(rs.getInt("cd_pessoa"),
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
						rs.getString("nr_celular2"),
						rs.getInt("cd_naturalidade"),
						rs.getInt("cd_escolaridade"),
						(rs.getTimestamp("dt_nascimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_nascimento").getTime()),
						rs.getString("nr_cpf"),
						rs.getString("sg_orgao_rg"),
						rs.getString("nm_mae"),
						rs.getString("nm_pai"),
						rs.getInt("tp_sexo"),
						rs.getInt("st_estado_civil"),
						rs.getString("nr_rg"),
						rs.getString("nr_cnh"),
						(rs.getTimestamp("dt_validade_cnh")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade_cnh").getTime()),
						(rs.getTimestamp("dt_primeira_habilitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeira_habilitacao").getTime()),
						rs.getInt("tp_categoria_habilitacao"),
						rs.getInt("tp_raca"),
						rs.getInt("lg_deficiente_fisico"),
						rs.getString("nm_forma_tratamento"),
						rs.getInt("cd_estado_rg"),
						(rs.getTimestamp("dt_emissao_rg")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao_rg").getTime()),
						rs.getBytes("blb_fingerprint")==null?null:rs.getBytes("blb_fingerprint"),
						rs.getInt("cd_conjuge"),
						rs.getInt("qt_membros_familia"),
						rs.getFloat("vl_renda_familiar_per_capta"),
						rs.getInt("tp_nacionalidade"),
						rs.getInt("tp_filiacao_pai"),
						rs.getInt("lg_documento_ausente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_fisica", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static PessoaFisica getByCpf(String nrCpfPessoaFisica) {
		return getByCpf(nrCpfPessoaFisica, null);
	}
	
	public static PessoaFisica getByCpf(String nrCpfPessoaFisica, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica A, grl_pessoa B WHERE A.cd_pessoa=B.cd_pessoa AND A.nr_cpf=?");
			pstmt.setString(1, nrCpfPessoaFisica);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaFisica(rs.getInt("cd_pessoa"),
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
						rs.getString("nr_celular2"),
						rs.getInt("cd_naturalidade"),
						rs.getInt("cd_escolaridade"),
						(rs.getTimestamp("dt_nascimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_nascimento").getTime()),
						rs.getString("nr_cpf"),
						rs.getString("sg_orgao_rg"),
						rs.getString("nm_mae"),
						rs.getString("nm_pai"),
						rs.getInt("tp_sexo"),
						rs.getInt("st_estado_civil"),
						rs.getString("nr_rg"),
						rs.getString("nr_cnh"),
						(rs.getTimestamp("dt_validade_cnh")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade_cnh").getTime()),
						(rs.getTimestamp("dt_primeira_habilitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeira_habilitacao").getTime()),
						rs.getInt("tp_categoria_habilitacao"),
						rs.getInt("tp_raca"),
						rs.getInt("lg_deficiente_fisico"),
						rs.getString("nm_forma_tratamento"),
						rs.getInt("cd_estado_rg"),
						(rs.getTimestamp("dt_emissao_rg")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao_rg").getTime()),
						rs.getBytes("blb_fingerprint")==null?null:rs.getBytes("blb_fingerprint"),
						rs.getInt("cd_conjuge"),
						rs.getInt("qt_membros_familia"),
						rs.getFloat("vl_renda_familiar_per_capta"),
						rs.getInt("tp_nacionalidade"),
						rs.getInt("tp_filiacao_pai"),
						rs.getInt("lg_documento_ausente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFisicaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
