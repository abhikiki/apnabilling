package com.retail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.retail.dao.TransactionDAO;
import com.retail.dto.CustomerDTO;
import com.retail.dto.DiamondTransactionItemDTO;
import com.retail.dto.GeneralTransactionItemDTO;
import com.retail.dto.GoldTransactionItemDTO;
import com.retail.dto.PriceDTO;
import com.retail.dto.SilverTransactionItemDTO;
import com.retail.dto.TransactionDTO;

@SpringBootApplication
public class RetailRestApiApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(RetailRestApiApplication.class, args);
//		//ctx.getBean(ShopDAO.class).saveShopInformation();
//		//List<ShopDTO> shopDtoList = ctx.getBean(ShopDAO.class).getShopInformation("admin", "admin");
////		List<ShopDTO> shopDtoList = ctx.getBean(ShopDAO.class).getShopInformation(2);
////		if(!shopDtoList.isEmpty()){
////			ShopDTO shopDto = shopDtoList.get(0);
////			System.out.println(shopDto.getShopId());
////			System.out.println(shopDto.getShopName());
////			System.out.println(shopDto.getTinNumber());
////			System.out.println(shopDto.getUserName());
////			System.out.println(shopDto.getPassword());
////		}
//		
//		//String id = ctx.getBean(RetailTransactionDAO.class).saveRetailTransaction(1, new Date(), "I");
//		//System.out.println("ID=" + id);
//		CustomerDTO customerDTO = new CustomerDTO();
//		customerDTO.setFirstName("SUKKU");
//		customerDTO.setLastName("MUKHIYA");
//		customerDTO.setContactNumber("9199990012");
//		List<GoldTransactionItemDTO> goldItemList = new ArrayList<>();
//		List<SilverTransactionItemDTO> silverItemList = new ArrayList<>();
//		List<DiamondTransactionItemDTO> diamondItemList = new ArrayList<>();
//		List<GeneralTransactionItemDTO> generalItemList = new ArrayList<>();
//		PriceDTO priceDto = new PriceDTO(1d, 200d, 12.23d, 12.34d, 23.3d, 100d, 20.30d, 12.23d);
//		GoldTransactionItemDTO goldDto = new GoldTransactionItemDTO("BRACELET2", "919", 2, "Piece", 100.200d, 200.300d, "net", 300.400d, 5000.899d);
//		SilverTransactionItemDTO silverDto = new SilverTransactionItemDTO("PETHA2", 2, "Pair", 12.12d, .234d, "NET", 347.100d, 12d);
//		DiamondTransactionItemDTO diamondDto = new DiamondTransactionItemDTO("Diamond2", 1, "pair", 0.00d, 0.15d, 4, true, 2345.435d);
//		GeneralTransactionItemDTO generalTransDto = new GeneralTransactionItemDTO("Genera2", 3, "piece", 123.43d, 12.23d, 23.4d);
//		goldItemList.add(goldDto);
//		silverItemList.add(silverDto);
//		diamondItemList.add(diamondDto);
//		generalItemList.add(generalTransDto);
//		
//		TransactionDTO tDto = new TransactionDTO(goldItemList, silverItemList, diamondItemList, generalItemList, customerDTO, priceDto, false, new Date(), "12", -1l, "Abhishek", true, "Test Notes", false);
//				//goldItemList, silverItemList, diamondItemList, generalItemList, customerDTO, priceDto, false, new Date(), 1234, 2, "Abhishek", true, "Test", false);
		
	//	ctx.getBean(TransactionDAO.class).saveTransaction(100, tDto);
		//TransactionDTO dd = ctx.getBean(TransactionDAO.class).getTransaction(3);
		//System.out.println(dd.isEstimateBill());
		//System.out.println(dd.getVinNumber());
		
		
	}
}
