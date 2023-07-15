package com.example.busui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.busbookingui.model.Seat
import com.example.busui.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val selectedList=binding.bus.getSelectedList()
        val seat1= Seat("A11")
        val seat2= Seat("A12")
        val seat3= Seat("A1")
        val bookedList= mutableListOf<Seat>()
        bookedList.add(seat1)
        bookedList.add(seat2)
        bookedList.add(seat3)
        binding.add.setOnClickListener {
            bookedList.addAll(selectedList)
            binding.bus.setBookedSeats(bookedList)
        }
    }
}