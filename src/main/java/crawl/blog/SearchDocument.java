package crawl.blog;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SearchDocument {

    private static String clientId = Config.API_CLIENT_ID.getValue();
    private static String clientSecret = Config.API_CLIENT_SECRET.getValue();

    public static int getDocumentCnt(String keyword) {
        String text = null;
        try {
            text = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }

        String blogApiURL = "https://openapi.naver.com/v1/search/blog?query=" + text;  // 블로그
        String cafeApiURL = "https://openapi.naver.com/v1/search/cafearticle?query=" + text;  // 카페
        String webApiURL = "https://openapi.naver.com/v1/search/webkr?query=" + text;  // 웹
        String newsApiURL = "https://openapi.naver.com/v1/search/news?query=" + text;  // 뉴스

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String blogResponseBody = get(blogApiURL,requestHeaders);
        String cafeResponseBody = get(cafeApiURL,requestHeaders);
        String webResponseBody = get(webApiURL,requestHeaders);
        String newsResponseBody = get(newsApiURL,requestHeaders);

        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        Map blog = gson.fromJson(blogResponseBody, Map.class);
        Map cafe = gson.fromJson(cafeResponseBody, Map.class);
        Map web = gson.fromJson(webResponseBody, Map.class);
        Map news = gson.fromJson(newsResponseBody, Map.class);

        double blogCnt = (Double) blog.get("total");
        double cafeCnt = (Double) cafe.get("total");
        double webCnt = (Double) web.get("total");
        double newsCnt = (Double) news.get("total");

        int blogCntInt = (int) blogCnt;
        int cafeCntInt = (int) cafeCnt;
        int webCntInt = (int) webCnt;
        int newsCntInt = (int) newsCnt;

//        System.out.printf("************ 문서 수 (검색어 : %s) ************\n", keyword);
        System.out.println("블로그 : " + blogCntInt);
        System.out.println("카페 : " + cafeCntInt);
        System.out.println("웹문서 : " + webCntInt);
        System.out.println("뉴스 : " + newsCntInt);
        return blogCntInt+cafeCntInt+webCntInt+newsCntInt;
    }

    public static void main(String[] args) {
        String clientId = "wY0wffBXU6pLnxmhwJL1"; //애플리케이션 클라이언트 아이디
        String clientSecret = "Nvw4n_jVq6"; //애플리케이션 클라이언트 시크릿
        String searchKeyword = "그린팩토리";

        String text = null;
        try {
            text = URLEncoder.encode(searchKeyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }

        String blogApiURL = "https://openapi.naver.com/v1/search/blog?query=" + text;  // 블로그
        String cafeApiURL = "https://openapi.naver.com/v1/search/cafearticle?query=" + text;  // 카페
        String webApiURL = "https://openapi.naver.com/v1/search/webkr?query=" + text;  // 웹
        String newsApiURL = "https://openapi.naver.com/v1/search/news?query=" + text;  // 뉴스

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String blogResponseBody = get(blogApiURL,requestHeaders);
        String cafeResponseBody = get(cafeApiURL,requestHeaders);
        String webResponseBody = get(webApiURL,requestHeaders);
        String newsResponseBody = get(newsApiURL,requestHeaders);

        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        Map blog = gson.fromJson(blogResponseBody, Map.class);
        Map cafe = gson.fromJson(cafeResponseBody, Map.class);
        Map web = gson.fromJson(webResponseBody, Map.class);
        Map news = gson.fromJson(newsResponseBody, Map.class);
        Double blogCnt = (Double) blog.get("total");
        Double cafeCnt = (Double) cafe.get("total");
        Double webCnt = (Double) web.get("total");
        Double newsCnt = (Double) news.get("total");

        System.out.printf("************ 문서 수 (검색어 : %s) ************\n", searchKeyword);
        System.out.println(blogCnt+cafeCnt+webCnt+newsCnt);
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