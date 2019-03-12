package com.example.mp2_holmes.graphexplorer.NodeGraphView;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;

import com.example.mp2_holmes.graphexplorer.nodeInfo;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class customView extends View {

    private  Context m_context;

    //gesture recognition
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    //panning functions
    private float mLastTouchX;
    private float mLastTouchY;
    private float mPosX;
    private float mPosY;

    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    //Background
    private  Rect m_rectGraphBackground;
    //Officers
    private JSONArray Officers;
    public class OfficerNode {
        public String name,address,DOB;
        public float posX,posY,radius;
        public Rect HitBox;

        public OfficerNode(float pX,float pY,float r)
        {
            posX = pX;
            posY = pY;
            radius = r;
            createHitBox();
        }
        public void createHitBox()
        {
            HitBox = new Rect();
            HitBox.left = (int) (posX -radius);
            HitBox.top =(int) (posY -radius);
            HitBox.right = (int) (posX +radius);
            HitBox.bottom = (int) (posY +radius);
        }

        public boolean Hit(int x,int y) {
            return HitBox.contains(x,y);
        }
    }
    private List<OfficerNode> OfficerNodes;

    //Legend
    private final static int LegendWidth = 300;
    private final static int LegendHeight = 400;
    private final static int nodeRadius = 50;

    private int minX = 0;
    private int maxX = 0;
    private int minY = 0;
    private int maxY = 0;

    private  Rect m_rectLegened;

    private  Paint m_paint;


    public customView(Context context)
    {
        super(context);
        init(context,null);
    }

    public customView(Context context,AttributeSet attributeSet)
    {
        super(context,attributeSet);
        init(context,attributeSet);
    }

    public customView(Context context,AttributeSet attributeSet,int defStyleAttr)
    {
        super(context,attributeSet,defStyleAttr);
        init(context,attributeSet);
    }

    public customView(Context context,AttributeSet attributeSet,int defStyleAttr,int defStyleRes)
    {
        super(context,attributeSet,defStyleAttr,defStyleRes);
        init(context,attributeSet);
    }

    private void init(Context context,@Nullable AttributeSet attributeSet)
    {
        m_context = context;
        m_rectLegened = new Rect();
        m_rectGraphBackground = new Rect();
        m_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Officers = new JSONArray();
        OfficerNodes = new ArrayList<OfficerNode>();
        mScaleDetector = new ScaleGestureDetector(m_context, new ScaleListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor);

        if((mPosX*-1 )<0)
        {
            mPosX =0;
        }else if ((mPosX *-1) >(getMeasuredWidth()*mScaleFactor)-getMeasuredHeight())
        {
            mPosX = (getMeasuredWidth()*mScaleFactor -getMeasuredWidth())*-1;
        }
        if((mPosY*-1 )<0)
        {
            mPosY =0;
        }else if ((mPosY *-1) >(getMeasuredHeight()*mScaleFactor)-getMeasuredHeight())
        {
            mPosY = (getMeasuredHeight()*mScaleFactor -getMeasuredHeight() )*-1;
        }
        if((getMeasuredHeight()*mScaleFactor)<getMeasuredHeight())
        {
            mPosY =0;
        }
        canvas.translate(mPosX, mPosY);


        canvas.drawColor(Color.WHITE);
        DrawGraphBackGround(canvas);
        DrawLegend(canvas);
        DrawCompanyNodes(canvas);
        canvas.restore();
   }

    private void DrawGraphBackGround(Canvas m_canvas)
    {
        maxY = (getMeasuredHeight() - 100);
        maxX = (int) (getMeasuredWidth() /1.7);
        minY = 10;
        minX = LegendWidth +50;

        //draw Backgorund box
        m_paint.setColor(Color.WHITE);
        m_rectGraphBackground.left = minX;
        m_rectGraphBackground.top = minY;
        m_rectGraphBackground.right = m_rectGraphBackground.left+maxX;
        m_rectGraphBackground.bottom = m_rectGraphBackground.top+maxY;
        m_canvas.drawRect(m_rectGraphBackground,m_paint);
    }

    public void DrawLegend(Canvas m_canvas)
    {
        //draw legend box
        m_paint.setColor(Color.WHITE);
        m_paint.setShadowLayer(12, 0, 0, Color.BLACK);
        setLayerType(LAYER_TYPE_SOFTWARE, m_paint);
        m_rectLegened.left = 10;
        m_rectLegened.top = 10;
        m_rectLegened.right = m_rectLegened.left+LegendWidth;
        m_rectLegened.bottom = m_rectLegened.top+LegendHeight;
        m_canvas.drawRect(m_rectLegened,m_paint);
        //Title
        m_paint.setColor(Color.BLACK);
        String KeyText = "Key";
        int KeytxX = 15,KeytxY = 80;
        m_paint.setTextSize(FindTextSize((float) LegendWidth/2,KeyText, 48f));
        m_canvas.drawText(KeyText,KeytxX,KeytxY,m_paint);

        //draw Legend items
        //should loop through an array of objects
        m_paint.setColor(Color.GREEN);
        int FirstItemX=KeytxX+(nodeRadius*2),FirstItemY= KeytxY+(nodeRadius*2);
        m_canvas.drawCircle(FirstItemX,FirstItemY,nodeRadius,m_paint);
        m_paint.setColor(Color.RED);
        m_canvas.drawCircle(FirstItemX,(FirstItemY*2),nodeRadius,m_paint);
    }

    private void DrawCompanyNodes(Canvas m_canvas) {
        m_paint.setColor(Color.RED);

        for (int i = 0; i < Officers.length(); i++) {
            try {
                JSONObject Officer = Officers.getJSONObject(i);
                OfficerNodes.get(i).name = Officer.getString("name");
                try {
                    JSONObject address = Officer.getJSONObject("address");
                    OfficerNodes.get(i).address = address.getString("address_line_1")+","+address.getString("postal_code");
                }catch (JSONException e) {
                    OfficerNodes.get(i).DOB ="N/A";
                }
                try {
                    JSONObject dob = Officer.getJSONObject("date_of_birth");
                    OfficerNodes.get(i).DOB = dob.getString("day") + "/" + dob.getString("month") + "/" + dob.getString("year");
                }catch (JSONException e) {
                    OfficerNodes.get(i).DOB ="N/A";
                }
                //node and connection
                m_paint.setColor(Color.RED);
                m_canvas.drawLine(OfficerNodes.get(i).posX, OfficerNodes.get(i).posY, m_rectGraphBackground.centerX(), m_rectGraphBackground.centerY(), m_paint);
                m_canvas.drawCircle(OfficerNodes.get(i).posX, OfficerNodes.get(i).posY, OfficerNodes.get(i).radius, m_paint);

                //officer name
                m_paint.setColor(Color.BLACK);
                m_paint.setTextSize(FindTextSize(((float) OfficerNodes.get(i).radius*4), OfficerNodes.get(i).name,100.0f));
                m_canvas.drawText(OfficerNodes.get(i).name, OfficerNodes.get(i).posX - OfficerNodes.get(i).radius, OfficerNodes.get(i).posY, m_paint);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //draw first circle
        m_paint.setColor(Color.GREEN);
        m_canvas.drawCircle(m_rectGraphBackground.centerX(), m_rectGraphBackground.centerY(), nodeRadius, m_paint);
    }

    private float FindTextSize(float desiredWidth,String text,float testTextSize)
    {
        m_paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        m_paint.getTextBounds(text, 0, text.length(), bounds);

        return testTextSize * desiredWidth / bounds.width();
    }

    public void setOfficers(JSONArray nwOfficers) {
        float x;
        float y;
        Officers = nwOfficers;
        for (int i = 0; i < Officers.length();) {
            x = new Random().nextInt((m_rectGraphBackground.right - m_rectGraphBackground.left) + 1) + m_rectGraphBackground.left;
            y = new Random().nextInt((m_rectGraphBackground.bottom - m_rectGraphBackground.top) + 1) + m_rectGraphBackground.top;
            if(!checkPostionIsFree(x,y)) {
            OfficerNodes.add(new OfficerNode(x,y,nodeRadius));
                i++;
            }
        }
        postInvalidate();
    }

    private boolean checkPostionIsFree(float x , float y)
    {
        for (OfficerNode Officer: OfficerNodes) {
            if(Officer.Hit((int)x,(int) y))
            {
                return  true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = event.getX();
                final float y = event.getY();

                mLastTouchX = x;
                mLastTouchY = y;

                for(OfficerNode officer : OfficerNodes){
                    if(officer.Hit((int) x,(int) y)){
                        Intent intent = new Intent(m_context, moreEmployeeDetails.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("name",officer.name);
                        intent.putExtra("address",officer.address);
                        intent.putExtra("DOB",officer.DOB);
                        m_context.startActivity(intent);
                    }
                }

                // Save the ID of this pointer
                mActivePointerId = event.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex = event.findPointerIndex(mActivePointerId);
                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                mLastTouchX = x;
                mLastTouchY = y;

                invalidate();

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                // Extract the index of the pointer that left the touch sensor
                final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = event.getX(newPointerIndex);
                    mLastTouchY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float minZoom= 0.8f;
            float maxZoom = 5.0f;
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(minZoom, Math.min(mScaleFactor, maxZoom));

            invalidate();
            return true;
        }
    }

}
