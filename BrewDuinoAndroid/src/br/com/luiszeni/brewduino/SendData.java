package br.com.luiszeni.brewduino;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class SendData extends AsyncTask<String, Void, Cervejaria>{

	
		@Override
		protected Cervejaria doInBackground(String... params) {

			if (params.length > 0) {
				String URL = params[0];

				String linha = "";

				if (params.length > 0)

					try {

						HttpClient client = new DefaultHttpClient();
						HttpGet requisicao = new HttpGet();
						requisicao.setHeader("Content-Type",
								"text/plain; charset=utf-8");
						requisicao.setURI(new URI(URL));
						HttpResponse resposta = client.execute(requisicao);
						BufferedReader br = new BufferedReader(
								new InputStreamReader(resposta.getEntity()
										.getContent()));
						StringBuffer sb = new StringBuffer("");

						while ((linha = br.readLine()) != null) {
							sb.append(linha);
						}

						br.close();

						
						linha =  sb.toString();
	
			
						Cervejaria cervejaria = new Cervejaria();
						
						cervejaria.setRelayLav(getValueAsBoolean(linha, "relayLav"));
						cervejaria.setRelayLavON(getValueAsBoolean(linha, "relayLavON"));
						cervejaria.setRelayCald(getValueAsBoolean(linha, "relayCald"));
						cervejaria.setRelayCaldON(getValueAsBoolean(linha, "relayCaldON"));
						cervejaria.setRelayBombON(getValueAsBoolean(linha, "relayBombON"));
						cervejaria.setTempMost(getValueAsDouble(linha, "tempMost"));
						cervejaria.setTempCald(getValueAsDouble(linha, "tempCald"));
						cervejaria.setTempLav(getValueAsDouble(linha, "tempLav"));
						cervejaria.setSettedMostTemp(getValueAsDouble(linha, "settedMostTemp"));
						cervejaria.setSettedLavTemp(getValueAsDouble(linha, "settedLavTemp"));
						cervejaria.setTempThresholdMost(getValueAsDouble(linha, "tempThresholdMost"));
						cervejaria.setTempThresholdLav(getValueAsDouble(linha, "tempThresholdLav"));
										
						return cervejaria;					
					} catch (Exception e) {
						e.printStackTrace();
					}

			}

			return null;
		}

		 public double getValueAsDouble(String code, String tag){
			return Double.parseDouble(getValue( code,  tag)); 
		 }
		
		 public boolean getValueAsBoolean(String code, String tag){
			if (getValue( code,  tag).equals("1")) {
				 return true;	
			}
			return false;	
		 }
		 
	    public String getValue(String code, String tag){
	    	code = code.toLowerCase();
	    	tag = tag.toLowerCase();  	
	    	String tagInit = "<" + tag + ">";
	    	
	    	int init = code.indexOf(tagInit) +  tagInit.length()  ;
	    	
	    	int end = code.indexOf("</" + tag + ">");
	    	
	    	
	    	return (String) code.subSequence(init, end);
	    }
}
