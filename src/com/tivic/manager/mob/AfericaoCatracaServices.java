package com.tivic.manager.mob;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.log.Sistema;
import com.tivic.manager.util.DateUtil;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;
import com.tivic.manager.mob.Lacre;
import com.tivic.manager.mob.LacreDAO;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.AgenteDAO;
import com.tivic.manager.mob.ConcessaoVeiculo;
import com.tivic.manager.mob.ConcessaoVeiculoDAO;

import ch.qos.logback.core.subst.Token.Type;

public class AfericaoCatracaServices {
	public static final int TP_NAO_INFORMADA      = 0;
	public static final int TP_LEITURA_AVULSA     = 1;
	public static final int TP_LEITURA_MENSAL     = 2;
	public static final int TP_LEITURA_IMPORTACAO = 3;
	public static final int TP_APLICACAO_VINCULACAO  = 4;
	public static final int TP_REMOCAO_DESVINCULACAO = 5;	
	public static final int TP_APLICACAO_MANUTENCAO  = 6;	
	public static final int TP_REMOCAO_MANUTENCAO    = 7;	
	public static final int TP_APLICACAO_LACRACAO    = 8;	
	public static final int TP_REMOCAO_SOLTURA    = 9;
	
	public static String[] tipoLeitura   = {"Não Informada", "Avulsa", "Mensal", "Importação", "Vinculação", "Desvinculação", "Aplicação Manutenção", "Remoção Manutenção"};
	
		
	//Salva array de objetos que vem direto da importacao
	public static Result saveImportacao(ArrayList<AfericaoCatraca> afericaoCatraca, ArrayList<Integer> impedimentos){
		int retorno = 0;
		for (AfericaoCatraca saveAfericaoCatraca : afericaoCatraca) {
			if(save(saveAfericaoCatraca,impedimentos).getCode()<=0)
				retorno += 1;
		}
		return new Result(retorno, (retorno > 0 ? "Erro ao salvar Aferição da Catraca na Importação!":"Aferição da Catraca na Importação salvo com sucesso!!"));
	}

	public static Result save(AfericaoCatraca afericaoCatraca){
		return save(afericaoCatraca, null, 0, null);
	}
	
	public static Result save(AfericaoCatraca afericaoCatraca, ArrayList<Integer> impedimentos){
		return save(afericaoCatraca, impedimentos, 0 ,null);
	}
	
	public static Result save(AfericaoCatraca afericaoCatraca, Connection connect){
		return save(afericaoCatraca, null, 0, connect);
	}
	
	public static Result save(AfericaoCatraca afericaoCatraca, ArrayList<Integer> impedimentos, int nrPrefixo){
		return save(afericaoCatraca, impedimentos, nrPrefixo ,null);
	}
	
	
	public static Result save(AfericaoCatraca afericaoCatraca, ArrayList<Integer> impedimentos, int nrPrefixo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
 
			if(afericaoCatraca==null)
				return new Result(-1, "AfericaoCatraca é nulo");
			
			int retorno;			
			ConcessaoVeiculo ultimaSituacao = getUltimaSituacaoByDataAfericao(nrPrefixo);
			
				if(afericaoCatraca.getCdAfericaoCatraca()==0){
					if(ultimaSituacao.getStConcessaoVeiculo() == ConcessaoVeiculoServices.ST_DESVINCULADO) {
						if(afericaoCatraca.getTpLeitura() == TP_APLICACAO_VINCULACAO) {
							retorno = AfericaoCatracaDAO.insert(afericaoCatraca, connect);
							afericaoCatraca.setCdAfericaoCatraca(retorno);
						} else {
							Conexao.rollback(connect);
							return new Result(-1, "Não é possível fazer uma leitura num carro desvinculado.");
						}
					} else {
						retorno = AfericaoCatracaDAO.insert(afericaoCatraca, connect);
						afericaoCatraca.setCdAfericaoCatraca(retorno);
					}
									
				}
				else {
					retorno = AfericaoCatracaDAO.update(afericaoCatraca, connect);				
				}
			/**	
			 * Impedimentos
			 */
			retorno = AfericaoImpedimentoServices.atualizarImpedimentos(afericaoCatraca.getCdAfericaoCatraca(), afericaoCatraca.getLgHodometroIlegivel(), impedimentos, connect);
			if(retorno<=0) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar impedimentos");
			}

