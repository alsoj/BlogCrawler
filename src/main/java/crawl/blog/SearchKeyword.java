package crawl.blog;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SearchKeyword {
    final static String pcSearchUrl = "https://search.naver.com/search.naver?query=";
    final static String mobileSearchUrl = "https://m.search.naver.com/search.naver?query=";
    final static String searchKeyword = "컴퓨터 추천";

    public static void main(String[] args) {

        // PC 섹션 순서 조회
        String pcSectionOrder = getSectionOrder("PC");
        System.out.println(pcSectionOrder);

        // Mobile 섹션 순서 조회
        String mobileSectionOrder = getSectionOrder("MOBILE");
        System.out.println(mobileSectionOrder);

    }

    private static String getSectionOrder(String device) {
        String result = "";

        String searchUrl = device.equals("PC") ? pcSearchUrl : mobileSearchUrl;
        Connection conn = Jsoup.connect(searchUrl+searchKeyword);

        try {
            Document document = conn.get();

//            System.out.println(document);
            Elements h2Tags = document.getElementsByTag("h2");

            for (Element tag : h2Tags) {
//                tag.ownText()
                result += tag.ownText() + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = "error";
        } finally {
            return result;
        }
    }
}
