package gdgtenerife.tlp.apis.mapas;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity { // TODO: Explicar este cambio

	// TODO: Explicar esto
	private GoogleMap mapa = null;
	private int vista = 0;
	private Marker marcador=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// TODO: Explicar Esto
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		switch(item.getItemId())
		{
			case R.id.menu_vista:
				alternarVista();
				break;
			case R.id.menu_mover:
				//Centramos el mapa en la TLP
				CameraUpdate camUpd1 = CameraUpdateFactory.newLatLng(new LatLng(28.454532710377187, -16.257910238360633));
				mapa.moveCamera(camUpd1);
				break;
			case R.id.menu_animar:
				//Centramos el mapa en la TLP y con nivel de zoom 5
				CameraUpdate camUpd2 = CameraUpdateFactory.newLatLngZoom(new LatLng(28.454532710377187, -16.257910238360633), 5F);
				mapa.animateCamera(camUpd2);
				break;
			case R.id.menu_3d:
				LatLng recintoFerial = new LatLng(28.454532710377187, -16.257910238360633);
				CameraPosition camPos = new CameraPosition.Builder()
					    .target(recintoFerial)   //Centramos el mapa en la TLP
					    .zoom(17)         //Establecemos el zoom en 19
					    .bearing(45)      //Establecemos la orientación con el noreste arriba
					    .tilt(70)         //Bajamos el punto de vista de la cámara 70 grados
					    .build();

				CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);

				mapa.animateCamera(camUpd3);
				break;
			case R.id.menu_posicion:
				CameraPosition camPos2 = mapa.getCameraPosition();
				LatLng pos = camPos2.target;
				Toast.makeText(MainActivity.this, 
						"Lat: " + pos.latitude + " - Lng: " + pos.longitude, 
						Toast.LENGTH_LONG).show();
				break;
			case R.id.menu_marcador:
				if (marcador==null)
					marcador = mapa.addMarker(new MarkerOptions()
		        		.position(new LatLng(28.454532710377187, -16.257910238360633))
		        		.title("Taller GDG Tenerife en la TLP"));
				else{
					marcador.remove();
					marcador=null;
				}
				
				break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void alternarVista() {
		vista = (vista + 1) % 4;

		switch(vista)
		{
			case 0:
				mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			case 1:
				mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			case 2:
				mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			case 3:
				mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				break;
		}
	}

}
