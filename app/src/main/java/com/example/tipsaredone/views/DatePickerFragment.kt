package com.example.tipsaredone.views

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnTouchListener
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tipsaredone.R
import com.example.tipsaredone.activities.ReportActivity
import com.example.tipsaredone.databinding.FragmentDatePickerBinding
import com.example.tipsaredone.viewmodels.DatePickerViewModel
import java.time.LocalDate
import kotlin.math.abs


class DatePickerFragment : Fragment() {

    private lateinit var datePickerViewModel: DatePickerViewModel
    private lateinit var reportActivity: ReportActivity
    private var _binding: FragmentDatePickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDatePickerBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val datePickerVM: DatePickerViewModel by activityViewModels()
        datePickerViewModel = datePickerVM
        reportActivity = (context as ReportActivity)


        binding.startDatePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            datePickerViewModel.setStartDate(year, monthOfYear, dayOfMonth)
        }
        binding.endDatePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            datePickerViewModel.setEndDate(year, monthOfYear, dayOfMonth)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkDateValidity(): Boolean {
        return if (datePickerViewModel.startDate.value != null && datePickerViewModel.endDate.value != null) {
            if (datePickerViewModel.startDate.value!!.isBefore(datePickerViewModel.endDate.value!!)) {
                true
            } else {
                val toast = resources.getString(R.string.invalid_dates2)
              // (context as MainActivity).makeToastMessage(toast)
                false
            }
        } else {
            val toast = resources.getString(R.string.invalid_dates1)
           //  (context as MainActivity).makeToastMessage(toast)
            false
        }
    }

    private fun convertDatesToString() {
        val startDate = datePickerViewModel.startDate.value!!
        val startMonthString =
            if (startDate.monthValue < 10) {
                "0${startDate.monthValue}"
            } else {
                startDate.monthValue
            }
        val startDayString =
            if (startDate.dayOfMonth < 10) {
                "0${startDate.dayOfMonth}"
            } else {
                startDate.dayOfMonth
            }
        startDate.monthValue
        val startDateString = "${startDate.year}$startMonthString$startDayString"



    }

    private fun displayStoredDatesFromVM() {
        val startDate = datePickerViewModel.startDate.value
        val endDate = datePickerViewModel.endDate.value

        if (startDate == null) {
            binding.startDatePicker.updateDate(LocalDate.now().year,LocalDate.now().monthValue,LocalDate.now().dayOfMonth)
        }
        else {
            val startYear = datePickerViewModel.startDate.value!!.year
            val startMonth = datePickerViewModel.startDate.value!!.monthValue
            val startDay = datePickerViewModel.startDate.value!!.dayOfMonth
            binding.startDatePicker.updateDate(startYear,startMonth,startDay)
        }

        if (endDate == null) {
            binding.endDatePicker.updateDate(LocalDate.now().year,LocalDate.now().monthValue,LocalDate.now().dayOfMonth)
        }
        else {
            val endYear = datePickerViewModel.endDate.value!!.year
            val endMonth = datePickerViewModel.endDate.value!!.monthValue
            val endDay = datePickerViewModel.endDate.value!!.dayOfMonth
            binding.endDatePicker.updateDate(endYear,endMonth,endDay)
        }
    }
}

open class OnSwipeTouchListener(context: Context?) : OnTouchListener {

    companion object {
        private const val SWIPE_DISTANCE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }

    private val gestureDetector: GestureDetector
    fun onSwipeLeft() {}
    fun onSwipeRight() {}
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        v.performClick()
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val distanceX = e2.x - e1.x
            val distanceY = e2.y - e1.y
            if (abs(distanceX) > abs(distanceY) && abs(distanceX) > Companion.SWIPE_DISTANCE_THRESHOLD && abs(
                    velocityX
                ) > Companion.SWIPE_VELOCITY_THRESHOLD
            ) {
                if (distanceX > 0) onSwipeRight() else onSwipeLeft()
                return true
            }
            return false
        }
    }

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}