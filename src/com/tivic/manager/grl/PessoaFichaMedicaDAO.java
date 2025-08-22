package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class PessoaFichaMedicaDAO{

	public static int insert(PessoaFichaMedica objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaFichaMedica objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_ficha_medica (cd_pessoa,"+
			                                  "tp_sangue,"+
			                                  "tp_fator_rh,"+
			                                  "dt_criacao,"+
			                                  "dt_alteracao,"+
			                                  "cd_usuario,"+
			                                  "txt_descricao,"+
                    						  "nr_cartao_sus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getTpSangue());
			pstmt.setInt(3,objeto.getTpFatorRh());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtAlteracao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAlteracao().getTimeInMillis()));
			pstmt.setInt(6,objeto.getCdUsuario());
			pstmt.setString(7,objeto.getTxtDescricao());
			pstmt.setString(8,objeto.getNrCartaoSus());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return objeto.getCdPessoa();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaFichaMedicaDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFichaMedicaDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaFichaMedica objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PessoaFichaMedica objeto, int cdPessoaOld) {
		return update(objeto, cdPessoaOld, null);
	}

	public static int update(PessoaFichaMedica objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PessoaFichaMedica objeto, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			PessoaFichaMedica objetoTemp = get(objeto.getCdPessoa(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_ficha_medica (cd_pessoa,"+
			                                  "tp_sangue,"+
			                                  "tp_fator_rh,"+
			                                  "dt_criacao,"+
			                                  "dt_alteracao,"+
			                                  "cd_usuario,"+
			                                  "txt_descricao=?,"+
											  "nr_cartao_sus=? WHERE cd_pessoa=?");
			else
				pstmt = connect.prepareStatement("UPDATE grl_pessoa_ficha_medica SET cd_pessoa=?,"+
												      		   "tp_sangue=?,"+
												      		   "tp_fator_rh=?,"+
												      		   "dt_criacao=?,"+
												      		   "dt_alteracao=?,"+
												      		   "cd_usuario=?,"+
												      		   "txt_descricao=?,"+
															   "nr_cartao_sus=? WHERE cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getTpSangue());
			pstmt.setInt(3,objeto.getTpFatorRh());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtAlteracao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtAlteracao().getTimeInMillis()));
			pstmt.setInt(6,objeto.getCdUsuario());
			pstmt.setString(7,objeto.getTxtDescricao());
			pstmt.setString(8,objeto.getNrCartaoSus());
			pstmt.setInt(9, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaFichaMedicaDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFichaMedicaDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_ficha_medica WHERE cd_pessoa=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.PessoaFisicaDAO.delete(cdPessoa, connect)<=0) {
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
			System.err.println("Erro! PessoaFichaMedicaDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFichaMedicaDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaFichaMedica get(int cdPessoa) {
		return get(cdPessoa, null);
	}

	public static PessoaFichaMedica get(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_ficha_medica A, grl_pessoa_fisica B, grl_pessoa C WHERE A.cd_pessoa=B.cd_pessoa AND A.cd_pessoa=? AND A.cd_pessoa = C.cd_pessoa");
			pstmt.setInt(1, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaFichaMedica(rs.getInt("cd_pessoa"),
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
						rs.getInt("tp_sangue"),
						rs.getInt("tp_fator_rh"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						(rs.getTimestamp("dt_alteracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_alteracao").getTime()),
						rs.getInt("cd_usuario"),
						rs.getString("txt_descricao"),
						rs.getString("nr_cartao_sus"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaFichaMedicaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFichaMedicaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_ficha_medica");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaFichaMedicaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFichaMedicaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PessoaFichaMedica> getList() {
		return getList(null);
	}

	public static ArrayList<PessoaFichaMedica> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PessoaFichaMedica> list = new ArrayList<PessoaFichaMedica>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PessoaFichaMedica obj = PessoaFichaMedicaDAO.get(rsm.getInt("cd_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaFichaMedicaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_ficha_medica", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
