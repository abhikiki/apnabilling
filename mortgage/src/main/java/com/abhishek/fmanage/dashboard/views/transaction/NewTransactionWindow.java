/**
 * 
 */
package com.abhishek.fmanage.dashboard.views.transaction;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.abhishek.fmanage.mortgage.data.bean.MortgageItem;
import com.abhishek.fmanage.mortgage.data.bean.MortgageTransaction;
import com.abhishek.fmanage.mortgage.data.container.MortgageItemViewContainer;
import com.abhishek.fmanage.retail.dto.CustomerDTO;
import com.avathartech.fastformfields.widgets.DecimalTextField;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * This class represent new transaction window
 * 
 * @author Abhishek Gupta
 *
 */
public class NewTransactionWindow extends Window
{
    private static final long serialVersionUID = 1L;
    private static final String TOOLTIP = "Add Item";
    private PopupDateField transactionDate = new PopupDateField();
    private DecimalTextField interestRate = new DecimalTextField("Interest Rate(%)");
    private DecimalTextField amountLended = new DecimalTextField("Amount Lended(INR)");
    private DecimalTextField totalWeight = new DecimalTextField("Total Weight");
    private Table itemTable = new Table();
    private final FormLayout layout = new FormLayout();
    private final CheckBox totalWeightCheckBox = new CheckBox("By Total Weight");

    public NewTransactionWindow(MortgageTransaction mTrans, boolean isNew)
    {
        super("Transaction Detail");
        center();
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setWidth("90%");
        setHeight("90%");
        setClosable(false);
        addStyleName("no-vertical-drag-hints");
        addStyleName("no-horizontal-drag-hints");
        setModal(true);

        transactionDate.setCaption("Transaction Date");
        transactionDate.setImmediate(true);
        transactionDate.setInvalidAllowed(false);
        transactionDate.setLocale(new Locale("en", "IN"));
        transactionDate.setDateFormat(MortgageTransactionView.INDIAN_DATE_FORMAT);
        transactionDate.setTextFieldEnabled(false);
        transactionDate.setInputPrompt(MortgageTransactionView.SELECT_DATE);
        transactionDate.setValue(new java.util.Date());

        HorizontalLayout topHorizontalLayout = new HorizontalLayout();
        
        VerticalLayout vL = new VerticalLayout();
        HorizontalLayout hL = new HorizontalLayout();
        hL.addComponent(transactionDate);
        hL.addComponent(interestRate);
        hL.addComponent(amountLended);
        hL.addComponent(totalWeight);
        
        interestRate.setValue("4.0");
        amountLended.setValue("0.0");
        vL.addComponent(hL);
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName("footer");
        footer.setWidth("100%");
        footer.setMargin(true);

        Button ok = new Button("Cancel");
        ok.addStyleName("wide");
        ok.addStyleName("default");
        ok.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

        final BeanFieldGroup<CustomerDTO> binder =
           new BeanFieldGroup<CustomerDTO>(CustomerDTO.class);
        binder.setItemDataSource(new CustomerDTO());
        binder.setBuffered(false);
        
        
        layout.setEnabled(true);
        layout.addComponent(binder.buildAndBind("FirstName", "firstName"));
        layout.addComponent(binder.buildAndBind("LastName", "lastName"));
        layout.addComponent(binder.buildAndBind("ContactNumber", "contactNumber"));
        layout.addComponent(binder.buildAndBind("EmailId", "emailId"));
        layout.addComponent(binder.buildAndBind("Street Address1", "streetAddress1"));
        layout.addComponent(binder.buildAndBind("Street Address2", "streetAddress2"));
        layout.addComponent(binder.buildAndBind("City", "city"));
        layout.addComponent(binder.buildAndBind("State", "stateprovince"));
        layout.addComponent(binder.buildAndBind("Zipcode", "zipcode"));
        layout.addComponent(binder.buildAndBind("Country", "country"));
        layout.setImmediate(true);
        layout.setData(binder);
        itemTable.setSizeFull();
        itemTable.addStyleName("borderless");
        itemTable.setSelectable(false);
        itemTable.setColumnCollapsingAllowed(false);
        itemTable.setColumnReorderingAllowed(true);
        
        
        itemTable.setFooterVisible(true);

        itemTable.setMultiSelect(false);
        itemTable.setSelectable(false);
        itemTable.setPageLength(5);
        final MortgageItemViewContainer mItemViewContainer = new MortgageItemViewContainer();
       
        itemTable.setContainerDataSource(mItemViewContainer);
        itemTable.setVisibleColumns(new Object[] {
            "Delete",
            "Item Name",
            "Item Weight",
            "Item Description",
            });
        itemTable.setFooterVisible(true);
        itemTable.setColumnFooter("Delete", "Total Items: " + String.valueOf(itemTable.size()));
        itemTable.setColumnFooter("Item Weight", "Total Weight: " + "100");
        final CheckBox switchEditable = new CheckBox("Editable");
        switchEditable.addValueChangeListener(
                new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					public void valueChange(ValueChangeEvent event) {
						itemTable.setEditable(((Boolean)event.getProperty().getValue()).booleanValue());
						itemTable.setImmediate(true);
						itemTable.setEnabled(((Boolean)event.getProperty().getValue()).booleanValue());
            }
        });
        
