package com.tivic.manager.adapter.base.antiga;

public interface IAdapterService<T, K> {
	T toBaseAntiga(K object) throws Exception;
	K toBaseNova(T objectOld) throws Exception;
}
