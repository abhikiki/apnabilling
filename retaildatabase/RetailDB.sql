CREATE TABLE SHOP(
	SHOPID SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
	SHOPNAME VARCHAR(255),
	TINNUMBER  INT UNSIGNED ,
	USERNAME VARCHAR(255) NOT NULL,
	USERPASSWORD VARCHAR(255) NOT NULL,
	ROLE VARCHAR(255) NOT NULL,
	UNIQUE(TINNUMBER),
	PRIMARY KEY(SHOPID)
)ENGINE=InnoDB;

CREATE TABLE RETAILTRANSACTION(
	SHOPID SMALLINT UNSIGNED,
	TRANSID BIGINT UNSIGNED AUTO_INCREMENT,
	TRANSDATE DATETIME NOT NULL,
	BILLTYPE ENUM('I', 'E') NOT NULL,
	FOREIGN KEY(SHOPID)REFERENCES SHOP(SHOPID) ON DELETE CASCADE,
	PRIMARY KEY(TRANSID)
)ENGINE=InnoDB;

CREATE TABLE RETAILTAXINVOICE (
   TRANSID BIGINT UNSIGNED,
   INVOICENUMBER BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   INVOICESTATUS ENUM('A', 'I') DEFAULT 'A' NOT NULL,
   FOREIGN KEY(TRANSID)REFERENCES RETAILTRANSACTION(TRANSID) ON DELETE CASCADE,
   PRIMARY KEY(INVOICENUMBER)
) ENGINE='InnoDB';

CREATE TABLE RETAILGOLDITEMTRANSACTION(
	TRANSID BIGINT UNSIGNED,
	GOLDTYPE VARCHAR(255) NOT NULL,
	ITEMNAME VARCHAR(255) NOT NULL,
	QUANTITY DECIMAL(4) UNSIGNED,
	PIECEPAIR VARCHAR(255) NOT NULL,
	WEIGHT   DECIMAL(9, 4) UNSIGNED,
	MAKINGCHARGE  DECIMAL(10, 4) UNSIGNED,
	MAKINGCHARGETYPE VARCHAR(8) NOT NULL,
	GOLDRATE DECIMAL(10, 4) UNSIGNED,
	ITEMPRICE DECIMAL(12, 3) UNSIGNED,
	FOREIGN KEY(TRANSID) REFERENCES RETAILTRANSACTION(TRANSID) ON DELETE CASCADE
)ENGINE='InnoDB';

CREATE TABLE RETAILSILVERITEMTRANSACTION(
	TRANSID BIGINT UNSIGNED,
	ITEMNAME VARCHAR(255) NOT NULL,
	QUANTITY DECIMAL(4) UNSIGNED,
	PIECEPAIR VARCHAR(255) NOT NULL,
	WEIGHT   DECIMAL(9, 4) UNSIGNED,
	MAKINGCHARGE  DECIMAL(10, 4) UNSIGNED,
	MAKINGCHARGETYPE VARCHAR(8) NOT NULL,
	SILVERRATE DECIMAL(10, 4) UNSIGNED,
	ITEMPRICE DECIMAL(12, 3) UNSIGNED,
	FOREIGN KEY(TRANSID) REFERENCES RETAILTRANSACTION(TRANSID) ON DELETE CASCADE
)ENGINE='InnoDB';

CREATE TABLE RETAILDIAMONDITEMTRANSACTION(
	TRANSID BIGINT UNSIGNED,
	ITEMNAME VARCHAR(255) NOT NULL,
	QUANTITY DECIMAL(4) UNSIGNED,
	PIECEPAIR VARCHAR(255) NOT NULL,
	GOLDWEIGHT   DECIMAL(9, 4) UNSIGNED,
	DIAMONDWEIGHT DECIMAL(9, 4) UNSIGNED,
	DIAMONDPIECECOUNT  DECIMAL(4) UNSIGNED,
	CERTIFICATE VARCHAR(3) NOT NULL,
	ITEMPRICE DECIMAL(12, 3) UNSIGNED,
	FOREIGN KEY(TRANSID) REFERENCES RETAILTRANSACTION(TRANSID) ON DELETE CASCADE
)ENGINE='InnoDB';

CREATE TABLE RETAILGENERALITEMTRANSACTION(
	TRANSID BIGINT UNSIGNED,
	ITEMNAME VARCHAR(255) NOT NULL,
	QUANTITY DECIMAL(4) UNSIGNED,
	PIECEPAIR VARCHAR(255) NOT NULL,
	ITEMPRICE DECIMAL(12, 3) UNSIGNED,
	FOREIGN KEY(TRANSID) REFERENCES RETAILTRANSACTION(TRANSID) ON DELETE CASCADE
)ENGINE='InnoDB';

CREATE TABLE RETAILTRANSACTIONPRICE(
	TRANSID BIGINT UNSIGNED,
	TOTALITEMSPRICE DECIMAL(12, 3) UNSIGNED NOT NULL,
	DISCOUNT DECIMAL(12, 3) UNSIGNED ,
	VATPERCENT DECIMAL(12, 3) UNSIGNED ,
	VATONNEWITEMS DECIMAL(12, 3) UNSIGNED,
	OLDPURCHASEPRICE DECIMAL(12, 3) UNSIGNED,
	NETPAYABLEPRICE DECIMAL(12, 3) UNSIGNED,
	ADVANCEPAYMENT DECIMAL(12, 3) UNSIGNED,
	NOTES VARCHAR(1024) ,
	INCLUDEPRICE VARCHAR(3) ,
	QUANTITY DECIMAL(4) UNSIGNED,
	PIECEPAIR VARCHAR(255) NOT NULL,
	ITEMPRICE DECIMAL(12, 3) UNSIGNED,
	FOREIGN KEY(TRANSID) REFERENCES RETAILTRANSACTION(TRANSID) ON DELETE CASCADE
)ENGINE='InnoDB';