        if(!isNew)
        {
            populateComponentData(mTrans, mItemViewContainer);
        }
        else
        {
            mItemViewContainer.addMortgageItem(totalWeightCheckBox.getValue(),"","0.0","");
        }
        ThemeResource resource = new ThemeResource("img/addButtonSmall.jpg");
        Image image = new Image("", resource);
        image.setHeight("40px");
        image.setWidth("40px");
        image.setDescription(TOOLTIP);
        image.addClickListener(new MouseEvents.ClickListener()
        {
			private static final long serialVersionUID = 1L;

			@Override
            public void click(com.vaadin.event.MouseEvents.ClickEvent event)
            {
                mItemViewContainer.addMortgageItem(totalWeightCheckBox.getValue(),"","0.0","");
            }
        });
        topHorizontalLayout.setSizeFull();
        
        vL.addComponent(image);
        vL.addComponent(itemTable);
        vL.setWidth("90%");
        vL.setHeight("90%");
        Panel pL = new Panel();
        VerticalLayout mainLayout = new VerticalLayout();
      
        Button saveBtn = new Button("Save");
        saveBtn.addStyleName("wide");
        saveBtn.addStyleName("default");
        saveBtn.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event)
            {
                Component c = layout.getComponent(0);
                ((TextField)c).setRequired(true);
                ((TextField)c).setImmediate(true);
                Notification.show("Not implemented: " +  ((TextField)c).getValue());
            }
        });

        totalWeightCheckBox.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 1L;

			@Override
            public void valueChange(ValueChangeEvent event)
            {
                totalWeight.setEnabled(totalWeightCheckBox.getValue());
                if(totalWeightCheckBox.getValue())
                {
                    List<?> li =   mItemViewContainer.getItemIds();
                    for(int i =0 ; i < li.size() ; i++)
                    {
                       
                        DecimalTextField itemWeight = (DecimalTextField) mItemViewContainer.getItem(li.get(i)).getItemProperty("Item Weight").getValue();
                        itemWeight.setEnabled(false);
                    }
                }
                else
                {
                    List<?> li =   mItemViewContainer.getItemIds();
                    for(int i =0 ; i < li.size() ; i++)
                    {
                       
                        DecimalTextField itemWeight = (DecimalTextField) mItemViewContainer.getItem(li.get(i)).getItemProperty("Item Weight").getValue();
                        itemWeight.setEnabled(true);
                    }
                }
            }
        });




        HorizontalLayout saveCancelLayout = new HorizontalLayout();
        saveCancelLayout.addComponent(saveBtn);
        saveCancelLayout.addComponent(ok);
        saveCancelLayout.setComponentAlignment(saveBtn, Alignment.BOTTOM_LEFT);
        saveCancelLayout.setComponentAlignment(ok, Alignment.BOTTOM_RIGHT);
        saveCancelLayout.setExpandRatio(saveBtn, 1);
        saveCancelLayout.setExpandRatio(ok, 1);

        vL.addComponent(saveCancelLayout);
        topHorizontalLayout.addComponent(layout);
        topHorizontalLayout.addComponent(vL);
        topHorizontalLayout.addComponent(switchEditable);
        topHorizontalLayout.addComponent(totalWeightCheckBox);
        topHorizontalLayout.setComponentAlignment(vL, Alignment.MIDDLE_CENTER);
        topHorizontalLayout.setExpandRatio(layout, 1);
        topHorizontalLayout.setExpandRatio(vL, 3);
        mainLayout.addComponent(topHorizontalLayout);
        pL.setContent(mainLayout);
        setContent(pL);
        
    }

    private void populateComponentData(MortgageTransaction mTrans, MortgageItemViewContainer mItemViewContainer)
    {
        @SuppressWarnings("unchecked")
		BeanFieldGroup<CustomerDTO> binder = (BeanFieldGroup<CustomerDTO>) layout.getData();
        binder.setItemDataSource(mTrans.getCustomer());
       
        transactionDate.setValue(new Date(989));
        interestRate.setValue(String.valueOf(mTrans.getInterestRateAssignedToCustomer()));
        amountLended.setValue(String.valueOf(mTrans.getAmountPaidToCustomer()));
        List<MortgageItem> mItemList = mTrans.getItemList();
        for(MortgageItem mItem : mItemList)
        {
            mItemViewContainer.addMortgageItem(false, mItem.getItemName(), String.valueOf(mItem.getItemWeight()), mItem.getItemDescription());
        }
    }
}
