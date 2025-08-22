package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletRequest;

import com.lowagie.text.pdf.AcroFields.Item;
import com.tivic.manager.acd.ProfessorDisciplina;
import com.tivic.manager.acd.ProfessorDisciplinaDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Formulario;
import com.tivic.manager.grl.FormularioDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.RequestUtilities;
import sol.util.Result;

public class GrupoServices {
	public static final int EVT_ADESAO = 0;
	public static final int EVT_CONTRATACAO = 1;

	public static final int CAT_RECEITA = 0;
	public static final int CAT_DESPESA = 1;


	public static int getEventoFinanceiro(int cdGrupo, int tpEvento) {
		return getEventoFinanceiro(cdGrupo, tpEvento, null);
	}

	public static int getEventoFinanceiro(int cdGrupo, int tpEvento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			Grupo grupo = GrupoDAO.get(cdGrupo, connection);
			return tpEvento==GrupoServices.EVT_ADESAO ? (grupo.getCdEventoAdesaoContrato()!=0 ?
					grupo.getCdEventoAdesaoContrato() : grupo.getCdGrupoSuperior()!=0 ? getEventoFinanceiro(grupo.getCdGrupoSuperior(), tpEvento, connection) : 0) :
						grupo.getCdEventoContratacao()!=0 ? grupo.getCdEventoContratacao() :
							grupo.getCdGrupoSuperior()!=0 ? getEventoFinanceiro(grupo.getCdGrupoSuperior(), tpEvento, connection) : 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getEventoFinanceiro: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int getCategoriaEconomomica(int cdGrupo, int tpCategoria) {
		return getCategoriaEconomomica(cdGrupo, tpCategoria, null);
	}

	public static int getCategoriaEconomomica(int cdGrupo, int tpCategoria, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			Grupo grupo = GrupoDAO.get(cdGrupo, connection);
			return tpCategoria==GrupoServices.CAT_RECEITA ? (grupo.getCdCategoriaReceita()!=0 ?
					grupo.getCdCategoriaReceita() : grupo.getCdGrupoSuperior()!=0 ? getCategoriaEconomomica(grupo.getCdGrupoSuperior(), tpCategoria, connection) : 0) :
						grupo.getCdCategoriaDespesa()!=0 ? grupo.getCdCategoriaDespesa() :
							grupo.getCdGrupoSuperior()!=0 ? getCategoriaEconomomica(grupo.getCdGrupoSuperior(), tpCategoria, connection) : 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getEventoFinanceiro: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllHierarquia() {
		return getAllHierarquia(null);
	}

	public static ResultSetMap getAllHierarquia(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
	
			ResultSetMap rsm = getAllSuperiores(connect);
			while (rsm.next()) {
				rsm.setValueToField("subResultSetMap", getAllHierarquiaAux(rsm.getInt("cd_grupo"), connect));
			}
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_GRUPO");
			//rsm.orderBy(fields, true);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoServices.getAllHierarquia: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllHierarquiaAux(int cdGrupo, Connection connect){
		ResultSetMap rsmFilhos = getAllFilhos(cdGrupo, connect);
		while(rsmFilhos.next()){
			rsmFilhos.setValueToField("subResultSetMap", getAllHierarquiaAux(rsmFilhos.getInt("cd_grupo"), connect));
		}
		return rsmFilhos;
	}
	
//	public static ResultSetMap getAllHierarquia(Connection connect) {
//		ResultSetMap rsm = getAllCompleto(connect);
//		System.out.println("rsm = " + rsm);
//		while (rsm != null && rsm.next()) {
//			if (rsm.getInt("CD_GRUPO_SUPERIOR") != 0) {
//				int pointer = rsm.getPointer();
//				int cdGrupo = rsm.getInt("CD_GRUPO_SUPERIOR");
//				HashMap<String,Object> register = rsm.getRegister();
//				if (rsm.locate("CD_GRUPO", new Integer(rsm.getInt("CD_GRUPO_SUPERIOR")), false, true)) {
//					HashMap<String,Object> parentNode = rsm.getRegister();
//					boolean isFound = rsm.getInt("CD_GRUPO")==cdGrupo;
//					ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
//					while (!isFound && subRsm!=null) {
//						subRsm = parentNode==null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
//						parentNode = subRsm==null ? null : subRsm.getRegister();
//						isFound = subRsm==null ? false : subRsm.getInt("CD_GRUPO")==cdGrupo;
//					}
//					subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
//					if (subRsm==null) {
//						subRsm = new ResultSetMap();
//						parentNode.put("subResultSetMap", subRsm);
//					}
//					subRsm.addRegister(register);
//					rsm.getLines().remove(register);
//					pointer--;
//				}
//				rsm.goTo(pointer);
//			}
//		}
//		ArrayList<String> fields = new ArrayList<String>();
//		fields.add("NM_GRUPO");
//		//rsm.orderBy(fields, true);
//		return rsm;
//	}

	public static ResultSetMap getAllAtributosOfGrupo(int cdGrupo) {
		return getAllAtributosOfGrupo(cdGrupo, null);
	}

	public static ResultSetMap getAllAtributosOfGrupo(int cdGrupo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* " +
															   "FROM grl_formulario_atributo A, grl_formulario B, alm_grupo C " +
															   "WHERE A.cd_formulario = B.cd_formulario " +
															   "  AND B.cd_formulario = C.cd_formulario " +
															   "  AND C.cd_grupo = ? ");
			pstmt.setInt(1, cdGrupo);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result save(Grupo grupo){
		return save(grupo, null);
	}

	public static Result save(Grupo grupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(grupo==null)
				return new Result(-1, "Erro ao salvar. Grupo é nulo");

			if(grupo.getCdGrupo()==0){
				grupo = insert(grupo, connect);
			}
			else {
				grupo = update(grupo, connect);
			}

			if(grupo == null)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(grupo == null?-1:1, (grupo == null)?"Erro ao salvar...":"Salvo com sucesso...", "GRUPO", grupo);
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
	
	public static Grupo insert(Grupo objeto) {
		return insert(objeto, null, null);
	}
	
	public static Grupo insert(Grupo objeto, Connection connect) {
		return insert(objeto, null, connect);
	}

	public static Grupo insert(Grupo objeto, Formulario formulario) {
		return insert(objeto, formulario, null);
	}

	public static Grupo insert(Grupo objeto, Formulario formulario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (objeto.getCdFormulario() <= 0 && formulario != null) {
				int cdFormulario = 0;
				cdFormulario = FormularioDAO.insert(formulario, connect);
				if (cdFormulario <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
				objeto.setCdFormulario(cdFormulario);
			}

			int cdGrupo = GrupoDAO.insert(objeto, connect);
			if (cdGrupo <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return null;
			}

			if (isConnectionNull)
				connect.commit();

			return objeto;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Grupo update(Grupo objeto) {
		return update(objeto, null, null);
	}
	
	public static Grupo update(Grupo objeto, Connection connect) {
		return update(objeto, null, connect);
	}

	public static Grupo update(Grupo objeto, Formulario formulario) {
		return update(objeto, formulario, null);
	}

	public static Grupo update(Grupo objeto, Formulario formulario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (formulario != null) {
				if (objeto.getCdFormulario() <= 0) {
					int cdFormulario = FormularioDAO.insert(formulario, connect);
					if (cdFormulario <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return null;
					}
					objeto.setCdFormulario(cdFormulario);
				}
				else if (FormularioDAO.update(formulario, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
			}

			int cdGrupo = GrupoDAO.update(objeto, connect);
			if (cdGrupo <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return null;
			}

			if (isConnectionNull)
				connect.commit();

			return objeto;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllCompleto() {
		return getAllCompleto(null);
	}

	public static ResultSetMap getAllCompleto(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_formulario, B.id_formulario " +
															   "FROM alm_grupo A " +
															   "LEFT OUTER JOIN grl_formulario B ON (A.cd_formulario = B.cd_formulario)" +
															   "WHERE st_grupo = 1");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllSuperiores() {
		return getAllSuperiores(null);
	}

	public static ResultSetMap getAllSuperiores(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_formulario, B.id_formulario " +
															   "FROM alm_grupo A " +
															   "LEFT OUTER JOIN grl_formulario B ON (A.cd_formulario = B.cd_formulario)" +
															   "WHERE st_grupo = 1 " + 
															   "  AND cd_grupo_superior IS NULL");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllFilhos(int cdGrupoSuperior) {
		return getAllFilhos(cdGrupoSuperior, null);
	}

	public static ResultSetMap getAllFilhos(int cdGrupoSuperior, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_formulario, B.id_formulario " +
															   "FROM alm_grupo A " +
															   "LEFT OUTER JOIN grl_formulario B ON (A.cd_formulario = B.cd_formulario)" +
															   "WHERE st_grupo = 1 " + 
															   "  AND cd_grupo_superior = " + cdGrupoSuperior);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllGruposCadastroIsolado() {
		return getAllGruposCadastroIsolado(null);
	}

	public static ResultSetMap getAllGruposCadastroIsolado(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM alm_grupo " +
					                                           "WHERE cd_formulario IS NOT NULL").executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM ALM_GRUPO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getProdutoServicoOfGrupo(int cdGrupo) {
		return getProdutoServicoOfGrupo(cdGrupo, 0, false, false, null);
	}

	public static ResultSetMap getProdutoServicoOfGrupo(int cdGrupo, boolean showItensSubGrupos) {
		return getProdutoServicoOfGrupo(cdGrupo, 0, showItensSubGrupos, false, null);
	}

	public static ResultSetMap getProdutoServicoOfGrupo(int cdGrupo, int cdEmpresa) {
		return getProdutoServicoOfGrupo(cdGrupo, cdEmpresa, false, false, null);
	}

	public static ResultSetMap getProdutoServicoOfGrupo(int cdGrupo, int cdEmpresa, boolean showItensSubGrupos) {
		return getProdutoServicoOfGrupo(cdGrupo, cdEmpresa, showItensSubGrupos, false, null);
	}

	public static ResultSetMap getProdutoServicoOfGrupo(int cdGrupo, int cdEmpresa, boolean showItensSubGrupos, boolean isPrincipal) {
		return getProdutoServicoOfGrupo(cdGrupo, cdEmpresa, showItensSubGrupos, isPrincipal, null);
	}

	public static ResultSetMap getProdutoServicoOfGrupo(int cdGrupo, int cdEmpresa, boolean showItensSubGrupos,
			boolean isPrincipal, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.cd_empresa, A.cd_grupo, A.lg_principal, B.*, C.nr_ordem, D.nm_grupo, E.id_reduzido, B.id_produto_servico " +
					"FROM alm_produto_grupo A " +
					"JOIN alm_grupo D ON (A.cd_grupo = D.cd_grupo) " +
					"JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					"LEFT OUTER JOIN grl_produto_servico_empresa E ON (B.cd_produto_servico = E.cd_produto_servico AND E.cd_empresa = ?) " +
					"LEFT OUTER JOIN grl_produto_servico_empresa C ON (B.cd_produto_servico = C.cd_produto_servico AND " +
					"												   C.cd_empresa = ?) "+
                    "WHERE (A.cd_grupo = ? " + (showItensSubGrupos ? " OR (D.cd_grupo_superior = ?)" : "") + ") " +
                    "  AND A.cd_empresa = ? " +
                    (isPrincipal ? "  AND A.lg_principal = 1 " : ""));
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdGrupo);
			int i = 4;
			if (showItensSubGrupos)
				pstmt.setInt(i++, cdGrupo);
			pstmt.setInt(i++, cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> criterios = new ArrayList<String>();
			criterios.add("NR_ORDEM");
			rsm.orderBy(criterios);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoServices.getProdutoServicoOfGrupo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static String findCdGrupo(int cdGrupo) {
		return findCdGrupo(cdGrupo, null);
	}

	public static String findCdGrupo(int cdGrupo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement("SELECT A.* FROM alm_grupo A WHERE A.cd_grupo = "+cdGrupo).executeQuery();;
			if (rs.next())
				return rs.getString("nm_grupo");
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result findIdGrupo(String idGrupo) {
		return findIdGrupo(idGrupo, null);
	}

	public static Result findIdGrupo(String idGrupo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			Result result = new Result(1);
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement("SELECT A.* FROM alm_grupo A WHERE A.id_grupo = '"+idGrupo+"'").executeQuery();;
			if (rs.next()){
				result.addObject("CD_GRUPO", rs.getString("cd_grupo"));
				result.addObject("NM_GRUPO", rs.getString("nm_grupo"));
				return result;
			}
			else{
				result.setCode(-1);
				return result;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static final int transferProdutosServicos(int cdEmpresa, int cdGrupoOld, int cdGrupo, ServletRequest request) {
		return transferProdutosServicos(cdEmpresa, cdGrupoOld, cdGrupo, request, null);
	}

	public static final int transferProdutosServicos(int cdEmpresa, int cdGrupoOld, int cdGrupo, ServletRequest request, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int countParametros = RequestUtilities.getAsInteger(request, "countParams", 0);
			for (int i=0; i<countParametros; i++) {
				int cdProdutoServico = RequestUtilities.getAsInteger(request, "cdProdutoServico_" + (i+1), 0);
				ProdutoGrupo itemOld = ProdutoGrupoDAO.get(cdProdutoServico, cdGrupoOld, cdEmpresa, connection);
				if (itemOld != null) {
					if (ProdutoGrupoDAO.delete(cdProdutoServico, cdGrupoOld, cdEmpresa, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
					ProdutoGrupo item = ProdutoGrupoDAO.get(cdProdutoServico, cdGrupo, cdEmpresa, connection);
					if (item==null) {
						if (ProdutoGrupoServices.insert(new ProdutoGrupo(cdProdutoServico, cdGrupo, cdEmpresa, itemOld.getLgPrincipal()), true, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return -1;
						}
					}
					else {
						item.setLgPrincipal(itemOld.getLgPrincipal());
						if (item.getLgPrincipal() == 1) {
							PreparedStatement pstmt = connection.prepareStatement("UPDATE alm_produto_grupo " +
									"SET lg_principal = 0 " +
									"WHERE cd_produto_servico = ? " +
									"  AND cd_empresa = ? " +
									"  AND lg_principal = 1");
							pstmt.setInt(1, cdProdutoServico);
							pstmt.setInt(2, cdEmpresa);
							pstmt.execute();
						}
						if (ProdutoGrupoDAO.update(item, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return -1;
						}
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnNull = (connection==null);
		try	{
			if(isConnNull)
				connection = Conexao.conectar();
			ResultSetMap rsm = Search.find("SELECT A.*, B.nm_grupo AS nm_grupo_superior " +
					"FROM alm_grupo            A " +
					"LEFT OUTER JOIN alm_grupo B ON (A.cd_grupo_superior = B.cd_grupo) " +
					"WHERE A.st_grupo = 1","ORDER BY A.nm_grupo", 
					criterios, connection!=null ? connection : Conexao.conectar(),connection==null);
			while(rsm.next()) {
				int nrNivel = 0;
				int cdGrupoSuperior = rsm.getInt("cd_grupo_superior");
				String dsHierarquia = "";
				while(cdGrupoSuperior > 0 && rsm.getInt("cd_grupo") != cdGrupoSuperior) {
					nrNivel++;
					Grupo grupo = GrupoDAO.get(cdGrupoSuperior, connection);
					if(grupo==null)
						break;
					dsHierarquia    = grupo.getNmGrupo() + dsHierarquia;
					cdGrupoSuperior = grupo.getCdGrupoSuperior();
				}
				rsm.setValueToField("DS_HIERARQUIA", dsHierarquia + rsm.getString("nm_grupo"));
				rsm.setValueToField("DS_GRUPO", Util.fill("", nrNivel*5, ' ', 'E')+rsm.getString("nm_grupo"));
			}
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("DS_HIERARQUIA");
			rsm.orderBy(orderBy);
			rsm.beforeFirst();
			return rsm;
		}
		finally {
			if(isConnNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getHierarquiaOfGrupo(int cdGrupo) {
		return getHierarquiaOfGrupo(cdGrupo, null);
	}

	public static ResultSetMap getHierarquiaOfGrupo(int cdGrupo, Connection connection) {
		boolean isConnNull = (connection==null);
		try	{
			if(isConnNull)
				connection = Conexao.conectar();
			
			Grupo grupo = GrupoDAO.get(cdGrupo);
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
																  " FROM alm_grupo            A " +
																  " WHERE A.st_grupo = 1 AND A.cd_grupo = ?");
			
			ResultSetMap rsmFinal = new ResultSetMap();
			cdGrupo = grupo.getCdGrupoSuperior();
			while(true) {
				pstmt.setInt(1, cdGrupo);
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				if(rsm.next()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("GRUPO", rsm.getString("nm_grupo"));
					rsmFinal.addRegister(register);
					cdGrupo = rsm.getInt("cd_grupo_superior");
				}
				if(cdGrupo==0)
					break;
			}
			rsmFinal.beforeFirst();
			return rsmFinal;
		}
		
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnNull)
				Conexao.desconectar(connection);
		}
	}

	public static String getAllCombustivel(int cdEmpresa, Connection connect) throws SQLException {
		if(connect==null)
			connect = Conexao.conectar();
		int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
		String codigos = "("+cdGrupoCombustivel;
		ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM alm_grupo WHERE cd_grupo_superior = " + cdGrupoCombustivel).executeQuery());
		while(rsm.next()){
			codigos += "," + rsm.getInt("cd_grupo");
		}
		codigos += ")";
		return codigos;
	}
	
	public static ArrayList<Integer> getAllCombustivelAsArray(int cdEmpresa, Connection connect) throws SQLException {
		if(connect==null)
			connect = Conexao.conectar();
		int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
		ArrayList<Integer> codigos = new ArrayList<Integer>();
		codigos.add(cdGrupoCombustivel);
		ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM alm_grupo WHERE (cd_grupo_superior = " + cdGrupoCombustivel + " OR cd_grupo = " + cdGrupoCombustivel + ")").executeQuery());
		while(rsm.next()){
			codigos.add(rsm.getInt("cd_grupo"));
		}
		return codigos;
	}
	
	public static String getAllLubrificante(int cdEmpresa, Connection connect) throws SQLException {
		int cdGrupoLubrificante = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_LUBRIFICANTE", 0, cdEmpresa);
		String codigos = "("+cdGrupoLubrificante;
		ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM alm_grupo WHERE cd_grupo_superior = " + cdGrupoLubrificante).executeQuery());
		while(rsm.next()){
			codigos += "," + rsm.getInt("cd_grupo");
		}
		codigos += ")";
		return codigos;
	}
	
	public static ArrayList<Integer> getAllLubrificanteAsArray(int cdEmpresa, Connection connect) throws SQLException {
		int cdGrupoLubrificante = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_LUBRIFICANTE", 0, cdEmpresa);
		ArrayList<Integer> codigos = new ArrayList<Integer>();
		codigos.add(cdGrupoLubrificante);
		ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM alm_grupo WHERE cd_grupo_superior = " + cdGrupoLubrificante).executeQuery());
		while(rsm.next()){
			codigos.add(rsm.getInt("cd_grupo"));
		}
		return codigos;
	}
	
	public static int isCombustivel(int cdEmpresa, int cdGrupo){
		Connection connect = Conexao.conectar();
		try{
			int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
			if(cdGrupoCombustivel == 0)
				return 0;
			if(cdGrupo == cdGrupoCombustivel) 
				return 1;
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM alm_grupo WHERE cd_grupo_superior = " + cdGrupoCombustivel).executeQuery());
			while(rsm.next()){
				if(cdGrupo == rsm.getInt("cd_grupo"))
					return 1;
			}
			
			return 0;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findPrincipais(ArrayList<ItemComparator> criterios) {
		return findPrincipais(criterios, null);
	}

	public static ResultSetMap findPrincipais(ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnNull = (connection==null);
		try	{
			if(isConnNull)
				connection = Conexao.conectar();
			ResultSetMap rsm = Search.find("SELECT A.* " +
					"FROM alm_grupo A " +
					"WHERE A.st_grupo = 1 "+ 
					"  AND A.cd_grupo_superior IS NULL","ORDER BY A.nm_grupo", 
					criterios, connection!=null ? connection : Conexao.conectar(),connection==null);
			return rsm;
		}
		finally {
			if(isConnNull)
				Conexao.desconectar(connection);
		}
	}
}
