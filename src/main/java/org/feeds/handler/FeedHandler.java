package org.feeds.handler;

import lombok.RequiredArgsConstructor;
import org.feeds.model.Article;
import org.feeds.model.Category;
import org.feeds.model.Feed;
import org.feeds.repository.FeedRepository;
import org.feeds.service.ArticleServiceImpl;
import org.feeds.service.CategoryServiceImpl;
import org.feeds.service.FeedServiceImpl;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FeedHandler extends DefaultHandler {
    private final FeedServiceImpl feedService;
    private final ArticleServiceImpl articleService;
    private final CategoryServiceImpl categoryService;
    private String currentData;
    private Feed currentFeed = new Feed();
    private Article currentArticle;

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) throws SAXException {
        if ("item".equalsIgnoreCase(qName)) {
            currentArticle = new Article();
            currentArticle.setFeed(currentFeed);
        }
        if ("media:content".equalsIgnoreCase(qName)) {
            String url = attributes.getValue("url");
            currentArticle.setImageUrl(url);
        }
        if ("category".equalsIgnoreCase(qName)) {
            String domain = attributes.getValue("domain");
            System.out.println(domain);
            String name = currentData;

            System.out.println(name);
            Category category = new Category();
            category.setLink(domain);
            category.setName(name);
            currentArticle.addCategory(category);
            category.addArticle(currentArticle);
            categoryService.createCategory(category);
        }
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentData = new String(ch, start, length);
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentArticle != null) {
            switch (qName) {
                case "title":
                    currentArticle.setTitle(currentData);
                    break;
                case "guid":
                    currentArticle.setGuid(currentData);
                    break;
                case "link":
                    currentArticle.setLink(currentData);
                    break;
                case "description":
                    currentArticle.setDescription(currentData);
                    break;
                case "pubDate":
                    Date date;
                    SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
                    try {
                        date = format.parse(currentData);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    currentArticle.setPublishedDate(date);
                    break;
                case "author":
                    currentArticle.setAuthor(currentData);
                    break;
                default:
                    currentFeed.addArticle(currentArticle);
                    articleService.createArticle(currentArticle);
            }
        } else if (currentFeed != null) {
            switch (qName) {
                case "title":
                    currentFeed.setTitle(currentData);
                    break;
                case "link":
                    currentFeed.setLink(currentData);
                    break;
                case "description":
                    currentFeed.setDescription(currentData);
                    feedService.createFeed(currentFeed);
                    break;
            }
        }
    }
}
