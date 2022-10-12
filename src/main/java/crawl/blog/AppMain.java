package crawl.blog;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public class AppMain {

    final static String searchKeyword = Config.SEARCH_KEYWORD.getValue();

    public static void main(String[] args) throws UnsupportedEncodingException {
/*
        // PC 섹션 순서 조회
        System.out.printf("************** PC 섹션 조회 순서 (검색어 : %s) **************", searchKeyword);
        String pcSectionOrder = SearchKeyword.getSectionOrder("PC");
        System.out.println(pcSectionOrder);
        System.out.println("");

        // Mobile 섹션 순서 조회
        System.out.printf("************** Mobile 섹션 조회 순서 (검색어 : %s) **************", searchKeyword);
        String mobileSectionOrder = SearchKeyword.getSectionOrder("MOBILE");
        System.out.println(mobileSectionOrder);
        System.out.println("");

        // 일간 검색량 추이 조회
        System.out.printf("************** 최근 1주, 일간 검색량 조회 (검색어 : %s) **************\n", searchKeyword);
        ArrayList dailyList = SearchStatistics.getSearchStatistics("daily");
        for (Object daily : dailyList) {
            Map map = (Map) daily;
            System.out.println(map.get("period") +  " | " + map.get("ratio"));
        }
        System.out.println("");

        // 주간 검색량 추이 조회
        System.out.printf("************** 최근 1개월, 주간 검색량 조회 (검색어 : %s) **************\n", searchKeyword);
        ArrayList weeklyList = SearchStatistics.getSearchStatistics("weekly");
        for (Object daily : weeklyList) {
            Map map = (Map) daily;
            System.out.println(map.get("period") +  " | " + map.get("ratio"));
        }
        System.out.println("");

        // 월간 검색량 추이 조회
        System.out.printf("************** 최근 1년, 월간 검색량 조회 (검색어 : %s) **************\n", searchKeyword);
        ArrayList monthlyList = SearchStatistics.getSearchStatistics("monthly");
        for (Object daily : monthlyList) {
            Map map = (Map) daily;
            System.out.println(map.get("period") +  " | " + map.get("ratio"));
        }
        System.out.println("");

        // 문서 수 조회
        System.out.printf("************** 문서 수 조회 (검색어 : %s) **************\n", searchKeyword);
        int documentCnt = SearchDocument.getDocumentCnt(searchKeyword);
        System.out.println("문서 수 합계 : " + documentCnt);
        System.out.println("");
*/
        // 블로그 노출 순위
        System.out.printf("************** 블로그 노출 순위 (검색어 : %s) **************\n", searchKeyword);
        Map result_map = SearchView.getBlogOrderInView(searchKeyword);
        System.out.println("블로그 노출 순위 : " + result_map.get("post_order"));
        System.out.println("포스팅 링크 : " + result_map.get("post_url"));
        System.out.println("포스팅 제목 : " + result_map.get("post_title"));

/*
        // 월간 검색량 조회(광고 API)
        System.out.printf("************** 월간 검색량 통계 조회 by 광고 API (검색어 : %s) **************\n", searchKeyword);
        Map resultMap = SearchAds.getSearchStatisticsByAd();
        System.out.println("월간 PC 검색량 : " + resultMap.get("monthlyPcQcCnt"));
        System.out.println("월간 Mobile 검색량 : " + resultMap.get("monthlyMobileQcCnt"));
        System.out.println("월간 PC 클릭량 : " + resultMap.get("monthlyAvePcClkCnt"));
        System.out.println("월간 Mobile 클릭량 : " + resultMap.get("monthlyAveMobileClkCnt"));
        System.out.println("월간 PC 클릭률 : " + resultMap.get("monthlyAvePcCtr"));
        System.out.println("월간 Mobile 클릭률 : " + resultMap.get("monthlyAveMobileCtr"));


        String request_url = "https://blog.naver.com/profile/history.naver?blogId=%s";
        String blog_id = "cou_leehyun";

        String url = String.format(request_url, blog_id);
        System.out.println(url);
        Connection conn = Jsoup.connect(url);

        try {
            Document document = conn.get();

            System.out.println(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }
}
