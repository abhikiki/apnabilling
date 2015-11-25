/**
 * 
 */
package com.abhishek.fmanage.retail.data.container;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.abhishek.fmanage.csv.utility.CustomShopSettingFileUtility;
import com.abhishek.fmanage.mortgage.data.container.CustomItemContainerInterface;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;

/**
 * @author GUPTAA6
 *
 */
public class GeneralItemContainer extends IndexedContainer implements CustomItemContainerInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2342899758697901330L;
	public static final String DELETE = "Delete";
	public static final String ITEM_NAME = "ItemName";
	public static final String QUANTITY = "Quantity";
	public static final String PIECE_PAIR = "PiecePair";
	public static final String WEIGHT = "Wt(gms)";
	public static final String PRICE_PER_PIECE_PAIR = "Price Per(Piece/Pair)";
	public static final String PRICE = "Price(INR)";
	
	public static final ThemeResource removeItemImageResource = new ThemeResource("img/removeButtonSmall.jpg");

	public GeneralItemContainer() {
		addContainerProperty(DELETE, Image.class, new Image());
		addContainerProperty(ITEM_NAME, ComboBox.class, new ComboBox());
		addContainerProperty(QUANTITY, TextField.class, new TextField());
		addContainerProperty(PIECE_PAIR, ComboBox.class, new ComboBox());
		addContainerProperty(WEIGHT, TextField.class, new TextField());
		addContainerProperty(PRICE_PER_PIECE_PAIR, TextField.class,  new TextField());
		addContainerProperty(PRICE, TextField.class, new TextField());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addCustomItem() {
		Object goldItemRowId = addItem();
		Item item = getItem(goldItemRowId);
		if (item != null) {
			item.getItemProperty(DELETE).setValue(getRemoveItemImage(goldItemRowId));
			item.getItemProperty(ITEM_NAME).setValue(getItemNameList(goldItemRowId));
			item.getItemProperty(QUANTITY).setValue(getQuantity(goldItemRowId));
			item.getItemProperty(PIECE_PAIR).setValue(getPiecePair(goldItemRowId));
			item.getItemProperty(WEIGHT).setValue(getWeight(goldItemRowId));
			item.getItemProperty(PRICE_PER_PIECE_PAIR).setValue(getPricePerPiece(goldItemRowId));
			item.getItemProperty(PRICE).setValue(getPrice());
		}
	}

	private Object getPrice() {
		TextField itemPrice = new TextField();
		itemPrice.setImmediate(true);
		itemPrice.setRequired(true);
		itemPrice.setValidationVisible(true);
		itemPrice.setWidth("100%");
		itemPrice.setEnabled(false);
		itemPrice.setStyleName("my-disabled");
		itemPrice.addValidator(new DoubleRangeValidator("Must be number and > 0", 0.0001, null));
		itemPrice.addValidator((value) -> {
		    if (!NumberUtils.isNumber(String.valueOf(value))
			    	|| (NumberUtils.isNumber(String.valueOf(value)) 
			    	&& (NumberUtils.toDouble(String.valueOf(value)) <= 0.0))){
		    	itemPrice.addStyleName("v-textfield-fail");
			}else{
				itemPrice.removeStyleName("v-textfield-fail");
			}
		});
		return itemPrice;
	}
	
	private Object getPricePerPiece(Object currentItemId) {
		TextField pricePerPiece = new TextField();
		pricePerPiece.setImmediate(true);
		pricePerPiece.setRequired(true);
		pricePerPiece.setValidationVisible(true);
		pricePerPiece.setWidth("100%");
		pricePerPiece.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		pricePerPiece.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 516109943650543255L;

			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (StringUtils.isEmpty(value)) {
					pricePerPiece.addStyleName("v-textfield-fail");
					TextField goldPriceTextField = (TextField) getItem(currentItemId).getItemProperty(PRICE).getValue();
					goldPriceTextField.setValue("");
					goldPriceTextField.removeStyleName("v-textfield-success");
				}
			}
		});
		pricePerPiece.addValidator(new DoubleRangeValidator("Must be number and > 0", 0.0001, null));
		pricePerPiece.addValidator((value) -> {
			if (!NumberUtils.isNumber(String.valueOf(value))
					|| (NumberUtils.isNumber(String.valueOf(value))
						&& (NumberUtils.toDouble(String.valueOf(value)) <= 0.0))) {
				pricePerPiece.addStyleName("v-textfield-fail");
			} else {
				pricePerPiece.setComponentError(null);
				pricePerPiece.removeStyleName("v-textfield-fail");
			}
		});
		return pricePerPiece;
	}

	private Object getWeight(Object currentItemId) {
		TextField weight = new TextField();
		weight.setImmediate(true);
		weight.setRequired(true);
		weight.setValidationVisible(true);
		weight.setWidth("100%");
		weight.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		weight.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 516109943650543255L;

			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (!StringUtils.isEmpty(value) && !NumberUtils.isNumber(value)) {
					weight.addStyleName("v-textfield-fail");
					TextField generalPriceTextField = (TextField) getItem(currentItemId).getItemProperty(PRICE).getValue();
					generalPriceTextField.setValue("");
					generalPriceTextField.removeStyleName("v-textfield-success");
				}else{
				weight.addStyleName("v-textfield-success");
			}
		}
		});
		return weight;
	}

	private Object getPiecePair(Object currentItemId) {
		ComboBox itemName = new ComboBox();
		itemName.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		itemName.addItem("Piece");
		itemName.addItem("Pair");
		itemName.setValue("Piece");
		itemName.setWidth("100%");
		return itemName;
	}

	private TextField getQuantity(final Object currentItemId) {
		TextField quantity = new TextField();
		quantity.setImmediate(true);
		quantity.setRequired(true);
		quantity.setValidationVisible(true);
		quantity.setWidth("90%");
		quantity.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		quantity.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 4708857967116319893L;
			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (StringUtils.isEmpty(value)) {
					quantity.addStyleName("v-textfield-fail");
					TextField generalPriceTextField = (TextField) getItem(currentItemId).getItemProperty(PRICE).getValue();
					generalPriceTextField.setValue("");
					generalPriceTextField.removeStyleName("v-textfield-success");
				}
			}
		});
		quantity.addValidator(new DoubleRangeValidator("Must be number and > 0", 1.0, null));
		quantity.addValidator((value) -> {
			if (!NumberUtils.isDigits(String.valueOf(value))
					|| (NumberUtils.isDigits(String.valueOf(value)) 
							&& (NumberUtils.toInt(String.valueOf(value)) <= 0))) {
				quantity.addStyleName("v-textfield-fail");
			} else {
				quantity.setComponentError(null);
				quantity.removeStyleName("v-textfield-fail");
			}
		});
		return quantity;
	}
	
	private ValueChangeListener getCustomValueChangeListener(final Object currentItemId) {
		return new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				ComboBox itemNameField = (ComboBox) getItem(currentItemId).getItemProperty(ITEM_NAME).getValue();
				ComboBox piecePairField = (ComboBox) getItem(currentItemId).getItemProperty(PIECE_PAIR).getValue();
				TextField quantityTxtField = (TextField) getItem(currentItemId).getItemProperty(QUANTITY).getValue();
				TextField weightTxtField = (TextField) getItem(currentItemId).getItemProperty(WEIGHT).getValue();
				TextField genralItemPriceTxtField = (TextField) getItem(currentItemId).getItemProperty(PRICE).getValue();
				TextField genralItemPricePerPieceTxtField = (TextField) getItem(currentItemId).getItemProperty(PRICE_PER_PIECE_PAIR).getValue();
				int quantity = NumberUtils.isDigits(quantityTxtField.getValue()) ? NumberUtils.toInt(quantityTxtField.getValue()) : 0;
				double pricePerPiecePair = NumberUtils.isNumber(genralItemPricePerPieceTxtField.getValue()) ? NumberUtils.toDouble(genralItemPricePerPieceTxtField.getValue()) : 0.0f;
				Double weight = null;
				if(StringUtils.isBlank(weightTxtField.getValue())){
					weight = null;
				}
				if(!StringUtils.isBlank(weightTxtField.getValue()) 
						&& NumberUtils.isNumber(weightTxtField.getValue()))
				{
					weight = NumberUtils.toDouble(weightTxtField.getValue());
				}
				
				if (quantity > 0
						&& (weight == null || NumberUtils.isNumber(weightTxtField.getValue()))
						&& pricePerPiecePair > 0.000f
						&& !StringUtils.isBlank(String.valueOf(piecePairField.getValue()))
						&& !StringUtils.isBlank(String.valueOf(itemNameField.getValue())))
				{
					genralItemPriceTxtField.setValue(String.format("%.3f", quantity * pricePerPiecePair));
					genralItemPriceTxtField.addStyleName("v-textfield-success");
					genralItemPriceTxtField.setImmediate(true);
				} else {
					genralItemPriceTxtField.clear();
					genralItemPriceTxtField.addStyleName("v-textfield-warning");
					genralItemPriceTxtField.setImmediate(true);
				}
			}
		};
	}
	
	private Image getRemoveItemImage(final Object goldItemRowId) {
		final Image image = new Image("", removeItemImageResource);
		image.setHeight("20px");
		image.setWidth("20px");
		image.setDescription("Remove Item");
		image.setData(goldItemRowId);
		image.addClickListener((event -> removeItem(image.getData())));
		return image;
	}
	
	private ComboBox getItemNameList(final Object currentItemId) {
		ComboBox itemName = new ComboBox();
		itemName.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		ArrayList<String> generalItemListFromCsvFile = (ArrayList<String>) CustomShopSettingFileUtility.getInstance().getGeneralItemsList();
		for(String generalItem : generalItemListFromCsvFile){
			itemName.addItem(generalItem);
		}
		itemName.addItem("SILVER COIN");
		itemName.addItem("GOLD COIN");
		itemName.addItem("SILVER PEN");
		itemName.addItem("PEN");
		itemName.setWidth("100%");
		return itemName;
	}

	@Override
	public Double getTotalPrice() {
		double totalCost = 0.0;
		List<Object> itemIdsList = getAllItemIds();
		for (Object obj : itemIdsList) {
			TextField generalPriceTxtField = (TextField) getItem(obj).getItemProperty(PRICE).getValue();
			String itemPrice = generalPriceTxtField.getValue();
			totalCost += NumberUtils.isNumber(itemPrice) ? NumberUtils.toDouble(itemPrice) : 0.0;
		}
		return totalCost;
	}
}
