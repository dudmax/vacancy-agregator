package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Strategy for HH.ru website
 */
public class HHStrategy implements Strategy {
    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+%s&page=%d";
    private static final String SITE_NAME = "http://hh.ru";

    public static String getUrlFormat() {
        return URL_FORMAT;
    }

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();
        try {
            int page = 0;
            while (page >=0) {
                Document doc = getDocument(searchString, page);
                Elements elements = doc.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");
                if (!elements.isEmpty()) {
                    for (Element el : elements) {
                        Vacancy vacancy = new Vacancy();
                        vacancy.setTitle(el.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").text());
                        Elements salaryElement = el.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation");
                        vacancy.setSalary(salaryElement.isEmpty() ? "" : salaryElement.text());
                        vacancy.setCity(el.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text());
                        vacancy.setCompanyName(el.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text().trim());
                        vacancy.setSiteName(SITE_NAME);
                        vacancy.setUrl(el.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").attr("href"));

                        vacancies.add(vacancy);
                        //System.out.println(vacancy);
                    }
                    page++;
                }
                else {
                    page = -1;
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getCause());
        }
        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, searchString, page))
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.2 Safari/605.1.15")
                .referrer("no-referrer-when-downgrade")
                .get();
    }
}
