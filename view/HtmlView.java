package com.javarush.task.task28.task2810.view;

import com.javarush.task.task28.task2810.Controller;
import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Parse HTML with Jsoup
 */
public class HtmlView implements View {

    private Controller controller;
    private final String filePath = "./4.JavaCollections/src/" + this.getClass().getPackage().getName().replace('.', '/') + "/vacancies.html";

    @Override
    public void update(List<Vacancy> vacancies) {
//        System.out.println(vacancies.size());
        updateFile(getUpdatedFileContent(vacancies));
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Odessa");
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(new File(filePath), "UTF-8");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        try {
            Document document = getDocument();
            Element element = document.getElementsByClass("template").first();
            Element template = element.clone();
            template.removeClass("template");
            template.removeAttr("style");

            document.getElementsByAttributeValueEnding("class", "vacancy").remove();

            for (Vacancy vacancy : vacancies) {
                Element vElement = template.clone();
                vElement.getElementsByClass("city").first().text(vacancy.getCity());
                vElement.getElementsByClass("companyName").first().text(vacancy.getCompanyName());
                vElement.getElementsByClass("salary").first().text(vacancy.getSalary());
                vElement.getElementsByClass("title").first().getElementsByAttribute("href").first().text(vacancy.getTitle());
                vElement.getElementsByClass("title").first().select("a").first().attr("href", vacancy.getUrl());

                element.before(vElement.outerHtml());
            }

            return document.html();
        }
        catch (IOException e) {
            e.printStackTrace();
            return "Some exception occurred";
        }
    }

    private void updateFile(String info) {

        try (FileWriter fileWriter = new FileWriter(new File(filePath))) {
            fileWriter.write(info);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
