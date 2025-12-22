package com.example.restaurantguestapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.util.List;

// interface for callbacks instead of listeners
interface UserListCallback {
    void onSuccess(List<User> userList);
    void onFailure(String errorMessage);
}

public class UserService {

    private static final String BASE_URL = "http://10.240.72.69/comp2000/coursework/"; // url for API
    private static RequestQueue requestQueue; // volley request queue
    private static final Gson gson = new Gson(); // gson instance for JSON parsing

    // starts requestqueue if needed
    private static void initialiseQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    // gets all users for local authenitcation
    public static void readAllUsers(Context context, String studentId, UserListCallback callback) {
        initialiseQueue(context);

        String url = BASE_URL + "read_all_users/" + studentId;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                UserResponse userResponse = gson.fromJson(response, UserResponse.class);

                if (userResponse != null && userResponse.users != null) {
                    callback.onSuccess(userResponse.users);
                } else {
                    callback.onFailure("API response empty");
                }
            } catch (Exception e) {
                Log.e("UserService", "JSON parsing error: " + e.getMessage());
                callback.onFailure("error from server");
            }
        },

                // errors
                error -> {
                    String errorMessage = "Netowork error check connection";
                    if (error.networkResponse != null) {
                        errorMessage = "server Error: " + error.networkResponse.statusCode;

                        // logging
                        String responseBody = new String(error.networkResponse.data);
                        Log.e("UserService", "Error Body: " + responseBody);
                    }
                    callback.onFailure(errorMessage);
                }
        );

        requestQueue.add(request); // add request to the queue
    }
}
