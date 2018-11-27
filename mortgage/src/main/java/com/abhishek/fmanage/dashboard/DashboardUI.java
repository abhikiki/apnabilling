package com.abhishek.fmanage.dashboard;

import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.restclient.service.RestRetailLoginService;
import com.abhishek.fmanage.retail.views.*;
import com.abhishek.fmanage.retail.window.ItemEntryWindow;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;


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

    private String[] adminViews = new String[] {"dashboard", "retailbilling", "transactions", "sms", "purchaserecord","mortgage", "mortgagetransaction", "wholesale"};
    private String[] adminExcludingMortgageViews = new String[] {"dashboard", "retailbilling", "transactions", "purchaserecord", "sms"};
    private String[] staffViews = new String[] {"retailbilling"};
    private String currentRole = "ADMIN";
    
    HashMap<String, Class<? extends View>> routes = new HashMap<String, Class<? extends View>>() {
		private static final long serialVersionUID = -8100418558296244344L;
		{
			put("/dashboard", DashboardView.class);
            put("/retailbilling", RetailInvoiceView.class);
            put("/transactions", RetailTransactionSearchView.class);
            put("/sms", SmsView.class);
            put("/purchaserecord", PurchaseRecordView.class);
            put("/mortgage", MortgageView.class);
            put("/mortgagetransaction", MortgageTransactionSearchView.class);
            put("/wholesale", RetailTransactionSearchView.class);
        }
    };

    private HashMap<String, Button> viewNameToMenuButton = new HashMap<String, Button>();
    public Navigator nav;
    private HelpManager helpManager = new HelpManager();

    @Override
    protected void init(final VaadinRequest request) {
    	VaadinSession.getCurrent().getSession().setMaxInactiveInterval(-1800);
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
                        "Welcome to the Abhishek Retail Billing",
                        "<p>Enter your login credentials</p>",
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
        username.setIcon(FontAwesome.USER);
        username.focus();
        fields.addComponent(username);

        final PasswordField password = new PasswordField("Password");
        password.setIcon(FontAwesome.LOCK);
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
				String userName = username.getValue();
				String userPassword = password.getValue();
				ShopDTO shopDto = new RestRetailLoginService(userName, userPassword).retailLogin();
                if (shopDto.getShopId() != -1L) {
                	getUI().getSession().setAttribute(ShopDTO.class, shopDto);
                	currentRole = shopDto.getRole();
                	//VaadinService.getCurrentRequest().getWrappedSession().setAttribute("shopdto", shopDto);
                    signin.removeShortcutListener(enter);
                    buildMainView();
                } else {
                    if (loginPanel.getComponentCount() > 2) {
                        loginPanel.removeComponent(loginPanel.getComponent(2));
                    }
                    Label error = new Label("Wrong username or password", ContentMode.HTML);
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
        
        String[] currentView = getViewBasedOnRole();
       
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
                                //ShopDTO shopDto = (ShopDTO)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("shopdto");
                                ShopDTO shopDto = (ShopDTO) getCurrent().getSession().getAttribute(ShopDTO.class);
                                //ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute("shopdto");
                                Label logo = new Label(
                                        "<span>" + shopDto.getShopName() + "</span> Dashboard",
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
                                MenuItem addBillingItemMenu = settingsMenu.addItem("Add Billing Item", cmd);
                               // addBillingItemMenu.setStyleName("icon-cog");
                                addBillingItemMenu.addItem("GOLD", (selectedItem) -> {
                                	Window bw = new ItemEntryWindow(selectedItem.getText());
                                	UI.getCurrent().addWindow(bw);
                            		bw.focus();
                                	});
                                addBillingItemMenu.addItem("SILVER", (selectedItem) -> {
                                	Window bw = new ItemEntryWindow(selectedItem.getText());
                                	UI.getCurrent().addWindow(bw);
                            		bw.focus();
                                	});
                                addBillingItemMenu.addItem("DIAMOND", (selectedItem) -> {
                                	Window bw = new ItemEntryWindow(selectedItem.getText());
                                	UI.getCurrent().addWindow(bw);
                            		bw.focus();
                                	});
                                addBillingItemMenu.addItem("GENERAL", (selectedItem) -> {
                                	Window bw = new ItemEntryWindow(selectedItem.getText());
                                	UI.getCurrent().addWindow(bw);
                            		bw.focus();
                                	});
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
        
        for (final String view : currentView) {
            Button b = new NativeButton(view.substring(0, 1).toUpperCase() + view.substring(1).replace('-', ' '));
            b.addStyleName("icon-" + view);
            b.addClickListener(event->{
            	clearMenuSelection();
                event.getButton().addStyleName("selected");
                if (!nav.getState().equals("/" + view))
                	
                    nav.navigateTo("/" + view);
            });
            menu.addComponent(b);
            viewNameToMenuButton.put("/" + view, b);
        }
        menu.addStyleName("menu");
        menu.setHeight("100%");

        String uriFragment = Page.getCurrent().getUriFragment();
        if (uriFragment != null && uriFragment.startsWith("!")) {
            uriFragment = uriFragment.substring(1);
        }
        if (uriFragment == null || uriFragment.equals("") || uriFragment.equals("/")) {
        	if(currentRole.equalsIgnoreCase("ADMIN")){
        		nav.navigateTo("/dashboard");
        	}else{
        		nav.navigateTo("/retailbilling");
        	}
            
            menu.getComponent(0).addStyleName("selected");
        } else {
        	if(currentRole.equalsIgnoreCase("STAFF")){
        		uriFragment = "/retailbilling";
        		nav.navigateTo(uriFragment);
        	}else{
        		uriFragment = "/dashboard";
        		nav.navigateTo(uriFragment);
        	}
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

	private String[] getViewBasedOnRole() {
		String[] currentView = new String[]{};
		switch(currentRole.toUpperCase()){
			case "ADMIN" :
					currentView = adminViews;
					routes.put("/dashboard", DashboardView.class);
		        	routes.put("/transactions", RetailTransactionSearchView.class);
		        	routes.put("/purchaserecord", PurchaseRecordView.class);
		        	routes.put("/sms", SmsView.class);
		        	routes.put("/mortgage", MortgageView.class);
		        	routes.put("/mortgagetransaction", MortgageTransactionSearchView.class);
                  //  routes.put("/wholesale", WholeSaleView.class);
					break;
			case "STAFF" :
					currentView = staffViews;
					routes.remove("/dashboard");
		         	routes.remove("/transactions");
		         	routes.remove("/purchaserecord", PurchaseRecordView.class);
		         	routes.remove("/sms");
		         	routes.remove("/mortgage");
		         	routes.remove("/mortgagetransaction");
				break;
			case "ADMIN_EXCLUDING_MORTGAGE":
					currentView = adminExcludingMortgageViews;
					routes.put("/dashboard", DashboardView.class);
		        	routes.put("/transactions", RetailTransactionSearchView.class);
		        	routes.put("/purchaserecord", PurchaseRecordView.class);
		        	routes.put("/sms", SmsView.class);
				break;
		}
		return currentView;
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
