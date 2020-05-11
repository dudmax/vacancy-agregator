package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.view.View;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Model class
 */
public class Model {

    private View view;
    /**
     * Strategy providers
     */
    private Provider[] providers;

    public Model(View view, Provider... providers) {
        if (providers != null && providers.length!=0 && view != null) {
            this.view = view;
            this.providers = providers;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public void selectCity(String city) {
        view.update(Arrays.stream(providers)
                .map(provider -> provider.getJavaVacancies(city))
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }
}
