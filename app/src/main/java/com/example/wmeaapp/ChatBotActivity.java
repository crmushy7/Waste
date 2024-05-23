package com.example.wmeaapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crmushi.wmeaapp.R;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;

import java.util.ArrayList;
import java.util.List;

import Adapters.MessageAdapter;
import Classes.Message;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class ChatBotActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ConstraintLayout lottieAnimation;
    private EditText messageEditText;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    public static final MediaType JSON = MediaType.parse("application/json");
    private final OkHttpClient client = new OkHttpClient();
    private ChatFutures chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        recyclerView = findViewById(R.id.boat_recyclerView);
        messageEditText = findViewById(R.id.message_edt_text);
        ImageButton sendButton = findViewById(R.id.send_btn);
        lottieAnimation = findViewById(R.id.lottie_animation);

        chatModel = getChatModel();

        messageList = new ArrayList<>();

        sendButton.setOnClickListener(v -> sendMessage());

        messageAdapter = new MessageAdapter(messageList, ChatBotActivity.this);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(ChatBotActivity.this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
    }

    private ChatFutures getChatModel() {
        GeminiPro model = new GeminiPro();
        GenerativeModelFutures modelFutures = model.getModel();

        return modelFutures.startChat();
    }

    private void sendMessage() {
        String question = messageEditText.getText().toString().trim();

        if (question.isEmpty()) {
            messageEditText.setError("Question required");
            messageEditText.requestFocus();
            return;
        }

        addToChat(question, Message.SEND_BY_ME);
        messageEditText.setText("");

        if (messageList.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            lottieAnimation.setVisibility(View.GONE);
        }


        callApi(question);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addToChat(String message, String sendBy) {
        runOnUiThread(() -> {
            messageList.add(new Message(message, sendBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void addResponse(String response) {
        runOnUiThread(() -> {
            messageList.remove(messageList.size() - 1);
            messageList.add(new Message(response, Message.SEND_BY_BOT));
            messageAdapter.notifyDataSetChanged();
        });
    }

    public void callApi(String question) {
        addToChat("Typing...", Message.SEND_BY_BOT);
        GeminiPro.getResponse(chatModel, question, new ResponseCallback() {
            @Override
            public void onResponse(String response) {
                addResponse(response);
            }

            @Override
            public void onError(Throwable throwable) {
                addResponse(throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }


/*
    private void callAPI(String question) {
        addToChat("Typing...", Message.SEND_BY_BOT);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "gpt-3.5-turbo-instruct");
            jsonObject.put("prompt", question);
            jsonObject.put("max_tokens", 7);
            jsonObject.put("temperature", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonResponseObject;
                    try {
                        assert response.body() != null;
                        jsonResponseObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonResponseObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    assert response.body() != null;
                    addResponse("failed to load response due to " + response.body().string());
                }
            }
        });
    }

 */
}
