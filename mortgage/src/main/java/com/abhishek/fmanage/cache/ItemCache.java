/**
 * 
 */
package com.abhishek.fmanage.cache;

import java.util.List;
import java.util.Map;

import com.abhishek.fmanage.retail.dto.ItemDTO;
import com.abhishek.fmanage.retail.dto.SellingItemsDTO;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.restclient.service.RestItemService;
import com.google.gwt.dev.util.collect.HashMap;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;

/**
 * @author guptaa6
 *
 */
public class ItemCache {
	private static volatile ItemCache instance = null;
	private static Map<String, List<ItemDTO>> itemMap = new HashMap<>();

	private ItemCache(){}

	public static ItemCache getInstance()
	{
		if(instance == null)
		{
			synchronized (ItemCache.class) {
				if(instance == null){
					instance = new ItemCache();
					instance.initializeCache();
				}
			}
		}
		return instance;
	}

	public Map<String, List<ItemDTO>> getItemMap(){
		return itemMap;
	}
	
	private void initializeCache() {
		RestItemService service = new RestItemService();
		ShopDTO shopDto =  (ShopDTO)UI.getCurrent().getSession().getAttribute(ShopDTO.class);
		SellingItemsDTO dto = service.getItems(shopDto.getShopId());
		itemMap.put("GOLD", dto.getGoldItemsList());
		itemMap.put("SILVER", dto.getSilverItemsList());
		itemMap.put("DIAMOND", dto.getDiamondItemsList());
		itemMap.put("GENERAL", dto.getGeneralItemsList());
	}
}
