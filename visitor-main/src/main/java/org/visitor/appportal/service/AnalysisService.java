/**
 * 
 */
package org.visitor.appportal.service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseSentenceTokenizerFactory;
import org.apache.lucene.analysis.cn.smart.SmartChineseWordTokenFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.util.AbstractAnalysisFactory;
import org.apache.lucene.analysis.util.CharFilterFactory;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.AttributeReflector;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;
import org.apache.solr.analysis.TokenizerChain;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.handler.AnalysisRequestHandlerBase.TokenTrackingAttribute;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.TextField;
import org.springframework.stereotype.Service;

/**
 * @author mengw
 *
 */
@Service
public class AnalysisService {
	
	static Map<String,String> ATTRIBUTE_MAPPING = Collections.unmodifiableMap(new HashMap<String,String>() {
		private static final long serialVersionUID = 5663741287405888312L;
		{
			put(OffsetAttribute.class.getName() + "#startOffset", "start");
			put(OffsetAttribute.class.getName() + "#endOffset", "end");
			put(TypeAttribute.class.getName() + "#type", "type");
			put(TokenTrackingAttribute.class.getName() + "#position", "position");
			put(TokenTrackingAttribute.class.getName() + "#positionHistory", "positionHistory");
		}
	});
	
	/**
	 * 
	 */
	public AnalysisService() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAnalyzeValue(String value) {
		List<String> list = new ArrayList<String>();
		NamedList<? extends Object> result = getAnalyzedValue(value);
		if(null != result && result.size() > 1) {
			Object sentence = result.getVal(0);
			List<NamedList<Object>> entryValue = (List<NamedList<Object>>)sentence;
			for(NamedList<Object> entry : entryValue) {
				list.add(String.valueOf(entry.get("text")));
			}
			sentence = result.getVal(result.size() - 1);
			
			entryValue = (List<NamedList<Object>>)sentence;
			for(NamedList<Object> entry : entryValue) {
				list.add(String.valueOf(entry.get("text")));
			}			
		}
		return list;
	}
	
	@SuppressWarnings("resource")
	public NamedList<? extends Object> getAnalyzedValue(String value) {
		TokenizerChain tokenizerChain = getTokenizerChain();
	    CharFilterFactory[] cfiltfacs = tokenizerChain.getCharFilterFactories();
	    TokenizerFactory tfac = tokenizerChain.getTokenizerFactory();
	    TokenFilterFactory[] filtfacs = tokenizerChain.getTokenFilterFactories();

	    NamedList<Object> namedList = new NamedList<Object>();

	    if( cfiltfacs != null ){
	    	String source = value;
	    	for(CharFilterFactory cfiltfac : cfiltfacs ){
	    		Reader reader = new StringReader(source);
	    		reader = cfiltfac.create(reader);
	    		source = writeCharStream(namedList, reader);
	    	}
	    }

	    TokenStream tokenStream = tfac.create(tokenizerChain.initReader(null, new StringReader(value)));
	    List<AttributeSource> tokens = analyzeTokenStream(tokenStream);

	    FieldType type = new TextField();
	    Set<BytesRef> set = new HashSet<BytesRef>();
	    
	    namedList.add(tokenStream.getClass().getName(), convertTokensToNamedLists(tokens, type, set));

	    ListBasedTokenStream listBasedTokenStream = new ListBasedTokenStream(tokens);

	    for (TokenFilterFactory tokenFilterFactory : filtfacs) {
	    	for (final AttributeSource tok : tokens) {
	    		tok.getAttribute(TokenTrackingAttribute.class).freezeStage();
	    	}
	    	tokenStream = tokenFilterFactory.create(listBasedTokenStream);
	    	tokens = analyzeTokenStream(tokenStream);
	    	namedList.add(tokenStream.getClass().getName(), convertTokensToNamedLists(tokens, type, set));
	    	listBasedTokenStream = new ListBasedTokenStream(tokens);
	    }
	    return namedList;
	}


