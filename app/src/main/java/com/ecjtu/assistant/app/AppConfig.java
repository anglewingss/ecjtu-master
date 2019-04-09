package com.ecjtu.assistant.app;

/**
 * Created by Bruce on 2016/10/11.
 */

public class AppConfig {

    //新闻URL：
    public static String NEWS_BASE_URL = "http://news.gdou.edu.cn";
    /**
     * 综合要闻
     */
    public static String NEWS_ZONGHE = String.format("%s%s", NEWS_BASE_URL, "/list.php?catid=18");
    /**
     * 科教动态
     */
    public static String NEWS_KEJIAO = String.format("%s%s", NEWS_BASE_URL, "/list.php?catid=19");
    /**
     * 校园快讯
     */
    public static String NEWS_XIAOYUAN = String.format("%s%s", NEWS_BASE_URL, "/list.php?catid=53");
    /**
     * 公示公告
     */
    public static String NEWS_GONGGAO = String.format("%s%s", NEWS_BASE_URL, "/list.php?catid=63");

    /**
     * 移动版图书馆首页
     */
    public static String LIBRARY_HOME = "http://210.38.138.7:8080/sms/opac/search/showSearch.action?xc=6";

    /**
     * 图书馆搜索URL part1
     */
    public static String SEARCH_URL_PART1 = "http://210.38.138.7:8080/search?kw=";

    /**
     * 图书馆搜索URL part2
     */
    public static String SEARCH_URL_PART2 = "&searchtype=&marcformat=&page=";

    /**
     * 图书馆搜索URL part3
     */
    public static String SEARCH_URL_PART3 = "&xc=6";

    /**
     * 图书馆登录URL(Web版)
     */
    public static String LOGIN_LIBRARY_URL = "http://210.38.138.8:8080/opac/reader/doLogin";
    public static String LIBRARY_RENEW_URL = "http://210.38.138.8:8080/opac/loan/doRenew";
    public static String LIBRARY_FURL = "/opac/loan/renewList";
    public static String LIBRARY_USER_CENTER_URL = "http://210.38.138.8:8080/opac/reader/space";
    public static String USER_ARGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";

