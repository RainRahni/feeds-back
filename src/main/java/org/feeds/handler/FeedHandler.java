package org.feeds.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.feeds.model.Article;
import org.feeds.model.Category;
import org.feeds.model.Feed;
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
@Slf4j
public class FeedHandler extends DefaultHandler {
    private final FeedServiceImpl feedService;
    private final ArticleServiceImpl articleService;
    private final CategoryServiceImpl categoryService;
    private String currentData;
    private final Feed currentFeed = new Feed();
    private Article currentArticle;
    private Feed existingFeed;
    private Category currentCategory;
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) throws SAXException {
        if ("item".equalsIgnoreCase(qName)) {
            log.debug("Start of article");
            currentArticle = new Article();
            currentArticle.setFeed(currentFeed);
        }
        if ("media:content".equalsIgnoreCase(qName)) {
            String url = attributes.getValue("url");
            currentArticle.setImageUrl(url);
        }
        if ("category".equalsIgnoreCase(qName) && attributes.getLength() > 0) {
            String domain = attributes.getValue("domain");
            currentCategory = new Category();
            currentCategory.setLink(domain);
        }
        if ("atom:link".equalsIgnoreCase(qName)) {
            String link = attributes.getValue("href");
            currentFeed.setLink(link);
        }
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentData = new String(ch, start, length).trim();
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentData.isEmpty()) {
            currentData = null;
        }
        if (currentArticle != null) {
            log.debug("Set article fields");
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
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
                    try {
                        date = format.parse(currentData);
                    } catch (ParseException e) {
                        log.error(e.getMessage());
                    }
                    currentArticle.setPublishedDate(date);
                    break;
                case "author":
                    currentArticle.setAuthor(currentData);
                    break;
                case "category":
                    if (currentCategory != null) {
                        Category category = categoryService.readCategory(currentData);
                        if (category == null) {
                            currentCategory.setName(currentData);
                            categoryService.createCategory(currentCategory);
                            category = currentCategory;
                        }
                        currentArticle.addCategory(category);
                        category.addArticle(currentArticle);
                        currentCategory = null;
                    }
                    break;
                default:
                    currentFeed.addArticle(currentArticle);
                    if (existingFeed == null) {
                        feedService.createFeed(currentFeed);
                    } else {
                        currentArticle.setFeed(existingFeed);
                        articleService.createArticle(currentArticle);
                    }
            }
        } else if (currentFeed != null) {
            log.debug("Set feed fields");
            switch (qName) {
                case "title":
                    currentFeed.setTitle(currentData);
                    break;
                case "link":
                    currentFeed.setLink(currentData);
                    break;
                case "description":
                    currentFeed.setDescription(currentData);
                    existingFeed = feedService.readFeed(currentFeed.getLink());
                    if (existingFeed == null) {
                        existingFeed = currentFeed;
                        feedService.createFeed(currentFeed);
                    }
                    break;
            }
        }
    }
}
