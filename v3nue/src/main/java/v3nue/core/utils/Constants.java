/**
 * 
 */
package v3nue.core.utils;

/**
 * Package paths, regexes, etc..
 * 
 * @author Ngoc Huy
 *
 */
public class Constants {

	public static final String entityPackage = "v3nue.application.model.entities";

	public static final String modelPackage = "v3nue.application.model.models";

	public static final String specificationPackage = "v3nue.application.model.entity.specifications";

	public static final String factoryPackage = "v3nue.application.model.factory";

	public static final String basePackage = "v3nue";

	public static final String IMAGE_FILE_PATH = "D:\\v3nue-upload\\images\\";

	public static final String[] publicEndPoints = { "/oauth/**", "/api/account\\POST", "/api/account/unique\\GET",
			"/api/account/{id:.+}\\GET", "/api/file/image/**\\GET", "/api/booking\\POST", "/api/factor\\GET" };

	public static final String resourceId = "v3nue-base";

	// stolen from stackoverflow below
	public static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

	public static final String BCRYPT_REGEX = "^\\$2[ayb]\\$.{56}$";

	public static final String WHITESPACE_CHARS = "" /* dummy empty string for homogeneity */
			+ "\\u0009" // CHARACTER TABULATION
			+ "\\u000A" // LINE FEED (LF)
			+ "\\u000B" // LINE TABULATION
			+ "\\u000C" // FORM FEED (FF)
			+ "\\u000D" // CARRIAGE RETURN (CR)
			+ "\\u0020" // SPACE
			+ "\\u0085" // NEXT LINE (NEL)4
			+ "\\u00A0" // NO-BREAK SPACE
			+ "\\u1680" // OGHAM SPACE MARK
			+ "\\u180E" // MONGOLIAN VOWEL SEPARATOR
			+ "\\u2000" // EN QUAD
			+ "\\u2001" // EM QUAD
			+ "\\u2002" // EN SPACE
			+ "\\u2003" // EM SPACE
			+ "\\u2004" // THREE-PER-EM SPACE
			+ "\\u2005" // FOUR-PER-EM SPACE
			+ "\\u2006" // SIX-PER-EM SPACE
			+ "\\u2007" // FIGURE SPACE
			+ "\\u2008" // PUNCTUATION SPACE
			+ "\\u2009" // THIN SPACE
			+ "\\u200A" // HAIR SPACE
			+ "\\u2028" // LINE SEPARATOR
			+ "\\u2029" // PARAGRAPH SEPARATOR
			+ "\\u202F" // NARROW NO-BREAK SPACE
			+ "\\u205F" // MEDIUM MATHEMATICAL SPACE
			+ "\\u3000"; // IDEOGRAPHIC SPACE

}
