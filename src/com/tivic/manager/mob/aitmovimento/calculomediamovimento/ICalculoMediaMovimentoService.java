package com.tivic.manager.mob.aitmovimento.calculomediamovimento;

public interface ICalculoMediaMovimentoService {

	public CalculoMediaDTO mediaJulgamento() throws Exception;
	public CalculoMediaDTO mediaEspera() throws Exception;
	public CalculoMediaDTO qtTaloesDisponiveis() throws Exception;
	
}
