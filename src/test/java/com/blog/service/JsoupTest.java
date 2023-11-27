package com.blog.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

public class JsoupTest {

    @Test
    void jsoup_test() {
        String html="<p><img src=\"https://drive.google.com/uc?export=download&amp;id=1-4fHai7Bs8V66J9n53OXQOheHmClYm8l\" style=\"width: 742px;\"></p><p><br></p><p><br></p>";
        String img="";
        Document doc = Jsoup.parse(html);
        Elements els = doc.select("img");
        if (els.size() == 0) {
            //임시 사진 제공
        } else {
            Element element = els.get(0);
            img += element.attr("src");
            System.out.println(img);
        }


    }

}
