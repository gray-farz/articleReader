package com.example.corona.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.corona.R;
import com.example.corona.interfaces.InterfaceBetweenListAndAdapter;
import com.example.corona.model.ModelRoom;
import java.util.ArrayList;
import java.util.List;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ResponseViewHolder> {

    private List<ModelRoom> responseList = new ArrayList<>();
    private InterfaceBetweenListAndAdapter interfacee;
    public AdapterSearch(List<ModelRoom> responseList, Context context) {
        this.responseList = responseList;
        interfacee = (InterfaceBetweenListAndAdapter) context;
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_response,parent,false);
        return new ResponseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseViewHolder holder, int position) {
        holder.bindResponse(responseList.get(position));
        holder.txtMore.setOnClickListener(view ->
        {
            interfacee.getOneArticleProperties(responseList.get(position).getTitle(),
                    responseList.get(position).getYear(),
                    responseList.get(position).getAbstractArticle(),
                    responseList.get(position).getLink());
        });
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public void setNewsList(List<ModelRoom> newsList) {
        this.responseList = newsList;
        notifyDataSetChanged();
    }

    public class ResponseViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitleResponse;
        private TextView txtDateResponse;
        private TextView txtAbstractResponse;
        private TextView txtMore;
        CardView cardViewItemResponse;

        public ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewItemResponse = itemView.findViewById(R.id.cardviewItemResponse);
            txtTitleResponse = itemView.findViewById(R.id.txtTitleResponse);
            txtDateResponse = itemView.findViewById(R.id.txtDateResponse);
            txtAbstractResponse = itemView.findViewById(R.id.txtAbstract);
            txtMore = itemView.findViewById(R.id.txtMore);
        }

        public void bindResponse(ModelRoom response)
        {
            txtDateResponse.setText(response.getYear());
            txtTitleResponse.setText(response.getTitle());
            txtAbstractResponse.setText(response.getAbstractArticle());
        }

    }

}
