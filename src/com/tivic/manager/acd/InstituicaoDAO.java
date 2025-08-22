package com.tivic.manager.acd;

import java.sql.*;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class InstituicaoDAO{

	public static int insert(Instituicao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Instituicao objeto, Connection connect){
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
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao (cd_instituicao,"+
			                                  "nr_secretario,"+
			                                  "nr_diretor,"+
			                                  "nm_diario_oficial,"+
			                                  "nm_resolucao,"+
			                                  "nm_parecer,"+
			                                  "qt_minutos_matutino,"+
			                                  "qt_minutos_vespertino,"+
			                                  "qt_minutos_noturno,"+
			                                  "nr_vagas_teorica,"+
			                                  "nr_vagas_pratica,"+
			                                  "vl_hora_aula,"+
			                                  "cd_diretor,"+
			                                  "cd_coordenador,"+
			                                  "cd_vice_diretor,"+
			                                  "cd_secretario,"+
			                                  "cd_tesoureiro,"+
			                                  "cd_administrador,"+
			                                  "txt_diario_classe_1,"+
			                                  "txt_diario_classe_2,"+
			                                  "qt_limite_faltas,"+
			                                  "lg_rede,"+
			                                  "nr_inep,"+
			                                  "cd_formulario,"+
			                                  "tp_instituicao,"+
			                                  "lg_offline) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setString(2,objeto.getNrSecretario());
			pstmt.setString(3,objeto.getNrDiretor());
			pstmt.setString(4,objeto.getNmDiarioOficial());
			pstmt.setString(5,objeto.getNmResolucao());
			pstmt.setString(6,objeto.getNmParecer());
			pstmt.setInt(7,objeto.getQtMinutosMatutino());
			pstmt.setInt(8,objeto.getQtMinutosVespertino());
			pstmt.setInt(9,objeto.getQtMinutosNoturno());
			pstmt.setInt(10,objeto.getNrVagasTeorica());
			pstmt.setInt(11,objeto.getNrVagasPratica());
			pstmt.setFloat(12,objeto.getVlHoraAula());
			if(objeto.getCdDiretor()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdDiretor());
			if(objeto.getCdCoordenador()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdCoordenador());
			if(objeto.getCdViceDiretor()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdViceDiretor());
			if(objeto.getCdSecretario()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdSecretario());
			if(objeto.getCdTesoureiro()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdTesoureiro());
			if(objeto.getCdAdministrador()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdAdministrador());
			pstmt.setString(19,objeto.getTxtDiarioClasse1());
			pstmt.setString(20,objeto.getTxtDiarioClasse2());
			pstmt.setInt(21,objeto.getQtLimiteFaltas());
			if(objeto.getLgRede()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getLgRede());
			pstmt.setString(23,objeto.getNrInep());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24,objeto.getCdFormulario());
			pstmt.setInt(25,objeto.getTpInstituicao());
			pstmt.setInt(26,objeto.getLgOffline());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Instituicao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Instituicao objeto, int cdInstituicaoOld) {
		return update(objeto, cdInstituicaoOld, null);
	}

	public static int update(Instituicao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Instituicao objeto, int cdInstituicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Instituicao objetoTemp = get(objeto.getCdEmpresa(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO acd_instituicao (cd_instituicao,"+
			                                  "nr_secretario,"+
			                                  "nr_diretor,"+
			                                  "nm_diario_oficial,"+
			                                  "nm_resolucao,"+
			                                  "nm_parecer,"+
			                                  "qt_minutos_matutino,"+
			                                  "qt_minutos_vespertino,"+
			                                  "qt_minutos_noturno,"+
			                                  "nr_vagas_teorica,"+
			                                  "nr_vagas_pratica,"+
			                                  "vl_hora_aula,"+
			                                  "cd_diretor,"+
			                                  "cd_coordenador,"+
			                                  "cd_vice_diretor,"+
			                                  "cd_secretario,"+
			                                  "cd_tesoureiro,"+
			                                  "cd_administrador,"+
			                                  "txt_diario_classe_1,"+
			                                  "txt_diario_classe_2,"+
			                                  "qt_limite_faltas,"+
			                                  "lg_rede,"+
			                                  "nr_inep,"+
			                                  "cd_formulario,"+
			                                  "tp_instituicao,"+
			                                  "lg_offline) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE acd_instituicao SET cd_instituicao=?,"+
												      		   "nr_secretario=?,"+
												      		   "nr_diretor=?,"+
												      		   "nm_diario_oficial=?,"+
												      		   "nm_resolucao=?,"+
												      		   "nm_parecer=?,"+
												      		   "qt_minutos_matutino=?,"+
												      		   "qt_minutos_vespertino=?,"+
												      		   "qt_minutos_noturno=?,"+
												      		   "nr_vagas_teorica=?,"+
												      		   "nr_vagas_pratica=?,"+
												      		   "vl_hora_aula=?,"+
												      		   "cd_diretor=?,"+
												      		   "cd_coordenador=?,"+
												      		   "cd_vice_diretor=?,"+
												      		   "cd_secretario=?,"+
												      		   "cd_tesoureiro=?,"+
												      		   "cd_administrador=?,"+
												      		   "txt_diario_classe_1=?,"+
												      		   "txt_diario_classe_2=?,"+
												      		   "qt_limite_faltas=?,"+
												      		   "lg_rede=?,"+
												      		   "nr_inep=?,"+
												      		   "cd_formulario=?,"+
												      		   "tp_instituicao=?,"+
												      		   "lg_offline=? WHERE cd_instituicao=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setString(2,objeto.getNrSecretario());
			pstmt.setString(3,objeto.getNrDiretor());
			pstmt.setString(4,objeto.getNmDiarioOficial());
			pstmt.setString(5,objeto.getNmResolucao());
			pstmt.setString(6,objeto.getNmParecer());
			pstmt.setInt(7,objeto.getQtMinutosMatutino());
			pstmt.setInt(8,objeto.getQtMinutosVespertino());
			pstmt.setInt(9,objeto.getQtMinutosNoturno());
			pstmt.setInt(10,objeto.getNrVagasTeorica());
			pstmt.setInt(11,objeto.getNrVagasPratica());
			pstmt.setFloat(12,objeto.getVlHoraAula());
			if(objeto.getCdDiretor()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdDiretor());
			if(objeto.getCdCoordenador()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdCoordenador());
			if(objeto.getCdViceDiretor()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdViceDiretor());
			if(objeto.getCdSecretario()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdSecretario());
			if(objeto.getCdTesoureiro()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdTesoureiro());
			if(objeto.getCdAdministrador()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdAdministrador());
			pstmt.setString(19,objeto.getTxtDiarioClasse1());
			pstmt.setString(20,objeto.getTxtDiarioClasse2());
			pstmt.setInt(21,objeto.getQtLimiteFaltas());
			if(objeto.getLgRede()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getLgRede());
			pstmt.setString(23,objeto.getNrInep());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24,objeto.getCdFormulario());
			pstmt.setInt(25,objeto.getTpInstituicao());
			pstmt.setInt(26,objeto.getLgOffline());
			pstmt.setInt(27, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
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
			System.err.println("Erro! InstituicaoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao) {
		return delete(cdInstituicao, null);
	}

	public static int delete(int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao WHERE cd_instituicao=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.EmpresaDAO.delete(cdInstituicao, connect)<=0) {
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
			System.err.println("Erro! InstituicaoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Instituicao get(int cdInstituicao) {
		return get(cdInstituicao, null);
	}

	public static Instituicao get(int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao A, grl_empresa B, grl_pessoa C, grl_pessoa_juridica D WHERE A.cd_instituicao=B.cd_empresa AND B.cd_empresa=C.cd_pessoa AND C.cd_pessoa=D.cd_pessoa AND A.cd_instituicao=?");
			pstmt.setInt(1, cdInstituicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Instituicao(rs.getInt("cd_empresa"),
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
						rs.getString("nr_secretario"),
						rs.getString("nr_diretor"),
						rs.getString("nm_diario_oficial"),
						rs.getString("nm_resolucao"),
						rs.getString("nm_parecer"),
						rs.getInt("qt_minutos_matutino"),
						rs.getInt("qt_minutos_vespertino"),
						rs.getInt("qt_minutos_noturno"),
						rs.getInt("nr_vagas_teorica"),
						rs.getInt("nr_vagas_pratica"),
						rs.getFloat("vl_hora_aula"),
						rs.getInt("cd_diretor"),
						rs.getInt("cd_coordenador"),
						rs.getInt("cd_vice_diretor"),
						rs.getInt("cd_secretario"),
						rs.getInt("cd_tesoureiro"),
						rs.getInt("cd_administrador"),
						rs.getString("txt_diario_classe_1"),
						rs.getString("txt_diario_classe_2"),
						rs.getInt("qt_limite_faltas"),
						rs.getInt("lg_rede"),
						rs.getString("nr_inep"),
						rs.getInt("cd_formulario"),
						rs.getInt("tp_instituicao"),
						rs.getInt("lg_offline"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + e);
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
		String sql = "SELECT A.* "+
			  	 "FROM grl_pessoa A " +
			  	 "JOIN acd_instituicao B ON (A.cd_pessoa = B.cd_instituicao)";
		
		return Search.find(sql, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
