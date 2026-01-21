package com.saveetha.ayurnutrition;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized Volley Helper for all API requests.
 * Singleton pattern ensures single RequestQueue instance.
 */
public class VolleyHelper {

    private static final String TAG = "VolleyHelper";
    private static VolleyHelper instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    // Base URL for all API endpoints (use HTTP for local XAMPP development)
    public static final String BASE_URL = "http://172.20.10.3/ayurvedic_API/";

    // API Endpoints
    public static final String LOGIN_URL = BASE_URL + "login.php";
    public static final String SIGNUP_URL = BASE_URL + "signup.php";
    public static final String ADD_CLINIC_URL = BASE_URL + "add_clinic.php";
    public static final String ADD_PATIENT_URL = BASE_URL + "add_patient.php";
    public static final String GET_PATIENTS_URL = BASE_URL + "get_patients.php";
    public static final String GET_RECIPES_URL = BASE_URL + "get_recipes.php";
    public static final String SAVE_PRAKRITI_URL = BASE_URL + "save_prakriti.php";
    public static final String SAVE_VIKRITI_URL = BASE_URL + "save_vikriti.php";
    public static final String SAVE_AGNI_URL = BASE_URL + "save_agni.php";
    public static final String SAVE_DIGESTIVE_URL = BASE_URL + "save_digestive.php";
    public static final String SAVE_RASA_URL = BASE_URL + "save_rasa.php";
    public static final String SAVE_FOOD_URL = BASE_URL + "save_food.php";

    // Phase 1 endpoints
    public static final String SAVE_MEDICAL_HISTORY_URL = BASE_URL + "save_medical_history.php";
    public static final String SAVE_DIETARY_HABITS_URL = BASE_URL + "save_dietary_habits.php";
    public static final String SAVE_LIFESTYLE_URL = BASE_URL + "save_lifestyle.php";
    public static final String GET_FOODS_URL = BASE_URL + "get_foods.php";
    public static final String GET_FOOD_DETAIL_URL = BASE_URL + "get_food_detail.php";
    public static final String SAVE_DIET_CHART_URL = BASE_URL + "save_diet_chart.php";
    public static final String GET_DIET_CHARTS_URL = BASE_URL + "get_diet_charts.php";

    // Phase 2 endpoints
    public static final String GET_CLINICAL_ALERTS_URL = BASE_URL + "get_clinical_alerts.php";
    public static final String GET_TASKS_URL = BASE_URL + "get_tasks.php";
    public static final String UPDATE_TASK_URL = BASE_URL + "update_task.php";
    public static final String ADD_CUSTOM_FOOD_URL = BASE_URL + "add_custom_food.php";
    public static final String GET_NUTRIENT_BALANCE_URL = BASE_URL + "get_nutrient_balance.php";
    public static final String GET_BALANCE_SCORE_URL = BASE_URL + "get_balance_score.php";
    public static final String GENERATE_REPORT_URL = BASE_URL + "generate_report.php";
    public static final String ADD_RECIPE_TO_DIET_URL = BASE_URL + "add_recipe_to_diet.php";

    // Phase 3 endpoints
    public static final String GET_ANALYTICS_URL = BASE_URL + "get_analytics.php";
    public static final String GET_SEASONAL_FOODS_URL = BASE_URL + "get_seasonal_foods.php";
    public static final String EDIT_FOOD_URL = BASE_URL + "edit_food.php";
    public static final String GET_DOSHA_IMPACT_URL = BASE_URL + "get_dosha_impact.php";
    public static final String GET_SUGGESTIONS_URL = BASE_URL + "get_suggestions.php";
    public static final String SAVE_PERMISSIONS_URL = BASE_URL + "save_permissions.php";

    // Phase 4 endpoints (Missing Features Implementation)
    public static final String SAVE_PREFERENCES_URL = BASE_URL + "save_preferences.php";
    public static final String GET_PREFERENCES_URL = BASE_URL + "get_preferences.php";
    public static final String GET_DOSHA_SUGGESTIONS_URL = BASE_URL + "get_dosha_suggestions.php";
    public static final String GET_FOOD_CONFLICTS_URL = BASE_URL + "get_food_conflicts.php";
    public static final String GET_LIFESTYLE_URL = BASE_URL + "get_lifestyle.php";
    public static final String GET_DIETARY_HABITS_URL = BASE_URL + "get_dietary_habits.php";
    public static final String GET_MEDICAL_HISTORY_URL = BASE_URL + "get_medical_history.php";

