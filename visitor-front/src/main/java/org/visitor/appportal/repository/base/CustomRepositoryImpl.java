package org.visitor.appportal.repository.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class CustomRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> 
	implements CustomRepository<T> {
	private Class<T> type;
	private Logger logger = LoggerFactory.getLogger(CustomRepositoryImpl.class);
	private final EntityManager em;
	
	public CustomRepositoryImpl(JpaEntityInformation<T, ID> entityInformation,
			EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.em = entityManager;
		type = entityInformation.getJavaType();
	}

	public List<T> find(T entity, SearchTemplate searchTemplate) {
		SearchTemplate localSearchTemplate = getLocalSearchTemplate(searchTemplate);

		Criteria criteria = getCriteria(entity, localSearchTemplate);
		HibernateUtil.applyPaginationAndOrderOnCriteria(criteria, localSearchTemplate);

		@SuppressWarnings("unchecked")
		List<T> entities = (List<T>) criteria.list();
		if (logger.isDebugEnabled()) {
			logger.debug("Returned " + entities.size() + " elements");
		}
		
		return entities;
	}


	public int findCount(T entity, SearchTemplate searchTemplate) {
		// TODO Auto-generated method stub
		SearchTemplate localSearchTemplate = getLocalSearchTemplate(searchTemplate);

		Criteria criteria = getCriteria(entity, localSearchTemplate);
		criteria.setProjection(Projections.rowCount());

		Number count = (Number) criteria.uniqueResult();

		if (count != null) {
			return count.intValue();
		} else {
			logger.warn("findCount returned null!");
			return 0;
		}
	}
	
	/**
	 * Simple helper to obtain the current Session.
	 */
	protected Session getCurrentSession() {
		Session sess = (Session) em.getDelegate();
		if (!sess.isOpen()) {
			sess = (Session) em.getEntityManagerFactory().createEntityManager().getDelegate();
		}
		return sess;
	}
	
	/**
	 * Create a new search template, taking into account the passed
	 * searchTemplate and the cacheable and cacheRegion properties of this
	 * instance.
	 */
	public SearchTemplate getLocalSearchTemplate(SearchTemplate searchTemplate) {
		if (searchTemplate == null) {
			SearchTemplate localSearchTemplate = new SearchTemplate();
			localSearchTemplate.setCacheable(getCacheable());
			localSearchTemplate.setCacheRegion(getCacheRegion());
			return localSearchTemplate;
		}

		SearchTemplate localSearchTemplate = new SearchTemplate(searchTemplate);

		if (searchTemplate.isCacheable() == null) {
			localSearchTemplate.setCacheable(getCacheable());
		}

		if (!searchTemplate.hasCacheRegion()) {
			localSearchTemplate.setCacheRegion(getCacheRegion());
		}

		return localSearchTemplate;
	}
	
	/**
	 * Create a criteria with caching enabled
	 * 
	 * @param entity
	 *            the entity to use in search by Example
	 * @param searchTemplate
	 *            the specific parameters such as named queries, extra infos,
	 *            limitations, order, ...
	 * @return an hibernate criteria
	 */
	protected Criteria getCriteria(T entity, SearchTemplate searchTemplate) {
		Criteria criteria = getCurrentSession().createCriteria(type);

		List<Criterion> criterions = getCriterions(entity, searchTemplate);
		for (Criterion criterion : criterions) {
			criteria.add(criterion); // AND
		}

		return criteria;
	}
	
	protected List<Criterion> getCriterions(T entity, SearchTemplate searchTemplate) {
		List<Criterion> criterions = new ArrayList<Criterion>();

		// search by date range
		Criterion dateCriterion = getByDateCriterion(searchTemplate);
		if (dateCriterion != null) {
			criterions.add(dateCriterion);
		}

		// search by example
		Criterion exampleCriterion = getByExampleCriterion(entity, searchTemplate);
		if (exampleCriterion != null) {
			criterions.add(exampleCriterion);
		}

		// search by pattern
		Criterion patternCriterion = getByPatternCriterion(searchTemplate);
		if (patternCriterion != null) {
			criterions.add(patternCriterion);
		}

		// additional criterion (for example isNull criterion on x-to-many
		// association)
		for (Criterion c : searchTemplate.getCriterions()) {
			criterions.add(c);
		}

		return criterions;
	}
	
	protected Criterion getByDateCriterion(SearchTemplate searchTemplate) {
		return HibernateUtil.constructDate(searchTemplate);
	}
	
	protected Criterion getByExampleCriterion(T entity, SearchTemplate searchTemplate) {
		Criterion example = HibernateUtil.constructExample(entity, searchTemplate);
		return example;
	}
	
	protected Criterion getByPatternCriterion(SearchTemplate searchTemplate) {
		return HibernateUtil.constructPattern(type, searchTemplate);
	}

	// ------------------------------------------
	// Configuration
	// ------------------------------------------
	private boolean cacheable = true;
	private String cacheRegion;

	/**
	 * Set the default cacheable property to be used when the searchTemplate
	 * argument is null or do not specify one.
	 */
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	public boolean getCacheable() {
		return cacheable;
	}

	/**
	 * Set the default cacheRegion property to use when the searchTemplate
	 * argument is null or do not specify one.
	 */
	public void setCacheRegion(String cacheRegion) {
		this.cacheRegion = cacheRegion;
	}

	public String getCacheRegion() {
		return cacheRegion;
	}
}
