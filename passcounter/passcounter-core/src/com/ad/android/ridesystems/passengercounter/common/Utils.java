package com.ad.android.ridesystems.passengercounter.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.dom4j.Element;
import org.dom4j.Node;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;

/**
 * Class implements basic helper methods.
 * @author Victor Melnik 
 *
 */
public class Utils {

	/**
	 * Cuts all namespaces (for xPath querying)
	 * @param xml XML 
	 * @return XML without namespaces
	 */
	public static String cleanNamespaces(String xml) {		
		xml = xml.replaceAll("xmlns(:[\\w])?=\\\"[^\"]*\\\"", ""); //xmlns(:[\w])?=\"[^\"]*\"
		xml = xml.replaceAll("<[a-z]{0,2}:","<");
		xml = xml.replaceAll("</[a-z]{0,2}:","</");
		return xml;
	}
	
	/**
	 * Get dom4j element string value
	 * @param el
	 * @param name element name
	 * @return string val
	 */
	public static String getElementStringValue(Element el, String name) {
		Node node = el.selectSingleNode(name);	
		return node != null ? node.getText().trim() : ""; 
	}
	
	/**
	 * 
	 * @param el
	 * @param name
	 * @return
	 */
	public static Integer getElementIntValue(Element el, String name) {
		String val = getElementStringValue(el, name);
		Integer ival = null;
		try {
			ival = Integer.parseInt(val);
		} catch (NumberFormatException e) { 
			ival = 0;
		}
		return ival; 
	}
	
	/**
	 * 
	 * @param el
	 * @param name
	 * @return
	 */
	public static boolean getElementBoolValue(Element el, String name) {
		String val = getElementStringValue(el, name);		
		return val.equals("true"); 
	}
	/**
	 * Get string in format yyyy-MM-ddTHH:mm:ss
	 * @param date date
	 * @return sting in format
	 */
	public static String getDotnetDate(Date date) {
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
		String timeStr = new SimpleDateFormat("HH:mm:ss").format(date);
		return dateStr + "T" + timeStr;
	}
	
	public static CharSequence setSpanBetweenTokens(CharSequence text, String token, CharacterStyle... cs)
	{
		// Start and end refer to the points where the span will apply
		int tokenLen = token.length();
		int start = text.toString().indexOf(token) + tokenLen;
		int end = text.toString().indexOf(token, start);

		if (start > -1 && end > -1)
		{
			// Copy the spannable string to a mutable spannable string
			SpannableStringBuilder ssb = new SpannableStringBuilder(text);
			for (CharacterStyle c : cs) ssb.setSpan(c, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

			// Delete the tokens before and after the span
			ssb.delete(end, end + tokenLen);
			ssb.delete(start - tokenLen, start);

			text = ssb;
		}
		return text;
	}
}
