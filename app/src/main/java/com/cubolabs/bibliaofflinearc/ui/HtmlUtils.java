package com.cubolabs.bibliaofflinearc.ui;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class HtmlUtils {
    public static String SmallCapitalize(String htmlText) {
        Document doc = Jsoup.parse(htmlText);
        Elements smallcaps = doc.select("[style~=small-caps]");
        for(Element el: smallcaps) {
            String html = String.format("%s<small>%s</small>",
                    el.text().charAt(0),
                    el.text().substring(1).toUpperCase());
            Element span = new Element(Tag.valueOf("span"), "");
            span.html(html);

            el.replaceWith(span);
        }
        return doc.body().html();
    }
}
