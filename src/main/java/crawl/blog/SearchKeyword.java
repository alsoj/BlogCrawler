package crawl.blog;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SearchKeyword {
    final static String pcSearchUrl = Config.PC_SEARCH_URL.getValue();
    final static String mobileSearchUrl = Config.MOBILE_SEARCH_URL.getValue();
    final static String searchKeyword = Config.SEARCH_KEYWORD.getValue();

    public static String getSectionOrder(String device) {
        StringBuilder result = new StringBuilder();

        String searchUrl = device.equals("PC") ? pcSearchUrl : mobileSearchUrl;
        Connection conn = Jsoup.connect(searchUrl+searchKeyword);

        try {
            Document document = conn.get();
            Elements h2Tags = document.getElementsByTag("h2");

            for (Element tag : h2Tags) {
                String text = tag.ownText().replaceAll("\n", "");
                if(!text.equals("")) {
                    result.append("\n").append(text);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = new StringBuilder("error");
        } finally {
            return result.toString();
        }
    }
}