	protected TokenizerChain getTokenizerChain() {
		SmartChineseSentenceTokenizerFactory sentence = new SmartChineseSentenceTokenizerFactory(new HashMap<String, String>());
		SmartChineseWordTokenFilterFactory word = new SmartChineseWordTokenFilterFactory(new HashMap<String, String>());
		Map<String, String> synonymConfig = new HashMap<String, String>(); 
		synonymConfig.put(AbstractAnalysisFactory.LUCENE_MATCH_VERSION_PARAM, Version.LUCENE_46.name());
//		SynonymFilterFactory synonym = new SynonymFilterFactory(synonymConfig);
		
		Map<String, String> stopConfig = new HashMap<String, String>(); 
		stopConfig.put(AbstractAnalysisFactory.LUCENE_MATCH_VERSION_PARAM, Version.LUCENE_46.name());
		StopFilterFactory stop = new StopFilterFactory(stopConfig);
		Map<String, String> lowerCaseConfig = new HashMap<String, String>(); 
		lowerCaseConfig.put(AbstractAnalysisFactory.LUCENE_MATCH_VERSION_PARAM, Version.LUCENE_46.name());
		LowerCaseFilterFactory lowerCase = new LowerCaseFilterFactory(lowerCaseConfig);

		TokenFilterFactory[] newtf = new TokenFilterFactory[3];
		newtf[0] = word;
//		newtf[1] = synonym;
		newtf[1] = stop;
		newtf[1] = lowerCase;
		return new TokenizerChain(sentence, newtf);
	}
	
	private String writeCharStream(NamedList<Object> out, Reader input ){
		final int BUFFER_SIZE = 1024;
		char[] buf = new char[BUFFER_SIZE];
		int len = 0;
		StringBuilder sb = new StringBuilder();
		do {
			try {
				len = input.read( buf, 0, BUFFER_SIZE );
			} catch (IOException e) {
				throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
			}
			if( len > 0 ) {
				sb.append(buf, 0, len);
			}
		} while( len == BUFFER_SIZE );
		
		out.add( input.getClass().getName(), sb.toString());
		return sb.toString();
	}	
	
	/**
	 * Analyzes the given TokenStream, collecting the Tokens it produces.
	 *
	 * @param tokenStream TokenStream to analyze
	 *
	 * @return List of tokens produced from the TokenStream
	 */
	private List<AttributeSource> analyzeTokenStream(TokenStream tokenStream) {
		final List<AttributeSource> tokens = new ArrayList<AttributeSource>();
	    final PositionIncrementAttribute posIncrAtt = tokenStream.addAttribute(PositionIncrementAttribute.class);
	    final TokenTrackingAttribute trackerAtt = tokenStream.addAttribute(TokenTrackingAttribute.class);
	    // for backwards compatibility, add all "common" attributes
	    tokenStream.addAttribute(OffsetAttribute.class);
	    tokenStream.addAttribute(TypeAttribute.class);
	    try {
	    	tokenStream.reset();
	    	int position = 0;
	    	while (tokenStream.incrementToken()) {
	    		position += posIncrAtt.getPositionIncrement();
	    		trackerAtt.setActPosition(position);
	    		tokens.add(tokenStream.cloneAttributes());
	    	}
	    	tokenStream.end();
	    } catch (IOException ioe) {
	    	throw new RuntimeException("Error occured while iterating over tokenstream", ioe);
	    } finally {
	    	IOUtils.closeWhileHandlingException(tokenStream);
	    }

	    return tokens;
	}	
	
