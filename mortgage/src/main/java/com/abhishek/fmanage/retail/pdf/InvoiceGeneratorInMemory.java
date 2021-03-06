package com.abhishek.fmanage.retail.pdf;

import com.abhishek.fmanage.csv.utility.CustomShopSettingFileUtility;
import com.abhishek.fmanage.retail.dto.*;
import com.abhishek.fmanage.utility.ConvertNumberToWords;
import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.*;
import com.vaadin.server.StreamResource.StreamSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InvoiceGeneratorInMemory implements PdfPTableEvent, StreamSource{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(InvoiceGenerator.class);
	/** A font that will be used in our PDF. */
	public static final Font BOLD_UNDERLINED = new Font(FontFamily.TIMES_ROMAN,
			11, Font.BOLD | Font.UNDERLINE);
	/** A font that will be used in our PDF. */
	public static final Font BOLD = new Font(FontFamily.TIMES_ROMAN, 11,
			Font.BOLD);
	/** A font that will be used in our PDF. */
	public static final Font NORMAL = new Font(FontFamily.TIMES_ROMAN, 10);

	private Calendar cal = Calendar.getInstance();

	private TransactionDTO retailTransaction;
	private long transId = -1L;

	private final ByteArrayOutputStream os = new ByteArrayOutputStream();
	
	public InvoiceGeneratorInMemory(long transId, TransactionDTO retailTransaction) {
		this.retailTransaction = retailTransaction;
		this.transId = transId;
	}

	private void addSignature(Document document, boolean isEstimateBill, String staffName) throws DocumentException, IOException {
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100f);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		table.getDefaultCell().setPadding(1);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setBackgroundColor(new BaseColor(243, 175, 250));
		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 11, Font.BOLD);
		Font rowFont = new Font(baseFont, 10, Font.NORMAL);

		if (!isEstimateBill) {
			Phrase authorizedSignatory = new Phrase();
			authorizedSignatory.add(new Chunk("Authorized Signatory",
					headerFont));
			table.addCell(authorizedSignatory);
			Phrase customerSignature = new Phrase();
			customerSignature.add(new Chunk("Customer Signature", headerFont));
			table.addCell(customerSignature);

		} else {
			Phrase authorizedSignatory = new Phrase();
			authorizedSignatory.add(new Chunk("", headerFont));
			table.addCell(authorizedSignatory);
			Phrase customerSignature = new Phrase();
			customerSignature.add(new Chunk("", headerFont));
			table.addCell(customerSignature);

		}
		Phrase staffNamePhrase = new Phrase();
		staffNamePhrase.add(new Chunk("Dealing Staff: ", headerFont));
		staffNamePhrase.add(new Chunk(staffName, rowFont));
		table.addCell(staffNamePhrase);
		document.add(table);
	}

	private void addInvoiceType(Document document, boolean isEstimateBill,
			String invoiceNumber) throws DocumentException,
			IOException {
		PdfPTable footerTable = getPDFTable(true, 2);
		footerTable.getDefaultCell().setBackgroundColor(
				new BaseColor(252, 175, 175));
		footerTable.getDefaultCell().setHorizontalAlignment(
				Element.ALIGN_CENTER);
		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
				BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 11, Font.BOLD);
		Font rowFont = new Font(baseFont, 10, Font.NORMAL);
		
		if (!isEstimateBill) {
			Phrase p = new Phrase(new Chunk("Invoice No: ", headerFont));
			p.add(new Chunk(invoiceNumber, rowFont));
			footerTable.addCell(p);
		}else{
			Phrase transIdPhrase = new Phrase(new Chunk("Advance Receipt No. : ", headerFont));
			transIdPhrase.add(new Chunk(String.valueOf(retailTransaction.getAdvanceReceiptId()), rowFont));
			footerTable.addCell(transIdPhrase);
		}
		
		
		Phrase notePhrase = new Phrase();
		if (isEstimateBill) {
			notePhrase.add(new Chunk("Advance Bill", headerFont));
		} else {
			notePhrase.add(new Chunk("Retail Invoice", headerFont));
		}

		PdfPCell cell = new PdfPCell(notePhrase);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(new BaseColor(247, 200, 153));
		footerTable.addCell(cell);
		document.add(footerTable);

	}

	private void addNotes(Document document, String notes)
			throws DocumentException, IOException {
		if (!StringUtils.isBlank(notes)) {
			PdfPTable footerTable = getPDFTable(true, 1);
			footerTable.getDefaultCell().setBackgroundColor(
					new BaseColor(252, 175, 175));
			BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
					BaseFont.CP1252, BaseFont.EMBEDDED);
			Font headerFont = new Font(baseFont, 11, Font.BOLD);
			Font rowFont = new Font(baseFont, 10, Font.NORMAL);
			Phrase notePhrase = new Phrase();
			notePhrase.add(new Chunk("Notes: ", headerFont));
			notePhrase.add(new Chunk(notes, rowFont));
			footerTable.addCell(notePhrase);
			document.add(footerTable);
		}

	}

	private void addGeneralItemsInformation(Document document,
			boolean includePrice) throws DocumentException, IOException {
		PdfPTable table;
		if (includePrice) {
			table = new PdfPTable(new float[] { 1, 1, 0.5f, 1, 0.7f, 1.8f });
		} else {
			table = new PdfPTable(5);
		}
		table.setWidthPercentage(100f);
		table.getDefaultCell().setPadding(1);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
		table.getDefaultCell().setFixedHeight(22f - getTotalItems() * 0.5f);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBackgroundColor(new BaseColor(255, 209, 179));
		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
				BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 11, Font.BOLD);
		Font rowFont = new Font(baseFont, 10, Font.NORMAL);

		String columnNames[] = new String[] { "Price", "General Item", "Qty",
				"Pc/Pair", "Wt(gms)", "Price per(piece/pair)" };
		if (retailTransaction.getGeneralTransactionItemBeanList().size() > 0) {
			for (int i = 0; i < columnNames.length; i++) {
				if ((i == 0) && includePrice == false)
					continue;
				table.addCell(new Phrase(columnNames[i], headerFont));
			}

			List<GeneralTransactionItemDTO> generalItemsTransactionList = retailTransaction.getGeneralTransactionItemBeanList();
			generalItemsTransactionList
					.forEach(item -> {
						if (includePrice) {
							table.addCell(new Phrase(String.format("%.2f", item.getItemPrice()),
									rowFont));
						}
						table.addCell(new Phrase(String.valueOf(item.getItemName()), rowFont));
						table.addCell(new Phrase(String.valueOf(item.getQuantity()), rowFont));
						table.addCell(new Phrase(String.valueOf(item.getPiecepair()), rowFont));
						table.addCell(new Phrase(item.getWeight() > 0.0 ? String.format("%.3f", item.getWeight()) : "N/A", rowFont));
						table.addCell(new Phrase(item.getPricePerPiecepair().toString(), rowFont));
					});
			document.add(table);
			PdfPTable footerTable = getPDFTable(true, 1);
			footerTable.getDefaultCell().setBackgroundColor(
					new BaseColor(255, 209, 179));

			Phrase totalGeneralPricePhrase = new Phrase();
			totalGeneralPricePhrase.add(new Chunk("Total Items Price(INR) = ",
					headerFont));
			totalGeneralPricePhrase.add(new Chunk(String.format("%.2f",
					 Math.round(generalItemsTransactionList.stream().map(item -> item.getItemPrice()).reduce((price1, price2) -> price1 + price2).get() * 100.0) / 100.0 , rowFont)));
			footerTable.addCell(new Phrase(totalGeneralPricePhrase));
			document.add(footerTable);
		}
	}

	private void addDiamondItemsInformation(Document document, boolean includePrice) throws DocumentException, IOException {
		PdfPTable table;
		if (includePrice) {
			table = new PdfPTable(new float[] { 1.2f, 1, 0.4f, 0.9f, 0.9f,
					1.0f, 1.3f, 1.4f });
		} else {
			table = new PdfPTable(7);
		}
		table.setWidthPercentage(100f);
		table.getDefaultCell().setPadding(1);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
		table.getDefaultCell().setFixedHeight(22f - getTotalItems() * 0.5f);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBackgroundColor(new BaseColor(189, 202, 242));
		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
				BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 11, Font.BOLD);
		Font rowFont = new Font(baseFont, 10, Font.NORMAL);

		String columnNames[] = new String[] { "Price", "Diamond Items", "Qty",
				"Pc/Pair", "Gold Wt(gms)", "Diamond Wt(Carat)",
				"#Diamond Piece", "Certificate" };
		List<DiamondTransactionItemDTO> diamondItemsTransactionList = retailTransaction.getDiamondTransactionItemBeanList();
		
		if (!diamondItemsTransactionList.isEmpty()) {
			for (int i = 0; i < columnNames.length; i++) {
				if ((i == 0) && includePrice == false)
					continue;
				table.addCell(new Phrase(columnNames[i], headerFont));
			}
			diamondItemsTransactionList
			.forEach(item -> {
				if (includePrice) {
					table.addCell(new Phrase(String.format("%.2f", item.getItemPrice()),
							rowFont));
				}
				String itemName= String.format("%s\n%s", item.getItemName(), "HSN:7113");
				table.addCell(new Phrase(itemName, rowFont));
				table.addCell(new Phrase(String.valueOf(item.getQuantity()), rowFont));
				table.addCell(new Phrase(String.valueOf(item.getPiecePair()), rowFont));
				table.addCell(new Phrase(String.format("%.3f", item.getGoldWeight()), rowFont));
				table.addCell(new Phrase(String.format("%.2f", item.getDiamondWeightCarat()), rowFont));
				table.addCell(new Phrase(String.valueOf(item.getDiamondPieceCount()), rowFont));
				table.addCell(new Phrase(item.isCertified() ? "YES" : "NO", rowFont));
			});
			
			document.add(table);
			PdfPTable footerTable = getPDFTable(true, 3);
			footerTable.getDefaultCell().setBackgroundColor(
					new BaseColor(189, 202, 242));

			Phrase totalDiamondPricePhrase = new Phrase();
			totalDiamondPricePhrase.add(new Chunk(
					"Total Diamond Price(INR) = ", headerFont));
			totalDiamondPricePhrase.add(new Chunk(String.format("%.2f",
					 Math.round(diamondItemsTransactionList.stream().map(item -> item.getItemPrice()).reduce((price1, price2) -> price1 + price2).get() * 100.0) / 100.0 , rowFont)));
			footerTable.addCell(new Phrase(totalDiamondPricePhrase));

			Phrase totalGoldWeightPhrase = new Phrase();
			totalGoldWeightPhrase.add(new Chunk("Total Gold Wt(gm) = ", headerFont));
			totalGoldWeightPhrase.add(new Chunk(String.format("%.3f", diamondItemsTransactionList.stream().map(item -> item.getGoldWeight()).reduce((price1, price2) -> price1 + price2).get(), rowFont)));
			footerTable.addCell(new Phrase(totalGoldWeightPhrase));

			Phrase totalDiamondWeightPhrase = new Phrase();
			totalDiamondWeightPhrase.add(new Chunk(
					"Total Diamond Wt(Carat) = ", headerFont));
			totalDiamondWeightPhrase.add(new Chunk(String.format("%.2f", diamondItemsTransactionList.stream().map(item -> item.getDiamondWeightCarat()).reduce((price1, price2) -> price1 + price2).get(), rowFont)));
			footerTable.addCell(new Phrase(totalDiamondWeightPhrase));

			document.add(footerTable);
		}
	}

	private void addSilverItemsInformation(Document document,
			boolean includePrice) throws DocumentException, IOException {
		PdfPTable table;
		if (includePrice) {
			table = new PdfPTable(new float[] { 1, 1, 0.4f, 0.8f, 0.8f, 1.2f,
					1.2f, 1.2f });
		} else {
			table = new PdfPTable(7);
		}
		table.setWidthPercentage(100f);
		table.getDefaultCell().setPadding(1);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
		table.getDefaultCell().setFixedHeight(22f - getTotalItems() * 0.5f);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBackgroundColor(new BaseColor(213, 213, 203));
		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
				BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 11, Font.BOLD);
		Font rowFont = new Font(baseFont, 10, Font.NORMAL);

		String columnNames[] = new String[] { "Price", "Silver Items", "Qty",
				"Pc/Pair", "Wt(gms)", "MakingCost", "MkCostType", "Rate-pergm" };
		List<SilverTransactionItemDTO> silverItemsTransactionList = retailTransaction.getSilverTransactionItemBeanList();
		if (silverItemsTransactionList.size() > 0) {
			for (int i = 0; i < columnNames.length; i++) {
				if ((i == 0) && includePrice == false)
					continue;
				table.addCell(new Phrase(columnNames[i], headerFont));
			}
			
			
			silverItemsTransactionList
			.forEach(item -> {
				if (includePrice) {
					table.addCell(new Phrase(String.format("%.2f", item.getSilverItemPrice()),
							rowFont));
				}
				String itemName= String.format("%s\n%s", item.getItemName(), "HSN:7113");
				table.addCell(new Phrase(itemName, rowFont));
				table.addCell(new Phrase(String.valueOf(item.getQuantity()), rowFont));
				table.addCell(new Phrase(String.valueOf(item.getPiecepair()), rowFont));
				table.addCell(new Phrase(String.format("%.3f", item.getWeight()), rowFont));
				table.addCell(new Phrase(String.format("%.3f", item.getMakingCharge()), rowFont));
				table.addCell(new Phrase(item.getMakingChargeType(), rowFont));
				table.addCell(new Phrase(String.format("%.3f", item.getSilverRate()), rowFont));
			});
			
		
			document.add(table);
			PdfPTable footerTable = getPDFTable(true, 2);
			footerTable.getDefaultCell().setBackgroundColor(
					new BaseColor(213, 213, 203));
			Phrase totalSilverPricePhrase = new Phrase();
			totalSilverPricePhrase.add(new Chunk("Total Silver Price(INR) = ",
					headerFont));
			totalSilverPricePhrase.add(new Chunk(String.format("%.2f", 
					 Math.round(silverItemsTransactionList.stream().map(item -> item.getSilverItemPrice()).reduce((price1, price2) -> price1 + price2).get() * 100.0) / 100.0 , rowFont)));
			footerTable.addCell(new Phrase(totalSilverPricePhrase));
			Phrase totalSilverWeightPhrase = new Phrase();
			totalSilverWeightPhrase.add(new Chunk("Total Silver Wt(gm) = ",
					headerFont));
			totalSilverWeightPhrase.add(new Chunk(String.format("%.3f", 
					 Math.round(silverItemsTransactionList.stream().map(item -> item.getWeight()).reduce((price1, price2) -> price1 + price2).get()
							 * 100.0) / 100.0 , rowFont)));
			footerTable.addCell(totalSilverWeightPhrase);
			document.add(footerTable);
		}

	}

	private void addGoldItemsInformation(Document document, boolean includePrice)
			throws DocumentException, IOException {
		PdfPTable table;
		if (includePrice) {
			table = new PdfPTable(new float[] { 1.2f, 1.6f, 1, 0.4f, 0.9f,
					0.9f, 0.8f, 1.3f, 1.4f });
		} else {
			table = new PdfPTable(
					new float[] { 1.3f, 1, 1, 1, 1, 1.3f, 1.3f, 1 });
		}
		table.setWidthPercentage(100f);
		table.getDefaultCell().setPadding(1);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
		table.getDefaultCell().setFixedHeight(22f - getTotalItems() * 0.5f);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBackgroundColor(new BaseColor(238, 225, 57));
		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
				BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 11, Font.BOLD);
		Font rowFont = new Font(baseFont, 10, Font.NORMAL);

		String columnNames[] = new String[] { "Price", "Gold Item", "GoldType",
				"Qty", "Pc/Pair", "Wt(gms)", "MakingCost", "MkCostType",
				"Rate-pergm" };
		List<GoldTransactionItemDTO> goldItemsTransactionList = retailTransaction.getGoldTransactionItemBeanList();
		
		if (!goldItemsTransactionList.isEmpty()) {
			for (int i = 0; i < columnNames.length; i++) {
				if ((i == 0) && includePrice == false)
					continue;
				table.addCell(new Phrase(columnNames[i], headerFont));
			}
			
		goldItemsTransactionList
			.forEach(golItem -> {
				if (includePrice) {
					table.addCell(new Phrase(String.format("%.2f", golItem.getGoldItemPrice()), rowFont));
				}
				String itemName= String.format("%s\n%s", golItem.getGoldItemName(), "HSN:7113");
				table.addCell(new Phrase(itemName, rowFont));
				table.addCell(new Phrase(golItem.getGoldType(), rowFont));
				table.addCell(new Phrase(String.valueOf(golItem.getQuantity()), rowFont));
				table.addCell(new Phrase(golItem.getPiecePair(), rowFont));
				table.addCell(new Phrase(String.format("%.3f", golItem.getWeight()), rowFont));
				table.addCell(new Phrase(String.format("%.3f", golItem.getMakingCharge()), rowFont));
				table.addCell(new Phrase(golItem.getMakingChargeType(), rowFont));
				table.addCell(new Phrase(String.format("%.3f",golItem.getGoldRate()), rowFont));
			});
			
			
			
			document.add(table);
			PdfPTable footerTable = getPDFTable(true, 2);
			footerTable.getDefaultCell().setBackgroundColor(
					new BaseColor(238, 225, 57));
			Phrase totalGoldPricePhrase = new Phrase();
			totalGoldPricePhrase.add(new Chunk("Total Gold Price(INR) = ",
					headerFont));
			totalGoldPricePhrase.add(new Chunk(String.format("%.2f", 
					 Math.round(goldItemsTransactionList.stream().map(item -> item.getGoldItemPrice()).reduce((price1, price2) -> price1 + price2).get() * 100.0) / 100.0 , rowFont)));
			footerTable.addCell(new Phrase(totalGoldPricePhrase));
			Phrase totalGoldWeightPhrase = new Phrase();
			totalGoldWeightPhrase.add(new Chunk("Total Gold Wt(gm) = ",
					headerFont));
			totalGoldWeightPhrase.add(new Chunk(String.format("%.3f", goldItemsTransactionList.stream().map(item -> item.getWeight()).reduce((price1, price2) -> price1 + price2).get(), rowFont)));
			footerTable.addCell(totalGoldWeightPhrase);
			document.add(footerTable);
		}
	}
	

	private PdfPTable getPDFTable(boolean includePrice, int columnCountWithPrice) {
		PdfPTable table;
		if (includePrice) {
			table = new PdfPTable(columnCountWithPrice);
		} else {
			table = new PdfPTable(columnCountWithPrice - 1);
		}
		table.setWidthPercentage(100f);
		table.getDefaultCell().setPadding(1);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBackgroundColor(new BaseColor(253, 220, 153));
		return table;
	}

	private void addDateVinInformation(Document document, Date invoiceDate,
			boolean isEstimateBill, String vinNumber) throws DocumentException,
			IOException {
		PdfPTable table;
		if (isEstimateBill) {
			table = new PdfPTable(1);
		} else {
			table = new PdfPTable(3);
		}
		table.setWidthPercentage(100f);
		table.getDefaultCell().setPadding(1);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setBackgroundColor(new BaseColor(243, 175, 250));
		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
				BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 11, Font.BOLD);
		Font rowFont = new Font(baseFont, 10, Font.NORMAL);

		Phrase datePhrase = new Phrase();
		datePhrase.add(new Chunk("Date: ", headerFont));
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String dateWithoutTime = sdf.format(invoiceDate);
		datePhrase.add(new Chunk(dateWithoutTime, rowFont));
		table.addCell(datePhrase);
		if (!isEstimateBill) {
			Phrase vinPhrase = new Phrase();
			vinPhrase.add(new Chunk("GSTIN: ", headerFont));
			vinPhrase.add(new Chunk(vinNumber, rowFont));
			table.addCell(vinPhrase);
			
			Phrase stateCode = new Phrase();
			stateCode.add(new Chunk("StateCode: ", headerFont));
			stateCode.add(new Chunk("10", rowFont));
			table.addCell(stateCode);
		}
		document.add(table);
	}
	
	private void addPaymentInformationTable(Document document, Boolean isEstimateBill) throws DocumentException, IOException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100f);
		table.getDefaultCell().setPadding(2);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
		// table.getDefaultCell().setColspan(1);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setBackgroundColor(new BaseColor(185, 236, 190));

		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
				BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 10, Font.BOLD);
		Font rowFont = new Font(baseFont, 9, Font.NORMAL);
		
		RetailTransactionPaymentDTO paymentDto  = retailTransaction.getRetailTransPaymentDto();
		if(paymentDto.getCashPayment() > 0){
			table.addCell(new Phrase(new Chunk("Cash(INR): ", headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.2f",paymentDto.getCashPayment()), rowFont)));
		}
		if(paymentDto.getTotalCardPayment() > 0){
			table.addCell(new Phrase(new Chunk("Card(INR): ", headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.2f",paymentDto.getTotalCardPayment()), rowFont)));
		}
		if(paymentDto.getChequePayment() > 0){
			table.addCell(new Phrase(new Chunk("Cheque(INR): ", headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.2f",paymentDto.getChequePayment()), rowFont)));
		}
		if(paymentDto.getRtgsPayment() > 0){
			table.addCell(new Phrase(new Chunk("RTGS(INR): ", headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.2f",paymentDto.getRtgsPayment()), rowFont)));
		}
		if(paymentDto.getNeftPayment() > 0){
			table.addCell(new Phrase(new Chunk("NEFT(INR): ", headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.2f",paymentDto.getNeftPayment()), rowFont)));
		}
		if(isEstimateBill){
			Double totalPayment = paymentDto.getCashPayment() + paymentDto.getChequePayment() + paymentDto.getNeftPayment() + paymentDto.getRtgsPayment() + paymentDto.getTotalCardPayment();
			//String.format(totalPayment, args)
			//totalPayment = Math.round(totalPayment);
			table.addCell(new Phrase(new Chunk("TotalPayment(INR): ", headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.2f(%s)", totalPayment, new ConvertNumberToWords(totalPayment).convertToWords()), rowFont)));
		}
		document.add(table);
	}

	private void addPriceInformationTable(Document document,
			boolean isEstimateBill) throws DocumentException, IOException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100f);
		table.getDefaultCell().setPadding(2);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
		// table.getDefaultCell().setColspan(1);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setBackgroundColor(new BaseColor(185, 236, 190));

		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
				BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 10, Font.BOLD);
		Font rowFont = new Font(baseFont, 9, Font.NORMAL);

		table.addCell(new Phrase(new Chunk("Total Items Price(INR): ", headerFont)));
		PriceDTO priceBean = retailTransaction.getPriceBean();
		table.addCell(new Phrase(new Chunk(String.format("%.2f",priceBean.getTotalItemsPrice()), rowFont)));

		
		Double discountPrice = 	retailTransaction.getPriceBean().getDiscount();
		if (discountPrice > 0) {
			table.addCell(new Phrase(new Chunk("Discount(INR): ", headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.2f",priceBean.getDiscount()), rowFont)));
		}
		if (!isEstimateBill) {
			//GST
			table.addCell(new Phrase(new Chunk("GST("
					+ String.valueOf(CustomShopSettingFileUtility.getInstance()
							.getVatPercentage()) + "%) (INR) : ",
					headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.2f", priceBean.getVatCharge()), rowFont)));
			//CGST
			table.addCell(new Phrase(new Chunk("CGST("
					+ String.valueOf(CustomShopSettingFileUtility.getInstance()
							.getVatPercentage()/2.0) + "%) (INR) : ",
					headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.3f", priceBean.getVatCharge()/2.0), rowFont)));
			//SGST
			table.addCell(new Phrase(new Chunk("SGST("
					+ String.valueOf(CustomShopSettingFileUtility.getInstance()
							.getVatPercentage()/2.0) + "%) (INR) : ",
					headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.3f", priceBean.getVatCharge()/2.0), rowFont)));
		}
		Double oldPurchasePrice = priceBean.getOldPurchase();
		if (oldPurchasePrice > 0) {
			table.addCell(new Phrase(new Chunk("Old Purchase(INR) : ",
					headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.2f", priceBean.getOldPurchase()), rowFont)));
		}
		table.addCell(new Phrase(new Chunk("Net Amount(INR) : ", headerFont)));
		Double advancedPayment = priceBean.getAdvancePaymentAmount();
		if (advancedPayment > 0) {
			table.addCell(new Phrase(new Chunk(String.format("%.2f", priceBean.getNetpayableAmount()), rowFont)));

		} else {
			Double neyPayAmt = (double) Math.round(priceBean.getNetpayableAmount());
			table.addCell(new Phrase(new Chunk(String.format("%.2f (%s)", neyPayAmt, new ConvertNumberToWords(neyPayAmt).convertToWords(),	rowFont))));
		}

		if (isEstimateBill && advancedPayment > 0) {
			table.addCell(new Phrase(new Chunk("Advance Payment(INR) : ",
					headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.2f", advancedPayment), rowFont)));

			table.addCell(new Phrase(new Chunk("Balance Amount(INR) : ",
					headerFont)));
			Double balanceAmt = (double) Math.round(priceBean.getBalanceAmount());
			table.addCell(new Phrase(new Chunk(String.format("%.2f (%s)", balanceAmt, new ConvertNumberToWords(balanceAmt).convertToWords(),
					rowFont))));
		}

		document.add(table);
	}

	private void addCustomerInformation(Document document)
			throws DocumentException, IOException {
		PdfPTable customerTable = new PdfPTable(4);
		customerTable.setWidthPercentage(100f);
		customerTable.getDefaultCell().setPadding(2);
		customerTable.getDefaultCell().setUseAscender(true);
		customerTable.getDefaultCell().setUseDescender(true);
		// table.getDefaultCell().setColspan(1);
		customerTable.getDefaultCell().setBorder(0);
		customerTable.getDefaultCell().setBackgroundColor(
				new BaseColor(189, 202, 242));
		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
				BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 10, Font.BOLD);
		Font rowFont = new Font(baseFont, 9, Font.NORMAL);
		CustomerDTO customer = retailTransaction.getCustomer();
		customerTable.addCell(new Phrase(new Chunk("Customer Name: ",
				headerFont)));
		customerTable.addCell(new Phrase(new Chunk(customer.getFirstName()
				+ " " + customer.getLastName(), rowFont)));

		customerTable
				.addCell(new Phrase(new Chunk("Contact No: ", headerFont)));
		customerTable.addCell(new Phrase(new Chunk(customer.getContactNumber(),
				rowFont)));

		customerTable.addCell(new Phrase(new Chunk("Street Address1: ",
				headerFont)));
		customerTable.addCell(new Phrase(new Chunk(
				customer.getStreetAddress1(), rowFont)));

		customerTable.addCell(new Phrase(new Chunk("Street Address2: ",
				headerFont)));
		customerTable.addCell(new Phrase(new Chunk(
				customer.getStreetAddress2(), rowFont)));

		customerTable.addCell(new Phrase(new Chunk("Zipcode: ", headerFont)));
		customerTable.addCell(new Phrase(new Chunk(customer.getZipcode(),
				rowFont)));

		customerTable
				.addCell(new Phrase(new Chunk("City/State: ", headerFont)));
		customerTable.addCell(new Phrase(new Chunk(customer.getCity() + " "
				+ customer.getStateprovince(), rowFont)));

		customerTable.addCell(new Phrase(new Chunk("Country: ", headerFont)));
		customerTable.addCell(new Phrase(new Chunk(customer.getCountry(),
				rowFont)));

		customerTable.addCell(new Phrase(new Chunk("EmailId: ", headerFont)));
		customerTable.addCell(new Phrase(new Chunk(customer.getEmailId(),
				rowFont)));

		document.add(customerTable);
	}

	/**
	 * Draws a background for every other row.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPTableEvent#tableLayout(com.itextpdf.text.pdf.PdfPTable,
	 *      float[][], float[], int, int,
	 *      com.itextpdf.text.pdf.PdfContentByte[])
	 */
	@Override
	public void tableLayout(PdfPTable table, float[][] widths, float[] heights,
			int headerRows, int rowStart, PdfContentByte[] canvases) {
		int columns;
		Rectangle rect;
		int footer = widths.length - table.getFooterRows();
		int header = table.getHeaderRows() - table.getFooterRows() + 1;
		for (int row = header; row < footer; row += 2) {
			columns = widths[row].length - 1;
			rect = new Rectangle(widths[row][0], heights[row],
					widths[row][columns], heights[row + 1]);
			rect.setBackgroundColor(BaseColor.YELLOW);
			rect.setBorder(Rectangle.NO_BORDER);
			canvases[PdfPTable.BASECANVAS].rectangle(rect);
		}
	}

	int getTotalItems() {
		return retailTransaction.getGoldTransactionItemBeanList().size() + retailTransaction.getSilverTransactionItemBeanList().size()
				+ retailTransaction.getDiamondTransactionItemBeanList().size();
	}

	@Override
    public InputStream getStream() {
		Date invoiceDate = retailTransaction.getTransactionDate();
		Boolean isEstimateBill = retailTransaction.isEstimateBill();
		String invoiceNumber = retailTransaction.getInvoiceNumber().toString();
		String staffName = retailTransaction.getDealingStaffName();
		Boolean includePrice = retailTransaction.getIncludePrice();
		String vinNumber = retailTransaction.getVinNumber();
		String notes = retailTransaction.getNotes();
		cal.setTime(invoiceDate);
		Document document = new Document(PageSize.A4);
		//String filePath = getFilePath(isEstimateBill, invoiceDate);
		//File file = new File(filePath);
		PdfWriter writer;
		try{
			writer = PdfWriter.getInstance(document, os);
			if(!retailTransaction.isTransactionActive()){
				writer.setPageEvent(new Watermark());
			}
	        
		// step 3
		document.open();
		// step 4
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));

		// document.add(generateBarCode(writer));
		addInvoiceType(document, isEstimateBill, invoiceNumber);
		document.add(new Paragraph(" "));
		addDateVinInformation(document, invoiceDate, isEstimateBill, vinNumber);
		document.add(new Paragraph(" "));

		addCustomerInformation(document);
		document.add(new Paragraph(" "));

		
		addGoldItemsInformation(document, includePrice);
		document.add(new Paragraph(" "));

		addSilverItemsInformation(document, includePrice);
		document.add(new Paragraph(" "));

		addDiamondItemsInformation(document, includePrice);
		document.add(new Paragraph(" "));

		addGeneralItemsInformation(document, includePrice);
		document.add(new Paragraph(" "));

		if(!isEstimateBill){
			addPriceInformationTable(document, isEstimateBill);
		}else{
			addPaymentInformationTable(document, isEstimateBill);
		}
		
		document.add(new Paragraph(" "));

		addNotes(document, notes);
		document.add(new Paragraph(" "));

		if (!isEstimateBill) {
			document.add(new Paragraph(" "));

		}
		addSignature(document, isEstimateBill, staffName);
		document.add(new Paragraph(" "));

     	addSubjectJurisdiction(document);
		document.add(new Paragraph(" "));

		document.newPage();

		// step 5
		document.close();
		//saveCustomerInformation(retailTransaction.getCustomer(), invoiceDate);
		}catch(Exception e){
			System.out.println("Error Generating invoice");
			e.printStackTrace();
		}
        // Here we return the pdf contents as a byte-array
        return new ByteArrayInputStream(os.toByteArray());
    }

    private void addSubjectJurisdiction(Document document) throws Exception{
		PdfPTable table;
		table = new PdfPTable(1);
		table.setWidthPercentage(100f);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		table.getDefaultCell().setPadding(1);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setBackgroundColor(new BaseColor(243, 175, 250));
		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 11, Font.BOLD);
		Font rowFont = new Font(baseFont, 10, Font.NORMAL);
		Phrase jurisdictionText = new Phrase();
		jurisdictionText.add(new Chunk("** SUBJECT TO PATNA JURISDICTION **", headerFont));
		table.addCell(jurisdictionText);
		document.add(table);
	}

	class Watermark extends PdfPageEventHelper {
		 
        Font FONT = new Font(FontFamily.HELVETICA, 52, Font.BOLD, new GrayColor(0.75f));
 
        public void onEndPage(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase("CANCELLED CANCELLED", FONT),
                    297.5f, 421, writer.getPageNumber() % 2 == 1 ? 45 : -45);
        }
    }
}
