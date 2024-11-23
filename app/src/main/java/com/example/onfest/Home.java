package com.example.onfest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private CalendarView calendarView;
    private View calendarOverlay;
    private View contentPanel;
    private TextView monthHeader;
    private Button calendarOpenButton;
    private Button calendarOkButton;
    private Button calendarCancelButton;
    private TextView modalDateText;

    private int selectedYear;
    private int selectedMonth;
    private LinearLayout festivalListContainer;
    private List<Festival> festivalList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        festivalListContainer = findViewById(R.id.festival_list_container);

        SettingFestival();

        // View 초기화
        calendarView = findViewById(R.id.calendarView);
        calendarOverlay = findViewById(R.id.calendarOverlay); // CalendarView의 Overlay 레이아웃
        contentPanel = findViewById(R.id.contentPanel); // Content Panel 레이아웃
        monthHeader = findViewById(R.id.month_header);
        calendarOpenButton = findViewById(R.id.calendar_open_button); // 화살표 버튼
        calendarOkButton = findViewById(R.id.calendarOK);
        calendarCancelButton = findViewById(R.id.calendarcancle);
        modalDateText = findViewById(R.id.modal_date_text); // 선택된 날짜 표시 텍스트
        recyclerView = findViewById(R.id.recyclerView);

        // 초기 날짜 설정
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);

        // 초기 달력 설정
        updateCalendarDates(selectedYear, selectedMonth);

        // CalendarView Overlay 표시 버튼 클릭
        calendarOpenButton.setOnClickListener(v -> {
            if (contentPanel.getVisibility() == View.VISIBLE) {
                // contentPanel이 활성화된 경우 동작 방지
                return;
            }
            calendarOverlay.setVisibility(View.VISIBLE);
        });

        // CalendarView 날짜 선택 리스너
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedYear = year;
            selectedMonth = month; // 0부터 시작하는 월
        });

        // OK 버튼 클릭 시
        calendarOkButton.setOnClickListener(v -> {
            if (contentPanel.getVisibility() == View.VISIBLE) {
                // contentPanel이 활성화된 경우 동작 방지
                return;
            }
            updateCalendarDates(selectedYear, selectedMonth);
            calendarOverlay.setVisibility(View.GONE); // CalendarView 숨기기
        });

        // Cancel 버튼 클릭 시
        calendarCancelButton.setOnClickListener(v -> {
            calendarOverlay.setVisibility(View.GONE);
        });

        // Content Panel 닫기 버튼
        findViewById(R.id.modal_exit_btn).setOnClickListener(v -> {
            contentPanel.setVisibility(View.GONE); // Content Panel 숨기기
        });

        // GridLayout 날짜 클릭 리스너 추가
        for (int i = 1; i <= 42; i++) { // 42개의 날짜 셀
            String textViewId = "calendar" + i;
            int resId = getResources().getIdentifier(textViewId, "id", getPackageName());
            TextView dateView = findViewById(resId);

            dateView.setClickable(true);

            dateView.setOnClickListener(v -> {
                if (calendarOverlay.getVisibility() == View.VISIBLE) {
                    // calendarOverlay가 활성화된 경우 동작 방지
                    return;
                }

                contentPanel.setVisibility(View.VISIBLE);

                // 클릭한 날짜 가져오기
                String clickedDate = dateView.getText().toString();
                if (!clickedDate.isEmpty()) {
                    // 선택된 날짜 업데이트
                    String formattedDate = selectedYear + "년 " + (selectedMonth + 1) + "월 " + clickedDate + "일";
                    modalDateText.setText(formattedDate);

                    // Content Panel 표시
                    List<Festival> filteredFestivals = getFestivalsForDate(formattedDate);

                    if (filteredFestivals.isEmpty()) {
                        // 축제가 없을 경우 contentConstrainLayer 활성화
                        findViewById(R.id.content_constrain).setVisibility(View.VISIBLE);
                        findViewById(R.id.recyclerView).setVisibility(View.GONE);
                    } else {
                        // 축제가 있을 경우 RecyclerView 활성화 및 데이터 업데이트
                        findViewById(R.id.content_constrain).setVisibility(View.GONE);
                        findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
                        updateRecyclerView(filteredFestivals);
                    }
                }
            });
        }

        // 축제 카드 동적 추가
        List<Festival> currentMonthFestivals = getFestivalsForMonth(selectedYear, selectedMonth);
        if (!currentMonthFestivals.isEmpty()) {
            // 축제 리스트를 섞고 최대 5개를 선택
            Collections.shuffle(currentMonthFestivals);
            int maxCards = Math.min(5, currentMonthFestivals.size());
            for (int i = 0; i < maxCards; i++) {
                addFestivalCard(currentMonthFestivals.get(i));
            }
        }
    }

    private List<Festival> getFestivalsForMonth(int year, int month) {
        List<Festival> filteredFestivals = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (Festival festival : festivalList) {
            try {
                Date startDate = dateFormat.parse(festival.startDate);
                Date endDate = dateFormat.parse(festival.endDate);

                if (startDate != null && endDate != null) {
                    calendar.setTime(startDate);
                    int startYear = calendar.get(Calendar.YEAR);
                    int startMonth = calendar.get(Calendar.MONTH);

                    calendar.setTime(endDate);
                    int endYear = calendar.get(Calendar.YEAR);
                    int endMonth = calendar.get(Calendar.MONTH);

                    // 축제가 해당 달에 포함되는지 확인
                    if ((year > startYear || (year == startYear && month >= startMonth)) &&
                            (year < endYear || (year == endYear && month <= endMonth))) {
                        filteredFestivals.add(festival);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return filteredFestivals;
    }

    private List<Festival> getFestivalsForDate(String date) {
        List<Festival> filteredFestivals = new ArrayList<>();

        if (festivalList == null || festivalList.isEmpty()) {
            return filteredFestivals; // 빈 리스트 반환
        }

        // 날짜가 특정 축제의 시작 날짜와 끝나는 날짜 사이에 있는지 확인
        for (Festival festival : festivalList) {
            if (isDateWithinRange(date, festival.startDate, festival.endDate)) {
                filteredFestivals.add(festival);
            }
        }

        return filteredFestivals;
    }

    // 주어진 날짜가 특정 범위에 포함되는지 확인하는 메서드
    private boolean isDateWithinRange(String targetDate, String startDate, String endDate) {
        try {
            SimpleDateFormat contentFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date target = contentFormat.parse(targetDate);
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            return target != null && start != null && end != null && !target.before(start) && !target.after(end);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // RecyclerView를 갱신하는 메서드
    private void updateRecyclerView(List<Festival> filteredFestivals) {
        if (filteredFestivals == null || filteredFestivals.isEmpty()) {
            Log.e("DEBUG", "Filtered festival list is empty!");
            Toast.makeText(this, "No festivals found for the selected date.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Festival festival : filteredFestivals) {
            Log.d("DEBUG", "Festival: " + festival.title);
        }

        FestivalAdapter adapter = new FestivalAdapter(filteredFestivals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void updateCalendarDates(int year, int month) {
        Calendar calendar = Calendar.getInstance();

        // month_header 텍스트 업데이트
        String monthHeaderText = year + "년 " + (month + 1) + "월";
        monthHeader.setText(monthHeaderText);

        // 현재 달의 첫 날로 설정
        calendar.set(year, month, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 1 = 일요일, 7 = 토요일

        int daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1); // 이전 달로 이동
        int daysInPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 2); // 다음 달로 이동
        int daysInNextMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.set(year, month, 1);
        int startOffset = firstDayOfWeek - Calendar.SUNDAY;

        // TextView에 날짜 채우기
        for (int i = 1; i <= 42; i++) {
            String textViewId = "calendar" + i;
            int resId = getResources().getIdentifier(textViewId, "id", getPackageName());
            TextView textView = findViewById(resId);

            if (i <= startOffset) {
                int day = daysInPreviousMonth - (startOffset - i);
                textView.setText(String.valueOf(day));
                textView.setAlpha(0.5f); // 이전 달 날짜 희미하게 표시
                textView.setBackground(null); // 배경 초기화
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null); // 밑에 원 제거
            } else if (i > startOffset + daysInCurrentMonth) {
                int day = i - (startOffset + daysInCurrentMonth);
                textView.setText(String.valueOf(day));
                textView.setAlpha(0.5f); // 다음 달 날짜 희미하게 표시
                textView.setBackground(null); // 배경 초기화
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null); // 밑에 원 제거
            } else {
                int day = i - startOffset;
                textView.setText(String.valueOf(day));
                textView.setAlpha(1.0f); // 현재 달 날짜는 기본 상태

                String currentDate = year + "년 " + String.format(Locale.getDefault(), "%02d", (month + 1)) + "월 " + String.format(Locale.getDefault(), "%02d" + "일", day);
                boolean hasFestival = false;

                for (Festival festival : festivalList) {
                    if (isDateWithinRange(currentDate, festival.startDate, festival.endDate)) {
                        hasFestival = true;
                        break;
                    }
                }

                if (hasFestival) {
                    Log.d("DEBUG", "Festival found on: " + currentDate);

                    // 글자 밑에 원 추가
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ellipse_1));
                } else {
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null); // 원 제거
                }
            }
        }

        updateFestivalCards(year, month);
    }

    private void updateFestivalCards(int year, int month) {
        // 기존 축제 카드 제거
        festivalListContainer.removeAllViews();

        // 해당 달의 축제를 가져옴
        List<Festival> currentMonthFestivals = getFestivalsForMonth(year, month);

        if (!currentMonthFestivals.isEmpty()) {
            // 축제 리스트를 섞고 최대 5개를 선택
            Collections.shuffle(currentMonthFestivals);
            int maxCards = Math.min(5, currentMonthFestivals.size());
            for (int i = 0; i < maxCards; i++) {
                addFestivalCard(currentMonthFestivals.get(i));
            }
        }
    }

    // 축제 카드 추가 메서드
    private void addFestivalCard(Festival festival) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View festivalCard = inflater.inflate(R.layout.item_festival_card, festivalListContainer, false);

        // 뷰에 데이터 바인딩
        ImageView imageView = festivalCard.findViewById(R.id.selected_image);
        TextView titleTextView = festivalCard.findViewById(R.id.festival_title);
        TextView subtitleTextView = festivalCard.findViewById(R.id.festival_subTitle);

        imageView.setImageResource(festival.imageResId);
        titleTextView.setText(festival.title);
        subtitleTextView.setText(festival.subtitle);

        // 카드 추가
        festivalListContainer.addView(festivalCard);
    }

    private void SettingFestival() {
        festivalList.add(new Festival(
                R.drawable.ic_launcher_background, // 이미지 리소스
                "축제 1",                         // 이름
                "부제목 1",                       // 부제
                "2024-11-01",                    // 시작 날짜
                "2024-11-03",                    // 끝나는 날짜
                "서울"                            // 지역
        ));

        festivalList.add(new Festival(
                R.drawable.ic_launcher_background,
                "축제 2",
                "부제목 2",
                "2024-11-02",
                "2024-11-05",
                "부산"
        ));

        festivalList.add(new Festival(
                R.drawable.ic_launcher_background,
                "축제 3",
                "부제목 3",
                "2024-11-10",
                "2024-11-12",
                "대구"
        ));

        festivalList.add(new Festival(
                R.drawable.ic_launcher_background,
                "축제 4",
                "부제목 4",
                "2024-11-20",
                "2024-11-25",
                "울산"
        ));

        festivalList.add(new Festival(
                R.drawable.ic_launcher_background,
                "축제 5",
                "부제목 5",
                "2024-10-30",
                "2024-10-30",
                "부산"
        ));

        festivalList.add(new Festival(
                R.drawable.ic_launcher_background,
                "축제 6",
                "부제목 6",
                "2024-10-28",
                "2024-10-31",
                "광주"
        ));
    }
}