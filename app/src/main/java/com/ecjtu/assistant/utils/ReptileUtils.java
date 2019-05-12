package com.ecjtu.assistant.utils;

import com.ecjtu.assistant.db.BannerDb;
import com.ecjtu.assistant.db.RecordDb;
import com.ecjtu.assistant.model.ScrollModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 爬取学校官网新闻内容
 */
public class ReptileUtils {

    private final String rootUrl = "http://www.qlu.edu.cn";
    private List<RecordDb.Record> recordList = new ArrayList<RecordDb.Record>();
    private List<BannerDb.Record> sceneList = new ArrayList<BannerDb.Record>();
    private List<ScrollModel> scrollModelList = new ArrayList<ScrollModel>();

    /**
     * 获得新闻列表
     * @param
     * @param urlSuffix url后缀
     */
    public  List<RecordDb.Record> getNewsList(String urlSuffix){

        recordList.clear();
        try {
            //String url = "http://www.qlu.edu.cn/38/list.htm";
            String url = rootUrl + "/"  + urlSuffix;
            // get请求获取页面信息
            String bb = doget(url);
            Document doc;
            //用jsoup接收页面信息
            doc = Jsoup.parse(bb);

            Elements news = doc.select("div[id=wp_news_w6]").select("li");
            for (Element result : news) {
                RecordDb.Record record = new RecordDb.Record();
                record.href = rootUrl;
                String link = result.select("a").attr("href");
                if (link.startsWith("http")){
                    record.href = link;
                }else {
                    record.href += link;
                }
                //record.href = rootUrl + result.select("a").attr("href");
                record.title = result.select("a").attr("title");
                record.date = result.select("[class=news_meta]").text();
                record.imgLink = getListImgLInk(record.href);
                recordList.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recordList;
    }


    /**
     * 获得滚动列表
     * @return
     */
    public  List<ScrollModel> getScrollModelList(){
        scrollModelList.clear();
        try {
//            System.err.println(new Date().toString());
            //String url = "http://www.qlu.edu.cn/38/list.htm";
            String url = rootUrl;
            // get请求获取页面信息
            String bb = doget(url);
            Document doc;
            //用jsoup接收页面信息
            doc = Jsoup.parse(bb);

            Elements news = doc.select("div[id=wp_news_w2]").select("script");
            Element element = news.first();
            String aString = element.data();
            String bString = aString.substring(aString.indexOf("["), aString.indexOf(";"));

            scrollModelList = new Gson().fromJson(bString,new TypeToken<List<ScrollModel>>(){}.getType());
            scrollModelList.remove(scrollModelList.size()-1);
            for (ScrollModel scrollModel : scrollModelList) {
                //地址添加根目录
                scrollModel.setSrc(rootUrl + scrollModel.getSrc());
                //连接地址，如果没有根目录，则加上
                if (!scrollModel.getUrl().startsWith("http")){
                    scrollModel.setUrl(rootUrl + scrollModel.getUrl());
                }
            }
//            System.err.println(new Date().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scrollModelList;
    }

    /**
     * 第四个页面的图片
     * @param urlSuffix 连接后缀名
     * @return 返回列表
     */
    public List<BannerDb.Record> getSceneList(String urlSuffix){
        sceneList.clear();
        String url = rootUrl + "/" + urlSuffix;
        String bb = doget(url);
        Document doc = Jsoup.parse(bb);
        Elements news = doc.select("table[class=wp_article_list_table]")
                .select("div[class=albumn_info]").select("span[class=Article_MicroImage]");
        for (Element element : news) {
            BannerDb.Record record = new BannerDb.Record();
            record.title = element.select("a").attr("title");
            record.imgLink = rootUrl + element.select("img").attr("src");
            sceneList.add(record);
        }
        return  sceneList;
    }

    private String getListImgLInk(String infoLink){

        String bb = doget(infoLink);
        Document doc = Jsoup.parse(bb);
        Elements elements = doc.select("div[class=article]");

        Element element = elements.select("img").first();

        String imglink = "";
        if ((null != element && element.attr("src").contains("images/icon")) || null == element){
            imglink = "";
        }
        else{
            imglink = rootUrl + element.attr("src");
        }

        return imglink;
    }


    private  String doget(String path) {
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            // 伪造referer 绕过防盗链设置
            URL url = new URL(path.trim());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134");

            if (200 == conn.getResponseCode()) {
                byte[] buff = new byte[4096];
                int count;
                ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
                InputStream in = conn.getInputStream();

                while ((count = in.read(buff)) != -1) {
                    out.write(buff, 0, count);
                }
                conn.disconnect();
                return out.toString("UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

}
