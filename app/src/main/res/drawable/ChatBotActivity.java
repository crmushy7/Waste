package com.example.wmeaapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import Adapters.MessageAdapter;
import Classes.Message;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBotActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText messageEditText;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    public static final MediaType JSON = MediaType.parse("application/json");
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        recyclerView = findViewById(R.id.boat_recyclerView);
        messageEditText = findViewById(R.id.message_edt_text);
        ImageButton sendButton = findViewById(R.id.send_btn);

        messageList = new ArrayList<>();

        sendButton.setOnClickListener(v -> sendMessage());

        messageAdapter = new MessageAdapter(messageList, ChatBotActivity.this);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(ChatBotActivity.this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
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

    private void callApi(String question) {


        addToChat("Typing...", Message.SEND_BY_BOT);
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyDJU60DG7gfKXU46RG-nTaJmewXyqIOIYk");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText(question)
                .build();


        ListenableFuture < GenerateContentResponse > response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();

                addResponse(resultText);
                System.out.println(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                addResponse(t.toString());
                t.printStackTrace();
            }
        }, this.getMainExecutor());


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
