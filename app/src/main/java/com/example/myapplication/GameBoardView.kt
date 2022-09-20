package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Color.TRANSPARENT
import android.graphics.Color.rgb
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.os.Handler
import androidx.core.os.postDelayed
import kotlin.math.abs

class GameBoardView : View {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    lateinit var positionClickedListener : PositionClickedListener
    private var gameboardSize :Int ? = null
    lateinit var gameBoardArray : Array<IntArray>
    private var xPositions : Array<Float>? = null
    private var yPositions : Array<Float>? = null

    var drawFill = false


    private var touching: Boolean = false

    var gameFinished : Boolean = false
    lateinit var winingPoints : Array<Pair<Int,Int>>

    //Potrzebne do rysowania wygranej lini
    var startX : Float? = null
    var startY : Float? = null
    var drawingIteration : Int  = 1

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if(::gameBoardArray.isInitialized){
            checkGameBoardSize()
            calculateXYPositions()
            drawSquares(canvas)
            drawBoard(canvas)
            if(gameFinished){
                drawWiningLine(canvas)
            }
        }
    }
    fun copy(old: GameBoardView)
    {
        //this.positionClickedListener = old.positionClickedListener
        this.gameboardSize = old.gameboardSize
        this.gameBoardArray = old.gameBoardArray
        this.xPositions = old.xPositions
        this.yPositions = old.yPositions
        this.drawFill = old.drawFill
        this.touching = old.touching
        this.gameFinished = old.gameFinished
        //this.winingPoints = old.winingPoints
        this.startX = old.startX
        this.startY = old.startY
        this.drawingIteration = old.drawingIteration
    }

    fun drawWiningLine(canvas: Canvas?){
        var paint = Paint()
        var paint1 = Paint()
        var paint2 = Paint()
        paint.color = (Color.argb(255,140, 43, 140))
        paint.strokeWidth=16f
        paint1.color = (Color.argb(255,190, 100, 190))
        paint1.strokeWidth=10f
        paint2.color = (Color.argb(255,215, 145, 215))
        paint2.strokeWidth=4f
         startX = xPositions?.get(winingPoints[0].first)
         startY = yPositions?.get(winingPoints[0].second)
        val endX = xPositions?.get(winingPoints[1].first)
        val endY = yPositions?.get(winingPoints[1].second)
        canvas!!.drawCircle(startX!!, startY!!, 8f,paint)

        canvas!!.drawCircle(startX!!, startY!!, 5f,paint1)

        canvas!!.drawCircle(startX!!, startY!!, 2f,paint2)
        canvas!!.drawCircle(endX!!, endY!!,8f, paint)
        canvas!!.drawCircle(endX!!, endY!!,5f, paint1)
        canvas!!.drawCircle(endX!!, endY!!,2f, paint2)

        val xAdd = (xPositions?.get(winingPoints[1].first)?.minus(startX!!))!!/(7)
        val yAdd = (yPositions?.get(winingPoints[1].second)?.minus(startY!!)!!/(7))

        if(drawingIteration!!<8){
            canvas!!.drawLine(startX!!,startY!!,startX!!+(drawingIteration!!*xAdd!!),startY!!+(drawingIteration!!*yAdd!!),paint)
            canvas!!.drawLine(startX!!,startY!!,startX!!+(drawingIteration!!*xAdd!!),startY!!+(drawingIteration!!*yAdd!!),paint1)
            canvas!!.drawLine(startX!!,startY!!,startX!!+(drawingIteration!!*xAdd!!),startY!!+(drawingIteration!!*yAdd!!),paint2)
            drawingIteration = drawingIteration!!+1
            if(drawingIteration!!<8){
                Handler().postDelayed({invalidate()},0)
            }

        }

    }
    fun checkGameBoardSize(){
        if(gameBoardArray.size == gameBoardArray[0].size ){
            gameboardSize=gameBoardArray.size
        }
    }
    fun calculateXYPositions(){
        if(::gameBoardArray.isInitialized) {
            //Log.d("view","rozmiar gameBoardSize="+gameboardSize)
            var start = 1.2f*width!!/(gameboardSize!!*3).toFloat()
            val correctedWidth = width!!.toFloat() - 2*start
            val addValue = correctedWidth / (gameboardSize!! - 1).toFloat()
            //Log.d("size",(start.toString()+" "+correctedWidth.toString()+" "+addValue.toString()))
            xPositions=Array(gameboardSize!!){0f}
            yPositions=Array(gameboardSize!!){0f}
            xPositions!![0] = start
            yPositions!![0] = start
            for (x in 1 until gameboardSize!! ) {
                start += addValue
                yPositions!![x] = start
                xPositions!![x] = start
            }
        }

    }
    fun drawSquares(canvas: Canvas?){
        //Kolor wypelnienia planszy
        val paintFill = Paint()
        paintFill.setColor(Color.argb(0,43, 43, 51))
        paintFill.style= Paint.Style.FILL
        //kolor obramowek planszy
        val paintStroke = Paint()
        paintStroke.setColor(Color.rgb(64, 170, 165))
        paintStroke.style= Paint.Style.STROKE
        paintStroke.strokeWidth= 3F

        val squares = ArrayList<RectF>()
//        var xstart=30f
//        var ystart=30f
//        var widthCorrect = width.toFloat()-60f
//        val addwidth=widthCorrect/(gameboardSize-1).toFloat()
//        var xfinish=addwidth+30F
//        var yfinish= addwidth+30F
        val roundAngle = 10f
        for (x in 0 until  gameboardSize!!-1){
            for(y in 0 until  gameboardSize!!-1){
                val rect:RectF = RectF(xPositions?.get(x)!!,
                    yPositions?.get(y)!!,
                    xPositions!![x+1],
                    yPositions!![y+1])
                squares.add(rect)

            }
        }

//        for (x in 0 until gameboardSize-1){
//            xstart=20F
//            xfinish=widthCorrect/(gameboardSize-1).toFloat()+30f
//            for(y in 0 until gameboardSize-1){
//                val rect: RectF = RectF(xstart,ystart,xfinish,yfinish)
//                squares.add(rect)
//                xstart = xfinish
//                xfinish += addwidth
//            }
//            ystart = yfinish
//            yfinish += addwidth
//
//        }

        for(rect in squares){
            if(drawFill){
                canvas!!.drawRoundRect(rect,roundAngle,roundAngle,paintFill)
            }

            canvas!!.drawRoundRect(rect,roundAngle,roundAngle,paintStroke)
        }
    }
    fun drawBoard(canvas: Canvas?){
        val paint1 = Paint()
        paint1.setColor(Color.BLACK)
        val paint2 = Paint()
        paint2.setColor(Color.WHITE)
        var positionVal = 0
        for (x in 0 until gameboardSize!!){
            for(y in 0 until gameboardSize!!){
                positionVal=gameBoardArray[x][y]
                if(positionVal==1){
                    //canvas!!.drawCircle(xPositions?.get(x)!!, yPositions?.get(y)!!,circleRadius,paint1)
                    val pionek =BitmapFactory.decodeResource(resources,R.drawable.white_pin)
                    val radius = width/(gameboardSize!!*3)
                    val rect = RectF(xPositions?.get(x)!!-radius,
                        yPositions?.get(y)!!-radius,
                        xPositions!![x]+radius,
                        yPositions!![y]+radius)
                        canvas!!.drawBitmap(pionek,null,rect,null)

                }
                if(positionVal==2){
                    //canvas!!.drawCircle(xPositions?.get(x)!!, yPositions?.get(y)!!,circleRadius,paint2)
                    val pionek =BitmapFactory.decodeResource(resources,R.drawable.black_pin)
                    val radius = width/(gameboardSize!!*3)
                    val rect = RectF(xPositions?.get(x)!!-radius,
                        yPositions?.get(y)!!-radius,
                        xPositions!![x]+radius,
                        yPositions!![y]+radius)
                    canvas!!.drawBitmap(pionek,null,rect,null)
                }
            }
        }
//        for( array in gameBoardArray){
//            x=30F
//            for(position in array){
//                if(position == 1){
//
//                    canvas!!.drawCircle(x,y,circleRadius,paint1)
//                }
//                if(position==2){
//                    canvas!!.drawCircle(x,y,circleRadius,paint2)
//                }
//                x+=addwidth
//            }
//            y+=addwidth
//        }
    }
    fun resetGame(){
        gameFinished = false
        startX=null
        startY=null
        drawingIteration = 1
    }
    fun getPositionOF(x:Float,y:Float): Pair<Int,Int>{
        var xpos = -1
        var ypos = -1
        var absolut = Float.MAX_VALUE
        var value = Float.MIN_VALUE
        for(xi in 0 until xPositions!!.size){
            value = abs(this!!.xPositions?.get(xi)!!-x )
            if(value<absolut){
                xpos=xi
                absolut = value
            }
        }
        absolut = Float.MAX_VALUE
        value = Float.MIN_VALUE
        for(xi in 0 until yPositions!!.size){
            value = abs(this!!.yPositions!![xi]-y )
            if(value<absolut){
                ypos=xi
                absolut = value
            }
        }
        //Log.d("POSITION","Position work")
        return Pair(xpos,ypos)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isEnabled) {
            return false
        }
        val x = event!!.x
        val y = event!!.y
        var pos = Pair(0,0)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if(::gameBoardArray.isInitialized){
                pos = getPositionOF(x,y)}
                positionClickedListener?.onPositionClicked(pos.first,pos.second)
                touching = true
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
//                touching = false
//                if(::gameBoardArray.isInitialized) {
//                    val (finalX1, finalY1) = getPositionOF(x, y)
//                    if ((finalX1 == pos.first) && (finalY1 == pos.second)) {
//                        positionClickedListener?.onPositionClicked(pos.first, pos.second)
//                    }
//                }
            }
            MotionEvent.ACTION_CANCEL -> {
               // touching = false
            }

        }
        return true


    }

    interface PositionClickedListener{
        fun onPositionClicked( x: Int, y: Int)
    }


}