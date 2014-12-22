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

	public Cervejaria lerDados() throws ExecutionException, InterruptedException{
			return new SendData().execute("http://" + ip + "/").get();
	}
	
	
	public Cervejaria setRelayBombON(boolean status) throws ExecutionException, InterruptedException{

			int value = status ? 1:0 ;
			return new SendData().execute("http://" + ip + "/?relayBombON="+value).get();
	}
	
	public Cervejaria setRelayLavON(boolean status) throws ExecutionException, InterruptedException{
			int value = status ? 1:0 ;
			return new SendData().execute("http://" + ip + "/?relayLavON="+value).get();

	}
	
	public Cervejaria setRelayCaldON(boolean status) throws ExecutionException, InterruptedException{
			int value = status ? 1:0 ;
			return new SendData().execute("http://" + ip + "/?relayCaldON="+value).get();

	}
	
	public Cervejaria sendTemps(double settedMostTemp, double tempThresoldMost,  double settedLavTemp,  double tempThresoldLav) throws ExecutionException, InterruptedException{
			DecimalFormat df = new DecimalFormat("00.00");
			new SendData().execute("http://" + ip + "/?tempThresholdMost="+df.format(tempThresoldMost) ).get();
			new SendData().execute("http://" + ip + "/?settedLavTemp="+df.format(settedLavTemp) ).get();
			new SendData().execute("http://" + ip + "/?tempThresholdLav="+df.format(tempThresoldLav)).get();
			return new SendData().execute("http://" + ip + "/?settedMostTemp="+df.format(settedMostTemp)).get();
	}
	
}
