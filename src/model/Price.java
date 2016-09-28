package model;

import java.time.LocalDate;
import java.util.Date;

public class Price {
	private LocalDate date;
	private double close;
	private double arithmeticMean;
	private double exponentiaMean;
	private double weightedMean;
	private double gain;
	private double loss;
	private double gainAverage;
	private double lossAverage;
	private double relativStrength;
	private double relativeStrengthIndex;
	
	public Price() {
	}
	
	public Price(LocalDate date, double close) {
		this.setDate(date);
		this.close = close;
	}
	
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public double getArithmeticMean() {
		return arithmeticMean;
	}

	public void setArithmeticMean(double arithmeticMean) {
		this.arithmeticMean = arithmeticMean;
	}

	public double getExponentiaMean() {
		return exponentiaMean;
	}

	public void setExponentiaMean(double exponentiaMean) {
		this.exponentiaMean = exponentiaMean;
	}

	public double getWeightedMean() {
		return weightedMean;
	}

	public void setWeightedMean(double weightedMean) {
		this.weightedMean = weightedMean;
	}

	public double getGain() {
		return gain;
	}
	public void setGain(double gain) {
		this.gain = gain;
	}
	public double getLoss() {
		return loss;
	}
	public void setLoss(double loss) {
		this.loss = loss;
	}
	public double getGainAverage() {
		return gainAverage;
	}
	public void setGainAverage(double average) {
		this.gainAverage = average;
	}
	public double getLossAverage() {
		return lossAverage;
	}
	public void setLossAverage(double average) {
		this.lossAverage = average;
	}
	public double getRelativStrength() {
		return relativStrength;
	}
	public void setRelativStrength(double relativStrength) {
		this.relativStrength = relativStrength;
	}
	public double getRelativeStrengthIndex() {
		return relativeStrengthIndex;
	}
	public void setRelativeStrengthIndex(double relativeStrengthIndex) {
		this.relativeStrengthIndex = relativeStrengthIndex;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void Fill(double gainAvg, double lossAvg, double rs, double rsi) {
		this.gainAverage = gainAvg;
		this.lossAverage = lossAvg;
		this.relativStrength = rs;
		this.relativeStrengthIndex = rsi;
	}
}
