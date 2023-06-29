package com.freecharge.financial.utility;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Http Request Util class used to fetch the cookie value from the incoming
 * request
 * 
 * @author fcaa17922
 *
 */
public class HttpRequestUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				logger.info(String.format("cookie [name:%s] [value:%s] ", cookie.getName(), cookie.getValue()));
				if (cookieName.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		logger.info(String.format("cookie %s not found in the request", cookieName));
		return null;
	}

	public static byte[] getRequestBodyAsBytes(HttpServletRequest request) throws IOException {
		byte[] requestBodyAsByteArray = IOUtils.toByteArray(request.getInputStream());
		return requestBodyAsByteArray;
	}

	public static byte[] decodeBase64(byte[] encodedArray) {
		return Base64.decodeBase64(encodedArray);
	}

	public static byte[] encodeBase64(byte[] decodedArray) {
		return Base64.encodeBase64(decodedArray);
	}

	public static String decompressGzip(byte[] compressedBytes) throws IOException {
		GZIPInputStream gzipStream = new GZIPInputStream(new ByteArrayInputStream(compressedBytes));
		int bytesRead = 0;
		String decompressedString = "";
		while (bytesRead != -1) {
			byte[] buffer = new byte[1024];
			bytesRead = gzipStream.read(buffer);
			decompressedString += new String(buffer);
		}
		return decompressedString;
	}

	public static byte[] compressGzip(String data) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
		GZIPOutputStream gzip = new GZIPOutputStream(bos);
		gzip.write(data.getBytes());
		gzip.close();
		byte[] compressed = bos.toByteArray();
		bos.close();
		return compressed;
	}
}
