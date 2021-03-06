DROP PROCEDURE IF EXISTS CUSTOMMORTGAGETRANSACTIONSEARCH;
DELIMITER $$
CREATE PROCEDURE `CUSTOMMORTGAGETRANSACTIONSEARCH`(
	SHOPID SMALLINT UNSIGNED,
	TRANSSTATUS1 VARCHAR(1),		
	TRANSSTATUS2 VARCHAR(1),		
	STARTDATE DATETIME,
	ENDDATE DATETIME
)
BEGIN
	DECLARE v_finished INTEGER DEFAULT 0;
	DECLARE v_transId  BIGINT UNSIGNED;
	
	DECLARE transId_cursor CURSOR FOR  SELECT TRANSID FROM TRANSANCTION_ID_HOLDER;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished = 1;

CREATE TEMPORARY TABLE TRANSANCTION_SEARCH_RESULT(
	TRANSDATE DATETIME,
	TRANSENDDATE DATETIME,
	TRANSID BIGINT UNSIGNED,
	TRANSACTIONSTATUS ENUM('A', 'I'),
	KEEPERNAME VARCHAR(255),
	GOLDITEMS VARCHAR(2048) DEFAULT '',
	SILVERITEMS VARCHAR(2048) DEFAULT '',
	DIAMONDITEMS VARCHAR(2048) DEFAULT '',
	CUSTOMERNAME VARCHAR(512),
	CONTACTNUMBER VARCHAR(255),
	EMAILID VARCHAR(255),
	ADDRESS VARCHAR(1024),
	AMOUNT DECIMAL(12, 3) UNSIGNED,
	NOTES VARCHAR(2048) DEFAULT '',
	TOTALGOLDWEIGHT   DECIMAL(9, 3) UNSIGNED,
	TOTALSILVERWEIGHT   DECIMAL(9, 3) UNSIGNED,
	PERCENTAGE DECIMAL(12, 3) UNSIGNED NOT NULL,
	DAYSDIFF BIGINT UNSIGNED
);
CREATE TEMPORARY TABLE TRANSANCTION_ID_HOLDER(
	TRANSID BIGINT UNSIGNED
);
INSERT INTO TRANSANCTION_SEARCH_RESULT(
	TRANSDATE, TRANSENDDATE, TRANSID, TRANSACTIONSTATUS, KEEPERNAME, CUSTOMERNAME, CONTACTNUMBER, EMAILID, ADDRESS, AMOUNT, NOTES, TOTALGOLDWEIGHT, TOTALSILVERWEIGHT, PERCENTAGE, DAYSDIFF)
	SELECT 	MT.TRANSDATE,
			ENDDATE,
			MT.TRANSID,
			MT.TRANSACTIONSTATUS,
			MT.KEEPERNAME,
			CONCAT(MC.FIRSTNAME, ' ', MC.LASTNAME) CUSTOMERNAME,
			MC.CONTACTNUMBER,
			MC.EMAILID,
			CONCAT(MC.STREETADDRESS1, ' ', MC.STREETADDRESS2, ' ', MC.CITY , ' ', MC.STATE, ' ', MC.ZIPCODE, ' ', MC.COUNTRY) ADDRESS,
			MT.TOTALPRICE,
			MT.NOTES,
			MT.TOTALGOLDWEIGHT,
			MT.TOTALSILVERWEIGHT,
			MT.PERCENTAGE,
			DATEDIFF(ENDDATE, MT.TRANSDATE)
			FROM SHOP S, MORTGAGETRANSACTION MT, MORTGAGECUSTOMER MC 
			WHERE S.SHOPID = MT.SHOPID
			AND MC.TRANSID = MT.TRANSID
			AND S.SHOPID = SHOPID
			AND MT.TRANSACTIONSTATUS in(TRANSSTATUS1, TRANSSTATUS2)
			AND CAST(MT.TRANSDATE AS DATE) BETWEEN CAST(STARTDATE AS DATE) AND CAST(ENDDATE AS DATE)
			ORDER BY MT.TRANSDATE DESC;
INSERT INTO TRANSANCTION_ID_HOLDER(TRANSID) SELECT TRANSID FROM TRANSANCTION_SEARCH_RESULT;
CREATE TEMPORARY TABLE GOLD_SILVER_ITEM_TABLE(
	ITEMNAME VARCHAR(255) DEFAULT '',
	WEIGHT   DECIMAL(9, 3) UNSIGNED,
	QUANTITY INT UNSIGNED,
	PIECEPAIR VARCHAR(255)
);

CREATE TEMPORARY TABLE DIAMOND_ITEM_TABLE(
	ITEMNAME VARCHAR(255) DEFAULT '',
	GOLDWEIGHT   DECIMAL(9, 3) UNSIGNED,
	DIAMONDWEIGHT   DECIMAL(9, 3) UNSIGNED,
	QUANTITY INT UNSIGNED,
	PIECEPAIR VARCHAR(255)
);

