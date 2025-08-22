package com.tivic.manager.fsc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class RecebimentoNotaFiscalServices {

	public static final int ST_DOCUMENTOS_FALTANDO   = 0;
	public static final int ST_DOCUMENTOS_PRESENTES	 = 1;
	public static final int ST_NOTA_FISCAL_GERADA	 = 2;
	public static String[] situacaoRecebimento = {"Documentos Faltando", "Documentos Presentes", "Nota Fiscal Gerada"};
	
	public static Result save(RecebimentoNotaFiscal recebimentoNotaFiscal, ArrayList<TipoDocumentoRecebimentoNotaFiscal> tiposDocumentoRecebimentoNotaFiscal){
		return save(recebimentoNotaFiscal, tiposDocumentoRecebimentoNotaFiscal, null, null);
	}
	
	public static Result save(RecebimentoNotaFiscal recebimentoNotaFiscal){
		return save(recebimentoNotaFiscal, null, null, null);
	}

	public static Result save(RecebimentoNotaFiscal recebimentoNotaFiscal, AuthData authData){
		return save(recebimentoNotaFiscal, null, authData, null);
	}

	public static Result save(RecebimentoNotaFiscal recebimentoNotaFiscal, ArrayList<TipoDocumentoRecebimentoNotaFiscal> tiposDocumentoRecebimentoNotaFiscal, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(recebimentoNotaFiscal==null)
				return new Result(-1, "Erro ao salvar. RecebimentoNotaFiscal é nulo");

			recebimentoNotaFiscal.setDtRecebimento(Util.getDataAtual());
			recebimentoNotaFiscal.setNrAno(Util.getDataAtual().get(Calendar.YEAR));
			
			int retorno;
			if(recebimentoNotaFiscal.getCdRecebimentoNotaFiscal()==0){
				retorno = RecebimentoNotaFiscalDAO.insert(recebimentoNotaFiscal, connect);
				recebimentoNotaFiscal.setCdRecebimentoNotaFiscal(retorno);
			}
			else {
				retorno = RecebimentoNotaFiscalDAO.update(recebimentoNotaFiscal, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			
			for(TipoDocumentoRecebimentoNotaFiscal tipoDocumentoRecebimentoNotaFiscal : tiposDocumentoRecebimentoNotaFiscal){
				RecebimentoNotaFiscalTipoDocumento recebimentoNotaFiscalTipoDocumento = new RecebimentoNotaFiscalTipoDocumento(recebimentoNotaFiscal.getCdRecebimentoNotaFiscal(), tipoDocumentoRecebimentoNotaFiscal.getCdTipoDocumento(), 1);
				Result result = RecebimentoNotaFiscalTipoDocumentoServices.save(recebimentoNotaFiscalTipoDocumento, null, connect);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
			
			boolean faltandoDocumentos = false;
			
			ResultSetMap rsmTipoDocumentoRecebimentoNotaFiscal = TipoDocumentoRecebimentoNotaFiscalServices.getAllAtivos(connect);
			while(rsmTipoDocumentoRecebimentoNotaFiscal.next()){
				boolean encontrado = false;
				for(TipoDocumentoRecebimentoNotaFiscal tipoDocumentoRecebimentoNotaFiscal : tiposDocumentoRecebimentoNotaFiscal){
					if(rsmTipoDocumentoRecebimentoNotaFiscal.getInt("cd_tipo_documento") == tipoDocumentoRecebimentoNotaFiscal.getCdTipoDocumento()){
						encontrado = true;
						break;
					}
				}
				
				if(!encontrado && rsmTipoDocumentoRecebimentoNotaFiscal.getInt("lg_obrigatorio")==1){
					faltandoDocumentos = true;
					RecebimentoNotaFiscalTipoDocumento recebimentoNotaFiscalTipoDocumento = new RecebimentoNotaFiscalTipoDocumento(recebimentoNotaFiscal.getCdRecebimentoNotaFiscal(), rsmTipoDocumentoRecebimentoNotaFiscal.getInt("cd_tipo_documento"), 0);
					Result result = RecebimentoNotaFiscalTipoDocumentoServices.save(recebimentoNotaFiscalTipoDocumento, null, connect);
					if(result.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}
			}
			
			if(faltandoDocumentos){
				recebimentoNotaFiscal.setStRecebimento(ST_DOCUMENTOS_FALTANDO);
				if(RecebimentoNotaFiscalDAO.update(recebimentoNotaFiscal, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar recebimento de nota fiscal");
				}
			}
			else{
				recebimentoNotaFiscal.setStRecebimento(ST_DOCUMENTOS_PRESENTES);
				if(RecebimentoNotaFiscalDAO.update(recebimentoNotaFiscal, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar recebimento de nota fiscal");
				}
			}
			
			if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "RECEBIMENTONOTAFISCAL", recebimentoNotaFiscal);
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
	public static Result remove(RecebimentoNotaFiscal recebimentoNotaFiscal) {
		return remove(recebimentoNotaFiscal.getCdRecebimentoNotaFiscal());
	}
	public static Result remove(int cdRecebimentoNotaFiscal){
		return remove(cdRecebimentoNotaFiscal, false, null, null);
	}
	public static Result remove(int cdRecebimentoNotaFiscal, boolean cascade){
		return remove(cdRecebimentoNotaFiscal, cascade, null, null);
	}
	public static Result remove(int cdRecebimentoNotaFiscal, boolean cascade, AuthData authData){
		return remove(cdRecebimentoNotaFiscal, cascade, authData, null);
	}
	public static Result remove(int cdRecebimentoNotaFiscal, boolean cascade, AuthData authData, Connection connect){
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
			retorno = RecebimentoNotaFiscalDAO.delete(cdRecebimentoNotaFiscal, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_recebimento_nota_fiscal");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalServices.getAll: " + e);
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
		return Search.find("SELECT * FROM fsc_recebimento_nota_fiscal", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getByPrestadorMes(int cdPessoa, int cdConcessao, int nrMes) {
		return getByPrestadorMes(cdPessoa, cdConcessao, nrMes, null);
	}

	public static ResultSetMap getByPrestadorMes(int cdPessoa, int cdConcessao, int nrMes, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_recebimento_nota_fiscal " + 
											 "	WHERE cd_pessoa = " + cdPessoa + 
											 "    AND nr_mes = " + nrMes + 
											 "	  AND nr_ano = " + Util.getDataAtual().get(Calendar.YEAR)+
											 "	  AND cd_concessao = " + cdConcessao + 
											 " ORDER BY dt_recebimento");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				
				ArrayList<Integer> documentosPresentes = new ArrayList<>();
				
				String documentosFaltantes = "";
				ResultSetMap rsmDocumentos = RecebimentoNotaFiscalTipoDocumentoServices.getAllByRecebimentoNotaFiscal(rsm.getInt("cd_recebimento_nota_fiscal"), connect);
				while(rsmDocumentos.next()){
					if(rsmDocumentos.getInt("lg_presente") == 0){
						TipoDocumentoRecebimentoNotaFiscal tipoDocumentoRecebimentoNotaFiscal = TipoDocumentoRecebimentoNotaFiscalDAO.get(rsmDocumentos.getInt("cd_tipo_documento"), connect);
						if(tipoDocumentoRecebimentoNotaFiscal.getLgObrigatorio()==1)
							documentosFaltantes += tipoDocumentoRecebimentoNotaFiscal.getNmTipoDocumento() + ", ";
					}
					else{
						documentosPresentes.add(rsmDocumentos.getInt("cd_tipo_documento"));
					}
				}
				rsmDocumentos.beforeFirst();
				
				if(documentosFaltantes.length() > 0)
					documentosFaltantes = documentosFaltantes.substring(0, documentosFaltantes.length()-2);
				else
					documentosFaltantes = "Nenhum";
						
				rsm.setValueToField("NM_DOCUMENTOS_FALTANTES", documentosFaltantes);
				
				rsm.setValueToField("DOCUMENTOS_PRESENTES", documentosPresentes);
				
				rsm.setValueToField("CL_DT_RECEBIMENTO", Util.convCalendarStringCompleto(rsm.getGregorianCalendar("dt_recebimento")));
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalServices.getByPrestadorMes: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalServices.getByPrestadorMes: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getPrintRecebimento(int cdRecebimentoNotaFiscal){
		return getPrintRecebimento(cdRecebimentoNotaFiscal, null);
	}
	
	public static ResultSetMap getPrintRecebimento(int cdRecebimentoNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_recebimento_nota_fiscal A, mob_concessao B " + 
											 "	WHERE A.cd_concessao = B.cd_concessao AND cd_recebimento_nota_fiscal = " + cdRecebimentoNotaFiscal);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				
				String documentosFaltantes = "";
				ResultSetMap rsmDocumentosFaltantes = RecebimentoNotaFiscalTipoDocumentoServices.getAllByRecebimentoNotaFiscal(rsm.getInt("cd_recebimento_nota_fiscal"), connect);
				while(rsmDocumentosFaltantes.next()){
					if(rsmDocumentosFaltantes.getInt("lg_presente") == 0){
						TipoDocumentoRecebimentoNotaFiscal tipoDocumentoRecebimentoNotaFiscal = TipoDocumentoRecebimentoNotaFiscalDAO.get(rsmDocumentosFaltantes.getInt("cd_tipo_documento"), connect);
						if(tipoDocumentoRecebimentoNotaFiscal.getLgObrigatorio()==1)
							documentosFaltantes += tipoDocumentoRecebimentoNotaFiscal.getNmTipoDocumento() + ", ";
					}
				}
				rsmDocumentosFaltantes.beforeFirst();
				
				if(documentosFaltantes.length() > 0)
					documentosFaltantes = documentosFaltantes.substring(0, documentosFaltantes.length()-2);
				else
					documentosFaltantes = "Nenhum";
						
				rsm.setValueToField("NM_DOCUMENTOS_FALTANTES", documentosFaltantes);
				
				rsm.setValueToField("CL_DT_RECEBIMENTO", Util.convCalendarStringCompleto(rsm.getGregorianCalendar("dt_recebimento")));
				
				rsm.setValueToField("CL_PERIODO_COMPETENCIA", (rsm.getInt("nr_mes")+1) + "/" + rsm.getInt("nr_ano"));
				
				Pessoa pessoa = PessoaDAO.get(rsm.getInt("cd_pessoa"), connect);
				
				rsm.setValueToField("NM_PRESTADOR", pessoa.getNmPessoa());
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalServices.getByPrestadorMes: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalServices.getByPrestadorMes: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}