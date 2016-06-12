package com.abhishek.fmanage.retail.tables;

import com.abhishek.fmanage.retail.data.container.GoldItemContainer;
import com.vaadin.ui.Table;

public class GoldItemTable extends Table{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3841455625215858648L;

	public GoldItemTable(GoldItemContainer goldItemContainer){
		setSizeFull();
		addStyleName("borderless");
		setSelectable(false);
		setColumnCollapsingAllowed(false);
		setColumnReorderingAllowed(true);
		setWidth("95%");
		setContainerDataSource(goldItemContainer);
		addStyleName("gold-table");
		addStyleName("gold-table-header");
		addStyleName("gold-table-footer");
		addStyleName("gold-table-footer-container");
		setVisibleColumns(new Object[] {
				GoldItemContainer.DELETE, GoldItemContainer.GOLD_TYPE,
				GoldItemContainer.ITEM_NAME, GoldItemContainer.QUANTITY,
				GoldItemContainer.PIECE_PAIR, GoldItemContainer.WEIGHT,
				GoldItemContainer.MAKING_CHARGE,
				GoldItemContainer.MAKING_CHARGE_TYPE,
				GoldItemContainer.GOLD_RATE, GoldItemContainer.PRICE });
		setFooterVisible(true);
		setMultiSelect(false);
		setPageLength(0);
		setColumnWidth(GoldItemContainer.DELETE, 70);
		setColumnWidth(GoldItemContainer.ITEM_NAME, 180);
		setColumnWidth(GoldItemContainer.GOLD_TYPE, 80);
		setColumnWidth(GoldItemContainer.QUANTITY, 60);
		setColumnWidth(GoldItemContainer.WEIGHT, 60);
		setColumnWidth(GoldItemContainer.MAKING_CHARGE, 95);
		setColumnWidth(GoldItemContainer.GOLD_RATE, 110);
		setColumnWidth(GoldItemContainer.MAKING_CHARGE_TYPE, 100);
		setColumnWidth(GoldItemContainer.PIECE_PAIR, 100);
		setColumnWidth(GoldItemContainer.PRICE, 120);
		String totalFormattedGoldPrice = String.format("%.3f", goldItemContainer.getTotalPrice());
		setColumnFooter(GoldItemContainer.PRICE, totalFormattedGoldPrice);
		setColumnFooter(GoldItemContainer.WEIGHT, String.format("%.3f", goldItemContainer.getTotalWeight()));
		setColumnFooter(GoldItemContainer.DELETE, ("Items=" + size()));
	}
}
