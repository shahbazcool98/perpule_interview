package perpule.shahbaz.interview.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import perpule.shahbaz.interview.R;
import perpule.shahbaz.interview.models.Mp3;
import perpule.shahbaz.interview.ui.MainPresenter;
import perpule.shahbaz.interview.ui.Mp3Activity;

public class Mp3sAdapter extends RecyclerView.Adapter<Mp3sAdapter.Mp3Holder> {

    List<Mp3> mp3List;
    Context context;

    public Mp3sAdapter(List<Mp3> mp3List, Context context) {
        this.mp3List = mp3List;
        this.context = context;
    }

    @Override
    public Mp3Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_mp3s,parent,false);
        Mp3Holder mh = new Mp3Holder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(Mp3Holder holder, final int position) {

        holder.tvTitle.setText(mp3List.get(position).getDesc());

        holder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainPresenter.selectedPos = position;
                context.startActivity(new Intent(context, Mp3Activity.class));
            }
        });


        }

    @Override
    public int getItemCount() {
        return mp3List.size();
    }

    public class Mp3Holder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        CardView cvParent;

        public Mp3Holder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            cvParent = (CardView) v.findViewById(R.id.cvParent);
        }
    }
}
