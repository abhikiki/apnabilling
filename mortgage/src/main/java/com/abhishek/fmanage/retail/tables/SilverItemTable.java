package com.abhishek.fmanage.retail.tables;

import com.abhishek.fmanage.retail.data.container.SilverItemContainer;
import com.vaadin.ui.Table;

public class SilverItemTable extends Table{



	private static final long serialVersionUID = 4051848879450628949L;

	public SilverItemTable(SilverItemContainer silverItemContainer){
		setSizeFull();
		addStyleName("borderless");
		setSelectable(false);
		setColumnCollapsingAllowed(false);
		setColumnReorderingAllowed(true);
		setWidth("95%");
		// silverItemContainer.addCustomItem();
		addStyleName("silver-table");
		addStyleName("silver-table-header");
		addStyleName("silver-table-footer");
		addStyleName("silver-table-footer-container");
		setContainerDataSource(silverItemContainer);
		setVisibleColumns(new Object[] {
				SilverItemContainer.DELETE, SilverItemContainer.ITEM_NAME,
				SilverItemContainer.QUANTITY, SilverItemContainer.PIECE_PAIR,
				SilverItemContainer.WEIGHT, SilverItemContainer.MAKING_CHARGE,
				SilverItemContainer.MAKING_CHARGE_TYPE,
				SilverItemContainer.SILVER_RATE, SilverItemContainer.PRICE });
		setFooterVisible(true);
		setMultiSelect(false);
		setPageLength(0);
		setColumnWidth(SilverItemContainer.DELETE, 70);
		setColumnWidth(SilverItemContainer.ITEM_NAME, 180);
		setColumnWidth(SilverItemContainer.QUANTITY, 60);
		setColumnWidth(SilverItemContainer.WEIGHT, 70);
		setColumnWidth(SilverItemContainer.PIECE_PAIR, 100);
		setColumnWidth(SilverItemContainer.SILVER_RATE, 125);
		setColumnWidth(SilverItemContainer.MAKING_CHARGE, 95);
		setColumnWidth(SilverItemContainer.MAKING_CHARGE_TYPE,
				105);
		String totalFormattedSilverPrice = String.format("%.2f", silverItemContainer.getTotalPrice());
		setColumnFooter(SilverItemContainer.PRICE, totalFormattedSilverPrice);
		setColumnFooter(SilverItemContainer.WEIGHT, String.format("%.3f", silverItemContainer.getTotalWeight()));
		setColumnFooter(SilverItemContainer.DELETE, ("Items=" + size()));
	}
}
