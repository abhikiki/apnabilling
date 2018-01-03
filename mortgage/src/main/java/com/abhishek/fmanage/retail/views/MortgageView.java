package com.abhishek.fmanage.retail.views;

import com.abhishek.fmanage.mortgage.data.container.MortgageItemContainer;
import com.abhishek.fmanage.mortgage.data.container.MortgageItemType;
import com.abhishek.fmanage.mortgage.dto.MortgageItemDTO;
import com.abhishek.fmanage.mortgage.dto.MortgageTransactionDTO;
import com.abhishek.fmanage.mortgage.tables.MortgageItemTable;
import com.abhishek.fmanage.retail.dto.CustomerDTO;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.restclient.service.RestMortgageTransactionService;
import com.abhishek.fmanage.retail.tables.CustomerInfoLayout;
import com.abhishek.fmanage.utility.ConvertNumberToWords;
import com.avathartech.fastformfields.widgets.DecimalTextField;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

public class MortgageView extends VerticalLayout implements View {

	public static final String INDIAN_DATE_FORMAT = "dd/MM/yyyy";
	public static final String SELECT_DATE = "Select Date";
	private Table mortgageGoldItemTable = new Table();
	private Table mortgageSilverItemTable = new Table();
	private Table mortgageDiamondItemTable = new Table();
	private Table workingTable = new Table();
	private VerticalLayout mortgageViewVerticalLayout = new VerticalLayout();
	private Component customerLayout;
	private CustomerDTO cusBean = new CustomerDTO();
	private MortgageItemContainer mortgageGoldItemContainer = new MortgageItemContainer(MortgageItemType.GOLD);
	private MortgageItemContainer mortgageSilverItemContainer = new MortgageItemContainer(MortgageItemType.SILVER);
	private MortgageItemContainer mortgageDiamondItemContainer = new MortgageItemContainer(MortgageItemType.DIAMOND);
	private ComboBox keeperNameComboBox = new ComboBox("Mortgage Keeper");
	private final PopupDateField billPopUpDate = new PopupDateField();
	private final Label amountInWords = new Label("<b>Amount in words: </b>",
			ContentMode.HTML);
	private TextArea notes = new TextArea("Invoice Notes");
	private Button saveBtn = new Button("Generate Bill");
	private DecimalTextField amountTxtField;
	private DecimalTextField interestPercentage;
	private ComboBox keeperListCombo;
	private CheckBox goldTableWeightCheckBox = new CheckBox("By Total Weight");
	private CheckBox silverTableWeightCheckBox = new CheckBox("By Total Weight");
	private DecimalTextField goldTableTotalWeightTxt = new DecimalTextField();
	private DecimalTextField silverTableTotalWeightTxt = new DecimalTextField();
	

