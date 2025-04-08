package com.myproject.solvestackadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportedPostAdapter extends RecyclerView.Adapter<ReportedPostAdapter.ViewHolder> {
    private List<ReportedPost> reportedPosts;
    private OnPostOptionsClickListener optionsClickListener;

    public ReportedPostAdapter(List<ReportedPost> reportedPosts, OnPostOptionsClickListener optionsClickListener) {
        this.reportedPosts = reportedPosts;
        this.optionsClickListener = optionsClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportedPost post = reportedPosts.get(position);
        holder.titleTextView.setText(post.getTitle());
        holder.descriptionTextView.setText(post.getDescription());
        holder.reportReasonTextView.setText(post.getReportReason());

        holder.threeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && optionsClickListener != null) {
                    optionsClickListener.onPostOptionsClicked(v, adapterPosition, post);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return reportedPosts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView reportReasonTextView;
        ImageView threeDots;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            reportReasonTextView = itemView.findViewById(R.id.reportReasonTextView);
            threeDots = itemView.findViewById(R.id.three_dots);
        }
    }

    public interface OnPostOptionsClickListener {
        void onPostOptionsClicked(View view, int position, ReportedPost post);
    }

    public void setOnPostOptionsClickListener(OnPostOptionsClickListener listener) {
        this.optionsClickListener = listener;
    }
}
