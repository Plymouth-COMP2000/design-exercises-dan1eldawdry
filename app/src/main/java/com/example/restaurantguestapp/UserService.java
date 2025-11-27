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

    private static final String BASE_URL = "http://10.240.72.69/comp2000/coursework/";
    private static RequestQueue requestQueue;
    private static final Gson gson = new Gson();

    // Initialises the RequestQueue if needed
    private static void initQueue(Context context) {
        if (requestQueue == null) {
            // Using Application Context prevents memory leaks
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    // fetches all employees for local authenitcation
    public static void readAllUsers(Context context, String studentId, UserListCallback callback) {
        initQueue(context);

        String url = BASE_URL + "read_all_users/" + studentId;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        UserResponse userResponse = gson.fromJson(response, UserResponse.class);

                        if (userResponse != null && userResponse.users != null) {
                            callback.onSuccess(userResponse.users);
                        } else {
                            callback.onFailure("API response empty or missing list");
                        }
                    } catch (Exception e) {
                        Log.e("UserService", "JSON parsing error: " + e.getMessage());
                        callback.onFailure("Data format error from server");
                    }
                },
                // errors
                error -> {
                    String errorMessage = "Netowork error. check connection";
                    if (error.networkResponse != null) {
                        errorMessage = "Server Error: Status " + error.networkResponse.statusCode;

                        // Logging
                        String responseBody = new String(error.networkResponse.data);
                        Log.e("UserService", "Error Body: " + responseBody);
                    }
                    callback.onFailure(errorMessage);
                }
        );

        requestQueue.add(request);
    }
}
