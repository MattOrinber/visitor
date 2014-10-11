/**
 * 
 */
package org.visitor.app.solr.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.Query;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.handler.component.QueryComponent;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.SyntaxError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mengw
 *
 */
public class FilterQueryComponent extends QueryComponent {
	public static final String COMPONENT_NAME = "filterquery";
	public static final String FILTER_QUERY_NAME = COMPONENT_NAME + "." + CommonParams.FQ;
	protected static final Logger log = LoggerFactory.getLogger(FilterQueryComponent.class);
	
	/**
	 * 
	 */
	public FilterQueryComponent() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepare(ResponseBuilder rb) throws IOException {
	    SolrQueryRequest req = rb.req;
	    String[] fqs = req.getParams().getParams(FILTER_QUERY_NAME);
	    if (fqs != null && fqs.length!=0) {
	    	List<Query> filters = rb.getFilters();
	        // if filters already exists, make a copy instead of modifying the original
	        filters = filters == null ? new ArrayList<Query>(fqs.length) : new ArrayList<Query>(filters);
	        for (String fq : fqs) {
	        	if (fq != null && fq.trim().length()!=0) {
	        		QParser fqp;
					try {
						fqp = QParser.getParser(fq, null, req);
		        		filters.add(fqp.getQuery());
					} catch (SyntaxError e) {
						log.error(e.getMessage(), e);
					}
	        	}
	        }
	        // only set the filters if they are not empty otherwise
	        // fq=&someotherParam= will trigger all docs filter for every request 
	        // if filter cache is disabled
	        if (!filters.isEmpty()) {
	        	rb.setFilters(filters);
	        }
	     }
		super.prepare(rb);
	}

	@Override
	public void process(ResponseBuilder rb) throws IOException {
		super.process(rb);
	}

	
}
