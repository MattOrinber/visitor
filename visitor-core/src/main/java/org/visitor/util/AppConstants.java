package org.visitor.util;

import java.util.regex.Pattern;

/**
 * @author mengw
 * @version 9/1/14.
 */
public class AppConstants {


    public static final String  COMMA_SEPARATOR                    = ",";

    public static final Pattern COMMA_SPLIT_PATTERN                = Pattern.compile("\\s*[,]+\\s*");

    public static final String  SEMICOLON_SEPARATOR                = ";";

    public static final Pattern SEMICOLON_SPLIT_PATTERN            = Pattern.compile("\\s*[;]+\\s*");
}
