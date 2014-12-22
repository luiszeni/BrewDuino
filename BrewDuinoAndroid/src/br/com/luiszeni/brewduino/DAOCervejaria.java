package br.com.luiszeni.brewduino;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

import android.widget.EditText;

import com.example.brewduinoandroid.R;

public class DAOCervejaria {

	public String ip;
	
	public DAOCervejaria(String ip) {
		super();
		this.ip = ip;
	}

	public Cervejaria lerDados() throws ExecutionException{
		try {
			return new SendData().execute("http://" + ip + "/").get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public Cervejaria setRelayBombON(boolean status) throws ExecutionException{
		try {
			int value = status ? 1:0 ;
			return new SendData().execute("http://" + ip + "/?relayBombON="+value).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public Cervejaria setRelayLavON(boolean status) throws ExecutionException{
		try {
			int value = status ? 1:0 ;
			return new SendData().execute("http://" + ip + "/?relayLavON="+value).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public Cervejaria setRelayCaldON(boolean status) throws ExecutionException{
		try {
			int value = status ? 1:0 ;
			return new SendData().execute("http://" + ip + "/?relayCaldON="+value).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public Cervejaria sendTemps(double settedMostTemp, double tempThresoldMost,  double settedLavTemp,  double tempThresoldLav) throws ExecutionException{
		try {
			DecimalFormat df = new DecimalFormat("00.00");
			return new SendData().execute("http://" + ip + "/?settedMostTemp="+df.format(settedMostTemp) + "&tempThresholdMost="+df.format(tempThresoldMost) + "&settedLavTemp="+df.format(settedLavTemp)  + "&tempThresholdLav="+df.format(tempThresoldLav) ).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
