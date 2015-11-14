package com.abhishek.fmanage.custom.component;

import com.vaadin.client.ui.VTextField;

public class CustomDecimalTextFieldWidget extends VTextField {

	public static final String CLASSNAME = "customdecimaltextfield";

	public CustomDecimalTextFieldWidget() {
		setStyleName(CLASSNAME);
	}
}
