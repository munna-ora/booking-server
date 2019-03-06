package com.orastays.bookingserver.dao;

import org.springframework.stereotype.Repository;

import com.orastays.bookingserver.entity.SacCodeEntity;

@Repository
public class SacCodeDAO extends GenericDAO<SacCodeEntity, Long> {

	private static final long serialVersionUID = -2475670577555521362L;

	public SacCodeDAO() {
		super(SacCodeEntity.class);

	}
}
