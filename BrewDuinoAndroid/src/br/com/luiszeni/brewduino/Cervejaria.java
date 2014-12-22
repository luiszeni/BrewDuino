package br.com.luiszeni.brewduino;

public class Cervejaria {

	private boolean relayLav;
	private boolean relayLavON;
	private boolean relayCald;
	private boolean relayCaldON;
	private boolean relayBombON;

	private double tempMost;
	private double tempCald;
	private double tempLav;

	private double settedMostTemp;
	private double settedLavTemp;
	private double tempThresholdMost;
	private double tempThresholdLav;

	public Cervejaria() {
	}

	public Cervejaria(boolean relayLav, boolean relayLavON, boolean relayCald, boolean relayCaldON, boolean relayBombON, double tempMost, double tempCald, double tempLav, double settedMostTemp, double settedLavTemp, double tempThresholdMost, double tempThresholdLav) {
		super();
		this.relayLav = relayLav;
		this.relayLavON = relayLavON;
		this.relayCald = relayCald;
		this.relayCaldON = relayCaldON;
		this.relayBombON = relayBombON;
		this.tempMost = tempMost;
		this.tempCald = tempCald;
		this.tempLav = tempLav;
		this.settedMostTemp = settedMostTemp;
		this.settedLavTemp = settedLavTemp;
		this.tempThresholdMost = tempThresholdMost;
		this.tempThresholdLav = tempThresholdLav;
	}

	public boolean isRelayLav() {
		return relayLav;
	}

	public void setRelayLav(boolean relayLav) {
		this.relayLav = relayLav;
	}

	public boolean isRelayLavON() {
		return relayLavON;
	}

	public void setRelayLavON(boolean relayLavON) {
		this.relayLavON = relayLavON;
	}

	public boolean isRelayCald() {
		return relayCald;
	}

	public void setRelayCald(boolean relayCald) {
		this.relayCald = relayCald;
	}

	public boolean isRelayCaldON() {
		return relayCaldON;
	}

	public void setRelayCaldON(boolean relayCaldON) {
		this.relayCaldON = relayCaldON;
	}

	public boolean isRelayBombON() {
		return relayBombON;
	}

	public void setRelayBombON(boolean relayBombON) {
		this.relayBombON = relayBombON;
	}

	public double getTempMost() {
		return tempMost;
	}

	public void setTempMost(double tempMost) {
		this.tempMost = tempMost;
	}

	public double getTempCald() {
		return tempCald;
	}

	public void setTempCald(double tempCald) {
		this.tempCald = tempCald;
	}

	public double getTempLav() {
		return tempLav;
	}

	public void setTempLav(double tempLav) {
		this.tempLav = tempLav;
	}

	public double getSettedMostTemp() {
		return settedMostTemp;
	}

	public void setSettedMostTemp(double settedMostTemp) {
		this.settedMostTemp = settedMostTemp;
	}

	public double getSettedLavTemp() {
		return settedLavTemp;
	}

	public void setSettedLavTemp(double settedLavTemp) {
		this.settedLavTemp = settedLavTemp;
	}

	public double getTempThresholdMost() {
		return tempThresholdMost;
	}

	public void setTempThresholdMost(double tempThresholdMost) {
		this.tempThresholdMost = tempThresholdMost;
	}

	public double getTempThresholdLav() {
		return tempThresholdLav;
	}

	public void setTempThresholdLav(double tempThresholdLav) {
		this.tempThresholdLav = tempThresholdLav;
	}

	@Override
	public String toString() {
		return "Cervejaria [relayLav=" + relayLav + ", relayLavON=" + relayLavON + ", relayCald=" + relayCald + ", relayCaldON=" + relayCaldON + ", relayBombON=" + relayBombON + ", tempMost=" + tempMost + ", tempCald=" + tempCald + ", tempLav=" + tempLav + ", settedMostTemp=" + settedMostTemp
				+ ", settedLavTemp=" + settedLavTemp + ", tempThresholdMost=" + tempThresholdMost + ", tempThresholdLav=" + tempThresholdLav + "]";
	}

}
