package crawl.blog;

import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Map;

public class SearchBlog {

    private static final String target_url = "https://blog.naver.com/PostList.naver?blogId=%s";
    private static final String target_blog = "alsoj";
    private static final String list_url = "https://blog.naver.com/PostViewBottomTitleListAsync.naver?blogId=%s&logNo=%s&sortDateInMilli=%s&showNextPage=true&showPreviousPage=false";

    public static void main(String[] args) {

        String url = String.format(target_url, target_blog);
        Connection conn = Jsoup.connect(url);

        try {
            Document document = conn.get();
            Element post_function = document.getElementsByClass("blog2_post_function").get(0);
            Element atag = post_function.getElementsByTag("a").get(0);
            String[] post_url = atag.attr("title").split("/");
            String log_no = post_url[post_url.length-1];


            String sort_date = "";
            boolean hasNext = true;
            while(hasNext) {
                url = String.format(list_url, target_blog, log_no, sort_date);
                conn = Jsoup.connect(url);
                document = conn.get();
                String response = document.body().ownText();

                Gson gson = new Gson(); // Or use new GsonBuilder().create();
                Map result = gson.fromJson(response, Map.class);
                ArrayList post_list = (ArrayList) result.get("postList");

                for(Object post : post_list) {
                    Map post_map = (Map) post;
                    String title = (String) post_map.get("filteredEncodedTitle");
                    System.out.println(URLDecoder.decode(title, "UTF-8"));
                }

                hasNext = (boolean) result.get("hasNextPage");
                if(hasNext){
                    Double org_next_log_no = (Double) result.get("nextIndexLogNo");
                    BigDecimal next_log_no = new BigDecimal(org_next_log_no);
                    log_no = next_log_no.toPlainString();

                    Double org_sort_date = (Double) result.get("nextIndexSortDate");
                    BigDecimal next_sort_date = new BigDecimal(org_sort_date);
                    sort_date = next_sort_date.toPlainString();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
