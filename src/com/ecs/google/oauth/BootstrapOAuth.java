package com.ecs.google.oauth;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

public class BootstrapOAuth {

//	private static final String REQUEST_URL = "https://www.google.com/accounts/OAuthGetRequestToken";
//	private static final String ACCESS_URL = "https://www.google.com/accounts/OAuthGetAccessToken";  
//	private static final String AUTHORIZE_URL = "https://www.google.com/latitude/apps/OAuthAuthorizeToken?domain=ecommitconsulting.be&location=all&granularity=best&hd=default";
//	
//	private static final String SCOPE = "https://www.googleapis.com/auth/latitude";
//	private static final String CALLBACK_URL = "";
	
//	private static final REQUEST = "https://www.googleapis.com/latitude/v1/currentLocation";

	private static final String CONSUMER_KEY = "anonymous";
	private static final String CONSUMER_SECRET = "anonymous";

	private static final String REQUEST_URL = "https://www.google.com/accounts/OAuthGetRequestToken";
	private static final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken";
	private static final String ACCESS_URL = "https://www.google.com/accounts/OAuthGetAccessToken";  

	private static final String SCOPE = "https://www.google.com/m8/feeds/";
	private static final String CALLBACK_URL = "";	
	
	private static final String REQUEST = "https://www.google.com/m8/feeds/contacts/default/full";
	
	
	private OAuthProvider provider;
	private OAuthConsumer consumer;

	public static void main(String[] args) throws Exception {
		System.setProperty("debug", "true");
		new BootstrapOAuth().initialize();
	}

	public void initialize() throws Exception {
		Scanner scanner = new Scanner(System.in);
		
		String secret = null;
		String token = null;

		this.consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY,
				CONSUMER_SECRET);
		this.provider = new CommonsHttpOAuthProvider(REQUEST_URL
				+ "?scope=" + URLEncoder.encode(SCOPE, "utf-8"),
				ACCESS_URL, AUTHORIZE_URL);

		
		String url = provider.retrieveRequestToken(consumer,CALLBACK_URL);
		System.out.println("1. Copy paste the following url in your browser : ");
		System.out.println(url);
		System.out.println("2. Grant access ");
		System.out.println("3. Copy paste the  " + OAuth.OAUTH_VERIFIER + "parameter here :");
		String oauth_verifier = scanner.nextLine();

		provider.retrieveAccessToken(consumer, URLDecoder.decode(oauth_verifier,"UTF-8"));

		token = consumer.getToken();
		secret = consumer.getTokenSecret();

		consumer.setTokenWithSecret(token, secret);
		
		System.out.println(doGet(REQUEST, consumer));

	}
	
	private String doGet(String url,OAuthConsumer consumer) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
    	HttpGet request = new HttpGet(url);
    	System.out.println("Using URL : " + url );
    	consumer.sign(request);
    	HttpResponse response = httpclient.execute(request);
    	System.out.println("Statusline : " + response.getStatusLine());
    	InputStream data = response.getEntity().getContent();
    	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(data));
        String responeLine;
        StringBuilder responseBuilder = new StringBuilder();
        while ((responeLine = bufferedReader.readLine()) != null) {
        	responseBuilder.append(responeLine);
        }
        return responseBuilder.toString();
	}	
}
