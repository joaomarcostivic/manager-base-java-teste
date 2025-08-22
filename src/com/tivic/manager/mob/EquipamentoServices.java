package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

public class EquipamentoServices {

	public static final int TALONARIO_ELETRONICO = 0;
	public static final int SEMAFORO = 1;
	public static final int RADAR_FIXO = 2;
	public static final int RADAR_MOVEL = 3;
	public static final int GPS = 4;
	public static final int TAXIMETRO = 5;
	public static final int IMPRESSORA = 6;
	public static final int FISCALIZADOR = 7;
	public static final int TACOGRAFO = 8;
	public static final int CAMERA = 9;
	public static final int RADAR_ESTATICO = 10;
	public static final int DETECTOR = 11;
	public static final int ZONA_AZUL = 12;
	
	//OBSERVAÇÃO: Esse status deve ser o mesmo de com.tivic.manager.grl.EquipamentoServices
	public static final int INATIVO = 0;
	public static final int ATIVO = 1;
	
	public static final String[] tiposEquipamento  = {"Talonário Eletrônico", "Semáforo", "Radar Fixo", "Radar Móvel", "GPS", "Taxímetro", "Impressora", "Fiscalizador", "Tacógrafo", "Camera", "Radar Estático", "Detector", "Zona Azul"};
	
	public static Result save(Equipamento equipamento){
		return save(equipamento, null);
	}

	public static Result save(Equipamento equipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(equipamento==null)
				return new Result(-1, "Erro ao salvar. Equipamento é nulo");

			int retorno;
			if(equipamento.getCdEquipamento()==0){
				ResultSetMap rsmValidacao = new ResultSetMap( 
							 connect.prepareStatement("SELECT A.*, B.* FROM mob_equipamento A " +
							           			      "LEFT JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento) " +
							           			      "WHERE B.id_equipamento = \'" + equipamento.getIdEquipamento() +"\'"+
							           			      "  AND B.tp_equipamento = " + equipamento.getTpEquipamento()).executeQuery());
				if( rsmValidacao.next()){				
					return new Result(-2, "O equipamento com número de série ("+equipamento.getIdEquipamento() +") "+
										  " já se encontra cadastrado.");
				}
				retorno = EquipamentoDAO.insert(equipamento, connect);
				equipamento.setCdEquipamento(retorno);
			}
			else {
				retorno = EquipamentoDAO.update(equipamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "EQUIPAMENTO", equipamento);
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
	
	public static Result registrar(Equipamento equipamento) {
		return registrar(equipamento, null);
	}

	public static Result registrar(Equipamento equipamento, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			if(equipamento==null)
				return new Result(-1, "Equipamento é nulo.");
			
			if(equipamento.getNmEquipamento()==null || equipamento.getNmEquipamento().equals(""))
				return new Result(-2, "Nome de equipamento inválido.");
			
			if(equipamento.getIdEquipamento()==null || equipamento.getIdEquipamento().equals(""))
				return new Result(-2, "ID de equipamento inválido.");
					
			boolean lgAtivarEquipamento = ParametroServices.getValorOfParametroAsInteger("LG_ATIVAR_EQUIPAMENTO", 0, 0, connect)==EquipamentoServices.ATIVO;
			equipamento.setStEquipamento(lgAtivarEquipamento ? EquipamentoServices.ATIVO : EquipamentoServices.INATIVO);
			
			Result result = save(equipamento, connect);
			
			if(result.getCode()>0) {
				return new Result((lgAtivarEquipamento ? 1 : 2), 
						"Equipamento registrado. "+ (lgAtivarEquipamento ? "" : "Aguarde a ativação por um de nossos operadores."), 
						"EQUIPAMENTO", equipamento);
			}
			else
				return new Result(-1, "Erro ao registrar aquipamento.");
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Util.registerLog(e);
			return new Result(-4, "Erro ao autenticar...");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdEquipamento){
		return remove(cdEquipamento, false, null);
	}
	public static Result remove(int cdEquipamento, boolean cascade){
		return remove(cdEquipamento, cascade, null);
	}
	public static Result remove(int cdEquipamento, boolean cascade, Connection connect){
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
			retorno = EquipamentoDAO.delete(cdEquipamento, connect);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.* FROM mob_equipamento A " + 
											 "LEFT JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento) "+
											 "LEFT JOIN str_orgao       C ON (B.cd_orgao = C.cd_orgao) ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoServices.getAll: " + e);
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
		return Search.find("SELECT A.*, B.*, C.* FROM mob_equipamento A " +
				           "LEFT JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento) "+
				           "LEFT JOIN str_orgao       C ON (B.cd_orgao = C.cd_orgao) ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
