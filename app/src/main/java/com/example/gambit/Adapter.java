package com.example.gambit;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;

public class Adapter extends RecyclerSwipeAdapter<Adapter.DishViewHolder>{
    ArrayList<Dishes> dishes;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences preferenceFav;
    int number=1;

    public Adapter (Context context,ArrayList<Dishes> dishes){
        this.dishes = dishes;
        this.context=context;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view,
                parent, false);
        DishViewHolder vh=new DishViewHolder ( view );
        if (sharedPreferences==null) {
            sharedPreferences=parent.getContext ().getSharedPreferences ( "Count", Context.MODE_PRIVATE );
        }
        if (preferenceFav == null) {
            preferenceFav=parent.getContext ().getSharedPreferences ( "FAVORITE", Context.MODE_PRIVATE );
        }
        return  vh;
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        holder.bind(dishes.get(position));

    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    public class DishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, price, count;
        Button basket, minus, plus;
        ImageView image;
        Flow flow;
        SwipeLayout swipe;
        RelativeLayout rel_layout;
        boolean favor;
        Dishes mDishes;


        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            count = itemView.findViewById(R.id.count);
            basket = itemView.findViewById(R.id.basket_button);
            minus = itemView.findViewById(R.id.minus);
            plus = itemView.findViewById(R.id.plus);
            image = itemView.findViewById(R.id.image);
            flow = itemView.findViewById(R.id.flow);
            swipe = itemView.findViewById(R.id.swipe);
            rel_layout = itemView.findViewById(R.id.conteiner_fav);

            swipeLayout();
            basket.setOnClickListener(this);
            plus.setOnClickListener(this);
            minus.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.plus:
                case R.id.basket_button:
                    addCounter();
                    break;
                case R.id.minus:
                    deleteCounter();
                    break;
            }
        }
        public void addCounter() {

            number=Integer.parseInt ( count.getText ().toString () ) + 1;
            count.setText(String.valueOf(number));
            saveCount(mDishes.getId(), number);

            if (number<=0){
                flow.setVisibility(View.GONE);
                basket.setVisibility(View.VISIBLE);
            } else {
                flow.setVisibility(View.VISIBLE);
                basket.setVisibility(View.GONE);
            }

            }

        public void deleteCounter(){
            number=Integer.parseInt ( count.getText ().toString () ) - 1;
            count.setText(String.valueOf(number));
            deleteCount(mDishes.getId());
            if (number<=0){
                flow.setVisibility(View.GONE);
                basket.setVisibility(View.VISIBLE);
            } else {
                flow.setVisibility(View.VISIBLE);
                basket.setVisibility(View.GONE);
            }
        }


        public void bind(Dishes dishes){
            mDishes=dishes;
            favor = preferenceFav.getBoolean ( String.valueOf ( dishes.getId () ), true );
            name.setText(dishes.getName());
            price.setText(dishes.getPrice()+ " P".toString());

            number= loadCount(dishes.getId());
            count.setText(String.valueOf(number));

            Glide.with(context).load(dishes.getImage()).apply(RequestOptions.centerCropTransform())
                    .into(image);

            if (number<=0){
                flow.setVisibility(View.GONE);
                basket.setVisibility(View.VISIBLE);
            } else {
                flow.setVisibility(View.VISIBLE);
                basket.setVisibility(View.GONE);
            }

            if (preferenceFav.getBoolean ( String.valueOf ( dishes.getId () ), true )) {
                rel_layout.setBackgroundResource(R.color.gray);
            } else {
                rel_layout.setBackgroundResource(R.color.red);
            }
        }

        private void saveCount(int id, int number) {
            sharedPreferences = context.getSharedPreferences("Count", Context.MODE_PRIVATE);
            sharedPreferences.edit().putInt(String.valueOf(id), number).apply();
        }

        private int loadCount(int id) {
            sharedPreferences = context.getSharedPreferences("Count", Context.MODE_PRIVATE);
            return sharedPreferences.getInt(String.valueOf(id), 0);
        }

        private void deleteCount(int id) {
            sharedPreferences = context.getSharedPreferences("Count", Context.MODE_PRIVATE);
            sharedPreferences.edit().remove(String.valueOf(id)).apply();
        }


        private void saveFavor(String id, boolean favorite) {
            sharedPreferences = context.getSharedPreferences("FAVORITE", Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(id, favorite).apply();
        }

        public void swipeLayout(){
            swipe.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipe.addDrag(SwipeLayout.DragEdge.Right, swipe.findViewById(R.id.conteiner_fav));
            swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {
                    if(favor){
                        rel_layout.setBackgroundResource(R.color.red);
                        favor=false;} else {
                        rel_layout.setBackgroundResource(R.color.gray);
                        favor=true;
                    }
                    saveFavor(String.valueOf(mDishes.getId()), favor);

                }

                @Override
                public void onOpen(SwipeLayout layout) {


                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onClose(SwipeLayout layout) {

                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    layout.postDelayed (new Runnable () {
                        @Override
                        public void run() {
                            layout.close ();
                        }
                    }, 200 );


                }
            });
        }



    }
}
