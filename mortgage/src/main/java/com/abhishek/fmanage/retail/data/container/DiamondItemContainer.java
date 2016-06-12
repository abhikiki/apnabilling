package com.abhishek.fmanage.retail.data.container;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.abhishek.fmanage.csv.utility.CustomShopSettingFileUtility;
import com.abhishek.fmanage.mortgage.data.container.CustomItemContainerInterface;
import com.abhishek.fmanage.retail.dto.DiamondTransactionItemDTO;
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

public class DiamondItemContainer extends IndexedContainer implements CustomItemContainerInterface{

	private static final long serialVersionUID = 1L;
	public static final String DELETE = "Delete";
	public static final String ITEM_NAME = "ItemName";
	public static final String QUANTITY = "Quantity";
	public static final String PIECE_PAIR = "PiecePair";
	public static final String GOLD_WEIGHT = "Gold Wt(gms)";
	public static final String DIAMOND_WEIGHT = "Diamond Wt(Carat)";
	public static final String DIAMOND_PIECE = "No. Of Diamond Piece";
	public static final String CERTIFICATE = "Certificate";
	public static final String PRICE = "Price(INR)";
	public static final ThemeResource removeItemImageResource = new ThemeResource("img/removeButtonSmall.jpg");
	
	public DiamondItemContainer(){
		 addContainerProperty(DELETE, Image.class, new Image());
	     addContainerProperty(ITEM_NAME, ComboBox.class, new ComboBox());
	     addContainerProperty(QUANTITY, TextField.class, new TextField());
	     addContainerProperty(PIECE_PAIR, ComboBox.class, new ComboBox());
	     addContainerProperty(GOLD_WEIGHT, TextField.class, new TextField());
	     addContainerProperty(DIAMOND_WEIGHT, TextField.class, new TextField());
	     addContainerProperty(DIAMOND_PIECE, TextField.class, new TextField());
	     addContainerProperty(CERTIFICATE, ComboBox.class, new ComboBox());
	     addContainerProperty(PRICE, TextField.class, new TextField());
	}
	
	@Override
	public Double getTotalPrice(){
		 double totalCost= 0.0;
		 List<Object> itemIdsList = getAllItemIds();
	     for (Object obj: itemIdsList){
	      	TextField goldPriceTxtField = (TextField)getItem(obj).getItemProperty(PRICE).getValue();
	       	String itemPrice = goldPriceTxtField.getValue();
	       	totalCost += NumberUtils.isNumber(itemPrice) ? NumberUtils.toDouble(itemPrice) : 0.0;
	     }
	     return totalCost;
	}
	
	public double getTotalGoldWeight() {
		double totalWeight = 0.0;
		List<Object> itemIdsList = getAllItemIds();
		for (Object obj : itemIdsList) {
			TextField weightPriceTxtField = (TextField) getItem(obj).getItemProperty(GOLD_WEIGHT).getValue();
			String weight = weightPriceTxtField.getValue();
			totalWeight += NumberUtils.isNumber(weight) ? NumberUtils.toDouble(weight) : 0.0;
		}
		return totalWeight;
	}
	
	public double getTotalDiamondWeight() {
		double totalWeight = 0.0;
		List<Object> itemIdsList = getAllItemIds();
		for (Object obj : itemIdsList) {
			TextField weightPriceTxtField = (TextField) getItem(obj).getItemProperty(DIAMOND_WEIGHT).getValue();
			String weight = weightPriceTxtField.getValue();
			totalWeight += NumberUtils.isNumber(weight) ? NumberUtils.toDouble(weight) : 0.0;
		}
		return totalWeight;
	}
	
	@Override
	@SuppressWarnings("unchecked")
    public void addCustomItem()
    {
        Object diamondItemRowId = addItem();
        Item item = getItem(diamondItemRowId);
        if (item != null)
        {
        	item.getItemProperty(DELETE).setValue(getRemoveItemImage(diamondItemRowId));
        	item.getItemProperty(ITEM_NAME).setValue(getItemNameList(diamondItemRowId));
        	item.getItemProperty(QUANTITY).setValue(getQuantity(diamondItemRowId));
        	item.getItemProperty(PIECE_PAIR).setValue(getPiecePair(diamondItemRowId));
        	item.getItemProperty(GOLD_WEIGHT).setValue(getWeight(diamondItemRowId));
        	item.getItemProperty(DIAMOND_WEIGHT).setValue(getWeight(diamondItemRowId));
        	item.getItemProperty(DIAMOND_PIECE).setValue(getQuantity(diamondItemRowId));
        	item.getItemProperty(CERTIFICATE).setValue(getCertificate(diamondItemRowId));
        	item.getItemProperty(PRICE).setValue(getPrice());
        }
    }

