package com.abhishek.fmanage.retail.tables;

import com.abhishek.fmanage.retail.data.container.DiamondItemContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class DiamondItemTable extends Table{

	/**
	 * 
	 */
	private static final long serialVersionUID = -268387811582083216L;

	public DiamondItemTable(DiamondItemContainer diamondItemContainer){
		setSizeFull();
		addStyleName("borderless");
		setSelectable(false);
		setColumnCollapsingAllowed(false);
		setColumnReorderingAllowed(true);
		setWidth("95%");
		addStyleName("diamond-table");
		addStyleName("diamond-table-header");
		addStyleName("diamond-table-footer");
		addStyleName("diamond-table-footer-container");
		setContainerDataSource(diamondItemContainer);
		setVisibleColumns(new Object[] {
				DiamondItemContainer.DELETE, DiamondItemContainer.ITEM_NAME,
				DiamondItemContainer.QUANTITY, DiamondItemContainer.PIECE_PAIR,
				DiamondItemContainer.GOLD_WEIGHT,
				DiamondItemContainer.DIAMOND_WEIGHT,
				DiamondItemContainer.DIAMOND_PIECE,
				DiamondItemContainer.CERTIFICATE, DiamondItemContainer.PRICE });
		setFooterVisible(true);
		setMultiSelect(false);
		setPageLength(0);
		setColumnWidth(DiamondItemContainer.ITEM_NAME, 180);
		setColumnWidth(DiamondItemContainer.QUANTITY, 60);
		setColumnWidth(DiamondItemContainer.GOLD_WEIGHT, 90);
		setColumnWidth(DiamondItemContainer.DIAMOND_WEIGHT,
				125);
		String totalFormattedDiamondPrice = String.format("%.3f", diamondItemContainer.getTotalPrice());
		setColumnFooter(DiamondItemContainer.PRICE, totalFormattedDiamondPrice);
		setColumnFooter(DiamondItemContainer.GOLD_WEIGHT, String.format("%.3f", diamondItemContainer.getTotalGoldWeight()));
		setColumnFooter(DiamondItemContainer.DIAMOND_WEIGHT, String.format("%.2f", diamondItemContainer.getTotalDiamondWeight()));
		setColumnFooter(DiamondItemContainer.DELETE, "Items=" + size());
		for (Object obj : getItemIds()) {
			TextField itemTxtField = (TextField) (diamondItemContainer.getItem(
					obj).getItemProperty(DiamondItemContainer.PRICE).getValue());
			itemTxtField.setImmediate(true);
			itemTxtField.addValueChangeListener(new ValueChangeListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(
						com.vaadin.data.Property.ValueChangeEvent event) {
					// String totalFormattedDiamondPrice = String.format("%.3f", diamondItemContainer.getTotalPrice());
					setColumnFooter(DiamondItemContainer.PRICE, totalFormattedDiamondPrice);
					
				}
			});
		}
	}
	
}
