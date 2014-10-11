/**
 * 
 */
package org.visitor.appportal.service;

import java.util.Arrays;
import java.util.List;

import com.ibm.icu.text.Transliterator;


/**
 * @author mengw
 *
 */
public class ICUTransformUtils {
	static Transliterator quanpin = Transliterator.getInstance("Han-Latin;NFD;[[:NonspacingMark:][:Space:]] Remove");		
	static Transliterator jianpin = Transliterator.createFromRules(
		    "Han-Latin;",
		    ":: Han-Latin;[[:any:]-[[:space:][\uFFFF]]] { [[:any:]-[:white_space:]] >;::Null;[[:NonspacingMark:][:Space:]]>;",
		    Transliterator.FORWARD);
	static Transliterator jianpinFirstQuanpin = Transliterator.createFromRules(null, 
			":: Han-Latin/Names;[[:space:]][bpmfdtnlgkhjqxzcsryw] { [[:any:]-[:white_space:]] >;::NFD;[[:NonspacingMark:][:Space:]]>;",
			Transliterator.FORWARD);

	/**
	 * 
	 */
	public ICUTransformUtils() {
		// TODO Auto-generated constructor stub
	}

	public static List<String> transliterate(String value) {
		return Arrays.asList(quanpin.transliterate(value), jianpin.transliterate(value));
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.err.println(transliterate("qq中华人民共和国"));
	}

}