OPEN transId_cursor;
    get_transId: LOOP
	FETCH transId_cursor INTO v_transId;
	IF v_finished = 1 THEN 
	 LEAVE get_transId;
	END IF;
	BLOCK2: BEGIN
		DECLARE v_finished_inner INTEGER DEFAULT 0;
		DECLARE item_quantity_inner INT UNSIGNED;
		DECLARE itemname_inner  VARCHAR(255);
		DECLARE itempiecepair_inner  VARCHAR(255);
		DECLARE weight_inner DECIMAL(9,3);
		DECLARE weight_diamond_carat DECIMAL(9,2);
		DECLARE total_weight DECIMAL(9, 3) UNSIGNED;
		DECLARE itemnamelist VARCHAR(2048) DEFAULT NULL;
		DECLARE item_cursor CURSOR FOR  SELECT ITEMNAME, WEIGHT, QUANTITY, PIECEPAIR  FROM GOLD_SILVER_ITEM_TABLE;
		DECLARE diamond_item_cursor CURSOR FOR  SELECT ITEMNAME, GOLDWEIGHT, DIAMONDWEIGHT, QUANTITY, PIECEPAIR  FROM DIAMOND_ITEM_TABLE;
		DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished_inner = 1;
		SET itemnamelist = NULL;
		SET total_weight = 0.0;
		INSERT INTO GOLD_SILVER_ITEM_TABLE(ITEMNAME, WEIGHT, QUANTITY, PIECEPAIR) SELECT ITEMNAME, WEIGHT, QUANTITY, PIECEPAIR FROM MORTGAGEGOLDITEMTRANSACTION WHERE TRANSID = v_transId;
		OPEN item_cursor;
			 get_item: LOOP
				FETCH item_cursor INTO itemname_inner, weight_inner, item_quantity_inner, itempiecepair_inner;
				IF v_finished_inner = 1 THEN 
					LEAVE get_item;
				END IF;
				SET itemnamelist = CONCAT_WS(',', CONCAT(itemname_inner, '#', item_quantity_inner, '#', itempiecepair_inner, '#', weight_inner), itemnamelist);
				SET total_weight = total_weight + weight_inner;
			 END LOOP get_item;
		    UPDATE TRANSANCTION_SEARCH_RESULT SET GOLDITEMS = itemnamelist WHERE TRANSID = v_transId;
		CLOSE item_cursor;
		TRUNCATE GOLD_SILVER_ITEM_TABLE;
		
		SET v_finished_inner = 0;
		SET itemnamelist = NULL;
		SET total_weight = 0.0;
		INSERT INTO GOLD_SILVER_ITEM_TABLE(ITEMNAME, WEIGHT, QUANTITY, PIECEPAIR) SELECT ITEMNAME, WEIGHT, QUANTITY, PIECEPAIR FROM MORTGAGESILVERITEMTRANSACTION WHERE TRANSID = v_transId;
		OPEN item_cursor;
			 get_item: LOOP
				FETCH item_cursor INTO itemname_inner, weight_inner, item_quantity_inner, itempiecepair_inner;
				IF v_finished_inner = 1 THEN 
					LEAVE get_item;
				END IF;
				SET itemnamelist = CONCAT_WS(',', CONCAT(itemname_inner, '#', item_quantity_inner, '#', itempiecepair_inner, '#', weight_inner), itemnamelist);
			 END LOOP get_item;
			 UPDATE TRANSANCTION_SEARCH_RESULT SET SILVERITEMS = itemnamelist WHERE TRANSID = v_transId;
		CLOSE item_cursor;
		TRUNCATE GOLD_SILVER_ITEM_TABLE;
		
		SET v_finished_inner = 0;
		SET itemnamelist = NULL;
		SET total_weight = 0.0;
		
		INSERT INTO DIAMOND_ITEM_TABLE(ITEMNAME, GOLDWEIGHT, DIAMONDWEIGHT, QUANTITY, PIECEPAIR) SELECT ITEMNAME, GOLDWEIGHT, DIAMONDWEIGHT, QUANTITY, PIECEPAIR FROM MORTGAGEDIAMONDITEMTRANSACTION WHERE TRANSID = v_transId;
		OPEN diamond_item_cursor;
			 get_item: LOOP
				FETCH diamond_item_cursor INTO itemname_inner, weight_inner, weight_diamond_carat, item_quantity_inner, itempiecepair_inner;
				IF v_finished_inner = 1 THEN 
					LEAVE get_item;
				END IF;
				SET weight_diamond_carat = weight_inner;
				SET itemnamelist = CONCAT_WS(',', CONCAT(itemname_inner, '#', item_quantity_inner, '#', itempiecepair_inner, '#', weight_inner, '#', weight_diamond_carat), itemnamelist);
			 END LOOP get_item;
			 UPDATE TRANSANCTION_SEARCH_RESULT SET DIAMONDITEMS = itemnamelist WHERE TRANSID = v_transId;
		CLOSE diamond_item_cursor;
		TRUNCATE DIAMOND_ITEM_TABLE;
	END BLOCK2;
END LOOP get_transId;
CLOSE transId_cursor;
SELECT * FROM TRANSANCTION_SEARCH_RESULT;
DROP TEMPORARY TABLE TRANSANCTION_SEARCH_RESULT;
DROP TEMPORARY TABLE TRANSANCTION_ID_HOLDER;
DROP TEMPORARY TABLE GOLD_SILVER_ITEM_TABLE;
DROP TEMPORARY TABLE DIAMOND_ITEM_TABLE;
END$$
