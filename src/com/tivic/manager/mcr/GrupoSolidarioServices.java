package com.tivic.manager.mcr;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.Search;

public class GrupoSolidarioServices {
	public static int save(GrupoSolidario grupoSolidario){
		return save(grupoSolidario, null);
	}

	public static int save(GrupoSolidario grupoSolidario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			int retorno = 1;
			if(grupoSolidario.getCdGrupoSolidario()==0){
				retorno = GrupoSolidarioDAO.insert(grupoSolidario, connect);
				if(retorno > 0){
					grupoSolidario.setCdGrupoSolidario(retorno);
				}
			}
			else {
				retorno = GrupoSolidarioDAO.update(grupoSolidario, connect);
			}

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findGrupoSolidarioParticipante(ArrayList<sol.dao.ItemComparator> criterios) {
		return findGrupoSolidarioParticipante(criterios, null);
	}

	public static ResultSetMap findGrupoSolidarioParticipante(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.cd_pessoa, A.nm_pessoa, " +
				           "       B.nr_cpf, B.dt_nascimento " +
				           "FROM grl_pessoa A, grl_pessoa_fisica B " +
				           "WHERE (A.cd_pessoa = B.cd_pessoa) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap findPessoasGrupo(int cdGrupoSolidario) {
		return findPessoasGrupo(cdGrupoSolidario, null);
	}

	//Busca todos os parcipantes de um grupo solidário
	public static ResultSetMap findPessoasGrupo(int cdGrupoSolidario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, " +
											 "                         B.lg_coordenador, " +
						                     "                         C.cd_pessoa, C.nm_pessoa, " +
				                             "                         D.nr_cpf, D.dt_nascimento " +
				                             "FROM mcr_grupo_solidario A, " +
											 "     mcr_grupo_solidario_pessoa B, " +
											 "     grl_pessoa C, " +
											 "     grl_pessoa_fisica D " +
											 "WHERE (A.cd_grupo_solidario = B.cd_grupo_solidario) " +
											 "  AND (B.cd_pessoa = C.cd_pessoa) " +
											 "  AND (C.cd_pessoa = D.cd_pessoa) " +
											 "  AND (B.cd_grupo_solidario = ?)");
			pstmt.setInt(1, cdGrupoSolidario);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoSolidarioServices.findPessoasGrupo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	//Busca os grupos solidário que uma pessoa faz parte
	public static ResultSetMap findGruposPessoa(ArrayList<sol.dao.ItemComparator> criterios) {
		return findGruposPessoa(criterios, null);
	}

	public static ResultSetMap findGruposPessoa(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, " +
				 		   "     B.cd_pessoa, B.lg_coordenador " +
	 			 		   "FROM mcr_grupo_solidario A, " +
				 		   "     mcr_grupo_solidario_pessoa B " +
				 		   "WHERE (A.cd_grupo_solidario = B.cd_grupo_solidario) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}