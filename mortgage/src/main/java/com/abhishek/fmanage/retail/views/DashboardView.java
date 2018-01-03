package com.abhishek.fmanage.retail.views;

import com.abhishek.fmanage.retail.charts.GoldTypeQuantitySaleSummaryChart;
import com.abhishek.fmanage.retail.charts.ItemSummaryChart;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.SummaryDTO;
import com.abhishek.fmanage.retail.restclient.service.RestRetailSummaryService;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

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
	private Label cardPaymentLabel = new Label("");
	private Label cashPaymentLabel = new Label("");
	private Label chequePaymentLabel = new Label("");
	private Label neftPaymentLabel = new Label("");
	private Label rtgsPaymentLabel = new Label("");
    
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
		 reportHead.addItem("Gold Type Quantity Sale");
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
	        	}else if(!StringUtils.isEmpty(eventName) && eventName.equals("Gold Type Quantity Sale")){

	        		hsplit.setSecondComponent(new GoldTypeQuantitySaleSummaryChart().getChart(summary.getGoldTypeQuantitySaleSummaryList(), "Gold Type Quantity Sale"));
				}else if(!StringUtils.isEmpty(eventName) && eventName.equals("Silver Quantity Sale")){
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
	            	cardPaymentLabel.setValue(String.format("%.3f",summary.getTotalCardPayment()));
	            	cashPaymentLabel.setValue(String.format("%.3f",summary.getTotalCashPayment()));
					chequePaymentLabel.setValue(String.format("%.3f",summary.getTotalChequePayment()));
					neftPaymentLabel.setValue(String.format("%.3f",summary.getTotalNeftPayment()));
					rtgsPaymentLabel.setValue(String.format("%.3f",summary.getTotalRtgsPayment()));
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
	        sparks.addComponent(buildSummaryLabels("GST(INR)", String .format("%.2f",vatTotal), "VAT"));
	        sparks.addComponent(buildSummaryLabels("Gold Weight(gms)", String.format("%.3f", summary.getTotalGoldWeight()), "GOLDWEIGHT"));
	        sparks.addComponent(buildSummaryLabels("Silver Weight(gms)", String .format("%.3f",summary.getTotalSilverWeight()), "SILVERWEIGHT"));
		 	sparks.addComponent(buildSummaryLabels("Card(INR)", String .format("%.3f",summary.getTotalCardPayment()), "CARDPAYMENT"));
		 	sparks.addComponent(buildSummaryLabels("Cash(INR)", String .format("%.3f",summary.getTotalCashPayment()), "CASHPAYMENT"));
		 	sparks.addComponent(buildSummaryLabels("Cheque(INR)", String .format("%.3f",summary.getTotalChequePayment()), "CHEQUEPAYMENT"));
		 	sparks.addComponent(buildSummaryLabels("Neft(INR)", String .format("%.3f",summary.getTotalNeftPayment()), "NEFTPAYMENT"));
		 	sparks.addComponent(buildSummaryLabels("Rtgs(INR)", String .format("%.3f",summary.getTotalRtgsPayment()), "RTGSPAYMENT"));
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
        }else if(labelName.equals("VAT")){
        	 vatLabel = new Label(weight);
        	 vatLabel.setImmediate(true);
        	 vatLabel.setSizeUndefined();
        	 vatLabel.addStyleName(ValoTheme.LABEL_SMALL);
        	 vatLabel.addStyleName(ValoTheme.LABEL_LIGHT);
             vl.addComponent(vatLabel);
        }else if(labelName.equals("CARDPAYMENT")){
			cardPaymentLabel = new Label(weight);
			cardPaymentLabel.setImmediate(true);
			cardPaymentLabel.setSizeUndefined();
			cardPaymentLabel.addStyleName(ValoTheme.LABEL_SMALL);
			cardPaymentLabel.addStyleName(ValoTheme.LABEL_LIGHT);
			vl.addComponent(cardPaymentLabel);
		}else if(labelName.equals("CASHPAYMENT")){
			cashPaymentLabel = new Label(weight);
			cashPaymentLabel.setImmediate(true);
			cashPaymentLabel.setSizeUndefined();
			cashPaymentLabel.addStyleName(ValoTheme.LABEL_SMALL);
			cashPaymentLabel.addStyleName(ValoTheme.LABEL_LIGHT);
			vl.addComponent(cashPaymentLabel);
		}else if(labelName.equals("CHEQUEPAYMENT")){
			chequePaymentLabel = new Label(weight);
			chequePaymentLabel.setImmediate(true);
			chequePaymentLabel.setSizeUndefined();
			chequePaymentLabel.addStyleName(ValoTheme.LABEL_SMALL);
			chequePaymentLabel.addStyleName(ValoTheme.LABEL_LIGHT);
			vl.addComponent(chequePaymentLabel);
		}else if(labelName.equals("NEFTPAYMENT")){
			neftPaymentLabel = new Label(weight);
			neftPaymentLabel.setImmediate(true);
			neftPaymentLabel.setSizeUndefined();
			neftPaymentLabel.addStyleName(ValoTheme.LABEL_SMALL);
			neftPaymentLabel.addStyleName(ValoTheme.LABEL_LIGHT);
			vl.addComponent(neftPaymentLabel);
		}else if(labelName.equals("RTGSPAYMENT")){
			rtgsPaymentLabel = new Label(weight);
			rtgsPaymentLabel.setImmediate(true);
			rtgsPaymentLabel.setSizeUndefined();
			rtgsPaymentLabel.addStyleName(ValoTheme.LABEL_SMALL);
			rtgsPaymentLabel.addStyleName(ValoTheme.LABEL_LIGHT);
			vl.addComponent(rtgsPaymentLabel);
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
