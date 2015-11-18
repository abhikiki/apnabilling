/**
 * 
 */
package com.abhishek.fmanage.retail.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.abhishek.fmanage.mortgage.data.bean.Customer;
import com.abhishek.fmanage.retail.data.container.DiamondItemContainer;
import com.abhishek.fmanage.retail.data.container.GoldItemContainer;
import com.abhishek.fmanage.retail.data.container.SilverItemContainer;
import com.abhishek.fmanage.retail.form.PriceForm;
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
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Image;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class InvoiceGenerator implements PdfPTableEvent {

	/** A font that will be used in our PDF. */
	public static final Font BOLD_UNDERLINED = new Font(FontFamily.TIMES_ROMAN,
			11, Font.BOLD | Font.UNDERLINE);
	/** A font that will be used in our PDF. */
	public static final Font BOLD = new Font(FontFamily.TIMES_ROMAN, 11,
			Font.BOLD);
	/** A font that will be used in our PDF. */
	public static final Font NORMAL = new Font(FontFamily.TIMES_ROMAN, 10);

	/** The resulting PDF file. */
	public final String INVOICE_FILE_NAME = "Invoice_"
			+ new Timestamp(new Date().getTime()).toString()
					.replaceAll(" ", "_").replace(":", "_") + ".pdf";

	private static final String NEW_LINE_SEPARATOR = "\n";
	// CSV file header
	private static final Object[] FILE_HEADER = { "FirstName", "LastName",
			"ContactNumber", "EmailId", "PhoneNumber", "StreetAddress1",
			"StreetAddress2", "City", "State", "Country", "Zipcode" };

	private Table goldBillingTable;
	private Table silverBillingTable;
	private Table diamondBillingTable;
	private PriceForm pfForm;
	private Customer customer;

	public InvoiceGenerator(Table goldBillingTable, Table silverBillingTable,
			Table diamondBillingTable, PriceForm pfForm, Customer customer) {
		this.goldBillingTable = goldBillingTable;
		this.silverBillingTable = silverBillingTable;
		this.diamondBillingTable = diamondBillingTable;
		this.pfForm = pfForm;
		this.customer = customer;
	}

	/**
	 * Creates a PDF document.
	 * 
	 * @param isEstimateBill
	 * @param vinNumber
	 * @param includePrice
	 * @param invoiceDate
	 * @param notes
	 * @param filename
	 *            the path to the new PDF document
	 * @throws DocumentException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String createPdf(final boolean includePrice, final String vinNumber,
			final boolean isEstimateBill, final Date invoiceDate,
			final TextArea notes) throws SQLException, DocumentException,
			IOException {
		// step 1
		Document document = new Document(PageSize.A4);
		// step 2
		// PdfWriter.getInstance(document, new
		// FileOutputStream(getDirectory(invoiceDate) +
		// "\" + INVOICE_FILE_NAME));"
		String filePath = System.getenv("BILL_PATH") + "\\"
				+ getDirectory(invoiceDate, isEstimateBill) + "\\"
				+ INVOICE_FILE_NAME;
		File file = new File(filePath);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

		// step 3
		document.open();
		// step 4
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));
		
		//document.add(generateBarCode(writer));
		addInvoiceType(document, isEstimateBill, invoiceDate);
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

		addPriceInformationTable(document, isEstimateBill);
		document.add(new Paragraph(" "));

		addNotes(document, notes);
		document.add(new Paragraph(" "));

		document.newPage();

		// step 5
		document.close();
		saveCustomerInformation(customer);
		return filePath;

	}

	private void saveCustomerInformation(Customer customer) {
		FileWriter fileWriter = null;
		CSVPrinter csvFilePrinter = null;
		CSVFormat csvFileFormat = CSVFormat.DEFAULT
				.withRecordSeparator(NEW_LINE_SEPARATOR);
		try {
			// initialize FileWriter object
			File f = new File(System.getenv("BILL_PATH") + "\\"	+ "customerInfo.csv");
			boolean isFileExist = false;
			if(isFileExist = f.exists()){
				f.createNewFile();
				
			}
			
			fileWriter = new FileWriter(f, true);
			// initialize CSVPrinter object
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
			// Create CSV file header
			if(!isFileExist){
				csvFilePrinter.printRecord(FILE_HEADER);
			}
			

			List<String> customerInfoList = new ArrayList<>();
			customerInfoList.add(customer.getFirstName());
			customerInfoList.add(customer.getLastName());
			customerInfoList.add(customer.getContactNumber());
			customerInfoList.add(customer.getEmailId());
			customerInfoList.add(customer.getContactNumber());
			customerInfoList.add(customer.getStreetAddress1());
			customerInfoList.add(customer.getStreetAddress2());
			customerInfoList.add(customer.getCity());
			customerInfoList.add(customer.getStateprovince());
			customerInfoList.add(customer.getCountry());
			customerInfoList.add(customer.getZipcode());
			csvFilePrinter.printRecord(customerInfoList);

		} catch (Exception e) {
			 e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvFilePrinter.close();
			} catch (Exception e) {

				System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
				e.printStackTrace();
			}

		}

	}

//	private com.itextpdf.text.Image generateBarCode(PdfWriter writer, Date date) throws DocumentException
//	{
//		Barcode128 barcd = new Barcode128();
//        barcd.setCode("10031"); 
//        PdfContentByte cb = writer.getDirectContent();
//        com.itextpdf.text.Image barcode1 = barcd.createImageWithBarcode(cb, BaseColor.DARK_GRAY, BaseColor.BLUE);
//        return barcode1;
//	}
	private void addInvoiceType(Document document, boolean isEstimateBill, Date invoiceDate)
			throws DocumentException, IOException {
		PdfPTable footerTable = getPDFTable(!isEstimateBill, 2);
		footerTable.getDefaultCell().setBackgroundColor(
				new BaseColor(252, 175, 175));
		footerTable.getDefaultCell().setHorizontalAlignment(
				Element.ALIGN_CENTER);
		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
				BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 11, Font.BOLD);
		Font rowFont = new Font(baseFont, 10, Font.NORMAL);
		Phrase notePhrase = new Phrase();
		if (isEstimateBill) {
			
			notePhrase.add(new Chunk("Estimate Letter", headerFont));
		} else {
			notePhrase.add(new Chunk("Retail Invoice", headerFont));

		}
		
		PdfPCell cell = new PdfPCell(notePhrase);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(new BaseColor(247, 200, 153));
		if(!isEstimateBill)
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(invoiceDate);
			String InvoiceId = String.valueOf(cal.get(Calendar.YEAR)) + "-"
					+ String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
					+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "-"
					+ String.valueOf(cal.get(Calendar.HOUR)) + "-"
					+ String.valueOf(cal.get(Calendar.MINUTE)) + "-"
					+ String.valueOf(cal.get(Calendar.SECOND)) + "-"
					+ String.valueOf(cal.get(Calendar.MILLISECOND));
			Phrase p = new Phrase(new Chunk("Invoice Id: ", headerFont));
			p.add(new Chunk(InvoiceId, rowFont));
			footerTable.addCell(p);
		}
		footerTable.addCell(cell);
		document.add(footerTable);

	}

	private void addNotes(Document document, TextArea notes)
			throws DocumentException, IOException {
		String invoiceNote = notes.getValue();
		if (!StringUtils.isBlank(invoiceNote)) {
			PdfPTable footerTable = getPDFTable(true, 1);
			footerTable.getDefaultCell().setBackgroundColor(
					new BaseColor(252, 175, 175));
			BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
					BaseFont.CP1252, BaseFont.EMBEDDED);
			Font headerFont = new Font(baseFont, 11, Font.BOLD);
			Font rowFont = new Font(baseFont, 10, Font.NORMAL);
			Phrase notePhrase = new Phrase();
			notePhrase.add(new Chunk("Notes: ", headerFont));
			notePhrase.add(new Chunk(invoiceNote, rowFont));
			footerTable.addCell(notePhrase);
			document.add(footerTable);
		}

	}

	private void addDiamondItemsInformation(Document document,
			boolean includePrice) throws DocumentException, IOException {
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
		if (diamondBillingTable.size() > 0) {
			for (int i = 0; i < columnNames.length; i++) {
				if ((i == 0) && includePrice == false)
					continue;
				table.addCell(new Phrase(columnNames[i], headerFont));
			}
			DiamondItemContainer con = (DiamondItemContainer) diamondBillingTable
					.getContainerDataSource();
			for (Object obj : diamondBillingTable.getItemIds()) {
				TextField itemTxtField = (TextField) (con.getItem(obj)
						.getItemProperty(DiamondItemContainer.PRICE).getValue());
				String itemPrice = itemTxtField.getValue();
				if (NumberUtils.isNumber(itemPrice)
						&& Double.valueOf(itemPrice) > 0.0) {
					if (includePrice == true) {
						table.addCell(new Phrase(itemTxtField.getValue(),
								rowFont));
					}
					ComboBox itemNameField = (ComboBox) con.getItem(obj)
							.getItemProperty(DiamondItemContainer.ITEM_NAME)
							.getValue();
					table.addCell(new Phrase(String.valueOf(itemNameField
							.getValue()), rowFont));

					TextField quantityTxtField = (TextField) con.getItem(obj)
							.getItemProperty(DiamondItemContainer.QUANTITY)
							.getValue();
					table.addCell(new Phrase(quantityTxtField.getValue()
							.toString(), rowFont));

					ComboBox piecePairField = (ComboBox) con.getItem(obj)
							.getItemProperty(DiamondItemContainer.PIECE_PAIR)
							.getValue();
					table.addCell(new Phrase(String.valueOf(piecePairField
							.getValue()), rowFont));

					TextField weightGoldTxtField = (TextField) con.getItem(obj)
							.getItemProperty(DiamondItemContainer.GOLD_WEIGHT)
							.getValue();
					table.addCell(new Phrase(weightGoldTxtField.getValue()
							.toString(), rowFont));

					TextField weightDiamondTxtField = (TextField) con
							.getItem(obj)
							.getItemProperty(
									DiamondItemContainer.DIAMOND_WEIGHT)
							.getValue();
					table.addCell(new Phrase(weightDiamondTxtField.getValue()
							.toString(), rowFont));

					TextField diamondPieceTxtField = (TextField) con
							.getItem(obj)
							.getItemProperty(DiamondItemContainer.DIAMOND_PIECE)
							.getValue();
					table.addCell(new Phrase(diamondPieceTxtField.getValue()
							.toString(), rowFont));

					ComboBox certificate = (ComboBox) con.getItem(obj)
							.getItemProperty(DiamondItemContainer.CERTIFICATE)
							.getValue();
					table.addCell(new Phrase(String.valueOf(certificate
							.getValue()), rowFont));
				}
			}
			document.add(table);
			PdfPTable footerTable = getPDFTable(true, 3);
			footerTable.getDefaultCell().setBackgroundColor(
					new BaseColor(189, 202, 242));

			Phrase totalDiamondPricePhrase = new Phrase();
			totalDiamondPricePhrase.add(new Chunk(
					"Total Diamond Price(INR) = ", headerFont));
			totalDiamondPricePhrase.add(new Chunk(String.format("%.3f",
					con.getTotalPrice()), rowFont));
			footerTable.addCell(new Phrase(totalDiamondPricePhrase));

			Phrase totalGoldWeightPhrase = new Phrase();
			totalGoldWeightPhrase.add(new Chunk("Total Gold Wt(gm) = ",
					headerFont));
			totalGoldWeightPhrase.add(new Chunk(String.format("%.3f",
					con.getTotalGoldWeight()), rowFont));
			footerTable.addCell(new Phrase(totalGoldWeightPhrase));

			Phrase totalDiamondWeightPhrase = new Phrase();
			totalDiamondWeightPhrase.add(new Chunk(
					"Total Diamond Wt(Carat) = ", headerFont));
			totalDiamondWeightPhrase.add(new Chunk(String.format("%.2f",
					con.getTotalDiamondWeight()), rowFont));
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
		if (silverBillingTable.size() > 0) {
			for (int i = 0; i < columnNames.length; i++) {
				if ((i == 0) && includePrice == false)
					continue;
				table.addCell(new Phrase(columnNames[i], headerFont));
			}
			SilverItemContainer con = (SilverItemContainer) silverBillingTable
					.getContainerDataSource();
			for (Object obj : silverBillingTable.getItemIds()) {
				TextField itemTxtField = (TextField) (con.getItem(obj)
						.getItemProperty(SilverItemContainer.PRICE).getValue());
				String itemPrice = itemTxtField.getValue();
				if (NumberUtils.isNumber(itemPrice)
						&& Double.valueOf(itemPrice) > 0.0) {
					if (includePrice == true) {
						table.addCell(new Phrase(itemTxtField.getValue(),
								rowFont));
					}
					ComboBox itemNameField = (ComboBox) con.getItem(obj)
							.getItemProperty(SilverItemContainer.ITEM_NAME)
							.getValue();
					table.addCell(new Phrase(String.valueOf(itemNameField
							.getValue()), rowFont));

					TextField quantityTxtField = (TextField) con.getItem(obj)
							.getItemProperty(SilverItemContainer.QUANTITY)
							.getValue();
					table.addCell(new Phrase(quantityTxtField.getValue()
							.toString(), rowFont));

					ComboBox piecePairField = (ComboBox) con.getItem(obj)
							.getItemProperty(SilverItemContainer.PIECE_PAIR)
							.getValue();
					table.addCell(new Phrase(String.valueOf(piecePairField
							.getValue()), rowFont));

					TextField weightTxtField = (TextField) con.getItem(obj)
							.getItemProperty(SilverItemContainer.WEIGHT)
							.getValue();
					table.addCell(new Phrase(weightTxtField.getValue()
							.toString(), rowFont));

					TextField makingChargeTxtField = (TextField) con
							.getItem(obj)
							.getItemProperty(SilverItemContainer.MAKING_CHARGE)
							.getValue();
					table.addCell(new Phrase(makingChargeTxtField.getValue()
							.toString(), rowFont));

					ComboBox makingChargeType = (ComboBox) con
							.getItem(obj)
							.getItemProperty(
									SilverItemContainer.MAKING_CHARGE_TYPE)
							.getValue();
					table.addCell(new Phrase(String.valueOf(makingChargeType
							.getValue()), rowFont));

					TextField silverRateTxtField = (TextField) con.getItem(obj)
							.getItemProperty(SilverItemContainer.SILVER_RATE)
							.getValue();
					table.addCell(new Phrase(silverRateTxtField.getValue()
							.toString(), rowFont));

				}
			}
			document.add(table);
			PdfPTable footerTable = getPDFTable(true, 2);
			footerTable.getDefaultCell().setBackgroundColor(
					new BaseColor(213, 213, 203));
			Phrase totalSilverPricePhrase = new Phrase();
			totalSilverPricePhrase.add(new Chunk("Total Silver Price(INR) = ",
					headerFont));
			totalSilverPricePhrase.add(new Chunk(String.format("%.3f",
					con.getTotalPrice()), rowFont));
			footerTable.addCell(new Phrase(totalSilverPricePhrase));
			Phrase totalSilverWeightPhrase = new Phrase();
			totalSilverWeightPhrase.add(new Chunk("Total Silver Wt(gm) = ",
					headerFont));
			totalSilverWeightPhrase.add(new Chunk(String.format("%.3f",
					con.getTotalWeight()), rowFont));
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

		String columnNames[] = new String[] { "Price", "Gold Items",
				"HallMark", "Qty", "Pc/Pair", "Wt(gms)", "MakingCost",
				"MkCostType", "Rate-pergm" };
		if (goldBillingTable.size() > 0) {
			for (int i = 0; i < columnNames.length; i++) {
				if ((i == 0) && includePrice == false)
					continue;
				table.addCell(new Phrase(columnNames[i], headerFont));
			}
			GoldItemContainer con = (GoldItemContainer) goldBillingTable
					.getContainerDataSource();
			for (Object obj : goldBillingTable.getItemIds()) {
				TextField itemTxtField = (TextField) (con.getItem(obj)
						.getItemProperty(GoldItemContainer.PRICE).getValue());
				String itemPrice = itemTxtField.getValue();
				if (NumberUtils.isNumber(itemPrice)
						&& Double.valueOf(itemPrice) > 0.0) {
					if (includePrice == true) {
						table.addCell(new Phrase(itemTxtField.getValue(),
								rowFont));
					}
					ComboBox itemNameField = (ComboBox) con.getItem(obj)
							.getItemProperty(GoldItemContainer.ITEM_NAME)
							.getValue();
					table.addCell(new Phrase(String.valueOf(itemNameField
							.getValue()), rowFont));

					ComboBox hallMarkTypeField = (ComboBox) con.getItem(obj)
							.getItemProperty(GoldItemContainer.HALL_MARK_TYPE)
							.getValue();
					table.addCell(new Phrase(String.valueOf(hallMarkTypeField
							.getValue()), rowFont));

					TextField quantityTxtField = (TextField) con.getItem(obj)
							.getItemProperty(GoldItemContainer.QUANTITY)
							.getValue();
					table.addCell(new Phrase(quantityTxtField.getValue()
							.toString(), rowFont));

					ComboBox piecePairField = (ComboBox) con.getItem(obj)
							.getItemProperty(GoldItemContainer.PIECE_PAIR)
							.getValue();
					table.addCell(new Phrase(String.valueOf(piecePairField
							.getValue()), rowFont));

					TextField weightTxtField = (TextField) con.getItem(obj)
							.getItemProperty(GoldItemContainer.WEIGHT)
							.getValue();
					table.addCell(new Phrase(weightTxtField.getValue()
							.toString(), rowFont));

					TextField makingChargeTxtField = (TextField) con
							.getItem(obj)
							.getItemProperty(GoldItemContainer.MAKING_CHARGE)
							.getValue();
					table.addCell(new Phrase(makingChargeTxtField.getValue()
							.toString(), rowFont));

					ComboBox makingChargeType = (ComboBox) con
							.getItem(obj)
							.getItemProperty(
									GoldItemContainer.MAKING_CHARGE_TYPE)
							.getValue();
					table.addCell(new Phrase(makingChargeType.getValue()
							.toString(), rowFont));

					TextField goldRateTxtField = (TextField) con.getItem(obj)
							.getItemProperty(GoldItemContainer.GOLD_RATE)
							.getValue();
					table.addCell(new Phrase(goldRateTxtField.getValue()
							.toString(), rowFont));

				}
			}
			document.add(table);
			PdfPTable footerTable = getPDFTable(true, 2);
			footerTable.getDefaultCell().setBackgroundColor(
					new BaseColor(238, 225, 57));
			Phrase totalGoldPricePhrase = new Phrase();
			totalGoldPricePhrase.add(new Chunk("Total Gold Price(INR) = ",
					headerFont));
			totalGoldPricePhrase.add(new Chunk(String.format("%.3f",
					con.getTotalPrice()), rowFont));
			footerTable.addCell(new Phrase(totalGoldPricePhrase));
			Phrase totalGoldWeightPhrase = new Phrase();
			totalGoldWeightPhrase.add(new Chunk("Total Gold Wt(gm) = ",
					headerFont));
			totalGoldWeightPhrase.add(new Chunk(String.format("%.3f",
					con.getTotalWeight()), rowFont));
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
			table = new PdfPTable(2);
		}
		table.setWidthPercentage(100f);
		table.getDefaultCell().setPadding(1);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
		// table.getDefaultCell().setColspan(5);
		// table.addCell(day.toString());
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		// table.getDefaultCell().setColspan(1);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setBackgroundColor(new BaseColor(243, 175, 250));
		BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER,
				BaseFont.CP1252, BaseFont.EMBEDDED);
		Font headerFont = new Font(baseFont, 11, Font.BOLD);
		Font rowFont = new Font(baseFont, 10, Font.NORMAL);

		Phrase datePhrase = new Phrase();
		datePhrase.add(new Chunk("Date: ", headerFont));
		datePhrase.add(new Chunk(invoiceDate.toString(), rowFont));
		table.addCell(datePhrase);
		if (!isEstimateBill) {
			Phrase vinPhrase = new Phrase();
			vinPhrase.add(new Chunk("TIN VAT NO: ", headerFont));
			vinPhrase.add(new Chunk(vinNumber, rowFont));
			table.addCell(vinPhrase);
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

		table.addCell(new Phrase(new Chunk("Total Items Price(INR): ",
				headerFont)));
		table.addCell(new Phrase(new Chunk(String.format("%.3f",
				Double.valueOf(pfForm.totalItemPrice.getValue())), rowFont)));

		Double discountPrice = new Double(pfForm.discountPrice.getValue());
		if (discountPrice > 0) {
			table.addCell(new Phrase(new Chunk("Discount(INR): ", headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.3f",
					Double.valueOf(pfForm.discountPrice.getValue())), rowFont)));
		}
		if (!isEstimateBill) {
			table.addCell(new Phrase(new Chunk("Vat(1%) Charge(INR) : ",
					headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.3f",
					Double.valueOf(pfForm.vatOnNewItemPrice.getValue())),
					rowFont)));
		}
		Double oldPurchasePrice = new Double(pfForm.oldPurchasePrice.getValue());
		if (oldPurchasePrice > 0) {
			table.addCell(new Phrase(new Chunk("Old Purchase(INR) : ",
					headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.3f",
					Double.valueOf(pfForm.oldPurchasePrice.getValue())),
					rowFont)));
		}
		table.addCell(new Phrase(new Chunk("Net Amount(INR) : ", headerFont)));
		Double advancedPayment = new Double(pfForm.advancePayment.getValue());
		if (advancedPayment > 0) {
			table.addCell(new Phrase(new Chunk(String.format("%.3f",
					Double.valueOf(pfForm.netAmountToPay.getValue())), rowFont)));

		} else {
			table.addCell(new Phrase(new Chunk(String
					.format("%d(rounded)", Math.round(Double
							.valueOf(pfForm.netAmountToPay.getValue()))),
					rowFont)));
		}

		if (isEstimateBill && advancedPayment > 0) {
			table.addCell(new Phrase(new Chunk("Advance Payment(INR) : ",
					headerFont)));
			table.addCell(new Phrase(new Chunk(String.format("%.3f",
					Double.valueOf(pfForm.advancePayment.getValue())), rowFont)));

			table.addCell(new Phrase(new Chunk("Balance Amount(INR) : ",
					headerFont)));
			table.addCell(new Phrase(new Chunk(String
					.format("%d(rounded)", Math.round(Double
							.valueOf(pfForm.balanceAmount.getValue()))),
					rowFont)));
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
		return goldBillingTable.size() + silverBillingTable.size()
				+ diamondBillingTable.size();
	}

	String getDirectory(Date invoiceDate, boolean isEstimateBill) {
		String directoryName = getDirectoryName(invoiceDate, isEstimateBill);
		File theDir = new File(System.getenv("BILL_PATH") + "\\"
				+ directoryName);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("Creating Directory: "
					+ System.getenv("BILL_PATH") + "\\" + directoryName);
			boolean result = false;

			try {
				theDir.mkdirs();
				result = true;
			} catch (SecurityException se) {

				// se.printStackTrace();
			}
			if (result) {
				System.out.println("DIR created");
			}
		}
		return directoryName;
	}

	String getDirectoryName(Date invoiceDate, boolean isEstimateBill) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(invoiceDate);
		String dir = String.valueOf(cal.get(Calendar.YEAR)) + "\\"
				+ String.valueOf(cal.get(Calendar.MONTH) + 1) + "\\"
				+ String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "-"
				+ String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
				+ String.valueOf(cal.get(Calendar.YEAR));
		if (isEstimateBill) {
			dir += "\\estimate";
		} else {
			dir += "\\invoice";
		}
		return dir;
	}
}
