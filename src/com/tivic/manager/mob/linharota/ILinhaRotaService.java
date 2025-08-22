package com.tivic.manager.mob.linharota;

import java.sql.Connection;

import com.tivic.manager.mob.LinhaRota;

public interface ILinhaRotaService {
	public LinhaRota remove(int cdLinha, int cdRota) throws Exception;
	public LinhaRota remove(int cdLinha, int cdRota, Connection connection) throws Exception;
	public LinhaRota inactivate(LinhaRota linhaRota) throws Exception;
	public LinhaRota inactivate(LinhaRota linhaRota, Connection connection) throws Exception;
	public LinhaRota activate(LinhaRota linhaRota) throws Exception;
	public LinhaRota activate(LinhaRota linhaRota, Connection connection) throws Exception;
}