    // Phase 5 endpoints (Medical Records)
    public static final String SAVE_MEDICAL_RECORD_URL = BASE_URL + "save_medical_record.php";
    public static final String GET_MEDICAL_RECORDS_URL = BASE_URL + "get_medical_records.php";

    // Phase 6 endpoints (Assessment Data)
    public static final String GET_PRAKRITI_URL = BASE_URL + "get_prakriti.php";
    public static final String GET_VIKRITI_URL = BASE_URL + "get_vikriti.php";
    public static final String GET_AGNI_URL = BASE_URL + "get_agni.php";

    // Phase 7 endpoints (Appointments)
    public static final String SAVE_APPOINTMENT_URL = BASE_URL + "save_appointment.php";
    public static final String GET_APPOINTMENTS_URL = BASE_URL + "get_appointments.php";

    // Phase 8 endpoints (Report Email)
    public static final String SEND_REPORT_EMAIL_URL = BASE_URL + "send_report_email.php";

    private VolleyHelper(Context context) {
        ctx = context.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyHelper getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyHelper(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        // Set retry policy with longer timeout for slow connections
        req.setRetryPolicy(new DefaultRetryPolicy(
                30000, // 30 second timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    /**
     * Callback interface for API responses
     */
    public interface ApiCallback {
        void onSuccess(JSONObject response);

        void onError(String error);
    }

    /**
     * POST request with form data
     */
    public void postRequest(String url, Map<String, String> params, ApiCallback callback) {
        Log.d(TAG, "POST Request: " + url);
        Log.d(TAG, "POST Params: " + params.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "POST Response from " + url + ": "
                            + response.substring(0, Math.min(response.length(), 200)));
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        callback.onSuccess(jsonResponse);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parse Error for " + url + ": " + e.getMessage());
                        Log.e(TAG, "Raw response: " + response);
                        callback.onError("JSON parsing error: " + e.getMessage());
                    }
                },
                error -> {
                    String errorMessage = getErrorMessage(error);
                    Log.e(TAG, "POST Error for " + url + ": " + errorMessage);
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        Log.e(TAG, "Error Response: " + new String(error.networkResponse.data));
                    }
                    callback.onError(errorMessage);
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        addToRequestQueue(stringRequest);
    }

    /**
     * GET request
     */
    public void getRequest(String url, ApiCallback callback) {
        Log.d(TAG, "GET Request: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d(TAG, "GET Response from " + url + ": "
                            + response.substring(0, Math.min(response.length(), 200)));
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        callback.onSuccess(jsonResponse);
                    } catch (JSONException e) {
                        // Try parsing as array wrapped in object
                        try {
                            JSONObject wrapper = new JSONObject();
                            wrapper.put("data", new org.json.JSONArray(response));
                            wrapper.put("success", true);
                            callback.onSuccess(wrapper);
                        } catch (JSONException e2) {
                            Log.e(TAG, "JSON Parse Error for " + url + ": " + e.getMessage());
                            Log.e(TAG, "Raw response: " + response);
                            callback.onError("JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                error -> {
                    String errorMessage = getErrorMessage(error);
                    Log.e(TAG, "GET Error for " + url + ": " + errorMessage);
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        Log.e(TAG, "Error Response: " + new String(error.networkResponse.data));
                    }
                    callback.onError(errorMessage);
                });

        addToRequestQueue(stringRequest);
    }

    /**
     * Get user-friendly error message
     */
    private String getErrorMessage(VolleyError error) {
        Log.e(TAG, "Volley Error: " + error.toString());
        if (error.getCause() != null) {
            Log.e(TAG, "Error Cause: " + error.getCause().toString());
        }

        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            Log.e(TAG, "HTTP Status Code: " + statusCode);
            return "Server error: " + statusCode;
        } else if (error.getMessage() != null) {
            if (error.getMessage().contains("SSL")) {
                Log.e(TAG, "SSL Error detected");
                return "SSL connection error. Check server certificate.";
            }
            return error.getMessage();
        }
        return "Network error. Please check your connection.";
    }