    /**
     * 正方系统URL
     */
    public static String HOST = "210.38.137.126:8016";
    public static String REFERER = "http://210.38.137.126:8016/";
    public static String URL_CODE = "http://210.38.137.126:8016/CheckCode.aspx";
    public static String URL_LOGIN = "http://210.38.137.126:8016/default2.aspx";
    public static String VIEWSTATELONG = "dDwtNzAwMjQ5NzExO3Q8cDxsPFNvcnRFeHByZXM7c2ZkY2JrO2RnMztkeWJ5c2NqO1NvcnREaXJlO3hoO3N0cl90YWJfYmpnO2NqY3hfbHNiO3p4Y2pjeHhzOz47bDxrY21jO1xlO2JqZzsxO2FzYzsyMDEzMTE2NzExMDM7emZfY3hjanRqXzIwMTMxMTY3MTEwMzs7MDs+PjtsPGk8MT47PjtsPHQ8O2w8aTw0PjtpPDEwPjtpPDE5PjtpPDI0PjtpPDMyPjtpPDM0PjtpPDM2PjtpPDM4PjtpPDQwPjtpPDQyPjtpPDQ0PjtpPDQ2PjtpPDQ4PjtpPDUyPjtpPDU0PjtpPDU2Pjs+O2w8dDx0PHA8cDxsPERhdGFUZXh0RmllbGQ7RGF0YVZhbHVlRmllbGQ7PjtsPFhOO1hOOz4+Oz47dDxpPDU+O0A8XGU7MjAxNi0yMDE3OzIwMTUtMjAxNjsyMDE0LTIwMTU7MjAxMy0yMDE0Oz47QDxcZTsyMDE2LTIwMTc7MjAxNS0yMDE2OzIwMTQtMjAxNTsyMDEzLTIwMTQ7Pj47Pjs7Pjt0PHQ8cDxwPGw8RGF0YVRleHRGaWVsZDtEYXRhVmFsdWVGaWVsZDs+O2w8a2N4em1jO2tjeHpkbTs+Pjs+O3Q8aTw1PjtAPOW/heS/rjvpmZDpgIk75Lu76YCJO+S4k+S4muS7u+mAiTtcZTs+O0A8MDE7MDI7MDM7MDQ7XGU7Pj47Pjs7Pjt0PHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Pjt0PHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Pjt0PHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPFxlOz4+Oz47Oz47dDxwPHA8bDxUZXh0O1Zpc2libGU7PjtsPOWtpuWPt++8mjIwMTMxMTY3MTEwMztvPHQ+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0O1Zpc2libGU7PjtsPOWnk+WQje+8muWGr+W+t+aYjjtvPHQ+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0O1Zpc2libGU7PjtsPOWtpumZou+8muaVsOWtpuS4juiuoeeul+acuuWtpumZojtvPHQ+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0O1Zpc2libGU7PjtsPOS4k+S4mu+8mjtvPHQ+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0O1Zpc2libGU7PjtsPOS/oeaBr+euoeeQhuS4juS/oeaBr+ezu+e7nztvPHQ+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzkuJPkuJrmlrnlkJE65L+h5oGv57O757uf5byA5Y+ROz4+Oz47Oz47dDxwPHA8bDxUZXh0O1Zpc2libGU7PjtsPOihjOaUv+ePre+8muS/oeeuoTExMzE7bzx0Pjs+Pjs+Ozs+O3Q8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47dDw7bDxpPDE+O2k8Mz47aTw1PjtpPDc+O2k8OT47aTwxMz47aTwxNT47aTwxNz47aTwyMT47aTwyMz47aTwyND47aTwyNT47aTwyNz47aTwyOT47aTwzMT47aTwzMz47aTwzNT47aTw0Mz47aTw0OT47aTw1Mz47aTw1ND47PjtsPHQ8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+Pjs+Ozs+O3Q8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+PjtwPGw8c3R5bGU7PjtsPERJU1BMQVk6bm9uZTs+Pj47Ozs7Ozs7Ozs7Pjs7Pjt0PDtsPGk8MTM+Oz47bDx0PEAwPDs7Ozs7Ozs7Ozs+Ozs+Oz4+O3Q8cDxwPGw8VGV4dDtWaXNpYmxlOz47bDzoh7Pku4rmnKrpgJrov4for77nqIvmiJDnu6nvvJo7bzx0Pjs+Pjs+Ozs+O3Q8QDA8cDxwPGw8UGFnZUNvdW50O18hSXRlbUNvdW50O18hRGF0YVNvdXJjZUl0ZW1Db3VudDtEYXRhS2V5czs+O2w8aTwxPjtpPDA+O2k8MD47bDw+Oz4+O3A8bDxzdHlsZTs+O2w8RElTUExBWTpibG9jazs+Pj47Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O3A8bDxzdHlsZTs+O2w8RElTUExBWTpub25lOz4+Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O3A8bDxzdHlsZTs+O2w8RElTUExBWTpub25lOz4+Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+PjtwPGw8c3R5bGU7PjtsPERJU1BMQVk6bm9uZTs+Pj47Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPDtAMDw7O0AwPHA8bDxIZWFkZXJUZXh0Oz47bDzliJvmlrDlhoXlrrk7Pj47Ozs7PjtAMDxwPGw8SGVhZGVyVGV4dDs+O2w85Yib5paw5a2m5YiGOz4+Ozs7Oz47QDA8cDxsPEhlYWRlclRleHQ7PjtsPOWIm+aWsOasoeaVsDs+Pjs7Ozs+Ozs7Pjs7Ozs7Ozs7Oz47Oz47dDxwPHA8bDxUZXh0O1Zpc2libGU7PjtsPOacrOS4k+S4muWFsTExN+S6ujtvPGY+Oz4+Oz47Oz47dDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+Oz47Oz47dDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+Oz47Oz47dDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDxISFhZOz4+Oz47Oz47dDxwPHA8bDxJbWFnZVVybDs+O2w8Li9leGNlbC8yMDEzMTE2NzExMDMuanBnOz4+Oz47Oz47Pj47dDw7bDxpPDM+Oz47bDx0PEAwPDs7Ozs7Ozs7Ozs+Ozs+Oz4+Oz4+Oz4+Oz7ADX+QyFxtnGqTQHf5EyI2w4vn3A==";
}
