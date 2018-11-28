package net.ytoec.kernel.service;

public interface BaseService<T> {

	public boolean add(T entity);

	public boolean edit(T entity);

	public boolean remove(T entity);

	public T get(T entity);

}
