package com.example.corona.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corona.R;
import com.example.corona.interfaces.InterfaceBetweenListAndAdapter;
import com.example.corona.model.ModelRoom;

import java.util.ArrayList;
import java.util.List;

public class AdapterArticle extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    List<ModelRoom> listArticles=new ArrayList<>();
    private boolean isLoading=false;
    private OnLoadMoreListener onLoadMoreListener;
    private InterfaceBetweenListAndAdapter interfacee;

    public AdapterArticle(List<ModelRoom> listArticles, RecyclerView recyclerView, Context context) {
        interfacee = (InterfaceBetweenListAndAdapter) context;
        this.listArticles = listArticles;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int sizeAll = linearLayoutManager.getItemCount();
                int lastIndex= linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if( !isLoading && lastIndex == sizeAll -1)
                {
                    if(onLoadMoreListener != null)
                        onLoadMoreListener.loadMore();
                    isLoading = true;
                }

            }
        });



    }

    @Override
    public int getItemViewType(int position)
    {
        if(listArticles.get(position) != null)
            return 1;
        else
            return 2;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_response, parent, false);
            return new MainViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MainViewHolder)
        {
            ((MainViewHolder) holder).bindMainHolder(listArticles.get(position));
            ((MainViewHolder) holder).txtMore.setOnClickListener(view ->
            {
                interfacee.getOneArticleProperties(listArticles.get(position).getTitle(),
                        listArticles.get(position).getYear(),
                        listArticles.get(position).getAbstractArticle(),
                        listArticles.get(position).getLink()
                        );
                //Log.d("aaa", "MainViewHolder: ");
            });
        }

        else if(holder instanceof LoadViewHolder)
            ((LoadViewHolder) holder).bindLoadHolder();

    }

    @Override
    public int getItemCount() {
        return listArticles.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txtTitleResponse;
        private TextView txtDateResponse;
        private TextView txtAbstractResponse;
        private TextView txtMore;
        CardView cardViewItemResponse;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewItemResponse = itemView.findViewById(R.id.cardviewItemResponse);
            txtTitleResponse = itemView.findViewById(R.id.txtTitleResponse);
            txtDateResponse = itemView.findViewById(R.id.txtDateResponse);
            txtAbstractResponse = itemView.findViewById(R.id.txtAbstract);
            txtMore = itemView.findViewById(R.id.txtMore);

        }

        public void bindMainHolder(ModelRoom response)
        {
            txtDateResponse.setText(response.getYear());
            txtTitleResponse.setText(response.getTitle());
            txtAbstractResponse.setText(response.getAbstractArticle());
        }
    }
    public class LoadViewHolder extends RecyclerView.ViewHolder
    {
        ProgressBar progressBar;
        public LoadViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
        public void bindLoadHolder()
        {
            progressBar.setIndeterminate(true);
        }
    }

    public interface OnLoadMoreListener
    {
        public void loadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
    {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoading(boolean isLoading)
    {
        this.isLoading = isLoading;
    }

}
