package com.myproject.solvestackadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private OnPostOptionsClickListener optionsClickListener;

    public PostAdapter(List<Post> postList, OnPostOptionsClickListener optionsClickListener) {
        this.postList = postList;
        this.optionsClickListener = optionsClickListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setPosts(List<Post> posts) {
        postList.clear();
        postList.addAll(posts);
        notifyDataSetChanged();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView categoryTextView;
        private TextView reply_text;
        private ImageView threeDots;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            reply_text = itemView.findViewById(R.id.reply_text);
            threeDots = itemView.findViewById(R.id.three_dots);

            threeDots.setOnClickListener(v -> {
                if (optionsClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    optionsClickListener.onPostOptionsClicked(v, getAdapterPosition(), postList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            descriptionTextView.setText(post.getDescription());
            threeDots.setVisibility(View.VISIBLE);
            setCategoryColor(post.getCategory());
            loadCommentCount(post.getPostId());
        }

        private void setCategoryColor(String category) {
            categoryTextView.setText(category);
            int color;
            switch (category) {
                case "Number Theory":
                    color = R.color.green;
                    break;
                case "Geometry":
                    color = R.color.teal_200;
                    break;
                case "Algebra":
                    color = R.color.algred;
                    break;
                case "Combinatorics":
                    color = R.color.orange;
                    break;
                default:
                    color = R.color.blue;
                    break;
            }
            categoryTextView.setBackgroundResource(color);
        }


        private void loadCommentCount(String postId) {
            FirebaseFirestore.getInstance()
                    .collection("comments")
                    .document(postId)
                    .collection("post_comments")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        int count = queryDocumentSnapshots.size();
                        reply_text.setText("Reply (" + count + ")");
                    })
                    .addOnFailureListener(e -> {
                        reply_text.setText("Reply");
                    });
        }
    }

    public interface OnPostOptionsClickListener {
        void onPostOptionsClicked(View view, int position, Post post);
    }
}