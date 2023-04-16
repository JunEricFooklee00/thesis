package com.test.thesis_application.fragments;

import static com.test.thesis_application.CalendarUtils.daysInWeekArray;
import static com.test.thesis_application.CalendarUtils.monthYearFromDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.thesis_application.CalendarAdapter;
import com.test.thesis_application.CalendarUtils;
import com.test.thesis_application.Event;
import com.test.thesis_application.EventAdapter;
import com.test.thesis_application.R;

import java.time.LocalDate;
import java.util.ArrayList;


public class weekView extends Fragment implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private Button back,forward;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_week_view, container, false);
        back = view.findViewById(R.id.back);
        forward = view.findViewById(R.id.forward);

        CalendarUtils.selectedDate = LocalDate.now();
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        eventListView = view.findViewById(R.id.eventListView);
        setWeekView();


        back.setOnClickListener(v -> previousWeekAction(v));
        forward.setOnClickListener(v -> nextWeekAction(v));
        return view;
    }


    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days,this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }


    public void previousWeekAction(View view)
    {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);

        setWeekView();
    }

    public void nextWeekAction(View view)
    {

        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    private void setEventAdpater()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(requireContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

//    public void newEventAction(View view)
//    {
//        startActivity(new Intent(this, EventEditActivity.class));
//    }
}