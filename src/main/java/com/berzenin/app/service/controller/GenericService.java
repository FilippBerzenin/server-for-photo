package com.berzenin.app.service.controller;

import java.util.List;

import javax.validation.Valid;

public interface GenericService<E> {

	public List<E> findAll();

	public E findById(Long id);

	public E add(@Valid E entity);

	public void remove(E entity);

	public void removeById(Long id);

	public E update(@Valid E entity);

}

