package com.inhatc.primrose;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    interface Listener {
        void onItemClickedAt(Integer position);
    }

    private Listener listener;
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void submitList(ArrayList<Flower> items) {
        this.arrayList = items;
        notifyDataSetChanged();
    }

    // adapter에 들어갈 list 입니다.
    private ArrayList<Flower> arrayList;
    private Context context;

    public RecyclerAdapter (ArrayList<Flower> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view, this.listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.tvName.setText(arrayList.get(position).getFname());
        holder.tvMean.setText(arrayList.get(position).getFloriography());

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://primrose-2df68.appspot.com/");
        StorageReference storageRef = storage.getReference();
        storageRef.child(arrayList.get(position).getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시
                Glide.with(context.getApplicationContext())
                        .load(uri)
                        .into(holder.imageView);
            }
        });
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return arrayList.size();
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvMean;
        private ImageView imageView;


        public ItemViewHolder(@NonNull View itemView, RecyclerAdapter.Listener itemListListener) {
            super(itemView);
            //MainActivity의 컨텍스트들을 지정해줌
            tvName = itemView.findViewById(R.id.tvName);
            tvMean = itemView.findViewById(R.id.tvMeaning);
            imageView = itemView.findViewById(R.id.imageView);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Integer currentPos = getAbsoluteAdapterPosition();//각 어댑터의 위치 클릭된
//                    itemListListener.onItemClickedAt(currentPos);
//                }
            //           });

        }
    }
}

