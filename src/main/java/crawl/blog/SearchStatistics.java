package crawl.blog;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchStatistics {

    final static String clientId = Config.API_CLIENT_ID.getValue();
    final static String clientSecret = Config.API_CLIENT_SECRET.getValue();
    final static String keyword = Config.SEARCH_KEYWORD.getValue();
    final static String apiUrl = "https://openapi.naver.com/v1/datalab/search";

    public static ArrayList getSearchStatistics(String interval) {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        requestHeaders.put("Content-Type", "application/json");

        LocalDate now = LocalDate.now();
        LocalDate beforeDate = LocalDate.now();
        String timeUnit = "";
        if(interval.equals("daily")){
            beforeDate = now.minusWeeks(1);
            timeUnit = "date";
        } else if (interval.equals("weekly")) {
            beforeDate = now.minusMonths(1);
            timeUnit = "week";
        } else if (interval.equals("monthly")) {
            beforeDate = now.minusYears(1);
            timeUnit = "month";
        } else {
            beforeDate = now;
            timeUnit = "date";
        }

        String requestBody = "{\"startDate\":\"%s\"," +
                "\"endDate\":\"%s\"," +
                "\"timeUnit\":\"%s\"," +
                "\"keywordGroups\":[{\"groupName\":\"%s\"," + "\"keywords\":[\"%s\"]}]}";

        String targetRequestBody = String.format(requestBody, beforeDate, now, timeUnit, keyword, keyword);
        String targetResponseBody = post(apiUrl, requestHeaders, targetRequestBody);

        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        Map result = gson.fromJson(targetResponseBody, Map.class);
        ArrayList resultList = getDataFromJson(result);
        return resultList;
    }

    private static ArrayList getDataFromJson(Map map) {
        ArrayList resultList = (ArrayList) map.get("results");
        Map resultMap = (Map) resultList.get(0);
        ArrayList dataList = (ArrayList) resultMap.get("data");
        return dataList;
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String requestBody) {
        HttpURLConnection con = connect(apiUrl);

        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(requestBody.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect(); // Connection을 재활용할 필요가 없는 프로세스일 경우
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body, StandardCharsets.UTF_8);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}