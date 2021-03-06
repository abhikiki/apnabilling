package com.abhishek.fmanage.retail.charts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.abhishek.fmanage.retail.dto.ItemSummaryDTO;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.addon.charts.model.Title;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class ItemSummaryChart {

	
    public Component getChart(List<ItemSummaryDTO> itemSummary, String title) {
    	
        //Chart columnChart = getColumnChart(itemSummary, title);
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        Chart pieChart = createPieChart(itemSummary, title);
        pieChart.setHeight("450px");
        pieChart.setWidth("100%");
        layout.addComponent(pieChart);
        //layout.addComponent(columnChart);
        //layout.setExpandRatio(columnChart, 1.0f);
        layout.setExpandRatio(pieChart, 1.0f);
        return layout;
    }


	private Chart getColumnChart(List<ItemSummaryDTO> itemSummary, String title) {
		Chart chart = new Chart(ChartType.COLUMN);

        Configuration conf = chart.getConfiguration();
       // conf.getChart().setMargin(50, 100, 100, 50);

        conf.setTitle(new Title(title));

        itemSummary.stream().map(item -> item.getItemName()).toArray();
        XAxis xAxis = new XAxis();
        String[] itemNameArr = new String[]{};
        xAxis.setCategories(itemSummary.stream().map(item -> item.getItemName()).collect(Collectors.toCollection(ArrayList<String>::new)).toArray(itemNameArr));
        Labels labels = new Labels();
        labels.setRotation(-45);
        labels.setAlign(HorizontalAlign.RIGHT);
        Style style = new Style();
        style.setFontSize("13px");
        style.setFontFamily("Verdana, sans-serif");
        labels.setStyle(style);
        xAxis.setLabels(labels);
        conf.addxAxis(xAxis);
        
        
        YAxis yAxis = new YAxis();
       // yAxis.setMin(0);
        yAxis.setTitle("Quantity");
        conf.addyAxis(yAxis);

        Legend legend = new Legend();
        legend.setEnabled(false);
        conf.setLegend(legend);

        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("'<b>'+ this.x +'</b><br/>'+'Quantity: '"
                + "+ Highcharts.numberFormat(this.y, 1)");
        conf.setTooltip(tooltip);

        Number[] itemQuantityArr = new Number[]{};
        ListSeries serie = new ListSeries("Quantity", itemSummary.stream().map(item -> item.getQuantity()).collect(Collectors.toCollection(ArrayList<Long>::new)).toArray(itemQuantityArr));
        Labels dataLabels = new Labels();
        dataLabels.setEnabled(true);
        dataLabels.setRotation(-90);
        dataLabels.setColor(new SolidColor(255, 255, 255));
        dataLabels.setAlign(HorizontalAlign.RIGHT);
        dataLabels.setX(4);
        dataLabels.setY(10);
        dataLabels.setFormatter("this.y");
        PlotOptionsColumn plotOptionsColumn = new PlotOptionsColumn();
        plotOptionsColumn.setDataLabels(dataLabels);
        plotOptionsColumn.setColor(SolidColor.CADETBLUE);
        serie.setPlotOptions(plotOptionsColumn);
        conf.addSeries(serie);

        chart.drawChart(conf);
		return chart;
	}
    
    
    private Chart createPieChart(List<ItemSummaryDTO> itemSummary, String title) {
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();
        conf.setTitle(title);
        conf.setExporting(true);
        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        Labels dataLabels = new Labels();
        dataLabels.setEnabled(true);
        dataLabels.setFormatter("'<b>'+ this.point.name +'</b>: '+ this.y");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        final DataSeries series = new DataSeries();
        for(ItemSummaryDTO summary : itemSummary){
        	series.add(new DataSeriesItem(summary.getItemName(), summary.getQuantity()));
        }
        conf.setSeries(series);
        
        chart.drawChart(conf);
        return chart;
    }
}
