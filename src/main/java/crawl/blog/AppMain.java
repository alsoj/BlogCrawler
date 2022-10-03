package crawl.blog;

import java.util.ArrayList;
import java.util.Map;

public class AppMain {

    final static String searchKeyword = Config.SEARCH_KEYWORD.getValue();

    public static void main(String[] args) {

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

        // 블로그 노출 순위
        System.out.printf("************** 블로그 노출 순위 (검색어 : %s) **************\n", searchKeyword);
        int result = SearchView.getBlogOrderInView(searchKeyword);
        System.out.println(result);
    }
}
