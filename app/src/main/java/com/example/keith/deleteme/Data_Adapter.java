package com.example.keith.deleteme;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by keith on 3/20/18.
 */

public class Data_Adapter extends BaseAdapter {
    private static final int MAX_ROWS = 1000;
    private final Context ctx;
    private LayoutInflater inflater;

    private class CalculateValueTask extends AsyncTask<Integer, Void, Integer> {

        private Viewholder myVh;    //ref to a viewholder
        private int origNumbToDouble; //since myVH may be recycled and reused
                                    //we have to verify that the result we are returning
                                    //is still what the viewholder wants

        public CalculateValueTask(Viewholder myVh) {
            this.myVh = myVh;
            this.origNumbToDouble = myVh.myNumberToDouble;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return integers[0] + integers[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            //got a result, if the following are NOT equal
            // then the view has been recycled and is being used by another
            // number DO NOT MODIFY
            if (this.myVh.myNumberToDouble == this.origNumbToDouble){
                //still valid
                //set the result (non threaded)
                String s1 = Integer.toString(2*myVh.myNumberToDouble);
                myVh.tv_result.setText(s1);
            }
        }
    }

    private static class Viewholder {
        TextView tv_equation;
        TextView tv_result;
        int myNumberToDouble;
    }
    public Data_Adapter(Context ctx) {
        this.ctx = ctx;
    }
    
    @Override
    public int getCount() {
        return MAX_ROWS;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder myVH;

        //if cannot recycle, then create a new one, this should only happen
        //with first screen of data (or rows)
        if (convertView == null){
            if (inflater == null)
                inflater =(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //get a row
            convertView = inflater.inflate(R.layout.row_layout,null);

            //create a viewholder for effeciency (and for thread usage)
            myVH = new Viewholder();

            //get refs to widgets
            myVH.tv_equation = (TextView)convertView.findViewById(R.id.tv_equation);
            myVH.tv_result = (TextView)convertView.findViewById(R.id.tv_result);

            //marry the viewholder to the convertview row
            convertView.setTag(myVH);
        }

        myVH = (Viewholder)convertView.getTag();

        //this viewholder is going to double the number given in position
        myVH.myNumberToDouble = position;
        String s1 = Integer.toString(myVH.myNumberToDouble);

        //set the first field
        myVH.tv_equation.setText(s1 + " + " + s1 + " = " );

        //set the result (non threaded)
//        s1 = Integer.toString(2*myVH.myNumberToDouble);
//        myVH.tv_result.setText(s1);

        //set the result (threaded)

        //and the second if doing threads field and you dont want the
        //old rubbish to appear
        myVH.tv_result.setText("?");

        CalculateValueTask myTask = new CalculateValueTask(myVH);
        myTask.execute(myVH.myNumberToDouble);

        return convertView;
    }
}
