package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class FeriadoServices {

	public static final int NACIONAL     = 0;
	public static final int ESTADO       = 1;
	public static final int MUNICIPAL    = 2;
	public static final int CARNAVAL     = 3;
	public static final int SEXTA_FEIRA_SANTA = 4;
	public static final int PASCOA       = 5;
	public static final int CORPUS_CRIST = 6;

	public static final int TP_FIXO = 0;
	public static final int TP_MOVEL = 1;

	public static String[] tipoFeriado = {"Fixo", "Móvel"};

	public static String[] getTipoFeriado() {
		return tipoFeriado;
	}

	public static Result save(Feriado feriado){
		return save(feriado, null);
	}
	
	public static Result save(Feriado feriado, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(feriado==null)
				return new Result(-1, "Erro ao salvar. Feriado é nulo");
			
			int retorno;
			if(feriado.getCdFeriado()==0){
				retorno = FeriadoDAO.insert(feriado, connect);
				feriado.setCdFeriado(retorno);
			}
			else {
				retorno = FeriadoDAO.update(feriado, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FERIADO", feriado);
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
	
	public static Result remove(int cdFeriado){
		return remove(cdFeriado, false, null);
	}
	
	public static Result remove(int cdFeriado, boolean cascade){
		return remove(cdFeriado, cascade, null);
	}
	
	public static Result remove(int cdFeriado, boolean cascade, Connection connect){
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
				retorno = FeriadoDAO.delete(cdFeriado, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este feriado está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Feriado excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir feriado!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArrayList<GregorianCalendar> getAllAsCalendar() {
		ResultSetMap rsm = getAll();
		ArrayList<GregorianCalendar> list = new ArrayList<>();
		while(rsm.next()) {
			list.add(rsm.getGregorianCalendar("DT_FERIADO"));
		}
		
		return list;
	}
	
	public static ResultSetMap getAll() {
		return getAll(null, null, null);
	}

	public static ResultSetMap getAll(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return getAll(dtInicial, dtFinal, null);
	}

	public static ResultSetMap getAll(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		if (dtInicial != null)
			criterios.add(new ItemComparator("dt_feriado", Util.formatDateTime(dtInicial, "dd/MM/yyyy"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
		if (dtFinal != null)
			criterios.add(new ItemComparator("dt_feriado", Util.formatDateTime(dtFinal, "dd/MM/yyyy") + " 23:59:99:999",
					ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
		ResultSetMap rsm = Search.find("SELECT A.*, " +
									   "	B.nm_estado, B.sg_estado " +
									   "FROM grl_feriado A " +
				                       "LEFT OUTER JOIN grl_estado B ON (A.cd_estado = B.cd_estado)",
				                       "ORDER BY sg_estado DESC, EXTRACT(MONTH FROM dt_feriado), EXTRACT(DAY FROM dt_feriado)", criterios, connection==null ? Conexao.conectar() : connection, connection==null);
		while(rsm.next())	{
			rsm.setValueToField("CL_DIA", Util.formatDateTime(rsm.getGregorianCalendar("dt_feriado"), "dd \'de\' MMMM"));
			rsm.setValueToField("CL_TIPO", rsm.getInt("tp_feriado") == TP_FIXO ? "Fixo" : "Móvel");
			rsm.setValueToField("NR_MES", rsm.getGregorianCalendar("dt_feriado").get(Calendar.MONTH));
		}
		rsm.beforeFirst();
		return rsm;
	}

	public static boolean isFeriado(GregorianCalendar data){
		return isFeriado(data, null);
	}

	public static boolean isFeriado(GregorianCalendar data, Connection connect){
		boolean isConnectionNull = (connect==null);
		if(isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int ano = data.get(GregorianCalendar.YEAR);
			data = new GregorianCalendar(ano, data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));

			if(getDataOfFeriadoMovel(ano, CARNAVAL).equals(data) ||
			   getDataOfFeriadoMovel(ano, CORPUS_CRIST).equals(data) ||
			   getDataOfFeriadoMovel(ano, PASCOA).equals(data)||
			   getDataOfFeriadoMovel(ano, SEXTA_FEIRA_SANTA).equals(data))
				return true;
			pstmt = connect.prepareStatement("SELECT * FROM grl_feriado "+
			                                 "WHERE EXTRACT(DAY FROM dt_feriado) = ? " +
			                                 "  AND EXTRACT(MONTH FROM dt_feriado) = ? "+
			                                 "  AND tp_feriado IN (0,1,2)");
			pstmt.setInt(1, data.get(GregorianCalendar.DAY_OF_MONTH));
			pstmt.setInt(2, data.get(GregorianCalendar.MONTH)+1);
			return pstmt.executeQuery().next();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! Feriado.isFeriado: " + sqlExpt);
			return false;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! Feriado.isFeriado: " +  e);
			return false;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GregorianCalendar	getDataOfFeriadoMovel(int nrAno, int tpFeriado) {
		int n1 = nrAno % 19;
		int n2 = nrAno / 100;
		int n3 = nrAno % 100;
		int n4 = n2 / 4;
		int n5 = n2 % 4;
		int n6 = (n2 + 8) / 25;
		int n7 = (n2 - n6 + 1) / 3;
		int n8 = (19 * n1 + n2 - n4 - n7 + 15) % 30;
		int n9 = n3 / 4;
		int n10 = n3 % 4;
		int n11 = (32 + 2 * n5 + 2 * n9 - n8 - n10) % 7;
		int n12 = (n1 + 11 * n8 + 22 * n11) / 451;
		int mes = (n8 + n11 - 7 * n12 + 114) / 31;
		int dia = (n8 + n11 - 7 * n12 + 114) % 31;
		GregorianCalendar data = null;
		switch(tpFeriado) {
			case CARNAVAL:
				data = new GregorianCalendar(nrAno, mes-1, dia+1);
				data.add(Calendar.DAY_OF_MONTH, -47);
				return data;
			case SEXTA_FEIRA_SANTA:
				return new GregorianCalendar(nrAno, mes-1, dia-1);
			case PASCOA:
				return new GregorianCalendar(nrAno, mes-1, dia+1);
			case CORPUS_CRIST:
				data = new GregorianCalendar(nrAno, mes-1, dia+1);
				data.add(Calendar.DAY_OF_MONTH, 60);
				return data;
			default:
				return data;
		}
	}

	public static void init(Connection connect) {
		boolean isConnectionNull = (connect==null);
		if(isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			if(connect==null)
				return;

			Feriado[] feriados = new Feriado[] {
				new Feriado(0, "CONFRATERNIZAÇÃO UNIVERSAL", new GregorianCalendar(0002,  0,  1), NACIONAL, "", 0),
				new Feriado(0, "TIRADENTES", new GregorianCalendar(0002,  3, 21), NACIONAL, "", 0),
				new Feriado(0, "DIA DO TRABALHO", new GregorianCalendar(0002,  4,  1), NACIONAL, "", 0),
				new Feriado(0, "INDEPENDÊNCIA", new GregorianCalendar(0002,  8,  7), NACIONAL, "", 0),
				new Feriado(0, "NOSSA SRA. APARECIDA", new GregorianCalendar(0002,  9, 12), NACIONAL, "", 0),
				new Feriado(0, "FINADOS", new GregorianCalendar(0002, 10,  2), NACIONAL, "", 0),
				new Feriado(0, "PROCLAMAÇÃO DA REPUBLICA", new GregorianCalendar(0002, 10, 15), NACIONAL, "", 0),
				new Feriado(0, "NATAL", new GregorianCalendar(0002, 11, 25), NACIONAL, "", 0),
				new Feriado(0, "CARNAVAL", new GregorianCalendar(0003, 11, 31), CARNAVAL, "", 0),
				new Feriado(0, "SEXTA-FEIRA SANTA", new GregorianCalendar(0004, 11, 31), SEXTA_FEIRA_SANTA, "", 0),
				new Feriado(0, "PÁSCOA", new GregorianCalendar(0005, 11, 31), PASCOA, "", 0),
				new Feriado(0, "CORPUS CRISTI", new GregorianCalendar(0006, 11, 31), CORPUS_CRIST, "",0)};


			for(int i=0; i<feriados.length; i++)	{
				Feriado feriado = feriados[i];
				String sql = "SELECT * FROM grl_feriado ";
				if(feriado.getTpFeriado()>=CARNAVAL)
					pstmt = connect.prepareStatement(sql+" WHERE tp_feriado = "+feriado.getTpFeriado());
				else	{
					pstmt = connect.prepareStatement(sql+" WHERE dt_feriado = ? ");
					pstmt.setTimestamp(1, new Timestamp(feriado.getDtFeriado().getTimeInMillis()));
				}
				if(!pstmt.executeQuery().next())
					FeriadoDAO.insert(feriado);
			}
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		String nmFeriado = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NM_FERIADO")) {
				nmFeriado =	Util.limparTexto(criterios.get(i).getValue());
				nmFeriado = nmFeriado.trim();
				criterios.remove(i);
				i--;
			}
		}
		return Search.find("SELECT * FROM grl_feriado "+
							"WHERE 1=1 "+
						(!nmFeriado.equals("") ?
									" AND TRANSLATE (nm_feriado, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', "+
									"					'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmFeriado)+"%' "
							: ""),
							"ORDER BY dt_feriado", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}