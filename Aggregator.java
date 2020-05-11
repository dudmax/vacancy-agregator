package com.javarush.task.task28.task2810;

import com.javarush.task.task28.task2810.model.*;
import com.javarush.task.task28.task2810.view.HtmlView;
import com.javarush.task.task28.task2810.view.View;

import java.util.Date;

/**
 * Main MVC config and start class of aplication
 */
public class Aggregator {

    public static void main(String[] args) {
        Provider provider = new Provider(new HHStrategy());
        Provider providerMoiKrug = new Provider(new MoikrugStrategy());

        HtmlView view = new HtmlView();
        Model model = new Model(view, provider, providerMoiKrug);
        Controller controller = new Controller(model);
        view.setController(controller);
        view.userCitySelectEmulationMethod();

    }
}
