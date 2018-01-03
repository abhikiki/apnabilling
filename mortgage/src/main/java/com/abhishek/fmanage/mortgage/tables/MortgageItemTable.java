package com.abhishek.fmanage.mortgage.tables;

import com.abhishek.fmanage.mortgage.data.container.MortgageItemContainer;
import com.abhishek.fmanage.mortgage.data.container.MortgageItemType;
import com.vaadin.ui.Table;

public class MortgageItemTable extends Table{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public MortgageItemTable(MortgageItemContainer mortgageItemContainer, MortgageItemType itemType){
		setSizeFull();
		addStyleName("borderless");
		setSelectable(false);
		setColumnCollapsingAllowed(false);
		setColumnReorderingAllowed(true);
		setWidth("80%");
		setContainerDataSource(mortgageItemContainer);
		switch(itemType){
			case GOLD :
				addStyleName("gold-table");
				addStyleName("gold-table-header");
				addStyleName("gold-table-footer");
				addStyleName("gold-table-footer-container");
				break;
			case SILVER : 
				addStyleName("silver-table");
				addStyleName("silver-table-header");
				addStyleName("silver-table-footer");
				addStyleName("silver-table-footer-container");
				break;
			case DIAMOND :
				addStyleName("diamond-table");
				addStyleName("diamond-table-header");
				addStyleName("diamond-table-footer");
				addStyleName("diamond-table-footer-container");
				break;
				
		}
		
		setFooterVisible(true);
		setMultiSelect(false);
		setPageLength(0);
		setColumnWidth(MortgageItemContainer.DELETE, 70);
		setColumnWidth(MortgageItemContainer.ITEM_NAME, 180);
		setColumnWidth(MortgageItemContainer.WEIGHT, 180);
		setColumnFooter(MortgageItemContainer.DELETE, ("Items=" + size()));
	}
}
