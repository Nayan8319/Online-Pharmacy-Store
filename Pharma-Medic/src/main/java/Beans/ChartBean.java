/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package Beans;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartDataSet;

import java.io.Serializable;
import java.util.Arrays;

@Named("chartBean")
@ViewScoped
public class ChartBean implements Serializable {

    private BarChartModel barModel;

    @PostConstruct
    public void init() {
        createBarModel();
    }

    private void createBarModel() {
        barModel = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Orders");

        // Static order data
        barDataSet.setData(Arrays.asList(15, 30, 45)); // January, February, March orders

        // Define colors for the bars
        barDataSet.setBackgroundColor(Arrays.asList(
                "rgba(75, 192, 192, 0.2)",
                "rgba(54, 162, 235, 0.2)",
                "rgba(255, 206, 86, 0.2)"
        ));
        barDataSet.setBorderColor(Arrays.asList(
                "rgb(75, 192, 192)",
                "rgb(54, 162, 235)",
                "rgb(255, 206, 86)"
        ));
        barDataSet.setBorderWidth(1);

        // Add data to the model
        data.addChartDataSet(barDataSet);

        // Set month labels
        data.setLabels(Arrays.asList("January", "February", "March"));

        barModel.setData(data);
    }

    public BarChartModel getBarModel() {
        return barModel;
    }
}
