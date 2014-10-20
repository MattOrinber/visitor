package org.visitor.appportal.front.repository.base;

import static org.springframework.core.GenericTypeResolver.resolveTypeArguments;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.springframework.data.repository.CrudRepository;

public class RepositoryDomainConverter implements ConditionalGenericConverter,
		ApplicationContextAware {

	private Map<Class<?>, CrudRepository<?, Serializable>> daoMap;
	private final ConversionService service;

	/**
	 * @param service
	 */
	public RepositoryDomainConverter(ConversionService service) {
		this.service = service;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.core.convert.converter.GenericConverter#
	 * getConvertibleTypes()
	 */
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Object.class, Object.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.core.convert.converter.GenericConverter#convert(java
	 * .lang.Object, org.springframework.core.convert.TypeDescriptor,
	 * org.springframework.core.convert.TypeDescriptor)
	 */
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		CrudRepository<?, Serializable> dao = getRepositoryForDomainType(targetType.getType());
        Serializable id = service.convert(source, getIdClass(dao.getClass()));
        return dao.findOne(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.core.convert.converter.ConditionalGenericConverter
	 * #matches(org.springframework.core.convert.TypeDescriptor,
	 * org.springframework.core.convert.TypeDescriptor)
	 */
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		CrudRepository<?, Serializable> dao = getRepositoryForDomainType(targetType.getType());
        if (dao == null) {
            return false;
        }
        Class<? extends Serializable> idClass = getIdClass(dao.getClass());
        return service.canConvert(sourceType.getType(), idClass);
	}

	private CrudRepository<?, Serializable> getRepositoryForDomainType(Class<?> domainType) {
		return daoMap.get(domainType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext
	 * (org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext context) {
		@SuppressWarnings("rawtypes")
		Collection<CrudRepository> daos = BeanFactoryUtils
        	.beansOfTypeIncludingAncestors(context, CrudRepository.class).values();

        this.daoMap = new HashMap<Class<?>, CrudRepository<?, Serializable>>(daos.size());

        for (CrudRepository<Object, Serializable> dao : daos) {
            Class<?> domainClass = getDomainClass(dao.getClass());
            this.daoMap.put(domainClass, (CrudRepository<Object, Serializable>) dao);
        }
	}
	
    @SuppressWarnings("unchecked")
    public static Class<? extends Serializable> getIdClass(Class<?> clazz) {
        Class<?>[] arguments = resolveTypeArguments(clazz, CrudRepository.class);
        return (Class<? extends Serializable>) (arguments == null ? null : arguments[1]);
    }
    
    public static Class<?> getDomainClass(Class<?> clazz) {
        Class<?>[] arguments = resolveTypeArguments(clazz, CrudRepository.class);
        return arguments == null ? null : arguments[0];
    }

}
