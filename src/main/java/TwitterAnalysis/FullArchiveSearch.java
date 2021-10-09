package TwitterAnalysis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/*
 * Sample code to demonstrate the use of the Full archive search endpoint
 * */
public class FullArchiveSearch {

    // To set your enviornment variables in your terminal run the following line:
    // export 'BEARER_TOKEN'='<your_bearer_token>'

    public static void main(String args[]) throws IOException, URISyntaxException {
        String bearerToken = "AAAAAAAAAAAAAAAAAAAAAB0BUgEAAAAAFP%2FBMfx6jCiTJE%2FEvHgt9C9Jnm4%3DeIXDOLSo5gH9gIls4YWpF25sW52LxqDpxvk29lMN6kZYtkkzPD";
        if (null != bearerToken) {
            //Replace the search term with a term of your choice
            String query = "(asian hate crime OR racism OR BLM) -is:retweet -is:reply";
            String expansion = "author_id";
            String placeF = "contained_within,country,country_code,full_name,geo,id,name,place_type";
            String tweetF = "geo";
            String userF = "location";
            String response = search(query, "100",expansion, placeF,tweetF, userF, bearerToken);
            System.out.print(response);
        } else {
            System.out.println("There was a problem getting your bearer token. Please make sure you set the BEARER_TOKEN environment variable");
        }
    }

    /*
     * This method calls the full-archive search endpoint with a the search term passed to it as a query parameter
     * */
    public static String search(String searchString, String maxResult,
                                 String expansions, String placeFields,
                                 String tweetFields, String userFields,
                                 String bearerToken) throws IOException, URISyntaxException {
        String searchResponse = null;

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/recent");
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("query", searchString));
        queryParameters.add(new BasicNameValuePair("max_results", maxResult));
        queryParameters.add(new BasicNameValuePair("expansions", expansions));
        queryParameters.add(new BasicNameValuePair("place.fields", placeFields));
        queryParameters.add(new BasicNameValuePair("tweet.fields", tweetFields));
        queryParameters.add(new BasicNameValuePair("user.fields", userFields));

        uriBuilder.addParameters(queryParameters);

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            searchResponse = EntityUtils.toString(entity, "UTF-8");
        }
        return searchResponse;
    }

}
