package com.myproject.solvestackadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AnswersFragment extends Fragment {

    private EditText problemEditText, solutionEditText;
    private Button saveProblemButton, saveSolutionButton;
    private FirebaseFirestore db;

    private static final String COLLECTION_PATH = "user_answers";  // Firestore collection path

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answers, container, false);

        db = FirebaseFirestore.getInstance();

        // Get references to the views
        problemEditText = view.findViewById(R.id.problemEditText);
        solutionEditText = view.findViewById(R.id.solutionEditText);
        saveProblemButton = view.findViewById(R.id.saveProblemButton);
        saveSolutionButton = view.findViewById(R.id.saveSolutionButton);

        // Set onClickListener for saving the problem
        saveProblemButton.setOnClickListener(v -> saveProblemToFirestore());

        // Set onClickListener for saving the solution
        saveSolutionButton.setOnClickListener(v -> saveSolutionToFirestore());

        return view;
    }

    private void saveProblemToFirestore() {
        String problemText = problemEditText.getText().toString().trim();

        if (problemText.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a problem", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to store the problem data
        Map<String, Object> data = new HashMap<>();
        data.put("problem", problemText);  // Store the problem text
        data.put("solution", "");  // Initially, the solution is empty
        data.put("timestamp", System.currentTimeMillis());  // Store the timestamp

        // Save the problem to Firestore
        db.collection(COLLECTION_PATH).document("problem_of_the_week")
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Problem saved successfully", Toast.LENGTH_SHORT).show();
                    // Clear the problem field after saving
                    problemEditText.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error saving problem: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveSolutionToFirestore() {
        String solutionText = solutionEditText.getText().toString().trim();

        if (solutionText.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a solution", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to store the solution data
        Map<String, Object> data = new HashMap<>();
        data.put("solution", solutionText);  // Store the solution text
        data.put("timestamp", System.currentTimeMillis());  // Store the timestamp

        // Update the solution in Firestore for the existing problem
        db.collection(COLLECTION_PATH).document("problem_of_the_week")
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Solution saved successfully", Toast.LENGTH_SHORT).show();
                    // Clear the solution field after saving
                    solutionEditText.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error saving solution: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
