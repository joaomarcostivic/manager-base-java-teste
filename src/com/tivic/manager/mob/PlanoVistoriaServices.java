package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Formulario;
import com.tivic.manager.grl.FormularioAtributo;
import com.tivic.manager.grl.FormularioAtributoServices;
import com.tivic.manager.grl.FormularioServices;

public class PlanoVistoriaServices {

	public static Result save(PlanoVistoria planoVistoria){
		return save(planoVistoria, null);
	}

	public static Result save(PlanoVistoria planoVistoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(planoVistoria==null)
				return new Result(-1, "Erro ao salvar. PlanoVistoria é nulo");

			int retorno;
			if(planoVistoria.getCdPlanoVistoria()==0){
				Formulario form = new Formulario();
				form.setNmFormulario( planoVistoria.getNmPlanoVistoria() );
				Result formResult = FormularioServices.save(form);
				form = (Formulario) formResult.getObjects().get("FORMULARIO");
				planoVistoria.setCdFormulario(form.getCdFormulario());
				retorno = PlanoVistoriaDAO.insert(planoVistoria, connect);
				planoVistoria.setCdPlanoVistoria(retorno);
			}
			else {
				retorno = PlanoVistoriaDAO.update(planoVistoria, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOVISTORIA", planoVistoria);
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
	public static Result remove(int cdPlanoVistoria){
		return remove(cdPlanoVistoria, false, null);
	}
	public static Result remove(int cdPlanoVistoria, boolean cascade){
		return remove(cdPlanoVistoria, cascade, null);
	}
	public static Result remove(int cdPlanoVistoria, boolean cascade, Connection connect){
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
			
			/** REMOVENDO FORMULÁRIO **/
			connect.prepareStatement("UPDATE mob_plano_vistoria SET cd_formulario = null "+
					 " WHERE cd_plano_vistoria = "+cdPlanoVistoria).executeUpdate();
			FormularioServices.remove( PlanoVistoriaDAO.get(cdPlanoVistoria).getCdFormulario(), true, connect);
			/** REMOVENDO DEFEITOS **/
			connect.prepareStatement("DELETE FROM mob_plano_vistoria_item_defeito "+
									 " WHERE cd_plano_vistoria = "+cdPlanoVistoria).executeUpdate();
			/** REMOVENDO ITENS **/
			connect.prepareStatement("DELETE FROM mob_plano_vistoria_item "+
					" WHERE cd_plano_vistoria = "+cdPlanoVistoria).executeUpdate();
			
			/** REMOVENDO ORDENAÇÃO DE GRUPOS **/
			connect.prepareStatement("DELETE FROM mob_plano_vistoria_grupo_ordem "+
					" WHERE cd_plano_vistoria = "+cdPlanoVistoria).executeUpdate();
			
			if(!cascade || retorno>0)
			retorno = PlanoVistoriaDAO.delete(cdPlanoVistoria, connect);
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
	public static Result gerarCopia(String nmPlanoVistoria, int cdPlanoVistoria){
		return gerarCopia(nmPlanoVistoria, cdPlanoVistoria, null);
	}
	public static Result gerarCopia(String nmPlanoVistoria, int cdPlanoVistoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PlanoVistoria novoPlano = new PlanoVistoria();
			novoPlano.setNmPlanoVistoria(nmPlanoVistoria);
			Result result = PlanoVistoriaServices.save(novoPlano, connect);
			if( result.getCode()<=0 ){
				if (isConnectionNull)
					Conexao.desconectar(connect);
				return new Result(-2, "Erro ao copiar Ficha de Vistoria!");
			}
			
			ResultSetMap rsmPlanoVistoriaItem = PlanoVistoriaItemServices.getAllByPlanoVistoria(cdPlanoVistoria);
			ResultSetMap rsmDefeitos;
			rsmPlanoVistoriaItem.beforeFirst();
			while( rsmPlanoVistoriaItem.next() ){
				PlanoVistoriaItem item = new PlanoVistoriaItem();
				item.setCdPlanoVistoria(novoPlano.getCdPlanoVistoria());
				item.setCdVistoriaItem( rsmPlanoVistoriaItem.getInt("CD_VISTORIA_ITEM") );
				item.setNrOrdemGrupo(rsmPlanoVistoriaItem.getInt("NR_ORDEM_GRUPO"));
				item.setNrOrdemItem(rsmPlanoVistoriaItem.getInt("NR_ORDEM_ITEM"));
				result = PlanoVistoriaItemServices.save(item, connect);
				if( result.getCode()<=0 ){
					if (isConnectionNull)
						Conexao.desconectar(connect);
					return new Result(-2, "Erro ao copiar Ficha de Vistoria!");
				}
				
				rsmDefeitos = (ResultSetMap) rsmPlanoVistoriaItem.getObject("DEFEITOS");
				if( rsmDefeitos != null && rsmDefeitos.size() > 0 ){
					rsmDefeitos.beforeFirst();
					while( rsmDefeitos.next() ){
						PlanoVistoriaItemDefeito defeito = new PlanoVistoriaItemDefeito();
						defeito.setCdDefeitoVistoriaItem( rsmDefeitos.getInt("CD_DEFEITO_VISTORIA_ITEM") );
						defeito.setCdPlanoVistoria(novoPlano.getCdPlanoVistoria());
						defeito.setCdVistoriaItem(item.getCdVistoriaItem());
						defeito.setTpPrazo(rsmPlanoVistoriaItem.getInt("TP_PRAZO"));
						defeito.setVlPrazo(rsmPlanoVistoriaItem.getInt("VL_PRAZO"));
						result = PlanoVistoriaItemDefeitoServices.save(defeito, connect);
						if( result.getCode()<=0 ){
							if (isConnectionNull)
								Conexao.desconectar(connect);
							return new Result(-2, "Erro ao copiar Ficha de Vistoria!");
						}
					}
				}
			}
			
			connect.commit();
			return new Result(1, "Ficha copiada com sucesso!", "PLANOVISTORIA", novoPlano);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao copiar Ficha!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByTpConcessao(int tpConcessao) {
		return getAllByTpConcessao(tpConcessao, null);
	}
	
	public static ResultSetMap getAllByTpConcessao(int tpConcessao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria WHERE tp_concessao = " + tpConcessao +
											" ORDER BY nm_plano_vistoria");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaServices.getAll: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveItemFormulario(FormularioAtributo formularioAtributo){
		return  FormularioAtributoServices.save(formularioAtributo, null);
	}

	public static Result saveItemFormulario(FormularioAtributo formularioAtributo, Connection connect){
		return FormularioAtributoServices.save(formularioAtributo, connect);
	}
	
	public static Result removeItemFormulario(int cdItemFormulario){
		return FormularioAtributoServices.remove(cdItemFormulario);
	}
	public static Result removeItemFormulario(int cdItemFormulario, boolean cascade){
		return FormularioAtributoServices.remove(cdItemFormulario, cascade);
	}
	public static Result removeItemFormulario(int cdItemFormulario, boolean cascade, Connection connect){
		return FormularioAtributoServices.remove(cdItemFormulario, cascade, connect);
	}
	
	public static ResultSetMap getAllItemFormulario(int cdPlanoVistoria) {
		return getAllItemFormulario(cdPlanoVistoria, null);
	}
	
	public static ResultSetMap getAllItemFormulario(int cdPlanoVistoria, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario_atributo A "+
											" JOIN grl_formulario B on ( A.cd_formulario = B.cd_formulario ) "+
											" JOIN mob_plano_vistoria C on ( A.cd_formulario = C.cd_formulario ) "+
											" WHERE cd_plano_vistoria = "+cdPlanoVistoria);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaServices.getAllFormularioAtributo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaServices.getAllFormularioAtributo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find() {
		return find(null, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		for (int i=0; criterios!=null && i<criterios.size(); i++){		
			if (criterios.get(i).getColumn().equalsIgnoreCase("TP_CONCESSAO") && 
					criterios.get(i).getValue().equals("-1")) {
				criterios.remove(i);
				i--;
				continue;
			}
		}
		return Search.find("SELECT * FROM mob_plano_vistoria A", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
