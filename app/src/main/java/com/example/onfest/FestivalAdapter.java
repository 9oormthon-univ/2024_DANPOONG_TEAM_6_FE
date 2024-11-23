package com.example.onfest;

import static android.text.format.DateUtils.formatDateRange;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FestivalAdapter extends RecyclerView.Adapter<FestivalAdapter.FestivalViewHolder> {

    private List<Festival> festivalList;

    // FestivalAdapter 생성자
    public FestivalAdapter(List<Festival> festivalList) {
        if (festivalList == null) {
            throw new IllegalArgumentException("Festival list cannot be null");
        }
        this.festivalList = festivalList;
    }

    // ViewHolder 정의
    static class FestivalViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView locationTextView;

        public FestivalViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.festival_title);
            dateTextView = itemView.findViewById(R.id.festival_date);
            locationTextView = itemView.findViewById(R.id.festival_location);
        }
    }
    private String formatDateRange(String startDate, String endDate) {
        if (startDate == null || startDate.isEmpty()) {
            startDate = "Unknown Start Date";
        }
        if (endDate == null || endDate.isEmpty()) {
            endDate = "Unknown End Date";
        }

        if (startDate.equals(endDate)) {
            // 시작 날짜와 끝 날짜가 같다면 하나만 표시
            return startDate;
        } else {
            // 날짜 범위를 "시작 ~ 끝" 형식으로 표시
            return startDate + " ~ " + endDate;
        }
    }
    public static void updateFestivals(List<Festival> newFestivalList) {
        if (newFestivalList == null) {
            throw new IllegalArgumentException("Festival list cannot be null");
        }
        //this.festivalList = newFestivalList;
        //notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FestivalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_festival_card 레이아웃을 inflate
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendaritem, parent, false);
        return new FestivalViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FestivalViewHolder holder, int position) {
        Festival festival = festivalList.get(position);

        // 데이터 설정
        holder.titleTextView.setText(festival.getTitle() != null ? festival.getTitle() : "No Title");
        holder.dateTextView.setText(formatDateRange(festival.getStartDate(), festival.getEndDate()));
        holder.locationTextView.setText(festival.getLocation() != null ? festival.getLocation() : "Unknown Location");
    }
//    @Override
//    public void onBindViewHolder(@NonNull FestivalViewHolder holder, int position) {
//        Festival festival = festivalList.get(position);
//
//        if (festival == null) {
//            Log.e("DEBUG", "Festival object is null at position " + position);
//            return;
//        }
//
//        if (holder.titleTextView == null) {
//            Log.e("DEBUG", "Title TextView is null at position " + position);
//            return;
//        }
//        if (holder.dateTextView == null) {
//            Log.e("DEBUG", "Date TextView is null at position " + position);
//            return;
//        }
//        if (holder.locationTextView == null) {
//            Log.e("DEBUG", "Location TextView is null at position " + position);
//            return;
//        }
//
//        holder.titleTextView.setText(festival.title);
//        holder.dateTextView.setText(festival.startDate + " ~ " + festival.endDate);
//        holder.locationTextView.setText(festival.location);
//    }

    @Override
    public int getItemCount() {
        // RecyclerView에서 표시할 아이템 수 반환
        return festivalList.size();
    }
}
