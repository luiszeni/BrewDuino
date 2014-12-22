package br.com.luiszeni.brewduino;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
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
	private DAOCervejaria dao;

	private TextView tvTempMost;
	private TextView tvTempCald;
	private TextView tvTempLav;
	private TextView tvUltimaLeitura;

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
	private Button brLerDados;

	private SimpleDateFormat sdf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sdf = new SimpleDateFormat("HH:mm:ss");
		tvTempMost = (TextView) findViewById(R.id.tvTempMost);
		tvTempCald = (TextView) findViewById(R.id.tvTempCald);
		tvTempLav = (TextView) findViewById(R.id.tvTempLav);
		tvUltimaLeitura = (TextView) findViewById(R.id.tvUltimaLeitura);

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
		brLerDados = (Button) findViewById(R.id.brLerDados);

		btEnviar.setOnClickListener(this);
		brLerDados.setOnClickListener(this);

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
			try {
				cervejaria = dao.lerDados();
			} catch (ExecutionException e) {
				Toast.makeText(MainActivity.this, "Erro alo Ler os Dados", Toast.LENGTH_SHORT).show();
			}
			readNotSettedParammeters();
		}
	};

	private void readNotSettedParammeters() {
		tvTempMost.setText("Temp Mosturação: " + cervejaria.getTempMost() + "°C");
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
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			cervejaria = dao.lerDados();
		} catch (ExecutionException e) {
			Toast.makeText(this, "Erro alo Ler os Dados", Toast.LENGTH_SHORT).show();
		}

		readNotSettedParammeters();

		etSettedMostTemp.setText(cervejaria.getSettedMostTemp() + "");
		etTempThresoldMost.setText(cervejaria.getTempThresholdMost() + "");
		etSettedLavTemp.setText(cervejaria.getSettedLavTemp() + "");
		etTempThresoldLav.setText(cervejaria.getTempThresholdLav() + "");

		tbRelayCaldON.setChecked(cervejaria.isRelayCaldON());
		tbRelayLavON.setChecked(cervejaria.isRelayLavON());
		tbRelayBombON.setChecked(cervejaria.isRelayBombON());

	}

	@Override
	public void onClick(View v) {
		try {
			if (R.id.btEnviar == v.getId()) {

				double settedMostTemp = Double.parseDouble(etSettedMostTemp.getText().toString());
				double tempThresoldMost = Double.parseDouble(etTempThresoldMost.getText().toString());
				double dettedLavTemp = Double.parseDouble(etSettedLavTemp.getText().toString());
				double tempThresoldLav = Double.parseDouble(etTempThresoldLav.getText().toString());

				dao.sendTemps(settedMostTemp, tempThresoldMost, dettedLavTemp, tempThresoldLav);

			} else if (R.id.tbRelayBombON == v.getId()) {
				dao.setRelayBombON(tbRelayBombON.isChecked());
			} else if (R.id.tbRelayCaldON == v.getId()) {
				dao.setRelayCaldON(tbRelayCaldON.isChecked());
			} else if (R.id.tbRelayLavON == v.getId()) {
				dao.setRelayLavON(tbRelayLavON.isChecked());
			}
		} catch (ExecutionException e) {
			Toast.makeText(MainActivity.this, "Erro ao Enviar os Dados", Toast.LENGTH_SHORT).show();
		}
		onResume();
	}

}
