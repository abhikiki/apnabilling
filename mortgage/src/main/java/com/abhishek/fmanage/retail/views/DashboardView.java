package com.abhishek.fmanage.retail.views;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.abhishek.fmanage.retail.charts.ItemSummaryChart;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.SummaryDTO;
import com.abhishek.fmanage.retail.restclient.service.RestRetailSummaryService;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DashboardView extends VerticalLayout implements View, ItemClickListener {


	public static final String INDIAN_DATE_FORMAT = "dd/MM/yyyy";
	public static final String SELECT_DATE = "Select Date";
	/** Name of the transaction css style */
    private static final String TRANSACTION_STYLE_NAME = "transactions";
	private static final long serialVersionUID = 1L;
	private SummaryDTO summary;
	private final PopupDateField startPopUpDate = new PopupDateField();
	private final PopupDateField endPopUpDate = new PopupDateField();
    HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
    private Label totalSaleLabel = new Label("");
    private Label silverWeightLabel = new Label("");
    private Label goldWeightLabel = new Label("");
    private Label vatLabel = new Label("");
    
	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();
		addStyleName(TRANSACTION_STYLE_NAME);
        addComponent(getToolBar());
        addComponent(buildSaleSummary());
        addComponent(hsplit = getGraphSplitPanel());
        setExpandRatio(hsplit, 1);
	}

	 private HorizontalSplitPanel getGraphSplitPanel(){
		HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
		hsplit.setSizeFull();
		//hsplit.setWidth("100%");
		hsplit.setSplitPosition(15);
		hsplit.setHeight("100%");
		hsplit.setFirstComponent(getTreeMenu());
		hsplit.setSecondComponent(new ItemSummaryChart().getChart(
				new RestRetailSummaryService((ShopDTO)UI.getCurrent().getSession().getAttribute(ShopDTO.class))
				.getRetailSummary(startPopUpDate.getValue(), endPopUpDate.getValue()).getGoldItemSummaryDtoList(), "Gold Items Sale"));
		return hsplit;
		 
	 }
	 
	 private Tree getTreeMenu(){
		 Tree reportHead = new Tree("Report");
		 reportHead.addItem("Gold Quantity Sale");
		 reportHead.addItem("Silver Quantity Sale");
		 reportHead.addItem("Diamond Quantity Sale");
		 reportHead.addItem("General Quantity Sale");
		 // Add ItemClickListener to the tree
		 reportHead.addListener(this);
		 reportHead.setImmediate(true);
		return reportHead; 
	 }
	 
	 public void itemClick(ItemClickEvent event) {
	        if (event.isDoubleClick()) {
	        	String eventName = event.getSource().toString();
	        	if(!StringUtils.isEmpty(eventName) && eventName.equals("Gold Quantity Sale")){
	        		hsplit.setSecondComponent(new ItemSummaryChart().getChart(
	        				summary.getGoldItemSummaryDtoList(), "Gold Items Sale"));
	        	}
	        	else if(!StringUtils.isEmpty(eventName) && eventName.equals("Silver Quantity Sale")){
	        		hsplit.setSecondComponent(new ItemSummaryChart().getChart(
	        				summary.getSilverItemSummaryDtoList(), "Silver Items Sale"));
	        	}else if(!StringUtils.isEmpty(eventName) && eventName.equals("Diamond Quantity Sale")){
	        		hsplit.setSecondComponent(new ItemSummaryChart().getChart(
	        				summary.getDiamondItemSummaryDtoList(), "Diamond Items Sale"));
	        	}else if(!StringUtils.isEmpty(eventName) && eventName.equals("General Quantity Sale")){
	        		hsplit.setSecondComponent(new ItemSummaryChart().getChart(
	        				summary.getGeneralItemSummaryDtoList(), "General Items Sale"));
	        	}
           }
	      
	    }
	 
	 private Component getToolBar(){
		 startPopUpDate.setCaption("Start Date");
	        startPopUpDate.setImmediate(true);
	        startPopUpDate.setInvalidAllowed(false);
	        startPopUpDate.setLocale(new Locale("en", "IN"));
	        startPopUpDate.setDateFormat(INDIAN_DATE_FORMAT);
	        startPopUpDate.setTextFieldEnabled(false);
	        startPopUpDate.setInputPrompt(SELECT_DATE);
	        startPopUpDate.setValue(getStartDate(new Date(), 2));

	        endPopUpDate.setCaption("End Date");
	        endPopUpDate.setImmediate(true);
	        endPopUpDate.setInvalidAllowed(false);
	        endPopUpDate.setLocale(new Locale("en", "IN"));
	        endPopUpDate.setDateFormat(INDIAN_DATE_FORMAT);
	        endPopUpDate.setTextFieldEnabled(false);
	        endPopUpDate.setInputPrompt(SELECT_DATE);
	        endPopUpDate.setValue(new Date());
	        HorizontalLayout toolbar = new HorizontalLayout();
	        toolbar.setWidth("100%");
	        toolbar.setSpacing(true);
	        toolbar.setMargin(true);
	        toolbar.addStyleName("mytoolbar");
	        
	        Label titleLabel = new Label("Dashboard");
	        titleLabel.setId("dashboard-title");
	        titleLabel.setSizeUndefined();
	        titleLabel.addStyleName(ValoTheme.LABEL_H1);
	        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        
	        Button searchButton = new Button("Search", new ClickListener()
	        {
	            private static final long serialVersionUID = 1L;

	            @Override
	            
	            public void buttonClick(ClickEvent event)
	            {
	            	summary = new RestRetailSummaryService((ShopDTO)UI.getCurrent().getSession().getAttribute(ShopDTO.class))
	            	.getRetailSummary(startPopUpDate.getValue(), endPopUpDate.getValue());
	            	Double vatTotal = Math.round(summary.getTotalVat() * 100.0) / 100.0;
	            	vatLabel.setValue(String.format("%.3f", vatTotal));
	            	silverWeightLabel.setValue(String.format("%.3f",summary.getTotalSilverWeight()));
	            	goldWeightLabel.setValue(String.format("%.3f",summary.getTotalGoldWeight()));
	            	totalSaleLabel.setValue(String.format("%.3f",summary.getTotalSale()));
	            	hsplit.setSecondComponent(new ItemSummaryChart().getChart(
	        				summary.getGoldItemSummaryDtoList(), "Gold Items Sale"));
	            }

				
	        });
	        searchButton.setWidth("80%");
	        searchButton.addStyleName("default");
	        searchButton.setIcon(FontAwesome.SEARCH);
	        
	        toolbar.addComponent(titleLabel);
	        toolbar.addComponent(startPopUpDate);
	        toolbar.addComponent(endPopUpDate);
	        toolbar.addComponent(searchButton);
	    return toolbar;
	 }
	 
	 
	 private Component buildSaleSummary() {
		 	summary = new RestRetailSummaryService((ShopDTO)UI.getCurrent().getSession().getAttribute(ShopDTO.class))
		 	.getRetailSummary(startPopUpDate.getValue(), endPopUpDate.getValue());
	        HorizontalLayout sparks = new HorizontalLayout();
	        //sparks.addStyleName("sparks");
	        sparks.setWidth("100%");
	        sparks.setSpacing(true);
	        sparks.setMargin(true);
	        sparks.addStyleName("mydasboardsummary");

	        sparks.addComponent(buildSummaryLabels("Total Sale(INR)", String.format("%.3f", summary.getTotalSale()), "SALE"));
	        Double vatTotal = Math.round(summary.getTotalVat() * 100.0) / 100.0;
	        sparks.addComponent(buildSummaryLabels("Total GST(INR)", String .format("%.2f",vatTotal), "VAT"));
	        sparks.addComponent(buildSummaryLabels("Total Gold Weight(gms)", String.format("%.3f", summary.getTotalGoldWeight()), "GOLDWEIGHT"));
	        sparks.addComponent(buildSummaryLabels("Total Silver Weight(gms)", String .format("%.3f",summary.getTotalSilverWeight()), "SILVERWEIGHT"));
	        return sparks;
	    }
	
	private Component buildSummaryLabels(String caption, String weight, String labelName){
		Panel p = new Panel();
		p.setHeight("100px");
		p.setWidth("200px");
		VerticalLayout  vl = new VerticalLayout();
		vl.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		Label current = new Label(caption);
        current.setSizeUndefined();
        current.addStyleName(ValoTheme.LABEL_HUGE);
        current.addStyleName(ValoTheme.LABEL_COLORED);
        vl.addComponent(current);
        vl.addStyleName("test");
        if(labelName.equals("SALE")){
        	 totalSaleLabel = new Label(weight);
        	 totalSaleLabel.setImmediate(true);
        	 totalSaleLabel.setSizeUndefined();
        	 totalSaleLabel.addStyleName(ValoTheme.LABEL_SMALL);
        	 totalSaleLabel.addStyleName(ValoTheme.LABEL_LIGHT);
             vl.addComponent(totalSaleLabel);
        }else if(labelName.equals("GOLDWEIGHT")){
        	 goldWeightLabel = new Label(weight);
        	 goldWeightLabel.setImmediate(true);
        	 goldWeightLabel.setSizeUndefined();
        	 goldWeightLabel.addStyleName(ValoTheme.LABEL_SMALL);
        	 goldWeightLabel.addStyleName(ValoTheme.LABEL_LIGHT);
        	 vl.addComponent(goldWeightLabel);
        }else if(labelName.equals("SILVERWEIGHT")){
        	 silverWeightLabel = new Label(weight);
        	 silverWeightLabel.setImmediate(true);
        	 silverWeightLabel.setSizeUndefined();
        	 silverWeightLabel.addStyleName(ValoTheme.LABEL_SMALL);
        	 silverWeightLabel.addStyleName(ValoTheme.LABEL_LIGHT);
             vl.addComponent(silverWeightLabel);
        }else{
        	 vatLabel = new Label(weight);
        	 vatLabel.setImmediate(true);
        	 vatLabel.setSizeUndefined();
        	 vatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        	 vatLabel.addStyleName(ValoTheme.LABEL_LIGHT);
             vl.addComponent(vatLabel);
        }
        
        p.setContent(vl);
        p.addStyleName("mydashboardsummary");
		return p;
	}
	
	private Date getStartDate(Date endDate, int monthsBack){
	    	LocalDateTime ldt = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
	    	ldt = ldt.minusMonths(monthsBack);
	    	return  Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}
}
