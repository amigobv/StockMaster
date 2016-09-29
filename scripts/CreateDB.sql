-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema StockMasterDb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema StockMasterDb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `StockMasterDb` DEFAULT CHARACTER SET utf8 ;
USE `StockMasterDb` ;

--DROP TABLE IF EXISTS `StockMasterDb`.`Entry`;
--DROP TABLE IF EXISTS `StockMasterDb`.`Ticker`;

-- -----------------------------------------------------
-- Table `StockMasterDb`.`Entry`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `StockMasterDb`.`Entry` (
  `idEntry` INT NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL,
  `open` DOUBLE NULL,
  `high` DOUBLE NULL,
  `low` DOUBLE NULL,
  `close` DOUBLE NULL,
  `volume` DOUBLE NULL,
  `value` DOUBLE NULL,
  `rs` DOUBLE NULL,
  `rsi` DOUBLE NULL,
  PRIMARY KEY (`idEntry`),
  UNIQUE INDEX `idEntry_UNIQUE` (`idEntry` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `StockMasterDb`.`Ticker`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `StockMasterDb`.`Ticker` (
  `idTicker` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `symbol` VARCHAR(45) NULL,
  `exchange` VARCHAR(45) NULL,
  `entry` INT NULL,
  PRIMARY KEY (`idTicker`),
  INDEX `entry_idx` (`entry` ASC),
  UNIQUE INDEX `idTicker_UNIQUE` (`idTicker` ASC),
  CONSTRAINT `FK_Entry`
    FOREIGN KEY (`entry`)
    REFERENCES `StockMasterDb`.`Entry` (`idEntry`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
