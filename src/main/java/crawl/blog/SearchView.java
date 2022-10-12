package crawl.blog;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SearchView {

    final static String veiwSearchUrl = Config.VIEW_SEARCH_URL.getValue();
    final static String targetUrl = Config.TARGET_BLOG_URL.getValue();

    public static Map getBlogOrderInView(String keyword) {

        int order = 1;
        HashMap<String, Object> result_map = new HashMap<String, Object>();
        int result = -1;
        String post_title = "";
        String post_url = "";
        result_map.put("post_order", result);
        result_map.put("post_title", post_title);
        result_map.put("post_url", post_url);

        while(order < 101) {
            String url = String.format(veiwSearchUrl, order, keyword);
            Connection conn = Jsoup.connect(url);

            try {
                Document document = conn.get();
                Elements lis = document.getElementsByTag("li");

                if(lis.isEmpty()) {
                    return result_map;
                } else {
                    for (Element li : lis) {
                        Elements atags = li.getElementsByTag("a");
                        for (Element a : atags) {
                            String href = a.attr("href");
                            String class_name = a.attr("class");
                            if(href.contains(targetUrl) && class_name.contains("api_txt_lines")) {
                                post_url= href.replaceAll("\"","").replaceAll("\\\\","");
                                post_title = a.text();
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

        result_map.put("post_order", result);
        result_map.put("post_title", post_title);
        result_map.put("post_url", post_url);
        return result_map;
    }
}