			/**
			 * ConcessaoVeiculo e LacreCatraca
			 */
			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(afericaoCatraca.getCdConcessaoVeiculo(), connect);
			
			
			switch (afericaoCatraca.getTpLeitura()) {
				case TP_APLICACAO_VINCULACAO: //Não pode aceitar LG_EM_MANUTENCAO / TP_REMOCAO_SOLTURA
					
					if(ultimaSituacao.getStConcessaoVeiculo() == ConcessaoVeiculoServices.ST_VINCULADO 
					&& ultimaSituacao.getLgManutencao() == ConcessaoVeiculoServices.LG_RODANDO) {
						Conexao.rollback(connect);
						return new Result(-1, "Veiculo já esta vinculado.");
					} else {
						concessaoVeiculo.setStConcessaoVeiculo(ConcessaoVeiculoServices.ST_VINCULADO);
						concessaoVeiculo.setLgManutencao(ConcessaoVeiculoServices.LG_RODANDO);
						if(concessaoVeiculo.getDtAssinatura() == null)
							concessaoVeiculo.setDtAssinatura(afericaoCatraca.getDtAfericao());
						if(concessaoVeiculo.getDtInicioOperacao() == null)
							concessaoVeiculo.setDtInicioOperacao(afericaoCatraca.getDtAfericao());
						
						ConcessaoVeiculoServices.save(concessaoVeiculo, null, connect);
						
						LacreCatracaServices.ativarLacre(afericaoCatraca.getCdLacre(), afericaoCatraca.getCdConcessaoVeiculo(), afericaoCatraca.getCdAfericaoCatraca(), connect);
					}
					break;

				case TP_APLICACAO_MANUTENCAO: //Só pode acontecer se o veiculo estiver  LG_EM_MANUTENCAO
					//Retira da manutenção 
					
					if(ultimaSituacao.getLgManutencao() == ConcessaoVeiculoServices.LG_EM_MANUTENCAO) {
						concessaoVeiculo.setLgManutencao(ConcessaoVeiculoServices.LG_RODANDO);
						ConcessaoVeiculoServices.save(concessaoVeiculo, null, connect);
						
						LacreCatracaServices.ativarLacre(afericaoCatraca.getCdLacre(), afericaoCatraca.getCdConcessaoVeiculo(), afericaoCatraca.getCdAfericaoCatraca(), connect);
					} else {		
						Conexao.rollback(connect);
						return new Result(-1, "Veiculo não está em manutenção.");
					}
					break;
				case TP_REMOCAO_MANUTENCAO: // Só pode enviar para a manutenção os que estiverem ST_VINCULADO && LG_RODANDO
					//Envia para manutenção
				
					if(ultimaSituacao.getStConcessaoVeiculo() == ConcessaoVeiculoServices.ST_VINCULADO 
					&& ultimaSituacao.getLgManutencao() == ConcessaoVeiculoServices.LG_RODANDO) {
						concessaoVeiculo.setLgManutencao(ConcessaoVeiculoServices.LG_EM_MANUTENCAO);
						ConcessaoVeiculoServices.save(concessaoVeiculo, null, connect);
						
						LacreCatracaServices.inativarLacre(afericaoCatraca.getCdLacre(), afericaoCatraca.getCdConcessaoVeiculo(), afericaoCatraca.getCdAfericaoCatraca(), connect);
					} else {		
						Conexao.rollback(connect);
						return new Result(-1, "Veiculo já se encontra em manutenção.");
					}
					break;
				case TP_REMOCAO_DESVINCULACAO: // Só os veiculos que estiverem  ST_VINCULADO
	
						if(concessaoVeiculo.getDtFinalOperacao() == null)
							concessaoVeiculo.setDtFinalOperacao(afericaoCatraca.getDtAfericao());
						
						ConcessaoVeiculoServices.save(concessaoVeiculo, null, connect);
						LacreCatracaServices.inativarLacre(afericaoCatraca.getCdLacre(), afericaoCatraca.getCdConcessaoVeiculo(), afericaoCatraca.getCdAfericaoCatraca(), connect);
					break;
				case TP_APLICACAO_LACRACAO: // Não pode com ST_DESVINCULADO && ST_NAO_VINCULADO && ST_LACRADO
					
					if(ultimaSituacao.getStConcessaoVeiculo() == ConcessaoVeiculoServices.ST_NAO_VINCULADO 
					|| ultimaSituacao.getStConcessaoVeiculo() == ConcessaoVeiculoServices.ST_DESVINCULADO || ultimaSituacao.getStConcessaoVeiculo() == ConcessaoVeiculoServices.ST_LACRADO) {
						Conexao.rollback(connect);
						return new Result(-1, "Só é possível lacrar um veiculo vinculado.");
					} else {
						concessaoVeiculo.setStConcessaoVeiculo(ConcessaoVeiculoServices.ST_LACRADO);
						ConcessaoVeiculoServices.save(concessaoVeiculo, null, connect);
						
						LacreCatracaServices.ativarLacre(afericaoCatraca.getCdLacre(), afericaoCatraca.getCdConcessaoVeiculo(), afericaoCatraca.getCdAfericaoCatraca(), connect);
					}
					break;
				case TP_REMOCAO_SOLTURA: //Só pode quando estiver ST_LACRADO
					
					if(ultimaSituacao.getStConcessaoVeiculo() == ConcessaoVeiculoServices.ST_LACRADO) {
						ultimaSituacao.setStConcessaoVeiculo(ConcessaoVeiculoServices.ST_VINCULADO);
					ConcessaoVeiculoServices.save(concessaoVeiculo, null, connect);
					
					LacreCatracaServices.inativarLacre(afericaoCatraca.getCdLacre(), afericaoCatraca.getCdConcessaoVeiculo(), afericaoCatraca.getCdAfericaoCatraca(), connect);
					} else {
						Conexao.rollback(connect);
						return new Result(-1, "Só é possível soltar um veículo lacrado.");
					}
					break;
				
				default:
					break;
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AFERICAOCATRACA", afericaoCatraca);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ConcessaoVeiculo getUltimaSituacaoByDataAfericao(int nrPrefixo) {
		return getUltimaSituacaoByDataAfericao(nrPrefixo, null);
	}
	