	@Override
	public void enter(ViewChangeEvent event) {

		setSizeFull();
		HorizontalLayout toolbarLayout = new HorizontalLayout();
		toolbarLayout.addComponent(getToolbar());
		toolbarLayout.setSizeUndefined();
		toolbarLayout.setSizeFull();
		toolbarLayout.addStyleName("mytoolbar");

		mortgageGoldItemTable = getMortgageBillingTable(
				mortgageGoldItemContainer, MortgageItemType.GOLD);
		mortgageSilverItemTable = getMortgageBillingTable(
				mortgageSilverItemContainer, MortgageItemType.SILVER);
		mortgageDiamondItemTable = getMortgageBillingTable(
				mortgageDiamondItemContainer, MortgageItemType.DIAMOND);
		VerticalLayout goldBillingLayout = getBillingLayout(
				mortgageGoldItemTable, mortgageGoldItemContainer,
				MortgageItemType.GOLD);
		VerticalLayout silverBillingLayout = getBillingLayout(
				mortgageSilverItemTable, mortgageSilverItemContainer,
				MortgageItemType.SILVER);
		VerticalLayout diamondBillingLayout = getBillingLayout(
				mortgageDiamondItemTable, mortgageDiamondItemContainer,
				MortgageItemType.DIAMOND);
		
		goldBillingLayout.setSpacing(true);
		silverBillingLayout.setSpacing(true);
		diamondBillingLayout.setSpacing(true);
		customerLayout = new CustomerInfoLayout(cusBean).getUserDetailFormLayout();
		mortgageViewVerticalLayout.addComponent(toolbarLayout);
		mortgageViewVerticalLayout.addComponent(customerLayout);
		mortgageViewVerticalLayout.addComponent(goldBillingLayout);
		mortgageViewVerticalLayout.addComponent(silverBillingLayout);
		mortgageViewVerticalLayout.addComponent(diamondBillingLayout);
		mortgageViewVerticalLayout.setSpacing(true);
		mortgageViewVerticalLayout.setImmediate(true);

		VerticalLayout itemAndNotesLayoutVL = new VerticalLayout();
		itemAndNotesLayoutVL.setWidth("100%");
		itemAndNotesLayoutVL.addComponent(mortgageViewVerticalLayout);
		itemAndNotesLayoutVL.addComponent(getNotesAndSaveTransactionLayout());
		itemAndNotesLayoutVL.setSpacing(true);
		itemAndNotesLayoutVL.setImmediate(true);

		setCheckboxListeners(goldTableWeightCheckBox, MortgageItemType.GOLD);
		setCheckboxListeners(silverTableWeightCheckBox, MortgageItemType.SILVER);
		
		VerticalSplitPanel vSplitPanel = new VerticalSplitPanel();
		vSplitPanel.setFirstComponent(toolbarLayout);
		vSplitPanel.setSecondComponent(itemAndNotesLayoutVL);
		vSplitPanel.setSizeFull();
		vSplitPanel.setSplitPosition(18, Unit.PERCENTAGE);
		Panel toolBarPanel = new Panel();
		toolBarPanel.setSizeUndefined();
		toolBarPanel.setWidth("100%");
		addComponent(vSplitPanel);
	}

