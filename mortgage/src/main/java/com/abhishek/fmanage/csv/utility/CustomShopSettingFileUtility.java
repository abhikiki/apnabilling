/**
 * 
 */
package com.abhishek.fmanage.csv.utility;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hsqldb.util.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.dev.util.collect.HashMap;

/**
 * @author GUPTAA6
 *
 */
public class CustomShopSettingFileUtility {

	private static volatile CustomShopSettingFileUtility instance = null;
	private static final String[] FILE_HEADER_MAPPING = { "VAT_PERCENT",
			"TIN_NUMBER", "GOLD_ITEMS", "SILVER_ITEMS", "DIAMOND_ITEMS", "GENERAL_ITEMS", "STAFF_NAMES" };
	private final Logger logger = LoggerFactory.getLogger(CustomShopSettingFileUtility.class);

	private static double vatPercent = 1;
	private static String tinNumber = "10100310077";
	private static Map<String, List<String>> customSettingMap = new HashMap<>();
	
	private CustomShopSettingFileUtility(){}
	
	public static CustomShopSettingFileUtility getInstance()
	{
		if(instance == null)
		{
			synchronized (CustomShopSettingFileUtility.class) {
				if(instance == null){
					instance = new CustomShopSettingFileUtility();
					instance.initialize();
				}
			}
		}
		return instance;
	}
	
	private void initialize() {
		FileReader fileReader = null;
		CSVParser csvFileParser = null;
		CSVFormat csvFileFormat = CSVFormat.DEFAULT
				.withHeader(FILE_HEADER_MAPPING);
		try {
			// initialize FileWriter object
			ClassLoader classLoader = getClass().getClassLoader();
			File f = new File(classLoader.getResource("CustomBillingSettings.csv").getFile());
			fileReader = new FileReader(f);
			csvFileParser = new CSVParser(fileReader, csvFileFormat);
			List csvRecords = csvFileParser.getRecords();
			// Read the CSV file records starting from the second record to skip
			// the header

			customSettingMap.put("GOLD_ITEMS", new ArrayList<String>());
			customSettingMap.put("SILVER_ITEMS", new ArrayList<String>());
			customSettingMap.put("DIAMOND_ITEMS", new ArrayList<String>());
			customSettingMap.put("GENERAL_ITEMS", new ArrayList<String>());
			customSettingMap.put("STAFF_NAMES", new ArrayList<String>());
			
			for (int i = 1; i < csvRecords.size(); i++) {

				CSVRecord record = (CSVRecord) csvRecords.get(i);
				String vatPercentFromCsv = record.get("VAT_PERCENT");
				String tinNumberFromCsv = record.get("TIN_NUMBER");
				String goldItemNameFromCsv = record.get("GOLD_ITEMS");
				String silverItemNameFromCsv = record.get("SILVER_ITEMS");
				String diamondItemNameFromCsv = record.get("DIAMOND_ITEMS");
				String generalItemNameFromCsv = record.get("GENERAL_ITEMS");
				String staffNamesFromCsv = record.get("STAFF_NAMES");

				if(NumberUtils.isNumber(vatPercentFromCsv)){
					vatPercent = NumberUtils.toDouble(vatPercentFromCsv);
				}
				if(NumberUtils.isNumber(tinNumberFromCsv)){
					tinNumber = tinNumberFromCsv;
				}
				if(!StringUtils.isBlank(goldItemNameFromCsv)){
					ArrayList<String> goldItemList = (ArrayList<String>) customSettingMap.get("GOLD_ITEMS");
					goldItemList.add(goldItemNameFromCsv);
					customSettingMap.put("GOLD_ITEMS", goldItemList);
				}
				if(!StringUtils.isBlank(silverItemNameFromCsv)){
					ArrayList<String> silverItemList = (ArrayList<String>) customSettingMap.get("SILVER_ITEMS");
					silverItemList.add(silverItemNameFromCsv);
					customSettingMap.put("SILVER_ITEMS", silverItemList);
				}
				if(!StringUtils.isBlank(diamondItemNameFromCsv)){
					ArrayList<String> diamondItemList = (ArrayList<String>) customSettingMap.get("DIAMOND_ITEMS");
					diamondItemList.add(diamondItemNameFromCsv);
					customSettingMap.put("DIAMOND_ITEMS", diamondItemList);
				}
				if(!StringUtils.isBlank(generalItemNameFromCsv)){
					ArrayList<String> generalItemList = (ArrayList<String>) customSettingMap.get("GENERAL_ITEMS");
					generalItemList.add(generalItemNameFromCsv);
					customSettingMap.put("GENERAL_ITEMS", generalItemList);
				}
				if(!StringUtils.isBlank(generalItemNameFromCsv)){
					ArrayList<String> generalItemList = (ArrayList<String>) customSettingMap.get("GENERAL_ITEMS");
					generalItemList.add(generalItemNameFromCsv);
					customSettingMap.put("GENERAL_ITEMS", generalItemList);
				}
				if(!StringUtils.isBlank(staffNamesFromCsv)){
					ArrayList<String> staffNamesList = (ArrayList<String>) customSettingMap.get("STAFF_NAMES");
					staffNamesList.add(staffNamesFromCsv);
					customSettingMap.put("STAFF_NAMES", staffNamesList);
				}
			}
		} catch (Exception e) {
			logger.error("Error picking up custom settings", e);
		} finally {
			try {
				fileReader.close();
				csvFileParser.close();
			} catch (Exception e) {
				logger.error("Error while flushing/closing fileReader/csvParser !!!", e);
			}
		}
	}
	
	public Double getVatPercentage(){
		return vatPercent;
	}
	
	public String getTinNumber(){
		return tinNumber;
	}
	
	public List<String> getGoldItemsList(){
		return customSettingMap.get("GOLD_ITEMS");
	}
	
	public List<String> getSilverItemsList(){
		return customSettingMap.get("SILVER_ITEMS");
	}
	
	public List<String> getDiamondItemsList(){
		return customSettingMap.get("DIAMOND_ITEMS");
	}
	
	public List<String> getGeneralItemsList(){
		return customSettingMap.get("GENERAL_ITEMS");
	}
	
	public List<String> getStaffNameList(){
		return customSettingMap.get("STAFF_NAMES");
	}
}