	public static ConcessaoVeiculo getUltimaSituacaoByDataAfericao(int nrPrefixo, Connection connect ) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;		
		try {

			String sql =("SELECT A.cd_concessao_veiculo, A.nr_prefixo, A.st_concessao_veiculo FROM mob_concessao_veiculo A   " 
					+ "LEFT OUTER JOIN mob_afericao_catraca B ON (A.cd_concessao_veiculo = B.cd_concessao_veiculo) "
					+ "WHERE A.nr_prefixo = ? ORDER BY B.dt_afericao DESC LIMIT 1");
			
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, nrPrefixo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				return	ConcessaoVeiculoDAO.get(rsm.getInt("cd_concessao_veiculo")) ;
			}
			return null;

		} catch (Exception e) {
			System.out.println("Erro!" + e.getMessage());
			return null;
		}
	}

	public static Result remove(int cdAfericaoCatraca){
		return remove(cdAfericaoCatraca, false, null);
	}
	
	public static Result remove(int cdAfericaoCatraca, boolean cascade){
		return remove(cdAfericaoCatraca, cascade, null);
	}
	
	public static Result remove(int cdAfericaoCatraca, boolean cascade, Connection connect){
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
			retorno = AfericaoCatracaDAO.delete(cdAfericaoCatraca, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluí­do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluí­do com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_afericao_catraca");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAfericoesByConcessaoVeiculo(int cdConcessaoVeiculo) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new ItemComparator("A.CD_CONCESSAO_VEICULO", String.valueOf(cdConcessaoVeiculo), ItemComparator.EQUAL, Types.INTEGER));
		crt.add(new ItemComparator("ORDERBY", "A.DT_AFERICAO DESC", ItemComparator.EQUAL, Types.INTEGER));

		return find(crt);
	}

	public static ResultSetMap getResultsetMapFromByte(byte[] bArquivo, String separator) {	
		return getResultsetMapFromByte(bArquivo, separator, null);
	}
	public static ResultSetMap getResultsetMapFromByte(byte[] bArquivo, String separator, ArrayList<ItemComparator> criterios) {		
		try {
			// Trata o array de bytes para ler o arquivo .xlsx			
			InputStream ExcelFileToRead = new ByteArrayInputStream(bArquivo);
			XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);
			
			XSSFWorkbook test = new XSSFWorkbook(); 
			
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row; 
			XSSFCell cell;

			Iterator rows = sheet.rowIterator();
			//
			boolean limiteQtCaracteres = false;
			int posicaoColuna = -1;
			int qtCasas = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("limiteQtCaracteres")) 
					limiteQtCaracteres = true;
				
				if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("posicaoColuna"))
					posicaoColuna = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
				
				if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("qtCasas"))
					qtCasas = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
			}
			
			//
			boolean primeiraLinha = true;
			boolean fimDaTabela = false;
			ResultSetMap rsm = new ResultSetMap();
			ResultSetMap colunas = new ResultSetMap();
			HashMap<String,Object> register = null;	
			HashMap<Integer,Object> nomeColunas = new HashMap<Integer,Object>();		
			while (rows.hasNext())
			{
				row=(XSSFRow) rows.next();
				if(row.getPhysicalNumberOfCells() < 4){
					break;					
				}
				
				Iterator cells = row.cellIterator();				
				
				register = new HashMap<String,Object>(); // Linha da tabela

				// Trata o cabeçalho das colunas
				if(primeiraLinha){
					while (cells.hasNext())
					{
						cell=(XSSFCell) cells.next();
						if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
						{
							nomeColunas.put(cell.getColumnIndex(), cell.getStringCellValue());								
//						}else if(HSSFDateUtil.isCellDateFormatted(cell))
//						{
//							nomeColunas.put(cell.getColumnIndex(), cell.getStringCellValue());	
						}else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
						{
							nomeColunas.put(cell.getColumnIndex(), cell.getNumericCellValue());	
						}																	
						
						if(!cells.hasNext()){
							primeiraLinha = false;							
						}
					}					
				}
				else{// Percorre linha por linha da tabela para criar

					while (cells.hasNext())
					{
						cell=(XSSFCell) cells.next();		
						
//						System.out.println("cell.getColumnIndex(): " + cell.getColumnIndex());

						if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING){				
//							System.out.println("cell.getStringCellValue(): " + cell.getStringCellValue());
							register.put(String.valueOf(cell.getColumnIndex()), cell.getStringCellValue());							
						}
						else if(HSSFDateUtil.isCellDateFormatted(cell)){
//							System.out.println("cell.getDateCellValue(): " + cell.getDateCellValue());
							register.put(String.valueOf(cell.getColumnIndex()), cell.getDateCellValue());
						}
						else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
							if(limiteQtCaracteres){
								if(posicaoColuna == cell.getColumnIndex())
									register.put(String.valueOf(cell.getColumnIndex()), Util.formatNumber(Double.valueOf(cell.getNumericCellValue()), qtCasas));
								else
									register.put(String.valueOf(cell.getColumnIndex()), Util.formatNumber(Double.valueOf(cell.getNumericCellValue()), qtCasas));								
							}else
								register.put(String.valueOf(cell.getColumnIndex()), cell.getNumericCellValue());
						}
						else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
//							System.out.println("cell.getRawValue(): " + cell.getRawValue());
							register.put(String.valueOf(cell.getColumnIndex()), cell.getRawValue());
						}
					}				
						rsm.addRegister(register);
					
				}
			}
			
			return rsm;

		} catch (IOException e) {			
			e.printStackTrace();
		}
		return null;		
		
//		return sol.dao.ResultSetMap.getResultsetMapFromByte(bArquivo, separator, false);
	}
	
	public static ResultSetMap findLeituraImportacao(ArrayList<ItemComparator> criterios) {
		try {
			int cdConcessao = 0;
			//
			ResultSetMap rsmVeiculos = new ResultSetMap();
			ResultSetMap rsmLeituras = new ResultSetMap();
			//
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			//
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("cdConcessao")) {
					
					cdConcessao = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
					criterios.remove(i);
					i--;
				}else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("A.DT_AFERICAO"))
					crt.add(new  ItemComparator("A.DT_INICIO_OPERACAO", 
							String.valueOf(((ItemComparator)criterios.get(i)).getValue()), 
							ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			}
			
			
			crt.add(new  ItemComparator("A.CD_CONCESSAO", String.valueOf(cdConcessao), ItemComparator.EQUAL, Types.INTEGER));
			crt.add(new  ItemComparator("A.ST_CONCESSAO_VEICULO", String.valueOf(ConcessaoVeiculoServices.ST_VINCULADO),
																  ItemComparator.EQUAL, Types.INTEGER));
			
			//Busco todos os veículos ativos para a concessão
			rsmVeiculos = ConcessaoVeiculoServices.find(crt);
			//Verifico se existem leituras para os veículos
			rsmLeituras = find(criterios);
			//
			
			while(rsmVeiculos.next()){
				
				while(rsmLeituras.next()) {
					
					if(rsmVeiculos.getInt("nr_prefixo") == rsmLeituras.getInt("nr_prefixo")){
						rsmVeiculos.setValueToField("CD_AFERICAO", "" + rsmLeituras.getInt("cd_afericao_catraca"));
						rsmVeiculos.setValueToField("CD_LACRE", "" + rsmLeituras.getInt("cd_lacre"));
						rsmVeiculos.setValueToField("QT_AFERIDO", "" + (rsmLeituras.getInt("qt_aferido") > 0 ? rsmLeituras.getInt("qt_aferido") : 0));
						rsmVeiculos.setValueToField("QT_HODOMETRO", "" + (rsmLeituras.getDouble("qt_hodometro") > 0 ? rsmLeituras.getDouble("qt_hodometro") : 0));
					}						
				}	
				//inicializa leituras
				rsmLeituras.beforeFirst();
			}
			//
			return rsmVeiculos;
			
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaServices.findLeituraImportacao: " + e);
			return null;
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		try{		
			LogUtils.debug("AfericaoCatracaServices.find");
			LogUtils.createTimer("AFERICAO_CATRACA_FIND_TIMER");
			String orderBy = "";
			int tpObservacao = 0;
			int qtLimite = 0;
			boolean listaMotivos = false;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
					orderBy = " ORDER BY "+ criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("txtObservacao")) {
					tpObservacao = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
					criterios.remove(i);
					i--;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("listaMotivos")) {
					listaMotivos = true;
					criterios.remove(i);
					i--; 
				}  else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					qtLimite = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
					criterios.remove(i);
					i--;
				}
			} 
			
			String sql = "SELECT A.cd_afericao_catraca, A.dt_afericao, A.qt_aferido, A.qt_hodometro, "
					+ 	 "A.lg_hodometro_ilegivel, A.txt_observacao, A.tp_leitura, B.*, C.*, "
