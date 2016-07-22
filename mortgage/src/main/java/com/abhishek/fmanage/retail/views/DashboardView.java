package com.abhishek.fmanage.retail.views;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.abhishek.fmanage.retail.charts.ItemSummaryChart;
import com.abhishek.fmanage.retail.dto.SummaryDTO;
import com.abhishek.fmanage.retail.restclient.service.RestSummaryService;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DashboardView extends VerticalLayout implements View, ItemClickListener {


	public static final String INDIAN_DATE_FORMAT = "dd/MM/yyyy";
	public static final String SELECT_DATE = "Select Date";
	/** Name of the transaction css style */
    private static final String TRANSACTION_STYLE_NAME = "transactions";
	private static final long serialVersionUID = 1L;
	private VerticalLayout root = null;
	private SummaryDTO summary;
	private final PopupDateField startPopUpDate = new PopupDateField();
	private final PopupDateField endPopUpDate = new PopupDateField();
    HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();
		addStyleName(TRANSACTION_STYLE_NAME);
        addComponent(getToolBar());
        addComponent(buildSaleSummary());
        addComponent(hsplit = getGraphSplitPanel());
        setExpandRatio(hsplit, 1);
	        
		//addStyleName(ValoTheme.PANEL_BORDERLESS);
        //setSizeFull();
       // root.addStyleName("dashboard-view");
        
       // Responsive.makeResponsive(root);
       // root.addComponent();
      //  root.addComponent();
//        Component header = buildHeader();
//        Component saleSummary = buildSaleSummary();
//        addComponent(header);
//        addComponent(saleSummary);
//        addComponent(buildSaleSummary());
//        addComponent(buildSaleSummary());
//        setExpandRatio(header, 1);
//        setExpandRatio(saleSummary, 1);
//        setComponentAlignment(header, Alignment.MIDDLE_LEFT);
//        setComponentAlignment(saleSummary,Alignment.MIDDLE_LEFT);
//       
	}

	 private HorizontalSplitPanel getGraphSplitPanel(){
		HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
		hsplit.setSizeFull();
		//hsplit.setWidth("100%");
		hsplit.setSplitPosition(15);
		hsplit.setHeight("100%");
		hsplit.setFirstComponent(getTreeMenu());
		hsplit.setSecondComponent(new ItemSummaryChart().getChart(
				new RestSummaryService().getRetailSummary(startPopUpDate.getValue(), endPopUpDate.getValue()).getGoldItemSummaryDtoList(), "Gold Items Sale"));
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
	        // Contents from a (prefilled example) hierarchical container:
	      //  sample.setContainerDataSource(ExampleUtil.getHardwareContainer());
	 
	        // Add actions (context menu)
	        //sample.addActionHandler(this);
	 
	        // Cause valueChange immediately when the user selects
		 reportHead.setImmediate(true);
		return reportHead; 
	 }
	 
	 public void itemClick(ItemClickEvent event) {
	        // Indicate which modifier keys are pressed
	        String modifiers = "";
	        if (event.isDoubleClick()) {
	        	String eventName = event.getSource().toString();
	        	if(!StringUtils.isEmpty(eventName) && eventName.equals("Gold Quantity Sale")){
	        		hsplit.setSecondComponent(new ItemSummaryChart().getChart(
		    				new RestSummaryService().getRetailSummary(startPopUpDate.getValue(), endPopUpDate.getValue()).getGoldItemSummaryDtoList(), "Gold Items Sale"));
	        	}
	        	else if(!StringUtils.isEmpty(eventName) && eventName.equals("Silver Quantity Sale")){
	        		hsplit.setSecondComponent(new ItemSummaryChart().getChart(
		    				new RestSummaryService().getRetailSummary(startPopUpDate.getValue(), endPopUpDate.getValue()).getSilverItemSummaryDtoList(), "Silver Items Sale"));
	        	}else if(!StringUtils.isEmpty(eventName) && eventName.equals("Diamond Quantity Sale")){
	        		hsplit.setSecondComponent(new ItemSummaryChart().getChart(
		    				new RestSummaryService().getRetailSummary(startPopUpDate.getValue(), endPopUpDate.getValue()).getDiamondItemSummaryDtoList(), "Diamond Items Sale"));
	        	}else if(!StringUtils.isEmpty(eventName) && eventName.equals("General Quantity Sale")){
	        		hsplit.setSecondComponent(new ItemSummaryChart().getChart(
		    				new RestSummaryService().getRetailSummary(startPopUpDate.getValue(), endPopUpDate.getValue()).getGeneralItemSummaryDtoList(), "General Items Sale"));
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
	        toolbar.addComponent(titleLabel);
	        toolbar.addComponent(startPopUpDate);
	        toolbar.addComponent(endPopUpDate);
	    return toolbar;
	 }
	 
	 
	 private Component buildSaleSummary() {
		 	summary = new RestSummaryService().getRetailSummary(startPopUpDate.getValue(), endPopUpDate.getValue());
	        HorizontalLayout sparks = new HorizontalLayout();
	        //sparks.addStyleName("sparks");
	        sparks.setWidth("100%");
	        sparks.setSpacing(true);
	        sparks.setMargin(true);
	        sparks.addStyleName("mydasboardsummary");

	       // Responsive.makeResponsive(sparks);
	        
	        sparks.addComponent(buildSummaryLabels("Total Sale(INR)", String.format("%.3f", summary.getTotalSale())));
	        sparks.addComponent(buildSummaryLabels("Total Gold Weight(gms)", String.format("%.3f", summary.getTotalGoldWeight())));
	        sparks.addComponent(buildSummaryLabels("Total Silver Weight(gms)", String .format("%.3f",summary.getTotalSilverWeight())));
	        return sparks;
	    }
	
	private Component buildSummaryLabels(String caption, String weight){
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
        
        Label title = new Label(weight);
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_SMALL);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        vl.addComponent(title);
        p.setContent(vl);
        p.addStyleName("mydasboardsummary");
		return p;
	}
	
	
	private Component buildHeader(){
		HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("sidebar");
        header.setWidth("100%");
        header.setSpacing(true);
        header.setMargin(true);
        Label titleLabel = new Label("Dashboard");
        titleLabel.setId("dashboard-title");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);
        header.addComponent(startPopUpDate);
        header.addComponent(endPopUpDate);
        header.setExpandRatio(titleLabel, 1);
        header.setExpandRatio(startPopUpDate, 1);
        header.setExpandRatio(endPopUpDate, 1);
        header.setComponentAlignment(titleLabel, Alignment.MIDDLE_LEFT);
        header.setComponentAlignment(startPopUpDate, Alignment.MIDDLE_LEFT);
        header.setComponentAlignment(endPopUpDate, Alignment.MIDDLE_LEFT);
        return header;
	}
	
	private Date getStartDate(Date endDate, int monthsBack){
	    	LocalDateTime ldt = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
	    	ldt = ldt.minusMonths(monthsBack);
	    	return  Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}
}
