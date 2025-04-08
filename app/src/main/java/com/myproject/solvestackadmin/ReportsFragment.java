package com.myproject.solvestackadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class ReportsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReportedPostAdapter adapter;
    private List<ReportedPost> reportedPosts;
    private FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        reportedPosts = new ArrayList<>();
        fetchReportedPosts();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewreport);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReportedPostAdapter(reportedPosts, new ReportedPostAdapter.OnPostOptionsClickListener() {
            @Override
            public void onPostOptionsClicked(View view, int position, ReportedPost post) {
                showPostOptionsMenu(view, position);
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void fetchReportedPosts() {
        reportedPosts.clear();

        db.collection("reports")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String description = documentSnapshot.getString("description");
                            String postId = documentSnapshot.getString("postId");
                            String reportReason = documentSnapshot.getString("reportReason");
                            String reporterId = documentSnapshot.getString("reporterId");
                            String title = documentSnapshot.getString("title");

                            ReportedPost reportedPost = new ReportedPost(description, postId, reportReason, reporterId, title);
                            reportedPosts.add(reportedPost);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void showPostOptionsMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.inflate(R.menu.post_options_menu_one);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_delete) {
                    deleteReportedPost(position);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }


    private void deleteReportedPost(int position) {
        ReportedPost post = reportedPosts.get(position);
        String postId = post.getPostId();

        if (postId != null) {
            WriteBatch batch = db.batch();

            batch.delete(db.collection("posts").document(postId));

            db.collection("reports")
                    .whereEqualTo("postId", postId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                batch.delete(documentSnapshot.getReference());
                            }

                            batch.commit()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            reportedPosts.remove(position);
                                            adapter.notifyItemRemoved(position);
                                            Toast.makeText(requireContext(), "Post and reports deleted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(requireContext(), "Failed to delete post and reports: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "Post ID is null", Toast.LENGTH_SHORT).show();
        }
    }
}