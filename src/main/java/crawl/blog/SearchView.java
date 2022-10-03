package crawl.blog;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SearchView {

    final static String veiwSearchUrl = Config.VIEW_SEARCH_URL.getValue();
    final static String targetUrl = Config.TARGET_BLOG_URL.getValue();

    public static int getBlogOrderInView(String keyword) {

        int order = 1;
        int result = -1;

        while(order < 101) {
            String url = String.format(veiwSearchUrl, order, keyword);
            Connection conn = Jsoup.connect(url);

            try {
                Document document = conn.get();
                Elements lis = document.getElementsByTag("li");

                if(lis.isEmpty()) {
                    return result;
                } else {
                    for (Element li : lis) {
                        Elements atags = li.getElementsByTag("a");
                        for (Element a : atags) {
                            String href = a.attr("href");
                            if(href.contains(targetUrl)) {
                                result = order;
                            }
                        }
                        order += 1;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = -1;
            }
        }

        return result;
    }
}
