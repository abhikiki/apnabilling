package com.abhishek.fmanage.dashboard;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

public class DashboardUIProvider extends UIProvider
{
    private static final String USER_AGENT = "user-agent";
	private static final String FALSE = "false";
	private static final long serialVersionUID = -5419308728686499083L;

    private static final String MOBILE = "mobile";
    private static final String IPAD = "ipad";

    /**
     * Get the UI class based on the device type which is accessing the application
     */
    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event)
    {
        if (event.getRequest().getParameter(MOBILE) != null
            && FALSE.equals(event.getRequest().getParameter(MOBILE)))
        {
            return DashboardUI.class;
        }
        if (event.getRequest().getHeader(USER_AGENT).toLowerCase().contains(MOBILE)
            && !event.getRequest().getHeader(USER_AGENT).toLowerCase().contains(IPAD))
        {
            return MobileCheckUI.class;
        }
        return DashboardUI.class;
    }
}