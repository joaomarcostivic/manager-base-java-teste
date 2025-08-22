package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.seg.AuthData;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class LacreCatracaServices {
	
	public static Result save(LacreCatraca lacreCatraca){
		return save(lacreCatraca, null, null);
	}

	public static Result save(LacreCatraca lacreCatraca, AuthData authData){
		return save(lacreCatraca, authData, null);
	}

	public static Result save(LacreCatraca lacreCatraca, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(lacreCatraca==null)
				return new Result(-1, "Erro ao salvar. LacreCatraca é nulo");

			int retorno;
			if(lacreCatraca.getCdLacreCatraca()==0){
				retorno = LacreCatracaDAO.insert(lacreCatraca, connect);
				lacreCatraca.setCdLacreCatraca(retorno);
			}
			else {
				retorno = LacreCatracaDAO.update(lacreCatraca, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LACRECATRACA", lacreCatraca);
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
	

	/**
	 * Save utilizado pelo aplicativo, onde o id do lacre é enviado, pois não
	 * houve como carregá-los com antecedência ou por falta de rede, ou por
	 * falta de cadastro prévio
	 * 
	 * @param lacreCatraca
	 * @param idLacre
	 * @param afericaoCatraca
	 * @param stConcessaoVeiculo
	 * @return
	 */
	
	public static Result save(LacreCatraca lacreCatraca, String idLacre, AfericaoCatraca afericaoCatraca,
			ArrayList<Integer> impedimentos, int stConcessaoVeiculo) {
		return save(lacreCatraca, idLacre, afericaoCatraca, impedimentos, stConcessaoVeiculo, null);
	}

	
	public static Result save(LacreCatraca lacreCatraca, AfericaoCatraca afericaoCatraca,
			ArrayList<Integer> impedimentos, int stConcessaoVeiculo) {
		return save(lacreCatraca, null, afericaoCatraca, impedimentos, stConcessaoVeiculo, null);
	}

	public static Result save(LacreCatraca lacreCatraca, String idLacre, AfericaoCatraca afericaoCatraca,
			ArrayList<Integer> impedimentos, int stConcessaoVeiculo, Connection connect) {
		boolean isConnectionNull = connect == null;
		Result resultAfericao;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (lacreCatraca == null)
				return new Result(-1, "Erro ao salvar. LacreCatraca é nulo");

//			if(afericaoCatraca.getQtHodometro() <= 0.0){
//				Conexao.rollback(connect);
//				return new Result(-1, "Erro: Hodômetro não informado.");
//			}
//			
//			if(afericaoCatraca.getQtAferido() <= 0){
//				Conexao.rollback(connect);
//				return new Result(-1, "Erro: Catraca não informada.");
//			}
			
			int retorno;
			//lançando uma nova aferição aplicando novo lacre
			if (lacreCatraca.getCdLacreCatraca() == 0) {
				if (idLacre != null && idLacre.trim().length() > 0) {
					//verifica se o lacre já está cadastrado
					ArrayList<ItemComparator> criterios = new ArrayList<>();
					criterios.add(new ItemComparator("id_lacre", idLacre.substring(1), ItemComparator.LIKE, Types.VARCHAR));
					criterios.add(new ItemComparator("id_serie", idLacre.substring(0, 1), ItemComparator.LIKE, Types.VARCHAR));
					
					ResultSetMap rsmLacre = LacreServices.find(criterios);
										
					if(rsmLacre.next()){						
						//
						if(rsmLacre.getInt("st_lacre") == LacreServices.ST_ATIVO){
							//
							criterios = new ArrayList<>();
							criterios.add(new ItemComparator("G.cd_lacre", String.valueOf(rsmLacre.getInt("cd_lacre")), ItemComparator.EQUAL, Types.INTEGER));
							ResultSetMap rsmLacreVeiculo = LacreServices.findVeiculosLacrados(criterios);
							//
							if(rsmLacreVeiculo.next()){								
								Conexao.rollback(connect);								
								return new Result(-1, "Erro: Este número de lacre já está informado para o veículo com Prefixo " + rsmLacreVeiculo.getInt("nr_prefixo"));
							}
							else{
								Conexao.rollback(connect);								
								return new Result(-1, "Erro: Este número de lacre já está informado como ATIVO.");
							}
						}
						//
						if(rsmLacre.getInt("st_lacre") == LacreServices.ST_INATIVO){
							Conexao.rollback(connect);
							return new Result(-1, "Erro: Este número de lacre encontra-se INATIVADO.");
						}
						
						lacreCatraca.setCdLacre(rsmLacre.getInt("cd_lacre"));
						afericaoCatraca.setCdLacre(rsmLacre.getInt("cd_lacre"));
							
					}//Cadastra o lacre do zero
					else{						
						Lacre lacre = new Lacre();
						lacre.setIdLacre(idLacre.substring(1));
						lacre.setIdSerie(idLacre.substring(0, 1));
						lacre.setStLacre(LacreServices.ST_ATIVO);
						Result resultLacre = LacreServices.save(lacre, connect);
						if (resultLacre.getCode() <= 0) {
							Conexao.rollback(connect);
							return new Result(-1, resultLacre.getMessage());
						}
						lacreCatraca.setCdLacre(resultLacre.getCode());
						afericaoCatraca.setCdLacre(resultLacre.getCode());
					}
				}else{
					Conexao.rollback(connect);
					return new Result(-1, "Erro: O lacre deve ser informado.");
				}
				resultAfericao = AfericaoCatracaServices.save(afericaoCatraca, impedimentos);
				if (resultAfericao.getCode() <= 0) {
					Conexao.rollback(connect);
					return new Result(resultAfericao.getCode(), "Erro ao lançar aferição da aplicação do lacre...");
				}
				lacreCatraca.setCdAfericaoAplicacao(resultAfericao.getCode());

				retorno = LacreCatracaDAO.insert(lacreCatraca, connect);
				lacreCatraca.setCdLacreCatraca(retorno);
				if (retorno <= 0) {
					Conexao.rollback(connect);
					return new Result(retorno, "Erro ao registrar o lacre...");
				}

			} else {
				/**
				 * Verifica se a atualização é uma remoção de lacre, para
				 * inativá-lo e lançar a aferição
				 */
				LacreCatraca lacreCatracaRegistrado = LacreCatracaDAO.get(lacreCatraca.getCdLacreCatraca());
				if (lacreCatraca.getCdAfericaoRemocao() == 0 && lacreCatracaRegistrado.getCdAfericaoRemocao() == 0) {
					Lacre lacre = LacreDAO.get(lacreCatraca.getCdLacre());
					lacre.setStLacre(LacreServices.ST_INATIVO);
					Result resultLacre = LacreServices.save(lacre, connect);
					if (resultLacre.getCode() <= 0) {
						Conexao.rollback(connect);
						return new Result(resultLacre.getCode(), "Erro ao lançar lacre da remoção da catraca...");
					}
					
					resultAfericao = AfericaoCatracaServices.save(afericaoCatraca, impedimentos);
					if (resultAfericao.getCode() <= 0) {
						Conexao.rollback(connect);
						return new Result(resultAfericao.getCode(), "Erro ao lançar leitura da remoção da catraca...");
					}
					lacreCatraca.setCdAfericaoRemocao(resultAfericao.getCode());
				}
				retorno = LacreCatracaDAO.update(lacreCatraca, connect);
			}

			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(lacreCatraca.getCdConcessaoVeiculo());
			concessaoVeiculo.setStConcessaoVeiculo(stConcessaoVeiculo);
			if (ConcessaoVeiculoServices.save(concessaoVeiculo).getCode() <= 0) {
				Conexao.rollback(connect);
				return new Result(retorno, "Erro ao atualizar situação do veículo...");
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "LACRECATRACA", lacreCatraca);
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static LacreCatraca ativarLacre(int cdLacre, int cdConcessaoVeiculo, int cdAfericaoCatraca, Connection connection) throws ValidationException {
		
		if (LacreServices.isDisponivel(cdLacre, connection)) {
			
			LacreCatraca lacreCatraca = new LacreCatraca();
			lacreCatraca.setCdLacre(cdLacre);
			lacreCatraca.setCdConcessaoVeiculo(cdConcessaoVeiculo);
			lacreCatraca.setCdAfericaoAplicacao(cdAfericaoCatraca);
			
			int cdLacreCatraca = LacreCatracaDAO.insert(lacreCatraca, connection);
			if (cdLacreCatraca <= 0) {
				Conexao.rollback(connection);
				throw new ValidationException("Erro ao ativar lacreCatraca.");
			}
			
			lacreCatraca.setCdLacreCatraca(cdLacreCatraca);
			
			int cod = LacreServices.atualizarSituacao(cdLacre, connection);
			if (cod <= 0) {
				Conexao.rollback(connection);
				throw new ValidationException("Erro ao atualizar situacao do lacre.");
			}
			
			return lacreCatraca;
		
		} else {
		
			throw new ValidationException("Erro ao ativar lacre. O lacre não está disponível.");
		}
	}
	
	public static LacreCatraca inativarLacre(int cdLacre, int cdConcessaoVeiculo, int cdAfericaoCatraca, Connection connection) throws ValidationException {
		
		if (LacreServices.isAtivo(cdLacre, connection)) {
			
			LacreCatraca lacreCatraca = null;
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_lacre", String.valueOf(cdLacre), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = LacreCatracaDAO.find(criterios);
			if(rsm.next()) {
				lacreCatraca = LacreCatracaDAO.get(rsm.getInt("cd_lacre_catraca"), connection);
			} else {
				throw new ValidationException("Erro ao buscar lacreCatraca.");
			}
			
			lacreCatraca.setCdAfericaoRemocao(cdAfericaoCatraca);
			int cod = LacreCatracaDAO.update(lacreCatraca, connection);
			
			if (cod <= 0) {
				Conexao.rollback(connection);
				throw new ValidationException("Erro ao inativar lacreCatraca.");
			}
			
			cod = LacreServices.atualizarSituacao(cdLacre, connection);
			if (cod <= 0) {
				Conexao.rollback(connection);
				throw new ValidationException("Erro ao atualizar situacao do lacre.");
			}
			
			return lacreCatraca;
		
		} else {
		
			throw new ValidationException("Erro ao inativar lacre. O lacre não está ativo.");
		}
	}
	

	public static Result remove(int cdLacreCatraca) {
		return remove(cdLacreCatraca, false, null);
	}

	public static Result remove(int cdLacreCatraca, boolean cascade) {
		return remove(cdLacreCatraca, cascade, null);
	}

	public static Result remove(int cdLacreCatraca, boolean cascade, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if (cascade) {
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if (!cascade || retorno > 0)
				retorno = LacreCatracaDAO.delete(cdLacreCatraca, connect);
			if (retorno <= 0) {
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			} else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_lacre_catraca");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaServices.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_lacre_catraca", criterios,
				connect != null ? connect : Conexao.conectar(), connect == null);
	}
	
	public static int updateLacre(LacreCatraca lacreCatraca) {
		return LacreCatracaDAO.update(lacreCatraca);
	}

}
