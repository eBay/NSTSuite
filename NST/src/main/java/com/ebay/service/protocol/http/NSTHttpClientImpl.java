package com.ebay.service.protocol.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.validation.constraints.NotNull;

import com.ebay.nst.NstRequestType;

public class NSTHttpClientImpl implements NSTHttpClient<NSTHttpRequest, NSTHttpResponse> {

	@Override
	public NSTHttpResponse sendRequest(NSTHttpRequest request) {
		return sendRequest(request, StandardCharsets.UTF_8);
	}

	@SuppressWarnings("fallthrough")
	@Override
	public NSTHttpResponse sendRequest(NSTHttpRequest request, Charset readResponseCharSet) {
		
		Objects.requireNonNull(request, "Request MUST NOT be null.");

		URL url = request.getUrl();

		if (request.getTrustAllCerts()) {
			try {
				configureTrustAllCerts();
			} catch (KeyManagementException | NoSuchAlgorithmException e) {
				throw new RuntimeException("Exception occurred while configuring trust all certs.", e);
			}
		}

		// -------------------------------
		// Prepare and then send the call.

		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (Exception e) {
			throw new RuntimeException("Exception occurred while opening connection.", e);
		}

		Map<String, String> headers = request.getHeaders();
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				connection.addRequestProperty(entry.getKey(), entry.getValue());
			}
		}

		connection.setConnectTimeout(request.getTimeoutInMilliseconds());

		NstRequestType requestType = request.getRequestType();
		
		try {
			connection.setRequestMethod(requestType.name());
		} catch (Exception e) {
			throw new RuntimeException("Exception occurred setting request method.", e);
		}

		String payload = request.getPayload();

		switch (requestType) {
		case PUT:
		case POST:
		case DELETE:
			if (payload != null) {
				connection.setDoOutput(true);
				
				try {
					OutputStream os = connection.getOutputStream();
					byte[] input = payload.getBytes(readResponseCharSet);
					os.write(input, 0, input.length);
				} catch (Exception e) {
					throw new RuntimeException(String.format("Exception occurred getting %s data from the connection.", requestType.name()), e);
				}
				break;
			}
			// Otherwise, fall through to default.
		default:
			try {
				connection.connect();
			} catch (Exception e) {
				throw new RuntimeException(String.format("Exception occurred connecting for %s data.", requestType.name()), e);
			}
			break;
		}

		NSTHttpResponse response;
		try {
			response = parseResponse(connection, readResponseCharSet);
		} catch (IOException e) {
			throw new RuntimeException("Exception occurred parsing response data.", e);
		}

		if (connection != null) {
			connection.disconnect();
		}

		return response;

	}

	/**
	 * Parse the response body and return it as a String.
	 *
	 * @param connection Connection to parse response from.
	 * @return Parsed response body.
	 * @throws IOException IO Error.
	 */
	protected final NSTHttpResponse parseResponse(HttpURLConnection connection, @NotNull Charset readResponseCharSet) throws IOException {

		NSTHttpResponseImpl response = new NSTHttpResponseImpl();
		
		if (connection == null) {
			return response;
		} if (readResponseCharSet == null) {
			return response;
		}
		
		response.setResponseCode(connection.getResponseCode());

		Map<String, List<String>> responseHeaders = connection.getHeaderFields();
		if (responseHeaders != null) {
			for (Map.Entry<String, List<String>> header : responseHeaders.entrySet()) {
	
				StringBuilder headerValues = new StringBuilder();
				for (String headerValue : header.getValue()) {
					if (headerValues.length() > 0) {
						headerValues.append(" ");
					}
					headerValues.append(headerValue);
				}
				response.addHeader(header.getKey(), headerValues.toString());
			}
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), readResponseCharSet));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		response.setPayload(content.toString());

		return response;
	}

	/**
	 * Allow unsigned/invalid certificates to be ignored. Useful when testing with
	 * internal signing authorities.
	 * 
	 * @throws NoSuchAlgorithmException Pass through.
	 * @throws KeyManagementException   Pass through.
	 */
	private void configureTrustAllCerts() throws NoSuchAlgorithmException, KeyManagementException {

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} };

		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}
}
