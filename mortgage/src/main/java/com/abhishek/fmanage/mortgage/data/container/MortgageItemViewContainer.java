/**
 * 
 */
package com.abhishek.fmanage.mortgage.data.container;

import java.lang.reflect.Constructor;

import com.avathartech.fastformfields.widgets.DecimalTextField;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;

/**
 * @author Abhishek
 *
 */
public class MortgageItemViewContainer extends IndexedContainer
{
    private static final long serialVersionUID = 1L;

    /**
     * {@link Constructor} for Transaction view container
     */
    public MortgageItemViewContainer()
    {
        addContainerProperty("Delete", Image.class, "");
        addContainerProperty("Item Name", TextField.class, "");
        addContainerProperty("Item Weight", DecimalTextField.class, "");
        addContainerProperty("Item Description", TextField.class, "");
    }

    public void addMortgageItem(
        boolean isByTotalWeight,
        String itemName,
        String itemWeight,
        String itemDescription)
    {
        Object mContainerRowId = addItem();
        Item item = getItem(mContainerRowId);
        
        ThemeResource resource = new ThemeResource("img/removeButtonSmall.jpg");
        // Use the resource
           final Image image = new Image("", resource);
           image.setHeight("40px");
           image.setWidth("40px");
           image.setDescription("Remove Item");
           image.setData(mContainerRowId);
           image.addClickListener(new MouseEvents.ClickListener()
           {
               @Override
               public void click(com.vaadin.event.MouseEvents.ClickEvent event)
               {
                   removeItem(image.getData());
               }
           });
           

        if (item != null)
        {
            item.getItemProperty("Delete").setValue(image);
            item.getItemProperty("Item Name").setValue(new TextField("", itemName));
            DecimalTextField decimalTextField = new DecimalTextField("", itemWeight);
            decimalTextField.setImmediate(true);
            decimalTextField.setValidationVisible(true);
            decimalTextField.setEnabled(!isByTotalWeight);
            item.getItemProperty("Item Weight").setValue(decimalTextField);
            item.getItemProperty("Item Description").setValue(new TextField("", itemDescription));
        }
    }
}