	  /**
	   * Converts the list of Tokens to a list of NamedLists representing the tokens.
	   *
	   * @param tokenList  Tokens to convert
	   * @param context The analysis context
	   *
	   * @return List of NamedLists containing the relevant information taken from the tokens
	   */
	private List<NamedList<Object>> convertTokensToNamedLists(final List<AttributeSource> tokenList, final FieldType fieldType, Set<BytesRef> termsToMatch) {
		final List<NamedList<Object>> tokensNamedLists = new ArrayList<NamedList<Object>>();
	    final AttributeSource[] tokens = tokenList.toArray(new AttributeSource[tokenList.size()]);
	    
	    // sort the tokens by absoulte position
	    ArrayUtil.timSort(tokens, new Comparator<AttributeSource>() {
	    	@Override
	    	public int compare(AttributeSource a, AttributeSource b) {
	    		return arrayCompare(
	    				a.getAttribute(TokenTrackingAttribute.class).getPositions(),
	    				b.getAttribute(TokenTrackingAttribute.class).getPositions()
	    				);
	    	}
	      
	    	private int arrayCompare(int[] a, int[] b) {
	    		int p = 0;
	    		final int stop = Math.min(a.length, b.length);
	    		while(p < stop) {
	    			int diff = a[p] - b[p];
	    			if (diff != 0) return diff;
	    			p++;
	    		}
	    		// One is a prefix of the other, or, they are equal:
	    		return a.length - b.length;
	    	}
	    });

	    for (int i = 0; i < tokens.length; i++) {
	    	AttributeSource token = tokens[i];
	    	final NamedList<Object> tokenNamedList = new SimpleOrderedMap<Object>();
	    	final TermToBytesRefAttribute termAtt = token.getAttribute(TermToBytesRefAttribute.class);
	    	BytesRef rawBytes = termAtt.getBytesRef();
	    	termAtt.fillBytesRef();
	    	final String text = fieldType.indexedToReadable(rawBytes, new CharsRef(rawBytes.length)).toString();
	    	tokenNamedList.add("text", text);
	    	if (token.hasAttribute(CharTermAttribute.class)) {
	    		final String rawText = token.getAttribute(CharTermAttribute.class).toString();
	    		if (!rawText.equals(text)) {
	    			tokenNamedList.add("raw_text", rawText);
	    		}
	    	}

	    	tokenNamedList.add("raw_bytes", rawBytes.toString());

	    	if (termsToMatch.contains(rawBytes)) {
	    		tokenNamedList.add("match", true);
	    	}

	    	token.reflectWith(new AttributeReflector() {
	    		@Override
	    		public void reflect(Class<? extends Attribute> attClass, String key, Object value) {
	    			// leave out position and bytes term
	    			if (TermToBytesRefAttribute.class.isAssignableFrom(attClass))
	    				return;
	    			if (CharTermAttribute.class.isAssignableFrom(attClass))
	    				return;
	    			if (PositionIncrementAttribute.class.isAssignableFrom(attClass))
	    				return;
	          
	    			String k = attClass.getName() + '#' + key;
	          
	    			//map keys for "standard attributes":
	    			if (ATTRIBUTE_MAPPING.containsKey(k)) {
	    				k = ATTRIBUTE_MAPPING.get(k);
	    			}
	          
	    			if (value instanceof BytesRef) {
	    				final BytesRef p = (BytesRef) value;
	    				value = p.toString();
	    			}

	    			tokenNamedList.add(k, value);
	    		}
	    	});

	    	tokensNamedLists.add(tokenNamedList);
	    }

	    return tokensNamedLists;
	}
	
	  /**
	   * TokenStream that iterates over a list of pre-existing Tokens
	   * @lucene.internal
	   */
	protected final static class ListBasedTokenStream extends TokenStream {
		private final List<AttributeSource> tokens;
	    private Iterator<AttributeSource> tokenIterator;

	    /**
	     * Creates a new ListBasedTokenStream which uses the given tokens as its token source.
	     *
	     * @param tokens Source of tokens to be used
	     */
	    ListBasedTokenStream(List<AttributeSource> tokens) {
	    	this.tokens = tokens;
	    	tokenIterator = tokens.iterator();
	    }

	    @Override
	    public boolean incrementToken() {
	    	if (tokenIterator.hasNext()) {
	    		clearAttributes();
	    		AttributeSource next = tokenIterator.next();
	    		Iterator<Class<? extends Attribute>> atts = next.getAttributeClassesIterator();
	    		while (atts.hasNext()) // make sure all att impls in the token exist here
	    			addAttribute(atts.next());
	    		next.copyTo(this);
	    		return true;
	    	} else {
	    		return false;
	    	}
	    }

	    @Override
	    public void reset() throws IOException {
	    	super.reset();
	    	tokenIterator = tokens.iterator();
	    }
	}
	
	public static void main(String[] args) {
		AnalysisService s = new AnalysisService();
		System.err.println(s.getAnalyzeValue("我是中国人"));
//		System.err.println(s.getAnalyzedValue("国人西服"));
//		System.err.println(s.getAnalyzedValue("支付宝"));
//		System.err.println(s.getAnalyzedValue("支付宝HD"));
	}
	
}
