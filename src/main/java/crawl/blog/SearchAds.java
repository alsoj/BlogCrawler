package crawl.blog;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import java.security.SignatureException;

public class SearchAds {

    private static String API_KEY = Config.AD_API_KEY.getValue();
    private static String SECRET_KEY = Config.AD_API_SECRET.getValue();
    private static String CUSTOMER_ID = Config.AD_API_CUSTOMER.getValue();
    private static String searchKeyword = Config.SEARCH_KEYWORD.getValue();

    public static Map getSearchStatisticsByAd() throws UnsupportedEncodingException {
        String adApiUrl = "https://api.naver.com/keywordstool?hintKeywords=%s&showDetail=1";
        String url = String.format(adApiUrl, URLEncoder.encode(searchKeyword, "UTF-8"));

        long timestamp = System.currentTimeMillis();
        String message = timestamp + ".GET./keywordstool";
        String key = SECRET_KEY;


        String algorithm = "HmacSHA256"; //HmacMD5,HmacSHA1,HmacSHA224,HmacSHA384,HmacSHA512
        String signature = null;
        try {
            signature = Hmac(key, message, algorithm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json; charset=UTF-8");
        requestHeaders.put("X-Timestamp", String.valueOf(timestamp));
        requestHeaders.put("X-API-KEY", API_KEY);
        requestHeaders.put("X-Customer", CUSTOMER_ID);
        requestHeaders.put("X-Signature", signature);

        String adResponseBody = get(url, requestHeaders);

        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        Map result = gson.fromJson(adResponseBody, Map.class);

        ArrayList keywordList = (ArrayList) result.get("keywordList");
        Map resultMap = (Map) keywordList.get(0);

        return resultMap;

    }

    public static void main(String[] args) throws SignatureException, UnsupportedEncodingException {

        String adApiUrl = "https://api.naver.com/keywordstool?hintKeywords=%s&showDetail=1";
        String url = String.format(adApiUrl, URLEncoder.encode(searchKeyword, "UTF-8"));

        long timestamp = System.currentTimeMillis();
        String message = timestamp + ".GET./keywordstool";
        String key = SECRET_KEY;


        String algorithm = "HmacSHA256"; //HmacMD5,HmacSHA1,HmacSHA224,HmacSHA384,HmacSHA512
        String signature = null;
        try {
            signature = Hmac(key, message, algorithm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json; charset=UTF-8");
        requestHeaders.put("X-Timestamp", String.valueOf(timestamp));
        requestHeaders.put("X-API-KEY", API_KEY);
        requestHeaders.put("X-Customer", CUSTOMER_ID);
        requestHeaders.put("X-Signature", signature);

        String adResponseBody = get(url, requestHeaders);

        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        Map result = gson.fromJson(adResponseBody, Map.class);

        ArrayList keywordList = (ArrayList) result.get("keywordList");
        Map resultMap = (Map) keywordList.get(0);
        System.out.println("월간 PC 검색량 : " + resultMap.get("monthlyPcQcCnt"));
        System.out.println("월간 Mobile 검색량 : " + resultMap.get("monthlyMobileQcCnt"));
        System.out.println("월간 PC 클릭량 : " + resultMap.get("monthlyAvePcClkCnt"));
        System.out.println("월간 Mobile 클릭량 : " + resultMap.get("monthlyAveMobileClkCnt"));
        System.out.println("월간 PC 클릭률 : " + resultMap.get("monthlyAvePcCtr"));
        System.out.println("월간 Mobile 클릭률 : " + resultMap.get("monthlyAveMobileCtr"));
    }


    public static String Hmac(String key, String message, String algorithm) throws Exception {
        Mac hasher = Mac.getInstance(algorithm);
        hasher.init(new SecretKeySpec(key.getBytes("utf-8"), algorithm));
        byte[] hash = hasher.doFinal(message.getBytes());
        return Base64.encodeBase64String(hash);
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }
}

