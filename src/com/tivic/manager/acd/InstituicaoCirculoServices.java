package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaEnderecoServices;

import groovy.transform.ToString;

public class InstituicaoCirculoServices {

	public static Result save(InstituicaoCirculo instituicaoCirculo){
		return save(instituicaoCirculo, null);
	}

	public static Result save(InstituicaoCirculo instituicaoCirculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoCirculo==null)
				return new Result(-1, "Erro ao salvar. InstituicaoCirculo é nulo");

			int retorno;
			if(InstituicaoCirculoDAO.get(instituicaoCirculo.getCdCirculo(), instituicaoCirculo.getCdInstituicao(), connect) == null){
				retorno = InstituicaoCirculoDAO.insert(instituicaoCirculo, connect);
			}
			else {
				retorno = InstituicaoCirculoDAO.update(instituicaoCirculo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOCIRCULO", instituicaoCirculo);
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

	public static Result remove(int cdCirculo, int cdInstituicao){
		return remove(cdCirculo, cdInstituicao, false, null);
	}
	public static Result remove(int cdCirculo, int cdInstituicao, boolean cascade){
		return remove(cdCirculo, cdInstituicao, cascade, null);
	}
	public static Result remove(int cdCirculo, int cdInstituicao, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = InstituicaoCirculoDAO.delete(cdCirculo, cdInstituicao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_circulo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllComCirculo() {
		return getAllComCirculo(null);
	}

	public static ResultSetMap getAllComCirculo(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdPeriodoRecenteSuperior = 0;
			ResultSetMap rsmPeriodo = InstituicaoServices.getPeriodoLetivoRecente(ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0, 0, connect), connect);
			if(rsmPeriodo.next())
				cdPeriodoRecenteSuperior = rsmPeriodo.getInt("cd_periodo_letivo");
			
			pstmt = connect.prepareStatement(
						"SELECT A.cd_circulo, A.cd_instituicao, J.nm_pessoa as nm_instituicao, COUNT(E.cd_matricula) AS NR_ALUNOS, COUNT(E1.cd_matricula) AS NR_ALUNOS_EXT 																	" + 
						"  FROM acd_instituicao_circulo 				A 																											" +
						"  JOIN acd_circulo 							B 	ON (A.cd_circulo 		= B.cd_circulo) 																" +
						"  LEFT OUTER JOIN acd_turma 					C	ON (A.cd_instituicao 	= C.cd_instituicao 		AND C.st_turma = 1 AND C.cd_instituicao_ext is null) 	" +
						"  LEFT OUTER JOIN acd_turma 					C1	ON (A.cd_instituicao 	= C1.cd_instituicao_ext	AND C1.st_turma = 1) 									" +
						"  LEFT OUTER JOIN acd_instituicao_periodo 		D 	ON (C.cd_periodo_letivo = D.cd_periodo_letivo 	AND D.cd_periodo_letivo_superior = ?) 					" +
						"  LEFT OUTER JOIN acd_instituicao_educacenso 	ED 	ON (D.cd_periodo_letivo = ED.cd_periodo_letivo 	AND C.cd_instituicao = ED.cd_instituicao)				" +
						"  LEFT OUTER JOIN acd_matricula 				E 	ON (C.cd_turma 			= E.cd_turma 			AND E.st_matricula = 0) 								" +
						"  LEFT OUTER JOIN acd_matricula 				E1 	ON (C1.cd_turma 		= E1.cd_turma 			AND E1.st_matricula = 0) 								" +
						"  LEFT OUTER JOIN acd_curso 					F 	ON (E.cd_curso 			= F.cd_curso) 																	" +
						"  LEFT OUTER JOIN acd_curso 					F1 	ON (E1.cd_curso 		= F1.cd_curso) 																	" +
						"  LEFT OUTER JOIN acd_curso_etapa 				G 	ON (F.cd_curso 			= G.cd_curso) 																	" +
						"  LEFT OUTER JOIN acd_curso_etapa 				G1 	ON (F1.cd_curso 		= G1.cd_curso) 																	" +
						"  LEFT OUTER JOIN acd_tipo_etapa 				H 	ON (G.cd_etapa 			= H.cd_etapa 			AND H.lg_eja = 0)										" +
						"  LEFT OUTER JOIN acd_tipo_etapa 				H1 	ON (G1.cd_etapa 		= H1.cd_etapa 			AND H1.lg_eja = 0)										" +
						"  LEFT OUTER JOIN alm_circulo_modalidade 		I 	ON (B.cd_circulo 		= I.cd_circulo) 																" +
						"  LEFT OUTER JOIN grl_pessoa 					J 	ON (A.cd_instituicao 	= J.cd_pessoa) 																	" +
						" WHERE B.tp_circulo = ? 																																	" +
					   	"GROUP BY A.cd_circulo, A.cd_instituicao, J.nm_pessoa 																										" 
					);
			
			pstmt.setInt(1, cdPeriodoRecenteSuperior);
			pstmt.setInt(2, CirculoServices.TP_DISTRIBUICAO_MERENDA);
			
			ResultSetMap rsmInstituicoes = new ResultSetMap(pstmt.executeQuery());
			
			while(rsmInstituicoes.next()) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> rsmEnderecoPrincipal = (HashMap<String, Object>)PessoaEnderecoServices.getEnderecoPrincipalCompleto(rsmInstituicoes.getInt("CD_INSTITUICAO"), connect).getObjects().get("PESSOAENDERECO");
				String nmLogradouro = (String)rsmEnderecoPrincipal.get("NM_LOGRADOURO");
				String nrEndereco	= (String)rsmEnderecoPrincipal.get("NR_ENDERECO");
				String nmBairro     = (String)rsmEnderecoPrincipal.get("NM_BAIRRO");
				String nrCep		= (String)rsmEnderecoPrincipal.get("NR_CEP");
//				String nmCidade 	= (String)rsmEnderecoPrincipal.get("NM_CIDADE");
//				String sgEstado 	= (String)rsmEnderecoPrincipal.get("SG_ESTADO");
				
				rsmInstituicoes.setValueToField("DS_ENDERECO", nmLogradouro + " " + nrEndereco + ", " + nmBairro + " - " + nrCep);
				
				if(rsmInstituicoes.getInt("NR_ALUNOS") == 0 && rsmInstituicoes.getInt("NR_ALUNOS_EXT") == 0)
					rsmInstituicoes.setValueToField("_LG_SHOW_CHECKBOX", false);
				else if (rsmInstituicoes.getInt("NR_ALUNOS") == 0)
					rsmInstituicoes.setValueToField("NR_ALUNOS", rsmInstituicoes.getInt("NR_ALUNOS_EXT"));
			}
			
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NR_ALUNOS DESC");
			rsmInstituicoes.orderBy(orderBy);
			
			return rsmInstituicoes;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoServices.getAllByCirculo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoServices.getAllByCirculo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByCirculoModalidade(){
		return getAllByCirculoModalidade(null);
	}
	public static ResultSetMap getAllByCirculoModalidade(Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String query = "SELECT COUNT(E.cd_matricula) AS qt_alunos, COUNT(E1.cd_matricula) AS qt_alunos_ext 		" + 
						   "FROM acd_instituicao_circulo 	A 														" +
						   "JOIN acd_circulo 				B 	ON (A.cd_circulo 		= B.cd_circulo)				" +
						   "JOIN acd_turma 					C 	ON (A.cd_instituicao 	= C.cd_instituicao)			" +
						   "JOIN acd_turma 					C1 	ON (A.cd_instituicao	= C1.cd_instituicao_ext)	" +
						   "JOIN acd_instituicao_periodo 	D 	ON (C.cd_periodo_letivo = D.cd_periodo_letivo) 		" +
						   "JOIN acd_instituicao_periodo 	D1 	ON (C1.cd_periodo_letivo = D1.cd_periodo_letivo) 	" +
						   "JOIN acd_matricula 				E 	ON (C.cd_turma 			= E.cd_turma) 				" +
						   "JOIN acd_matricula 				E1 	ON (C1.cd_turma 		= E1.cd_turma) 				" +
						   "JOIN acd_curso 					F 	ON (E.cd_curso 			= F.cd_curso) 				" +
						   "JOIN acd_curso 					F1 	ON (E1.cd_curso 		= F1.cd_curso) 				" +
						   "JOIN acd_curso_etapa 			G 	ON (F.cd_curso 			= G.cd_curso) 				" + 
						   "JOIN acd_curso_etapa 			G1 	ON (F1.cd_curso 		= G1.cd_curso) 				" + 
						   "JOIN acd_tipo_etapa 			H 	ON (G.cd_etapa 			= H.cd_etapa) 				" +
						   "JOIN acd_tipo_etapa 			H1 	ON (G1.cd_etapa 		= H1.cd_etapa) 				" +
						   "JOIN alm_circulo_modalidade 	I 	ON (B.cd_circulo 		= I.cd_circulo) 			" +
						   
						   "WHERE A.cd_circulo     				= ? 	" +
						   "  AND A.cd_instituicao 				= ? 	" +
						   "  AND B.tp_circulo     				= 2 	" +
						   "  AND C.st_turma       				= 1 	" +
						   "  AND D.cd_periodo_letivo_superior 	= 1347 	" +
						   "  AND E.st_matricula   				= 0 	" + 
						   "  AND H.lg_eja         				= 0 	" + 
						   "GROUP BY B.nm_circulo";	
			
			ResultSetMap rsmInstituicoes = InstituicaoServices.getAll(0, false, true, connect);
			
			while(rsmInstituicoes.next()) {
				pstmt = connect.prepareStatement(query);
				pstmt.setInt(1, 27);
				pstmt.setInt(2, rsmInstituicoes.getInt("CD_INSTITUICAO"));
							
				ResultSetMap rs = new ResultSetMap(pstmt.executeQuery());

				System.out.println(rs.toString());
			}
			
			return rsmInstituicoes;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoServices.getAllByCirculoModalidade: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoServices.getAllByCirculoModalidade: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByCirculo(int cdCirculo){
		return getAllByCirculo(cdCirculo, null);
	}
	
	public static ResultSetMap getAllByCirculo(int cdCirculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();

		PreparedStatement pstmt;
		try {
			String query = "SELECT A.nm_pessoa as nm_instituicao, B.* " + 
						   "  FROM grl_pessoa A, acd_instituicao_circulo B, acd_circulo C" + 
						   " WHERE A.cd_pessoa  = B.cd_instituicao " + 
						   "   AND B.cd_circulo = C.cd_circulo " + 
						   "   AND B.cd_circulo = ? ";	
			
			pstmt = connect.prepareStatement(query);
			pstmt.setInt(1, cdCirculo);
			
			ResultSetMap rsmInstituicoes = new ResultSetMap(pstmt.executeQuery());
			
			return rsmInstituicoes;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoServices.getAllByCirculoModalidade: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoServices.getAllByCirculoModalidade: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_circulo A, acd_circulo B WHERE A.cd_circulo = B.cd_circulo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
