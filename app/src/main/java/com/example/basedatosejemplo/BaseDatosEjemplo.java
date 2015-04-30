package com.example.basedatosejemplo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.basedatosejemplo.datos.Persona;
import com.example.basedatosejemplo.utilidades.SQLite;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BaseDatosEjemplo extends Activity {

	private SQLite sqLiteUtil;
	private List<Persona> personas;

	private TextView resultadoV;

	private static final int VERSION_ = 1;
	private static final String BASEDATOS = "basedatos.db";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_datos_ejemplo);

		resultadoV = (TextView) findViewById(R.id.resultadoV);

		Set<String> tablas = new HashSet<String>();

		tablas.add("CREATE TABLE persona ("
				+ "persona_id Integer PRIMARY KEY AUTOINCREMENT, "
				+ "nombre Text," + "cedula Text," + "fechaNac DATE,"
				+ "estadoCivil Text," + "discapacitado Integer,"
				+ "estatura Double ); ");

		sqLiteUtil = new SQLite(getApplicationContext(), BASEDATOS, VERSION_, tablas);
		personas = new ArrayList<Persona>();
	}

	public void vaciar(View v) {
		sqLiteUtil.delete("persona", null, null);
		resultadoV.setText("");
	}
	
	public void insertar(View v) throws ParseException {
		Map<String, Object> dato = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();

		personas.clear();
		personas.add(new Persona("Persona 1", "111111", new SimpleDateFormat("dd/MM/yyyy").parse("12/03/2014"), "casado", false, 5.2f));
		personas.add(new Persona("Persona 2", "222222", new Date(), "soltero", true, 5.4f));

		Integer i = 0;
		for (Persona p : personas) {
			dato.clear();
			dato.put("nombre", p.getNombre());
			dato.put("cedula", p.getCedula());
			dato.put("fechaNac", p.getFechaNac());
			dato.put("estadoCivil", p.getEstadoCivil());
			dato.put("discapacitado", p.getDiscapacitado());
			dato.put("estatura", p.getEstatura());

			sqLiteUtil.insert("persona", dato);
			sb.append("Insertado registro ").append(++i).append("\n");
		}
		resultadoV.setText(sb.toString());

	}


	public void mostrar(View v) {
		List<Map<String, ?>> resultSet;
		Map<String, ?> registro;
		StringBuilder sb = new StringBuilder();

		resultadoV.setText("");
		resultSet = sqLiteUtil.select("persona", null, null, null);

		if (resultSet == null) {
			resultadoV.setText("No existen datos");
			return;
		}
		
		personas.clear();
		for (Iterator<Map<String, ?>> it = resultSet.iterator(); it.hasNext();) {
			registro = (Map<String, ?>) it.next();

			try {
				personas.add(new Persona(registro.get("nombre").toString(),
						registro.get("cedula").toString(),
						new SimpleDateFormat("yyyy-MM-dd").parse(registro.get(
								"fechaNac").toString()), 
								registro.get("estadoCivil").toString(), 
								(registro.get("discapacitado").equals("1")),
								Float.parseFloat(registro.get("estatura").toString())));
				
			} catch (NumberFormatException e) {
				sb.append("ERROR ").append(e.toString()).append("\n");
			} catch (ParseException e) {
				sb.append("ERROR ").append(e.toString()).append("\n");
			}

		}
		for (Persona p : personas) {
			sb.append(p.toString("json")).append("\n");
		}

		resultadoV.setText(sb.toString());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base_datos_ejemplo, menu);
		return true;
	}

}