	@SuppressWarnings("unchecked")
	public void addCustomItem(DiamondTransactionItemDTO diamondItemBean) {
		Object diamondItemRowId = addItem();
		Item item = getItem(diamondItemRowId);
		if (item != null) {
			item.getItemProperty(DELETE).setValue(getRemoveItemImage(diamondItemRowId));

			ComboBox itemNameCombo = getItemNameList(diamondItemRowId);
			itemNameCombo.addItem(diamondItemBean.getItemName());
			itemNameCombo.setValue(diamondItemBean.getItemName());
			item.getItemProperty(ITEM_NAME).setValue(itemNameCombo);
			
			TextField quantity = getQuantity(diamondItemRowId);
			quantity.setValue(String.valueOf(diamondItemBean.getQuantity()));
			item.getItemProperty(QUANTITY).setValue(quantity);
			
			ComboBox piecePairCombo = getPiecePair(diamondItemRowId);
			piecePairCombo.addItem(diamondItemBean.getPiecePair());
			piecePairCombo.setValue(diamondItemBean.getPiecePair());
			item.getItemProperty(PIECE_PAIR).setValue(piecePairCombo);
			
			TextField goldWeightTxt = (TextField) getWeight(diamondItemRowId);
			goldWeightTxt.setValue(String.valueOf(diamondItemBean.getGoldWeight()));
			item.getItemProperty(GOLD_WEIGHT).setValue(goldWeightTxt);
			
			TextField diamondWeightTxt = (TextField) getWeight(diamondItemRowId);
			diamondWeightTxt.setValue(String.valueOf(diamondItemBean.getDiamondWeightCarat()));
			item.getItemProperty(DIAMOND_WEIGHT).setValue(diamondWeightTxt);
			
			TextField diamondPieceTxt = (TextField) getQuantity(diamondItemRowId);
			diamondPieceTxt.setValue(String.valueOf(diamondItemBean.getQuantity()));
			item.getItemProperty(DIAMOND_PIECE).setValue(diamondPieceTxt);
			
			ComboBox certificateCombo = getCertificate(diamondItemRowId);
			certificateCombo.addItem(diamondItemBean.isCertified() ? "Yes" : "No");
			certificateCombo.setValue(diamondItemBean.isCertified() ? "Yes" : "No");
			item.getItemProperty(CERTIFICATE).setValue(certificateCombo);
			
			TextField itemPriceTxt = (TextField) getPrice();
			itemPriceTxt.setValue(String.valueOf(diamondItemBean.getItemPrice()));
			item.getItemProperty(PRICE).setValue(itemPriceTxt);
		
		}
	}
	
	private Image getRemoveItemImage(Object diamondItemRowId) {
		final Image image = new Image("", removeItemImageResource);
        image.setHeight("20px");
        image.setWidth("20px");
        image.setDescription("Remove Item");
        image.setData(diamondItemRowId);
        image.addClickListener((event -> removeItem(image.getData())));
		return image;
	}

	private ComboBox getCertificate(final Object diamondItemRowId) {
		ComboBox itemName = new ComboBox();
		itemName.setNullSelectionAllowed(false);
		itemName.addItem("Yes");
		itemName.addItem("No");
		itemName.setValue("Yes");
		return itemName;
	}

	private Object getPrice(){
		TextField itemPrice = new TextField();
		itemPrice.setImmediate(true);
		itemPrice.setRequired(true);
		itemPrice.setValidationVisible(true);
		itemPrice.addValidator(new DoubleRangeValidator("Must be number and > 0",  0.0001, null));
		itemPrice.addValidator(
			(value) -> {
				if(!NumberUtils.isNumber(String.valueOf(value)) 
						|| ( NumberUtils.isNumber(String.valueOf(value))
							&& (NumberUtils.toDouble(String.valueOf(value)) <= 0.0))){
					itemPrice.addStyleName("v-textfield-fail");
				}else{
					itemPrice.removeStyleName("v-textfield-fail");
					itemPrice.removeStyleName("v-textfield-warning");
					itemPrice.addStyleName("v-textfield-success");
				}
			});
		return itemPrice;
	}