//					+ 	 "D1.cd_lacre_catraca, D1.cd_concessao_veiculo, D1.cd_afericao_aplicacao, D1.cd_afericao_remocao, "
					+ 	 "D.*, F.*, G.*, H.*, I.* "
					+ 	 "FROM mob_afericao_catraca A "+
					   	 "JOIN mob_concessao_veiculo  B  ON ( A.cd_concessao_veiculo = B.cd_concessao_veiculo ) "+
					     "JOIN fta_veiculo 		      C  ON ( B.cd_veiculo           = C.cd_veiculo ) "+
//					     "LEFT JOIN mob_lacre_catraca D1 ON ( B.cd_concessao_veiculo = D1.cd_concessao_veiculo AND A.cd_lacre = D1.cd_lacre) " +
//					     "                                AND D1.cd_afericao_remocao IS NULL ) "+
					     "LEFT JOIN mob_lacre 	  	  D  ON ( A.cd_lacre             = D.cd_lacre ) "+
					     "JOIN mob_concessao 	   	  F  ON ( B.cd_concessao         = F.cd_concessao ) "+
					     "JOIN grl_pessoa 		   	  G  ON ( F.cd_concessionario    = G.cd_pessoa ) "+
					     "LEFT OUTER JOIN mob_agente 		      H  ON ( H.cd_agente            = A.cd_agente ) "+
					     "JOIN seg_usuario 		      I  ON ( I.cd_usuario           = A.cd_usuario ) " + 
					     "WHERE 1=1 " +
					      (tpObservacao == 1? " AND A.TXT_OBSERVACAO <> '' ":(tpObservacao == 2?" AND A.TXT_OBSERVACAO = '' ":""));
			
