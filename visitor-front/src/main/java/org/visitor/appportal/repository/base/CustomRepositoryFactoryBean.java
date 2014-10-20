package org.visitor.appportal.repository.base;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class CustomRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable>
		extends JpaRepositoryFactoryBean<T, S, ID> {
	
	/* (non-Javadoc)
	 * @see org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean#createRepositoryFactory(javax.persistence.EntityManager)
	 */
	@Override
	protected RepositoryFactorySupport createRepositoryFactory(
			EntityManager entityManager) {
		return new CustomRepositoryFactory(entityManager);
	}
	
	private static class CustomRepositoryFactory extends JpaRepositoryFactory{
		public CustomRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
		}
		/**
		 * Callback to create a {@link JpaRepository} instance with the given {@link EntityManager}
		 * 
		 * @param <T>
		 * @param <ID>
		 * @param entityManager
		 * @see #getTargetRepository(RepositoryMetadata)
		 * @return
		 */
		@SuppressWarnings({ "unchecked" })
		protected <T, ID extends Serializable> JpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata,
				EntityManager entityManager) {

			JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainClass());

			return new CustomRepositoryImpl(entityInformation, entityManager);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.springframework.data.repository.support.RepositoryFactorySupport#
		 * getRepositoryBaseClass()
		 */
		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return CustomRepositoryImpl.class;
		}		
	 }
}
