package com.example.cci.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cci.R;
import com.example.cci.model.Product;
import java.util.ArrayList;
import java.util.List;

public class AdapterProductList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> items = new ArrayList<>();
    private Context ctx;
    private Product header;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private OnItemClickListener mOnItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, Product obj, int actionId);
    }
    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position, Product product);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterProductList(Context context, Product header, List<Product> items) {
        this.items = items;
        this.header = header;
        ctx = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView code, name, price;
        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            code = (TextView) v.findViewById(R.id.code);
            name = (TextView) v.findViewById(R.id.name);
            price = (TextView) v.findViewById(R.id.price);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);

        }
    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {
        public TextView code, name, price;
        public LinearLayout lyt_parent;

        public ViewHolderHeader(View v) {
            super(v);
            code = (TextView) v.findViewById(R.id.code);
            name = (TextView) v.findViewById(R.id.name);
            price = (TextView) v.findViewById(R.id.price);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Product c = items.get(position);
        AdapterProductList.ViewHolder vItem = (AdapterProductList.ViewHolder) holder;
        vItem.code.setText(c.getCode());
        vItem.name.setText(c.getName());
        vItem.price.setText(c.getAdvicedPrice());
        vItem.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position, c);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public Product getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}