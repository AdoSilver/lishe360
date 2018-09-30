package com.cardeanutrition.lishe360app.services;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationService extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String title, message, imageLink;
    private Response.Listener<String> responseListener;
    private Response.ErrorListener errorListener;

    public NotificationService(Context context,String title, String message, String imageLink){
        this.context = context;
        this.title = title;
        this.message = message;
        this.imageLink = imageLink;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        sendNotification();
        return null;
    }


    public void sendNotification() {
        Response.Listener<String> notificationResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String allresponse = jsonObject.getString("allresponses");
                    JSONObject respJsonObject = new JSONObject(allresponse);
                    String respId = respJsonObject.getString("id");
                    if(respId != null){
                        //Do something
                        Toast.makeText(context,"notification sent",Toast.LENGTH_SHORT).show();
                    }else{
                        //Do something
                        Toast.makeText(context,"notification failed",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException em) {
                    em.printStackTrace();
                }

            }
        };

        String API = "https://api.tymtalk.com/android/production/lishe360/notification-service.php";
        SendNotificationRequest vir = new SendNotificationRequest(API,title,message,"",notificationResponseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(vir);
    }


    class SendNotificationRequest extends StringRequest {

        private Map<String, String> params;

        public SendNotificationRequest(String API,String title,String message,String imageLink,Response.Listener<String> listener){
            super(Request.Method.POST,API,listener,null);
            params = new HashMap<>();
            params.put("title",title);
            params.put("message",message);
            params.put("image",imageLink);

        }

        public Map<String, String> getParams(){
            return params;
        }
    }


}
