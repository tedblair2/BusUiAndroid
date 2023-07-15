package com.example.busui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.example.busbookingui.model.Seat

class BusUi(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint= Paint()
    private val textPaint= Paint()
    private var left=-1F
    private var top=getDensityPixels(150F)
    private var right=-1F
    private var bottom= -1F
    private var seatWidth=-1F
    private var seatHeight=-1F
    private var seatPadding=getDensityPixels(6F)
    private var columns=6
    private val steerBitmap= BitmapFactory.decodeResource(resources,R.drawable.steer)
    private val selectedSeats= mutableListOf<Seat>()
    private val bookedSeats= mutableListOf<Seat>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val padding=getDensityPixels(20F)
        seatWidth= (width-(padding*2))/columns
        left=padding
        right=left + seatWidth
        seatHeight=seatWidth-seatPadding
        bottom=top+seatHeight
        val desiredHeight = calculateHeight()
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                val column=((event.x-left)/seatWidth).toInt()
                val middleColumn=((event.x - left - (seatWidth / 2)) / seatWidth).toInt()
                val row=((event.y-top)/seatHeight).toInt()
                if (middleColumn==2 && row==11){
                    val seat=Seat("25")
                    if (!bookedSeats.contains(seat)){
                        if (selectedSeats.contains(seat)){
                            selectedSeats.remove(seat)
                        }else{
                            selectedSeats.add(seat)
                        }
                    }
                    invalidate()
                }
                if ((column in 0..1 || column in 4..5) && row in 0..11){
                    val seat=Seat(getSeatNo(row,column))
                    if (!bookedSeats.contains(seat)){
                        if (selectedSeats.contains(seat)){
                            selectedSeats.remove(seat)
                        }else{
                            selectedSeats.add(seat)
                        }
                    }
                    invalidate()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val busSeat=getBitmapFromVector(context,R.drawable.baseline_event_seat_24)
        val selectedDrawable=getBitmapFromVector(context,R.drawable.ic_selected_seat)
        val bookedDrawable=getBitmapFromVector(context,R.drawable.ic_booked)
        val textLeft=getScaledDensityPixels(15F)
        val textTop=getDensityPixels(23F)
        paint.color= Color.DKGRAY
        paint.strokeWidth=getDensityPixels(2F)
        paint.style=Paint.Style.STROKE
        textPaint.color= Color.BLACK
        textPaint.textSize=getScaledDensityPixels(10F)
        textPaint.isFakeBoldText=true
        //canvas?.drawRect(RectF(left+(5*seatWidth)+(seatWidth/5),top-seatWidth+(seatWidth/5),right+(5*seatWidth)-(seatWidth/5),top-(seatWidth/5)),paint)
        canvas?.drawBitmap(steerBitmap,null,RectF(left+(5*seatWidth)+(seatWidth/4),top-seatWidth+(seatWidth/4),right+(5*seatWidth)-(seatWidth/4),top-(seatWidth/4)),null)
        for (row in 0..11){
            colums@for (column in 0..5){
                if (column in 2..3){
                    if (row==11 && column==2){
                        canvas?.drawBitmap(busSeat!!,null,
                            RectF(left+(column*seatWidth)+(seatWidth/2),top+(row*seatHeight),right+(column*seatWidth)-seatPadding+(seatWidth/2),bottom+(row*seatHeight)),null
                        )
                        val seat=Seat("25")
                        if (selectedSeats.contains(seat)){
                            canvas?.drawBitmap(selectedDrawable!!,null,
                                RectF(left+(column*seatWidth)+(seatWidth/2),top+(row*seatHeight),right+(column*seatWidth)-seatPadding+(seatWidth/2),bottom+(row*seatHeight)),null
                            )
                        }
                        isClickable = if (bookedSeats.contains(seat)){
                            canvas?.drawBitmap(bookedDrawable!!,null,
                                RectF(left+(column*seatWidth)+(seatWidth/2),top+(row*seatHeight),right+(column*seatWidth)-seatPadding+(seatWidth/2),bottom+(row*seatHeight)),null
                            )
                            false
                        }else{
                            true
                        }
                        canvas?.drawText("25",left+(column*seatWidth)+(seatWidth/2)+textLeft+getDensityPixels(3F),top+(row*seatHeight)+textTop,textPaint)
                    }
                    continue@colums
                }
                canvas?.drawBitmap(busSeat!!,null,
                    RectF(left+(column*seatWidth),top+(row*seatHeight),right+(column*seatWidth)-seatPadding,bottom+(row*seatHeight)),null
                )
                val seat=Seat(getSeatNo(row,column))
                if (selectedSeats.contains(seat)){
                    canvas?.drawBitmap(selectedDrawable!!,null,
                        RectF(left+(column*seatWidth),top+(row*seatHeight),right+(column*seatWidth)-seatPadding,bottom+(row*seatHeight)),null
                    )
                }
                isClickable = if (bookedSeats.contains(seat)){
                    canvas?.drawBitmap(bookedDrawable!!,null,
                        RectF(left+(column*seatWidth),top+(row*seatHeight),right+(column*seatWidth)-seatPadding,bottom+(row*seatHeight)),null
                    )
                    false
                }else{
                    false
                }
                val seatNo=getSeatNo(row,column)
                canvas?.drawText(seatNo,left+(column*seatWidth)+textLeft,top+(row*seatHeight)+textTop,textPaint)
            }
        }
    }
    fun getSelectedList():MutableList<Seat>{
        return selectedSeats
    }
    fun setBookedSeats(list:MutableList<Seat>){
        this.bookedSeats.clear()
        this.bookedSeats.addAll(list)
        invalidate()
    }
    private fun getScaledDensityPixels(dps: Float):Float{
        return dps * resources.displayMetrics.scaledDensity
    }
    private fun getDensityPixels(dps:Float):Float{
        return dps * resources.displayMetrics.density
    }
    private fun getSeatNo(row:Int,column:Int):String{
        var seatNo=-1
        when(column){
            0-> seatNo = (row*2)+1
            1-> seatNo = (row+1)*2
            2-> seatNo = 25
            4-> seatNo = (row*2)+1
            5-> seatNo = (row+1)*2
        }
        return if (column<=1){
            "A$seatNo"
        }else if (column>=4){
            "B$seatNo"
        }else{
            "25"
        }
    }
    private fun calculateHeight(): Int {
        val numRows = 12
        val padding=getDensityPixels(15F)
        return (numRows * seatHeight + top + padding).toInt()
    }
    private fun getBitmapFromVector(context: Context, vectorResourceId: Int): Bitmap? {
        val drawable = VectorDrawableCompat.create(context.resources, vectorResourceId, null)
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

        val bitmap = Bitmap.createBitmap(
            drawable?.intrinsicWidth ?: 0,
            drawable?.intrinsicHeight ?: 0,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable?.draw(canvas)

        return bitmap
    }
}