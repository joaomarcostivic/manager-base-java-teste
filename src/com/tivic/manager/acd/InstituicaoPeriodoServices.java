package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.Validator;
import com.tivic.manager.util.ValidatorResult;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class InstituicaoPeriodoServices {

	/* situações de período letivo */
	public static final int ST_ATUAL = 0;
	public static final int ST_CONCLUIDO = 1;
	public static final int ST_PENDENTE = 2;
	
	public static final String[] situacoes = {"Atual (em curso)", 
		"Concluído",
		"Pendente"};
	
	//Grupos de Validação
	public static final int GRUPO_VALIDACAO_INSERT = 0;
	public static final int GRUPO_VALIDACAO_UPDATE = 1;
	public static final int GRUPO_VALIDACAO_EDUCACENSO = 2;
	//Validações
	public static final int VALIDATE_NOME = 0;
	public static final int VALIDATE_HORARIOS = 1;
	public static final int VALIDATE_DATA_INICIO_PERIODO = 2;
	public static final int VALIDATE_DATA_TERMINO_PERIODO = 3;
	
	
	public static Result save(InstituicaoPeriodo instituicaoPeriodo, int cdUsuario){
		return save(instituicaoPeriodo, cdUsuario, null);
	}

	public static Result save(InstituicaoPeriodo instituicaoPeriodo, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoPeriodo==null)
				return new Result(-1, "Erro ao salvar. InstituicaoPeriodo é nulo");
			
			int retorno;
			if(instituicaoPeriodo.getCdPeriodoLetivo()==0){
//				ValidatorResult resultadoValidacao = validate(instituicaoPeriodo, cdUsuario, GRUPO_VALIDACAO_INSERT, connect);
//				if(!resultadoValidacao.hasPassed()){
//					return resultadoValidacao;
//				}
				
				retorno = InstituicaoPeriodoDAO.insert(instituicaoPeriodo, connect);
				instituicaoPeriodo.setCdPeriodoLetivo(retorno);
				
				int cdTipoOcorrenciaCadastroInstituicao = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_PERIODO_LETIVO, connect).getCdTipoOcorrencia();
				OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, instituicaoPeriodo.getCdInstituicao(), "Periodo de "+instituicaoPeriodo.getNmPeriodoLetivo()+" da Instituicao " + InstituicaoDAO.get(instituicaoPeriodo.getCdInstituicao(), connect).getNmPessoa() + " cadastrado", Util.getDataAtual(), cdTipoOcorrenciaCadastroInstituicao, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicaoPeriodo.getCdInstituicao(), Util.getDataAtual(), cdUsuario, instituicaoPeriodo.getCdPeriodoLetivo());
				Result ret = OcorrenciaInstituicaoServices.save(ocorrencia, connect);
				if(ret.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return ret;
				}
				
			}
			else {
				ValidatorResult resultadoValidacao = validate(instituicaoPeriodo, cdUsuario, GRUPO_VALIDACAO_UPDATE, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
				
				retorno = InstituicaoPeriodoDAO.update(instituicaoPeriodo, connect);
				
				int cdTipoOcorrenciaCadastroInstituicao = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_PERIODO_LETIVO, connect).getCdTipoOcorrencia();
				OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, instituicaoPeriodo.getCdInstituicao(), "Periodo de "+instituicaoPeriodo.getNmPeriodoLetivo()+" da Instituicao " + InstituicaoDAO.get(instituicaoPeriodo.getCdInstituicao(), connect).getNmPessoa() + " alterado", Util.getDataAtual(), cdTipoOcorrenciaCadastroInstituicao, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicaoPeriodo.getCdInstituicao(), Util.getDataAtual(), cdUsuario, instituicaoPeriodo.getCdPeriodoLetivo());
				Result ret = OcorrenciaInstituicaoServices.save(ocorrencia, connect);
				if(ret.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return ret;
				}
				
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOPERIODO", instituicaoPeriodo);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdPeriodoLetivo){
		return remove(cdPeriodoLetivo, 0, false, null);
	}
	public static Result remove(int cdPeriodoLetivo, int cdUsuario, Connection connect){
		return remove(cdPeriodoLetivo, cdUsuario, false, connect);
	}
	public static Result remove(int cdPeriodoLetivo, int cdUsuario, boolean cascade){
		return remove(cdPeriodoLetivo, cdUsuario, cascade, null);
	}
	public static Result remove(int cdPeriodoLetivo, int cdUsuario, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
			if(instituicaoPeriodo.getStPeriodoLetivo() != ST_PENDENTE){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Apenas períodos letivos PENDENTES podem ser removidos");
			}
			
			int cdInstituicao = 0;
			ResultSetMap rsmInstituicaoAtual = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery());
			if(rsmInstituicaoAtual.next())
				cdInstituicao = rsmInstituicaoAtual.getInt("cd_instituicao");
			
			//Faz a busca das instituicoes dependentes
			ResultSetMap rsmInstituicoesDependentes = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa WHERE cd_pessoa_superior = " + cdInstituicao).executeQuery());
			while(rsmInstituicoesDependentes.next()){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_instituicao", rsmInstituicoesDependentes.getString("cd_pessoa"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("st_periodo_letivo", "" + InstituicaoPeriodoServices.ST_PENDENTE, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_periodo", "" + ParametroServices.getValorOfParametro("CD_TIPO_PERIODO_LETIVO", 0), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPeriodoLetivoAtual = InstituicaoPeriodoDAO.find(criterios, connect);
				if(rsmPeriodoLetivoAtual.next()){
					Result resultadoTemp = remove(rsmPeriodoLetivoAtual.getInt("cd_periodo_letivo"), cdUsuario, cascade, connect);
					if(resultadoTemp.getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return resultadoTemp;
					}
					
				}
			}
			
			
			int retorno = 0;
			if(cascade){
				
				retorno = InstituicaoCursoServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = InstituicaoHorarioServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = InstituicaoAbastecimentoAguaServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = InstituicaoAbastecimentoEnergiaServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = InstituicaoDestinacaoLixoServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = InstituicaoEsgotoSanitarioServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = InstituicaoDependenciaServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = InstituicaoLocalFuncionamentoServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = InstituicaoTipoEquipamentoServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = InstituicaoTipoEtapaServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = InstituicaoTipoMantenedoraServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = OcorrenciaInstituicaoServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = InstituicaoEducacensoServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
				retorno = QuadroVagasServices.removeByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, connect).getCode();
				if(retorno < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1);
				}
				
			}
			
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
			
			if(!cascade || retorno>0){
				retorno = InstituicaoPeriodoDAO.delete(cdPeriodoLetivo, connect);
			}
			
			int cdTipoOcorrenciaRemocaoPeriodoLetivo = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMOVER_PERIODO_LETIVO, connect).getCdTipoOcorrencia();
			
			OcorrenciaInstituicao ocorrenciaInstituicao = new OcorrenciaInstituicao(0/*cdOcorrencia*/, instituicaoPeriodo.getCdInstituicao(), "Remoção do período letivo de " + instituicaoPeriodo.getNmPeriodoLetivo() + " para a instituição " + instituicao.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrenciaRemocaoPeriodoLetivo, OcorrenciaServices.ST_CONCLUIDO, 1, cdUsuario, instituicao.getCdInstituicao(), Util.getDataAtual(), cdUsuario, instituicaoPeriodo.getCdPeriodoLetivo());
			if(OcorrenciaInstituicaoDAO.insert(ocorrenciaInstituicao, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir ocorrência");
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_periodo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getAllPrincipalAtual() {
		ResultSetMap rsm =  getAllPrincipal(null);
		rsm.next();
		return rsm.getString("nm_periodo_letivo");
	}
	
	public static ResultSetMap getAllPrincipal() {
		return getAllPrincipal(null);
	}

	public static ResultSetMap getAllPrincipal(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + cdSecretaria + " ORDER BY nm_periodo_letivo DESC");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllPrincipalAtualRecente() {
		return getAllPrincipalAtualRecente(null);
	}

	public static ResultSetMap getAllPrincipalAtualRecente(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + cdSecretaria + " AND st_periodo_letivo IN ("+ST_ATUAL+", "+ST_PENDENTE+") ORDER BY nm_periodo_letivo DESC");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findRecente(ArrayList<ItemComparator> criterios) {
		
		ArrayList<ItemComparator> crt = (ArrayList<ItemComparator>)criterios.clone();
		
		crt.add(new ItemComparator("A.st_periodo_letivo", "" + InstituicaoPeriodoServices.ST_PENDENTE, ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsm = find(crt, null);
		
		if(rsm.size() == 0){
			crt = (ArrayList<ItemComparator>)criterios.clone();
			crt.add(new ItemComparator("A.st_periodo_letivo", "" + InstituicaoPeriodoServices.ST_ATUAL, ItemComparator.EQUAL, Types.INTEGER));
			return find(crt, null);
		}
		
		return rsm;
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO")){
				int cdInstituicao = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
				int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
				if(cdInstituicao != cdSecretaria)
					crt.add(criterios.get(i));
			}
			else
				crt.add(criterios.get(i));
				
		}
		
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		
		String sql = " SELECT A.*, B.*, C.*, C.nm_pessoa AS nm_instituicao, D.nm_periodo_letivo AS nm_periodo_letivo_superior, F.nm_pessoa AS nm_instituicao_periodo_superior FROM acd_instituicao_periodo A "
				+ "		 JOIN acd_instituicao B ON (A.cd_instituicao = B.cd_instituicao) "
				+ "		 JOIN grl_pessoa      C ON (A.cd_instituicao = C.cd_pessoa) "
				+ "		 LEFT OUTER JOIN acd_instituicao_periodo D ON (A.cd_periodo_letivo_superior = D.cd_periodo_letivo)"
				+ "		 LEFT OUTER JOIN acd_instituicao E ON (D.cd_instituicao = E.cd_instituicao)"
				+ "		 LEFT OUTER JOIN grl_pessoa F ON (E.cd_instituicao = F.cd_pessoa)" +limit;
		
		ResultSetMap rsm = Search.find(sql, "", crt, connect!=null ? connect : Conexao.conectar(), connect==null);
		while(rsm.next()){
			if(rsm.getInt("cd_periodo_letivo_superior") > 0)
				rsm.setValueToField("NM_PERIODO_LETIVO_SUPERIOR", rsm.getString("nm_periodo_letivo_superior") + " - " + rsm.getString("nm_instituicao_periodo_superior"));
			else
				rsm.setValueToField("NM_PERIODO_LETIVO_SUPERIOR", "");
		}
		rsm.beforeFirst();
		
		return rsm;
	}

	public static ResultSetMap getPeriodoAtualOfInstituicao(int cdInstituicao) {
		return getPeriodoAtualOfInstituicao(cdInstituicao, null);
	}

	public static ResultSetMap getPeriodoAtualOfInstituicao(int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rsm;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.*, C.nm_pessoa AS nm_instituicao, D.nm_periodo_letivo AS nm_periodo_letivo_superior, F.nm_pessoa AS nm_instituicao_periodo_superior FROM acd_instituicao_periodo A "
				+ "		 JOIN acd_instituicao B ON (A.cd_instituicao = B.cd_instituicao) "
				+ "		 JOIN grl_pessoa      C ON (A.cd_instituicao = C.cd_pessoa) " 
				+ "		 LEFT OUTER JOIN acd_instituicao_periodo D ON (A.cd_periodo_letivo_superior = D.cd_periodo_letivo)"
				+ "		 LEFT OUTER JOIN acd_instituicao E ON (D.cd_instituicao = E.cd_instituicao)"
				+ "		 LEFT OUTER JOIN grl_pessoa F ON (E.cd_instituicao = F.cd_pessoa)"
				+ "	  WHERE A.cd_instituicao=? AND A.st_periodo_letivo = " + ST_ATUAL);
			pstmt.setInt(1, cdInstituicao);
			rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				if(rsm.getInt("cd_periodo_letivo_superior") > 0)
					rsm.setValueToField("NM_PERIODO_LETIVO_SUPERIOR", rsm.getString("nm_periodo_letivo_superior") + " - " + rsm.getString("nm_instituicao_periodo_superior"));
				else
					rsm.setValueToField("NM_PERIODO_LETIVO_SUPERIOR", "");
			}
			rsm.beforeFirst();
			
			return rsm;
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static InstituicaoPeriodo getPeriodoAtualOfSecretaria() {
		return getPeriodoAtualOfSecretaria(null);
	}

	public static InstituicaoPeriodo getPeriodoAtualOfSecretaria(Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			ResultSetMap rsmPeriodoAtualSecretaria = InstituicaoServices.getPeriodoLetivoVigente(cdSecretaria, connect);
			InstituicaoPeriodo instituicaoPeriodoAtualSecretaria = null;
			if(rsmPeriodoAtualSecretaria.next()){
				instituicaoPeriodoAtualSecretaria = InstituicaoPeriodoDAO.get(rsmPeriodoAtualSecretaria.getInt("cd_periodo_letivo"), connect);
			}
			return instituicaoPeriodoAtualSecretaria;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoServices.getPeriodoAtualOfSecretaria: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoPeriodo getPeriodoRecenteOfSecretaria() {
		return getPeriodoRecenteOfSecretaria(null);
	}

	public static InstituicaoPeriodo getPeriodoRecenteOfSecretaria(Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			ResultSetMap rsmPeriodoRecenteSecretaria = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdSecretaria, connect);
			InstituicaoPeriodo instituicaoPeriodoRecenteSecretaria = null;
			if(rsmPeriodoRecenteSecretaria.next()){
				instituicaoPeriodoRecenteSecretaria = InstituicaoPeriodoDAO.get(rsmPeriodoRecenteSecretaria.getInt("cd_periodo_letivo"), connect);
			}
			return instituicaoPeriodoRecenteSecretaria;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoServices.getPeriodoAtualOfSecretaria: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public static ResultSetMap getPeriodoRecenteOfInstituicao(int cdInstituicao) {
		return getPeriodoRecenteOfInstituicao(cdInstituicao, null);
	}

	public static ResultSetMap getPeriodoRecenteOfInstituicao(int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rsm;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.*, C.nm_pessoa AS nm_instituicao, D.nm_periodo_letivo AS nm_periodo_letivo_superior, F.nm_pessoa AS nm_instituicao_periodo_superior FROM acd_instituicao_periodo A "
				+ "		 JOIN acd_instituicao B ON (A.cd_instituicao = B.cd_instituicao) "
				+ "		 JOIN grl_pessoa      C ON (A.cd_instituicao = C.cd_pessoa) " 
				+ "		 LEFT OUTER JOIN acd_instituicao_periodo D ON (A.cd_periodo_letivo_superior = D.cd_periodo_letivo)"
				+ "		 LEFT OUTER JOIN acd_instituicao E ON (D.cd_instituicao = E.cd_instituicao)"
				+ "		 LEFT OUTER JOIN grl_pessoa F ON (E.cd_instituicao = F.cd_pessoa)"
				+ "	  WHERE A.cd_instituicao=?  ORDER BY A.dt_inicial DESC");
			pstmt.setInt(1, cdInstituicao);
			rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				if(rsm.getInt("cd_periodo_letivo_superior") > 0)
					rsm.setValueToField("NM_PERIODO_LETIVO_SUPERIOR", rsm.getString("nm_periodo_letivo_superior") + " - " + rsm.getString("nm_instituicao_periodo_superior"));
				else
					rsm.setValueToField("NM_PERIODO_LETIVO_SUPERIOR", "");
			}
			rsm.beforeFirst();
			
			return rsm;
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getPeriodoOfInstituicao(int cdInstituicao) {
		return getPeriodoOfInstituicao(cdInstituicao, 0, null);
	}

	public static ResultSetMap getPeriodoOfInstituicao(int cdInstituicao, Connection connect){
		return getPeriodoOfInstituicao(cdInstituicao, 0, connect);
	}
	
	public static ResultSetMap getPeriodoOfInstituicao(int cdInstituicao, int cdPeriodoLetivoOrigem) {
		return getPeriodoOfInstituicao(cdInstituicao, cdPeriodoLetivoOrigem, null);
	}

	public static ResultSetMap getPeriodoOfInstituicao(int cdInstituicao, int cdPeriodoLetivoOrigem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rsm;
		try {
			if(cdPeriodoLetivoOrigem <= 0){
				pstmt = connect.prepareStatement("SELECT A.*, B.*, C.*, C.nm_pessoa AS nm_instituicao, D.nm_periodo_letivo AS nm_periodo_letivo_superior, F.nm_pessoa AS nm_instituicao_periodo_superior FROM acd_instituicao_periodo A "
					+ "		 JOIN acd_instituicao B ON (A.cd_instituicao = B.cd_instituicao) "
					+ "		 JOIN grl_pessoa      C ON (A.cd_instituicao = C.cd_pessoa) " 
					+ "		 LEFT OUTER JOIN acd_instituicao_periodo D ON (A.cd_periodo_letivo_superior = D.cd_periodo_letivo)"
					+ "		 LEFT OUTER JOIN acd_instituicao E ON (D.cd_instituicao = E.cd_instituicao)"
					+ "		 LEFT OUTER JOIN grl_pessoa F ON (E.cd_instituicao = F.cd_pessoa)"
					+ "	  WHERE A.cd_instituicao=? ORDER BY dt_inicial DESC");
				pstmt.setInt(1, cdInstituicao);
				rsm = new ResultSetMap(pstmt.executeQuery());
				while(rsm.next()){
					if(rsm.getInt("cd_periodo_letivo_superior") > 0)
						rsm.setValueToField("NM_PERIODO_LETIVO_SUPERIOR", rsm.getString("nm_periodo_letivo_superior") + " - " + rsm.getString("nm_instituicao_periodo_superior"));
					else
						rsm.setValueToField("NM_PERIODO_LETIVO_SUPERIOR", "");
				}
				rsm.beforeFirst();
				return rsm;
			}
			else{
				
				InstituicaoPeriodo instituicaoPeriodoOrigem = InstituicaoPeriodoDAO.get(cdPeriodoLetivoOrigem, connect);
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("nm_periodo_letivo", instituicaoPeriodoOrigem.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
				return InstituicaoPeriodoDAO.find(criterios, connect);
			}
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que faz a criação de um novo periodo letivo. Será reformado para que possua indicações do que se deve aproveitar de um periodo letivo para o outro. 
	 * @param cdPeriodoLetivo Código do periodo letivo atual
	 * @param cdInstituicao Instituição onde será criado o novo periodo letivo (normalmente será a Secretaria)
	 * @return Um Result que terá um objeto Instituicao Periodo Letivo, com o novo periodo
	 */
	public static Result reaproveitamentoPeriodoLetivo(int cdPeriodoLetivoAntigo, int cdInstituicao, int cdUsuario) {
		return reaproveitamentoPeriodoLetivo(cdPeriodoLetivoAntigo, cdInstituicao, 0, cdUsuario, null);
	}
	
	public static Result reaproveitamentoPeriodoLetivo(int cdPeriodoLetivoAntigo, int cdInstituicao, int cdUsuario, Connection connect) {
		return reaproveitamentoPeriodoLetivo(cdPeriodoLetivoAntigo, cdInstituicao, 0, cdUsuario, connect);
	}
	
	public static Result reaproveitamentoPeriodoLetivo(int cdPeriodoLetivoAntigo, int cdInstituicao, int cdPeriodoLetivoSuperior, int cdUsuario) {
		return reaproveitamentoPeriodoLetivo(cdPeriodoLetivoAntigo, cdInstituicao, cdPeriodoLetivoSuperior, cdUsuario, null);
	}

	public static Result reaproveitamentoPeriodoLetivo(int cdPeriodoLetivoAntigo, int cdInstituicao, int cdPeriodoLetivoSuperior, int cdUsuario, Connection connect){
		return reaproveitamentoPeriodoLetivo(cdPeriodoLetivoAntigo, cdInstituicao, cdPeriodoLetivoSuperior, cdUsuario, ST_PENDENTE, connect);
	}
	
	public static Result reaproveitamentoPeriodoLetivo(int cdPeriodoLetivoAntigo, int cdInstituicao, int cdPeriodoLetivoSuperior, int cdUsuario, int stPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//Restrição para criação de periodos letivos apenas de instituições ativas			
			InstituicaoEducacenso educacenso = InstituicaoEducacensoDAO.get(cdInstituicao, cdPeriodoLetivoAntigo, connect);
			InstituicaoPeriodo instituicaoPeriodoAntigo = InstituicaoPeriodoDAO.get(cdPeriodoLetivoAntigo, connect);
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
			if(educacenso.getStInstituicaoPublica() != InstituicaoEducacensoServices.ST_EM_ATIVIDADE){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-100, "Apenas instituições ativas podem receber um novo periodo letivo");
			}
			
			//Impede que se crie vários periodos letivos de uma mesma instituição, no mesmo ano
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + cdInstituicao + " AND st_periodo_letivo = " + ST_PENDENTE).executeQuery());
			if(rsm.next()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-100, "Não é possível criar um novo período letivo pois já existe um periodo pendente para essa instituição");
			}
			
			//Inicio do novo periodo letívo sem data de inicio e término, para que os secretários sejam forçados a colocar no periodo do censo
			GregorianCalendar dtInicial = null;
			GregorianCalendar dtFinal = null;
			
			//Constroi o proximo periodo letivo
			InstituicaoPeriodo instituicaoProximoPeriodo = new InstituicaoPeriodo(0, 
																					cdInstituicao, 
																					(instituicaoPeriodoAntigo != null && instituicaoPeriodoAntigo.getDtInicial() != null ? String.valueOf((instituicaoPeriodoAntigo.getDtInicial().get(Calendar.YEAR)+1)) : String.valueOf((new GregorianCalendar()).get(Calendar.YEAR)+1)), 
																					dtInicial, 
																					dtFinal, 
																					0, 
																					stPeriodoLetivo, 
																					TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), 
																					cdPeriodoLetivoSuperior);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_periodo_letivo", instituicaoProximoPeriodo.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmInstituicaoProximoPeriodo = InstituicaoPeriodoDAO.find(criterios, connect);
			if(rsmInstituicaoProximoPeriodo.next())
				instituicaoProximoPeriodo = InstituicaoPeriodoDAO.get(rsmInstituicaoProximoPeriodo.getInt("cd_periodo_letivo"), connect);
			else{
				Result resultado = InstituicaoPeriodoServices.save(instituicaoProximoPeriodo, cdUsuario, connect);
				if(resultado.getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return resultado;
				}
			}
			InstituicaoEducacenso instituicaoEducacensoAntigo = InstituicaoEducacensoDAO.get(cdInstituicao, cdPeriodoLetivoAntigo, connect);
			
			//Campos que não devem sofrer variação de um periodo letivo para o outro
			InstituicaoEducacenso instituicaoEducacenso = new InstituicaoEducacenso();
			instituicaoEducacenso.setCdInstituicao(cdInstituicao);
			instituicaoEducacenso.setNmOrgaoRegional(instituicaoEducacensoAntigo.getNmOrgaoRegional());
			instituicaoEducacenso.setNrOrgaoRegional(instituicaoEducacensoAntigo.getNrOrgaoRegional());
			instituicaoEducacenso.setNrInep(instituicaoEducacensoAntigo.getNrInep());
			instituicaoEducacenso.setStInstituicaoPublica(instituicaoEducacensoAntigo.getStInstituicaoPublica());
			instituicaoEducacenso.setTpLocalizacao(instituicaoEducacensoAntigo.getTpLocalizacao());
			instituicaoEducacenso.setTpLocalizacaoDiferenciada(instituicaoEducacensoAntigo.getTpLocalizacaoDiferenciada());
			instituicaoEducacenso.setCdPeriodoLetivo(instituicaoProximoPeriodo.getCdPeriodoLetivo());
			
			
			if(InstituicaoEducacensoDAO.insert(instituicaoEducacenso, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao adicionar educacenso");
			}
			
			ArrayList<ItemComparator> criteriosInsituticaoPeriodo = new ArrayList<ItemComparator>();
			
			//Criação de Instituição Curso
			criteriosInsituticaoPeriodo.add(new ItemComparator("cd_instituicao", cdInstituicao + "", ItemComparator.EQUAL, Types.INTEGER));
			criteriosInsituticaoPeriodo.add(new ItemComparator("cd_periodo_letivo", cdPeriodoLetivoAntigo + "", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmInstituicaoCurso = InstituicaoCursoDAO.find(criteriosInsituticaoPeriodo);
			while(rsmInstituicaoCurso.next()){
				InstituicaoCurso instituicaoCurso = InstituicaoCursoDAO.get(cdInstituicao, rsmInstituicaoCurso.getInt("cd_curso"), cdPeriodoLetivoAntigo, connect);
				instituicaoCurso.setCdPeriodoLetivo(instituicaoProximoPeriodo.getCdPeriodoLetivo());
				if(InstituicaoCursoDAO.insert(instituicaoCurso, connect) < 0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao adicionar Instituicao Curso");
				}
			}
			
//			//Criação de Instituição Abastecimento de Agua
//			ResultSetMap rsmInstituicaoAbastecimentoAgua = InstituicaoAbastecimentoAguaDAO.find(criteriosInsituticaoPeriodo);
//			while(rsmInstituicaoAbastecimentoAgua.next()){
//				InstituicaoAbastecimentoAgua instituicaoAbastecimentoAgua = InstituicaoAbastecimentoAguaDAO.get(cdInstituicao, rsmInstituicaoAbastecimentoAgua.getInt("cd_abastecimento_agua"), cdPeriodoLetivoAntigo, connect);
//				instituicaoAbastecimentoAgua.setCdPeriodoLetivo(instituicaoProximoPeriodo.getCdPeriodoLetivo());
//				if(InstituicaoAbastecimentoAguaDAO.insert(instituicaoAbastecimentoAgua, connect) < 0 ){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "Erro ao adicionar Instituicao Abastecimento de Agua");
//				}
//			}
			
//			//Criação de Instituição Abastecimento de Energia
//			ResultSetMap rsmInstituicaoAbastecimentoEnergia = InstituicaoAbastecimentoEnergiaDAO.find(criteriosInsituticaoPeriodo);
//			while(rsmInstituicaoAbastecimentoEnergia.next()){
//				InstituicaoAbastecimentoEnergia instituicaoAbastecimentoEnergia = InstituicaoAbastecimentoEnergiaDAO.get(cdInstituicao, rsmInstituicaoAbastecimentoEnergia.getInt("cd_abastecimento_energia"), cdPeriodoLetivoAntigo, connect);
//				instituicaoAbastecimentoEnergia.setCdPeriodoLetivo(instituicaoProximoPeriodo.getCdPeriodoLetivo());
//				if(InstituicaoAbastecimentoEnergiaDAO.insert(instituicaoAbastecimentoEnergia, connect) < 0 ){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "Erro ao adicionar Instituicao Abastecimento de Energia");
//				}
//			}
			
//			//Criação de Instituição Destinacao de Lixo
//			ResultSetMap rsmInstituicaoDestinacaoLixo = InstituicaoDestinacaoLixoDAO.find(criteriosInsituticaoPeriodo);
//			while(rsmInstituicaoDestinacaoLixo.next()){
//				InstituicaoDestinacaoLixo instituicaoDestinacaoLixo = InstituicaoDestinacaoLixoDAO.get(cdInstituicao, rsmInstituicaoDestinacaoLixo.getInt("cd_destinacao_lixo"), cdPeriodoLetivoAntigo, connect);
//				instituicaoDestinacaoLixo.setCdPeriodoLetivo(instituicaoProximoPeriodo.getCdPeriodoLetivo());
//				if(InstituicaoDestinacaoLixoDAO.insert(instituicaoDestinacaoLixo, connect) < 0 ){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "Erro ao adicionar Instituicao Destinacao de Lixo");
//				}
//			}
			
//			//Criação de Instituição Esgoto Sanitario
//			ResultSetMap rsmInstituicaoEsgotoSanitario = InstituicaoEsgotoSanitarioDAO.find(criteriosInsituticaoPeriodo);
//			while(rsmInstituicaoEsgotoSanitario.next()){
//				InstituicaoEsgotoSanitario instituicaoEsgotoSanitario = InstituicaoEsgotoSanitarioDAO.get(cdInstituicao, rsmInstituicaoEsgotoSanitario.getInt("cd_esgoto_sanitario"), cdPeriodoLetivoAntigo, connect);
//				instituicaoEsgotoSanitario.setCdPeriodoLetivo(instituicaoProximoPeriodo.getCdPeriodoLetivo());
//				if(InstituicaoEsgotoSanitarioDAO.insert(instituicaoEsgotoSanitario, connect) < 0 ){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "Erro ao adicionar Instituicao Esgoto Sanitario");
//				}
//			}
			
//			//Criação de Instituição Dependencia
//			ResultSetMap rsmInstituicaoDependencia = InstituicaoDependenciaDAO.find(criteriosInsituticaoPeriodo);
//			while(rsmInstituicaoDependencia.next()){
//				InstituicaoDependencia instituicaoDependencia = InstituicaoDependenciaDAO.get(rsmInstituicaoDependencia.getInt("cd_dependencia"), connect);
//				instituicaoDependencia.setCdPeriodoLetivo(instituicaoProximoPeriodo.getCdPeriodoLetivo());
//				instituicaoDependencia.setCdDependencia(0);
//				if(InstituicaoDependenciaDAO.insert(instituicaoDependencia, connect) < 0 ){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "Erro ao adicionar Instituicao Dependência");
//				}
//			}
			
//			//Criação de Instituição Local Funcionamento
//			ResultSetMap rsmInstituicaoLocalFuncionamento = InstituicaoLocalFuncionamentoDAO.find(criteriosInsituticaoPeriodo);
//			while(rsmInstituicaoLocalFuncionamento.next()){
//				InstituicaoLocalFuncionamento instituicaoLocalFuncionamento = InstituicaoLocalFuncionamentoDAO.get(cdInstituicao, rsmInstituicaoLocalFuncionamento.getInt("cd_local_funcionamento"), cdPeriodoLetivoAntigo, connect);
//				instituicaoLocalFuncionamento.setCdPeriodoLetivo(instituicaoProximoPeriodo.getCdPeriodoLetivo());
//				if(InstituicaoLocalFuncionamentoDAO.insert(instituicaoLocalFuncionamento, connect) < 0 ){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "Erro ao adicionar Instituicao Local Funcionamento");
//				}
//			}
			
			//Criação de Instituição Tipo Etapa
			ResultSetMap rsmInstituicaoTipoEtapa = InstituicaoTipoEtapaDAO.find(criteriosInsituticaoPeriodo);
			while(rsmInstituicaoTipoEtapa.next()){
				InstituicaoTipoEtapa instituicaoTipoEtapa = InstituicaoTipoEtapaDAO.get(cdInstituicao, rsmInstituicaoTipoEtapa.getInt("cd_etapa"), cdPeriodoLetivoAntigo, connect);
				instituicaoTipoEtapa.setCdPeriodoLetivo(instituicaoProximoPeriodo.getCdPeriodoLetivo());
				if(InstituicaoTipoEtapaDAO.insert(instituicaoTipoEtapa, connect) < 0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao adicionar Instituicao Tipo Etapa");
				}
			}
			
//			//Criação de Instituição Tipo Mantenedora
//			ResultSetMap rsmInstituicaoTipoMantenedora = InstituicaoTipoMantenedoraDAO.find(criteriosInsituticaoPeriodo);
//			while(rsmInstituicaoTipoMantenedora.next()){
//				InstituicaoTipoMantenedora instituicaoTipoMantenedora = InstituicaoTipoMantenedoraDAO.get(cdInstituicao, rsmInstituicaoTipoMantenedora.getInt("cd_tipo_mantenedora"), cdPeriodoLetivoAntigo, connect);
//				instituicaoTipoMantenedora.setCdPeriodoLetivo(instituicaoProximoPeriodo.getCdPeriodoLetivo());
//				if(InstituicaoTipoMantenedoraDAO.insert(instituicaoTipoMantenedora, connect) < 0 ){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "Erro ao adicionar Instituicao Tipo Mantenedora");
//				}
//			}
			
			
//			//Projeção de vagas (cdCurso, tpTurno, [qtTurmas, qtVagas]) - Não está sendo passado a projeção de um ano para o outro no momento
			HashMap<Integer, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>> hashProjecaoVagas = new HashMap<Integer, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>>();
			
			//Faz a busca das instituicoes dependentes, e busca se há periodos letivos atuais nas próprias, fazendo com que se crie o mesmo contexto que havia no periodo anterior
			//Caso necessite de alterações, elas serão feitas manualmente
			ResultSetMap rsmInstituicoesDependentes = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa WHERE cd_pessoa_superior = " + cdInstituicao).executeQuery());
			while(rsmInstituicoesDependentes.next()){
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_instituicao", rsmInstituicoesDependentes.getString("cd_pessoa"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("st_periodo_letivo", "" + InstituicaoPeriodoServices.ST_ATUAL, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_periodo", "" + ParametroServices.getValorOfParametro("CD_TIPO_PERIODO_LETIVO", 0), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPeriodoLetivoAtual = InstituicaoPeriodoDAO.find(criterios, connect);
				if(rsmPeriodoLetivoAtual.next()){
					Result resultadoTemp = reaproveitamentoPeriodoLetivo(rsmPeriodoLetivoAtual.getInt("cd_periodo_letivo"), rsmInstituicoesDependentes.getInt("cd_pessoa"), instituicaoProximoPeriodo.getCdPeriodoLetivo(), cdUsuario, connect);
					if(resultadoTemp.getCode() == -100){
						continue;
					}
					else if(resultadoTemp.getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return resultadoTemp;
					}
					
				}
			}
			//Esta apenas registrando o quadro de vagas no momento
			for(int cdInstituicaoProjecao : hashProjecaoVagas.keySet()){
				QuadroVagas quadroVagas = new QuadroVagas(0/*cdQuadroVagas*/, cdInstituicaoProjecao, instituicaoProximoPeriodo.getCdPeriodoLetivo(), com.tivic.manager.util.Util.getDataAtual(), QuadroVagasServices.ST_PENDENTE, null/*txtObservacao*/);
				
				if(QuadroVagasServices.save(quadroVagas, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao fazer quadro de vagas!");
				}
				
			}
			
			//Registra a ocorrência da criação do periodo letivo
			int cdTipoOcorrenciaReaproveitamento = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REAPROVEITAMENTO_PERIODO_LETIVO, connect).getCdTipoOcorrencia();
			OcorrenciaInstituicao ocorrenciaInstituicao = new OcorrenciaInstituicao(0/*cdOcorrencia*/, instituicaoProximoPeriodo.getCdInstituicao(), "Reaproveitamento do período letivo de " + instituicaoPeriodoAntigo.getNmPeriodoLetivo() + " e criado o período letivo de " + instituicaoProximoPeriodo.getNmPeriodoLetivo() + " para a instituição " + instituicao.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrenciaReaproveitamento, OcorrenciaServices.ST_CONCLUIDO, 1, cdUsuario, instituicao.getCdInstituicao(), Util.getDataAtual(), cdUsuario, instituicaoProximoPeriodo.getCdPeriodoLetivo());
			if(OcorrenciaInstituicaoDAO.insert(ocorrenciaInstituicao, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir ocorrência");
			}
			
			
			if(isConnectionNull)
				connect.commit();
			return new Result(1, "Reaproveitamento realizado com sucesso", "INSTITUICAOPERIODO", instituicaoProximoPeriodo);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if(isConnectionNull)
				Conexao.rollback(connect);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * REGISTRAVA O PERIODO LETIVO NOVO, JUNTO COM UMA PROJEÇÃO DE QUADRO DE VAGAS, CRIACAO DE TURMAS e PASSAGEM DOS ALUNOS DE UMA TURMA PARA A OUTRA
	 */
//	public static Result reaproveitamentoPeriodoLetivo(int cdPeriodoLetivoAntigo, int cdInstituicao) {
//		return reaproveitamentoPeriodoLetivo(cdPeriodoLetivoAntigo, cdInstituicao, 0, null);
//	}
//	
//	public static Result reaproveitamentoPeriodoLetivo(int cdPeriodoLetivoAntigo, int cdInstituicao, Connection connect) {
//		return reaproveitamentoPeriodoLetivo(cdPeriodoLetivoAntigo, cdInstituicao, 0, connect);
//	}
//	
//	public static Result reaproveitamentoPeriodoLetivo(int cdPeriodoLetivoAntigo, int cdInstituicao, int cdPeriodoLetivoSuperior) {
//		return reaproveitamentoPeriodoLetivo(cdPeriodoLetivoAntigo, cdInstituicao, cdPeriodoLetivoSuperior, null);
//	}
//
//	public static Result reaproveitamentoPeriodoLetivo(int cdPeriodoLetivoAntigo, int cdInstituicao, int cdPeriodoLetivoSuperior, Connection connect){
//		boolean isConnectionNull = connect==null;
//		try {
//		
//			if (isConnectionNull){
//				connect = Conexao.conectar();
//				connect.setAutoCommit(false);
//			}
//			
//			boolean importTurmas 					= true;
//			boolean importOfertas 					= true;
//			boolean importMatriculas 				= true;
//			boolean importOfertaHorario 			= true;
//		
//			//Fecha o periodo letivo anterior
//			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivoAntigo, connect);
//			
//			//Considera a data padrão do periodo anterior, acrescentando apenas um ano
//			GregorianCalendar dtInicial = instituicaoPeriodo.getDtInicial();
//			if(dtInicial != null)
//				dtInicial.set(Calendar.YEAR, (dtInicial.get(Calendar.YEAR) + 1));
//			GregorianCalendar dtFinal = instituicaoPeriodo.getDtFinal();
//			if(dtFinal != null)
//				dtFinal.set(Calendar.YEAR, (dtFinal.get(Calendar.YEAR) + 1));
//			//Constroi o proximo periodo letivo
//			InstituicaoPeriodo instituicaoProximoPeriodo = new InstituicaoPeriodo(0, cdInstituicao, (dtInicial != null ? String.valueOf(dtInicial.get(Calendar.YEAR)) : null), dtInicial, dtFinal, 0, InstituicaoPeriodoServices.ST_PENDENTE, TipoPeriodoServices.getById(InstituicaoEducacensoServices.TP_PERIODO_LETIVO, connect).getCdTipoPeriodo(), cdPeriodoLetivoSuperior);
//			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//			criterios.add(new ItemComparator("nm_periodo_letivo", instituicaoProximoPeriodo.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
//			criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
//			ResultSetMap rsmInstituicaoProximoPeriodo = InstituicaoPeriodoDAO.find(criterios, connect);
//			if(rsmInstituicaoProximoPeriodo.next())
//				instituicaoProximoPeriodo = InstituicaoPeriodoDAO.get(rsmInstituicaoProximoPeriodo.getInt("cd_periodo_letivo"), connect);
//			else{
//				Result resultado = InstituicaoPeriodoServices.save(instituicaoProximoPeriodo, connect);
//				if(resultado.getCode() <= 0){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "Erro ao inserir relação entre instituição e periodo");
//				}
//			}
//			//Turmas que ficarão em transferência por não haver curso para elas nas instituições atuais
//			HashMap<Integer, Integer> hashTurmasTransferencia = new HashMap<Integer, Integer>();
//			
//			//Turmas que ficarão fora da rede pois não há na rede curso além
//			ArrayList<Integer> arrayTurmasForaRede = new ArrayList<Integer>();
//			
//			//Projeção de vagas (cdCurso, tpTurno, [qtTurmas, qtVagas])
//			HashMap<Integer, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>> hashProjecaoVagas = new HashMap<Integer, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>>();
//			
//			//Turmas que não possuem sequencia anterior, e precisarão serem reproduzidos para o novo periodo letivo
//			ArrayList<Integer> arrayTurmasSemAnterior = new ArrayList<Integer>();
//			
//			//Faz uma busca por todas as turmas do periodo anterior, para que elas sejam reconstruidas no proximo periodo letivo
//			ResultSetMap rsmTurmas = InstituicaoServices.getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivoAntigo, false, false, connect);
//			while(rsmTurmas.next()){
//				if(importTurmas){
//					
//					//TEMPORARIO
////					Curso curso = CursoDAO.get(rsmTurmas.getInt("cd_curso"), connect);
////					if(curso.getIdProdutoServico().equals("23")){
////						if(InstituicaoCursoDAO.get(rsmTurmas.getInt("cd_instituicao"), rsmTurmas.getInt("cd_curso"), connect) == null){
////							if(InstituicaoCursoDAO.insert(new InstituicaoCurso(rsmTurmas.getInt("cd_instituicao"), rsmTurmas.getInt("cd_curso")), connect) <= 0){
////								if(isConnectionNull)
////									Conexao.rollback(connect);
////								return new Result(-1, "Erro ao fazer instituicao curso para turmas multi!");
////							}
////						}
////					}
//					
//					
//					//Verifica se o curso da turma possui uma sequencia anterior, caso não exista, essa turma será reproduzida para o proximo
//					//ano, da mesma forma desse ano
//					if(!CursoEtapaServices.hasAnteriorCurso(rsmTurmas.getInt("cd_curso"), rsmTurmas.getInt("cd_instituicao"), connect)){
//						arrayTurmasSemAnterior.add(rsmTurmas.getInt("cd_turma"));
//					}
//					
//					//Buscar proximo curso a partir das etapas
//					//Caso não exista etapa para esse curso, o mesmo curso continuará para o próximo periodo
//					Curso proximoCurso = CursoEtapaServices.getProximoCurso(rsmTurmas.getInt("cd_curso"), rsmTurmas.getInt("cd_instituicao"), connect);
//					int cdCurso = (proximoCurso != null ? proximoCurso.getCdCurso() : 0);
//					if(cdCurso == 0){
//						arrayTurmasForaRede.add(rsmTurmas.getInt("cd_turma"));
//					}
//					else{
//						//Verificando se existi permissão para a instituição oferecer esse curso
//						InstituicaoCurso instituicaoCurso = InstituicaoCursoDAO.get(rsmTurmas.getInt("cd_instituicao"), cdCurso, connect);
//						if(instituicaoCurso != null){
//							//Inserindo CursoMatriz, caso não exista
//							CursoMatriz cursoMatriz = CursoMatrizDAO.get(rsmTurmas.getInt("cd_matriz"), cdCurso, connect);
//							if(cursoMatriz == null){
//								cursoMatriz = CursoMatrizDAO.get(rsmTurmas.getInt("cd_matriz"), rsmTurmas.getInt("cd_curso"), connect);
//								if(cursoMatriz != null){
//									cursoMatriz.setCdCurso(cdCurso);
//									if(CursoMatrizServices.save(cursoMatriz, connect).getCode() <= 0){
//										if(isConnectionNull)
//											Conexao.rollback(connect);
//										return new Result(-1, "Erro ao cadastrar curso matriz");
//									}
//								}
//							}
//							//Inserindo CursoModulo, caso não exista
//							criterios = new ArrayList<ItemComparator>();
//							criterios.add(new ItemComparator("cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
//							ResultSetMap rsmCursoModulo = CursoModuloDAO.find(criterios, connect);
//							CursoModulo cursoModulo = null;
//							if(rsmCursoModulo.next()){
//								cursoModulo = CursoModuloDAO.get(rsmCursoModulo.getInt("cd_curso_modulo"), connect);
//							}
//							else{
//								cursoModulo = CursoModuloDAO.get(rsmTurmas.getInt("cd_curso_modulo"), connect);
//								if(cursoModulo != null){
//									cursoModulo.setCdCurso(cdCurso);
//									cursoModulo.setCdCursoModulo(0);
//									if(CursoModuloServices.save(cursoModulo, connect).getCode() <= 0){
//										if(isConnectionNull)
//											Conexao.rollback(connect);
//										return new Result(-1, "Erro ao cadastrar curso modulo");
//									}
//								}
//							}
//							if(cursoModulo == null){
//								cursoModulo = new CursoModulo();
//								cursoModulo.setCdCurso(cdCurso);
//								cursoModulo.setNmProdutoServico("MODULO I");
//								if(CursoModuloDAO.insert(cursoModulo, connect) <= 0){
//									if(isConnectionNull)
//										Conexao.rollback(connect);
//									return new Result(-1, "Erro ao cadastrar curso modulo");
//								}
//							}
//							
//							int cdCursoModulo = cursoModulo.getCdCursoModulo();
//							cursoModulo.setIdProdutoServico(cursoModulo.getCdCurso()+"001");
//							if(CursoModuloServices.save(cursoModulo, connect).getCode() <= 0){
//								if(isConnectionNull)
//									Conexao.rollback(connect);
//								return new Result(-1, "Erro ao atualizar curso modulo");
//							}
//							
//						
//							//Inserindo todos os CursoDisciplina, caso não existam
//							criterios = new ArrayList<ItemComparator>();
//							criterios.add(new ItemComparator("cd_curso", rsmTurmas.getString("cd_curso"), ItemComparator.EQUAL, Types.INTEGER));
//							ResultSetMap rsmCursoDisciplina = CursoDisciplinaDAO.find(criterios, connect);
//							while(rsmCursoDisciplina.next()){
//								//Verifica se o curso disciplina já existi
//								CursoDisciplina cursoDisciplina = CursoDisciplinaDAO.get(cdCurso, cdCursoModulo, rsmCursoDisciplina.getInt("cd_disciplina"), rsmCursoDisciplina.getInt("cd_matriz"), connect);
//								if(cursoDisciplina == null){
//									//Caso não exista, insere uma permissao de curso para a instituicao superior (onde estará ligado o curso disciplina)
//									if(InstituicaoCursoDAO.get(rsmCursoDisciplina.getInt("cd_instituicao"), cdCurso, connect) == null){
//										InstituicaoCurso instituicaoCursoSecretaria = new InstituicaoCurso(rsmCursoDisciplina.getInt("cd_instituicao"), cdCurso);
//										if(InstituicaoCursoDAO.insert(instituicaoCursoSecretaria, connect) <= 0){
//											if(isConnectionNull)
//												Conexao.rollback(connect);
//											return new Result(-1, "Erro ao inserir instituição curso");
//										}
//									}
//									
//									//Cria o curso nucleo do curso disciplina, caso não exista
//									CursoNucleo cursoNucleo = CursoNucleoDAO.get(rsmCursoDisciplina.getInt("cd_nucleo"), cdCurso, connect);
//									if(cursoNucleo == null){
//										cursoNucleo = CursoNucleoDAO.get(rsmCursoDisciplina.getInt("cd_nucleo"), rsmCursoDisciplina.getInt("cd_curso"), connect);
//										if(cursoNucleo != null){
//											cursoNucleo.setCdCurso(cdCurso);
//											cursoNucleo.setCdNucleo(0);
//											if(CursoNucleoServices.save(cursoNucleo, connect).getCode() <= 0){
//												if(isConnectionNull)
//													Conexao.rollback(connect);
//												return new Result(-1, "Erro ao inserir curso nucleo");
//											}
//										}
//									}
//									
//									//Busca o curso disciplina anterior, e apenas modufica o Curso e o CursoModulo
//									cursoDisciplina = CursoDisciplinaDAO.get(rsmCursoDisciplina.getInt("cd_curso"), rsmCursoDisciplina.getInt("cd_curso_modulo"), rsmCursoDisciplina.getInt("cd_disciplina"), rsmCursoDisciplina.getInt("cd_matriz"), connect);
//									cursoDisciplina.setCdCurso(cdCurso);
//									cursoDisciplina.setCdCursoModulo(cdCursoModulo);
//									if(cursoNucleo != null)
//										cursoDisciplina.setCdNucleo(cursoNucleo.getCdNucleo());
//									if(CursoDisciplinaServices.save(cursoDisciplina, connect).getCode() <= 0){
//										if(isConnectionNull)
//											Conexao.rollback(connect);
//										return new Result(-1, "Erro ao cadastrar curso disicplina");
//									}
//								}
//							}
//							//Constroi a nova turma como uma turma pendente, necessario usuario verificar as informações e efetivar a turma manualmente
//							Turma turma = new Turma(0, rsmTurmas.getInt("cd_matriz"), instituicaoProximoPeriodo.getCdPeriodoLetivo(), rsmTurmas.getString("nm_turma"), dtInicial, null/*dtConclusao*/, rsmTurmas.getInt("tp_turno"), rsmTurmas.getInt("cd_categoria_mensalidade"), 
//									rsmTurmas.getInt("cd_categoria_matricula"), TurmaServices.ST_PENDENTE, rsmTurmas.getInt("cd_tabela_preco"), rsmTurmas.getInt("cd_instituicao"), cdCurso, rsmTurmas.getInt("qt_vagas"), cdCursoModulo, 
//									null/*nrInep*/, rsmTurmas.getInt("qt_dias_semana_atividade"), rsmTurmas.getInt("tp_atendimento"), rsmTurmas.getInt("tp_modalidade_ensino"), null/*idTurma*/, rsmTurmas.getInt("tp_educacao_infantil"), rsmTurmas.getInt("lg_mais_educa"), rsmTurmas.getInt("cd_turma"), rsmTurmas.getInt("tp_turno_atividade_complementar"));
//							HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> hashProjecaoVagasAux = hashProjecaoVagas.get(rsmTurmas.getInt("cd_instituicao"));
//							if(hashProjecaoVagasAux == null){
//								hashProjecaoVagasAux = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();
//								hashProjecaoVagas.put(rsmTurmas.getInt("cd_instituicao"), hashProjecaoVagasAux);
//							}
//							
//							HashMap<Integer, ArrayList<Integer>> hashProjecaoVagasAuxAux = hashProjecaoVagasAux.get(cdCurso);
//							if(hashProjecaoVagasAuxAux == null){
//								hashProjecaoVagasAuxAux = new HashMap<Integer, ArrayList<Integer>>();
//								hashProjecaoVagasAux.put(cdCurso, hashProjecaoVagasAuxAux);
//							}
//							
//							ArrayList<Integer> arrayProjecaoVagas = hashProjecaoVagasAuxAux.get(rsmTurmas.getInt("tp_turno"));
//							if(arrayProjecaoVagas == null){
//								arrayProjecaoVagas = new ArrayList<Integer>();
//								hashProjecaoVagasAuxAux.put(rsmTurmas.getInt("tp_turno"), arrayProjecaoVagas);
//							}
//							
//							if(arrayProjecaoVagas.size() == 0){
//								arrayProjecaoVagas.add(0);
//								arrayProjecaoVagas.add(0);
//							}
//							
//							arrayProjecaoVagas.set(0, arrayProjecaoVagas.get(0) + 1);
//							arrayProjecaoVagas.set(1, arrayProjecaoVagas.get(1) + rsmTurmas.getInt("qt_vagas"));
//							
//							
//							//Busca os atendimentos especializados que haviam na turma
//							ResultSetMap rsmAee = TurmaServices.getAtendimentoEspecializadoOf(rsmTurmas.getInt("cd_turma"));
//							ArrayList<Integer> atendimentoEspecializado = new ArrayList<Integer>();
//							while(rsmAee.next()){
//								atendimentoEspecializado.add(rsmAee.getInt("cd_atendimento_especializado"));
//							}
//							
//							//Busca as atividades complementares que haviam na turma
//							ResultSetMap rsmAtividadeComplementar = TurmaServices.getAtividadeComplementarOf(rsmTurmas.getInt("cd_turma"));
//							ArrayList<Integer> atividadeComplementar = new ArrayList<Integer>();
//							while(rsmAtividadeComplementar.next()){
//								atividadeComplementar.add(rsmAtividadeComplementar.getInt("cd_atividade_complementar"));
//							}
//							
//							
//							Result resultado = TurmaServices.save(turma, atividadeComplementar, atendimentoEspecializado, connect);
//							if(resultado.getCode() <= 0){
//								if(isConnectionNull)
//									Conexao.rollback(connect);
//								return new Result(-1, "Erro ao inserir oferta para novo periodo letivo");
//							}
//							
//							//Gerar Id da Nova Turma
////							String idTurma = TurmaServices.gerarIdTurma(turma.getCdTurma(), connect);
////							turma.setIdTurma(idTurma);
////							resultado = TurmaServices.save(turma, null, null, connect);
////							if(resultado.getCode() <= 0){
////								if(isConnectionNull)
////									Conexao.rollback(connect);
////								return new Result(-1, "Erro ao inserir turma para novo periodo letivo");
////							}
//						}
//						else{
//							hashTurmasTransferencia.put(rsmTurmas.getInt("cd_turma"), cdCurso);
//						}
//					}
//				}
//			}
//			//A partir das novas turmas criadas, irá se fazer as novas ofertas e matriculas, baseadas nas ofertas e matriculas do periodo anterior
//			ResultSetMap rsmNovasTurmas = InstituicaoServices.getAllTurmasByInstituicaoPeriodo(cdInstituicao, instituicaoProximoPeriodo.getCdPeriodoLetivo(), false, false, connect);
//			while(rsmNovasTurmas.next()){ 
//				//Buscadno curso e curso modulo
//				int cdCurso 	  = rsmNovasTurmas.getInt("cd_curso");
//				int cdCursoModulo = rsmNovasTurmas.getInt("cd_curso_modulo");
//				//Hash para relacionar as ofertas antigas e as novas, para as tabelas que relacionam matricula e oferta
//				//Busca todas as ofertas da turma anterior a que foi criada
//				ResultSetMap rsmOfertas = InstituicaoServices.getAllOfertasAtivas(cdInstituicao, cdPeriodoLetivoAntigo, rsmNovasTurmas.getInt("cd_turma_anterior"), false, connect);
//				while(rsmOfertas.next()){
//					Oferta ofertaAnterior = OfertaDAO.get(rsmOfertas.getInt("cd_oferta"), connect);
//					if(importOfertas){
//						//Cria a nova oferta utilizando a nova turma e a nova instituicao, alem das datas inicial e final criadas para o novo periodo letivo
//						Oferta ofertaNova = new Oferta(0, rsmNovasTurmas.getInt("cd_turma"), instituicaoProximoPeriodo.getCdPeriodoLetivo(), dtInicial, dtFinal, rsmOfertas.getInt("nr_vagas"), rsmOfertas.getInt("nr_dias"), rsmOfertas.getInt("tp_turno"), 
//														rsmOfertas.getFloat("vl_disciplina"), OfertaServices.ST_OFERTA_PENDENTE, rsmOfertas.getInt("cd_instituicao_pratica"), rsmOfertas.getInt("cd_supervisor_pratica"), rsmOfertas.getInt("cd_professor"), 
//														rsmOfertas.getInt("tp_controle_frequencia"), rsmOfertas.getInt("cd_matriz"), cdCurso, cdCursoModulo, rsmOfertas.getInt("cd_disciplina"), rsmOfertas.getInt("cd_dependencia"), 0);
//						Result resultado = OfertaServices.save(ofertaNova, connect);
//						
//						if(resultado.getCode() <= 0){
//							if(isConnectionNull)
//								Conexao.rollback(connect);
//							return new Result(-1, "Erro ao inserir oferta para novo periodo letivo");
//						}
//						//Importa os horarios das ofertas pelo periodo letivo anterior
//						if(importOfertaHorario){
//							
//							ResultSetMap rsmOfertaHorario = OfertaHorarioServices.getAllByOferta(ofertaAnterior.getCdOferta(), connect);
//							while(rsmOfertaHorario.next()){
//								OfertaHorario ofertaHorario = OfertaHorarioDAO.get(rsmOfertaHorario.getInt("cd_horario"), ofertaAnterior.getCdOferta(), connect);
//								InstituicaoHorario instituicaoHorario = InstituicaoHorarioDAO.get(ofertaHorario.getCdHorarioInstituicao(), connect);
//								resultado = OfertaHorarioServices.save(new OfertaHorario(0/*cdHorario*/, ofertaNova.getCdOferta(), ofertaHorario.getNrDiaSemana(), 
//																		ofertaHorario.getHrInicio(), ofertaHorario.getHrTermino(), ofertaHorario.getLgSemanal(), OfertaHorarioServices.ST_ATIVO, 
//																		ofertaHorario.getCdHorarioInstituicao()), instituicaoHorario.getCdInstituicao(), instituicaoProximoPeriodo.getCdPeriodoLetivo(), instituicaoHorario.getTpTurno(), connect);
//
//								if(resultado.getCode() <= 0){
//									if(isConnectionNull)
//										Conexao.rollback(connect);
//									return new Result(-1, resultado.getMessage());
//								}
//							}
//						}
//					}
//				}
//				//Busca todas as matriculas da turma anterior a que foi criada
//				ResultSetMap rsmMatriculas = InstituicaoServices.getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivoAntigo, rsmNovasTurmas.getInt("cd_turma_anterior"), false, true, true, connect);
//				while(rsmMatriculas.next()){
//					Matricula matriculaAnterior = MatriculaDAO.get(rsmMatriculas.getInt("cd_matricula"), connect);
//					Turma turmaAnterior = TurmaDAO.get(matriculaAnterior.getCdTurma(), connect);
//					int cdCursoMatricula = 0;
//					int stMatricula = MatriculaServices.ST_PENDENTE;
//					//Multi
//					if(matriculaAnterior.getCdCurso() != turmaAnterior.getCdCurso()){
//						Curso cursoMatricula = CursoEtapaServices.getProximoCurso(matriculaAnterior.getCdCurso(), cdInstituicao, connect);
//						if(cursoMatricula != null){
//							cdCursoMatricula = cursoMatricula.getCdCurso();
//							if(InstituicaoCursoDAO.get(cdInstituicao, cdCursoMatricula, connect) == null)
//								stMatricula = MatriculaServices.ST_EM_TRANSFERENCIA;
//						}
//						else{
//							matriculaAnterior.setStMatricula(MatriculaServices.ST_CONCLUIDA);
//							if(MatriculaServices.save(matriculaAnterior, false, connect).getCode() <= 0){
//								if(isConnectionNull)
//									Conexao.rollback(connect);
//								return new Result(-1, "Erro ao atualizar matricula finalizada na rede");
//							}
//							continue;
//						}
//							
//					}
//					else{
//						cdCursoMatricula = cdCurso;
//					}
//					
//					if(importMatriculas){
//						//Cria a nova matricula, utilizando a nova turma, o novo curso e o novo periodo letivo
//						Matricula matricula = new Matricula(0, rsmMatriculas.getInt("cd_matriz"), rsmNovasTurmas.getInt("cd_turma"), instituicaoProximoPeriodo.getCdPeriodoLetivo(), 
//															com.tivic.manager.util.Util.getDataAtual(), null/*dtConclusao*/, stMatricula, rsmMatriculas.getInt("tp_matricula"), 
//															rsmMatriculas.getString("nr_matricula"), rsmMatriculas.getInt("cd_aluno"), rsmMatriculas.getInt("cd_matricula"), 
//															0/*cdReserva*/, rsmMatriculas.getInt("cd_area_interesse"), rsmMatriculas.getString("txt_observacao"), rsmMatriculas.getString("txt_boletim"), 
//															cdCursoMatricula, 0/*cdPreMatricula*/, rsmMatriculas.getInt("tp_escolarizacao_outro_espaco"), rsmMatriculas.getInt("lg_transporte_publico"), 
//															rsmMatriculas.getInt("tp_poder_responsavel"), rsmMatriculas.getInt("tp_forma_ingresso"), rsmMatriculas.getString("txt_documento_oficial"), null/*dtInterrupcao*/, 0/*lgAutorizacaoRematricula*/, 0/*lgAtividadeComplementar*/, 0/*lgReprovacao*/);
//						Result resultado = MatriculaServices.save(matricula, 0, false, connect);
//						
//						if(resultado.getCode() <= 0){
//							if(isConnectionNull)
//								Conexao.rollback(connect);
//							return new Result(-1, "Erro ao inserir matricula para novo periodo letivo");
//						}
//						
//					}
//				}
//				
//				if(rsmOfertas.size() == 0 && TurmaServices.getAlunos(rsmNovasTurmas.getInt("cd_turma"), connect).size() == 0){
//					if(TurmaDAO.delete(rsmNovasTurmas.getInt("cd_turma"), connect) <= 0){
//						if(isConnectionNull)
//							Conexao.rollback(connect);
//						return new Result(-1, "Erro ao remover turma para novo periodo letivo");
//					}
//				}
//				
//			}
//			//Faz a busca das instituicoes dependentes, e busca se há periodos letivos atuais nas próprias, fazendo com que se crie o mesmo contexto que havia no periodo anterior
//			//Caso necessite de alterações, elas serão feitas manualmente
//			ResultSetMap rsmInstituicoesDependentes = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_pessoa WHERE cd_pessoa_superior = " + cdInstituicao).executeQuery());
//			while(rsmInstituicoesDependentes.next()){
//				criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_instituicao", rsmInstituicoesDependentes.getString("cd_pessoa"), ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("st_periodo_letivo", "" + InstituicaoPeriodoServices.ST_ATUAL, ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("cd_tipo_periodo", "" + ParametroServices.getValorOfParametro("CD_TIPO_PERIODO_LETIVO", 0), ItemComparator.EQUAL, Types.INTEGER));
//				ResultSetMap rsmPeriodoLetivoAtual = InstituicaoPeriodoDAO.find(criterios, connect);
//				if(rsmPeriodoLetivoAtual.next()){
//					Result resultadoTemp = reaproveitamentoPeriodoLetivo(rsmPeriodoLetivoAtual.getInt("cd_periodo_letivo"), rsmInstituicoesDependentes.getInt("cd_pessoa"), instituicaoProximoPeriodo.getCdPeriodoLetivo(), connect);
//					if(resultadoTemp.getCode() <= 0){
//						if(isConnectionNull)
//							Conexao.rollback(connect);
//						return resultadoTemp;
//					}
//					
//				}
//			}
//			//Coloca os alunos que chegarem até a última etapa oferecida pela rede como em transferência, apenas para buscar seus documentos
//			for(int cdTurma : arrayTurmasForaRede){
//				ResultSetMap rsmAlunosTurmasForaRede = TurmaServices.getAlunos(cdTurma, connect);
//				while(rsmAlunosTurmasForaRede.next()){
//					Matricula matriculaAntiga = MatriculaDAO.get(rsmAlunosTurmasForaRede.getInt("cd_matricula"), connect);
//					matriculaAntiga.setStMatricula(MatriculaServices.ST_CONCLUIDA);
//					if(MatriculaServices.save(matriculaAntiga, false, connect).getCode() <= 0){
//						if(isConnectionNull)
//							Conexao.rollback(connect);
//						return new Result(-1, "Erro ao fazer atualizar matricula de fora da rede!");
//					}
//				}
//			}
//			//Reproduz as turmas sem curso anterior
//			for(int cdTurma : arrayTurmasSemAnterior){
//				Turma turmaAnterior = TurmaDAO.get(cdTurma, connect);
//				Turma turmaAtual = (Turma)turmaAnterior.clone();
//				turmaAtual.setCdPeriodoLetivo(instituicaoProximoPeriodo.getCdPeriodoLetivo());
//				turmaAtual.setCdTurmaAnterior(turmaAtual.getCdTurma());
//				turmaAtual.setCdTurma(0);
//				turmaAtual.setDtAbertura(com.tivic.manager.util.Util.getDataAtual());
//				turmaAtual.setStTurma(TurmaServices.ST_PENDENTE);
//				if(TurmaServices.save(turmaAtual, null, null, connect).getCode() <= 0){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "Erro ao cadastrar nova turma");
//				}
//				HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> hashProjecaoVagasAux = hashProjecaoVagas.get(turmaAtual.getCdInstituicao());
//				if(hashProjecaoVagasAux == null){
//					hashProjecaoVagasAux = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();
//					hashProjecaoVagas.put(turmaAtual.getCdInstituicao(), hashProjecaoVagasAux);
//				}
//				
//				HashMap<Integer, ArrayList<Integer>> hashProjecaoVagasAuxAux = hashProjecaoVagasAux.get(turmaAtual.getCdCurso());
//				if(hashProjecaoVagasAuxAux == null){
//					hashProjecaoVagasAuxAux = new HashMap<Integer, ArrayList<Integer>>();
//					hashProjecaoVagasAux.put(turmaAtual.getCdCurso(), hashProjecaoVagasAuxAux);
//				}
//				
//				ArrayList<Integer> arrayProjecaoVagas = hashProjecaoVagasAuxAux.get(turmaAtual.getTpTurno());
//				if(arrayProjecaoVagas == null){
//					arrayProjecaoVagas = new ArrayList<Integer>();
//					hashProjecaoVagasAuxAux.put(turmaAtual.getTpTurno(), arrayProjecaoVagas);
//				}
//				
//				if(arrayProjecaoVagas.size() == 0){
//					arrayProjecaoVagas.add(0);
//					arrayProjecaoVagas.add(0);
//				}
//				
//				arrayProjecaoVagas.set(0, arrayProjecaoVagas.get(0) + 1);
//				arrayProjecaoVagas.set(1, arrayProjecaoVagas.get(1) + turmaAtual.getQtVagas());
//			}
//			//Realiza as projeções para o periodo letivo baseado nas quantidades de vagas do periodo anterior
//			for(int cdInstituicaoProjecao : hashProjecaoVagas.keySet()){
//				QuadroVagas quadroVagas = new QuadroVagas(0/*cdQuadroVagas*/, cdInstituicaoProjecao, instituicaoProximoPeriodo.getCdPeriodoLetivo(), com.tivic.manager.util.Util.getDataAtual(), QuadroVagasServices.ST_PENDENTE, null/*txtObservacao*/);
//				
//				if(QuadroVagasServices.save(quadroVagas, connect).getCode() <= 0){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "Erro ao fazer quadro de vagas!");
//				}
//				
//				HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> hashProjecaoVagasAux = hashProjecaoVagas.get(cdInstituicaoProjecao);
//				for(int cdCurso : hashProjecaoVagasAux.keySet()){
//					HashMap<Integer, ArrayList<Integer>> hashProjecaoVagasAuxAux = hashProjecaoVagasAux.get(cdCurso);
//					for(int tpTurno : hashProjecaoVagasAuxAux.keySet()){
//						ArrayList<Integer> arrayProjecaoVagas = hashProjecaoVagasAuxAux.get(tpTurno);
//						int qtTurmas = arrayProjecaoVagas.get(0);
//						int qtVagas = arrayProjecaoVagas.get(1);
//						
//						if(QuadroVagasCursoServices.save(new QuadroVagasCurso(quadroVagas.getCdQuadroVagas(), quadroVagas.getCdInstituicao(), cdCurso, tpTurno, qtTurmas, qtVagas), connect).getCode() <= 0){
//							if(isConnectionNull)
//								Conexao.rollback(connect);
//							return new Result(-1, "Erro ao fazer quadro de vagas curso!");
//						}
//					}
//				}
//			}
//			//Cria uma turma de transferência para os alunos que não puderem continuar na escola em que estão (por terem chegado à ultima etapa
//			//oferecida por essa escola), e estes ficarão disponíveis para entrarem em uma nova escola
//			for(int cdTurma : hashTurmasTransferencia.keySet()){
//				ResultSetMap rsmAlunosTurmasTransferencia = TurmaServices.getAlunos(cdTurma, connect);
//				int cdCurso = hashTurmasTransferencia.get(cdTurma);
//				Turma velhaTurma = TurmaDAO.get(cdTurma, connect);
//				Turma novaTurma = new Turma(0/*cdTurma*/, 0/*cdMatriz*/, instituicaoProximoPeriodo.getCdPeriodoLetivo(), "TRANSFERÊNCIA", com.tivic.manager.util.Util.getDataAtual(), null/*dtConclusao*/, velhaTurma.getTpTurno(), 
//										velhaTurma.getCdCategoriaMensalidade(), velhaTurma.getCdCategoriaMatricula(), TurmaServices.ST_CONCLUIDO, velhaTurma.getCdTabelaPreco(), velhaTurma.getCdInstituicao(), cdCurso, velhaTurma.getQtVagas(), 
//										velhaTurma.getCdCursoModulo(), velhaTurma.getNrInep(), velhaTurma.getQtDiasSemanaAtividade(), velhaTurma.getTpAtendimento(), velhaTurma.getTpModalidadeEnsino(), velhaTurma.getIdTurma(), velhaTurma.getTpEducacaoInfantil(), 
//										velhaTurma.getLgMaisEduca(), velhaTurma.getCdTurma(), velhaTurma.getTpTurnoAtividadeComplementar());
//				Result result = TurmaServices.save(novaTurma, null, null, connect);
//				if(result.getCode() <= 0){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, result.getMessage());
//				}
//				while(rsmAlunosTurmasTransferencia.next()){
//					Matricula matriculaAntiga = MatriculaDAO.get(rsmAlunosTurmasTransferencia.getInt("cd_matricula"), connect);
//					
//					Matricula matriculaNova = new Matricula(0/*cdMatricula*/, 0/*cdMatriz*/, novaTurma.getCdTurma(), instituicaoProximoPeriodo.getCdPeriodoLetivo(), com.tivic.manager.util.Util.getDataAtual(), null/*dtConclusao*/, MatriculaServices.ST_EM_TRANSFERENCIA, 
//														   matriculaAntiga.getTpMatricula(), matriculaAntiga.getNrMatricula(), matriculaAntiga.getCdAluno(), matriculaAntiga.getCdMatricula(), matriculaAntiga.getCdReserva(), matriculaAntiga.getCdAreaInteresse(), 
//														   matriculaAntiga.getTxtObservacao(), matriculaAntiga.getTxtBoletim(), cdCurso, 0/*cdPreMatricula*/, matriculaAntiga.getTpEscolarizacaoOutroEspaco(), matriculaAntiga.getLgTransportePublico(), 
//														   matriculaAntiga.getTpPoderResponsavel(), matriculaAntiga.getTpFormaIngresso(), matriculaAntiga.getTxtDocumentoOficial(), null/*dtInterrupcao*/, 0/*lgAutorizacaoRematricula*/, matriculaAntiga.getLgAtividadeComplementar(), 0/*lgReprovacao*/);
//					
//					if(MatriculaServices.save(matriculaNova, false, connect).getCode() <= 0){
//						if(isConnectionNull)
//							Conexao.rollback(connect);
//						return new Result(-1, "Erro ao fazer nova matricula de transferência!");
//					}
//				}
//			}
//			if(isConnectionNull)
//				connect.commit();
//			return new Result(1, "Reaproveitamento realizado com sucesso", "INSTITUICAOPERIODO", instituicaoProximoPeriodo);
//		}
//		catch(Exception e) {
//			if(isConnectionNull)
//				Conexao.rollback(connect);
//			e.printStackTrace(System.out);
//			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + e);
//			return null;
//		}
//		finally {
//			if (isConnectionNull)
//				Conexao.desconectar(connect);
//		}
//	}
	
	
	public static ResultSetMap progressaoTurma(int cdTurma) {
		return progressaoTurma(cdTurma, null);
	}
	
	public static ResultSetMap progressaoTurma(int cdTurma, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
					
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Turma turma  = TurmaDAO.get(cdTurma, connect);
			
			
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);
			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return null;
			}
			
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turma.getCdPeriodoLetivo(), connect);
			
			Turma turmaNova = null;
			
			//Constroi o proximo periodo letivo
			InstituicaoPeriodo instituicaoProximoPeriodo = null;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_periodo_letivo", "" + String.valueOf((Integer.parseInt(instituicaoPeriodo.getNmPeriodoLetivo()) + 1)), ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("cd_instituicao", "" + instituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmInstituicaoProximoPeriodo = InstituicaoPeriodoDAO.find(criterios, connect);
			if(rsmInstituicaoProximoPeriodo.next())
				instituicaoProximoPeriodo = InstituicaoPeriodoDAO.get(rsmInstituicaoProximoPeriodo.getInt("cd_periodo_letivo"), connect);
			else
				return null;
			
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_turma_anterior", "" + turma.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("st_turma", "" + TurmaServices.ST_INATIVO, ItemComparator.DIFFERENT, Types.INTEGER));
			ResultSetMap rsmNovaTurma = TurmaDAO.find(criterios, connect);
			if(!rsmNovaTurma.next()){
				//Buscar proximo curso a partir das etapas
				//Caso não exista etapa para esse curso, o mesmo curso continuará para o próximo periodo
				Curso proximoCurso = CursoEtapaServices.getProximoCurso(turma.getCdCurso(), turma.getCdInstituicao(), connect);
				int cdCurso = (proximoCurso != null ? proximoCurso.getCdCurso() : 0);
				if(cdCurso == 0){
					ResultSetMap rsmAlunosTurmasForaRede = TurmaServices.getAlunos(cdTurma, connect);
					while(rsmAlunosTurmasForaRede.next()){
						Matricula matriculaAntiga = MatriculaDAO.get(rsmAlunosTurmasForaRede.getInt("cd_matricula"), connect);
						matriculaAntiga.setStMatricula(MatriculaServices.ST_CONCLUIDA);
						if(MatriculaServices.save(matriculaAntiga, false, connect).getCode() <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return null;
						}
					}
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("A.CD_TURMA", "" + turma.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmTurma = TurmaServices.find(criterios, connect);
					
					return rsmTurma;
				}
				else{
					//Verificando se existi permissão para a instituição oferecer esse curso
					InstituicaoCurso instituicaoCurso = InstituicaoCursoDAO.get(turma.getCdInstituicao(), cdCurso, instituicaoProximoPeriodo.getCdPeriodoLetivo(), connect);
					if(instituicaoCurso != null){
						//Inserindo CursoMatriz, caso não exista
						CursoMatriz cursoMatriz = CursoMatrizDAO.get(turma.getCdMatriz(), cdCurso, connect);
						if(cursoMatriz == null){
							cursoMatriz = CursoMatrizDAO.get(turma.getCdMatriz(), turma.getCdCurso(), connect);
							if(cursoMatriz != null){
								cursoMatriz.setCdCurso(cdCurso);
								if(CursoMatrizServices.save(cursoMatriz, connect).getCode() <= 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return null;
								}
							}
						}
						//Inserindo CursoModulo, caso não exista
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmCursoModulo = CursoModuloDAO.find(criterios, connect);
						CursoModulo cursoModulo = null;
						if(rsmCursoModulo.next()){
							cursoModulo = CursoModuloDAO.get(rsmCursoModulo.getInt("cd_curso_modulo"), connect);
						}
						else{
							cursoModulo = CursoModuloDAO.get(turma.getCdCursoModulo(), connect);
							if(cursoModulo != null){
								cursoModulo.setCdCurso(cdCurso);
								cursoModulo.setCdCursoModulo(0);
								if(CursoModuloServices.save(cursoModulo, connect).getCode() <= 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return null;
								}
							}
						}
						if(cursoModulo == null){
							cursoModulo = new CursoModulo();
							cursoModulo.setCdCurso(cdCurso);
							cursoModulo.setNmProdutoServico("MODULO I");
							if(CursoModuloDAO.insert(cursoModulo, connect) <= 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return null;
							}
						}
						
						int cdCursoModulo = cursoModulo.getCdCursoModulo();
						cursoModulo.setIdProdutoServico(cursoModulo.getCdCurso()+"001");
						if(CursoModuloServices.save(cursoModulo, connect).getCode() <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return null;
						}
						
					
						//Inserindo todos os CursoDisciplina, caso não existam
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_curso", "" + turma.getCdCurso(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmCursoDisciplina = CursoDisciplinaDAO.find(criterios, connect);
						while(rsmCursoDisciplina.next()){
							//Verifica se o curso disciplina já existi
							CursoDisciplina cursoDisciplina = CursoDisciplinaDAO.get(cdCurso, cdCursoModulo, rsmCursoDisciplina.getInt("cd_disciplina"), rsmCursoDisciplina.getInt("cd_matriz"), connect);
							if(cursoDisciplina == null){
								//Caso não exista, insere uma permissao de curso para a instituicao superior (onde estará ligado o curso disciplina)
								if(InstituicaoCursoDAO.get(rsmCursoDisciplina.getInt("cd_instituicao"), cdCurso, instituicaoProximoPeriodo.getCdPeriodoLetivo(), connect) == null){
									InstituicaoCurso instituicaoCursoSecretaria = new InstituicaoCurso(rsmCursoDisciplina.getInt("cd_instituicao"), cdCurso, instituicaoProximoPeriodo.getCdPeriodoLetivo());
									if(InstituicaoCursoDAO.insert(instituicaoCursoSecretaria, connect) <= 0){
										if(isConnectionNull)
											Conexao.rollback(connect);
										return null;
									}
								}
								
								//Cria o curso nucleo do curso disciplina, caso não exista
								CursoNucleo cursoNucleo = CursoNucleoDAO.get(rsmCursoDisciplina.getInt("cd_nucleo"), cdCurso, connect);
								if(cursoNucleo == null){
									cursoNucleo = CursoNucleoDAO.get(rsmCursoDisciplina.getInt("cd_nucleo"), rsmCursoDisciplina.getInt("cd_curso"), connect);
									if(cursoNucleo != null){
										cursoNucleo.setCdCurso(cdCurso);
										cursoNucleo.setCdNucleo(0);
										if(CursoNucleoServices.save(cursoNucleo, connect).getCode() <= 0){
											if(isConnectionNull)
												Conexao.rollback(connect);
											return null;
										}
									}
								}
								
								//Busca o curso disciplina anterior, e apenas modufica o Curso e o CursoModulo
								cursoDisciplina = CursoDisciplinaDAO.get(rsmCursoDisciplina.getInt("cd_curso"), rsmCursoDisciplina.getInt("cd_curso_modulo"), rsmCursoDisciplina.getInt("cd_disciplina"), rsmCursoDisciplina.getInt("cd_matriz"), connect);
								cursoDisciplina.setCdCurso(cdCurso);
								cursoDisciplina.setCdCursoModulo(cdCursoModulo);
								if(cursoNucleo != null)
									cursoDisciplina.setCdNucleo(cursoNucleo.getCdNucleo());
								if(CursoDisciplinaServices.save(cursoDisciplina, connect).getCode() <= 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return null;
								}
							}
						}
						//Constroi a nova turma como uma turma pendente, necessario usuario verificar as informações e efetivar a turma manualmente
						turmaNova = new Turma(0, turma.getCdMatriz(), instituicaoProximoPeriodo.getCdPeriodoLetivo(), turma.getNmTurma(), instituicaoProximoPeriodo.getDtInicial(), null/*dtConclusao*/, turma.getTpTurno(), turma.getCdCategoriaMensalidade(), 
								turma.getCdCategoriaMatricula(), TurmaServices.ST_PENDENTE, turma.getCdTabelaPreco(), turma.getCdInstituicao(), cdCurso, turma.getQtVagas(), cdCursoModulo, 
								null/*nrInep*/, turma.getQtDiasSemanaAtividade(), turma.getTpAtendimento(), turma.getTpModalidadeEnsino(), null/*idTurma*/, turma.getTpEducacaoInfantil(), turma.getLgMaisEduca(), turma.getCdTurma(), turma.getTpTurnoAtividadeComplementar(), turma.getTpLocalDiferenciado());
						
						
						//Busca os atendimentos especializados que haviam na turma
						ResultSetMap rsmAee = TurmaServices.getAtendimentoEspecializadoOf(turma.getCdTurma());
						ArrayList<Integer> atendimentoEspecializado = new ArrayList<Integer>();
						while(rsmAee.next()){
							atendimentoEspecializado.add(rsmAee.getInt("cd_atendimento_especializado"));
						}
						
						//Busca as atividades complementares que haviam na turma
						ResultSetMap rsmAtividadeComplementar = TurmaServices.getAtividadeComplementarOf(turma.getCdTurma());
						ArrayList<Integer> atividadeComplementar = new ArrayList<Integer>();
						while(rsmAtividadeComplementar.next()){
							atividadeComplementar.add(rsmAtividadeComplementar.getInt("cd_atividade_complementar"));
						}
						
						
						Result resultado = TurmaServices.save(turmaNova, atividadeComplementar, atendimentoEspecializado, connect);
						if(resultado.getCode() <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return null;
						}
						
					}
					else{
						ResultSetMap rsmAlunosTurmasTransferencia = TurmaServices.getAlunos(cdTurma, connect);
						Turma velhaTurma = TurmaDAO.get(cdTurma, connect);
						Turma novaTurma = new Turma(0/*cdTurma*/, 0/*cdMatriz*/, instituicaoProximoPeriodo.getCdPeriodoLetivo(), "TRANSFERÊNCIA", com.tivic.manager.util.Util.getDataAtual(), null/*dtConclusao*/, velhaTurma.getTpTurno(), 
												velhaTurma.getCdCategoriaMensalidade(), velhaTurma.getCdCategoriaMatricula(), TurmaServices.ST_CONCLUIDO, velhaTurma.getCdTabelaPreco(), velhaTurma.getCdInstituicao(), cdCurso, velhaTurma.getQtVagas(), 
												velhaTurma.getCdCursoModulo(), velhaTurma.getNrInep(), velhaTurma.getQtDiasSemanaAtividade(), velhaTurma.getTpAtendimento(), velhaTurma.getTpModalidadeEnsino(), velhaTurma.getIdTurma(), velhaTurma.getTpEducacaoInfantil(), 
												velhaTurma.getLgMaisEduca(), velhaTurma.getCdTurma(), velhaTurma.getTpTurnoAtividadeComplementar(), velhaTurma.getTpLocalDiferenciado());
						Result result = TurmaServices.save(novaTurma, null, null, connect);
						if(result.getCode() <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return null;
						}
						while(rsmAlunosTurmasTransferencia.next()){
							Matricula matriculaAntiga = MatriculaDAO.get(rsmAlunosTurmasTransferencia.getInt("cd_matricula"), connect);
							
							Matricula matriculaNova = new Matricula(0/*cdMatricula*/, 0/*cdMatriz*/, novaTurma.getCdTurma(), instituicaoProximoPeriodo.getCdPeriodoLetivo(), com.tivic.manager.util.Util.getDataAtual(), null/*dtConclusao*/, MatriculaServices.ST_EM_TRANSFERENCIA, 
																   matriculaAntiga.getTpMatricula(), matriculaAntiga.getNrMatricula(), matriculaAntiga.getCdAluno(), matriculaAntiga.getCdMatricula(), matriculaAntiga.getCdReserva(), matriculaAntiga.getCdAreaInteresse(), 
																   matriculaAntiga.getTxtObservacao(), matriculaAntiga.getTxtBoletim(), cdCurso, 0/*cdPreMatricula*/, matriculaAntiga.getTpEscolarizacaoOutroEspaco(), matriculaAntiga.getLgTransportePublico(), 
																   matriculaAntiga.getTpPoderResponsavel(), matriculaAntiga.getTpFormaIngresso(), matriculaAntiga.getTxtDocumentoOficial(), null/*dtInterrupcao*/, 0/*lgAutorizacaoRematricula*/, matriculaAntiga.getLgAtividadeComplementar(), 0/*lgReprovacao*/);
							
							if(MatriculaServices.save(matriculaNova, false, connect).getCode() <= 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return null;
							}
						}
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("A.CD_TURMA", "" + novaTurma.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmTurmaNova = TurmaServices.find(criterios, connect);
						
						return rsmTurmaNova;
					}
				}
			}
			else{
				turmaNova = TurmaDAO.get(rsmNovaTurma.getInt("cd_turma"), connect);
			}
					
					
			ResultSetMap rsmMatriculas = TurmaServices.getAlunos(turma.getCdTurma(), connect);
			while(rsmMatriculas.next()){
				if(progressaoMatricula(rsmMatriculas.getInt("cd_matricula"), connect) == null){
					return null;
				}
			}
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_TURMA", "" + turmaNova.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmTurmaNova = TurmaServices.find(criterios, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			return rsmTurmaNova;
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result progressaoMatricula(int cdMatricula) {
		return progressaoMatricula(cdMatricula, 0, 0, null);
	}
	
	public static Result progressaoMatricula(int cdMatricula, Connection connect){
		return progressaoMatricula(cdMatricula, 0, 0, connect);
	}
	
	public static Result progressaoMatricula(int cdMatricula, int cdUsuario) {
		return progressaoMatricula(cdMatricula, 0, cdUsuario, null);
	}
	
	public static Result progressaoMatricula(int cdMatricula, int cdUsuario, Connection connect){
		return progressaoMatricula(cdMatricula, 0, cdUsuario, connect);
	}
	
	public static Result progressaoMatriculaUnica(int cdMatricula, int cdTurma, int cdUsuario) {
		return progressaoMatricula(cdMatricula, cdTurma, cdUsuario, null);
	}
	
	public static Result progressaoMatricula(int cdUsuario, ArrayList<Integer> matriculas) {
		return progressaoMatricula(0, cdUsuario, matriculas, null);
	}
	
	public static Result progressaoMatricula(int cdTurma, int cdUsuario, ArrayList<Integer> matriculas) {
		return progressaoMatricula(cdTurma, cdUsuario, matriculas, null);
	}
	
	/**
	 * Método que permite gerenciar a progressão de matrículas de um periodo para o outro
	 * podendo escolher a turma, ou mandando as matrículas para transferência (caso a escola não tenha o proximo curso da grade)
	 * @param cdTurma
	 * @param cdUsuario
	 * @param matriculas
	 * @param connect
	 * @return
	 */
	public static Result progressaoMatricula(int cdTurma, int cdUsuario, ArrayList<Integer> matriculas, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//Itera sobre as matriculas passadas para fazer a progressão uma a uma
			for(int cdMatricula : matriculas){
				Result result = progressaoMatricula(cdMatricula, cdTurma, cdUsuario, connect);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Matrículas progredidas com sucesso.");
			return result;
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoServices.progressaoMatricula: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result progressaoMatricula(int cdMatricula, int cdTurma, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//Busca a matricula atual (ou anterior àquela que será feita) para analisar como ficará a próxima matrícula
			Matricula matriculaAnterior = MatriculaDAO.get(cdMatricula, connect);
			
			
			//Só é possível progredir matrículas ativas
			if(matriculaAnterior.getStMatricula() != MatriculaServices.ST_ATIVA){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Progressão não pode ser realizada para matrículas que não estejam Ativas");
			}
			
			Turma turmaAnterior = TurmaDAO.get(matriculaAnterior.getCdTurma(), connect);
			
			Instituicao instituicao = InstituicaoDAO.get(turmaAnterior.getCdInstituicao(), connect);
			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Escolas que estao no modo offline nao podem fazer progressao de matriculas");
			}
			
			
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(matriculaAnterior.getCdPeriodoLetivo(), connect);
			
			String nmProximoPeriodo = String.valueOf((Integer.parseInt(instituicaoPeriodo.getNmPeriodoLetivo()) + 1));
			
			//Verifica se o aluno já possui matrícula no periodo seguinte
			ResultSetMap rsmMatriculaAlunoPeriodo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, "
					+ "																						 acd_instituicao_periodo B "
					+ "																			WHERE A.cd_periodo_letivo = B.cd_periodo_letivo"
					+ "																			  AND A.cd_aluno = " + matriculaAnterior.getCdAluno()
					+ "																			  AND A.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_PENDENTE + ", " + MatriculaServices.ST_EM_TRANSFERENCIA + ", " + MatriculaServices.ST_CONCLUIDA + ")"
					+ "																			  AND B.nm_periodo_letivo = '" + nmProximoPeriodo + "'").executeQuery());
			if(rsmMatriculaAlunoPeriodo.next()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				
				Pessoa pessoa = PessoaDAO.get(rsmMatriculaAlunoPeriodo.getInt("cd_aluno"), connect);
				
				return new Result(-1, "Aluno(a) "+pessoa.getNmPessoa()+" já possui matrícula no período de " + nmProximoPeriodo);
			}
			
			//Constroi o proximo periodo letivo
			InstituicaoPeriodo instituicaoProximoPeriodo = null;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_periodo_letivo", "" + nmProximoPeriodo, ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("cd_instituicao", "" + instituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmInstituicaoProximoPeriodo = InstituicaoPeriodoDAO.find(criterios, connect);
			if(rsmInstituicaoProximoPeriodo.next())
				instituicaoProximoPeriodo = InstituicaoPeriodoDAO.get(rsmInstituicaoProximoPeriodo.getInt("cd_periodo_letivo"), connect);
			else{
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Periodo Letivo de " + nmProximoPeriodo + " não foi encontrado.");
			}
			
			
			int cdCursoMatricula = 0;
			//A situação padrão para a progressão da matrícula é de uma matrícula pendente que deva ser renovada
			int stMatricula = MatriculaServices.ST_PENDENTE;
			int cdInstituicao = instituicao.getCdInstituicao();
			
			//Faz a busca do próximo curso, analisando o curso atual e a escola ao qual o aluno está
			Curso cursoMatricula = CursoEtapaServices.getProximoCurso(matriculaAnterior.getCdCurso(), cdInstituicao, connect);
			if(cursoMatricula != null){
				cdCursoMatricula = cursoMatricula.getCdCurso();
				//Se não houver registro em InstituicaoCurso, significa que a escola não tem a próxima matrícula na grade, colocando a matrícula para transferência
				if(InstituicaoCursoDAO.get(cdInstituicao, cdCursoMatricula, instituicaoProximoPeriodo.getCdPeriodoLetivo(), connect) == null)
					stMatricula = MatriculaServices.ST_EM_TRANSFERENCIA;
			}
			//Se não houver registro do próximo curso, significa que o aluno já está na última modalidade da rede (na municipal é o 9 ano)
			//Não realizando assim a progressão
			else{
				matriculaAnterior.setStMatricula(MatriculaServices.ST_CONCLUIDA);
				Result result = MatriculaServices.save(matriculaAnterior, false, connect);
				if(result.getCode() <= 0 && result.getCode() != MatriculaServices.ERR_ALUNO_FORA_IDADE){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar matrícula antiga.");
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.CD_MATRICULA", "" + matriculaAnterior.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmMatriculaVelha = MatriculaServices.find(criterios, connect);
				
				result = new Result(1, "Aluno já está na última modalidade da rede.");
				result.addObject("RSM", rsmMatriculaVelha);
				return result;
			}
					
			int cdNovaTurma = 0;
			if(cdTurma > 0){
				cdNovaTurma = cdTurma;
			}
			else{
				//O sistema busca a turma ao qual o aluno deverá ser inserido (buscando por uma turma de transferência caso a escola não tenha o curso na grade)
				//ou buscando a próxima turma. Caso ele não ache, cria ou indica que não existe próxima turma
				criterios = new ArrayList<ItemComparator>();
				if(InstituicaoCursoDAO.get(cdInstituicao, cdCursoMatricula, instituicaoProximoPeriodo.getCdPeriodoLetivo(), connect) == null){
					criterios.add(new ItemComparator("nm_turma", "TRANSFERENCIA", ItemComparator.EQUAL, Types.VARCHAR));
					criterios.add(new ItemComparator("cd_curso", "" +cdCursoMatricula, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_periodo_letivo", "" +instituicaoProximoPeriodo.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				}
				else{
					criterios.add(new ItemComparator("cd_turma_anterior", "" + turmaAnterior.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("st_turma", "" + TurmaServices.ST_INATIVO, ItemComparator.DIFFERENT, Types.INTEGER));
				}
				ResultSetMap rsmNovaTurma = TurmaDAO.find(criterios, connect);
				if(rsmNovaTurma.next()){
					cdNovaTurma = rsmNovaTurma.getInt("cd_turma");
				}
				else{
					if(InstituicaoCursoDAO.get(cdInstituicao, cdCursoMatricula, instituicaoProximoPeriodo.getCdPeriodoLetivo(), connect) == null){
						cdNovaTurma = TurmaDAO.insert(new Turma(0/*cdTurma*/, 1/*cdMatriz*/, instituicaoProximoPeriodo.getCdPeriodoLetivo(), "TRANSFERENCIA", Util.getDataAtual(), null/*dtConclusao*/, 
														0/*tpTurno*/, 0/*cdCategoriaMensalidade*/, 0/*cdCategoriaMatricula*/, 0/*stTurma*/, 0/*cdTabelaPreco*/, 
														cdInstituicao, cursoMatricula.getCdCurso(), 0/*qtVagas*/, 0/*cdCursoModulo*/, null/*nrInep*/, 0/*qtDiasSemanaAtividade*/, 
														0/*tpAtendimento*/, 0/*tpModalidadeEnsino*/, null/*idTurma*/, 0/*tpEducacaoInfantil*/, 0/*lgMaisEduca*/, 
														0/*cdTurmaAnterior*/, 0/*tpTurnoAtividadeComplementar*/, 0/*tpLocalDiferenciado*/), connect);
						
						if(cdNovaTurma < 0){
							if(isConnectionNull){
								Conexao.rollback(connect);
							}
							return new Result(-1, "Erro ao criar turma de transferencia");
						}
					}
					else{
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-2, "Turma de progressão não encontrada.");
					}
				}
			}	
					
			//Cria a nova matricula, utilizando a nova turma, o novo curso e o novo periodo letivo
			Matricula matricula = new Matricula(0, matriculaAnterior.getCdMatriz(), cdNovaTurma, instituicaoProximoPeriodo.getCdPeriodoLetivo(), 
												com.tivic.manager.util.Util.getDataAtual(), null/*dtConclusao*/, stMatricula, matriculaAnterior.getTpMatricula(), 
												matriculaAnterior.getNrMatricula(), matriculaAnterior.getCdAluno(), matriculaAnterior.getCdMatricula(), 
												0/*cdReserva*/, matriculaAnterior.getCdAreaInteresse(), matriculaAnterior.getTxtObservacao(), matriculaAnterior.getTxtBoletim(), 
												cdCursoMatricula, 0/*cdPreMatricula*/, matriculaAnterior.getTpEscolarizacaoOutroEspaco(), 0/*lgTransportePublico*/, 
												matriculaAnterior.getTpPoderResponsavel(), matriculaAnterior.getTpFormaIngresso(), matriculaAnterior.getTxtDocumentoOficial(), 
												null/*dtInterrupcao*/, 0/*lgAutorizacaoRematricula*/, 0/*lgAtividadeComplementar*/, 0/*lgReprovacao*/, 0/*stMatriculaCentaurus*/, 
												MatriculaServices.ST_ALUNO_CENSO_APROVADO, null/*nrMatriculaCenso*/, MatriculaServices.ST_CENSO_FINAL_NAO_LANCADO, PessoaDAO.get(instituicaoPeriodo.getCdInstituicao(), connect).getNmPessoa());
			Result resultado = MatriculaServices.save(matricula, 0, false, cdUsuario, connect);
			
			if(resultado.getCode() <= 0 && resultado.getCode() != MatriculaServices.ERR_ALUNO_FORA_IDADE){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return resultado;
			}
			
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_MATRICULA", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmMatriculaNova = MatriculaServices.find(criterios, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Matrícula progredida com sucesso.");
			result.addObject("RSM", rsmMatriculaNova);
			return result;
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result progressaoMatricula9Ano(int cdMatricula, int cdUsuario){
		return progressaoMatricula9Ano(cdMatricula, cdUsuario, null);
	}
	
	public static Result progressaoMatricula9Ano(int cdMatricula, int cdUsuario, Connection connect){
		return progressaoMatricula9Ano(cdMatricula, cdUsuario, 0, connect);
	}
	
	public static Result progressaoMatricula9Ano(int cdMatricula, int cdUsuario, int cdTurma){
		return progressaoMatricula9Ano(cdMatricula, cdUsuario, cdTurma, null);
	}
	
	public static Result progressaoMatricula9Ano(int cdMatricula, int cdUsuario, int cdTurma, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Matricula matriculaAnterior = MatriculaDAO.get(cdMatricula, connect);
			
			if(matriculaAnterior.getStMatricula() != MatriculaServices.ST_ATIVA && matriculaAnterior.getStMatricula() != MatriculaServices.ST_CONCLUIDA){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Progressão não pode ser realizada para matrículas que não estejam Ativas ou Concluídas");
			}
			
			Turma turmaAnterior = TurmaDAO.get(matriculaAnterior.getCdTurma(), connect);
			
			Curso cursoAnterior = CursoDAO.get(matriculaAnterior.getCdCurso(), connect);
			if(cursoAnterior.getNrOrdem() != 8){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Apenas matrículas no 9º ano podem realizar essa progressão");
			}
			
			Instituicao instituicao = InstituicaoDAO.get(turmaAnterior.getCdInstituicao(), connect);
			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Escolas que estao no modo offline nao podem fazer progressao de matriculas do 9 ano");
			}
			
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(matriculaAnterior.getCdPeriodoLetivo(), connect);
			
			String nmProximoPeriodo = String.valueOf((Integer.parseInt(instituicaoPeriodo.getNmPeriodoLetivo()) + 1));
			
			ResultSetMap rsmMatriculaAlunoPeriodo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, "
					+ "																						 acd_instituicao_periodo B "
					+ "																			WHERE A.cd_periodo_letivo = B.cd_periodo_letivo"
					+ "																			  AND A.cd_aluno = " + matriculaAnterior.getCdAluno()
					+ "																			  AND A.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_PENDENTE + ", " + MatriculaServices.ST_CONCLUIDA + ")"
					+ "																			  AND B.nm_periodo_letivo = '" + nmProximoPeriodo + "'").executeQuery());
			if(rsmMatriculaAlunoPeriodo.next()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Aluno já possui matrícula no período de " + nmProximoPeriodo);
			}
			
			//Constroi o proximo periodo letivo
			InstituicaoPeriodo instituicaoProximoPeriodo = null;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("nm_periodo_letivo", "" + nmProximoPeriodo, ItemComparator.EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("cd_instituicao", "" + instituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmInstituicaoProximoPeriodo = InstituicaoPeriodoDAO.find(criterios, connect);
			if(rsmInstituicaoProximoPeriodo.next())
				instituicaoProximoPeriodo = InstituicaoPeriodoDAO.get(rsmInstituicaoProximoPeriodo.getInt("cd_periodo_letivo"), connect);
			else{
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Periodo Letivo de " + nmProximoPeriodo + " não foi encontrado.");
			}
			
			
			int cdCursoMatricula = matriculaAnterior.getCdCurso();
			int stMatricula = MatriculaServices.ST_PENDENTE;
				
			int cdNovaTurma = 0;
			if(cdTurma > 0){
				cdNovaTurma = cdTurma;
			}
			else{
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_turma_anterior", "" + turmaAnterior.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("st_turma", "" + TurmaServices.ST_INATIVO, ItemComparator.DIFFERENT, Types.INTEGER));
				
				ResultSetMap rsmNovaTurma = TurmaDAO.find(criterios, connect);
				if(rsmNovaTurma.next()){
					cdNovaTurma = rsmNovaTurma.getInt("cd_turma");
				}
				else{
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_curso", "" + cdCursoMatricula, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_periodo_letivo", "" + instituicaoProximoPeriodo.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("st_turma", "" + TurmaServices.ST_ATIVO, ItemComparator.EQUAL, Types.INTEGER));
					rsmNovaTurma = TurmaDAO.find(criterios, connect);
					if(rsmNovaTurma.size() > 0){
						boolean encontrado = false;
						while(rsmNovaTurma.next()){
							Turma novaTurma = TurmaDAO.get(rsmNovaTurma.getInt("cd_turma"), connect);
							ResultSetMap rsmAlunosNovaTurma = TurmaServices.getAlunosAtivosOf(novaTurma.getCdTurma(), connect);
							if(novaTurma.getQtVagas() > (rsmAlunosNovaTurma.size() + (rsmAlunosNovaTurma.size() * 10/100))){
								cdNovaTurma = rsmNovaTurma.getInt("cd_turma");
								encontrado = true;
								break;
							}
						}
						
						if(!encontrado){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-2, "Turma de progressão não encontrada.");
						}
						
					}
					else{
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-2, "Turma de progressão não encontrada.");
					}
				}
				
			}	
					
			Turma turmaNova = TurmaDAO.get(cdNovaTurma, connect);
			Curso cursoTurma = CursoDAO.get(turmaNova.getCdCurso(), connect);
			
			Curso cursoNovo = CursoDAO.get(cdCursoMatricula, connect);
			
			//Matricula anterior é colocada como REPROVADA, caso tenha o status de Aprovada
			if(matriculaAnterior.getStAlunoCenso() == MatriculaServices.ST_ALUNO_CENSO_APROVADO){
				matriculaAnterior.setStAlunoCenso(MatriculaServices.ST_ALUNO_CENSO_REPROVADO);
				if(MatriculaDAO.update(matriculaAnterior, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar matricula anterior");
				}
			}
			
			//Cria a nova matricula, utilizando a nova turma, o novo curso e o novo periodo letivo
			Matricula matricula = new Matricula(0, matriculaAnterior.getCdMatriz(), cdNovaTurma, instituicaoProximoPeriodo.getCdPeriodoLetivo(), 
												com.tivic.manager.util.Util.getDataAtual(), null/*dtConclusao*/, stMatricula, matriculaAnterior.getTpMatricula(), 
												matriculaAnterior.getNrMatricula(), matriculaAnterior.getCdAluno(), matriculaAnterior.getCdMatricula(), 
												0/*cdReserva*/, matriculaAnterior.getCdAreaInteresse(), matriculaAnterior.getTxtObservacao(), matriculaAnterior.getTxtBoletim(), 
												cdCursoMatricula, 0/*cdPreMatricula*/, matriculaAnterior.getTpEscolarizacaoOutroEspaco(), 0/*lgTransportePublico*/, 
												matriculaAnterior.getTpPoderResponsavel(), matriculaAnterior.getTpFormaIngresso(), matriculaAnterior.getTxtDocumentoOficial(), null/*dtInterrupcao*/, 0/*lgAutorizacaoRematricula*/, 0/*lgAtividadeComplementar*/, 0/*lgReprovacao*/);
			Result resultado = MatriculaServices.save(matricula, 0, false, cdUsuario, connect);
			
			if(resultado.getCode() <= 0 && resultado.getCode() != MatriculaServices.ERR_ALUNO_FORA_IDADE){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return resultado;
			}
			
			int cdTipoOcorrenciaConservacao = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CONSERVACAO, connect).getCdTipoOcorrencia();
			
			OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno conservado da turma "+turmaAnterior.getNmTurma()+" da modalidade " + cursoAnterior.getNmProdutoServico() + " para a turma " + turmaNova.getNmTurma() + "/"+cursoTurma.getNmProdutoServico()+" na modalidade " + cursoNovo.getNmProdutoServico(), Util.getDataAtual(), cdTipoOcorrenciaConservacao, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), turmaAnterior.getCdTurma(), turmaNova.getCdTurma(), matricula.getStMatricula(), cdUsuario);
			OcorrenciaMatriculaServices.save(ocorrencia, connect);
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_MATRICULA", "" + matricula.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmMatriculaNova = MatriculaServices.find(criterios, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Matrícula progredida com sucesso.");
			result.addObject("RSM", rsmMatriculaNova);
			return result;
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que remove progressões feitas
	 * @param cdUsuario
	 * @param matriculas
	 * @return
	 */
	public static Result remocaoProgressaoMatricula(int cdUsuario, ArrayList<Integer> matriculas){
		return remocaoProgressaoMatricula(cdUsuario, matriculas, null);
	}
	
	public static Result remocaoProgressaoMatricula(int cdUsuario, ArrayList<Integer> matriculas, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//Itera sobre as matrícula, fazendo as remoções uma a uma
			for(int cdMatricula : matriculas){
				Result result = removerProgressaoMatricula(cdMatricula, cdUsuario, connect);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Matrículas removidas com sucesso.");
			return result;
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoServices.remocaoProgressaoMatricula: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removerProgressaoMatricula(int cdMatricula, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Matricula matriculaAnterior = MatriculaDAO.get(cdMatricula, connect);
			
			//Apenas matrículas ativas podem ter suas progressões removidas
			if(matriculaAnterior.getStMatricula() != MatriculaServices.ST_ATIVA){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Remoção de progressão não pode ser realizada para matrículas que não estejam Ativas");
			}
			
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula WHERE cd_matricula_origem = " + matriculaAnterior.getCdMatricula() + " AND cd_periodo_letivo > " + matriculaAnterior.getCdPeriodoLetivo()).executeQuery());
			if(rsm.next()){
				Matricula matricula = MatriculaDAO.get(rsm.getInt("cd_matricula"), connect);
				
				//Somente matrículas pendentes ou em transferência podem ser removidas
				if(matricula.getStMatricula() != MatriculaServices.ST_PENDENTE && matricula.getStMatricula() != MatriculaServices.ST_EM_TRANSFERENCIA){
					return new Result(-1, "Apenas matrículas pendentes ou em transferência podem ser removidas");
				}
				
				//Faz uma remoção completa da matrícula, incluindo suas ocorrências
				Result resultado = MatriculaServices.remove(matricula.getCdMatricula(), true, connect);
				if(resultado.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return resultado;
				}
				
				
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Matrículas removidas com sucesso.");
			return result;
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoServices.removerProgressaoMatricula: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result ativarPeriodoLetivo(int cdUsuario){
		return ativarPeriodoLetivo(cdUsuario, null);
	}
	
	public static Result ativarPeriodoLetivo(int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
		
			System.out.println("Inicio do processo de fechamento de periodo letivo");
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("st_periodo_letivo", "" + ST_PENDENTE, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_instituicao", "" + cdSecretaria, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoDAO.find(criterios, connect);
			if(!rsmPeriodoRecente.next()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não há um periodo pendente para atualizar");
			}
			
			InstituicaoPeriodo periodoLetivoSecretariaRecente = InstituicaoPeriodoDAO.get(rsmPeriodoRecente.getInt("cd_periodo_letivo"), connect);
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("st_periodo_letivo", "" + ST_ATUAL, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_instituicao", "" + cdSecretaria, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmPeriodoAtualSec = InstituicaoPeriodoDAO.find(criterios, connect);
			rsmPeriodoAtualSec.next();
			
			InstituicaoPeriodo periodoLetivoSecretariaAtual = InstituicaoPeriodoDAO.get(rsmPeriodoAtualSec.getInt("cd_periodo_letivo"), connect);
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_periodo_letivo_superior", "" + periodoLetivoSecretariaRecente.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
			rsmPeriodoRecente = InstituicaoPeriodoDAO.find(criterios, connect);
			while(rsmPeriodoRecente.next()){
				
				InstituicaoPeriodo instituicaoPeriodoRecente = InstituicaoPeriodoDAO.get(rsmPeriodoRecente.getInt("cd_periodo_letivo"));
				Instituicao instituicao = InstituicaoDAO.get(rsmPeriodoRecente.getInt("cd_instituicao"));
				
				System.out.println(instituicao.getNmPessoa());
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("st_periodo_letivo", "" + ST_ATUAL, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_instituicao", "" + instituicaoPeriodoRecente.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoDAO.find(criterios, connect);
				InstituicaoPeriodo instituicaoPeriodoAtual = null;
				if(rsmPeriodoAtual.next()){
					instituicaoPeriodoAtual = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
				}
				
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("st_matricula", "" + MatriculaServices.ST_ATIVA, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_periodo_letivo", "" + instituicaoPeriodoAtual.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmMatriculasAtual = MatriculaDAO.find(criterios, connect);
				while(rsmMatriculasAtual.next()){
					
					Matricula matriculaAtual = MatriculaDAO.get(rsmMatriculasAtual.getInt("cd_matricula"), connect);
					matriculaAtual.setStMatricula(MatriculaServices.ST_CONCLUIDA);
					if(MatriculaDAO.update(matriculaAtual, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar matrícula");
					}
					
					int cdTipoOcorrencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_MATRICULA, connect).getCdTipoOcorrencia();
					if(cdTipoOcorrencia > 0){
						Aluno aluno = AlunoDAO.get(matriculaAtual.getCdAluno(), connect);
						Turma turma = TurmaDAO.get(matriculaAtual.getCdTurma(), connect);
						
						OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matriculaAtual.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " teve a matrícula do periodo letivo de " + instituicaoPeriodoAtual.getNmPeriodoLetivo() + " concluída", Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 1, cdUsuario, matriculaAtual.getCdMatricula(), matriculaAtual.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), MatriculaServices.ST_ATIVA, cdUsuario);
						if(OcorrenciaMatriculaServices.save(ocorrencia, connect).getCode() <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao registrar ocorrencia de conclusao de matricula do aluno");
						}
					}
					
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("st_turma", "" + TurmaServices.ST_ATIVO, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_periodo_letivo", "" + instituicaoPeriodoAtual.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmTurmasAtual = TurmaDAO.find(criterios, connect);
				while(rsmTurmasAtual.next()){
					Turma turmaAtual = TurmaDAO.get(rsmTurmasAtual.getInt("cd_turma"), connect);
					turmaAtual.setStTurma(TurmaServices.ST_CONCLUIDO);
					if(TurmaDAO.update(turmaAtual, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar turma");
					}
					
					int cdTipoOcorrencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_TURMA, connect).getCdTipoOcorrencia();
					if(cdTipoOcorrencia > 0){
						Curso curso = CursoDAO.get(turmaAtual.getCdCurso(), connect);
						
						OcorrenciaTurma ocorrencia = new OcorrenciaTurma(0, instituicao.getCdPessoa(), "Turma " + curso.getNmProdutoServico() + " - " + turmaAtual.getNmTurma() + " da instituição "+instituicao.getNmPessoa()+" no periodo letivo de " + instituicaoPeriodoAtual.getNmPeriodoLetivo() + " foi concluída", Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 1, cdUsuario, turmaAtual.getCdTurma(), Util.getDataAtual(), cdUsuario, TurmaServices.ST_ATIVO, TurmaServices.ST_CONCLUIDO);
						if(OcorrenciaTurmaServices.save(ocorrencia, connect).getCode() <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao registrar ocorrencia de conclusao de turma");
						}
					}
				}
				
				
				instituicaoPeriodoAtual.setStPeriodoLetivo(ST_CONCLUIDO);
				if(InstituicaoPeriodoDAO.update(instituicaoPeriodoAtual, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar periodo letivo");
				}
				
				instituicaoPeriodoRecente.setStPeriodoLetivo(ST_ATUAL);
				if(InstituicaoPeriodoDAO.update(instituicaoPeriodoRecente, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar periodo letivo");
				}
				
				OcorrenciaInstituicao ocorrenciaInstituicao = new OcorrenciaInstituicao(0/*cdOcorrencia*/, instituicao.getCdInstituicao(), "Fechamento do período letivo de " + instituicaoPeriodoAtual.getNmPeriodoLetivo() + " e iniciado o período letivo de " + instituicaoPeriodoRecente.getNmPeriodoLetivo() + " para a instituição " + instituicao.getNmPessoa(), Util.getDataAtual(), TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_PERIODOS, connect).getCdTipoOcorrencia(), OcorrenciaServices.ST_CONCLUIDO, 1, cdUsuario, instituicao.getCdInstituicao(), Util.getDataAtual(), cdUsuario, instituicaoPeriodoRecente.getCdPeriodoLetivo());
				if(OcorrenciaInstituicaoDAO.insert(ocorrenciaInstituicao, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir ocorrência");
				}
			}
			
			periodoLetivoSecretariaRecente.setStPeriodoLetivo(ST_ATUAL);
			if(InstituicaoPeriodoDAO.update(periodoLetivoSecretariaRecente, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar periodo letivo");
			}
			
			periodoLetivoSecretariaAtual.setStPeriodoLetivo(ST_CONCLUIDO);
			if(InstituicaoPeriodoDAO.update(periodoLetivoSecretariaAtual, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar periodo letivo");
			}
			
			System.out.println("Fim do processo de fechamento de periodo letivo");
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Períodos fechados e iniciados com sucessos com sucesso.");
			return result;
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPeriodoDAO.get: " + e);
			return new Result(-1, "Erro ao executar InstituicaoPeriodoServices.ativarPeriodoLetivo");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result validacaoPeriodoLetivo(InstituicaoPeriodo periodo, int tpRelatorio, int cdUsuario){
		return validacaoPeriodoLetivo(periodo, tpRelatorio, cdUsuario, null);
	}
	
	public static Result validacaoPeriodoLetivo(InstituicaoPeriodo periodo, int tpRelatorio, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ValidatorResult resultadoValidacao = validate(periodo, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			Result resultValidacoesPendencia = InstituicaoPendenciaServices.atualizarValidacaoPendencia(resultadoValidacao, periodo.getCdInstituicao(), 0/*cdTurma*/, 0/*cdAluno*/, 0/*cdProfessor*/, InstituicaoPendenciaServices.TP_REGISTRO_PERIODO_LETIVO_CENSO, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			if(resultValidacoesPendencia.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return resultValidacoesPendencia;
			}
			
			
			
			if(isConnectionNull)
				connect.commit();
			
			Result result =  new Result(1, "Atualização na validação do periodo letivo realizada com sucesso");
			return result;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1);
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ValidatorResult validate(InstituicaoPeriodo instituicaoPeriodo, int cdUsuario, int idGrupo){
		return validate(instituicaoPeriodo, cdUsuario, idGrupo, null);
	}
	
	public static ValidatorResult validate(InstituicaoPeriodo instituicaoPeriodo, int cdUsuario, int idGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(instituicaoPeriodo == null){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new ValidatorResult(ValidatorResult.ERROR, "Périodo Letivo não encontrado");
			}
			
			InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(instituicaoPeriodo.getCdInstituicao(), instituicaoPeriodo.getCdPeriodoLetivo(), connect);
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoLetivoSecretaria = 0;
			ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoRecente.next()){
				cdPeriodoLetivoSecretaria = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			InstituicaoPeriodo instituicaoSecretaria = InstituicaoPeriodoDAO.get(cdPeriodoLetivoSecretaria, connect);
			
			ValidatorResult result = new ValidatorResult(ValidatorResult.VALID, "Instituição Período passou pela validação");
			HashMap<Integer, Validator> listValidator = getListValidation();
			
			
			//PERIODO LETIVO NOME - Verifica se existe um nome para o periodo letivo
			if(instituicaoPeriodo.getNmPeriodoLetivo() == null || instituicaoPeriodo.getNmPeriodoLetivo().trim().equals("")){
				listValidator.get(VALIDATE_NOME).add(ValidatorResult.ERROR, "Não foi registrado um nome do período letivo");
			}
						
			//HORARIOS - Verificar se existe horários cadastrados para a Instituição
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_instituicao", "" + instituicaoPeriodo.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_periodo_letivo", "" + instituicaoPeriodo.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmHorarios = InstituicaoHorarioDAO.find(criterios, connect);
			if(rsmHorarios == null || rsmHorarios.size() == 0){
				listValidator.get(VALIDATE_HORARIOS).add(ValidatorResult.ERROR, "Nenhum horário foi registrado", GRUPO_VALIDACAO_EDUCACENSO);
			}
			
			if(instituicaoEducacenso.getStInstituicaoPublica() == InstituicaoEducacensoServices.ST_EM_ATIVIDADE){
				if(instituicaoPeriodo.getDtInicial() == null || 
						instituicaoPeriodo.getDtInicial().get(Calendar.YEAR) < (instituicaoSecretaria.getDtInicial().get(Calendar.YEAR) - 1) || 
						instituicaoPeriodo.getDtInicial().get(Calendar.YEAR) > instituicaoSecretaria.getDtInicial().get(Calendar.YEAR)){
					listValidator.get(VALIDATE_DATA_INICIO_PERIODO).add(ValidatorResult.ERROR, "Data de inicio do ano letivo inexistente ou inválida");
				}
				if(instituicaoPeriodo.getDtFinal() == null || 
						instituicaoPeriodo.getDtFinal().get(Calendar.YEAR) < 
						instituicaoSecretaria.getDtFinal().get(Calendar.YEAR) || 
						instituicaoPeriodo.getDtFinal().get(Calendar.YEAR) > 
				(instituicaoSecretaria.getDtFinal().get(Calendar.YEAR) + 1)){
					listValidator.get(VALIDATE_DATA_TERMINO_PERIODO).add(ValidatorResult.ERROR, "Data de término do ano letivo inexistente ou inválida");
				}
			}
			
				
			//Faz a verificação das validações a partir do grupo que chamou
			result.addListValidator(listValidator);
			result.verify(idGrupo);
			//RETORNO
			return result;
		
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoServices.validate: " + e);
			return new ValidatorResult(ValidatorResult.ERROR, "Erro em InstituicaoServices.validate");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static HashMap<Integer, Validator> getListValidation(){
		HashMap<Integer, Validator> list = new HashMap<Integer, Validator>();
	
		list.put(VALIDATE_NOME, new Validator(VALIDATE_NOME, ValidatorResult.VALID));
		list.put(VALIDATE_HORARIOS, new Validator(VALIDATE_HORARIOS, ValidatorResult.VALID));
		list.put(VALIDATE_DATA_INICIO_PERIODO, new Validator(VALIDATE_DATA_INICIO_PERIODO, ValidatorResult.VALID));
		list.put(VALIDATE_DATA_TERMINO_PERIODO, new Validator(VALIDATE_DATA_TERMINO_PERIODO, ValidatorResult.VALID));
		
		return list;
	}
}
