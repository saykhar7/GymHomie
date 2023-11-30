package com.gymhomie;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class publicExercises_Activity extends Activity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth auth = FirebaseAuth.getInstance();

    String collectionPath = "exercises"; //path for the water intakes on firestore
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
///Instantiating Buttons
        setContentView(R.layout.publicworkouts);
        LinearLayout ll = findViewById(R.id.public_exercise_linearlayout);
        db.collection(collectionPath).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String name = document.getString("exercise_name");
                            String url = document.getString("exercise_url");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Button tempButton = new Button(getApplicationContext());
                                    tempButton.setText(name);
                                    tempButton.setTag(url);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    param.gravity = Gravity.CENTER;
                                    tempButton.setLayoutParams(param);
                                    tempButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String storedData = (String) tempButton.getTag();
                                            copyToClipboard(storedData);
                                        }
                                    });
                                    ll.addView(tempButton);

                                }
                            });
                        }
                    }
                });
    }
    private void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clipData = ClipData.newPlainText("text", text);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    }

}