    // ============== CONVENIENCE METHODS ==============

    /**
     * Login user
     */
    public void login(String email, String password, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        postRequest(LOGIN_URL, params, callback);
    }

    /**
     * Signup user
     */
    public void signup(String name, String email, String phone, String password, String clinic, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("phone", phone);
        params.put("password", password);
        params.put("clinic", clinic);
        postRequest(SIGNUP_URL, params, callback);
    }

    /**
     * Add clinic
     */
    public void addClinic(int userId, String name, String type, String practitioners, String specialization,
            ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(userId));
        params.put("clinic_name", name);
        params.put("clinic_type", type);
        params.put("practitioners", practitioners);
        params.put("specialization", specialization);
        postRequest(ADD_CLINIC_URL, params, callback);
    }

    /**
     * Add patient
     */
    public void addPatient(String firstName, String lastName, String dob, String gender,
            String email, String phone, String address, String regDate,
            String followUp1, String followUp2, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("dob", dob);
        params.put("gender", gender);
        params.put("email", email);
        params.put("phone", phone);
        params.put("address", address);
        params.put("reg_date", regDate);
        params.put("followup1", followUp1);
        params.put("followup2", followUp2);
        postRequest(ADD_PATIENT_URL, params, callback);
    }

    /**
     * Get all patients
     */
    public void getPatients(ApiCallback callback) {
        getRequest(GET_PATIENTS_URL, callback);
    }

    /**
     * Get all recipes
     */
    public void getRecipes(ApiCallback callback) {
        getRequest(GET_RECIPES_URL, callback);
    }

    /**
     * Save Prakriti assessment
     */
    public void savePrakriti(int patientId, int vata, int pitta, int kapha, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(patientId));
        params.put("vata", String.valueOf(vata));
        params.put("pitta", String.valueOf(pitta));
        params.put("kapha", String.valueOf(kapha));
        postRequest(SAVE_PRAKRITI_URL, params, callback);
    }

    /**
     * Save Vikriti assessment
     */
    public void saveVikriti(int patientId, int vata, int pitta, int kapha, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(patientId));
        params.put("vata", String.valueOf(vata));
        params.put("pitta", String.valueOf(pitta));
        params.put("kapha", String.valueOf(kapha));
        postRequest(SAVE_VIKRITI_URL, params, callback);
    }

    /**
     * Save Agni assessment
     */
    public void saveAgni(int patientId, String agniType, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(patientId));
        params.put("agni_type", agniType);
        postRequest(SAVE_AGNI_URL, params, callback);
    }

    /**
     * Save Digestive strength
     */
    public void saveDigestive(int patientId, String strengthLevel, String symptoms, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(patientId));
        params.put("strength_level", strengthLevel);
        params.put("symptoms", symptoms);
        postRequest(SAVE_DIGESTIVE_URL, params, callback);
    }

    /**
     * Save Rasa preferences
     */
    public void saveRasa(int patientId, int sweet, int sour, int salty,
            int pungent, int bitter, int astringent, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(patientId));
        params.put("sweet", String.valueOf(sweet));
        params.put("sour", String.valueOf(sour));
        params.put("salty", String.valueOf(salty));
        params.put("pungent", String.valueOf(pungent));
        params.put("bitter", String.valueOf(bitter));
        params.put("astringent", String.valueOf(astringent));
        postRequest(SAVE_RASA_URL, params, callback);
    }

    /**
     * Save Food contraindications
     */
    public void saveFood(int patientId, int milk, int honey, int fruits,
            int curd, int radish, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(patientId));
        params.put("milk", String.valueOf(milk));
        params.put("honey", String.valueOf(honey));
        params.put("fruits", String.valueOf(fruits));
        params.put("curd", String.valueOf(curd));
        params.put("radish", String.valueOf(radish));
        postRequest(SAVE_FOOD_URL, params, callback);
    }
}
