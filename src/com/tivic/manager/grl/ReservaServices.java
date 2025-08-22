package com.tivic.manager.grl;

import sol.util.Result;
import sol.dao.ResultSetMap;
import java.sql.*;
import java.util.GregorianCalendar;

import com.tivic.manager.adm.ContratoServices;
import com.tivic.sol.connection.Conexao;


public class ReservaServices {
	public static final String[] tipoReserva = {"Operacional/Vendas","Gerência de Vendas","Diretoria Construtora"};
	
	public static Result save(Reserva objeto)	{
		Connection connect = Conexao.conectar();
		try	{
			// Verificações na INCLUSÃO
			if(objeto.getCdReserva() <= 0){
				objeto.setDtReserva(new GregorianCalendar());
				objeto.setStReserva(1); // Ativa
				// Se for reserva de referencia 
				if(objeto.getCdReferencia() > 0)	{
					// Verifica a existência de outra reserva
					PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_reserva " +
							                                           "WHERE cd_empresa         = " +objeto.getCdEmpresa() +
							                                           "  AND cd_produto_servico = " +objeto.getCdProdutoServico() +
							                                           "  AND cd_referencia      = "+objeto.getCdReferencia()+
							                                           "  AND st_reserva         = 1 "+
							                                           "  AND dt_validade >= ? ");
					pstmt.setTimestamp(1, new Timestamp(new GregorianCalendar().getTimeInMillis()));
					ResultSet rs = pstmt.executeQuery();
					if(rs.next())
						return new Result(-1, "Esta unidade já está reservada!");
					// Verifica se não foi vendido
					pstmt = connect.prepareStatement("SELECT * FROM adm_contrato A, adm_contrato_produto_servico B " +
						                             "WHERE A.cd_contrato = B.cd_contrato " +
						                             "  AND A.st_contrato IN ("+ContratoServices.ST_ATIVO+","+ContratoServices.ST_ANALISE+
						                             ","+ContratoServices.ST_REPROVADO+") " +
						                             "  and B.cd_empresa         = " + objeto.getCdEmpresa() +
						                             "  AND B.cd_produto_servico = " + objeto.getCdProdutoServico() +
						                             "  AND B.cd_referencia      = " + objeto.getCdReferencia());
					rs = pstmt.executeQuery();
					if(rs.next())
						return new Result(-1, "Unidade já vendida!");
				}
			}
			if(objeto.getCdReserva() <= 0)
				return new Result(ReservaDAO.insert(objeto, connect));
			else
				return new Result(ReservaDAO.update(objeto, connect));
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar salvar reserva!", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static Result cancelarReserva(int cdReserva, int cdUsuario)	{
		Connection connect = Conexao.conectar();
		try	{
			if(cdUsuario <= 0)
				return new Result(-1, "O usuário responsável pelo cancelamento não foi informado!");
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_reserva " +
					                                           "SET st_reserva = 0, cd_usuario_cancelamento = "+cdUsuario+", dt_cancelamento = ? "+
					                                           "WHERE cd_reserva = "+cdReserva+
					                                           "  AND st_reserva = 1");
			pstmt.setTimestamp(1, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar cancelar reserva!", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAsResult(int cdReserva){
		Connection connect = Conexao.conectar();
		try	{
			String sql =
					"SELECT A.*, C.nm_pessoa AS nm_atendimento, D.nm_pessoa AS nm_cliente, " +
		            "       E.nm_pessoa AS nm_responsavel, F.nm_login, G.nm_pessoa AS nm_usuario " +
		            "FROM grl_reserva A " +
		            "LEFT OUTER JOIN crm_atendimento B ON (A.cd_atendimento = B.cd_atendimento)" +
		            "LEFT OUTER JOIN grl_pessoa      C ON (B.cd_pessoa = C.cd_pessoa) " +
		            "LEFT OUTER JOIN grl_pessoa      D ON (A.cd_pessoa = D.cd_pessoa) " +
		            "LEFT OUTER JOIN grl_pessoa      E ON (A.cd_responsavel = E.cd_pessoa) " +
		            "LEFT OUTER JOIN seg_usuario     F ON (A.cd_usuario = F.cd_usuario) " +
		            "LEFT OUTER JOIN grl_pessoa      G ON (F.cd_pessoa = G.cd_pessoa) " +
					"WHERE A.cd_reserva = "+cdReserva;
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
}
