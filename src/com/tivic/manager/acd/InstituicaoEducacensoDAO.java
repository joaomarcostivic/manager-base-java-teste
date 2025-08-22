package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class InstituicaoEducacensoDAO{

	public static int insert(InstituicaoEducacenso objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoEducacenso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_educacenso (cd_instituicao,"+
			                                  "nr_inep,"+
			                                  "nr_orgao_regional,"+
			                                  "nm_orgao_regional,"+
			                                  "tp_dependencia_administrativa,"+
			                                  "tp_localizacao,"+
			                                  "nr_cnpj_executora,"+
			                                  "tp_categoria_privada,"+
			                                  "tp_convenio,"+
			                                  "nr_cnas,"+
			                                  "nr_cebas,"+
			                                  "st_regulamentacao,"+
			                                  "lg_predio_compartilhado,"+
			                                  "st_agua_consumida,"+
			                                  "tp_abastecimento_agua,"+
			                                  "tp_fornecimento_energia,"+
			                                  "tp_esgoto_sanitario,"+
			                                  "tp_destino_lixo,"+
			                                  "qt_sala_aula,"+
			                                  "qt_sala_aula_externa,"+
			                                  "qt_computador_administrativo,"+
			                                  "qt_computador_aluno,"+
			                                  "lg_internet,"+
			                                  "lg_banda_larga,"+
			                                  "lg_alimentacao_escolar,"+
			                                  "tp_atendimento_especializado,"+
			                                  "tp_atividade_complementar,"+
			                                  "tp_modalidade_ensino,"+
			                                  "lg_ensino_fundamental_ciclo,"+
			                                  "tp_educacao_infantil,"+
			                                  "nr_anos_ensino_fundalmental,"+
			                                  "tp_ensino_medio,"+
			                                  "tp_educacao_jovem_adulto,"+
			                                  "tp_localizacao_diferenciada,"+
			                                  "tp_material_especifico,"+
			                                  "lg_educacao_indigena,"+
			                                  "tp_lingua,"+
			                                  "nr_lingua_indigena,"+
			                                  "st_instituicao_publica,"+
			                                  "lg_material_didatico_indigena,"+
			                                  "lg_material_didatico_quilombola,"+
			                                  "lg_brasil_alfabetizado,"+
			                                  "lg_espaco_comunidade,"+
			                                  "lg_formacao_alternancia,"+
			                                  "tp_forma_ocupacao,"+
			                                  "lg_modalidade_regular,"+
			                                  "lg_modalidade_especial,"+
			                                  "lg_modalidade_eja,"+
			                                  "cd_lingua_indigena,"+
			                                  "lg_lingua_ministrada_indigena,"+
			                                  "lg_lingua_ministrada_portuguesa,"+
			                                  "cd_periodo_letivo,"+
			                                  "qt_total_funcionarios,"+
			                                  "nr_codigo_escola_compartilhada1,"+
			                                  "nr_codigo_escola_compartilhada2,"+
			                                  "nr_codigo_escola_compartilhada3,"+
			                                  "nr_codigo_escola_compartilhada4,"+
			                                  "nr_codigo_escola_compartilhada5,"+
			                                  "nr_codigo_escola_compartilhada6,"+
			                                  "qt_sala_aula_climatizada,"+
			                                  "qt_sala_aula_acessibilidade,"+
			                                  "lg_site,"+
			                                  "lg_entorno,"+
			                                  "lg_projeto_politico_pedagogico) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setString(2,objeto.getNrInep());
			pstmt.setString(3,objeto.getNrOrgaoRegional());
			pstmt.setString(4,objeto.getNmOrgaoRegional());
			pstmt.setInt(5,objeto.getTpDependenciaAdministrativa());
			pstmt.setInt(6,objeto.getTpLocalizacao());
			pstmt.setString(7,objeto.getNrCnpjExecutora());
			pstmt.setInt(8,objeto.getTpCategoriaPrivada());
			pstmt.setInt(9,objeto.getTpConvenio());
			pstmt.setString(10,objeto.getNrCnas());
			pstmt.setString(11,objeto.getNrCebas());
			pstmt.setInt(12,objeto.getStRegulamentacao());
			pstmt.setInt(13,objeto.getLgPredioCompartilhado());
			pstmt.setInt(14,objeto.getStAguaConsumida());
			pstmt.setInt(15,objeto.getTpAbastecimentoAgua());
			pstmt.setInt(16,objeto.getTpFornecimentoEnergia());
			pstmt.setInt(17,objeto.getTpEsgotoSanitario());
			pstmt.setInt(18,objeto.getTpDestinoLixo());
			pstmt.setInt(19,objeto.getQtSalaAula());
			pstmt.setInt(20,objeto.getQtSalaAulaExterna());
			pstmt.setInt(21,objeto.getQtComputadorAdministrativo());
			pstmt.setInt(22,objeto.getQtComputadorAluno());
			pstmt.setInt(23,objeto.getLgInternet());
			pstmt.setInt(24,objeto.getLgBandaLarga());
			pstmt.setInt(25,objeto.getLgAlimentacaoEscolar());
			pstmt.setInt(26,objeto.getTpAtendimentoEspecializado());
			pstmt.setInt(27,objeto.getTpAtividadeComplementar());
			pstmt.setInt(28,objeto.getTpModalidadeEnsino());
			pstmt.setInt(29,objeto.getLgEnsinoFundamentalCiclo());
			pstmt.setInt(30,objeto.getTpEducacaoInfantil());
			pstmt.setInt(31,objeto.getNrAnosEnsinoFundalmental());
			pstmt.setInt(32,objeto.getTpEnsinoMedio());
			pstmt.setInt(33,objeto.getTpEducacaoJovemAdulto());
			pstmt.setInt(34,objeto.getTpLocalizacaoDiferenciada());
			pstmt.setInt(35,objeto.getTpMaterialEspecifico());
			pstmt.setInt(36,objeto.getLgEducacaoIndigena());
			pstmt.setInt(37,objeto.getTpLingua());
			pstmt.setString(38,objeto.getNrLinguaIndigena());
			pstmt.setInt(39,objeto.getStInstituicaoPublica());
			pstmt.setInt(40,objeto.getLgMaterialDidaticoIndigena());
			pstmt.setInt(41,objeto.getLgMaterialDidaticoQuilombola());
			pstmt.setInt(42,objeto.getLgBrasilAlfabetizado());
			pstmt.setInt(43,objeto.getLgEspacoComunidade());
			pstmt.setInt(44,objeto.getLgFormacaoAlternancia());
			pstmt.setInt(45,objeto.getTpFormaOcupacao());
			pstmt.setInt(46,objeto.getLgModalidadeRegular());
			pstmt.setInt(47,objeto.getLgModalidadeEspecial());
			pstmt.setInt(48,objeto.getLgModalidadeEja());
			if(objeto.getCdLinguaIndigena()==0)
				pstmt.setNull(49, Types.INTEGER);
			else
				pstmt.setInt(49,objeto.getCdLinguaIndigena());
			pstmt.setInt(50,objeto.getLgLinguaMinistradaIndigena());
			pstmt.setInt(51,objeto.getLgLinguaMinistradaPortuguesa());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(52, Types.INTEGER);
			else
				pstmt.setInt(52,objeto.getCdPeriodoLetivo());
			pstmt.setInt(53,objeto.getQtTotalFuncionarios());
			pstmt.setString(54,objeto.getNrCodigoEscolaCompartilhada1());
			pstmt.setString(55,objeto.getNrCodigoEscolaCompartilhada2());
			pstmt.setString(56,objeto.getNrCodigoEscolaCompartilhada3());
			pstmt.setString(57,objeto.getNrCodigoEscolaCompartilhada4());
			pstmt.setString(58,objeto.getNrCodigoEscolaCompartilhada5());
			pstmt.setString(59,objeto.getNrCodigoEscolaCompartilhada6());
			pstmt.setInt(60,objeto.getQtSalaAulaClimatizada());
			pstmt.setInt(61,objeto.getQtSalaAulaAcessibilidade());
			pstmt.setInt(62,objeto.getLgSite());
			pstmt.setInt(63,objeto.getLgEntorno());
			pstmt.setInt(64,objeto.getLgProjetoPoliticoPedagogico());
			
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoEducacenso objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(InstituicaoEducacenso objeto, int cdInstituicaoOld, int cdPeriodoLetivoOld) {
		return update(objeto, cdInstituicaoOld, cdPeriodoLetivoOld, null);
	}

	public static int update(InstituicaoEducacenso objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(InstituicaoEducacenso objeto, int cdInstituicaoOld, int cdPeriodoLetivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_educacenso SET cd_instituicao=?,"+
												      		   "nr_inep=?,"+
												      		   "nr_orgao_regional=?,"+
												      		   "nm_orgao_regional=?,"+
												      		   "tp_dependencia_administrativa=?,"+
												      		   "tp_localizacao=?,"+
												      		   "nr_cnpj_executora=?,"+
												      		   "tp_categoria_privada=?,"+
												      		   "tp_convenio=?,"+
												      		   "nr_cnas=?,"+
												      		   "nr_cebas=?,"+
												      		   "st_regulamentacao=?,"+
												      		   "lg_predio_compartilhado=?,"+
												      		   "st_agua_consumida=?,"+
												      		   "tp_abastecimento_agua=?,"+
												      		   "tp_fornecimento_energia=?,"+
												      		   "tp_esgoto_sanitario=?,"+
												      		   "tp_destino_lixo=?,"+
												      		   "qt_sala_aula=?,"+
												      		   "qt_sala_aula_externa=?,"+
												      		   "qt_computador_administrativo=?,"+
												      		   "qt_computador_aluno=?,"+
												      		   "lg_internet=?,"+
												      		   "lg_banda_larga=?,"+
												      		   "lg_alimentacao_escolar=?,"+
												      		   "tp_atendimento_especializado=?,"+
												      		   "tp_atividade_complementar=?,"+
												      		   "tp_modalidade_ensino=?,"+
												      		   "lg_ensino_fundamental_ciclo=?,"+
												      		   "tp_educacao_infantil=?,"+
												      		   "nr_anos_ensino_fundalmental=?,"+
												      		   "tp_ensino_medio=?,"+
												      		   "tp_educacao_jovem_adulto=?,"+
												      		   "tp_localizacao_diferenciada=?,"+
												      		   "tp_material_especifico=?,"+
												      		   "lg_educacao_indigena=?,"+
												      		   "tp_lingua=?,"+
												      		   "nr_lingua_indigena=?,"+
												      		   "st_instituicao_publica=?,"+
												      		   "lg_material_didatico_indigena=?,"+
												      		   "lg_material_didatico_quilombola=?,"+
												      		   "lg_brasil_alfabetizado=?,"+
												      		   "lg_espaco_comunidade=?,"+
												      		   "lg_formacao_alternancia=?,"+
												      		   "tp_forma_ocupacao=?,"+
												      		   "lg_modalidade_regular=?,"+
												      		   "lg_modalidade_especial=?,"+
												      		   "lg_modalidade_eja=?,"+
												      		   "cd_lingua_indigena=?,"+
												      		   "lg_lingua_ministrada_indigena=?,"+
												      		   "lg_lingua_ministrada_portuguesa=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "qt_total_funcionarios=?,"+
												      		   "nr_codigo_escola_compartilhada1=?,"+
												      		   "nr_codigo_escola_compartilhada2=?,"+
												      		   "nr_codigo_escola_compartilhada3=?,"+
												      		   "nr_codigo_escola_compartilhada4=?,"+
												      		   "nr_codigo_escola_compartilhada5=?,"+
												      		   "nr_codigo_escola_compartilhada6=?,"+
												      		   "qt_sala_aula_climatizada=?,"+
												      		   "qt_sala_aula_acessibilidade=?,"+
												      		   "lg_site=?,"+
												      		   "lg_entorno=?,"+
												      		   "lg_projeto_politico_pedagogico=? WHERE cd_instituicao=? AND cd_periodo_letivo=?");
			pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setString(2,objeto.getNrInep());
			pstmt.setString(3,objeto.getNrOrgaoRegional());
			pstmt.setString(4,objeto.getNmOrgaoRegional());
			pstmt.setInt(5,objeto.getTpDependenciaAdministrativa());
			pstmt.setInt(6,objeto.getTpLocalizacao());
			pstmt.setString(7,objeto.getNrCnpjExecutora());
			pstmt.setInt(8,objeto.getTpCategoriaPrivada());
			pstmt.setInt(9,objeto.getTpConvenio());
			pstmt.setString(10,objeto.getNrCnas());
			pstmt.setString(11,objeto.getNrCebas());
			pstmt.setInt(12,objeto.getStRegulamentacao());
			pstmt.setInt(13,objeto.getLgPredioCompartilhado());
			pstmt.setInt(14,objeto.getStAguaConsumida());
			pstmt.setInt(15,objeto.getTpAbastecimentoAgua());
			pstmt.setInt(16,objeto.getTpFornecimentoEnergia());
			pstmt.setInt(17,objeto.getTpEsgotoSanitario());
			pstmt.setInt(18,objeto.getTpDestinoLixo());
			pstmt.setInt(19,objeto.getQtSalaAula());
			pstmt.setInt(20,objeto.getQtSalaAulaExterna());
			pstmt.setInt(21,objeto.getQtComputadorAdministrativo());
			pstmt.setInt(22,objeto.getQtComputadorAluno());
			pstmt.setInt(23,objeto.getLgInternet());
			pstmt.setInt(24,objeto.getLgBandaLarga());
			pstmt.setInt(25,objeto.getLgAlimentacaoEscolar());
			pstmt.setInt(26,objeto.getTpAtendimentoEspecializado());
			pstmt.setInt(27,objeto.getTpAtividadeComplementar());
			pstmt.setInt(28,objeto.getTpModalidadeEnsino());
			pstmt.setInt(29,objeto.getLgEnsinoFundamentalCiclo());
			pstmt.setInt(30,objeto.getTpEducacaoInfantil());
			pstmt.setInt(31,objeto.getNrAnosEnsinoFundalmental());
			pstmt.setInt(32,objeto.getTpEnsinoMedio());
			pstmt.setInt(33,objeto.getTpEducacaoJovemAdulto());
			pstmt.setInt(34,objeto.getTpLocalizacaoDiferenciada());
			pstmt.setInt(35,objeto.getTpMaterialEspecifico());
			pstmt.setInt(36,objeto.getLgEducacaoIndigena());
			pstmt.setInt(37,objeto.getTpLingua());
			pstmt.setString(38,objeto.getNrLinguaIndigena());
			pstmt.setInt(39,objeto.getStInstituicaoPublica());
			pstmt.setInt(40,objeto.getLgMaterialDidaticoIndigena());
			pstmt.setInt(41,objeto.getLgMaterialDidaticoQuilombola());
			pstmt.setInt(42,objeto.getLgBrasilAlfabetizado());
			pstmt.setInt(43,objeto.getLgEspacoComunidade());
			pstmt.setInt(44,objeto.getLgFormacaoAlternancia());
			pstmt.setInt(45,objeto.getTpFormaOcupacao());
			pstmt.setInt(46,objeto.getLgModalidadeRegular());
			pstmt.setInt(47,objeto.getLgModalidadeEspecial());
			pstmt.setInt(48,objeto.getLgModalidadeEja());
			if(objeto.getCdLinguaIndigena()==0)
				pstmt.setNull(49, Types.INTEGER);
			else
				pstmt.setInt(49,objeto.getCdLinguaIndigena());
			pstmt.setInt(50,objeto.getLgLinguaMinistradaIndigena());
			pstmt.setInt(51,objeto.getLgLinguaMinistradaPortuguesa());
			pstmt.setInt(52,objeto.getCdPeriodoLetivo());
			pstmt.setInt(53,objeto.getQtTotalFuncionarios());
			pstmt.setString(54,objeto.getNrCodigoEscolaCompartilhada1());
			pstmt.setString(55,objeto.getNrCodigoEscolaCompartilhada2());
			pstmt.setString(56,objeto.getNrCodigoEscolaCompartilhada3());
			pstmt.setString(57,objeto.getNrCodigoEscolaCompartilhada4());
			pstmt.setString(58,objeto.getNrCodigoEscolaCompartilhada5());
			pstmt.setString(59,objeto.getNrCodigoEscolaCompartilhada6());
			pstmt.setInt(60,objeto.getQtSalaAulaClimatizada());
			pstmt.setInt(61,objeto.getQtSalaAulaAcessibilidade());
			pstmt.setInt(62,objeto.getLgSite());
			pstmt.setInt(63,objeto.getLgEntorno());
			pstmt.setInt(64,objeto.getLgProjetoPoliticoPedagogico());
			pstmt.setInt(65, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(66, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, int cdPeriodoLetivo) {
		return delete(cdInstituicao, cdPeriodoLetivo, null);
	}

	public static int delete(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_educacenso WHERE cd_instituicao=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdPeriodoLetivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoEducacenso get(int cdInstituicao, int cdPeriodoLetivo) {
		return get(cdInstituicao, cdPeriodoLetivo, null);
	}

	public static InstituicaoEducacenso get(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_educacenso WHERE cd_instituicao=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoEducacenso(rs.getInt("cd_instituicao"),
						rs.getString("nr_inep"),
						rs.getString("nr_orgao_regional"),
						rs.getString("nm_orgao_regional"),
						rs.getInt("tp_dependencia_administrativa"),
						rs.getInt("tp_localizacao"),
						rs.getString("nr_cnpj_executora"),
						rs.getInt("tp_categoria_privada"),
						rs.getInt("tp_convenio"),
						rs.getString("nr_cnas"),
						rs.getString("nr_cebas"),
						rs.getInt("st_regulamentacao"),
						rs.getInt("lg_predio_compartilhado"),
						rs.getInt("st_agua_consumida"),
						rs.getInt("tp_abastecimento_agua"),
						rs.getInt("tp_fornecimento_energia"),
						rs.getInt("tp_esgoto_sanitario"),
						rs.getInt("tp_destino_lixo"),
						rs.getInt("qt_sala_aula"),
						rs.getInt("qt_sala_aula_externa"),
						rs.getInt("qt_computador_administrativo"),
						rs.getInt("qt_computador_aluno"),
						rs.getInt("lg_internet"),
						rs.getInt("lg_banda_larga"),
						rs.getInt("lg_alimentacao_escolar"),
						rs.getInt("tp_atendimento_especializado"),
						rs.getInt("tp_atividade_complementar"),
						rs.getInt("tp_modalidade_ensino"),
						rs.getInt("lg_ensino_fundamental_ciclo"),
						rs.getInt("tp_educacao_infantil"),
						rs.getInt("nr_anos_ensino_fundalmental"),
						rs.getInt("tp_ensino_medio"),
						rs.getInt("tp_educacao_jovem_adulto"),
						rs.getInt("tp_localizacao_diferenciada"),
						rs.getInt("tp_material_especifico"),
						rs.getInt("lg_educacao_indigena"),
						rs.getInt("tp_lingua"),
						rs.getString("nr_lingua_indigena"),
						rs.getInt("st_instituicao_publica"),
						rs.getInt("lg_material_didatico_indigena"),
						rs.getInt("lg_material_didatico_quilombola"),
						rs.getInt("lg_brasil_alfabetizado"),
						rs.getInt("lg_espaco_comunidade"),
						rs.getInt("lg_formacao_alternancia"),
						rs.getInt("tp_forma_ocupacao"),
						rs.getInt("lg_modalidade_regular"),
						rs.getInt("lg_modalidade_especial"),
						rs.getInt("lg_modalidade_eja"),
						rs.getInt("cd_lingua_indigena"),
						rs.getInt("lg_lingua_ministrada_indigena"),
						rs.getInt("lg_lingua_ministrada_portuguesa"),
						rs.getInt("cd_periodo_letivo"),
						rs.getInt("qt_total_funcionarios"),
						rs.getString("nr_codigo_escola_compartilhada1"),
						rs.getString("nr_codigo_escola_compartilhada2"),
						rs.getString("nr_codigo_escola_compartilhada3"),
						rs.getString("nr_codigo_escola_compartilhada4"),
						rs.getString("nr_codigo_escola_compartilhada5"),
						rs.getString("nr_codigo_escola_compartilhada6"),
						rs.getInt("qt_sala_aula_climatizada"),
						rs.getInt("qt_sala_aula_acessibilidade"),
						rs.getInt("lg_site"),
						rs.getInt("lg_entorno"),
						rs.getInt("lg_projeto_politico_pedagogico"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_educacenso");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoEducacenso> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoEducacenso> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoEducacenso> list = new ArrayList<InstituicaoEducacenso>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoEducacenso obj = InstituicaoEducacensoDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_periodo_letivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_educacenso", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}