	private void setCheckboxListeners(CheckBox totalWeightcheckBox, MortgageItemType itemType) {
		
		totalWeightcheckBox.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -6857112166321059475L;

            public void valueChange(ValueChangeEvent event) {
                boolean value = (Boolean) event.getProperty().getValue();
                DecimalTextField totalWeightTxt = new DecimalTextField();
                Table table = null;
                switch(itemType){
                	case GOLD : table = mortgageGoldItemTable;
                		totalWeightTxt = goldTableTotalWeightTxt;
                		break;
                	case SILVER : table = mortgageSilverItemTable;
                		totalWeightTxt = silverTableTotalWeightTxt;
                		break;
                }
                if(value){
                	totalWeightTxt.setEnabled(true);
                	for (Object obj : table.getItemIds()) {
                		 TextField goldWeight =  (TextField) (table
         						.getContainerDataSource().getItem(obj)
         						.getItemProperty(MortgageItemContainer.WEIGHT).getValue());
                		 goldWeight.setValue("0.000");
                		 goldWeight.setEnabled(false);
                	}
                	
                }else{
                	totalWeightTxt.setValue("0.000");
                	totalWeightTxt.setEnabled(false);
                	for (Object obj : table.getItemIds()) {
               		 TextField goldWeight =  (TextField) (table
        						.getContainerDataSource().getItem(obj)
        						.getItemProperty(MortgageItemContainer.WEIGHT).getValue());
               		 goldWeight.setValue("0.000");
               		 goldWeight.setEnabled(true);
               	}
                }
               
            }
        });
		totalWeightcheckBox.setValue(false);
		
	}

	private HorizontalLayout getNotesAndSaveTransactionLayout() {
		HorizontalLayout notesAndSaveHL = new HorizontalLayout();
		notesAndSaveHL.setSizeFull();
		notesAndSaveHL.setSpacing(true);
		notes.setSizeFull();
		notes.setIcon(FontAwesome.COMMENTS);
		notesAndSaveHL.addComponent(notes);
		saveBtn = (Button) getSaveTransactionButton();
		notesAndSaveHL.addComponent(getSaveTransactionButton());
		notesAndSaveHL.setComponentAlignment(saveBtn, Alignment.MIDDLE_LEFT);
		return notesAndSaveHL;
	}

	private Component getSaveTransactionButton() {
		saveBtn = new Button("Save");
		saveBtn.setIcon(FontAwesome.SAVE);
		saveBtn.setSizeUndefined();
		saveBtn.setImmediate(true);
		saveBtn.addStyleName("default");
		saveBtn.setData(this);
		saveBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				MortgageTransactionDTO transDto = new MortgageTransactionDTO();
				transDto.setActive(true);
				transDto.setCustomerDto(cusBean);
				transDto.setAmount(Double.valueOf(amountTxtField.getValue()));
				transDto.setInterestRate(Double.valueOf(interestPercentage
						.getValue()));
				transDto.setGoldItemList(getItemList(MortgageItemType.GOLD));
				transDto.setSilverItemList(getItemList(MortgageItemType.SILVER));
				transDto.setDiamondItemList(getItemList(MortgageItemType.DIAMOND));
				transDto.setStartDate(billPopUpDate.getValue());
				transDto.setNotes(notes.getValue());
				transDto.setTotalGoldWeight(Double.valueOf(goldTableTotalWeightTxt.getValue()));
				transDto.setTotalSilverWeight(Double.valueOf(silverTableTotalWeightTxt.getValue()));
				transDto.setMortgageKeeper(String.valueOf(keeperListCombo
						.getValue()));
				ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(
						ShopDTO.class);
				String validationMessage = checkToSaveTransaction(transDto);
				if(StringUtils.isEmpty(validationMessage)){
					long transId = new RestMortgageTransactionService(shopDto).createBill(shopDto,	transDto);
					if(transId > 0){
						Notification.show("Transaction Saved successfully", Type.WARNING_MESSAGE);
					}else{
						Notification.show("Failed Saving transactiony", Type.WARNING_MESSAGE);
					}
				}else{
					Notification.show(validationMessage, Type.WARNING_MESSAGE);
				}
				
			}
		});
		return saveBtn;
	}

	private List<MortgageItemDTO> getItemList(MortgageItemType itemType) {
		switch (itemType) {
		case GOLD:
			workingTable = mortgageGoldItemTable;
			break;
		case SILVER:
			workingTable = mortgageSilverItemTable;
			break;
		case DIAMOND:
			workingTable = mortgageDiamondItemTable;
			break;
		}

		List<MortgageItemDTO> itemList = new ArrayList<MortgageItemDTO>();
		for (Object obj : workingTable.getItemIds()) {
			Double diamondGoldWeight = 0.000d;
			Double diamondDiamondWeight = 0.000d;
			Double weight = 0.000d;
			String piecePair = "";
			Double quantity = 0.0d;
			quantity = Double.valueOf(((DecimalTextField) (workingTable
					.getContainerDataSource().getItem(obj)
					.getItemProperty(MortgageItemContainer.QUANTITY).getValue()))
					.getValue());
			piecePair = (String) ((ComboBox) (workingTable
					.getContainerDataSource().getItem(obj)
					.getItemProperty(MortgageItemContainer.PIECE_PAIR)
					.getValue())).getValue();
			if(itemType != MortgageItemType.DIAMOND){

				 weight = Double.valueOf(((DecimalTextField) (workingTable
						.getContainerDataSource().getItem(obj)
						.getItemProperty(MortgageItemContainer.WEIGHT).getValue()))
						.getValue());
				 
			}else{
				diamondGoldWeight = Double.valueOf(((DecimalTextField) (workingTable
						.getContainerDataSource().getItem(obj)
						.getItemProperty(MortgageItemContainer.GOLD_WEIGHT).getValue()))
						.getValue());
				diamondDiamondWeight = Double.valueOf(((DecimalTextField) (workingTable
						.getContainerDataSource().getItem(obj)
						.getItemProperty(MortgageItemContainer.DIAMOND_WEIGHT).getValue()))
						.getValue());
			}
			
			String itemName = (String) ((ComboBox) (workingTable
				.getContainerDataSource().getItem(obj)
				.getItemProperty(MortgageItemContainer.ITEM_NAME)
				.getValue())).getValue();
			itemList.add(new MortgageItemDTO(itemName, weight, diamondGoldWeight, diamondDiamondWeight, piecePair, quantity));
		}
		return itemList;
	}

	private VerticalLayout getToolbar() {
		VerticalLayout toolbarMainLayout = new VerticalLayout();
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setWidth("100%");
		toolbar.setSpacing(true);
		toolbar.setMargin(true);

		Label title = new Label("Mortgage");
		title.addStyleName("h1");
		title.setSizeUndefined();
		toolbar.addComponent(title);
		toolbar.setComponentAlignment(title, Alignment.MIDDLE_LEFT);

		Button newBillBtn = new Button("New Bill");
		newBillBtn.setSizeUndefined();
		newBillBtn.addStyleName("icon-newbill");
		newBillBtn.addStyleName("default");

		newBillBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo("/mortgage");
			}
		});
		toolbar.addComponent(newBillBtn);
		toolbar.setComponentAlignment(newBillBtn, Alignment.MIDDLE_LEFT);

		keeperListCombo = getStaffListComboBox();
		toolbar.addComponent(keeperListCombo);
		toolbar.setComponentAlignment(keeperListCombo, Alignment.MIDDLE_LEFT);

		billPopUpDate.setCaption("Billing Date");
		billPopUpDate.setImmediate(true);
		billPopUpDate.setInvalidAllowed(false);
		billPopUpDate.setLocale(new Locale("en", "IN"));
		billPopUpDate.setDateFormat(INDIAN_DATE_FORMAT);
		billPopUpDate.setTextFieldEnabled(false);
		billPopUpDate.setInputPrompt(SELECT_DATE);
		billPopUpDate.setValue(new Date());
		toolbar.addComponent(billPopUpDate);
		interestPercentage = new DecimalTextField("Interest(%)");
		interestPercentage.setValue("0.000");
		interestPercentage.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (StringUtils.isEmpty(String.valueOf(event.getProperty()
						.getValue()))) {
					interestPercentage.setValue("0.00");
				} else {
					if(NumberUtils.isNumber(interestPercentage.getValue())){
						interestPercentage.setValue(String.format("%.2f",
								Double.valueOf(interestPercentage.getValue())));
					}
					
				}

			}
		});
		amountTxtField = new DecimalTextField("Amount(INR)");
		amountTxtField.setValue("0.00");
		amountTxtField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (StringUtils.isEmpty(String.valueOf(event.getProperty()
						.getValue()))) {
					amountTxtField.setValue("0.00");
					amountInWords.setValue("<b>Amount in words: </b>"
							+ new ConvertNumberToWords(Double.valueOf("0.000"))
									.convertToWords());
				} else {
					if (NumberUtils.isNumber(amountTxtField.getValue())) {

						amountTxtField.setValue(String.format("%.2f",
								Math.round(Double.valueOf(amountTxtField
										.getValue()) * 100.0) / 100.0));
						amountInWords.setValue("<b>Amount in words: </b>"
								+ new ConvertNumberToWords(Double
										.valueOf(amountTxtField.getValue()))
										.convertToWords());
					}
				}

			}
		});
		toolbar.addComponent(interestPercentage);
		toolbar.addComponent(amountTxtField);

		toolbarMainLayout.addComponent(toolbar);
		toolbarMainLayout.addComponent(amountInWords);
		toolbarMainLayout.setSpacing(true);

		return toolbarMainLayout;
	}

	private ComboBox getStaffListComboBox() {
		keeperNameComboBox.setNullSelectionAllowed(false);
		keeperNameComboBox.setNullSelectionAllowed(false);
		keeperNameComboBox.addItem("MOM");
		keeperNameComboBox.addItem("DOLLY");
		keeperNameComboBox.addItem("A.J");
		keeperNameComboBox.addItem("G.P");
		keeperNameComboBox.addItem("B.B");
		keeperNameComboBox.addItem("KHATRI");
		keeperNameComboBox.addItem("ROHIT");
		keeperNameComboBox.setValue("MOM");
		return keeperNameComboBox;
	}

	private VerticalLayout getBillingLayout(Table table,
			MortgageItemContainer container, MortgageItemType itemType) {
		String buttonType = "Gold";
		String buttonStyle = "";
		switch (itemType) {
		case GOLD:
			buttonType = "Gold Item";
			buttonStyle = "gold-table";
			break;
		case SILVER:
			buttonType = "Silver Item";
			buttonStyle = "silver-table";
			break;
		case DIAMOND:
			buttonType = "Diamond Item";
			buttonStyle = "diamond-table";
			break;
		}
		Button newBillBtn = new Button(buttonType);
		newBillBtn.setSizeUndefined();
		newBillBtn.addStyleName("icon-newbill");
		newBillBtn.addStyleName(buttonStyle);

		newBillBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				Object itemId = container.addCustomItem(itemType);
				if(itemType == MortgageItemType.GOLD && goldTableWeightCheckBox.getValue()){
					DecimalTextField goldWeight =  (DecimalTextField) (mortgageGoldItemTable
     						.getContainerDataSource().getItem(itemId))
     						.getItemProperty(MortgageItemContainer.WEIGHT).getValue();
					goldWeight.setEnabled(false);
				}else if(itemType == MortgageItemType.SILVER && silverTableWeightCheckBox.getValue()){
					DecimalTextField silverWeight =  (DecimalTextField) (mortgageSilverItemTable
     						.getContainerDataSource().getItem(itemId)
     						.getItemProperty(MortgageItemContainer.WEIGHT)).getValue();
					silverWeight.setEnabled(false);
				}
				table.setPageLength(table.size());
				table.setImmediate(true);
				table.setColumnFooter(MortgageItemContainer.DELETE,
						String.valueOf(table.size()));
				if(MortgageItemType.DIAMOND != itemType){
					table.setColumnFooter(MortgageItemContainer.WEIGHT, String
							.format("%.3f", ((MortgageItemContainer) table
									.getContainerDataSource()).getTotalWeight()));
				}else{
					table.setColumnFooter(MortgageItemContainer.GOLD_WEIGHT, String
							.format("%.3f", ((MortgageItemContainer) table
									.getContainerDataSource()).getDiamondItemTotalGoldWeight()));
					table.setColumnFooter(MortgageItemContainer.DIAMOND_WEIGHT, String
							.format("%.2f", ((MortgageItemContainer) table
									.getContainerDataSource()).getDiamondItemTotalDiamondWeight()));
				}
				mortgageItemTableCountValueChange(itemType);
			}
		});
		HorizontalLayout totalWeightLayout = new HorizontalLayout();
		totalWeightLayout.addComponent(newBillBtn);
		totalWeightLayout.setSpacing(true);
		goldTableTotalWeightTxt.setValue("0.000");
		silverTableTotalWeightTxt.setValue("0.000");
		goldTableTotalWeightTxt.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(!NumberUtils.isNumber(goldTableTotalWeightTxt.getValue())){
					goldTableTotalWeightTxt.setValue("0.000");
				}
				
			}
		});
		silverTableTotalWeightTxt.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(!NumberUtils.isNumber(silverTableTotalWeightTxt.getValue())){
					silverTableTotalWeightTxt.setValue("0.000");
				}
			}
		});
		
		switch(itemType){
			case GOLD : 
				totalWeightLayout.addComponent(goldTableWeightCheckBox);
				totalWeightLayout.addComponent(goldTableTotalWeightTxt);
				break;
			case SILVER :
				totalWeightLayout.addComponent(silverTableWeightCheckBox);
				totalWeightLayout.addComponent(silverTableTotalWeightTxt);
				break;
		}
		totalWeightLayout.addStyleName(buttonStyle);
		VerticalLayout itemHolderLayout = new VerticalLayout();
		itemHolderLayout.setWidth("100%");
		itemHolderLayout.addComponent(totalWeightLayout);
		itemHolderLayout.addComponent(table);
		return itemHolderLayout;
	}

	private Table getMortgageBillingTable(
			MortgageItemContainer mortgageItemContainer,
			MortgageItemType itemType) {
		MortgageItemTable mortgageItemTable = new MortgageItemTable(
				mortgageItemContainer, itemType);
		mortgageItemTableCountValueChange(itemType);
		mortgageItemTable.setPageLength(mortgageItemTable.size());
		mortgageItemTable.setColumnFooter(MortgageItemContainer.DELETE, ""
				+ mortgageItemTable.size());
		if(itemType != MortgageItemType.DIAMOND){
			mortgageItemTable.setColumnFooter(MortgageItemContainer.WEIGHT, String
					.format("%.3f", ((MortgageItemContainer) mortgageItemTable
							.getContainerDataSource()).getTotalWeight()));
		}else{
			mortgageItemTable.setColumnFooter(MortgageItemContainer.GOLD_WEIGHT, String
					.format("%.3f", ((MortgageItemContainer) mortgageItemTable
							.getContainerDataSource()).getDiamondItemTotalGoldWeight()));
			mortgageItemTable.setColumnFooter(MortgageItemContainer.DIAMOND_WEIGHT, String
					.format("%.2f", ((MortgageItemContainer) mortgageItemTable
							.getContainerDataSource()).getDiamondItemTotalDiamondWeight()));
		}
		
		mortgageItemTable.setImmediate(true);
		return mortgageItemTable;
	}

	private void mortgageItemTableCountValueChange(MortgageItemType itemType) {
		Collection<?> itemIdsList = null;
		workingTable = getWorkingTable(itemType);
		itemIdsList = workingTable.getItemIds();

		for (Object itemId : itemIdsList) {
			Image deleteImage = (Image) ((workingTable.getItem(itemId)
					.getItemProperty(MortgageItemContainer.DELETE).getValue()));
			deleteImage.addClickListener(new MouseEvents.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					MortgageItemType itemType = (MortgageItemType)(((Image) event.getComponent()).getData());
					workingTable = getWorkingTable(itemType);
					workingTable.setPageLength(workingTable.size());
					workingTable.setColumnFooter(MortgageItemContainer.DELETE,
							"" + workingTable.size());
					if(MortgageItemType.DIAMOND != itemType){
						workingTable.setColumnFooter(MortgageItemContainer.WEIGHT,
								String.format("%.3f",
										((MortgageItemContainer) workingTable
												.getContainerDataSource())
												.getTotalWeight()));
					}else{
						workingTable.setColumnFooter(MortgageItemContainer.GOLD_WEIGHT,
								String.format("%.3f",
										((MortgageItemContainer) workingTable
												.getContainerDataSource())
												.getDiamondItemTotalGoldWeight()));
						workingTable.setColumnFooter(MortgageItemContainer.DIAMOND_WEIGHT,
								String.format("%.2f",
										((MortgageItemContainer) workingTable
												.getContainerDataSource())
												.getDiamondItemTotalDiamondWeight()));
					}
					
					workingTable.setImmediate(true);
				}
			});

			handleWeightValueChange(itemId, itemType);
		}

	}

	private void handleWeightValueChange(Object itemId, MortgageItemType itemType) {
		DecimalTextField weightTxtField = new DecimalTextField();
		if(MortgageItemType.DIAMOND != itemType){
			weightTxtField = (DecimalTextField) ((MortgageItemContainer) workingTable
					.getContainerDataSource()).getItem(itemId)
					.getItemProperty(MortgageItemContainer.WEIGHT).getValue();
			setWeightValueChangeListener(weightTxtField);
		}else{
			weightTxtField = (DecimalTextField) ((MortgageItemContainer) workingTable
					.getContainerDataSource()).getItem(itemId)
					.getItemProperty(MortgageItemContainer.GOLD_WEIGHT).getValue();
			setWeightValueChangeListener(weightTxtField);
			
			weightTxtField = (DecimalTextField) ((MortgageItemContainer) workingTable
					.getContainerDataSource()).getItem(itemId)
					.getItemProperty(MortgageItemContainer.DIAMOND_WEIGHT).getValue();
			setWeightValueChangeListener(weightTxtField);
		}

		
	}

	private void setWeightValueChangeListener(TextField weightTxtField) {
		weightTxtField.setImmediate(true);
		weightTxtField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				MortgageItemType itemType = (MortgageItemType) ((TextField) event.getProperty()).getData();
				workingTable = getWorkingTable(itemType);
				
				if(MortgageItemType.DIAMOND != itemType){
					workingTable.setColumnFooter(MortgageItemContainer.WEIGHT,
							String.format("%.3f",
									((MortgageItemContainer) workingTable
											.getContainerDataSource())
											.getTotalWeight()));
				}else{
					workingTable.setColumnFooter(MortgageItemContainer.GOLD_WEIGHT,
							String.format("%.3f",
									((MortgageItemContainer) workingTable
											.getContainerDataSource())
											.getDiamondItemTotalGoldWeight()));
					workingTable.setColumnFooter(MortgageItemContainer.DIAMOND_WEIGHT,
							String.format("%.2f",
									((MortgageItemContainer) workingTable
											.getContainerDataSource())
											.getDiamondItemTotalDiamondWeight()));
				}
			}
		});
	}

	private Table getWorkingTable(MortgageItemType itemType) {
		switch (itemType) {
		case GOLD:
			workingTable = mortgageGoldItemTable;
			break;
		case SILVER:
			workingTable = mortgageSilverItemTable;
			break;
		case DIAMOND:
			workingTable = mortgageDiamondItemTable;
			break;
		}
		return workingTable;
	}
	
	private String checkToSaveTransaction(MortgageTransactionDTO transDto){
		String transValidationMessage = "";
		if(transDto.getGoldItemList().size() == 0 && transDto.getSilverItemList().size() == 0 && transDto.getDiamondItemList().size() ==0){
			transValidationMessage = "There are no items added";
			return transValidationMessage;
		}
		
		if(NumberUtils.isNumber(String.valueOf(transDto.getInterestRate()))){
			if(transDto.getInterestRate() <= 0.000){
				transValidationMessage = "Please check the interest rate";
			}
			
		}else{
			transValidationMessage = "Please check the interest rate";
		}
		if(NumberUtils.isNumber(String.valueOf(transDto.getAmount()))){
			if(transDto.getAmount() <= 0.000){
				transValidationMessage = "Please check the mortgage amount";
			}
			
		}else{
			transValidationMessage = "Please check the mortgage amount";
		}
		transValidationMessage = validateItemList(transDto, MortgageItemType.GOLD, transValidationMessage);
		transValidationMessage = validateItemList(transDto, MortgageItemType.SILVER, transValidationMessage);
		transValidationMessage = validateItemList(transDto, MortgageItemType.DIAMOND, transValidationMessage);
		return transValidationMessage;
	}

	private String validateItemList(MortgageTransactionDTO transDto, MortgageItemType itemType, String transValidationMessage) {
		List <MortgageItemDTO> itemList = new ArrayList<MortgageItemDTO>();
		String tableName = "";
		boolean totalWeightEnabled = false;
		switch(itemType){
			case GOLD : itemList = transDto.getGoldItemList();
					tableName = "gold";
					if(transDto.getGoldItemList().size() > 0 && goldTableWeightCheckBox.getValue()){
						totalWeightEnabled = true;
						if(Double.valueOf(goldTableTotalWeightTxt.getValue()) <= 0.000){
							transValidationMessage = "Total Gold weight is not valid";
							return transValidationMessage;
						}
						
					}else if(transDto.getGoldItemList().size() == 0 && Double.valueOf(goldTableTotalWeightTxt.getValue()) > 0.000){
						transValidationMessage = "Total Gold weight should be zero if no gold items are present";
						return transValidationMessage;
					}
					
				break;
			case SILVER : itemList = transDto.getSilverItemList();
					tableName = "silver";
					if(transDto.getSilverItemList().size() > 0  && silverTableWeightCheckBox.getValue()){
						totalWeightEnabled = true;
						if(Double.valueOf(silverTableTotalWeightTxt.getValue()) <= 0.000){
							transValidationMessage = "Total Silver weight is not valid";
						}
						return transValidationMessage;
					}else if(transDto.getSilverItemList().size() == 0 && Double.valueOf(silverTableTotalWeightTxt.getValue()) > 0.000){
						transValidationMessage = "Total Silver weight should be zero if no silvers items are present";
						return transValidationMessage;
					}
					break;
			case DIAMOND : itemList = transDto.getDiamondItemList();
					tableName = "diamond";
					break;
		}
		for(MortgageItemDTO mdto : itemList){
			transValidationMessage = validateItemQuantity(transValidationMessage, tableName, mdto, itemType);
			if(!StringUtils.isEmpty(transValidationMessage)){
				return transValidationMessage;
			}
			if(!totalWeightEnabled){
				transValidationMessage = validateWeight(transValidationMessage, tableName, mdto, itemType);
			}
		}
		return transValidationMessage;
	}

	private String validateItemQuantity(String transValidationMessage, String tableName, MortgageItemDTO mdto, MortgageItemType itemType){
		if(NumberUtils.isNumber(String.valueOf(mdto.getQuantity()))){
			if(mdto.getQuantity() <= 0.000){
				transValidationMessage = "Please check the quantity in " + tableName + " items";
			}
			
		}else{
			transValidationMessage = "Please check the quantity in " + tableName + " items";
		}
		return transValidationMessage;
	}
	
	private String validateWeight(String transValidationMessage, String tableName, MortgageItemDTO mdto, MortgageItemType itemType) {
		if(MortgageItemType.DIAMOND != itemType){
			Double weight = mdto.getWeight();
			if(NumberUtils.isNumber(String.valueOf(weight)) && weight <= 0.000){
				transValidationMessage = "Please check the weight of " + tableName + " items";
			}
			if(StringUtils.isEmpty(mdto.getItemName())){
					transValidationMessage = "Please check the item names in " + tableName + " item table";
			}
		}else{
			Double weight = mdto.getDiamondGoldWeight();
			if(NumberUtils.isNumber(String.valueOf(weight)) && weight <= 0.000){
				transValidationMessage = "Please check the gold weight in " + tableName + " items";
			}
			weight = mdto.getDiamondDiamondWeight();
			if(NumberUtils.isNumber(String.valueOf(weight)) && weight <= 0.000){
				transValidationMessage = "Please check the diamond weight in " + tableName + " items";
			}
			if(StringUtils.isEmpty(mdto.getItemName())){
					transValidationMessage = "Please check the item names in " + tableName + " item table";
			} 
		}
		return transValidationMessage;
	}
}
