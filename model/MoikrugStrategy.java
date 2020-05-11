package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Strategy for Moikrug website
 */
public class MoikrugStrategy implements Strategy {
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?q=java+%s&page=%d";
    private static final String SITE_NAME = "https://moikrug.ru";

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
                Elements elements = doc.getElementsByClass("job");
                if (!elements.isEmpty()) {
                    for (Element el : elements) {
                        Vacancy vacancy = new Vacancy();
                        vacancy.setTitle(el.getElementsByAttribute("title").first().text());
                        vacancy.setSalary(el.getElementsByClass("salary").first().text());
                        Elements locationElement = el.getElementsByClass("location");
                        vacancy.setCity(locationElement.isEmpty() ? "" : locationElement.text());
                        vacancy.setCompanyName(el.getElementsByClass("company_name").first().text());
                        vacancy.setSiteName(SITE_NAME);
                        vacancy.setUrl(SITE_NAME + el.getElementsByAttribute("title").first().getElementsByAttribute("href").attr("href"));

                        vacancies.add(vacancy);
//                        System.out.println(vacancy);
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
