package com.abhishek.fmanage.retail.data.container;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.abhishek.fmanage.csv.utility.CustomShopSettingFileUtility;
import com.abhishek.fmanage.mortgage.data.container.CustomItemContainerInterface;
import com.abhishek.fmanage.retail.dto.GoldTransactionItemDTO;
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

public class GoldItemContainer extends IndexedContainer implements
		CustomItemContainerInterface {

	public static final String DELETE = "Delete";
	public static final String PRICE = "Price(INR)";
	public static final String GOLD_RATE = "GoldRate(pergm)";
	public static final String MAKING_CHARGE = "MakingCharge";
	public static final String MAKING_CHARGE_TYPE = "Mk ChargeType";
	public static final String WEIGHT = "Wt(gms)";
	public static final String PIECE_PAIR = "PiecePair";
	public static final String QUANTITY = "Quantity";
	public static final String ITEM_NAME = "ItemName";
	public static final String GOLD_TYPE = "GoldType";
	public static final ThemeResource removeItemImageResource = new ThemeResource(
			"img/removeButtonSmall.jpg");

	private static final String MAKING_COST_TYPE_NET = "net";
	private static final String MAKING_COST_TYPE_PER_GM = "per gm";
	private static final String MAKING_COST_TYPE_PERCENT = "%";
	private static final long serialVersionUID = 1L;

	public GoldItemContainer() {
		addContainerProperty(DELETE, Image.class, new Image());
		addContainerProperty(GOLD_TYPE, ComboBox.class, new ComboBox());
		addContainerProperty(ITEM_NAME, ComboBox.class, new ComboBox());
		addContainerProperty(QUANTITY, TextField.class, new TextField());
		addContainerProperty(PIECE_PAIR, ComboBox.class, new ComboBox());
		addContainerProperty(WEIGHT, TextField.class, new TextField());
		addContainerProperty(MAKING_CHARGE_TYPE, ComboBox.class, new ComboBox());
		addContainerProperty(MAKING_CHARGE, TextField.class, new TextField());
		addContainerProperty(GOLD_RATE, TextField.class, new TextField());
		addContainerProperty(PRICE, TextField.class, new TextField());
	}

	@Override
	public Double getTotalPrice() {
		double totalCost = 0.0;
		List<Object> itemIdsList = getAllItemIds();
		for (Object obj : itemIdsList) {
			TextField goldPriceTxtField = (TextField) getItem(obj)
					.getItemProperty(PRICE).getValue();
			String itemPrice = goldPriceTxtField.getValue();
			totalCost += NumberUtils.isNumber(itemPrice) ? NumberUtils
					.toDouble(itemPrice) : 0.0;
		}
		return totalCost;
	}

	public Double getTotalWeight() {
		double totalWeight = 0.0;
		List<Object> itemIdsList = getAllItemIds();
		for (Object obj : itemIdsList) {
			TextField weightPriceTxtField = (TextField) getItem(obj)
					.getItemProperty(WEIGHT).getValue();
			String weight = weightPriceTxtField.getValue();
			totalWeight += NumberUtils.isNumber(weight) ? NumberUtils
					.toDouble(weight) : 0.0;
		}
		return totalWeight;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addCustomItem() {
		Object goldItemRowId = addItem();
		Item item = getItem(goldItemRowId);
		if (item != null) {
			item.getItemProperty(DELETE).setValue(
					getRemoveItemImage(goldItemRowId));
			item.getItemProperty(GOLD_TYPE).setValue(
					getHallMarkTypeList(goldItemRowId));
			item.getItemProperty(ITEM_NAME).setValue(
					getItemNameList(goldItemRowId));
			item.getItemProperty(QUANTITY).setValue(getQuantity(goldItemRowId));
			item.getItemProperty(PIECE_PAIR).setValue(
					getPiecePair(goldItemRowId));
			item.getItemProperty(WEIGHT).setValue(getWeight(goldItemRowId));
			item.getItemProperty(MAKING_CHARGE).setValue(
					getMakingCharge(goldItemRowId));
			item.getItemProperty(MAKING_CHARGE_TYPE).setValue(
					getMakingChargeType(goldItemRowId));
			item.getItemProperty(GOLD_RATE)
					.setValue(getGoldRate(goldItemRowId));
			item.getItemProperty(PRICE).setValue(getPrice());
		}
	}

	@SuppressWarnings("unchecked")
	public void addCustomItem(List<GoldTransactionItemDTO> goldItemBeanList) {
		// GoldTransactionItemDTO goldItemBean;
		for (GoldTransactionItemDTO goldItemBean : goldItemBeanList) {

			Object goldItemRowId = addItem();
			Item item = getItem(goldItemRowId);
			if (item != null) {
				item.getItemProperty(DELETE).setValue(
						getRemoveItemImage(goldItemRowId));

				ComboBox hallMarkTypeCombo = getHallMarkTypeList(goldItemRowId);
				hallMarkTypeCombo.addItem(goldItemBean.getGoldType());
				hallMarkTypeCombo.setValue(goldItemBean.getGoldType());
				item.getItemProperty(GOLD_TYPE).setValue(hallMarkTypeCombo);

				ComboBox itemNameCombo = getItemNameList(goldItemRowId);
				itemNameCombo.addItem(goldItemBean.getGoldItemName());
				itemNameCombo.setValue(goldItemBean.getGoldItemName());
				item.getItemProperty(ITEM_NAME).setValue(itemNameCombo);

				TextField quantity = getQuantity(goldItemRowId);
				quantity.setValue(String.valueOf(goldItemBean.getQuantity()));
				item.getItemProperty(QUANTITY).setValue(quantity);

				ComboBox piecePairCombo = getPiecePair(goldItemRowId);
				piecePairCombo.addItem(goldItemBean.getPiecePair());
				piecePairCombo.setValue(goldItemBean.getPiecePair());
				item.getItemProperty(PIECE_PAIR).setValue(piecePairCombo);

				TextField goldWeightTxt = (TextField) getWeight(goldItemRowId);
				goldWeightTxt
						.setValue(String.valueOf(goldItemBean.getWeight()));
				item.getItemProperty(WEIGHT).setValue(goldWeightTxt);

				TextField makingChargeText = (TextField) getMakingCharge(goldItemRowId);
				makingChargeText.setValue(String.valueOf(goldItemBean
						.getMakingCharge()));
				item.getItemProperty(MAKING_CHARGE).setValue(makingChargeText);

				ComboBox makingChargeTypeCombo = getMakingChargeType(goldItemRowId);
				makingChargeTypeCombo.addItem(goldItemBean
						.getMakingChargeType());
				makingChargeTypeCombo.setValue(goldItemBean
						.getMakingChargeType());
				item.getItemProperty(MAKING_CHARGE_TYPE).setValue(
						makingChargeTypeCombo);

				TextField goldRateTxt = (TextField) getGoldRate(goldItemRowId);
				goldRateTxt
						.setValue(String.valueOf(goldItemBean.getGoldRate()));
				item.getItemProperty(GOLD_RATE).setValue(goldRateTxt);

				TextField goldItemPriceTxt = (TextField) getPrice();
				goldItemPriceTxt.setValue(String.valueOf(goldItemBean
						.getGoldItemPrice()));
				item.getItemProperty(PRICE).setValue(goldItemPriceTxt);
			}
		}

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

	private Object getPrice() {
		TextField itemPrice = new TextField();
		itemPrice.setImmediate(true);
		itemPrice.setRequired(true);
		itemPrice.setValidationVisible(true);
		itemPrice.setWidth("100%");
		itemPrice.setEnabled(false);
		itemPrice.setStyleName("my-disabled");
		itemPrice.addValidator(new DoubleRangeValidator(
				"Must be number and > 0", 0.0001, null));
		itemPrice
				.addValidator((value) -> {
					if (!NumberUtils.isNumber(String.valueOf(value))
							|| (NumberUtils.isNumber(String.valueOf(value)) && (NumberUtils
									.toDouble(String.valueOf(value)) <= 0.0))) {
						itemPrice.addStyleName("v-textfield-fail");
					} else {
						itemPrice.removeStyleName("v-textfield-fail");
					}
				});
		return itemPrice;
	}

	private Object getGoldRate(final Object currentItemId) {
		TextField goldRate = new TextField();
		goldRate.setImmediate(true);
		goldRate.setRequired(true);
		goldRate.setValidationVisible(true);
		goldRate.setWidth("80%");
		goldRate.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		goldRate.addValidator(new DoubleRangeValidator(
				"Must be number and > 0", 0.0001, null));
		goldRate.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 8427047967928189057L;

			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (StringUtils.isEmpty(value)) {
					goldRate.addStyleName("v-textfield-fail");
					TextField goldPriceTextField = (TextField) getItem(
							currentItemId).getItemProperty(PRICE).getValue();
					goldPriceTextField.setValue("");
					goldPriceTextField.removeStyleName("v-textfield-success");
				}
			}
		});
		goldRate.addValidator((value) -> {
			if (!NumberUtils.isNumber(String.valueOf(value))
					|| (NumberUtils.isNumber(String.valueOf(value)) && (NumberUtils
							.toDouble(String.valueOf(value)) <= 0.0))) {
				goldRate.addStyleName("v-textfield-fail");
				TextField goldPriceTextField = (TextField) getItem(
						currentItemId).getItemProperty(PRICE).getValue();
				goldPriceTextField.setValue("");
				goldPriceTextField.removeStyleName("v-textfield-success");
			} else {
				goldRate.removeStyleName("v-textfield-fail");
			}
		});
		return goldRate;
	}

	private Object getMakingCharge(final Object currentItemId) {
		TextField makingCharge = new TextField();
		makingCharge.setImmediate(true);
		makingCharge.setRequired(true);
		makingCharge.setValidationVisible(true);
		makingCharge.setWidth("100%");
		makingCharge
				.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		makingCharge.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = -4068221795365506559L;

			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (StringUtils.isEmpty(value)) {
					makingCharge.addStyleName("v-textfield-fail");
					TextField goldPriceTextField = (TextField) getItem(
							currentItemId).getItemProperty(PRICE).getValue();
					goldPriceTextField.setValue("");
					goldPriceTextField.removeStyleName("v-textfield-success");
				}
			}
		});
		makingCharge.addValidator(new DoubleRangeValidator(
				"Must be number and >= 0", 0.0, null));
		makingCharge
				.addValidator((value) -> {
					if (!NumberUtils.isNumber(String.valueOf(value))
							|| (NumberUtils.isNumber(String.valueOf(value)) && (NumberUtils
									.toDouble(String.valueOf(value)) < 0.0))) {
						makingCharge.addStyleName("v-textfield-fail");
						TextField goldPriceTextField = (TextField) getItem(
								currentItemId).getItemProperty(PRICE)
								.getValue();
						goldPriceTextField.setValue("");
						goldPriceTextField
								.removeStyleName("v-textfield-success");
					} else {
						makingCharge.setComponentError(null);
						makingCharge.removeStyleName("v-textfield-fail");
					}
				});
		return makingCharge;
	}

	private ComboBox getMakingChargeType(final Object goldItemRowId) {
		ComboBox itemName = new ComboBox();
		itemName.setNullSelectionAllowed(false);
		itemName.addItem(MAKING_COST_TYPE_PERCENT);
		itemName.addItem(MAKING_COST_TYPE_PER_GM);
		itemName.addItem(MAKING_COST_TYPE_NET);
		itemName.setValue(MAKING_COST_TYPE_PERCENT);
		itemName.setImmediate(true);
		itemName.setWidth("85%");
		itemName.addValueChangeListener(getCustomValueChangeListener(goldItemRowId));
		return itemName;
	}

	private Object getWeight(final Object currentItemId) {
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
				if (StringUtils.isEmpty(value)) {
					weight.addStyleName("v-textfield-fail");
					TextField goldPriceTextField = (TextField) getItem(
							currentItemId).getItemProperty(PRICE).getValue();
					goldPriceTextField.setValue("");
					goldPriceTextField.removeStyleName("v-textfield-success");
					goldPriceTextField.setImmediate(true);
				}
			}
		});
		weight.addValidator(new DoubleRangeValidator("Must be number and > 0",
				0.0001, null));
		weight.addValidator((value) -> {
			if (!NumberUtils.isNumber(String.valueOf(value))
					|| (NumberUtils.isNumber(String.valueOf(value)) && (NumberUtils
							.toDouble(String.valueOf(value)) <= 0.0))) {
				weight.addStyleName("v-textfield-fail");
				TextField goldPriceTextField = (TextField) getItem(
						currentItemId).getItemProperty(PRICE).getValue();
				goldPriceTextField.setValue("");
				goldPriceTextField.removeStyleName("v-textfield-success");
			} else {
				weight.setComponentError(null);
				weight.removeStyleName("v-textfield-fail");
			}
		});
		return weight;
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
					TextField goldPriceTextField = (TextField) getItem(
							currentItemId).getItemProperty(PRICE).getValue();
					goldPriceTextField.setValue("");
					goldPriceTextField.removeStyleName("v-textfield-success");
				}
			}
		});
		quantity.addValidator(new DoubleRangeValidator(
				"Must be number and > 0", 1.0, null));
		quantity.addValidator((value) -> {
			if (!NumberUtils.isDigits(String.valueOf(value))
					|| (NumberUtils.isDigits(String.valueOf(value)) && (NumberUtils
							.toInt(String.valueOf(value)) <= 0))) {
				quantity.addStyleName("v-textfield-fail");
				TextField goldPriceTextField = (TextField) getItem(
						currentItemId).getItemProperty(PRICE).getValue();
				goldPriceTextField.addStyleName("v-textfield-warning");

			} else {
				quantity.setComponentError(null);
				quantity.removeStyleName("v-textfield-fail");
			}
		});
		return quantity;
	}

	private ValueChangeListener getCustomValueChangeListener(
			final Object currentItemId) {
		return new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				ComboBox itemNameField = (ComboBox) getItem(currentItemId)
						.getItemProperty(ITEM_NAME).getValue();
				ComboBox hallMarkTypeField = (ComboBox) getItem(currentItemId)
						.getItemProperty(GOLD_TYPE).getValue();
				ComboBox piecePairField = (ComboBox) getItem(currentItemId)
						.getItemProperty(PIECE_PAIR).getValue();
				ComboBox makingChargeType = (ComboBox) getItem(currentItemId)
						.getItemProperty(MAKING_CHARGE_TYPE).getValue();
				TextField quantityTxtField = (TextField) getItem(currentItemId)
						.getItemProperty(QUANTITY).getValue();
				TextField weightTxtField = (TextField) getItem(currentItemId)
						.getItemProperty(WEIGHT).getValue();
				TextField makingChargeTxtField = (TextField) getItem(
						currentItemId).getItemProperty(MAKING_CHARGE)
						.getValue();
				TextField goldRateTxtField = (TextField) getItem(currentItemId)
						.getItemProperty(GOLD_RATE).getValue();
				TextField goldPriceTxtField = (TextField) getItem(currentItemId)
						.getItemProperty(PRICE).getValue();
				int quantity = NumberUtils
						.isDigits(quantityTxtField.getValue()) ? NumberUtils
						.toInt(quantityTxtField.getValue()) : 0;
				double weight = NumberUtils.isNumber(weightTxtField.getValue()) ? NumberUtils
						.toDouble(weightTxtField.getValue()) : 0.0;
				double makingCharge = NumberUtils.isNumber(makingChargeTxtField
						.getValue()) ? NumberUtils
						.toDouble(makingChargeTxtField.getValue()) : 0.0;
				double goldRate = NumberUtils.isNumber(goldRateTxtField
						.getValue()) ? NumberUtils.toDouble(goldRateTxtField
						.getValue()) : 0.0;
				if ((quantity > 0)
						&& (NumberUtils.isNumber(weightTxtField.getValue()) && weight > 0.0)
						&& (NumberUtils.isNumber(makingChargeTxtField
								.getValue()) && makingCharge >= 0.0)
						&& (NumberUtils.isNumber(weightTxtField.getValue()) && goldRate > 0.0)
						&& !StringUtils.isBlank((String) hallMarkTypeField
								.getValue())
						&& !StringUtils.isBlank((String) piecePairField
								.getValue())
						&& !StringUtils.isBlank((String) makingChargeType
								.getValue())
						&& !StringUtils.isBlank((String) itemNameField
								.getValue())) {
					double goldPrice = 0.0;
					switch (String.valueOf(makingChargeType.getValue())) {
					case MAKING_COST_TYPE_PERCENT:
						goldPrice = (weight * goldRate)
								* (1 + makingCharge / 100.0f);
						break;
					case MAKING_COST_TYPE_PER_GM:
						goldPrice = weight * (goldRate + makingCharge);
						break;
					case MAKING_COST_TYPE_NET:
						goldPrice = (weight * goldRate) + makingCharge;
					}
					goldPriceTxtField
							.setValue(String.format("%.3f", goldPrice));
					goldPriceTxtField.addStyleName("v-textfield-success");
					goldPriceTxtField.setImmediate(true);
				} else {
					goldPriceTxtField.clear();

					goldPriceTxtField.removeStyleName("v-textfield-success");
					goldPriceTxtField.addStyleName("v-textfield-warning");
					goldPriceTxtField.setImmediate(true);
				}
			}
		};
	}

	private ComboBox getHallMarkTypeList(final Object currentItemId) {
		ComboBox hallMarkType = new ComboBox();
		hallMarkType.setNullSelectionAllowed(false);
		hallMarkType
				.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		hallMarkType.addItem("916");
		hallMarkType.addItem("875");
		hallMarkType.addItem("833");
		hallMarkType.addItem("750");
		hallMarkType.setValue("916");
		hallMarkType.setWidth("90%");
		return hallMarkType;
	}

	private ComboBox getPiecePair(final Object currentItemId) {
		ComboBox itemName = new ComboBox();
		itemName.setNullSelectionAllowed(false);
		itemName.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		itemName.addItem("Piece");
		itemName.addItem("Pair");
		itemName.setValue("Piece");
		itemName.setWidth("100%");
		return itemName;
	}

	private ComboBox getItemNameList(final Object currentItemId) {
		ComboBox itemName = new ComboBox();
		itemName.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		ArrayList<String> goldItemListFromCsvFile = (ArrayList<String>) CustomShopSettingFileUtility
				.getInstance().getGoldItemsList();
		for (String goldItem : goldItemListFromCsvFile) {
			itemName.addItem(goldItem);
		}
		itemName.setWidth("100%");
		itemName.addItem("AD NOSEPIN");
		itemName.addItem("BABY BALA");
		itemName.addItem("BABY RING");
		itemName.addItem("BALI");
		itemName.addItem("CHAIN");
		itemName.addItem("CHAND");
		itemName.addItem("CHURI");
		itemName.addItem("DHOLNA");
		itemName.addItem("EAR LARI");
		itemName.addItem("EARRING");
		itemName.addItem("GENTS BRACELET");
		itemName.addItem("GENTS RING");
		itemName.addItem("GOD LOCKET");
		itemName.addItem("GOLD ITEM");
		itemName.addItem("JAL EARRING");
		itemName.addItem("JHUMKA");
		itemName.addItem("JHUMKI");
		itemName.addItem("JITIYA");
		itemName.addItem("KADA");
		itemName.addItem("KAMARDHANI");
		itemName.addItem("KANGAN");
		itemName.addItem("KANTHI CHAIN");
		itemName.addItem("KANTHI SET");
		itemName.addItem("LADIES BRACELET");
		itemName.addItem("LADIES RING");
		itemName.addItem("LOCKET");
		itemName.addItem("M.S.LARI");
		itemName.addItem("NATHIA");
		itemName.addItem("NATHIA LARI");
		itemName.addItem("NECKLACE");
		itemName.addItem("NOSEPIN");
		itemName.addItem("NOSERING");
		itemName.addItem("PASSA (TOPS)");
		itemName.addItem("PATLA");
		itemName.addItem("PENDANT");
		itemName.addItem("POLA CHURI");
		itemName.addItem("SET");
		itemName.addItem("SETTING NOSEPIN");
		itemName.addItem("SETTING TOPS");
		itemName.addItem("SUI-DHAGA");
		itemName.addItem("TANA");
		itemName.addItem("TIKA");
		itemName.addItem("TOPS");
		itemName.addItem("POLA CHURI");
		itemName.addItem("RULY BALA");
		itemName.addItem("JAL PATLA");
		itemName.addItem("Gold Item");
		itemName.addItem("MISCELLANEOUS");
		return itemName;
	}
}
