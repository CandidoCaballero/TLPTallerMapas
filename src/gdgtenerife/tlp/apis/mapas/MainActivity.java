package gdgtenerife.tlp.apis.mapas;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import gdgtenerife.tlp.apis.mapas.adapters.IStandardTaskListener;
import gdgtenerife.tlp.apis.mapas.servidor.RequestResponse;
import gdgtenerife.tlp.apis.mapas.servidor.ServerUtilities;
import gdgtenerife.tlp.apis.mapas.ws.DetailsPlaceOne;
import gdgtenerife.tlp.apis.mapas.ws.FillPlace;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity {

	private GoogleMap mapa = null;
	private AutoCompleteTextView lugar = null;
	private DetailsPlaceOne geoLugar;
	private FillPlace buscarLugar;
	private Marker marcador=null;
	private LatLng coordenadas=null;
	private Button botonCompartir = null;
	private static boolean registrado=false;
	
	AsyncTask<Void, Void, Void> mRegisterTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		
		// TODO: Explicar Esto
		botonCompartir = (Button) findViewById(R.id.botonCompartir);
		lugar = (AutoCompleteTextView) findViewById(R.id.editTextPlace);

		// Preparamos el adaptador usado para mostrar las propuestas de autocompletado, en este caso una lista
		final ArrayAdapter<String> adapterFrom = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		adapterFrom.setNotifyOnChange(true);
		lugar.setAdapter(adapterFrom);
		// Monitorizamos el evento de cambio en el campo a autocompletar para buscar las propuestas de autocompletado
		lugar.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Calculamos el autocompletado
				if (count%3 == 1){
					coordenadas = null;
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
				if (marcador != null)
					marcador.remove();
				geoLugar = new DetailsPlaceOne();
		        // TODO: ¡CUIDADO!
		        while(buscarLugar.referencesPlace.isEmpty()){}
		        geoLugar.setListener(new PlaceToPointMap_TaskListener(lugar.getText().toString()));
				geoLugar.execute(buscarLugar.referencesPlace.get(lugar.getText().toString()));
			}

        });
		
		// TODO: Explicar Esto
		botonCompartir.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (coordenadas != null){
					try {
						JSONObject data = new JSONObject();
	
						data.put("receiver", "Paco");
						if (coordenadas != null)
						data.put("latitud", coordenadas.latitude);
						data.put("longitud", coordenadas.longitude);
						StringEntity body = new StringEntity(data.toString(), "UTF-8");

			         	HttpPost post = ServerUtilities.getPost(ServerUtilities.ipServer, ServerUtilities.portServer, "gcm/send/", body);
			 			
			 			RequestResponse taskResquest = new RequestResponse();
			 	        
			 			taskResquest.setParams(MainActivity.this, ServerUtilities.getClient(), post);
			 			taskResquest.execute();
			        } catch (Exception e){
			        	Toast.makeText(MainActivity.this, "No se pudo establecer conexión con el servidor. Vuelva a intentarlo.", Toast.LENGTH_LONG).show();
			        	return;
			        }
					Toast.makeText(MainActivity.this, "Dirección Compartida con EXITO.", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(MainActivity.this, "La dirección es incosistente.", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		// TODO: Explicar esto
		if (!registrado)
			initGCM();
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
        		coordenadas = new LatLng(lat, lng);
        		
        		// Creamos la animación de movimiento hacia el lugar que hemos introducido
        		CameraPosition camPos = new CameraPosition.Builder()
	                .target(coordenadas)
	                .zoom(17)         //Establecemos el zoom en 17
	                .tilt(45)         //Bajamos el punto de vista de la cámara 70 grados
	                .build();
         
        		// Lanzamos el movimiento
        		CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);
        		mapa.animateCamera(camUpd);
        		
        		// Insertamos el Marcador
        		marcador = mapa.addMarker(new MarkerOptions()
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
	
	private void initGCM(){
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
        GCMRegistrar.register(getApplicationContext() , ServerUtilities.idClient);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            // Automatically registers application on startup.
            GCMRegistrar.register(getApplicationContext() , ServerUtilities.idClient);
        } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        boolean registered = ServerUtilities.register(context, regId);
                        // At this point all attempts to register with the app
                        // server failed, so we need to unregister the device
                        // from GCM - the app will try to register again when
                        // it is restarted. Note that GCM will send an
                        // unregistered callback upon completion, but
                        // GCMIntentService.onUnregistered() will ignore it.
                        if (!registered) {
                            GCMRegistrar.unregister(context);
                        } else {
                        	registrado=true;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
        }
   }
	
	@Override
    public void onDestroy() {
    	if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        GCMRegistrar.onDestroy(getApplicationContext());
        super.onDestroy();
    }

}
