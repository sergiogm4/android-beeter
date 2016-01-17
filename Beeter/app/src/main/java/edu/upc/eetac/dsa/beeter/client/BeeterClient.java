package edu.upc.eetac.dsa.beeter.client;

import android.util.Log;

import com.google.gson.Gson;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.beeter.client.entity.AuthToken;
import edu.upc.eetac.dsa.beeter.client.entity.Link;
import edu.upc.eetac.dsa.beeter.client.entity.Root;

/**
 * Created by Marta_ on 27/12/2015.
 */
public class BeeterClient {
    private final static String BASE_URI = "http://192.168.1.129:8080/beeter";
    private static BeeterClient instance;
    private Root root;
    private ClientConfig clientConfig = null;
    private Client client = null;
    private AuthToken authToken = null;
    private final static String TAG = BeeterClient.class.toString();
    private Response response;

    private BeeterClient() {
        clientConfig = new ClientConfig();
        client = ClientBuilder.newClient(clientConfig);
        loadRoot();
    }

    public static BeeterClient getInstance() {
        if (instance == null)
            instance = new BeeterClient();
        return instance;
    }

    private void loadRoot() {
        WebTarget target = client.target(BASE_URI);
        Response response = target.request().get();

        String json = response.readEntity(String.class);
        root = (new Gson()).fromJson(json, Root.class);
    }

    public Root getRoot() {
        return root;
    }
    public String getSting(String uri) throws BeeterClientException {
        return null;
        //TODO:implementarlo
    }
    public final static Link getLink(List<Link> links, String rel){
        for(Link link : links){
            if(link.getRels().contains(rel)) {
                return link;
            }
        }
        return null;
    }
    public boolean login(String userid, String password) {
        Log.d(TAG, "entra en login");
        String loginUri = getLink(root.getLinks(), "login").getUri().toString();
        WebTarget target = client.target(loginUri);
        Log.d(TAG, "entiende el link");
        Form form = new Form();
        //en vez de spongebob y 1234 hay que poner los parameros.
        form.param("loginid", "spongebob");
        form.param("password", "1234");
        Log.d(TAG, "username y password ok");
        String json = "";
        json = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
        Log.d(TAG, "no se que del json");
        authToken = (new Gson()).fromJson(json, AuthToken.class);
        Log.d(TAG, "todo ok");
        return true;
    }

    public String getStings(String uri) throws BeeterClientException {
        Log.d(TAG, "entro en el Beeter Client");
        if(uri==null){
            uri = getLink(authToken.getLinks(), "current-stings").getUri().toString();
            Log.d(TAG, "Uri stings: " +uri);
        }
        WebTarget target = client.target(uri);
        Response response = target.request().get();
        Log.d(TAG, "response"+ response);
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        }
        else {
            throw new BeeterClientException(response.readEntity(String.class));
        }
    }
}
