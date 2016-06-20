package com.abhishek.fmanage.retail.data.container;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.abhishek.fmanage.csv.utility.CustomShopSettingFileUtility;
import com.abhishek.fmanage.mortgage.data.container.CustomItemContainerInterface;
import com.abhishek.fmanage.retail.dto.SilverTransactionItemDTO;
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

public class SilverItemContainer extends IndexedContainer implements
		CustomItemContainerInterface {

	private static final long serialVersionUID = 1L;
	public static final ThemeResource removeItemImageResource = new ThemeResource(
			"img/removeButtonSmall.jpg");
	public static final String DELETE = "Delete";
	public static final String PRICE = "Price(INR)";
	public static final String SILVER_RATE = "SilverRate(pergm)";
	public static final String MAKING_CHARGE = "MakingCharge";
	public static final String MAKING_CHARGE_TYPE = "MK ChargeType";
	public static final String WEIGHT = "Wt(gms)";
	public static final String PIECE_PAIR = "PiecePair";
	public static final String QUANTITY = "Quantity";
	public static final String ITEM_NAME = "ItemName";

	private static final String MAKING_COST_TYPE_NET = "net";
	private static final String MAKING_COST_TYPE_PER_GM = "per gm";
	private static final String MAKING_COST_TYPE_PERCENT = "%";

	public SilverItemContainer() {
		addContainerProperty(DELETE, Image.class, new Image());
		addContainerProperty(ITEM_NAME, ComboBox.class, new ComboBox());
		addContainerProperty(QUANTITY, TextField.class, new TextField());
		addContainerProperty(PIECE_PAIR, ComboBox.class, new ComboBox());
		addContainerProperty(WEIGHT, TextField.class, new TextField());
		addContainerProperty(MAKING_CHARGE, TextField.class, new TextField());
		addContainerProperty(MAKING_CHARGE_TYPE, ComboBox.class, new ComboBox());
		addContainerProperty(SILVER_RATE, TextField.class, new TextField());
		addContainerProperty(PRICE, TextField.class, new TextField());
	}

	@Override
	public Double getTotalPrice() {
		double totalCost = 0.0;
		for (Object obj : getAllItemIds()) {
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
		Object silverItemRowId = addItem();
		Item item = getItem(silverItemRowId);

		if (item != null) {
			item.getItemProperty(DELETE).setValue(
					getRemoveItemImage(silverItemRowId));
			item.getItemProperty(ITEM_NAME).setValue(
					getItemNameList(silverItemRowId));
			item.getItemProperty(QUANTITY).setValue(
					getQuantity(silverItemRowId));
			item.getItemProperty(PIECE_PAIR).setValue(
					getPiecePair(silverItemRowId));
			item.getItemProperty(WEIGHT).setValue(getWeight(silverItemRowId));
			item.getItemProperty(MAKING_CHARGE_TYPE).setValue(
					getMakingChargeType(silverItemRowId));
			item.getItemProperty(MAKING_CHARGE).setValue(
					getMakingCharge(silverItemRowId));
			item.getItemProperty(SILVER_RATE).setValue(
					getSilverRate(silverItemRowId));
			item.getItemProperty(PRICE).setValue(getPrice());
		}
	}

	private Image getRemoveItemImage(Object silverItemRowId) {
		final Image image = new Image("", removeItemImageResource);
		image.setHeight("20px");
		image.setWidth("20px");
		image.setDescription("Remove Item");
		image.setData(silverItemRowId);
		image.addClickListener((event -> removeItem(image.getData())));
		return image;
	}

	@SuppressWarnings("unchecked")
	public void addCustomItem(List<SilverTransactionItemDTO> silverItemBeanList) {
		for (SilverTransactionItemDTO silverItemBean : silverItemBeanList) {
			Object silverItemRowId = addItem();
			Item item = getItem(silverItemRowId);
			if (item != null) {
				item.getItemProperty(DELETE).setValue(
						getRemoveItemImage(silverItemRowId));

				ComboBox itemNameCombo = getItemNameList(silverItemRowId);
				itemNameCombo.addItem(silverItemBean.getItemName());
				itemNameCombo.setValue(silverItemBean.getItemName());
				item.getItemProperty(ITEM_NAME).setValue(itemNameCombo);

				TextField quantity = getQuantity(silverItemRowId);
				quantity.setValue(String.valueOf(silverItemBean.getQuantity()));
				item.getItemProperty(QUANTITY).setValue(quantity);

				ComboBox piecePairCombo = getPiecePair(silverItemRowId);
				piecePairCombo.addItem(silverItemBean.getPiecepair());
				piecePairCombo.setValue(silverItemBean.getPiecepair());
				item.getItemProperty(PIECE_PAIR).setValue(piecePairCombo);

				TextField silverWeightTxt = (TextField) getWeight(silverItemRowId);
				silverWeightTxt.setValue(String.valueOf(silverItemBean
						.getWeight()));
				item.getItemProperty(WEIGHT).setValue(silverWeightTxt);

				TextField makingChargeText = (TextField) getMakingCharge(silverItemRowId);
				makingChargeText.setValue(String.valueOf(silverItemBean
						.getMakingCharge()));
				item.getItemProperty(MAKING_CHARGE).setValue(makingChargeText);

				ComboBox makingChargeTypeCombo = getMakingChargeType(silverItemRowId);
				makingChargeTypeCombo.addItem(silverItemBean
						.getMakingChargeType());
				makingChargeTypeCombo.setValue(silverItemBean
						.getMakingChargeType());
				item.getItemProperty(MAKING_CHARGE_TYPE).setValue(
						makingChargeTypeCombo);

				TextField silverRateTxt = (TextField) getSilverRate(silverItemRowId);
				silverRateTxt.setValue(String.valueOf(silverItemBean
						.getSilverRate()));
				item.getItemProperty(SILVER_RATE).setValue(silverRateTxt);

				TextField silverItemPriceTxt = (TextField) getPrice();
				silverItemPriceTxt.setValue(String.valueOf(silverItemBean
						.getSilverItemPrice()));
				item.getItemProperty(PRICE).setValue(silverItemPriceTxt);

			}
		}
	}

	private Object getPrice() {
		TextField itemPrice = new TextField();
		itemPrice.setImmediate(true);
		itemPrice.setRequired(true);
		itemPrice.setEnabled(false);
		itemPrice.setValidationVisible(true);
		itemPrice.setEnabled(false);
		itemPrice.setStyleName("my-disabled");
		itemPrice.addValidator(new DoubleRangeValidator(
				"Must be number and > 0", 0.1, null));
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

	private Object getSilverRate(final Object currentItemId) {
		TextField silverRate = new TextField();
		silverRate.setImmediate(true);
		silverRate.setRequired(true);
		silverRate.setValidationVisible(true);
		silverRate.setWidth("80%");
		silverRate
				.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		silverRate.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 2301555384588108852L;

			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (StringUtils.isEmpty(value)) {
					silverRate.addStyleName("v-textfield-fail");
					TextField silverPriceTextField = (TextField) getItem(
							currentItemId).getItemProperty(PRICE).getValue();
					silverPriceTextField.setValue("");
					silverPriceTextField.removeStyleName("v-textfield-success");
				}
			}
		});
		silverRate
				.addValidator((value) -> {
					if (!NumberUtils.isNumber(String.valueOf(value))
							|| (NumberUtils.isNumber(String.valueOf(value)) && (NumberUtils
									.toDouble(String.valueOf(value)) <= 0.0))) {
						silverRate.addStyleName("v-textfield-fail");
					} else {
						silverRate.removeStyleName("v-textfield-fail");
					}
				});
		return silverRate;
	}

	private Object getMakingCharge(final Object currentItemId) {
		TextField makingCharge = new TextField();
		makingCharge.setImmediate(true);
		makingCharge.setRequired(true);
		makingCharge.setValidationVisible(true);
		makingCharge.setWidth("100%");
		makingCharge
				.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		makingCharge.addValidator(new DoubleRangeValidator(
				"Must be number and >= 0", 0.0, null));
		makingCharge.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = -2834457288572556392L;

			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (StringUtils.isEmpty(value)) {
					makingCharge.addStyleName("v-textfield-fail");
					TextField silverPriceTextField = (TextField) getItem(
							currentItemId).getItemProperty(PRICE).getValue();
					silverPriceTextField.setValue("");
					silverPriceTextField.removeStyleName("v-textfield-success");
				}
			}
		});
		makingCharge
				.addValidator((value) -> {
					if (!NumberUtils.isNumber(String.valueOf(value))
							|| (NumberUtils.isNumber(String.valueOf(value)) && (NumberUtils
									.toDouble(String.valueOf(value)) < 0.0))) {
						makingCharge.addStyleName("v-textfield-fail");
						TextField silverPriceTextField = (TextField) getItem(
								currentItemId).getItemProperty(PRICE)
								.getValue();
						silverPriceTextField.setValue("");
						silverPriceTextField
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
		itemName.addItem("%");
		itemName.addItem("per gm");
		itemName.addItem("net");
		itemName.setValue("%");
		itemName.setImmediate(true);
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
		weight.addValidator(new DoubleRangeValidator("Must be number and > 0",
				0.0001, null));
		weight.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 8048545363261290937L;

			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (StringUtils.isEmpty(value)) {
					weight.addStyleName("v-textfield-fail");
					TextField silverPriceTextField = (TextField) getItem(
							currentItemId).getItemProperty(PRICE).getValue();
					silverPriceTextField.setValue("");
					silverPriceTextField.removeStyleName("v-textfield-success");
				}
			}
		});
		weight.addValidator((value) -> {
			if (!NumberUtils.isNumber(String.valueOf(value))
					|| (NumberUtils.isNumber(String.valueOf(value)) && (NumberUtils
							.toDouble(String.valueOf(value)) <= 0.0))) {
				weight.addStyleName("v-textfield-fail");
				TextField silverPriceTextField = (TextField) getItem(
						currentItemId).getItemProperty(PRICE).getValue();
				silverPriceTextField.setValue("");
				silverPriceTextField.removeStyleName("v-textfield-success");
			} else {
				weight.setComponentError(null);
				weight.removeStyleName("v-textfield-fail");
			}
		});
		return weight;
	}

	private ComboBox getPiecePair(final Object currentItemId) {
		ComboBox itemName = new ComboBox();
		itemName.setNullSelectionAllowed(false);
		itemName.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		itemName.addItem("Piece");
		itemName.addItem("Pair");
		itemName.setValue("Piece");
		return itemName;
	}

	private TextField getQuantity(final Object currentItemId) {
		TextField quantity = new TextField();
		quantity.setImmediate(true);
		quantity.setRequired(true);
		quantity.setValidationVisible(true);
		quantity.setWidth("90%");
		quantity.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		quantity.addValidator(new DoubleRangeValidator(
				"Must be number and > 0", 1.0, null));
		quantity.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 2059828274806164304L;

			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (StringUtils.isEmpty(value)) {
					quantity.addStyleName("v-textfield-fail");
					TextField silverPriceTextField = (TextField) getItem(
							currentItemId).getItemProperty(PRICE).getValue();
					silverPriceTextField.setValue("");
					silverPriceTextField.removeStyleName("v-textfield-success");
				}
			}
		});
		quantity.addValidator((value) -> {
			if (!NumberUtils.isDigits(String.valueOf(value))
					|| (NumberUtils.isDigits(String.valueOf(value)) && (NumberUtils
							.toInt(String.valueOf(value)) <= 0))) {
				quantity.addStyleName("v-textfield-fail");
				TextField silverPriceTextField = (TextField) getItem(
						currentItemId).getItemProperty(PRICE).getValue();
				silverPriceTextField.setValue("");
				silverPriceTextField.removeStyleName("v-textfield-success");
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
				TextField silverRateTxtField = (TextField) getItem(
						currentItemId).getItemProperty(SILVER_RATE).getValue();
				TextField silverPriceTxtField = (TextField) getItem(
						currentItemId).getItemProperty(PRICE).getValue();
				int quantity = NumberUtils
						.isDigits(quantityTxtField.getValue()) ? NumberUtils
						.toInt(quantityTxtField.getValue()) : 0;
				double weight = NumberUtils.isNumber(weightTxtField.getValue()) ? NumberUtils
						.toDouble(weightTxtField.getValue()) : 0.0;
				double makingCharge = NumberUtils.isNumber(makingChargeTxtField
						.getValue()) ? NumberUtils
						.toDouble(makingChargeTxtField.getValue()) : 0.0;
				double silverRate = NumberUtils.isNumber(silverRateTxtField
						.getValue()) ? NumberUtils.toDouble(silverRateTxtField
						.getValue()) : 0.0;
				if ((NumberUtils.isDigits(quantityTxtField.getValue()) && quantity > 0)
						&& (NumberUtils.isNumber(weightTxtField.getValue()) && weight > 0.0)
						&& (NumberUtils.isNumber(makingChargeTxtField
								.getValue()) && makingCharge >= 0.0)
						&& (NumberUtils.isNumber(silverRateTxtField.getValue()) && silverRate > 0.0)
						&& !StringUtils.isBlank((String) piecePairField
								.getValue())
						&& !StringUtils.isBlank((String) makingChargeType
								.getValue())
						&& !StringUtils.isBlank((String) itemNameField
								.getValue())) {
					double silverPrice = 0.0;
					switch (String.valueOf(makingChargeType.getValue())) {
					case MAKING_COST_TYPE_PERCENT:
						silverPrice = (weight * silverRate)
								* (1 + makingCharge / 100.0f);
						break;
					case MAKING_COST_TYPE_PER_GM:
						silverPrice = weight * (silverRate + makingCharge);
						break;
					case MAKING_COST_TYPE_NET:
						silverPrice = (weight * silverRate) + makingCharge;
					}
					silverPriceTxtField.setValue(String.format("%.3f",
							silverPrice));
					silverPriceTxtField.addStyleName("v-textfield-success");
					silverPriceTxtField.setImmediate(true);
					// Notification.show("Item entry complete");
				} else {
					silverPriceTxtField.clear();
					silverPriceTxtField.removeStyleName("v-textfield-success");
					silverPriceTxtField.addStyleName("v-textfield-warning");
					silverPriceTxtField.setImmediate(true);
				}
			}
		};
	}

	private ComboBox getItemNameList(final Object currentItemId) {
		ComboBox itemName = new ComboBox();
		itemName.addValueChangeListener(getCustomValueChangeListener(currentItemId));
		ArrayList<String> silverItemListFromCsvFile = (ArrayList<String>) CustomShopSettingFileUtility
				.getInstance().getSilverItemsList();
		for (String silverItem : silverItemListFromCsvFile) {
			itemName.addItem(silverItem);
		}
		itemName.setWidth("100%");
		itemName.addItem("AD NECKLACE");
		itemName.addItem("AD SET");
		itemName.addItem("AD SILVER SET");
		itemName.addItem("AD TOPS");
		itemName.addItem("AJ SILVER COIN");
		itemName.addItem("ARGHA");
		itemName.addItem("BABY BALA");
		itemName.addItem("BABY PAYAL");
		itemName.addItem("BICHIYA");
		itemName.addItem("BOWL");
		itemName.addItem("BUTTON");
		itemName.addItem("CHAIN");
		itemName.addItem("CHALLA");
		itemName.addItem("CHAND");
		itemName.addItem("CHOTI");
		itemName.addItem("COIN");
		itemName.addItem("DHOLNA");
		itemName.addItem("DIYA");
		itemName.addItem("EARRING");
		itemName.addItem("GENTS BRACELET");
		itemName.addItem("GENTS RING");
		itemName.addItem("GHANTI");
		itemName.addItem("GHUNGUROO");
		itemName.addItem("GLASS");
		itemName.addItem("GOD LOCKET");
		itemName.addItem("ITTARDAAN");
		itemName.addItem("JAL JHUMKA");
		itemName.addItem("JAL TOPS");
		itemName.addItem("JHUMKA");
		itemName.addItem("JHUNJHOONA");
		itemName.addItem("JITIYA");
		itemName.addItem("JITIYA");
		itemName.addItem("JUG");
		itemName.addItem("KADA");
		itemName.addItem("KAJROUTI");
		itemName.addItem("KAMARDHANI");
		itemName.addItem("KAMARJAI");
		itemName.addItem("KANGAN");
		itemName.addItem("KASAILI");
		itemName.addItem("KATORI");
		itemName.addItem("KEY RING");
		itemName.addItem("KHADAO");
		itemName.addItem("KIYA");
		itemName.addItem("LADIES BRACELET");
		itemName.addItem("LADIES RING");
		itemName.addItem("LOCKET");
		itemName.addItem("LOTANI");
		itemName.addItem("M.S.LARI");
		itemName.addItem("MACHALI");
		itemName.addItem("MUKUT");
		itemName.addItem("MUKUT");
		itemName.addItem("MURTI");
		itemName.addItem("MURTI");
		itemName.addItem("NAJARIA");
		itemName.addItem("NARIAL");
		itemName.addItem("NECKLACE");
		itemName.addItem("OLD COIN");
		itemName.addItem("PAAN");
		itemName.addItem("PAAN KASAILI");
		itemName.addItem("PAANBATTI");
		itemName.addItem("PAANDAAN");
		itemName.addItem("PANJA");
		itemName.addItem("PATLA");
		itemName.addItem("PATLA");
		itemName.addItem("PATRI");
		itemName.addItem("PAYAL");
		itemName.addItem("PEN");
		itemName.addItem("PENDANT");
		itemName.addItem("PENDANT SET");
		itemName.addItem("PHOTO FRAME");
		itemName.addItem("PLATE");
		itemName.addItem("QUARTZ");
		itemName.addItem("S GHUNGUROO");
		itemName.addItem("SCISSORS");
		itemName.addItem("SET");
		itemName.addItem("SILVER COIN");
		itemName.addItem("SILVER ITEM");
		itemName.addItem("SILVER NOTE");
		itemName.addItem("SINGHASHAN");
		itemName.addItem("SOOP");
		itemName.addItem("SPOON");
		itemName.addItem("SRIYANTRA");
		itemName.addItem("STATUE");
		itemName.addItem("SWASTIC");
		itemName.addItem("TABEEZ");
		itemName.addItem("TANA");
		itemName.addItem("TAST");
		itemName.addItem("TOPS");
		itemName.addItem("TOPS");
		itemName.addItem("CHURI");
		itemName.addItem("NATHIA LARI");
		itemName.addItem("NATHIA");
		itemName.addItem("TIKA");
		itemName.addItem("POLA CHURI");
		itemName.addItem("RULY BALA");
		itemName.addItem("JAL PATLA");
		itemName.addItem("Silver Item");
		itemName.addItem("MISCELLANEOUS");
		return itemName;
	}
}
