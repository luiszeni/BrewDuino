package br.com.luiszeni.brewduino;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.brewduinoandroid.R;

public class MainActivity extends Activity implements OnClickListener {
	private Cervejaria cervejaria;
	private Cervejaria cervejariaUltimaLeitura;
	private DAOCervejaria dao;

	private TextView tvTempMost;
	private TextView tvTempCald;
	private TextView tvTempLav;
	private TextView tvUltimaLeitura;
	private TextView tvMosturacao;
	private TextView tvLavagem;
	private TextView tvBomba;
	
	private ToggleButton tbRelayCaldON;
	private ToggleButton tbRelayLavON;
	private ToggleButton tbRelayBombON;
	
	private CheckBox cbRelayCald;
	private CheckBox cbRelayLav;

	private EditText etSettedMostTemp;
	private EditText etTempThresoldMost;
	private EditText etSettedLavTemp;
	private EditText etTempThresoldLav;
	private final Handler myHandler = new Handler();
	private Button btEnviar;
	private Button btLerDados;

	private SimpleDateFormat sdf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("Brewduino", "Inicializou");

		cervejaria = new Cervejaria(false, false, false, false, false, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		cervejariaUltimaLeitura= new Cervejaria(true, true, true, true, true, 1.0,
				1.0, 01.0, 01.0, 01.0, 01.0, 01.0);
		setContentView(R.layout.activity_main);
		sdf = new SimpleDateFormat("HH:mm:ss");
		tvTempMost = (TextView) findViewById(R.id.tvTempMost);
		tvTempCald = (TextView) findViewById(R.id.tvTempCald);
		tvTempLav = (TextView) findViewById(R.id.tvTempLav);
		tvUltimaLeitura = (TextView) findViewById(R.id.tvUltimaLeitura);
		tvMosturacao = (TextView) findViewById(R.id.tvMosturacao);
		tvLavagem = (TextView) findViewById(R.id.tvLavagem);
		tvBomba = (TextView) findViewById(R.id.tvBomba);
		
		tbRelayCaldON = (ToggleButton) findViewById(R.id.tbRelayCaldON);
		tbRelayLavON = (ToggleButton) findViewById(R.id.tbRelayLavON);
		tbRelayBombON = (ToggleButton) findViewById(R.id.tbRelayBombON);

		cbRelayCald = (CheckBox) findViewById(R.id.cbRelayCald);
		cbRelayLav = (CheckBox) findViewById(R.id.cbRelayLav);

		etSettedMostTemp = (EditText) findViewById(R.id.etSettedMostTemp);
		etTempThresoldMost = (EditText) findViewById(R.id.etTempThresoldMost);
		etSettedLavTemp = (EditText) findViewById(R.id.etSettedLavTemp);
		etTempThresoldLav = (EditText) findViewById(R.id.etTempThresoldLav);

		btEnviar = (Button) findViewById(R.id.btEnviar);
		btLerDados = (Button) findViewById(R.id.brLerDados);

		btEnviar.setOnClickListener(this);
		btLerDados.setOnClickListener(this);

		tbRelayCaldON.setOnClickListener(this);
		tbRelayLavON.setOnClickListener(this);
		tbRelayBombON.setOnClickListener(this);

		dao = new DAOCervejaria("192.168.0.25");// TODO colocar em algum lugar
												// configuravel esse cara

		Timer myTimer = new Timer();
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				UpdateGUI();
			}
		}, 0, 5000);

	}

	private void UpdateGUI() {
		// tv.setText(String.valueOf(i));
		myHandler.post(myRunnable);
	}

	final Runnable myRunnable = new Runnable() {
		public void run() {
			statusMessage("Lendo dados...",true);	

			try {
				ConnectivityManager cm = (ConnectivityManager) MainActivity.this
						.getSystemService(Context.CONNECTIVITY_SERVICE);

				NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
				boolean isConnected = activeNetwork.isConnectedOrConnecting();
				boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

				Log.i("Brewduino", "Status rede: isConnected-" + isConnected
						+ " isWiFi-" + isWiFi);

				if (!isConnected) {
					statusMessage("Sem Rede",true);	
					return;
				} else if (!isWiFi) {
					statusMessage("Sem Rede",true);	
					return;
				}
				Cervejaria leituraCervejaria = dao.lerDados();
				if (leituraCervejaria != null) {
					cervejaria = leituraCervejaria;
					readNotSettedParammeters();
					cervejariaUltimaLeitura = cervejaria;
					
				} else {
					statusMessage("Sem Rede: Modulo Arduino",false);	
				}
			} catch (NullPointerException e) {
				statusMessage("Sem rede: Verifique wifi",false);	
			}catch (Exception e) {
				e.printStackTrace();
			}

		}
	};
	
	
	private void statusMessage(String message, boolean enabled){
		tvUltimaLeitura.setText(message);
		if (enabled) {
			tvUltimaLeitura.setTextColor(Color.rgb(0, 0, 0));
				
		}else{
			tvUltimaLeitura.setTextColor(Color.rgb(200, 0, 0));
		}
		Log.i("Brewduino", message);
		enableALL(enabled);
	}

	private void readNotSettedParammeters() {
		tvTempMost.setText("Temp Mosturação: " + cervejaria.getTempMost()
				+ "°C");
		tvTempCald.setText("Temp Caldeira: " + cervejaria.getTempCald() + "°C");
		tvTempLav.setText("Temp Lavagem: " + cervejaria.getTempLav() + "°C");

		tvUltimaLeitura.setText("Ultima Leitura: " + sdf.format(new Date()));// TODO
																				// mostrar
																				// os
																				// segundos
																				// da
		// ultima leitura

		cbRelayCald.setChecked(cervejaria.isRelayCald());
		cbRelayLav.setChecked(cervejaria.isRelayLav());
		
		
		
		if(cervejariaUltimaLeitura.getSettedMostTemp() != cervejaria.getSettedMostTemp()){
			etSettedMostTemp.setText(cervejaria.getSettedMostTemp() + "");
		}
		
		if(cervejariaUltimaLeitura.getTempThresholdMost() != cervejaria.getTempThresholdMost()){
			etTempThresoldMost.setText(cervejaria.getTempThresholdMost() + "");
		}
		
		if(cervejariaUltimaLeitura.getSettedLavTemp() != cervejaria.getSettedLavTemp()){
			etSettedLavTemp.setText(cervejaria.getSettedLavTemp() + "");
		}
		
		if(cervejariaUltimaLeitura.getTempThresholdLav() != cervejaria.getTempThresholdLav()){
			etTempThresoldLav.setText(cervejaria.getTempThresholdLav() + "");

		}
		
		
		
		tbRelayCaldON.setChecked(cervejaria.isRelayCaldON());
		tbRelayLavON.setChecked(cervejaria.isRelayLavON());
		tbRelayBombON.setChecked(cervejaria.isRelayBombON());
		
	}

	private void enableALL(boolean enabled){
		etSettedMostTemp.setEnabled(enabled);
		etTempThresoldMost.setEnabled(enabled);
		etSettedLavTemp.setEnabled(enabled);
		etTempThresoldLav.setEnabled(enabled);

		tbRelayCaldON.setEnabled(enabled);
		tbRelayLavON.setEnabled(enabled);
		tbRelayBombON.setEnabled(enabled);
		
		tvTempMost.setEnabled(enabled);
		tvTempCald.setEnabled(enabled);
		tvTempLav.setEnabled(enabled);
		btEnviar.setEnabled(enabled);
		btLerDados.setEnabled(enabled);
		tvUltimaLeitura.setEnabled(enabled);
		tvMosturacao.setEnabled(enabled);
		tvLavagem.setEnabled(enabled);
		tvBomba.setEnabled(enabled);
		
	}
	
	@Override
	public void onClick(View v) {
		try {
			if (R.id.btEnviar == v.getId()) {

				double settedMostTemp = Double.parseDouble(etSettedMostTemp
						.getText().toString());
				double tempThresoldMost = Double.parseDouble(etTempThresoldMost
						.getText().toString());
				double dettedLavTemp = Double.parseDouble(etSettedLavTemp
						.getText().toString());
				double tempThresoldLav = Double.parseDouble(etTempThresoldLav
						.getText().toString());

				dao.sendTemps(settedMostTemp, tempThresoldMost, dettedLavTemp,
						tempThresoldLav);

			} else if (R.id.tbRelayBombON == v.getId()) {
				dao.setRelayBombON(tbRelayBombON.isChecked());
			} else if (R.id.tbRelayCaldON == v.getId()) {
				dao.setRelayCaldON(tbRelayCaldON.isChecked());
			} else if (R.id.tbRelayLavON == v.getId()) {
				dao.setRelayLavON(tbRelayLavON.isChecked());
			}
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, "Erro ao Enviar os Dados",
					Toast.LENGTH_SHORT).show();
			Log.i("Brewduino", "erro ao ler os dados");
		}
		onResume();
	}

}
