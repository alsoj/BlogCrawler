package crawl.blog;

public enum Config {
    PC_SEARCH_URL("https://search.naver.com/search.naver?query="),
    MOBILE_SEARCH_URL("https://m.search.naver.com/search.naver?query="),
    VIEW_SEARCH_URL("https://s.search.naver.com/p/review/search.naver?rev=44&where=view&api_type=11&start=%s&query=%s&nso=&main_q=&mode=normal&q_material=&ac=0&aq=0&spq=0&st_coll=&topic_r_cat=&nx_search_query=&nx_and_query=&nx_sub_query=&prank=96&sm=tab_jum&ssc=tab.view.view&ngn_country=KR&lgl_rcode=02465101&fgn_region=&fgn_city=&lgl_lat=37.3279495&lgl_long=127.0949293&abt=&_callback=viewMoreContents"),
    API_CLIENT_ID("클라이언트 ID"),  //애플리케이션 클라이언트 아이디
    API_CLIENT_SECRET("클라이언트 SECRET"),  //애플리케이션 클라이언트 시크릿
    TARGET_BLOG_URL("https://blog.naver.com/블로그주소"),
    SEARCH_KEYWORD("키워드");

    final private String value;

    public String getValue() {
        return value;
    }
    Config(String value){
        this.value = value;
    }
}
