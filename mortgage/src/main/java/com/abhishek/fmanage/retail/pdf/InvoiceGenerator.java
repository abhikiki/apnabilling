package com.abhishek.fmanage.retail.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abhishek.fmanage.csv.utility.CustomShopSettingFileUtility;
import com.abhishek.fmanage.retail.dto.CustomerDTO;
import com.abhishek.fmanage.retail.dto.DiamondTransactionItemDTO;
import com.abhishek.fmanage.retail.dto.GeneralTransactionItemDTO;
import com.abhishek.fmanage.retail.dto.GoldTransactionItemDTO;
import com.abhishek.fmanage.retail.dto.PriceDTO;
import com.abhishek.fmanage.retail.dto.TransactionDTO;
import com.abhishek.fmanage.retail.dto.SilverTransactionItemDTO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;

public class InvoiceGenerator {//implements PdfPTableEvent {
//
//	private final Logger logger = LoggerFactory
//			.getLogger(InvoiceGenerator.class);
//	private static final String BILL_PATH = "BILL_PATH";
//	/** A font that will be used in our PDF. */
//	public static final Font BOLD_UNDERLINED = new Font(FontFamily.TIMES_ROMAN,
//			11, Font.BOLD | Font.UNDERLINE);
//	/** A font that will be used in our PDF. */
//	public static final Font BOLD = new Font(FontFamily.TIMES_ROMAN, 11,
//			Font.BOLD);
//	/** A font that will be used in our PDF. */
//	public static final Font NORMAL = new Font(FontFamily.TIMES_ROMAN, 10);
//
//	private static final String NEW_LINE_SEPARATOR = "\n";
//	// CSV file header
//	private static final Object[] FILE_HEADER = { "Date", "FirstName",
//			"LastName", "ContactNumber", "EmailId", "StreetAddress1",
//			"StreetAddress2", "City", "State", "Country", "Zipcode" };
//
//
//	private Calendar cal = Calendar.getInstance();
//
//	private TransactionDTO retailTransaction;
//
//	public InvoiceGenerator(TransactionDTO retailTransaction) {
//		this.retailTransaction = retailTransaction;
//	}
//
//
//	/**
//	 * Creates a PDF document.
//	 *
//	 * @param isEstimateBill
//	 * @param vinNumber
//	 * @param includePrice
//	 * @param invoiceDate
//	 * @param notes
//	 * @param invoiceNumber
//	 * @param filename
//	 *            the path to the new PDF document
//	 * @throws DocumentException
//	 * @throws IOException
//	 * @throws SQLException
//	 */
//	public String createPdf() throws SQLException, DocumentException, IOException {
//		Date invoiceDate = retailTransaction.getTransactionDate();
//		Boolean isEstimateBill = retailTransaction.isEstimateBill();
//		String invoiceNumber = retailTransaction.getInvoiceNumber().toString();
//		String staffName = retailTransaction.getDealingStaffName();
//		Boolean includePrice = retailTransaction.getIncludePrice();
//		String vinNumber = retailTransaction.getVinNumber();
//		String notes = retailTransaction.getNotes();
//		cal.setTime(invoiceDate);
//		Document document = new Document(PageSize.A4);
//		String filePath = getFilePath(isEstimateBill, invoiceDate);
//		File file = new File(filePath);
//		PdfWriter.getInstance(document, new FileOutputStream(file));
//
//		// step 3
//		document.open();
//		// step 4
//		document.add(new Paragraph(" "));
//		document.add(new Paragraph(" "));
//		document.add(new Paragraph(" "));
//		document.add(new Paragraph(" "));
//
//		// document.add(generateBarCode(writer));
//		addInvoiceType(document, isEstimateBill, invoiceDate, invoiceNumber);
//		document.add(new Paragraph(" "));
//		addDateVinInformation(document, invoiceDate, isEstimateBill, vinNumber);
//		document.add(new Paragraph(" "));
//
//		addCustomerInformation(document);
//		document.add(new Paragraph(" "));
//
//		addGoldItemsInformation(document, includePrice);
//		document.add(new Paragraph(" "));
//
//		addSilverItemsInformation(document, includePrice);
//		document.add(new Paragraph(" "));
//
//		addDiamondItemsInformation(document, includePrice);
//		document.add(new Paragraph(" "));
//
//		addGeneralItemsInformation(document, includePrice);
//		document.add(new Paragraph(" "));
//
//		addPriceInformationTable(document, isEstimateBill);
//		document.add(new Paragraph(" "));
//
//		addNotes(document, notes);
//		document.add(new Paragraph(" "));
//
//		if (!isEstimateBill) {
//			document.add(new Paragraph(" "));
//
//		}
//		addSignature(document, isEstimateBill, staffName);
//		document.add(new Paragraph(" "));
//
//		document.newPage();
//
//		// step 5
//		document.close();
//		saveCustomerInformation(retailTransaction.getCustomer(), invoiceDate);
//		return filePath;
//
//	}
//
//	private void addSignature(Document document, boolean isEstimateBill,
//			String staffName) throws DocumentException, IOException {
//		PdfPTable table;
//		table = new PdfPTable(3);
//		table.setWidthPercentage(100f);
//		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
//		table.getDefaultCell().setPadding(1);
//		table.getDefaultCell().setUseAscender(true);
//		table.getDefaultCell().setUseDescender(true);
//		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		table.getDefaultCell().setBorder(0);
//		table.getDefaultCell().setBackgroundColor(new BaseColor(243, 175, 250));
//		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
//				BaseFont.CP1252, BaseFont.EMBEDDED);
//		Font headerFont = new Font(baseFont, 11, Font.BOLD);
//		Font rowFont = new Font(baseFont, 10, Font.NORMAL);
//
//		if (!isEstimateBill) {
//			Phrase authorizedSignatory = new Phrase();
//			authorizedSignatory.add(new Chunk("Authorized Signatory",
//					headerFont));
//			table.addCell(authorizedSignatory);
//			Phrase customerSignature = new Phrase();
//			customerSignature.add(new Chunk("Customer Signature", headerFont));
//			table.addCell(customerSignature);
//
//		} else {
//			Phrase authorizedSignatory = new Phrase();
//			authorizedSignatory.add(new Chunk("", headerFont));
//			table.addCell(authorizedSignatory);
//			Phrase customerSignature = new Phrase();
//			customerSignature.add(new Chunk("", headerFont));
//			table.addCell(customerSignature);
//
//		}
//		Phrase staffNamePhrase = new Phrase();
//		staffNamePhrase.add(new Chunk("Dealing Staff: ", headerFont));
//		staffNamePhrase.add(new Chunk(staffName, rowFont));
//		table.addCell(staffNamePhrase);
//		document.add(table);
//	}
//
//	private String getFilePath(final boolean isEstimateBill,
//			final Date invoiceDate) {
//		String filePath = System.getenv(BILL_PATH) + File.separator
//				+ getDirectory(invoiceDate, isEstimateBill) + File.separator;
//		if (isEstimateBill) {
//
//			filePath += "Estimate_";
//
//		} else {
//			filePath += "Invoice_";
//		}
//		filePath += String.valueOf(cal.get(Calendar.YEAR)) + "-"
//				+ String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
//				+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "-"
//				+ String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + "-"
//				+ String.valueOf(cal.get(Calendar.MINUTE)) + "-"
//				+ String.valueOf(cal.get(Calendar.SECOND)) + "-"
//				+ String.valueOf(cal.get(Calendar.MILLISECOND)) + ".pdf";
//
//		return filePath;
//	}
//
//	private void saveCustomerInformation(CustomerDTO customer, Date invoiceDate) {
//		FileWriter fileWriter = null;
//		CSVPrinter csvFilePrinter = null;
//		CSVFormat csvFileFormat = CSVFormat.DEFAULT
//				.withRecordSeparator(NEW_LINE_SEPARATOR);
//		try {
//			// initialize FileWriter object
//			File f = new File(System.getenv(BILL_PATH) + File.separator
//					+ "customerInfo.csv");
//			boolean isFileExist = false;
//			if (isFileExist = f.exists()) {
//				f.createNewFile();
//
//			}
//
//			fileWriter = new FileWriter(f, true);
//			// initialize CSVPrinter object
//			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
//			// Create CSV file header
//			if (!isFileExist) {
//				csvFilePrinter.printRecord(FILE_HEADER);
//			}
//
//			List<String> customerInfoList = new ArrayList<>();
//			customerInfoList.add(String.valueOf(invoiceDate));
//			customerInfoList.add(customer.getFirstName());
//			customerInfoList.add(customer.getLastName());
//			customerInfoList.add(customer.getContactNumber());
//			customerInfoList.add(customer.getEmailId());
//			customerInfoList.add(customer.getStreetAddress1());
//			customerInfoList.add(customer.getStreetAddress2());
//			customerInfoList.add(customer.getCity());
//			customerInfoList.add(customer.getStateprovince());
//			customerInfoList.add(customer.getCountry());
//			customerInfoList.add(customer.getZipcode());
//			csvFilePrinter.printRecord(customerInfoList);
//
//		} catch (Exception e) {
//			logger.error("Error updating customer information", e);
//		} finally {
//			try {
//				fileWriter.flush();
//				fileWriter.close();
//				csvFilePrinter.close();
//			} catch (Exception e) {
//				logger.error(
//						"Error while flushing/closing fileWriter/csvPrinter !!!",
//						e);
//			}
//		}
//	}
//
//	// private com.itextpdf.text.Image generateBarCode(PdfWriter writer, Date
//	// date) throws DocumentException
//	// {
//	// Barcode128 barcd = new Barcode128();
//	// barcd.setCode("10031");
//	// PdfContentByte cb = writer.getDirectContent();
//	// com.itextpdf.text.Image barcode1 = barcd.createImageWithBarcode(cb,
//	// BaseColor.DARK_GRAY, BaseColor.BLUE);
//	// return barcode1;
//	// }
//	private void addInvoiceType(Document document, boolean isEstimateBill,
//			Date invoiceDate, String invoiceNumber) throws DocumentException,
//			IOException {
//		PdfPTable footerTable = getPDFTable(!isEstimateBill, 2);
//		footerTable.getDefaultCell().setBackgroundColor(
//				new BaseColor(252, 175, 175));
//		footerTable.getDefaultCell().setHorizontalAlignment(
//				Element.ALIGN_CENTER);
//		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
//				BaseFont.CP1252, BaseFont.EMBEDDED);
//		Font headerFont = new Font(baseFont, 11, Font.BOLD);
//		Font rowFont = new Font(baseFont, 10, Font.NORMAL);
//		Phrase notePhrase = new Phrase();
//		if (isEstimateBill) {
//
//			notePhrase.add(new Chunk("Estimate Letter", headerFont));
//		} else {
//			notePhrase.add(new Chunk("Retail Invoice", headerFont));
//
//		}
//
//		PdfPCell cell = new PdfPCell(notePhrase);
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		cell.setBackgroundColor(new BaseColor(247, 200, 153));
//		if (!isEstimateBill) {
//			// String InvoiceId = String.valueOf(cal.get(Calendar.YEAR)) + "-"
//			// + String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
//			// + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "-"
//			// + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + "-"
//			// + String.valueOf(cal.get(Calendar.MINUTE)) + "-"
//			// + String.valueOf(cal.get(Calendar.SECOND)) + "-"
//			// + String.valueOf(cal.get(Calendar.MILLISECOND));
//			Phrase p = new Phrase(new Chunk("Invoice Id: ", headerFont));
//			p.add(new Chunk(invoiceNumber, rowFont));
//			footerTable.addCell(p);
//		}
//		footerTable.addCell(cell);
//		document.add(footerTable);
//
//	}
//
//	private void addNotes(Document document, String notes)
//			throws DocumentException, IOException {
//		if (!StringUtils.isBlank(notes)) {
//			PdfPTable footerTable = getPDFTable(true, 1);
//			footerTable.getDefaultCell().setBackgroundColor(
//					new BaseColor(252, 175, 175));
//			BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
//					BaseFont.CP1252, BaseFont.EMBEDDED);
//			Font headerFont = new Font(baseFont, 11, Font.BOLD);
//			Font rowFont = new Font(baseFont, 10, Font.NORMAL);
//			Phrase notePhrase = new Phrase();
//			notePhrase.add(new Chunk("Notes: ", headerFont));
//			notePhrase.add(new Chunk(notes, rowFont));
//			footerTable.addCell(notePhrase);
//			document.add(footerTable);
//		}
//
//	}
//
//	private void addGeneralItemsInformation(Document document,
//			boolean includePrice) throws DocumentException, IOException {
//		PdfPTable table;
//		if (includePrice) {
//			table = new PdfPTable(new float[] { 1, 1, 0.5f, 1, 0.7f, 1.8f });
//		} else {
//			table = new PdfPTable(5);
//		}
//		table.setWidthPercentage(100f);
//		table.getDefaultCell().setPadding(1);
//		table.getDefaultCell().setUseAscender(true);
//		table.getDefaultCell().setUseDescender(true);
//		table.getDefaultCell().setFixedHeight(22f - getTotalItems() * 0.5f);
//		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		table.getDefaultCell().setBackgroundColor(new BaseColor(255, 209, 179));
//		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
//				BaseFont.CP1252, BaseFont.EMBEDDED);
//		Font headerFont = new Font(baseFont, 11, Font.BOLD);
//		Font rowFont = new Font(baseFont, 10, Font.NORMAL);
//
//		String columnNames[] = new String[] { "Price", "General Item", "Qty",
//				"Pc/Pair", "Wt(gms)", "Price per(piece/pair)" };
//		if (retailTransaction.getGeneralTransactionItemBeanList().size() > 0) {
//			for (int i = 0; i < columnNames.length; i++) {
//				if ((i == 0) && includePrice == false)
//					continue;
//				table.addCell(new Phrase(columnNames[i], headerFont));
//			}
//
//			List<GeneralTransactionItemDTO> generalItemsTransactionList = retailTransaction.getGeneralTransactionItemBeanList();
//			generalItemsTransactionList
//					.forEach(item -> {
//						if (includePrice) {
//							table.addCell(new Phrase(item.getItemPrice().toString(),
//									rowFont));
//						}
//						table.addCell(new Phrase(String.valueOf(item.getItemName()), rowFont));
//						table.addCell(new Phrase(String.valueOf(item.getQuantity()), rowFont));
//						table.addCell(new Phrase(String.valueOf(item.getPiecepair()), rowFont));
//						table.addCell(new Phrase(item.getWeight() > 0.0 ? item.getWeight().toString() : "N/A", rowFont));
//						table.addCell(new Phrase(item.getPricePerPiecepair().toString(), rowFont));
//					});
//			document.add(table);
//			PdfPTable footerTable = getPDFTable(true, 1);
//			footerTable.getDefaultCell().setBackgroundColor(
//					new BaseColor(255, 209, 179));
//
//			Phrase totalGeneralPricePhrase = new Phrase();
//			totalGeneralPricePhrase.add(new Chunk("Total Items Price(INR) = ",
//					headerFont));
//			totalGeneralPricePhrase.add(new Chunk(String.format("%.3f",
//					generalItemsTransactionList.stream().map(item -> item.getItemPrice()).reduce((price1, price2) -> price1 + price2).get(), rowFont)));
//			footerTable.addCell(new Phrase(totalGeneralPricePhrase));
//			document.add(footerTable);
//		}
//	}
//
//	private void addDiamondItemsInformation(Document document, boolean includePrice) throws DocumentException, IOException {
//		PdfPTable table;
//		if (includePrice) {
//			table = new PdfPTable(new float[] { 1.2f, 1, 0.4f, 0.9f, 0.9f,
//					1.0f, 1.3f, 1.4f });
//		} else {
//			table = new PdfPTable(7);
//		}
//		table.setWidthPercentage(100f);
//		table.getDefaultCell().setPadding(1);
//		table.getDefaultCell().setUseAscender(true);
//		table.getDefaultCell().setUseDescender(true);
//		table.getDefaultCell().setFixedHeight(22f - getTotalItems() * 0.5f);
//		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		table.getDefaultCell().setBackgroundColor(new BaseColor(189, 202, 242));
//		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
//				BaseFont.CP1252, BaseFont.EMBEDDED);
//		Font headerFont = new Font(baseFont, 11, Font.BOLD);
//		Font rowFont = new Font(baseFont, 10, Font.NORMAL);
//
//		String columnNames[] = new String[] { "Price", "Diamond Items", "Qty",
//				"Pc/Pair", "Gold Wt(gms)", "Diamond Wt(Carat)",
//				"#Diamond Piece", "Certificate" };
//		List<DiamondTransactionItemDTO> diamondItemsTransactionList = retailTransaction.getDiamondTransactionItemBeanList();
//
//		if (!diamondItemsTransactionList.isEmpty()) {
//			for (int i = 0; i < columnNames.length; i++) {
//				if ((i == 0) && includePrice == false)
//					continue;
//				table.addCell(new Phrase(columnNames[i], headerFont));
//			}
//			diamondItemsTransactionList
//			.forEach(item -> {
//				if (includePrice) {
//					table.addCell(new Phrase(item.getItemPrice().toString(),
//							rowFont));
//				}
//				table.addCell(new Phrase(String.valueOf(item.getItemName()), rowFont));
//				table.addCell(new Phrase(String.valueOf(item.getQuantity()), rowFont));
//				table.addCell(new Phrase(String.valueOf(item.getPiecePair()), rowFont));
//				table.addCell(new Phrase(item.getGoldWeight().toString(), rowFont));
//				table.addCell(new Phrase(item.getDiamondWeightCarat().toString(), rowFont));
//				table.addCell(new Phrase(String.valueOf(item.getDiamondPieceCount()), rowFont));
//				table.addCell(new Phrase(item.isCertified() ? "YES" : "NO", rowFont));
//			});
//
//			document.add(table);
//			PdfPTable footerTable = getPDFTable(true, 3);
//			footerTable.getDefaultCell().setBackgroundColor(
//					new BaseColor(189, 202, 242));
//
//			Phrase totalDiamondPricePhrase = new Phrase();
//			totalDiamondPricePhrase.add(new Chunk(
//					"Total Diamond Price(INR) = ", headerFont));
//			totalDiamondPricePhrase.add(new Chunk(String.format("%.3f", diamondItemsTransactionList.stream().map(item -> item.getItemPrice()).reduce((price1, price2) -> price1 + price2).get(), rowFont)));
//			footerTable.addCell(new Phrase(totalDiamondPricePhrase));
//
//			Phrase totalGoldWeightPhrase = new Phrase();
//			totalGoldWeightPhrase.add(new Chunk("Total Gold Wt(gm) = ", headerFont));
//			totalGoldWeightPhrase.add(new Chunk(String.format("%.3f", diamondItemsTransactionList.stream().map(item -> item.getGoldWeight()).reduce((price1, price2) -> price1 + price2).get(), rowFont)));
//			footerTable.addCell(new Phrase(totalGoldWeightPhrase));
//
//			Phrase totalDiamondWeightPhrase = new Phrase();
//			totalDiamondWeightPhrase.add(new Chunk(
//					"Total Diamond Wt(Carat) = ", headerFont));
//			totalDiamondWeightPhrase.add(new Chunk(String.format("%.2f", diamondItemsTransactionList.stream().map(item -> item.getDiamondWeightCarat()).reduce((price1, price2) -> price1 + price2).get(), rowFont)));
//			footerTable.addCell(new Phrase(totalDiamondWeightPhrase));
//
//			document.add(footerTable);
//		}
//	}
//
//	private void addSilverItemsInformation(Document document,
//			boolean includePrice) throws DocumentException, IOException {
//		PdfPTable table;
//		if (includePrice) {
//			table = new PdfPTable(new float[] { 1, 1, 0.4f, 0.8f, 0.8f, 1.2f,
//					1.2f, 1.2f });
//		} else {
//			table = new PdfPTable(7);
//		}
//		table.setWidthPercentage(100f);
//		table.getDefaultCell().setPadding(1);
//		table.getDefaultCell().setUseAscender(true);
//		table.getDefaultCell().setUseDescender(true);
//		table.getDefaultCell().setFixedHeight(22f - getTotalItems() * 0.5f);
//		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		table.getDefaultCell().setBackgroundColor(new BaseColor(213, 213, 203));
//		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
//				BaseFont.CP1252, BaseFont.EMBEDDED);
//		Font headerFont = new Font(baseFont, 11, Font.BOLD);
//		Font rowFont = new Font(baseFont, 10, Font.NORMAL);
//
//		String columnNames[] = new String[] { "Price", "Silver Items", "Qty",
//				"Pc/Pair", "Wt(gms)", "MakingCost", "MkCostType", "Rate-pergm" };
//		List<SilverTransactionItemDTO> silverItemsTransactionList = retailTransaction.getSilverTransactionItemBeanList();
//		if (silverItemsTransactionList.size() > 0) {
//			for (int i = 0; i < columnNames.length; i++) {
//				if ((i == 0) && includePrice == false)
//					continue;
//				table.addCell(new Phrase(columnNames[i], headerFont));
//			}
//
//
//			silverItemsTransactionList
//			.forEach(item -> {
//				if (includePrice) {
//					table.addCell(new Phrase(item.getSilverItemPrice().toString(),
//							rowFont));
//				}
//				table.addCell(new Phrase(String.valueOf(item.getItemName()), rowFont));
//				table.addCell(new Phrase(String.valueOf(item.getQuantity()), rowFont));
//				table.addCell(new Phrase(String.valueOf(item.getPiecepair()), rowFont));
//				table.addCell(new Phrase(item.getWeight().toString(), rowFont));
//				table.addCell(new Phrase(item.getMakingCharge().toString(), rowFont));
//				table.addCell(new Phrase(item.getMakingChargeType(), rowFont));
//				table.addCell(new Phrase(item.getSilverRate().toString(), rowFont));
//			});
//
//
//			document.add(table);
//			PdfPTable footerTable = getPDFTable(true, 2);
//			footerTable.getDefaultCell().setBackgroundColor(
//					new BaseColor(213, 213, 203));
//			Phrase totalSilverPricePhrase = new Phrase();
//			totalSilverPricePhrase.add(new Chunk("Total Silver Price(INR) = ",
//					headerFont));
//			totalSilverPricePhrase.add(new Chunk(String.format("%.3f", silverItemsTransactionList.stream().map(item -> item.getSilverItemPrice()).reduce((price1, price2) -> price1 + price2).get(), rowFont)));
//			footerTable.addCell(new Phrase(totalSilverPricePhrase));
//			Phrase totalSilverWeightPhrase = new Phrase();
//			totalSilverWeightPhrase.add(new Chunk("Total Silver Wt(gm) = ",
//					headerFont));
//			totalSilverWeightPhrase.add(new Chunk(String.format("%.3f", silverItemsTransactionList.stream().map(item -> item.getWeight()).reduce((price1, price2) -> price1 + price2).get(), rowFont)));
//			footerTable.addCell(totalSilverWeightPhrase);
//			document.add(footerTable);
//		}
//
//	}
//
//	private void addGoldItemsInformation(Document document, boolean includePrice)
//			throws DocumentException, IOException {
//		PdfPTable table;
//		if (includePrice) {
//			table = new PdfPTable(new float[] { 1.2f, 1.6f, 1, 0.4f, 0.9f,
//					0.9f, 0.8f, 1.3f, 1.4f });
//		} else {
//			table = new PdfPTable(
//					new float[] { 1.3f, 1, 1, 1, 1, 1.3f, 1.3f, 1 });
//		}
//		table.setWidthPercentage(100f);
//		table.getDefaultCell().setPadding(1);
//		table.getDefaultCell().setUseAscender(true);
//		table.getDefaultCell().setUseDescender(true);
//		table.getDefaultCell().setFixedHeight(22f - getTotalItems() * 0.5f);
//		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		table.getDefaultCell().setBackgroundColor(new BaseColor(238, 225, 57));
//		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
//				BaseFont.CP1252, BaseFont.EMBEDDED);
//		Font headerFont = new Font(baseFont, 11, Font.BOLD);
//		Font rowFont = new Font(baseFont, 10, Font.NORMAL);
//
//		String columnNames[] = new String[] { "Price", "Gold Item", "GoldType",
//				"Qty", "Pc/Pair", "Wt(gms)", "MakingCost", "MkCostType",
//				"Rate-pergm" };
//		List<GoldTransactionItemDTO> goldItemsTransactionList = retailTransaction.getGoldTransactionItemBeanList();
//
//		if (!goldItemsTransactionList.isEmpty()) {
//			for (int i = 0; i < columnNames.length; i++) {
//				if ((i == 0) && includePrice == false)
//					continue;
//				table.addCell(new Phrase(columnNames[i], headerFont));
//			}
//
//		goldItemsTransactionList
//			.forEach(golItem -> {
//				if (includePrice) {
//					table.addCell(new Phrase(golItem.getGoldItemPrice().toString(), rowFont));
//				}
//				table.addCell(new Phrase(String.valueOf(golItem.getGoldItemName()), rowFont));
//				table.addCell(new Phrase(golItem.getGoldType(), rowFont));
//				table.addCell(new Phrase(String.valueOf(golItem.getQuantity()), rowFont));
//				table.addCell(new Phrase(golItem.getPiecePair(), rowFont));
//				table.addCell(new Phrase(golItem.getWeight().toString(), rowFont));
//				table.addCell(new Phrase(golItem.getMakingCharge().toString(), rowFont));
//				table.addCell(new Phrase(golItem.getMakingChargeType(), rowFont));
//				table.addCell(new Phrase(golItem.getGoldRate().toString(), rowFont));
//			});
//
//
//
//			document.add(table);
//			PdfPTable footerTable = getPDFTable(true, 2);
//			footerTable.getDefaultCell().setBackgroundColor(
//					new BaseColor(238, 225, 57));
//			Phrase totalGoldPricePhrase = new Phrase();
//			totalGoldPricePhrase.add(new Chunk("Total Gold Price(INR) = ",
//					headerFont));
//			totalGoldPricePhrase.add(new Chunk(String.format("%.3f", goldItemsTransactionList.stream().map(item -> item.getGoldItemPrice()).reduce((price1, price2) -> price1 + price2).get(), rowFont)));
//			footerTable.addCell(new Phrase(totalGoldPricePhrase));
//			Phrase totalGoldWeightPhrase = new Phrase();
//			totalGoldWeightPhrase.add(new Chunk("Total Gold Wt(gm) = ",
//					headerFont));
//			totalGoldWeightPhrase.add(new Chunk(String.format("%.3f", goldItemsTransactionList.stream().map(item -> item.getWeight()).reduce((price1, price2) -> price1 + price2).get(), rowFont)));
//			footerTable.addCell(totalGoldWeightPhrase);
//			document.add(footerTable);
//		}
//	}
//
//
//	private PdfPTable getPDFTable(boolean includePrice, int columnCountWithPrice) {
//		PdfPTable table;
//		if (includePrice) {
//			table = new PdfPTable(columnCountWithPrice);
//		} else {
//			table = new PdfPTable(columnCountWithPrice - 1);
//		}
//		table.setWidthPercentage(100f);
//		table.getDefaultCell().setPadding(1);
//		table.getDefaultCell().setUseAscender(true);
//		table.getDefaultCell().setUseDescender(true);
//		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		table.getDefaultCell().setBackgroundColor(new BaseColor(253, 220, 153));
//		return table;
//	}
//
//	private void addDateVinInformation(Document document, Date invoiceDate,
//			boolean isEstimateBill, String vinNumber) throws DocumentException,
//			IOException {
//		PdfPTable table;
//		if (isEstimateBill) {
//			table = new PdfPTable(1);
//		} else {
//			table = new PdfPTable(2);
//		}
//		table.setWidthPercentage(100f);
//		table.getDefaultCell().setPadding(1);
//		table.getDefaultCell().setUseAscender(true);
//		table.getDefaultCell().setUseDescender(true);
//		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		table.getDefaultCell().setBorder(0);
//		table.getDefaultCell().setBackgroundColor(new BaseColor(243, 175, 250));
//		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
//				BaseFont.CP1252, BaseFont.EMBEDDED);
//		Font headerFont = new Font(baseFont, 11, Font.BOLD);
//		Font rowFont = new Font(baseFont, 10, Font.NORMAL);
//
//		Phrase datePhrase = new Phrase();
//		datePhrase.add(new Chunk("Date: ", headerFont));
//		datePhrase.add(new Chunk(invoiceDate.toString(), rowFont));
//		table.addCell(datePhrase);
//		if (!isEstimateBill) {
//			Phrase vinPhrase = new Phrase();
//			vinPhrase.add(new Chunk("TIN VAT NO: ", headerFont));
//			vinPhrase.add(new Chunk(vinNumber, rowFont));
//			table.addCell(vinPhrase);
//		}
//		document.add(table);
//
//	}
//
//	private void addPriceInformationTable(Document document,
//			boolean isEstimateBill) throws DocumentException, IOException {
//		PdfPTable table = new PdfPTable(2);
//		table.setWidthPercentage(100f);
//		table.getDefaultCell().setPadding(2);
//		table.getDefaultCell().setUseAscender(true);
//		table.getDefaultCell().setUseDescender(true);
//		// table.getDefaultCell().setColspan(1);
//		table.getDefaultCell().setBorder(0);
//		table.getDefaultCell().setBackgroundColor(new BaseColor(185, 236, 190));
//
//		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
//				BaseFont.CP1252, BaseFont.EMBEDDED);
//		Font headerFont = new Font(baseFont, 10, Font.BOLD);
//		Font rowFont = new Font(baseFont, 9, Font.NORMAL);
//
//		table.addCell(new Phrase(new Chunk("Total Items Price(INR): ",
//				headerFont)));
//		PriceDTO priceBean = retailTransaction.getPriceBean();
//		table.addCell(new Phrase(new Chunk(String.format("%.3f",priceBean.getTotalItemsPrice()), rowFont)));
//
//
//		Double discountPrice = 	retailTransaction.getPriceBean().getDiscount();
//		if (discountPrice > 0) {
//			table.addCell(new Phrase(new Chunk("Discount(INR): ", headerFont)));
//			table.addCell(new Phrase(new Chunk(String.format("%.3f",priceBean.getDiscount(), rowFont))));
//		}
//		if (!isEstimateBill) {
//			table.addCell(new Phrase(new Chunk("Vat("
//					+ String.valueOf(CustomShopSettingFileUtility.getInstance()
//							.getVatPercentage()) + "%) Charge(INR) : ",
//					headerFont)));
//			table.addCell(new Phrase(new Chunk(String.format("%.3f", priceBean.getVatCharge(), rowFont))));
//		}
//		Double oldPurchasePrice = priceBean.getOldPurchase();
//		if (oldPurchasePrice > 0) {
//			table.addCell(new Phrase(new Chunk("Old Purchase(INR) : ",
//					headerFont)));
//			table.addCell(new Phrase(new Chunk(String.format("%.3f", priceBean.getOldPurchase(), rowFont))));
//		}
//		table.addCell(new Phrase(new Chunk("Net Amount(INR) : ", headerFont)));
//		Double advancedPayment = priceBean.getAdvancePaymentAmount();
//		if (advancedPayment > 0) {
//			table.addCell(new Phrase(new Chunk(String.format("%.3f", priceBean.getNetpayableAmount(), rowFont))));
//
//		} else {
//			table.addCell(new Phrase(new Chunk(String
//					.format("%d(round off)", Math.round(priceBean.getNetpayableAmount())),
//					rowFont)));
//		}
//
//		if (isEstimateBill && advancedPayment > 0) {
//			table.addCell(new Phrase(new Chunk("Advance Payment(INR) : ",
//					headerFont)));
//			table.addCell(new Phrase(new Chunk(String.format("%.3f", advancedPayment, rowFont))));
//
//			table.addCell(new Phrase(new Chunk("Balance Amount(INR) : ",
//					headerFont)));
//			table.addCell(new Phrase(new Chunk(String.format("%d(rounded)", Math.round(priceBean.getBalanceAmount())),
//					rowFont)));
//		}
//
//		document.add(table);
//
//	}
//
//	private void addCustomerInformation(Document document)
//			throws DocumentException, IOException {
//		PdfPTable customerTable = new PdfPTable(4);
//		customerTable.setWidthPercentage(100f);
//		customerTable.getDefaultCell().setPadding(2);
//		customerTable.getDefaultCell().setUseAscender(true);
//		customerTable.getDefaultCell().setUseDescender(true);
//		// table.getDefaultCell().setColspan(1);
//		customerTable.getDefaultCell().setBorder(0);
//		customerTable.getDefaultCell().setBackgroundColor(
//				new BaseColor(189, 202, 242));
//		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
//				BaseFont.CP1252, BaseFont.EMBEDDED);
//		Font headerFont = new Font(baseFont, 10, Font.BOLD);
//		Font rowFont = new Font(baseFont, 9, Font.NORMAL);
//		CustomerDTO customer = retailTransaction.getCustomer();
//		customerTable.addCell(new Phrase(new Chunk("Customer Name: ",
//				headerFont)));
//		customerTable.addCell(new Phrase(new Chunk(customer.getFirstName()
//				+ " " + customer.getLastName(), rowFont)));
//
//		customerTable
//				.addCell(new Phrase(new Chunk("Contact No: ", headerFont)));
//		customerTable.addCell(new Phrase(new Chunk(customer.getContactNumber(),
//				rowFont)));
//
//		customerTable.addCell(new Phrase(new Chunk("Street Address1: ",
//				headerFont)));
//		customerTable.addCell(new Phrase(new Chunk(
//				customer.getStreetAddress1(), rowFont)));
//
//		customerTable.addCell(new Phrase(new Chunk("Street Address2: ",
//				headerFont)));
//		customerTable.addCell(new Phrase(new Chunk(
//				customer.getStreetAddress2(), rowFont)));
//
//		customerTable.addCell(new Phrase(new Chunk("Zipcode: ", headerFont)));
//		customerTable.addCell(new Phrase(new Chunk(customer.getZipcode(),
//				rowFont)));
//
//		customerTable
//				.addCell(new Phrase(new Chunk("City/State: ", headerFont)));
//		customerTable.addCell(new Phrase(new Chunk(customer.getCity() + " "
//				+ customer.getStateprovince(), rowFont)));
//
//		customerTable.addCell(new Phrase(new Chunk("Country: ", headerFont)));
//		customerTable.addCell(new Phrase(new Chunk(customer.getCountry(),
//				rowFont)));
//
//		customerTable.addCell(new Phrase(new Chunk("EmailId: ", headerFont)));
//		customerTable.addCell(new Phrase(new Chunk(customer.getEmailId(),
//				rowFont)));
//
//		document.add(customerTable);
//	}
//
//	/**
//	 * Draws a background for every other row.
//	 *
//	 * @see com.itextpdf.text.pdf.PdfPTableEvent#tableLayout(com.itextpdf.text.pdf.PdfPTable,
//	 *      float[][], float[], int, int,
//	 *      com.itextpdf.text.pdf.PdfContentByte[])
//	 */
//	@Override
//	public void tableLayout(PdfPTable table, float[][] widths, float[] heights,
//			int headerRows, int rowStart, PdfContentByte[] canvases) {
//		int columns;
//		Rectangle rect;
//		int footer = widths.length - table.getFooterRows();
//		int header = table.getHeaderRows() - table.getFooterRows() + 1;
//		for (int row = header; row < footer; row += 2) {
//			columns = widths[row].length - 1;
//			rect = new Rectangle(widths[row][0], heights[row],
//					widths[row][columns], heights[row + 1]);
//			rect.setBackgroundColor(BaseColor.YELLOW);
//			rect.setBorder(Rectangle.NO_BORDER);
//			canvases[PdfPTable.BASECANVAS].rectangle(rect);
//		}
//	}
//
//	int getTotalItems() {
//		return retailTransaction.getGoldTransactionItemBeanList().size() + retailTransaction.getSilverTransactionItemBeanList().size()
//				+ retailTransaction.getDiamondTransactionItemBeanList().size();
//	}
//
//	String getDirectory(Date invoiceDate, boolean isEstimateBill) {
//		String directoryName = getDirectoryName(invoiceDate, isEstimateBill);
//		File theDir = new File(System.getenv(BILL_PATH) + File.separator
//				+ directoryName);
//
//		// if the directory does not exist, create it
//		if (!theDir.exists()) {
//			System.out
//					.println("Creating Directory: " + System.getenv(BILL_PATH)
//							+ File.separator + directoryName);
//			boolean result = false;
//
//			try {
//				theDir.mkdirs();
//				result = true;
//			} catch (SecurityException se) {
//
//				logger.error("Error creating directory ", se);
//			}
//			if (result) {
//				System.out.println("Directory created");
//			}
//		}
//		return directoryName;
//	}
//
//	String getDirectoryName(Date invoiceDate, boolean isEstimateBill) {
//		cal.setTime(invoiceDate);
//		String dir = String.valueOf(cal.get(Calendar.YEAR));
//		if (isEstimateBill) {
//			dir += File.separator + "ESTIMATE";
//		} else {
//			dir += File.separator + "INVOICE";
//		}
//		dir += File.separator + String.valueOf(cal.get(Calendar.MONTH) + 1)
//				+ File.separator
//				+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "-"
//				+ String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
//				+ String.valueOf(cal.get(Calendar.YEAR));
//		return dir;
//	}
}
