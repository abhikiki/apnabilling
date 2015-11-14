package com.abhishek.fmanage.dashboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class HelpManager implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<HelpOverlay> overlays = new ArrayList<HelpOverlay>();

    public void closeAll() {
        for (HelpOverlay overlay : overlays) {
            overlay.close();
        }
        overlays.clear();
    }

    protected HelpOverlay addOverlay(String caption, String text, String style) {
        HelpOverlay o = new HelpOverlay();
        o.setCaption(caption);
        o.addComponent(new Label(text, ContentMode.HTML));
        o.setStyleName(style);
        overlays.add(o);
        return o;
    }
}
