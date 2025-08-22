package com.tivic.manager.srh;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class DadosFuncionaisDAO{

	public static int insert(DadosFuncionais objeto) {
		return insert(objeto, null);
	}

	public static int insert(DadosFuncionais objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("srh_dados_funcionais", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMatricula(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_dados_funcionais (cd_matricula,"+
			                                  "cd_tabela_horario,"+
			                                  "cd_setor,"+
			                                  "cd_funcao,"+
			                                  "cd_turma,"+
			                                  "cd_empresa,"+
			                                  "cd_grupo_pagamento,"+
			                                  "cd_agente_nocivo,"+
			                                  "cd_tipo_admissao,"+
			                                  "cd_vinculo_empregaticio,"+
			                                  "cd_categoria_fgts,"+
			                                  "cd_tabela_salario,"+
			                                  "cd_pessoa,"+
			                                  "cd_conta_bancaria,"+
			                                  "tp_salario,"+
			                                  "nr_matricula,"+
			                                  "dt_matricula,"+
			                                  "dt_desligamento,"+
			                                  "nr_cartao,"+
			                                  "vl_previdencia_outra_fonte,"+
			                                  "vl_salario_contratual,"+
			                                  "qt_licencas_gozadas,"+
			                                  "qt_ferias_gozadas,"+
			                                  "dt_opcao_fgts,"+
			                                  "st_funcional,"+
			                                  "tp_status_rais,"+
			                                  "tp_provento_principal,"+
			                                  "nr_conta_fgts,"+
			                                  "cd_convenio,"+
			                                  "dt_final_contrato,"+
			                                  "tp_pagamento,"+
			                                  "cd_tipo_desligamento,"+
			                                  "cd_banco_fgts,"+
			                                  "nr_agencia_fgts,"+
			                                  "blb_biometria,"+
			                                  "qt_dependente_ir,"+
			                                  "qt_dependente_sal_fam,"+
			                                  "lg_vale_transporte,"+
			                                  "nr_horas_mes,"+
			                                  "cd_estado_ctps,"+
			                                  "nr_serie_ctps,"+
			                                  "nr_pis_pasep,"+
			                                  "nr_conselho_profissional,"+
			                                  "cd_conselho_profissional,"+
			                                  "cd_nivel_salario,"+
			                                  "tp_acesso_cargo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTabelaHorario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTabelaHorario());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetor());
			if(objeto.getCdFuncao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdFuncao());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTurma());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresa());
			if(objeto.getCdGrupoPagamento()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdGrupoPagamento());
			if(objeto.getCdAgenteNocivo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdAgenteNocivo());
			if(objeto.getCdTipoAdmissao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdTipoAdmissao());
			if(objeto.getCdVinculoEmpregaticio()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdVinculoEmpregaticio());
			if(objeto.getCdCategoriaFgts()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdCategoriaFgts());
			if(objeto.getCdTabelaSalario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdTabelaSalario());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdPessoa());
			if(objeto.getCdContaBancaria()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdContaBancaria());
			pstmt.setInt(15,objeto.getTpSalario());
			pstmt.setString(16,objeto.getNrMatricula());
			if(objeto.getDtMatricula()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtMatricula().getTimeInMillis()));
			if(objeto.getDtDesligamento()==null)
				pstmt.setNull(18, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(18,new Timestamp(objeto.getDtDesligamento().getTimeInMillis()));
			pstmt.setString(19,objeto.getNrCartao());
			pstmt.setDouble(20,objeto.getVlPrevidenciaOutraFonte());
			pstmt.setDouble(21,objeto.getVlSalarioContratual());
			pstmt.setInt(22,objeto.getQtLicencasGozadas());
			pstmt.setInt(23,objeto.getQtFeriasGozadas());
			if(objeto.getDtOpcaoFgts()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtOpcaoFgts().getTimeInMillis()));
			pstmt.setInt(25,objeto.getStFuncional());
			pstmt.setInt(26,objeto.getTpStatusRais());
			pstmt.setInt(27,objeto.getTpProventoPrincipal());
			pstmt.setString(28,objeto.getNrContaFgts());
			if(objeto.getCdConvenio()==0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdConvenio());
			if(objeto.getDtFinalContrato()==null)
				pstmt.setNull(30, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(30,new Timestamp(objeto.getDtFinalContrato().getTimeInMillis()));
			pstmt.setInt(31,objeto.getTpPagamento());
			if(objeto.getCdTipoDesligamento()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdTipoDesligamento());
			if(objeto.getCdBancoFgts()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdBancoFgts());
			pstmt.setString(34,objeto.getNrAgenciaFgts());
			if(objeto.getBlbBiometria()==null)
				pstmt.setNull(35, Types.BINARY);
			else
				pstmt.setBytes(35,objeto.getBlbBiometria());
			pstmt.setInt(36,objeto.getQtDependenteIr());
			pstmt.setInt(37,objeto.getQtDependenteSalFam());
			pstmt.setInt(38,objeto.getLgValeTransporte());
			pstmt.setInt(39,objeto.getNrHorasMes());
			if(objeto.getCdEstadoCtps()==0)
				pstmt.setNull(40, Types.INTEGER);
			else
				pstmt.setInt(40,objeto.getCdEstadoCtps());
			pstmt.setString(41,objeto.getNrSerieCtps());
			pstmt.setString(42,objeto.getNrPisPasep());
			pstmt.setString(43,objeto.getNrConselhoProfissional());
			if(objeto.getCdConselhoProfissional()==0)
				pstmt.setNull(44, Types.INTEGER);
			else
				pstmt.setInt(44,objeto.getCdConselhoProfissional());
			if(objeto.getCdNivelSalario()==0)
				pstmt.setNull(45, Types.INTEGER);
			else
				pstmt.setInt(45,objeto.getCdNivelSalario());
			pstmt.setInt(46,objeto.getTpAcessoCargo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DadosFuncionaisDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DadosFuncionaisDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DadosFuncionais objeto) {
		return update(objeto, 0, null);
	}

	public static int update(DadosFuncionais objeto, int cdMatriculaOld) {
		return update(objeto, cdMatriculaOld, null);
	}

	public static int update(DadosFuncionais objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(DadosFuncionais objeto, int cdMatriculaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_dados_funcionais SET cd_matricula=?,"+
												      		   "cd_tabela_horario=?,"+
												      		   "cd_setor=?,"+
												      		   "cd_funcao=?,"+
												      		   "cd_turma=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_grupo_pagamento=?,"+
												      		   "cd_agente_nocivo=?,"+
												      		   "cd_tipo_admissao=?,"+
												      		   "cd_vinculo_empregaticio=?,"+
												      		   "cd_categoria_fgts=?,"+
												      		   "cd_tabela_salario=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_conta_bancaria=?,"+
												      		   "tp_salario=?,"+
												      		   "nr_matricula=?,"+
												      		   "dt_matricula=?,"+
												      		   "dt_desligamento=?,"+
												      		   "nr_cartao=?,"+
												      		   "vl_previdencia_outra_fonte=?,"+
												      		   "vl_salario_contratual=?,"+
												      		   "qt_licencas_gozadas=?,"+
												      		   "qt_ferias_gozadas=?,"+
												      		   "dt_opcao_fgts=?,"+
												      		   "st_funcional=?,"+
												      		   "tp_status_rais=?,"+
												      		   "tp_provento_principal=?,"+
												      		   "nr_conta_fgts=?,"+
												      		   "cd_convenio=?,"+
												      		   "dt_final_contrato=?,"+
												      		   "tp_pagamento=?,"+
												      		   "cd_tipo_desligamento=?,"+
												      		   "cd_banco_fgts=?,"+
												      		   "nr_agencia_fgts=?,"+
												      		   "blb_biometria=?,"+
												      		   "qt_dependente_ir=?,"+
												      		   "qt_dependente_sal_fam=?,"+
												      		   "lg_vale_transporte=?,"+
												      		   "nr_horas_mes=?,"+
												      		   "cd_estado_ctps=?,"+
												      		   "nr_serie_ctps=?,"+
												      		   "nr_pis_pasep=?,"+
												      		   "nr_conselho_profissional=?,"+
												      		   "cd_conselho_profissional=?,"+
												      		   "cd_nivel_salario=?,"+
												      		   "tp_acesso_cargo=? WHERE cd_matricula=?");
			pstmt.setInt(1,objeto.getCdMatricula());
			if(objeto.getCdTabelaHorario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTabelaHorario());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetor());
			if(objeto.getCdFuncao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdFuncao());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTurma());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresa());
			if(objeto.getCdGrupoPagamento()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdGrupoPagamento());
			if(objeto.getCdAgenteNocivo()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdAgenteNocivo());
			if(objeto.getCdTipoAdmissao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdTipoAdmissao());
			if(objeto.getCdVinculoEmpregaticio()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdVinculoEmpregaticio());
			if(objeto.getCdCategoriaFgts()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdCategoriaFgts());
			if(objeto.getCdTabelaSalario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdTabelaSalario());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdPessoa());
			if(objeto.getCdContaBancaria()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdContaBancaria());
			pstmt.setInt(15,objeto.getTpSalario());
			pstmt.setString(16,objeto.getNrMatricula());
			if(objeto.getDtMatricula()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtMatricula().getTimeInMillis()));
			if(objeto.getDtDesligamento()==null)
				pstmt.setNull(18, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(18,new Timestamp(objeto.getDtDesligamento().getTimeInMillis()));
			pstmt.setString(19,objeto.getNrCartao());
			pstmt.setDouble(20,objeto.getVlPrevidenciaOutraFonte());
			pstmt.setDouble(21,objeto.getVlSalarioContratual());
			pstmt.setInt(22,objeto.getQtLicencasGozadas());
			pstmt.setInt(23,objeto.getQtFeriasGozadas());
			if(objeto.getDtOpcaoFgts()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtOpcaoFgts().getTimeInMillis()));
			pstmt.setInt(25,objeto.getStFuncional());
			pstmt.setInt(26,objeto.getTpStatusRais());
			pstmt.setInt(27,objeto.getTpProventoPrincipal());
			pstmt.setString(28,objeto.getNrContaFgts());
			if(objeto.getCdConvenio()==0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdConvenio());
			if(objeto.getDtFinalContrato()==null)
				pstmt.setNull(30, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(30,new Timestamp(objeto.getDtFinalContrato().getTimeInMillis()));
			pstmt.setInt(31,objeto.getTpPagamento());
			if(objeto.getCdTipoDesligamento()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdTipoDesligamento());
			if(objeto.getCdBancoFgts()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdBancoFgts());
			pstmt.setString(34,objeto.getNrAgenciaFgts());
			if(objeto.getBlbBiometria()==null)
				pstmt.setNull(35, Types.BINARY);
			else
				pstmt.setBytes(35,objeto.getBlbBiometria());
			pstmt.setInt(36,objeto.getQtDependenteIr());
			pstmt.setInt(37,objeto.getQtDependenteSalFam());
			pstmt.setInt(38,objeto.getLgValeTransporte());
			pstmt.setInt(39,objeto.getNrHorasMes());
			if(objeto.getCdEstadoCtps()==0)
				pstmt.setNull(40, Types.INTEGER);
			else
				pstmt.setInt(40,objeto.getCdEstadoCtps());
			pstmt.setString(41,objeto.getNrSerieCtps());
			pstmt.setString(42,objeto.getNrPisPasep());
			pstmt.setString(43,objeto.getNrConselhoProfissional());
			if(objeto.getCdConselhoProfissional()==0)
				pstmt.setNull(44, Types.INTEGER);
			else
				pstmt.setInt(44,objeto.getCdConselhoProfissional());
			if(objeto.getCdNivelSalario()==0)
				pstmt.setNull(45, Types.INTEGER);
			else
				pstmt.setInt(45,objeto.getCdNivelSalario());
			pstmt.setInt(46,objeto.getTpAcessoCargo());
			pstmt.setInt(47, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DadosFuncionaisDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DadosFuncionaisDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula) {
		return delete(cdMatricula, null);
	}

	public static int delete(int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_dados_funcionais WHERE cd_matricula=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DadosFuncionaisDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DadosFuncionaisDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DadosFuncionais get(int cdMatricula) {
		return get(cdMatricula, null);
	}

	public static DadosFuncionais get(int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_dados_funcionais WHERE cd_matricula=?");
			pstmt.setInt(1, cdMatricula);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DadosFuncionais(rs.getInt("cd_matricula"),
						rs.getInt("cd_tabela_horario"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_funcao"),
						rs.getInt("cd_turma"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_grupo_pagamento"),
						rs.getInt("cd_agente_nocivo"),
						rs.getInt("cd_tipo_admissao"),
						rs.getInt("cd_vinculo_empregaticio"),
						rs.getInt("cd_categoria_fgts"),
						rs.getInt("cd_tabela_salario"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_conta_bancaria"),
						rs.getInt("tp_salario"),
						rs.getString("nr_matricula"),
						(rs.getTimestamp("dt_matricula")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_matricula").getTime()),
						(rs.getTimestamp("dt_desligamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_desligamento").getTime()),
						rs.getString("nr_cartao"),
						rs.getFloat("vl_previdencia_outra_fonte"),
						rs.getFloat("vl_salario_contratual"),
						rs.getInt("qt_licencas_gozadas"),
						rs.getInt("qt_ferias_gozadas"),
						(rs.getTimestamp("dt_opcao_fgts")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_opcao_fgts").getTime()),
						rs.getInt("st_funcional"),
						rs.getInt("tp_status_rais"),
						rs.getInt("tp_provento_principal"),
						rs.getString("nr_conta_fgts"),
						rs.getInt("cd_convenio"),
						(rs.getTimestamp("dt_final_contrato")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_contrato").getTime()),
						rs.getInt("tp_pagamento"),
						rs.getInt("cd_tipo_desligamento"),
						rs.getInt("cd_banco_fgts"),
						rs.getString("nr_agencia_fgts"),
						rs.getBytes("blb_biometria")==null?null:rs.getBytes("blb_biometria"),
						rs.getInt("qt_dependente_ir"),
						rs.getInt("qt_dependente_sal_fam"),
						rs.getInt("lg_vale_transporte"),
						rs.getInt("nr_horas_mes"),
						rs.getInt("cd_estado_ctps"),
						rs.getString("nr_serie_ctps"),
						rs.getString("nr_pis_pasep"),
						rs.getString("nr_conselho_profissional"),
						rs.getInt("cd_conselho_profissional"),
						rs.getInt("cd_nivel_salario"),
						rs.getInt("tp_acesso_cargo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DadosFuncionaisDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DadosFuncionaisDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_dados_funcionais");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DadosFuncionaisDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DadosFuncionaisDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DadosFuncionais> getList() {
		return getList(null);
	}

	public static ArrayList<DadosFuncionais> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DadosFuncionais> list = new ArrayList<DadosFuncionais>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DadosFuncionais obj = DadosFuncionaisDAO.get(rsm.getInt("cd_matricula"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DadosFuncionaisDAO.getList: " + e);
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
		return Search.find("SELECT * FROM srh_dados_funcionais", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}