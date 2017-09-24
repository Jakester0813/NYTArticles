package com.jakester.nytarticlesapp.fragments;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakester.nytarticlesapp.R;
import com.jakester.nytarticlesapp.managers.FiltersManager;
import com.jakester.nytarticlesapp.util.NYTConstants;

import java.util.Calendar;

/**
 * Created by Jake on 9/20/2017.
 */

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener{
    private TextView mDateText,mBeginDateText,mSortByText,mFilterByText, mSaveText;
    private Spinner mSortSpinner;
    private CheckBox mArtCheck;
    private CheckBox mFashionCheck;
    private CheckBox mSportsCheck;
    private LinearLayout mSaveLayout;
    StringBuilder dateFilterBuilder;

    public FilterDialogFragment(){

    }

    public static FilterDialogFragment newInstance(String title){
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onClick(View view) {
        if(!mDateText.getText().toString().equals(NYTConstants.SET_DATE)){
            FiltersManager.getInstance(getActivity()).setDate(mDateText.getText().toString());
            FiltersManager.getInstance(getActivity()).setDateFilter(dateFilterBuilder.toString());
        }
        FiltersManager.getInstance(getActivity()).setSortFilter(mSortSpinner.getSelectedItem().toString().toLowerCase());
        FiltersManager.getInstance(getActivity()).setSortPosition(mSortSpinner.getSelectedItemPosition());
        FiltersManager.getInstance(getActivity()).setArtChecked(mArtCheck.isChecked());
        FiltersManager.getInstance(getActivity()).setFashionCheck(mFashionCheck.isChecked());
        FiltersManager.getInstance(getActivity()).setSportCheck(mSportsCheck.isChecked());
        FilterDialogListener listener = (FilterDialogListener) getActivity();
        listener.onFinishFilterDialog();
        dismiss();
    }

    public interface FilterDialogListener {
        void onFinishFilterDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout_search_filters, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBeginDateText = (TextView) view.findViewById(R.id.tv_begin_date);
        mDateText = (TextView) view.findViewById(R.id.tv_begin_date_text);
        mDateText.setText(FiltersManager.getInstance(getActivity()).getDate());
        mDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                final StringBuilder dateBuilder = new StringBuilder();
                dateFilterBuilder = new StringBuilder();
                final int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                final int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        (view1, year1, monthOfYear, dayOfMonth) -> {
                            dateBuilder.append(monthOfYear+1).append("/").append(dayOfMonth).append("/").append(year1);
                            mDateText.setText(dateBuilder.toString());
                            dateFilterBuilder.append(year1);
                            if(monthOfYear+1 < 10){
                                dateFilterBuilder.append("0");
                            }
                            dateFilterBuilder.append(monthOfYear+1);
                            if(dayOfMonth < 10){
                                dateFilterBuilder.append("0");
                            }
                            dateFilterBuilder.append(dayOfMonth);
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        mSortByText = (TextView) view.findViewById(R.id.tv_sort_order);
        mSortSpinner = (Spinner) view.findViewById(R.id.sp_sort);
        mSortSpinner.setAdapter(ArrayAdapter.createFromResource(getActivity(), R.array.sort_array, R.layout.spinner_item));
        mSortSpinner.setSelection(FiltersManager.getInstance(getActivity()).getSortPosition());

        mFilterByText = (TextView) view.findViewById(R.id.tv_news_values);
        mArtCheck = (CheckBox) view.findViewById(R.id.checkbox_art);
        mArtCheck.setChecked(FiltersManager.getInstance(getActivity()).getArt());
        mFashionCheck = (CheckBox) view.findViewById(R.id.checkbox_fashion);
        mFashionCheck.setChecked(FiltersManager.getInstance(getActivity()).getFashion());
        mSportsCheck = (CheckBox) view.findViewById(R.id.checkbox_sport);
        mSportsCheck.setChecked(FiltersManager.getInstance(getActivity()).getSports());

        mSaveText = (TextView) view.findViewById(R.id.tv_save);
        mSaveLayout = (LinearLayout) view.findViewById(R.id.ll_save_filter);
        mSaveLayout.setOnClickListener(this);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
