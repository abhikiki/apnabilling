package com.abhishek.fmanage.dashboard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import com.abhishek.fmanage.dashboard.views.transaction.MortgageTransactionView;
import com.abhishek.fmanage.retail.views.RetailInvoiceView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect.AcceptItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represent the main UI of the application
 * 
 * @author Abhishek Gupta
 *
 */
@Theme("dashboard")
@Title("Finance Management Dashboard")
public class DashboardUI extends UI {

    private static final String SIGN_OUT = "Sign Out";
	private static final String EXIT = "Exit";
	private static final String MY_ACCOUNT = "My Account";
	private static final String PREFERENCES = "Preferences";
	private static final String SETTINGS = "Settings";
	private static final String SIGN_IN = "Sign In";
	private static final long serialVersionUID = 1L;

	private CssLayout root = new CssLayout();
    private VerticalLayout loginLayout;
    private CssLayout menu = new CssLayout();
    private CssLayout content = new CssLayout();

    HashMap<String, Class<? extends View>> routes = new HashMap<String, Class<? extends View>>() {
		private static final long serialVersionUID = -8100418558296244344L;
		{
            //put("/dashboard", DashboardView.class);
            //put("/transactions", MortgageTransactionView.class);
            put("/retailbilling", RetailInvoiceView.class);
        }
    };

    private HashMap<String, Button> viewNameToMenuButton = new HashMap<String, Button>();
    public Navigator nav;
    private HelpManager helpManager = new HelpManager();

    @Override
    protected void init(final VaadinRequest request) {
    	VaadinSession.getCurrent().getSession().setMaxInactiveInterval(1800);
        setLocale(Locale.US);
        setContent(root);
        root.addStyleName("root");
        root.setSizeFull();

        Label bg = new Label();
        bg.setSizeUndefined();
        bg.addStyleName("login-bg");
        root.addComponent(bg);

        buildLoginView(false);
    }

    private void buildLoginView(final boolean exit) {
        if (exit) {
            root.removeAllComponents();
        }
        helpManager.closeAll();
        HelpOverlay w = helpManager.addOverlay(
                        "Welcome to the Apna Jewellery Billing",
                        "<p>No username or password is required currently at this time, just click the (Sign In) button to continue.</p>",
                        "login");
        w.center();
        addWindow(w);
        addStyleName("login");

        loginLayout = new VerticalLayout();
        loginLayout.setSizeFull();
        loginLayout.addStyleName("login-layout");
        root.addComponent(loginLayout);

        HorizontalLayout labels = new HorizontalLayout();
        labels.setWidth("100%");
        labels.setMargin(true);
        labels.addStyleName("labels");
        
        final CssLayout loginPanel = new CssLayout();
        loginPanel.addStyleName("login-panel");
        loginPanel.addComponent(labels);

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName("h4");
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

        Label title = new Label("Apna Jewellery Billing");
        title.setSizeUndefined();
        title.addStyleName("h2");
        title.addStyleName("light");
        labels.addComponent(title);
        labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);

        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.setMargin(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Username");
        username.focus();
        fields.addComponent(username);

        final PasswordField password = new PasswordField("Password");
        fields.addComponent(password);

        final Button signin = new Button(SIGN_IN);
        signin.addStyleName("default");
        fields.addComponent(signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        final ShortcutListener enter = new ShortcutListener(SIGN_IN, KeyCode.ENTER, null) {
			private static final long serialVersionUID = 1L;

			@Override
            public void handleAction(Object sender, Object target) {
                signin.click();
            }
        };

        signin.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
            public void buttonClick(ClickEvent event) {
                if (username.getValue() != null
                        && username.getValue().equals("")
                        && password.getValue() != null
                        && password.getValue().equals("")) {
                    signin.removeShortcutListener(enter);
                    buildMainView();
                } else {
                    if (loginPanel.getComponentCount() > 2) {
                        loginPanel.removeComponent(loginPanel.getComponent(2));
                    }
                    Label error = new Label(
                            "Wrong username or password. <span>Hint: try empty values</span>",
                            ContentMode.HTML);
                    error.addStyleName("error");
                    error.setSizeUndefined();
                    error.addStyleName("light");
                    // Add animation
                    error.addStyleName("v-animate-reveal");
                    loginPanel.addComponent(error);
                    username.focus();
                }
            }
        });

