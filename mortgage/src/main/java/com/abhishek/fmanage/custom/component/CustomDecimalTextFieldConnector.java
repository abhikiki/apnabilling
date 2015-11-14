package com.abhishek.fmanage.custom.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CustomDecimalTextField.class)
public class CustomDecimalTextFieldConnector extends TextFieldConnector {

	private static final long serialVersionUID = -491559284787322054L;
	private boolean hasDot = false;
	private boolean hasNum = false;

	public CustomDecimalTextFieldConnector() {
		getWidget().addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (getWidget().getText().contains(".")) {
					hasDot = true;
				} else {
					hasDot = false;
				}
				if ('.' == event.getCharCode() && hasDot) {
					getWidget().cancelKey();
				} else if ('.' != event.getCharCode() && !Character.isDigit(event.getCharCode())) {
					getWidget().cancelKey();
				} else if ('.' == event.getCharCode() && hasDot && !hasNum ) {
					getWidget().cancelKey();
				}
			}
		});
	}

	@Override
	protected Widget createWidget() {
		return GWT.create(CustomDecimalTextFieldWidget.class);
	}

	@Override
	public CustomDecimalTextFieldWidget getWidget() {
		return (CustomDecimalTextFieldWidget) super.getWidget();
	}

	@Override
	public CustomDecimalTextFieldState getState() {
		return (CustomDecimalTextFieldState) super.getState();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		final String text = getState().text;
		getWidget().setText(text);
	}

}
