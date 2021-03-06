CREATE TABLE WHOLESALETRANSACTION(
	SHOPID SMALLINT UNSIGNED,
	TRANSID BIGINT UNSIGNED AUTO_INCREMENT,
	TRANSDATE DATETIME NOT NULL,
	BILLTYPE ENUM('I', 'E') NOT NULL,
	DEALINGSTAFFNAME VARCHAR(255) DEFAULT 'STAFF' NOT NULL,
	NOTES VARCHAR(2048) ,
	TRANSACTIONSTATUS ENUM('A', 'I') DEFAULT 'A' NOT NULL,
	FOREIGN KEY(SHOPID)REFERENCES SHOP(SHOPID) ON DELETE CASCADE,
	PRIMARY KEY(TRANSID)
)ENGINE=InnoDB;

CREATE TABLE WHOLESALECUSTOMER(
	TRANSID BIGINT UNSIGNED,
	FIRSTNAME VARCHAR(255),
	LASTNAME  VARCHAR(255),
	CONTACTNUMBER VARCHAR(255),
	EMAILID VARCHAR(255),
	STREETADDRESS1 VARCHAR(512),
	STREETADDRESS2 VARCHAR(512),
	CITY VARCHAR(128),
	STATE VARCHAR(128),
	ZIPCODE VARCHAR(10),
	COUNTRY VARCHAR(64),
	FOREIGN KEY(TRANSID) REFERENCES WHOLESALETRANSACTION(TRANSID)ON DELETE CASCADE
)ENGINE='InnoDB';

CREATE TABLE WHOLESALETAXINVOICE (
   SHOPID BIGINT UNSIGNED,
   TRANSID BIGINT UNSIGNED,
   INVOICENUMBER BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   FOREIGN KEY(TRANSID)REFERENCES WHOLESALETRANSACTION(TRANSID) ON DELETE CASCADE,
   PRIMARY KEY(SHOPID, INVOICENUMBER)
) ENGINE='MyISAM';

CREATE TABLE WHOLESALESILVERITEMTRANSACTION(
	TRANSID BIGINT UNSIGNED,
	ITEMNAME VARCHAR(255) NOT NULL,
	QUANTITY DECIMAL(4) UNSIGNED,
	PIECEPAIR VARCHAR(255) NOT NULL,
	WEIGHT   DECIMAL(9, 4) UNSIGNED,
	MAKINGCHARGE  DECIMAL(10, 4) UNSIGNED,
	MAKINGCHARGETYPE VARCHAR(8) NOT NULL,
	SILVERRATE DECIMAL(10, 4) UNSIGNED,
	ITEMPRICE DECIMAL(12, 3) UNSIGNED,
	FOREIGN KEY(TRANSID) REFERENCES WHOLESALETRANSACTION(TRANSID) ON DELETE CASCADE
)ENGINE='InnoDB';

CREATE TABLE WHOLESALETRANSACTIONPRICE(
	TRANSID BIGINT UNSIGNED,
	TOTALITEMSPRICE DECIMAL(12, 3) UNSIGNED NOT NULL,
	DISCOUNT DECIMAL(15, 3) SIGNED ,
	VATPERCENT DECIMAL(12, 3) UNSIGNED ,
	VATONNEWITEMS DECIMAL(12, 3) UNSIGNED,
	OLDPURCHASEPRICE DECIMAL(15, 3) SIGNED,
	NETPAYABLEPRICE DECIMAL(15, 3) SIGNED,
	ADVANCEPAYMENT DECIMAL(15, 3) SIGNED,
	BALANCEAMOUNT DECIMAL(15, 3) SIGNED,
	FOREIGN KEY(TRANSID) REFERENCES WHOLESALETRANSACTION(TRANSID) ON DELETE CASCADE
)ENGINE='InnoDB';

CREATE TABLE WHOLESALETRANSACTIONPAYMENT(
	TRANSID BIGINT UNSIGNED,
	TOTALCARDPAYMENT DECIMAL(15, 3) UNSIGNED DEFAULT 0.00,
	CASHPAYMENT DECIMAL(15, 3) UNSIGNED DEFAULT 0.00,
	CHEQUEPAYMENT DECIMAL(15, 3) UNSIGNED DEFAULT 0.00,
	NEFTPAYMENT DECIMAL(15, 3) UNSIGNED DEFAULT 0.00,
	RGTSPAYMENT DECIMAL(15, 3) UNSIGNED DEFAULT 0.00,
	FOREIGN KEY(TRANSID) REFERENCES WHOLESALETRANSACTION(TRANSID) ON DELETE CASCADE
)ENGINE='InnoDB';

CREATE TABLE WHOLESALESILVERITEM(
	SHOPID SMALLINT UNSIGNED NOT NULL,
	ITEMNAME VARCHAR(255) NOT NULL,
	OWNER ENUM('S', 'U') DEFAULT 'S' NOT NULL,
	QUANTITY SMALLINT NOT NULL DEFAULT 0,
	SILVERWEIGHTSTOCK WEIGHT DECIMAL(12, 3) DEFAULT 0.000,
	FOREIGN KEY(SHOPID)REFERENCES SHOP(SHOPID) ON DELETE CASCADE,
	PRIMARY KEY(SHOPID, ITEMNAME)
)ENGINE='InnoDB';

CREATE TABLE WHOLESALESTAFF(
	SHOPID SMALLINT UNSIGNED NOT NULL,
	STAFFNAME VARCHAR(255) NOT NULL,
	OWNER ENUM('S', 'U') DEFAULT 'S' NOT NULL,
	FOREIGN KEY(SHOPID)REFERENCES SHOP(SHOPID) ON DELETE CASCADE,
	PRIMARY KEY(SHOPID, STAFFNAME)
)ENGINE='InnoDB';