//			LogUtils.debug("SQL:\n"+Search.getStatementSQL(sql, (orderBy != "" ? orderBy : "") + (qtLimite > 0 ? " LIMIT " + qtLimite : ""), criterios, true));
			
			
			ResultSetMap rsm = Search.find(sql + " ", (orderBy != "" ? orderBy : " ORDER BY dt_afericao DESC ") + (qtLimite > 0 ? " LIMIT " + qtLimite : ""), criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
		
//			LogUtils.logTimer("AFERICAO_CATRACA_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
//			LogUtils.debug("AfericaoCatracaServices.find: Iniciando injeção de dados adicionais...");
			
			 if(listaMotivos)
				while(rsm.next())
					rsm.setValueToField("LISTA_MOTIVOS", ImpedimentoAfericaoServices.getListaImpedimentosByAfericao(rsm.getInt("cd_afericao_catraca"))); 
//			
//			LogUtils.debug(rsm.size() + " registro(s)");
//			LogUtils.logTimer("AFERICAO_CATRACA_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
//			LogUtils.destroyTimer("AFERICAO_CATRACA_FIND_TIMER");
//			
			
			
			return rsm;
		}catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaServices.find: " + e);
			return null;
		}
	}	
	
	public static List<AfericaoCatracaDTO> findDTO(ArrayList<ItemComparator> criterios) throws Exception{
		return findDTO(criterios, null);
	}
	
	public static List<AfericaoCatracaDTO> findDTO(ArrayList<ItemComparator> criterios, Connection connect){
		ResultSetMap rsm = find(criterios);
		
		List<AfericaoCatracaDTO> list = new ArrayList<AfericaoCatracaDTO>();
		
		while(rsm.next()) {
			AfericaoCatracaDTO dto = new AfericaoCatracaDTO();
			
			AfericaoCatraca afericaoCatraca = AfericaoCatracaDAO.get(rsm.getInt("CD_AFERICAO_CATRACA"));
			dto.setAfericaoCatraca(afericaoCatraca);
			
			Lacre lacre = LacreDAO.get(rsm.getInt("CD_LACRE"));
			dto.setLacre(lacre);
			
			Agente agente = AgenteDAO.get(rsm.getInt("CD_AGENTE"));
			dto.setAgente(agente);
			
			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(rsm.getInt("CD_CONCESSAO_VEICULO"));
			dto.setConcessaoVeiculo(concessaoVeiculo);
      
			Pessoa pessoa = PessoaDAO.get(rsm.getInt("CD_PESSOA"));
			dto.setPessoa(pessoa);
			
			list.add(dto);
		}
		
		
		return list;
	}
	
	public static AfericaoCatraca getDataPosterior(int nrPrefixo) {
		return getDataPosterior(nrPrefixo, null);
	}
	
	public static AfericaoCatraca getDataPosterior(int nrPrefixo, Connection connect ) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;		
		try {

			String sql =("SELECT A.dt_afericao, A.cd_afericao_catraca, B.nr_prefixo FROM mob_afericao_catraca A " 
					+ "JOIN mob_concessao_veiculo B ON (A.cd_concessao_veiculo = B.cd_concessao_veiculo) "
					+ "WHERE B.nr_prefixo = ? ORDER BY dt_afericao DESC LIMIT 1");
			
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, nrPrefixo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				return	AfericaoCatracaDAO.get(rsm.getInt("cd_afericao_catraca")) ;
			}
			
			
			return null;

		} catch (Exception e) {
			System.out.println("Erro!" + e.getMessage());
			return null;
		}
	}
	
	
	
	/**
	 * @deprecated use calculoDiferencaMensal().  
	 */
	@Deprecated
	public static ResultSetMap calcDiferencaMensal(ArrayList<ItemComparator> criterios) {
		return calcDiferencaMensal(criterios, null);
	}
	@Deprecated
	public static ResultSetMap calcDiferencaMensal(ArrayList<ItemComparator> criterios, Connection connect) {
		try{		
			LogUtils.debug("AfericaoCatracaServices.calcDiferencaMensal");
			LogUtils.createTimer("AFERICAO_CATRACA_CALC_TIMER");
			
			GregorianCalendar dtInicial = new GregorianCalendar();

			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("dtInicial")) {
					
					dtInicial = Util.convStringToCalendar(criterios.get(i).getValue().toString());
					dtInicial.set(Calendar.HOUR, 23);
					dtInicial.set(Calendar.MINUTE, 59);
					dtInicial.set(Calendar.SECOND, 59);

					criterios.remove(i);
					i--;
				}
			}
			
			//Os critérios passados já trazem ordenados por nr_prefixo, tp_leitura e dt_afericao
			ResultSetMap rsm = new ResultSetMap(); 
			ResultSetMap rsmFind = find(criterios);
			int nrPrefixo = 0;
			Boolean primeiraLeituraMensal = false;
			Boolean segundaLeituraMensal = false;
			Boolean primeiraLeituraImport = false;
			Boolean segundaLeituraImport = false;
			//
			HashMap<String, Object> elementoCatraca = new HashMap<String, Object>();
			HashMap<String, Object> elementoKm = new HashMap<String, Object>();
			HashMap<String, Object> elementoCatracaResultado = new HashMap<String, Object>();
			HashMap<String, Object> elementoLinha = new HashMap<String, Object>();
			//
			ArrayList<HashMap<String, Object>> linha = new ArrayList<HashMap<String, Object>>();
			//
			LogUtils.logTimer("AFERICAO_CATRACA_CALC_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.debug("AfericaoCatracaServices.calcDiferencaMensal: Iniciando injeção de dados adicionais...");
			//			
			rsmFind.beforeFirst();

			while(rsmFind.next()){
				
				//Verifica se o prefixo mudou
				if(nrPrefixo != rsmFind.getInt("nr_prefixo")){
					/**
					 * Inicio verificação de leitura
					 */
					//Se passou pela primeira leitura e não passou pela segunda depois que mudou o prefixo, 
					//então deve ser inserido o valor 0 na segunda
					if(primeiraLeituraMensal && !segundaLeituraMensal){		
						//insere valor
						//elementoCatraca.put("CATRACA_FINAL_SISTEMA", 0);
						
						elementoLinha.put("CATRACA_MENSAL", elementoCatraca);
						elementoLinha.put("KM_MENSAL", elementoKm);
						elementoLinha.put("CATRACA_RESULTADO", elementoCatracaResultado);
						
						elementoCatraca = new HashMap<String, Object>();
						elementoKm = new HashMap<String, Object>();
						elementoCatracaResultado = new HashMap<String, Object>();
						
						primeiraLeituraMensal = false;
						segundaLeituraMensal  = false;
					}
					
					if(primeiraLeituraImport && !segundaLeituraImport){		
						//insere valor
						//elementoCatraca.put("CATRACA_FINAL_IMPORTACAO", 0);
						
						elementoLinha.put("CATRACA_IMPORTACAO", elementoCatraca);
						elementoLinha.put("KM_IMPORTACAO", elementoKm);
						elementoLinha.put("CATRACA_RESULTADO", elementoCatracaResultado);
						
						elementoCatraca = new HashMap<String, Object>();
						elementoKm      = new HashMap<String, Object>();
						elementoCatracaResultado = new HashMap<String, Object>();
						
						primeiraLeituraImport = false;
						segundaLeituraImport  = false;
						
					}

					//insere todos os elementos na linha
					if (elementoLinha != null){
						if (nrPrefixo > 0)
							linha.add(elementoLinha);
						
						elementoLinha = new HashMap<String, Object>();
					}
					/**
					 * Fim verificação de leitura
					 */
					
					//Pega novo prefixo
					nrPrefixo = rsmFind.getInt("nr_prefixo");
					//começa outro prefixo
					elementoLinha.put("NR_PREFIXO", nrPrefixo);
					
				}
				
				if(nrPrefixo == rsmFind.getInt("nr_prefixo")){

					switch (rsmFind.getInt("tp_leitura")) {
					
					case TP_LEITURA_MENSAL:
						//verifica se é a leitura inicial do mes. data menor ou igual ao do primeiro dia do mês informado.	
						if(dtInicial.compareTo(rsmFind.getGregorianCalendar("dt_afericao")) > 0){
							elementoCatraca.put("CD_AFERICAO_CATRACA_INICIAL", rsmFind.getInt("cd_afericao_catraca"));
							elementoCatraca.put("CD_CONCESSAO_VEICULO_INICIAL", rsmFind.getInt("cd_concessao_veiculo"));
							elementoCatraca.put("CD_LACRE_INICIAL", rsmFind.getInt("cd_lacre"));
							elementoCatraca.put("DT_AFERICAO_INICIAL", rsmFind.getTimestamp("dt_afericao"));
							elementoCatraca.put("QT_AFERIDO_INICIAL", rsmFind.getInt("qt_aferido"));
							elementoCatraca.put("QT_HODOMETRO_INICIAL", rsmFind.getDouble("qt_hodometro"));
							elementoCatraca.put("LG_HODOMETRO_ILEGIVEL_INICIAL", rsmFind.getInt("lg_hodometro_ilegivel"));
							elementoCatraca.put("TXT_OBSERVACAO_INICIAL", rsmFind.getString("txt_observacao"));
							elementoCatraca.put("CD_AGENTE_INICIAL", rsmFind.getInt("cd_agente"));
							elementoCatraca.put("CD_USUARIO_INICIAL", rsmFind.getInt("cd_usuario"));
							elementoCatraca.put("TP_LEITURA_INICIAL", rsmFind.getInt("tp_leitura"));							
							
							elementoCatracaResultado.put("CATRACA_INICIAL_RESULTADO", 0);
							
							primeiraLeituraMensal = true;
							
						}else{
							elementoCatraca.put("CD_AFERICAO_CATRACA_FINAL", rsmFind.getInt("cd_afericao_catraca"));
							elementoCatraca.put("CD_CONCESSAO_VEICULO_FINAL", rsmFind.getInt("cd_concessao_veiculo"));
							elementoCatraca.put("CD_LACRE_FINAL", rsmFind.getInt("cd_lacre"));
							elementoCatraca.put("DT_AFERICAO_FINAL", rsmFind.getTimestamp("dt_afericao"));
							elementoCatraca.put("QT_AFERIDO_FINAL", rsmFind.getInt("qt_aferido"));
							elementoCatraca.put("QT_HODOMETRO_FINAL", rsmFind.getDouble("qt_hodometro"));
							elementoCatraca.put("LG_HODOMETRO_ILEGIVEL_FINAL", rsmFind.getInt("lg_hodometro_ilegivel"));
							elementoCatraca.put("TXT_OBSERVACAO_FINAL", rsmFind.getString("txt_observacao"));
							elementoCatraca.put("CD_AGENTE_FINAL", rsmFind.getInt("cd_agente"));
							elementoCatraca.put("CD_USUARIO_FINAL", rsmFind.getInt("cd_usuario"));
							elementoCatraca.put("TP_LEITURA_FINAL", rsmFind.getInt("tp_leitura"));
							
							elementoCatracaResultado.put("CATRACA_FINAL_RESULTADO", 0);
							segundaLeituraMensal = true;							
						}	
						
						if(segundaLeituraMensal){
							elementoLinha.put("CATRACA_MENSAL", elementoCatraca);
							elementoLinha.put("CATRACA_RESULTADO", elementoCatracaResultado);

							elementoCatraca = new HashMap<String, Object>();
							elementoKm      = new HashMap<String, Object>();
							elementoCatracaResultado = new HashMap<String, Object>();
							
							primeiraLeituraMensal = false;
							segundaLeituraMensal  = false;
						}
						
						break;
						
					case TP_LEITURA_IMPORTACAO:
						if(dtInicial.compareTo(rsmFind.getGregorianCalendar("dt_afericao")) > 0){
							elementoCatraca.put("CD_AFERICAO_CATRACA_INICIAL", rsmFind.getInt("cd_afericao_catraca"));
							elementoCatraca.put("CD_CONCESSAO_VEICULO_INICIAL", rsmFind.getInt("cd_concessao_veiculo"));
							elementoCatraca.put("CD_LACRE_INICIAL", rsmFind.getInt("cd_lacre"));
							elementoCatraca.put("DT_AFERICAO_INICIAL", rsmFind.getTimestamp("dt_afericao"));
							elementoCatraca.put("QT_AFERIDO_INICIAL", rsmFind.getInt("qt_aferido"));
							elementoCatraca.put("QT_HODOMETRO_INICIAL", rsmFind.getDouble("qt_hodometro"));
							elementoCatraca.put("LG_HODOMETRO_ILEGIVEL_INICIAL", rsmFind.getInt("lg_hodometro_ilegivel"));
							elementoCatraca.put("TXT_OBSERVACAO_INICIAL", rsmFind.getString("txt_observacao"));
							elementoCatraca.put("CD_AGENTE_INICIAL", rsmFind.getInt("cd_agente"));
							elementoCatraca.put("CD_USUARIO_INICIAL", rsmFind.getInt("cd_usuario"));
							elementoCatraca.put("TP_LEITURA_INICIAL", rsmFind.getInt("tp_leitura"));
							
							primeiraLeituraImport = true;
							
						}else{
							elementoCatraca.put("CD_AFERICAO_CATRACA_FINAL", rsmFind.getInt("cd_afericao_catraca"));
							elementoCatraca.put("CD_CONCESSAO_VEICULO_FINAL", rsmFind.getInt("cd_concessao_veiculo"));
							elementoCatraca.put("CD_LACRE_FINAL", rsmFind.getInt("cd_lacre"));
							elementoCatraca.put("DT_AFERICAO_FINAL", rsmFind.getTimestamp("dt_afericao"));
							elementoCatraca.put("QT_AFERIDO_FINAL", rsmFind.getInt("qt_aferido"));
							elementoCatraca.put("QT_HODOMETRO_FINAL", rsmFind.getDouble("qt_hodometro"));
							elementoCatraca.put("LG_HODOMETRO_ILEGIVEL_FINAL", rsmFind.getInt("lg_hodometro_ilegivel"));
							elementoCatraca.put("TXT_OBSERVACAO_FINAL", rsmFind.getString("txt_observacao"));
							elementoCatraca.put("CD_AGENTE_FINAL", rsmFind.getInt("cd_agente"));
							elementoCatraca.put("CD_USUARIO_FINAL", rsmFind.getInt("cd_usuario"));
							elementoCatraca.put("TP_LEITURA_FINAL", rsmFind.getInt("tp_leitura"));
							
							segundaLeituraImport = true;
						}	
						
						if(segundaLeituraImport){
							elementoLinha.put("CATRACA_IMPORTACAO", elementoCatraca);
							elementoLinha.put("KM_IMPORTACAO", elementoKm);
							
							elementoCatraca = new HashMap<String, Object>();
							elementoKm      = new HashMap<String, Object>();
							
							primeiraLeituraImport = false;
							segundaLeituraImport  = false;
						}
											
						break;					
						
					default:
						break;
					}		
				}
			}
			
			//insere ultima linha
			if (elementoLinha != null){
				if (nrPrefixo > 0)
					linha.add(elementoLinha);
				
				elementoLinha = new HashMap<String, Object>();
			}
			
			rsm.setLines(linha);
			LogUtils.debug(rsm.toString() + " ");
			LogUtils.logTimer("AFERICAO_CATRACA_CALC_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.destroyTimer("AFERICAO_CATRACA_CALC_TIMER");
			
			return rsm;
		}catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoCatracaServices.calcDiferencaMensal: " + e);
			return null;
		}
	}
	
	/**
	 * Metodo para preencher o relatorio com calculo das catracas e hodometros por mes
	 */
	public static ResultSetMap calcularDiferencaMensal(ArrayList<ItemComparator> criterios) {
		return calcularDiferencaMensal(criterios, null);
	}
	public static ResultSetMap calcularDiferencaMensal(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdConcessionario = 0, mes = 0;

			String sql = "";

			String dtInicial = "";
			String dtFinal = "";
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("CD_CONCESSIONARIO")) {
					cdConcessionario = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("DT_INICIAL")) {
					dtInicial = criterios.get(i).getValue().toString();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("DT_FINAL")) {
					dtFinal = criterios.get(i).getValue().toString();
					criterios.remove(i);
					i--;
				}
			}
			
			GregorianCalendar dtAfericao = Util.convStringToCalendar(dtFinal);
			GregorianCalendar dtAfericaoMensalInicial = Util.convStringToCalendar(dtInicial);
			GregorianCalendar dtAfericaoMensalFinal = Util.convStringToCalendar(dtFinal);
			
			mes = dtAfericaoMensalInicial.get(Calendar.MONTH);
			dtAfericaoMensalInicial.add(Calendar.DATE, -2);
			dtAfericaoMensalFinal.add(Calendar.DATE, 1);
			
			Double kmTotal = 0.0;
			int passageirosTotal = 0;
			
			// Busca das concessoes_veiculo que estavam vinculados no mes escolhido
			sql = "SELECT A.* FROM mob_concessao_veiculo A" +
					" LEFT OUTER JOIN mob_concessao B ON (A.cd_concessao = B.cd_concessao)" +
					" WHERE ((dt_inicio_operacao <= '" + Util.convCalendarStringSql(dtAfericao) + " 23:59:59" + "') " +
					" AND (dt_final_operacao IS NULL OR dt_final_operacao >= '" + Util.convCalendarStringSql(dtAfericao) + " 23:59:59" + "'))" +
					" AND cd_concessionario = " + cdConcessionario +
					" ORDER BY A.nr_prefixo";
			
			pstmt = connect.prepareStatement(sql);
			ResultSetMap rsmVeiculos = new ResultSetMap(pstmt.executeQuery());
			

			while (rsmVeiculos.next()) {
				
				ArrayList<Integer> listRemocao = new ArrayList<>(); // guarda os valores das afericoes quando a catraca foi removida
				ArrayList<Integer> listAplicacao = new ArrayList<>(); // guarda os valores das afericoes quando a catraca for reaplicada
				
				// Busca as afericoes feitas na concessao_veiculo durante o periodo escolhido
				sql = "SELECT * FROM mob_afericao_catraca A" +
					" WHERE cd_concessao_veiculo = " + rsmVeiculos.getInt("CD_CONCESSAO_VEICULO") +
					" AND A.dt_afericao >= '" + Util.convCalendarStringSql(dtAfericaoMensalInicial) + " 00:00:00"  + "'" +
					" AND A.dt_afericao <= '" + Util.convCalendarStringSql(dtAfericaoMensalFinal) + " 23:59:59" + "'" +
					" ORDER BY dt_afericao";
				
				pstmt = connect.prepareStatement(sql);
				ResultSetMap rsmAfericoes = new ResultSetMap(pstmt.executeQuery());
				
				while (rsmAfericoes.next()) {					
					switch (rsmAfericoes.getInt("TP_LEITURA")) {
					
					case TP_REMOCAO_MANUTENCAO:
						if (rsmAfericoes.getGregorianCalendar("DT_AFERICAO").get(Calendar.MONTH)==mes) 
							listRemocao.add(rsmAfericoes.getInt("QT_AFERIDO"));
						break;
						
					case TP_APLICACAO_MANUTENCAO:
						if (rsmAfericoes.getGregorianCalendar("DT_AFERICAO").get(Calendar.MONTH)==mes) {
							if (!listRemocao.isEmpty())
								listAplicacao.add(rsmAfericoes.getInt("QT_AFERIDO"));
						}
						break;
						
					case TP_LEITURA_MENSAL:
						if (rsmAfericoes.getGregorianCalendar("DT_AFERICAO").get(Calendar.MONTH)>mes) {
							rsmVeiculos.setValueToField("CATRACA_FINAL", rsmAfericoes.getInt("QT_AFERIDO"));
							rsmVeiculos.setValueToField("HODOMETRO_FINAL", rsmAfericoes.getDouble("QT_HODOMETRO"));
							
						} else if (rsmAfericoes.getGregorianCalendar("DT_AFERICAO").get(Calendar.MONTH)<mes)
							if (mes!=Calendar.DECEMBER) {
								rsmVeiculos.setValueToField("CATRACA_INICIAL", rsmAfericoes.getInt("QT_AFERIDO"));
								rsmVeiculos.setValueToField("HODOMETRO_INICIAL", rsmAfericoes.getDouble("QT_HODOMETRO"));
							} else {
								rsmVeiculos.setValueToField("CATRACA_FINAL", rsmAfericoes.getInt("QT_AFERIDO"));
								rsmVeiculos.setValueToField("HODOMETRO_FINAL", rsmAfericoes.getDouble("QT_HODOMETRO"));
							}
						else {
							if (rsmAfericoes.getGregorianCalendar("DT_AFERICAO").get(Calendar.DAY_OF_MONTH)<15) {
								rsmVeiculos.setValueToField("CATRACA_INICIAL", rsmAfericoes.getInt("QT_AFERIDO"));
								rsmVeiculos.setValueToField("HODOMETRO_INICIAL", rsmAfericoes.getDouble("QT_HODOMETRO"));
							} else {
								rsmVeiculos.setValueToField("CATRACA_FINAL", rsmAfericoes.getInt("QT_AFERIDO"));
								rsmVeiculos.setValueToField("HODOMETRO_FINAL", rsmAfericoes.getDouble("QT_HODOMETRO"));
							}
						}
						break;
					
					case TP_APLICACAO_VINCULACAO:
						if (rsmAfericoes.getGregorianCalendar("DT_AFERICAO").get(Calendar.MONTH)==mes) {
							rsmVeiculos.setValueToField("CATRACA_INICIAL", rsmAfericoes.getInt("QT_AFERIDO"));
							rsmVeiculos.setValueToField("HODOMETRO_INICIAL", rsmAfericoes.getDouble("QT_HODOMETRO"));
						}
						break;
						
					case TP_REMOCAO_DESVINCULACAO:
						if (rsmAfericoes.getGregorianCalendar("DT_AFERICAO").get(Calendar.MONTH)==mes) {
							rsmVeiculos.setValueToField("CATRACA_FINAL", rsmAfericoes.getInt("QT_AFERIDO"));
							rsmVeiculos.setValueToField("HODOMETRO_FINAL", rsmAfericoes.getDouble("QT_HODOMETRO"));
						}
						break;
					
					case TP_LEITURA_AVULSA:
						if (rsmVeiculos.getObject("CATRACA_INICIAL")==null) {
							rsmVeiculos.setValueToField("CATRACA_INICIAL", rsmAfericoes.getInt("QT_AFERIDO"));
							rsmVeiculos.setValueToField("HODOMETRO_INICIAL", rsmAfericoes.getDouble("QT_HODOMETRO"));
						}
						break;
					}
				}
				
				// calculo da quantidade de passageiros transportados com base no numero da catraca
				int passageiros = 0;
				if (rsmVeiculos.getObject("CATRACA_INICIAL")!=null && rsmVeiculos.getObject("CATRACA_FINAL")!=null) {
					
					// Nao houve manutencao de catraca e nao zerou numeracao
					if (listRemocao.isEmpty() && rsmVeiculos.getInt("CATRACA_FINAL") >= rsmVeiculos.getInt("CATRACA_INICIAL")) {
						passageiros =  (rsmVeiculos.getInt("CATRACA_FINAL") - rsmVeiculos.getInt("CATRACA_INICIAL"));
					
					// Nao houve manutencao de catraca, mas zerou numeracao
					} else if (listRemocao.isEmpty() && rsmVeiculos.getInt("CATRACA_FINAL") < rsmVeiculos.getInt("CATRACA_INICIAL")) {
						passageiros = (99999 - rsmVeiculos.getInt("CATRACA_INICIAL"));
						passageiros += rsmVeiculos.getInt("CATRACA_FINAL");
					
					// Houve manutencao de catraca
					} else if (!listRemocao.isEmpty() && listRemocao.size() == listAplicacao.size()) {
						int inicial = rsmVeiculos.getInt("CATRACA_INICIAL");
						
						for (int i =0; i<listRemocao.size(); i++) {
							
							if (listRemocao.get(i)>inicial) {
								passageiros += listRemocao.get(i) - inicial;
								
							} else {
								passageiros += (99999 - inicial);
								passageiros += listRemocao.get(i);
							}
							
							inicial = listAplicacao.get(i);
						}
						
						// passageiros apos a ultima troca
						if (rsmVeiculos.getInt("CATRACA_FINAL")>inicial)
							passageiros += (rsmVeiculos.getInt("CATRACA_FINAL") - inicial);
						else {
							passageiros += (99999 - inicial);
							passageiros += rsmVeiculos.getInt("CATRACA_FINAL");
						}	
					
					// Houve manutencao de catraca, mas a quantidade de aplicacoes e remocoes eh diferente
					} else if (!listRemocao.isEmpty() && listRemocao.size() != listAplicacao.size()) {
						passageiros = -1;
					
					// Erro, caso nao previsto!
					} else
						passageiros = -2;
				}
				
				Double vlPercorrido = rsmVeiculos.getDouble("HODOMETRO_FINAL") - rsmVeiculos.getDouble("HODOMETRO_INICIAL");
				if (vlPercorrido>0)
					kmTotal += vlPercorrido;
				
				if (passageiros>0)
					passageirosTotal += passageiros;
					
				rsmVeiculos.setValueToField("VL_HODOMETRO", vlPercorrido<0?"": new DecimalFormat("#,##0.00").format(vlPercorrido));
				rsmVeiculos.setValueToField("QT_MANUTENCAO", listRemocao.size());
				rsmVeiculos.setValueToField("QT_PASSAGEIROS", passageiros);
			}
			
			rsmVeiculos.setValueToField("KM_TOTAL", new DecimalFormat("#,##0.00").format(kmTotal));
			rsmVeiculos.setValueToField("PASSAGEIROS_TOTAL", passageirosTotal);

			return rsmVeiculos;
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
	
	public static HashMap<String, Object> getSyncData() {
		return getSyncData(null, null, null);
	}

	public static HashMap<String, Object> getSyncData(ArrayList<AfericaoCatraca> afericaoCatraca, ArrayList<AfericaoImpedimento> afericaoImpedimento, ArrayList<LacreCatraca> lacreCatraca) {
		return getSyncData(afericaoCatraca, afericaoImpedimento, lacreCatraca, null);
	}

	public static HashMap<String, Object> getSyncData(ArrayList<AfericaoCatraca> afericaoCatraca, ArrayList<AfericaoImpedimento> afericaoImpedimento, ArrayList<LacreCatraca> lacreCatraca, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(afericaoCatraca != null && afericaoCatraca.size() > 0) {
				for(AfericaoCatraca aC : afericaoCatraca) {
					
					List<AfericaoImpedimento> impedimentos = new ArrayList<AfericaoImpedimento>();
					List<LacreCatraca> lacreCatracas = new ArrayList<LacreCatraca>();
					
					if(afericaoImpedimento != null) {
						impedimentos = afericaoImpedimento.stream()
								.filter(impedimento -> impedimento.getCdAfericaoCatraca() == aC.getCdAfericaoCatraca())
				                .collect(Collectors.toList());
					}
					
					if(lacreCatraca != null) {
						lacreCatracas = lacreCatraca.stream()
								.filter(lacre -> lacre.getCdAfericaoAplicacao() == aC.getCdAfericaoCatraca() || lacre.getCdAfericaoRemocao() == aC.getCdAfericaoCatraca())
				                .collect(Collectors.toList());	
					}
					
					aC.setCdAfericaoCatraca(0);
					Result save = save(aC, null, ConcessaoVeiculoDAO.get(aC.getCdConcessaoVeiculo()).getNrPrefixo(), connect);
					
					if(save.getCode() <= 0) {
						throw new Exception("Não foi poss�vel completar a sincronização");
					}

					AfericaoCatraca afericaoCatracaNovo = (AfericaoCatraca) (save.getObjects().get("AFERICAOCATRACA"));
					
					for(AfericaoImpedimento imp : impedimentos) {
						imp.setCdAfericaoImpedimento(0);
						imp.setCdAfericaoCatraca(afericaoCatracaNovo.getCdAfericaoCatraca());
						save = AfericaoImpedimentoServices.save(imp, connect);
						
						if(save.getCode() <= 0) {
							throw new Exception("Não foi poss�vel completar a sincronização");
						}
					}
					/*
					for(LacreCatraca lacre : lacreCatracas) {						
						if(aC.getTpLeitura() == AfericaoCatracaServices.TP_APLICACAO_VINCULACAO) {
							lacre.setCdLacreCatraca(0);
							lacre.setCdAfericaoAplicacao(afericaoCatracaNovo.getCdAfericaoCatraca());
						} else {
							lacre.setCdAfericaoRemocao(afericaoCatracaNovo.getCdAfericaoCatraca());
						}
						
						save = LacreCatracaServices.save(lacre, null, connect);
						
						if(save.getCode() <= 0) {
							throw new Exception("Não foi poss�vel completar a sincronização");
						}
					}
					*/
				}
				
				connect.commit();
			}
			
			String sqlAfericao = "SELECT * FROM ( SELECT DISTINCT ON (sA.cd_concessao_veiculo) sA.* " + 
						"    FROM mob_afericao_catraca sA, mob_concessao_veiculo sB " + 
						"   WHERE sA.cd_concessao_veiculo = sB.cd_concessao_veiculo " + 
						"     AND (sB.st_concessao_veiculo = ? OR sB.st_concessao_veiculo = ?)" + 
						"ORDER BY cd_concessao_veiculo) cv ORDER BY cv.cd_afericao_catraca";
	
			PreparedStatement pstmt1 = connect.prepareStatement(sqlAfericao);
			pstmt1.setInt(1, ConcessaoVeiculoServices.ST_VINCULADO);
			pstmt1.setInt(2, ConcessaoVeiculoServices.ST_LACRADO);
			
			String sqlLacres = " SELECT sA.* " + 
					"    FROM mob_lacre_catraca sA, mob_concessao_veiculo sB " + 
					"   WHERE sA.cd_concessao_veiculo = sB.cd_concessao_veiculo " + 
					"     AND (sB.st_concessao_veiculo = ? OR sB.st_concessao_veiculo = ?)" + 
					"ORDER BY sA.cd_lacre_catraca";

			PreparedStatement pstmt2 = connect.prepareStatement(sqlLacres);
			pstmt2.setInt(1, ConcessaoVeiculoServices.ST_VINCULADO);
			pstmt2.setInt(2, ConcessaoVeiculoServices.ST_LACRADO);

			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("AfericaoCatraca", Util.resultSetToArrayList(pstmt1.executeQuery()));
			register.put("LacreCatraca", Util.resultSetToArrayList(pstmt2.executeQuery()));
						
			return register;
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
}
