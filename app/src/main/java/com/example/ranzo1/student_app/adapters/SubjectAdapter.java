package com.example.ranzo1.student_app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ranzo1.student_app.objects.Subject;
import com.example.ranzo1.student_app.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;
import com.squareup.picasso.Picasso;


import java.util.List;



/**
 * Created by ranzo1 on 7.12.2016..
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {



    private Context context;
    private TextView subjectName;
    private TextView professorName;
    private VectorMasterView bookmarkVector;
    private CircularImageView circularImageView;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;




    //private DatabaseHendler db;
     //private LayoutInflater layoutInflater;
    private List<Subject> mSubjects;


    public SubjectAdapter(Context context, List<Subject> mSubjects ){

        this.context=context;
        this.mInflater=LayoutInflater.from(context);
        this.mSubjects=mSubjects;

    }

    // inflates the row layout from xml when needed
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_row, parent, false);
        SubjectAdapter.ViewHolder vHolder = new SubjectAdapter.ViewHolder(view);
        return vHolder;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.setIsRecyclable(false);

        subjectName.setText(String.valueOf(mSubjects.get(position).getName()));
        professorName.setText(String.valueOf(mSubjects.get(position).getProfessor()));

        PathModel outline = bookmarkVector.getPathModelByName("outline");
        outline.setFillColor(mSubjects.get(position).getColor());



        Picasso.with(context).load(mSubjects.get(position).getProfImage())
                .placeholder(R.drawable.man)
                .into(circularImageView);




    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mSubjects.size();



    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener{


        public ViewHolder(View itemView) {
            super(itemView);

            subjectName = (TextView) itemView.findViewById(R.id.nameLabel);
            professorName = (TextView) itemView.findViewById(R.id.profesorsss);
             bookmarkVector = (VectorMasterView) itemView.findViewById(R.id.bookmark);
             circularImageView = (CircularImageView) itemView.findViewById(R.id.circularImageView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }


        @Override
        public boolean onLongClick(View view) {

            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClicked(view,getAdapterPosition());

            }



            return true;
        }


    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }

    public interface OnItemLongClickListener {
         boolean onItemLongClicked(View view, int position);
    }


    public void setLongClickListener(OnItemLongClickListener mLongClickListener) {
        this.mLongClickListener = mLongClickListener;
    }





    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }
}
