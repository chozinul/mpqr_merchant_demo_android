package com.mastercard.labs.mpqrmerchant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.data.model.Settings;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/16/17
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {
    private SettingsItemListener mListener;

    List<Settings> settings;

    public SettingsAdapter(List<Settings> settings, SettingsItemListener listener) {
        this.settings = settings;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new ViewHolder(view, new ViewHolder.ViewHolderClickListener() {
            @Override
            public void onClick(int position) {
                if (mListener != null) {
                    mListener.onSettingsItemClicked(settings.get(position));
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Settings settings = this.settings.get(position);

        if (settings.isEditable()) {
            holder.mEditableImageView.setVisibility(View.VISIBLE);
        } else {
            holder.mEditableImageView.setVisibility(View.GONE);
        }

        holder.mTitleTextView.setText(settings.getTitle());
        holder.mValueTextView.setText(settings.getValue());
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txt_title)
        TextView mTitleTextView;

        @BindView(R.id.txt_value)
        TextView mValueTextView;

        @BindView(R.id.img_editable)
        ImageView mEditableImageView;

        private ViewHolderClickListener mListener;

        ViewHolder(View itemView, ViewHolderClickListener listener) {
            super(itemView);

            mListener = listener;

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(getAdapterPosition());
            }
        }

        interface ViewHolderClickListener {
            void onClick(int position);
        }
    }

    public interface SettingsItemListener {
        void onSettingsItemClicked(Settings settings);
    }
}
