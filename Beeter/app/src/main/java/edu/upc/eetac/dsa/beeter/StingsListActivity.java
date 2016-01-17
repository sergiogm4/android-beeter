package edu.upc.eetac.dsa.beeter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.content.Context;
import android.widget.Adapter;

import com.google.gson.Gson;

import edu.upc.eetac.dsa.beeter.client.BeeterClient;
import edu.upc.eetac.dsa.beeter.client.BeeterClientException;
import edu.upc.eetac.dsa.beeter.client.entity.Sting;
import edu.upc.eetac.dsa.beeter.client.entity.StingCollection;
import edu.upc.eetac.dsa.beeter.client.entity.StingCollectionAdapter;

public class StingsListActivity extends AppCompatActivity{
    private final static String TAG = StingsListActivity.class.toString();
    private GetStingsTask mGetStingsTask = null;
    private StingCollection stings = new StingCollection();
    private StingCollectionAdapter  adapter;
    //adapter = new StingCollectionAdapter(this, strings);
    //ListView list = (ListView)findViewById(R.id.list);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stings_list);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        adapter = new StingCollectionAdapter(this, stings);
        //list.setAdapter(adapter);
        ListView list = (ListView)findViewById(R.id.list);

        list.setAdapter(adapter);
        // set list OnItemClick listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StingsListActivity.this, StingDetailActivity.class);
                String uri = BeeterClient.getLink(stings.getStings().get(position).getLinks(), "self").getUri().toString();
                intent.putExtra("uri", uri);
                startActivity(intent);
            }
        });

        // Execute AsyncTask
        mGetStingsTask = new GetStingsTask(null);
        mGetStingsTask.execute((Void) null);
    }


    class GetStingsTask extends AsyncTask<Void, Void, String> {
        private String uri;

        public GetStingsTask(String uri) {
            this.uri = uri;

        }

        @Override
        protected String doInBackground(Void... params) {
            String jsonStingCollection = null;
            try {
                jsonStingCollection = BeeterClient.getInstance().getStings(uri);
            } catch (BeeterClientException e) {
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }
            Log.d(TAG, "no se que del json:" + jsonStingCollection);
            return jsonStingCollection;
        }

        @Override
        protected void onPostExecute(String jsonStingCollection) {
            Log.d(TAG, "onPostExecute");
            Log.d(TAG, jsonStingCollection);
            //ListView list = (ListView)findViewById(R.id.list);

            //list.setAdapter(adapter);
            StingCollection stingCollection = (new Gson()).fromJson(jsonStingCollection, StingCollection.class);
            Log.d(TAG, jsonStingCollection);
            int count = 1;
            //list.setAdapter(adapter);
            for(Sting sting : stingCollection.getStings()){
                int pos = 0;
                Log.d(TAG, count +"entro en el for del sting: "+sting.getSubject());
                stings.getStings().add(stings.getStings().size(), sting);
            }

            Log.d(TAG, "voy a haccer el set adapter 2");


            Log.d(TAG, "voy a haccer el notify noseke");
            adapter.notifyDataSetChanged();
            //TODO: mostrar contenido del sting (Nueva Activity)
            //
            // set list OnItemClick listener
            /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, BeeterClient.getLink(stings.getStings().get(position).getLinks(), "self").getUri().toString());
                }
            });*/
        }
    }
}