	private Object getWeight(final Object currentItemId) {
		TextField weight = new TextField();
		weight.setImmediate(true);
		weight.setRequired(true);
		weight.setValidationVisible(true);
		weight.addValidator(new DoubleRangeValidator("Must be number and > 0", 0.0001, null));
		weight.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 8427047967928189057L;
			public void valueChange(ValueChangeEvent event) {
		        String value = (String) event.getProperty().getValue();
		        if(StringUtils.isEmpty(value)){
		        	weight.addStyleName("v-textfield-fail");
		        	TextField diamondPriceTextField = (TextField) getItem(currentItemId).getItemProperty(PRICE).getValue();
		        	diamondPriceTextField.setValue("");
		        	diamondPriceTextField.removeStyleName("v-textfield-success");
		        	diamondPriceTextField.addStyleName("v-textfield-warning");
		        }
		    }
		});
		weight.addValidator(
			(value) -> {
				if(!NumberUtils.isNumber(String.valueOf(value)) 
						|| ( NumberUtils.isNumber(String.valueOf(value))
							&& (NumberUtils.toDouble(String.valueOf(value)) <= 0.0)))
				{
					weight.addStyleName("v-textfield-fail");
					TextField diamondPriceTextField = (TextField) getItem(currentItemId).getItemProperty(PRICE).getValue();
		        	diamondPriceTextField.setValue("");
		        	diamondPriceTextField.removeStyleName("v-textfield-success");
		        	diamondPriceTextField.addStyleName("v-textfield-warning");
				}else{
					weight.setComponentError(null);
					weight.removeStyleName("v-textfield-fail");
				}
		});		
		return weight;
	}

	private ComboBox getPiecePair(final Object currentItemId) {
		ComboBox itemName = new ComboBox();
		itemName.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		itemName.setNullSelectionAllowed(false);
		itemName.addItem("Piece");
		itemName.addItem("Pair");
		itemName.setValue("Piece");
		itemName.setWidth("100%");
		return itemName;
	}

	private TextField getQuantity(final Object currentItemId){
		TextField quantity = new TextField();
		quantity.setImmediate(true);
		quantity.setRequired(true);
		quantity.setValidationVisible(true);
		quantity.addValidator(new DoubleRangeValidator("Must be number and > 0", 1.0, null));
		quantity.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 8427047967928189057L;
			public void valueChange(ValueChangeEvent event) {
		        String value = (String) event.getProperty().getValue();
		        if(StringUtils.isEmpty(value)){
		        	quantity.addStyleName("v-textfield-fail");
		        	TextField diamondPriceTextField = (TextField) getItem(currentItemId).getItemProperty(PRICE).getValue();
		        	diamondPriceTextField.setValue("");
		        	diamondPriceTextField.removeStyleName("v-textfield-success");
		        	diamondPriceTextField.addStyleName("v-textfield-warning");
		        }
		    }
		});
		quantity.addValidator((value) ->
			{
				if(!NumberUtils.isDigits(String.valueOf(value)) 
						|| ( NumberUtils.isDigits(String.valueOf(value))
							&& (NumberUtils.toInt(String.valueOf(value)) <= 0.0))){
					quantity.addStyleName("v-textfield-fail");
					TextField diamondPriceTextField = (TextField) getItem(currentItemId).getItemProperty(PRICE).getValue();
		        	diamondPriceTextField.setValue("");
		        	diamondPriceTextField.removeStyleName("v-textfield-success");
		        	diamondPriceTextField.addStyleName("v-textfield-warning");
				}else{
					quantity.setComponentError(null);
					quantity.removeStyleName("v-textfield-fail");
				}
			});
		return quantity;
	}
	
	private ComboBox getItemNameList(final Object currentItemId){
		ComboBox itemName = new ComboBox();
		itemName.setWidth("100%");
		itemName.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		ArrayList<String> diamondItemListFromCsvFile = (ArrayList<String>) CustomShopSettingFileUtility.getInstance().getDiamondItemsList();
		for(String diamondItem : diamondItemListFromCsvFile){
			itemName.addItem(diamondItem);
		}
		itemName.addItem("TOPS");
		itemName.addItem("LADIES RING");
		itemName.addItem("GENTS RING");
		itemName.addItem("LOCKET SET");
		itemName.addItem("PENDANT SET");
		itemName.addItem("PENDANT");
		itemName.addItem("LOCKET");
		itemName.addItem("NECKLACE");
		itemName.addItem("EARRING");
		itemName.addItem("SET");
		itemName.addItem("DIAMOND ITEM");
		itemName.addItem("MISCELLANEOUS");
		return itemName;
	}

	private ValueChangeListener getCustomValueChangeListener(Object currentItemId) {
		
		return new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				
				ComboBox itemNameField = (ComboBox) getItem(currentItemId).getItemProperty(ITEM_NAME).getValue();
				if(StringUtils.isBlank((String)itemNameField.getValue())){
					TextField diamondPriceTextField = (TextField) getItem(currentItemId).getItemProperty(PRICE).getValue();
		        	diamondPriceTextField.setValue("");
		        	diamondPriceTextField.removeStyleName("v-textfield-success");
		        	diamondPriceTextField.addStyleName("v-textfield-warning");
		        	itemNameField.addStyleName("v-textfield-fail");
				}else{
					itemNameField.removeStyleName("v-textfield-fail");
				}
			}
		};
	}
}
