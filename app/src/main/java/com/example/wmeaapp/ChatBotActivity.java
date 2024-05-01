package com.example.wmeaapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.util.concurrent.Futures;

import java.util.ArrayList;
import java.util.List;

import Adapters.MessageAdapter;
import Classes.Message;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

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


}
