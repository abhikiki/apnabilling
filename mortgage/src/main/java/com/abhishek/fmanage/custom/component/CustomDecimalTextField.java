/**
 * 
 */
package com.abhishek.fmanage.custom.component;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Property;
import com.vaadin.ui.TextField;

/**
 * @author guptaa6
 *
 */
public class CustomDecimalTextField extends TextField{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6200360085660971854L;

	public CustomDecimalTextField() {
		setValue("");
	}

	public CustomDecimalTextField(String caption) {
		this();
		setCaption(caption);
	}

	public CustomDecimalTextField(@SuppressWarnings("rawtypes") Property dataSource) {
		setPropertyDataSource(dataSource);
	}

	public CustomDecimalTextField(String caption, @SuppressWarnings("rawtypes") Property dataSource) {
		this(dataSource);
		setCaption(caption);
	}

	public CustomDecimalTextField(String caption, String value) {
		setValue(value);
		setCaption(caption);
	}

	@Override
	public CustomDecimalTextFieldState getState() {
		return (CustomDecimalTextFieldState) super.getState();
	}
	
	@Override
	public String getValue() {
		return super.getValue();
	}

	@Override
	public void setValue(String value) {
		value = onlyDigitsNegativeSignAndOneDot(value);
		super.setValue(value);
		getState().text = value;
		
	}

	private String onlyDigitsNegativeSignAndOneDot(String value) {
		boolean hasDot = false;
		String digits = "";
		if(!StringUtils.isEmpty(value))
		{
			if(value.charAt(0) == '-'){
				digits = "-";
			}
		}
		
		for (int i = 0; i < value.length(); i++) {
			
			char chrs = value.charAt(i);
			if (chrs == '.' && hasDot) {
				continue;
			} else if (chrs == '.' && !hasDot) {
				digits = digits + chrs;
				hasDot = true;
			} else if ('.' != chrs && !Character.isDigit(chrs)) {
				continue;
			} else {
				digits = digits + chrs;
			}
		}
		return digits;
	}
}
