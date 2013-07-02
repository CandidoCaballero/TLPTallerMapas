package gdgtenerife.tlp.apis.mapas.servidor;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpUriRequest;

import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RequestResponse extends AsyncTask<String, Void, Boolean>{

	private HttpClient httpClient;
	private HttpUriRequest method;

	@Override
	// Se ejecuta en segundo plano
	protected Boolean doInBackground(String... args) {
		try {
	        HttpResponse resp = httpClient.execute(method);
	        return true;
		 } catch (Exception e) {
			Log.e("ServicioRest","Error en los preparativos!", e);
	        return false;
		 }
	}

	// Este metodo se ejecuta cuando el 'doInBackground' ha terminado, y recibe como parametro
	// lo devuelto en el 'doInBackground'
	@Override
	protected void onPostExecute(Boolean result){
        
	}
	
	public void setParams(Context context, HttpClient httpClient, HttpUriRequest method) {
         this.httpClient = httpClient;
         this.method = method;
	}

}
