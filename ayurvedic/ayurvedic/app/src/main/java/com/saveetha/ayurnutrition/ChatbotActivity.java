package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatbotActivity extends AppCompatActivity {

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash-latest:generateContent";

    // Gemini API Key
    private String GEMINI_API_KEY = "AIzaSyC2lEQkyPBGm3KtI-5jd8VzYf-JhjTHYaY";

    private ImageView btnBack, btnClearChat, btnSend;
    private EditText etMessage;
    private RecyclerView rvMessages;
    private LinearLayout llTyping, llSuggestions;
    private Chip chipDosha, chipDiet, chipHerbs, chipLifestyle;

    private ChatMessageAdapter adapter;
    private List<ChatMessage> messages = new ArrayList<>();
    private RequestQueue requestQueue;

    // System prompt for Ayurveda context
    private static final String SYSTEM_PROMPT = "You are an expert Ayurvedic wellness advisor. Your name is Ayur AI. " +
            "You provide advice based on traditional Ayurvedic principles including: " +
            "- Dosha assessment and balancing (Vata, Pitta, Kapha) " +
            "- Dietary recommendations based on Ayurvedic principles " +
            "- Herbal remedies and their benefits " +
            "- Daily routines (Dinacharya) and seasonal routines (Ritucharya) " +
            "- Food combinations (Viruddha Ahara) to avoid " +
            "- Rasa (taste), Guna (quality), Virya (potency), and Vipaka concepts " +
            "Keep responses concise, practical, and focused on wellness. " +
            "Always recommend consulting a qualified Ayurvedic practitioner for serious health concerns. " +
            "Use emoji occasionally to make responses friendly. Format responses with clear sections when appropriate.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        requestQueue = Volley.newRequestQueue(this);

        initViews();
        setupRecyclerView();
        setupClickListeners();

        // Add welcome message
        addWelcomeMessage();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnClearChat = findViewById(R.id.btnClearChat);
        btnSend = findViewById(R.id.btnSend);
        etMessage = findViewById(R.id.etMessage);
        rvMessages = findViewById(R.id.rvMessages);
        llTyping = findViewById(R.id.llTyping);
        llSuggestions = findViewById(R.id.llSuggestions);
        chipDosha = findViewById(R.id.chipDosha);
        chipDiet = findViewById(R.id.chipDiet);
        chipHerbs = findViewById(R.id.chipHerbs);
        chipLifestyle = findViewById(R.id.chipLifestyle);
    }

    private void setupRecyclerView() {
        adapter = new ChatMessageAdapter(this, messages);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnClearChat.setOnClickListener(v -> {
            adapter.clearMessages();
            addWelcomeMessage();
            Toast.makeText(this, "Chat cleared", Toast.LENGTH_SHORT).show();
        });

        btnSend.setOnClickListener(v -> sendMessage());

        // Suggestion chips
        chipDosha.setOnClickListener(v -> sendQuickMessage("What are some tips to balance my doshas?"));
        chipDiet.setOnClickListener(v -> sendQuickMessage("What diet should I follow based on Ayurvedic principles?"));
        chipHerbs.setOnClickListener(v -> sendQuickMessage("What are some common Ayurvedic herbs and their benefits?"));
        chipLifestyle
                .setOnClickListener(v -> sendQuickMessage("What is the ideal daily routine according to Ayurveda?"));
    }

    private void addWelcomeMessage() {
        String welcome = "🙏 Namaste! I'm your Ayurvedic wellness companion.\n\n" +
                "I can help you with:\n" +
                "• Dosha assessment & balancing\n" +
                "• Diet recommendations\n" +
                "• Herbal remedies\n" +
                "• Daily routines (Dinacharya)\n" +
                "• Food combinations to avoid\n\n" +
                "How can I assist your wellness journey today?";
        adapter.addMessage(new ChatMessage(welcome, ChatMessage.TYPE_AI));
    }

    private void sendQuickMessage(String message) {
        etMessage.setText(message);
        sendMessage();
    }

    private void sendMessage() {
        String userMessage = etMessage.getText().toString().trim();
        if (userMessage.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        if (GEMINI_API_KEY.isEmpty()) {
            Toast.makeText(this, "API key not configured", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add user message to chat
        adapter.addMessage(new ChatMessage(userMessage, ChatMessage.TYPE_USER));
        etMessage.setText("");
        scrollToBottom();

        // Hide suggestions after first message
        llSuggestions.setVisibility(View.GONE);

        // Show typing indicator
        llTyping.setVisibility(View.VISIBLE);

        // Call Gemini API
        callGeminiAPI(userMessage);
    }

    private void callGeminiAPI(String userMessage) {
        try {
            String url = GEMINI_API_URL + "?key=" + GEMINI_API_KEY;

            // Build request body
            JSONObject requestBody = new JSONObject();
            JSONArray contents = new JSONArray();

            // Add system instruction
            JSONObject systemContent = new JSONObject();
            systemContent.put("role", "user");
            JSONArray systemParts = new JSONArray();
            JSONObject systemPart = new JSONObject();
            systemPart.put("text", SYSTEM_PROMPT + "\n\nUser question: " + userMessage);
            systemParts.put(systemPart);
            systemContent.put("parts", systemParts);
            contents.put(systemContent);

            requestBody.put("contents", contents);

            // Safety settings
            JSONArray safetySettings = new JSONArray();
            JSONObject safetySetting = new JSONObject();
            safetySetting.put("category", "HARM_CATEGORY_DANGEROUS_CONTENT");
            safetySetting.put("threshold", "BLOCK_ONLY_HIGH");
            safetySettings.put(safetySetting);
            requestBody.put("safetySettings", safetySettings);

            // Generation config
            JSONObject genConfig = new JSONObject();
            genConfig.put("temperature", 0.7);
            genConfig.put("maxOutputTokens", 1024);
            requestBody.put("generationConfig", genConfig);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    requestBody,
                    response -> {
                        llTyping.setVisibility(View.GONE);
                        try {
                            JSONArray candidates = response.optJSONArray("candidates");
                            if (candidates != null && candidates.length() > 0) {
                                JSONObject candidate = candidates.getJSONObject(0);
                                JSONObject content = candidate.optJSONObject("content");
                                if (content != null) {
                                    JSONArray parts = content.optJSONArray("parts");
                                    if (parts != null && parts.length() > 0) {
                                        String aiResponse = parts.getJSONObject(0).optString("text", "");
                                        adapter.addMessage(new ChatMessage(aiResponse, ChatMessage.TYPE_AI));
                                        scrollToBottom();
                                        return;
                                    }
                                }
                            }
                            adapter.addMessage(new ChatMessage("I couldn't process that request. Please try again.",
                                    ChatMessage.TYPE_AI));
                            scrollToBottom();
                        } catch (Exception e) {
                            adapter.addMessage(new ChatMessage("Error processing response: " + e.getMessage(),
                                    ChatMessage.TYPE_AI));
                            scrollToBottom();
                        }
                    },
                    error -> {
                        llTyping.setVisibility(View.GONE);
                        String errorMsg = "Sorry, I'm having trouble connecting. Please check your internet connection.";
                        if (error.networkResponse != null) {
                            errorMsg = "Error: " + error.networkResponse.statusCode;
                        }
                        adapter.addMessage(new ChatMessage(errorMsg, ChatMessage.TYPE_AI));
                        scrollToBottom();
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(request);

        } catch (Exception e) {
            llTyping.setVisibility(View.GONE);
            adapter.addMessage(new ChatMessage("Error: " + e.getMessage(), ChatMessage.TYPE_AI));
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
        rvMessages.post(() -> {
            if (adapter.getItemCount() > 0) {
                rvMessages.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    /**
     * Call this method to set the API key after retrieval
     */
    public void setApiKey(String apiKey) {
        this.GEMINI_API_KEY = apiKey;
    }
}
