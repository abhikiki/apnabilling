/**
 * 
 */
package com.abhishek.fmanage.mortgage.data.container;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.abhishek.fmanage.cache.ItemCache;
import com.abhishek.fmanage.retail.dto.ItemDTO;
import com.avathartech.fastformfields.widgets.DecimalTextField;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;

/**
 * @author Abhishek
 *
 */
public class MortgageItemContainer extends IndexedContainer implements CustomMortgageItemContainerInterface
{
    private static final long serialVersionUID = 1L;
    public static final String DELETE = "Delete";
    public static final String ITEM_NAME = "ItemName";
    public static final String QUANTITY = "Quantity";
    public static final String WEIGHT = "Wt(gms)";
    public static final String GOLD_WEIGHT = "Gold wt(gms)";
    public static final String DIAMOND_WEIGHT = "Diamond wt(ct)";
    public static final String PIECE_PAIR = "Piece/pair";
    public static final ThemeResource removeItemImageResource = new ThemeResource("img/removeButtonSmall.jpg");
    public MortgageItemType itemType;
    /**
     * {@link Constructor} for Transaction view container
     */
    public MortgageItemContainer(MortgageItemType itemType)
    {
    	this.itemType = itemType;
        addContainerProperty(DELETE, Image.class, new Image());
        addContainerProperty(ITEM_NAME, ComboBox.class, new ComboBox());
        addContainerProperty(QUANTITY, DecimalTextField.class, new DecimalTextField());
        addContainerProperty(PIECE_PAIR, ComboBox.class, new ComboBox());
        if(MortgageItemType.DIAMOND == itemType){
        	addContainerProperty(GOLD_WEIGHT, DecimalTextField.class, new DecimalTextField());
        	addContainerProperty(DIAMOND_WEIGHT, DecimalTextField.class, new DecimalTextField());
        }else{
        	
        	addContainerProperty(WEIGHT, DecimalTextField.class, new DecimalTextField());
        }
    }

    @Override
    public Double getTotalWeight() {
		double totalWeight = 0.000f;
		List<Object> itemIdsList = getAllItemIds();
		for (Object obj : itemIdsList) {
			TextField weightPriceTxtField = (TextField) getItem(obj).getItemProperty(WEIGHT).getValue();
			String weight = weightPriceTxtField.getValue();
			totalWeight += NumberUtils.isNumber(weight) ? NumberUtils.toDouble(weight) : 0.000f;
		}
		return totalWeight;
	}
    
     public Double getDiamondItemTotalGoldWeight() {
		double totalWeight = 0.000f;
		List<Object> itemIdsList = getAllItemIds();
		for (Object obj : itemIdsList) {
			TextField weightPriceTxtField = (TextField) getItem(obj).getItemProperty(GOLD_WEIGHT).getValue();
			String weight = weightPriceTxtField.getValue();
			totalWeight += NumberUtils.isNumber(weight) ? NumberUtils.toDouble(weight) : 0.000f;
		}
		return totalWeight;
	}
    
    public Double getDiamondItemTotalDiamondWeight() {
		double totalWeight = 0.000f;
		List<Object> itemIdsList = getAllItemIds();
		for (Object obj : itemIdsList) {
			TextField weightPriceTxtField = (TextField) getItem(obj).getItemProperty(DIAMOND_WEIGHT).getValue();
			String weight = weightPriceTxtField.getValue();
			totalWeight += NumberUtils.isNumber(weight) ? NumberUtils.toDouble(weight) : 0.00f;
		}
		return totalWeight;
	}
    
    @SuppressWarnings("unchecked")
    @Override
	public Object addCustomItem(MortgageItemType itemType){
    	Object itemRowId = addItem();
		Item item = getItem(itemRowId);
		if (item != null) {
			item.getItemProperty(DELETE).setValue(getRemoveItemImage(itemRowId, itemType));
			item.getItemProperty(ITEM_NAME).setValue(getItemNameList(itemRowId, itemType));
			item.getItemProperty(PIECE_PAIR).setValue(getPiecePair(itemRowId, itemType));
			item.getItemProperty(QUANTITY).setValue(getWeight(itemRowId, itemType));
			if(MortgageItemType.DIAMOND == itemType){
				item.getItemProperty(GOLD_WEIGHT).setValue(getWeight(itemRowId, itemType));
				item.getItemProperty(DIAMOND_WEIGHT).setValue(getWeight(itemRowId, itemType));
			}else{
				
				item.getItemProperty(WEIGHT).setValue(getWeight(itemRowId, itemType));
			}
		}
		return itemRowId;
    }
    
    private Object getPiecePair(Object itemRowId, MortgageItemType itemType) {
    	ComboBox piecePairCombo = new ComboBox();
    	piecePairCombo.setRequired(true);
    	piecePairCombo.addItem("PIECE");
    	piecePairCombo.addItem("PAIR");
    	piecePairCombo.setValue("PIECE");
		return piecePairCombo;
	}

	private Object getWeight(final Object currentItemId, MortgageItemType itemType) {
    	DecimalTextField weight = new DecimalTextField();
		weight.setImmediate(true);
		weight.setRequired(true);
		weight.setValidationVisible(true);
		weight.setWidth("100%");
		weight.setData(itemType);
		weight.setValue("0.000");
		weight.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 516109943650543255L;

			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (NumberUtils.isNumber(value)) {
					weight.addStyleName("v-textfield-success");
				}else{
					weight.setValue("0.000");
				}
			}
		});
		return weight;
    }
		
    private ComboBox getItemNameList(final Object currentItemId, MortgageItemType itemType) {
		ComboBox itemName = new ComboBox();
		List<ItemDTO> itemDTOList = new ArrayList<ItemDTO>();
		switch(itemType){
			case GOLD : itemDTOList = ItemCache.getInstance().getItemMap().get("GOLD");
				break;
			case SILVER : itemDTOList = ItemCache.getInstance().getItemMap().get("SILVER");
				break;
			case DIAMOND : itemDTOList = ItemCache.getInstance().getItemMap().get("DIAMOND");
		}
		
		for (ItemDTO itemDto : itemDTOList) {
			itemName.addItem(itemDto.getItemName());
		}
		return itemName;
	}
    
    private Image getRemoveItemImage(final Object itemRowId, MortgageItemType itemType) {
		final Image image = new Image("", removeItemImageResource);
		image.setHeight("20px");
		image.setWidth("20px");
		image.setDescription("Remove Item");
		image.setData(itemType);
		image.addClickListener((event -> removeItem(itemRowId)));
		image.setImmediate(true);
		return image;
	}
}
