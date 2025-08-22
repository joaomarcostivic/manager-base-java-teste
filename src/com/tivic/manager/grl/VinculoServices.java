package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Formulario;
import com.tivic.manager.grl.FormularioDAO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class VinculoServices {

	public static final int PARCEIRO   = 1;
	public static final int CORRETOR   = 2;
	public static final int PROMOTOR   = 3;

	public static Result save(Vinculo vinculo){
		return save(vinculo, null);
	}
	
	public static Result save(Vinculo vinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(vinculo==null)
				return new Result(-1, "Erro ao salvar. Vínculo é nulo");
			
			int retorno;
			if(vinculo.getCdVinculo()==0){
				retorno = VinculoDAO.insert(vinculo, connect);
				vinculo.setCdVinculo(retorno);
			}
			else {
				retorno = VinculoDAO.update(vinculo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VINCULO", vinculo);
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
	
	public static Result remove(int cdVinculo){
		return remove(cdVinculo, false, null);
	}
	
	public static Result remove(int cdVinculo, boolean cascade){
		return remove(cdVinculo, cascade, null);
	}
	
	public static Result remove(int cdVinculo, boolean cascade, Connection connect){
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
				retorno = VinculoDAO.delete(cdVinculo, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este vínculo está associado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Vínculo excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir vínculo!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	public static Vinculo insert(Vinculo objeto) {
		return insert(objeto, null, null);
	}

	public static Vinculo insert(Vinculo objeto, Formulario formulario) {
		return insert(objeto, formulario, null);
	}

	public static Vinculo insert(Vinculo objeto, Formulario formulario, Connection connect){
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

			int cdVinculo = VinculoDAO.insert(objeto, connect);
			if (cdVinculo <= 0) {
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

	public static Vinculo update(Vinculo objeto) {
		return update(objeto, null, null);
	}

	public static Vinculo update(Vinculo objeto, Formulario formulario) {
		return update(objeto, formulario, null);
	}

	public static Vinculo update(Vinculo objeto, Formulario formulario, Connection connect){
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

			int cdVinculo = VinculoDAO.update(objeto, connect);
			if (cdVinculo <= 0) {
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_formulario " +
											 "FROM grl_vinculo A " +
											 "   LEFT OUTER JOIN grl_formulario B ON (A.cd_formulario = B.cd_formulario)"
											 + " ORDER BY A.nm_vinculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllVinculosCadastroIsolado() {
		return getAllVinculosCadastroIsolado(null);
	}

	public static ResultSetMap getAllVinculosCadastroIsolado(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM grl_vinculo " +
					"WHERE cd_formulario > 0");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoServices.getAllVinculosCadastroIsolado: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllAtributosOfVinculo(int cdVinculo) {
		return getAllAtributosOfVinculo(cdVinculo, null);
	}

	public static ResultSetMap getAllAtributosOfVinculo(int cdVinculo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* " +
															   "FROM grl_formulario_atributo A, grl_formulario B, grl_vinculo C " +
															   "WHERE A.cd_formulario = B.cd_formulario " +
															   "  AND B.cd_formulario = C.cd_formulario " +
															   "  AND C.cd_vinculo = ?");
			pstmt.setInt(1, cdVinculo);
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
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		String nmVinculo = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NM_VINCULO")) {
				nmVinculo =	Util.limparTexto(criterios.get(i).getValue());
				nmVinculo = nmVinculo.trim();
				criterios.remove(i);
				i--;
			}
		}
		return Search.find("SELECT A.*, B.nm_formulario " +
				 "FROM grl_vinculo A " +
				 "   LEFT OUTER JOIN grl_formulario B ON (A.cd_formulario = B.cd_formulario) "+
				 "WHERE 1=1 " +
				 (!nmVinculo.equals("") ?
							" AND TRANSLATE (nm_vinculo, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', "+
							"					'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmVinculo)+"%' "
							: ""),
				  criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Vinculo getVinculoByNome (String nmVinculo) {
		return getVinculoByNome(nmVinculo, null);
	}
	
	public static Vinculo getVinculoByNome (String nmVinculo, Connection conn) {
		boolean isConnNull = (conn == null);
		
		if (isConnNull) 
			conn = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM GRL_VINCULO WHERE NM_VINCULO ILIKE ?");
			pstmt.setString(1, nmVinculo);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			ResultSetMapper<Vinculo> _conv = new ResultSetMapper<Vinculo>(rsm, Vinculo.class);
			return _conv.toList().get(0);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnNull)
				Conexao.desconectar(conn);
		}
	}
	
}