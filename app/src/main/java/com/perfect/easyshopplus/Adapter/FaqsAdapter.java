package com.perfect.easyshopplus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.perfect.easyshopplus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FaqsAdapter extends RecyclerView.Adapter{

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;

    public FaqsAdapter(Context context, JSONArray jsonArray) {
        this.context=context;
        this.jsonArray=jsonArray;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.faqitem, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {

            jsonObject=jsonArray.getJSONObject(position);
            if (holder instanceof MainViewHolder) {
                String ID_FAQ=jsonObject.getString("ID_FAQ");
                String Question=jsonObject.getString("Question");
                String Answer=jsonObject.getString("Answer");

                ((MainViewHolder)holder).quistain.setText("Q)  "+Question);
                ((MainViewHolder)holder).answer.setText("A)  "+Answer);

                ((MainViewHolder)holder).main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    private class MainViewHolder extends RecyclerView.ViewHolder {

        TextView quistain, answer;
        LinearLayout main_layout;

        public MainViewHolder(View v) {
            super(v);

            quistain=(TextView)v.findViewById(R.id.quistain);
            answer=(TextView)v.findViewById(R.id.answer);
            main_layout  = v.findViewById(R.id.main_layout);

        }
    }
}