        signin.addShortcutListener(enter);
        loginPanel.addComponent(fields);
        loginLayout.addComponent(loginPanel);
        loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
    }

    private void buildMainView() {
        nav = new Navigator(this, content);
        for (String route : routes.keySet()) {
            nav.addView(route, routes.get(route));
        }
        helpManager.closeAll();
        removeStyleName("login");
        root.removeComponent(loginLayout);
        root.addComponent(new HorizontalLayout() {
            {
                setSizeFull();
                addStyleName("main-view");
                addComponent(new VerticalLayout() {
                    {
                        addStyleName("sidebar");
                        setWidth(null);
                        setHeight("100%");
                        addComponent(new CssLayout() {
                            {
                                addStyleName("branding");
                                Label logo = new Label(
                                        "<span>Apna Jewellery</span> Dashboard",
                                        ContentMode.HTML);
                                logo.setSizeUndefined();
                                addComponent(logo);
                            }
                        });

                        // Main menu
                        addComponent(menu);
                        setExpandRatio(menu, 1);

                        // User menu
                        addComponent(new VerticalLayout() {
							private static final long serialVersionUID = 1L;

							{
                                setSizeUndefined();
                                addStyleName("user");
                                Image profilePic = new Image(null, new ThemeResource("img/profile-pic.png"));
                                profilePic.setWidth("34px");
                                addComponent(profilePic);
                                Label userName = new Label("Abhishek Gupta");
                                userName.setSizeUndefined();
                                addComponent(userName);

                                Command cmd = new Command() {
									private static final long serialVersionUID = 1L;

									@Override
                                    public void menuSelected(
                                            MenuItem selectedItem) {
                                        Notification.show("Not implemented in this demo");
                                    }
                                };
                                MenuBar settings = new MenuBar();
                                MenuItem settingsMenu = settings.addItem("", null);
                                settingsMenu.setStyleName("icon-cog");
                                settingsMenu.addItem(SETTINGS, cmd);
                                settingsMenu.addItem(PREFERENCES, cmd);
                                settingsMenu.addSeparator();
                                settingsMenu.addItem(MY_ACCOUNT, cmd);
                                addComponent(settings);

                                Button exit = new NativeButton(EXIT);
                                exit.addStyleName("icon-cancel");
                                exit.setDescription(SIGN_OUT);
                                addComponent(exit);
                                exit.addClickListener(new ClickListener() {
                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        buildLoginView(true);
                                    }
                                });
                            }
                        });
                    }
                });
                addComponent(content);
                content.setSizeFull();
                content.addStyleName("view-content");
                setExpandRatio(content, 1);
            }
        });

        menu.removeAllComponents();
        //for (final String view : new String[] { "dashboard", "transactions", "retailbilling"}) {
        for (final String view : new String[] {"retailbilling"}) {
            Button b = new NativeButton(view.substring(0, 1).toUpperCase()
                    + view.substring(1).replace('-', ' '));
            b.addStyleName("icon-" + view);
            b.addClickListener(event->{
            	clearMenuSelection();
                event.getButton().addStyleName("selected");
                if (!nav.getState().equals("/" + view))
                    nav.navigateTo("/" + view);
            });

            if (view.equals("reports")) {
                DragAndDropWrapper reports = new DragAndDropWrapper(b);
                reports.setDragStartMode(DragStartMode.NONE);
                reports.setDropHandler(new DropHandler() {

                    @Override
                    public void drop(DragAndDropEvent event) {
                        clearMenuSelection();
                        viewNameToMenuButton.get("/reports").addStyleName(
                                "selected");
                        Transferable items = event.getTransferable();
                        nav.navigateTo("/reports");
                    }

                    @Override
                    public AcceptCriterion getAcceptCriterion() {
                        return AcceptItem.ALL;
                    }

                });
                menu.addComponent(reports);
                menu.addStyleName("no-vertical-drag-hints");
                menu.addStyleName("no-horizontal-drag-hints");
            } else {
                menu.addComponent(b);
            }

            viewNameToMenuButton.put("/" + view, b);
        }
        menu.addStyleName("menu");
        menu.setHeight("100%");

//        viewNameToMenuButton.get("/dashboard").setHtmlContentAllowed(true);
//        viewNameToMenuButton.get("/dashboard").setCaption(
//                "Dashboard<span class=\"badge\">2</span>");
        

        String uriFragment = Page.getCurrent().getUriFragment();
        if (uriFragment != null && uriFragment.startsWith("!")) {
            uriFragment = uriFragment.substring(1);
        }
        if (uriFragment == null || uriFragment.equals("") || uriFragment.equals("/")) {
            nav.navigateTo("/retailbilling");
            menu.getComponent(0).addStyleName("selected");
        } else {
            nav.navigateTo(uriFragment);
            viewNameToMenuButton.get(uriFragment).addStyleName("selected");
        }

        nav.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                helpManager.closeAll();
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                View newView = event.getNewView();
            }
        });

    }

    private void clearMenuSelection() {
        for (Iterator<Component> it = menu.iterator(); it.hasNext();) {
            Component next = it.next();
            if (next instanceof NativeButton) {
                next.removeStyleName("selected");
            } else if (next instanceof DragAndDropWrapper) {
                ((DragAndDropWrapper) next).iterator().next()
                        .removeStyleName("selected");
            }
        }
    }
}
