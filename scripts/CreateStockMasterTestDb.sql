-- -----------------------------------------------------
-- Schema StockMasterDb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema StockMasterDb
-- -----------------------------------------------------
DROP SCHEMA StockTestDb RESTRICT;
CREATE SCHEMA StockTestDb;

-- -----------------------------------------------------
-- 
-- -----------------------------------------------------
DROP TABLE Entry;
DROP TABLE Ticker;

CREATE TABLE Ticker (
  idTicker INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  name VARCHAR(45) NOT NULL,
  symbol VARCHAR(45),
  exchange VARCHAR(45),
  PRIMARY KEY (idTicker));


-- -----------------------------------------------------
-- Table `StockMasterDb`.`Entry`
-- -----------------------------------------------------

CREATE TABLE Entry (
  idEntry INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  date DATE NOT NULL,
  openPrice DOUBLE ,
  highPrice DOUBLE ,
  lowPrice DOUBLE ,
  closePrice DOUBLE ,
  volume DOUBLE ,
  value DOUBLE ,
  rs DOUBLE ,
  rsi DOUBLE ,
  ticker INT ,
  PRIMARY KEY (idEntry),
  CONSTRAINT tickerFK FOREIGN KEY (ticker) REFERENCES Ticker (idTicker) ON DELETE CASCADE ON UPDATE RESTRICT);
