package gdgtenerife.tlp.apis.mapas;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity {

	private GoogleMap mapa = null;
	private AutoCompleteTextView lugar = null;
	private DetailsPlaceOne geoLugar;
	private FillPlace buscarLugar;
	private Marker marcador=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		
		// TODO: Explicar Esto
		lugar = (AutoCompleteTextView) findViewById(R.id.editTextPlace);
		
		// Preparamos el adaptador usado para mostrar las propuestas de autocompletado, en este caso una lista
		final ArrayAdapter<String> adapterFrom = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		adapterFrom.setNotifyOnChange(true);
		lugar.setAdapter(adapterFrom);
		// Monitorizamos el evento de cambio en el campo a autocompletar para buscar las propuestas de autocompletado
		lugar.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Calculamos el autocompletado
				if (count == 0){
					if (marcador != null)
						marcador.remove();
				} else if (count%3 == 1){
					adapterFrom.clear();
					// Ejecutamos en segundo plano la busqueda de propuestas de autocompletado
					buscarLugar = new FillPlace(adapterFrom, lugar, getBaseContext());
					buscarLugar.execute(lugar.getText().toString());
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {}
		});
		
		lugar.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				geoLugar = new DetailsPlaceOne();
		        // TODO: ¡CUIDADO!
		        while(buscarLugar.referencesPlace.isEmpty()){}
		        geoLugar.setListener(new PlaceToPointMap_TaskListener(lugar.getText().toString()));
				geoLugar.execute(buscarLugar.referencesPlace.get(lugar.getText().toString()));
			}

        });

	}
	
	private class PlaceToPointMap_TaskListener implements IStandardTaskListener {

        private String markerStr;
        
        public PlaceToPointMap_TaskListener(String markerStr) {
        	this.markerStr =  markerStr;
        }
        
        @Override
        public void taskComplete(Object result) {
        	if ((Boolean)result){
        		// Recogemos la posicion que hemos pedido al Web Service de Google Place
        		Double lat = geoLugar.coordinates.get("lat");
        		Double lng = geoLugar.coordinates.get("lng");
        		LatLng go = new LatLng(lat, lng);
        		
        		// Creamos la animación de movimiento hacia el lugar que hemos introducido
        		CameraPosition camPos = new CameraPosition.Builder()
	                .target(go)
	                .zoom(17)         //Establecemos el zoom en 17
	                .tilt(45)         //Bajamos el punto de vista de la cámara 70 grados
	                .build();
         
        		// Lanzamos el movimiento
        		CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);
        		mapa.animateCamera(camUpd);
        		
        		// Insertamos el Marcador
        		mapa.addMarker(new MarkerOptions()
    	        	.position(new LatLng(lat, lng))
    	        	.title(markerStr)
    	        	.draggable(true)
    	        	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        		
        		// Esconder teclado para que se vea la animación del mapa
        		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        	}
        }
	}

}
