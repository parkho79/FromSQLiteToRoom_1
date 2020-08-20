package com.parkho.sqlite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parkho.sqlite.PhRecyclerAdapter.itemViewHolder;

import java.util.List;

public class PhRecyclerAdapter extends RecyclerView.Adapter<itemViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    private List mItemList;

    private OnItemClickListener mListener;

    public class itemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGrade;
        private TextView tvNumber;
        private TextView tvName;

        public itemViewHolder(View a_view) {
            super(a_view);
            tvGrade = a_view.findViewById(R.id.tv_item_grade);
            tvNumber = a_view.findViewById(R.id.tv_item_number);
            tvName = a_view.findViewById(R.id.tv_item_name);

            // 아이템 클릭 이벤트 처리.
            a_view.setOnClickListener(a_view1 -> {
                final int pos = getAdapterPosition() ;
                if (pos != RecyclerView.NO_POSITION) {
                    // 리스너 객체의 메서드 호출.
                    if (mListener != null) {
                        mListener.onItemClick(a_view1, pos) ;
                    }
                }
            });
        }
    }

    public PhRecyclerAdapter(List a_itemList) {
        mItemList = a_itemList;
    }

    @Override
    public itemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new itemViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(itemViewHolder a_holder, int a_position) {
        PhStudentEntity studentEntity = (PhStudentEntity) mItemList.get(a_position);

        a_holder.tvGrade.setText(Integer.toString(studentEntity.getGrade()));
        a_holder.tvNumber.setText(Integer.toString(studentEntity.getNumber()));
        a_holder.tvName.setText(studentEntity.getName());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setOnItemClickListener(OnItemClickListener a_listener) {
        mListener = a_listener ;
    }
}