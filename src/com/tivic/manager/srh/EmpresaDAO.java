package com.tivic.manager.srh;

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
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_empresa (cd_empresa,"+
			                                  "cd_fpas,"+
			                                  "cd_terceiros,"+
			                                  "cd_tabela_evento,"+
			                                  "cd_folha_pagamento,"+
			                                  "id_fgts,"+
			                                  "id_gps,"+
			                                  "id_sureg,"+
			                                  "id_sat,"+
			                                  "id_cei,"+
			                                  "pr_sat,"+
			                                  "pr_isencao_filantropia,"+
			                                  "pr_anuidade,"+
			                                  "pr_prolabore_gps,"+
			                                  "nr_mes_dissidio_coletivo,"+
			                                  "nr_mes_adiantamento_decimo,"+
			                                  "nr_mes_antecipacao_decimo,"+
			                                  "qt_anos_intervalo_licenca,"+
			                                  "qt_meses_licenca_premio,"+
			                                  "lg_pat,"+
			                                  "lg_recolhe_pis,"+
			                                  "lg_caged_disco,"+
			                                  "lg_recolhe_grps,"+
			                                  "lg_verifica_vaga,"+
			                                  "lg_calcula_adicional_tempo,"+
			                                  "lg_dependente_informado,"+
			                                  "qt_anos_anuidade,"+
			                                  "tp_deducao_falta,"+
			                                  "tp_categoria_fgts,"+
			                                  "tp_calculo_ferias,"+
			                                  "tp_pagamento_ferias,"+
			                                  "tp_adiantamento_decimo,"+
			                                  "vl_arredondamento,"+
			                                  "nm_departamento_rh,"+
			                                  "nm_chefe_departamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdFpas()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFpas());
			if(objeto.getCdTerceiros()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTerceiros());
			if(objeto.getCdTabelaEvento()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTabelaEvento());
			if(objeto.getCdFolhaPagamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFolhaPagamento());
			pstmt.setString(6,objeto.getIdFgts());
			pstmt.setString(7,objeto.getIdGps());
			pstmt.setString(8,objeto.getIdSureg());
			pstmt.setString(9,objeto.getIdSat());
			pstmt.setString(10,objeto.getIdCei());
			pstmt.setFloat(11,objeto.getPrSat());
			pstmt.setFloat(12,objeto.getPrIsencaoFilantropia());
			pstmt.setFloat(13,objeto.getPrAnuidade());
			pstmt.setFloat(14,objeto.getPrProlaboreGps());
			pstmt.setInt(15,objeto.getNrMesDissidioColetivo());
			pstmt.setInt(16,objeto.getNrMesAdiantamentoDecimo());
			pstmt.setInt(17,objeto.getNrMesAntecipacaoDecimo());
			pstmt.setInt(18,objeto.getQtAnosIntervaloLicenca());
			pstmt.setInt(19,objeto.getQtMesesLicencaPremio());
			pstmt.setInt(20,objeto.getLgPat());
			pstmt.setInt(21,objeto.getLgRecolhePis());
			pstmt.setInt(22,objeto.getLgCagedDisco());
			pstmt.setInt(23,objeto.getLgRecolheGrps());
			pstmt.setInt(24,objeto.getLgVerificaVaga());
			pstmt.setInt(25,objeto.getLgCalculaAdicionalTempo());
			pstmt.setInt(26,objeto.getLgDependenteInformado());
			pstmt.setInt(27,objeto.getQtAnosAnuidade());
			pstmt.setInt(28,objeto.getTpDeducaoFalta());
			pstmt.setInt(29,objeto.getTpCategoriaFgts());
			pstmt.setInt(30,objeto.getTpCalculoFerias());
			pstmt.setInt(31,objeto.getTpPagamentoFerias());
			pstmt.setInt(32,objeto.getTpAdiantamentoDecimo());
			pstmt.setFloat(33,objeto.getVlArredondamento());
			pstmt.setString(34,objeto.getNmDepartamentoRh());
			pstmt.setString(35,objeto.getNmChefeDepartamento());
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
				pstmt = connect.prepareStatement("INSERT INTO srh_empresa (cd_empresa,"+
			                                  "cd_fpas,"+
			                                  "cd_terceiros,"+
			                                  "cd_tabela_evento,"+
			                                  "cd_folha_pagamento,"+
			                                  "id_fgts,"+
			                                  "id_gps,"+
			                                  "id_sureg,"+
			                                  "id_sat,"+
			                                  "id_cei,"+
			                                  "pr_sat,"+
			                                  "pr_isencao_filantropia,"+
			                                  "pr_anuidade,"+
			                                  "pr_prolabore_gps,"+
			                                  "nr_mes_dissidio_coletivo,"+
			                                  "nr_mes_adiantamento_decimo,"+
			                                  "nr_mes_antecipacao_decimo,"+
			                                  "qt_anos_intervalo_licenca,"+
			                                  "qt_meses_licenca_premio,"+
			                                  "lg_pat,"+
			                                  "lg_recolhe_pis,"+
			                                  "lg_caged_disco,"+
			                                  "lg_recolhe_grps,"+
			                                  "lg_verifica_vaga,"+
			                                  "lg_calcula_adicional_tempo,"+
			                                  "lg_dependente_informado,"+
			                                  "qt_anos_anuidade,"+
			                                  "tp_deducao_falta,"+
			                                  "tp_categoria_fgts,"+
			                                  "tp_calculo_ferias,"+
			                                  "tp_pagamento_ferias,"+
			                                  "tp_adiantamento_decimo,"+
			                                  "vl_arredondamento,"+
			                                  "nm_departamento_rh,"+
			                                  "nm_chefe_departamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE srh_empresa SET cd_empresa=?,"+
												      		   "cd_fpas=?,"+
												      		   "cd_terceiros=?,"+
												      		   "cd_tabela_evento=?,"+
												      		   "cd_folha_pagamento=?,"+
												      		   "id_fgts=?,"+
												      		   "id_gps=?,"+
												      		   "id_sureg=?,"+
												      		   "id_sat=?,"+
												      		   "id_cei=?,"+
												      		   "pr_sat=?,"+
												      		   "pr_isencao_filantropia=?,"+
												      		   "pr_anuidade=?,"+
												      		   "pr_prolabore_gps=?,"+
												      		   "nr_mes_dissidio_coletivo=?,"+
												      		   "nr_mes_adiantamento_decimo=?,"+
												      		   "nr_mes_antecipacao_decimo=?,"+
												      		   "qt_anos_intervalo_licenca=?,"+
												      		   "qt_meses_licenca_premio=?,"+
												      		   "lg_pat=?,"+
												      		   "lg_recolhe_pis=?,"+
												      		   "lg_caged_disco=?,"+
												      		   "lg_recolhe_grps=?,"+
												      		   "lg_verifica_vaga=?,"+
												      		   "lg_calcula_adicional_tempo=?,"+
												      		   "lg_dependente_informado=?,"+
												      		   "qt_anos_anuidade=?,"+
												      		   "tp_deducao_falta=?,"+
												      		   "tp_categoria_fgts=?,"+
												      		   "tp_calculo_ferias=?,"+
												      		   "tp_pagamento_ferias=?,"+
												      		   "tp_adiantamento_decimo=?,"+
												      		   "vl_arredondamento=?,"+
												      		   "nm_departamento_rh=?,"+
												      		   "nm_chefe_departamento=? WHERE cd_empresa=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdFpas()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFpas());
			if(objeto.getCdTerceiros()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTerceiros());
			if(objeto.getCdTabelaEvento()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTabelaEvento());
			if(objeto.getCdFolhaPagamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFolhaPagamento());
			pstmt.setString(6,objeto.getIdFgts());
			pstmt.setString(7,objeto.getIdGps());
			pstmt.setString(8,objeto.getIdSureg());
			pstmt.setString(9,objeto.getIdSat());
			pstmt.setString(10,objeto.getIdCei());
			pstmt.setFloat(11,objeto.getPrSat());
			pstmt.setFloat(12,objeto.getPrIsencaoFilantropia());
			pstmt.setFloat(13,objeto.getPrAnuidade());
			pstmt.setFloat(14,objeto.getPrProlaboreGps());
			pstmt.setInt(15,objeto.getNrMesDissidioColetivo());
			pstmt.setInt(16,objeto.getNrMesAdiantamentoDecimo());
			pstmt.setInt(17,objeto.getNrMesAntecipacaoDecimo());
			pstmt.setInt(18,objeto.getQtAnosIntervaloLicenca());
			pstmt.setInt(19,objeto.getQtMesesLicencaPremio());
			pstmt.setInt(20,objeto.getLgPat());
			pstmt.setInt(21,objeto.getLgRecolhePis());
			pstmt.setInt(22,objeto.getLgCagedDisco());
			pstmt.setInt(23,objeto.getLgRecolheGrps());
			pstmt.setInt(24,objeto.getLgVerificaVaga());
			pstmt.setInt(25,objeto.getLgCalculaAdicionalTempo());
			pstmt.setInt(26,objeto.getLgDependenteInformado());
			pstmt.setInt(27,objeto.getQtAnosAnuidade());
			pstmt.setInt(28,objeto.getTpDeducaoFalta());
			pstmt.setInt(29,objeto.getTpCategoriaFgts());
			pstmt.setInt(30,objeto.getTpCalculoFerias());
			pstmt.setInt(31,objeto.getTpPagamentoFerias());
			pstmt.setInt(32,objeto.getTpAdiantamentoDecimo());
			pstmt.setFloat(33,objeto.getVlArredondamento());
			pstmt.setString(34,objeto.getNmDepartamentoRh());
			pstmt.setString(35,objeto.getNmChefeDepartamento());
			if (objetoTemp != null) {
				pstmt.setInt(36, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
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
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_empresa A, grl_pessoa_juridica B, grl_pessoa C, srh_empresa D WHERE A.cd_empresa=B.cd_pessoa AND B.cd_pessoa = C.cd_pessoa AND A.cd_empresa=D.cd_empresa AND A.cd_empresa=?");
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_empresa A, grl_pessoa_juridica B, grl_pessoa C, srh_empresa D WHERE A.cd_empresa=B.cd_pessoa AND B.cd_pessoa = C.cd_pessoa AND A.cd_empresa=D.cd_empresa AND A.cd_empresa=?");
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
						rs.getInt("cd_fpas"),
						rs.getInt("cd_terceiros"),
						rs.getInt("cd_tabela_evento"),
						rs.getInt("cd_folha_pagamento"),
						rs.getString("id_fgts"),
						rs.getString("id_gps"),
						rs.getString("id_sureg"),
						rs.getString("id_sat"),
						rs.getString("id_cei"),
						rs.getFloat("pr_sat"),
						rs.getFloat("pr_isencao_filantropia"),
						rs.getFloat("pr_anuidade"),
						rs.getFloat("pr_prolabore_gps"),
						rs.getInt("nr_mes_dissidio_coletivo"),
						rs.getInt("nr_mes_adiantamento_decimo"),
						rs.getInt("nr_mes_antecipacao_decimo"),
						rs.getInt("qt_anos_intervalo_licenca"),
						rs.getInt("qt_meses_licenca_premio"),
						rs.getInt("lg_pat"),
						rs.getInt("lg_recolhe_pis"),
						rs.getInt("lg_caged_disco"),
						rs.getInt("lg_recolhe_grps"),
						rs.getInt("lg_verifica_vaga"),
						rs.getInt("lg_calcula_adicional_tempo"),
						rs.getInt("lg_dependente_informado"),
						rs.getInt("qt_anos_anuidade"),
						rs.getInt("tp_deducao_falta"),
						rs.getInt("tp_categoria_fgts"),
						rs.getInt("tp_calculo_ferias"),
						rs.getInt("tp_pagamento_ferias"),
						rs.getInt("tp_adiantamento_decimo"),
						rs.getFloat("vl_arredondamento"),
						rs.getString("nm_departamento_rh"),
						rs.getString("nm_chefe_departamento"));
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_empresa");
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
		return Search.find("SELECT * FROM srh_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
