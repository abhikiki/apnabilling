package com.abhishek.fmanage.retail.tables;

import com.abhishek.fmanage.retail.data.container.GeneralItemContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class GeneralItemTable extends Table{


	/**
	 * 
	 */
	private static final long serialVersionUID = -2472188848731339751L;


	public GeneralItemTable(GeneralItemContainer generalItemContainer){
		setSizeFull();
		addStyleName("borderless");
		setSelectable(false);
		setColumnCollapsingAllowed(false);
		setColumnReorderingAllowed(true);
		setWidth("95%");
		addStyleName("general-table");
		addStyleName("general-table-header");
		addStyleName("general-table-footer");
		addStyleName("general-table-footer-container");
		setContainerDataSource(generalItemContainer);
		setVisibleColumns(new Object[] {
				GeneralItemContainer.DELETE, GeneralItemContainer.ITEM_NAME,
				GeneralItemContainer.QUANTITY, GeneralItemContainer.PIECE_PAIR,
				GeneralItemContainer.WEIGHT,
				GeneralItemContainer.PRICE_PER_PIECE_PAIR,
				GeneralItemContainer.PRICE });
		setFooterVisible(true);
		setMultiSelect(false);
		setPageLength(0);
		setColumnWidth(GeneralItemContainer.ITEM_NAME, 180);
		setColumnWidth(GeneralItemContainer.QUANTITY, 60);
		setColumnWidth(GeneralItemContainer.WEIGHT, 90);
		String totalFormattedDiamondPrice = String.format("%.3f", generalItemContainer.getTotalPrice());
		setColumnFooter(GeneralItemContainer.PRICE, totalFormattedDiamondPrice);
		setColumnFooter(GeneralItemContainer.DELETE, "Items=" + size());
		for (Object obj : getItemIds()) {
			TextField itemTxtField = (TextField) (generalItemContainer.getItem(
					obj).getItemProperty(GeneralItemContainer.PRICE).getValue());
			itemTxtField.setImmediate(true);
			itemTxtField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(
						com.vaadin.data.Property.ValueChangeEvent event) {
					String totalFormattedDiamondPrice = String.format("%.3f", generalItemContainer.getTotalPrice());
					setColumnFooter(GeneralItemContainer.PRICE, totalFormattedDiamondPrice);
				}
			});
		}
	}
}
